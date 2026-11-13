<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Audit {
    public static function write(string $action, string $actor='system',
        ?int $license=null, string $details=''): void {
        Database::run('INSERT INTO td_audit(action,actor,license_id,ip_hash,details) VALUES(?,?,?,?,?)',
            [$action,$actor,$license,Crypto::ipHash(),mb_substr($details,0,500)]);
    }
    public static function recent(int $limit=50): array {
        $limit=max(1,min(100,$limit));
        return Database::all("SELECT action,actor,license_id,details,created_at FROM td_audit ORDER BY id DESC LIMIT $limit");
    }
}
