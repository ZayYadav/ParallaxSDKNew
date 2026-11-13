<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class License {
    public static function list(): array {
        return Database::all('SELECT l.*,p.package_rule,p.signer_rule,p.device_rule,p.requests_per_minute,
          (SELECT COUNT(*) FROM td_devices d WHERE d.license_id=l.id) AS device_count
          FROM td_licenses l LEFT JOIN td_license_rules p ON p.license_id=l.id ORDER BY l.id DESC LIMIT 150');
    }
    public static function issue(array $v): string {
        $rules=Validation::choices($v);
        $days=Validation::integer($v['days']??30,1,3650);
        $label=mb_substr(trim((string)($v['label']??'')),0,120);
        $key=Crypto::issueKey();
        $id=Database::transaction(static function () use ($rules,$days,$label,$key): int {
            Database::run('INSERT INTO td_licenses
             (key_digest,label,expires_at,max_devices,package_name,signing_sha256,feature_mask)
             VALUES(?,?,DATE_ADD(UTC_TIMESTAMP(), INTERVAL '.$days.' DAY),?,?,?,?)',[
                Crypto::digest($key),$label,$rules['max'],$rules['pkg'],$rules['cert'],$rules['feature']
             ]);
            $id=(int)Database::db()->lastInsertId();
            Database::run('INSERT INTO td_license_rules(license_id,package_rule,signer_rule,device_rule,requests_per_minute)
              VALUES (?,?,?,?,?)',[$id,$rules['package'],$rules['signer'],$rules['device'],$rules['perMin']]);
            return $id;
        });
        Audit::write('license_created','admin',$id,$label);
        return $key;
    }
    public static function status(int $id, string $status): void {
        if (!in_array($status,['active','disabled','revoked'],true))
            throw new Deny('INVALID_STATUS',422);
        if (!Database::run('UPDATE td_licenses SET status=? WHERE id=?',[$status,$id]))
            throw new Deny('UNKNOWN_LICENSE',404);
        Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        Audit::write('license_status','admin',$id,$status);
    }
    public static function rules(int $id, array $v): void {
        $r=Validation::choices($v);
        $expiry=trim((string)($v['expires_at']??''));
        if (!preg_match('/^\d{4}-\d{2}-\d{2}$/D',$expiry) ||
            !\DateTimeImmutable::createFromFormat('!Y-m-d',$expiry,new \DateTimeZone('UTC')))
            throw new Deny('INVALID_EXPIRY',422);
        Database::transaction(static function () use ($id,$r,$expiry): void {
            $n=Database::run('UPDATE td_licenses SET max_devices=?,feature_mask=?,package_name=?,signing_sha256=?,expires_at=? WHERE id=?',[
                $r['max'],$r['feature'],$r['pkg'],$r['cert'],$expiry.' 23:59:59',$id
            ]);
            $exists=Database::one('SELECT id FROM td_licenses WHERE id=?',[$id]);
            if (!$exists) throw new Deny('UNKNOWN_LICENSE',404);
            Database::run('INSERT INTO td_license_rules(license_id,package_rule,signer_rule,device_rule,requests_per_minute)
             VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE package_rule=VALUES(package_rule),
             signer_rule=VALUES(signer_rule),device_rule=VALUES(device_rule),requests_per_minute=VALUES(requests_per_minute)',[
                $id,$r['package'],$r['signer'],$r['device'],$r['perMin']
            ]);
            Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        });
        Audit::write('license_policy_update','admin',$id);
    }
    public static function check(array $v): array {
        $settings=Settings::get();
        if (!(int)$settings['enabled'] || (int)$settings['maintenance'])
            throw new Deny('SERVICE_DISABLED');
        $digest=Crypto::digest($v['key']);
        $candidate=Database::one('SELECT l.id, COALESCE(r.requests_per_minute,30) AS limit_per_minute
           FROM td_licenses l LEFT JOIN td_license_rules r ON r.license_id=l.id WHERE l.key_digest=?',[$digest]);
        if (!$candidate) throw new Deny('INVALID_LICENSE',401);
        RateLimit::hit('license:'.$candidate['id'],(int)$candidate['limit_per_minute'],60);
        $data=Database::transaction(static function () use ($digest,$v): array {
            $l=Database::one('SELECT l.*,r.package_rule,r.signer_rule,r.device_rule
              FROM td_licenses l LEFT JOIN td_license_rules r ON r.license_id=l.id
              WHERE l.key_digest=? FOR UPDATE',[$digest]);
            if (!$l) throw new Deny('INVALID_LICENSE',401);
            if ($l['status']!=='active') throw new Deny('LICENSE_DISABLED');
            $expires=strtotime($l['expires_at'].' UTC');
            if (!$expires || $expires<=time()) throw new Deny('LICENSE_EXPIRED');
            $p=$l['package_rule']??'if_present';
            $s=$l['signer_rule']??'if_present';
            $d=$l['device_rule']??'bind';
            self::matchRule($p,(string)$l['package_name'],$v['pkg'],'PACKAGE_MISMATCH');
            self::matchRule($s,strtoupper((string)$l['signing_sha256']),$v['cert'],'SIGNER_MISMATCH');
            Device::ensure($l,$v,$d);
            // Nonces prevent reuse of the same challenge within the short lease.
            Database::run('DELETE FROM td_used_nonces WHERE created_at<UTC_TIMESTAMP()-INTERVAL 5 MINUTE');
            try {
                Database::run('INSERT INTO td_used_nonces(license_id,nonce_hash) VALUES(?,?)',[
                    $l['id'],Crypto::digest('nonce:'.$v['nonce'])
                ]);
            } catch (\PDOException $e) {
                if ($e->getCode()==='23000') throw new Deny('NONCE_REPLAY',409);
                throw $e;
            }
            return [$l,$expires];
        });
        [$l,$expires]=$data;
        $now=time(); $until=min($now+60,$expires);
        $features=(int)$l['feature_mask'] ?: (int)$settings['default_features'];
        $sig=Crypto::sign(['tdcore-policy-v1',(string)$l['id'],$v['device'],$v['pkg'],$v['cert'],$v['nonce'],
           (string)$now,(string)$until,(string)$features,(string)$settings['policy_version']]);
        Audit::write('license_authorized','sdk',(int)$l['id']);
        return ['status'=>true] + $sig + ['data'=>[
            'expires_at'=>$until,'features'=>$features,'notice'=>$settings['notice'],
            'policy_version'=>(int)$settings['policy_version']
        ]];
    }
    private static function matchRule(string $mode, string $configured,
        string $presented, string $reason): void {
        if ($mode==='disabled') return;
        if ($mode==='required' && $configured==='') throw new Deny($reason);
        if ($configured!=='' && !hash_equals($configured,$presented)) throw new Deny($reason);
    }
}
