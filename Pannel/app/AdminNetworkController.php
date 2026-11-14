<?php
declare(strict_types=1);
namespace TDCore\Panel;
/**
 * Owner/admin network management: accounts, credits and referral links.
 * Resellers only see their own account, balance and licence scope.
 */
final class AdminNetworkController {
    /* ------------------------------------------------------------------ accounts */
    public static function accounts(): never {
        Auth::requireStaff();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $owner=Account::isOwner();
        $accounts=Account::visibleAccounts();
        $createRoles=[];
        if (Account::canCreateRole($actorId,'reseller')) $createRoles['reseller']='Reseller — generate keys only';
        if (Account::canCreateRole($actorId,'admin')) $createRoles['admin']='Admin — manage downline and credits';

        $stats='<div class="grid g-4">'
            .View::stat('Visible accounts',count($accounts),
                $owner?'Entire network':'You and your downline','users')
            .View::stat('Admins',self::countRole($accounts,'admin'),'Can manage downline','shield')
            .View::stat('Resellers',self::countRole($accounts,'reseller'),'Key generation only','key')
            .View::stat('Credits in network',number_format(Credit::totalInCirculation()),
                'Excludes unlimited accounts','coin','ok')
            .'</div>';

        $rows='';
        foreach ($accounts as $a) {
            $id=(int)$a['id'];
            $self=$id===$actorId;
            $parent='';
            if ((int)($a['parent_id']??0)>0) {
                $p=AdminUser::byId((int)$a['parent_id']);
                $parent=$p?'<p class="dim small">upline: '.View::e($p['username']).'</p>':'';
            }
            $name=trim((string)($a['display_name']??''));
            $actions='<span class="badge info">read only</span>';
            if ($self) {
                $actions='<span class="badge active">this is you</span>';
            } elseif (Account::canManage($actorId,$id)) {
                $actions='<form method="post" action="/admin/accounts/'.$id.'/toggle" style="display:inline">'
                    .View::csrf().'<button class="ghost sm" type="submit">'
                    .((int)$a['disabled']?'Enable':'Disable').'</button></form> '
                    .'<details style="display:inline-block;margin:0"><summary>Reset password</summary>'
                    .'<form method="post" action="/admin/accounts/'.$id.'/password">'.View::csrf()
                    .View::field('New password','new','','password',
                        'required minlength="8" autocomplete="new-password"')
                    .'<button class="sm" type="submit">Set password</button></form></details> '
                    .'<details style="display:inline-block;margin:0"><summary>Delete</summary>'
                    .'<form method="post" action="/admin/accounts/'.$id.'/delete">'.View::csrf()
                    .'<label class="small"><input type="checkbox" name="confirm" value="1" required>
                      confirm deletion</label><button class="danger sm" type="submit">Delete account</button>'
                    .'</form></details>';
            }
            $credit=Account::isUnlimited($id)
                ?'<span class="badge active">unlimited</span>'
                :'<span class="badge neutral">'.number_format((int)$a['credit_balance']).' cr</span>';
            $rows.='<tr><td><strong>'.View::e($a['username']).'</strong>'
                .($name!==''?'<p class="dim small">'.View::e($name).'</p>':'')
                .$parent.'</td>'
                .'<td>'.View::badge((string)$a['role']).'</td>'
                .'<td>'.$credit.'</td>'
                .'<td>'.((int)$a['disabled']?'<span class="badge revoked">disabled</span>'
                    :'<span class="badge active">active</span>').'</td>'
                .'<td class="dim small">'.View::e($a['last_login_at']??'never').'</td>'
                .'<td><div class="btn-row">'.$actions.'</div></td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="6"><div class="empty">'.View::icon('users',28)
            .'<div>No accounts in your scope yet.</div></div></td></tr>';

        $create='';
        if ($createRoles) {
            $parentField='';
            if ($owner) {
                $parentField=View::select('parent_id',(string)$actorId,Account::parentOptions(),
                    'Place under upline');
            } else {
                $parentField='<input type="hidden" name="parent_id" value="'.$actorId.'">'
                    .'<p class="muted small">New accounts join your downline automatically.</p>';
            }
            $create='<section class="card"><div class="card-head"><div><h2>Create an account</h2>
                <p>New accounts start with zero credits and no licence keys.</p></div>'
                .'<span class="badge info">staff action</span></div>'
                .'<form method="post" action="/admin/accounts">'.View::csrf()
                .'<div class="grid g-2">'
                .View::field('Display name','display_name','','text','maxlength="80" placeholder="Optional"')
                .View::field('Username','username','','text',
                    'required minlength="3" maxlength="60" autocomplete="off"')
                .View::field('Password','password','','password',
                    'required minlength="8" autocomplete="new-password"')
                .View::field('Confirm password','confirm','','password',
                    'required minlength="8" autocomplete="new-password"')
                .View::select('role',(string)array_key_first($createRoles),$createRoles,'Role')
                .'</div>'.$parentField
                .'<div class="btn-row" style="margin-top:16px"><button type="submit">'
                .View::icon('plus',16).'Create account</button>'
                .'<a class="btn ghost" href="/admin/referrals">'.View::icon('link',16)
                .'Invite with a link instead</a></div></form></section>';
        }

        $list='<section class="card"><div class="card-head"><div><h2>Network accounts</h2>
            <p>'.($owner?'Owner view: every account in the network.'
                :'Admin view: your account and everyone below you.').'</p></div>'
            .'<span class="badge neutral">'.count($accounts).' account(s)</span></div>'
            .'<div class="table-wrap"><table><thead><tr><th>Account</th><th>Role</th><th>Credit</th>'
            .'<th>Status</th><th>Last login (UTC)</th><th>Actions</th></tr></thead><tbody>'
            .$rows.'</tbody></table></div></section>';

        $rules='<section class="card"><div class="card-head"><div><h2>How the hierarchy works</h2>
            <p>Scope and credit rules are enforced server-side on every action.</p></div></div>
            <ul class="checklist">'
            .'<li><span class="dot ok">✓</span><span class="txt">Owner — unlimited credit, sees the whole network, can create admin and reseller links.</span></li>'
            .'<li><span class="dot ok">✓</span><span class="txt">Admin — sees its own downline, allocates only from its balance. Admin links need <code>can_refer_admins</code>.</span></li>'
            .'<li><span class="dot ok">✓</span><span class="txt">Reseller — sees only its own licences, spends credits to generate keys, cannot change policy.</span></li>'
            .'</ul></section>';

        View::page('Accounts',$stats.'<div style="margin-top:18px">'.$create.'</div>'
            .'<div style="margin-top:18px">'.$list.'</div>'
            .'<div style="margin-top:18px">'.$rules.'</div>',true,
            'Owner and admin hierarchy, roles and account lifecycle.',
            '<a class="btn ghost" href="/admin/credits">'.View::icon('coin',16).'Credits</a>'
            .'<a class="btn" href="/admin/referrals">'.View::icon('link',16).'Referrals</a>');
    }

