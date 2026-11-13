<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminController {
    public static function home(): never {
        Auth::requireAdmin();
        $settings=Settings::get();
        $counts=Database::one('SELECT COUNT(*) AS total, SUM(CASE WHEN status=? THEN 1 ELSE 0 END) AS active FROM td_licenses',['active']) ?: [];
        $body='<div class="grid">
        <section class="panel"><h3>Licenses</h3><p class="big">'.View::e($counts['total']??0).'</p></section>
        <section class="panel"><h3>Active records</h3><p class="big">'.View::e($counts['active']??0).'</p></section>
        <section class="panel"><h3>Service enabled</h3><p class="big">'.((int)$settings['enabled']?'YES':'NO').'</p></section>
        <section class="panel"><h3>Maintenance</h3><p class="big">'.((int)$settings['maintenance']?'ON':'OFF').'</p></section></div>
        <section class="panel"><h2>Authorization model</h2>
        <p>The SDK requires a fresh HTTPS response authenticated by ECDSA P-256.
        Signed policies expire in 60 seconds. Server-side per-key restrictions apply
        before signing. The panel cannot enforce SDK behaviors that have no matching
        client-side feature gate.</p><p class="muted">API: <code>POST /api/v1/license/check</code> (alias: <code>/connect</code>)</p></section>';
        View::page('Overview',$body);
    }
    public static function loginPage(): never {
        Auth::session();
        if (!empty($_SESSION['admin'])) Http::redirect('/admin');
        $body='<section class="panel" style="max-width:420px;margin:50px auto">
        <h2>Administrator sign in</h2><form method="post" action="/admin/login">'
        .View::csrf().View::field('Admin password','password','','password','required autocomplete="current-password"')
        .'<button type="submit">Sign in</button></form></section>';
        View::page('Sign in',$body,false);
    }
    public static function login(): never {
        Auth::csrfCheck();
        $data=Http::form();
        if (!Auth::login((string)($data['password']??''))) {
            Auth::flash('Invalid credentials'); Http::redirect('/admin/login');
        }
        Http::redirect('/admin');
    }
    public static function logout(): never {
        Auth::csrfCheck(); Auth::requireAdmin(); Auth::logout(); Http::redirect('/admin/login');
    }
    public static function audit(): never {
        Auth::requireAdmin();
        $html='<section class="panel"><h2>Recent security activity</h2><div class="scroll"><table><tr>
        <th>UTC</th><th>Action</th><th>Actor</th><th>License ID</th><th>Details</th></tr>';
        foreach (Audit::recent() as $r) {
            $html.='<tr><td>'.View::e($r['created_at']).'</td><td>'.View::e($r['action']).'</td><td>'.
            View::e($r['actor']).'</td><td>'.View::e($r['license_id']??'').'</td><td>'.
            View::e($r['details']).'</td></tr>';
        }
        View::page('Audit log',$html.'</table></div></section>');
    }
}
