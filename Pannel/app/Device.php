<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Device {
    public static function ensure(array $license, array $v, string $rule): void {
        $hash=Crypto::digest('device:'.$v['device']);
        if (Database::one('SELECT device_hash FROM td_blocked_devices WHERE device_hash=?',[$hash]))
            throw new Deny('DEVICE_BLOCKED');
        if ($rule==='unrestricted') return;
        $id=(int)$license['id'];
        $existing=Database::one('SELECT id FROM td_devices WHERE license_id=? AND device_hash=?',[$id,$hash]);
        if ($existing) {
            Database::run('UPDATE td_devices SET last_seen=UTC_TIMESTAMP() WHERE id=?',[$existing['id']]);
            return;
        }
        $count=Database::one('SELECT COUNT(*) AS n FROM td_devices WHERE license_id=?',[$id]);
        if ((int)($count['n']??0) >= (int)$license['max_devices'])
            throw new Deny('DEVICE_LIMIT');
        Database::run('INSERT INTO td_devices(license_id,device_hash) VALUES (?,?)',[$id,$hash]);
    }
    public static function listBlocked(): array {
        return Database::all('SELECT device_hash, reason, created_at FROM td_blocked_devices ORDER BY created_at DESC LIMIT 100');
    }
    public static function block(string $value,string $reason): void {
        $val=strtoupper(trim($value));
        if (!preg_match('/^[A-F0-9]{32,64}$/D',$val)) throw new Deny('INVALID_DEVICE',422);
        Database::run('INSERT INTO td_blocked_devices(device_hash,reason) VALUES(?,?) ON DUPLICATE KEY UPDATE reason=VALUES(reason)',
            [Crypto::digest('device:'.$val),mb_substr($reason,0,200)]);
        Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        Audit::write('device_blocked','admin');
    }
    public static function unblock(string $hash): void {
        if (!preg_match('/^[a-f0-9]{64}$/D',$hash)) throw new Deny('INVALID_DEVICE_HASH',422);
        Database::run('DELETE FROM td_blocked_devices WHERE device_hash=?',[$hash]);
        Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        Audit::write('device_unblocked','admin');
    }
    public static function reset(int $license): void {
        Database::run('DELETE FROM td_devices WHERE license_id=?',[$license]);
        Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        Audit::write('device_reset','admin',$license);
    }
}
