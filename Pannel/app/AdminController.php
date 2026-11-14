<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminController {
    public static function home(): never {
        Auth::requireAdmin();
        $me=Auth::current() ?: [];
        if (Auth::isReseller()) self::resellerHome($me);

        $actorId=(int)($me['id']??0);
        $scope=Account::scopeIds($actorId);
        $licWhere=''; $licParams=[];
        if ($scope!==null) {
            if (!$scope) { $licWhere=' WHERE 1=0'; }
            else {
                $licWhere=' WHERE owner_account_id IN ('.implode(',',array_fill(0,count($scope),'?')).')';
                $licParams=$scope;
            }
        }
        $settings=Settings::get();
        $lic=Database::one("SELECT COUNT(*) AS total,
            COALESCE(SUM(status='active'),0) AS active,
            COALESCE(SUM(status='disabled'),0) AS disabled,
            COALESCE(SUM(status='revoked'),0) AS revoked,
            COALESCE(SUM(status='active' AND expires_at<=UTC_TIMESTAMP()),0) AS expired,
            COALESCE(SUM(status='active' AND expires_at>UTC_TIMESTAMP()
                AND expires_at<=UTC_TIMESTAMP()+INTERVAL 7 DAY),0) AS expiring
            FROM td_licenses".$licWhere,$licParams) ?: [];
        if ($scope===null) {
            $dev=Database::one('SELECT COUNT(*) AS bound, COUNT(DISTINCT license_id) AS keys_used
                FROM td_devices') ?: [];
        } else {
            $in=$scope?implode(',',array_fill(0,count($scope),'?')):'0';
            $dev=Database::one('SELECT COUNT(*) AS bound, COUNT(DISTINCT d.license_id) AS keys_used
                FROM td_devices d JOIN td_licenses l ON l.id=d.license_id
                WHERE l.owner_account_id IN ('.$in.')',$scope) ?: [];
        }
        $blocked=(int)(Database::one('SELECT COUNT(*) AS n FROM td_blocked_devices')['n']??0);
        $authWhere="action='license_authorized' AND created_at>=UTC_DATE()";
        $authParams=[];
        if ($scope!==null) {
            $in=$scope?implode(',',array_fill(0,count($scope),'?')):'0';
            $authWhere.=' AND license_id IN (SELECT id FROM td_licenses WHERE owner_account_id IN ('.$in.'))';
            $authParams=$scope;
        }
        $authToday=(int)(Database::one("SELECT COUNT(*) AS n FROM td_audit WHERE ".$authWhere,
            $authParams)['n']??0);
        $deniedToday=(int)(Database::one("SELECT COUNT(*) AS n FROM td_audit
            WHERE action='license_denied' AND created_at>=UTC_DATE()")['n']??0);
        $failed=Auth::failedLogins24h();
        $recent=Audit::recent(7);
        $series=self::series(14);
        $max=max(1,...array_values($series));
        $key=Crypto::signingKeyInfo();
        $network=count(Account::visibleAccounts());

        $checks=[
            ['TLS / HTTPS enforced',Config::get('TD_FORCE_HTTPS','1')==='1',25,
                'Locally this panel runs on HTTP. Enable TD_FORCE_HTTPS behind a TLS proxy.'],
            ['Licensing service enabled',(int)$settings['enabled']===1,20,
                'Disabled switches deny every /connect request.'],
            ['Maintenance mode off',(int)$settings['maintenance']===0,15,
                'Maintenance denies new authorizations.'],
            ['No failed admin logins (24h)',$failed===0,15,
                $failed.' failed attempt(s) recorded in the last 24 hours.'],
            ['More than one admin account',AdminUser::count()>1,10,
                'A second owner/admin prevents lock-out.'],
            ['ECDSA P-256 signing key healthy',$key['ok'],15,
                'Curve: '.$key['curve'].'.'],
        ];
        $score=0; $total=0;
        foreach ($checks as $c) { $total+=$c[2]; if ($c[1]) $score+=$c[2]; }
        $pct=$total>0 ? (int)round($score/$total*100) : 0;
        $ringClass=$pct>=80?'':($pct>=55?'warn':'bad');

        $stats='<div class="grid g-4">'
            .View::stat('Licenses in scope',$lic['total']??0,
                ((int)($lic['active']??0)).' active · '.((int)($lic['revoked']??0)).' revoked','key')
            .View::stat('Expiring in 7 days',$lic['expiring']??0,
                ((int)($lic['expired']??0)).' already expired','clock',((int)($lic['expiring']??0))>0?'warn':'ok')
            .View::stat('Bound devices',$dev['bound']??0,
                ((int)($dev['keys_used']??0)).' keys with devices','shield')
            .View::stat('Authorizations today',$authToday,
                $deniedToday.' denied · '.$failed.' login fail(s)','activity',
                $deniedToday>0?'warn':'ok')
            .'</div>';

        $bars='';
        foreach ($series as $day=>$count) {
            $h=$count>0?max(6,(int)round($count/$max*100)):3;
            $bars.='<span class="bar'.($count===0?' zero':'').'" style="height:'.$h.'%"
                title="'.View::e($day).' · '.$count.' authorization(s)"></span>';
        }
        $spark='<section class="card"><div class="card-head"><div><h2>Authorization traffic</h2>
            <p>ECDSA-signed leases issued per day (last 14 days).</p></div>
            <span class="badge info">'.View::e(array_sum($series)).' total</span></div>
            <div class="spark">'.$bars.'</div>
            <div class="spark-labels"><span>'.View::e(array_key_first($series)).'</span>
            <span>'.View::e(array_key_last($series)).'</span></div></section>';

        $list='';
        foreach ($checks as $c) {
            $list.='<li><span class="dot '.($c[1]?'ok':'no').'">'.($c[1]?'✓':'!').'</span>
                <span class="txt">'.View::e($c[0]).'<small>'.View::e($c[3]).'</small></span></li>';
        }
        $posture='<section class="card"><div class="card-head"><div><h2>Security posture</h2>
            <p>Weighted health score of the current deployment.</p></div>
            <span class="badge '.($pct>=80?'active':($pct>=55?'disabled':'revoked')).'">'.$pct.'%</span></div>
            <div class="btn-row" style="align-items:center;gap:20px;flex-wrap:nowrap">
            <div class="ring '.$ringClass.'" style="--p:'.$pct.'"><span>'.$pct.'<small>score</small></span></div>
            <ul class="checklist" style="flex:1;min-width:0">'.$list.'</ul></div>
            <div class="divider"></div>
            <a class="btn ghost sm" href="/admin/security">Open security center '.View::icon('arrow',14).'</a>
            </section>';

        $quick='<section class="card"><div class="card-head"><div><h2>Quick actions</h2>
            <p>Everything important is one tap away.</p></div></div><div class="quick">'
            .'<a href="/admin/licenses"><span class="chip">'.View::icon('plus',18).'</span>
              <span>Create license<small>Issue a signed key</small></span></a>'
            .'<a href="/admin/credits"><span class="chip">'.View::icon('coin',18).'</span>
              <span>Credits<small>Allocate from your balance</small></span></a>'
            .'<a href="/admin/referrals"><span class="chip">'.View::icon('link',18).'</span>
              <span>Referrals<small>Invite admin or reseller</small></span></a>'
            .'<a href="/admin/accounts"><span class="chip">'.View::icon('users',18).'</span>
              <span>Accounts<small>'.$network.' in your network</small></span></a>'
            .'</div></section>';

        $activity='';
        foreach ($recent as $r) {
            $activity.='<tr><td><span class="badge '.self::tone((string)$r['action']).'">'
                .View::e($r['action']).'</span></td><td>'.View::e($r['actor']).'</td>'
                .'<td class="dim small">'.View::e($r['created_at']).'</td></tr>';
        }
        if ($activity==='') $activity='<tr><td colspan="3"><div class="empty">'.View::icon('activity',26)
            .'<div>No activity recorded yet.</div></div></td></tr>';
        $recentCard='<section class="card"><div class="card-head"><div><h2>Recent activity</h2>
            <p>Latest security and licensing events.</p></div>
            <a class="badge info" href="/admin/audit">Full log</a></div>
            <div class="table-wrap"><table><thead><tr><th>Action</th><th>Actor</th><th>UTC</th></tr></thead>
            <tbody>'.$activity.'</tbody></table></div></section>';

        $status='<section class="card"><div class="card-head"><div><h2>System status</h2>
            <p>Live runtime and signing information.</p></div>
            <span class="badge active"><span class="dotmark"></span>online</span></div>
            <dl class="kv">
            <dt>Service</dt><dd>'.((int)$settings['enabled']?'Enabled':'Disabled')
              .' · maintenance '.((int)$settings['maintenance']?'ON':'OFF').'</dd>
            <dt>Policy revision</dt><dd>v'.View::e($settings['policy_version']).'</dd>
            <dt>Default flags</dt><dd>'.View::maskChips((int)$settings['default_features']).'</dd>
            <dt>Key cost</dt><dd>'.number_format(Credit::keyCost()).' credits per key</dd>
            <dt>Signing key</dt><dd><span class="badge '.($key['ok']?'active':'revoked').'">'
              .($key['ok']?'P-256 healthy':'problem').'</span> '.View::e($key['curve']).'</dd>
            <dt>Key fingerprint</dt><dd class="mono small">'.View::e($key['fingerprint']).'</dd>
            <dt>API endpoint</dt><dd><code>POST /connect</code> · lease 60s</dd>
            </dl></section>';

        $body=$stats.'<div class="grid g-2" style="margin-top:18px">'.$spark.$posture.'</div>'
            .'<div style="margin-top:18px">'.$quick.'</div>'
            .'<div class="grid g-2" style="margin-top:18px">'.$recentCard.$status.'</div>';
        View::page('Overview',$body,true,
            'A live control room for license keys, bound devices and signed SDK policies.',
            '<a class="btn" href="/admin/licenses">'.View::icon('plus',16).'New license</a>'
            .'<a class="btn ghost" href="/admin/credits">'.View::icon('coin',16).'Credits</a>');
    }

    /** Focused dashboard for reseller accounts: own keys, credits and referral scope. */
    private static function resellerHome(array $me): never {
        $id=(int)($me['id']??0);
        $licenses=License::listFor($id);
        $active=$expiring=0;
        foreach ($licenses as $l) {
            if ($l['status']==='active') $active++;
            $exp=strtotime((string)$l['expires_at'].' UTC');
            if ($l['status']==='active' && $exp!==false && $exp>time() && $exp<=time()+7*86400) $expiring++;
        }
        $unlimited=Account::isUnlimited($id);
        $balance=(int)($me['credit_balance']??0);
        $cost=Credit::keyCost();
        $codes=Referral::listFor($id);
        $personal=null;
        foreach ($codes as $c) {
            if ((int)$c['parent_id']===$id && (int)$c['created_by']===$id
                && $c['target_role']==='reseller') { $personal=$c; break; }
        }
        $signups=Referral::signupsFor($id,10);

        $stats='<div class="grid g-4">'
            .View::stat('My licenses',count($licenses),$active.' active','key')
            .View::stat('Expiring in 7 days',$expiring,'Renew before the lease ends','clock',$expiring>0?'warn':'ok')
            .View::stat('Credit balance',$unlimited?'∞':number_format($balance),
                $unlimited?'Unlimited credit':($cost>0?number_format($cost).' credit(s) per key':'free keys'),'coin','ok')
            .View::stat('Referral signups',count($signups),'Read-only network view','link')
            .'</div>';

        $quick='<section class="card"><div class="card-head"><div><h2>Quick actions</h2>
            <p>Your permitted workspace.</p></div></div><div class="quick">'
            .'<a href="/admin/licenses"><span class="chip">'.View::icon('key',18).'</span>
              <span>Generate a key<small>'.($cost>0?number_format($cost).' credits':'Free').'</small></span></a>'
            .'<a href="/admin/credits"><span class="chip">'.View::icon('coin',18).'</span>
              <span>Credits<small>Balance and ledger</small></span></a>'
            .'<a href="/admin/referrals"><span class="chip">'.View::icon('link',18).'</span>
              <span>Referrals<small>Your invite network</small></span></a>'
            .'<a href="/admin/account"><span class="chip">'.View::icon('user',18).'</span>
              <span>My account<small>Profile and password</small></span></a>'
            .'</div></section>';

        $rows='';
        foreach (array_slice($licenses,0,6) as $l) {
            $exp=strtotime((string)$l['expires_at'].' UTC');
            $days=$exp!==false?(int)floor(($exp-time())/86400):0;
            $rows.='<tr><td><strong>#'.(int)$l['id'].'</strong><p class="dim small">'
                .View::e($l['label']).'</p></td><td>'.View::badge((string)$l['status']).'</td>'
                .'<td>'.View::e($l['device_count']).' / '.View::e($l['max_devices']).'</td>'
                .'<td><span class="badge '.($days<0?'revoked':($days<=7?'disabled':'neutral')).'">'
                .($days<0?'expired':$days.' day(s) left').'</span></td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="4"><div class="empty">'.View::icon('key',26)
            .'<div>No keys yet. Generate your first licence.</div>'
            .'<div class="btn-row" style="justify-content:center;margin-top:14px">'
            .'<a class="btn sm" href="/admin/licenses">'.View::icon('plus',15).'Create license</a>'
            .'</div></div></td></tr>';
        $recent='<section class="card"><div class="card-head"><div><h2>Recent licenses</h2>
            <p>Newest keys in your account.</p></div><a class="badge info" href="/admin/licenses">Open</a>'
            .'</div><div class="table-wrap"><table><thead><tr><th>License</th><th>Status</th>'
            .'<th>Devices</th><th>Expiry</th></tr></thead><tbody>'.$rows.'</tbody></table></div></section>';

        $invite='';
        if ($personal) {
            $invite='<section class="card"><div class="card-head"><div><h2>Your referral link</h2>
                <p>Share it to invite new resellers. They join under your upline automatically.</p></div>'
                .View::icon('link',20).'</div><div class="secret">'
                .View::e(Referral::url((string)$personal['code'])).'</div>'
                .'<div class="btn-row" style="margin-top:14px">'
                .'<span class="badge info">'.(int)$personal['uses'].' use(s)</span>'
                .'<a class="btn ghost sm" href="/admin/referrals">'.View::icon('link',15)
                .'Referral page</a></div></section>';
        } else {
            $invite='<section class="card"><div class="card-head"><div><h2>Credit policy</h2>
                <p>Your upline funds credits; you spend them on keys.</p></div></div>
                <dl class="kv"><dt>Balance</dt><dd>'.($unlimited?'unlimited':number_format($balance).' credits').'</dd>
                <dt>Per key</dt><dd>'.number_format($cost).' credits</dd>
                <dt>Upline</dt><dd>'.View::e(self::parentName($me)).'</dd></dl>
                <p class="muted small" style="margin-top:14px">Reseller accounts cannot create
                referral links, change policy or mint credits.</p></section>';
        }

        View::page('Overview',$stats.'<div style="margin-top:18px">'.$quick.'</div>'
            .'<div class="grid g-2" style="margin-top:18px">'.$recent.$invite.'</div>',true,
            'Your keys, credits and invite network at a glance.',
            '<a class="btn" href="/admin/licenses">'.View::icon('plus',16).'New license</a>'
            .'<a class="btn ghost" href="/admin/credits">'.View::icon('coin',16).'Credits</a>');
    }

    private static function parentName(array $me): string {
        $pid=(int)($me['parent_id']??0);
        if ($pid<=0) return 'top level';
        $p=AdminUser::byId($pid);
        return $p?(string)$p['username']:'—';
    }

    private static function series(int $days): array {
        $rows=Database::all("SELECT DATE(created_at) AS d, COUNT(*) AS c FROM td_audit
            WHERE action='license_authorized' AND created_at>=UTC_TIMESTAMP()-INTERVAL ".($days-1)." DAY
            GROUP BY DATE(created_at)");
        $map=[];
        foreach ($rows as $r) $map[(string)$r['d']]=(int)$r['c'];
        $out=[];
        for ($i=$days-1;$i>=0;$i--) {
            $d=gmdate('Y-m-d',time()-$i*86400);
            $out[$d]=$map[$d]??0;
        }
        return $out;
    }
    private static function tone(string $action): string {
        if (str_contains($action,'denied') || str_contains($action,'disabled') || str_contains($action,'revoked'))
            return 'revoked';
        if (str_contains($action,'login') || str_contains($action,'authorized')) return 'active';
        return 'info';
    }
    public static function loginPage(): never {
        Auth::session();
        if (!empty($_SESSION['admin'])) Http::redirect('/admin');
        $expired=isset($_GET['expired']);
        $body='<section class="card auth-card"><div class="auth-head">
          <span class="brand"><span class="brand-mark">'.View::icon('logo',22).'</span>
          <span><span class="t">TDCore</span><small>Licensing control</small></span></span>
          <h1><span class="grad">Welcome back</span></h1>
          <p>Sign in with your account username and password.</p></div>'
          .($expired?'<div class="flash">'.View::icon('clock',18)
            .'<span>Session expired. Please sign in again.</span></div>':'')
          .'<form method="post" action="/admin/login">'.View::csrf()
          .View::field('Username','username','','text',
            'required autofocus autocomplete="username" maxlength="60" placeholder="admin"')
          .View::field('Password','password','','password',
            'required autocomplete="current-password" placeholder="••••••••••"')
          .'<button type="submit" style="width:100%;margin-top:4px">'
          .View::icon('lock',16).'Sign in securely</button></form>
          <div class="auth-split"><span></span><em>new here</em><span></span></div>
          <a class="btn ghost" style="width:100%" href="/register">'.View::icon('user',16)
          .'Create an account</a>
          <ul class="auth-bullets">
          <li>'.View::icon('shield',15).'Signed ECDSA P-256 policy leases</li>
          <li>'.View::icon('users',15).'Owner, admin and reseller roles</li>
          <li>'.View::icon('activity',15).'Login tracking and audit trail</li>
          </ul></section>';
        View::page('Sign in',$body,false,'','','/assets/tdcore.mp4','auth login');
    }
    public static function login(): never {
        Auth::csrfCheck();
        $data=Http::form();
        $username=(string)($data['username']??'');
        $password=(string)($data['password']??'');
        try {
            $ok=Auth::login($username,$password);
        } catch (Deny $e) {
            Auth::flash('Too many attempts. Please wait 15 minutes and try again.');
            Http::redirect('/admin/login');
        }
        if (!$ok) {
            Auth::flash('Invalid username or password');
            Http::redirect('/admin/login');
        }
        Http::redirect('/admin');
    }
    public static function logout(): never {
        Auth::csrfCheck(); Auth::requireAdmin(); Auth::logout(); Http::redirect('/admin/login');
    }
    public static function audit(): never {
        Auth::requireStaff();
        $rows=Audit::recent(100);
        $count=(int)(Database::one('SELECT COUNT(*) AS n FROM td_audit')['n']??0);
        $html='<div class="grid g-3">'
            .View::stat('Audit events',$count,'Stored security records','activity')
            .View::stat('Licenses authorized today',(int)(Database::one("SELECT COUNT(*) AS n FROM td_audit
                WHERE action='license_authorized' AND created_at>=UTC_DATE()")['n']??0),
                'Successful /connect checks','check','ok')
            .View::stat('Denied today',(int)(Database::one("SELECT COUNT(*) AS n FROM td_audit
                WHERE action='license_denied' AND created_at>=UTC_DATE()")['n']??0),
                'Rejected SDK requests','alert','warn').'</div>';
        $body='';
        foreach ($rows as $r) {
            $body.='<tr><td class="dim small">'.View::e($r['created_at']).'</td>
                <td><span class="badge '.self::tone((string)$r['action']).'">'.View::e($r['action']).'</span></td>
                <td>'.View::e($r['actor']).'</td>
                <td>'.($r['license_id']!==null?'#'.View::e($r['license_id']):'—').'</td>
                <td class="small">'.View::e($r['details']).'</td></tr>';
        }
        if ($body==='') $body='<tr><td colspan="5"><div class="empty">'.View::icon('activity',26)
            .'<div>No audit records yet.</div></div></td></tr>';
        $html.='<section class="card" style="margin-top:18px"><div class="card-head"><div>
            <h2>Audit trail</h2><p>Newest 100 events. Actor is the authenticated panel account or the SDK.</p>
            </div><a class="btn ghost sm" href="/admin/security">Security center</a></div>
            <div class="table-wrap"><table><thead><tr><th>UTC</th><th>Action</th><th>Actor</th>
            <th>License</th><th>Details</th></tr></thead><tbody>'.$body.'</tbody></table></div></section>';
        View::page('Audit log',$html,true,'Immutable security history for logins, keys, devices and policy changes.');
    }
}
