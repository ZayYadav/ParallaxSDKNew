<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Self-service account, balance and personal referral page. */
final class AdminAccountController {
    public static function page(): never {
        Auth::requireAdmin();
        $me=Auth::current() ?: [];
        $id=(int)($me['id']??0);
        $staff=Account::isStaff();
        $unlimited=Account::isUnlimited($id);
        $balance=(int)($me['credit_balance']??0);
        $parent=(int)($me['parent_id']??0)>0?AdminUser::byId((int)$me['parent_id']):null;
        $codes=Referral::listFor($id);
        $personal=null;
        foreach ($codes as $c) {
            if ((int)$c['parent_id']===$id && (int)$c['created_by']===$id
                && $c['target_role']==='reseller') { $personal=$c; break; }
        }
        $ledger=Credit::ledger($id,12);

        $profile='<section class="card"><div class="card-head"><div><h2>Your account</h2>
            <p>Identity and session details.</p></div>'.View::badge((string)($me['role']??'reseller')).'</div>
            <dl class="kv">'
            .'<dt>Username</dt><dd><strong>'.View::e($me['username']??'—').'</strong></dd>'
            .'<dt>Display name</dt><dd>'.View::e(($me['display_name']??'')!==''?$me['display_name']:'—').'</dd>'
            .'<dt>Role</dt><dd>'.View::e(Account::roleLabel((string)($me['role']??'reseller'))).'</dd>'
            .'<dt>Upline</dt><dd>'.($parent?View::e($parent['username']).' · '
                .Account::roleLabel((string)$parent['role']):'top level').'</dd>'
            .'<dt>Credit</dt><dd>'.($unlimited?'<span class="badge active">unlimited</span>'
                :number_format($balance).' credits').'</dd>'
            .'<dt>Last login</dt><dd>'.View::e($me['last_login_at']??'just now').' UTC</dd>'
            .'<dt>Member since</dt><dd>'.View::e($me['created_at']??'—').' UTC</dd>'
            .'</dl></section>';

        $creditCard='<section class="card"><div class="card-head"><div><h2>Credits</h2>
            <p>'.($unlimited?'This account has unlimited owner-funded credit.'
                :'Spent only when you generate a licence key.').'</p></div>'
            .'<span class="chip">'.View::icon('coin',19).'</span></div>
            <div class="stat" style="border:0;box-shadow:none;padding:0;background:none">
            <div class="value">'.($unlimited?'∞':number_format($balance)).'</div>
            <p class="hint">'.($unlimited?'unlimited credits':'available credits').'</p></div>'
            .'<dl class="kv" style="margin-top:16px">'
            .'<dt>Key cost</dt><dd>'.number_format(Credit::keyCost()).' credits</dd>'
            .'<dt>Keys available</dt><dd>'.($unlimited?'unlimited'
                :(Credit::keyCost()>0?floor($balance/Credit::keyCost()):'unlimited')).'</dd>'
            .'</dl><div class="btn-row" style="margin-top:16px">'
            .'<a class="btn ghost sm" href="/admin/credits">'.View::icon('activity',15)
            .'Credit ledger</a><a class="btn sm" href="/admin/licenses">'.View::icon('key',15)
            .'Generate key</a></div></section>';

        $referralCard='';
        if ($personal) {
            $referralCard='<section class="card"><div class="card-head"><div><h2>Your referral link</h2>
                <p>Invite resellers into your network. Registrations are auto-approved.</p></div>'
                .View::icon('link',20).'</div><div class="secret">'
                .View::e(Referral::url((string)$personal['code'])).'</div>'
                .'<div class="btn-row" style="margin-top:14px">'
                .'<span class="badge info">'.(int)$personal['uses'].' signup(s)</span>'
                .'<a class="btn ghost sm" href="/admin/referrals">'.View::icon('link',15)
                .'Manage referrals</a></div></section>';
        } else {
            $referralCard='<section class="card"><div class="card-head"><div><h2>Referral access</h2>
                <p>Referral links are issued by your upline for staff accounts.</p></div></div>
                <p class="muted">Reseller accounts focus on key generation. Ask your upline for
                credits, keys or a role change.</p></section>';
        }

        $ledgerRows='';
        foreach ($ledger as $l) {
            $delta=(int)$l['delta'];
            $ledgerRows.='<tr><td class="dim small">'.View::e($l['created_at']).'</td>'
                .'<td><span class="badge '.($delta>0?'active':($delta<0?'disabled':'neutral')).'">'
                .($delta>0?'+':'').number_format($delta).'</span></td>'
                .'<td class="small">'.View::e($l['kind']).'</td>'
                .'<td class="small dim">'.View::e($l['note']).'</td></tr>';
        }
        if ($ledgerRows==='') $ledgerRows='<tr><td colspan="4"><div class="empty">'
            .View::icon('coin',24).'<div>No credit movements yet.</div></div></td></tr>';
        $ledgerCard='<section class="card"><div class="card-head"><div><h2>Recent credit activity</h2>
            <p>Newest 12 movements on this account.</p></div>'
            .'<a class="badge info" href="/admin/credits">Open ledger</a></div>'
            .'<div class="table-wrap"><table><thead><tr><th>UTC</th><th>Delta</th><th>Kind</th>'
            .'<th>Note</th></tr></thead><tbody>'.$ledgerRows.'</tbody></table></div></section>';

        $password='<section class="card"><div class="card-head"><div><h2>Change password</h2>
            <p>Current password required. Other sessions keep their lease until it expires.</p></div>'
            .'</div><form method="post" action="/admin/account/password">'.View::csrf()
            .View::field('Current password','current','','password','required autocomplete="current-password"')
            .View::field('New password','new','','password','required minlength="8" autocomplete="new-password"')
            .View::field('Confirm new password','confirm','','password','required minlength="8" autocomplete="new-password"')
            .'<button type="submit">'.View::icon('lock',16).'Update password</button></form></section>';

        View::page('My account','<div class="grid g-2">'.$profile.$creditCard.'</div>'
            .'<div class="grid g-2" style="margin-top:18px">'.$referralCard.$password.'</div>'
            .'<div style="margin-top:18px">'.$ledgerCard.'</div>',true,
            'Your profile, balance, referral link and password.',
            '<a class="btn ghost" href="/admin">'.View::icon('grid',16).'Overview</a>');
    }

    public static function password(): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        $v=Http::form();
        try {
            Auth::changePassword((string)($v['current']??''),(string)($v['new']??''),
                (string)($v['confirm']??''));
        } catch (Deny $e) {
            Auth::flash('Password not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/account');
        }
        Auth::flash('Password updated');
        Http::redirect('/admin/account');
    }

    private static function reason(string $code): string {
        return [
            'CURRENT_PASSWORD_WRONG'=>'current password is incorrect',
            'PASSWORD_MISMATCH'=>'the new passwords do not match',
            'PASSWORD_TOO_SHORT'=>'password must be at least 8 characters',
            'PASSWORD_TOO_LONG'=>'password is too long',
            'PASSWORD_TOO_WEAK'=>'password must not equal the username',
        ][$code]??str_replace('_',' ',strtolower($code));
    }
}
