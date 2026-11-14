<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminSecurityController {
    public static function page(): never {
        Auth::requireStaff();
        $me=Auth::current() ?: [];
        $owner=Auth::isOwner();
        $admins=Account::visibleAccounts();
        $events=Auth::loginEvents(12);
        $key=Crypto::signingKeyInfo();
        $failed=Auth::failedLogins24h();
        $blocked=(int)(Database::one('SELECT COUNT(*) AS n FROM td_blocked_devices')['n']??0);
        $loginToday=(int)(Database::one('SELECT COUNT(*) AS n FROM td_login_events
            WHERE success=1 AND created_at>=UTC_DATE()')['n']??0);

        $stats='<div class="grid g-4">'
            .View::stat('Admin accounts',count($admins),($owner?'You are the owner':'Owner-only management'),'users')
            .View::stat('Logins today',$loginToday,$failed.' failed in 24h','lock',$failed>0?'warn':'ok')
            .View::stat('Blocked devices',$blocked,'Hashed SDK fingerprints','shield')
            .View::stat('Signing key',$key['ok']?'P-256':'Issue',$key['curve'],'key',$key['ok']?'ok':'bad')
            .'</div>';

        $session='<section class="card"><div class="card-head"><div><h2>Your session</h2>
            <p>Strict cookie, CSRF-protected, 8-hour idle timeout.</p></div>'
            .View::badge((string)($me['role']??'admin')).'</div>
            <dl class="kv">
            <dt>Username</dt><dd><strong>'.View::e($me['username']??'—').'</strong></dd>
            <dt>Role</dt><dd>'.View::e($me['role']??'—').'</dd>
            <dt>Last login</dt><dd>'.View::e($me['last_login_at']??'just now').' UTC</dd>
            <dt>IP fingerprint</dt><dd class="mono small">'.View::e(substr(Crypto::ipHash(),0,24)).'…</dd>
            <dt>Session policy</dt><dd class="small">HttpOnly · SameSite=Strict · regenerate on login</dd>
            </dl><div class="divider"></div>
            <form method="post" action="/admin/logout">'.View::csrf()
            .'<button class="ghost" type="submit">'.View::icon('logout',16).'Sign out of this session</button></form>
            </section>';

        $password='<section class="card"><div class="card-head"><div><h2>Change password</h2>
            <p>Minimum 8 characters. Current password is required.</p></div></div>
            <form method="post" action="/admin/security/password">'.View::csrf()
            .View::field('Current password','current','','password','required autocomplete="current-password"')
            .View::field('New password','new','','password','required minlength="8" autocomplete="new-password"')
            .View::field('Confirm new password','confirm','','password','required minlength="8" autocomplete="new-password"')
            .'<button type="submit">'.View::icon('lock',16).'Update password</button></form></section>';

        $rows='';
        foreach ($admins as $a) {
            $isMe=(int)$a['id']===(int)($me['id']??0);
            $actions='';
            if (!$isMe && Account::canManage((int)($me['id']??0),(int)$a['id'])) {
                $actions='<form method="post" action="/admin/security/admins/toggle" style="display:inline">'
                    .View::csrf().'<input type="hidden" name="id" value="'.(int)$a['id'].'">'
                    .'<button class="ghost sm" type="submit">'.((int)$a['disabled']?'Enable':'Disable').'</button></form> '
                    .'<details style="display:inline-block;margin:0"><summary>Delete account</summary>'
                    .'<form method="post" action="/admin/security/admins/delete">'.View::csrf()
                    .'<input type="hidden" name="id" value="'.(int)$a['id'].'">'
                    .'<label class="small"><input type="checkbox" name="confirm" value="1" required>
                    confirm deletion</label>'
                    .'<button class="danger sm" type="submit">Delete</button></form></details>';
            } elseif ($isMe) {
                $actions='<span class="badge info">this is you</span>';
            } else {
                $actions='<span class="dim small">outside your scope</span>';
            }
            $reset=!$isMe&&Account::canManage((int)($me['id']??0),(int)$a['id'])
                ?'<details><summary>Reset password</summary>
                <form method="post" action="/admin/security/admins/password">'.View::csrf()
                .'<input type="hidden" name="id" value="'.(int)$a['id'].'">'
                .View::field('New password','new','','password','required minlength="8" autocomplete="new-password"')
                .'<button class="sm" type="submit">Set password</button></form></details>':'';
            $rows.='<tr><td>'.(int)$a['id'].'</td>
                <td><strong>'.View::e($a['username']).'</strong>'.($isMe?' <span class="badge active">you</span>':'').'</td>
                <td>'.View::badge((string)$a['role']).'</td>
                <td>'.((int)$a['disabled']?'<span class="badge revoked">disabled</span>':'<span class="badge active">active</span>').'</td>
                <td class="dim small">'.View::e($a['last_login_at']??'never').'</td>
                <td><div class="btn-row">'.$actions.'</div>'.$reset.'</td></tr>';
        }
        $accounts='<section class="card"><div class="card-head"><div><h2>Admin accounts</h2>
            <p>'.($owner?'Create, disable or remove panel operators.':'Only the owner can manage accounts.').'</p></div>
            <span class="badge '.($owner?'active':'neutral').'">'.($owner?'owner':'read only').'</span></div>
            <div class="table-wrap"><table><thead><tr><th>ID</th><th>Username</th><th>Role</th>
            <th>Status</th><th>Last login (UTC)</th><th>Actions</th></tr></thead><tbody>'.$rows.'</tbody></table></div>';

        if ($owner) {
            $accounts.='<div class="divider"></div><h3 style="margin-bottom:12px">Create administrator</h3>
                <form method="post" action="/admin/security/admins/create">'.View::csrf()
                .'<div class="grid g-3">'.View::field('Username','username','','text',
                    'required minlength="3" maxlength="60" autocomplete="off"')
                .View::field('Password','password','','password','required minlength="8" autocomplete="new-password"')
                .View::field('Confirm password','confirm','','password','required minlength="8" autocomplete="new-password"')
                .View::select('role','admin',['admin'=>'Admin — full panel access',
                    'owner'=>'Owner — can manage administrators'],'Role').'</div>
                <button type="submit">'.View::icon('plus',16).'Create account</button></form>';
        }
        $accounts.='</section>';

        $evRows='';
        foreach ($events as $e) {
            $evRows.='<tr><td class="dim small">'.View::e($e['created_at']).'</td>
                <td>'.View::e($e['username']!==''?$e['username']:'—').'</td>
                <td>'.((int)$e['success']?'<span class="badge active">success</span>':'<span class="badge revoked">failed</span>').'</td>
                <td class="mono small dim">'.View::e(substr((string)$e['ip_hash'],0,20)).'…</td>
                <td class="small dim">'.View::e(mb_substr((string)$e['user_agent'],0,60)).'</td></tr>';
        }
        if ($evRows==='') $evRows='<tr><td colspan="5"><div class="empty">'.View::icon('clock',24)
            .'<div>No login events yet.</div></div></td></tr>';
        $eventsCard='<section class="card"><div class="card-head"><div><h2>Login activity</h2>
            <p>Newest 12 attempts. IP addresses are stored as keyed hashes only.</p></div>
            <a class="badge info" href="/admin/audit">Audit log</a></div>
            <div class="table-wrap"><table><thead><tr><th>UTC</th><th>Username</th><th>Result</th>
            <th>IP hash</th><th>Client</th></tr></thead><tbody>'.$evRows.'</tbody></table></div></section>';

        $keyCard='<section class="card"><div class="card-head"><div><h2>Signing key</h2>
            <p>The private key stays on the server; only the public key ships to the SDK.</p></div>'
            .'<span class="badge '.($key['ok']?'active':'revoked').'">'.($key['ok']?'healthy':'check key').'</span></div>
            <dl class="kv">
            <dt>Curve</dt><dd>'.View::e($key['curve']).' · '.$key['bits'].'-bit</dd>
            <dt>Fingerprint</dt><dd class="mono small">'.View::e($key['fingerprint']).'</dd>
            <dt>Private key file</dt><dd class="mono small">'.View::e($key['path']).'</dd>
            <dt>Public key (X.509 DER, base64)</dt><dd><code>'.View::e(Crypto::publicKeyBase64()).'</code></dd>
            </dl><div class="divider"></div>
            <p class="muted small" style="margin:0">The SDK accepts only policies signed by this
            key and rejects expired or replayed nonces. Rotating the key requires a new SDK build
            with the replacement public key.</p></section>';

        $checks=[];
        $s=Settings::get();
        $checks[]=[Config::get('TD_FORCE_HTTPS','1')==='1','HTTPS enforced',
            'This local deployment runs HTTP. Set TD_FORCE_HTTPS=1 behind a TLS proxy.'];
        $checks[]=[(int)$s['enabled']===1,'Licensing service enabled','Disabled denies all SDK checks.'];
        $checks[]=[(int)$s['maintenance']===0,'Maintenance mode off','Maintenance denies new leases.'];
        $checks[]=[$key['ok'],'P-256 signing key healthy','Curve '.$key['curve'].'.'];
        $checks[]=[AdminUser::count()>1,'Multiple administrators','A second account prevents lock-out.'];
        $checks[]=[$failed===0,'No failed logins in 24h',$failed.' failure(s) recorded.'];
        $list='';
        foreach ($checks as $c) {
            $list.='<li><span class="dot '.($c[0]?'ok':'no').'">'.($c[0]?'✓':'!').'</span>
                <span class="txt">'.View::e($c[1]).'<small>'.View::e($c[2]).'</small></span></li>';
        }
        $auditCard='<section class="card"><div class="card-head"><div><h2>Security checklist</h2>
            <p>Deployment hardening at a glance.</p></div>
            <a class="btn ghost sm" href="/admin/devices">Manage bans</a></div>
            <ul class="checklist">'.$list.'</ul></section>';

        View::page('Security center',$stats
            .'<div class="grid g-2" style="margin-top:18px">'.$session.$password.'</div>'
            .'<div style="margin-top:18px">'.$accounts.'</div>'
            .'<div class="grid g-2" style="margin-top:18px">'.$eventsCard.$keyCard.'</div>'
            .'<div style="margin-top:18px">'.$auditCard.'</div>',true,
            'Administrator accounts, passwords, login history and signing-key health.',
            '<a class="btn ghost" href="/admin/audit">'.View::icon('activity',16).'Audit log</a>');
    }
    public static function changePassword(): never {
        Auth::csrfCheck(); Auth::requireStaff();
        $v=Http::form();
        try {
            Auth::changePassword((string)($v['current']??''),(string)($v['new']??''),(string)($v['confirm']??''));
        } catch (Deny $e) {
            Auth::flash('Password not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/security');
        }
        Auth::flash('Password updated. Other sessions keep their current lease until they expire.');
        Http::redirect('/admin/security');
    }
    public static function createAdmin(): never {
        self::ownerPost();
        $v=Http::form();
        if ((string)($v['password']??'')!==(string)($v['confirm']??'')) {
            Auth::flash('Passwords do not match'); Http::redirect('/admin/security');
        }
        try {
            AdminUser::create((string)($v['username']??''),(string)($v['password']??''),
                (string)($v['role']??'admin'));
        } catch (Deny $e) {
            Auth::flash('Account not created: '.self::reason($e->getMessage()));
            Http::redirect('/admin/security');
        }
        Auth::flash('Administrator account created');
        Http::redirect('/admin/security');
    }
    public static function toggleAdmin(): never {
        self::ownerPost();
        $v=Http::form();
        $id=(int)($v['id']??0);
        $me=Auth::current();
        if ($id<1 || $id===(int)($me['id']??0)) {
            Auth::flash('You cannot disable your own account'); Http::redirect('/admin/security');
        }
        $target=AdminUser::byId($id);
        if (!$target) { Auth::flash('Account not found'); Http::redirect('/admin/security'); }
        $disable=(int)$target['disabled']===0;
        try { AdminUser::setDisabled($id,$disable); }
        catch (Deny $e) { Auth::flash(self::reason($e->getMessage())); Http::redirect('/admin/security'); }
        Auth::flash('Account '.($disable?'disabled':'enabled'));
        Http::redirect('/admin/security');
    }
    public static function deleteAdmin(): never {
        self::ownerPost();
        $v=Http::form();
        $id=(int)($v['id']??0);
        $me=Auth::current();
        if ($id<1 || $id===(int)($me['id']??0)) {
            Auth::flash('You cannot delete your own account'); Http::redirect('/admin/security');
        }
        if (empty($v['confirm'])) {
            Auth::flash('Tick the confirmation box before deleting an account');
            Http::redirect('/admin/security');
        }
        try { AdminUser::delete($id); }
        catch (Deny $e) { Auth::flash(self::reason($e->getMessage())); Http::redirect('/admin/security'); }
        Auth::flash('Administrator deleted');
        Http::redirect('/admin/security');
    }
    public static function resetAdminPassword(): never {
        self::ownerPost();
        $v=Http::form();
        $id=(int)($v['id']??0);
        try {
            $target=AdminUser::byId($id);
            if (!$target) throw new Deny('ADMIN_NOT_FOUND',404);
            AdminUser::setPassword($id,(string)($v['new']??''),false);
        } catch (Deny $e) {
            Auth::flash('Password not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/security');
        }
        Auth::flash('Password reset for administrator');
        Http::redirect('/admin/security');
    }
    private static function ownerPost(): void {
        Auth::csrfCheck(); Auth::requireStaff();
        if (!Auth::isOwner()) {
            Auth::flash('Only the owner can manage administrator accounts');
            Http::redirect('/admin/security');
        }
    }
    private static function reason(string $code): string {
        return [
            'CURRENT_PASSWORD_WRONG'=>'current password is incorrect',
            'PASSWORD_MISMATCH'=>'the new passwords do not match',
            'PASSWORD_TOO_SHORT'=>'password must be at least 8 characters',
            'PASSWORD_TOO_LONG'=>'password is too long',
            'PASSWORD_TOO_WEAK'=>'password must not equal the username',
            'USERNAME_INVALID'=>'use 3-60 letters, numbers, dot, dash or underscore',
            'USERNAME_TAKEN'=>'that username already exists',
            'ROLE_INVALID'=>'invalid role',
            'LAST_OWNER'=>'the last active owner cannot be removed',
            'ADMIN_NOT_FOUND'=>'account not found',
        ][$code]??$code;
    }
}
