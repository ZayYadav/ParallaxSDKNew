<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Auth {
    private const IDLE_SECONDS=28800; // 8 hours without activity
    private static ?array $cached=null;

    public static function session(): void {
        if (session_status()===PHP_SESSION_ACTIVE) return;
        ini_set('session.use_strict_mode','1');
        ini_set('session.use_only_cookies','1');
        session_name('TDCORE_ADMIN');
        session_set_cookie_params([
            'lifetime'=>0, 'path'=>'/', 'secure'=>Config::get('TD_FORCE_HTTPS','1')==='1',
            'httponly'=>true, 'samesite'=>'Strict'
        ]);
        session_start();
    }
    public static function csrf(): string {
        self::session();
        if (empty($_SESSION['tdcsrf'])) $_SESSION['tdcsrf']=bin2hex(random_bytes(32));
        return $_SESSION['tdcsrf'];
    }
    public static function csrfCheck(): void {
        self::session();
        $form=Http::form();
        if (!hash_equals(self::csrf(),(string)($form['csrf']??'')))
            throw new Deny('CSRF_REJECTED',403);
    }
    public static function requireAdmin(): void {
        self::session();
        if (empty($_SESSION['admin']) || $_SESSION['admin']!==true)
            Http::redirect('/admin/login');
        $last=(int)($_SESSION['last_seen']??0);
        if ($last>0 && time()-$last>self::IDLE_SECONDS) {
            self::logout();
            Http::redirect('/admin/login?expired=1');
        }
        $admin=self::current();
        if (!$admin || (int)$admin['disabled']===1) {
            self::logout();
            Http::redirect('/admin/login?expired=1');
        }
        $_SESSION['last_seen']=time();
        $_SESSION['admin_username']=(string)$admin['username'];
        $_SESSION['admin_role']=(string)$admin['role'];
    }
    public static function current(): ?array {
        if (self::$cached!==null) return self::$cached;
        self::session();
        $id=(int)($_SESSION['admin_id']??0);
        if ($id<=0) return null;
        $row=AdminUser::byId($id);
        return self::$cached=$row;
    }
    public static function username(): string {
        $a=self::current();
        return $a?(string)$a['username']:(string)($_SESSION['admin_username']??'admin');
    }
    public static function role(): string {
        $a=self::current();
        return $a?(string)$a['role']:(string)($_SESSION['admin_role']??'reseller');
    }
    public static function isOwner(): bool { return self::role()==='owner'; }
    public static function isAdmin(): bool { return self::role()==='admin'; }
    public static function isReseller(): bool { return self::role()==='reseller'; }
    public static function isStaff(): bool { return in_array(self::role(),['owner','admin'],true); }

    /** Require one of the given roles, otherwise send the account to its dashboard. */
    public static function requireRole(string ...$roles): void {
        self::requireAdmin();
        if (!in_array(self::role(),$roles,true)) {
            self::flash('That area is not available for your role');
            Http::redirect('/admin');
        }
    }
    public static function requireStaff(): void { self::requireRole('owner','admin'); }
    public static function requireOwner(): void { self::requireRole('owner'); }

    public static function login(string $username,string $password): bool {
        $username=trim($username);
        RateLimit::hit('admin-login-ip:'.Crypto::ipHash(),12,900);
        RateLimit::hit('admin-login-user:'.Crypto::digest('admin:'.strtolower($username)),7,900);
        $row=strlen($username)<=60 ? AdminUser::byUsername($username) : null;
        $ok=$row!==null && (int)$row['disabled']===0 && password_verify($password,(string)$row['password_hash']);
        self::recordLogin($username,$ok);
        if (!$ok) {
            Audit::write('login_denied','anonymous',null,
                'username: '.mb_substr($username,0,60));
            return false;
        }
        if (password_needs_rehash((string)$row['password_hash'],PASSWORD_DEFAULT)) {
            Database::run('UPDATE td_admins SET password_hash=? WHERE id=?',
                [AdminUser::hash($password),(int)$row['id']]);
        }
        self::establish($row);
        AdminUser::touchLogin((int)$row['id']);
        Audit::write('admin_login','panel',null,'account: '.$row['username'].' ('.$row['role'].')');
        return true;
    }

    /** Start an authenticated session for an existing account row. */
    public static function establish(array $row): void {
        self::session();
        session_regenerate_id(true);
        $_SESSION['admin']=true;
        $_SESSION['admin_id']=(int)$row['id'];
        $_SESSION['admin_username']=(string)$row['username'];
        $_SESSION['admin_role']=(string)$row['role'];
        $_SESSION['last_seen']=time();
        self::$cached=$row;
    }
    public static function changePassword(string $current,string $new,string $confirm): void {
        Auth::requireAdmin();
        $row=self::current();
        if (!$row) throw new Deny('ADMIN_NOT_FOUND',404);
        if (!password_verify($current,(string)$row['password_hash']))
            throw new Deny('CURRENT_PASSWORD_WRONG',422);
        if ($new!==$confirm) throw new Deny('PASSWORD_MISMATCH',422);
        AdminUser::setPassword((int)$row['id'],$new,true);
        session_regenerate_id(true);
        $_SESSION['last_seen']=time();
    }
    public static function logout(): void {
        self::session();
        if (!empty($_SESSION['admin'])) Audit::write('admin_logout','panel',null,'account: '.self::username());
        $_SESSION=[];
        if (ini_get('session.use_cookies')) {
            $params=session_get_cookie_params();
            setcookie(session_name(),'',time()-3600,$params['path'],$params['domain'],
                (bool)$params['secure'],(bool)$params['httponly']);
        }
        session_destroy();
        self::$cached=null;
    }
    public static function recordLogin(string $username,bool $success): void {
        try {
            Database::run('INSERT INTO td_login_events(username,ip_hash,success,user_agent) VALUES(?,?,?,?)',[
                mb_substr(trim($username),0,60),Crypto::ipHash(),$success?1:0,
                mb_substr((string)($_SERVER['HTTP_USER_AGENT']??''),0,200)
            ]);
            Database::run('DELETE FROM td_login_events WHERE created_at<UTC_TIMESTAMP()-INTERVAL 90 DAY');
        } catch (\Throwable $e) {
            error_log('TDCore login event: '.$e->getMessage());
        }
    }
    public static function loginEvents(int $limit=12): array {
        $limit=max(1,min(100,$limit));
        return Database::all("SELECT username,ip_hash,success,user_agent,created_at
            FROM td_login_events ORDER BY id DESC LIMIT $limit");
    }
    public static function failedLogins24h(): int {
        $r=Database::one('SELECT COUNT(*) AS n FROM td_login_events
            WHERE success=0 AND created_at>=UTC_TIMESTAMP()-INTERVAL 24 HOUR');
        return (int)($r['n']??0);
    }
    public static function flash(string $text): void {
        self::session(); $_SESSION['notice']=$text;
    }
    public static function takeFlash(): string {
        self::session(); $v=(string)($_SESSION['notice']??''); unset($_SESSION['notice']); return $v;
    }
}