    public static function createAccount(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $v=Http::form();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $role=(string)($v['role']??'reseller');
        $parentId=Account::isOwner()?(int)($v['parent_id']??$actorId):$actorId;
        try {
            if (!Account::canCreateRole($actorId,$role)) throw new Deny('ACCOUNT_FORBIDDEN',403);
            $parent=AdminUser::byId($parentId);
            if (!$parent || $parent['role']==='reseller') throw new Deny('PARENT_FORBIDDEN',403);
            if ((string)($v['password']??'')!==(string)($v['confirm']??''))
                throw new Deny('PASSWORD_MISMATCH',422);
            AdminUser::create((string)($v['username']??''),(string)($v['password']??''),
                $role,$parentId,(string)($v['display_name']??''));
        } catch (Deny $e) {
            Auth::flash('Account not created: '.self::reason($e->getMessage()));
            Http::redirect('/admin/accounts');
        }
        Auth::flash('Account created');
        Http::redirect('/admin/accounts');
    }

    public static function toggleAccount(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        if ($id<1 || $id===$actorId) {
            Auth::flash('You cannot disable your own account'); Http::redirect('/admin/accounts');
        }
        $target=AdminUser::byId($id);
        if (!$target) { Auth::flash('Account not found'); Http::redirect('/admin/accounts'); }
        if (!Account::canManage($actorId,$id)) {
            Auth::flash('That account is outside your permission scope'); Http::redirect('/admin/accounts');
        }
        $disable=(int)$target['disabled']===0;
        try { AdminUser::setDisabled($id,$disable); }
        catch (Deny $e) { Auth::flash(self::reason($e->getMessage())); Http::redirect('/admin/accounts'); }
        Auth::flash('Account '.($disable?'disabled':'enabled'));
        Http::redirect('/admin/accounts');
    }

