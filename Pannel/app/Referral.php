<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Referral links, registration tracking and upline assignment. */
final class Referral {
    private const ALPHABET='ABCDEFGHJKLMNPQRSTUVWXYZ23456789';

    public static function generateCode(): string {
        for ($attempt=0;$attempt<20;$attempt++) {
            $code='TD';
            for ($i=0;$i<10;$i++) $code.=self::ALPHABET[random_int(0,strlen(self::ALPHABET)-1)];
            if (!Database::one('SELECT id FROM td_referral_codes WHERE code=?',[$code])) return $code;
        }
        throw new \RuntimeException('Unable to allocate referral code');
    }

    public static function ensurePersonalCode(int $accountId): array {
        $row=Database::one("SELECT * FROM td_referral_codes
            WHERE parent_id=? AND created_by=? AND target_role='reseller' AND active=1
            ORDER BY id ASC LIMIT 1",[$accountId,$accountId]);
        if ($row) return $row;
        $actor=AdminUser::byId($accountId);
        if (!$actor) throw new Deny('ACCOUNT_NOT_FOUND',404);
        $code=self::generateCode();
        Database::run("INSERT INTO td_referral_codes(code,parent_id,created_by,target_role,note)
            VALUES(?,?,?,'reseller',?)",[$code,$accountId,$accountId,
            'Personal reseller link for '.$actor['username']]);
        return Database::one('SELECT * FROM td_referral_codes WHERE code=?',[$code]) ?: [];
    }

    public static function byCode(string $code): ?array {
        $code=strtoupper(trim($code));
        if (!preg_match('/^TD[A-Z2-9]{10}$/D',$code)) return null;
        return Database::one('SELECT * FROM td_referral_codes WHERE code=? AND active=1',[$code]);
    }

    public static function create(int $actorId,int $parentId,string $targetRole,
        int $maxUses=0,string $note=''): array {
        $actor=AdminUser::byId($actorId);
        if (!$actor) throw new Deny('ACCOUNT_NOT_FOUND',404);
        if (!in_array($targetRole,['admin','reseller'],true)) throw new Deny('ROLE_INVALID',422);
        if (!Account::canReferRole($actorId,$targetRole)) throw new Deny('REFERRAL_FORBIDDEN',403);
        $parent=AdminUser::byId($parentId);
        if (!$parent) throw new Deny('PARENT_NOT_FOUND',404);
        if ($parent['role']==='reseller') throw new Deny('PARENT_FORBIDDEN',403);
        if ($actor['role']!=='owner' && $parentId!==$actorId)
            throw new Deny('PARENT_FORBIDDEN',403);
        $maxUses=max(0,min(10000,$maxUses));
        $code=self::generateCode();
        Database::run('INSERT INTO td_referral_codes(code,parent_id,created_by,target_role,max_uses,note)
            VALUES(?,?,?,?,?,?)',[$code,$parentId,$actorId,$targetRole,$maxUses,
            mb_substr(trim($note),0,120)]);
        $row=Database::one('SELECT * FROM td_referral_codes WHERE code=?',[$code]) ?: [];
        Audit::write('referral_created','panel',null,$code.' → '.$targetRole);
        return $row;
    }

    public static function listFor(int $actorId): array {
        $actor=AdminUser::byId($actorId);
        if (!$actor) return [];
        if ($actor['role']==='owner') {
            return Database::all('SELECT r.*,a.username AS parent_name,a.role AS parent_role
                FROM td_referral_codes r LEFT JOIN td_admins a ON a.id=r.parent_id
                ORDER BY r.id DESC LIMIT 100');
        }
        return Database::all('SELECT r.*,a.username AS parent_name,a.role AS parent_role
            FROM td_referral_codes r LEFT JOIN td_admins a ON a.id=r.parent_id
            WHERE r.parent_id=? OR r.created_by=? ORDER BY r.id DESC LIMIT 100',
            [$actorId,$actorId]);
    }

    public static function signupsFor(int $actorId,int $limit=50): array {
        $actor=AdminUser::byId($actorId);
        if (!$actor) return [];
        $limit=max(1,min(100,$limit));
        if ($actor['role']==='owner') {
            return Database::all("SELECT f.*,c.code,a.username FROM td_referrals f
                LEFT JOIN td_referral_codes c ON c.id=f.code_id
                LEFT JOIN td_admins a ON a.id=f.account_id
                ORDER BY f.id DESC LIMIT $limit");
        }
        $scope=Account::scopeIds($actorId) ?: [];
        if (!$scope) return [];
        $in=implode(',',array_fill(0,count($scope),'?'));
        $params=array_merge([$actorId],$scope);
        return Database::all("SELECT f.*,c.code,a.username FROM td_referrals f
            LEFT JOIN td_referral_codes c ON c.id=f.code_id
            LEFT JOIN td_admins a ON a.id=f.account_id
            WHERE f.referrer_id=? OR f.account_id IN ($in)
            ORDER BY f.id DESC LIMIT $limit",$params);
    }

    /** Consume one use inside an existing transaction and return the code row. */
    public static function consumeLocked(string $code): array {
        $code=strtoupper(trim($code));
        $row=Database::one('SELECT * FROM td_referral_codes WHERE code=? FOR UPDATE',[$code]);
        if (!$row || (int)$row['active']!==1) throw new Deny('REFERRAL_INVALID',422);
        if ((int)$row['max_uses']>0 && (int)$row['uses'] >= (int)$row['max_uses'])
            throw new Deny('REFERRAL_EXHAUSTED',422);
        Database::run('UPDATE td_referral_codes SET uses=uses+1,last_used_at=UTC_TIMESTAMP() WHERE id=?',
            [$row['id']]);
        return $row;
    }

    public static function setActive(int $actorId,int $codeId,bool $active): void {
        $code=Database::one('SELECT * FROM td_referral_codes WHERE id=?',[$codeId]);
        if (!$code) throw new Deny('REFERRAL_NOT_FOUND',404);
        if ($actorId!==(int)$code['created_by'] && !Account::isOwner())
            throw new Deny('REFERRAL_FORBIDDEN',403);
        Database::run('UPDATE td_referral_codes SET active=? WHERE id=?',[$active?1:0,$codeId]);
        Audit::write($active?'referral_enabled':'referral_disabled','panel',null,(string)$code['code']);
    }

    public static function url(string $code): string {
        $host=(string)($_SERVER['HTTP_HOST']??'127.0.0.1:8080');
        $host=preg_replace('/[^A-Za-z0-9.:\-\[\]]/','',$host) ?: '127.0.0.1:8080';
        return 'http://'.$host.'/register?ref='.rawurlencode($code);
    }
}
