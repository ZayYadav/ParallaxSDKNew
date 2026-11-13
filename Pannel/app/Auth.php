<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Auth {
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
    }
    public static function login(string $password): bool {
        RateLimit::hit('admin-login:'.Crypto::ipHash(),8,900);
        $hash=Config::need('TD_ADMIN_PASSWORD_HASH');
        if (!password_verify($password,$hash)) {
            Audit::write('login_denied','anonymous');
            return false;
        }
        self::session();
        session_regenerate_id(true);
        $_SESSION['admin']=true;
        Audit::write('admin_login','admin');
        return true;
    }
    public static function logout(): void {
        self::session(); $_SESSION=[];
        if (ini_get('session.use_cookies')) {
            $params=session_get_cookie_params();
            setcookie(session_name(),'',time()-3600,$params['path'],$params['domain'],
                (bool)$params['secure'],(bool)$params['httponly']);
        }
        session_destroy();
    }
    public static function flash(string $text): void {
        self::session(); $_SESSION['notice']=$text;
    }
    public static function takeFlash(): string {
        self::session(); $v=(string)($_SESSION['notice']??''); unset($_SESSION['notice']); return $v;
    }
}