    public static function deleteAccount(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $v=Http::form();
        if ($id<1 || $id===$actorId) {
            Auth::flash('You cannot delete your own account'); Http::redirect('/admin/accounts');
        }
        if (!Account::canManage($actorId,$id)) {
            Auth::flash('That account is outside your permission scope'); Http::redirect('/admin/accounts');
        }
        if (empty($v['confirm'])) {
            Auth::flash('Tick the confirmation box before deleting an account');
            Http::redirect('/admin/accounts');
        }
        try { AdminUser::delete($id); }
        catch (Deny $e) { Auth::flash(self::reason($e->getMessage())); Http::redirect('/admin/accounts'); }
        Auth::flash('Account deleted');
        Http::redirect('/admin/accounts');
    }

    public static function resetPassword(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        if ($id<1 || $id===$actorId || !Account::canManage($actorId,$id)) {
            Auth::flash('That account is outside your permission scope'); Http::redirect('/admin/accounts');
        }
        try { AdminUser::setPassword($id,(string)(Http::form()['new']??''),false); }
        catch (Deny $e) { Auth::flash('Password not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/accounts'); }
        Auth::flash('Password reset');
        Http::redirect('/admin/accounts');
    }

    /* ------------------------------------------------------------------- credits */
    public static function credits(): never {
        Auth::requireAdmin();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $staff=Account::isStaff();
        $unlimited=Account::isUnlimited($actorId);
        $balance=(int)($me['credit_balance']??0);
        $cost=Credit::keyCost();
        $targets=$staff?Account::targetOptions():[];
        $scope=Account::scopeIds($actorId);
        $ledger=Credit::recentFor($scope,80);

        $stats='<div class="grid g-4">'
            .View::stat('Your balance',$unlimited?'∞':number_format($balance),
                $unlimited?'Unlimited credit':'Available credits','coin','ok')
            .View::stat('Key cost',number_format($cost),'Deducted per generated key','key')
            .View::stat('Ledger entries',count($ledger),'Newest 80 in your scope','activity')
            .View::stat('Role',Account::roleLabel(Account::role()),
                $staff?'Credit allocation enabled':'Read-only balance','users')
            .'</div>';

        $grant='';
        if ($staff) {
            if ($targets) {
                $grant='<section class="card"><div class="card-head"><div><h2>Allocate credits</h2>
                    <p>'.($unlimited?'Owner credits are unlimited, so nothing is deducted from you.'
                        :'The amount is deducted from your balance immediately.').'</p></div>'
                    .($unlimited?'<span class="badge active">owner funded</span>'
                        :'<span class="badge neutral">'.number_format($balance).' cr available</span>')
                    .'</div><form method="post" action="/admin/credits/grant">'.View::csrf()
                    .'<div class="grid g-3">'
                    .View::select('target_id',(string)array_key_first($targets),$targets,'Send to')
                    .View::field('Amount','amount',10,'number','required min="1" max="1000000000"')
                    .View::field('Note','note','','text','maxlength="160" placeholder="Optional reference"')
                    .'</div><button type="submit">'.View::icon('coin',16).'Transfer credits</button>'
                    .'</form></section>';
            } else {
                $grant='<section class="card"><div class="empty">'.View::icon('users',28)
                    .'<div>No downline accounts yet. Create or invite someone first.</div>'
                    .'<div class="btn-row" style="justify-content:center;margin-top:14px">'
                    .'<a class="btn ghost" href="/admin/accounts">'.View::icon('plus',16)
                    .'Accounts</a></div></div></section>';
            }
        } else {
            $grant='<section class="card"><div class="card-head"><div><h2>Reseller credit policy</h2>
                <p>Credits arrive from your upline and are spent on key generation.</p></div></div>
                <dl class="kv"><dt>Balance</dt><dd>'.number_format($balance).' credits</dd>
                <dt>Per key</dt><dd>'.number_format($cost).' credits</dd>
                <dt>Keys available</dt><dd>'.($cost>0?floor($balance/$cost):'unlimited').'</dd></dl>'
                .'<p class="muted small" style="margin-top:14px">Ask your upline to top up this balance.
                You cannot mint credits yourself.</p></section>';
        }

