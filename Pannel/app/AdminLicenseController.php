<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminLicenseController {
    private static function selectors(array $v): string {
        return '<div class="grid g-2">'.
        View::select('package_rule',(string)($v['package_rule']??'required'),[
            'required'=>'Strict: require exact package',
            'if_present'=>'Optional: enforce if package is configured',
            'disabled'=>'Off: do not bind package'], 'Package rule').
        View::select('signer_rule',(string)($v['signer_rule']??'required'),[
            'required'=>'Strict: require exact signing SHA-256',
            'if_present'=>'Optional: enforce if signer is configured',
            'disabled'=>'Off: no signer restriction'], 'Signing rule').
        View::select('device_rule',(string)($v['device_rule']??'bind'),[
            'bind'=>'Strict: bind and cap devices',
            'unrestricted'=>'Optional: allow multiple devices (blacklist still applies)'], 'Device rule').
        View::field('Android package','package_name',$v['package_name']??'','text','maxlength="180" placeholder="com.example.app"').
        View::field('APK signing SHA-256','signing_sha256',$v['signing_sha256']??'','text','maxlength="64" placeholder="64 hex characters"').
        View::field('Max bound devices','max_devices',$v['max_devices']??1,'number','min="1" max="1000"').
        View::field('Requests per minute','requests_per_minute',$v['requests_per_minute']??30,'number','min="1" max="600"').
        '</div>'.View::featureChoices((int)($v['feature_mask']??0)).
        '<p class="muted small">For strict mode, enter an allowed package and signer fingerprint.
        Disabling a restriction reduces assurance. Expiry, signature checks, nonce binding and
        online authorization always stay mandatory.</p>';
    }

    public static function page(): never {
        Auth::requireAdmin();
        $me=Auth::current() ?: [];
        $actorId=(int)($me['id']??0);
        $staff=Account::isStaff();
        $licenses=License::listFor($actorId);
        $new=(string)($_SESSION['new_td_key']??''); unset($_SESSION['new_td_key']);
        $newCard=$new!==''?'<section class="card"><div class="card-head"><div><h2>New license key</h2>
            <p>This value is shown once and is not stored in plaintext. Copy it now.</p></div>
            <span class="badge active">issued</span></div><div class="secret">'.View::e($new).'</div>
            </section>':'';
        $active=$expiring=0;
        foreach ($licenses as $l) {
            if ($l['status']==='active') $active++;
            $exp=strtotime((string)$l['expires_at'].' UTC');
            if ($l['status']==='active' && $exp!==false && $exp>time() && $exp<=time()+7*86400) $expiring++;
        }
        $cost=Credit::keyCost();
        $unlimited=Account::isUnlimited($actorId);
        $balance=(int)($me['credit_balance']??0);
        $creditLine=$unlimited
            ? '<span class="badge active">unlimited credit</span>'
            : '<span class="badge '.(($cost>0 && $balance<$cost)?'revoked':'info').'">'.
              number_format($balance).' credits · '.($cost>0?number_format($cost).' per key':'free keys').'</span>';
        $stats='<div class="grid g-3">'
            .View::stat('Issued licenses',count($licenses),'Newest 150 in your scope','key')
            .View::stat('Active records',$active,'Ready to authorize SDKs','shield','ok')
            .View::stat('Expiring in 7 days',$expiring,'Rotate or renew soon','clock',$expiring>0?'warn':'ok')
            .'</div>';

        $generate='<section class="card"><div class="card-head"><div><h2>Generate a license key</h2>
            <p>A fresh 256-bit key with its own server-side enforcement policy.</p></div>
            '.$creditLine.'</div>
            <form method="post" action="/admin/licenses">'.View::csrf()
            .'<div class="grid g-2">'.View::field('Customer label','label','','text',
                'maxlength="120" placeholder="e.g. Ravi · yearly"')
            .View::field('Duration in days','days',30,'number','min="1" max="3650"').'</div>'
            .self::selectors([])
            .'<div class="btn-row" style="margin-top:16px"><button type="submit">'
            .View::icon('key',16).'Generate license key'.($cost>0?' · '.number_format($cost).' cr':'')
            .'</button></div></form></section>';

        $rows='';
        foreach ($licenses as $l) {
            $id=(int)$l['id'];
            $exp=strtotime((string)$l['expires_at'].' UTC');
            $days=$exp!==false?(int)floor(($exp-time())/86400):0;
            $expTone=$days<0?'revoked':($days<=7?'disabled':'neutral');
            $expLabel=$days<0?'expired':($days===0?'today':$days.' day(s) left');
            $status=(string)$l['status'];
            $policy='<span class="badge neutral">pkg '.View::e($l['package_rule']??'—').'</span> '
                .'<span class="badge neutral">sign '.View::e($l['signer_rule']??'—').'</span> '
                .'<span class="badge neutral">'.View::e($l['device_rule']??'—').'</span> '
                .'<span class="badge neutral">'.View::e($l['requests_per_minute']??30).'/min</span>';
            $owner=$staff && $l['owner_name']?'<p class="dim small">issuer: '.View::e($l['owner_name']).'</p>':'';
            $actions='';
            if ($staff) {
                $actions='<form method="post" action="/admin/licenses/'.$id.'/status">'.View::csrf()
                    .View::select('status',$status,['active'=>'Active','disabled'=>'Disabled','revoked'=>'Revoked'],
                        'License state')
                    .'<div class="btn-row"><button class="sm" type="submit">Update status</button></div></form>
                    <form method="post" action="/admin/licenses/'.$id.'/reset-devices" style="margin-top:6px">'
                    .View::csrf().'<button class="ghost sm" type="submit">Reset device binding</button></form>
                    <details><summary>Edit advanced enforcement</summary>
                    <form method="post" action="/admin/licenses/'.$id.'/policy">'.View::csrf()
                    .self::selectors($l)
                    .View::field('Expiry date UTC','expires_at',substr((string)$l['expires_at'],0,10),'date')
                    .'<button class="sm" type="submit">Save enforcement</button></form></details>';
            } else {
                $actions='<span class="badge info">read only</span>';
            }
            $rows.='<tr><td><strong>#'.$id.'</strong><p class="dim small">'.View::e($l['label']).'</p>'
                .$owner.'<div class="btn-row" style="margin-top:6px">'.View::maskChips((int)$l['feature_mask']).'</div></td>
                <td>'.View::badge($status).'</td>
                <td>'.View::e($l['device_count']).' / '.View::e($l['max_devices']).'</td>
                <td><span class="badge '.$expTone.'">'.$expLabel.'</span>
                  <p class="dim small">'.View::e($l['expires_at']).' UTC</p></td>
                <td><div class="btn-row" style="margin-bottom:8px">'.$policy.'</div>'.$actions.'</td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="5"><div class="empty">'.View::icon('key',30)
            .'<div>No licenses yet. Generate the first key above.</div></div></td></tr>';
        $list='<section class="card"><div class="card-head"><div><h2>Issued licenses</h2>
            <p>Keys are stored as HMAC digests; the plaintext value is shown only once.</p></div>
            <span class="badge info">'.count($licenses).' record(s)</span></div>
            <div class="table-wrap"><table><thead><tr><th>License</th><th>Status</th><th>Devices</th>
            <th>Expiry</th><th>Policy &amp; actions</th></tr></thead><tbody>'.$rows.'</tbody></table></div></section>';

        View::page('Licenses and keys',$stats.$newCard.$generate.$list,true,
            $staff?'Issue keys, tune per-key enforcement and manage signed feature masks.'
                  :'Generate keys, watch expiry and copy the one-time secret.',
            '<a class="btn ghost" href="/admin/credits">'.View::icon('coin',16).'Credits</a>'
            .($staff?'<a class="btn ghost" href="/admin/features">'.View::icon('chip',16).'Feature labels</a>':''));
    }

    public static function create(): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        $me=Auth::current() ?: [];
        try {
            $key=License::issue(Http::form(),(int)($me['id']??0));
        } catch (Deny $e) {
            Auth::flash('License not created: '.self::reason($e->getMessage()));
            Http::redirect('/admin/licenses');
        }
        Auth::session(); $_SESSION['new_td_key']=$key;
        Auth::flash('License issued. Copy the one-time key now.');
        Http::redirect('/admin/licenses');
    }

    public static function status(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $v=Http::form();
        $status=(string)($v['status']??'');
        try {
            License::status($id,$status,(int)(Auth::current()['id']??0));
        } catch (Deny $e) {
            Auth::flash('Status not changed: '.self::reason($e->getMessage()));
            Http::redirect('/admin/licenses');
        }
        Auth::flash('License status updated');
        Http::redirect('/admin/licenses');
    }

    public static function policy(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        try {
            License::rules($id,Http::form(),(int)(Auth::current()['id']??0));
        } catch (Deny $e) {
            Auth::flash('Policy not saved: '.self::reason($e->getMessage()));
            Http::redirect('/admin/licenses');
        }
        Auth::flash('Per-key policy updated');
        Http::redirect('/admin/licenses');
    }

    public static function resetDevices(int $id): never {
        Auth::requireStaff(); Auth::csrfCheck();
        try {
            License::findScoped($id,(int)(Auth::current()['id']??0));
            Device::reset($id);
        } catch (Deny $e) {
            Auth::flash('Device binding not reset: '.self::reason($e->getMessage()));
            Http::redirect('/admin/licenses');
        }
        Auth::flash('Device bindings cleared');
        Http::redirect('/admin/licenses');
    }

    private static function reason(string $code): string {
        return [
            'INSUFFICIENT_CREDIT'=>'not enough credits for this key',
            'LICENSE_FORBIDDEN'=>'that license is outside your network',
            'UNKNOWN_LICENSE'=>'license not found',
        ][$code]??$code;
    }
}
