<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminLicenseController {
    private static function selectors(array $v): string {
        return '<div class="grid">'.
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
        View::field('Android package','package_name',$v['package_name']??'','text','maxlength="180"').
        View::field('APK signing SHA-256','signing_sha256',$v['signing_sha256']??'','text','maxlength="64"').
        View::field('Max bound devices','max_devices',$v['max_devices']??1,'number','min="1" max="1000"').
        View::field('Requests per minute','requests_per_minute',$v['requests_per_minute']??30,'number','min="1" max="600"').
        '</div>'.View::featureChoices((int)($v['feature_mask']??0)).
        '<p class="muted">For strict mode, enter an allowed package and signer fingerprint.
        Disabling a restriction reduces assurance. Expiry, signature checks, nonce binding and online authorization are always mandatory.</p>';
    }
    public static function page(): never {
        Auth::requireAdmin();
        $new=(string)($_SESSION['new_td_key']??''); unset($_SESSION['new_td_key']);
        $html=$new!==''?'<section class="panel"><h2>New license (shown once)</h2><div class="secret">'.View::e($new).'</div></section>':'';
        $html.='<section class="panel"><h2>Generate a license key</h2><form method="post" action="/admin/licenses">'.View::csrf().
            '<div class="grid">'.View::field('Customer label','label','','text','maxlength="120"').
            View::field('Duration in days','days',30,'number','min="1" max="3650"').'</div>'.
            self::selectors([]).'<button type="submit">Generate 256-bit license key</button></form></section>';
        $html.='<section class="panel"><h2>Issued licenses</h2><div class="scroll"><table><tr>
        <th>ID / label</th><th>Status</th><th>Devices</th><th>Expiry UTC</th><th>Policy / actions</th></tr>';
        foreach(License::list() as $l) {
            $id=(int)$l['id'];
            $html.='<tr><td>#'.View::e($id).'<p>'.View::e($l['label']).'</p></td><td>'.
                View::e($l['status']).'</td><td>'.View::e($l['device_count']).' / '.
                View::e($l['max_devices']).'</td><td>'.View::e($l['expires_at']).'</td><td>'.
                '<form method="post" action="/admin/licenses/'.$id.'/status">'.View::csrf().
                View::select('status',(string)$l['status'],[
                    'active'=>'Active','disabled'=>'Disabled','revoked'=>'Revoked'],'License state').
                '<button>Update status</button></form>'.
                '<form method="post" action="/admin/licenses/'.$id.'/reset-devices">'.View::csrf().
                '<button class="ghost">Reset device binding</button></form>'.
                '<details><summary>Edit advanced enforcement</summary><form method="post" action="/admin/licenses/'.$id.'/policy">'.
                View::csrf().self::selectors($l).
                View::field('Expiry date UTC','expires_at',substr((string)$l['expires_at'],0,10),'date').
                '<button>Save enforcement</button></form></details></td></tr>';
        }
        View::page('License and key controls',$html.'</table></div></section>');
    }
    public static function create(): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        $key=License::issue(Http::form()); Auth::session(); $_SESSION['new_td_key']=$key;
        Auth::flash('Key issued. Copy the one-time value.'); Http::redirect('/admin/licenses');
    }
    public static function status(int $id): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        $v=Http::form(); License::status($id,(string)($v['status']??''));
        Auth::flash('Status updated'); Http::redirect('/admin/licenses');
    }
    public static function policy(int $id): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        License::rules($id,Http::form());
        Auth::flash('Per-key policy updated'); Http::redirect('/admin/licenses');
    }
    public static function resetDevices(int $id): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        Device::reset($id); Auth::flash('Device bindings cleared'); Http::redirect('/admin/licenses');
    }
}