        $rows='';
        foreach ($ledger as $l) {
            $delta=(int)$l['delta'];
            $sign=$delta>0?'+':'';
            $tone=$delta>0?'active':($delta<0?'disabled':'neutral');
            $counterparty=$delta>=0?('from '.($l['actor_name']??'system'))
                :('to '.($l['target_name']??$l['actor_name']??'account'));
            if ((int)$l['account_id']!==$actorId) {
                $counterparty='account: '.($l['target_name']??'#'.$l['account_id']);
            }
            $rows.='<tr><td class="dim small">'.View::e($l['created_at']).'</td>'
                .'<td><span class="badge '.$tone.'">'.$sign.number_format($delta).'</span></td>'
                .'<td>'.View::e($l['kind']).'</td><td class="small">'.View::e($counterparty).'</td>'
                .'<td class="small dim">'.View::e($l['note']).'</td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="5"><div class="empty">'.View::icon('coin',26)
            .'<div>No credit movements yet.</div></div></td></tr>';
        $list='<section class="card"><div class="card-head"><div><h2>Credit ledger</h2>
            <p>Every grant, spend and bonus is recorded with the actor.</p></div>'
            .'<span class="badge neutral">audit backed</span></div>'
            .'<div class="table-wrap"><table><thead><tr><th>UTC</th><th>Delta</th><th>Kind</th>'
            .'<th>Counterparty</th><th>Note</th></tr></thead><tbody>'.$rows.'</tbody></table></div></section>';

        $ownerTools='';
        if (Account::isOwner()) {
            $all=array_values(array_filter(AdminUser::all(),
                static fn(array $r): bool => $r['role']!=='owner'));
            $opts=[];
            foreach ($all as $r) {
                $opts[(int)$r['id']]=$r['username'].' · '.Account::roleLabel((string)$r['role'])
                    .' · '.(Account::isUnlimited((int)$r['id'])?'unlimited':'limited');
            }
            if ($opts) {
                $ownerTools='<section class="card"><div class="card-head"><div><h2>Owner credit controls</h2>
                    <p>Grant unlimited privilege or top up any account without spending your own balance.</p>'
                    .'</div><span class="badge active">owner</span></div>'
                    .'<form method="post" action="/admin/credits/unlimited">'.View::csrf()
                    .'<div class="grid g-3">'
                    .View::select('target_id',(string)array_key_first($opts),$opts,'Account')
                    .View::select('value','1',['1'=>'Grant unlimited credit','0'=>'Return to metered credits'],
                        'Mode')
                    .'</div><button class="ghost" type="submit">'.View::icon('shield',16)
                    .'Apply credit mode</button></form></section>';
            }
        }

        View::page('Credits',$stats.'<div class="grid g-2" style="margin-top:18px">'
            .$grant.($ownerTools!==''?$ownerTools:'<section class="card"><div class="card-head"><div>'
            .'<h2>Credit economy</h2><p>Configurable by the owner in global settings.</p></div></div>'
            .'<dl class="kv"><dt>Key cost</dt><dd>'.number_format($cost).' credits</dd>'
            .'<dt>Signup bonus</dt><dd>'.View::e(Settings::get()['signup_bonus']).' credits</dd>'
            .'<dt>Referral bonus</dt><dd>'.View::e(Settings::get()['referral_bonus']).' credits</dd>'
            .'<dt>Total in circulation</dt><dd>'.number_format(Credit::totalInCirculation()).'</dd>'
            .'</dl></section>').'</div>'
            .'<div style="margin-top:18px">'.$list.'</div>',true,
            'Balance, transfers and the immutable credit ledger.',
            '<a class="btn ghost" href="/admin/referrals">'.View::icon('link',16).'Referrals</a>'
            .(Account::isStaff()?'<a class="btn" href="/admin/accounts">'.View::icon('users',16)
                .'Accounts</a>':'<a class="btn" href="/admin/licenses">'.View::icon('key',16)
                .'Generate a key</a>'));
    }

