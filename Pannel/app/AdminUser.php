<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminUser {
    public const ROLES=['owner','admin','reseller'];

    public static function byUsername(string $username): ?array {
        return Database::one('SELECT * FROM td_admins WHERE username=? LIMIT 1',[$username]);
    }
    public static function byId(int $id): ?array {
        return Database::one('SELECT * FROM td_admins WHERE id=? LIMIT 1',[$id]);
    }
    public static function all(): array {
        return Database::all('SELECT id,username,display_name,role,parent_id,disabled,credit_balance,
            unlimited_credits,can_refer_admins,last_login_at,created_at
            FROM td_admins ORDER BY id ASC');
    }
    public static function downline(int $accountId): array {
        $rows=Database::all('SELECT id,username,display_name,role,parent_id,disabled,credit_balance,
            unlimited_credits,can_refer_admins,last_login_at,created_at FROM td_admins ORDER BY id ASC');
        $children=[];
        foreach ($rows as $r) $children[(int)($r['parent_id']??0)][]=$r;
        $out=[]; $queue=[$accountId]; $seen=[$accountId=>true];
        while ($queue) {
            $id=array_shift($queue);
            foreach ($children[$id]??[] as $child) {
                $cid=(int)$child['id'];
                if (isset($seen[$cid])) continue;
                $seen[$cid]=true; $out[]=$child; $queue[]=$cid;
            }
        }
        return $out;
    }
    public static function count(): int {
        $r=Database::one('SELECT COUNT(*) AS n FROM td_admins');
        return (int)($r['n']??0);
    }
    public static function owners(): int {
        $r=Database::one('SELECT COUNT(*) AS n FROM td_admins WHERE role=? AND disabled=0',['owner']);
        return (int)($r['n']??0);
    }
    public static function username(mixed $raw): string {
        $u=trim((string)$raw);
        if (!preg_match('/^[A-Za-z0-9][A-Za-z0-9_.-]{2,59}$/D',$u))
            throw new Deny('USERNAME_INVALID',422);
        return $u;
    }
    public static function password(mixed $raw, string $username=''): string {
        $p=(string)$raw;
        if (mb_strlen($p)<8) throw new Deny('PASSWORD_TOO_SHORT',422);
        if (mb_strlen($p)>200) throw new Deny('PASSWORD_TOO_LONG',422);
        if ($username!=='' && strtolower($p)===strtolower($username))
            throw new Deny('PASSWORD_TOO_WEAK',422);
        return $p;
    }
    public static function hash(string $password): string {
        return password_hash($password,PASSWORD_DEFAULT);
    }
    /** Create any account. Callers must enforce creator permissions. */
    public static function create(string $username,string $password,string $role='reseller',
        ?int $parentId=null,string $displayName=''): int {
        $username=self::username($username);
        if (!in_array($role,self::ROLES,true)) throw new Deny('ROLE_INVALID',422);
        $password=self::password($password,$username);
        if (self::byUsername($username)) throw new Deny('USERNAME_TAKEN',409);
        if ($parentId!==null && !self::byId($parentId)) throw new Deny('PARENT_NOT_FOUND',404);
        $displayName=mb_substr(trim($displayName),0,80);
        Database::run('INSERT INTO td_admins(username,display_name,password_hash,role,parent_id,
            unlimited_credits,can_refer_admins) VALUES(?,?,?,?,?,?,?)',[
            $username,$displayName,self::hash($password),$role,$parentId,
            $role==='owner'?1:0,$role==='owner'?1:0
        ]);
        $id=(int)Database::db()->lastInsertId();
        if ($role!=='reseller') Referral::ensurePersonalCode($id);
        Audit::write('account_created','panel',null,'new account: '.$username.' ('.$role.')');
        return $id;
    }
    /** Insert a registered account inside an existing transaction. */
    public static function insertLocked(string $username,string $password,string $role,int $parentId,
        string $displayName=''): int {
        $username=self::username($username);
        if (!in_array($role,self::ROLES,true) || $role==='owner') throw new Deny('ROLE_INVALID',422);
        $password=self::password($password,$username);
        if (self::byUsername($username)) throw new Deny('USERNAME_TAKEN',409);
        Database::run('INSERT INTO td_admins(username,display_name,password_hash,role,parent_id)
            VALUES(?,?,?,?,?)',[$username,mb_substr(trim($displayName),0,80),
            self::hash($password),$role,$parentId]);
        return (int)Database::db()->lastInsertId();
    }
    public static function setPassword(int $id,string $password,bool $changedBySelf): void {
        $row=self::byId($id);
        if (!$row) throw new Deny('ADMIN_NOT_FOUND',404);
        $password=self::password($password,(string)$row['username']);
        Database::run('UPDATE td_admins SET password_hash=?,password_changed_at=UTC_TIMESTAMP() WHERE id=?',
            [self::hash($password),$id]);
        Audit::write($changedBySelf?'password_changed':'admin_password_reset','panel',null,
            'account: '.$row['username']);
    }
    public static function setDisabled(int $id,bool $disabled): void {
        $row=self::byId($id);
        if (!$row) throw new Deny('ADMIN_NOT_FOUND',404);
        if ($disabled && $row['role']==='owner' && self::owners()<=1)
            throw new Deny('LAST_OWNER',409);
        if ((int)$row['disabled']!==($disabled?1:0)) {
            Database::run('UPDATE td_admins SET disabled=? WHERE id=?',[$disabled?1:0,$id]);
            Audit::write($disabled?'account_disabled':'account_enabled','panel',null,
                'account: '.$row['username']);
        }
    }
    public static function delete(int $id): void {
        $row=self::byId($id);
        if (!$row) throw new Deny('ADMIN_NOT_FOUND',404);
        if ($row['role']==='owner' && self::owners()<=1)
            throw new Deny('LAST_OWNER',409);
        Database::run('UPDATE td_admins SET parent_id=NULL WHERE parent_id=?',[$id]);
        Database::run('DELETE FROM td_admins WHERE id=?',[$id]);
        Audit::write('account_deleted','panel',null,'account: '.$row['username']);
    }
    public static function touchLogin(int $id): void {
        Database::run('UPDATE td_admins SET last_login_at=UTC_TIMESTAMP() WHERE id=?',[$id]);
    }
}