    public static function grant(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $v=Http::form();
        $me=Auth::current() ?: [];
        try {
            $amount=Credit::amount($v['amount']??'');
            Credit::grant((int)($me['id']??0),(int)($v['target_id']??0),$amount,
                (string)($v['note']??''));
        } catch (Deny $e) {
            Auth::flash('Transfer failed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/credits');
        }
        Auth::flash('Credits transferred');
        Http::redirect('/admin/credits');
    }

    public static function unlimited(): never {
        Auth::requireOwner(); Auth::csrfCheck();
        $v=Http::form();
        $id=(int)($v['target_id']??0);
        $value=(string)($v['value']??'0')==='1';
        $target=AdminUser::byId($id);
        if (!$target || $target['role']==='owner') {
            Auth::flash('Choose a non-owner account'); Http::redirect('/admin/credits');
        }
        Database::run('UPDATE td_admins SET unlimited_credits=? WHERE id=?',[$value?1:0,$id]);
        Audit::write($value?'credit_unlimited_on':'credit_unlimited_off','panel',null,
            'account: '.$target['username']);
        Auth::flash('Credit mode updated for '.$target['username']);
        Http::redirect('/admin/credits');
    }

    /* ----------------------------------------------------------------- referrals */
    public static function referrals(): never {
        Auth::requireAdmin();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $staff=Account::isStaff();
        $owner=Account::isOwner();
        $codes=Referral::listFor($actorId);
        $signups=Referral::signupsFor($actorId,40);
        $personal=null;
        foreach ($codes as $c) {
            if ((int)$c['parent_id']===$actorId && (int)$c['created_by']===$actorId
                && $c['target_role']==='reseller') { $personal=$c; break; }
        }

        $roles=[];
        if (Account::canReferRole($actorId,'reseller')) $roles['reseller']='Reseller — can generate keys';
        if (Account::canReferRole($actorId,'admin')) $roles['admin']='Admin — can manage downline and credits';

        $create='';
        if ($staff && $roles) {
            $parentField='';
            if ($owner) {
                $parentField=View::select('parent_id',(string)$actorId,Account::parentOptions(),
                    'Attach signups under');
            } else {
                $parentField='<input type="hidden" name="parent_id" value="'.$actorId.'">';
            }
            $adminNote=$owner
                ?'<span class="badge active">owner: all link types</span>'
                :((int)$me['can_refer_admins']===1
                    ?'<span class="badge info">admin links permitted</span>'
                    :'<span class="badge neutral">reseller links only</span>');
            $create='<section class="card"><div class="card-head"><div><h2>Create a referral link</h2>
                <p>Anyone who registers through it joins under you with the chosen role.</p></div>'
                .$adminNote.'</div><form method="post" action="/admin/referrals/create">'.View::csrf()
                .'<div class="grid g-3">'
                .View::select('target_role',(string)array_key_first($roles),$roles,'Signup role')
                .View::field('Max uses (0 = unlimited)','max_uses',0,'number','min="0" max="10000"')
                .View::field('Note','note','','text','maxlength="120" placeholder="Campaign or person"')
                .'</div>'.$parentField
                .'<button type="submit">'.View::icon('link',16).'Generate link</button></form>'
                .'<p class="muted small" style="margin:14px 0 0">Admin links require owner permission.
                Resellers cannot create referral links, they only generate licence keys.</p></section>';
        } else {
            $create='<section class="card"><div class="card-head"><div><h2>Referral links</h2>
                <p>Your upline owns link creation for this account.</p></div></div>
                <p class="muted">Reseller accounts cannot invite other accounts or mint credits.
                Use your credits to generate licence keys instead.</p>'
                .'<a class="btn ghost" href="/admin/licenses">'.View::icon('key',16)
                .'Go to licences</a></section>';
        }

        $personalCard='';
        if ($personal) {
            $url=Referral::url((string)$personal['code']);
            $personalCard='<section class="card"><div class="card-head"><div><h2>Your personal link</h2>
                <p>Share this to bring new resellers into your network.</p></div>'
                .View::badge('reseller').'</div><div class="secret">'.View::e($url).'</div>'
                .'<div class="btn-row" style="margin-top:14px">'
                .'<span class="badge neutral">'.(int)$personal['uses'].' use(s)</span>'
                .'<span class="badge '.((int)$personal['active']?'active':'revoked').'">'
                .((int)$personal['active']?'active':'paused').'</span>'
                .'<form method="post" action="/admin/referrals/'.(int)$personal['id'].'/toggle">'
                .View::csrf().'<button class="ghost sm" type="submit">'
                .((int)$personal['active']?'Pause link':'Enable link').'</button></form></div>'
                .'<p class="muted small" style="margin:12px 0 0">Copy the full URL into a message.
                Signups are auto-approved and logged in immediately.</p></section>';
        }

        $rows='';
        foreach ($codes as $c) {
            $url=Referral::url((string)$c['code']);
            $canToggle=$owner || (int)$c['created_by']===$actorId;
            $toggle=$canToggle?'<form method="post" action="/admin/referrals/'.(int)$c['id'].'/toggle">'
                .View::csrf().'<button class="ghost sm" type="submit">'
                .((int)$c['active']?'Pause':'Enable').'</button></form>':'';
            $rows.='<tr><td><strong>'.View::e($c['code']).'</strong>
                <p class="dim small">'.View::e($c['note']).'</p></td>'
                .'<td>'.View::badge((string)$c['target_role']).'</td>'
                .'<td class="small">'.View::e($c['parent_name']??'—').'</td>'
                .'<td>'.(int)$c['uses'].((int)$c['max_uses']>0?' / '.(int)$c['max_uses']:' / ∞').'</td>'
                .'<td>'.((int)$c['active']?'<span class="badge active">active</span>'
                    :'<span class="badge revoked">paused</span>').'</td>'
                .'<td><div class="secret small" style="padding:8px 10px">'.View::e($url).'</div></td>'
                .'<td><div class="btn-row">'.$toggle.'</div></td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="7"><div class="empty">'.View::icon('link',26)
            .'<div>No referral links yet.</div></div></td></tr>';
        $list='<section class="card"><div class="card-head"><div><h2>Links</h2>
            <p>Role-targeted codes with optional use limits.</p></div>'
            .'<span class="badge neutral">'.count($codes).' code(s)</span></div>'
            .'<div class="table-wrap"><table><thead><tr><th>Code</th><th>Role</th><th>Upline</th>'
            .'<th>Uses</th><th>Status</th><th>URL</th><th>Manage</th></tr></thead><tbody>'
            .$rows.'</tbody></table></div></section>';

        $signupRows='';
        foreach ($signups as $f) {
            $signupRows.='<tr><td><strong>'.View::e($f['username']??('account #'.$f['account_id'])).'</strong></td>'
                .'<td>'.View::badge((string)$f['role']).'</td>'
                .'<td class="mono small">'.View::e($f['code']??'—').'</td>'
                .'<td class="dim small">'.View::e($f['created_at']).'</td></tr>';
        }
        if ($signupRows==='') $signupRows='<tr><td colspan="4"><div class="empty">'.View::icon('users',24)
            .'<div>No signups through your links yet.</div></div></td></tr>';
        $signupCard='<section class="card"><div class="card-head"><div><h2>Referral signups</h2>
            <p>Newest accounts created through your network.</p></div></div>'
            .'<div class="table-wrap"><table><thead><tr><th>Account</th><th>Role</th><th>Code</th>'
            .'<th>Joined (UTC)</th></tr></thead><tbody>'.$signupRows.'</tbody></table></div></section>';

        $stats='<div class="grid g-4">'
            .View::stat('Your links',count($codes),'Active and paused','link')
            .View::stat('Signups tracked',count($signups),'Through your network','users','ok')
            .View::stat('Reseller link','✓',Account::canReferRole($actorId,'reseller')?'enabled':'locked','key')
            .View::stat('Admin link',Account::canReferRole($actorId,'admin')?'✓':'—',
                $owner?'owner permission':((int)$me['can_refer_admins']===1?'permitted':'owner approval needed'),
                'shield',Account::canReferRole($actorId,'admin')?'ok':'warn')
            .'</div>';

        View::page('Referrals',$stats
            .'<div class="grid g-2" style="margin-top:18px">'.$create.$personalCard.'</div>'
            .'<div style="margin-top:18px">'.$list.'</div>'
            .'<div style="margin-top:18px">'.$signupCard.'</div>',true,
            'Role-targeted invitation links and registration tracking.',
            '<a class="btn ghost" href="/admin/credits">'.View::icon('coin',16).'Credits</a>'
            .(Account::isStaff()?'<a class="btn" href="/admin/accounts">'.View::icon('users',16)
                .'Accounts</a>':''));
    }

    public static function createReferral(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $v=Http::form();
        $parentId=Account::isOwner()?(int)($v['parent_id']??$actorId):$actorId;
        try {
            Referral::create($actorId,$parentId,(string)($v['target_role']??'reseller'),
                (int)($v['max_uses']??0),(string)($v['note']??''));
        } catch (Deny $e) {
            Auth::flash('Link not created: '.self::reason($e->getMessage()));
            Http::redirect('/admin/referrals');
        }
        Auth::flash('Referral link created');
        Http::redirect('/admin/referrals');
    }

    public static function toggleReferral(int $id): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        $code=Database::one('SELECT * FROM td_referral_codes WHERE id=?',[$id]);
        if (!$code) { Auth::flash('Referral link not found'); Http::redirect('/admin/referrals'); }
        try {
            Referral::setActive((int)(Auth::current()['id']??0),$id,(int)$code['active']!==1);
        } catch (Deny $e) {
            Auth::flash('Link not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/referrals');
        }
        Auth::flash('Referral link updated');
        Http::redirect('/admin/referrals');
    }

    /* -------------------------------------------------------------------- helper */
    private static function countRole(array $rows,string $role): int {
        $n=0; foreach ($rows as $r) if ((string)$r['role']===$role) $n++;
        return $n;
    }

    private static function reason(string $code): string {
        return [
            'ACCOUNT_FORBIDDEN'=>'your role cannot create that account type',
            'PARENT_FORBIDDEN'=>'the chosen upline cannot own downline accounts',
            'REFERRAL_FORBIDDEN'=>'your role cannot create that referral type',
            'REFERRAL_NOT_FOUND'=>'referral link not found',
            'USERNAME_INVALID'=>'use 3-60 letters, numbers, dot, dash or underscore',
            'USERNAME_TAKEN'=>'that username already exists',
            'PASSWORD_TOO_SHORT'=>'password must be at least 8 characters',
            'PASSWORD_TOO_LONG'=>'password is too long',
            'PASSWORD_TOO_WEAK'=>'password must not equal the username',
            'PASSWORD_MISMATCH'=>'the passwords do not match',
            'CREDIT_FORBIDDEN'=>'you cannot send credits to that account',
            'INSUFFICIENT_CREDIT'=>'not enough credits for that transfer',
            'INVALID_AMOUNT'=>'enter a whole number between 1 and 1,000,000,000',
            'LAST_OWNER'=>'the last owner cannot be removed',
            'ADMIN_NOT_FOUND'=>'account not found',
            'RATE_LIMITED'=>'too many attempts, please wait',
        ][$code]??str_replace('_',' ',strtolower($code));
    }
}
