<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminDeviceController {
    public static function page(): never {
        Auth::requireAdmin();
        $html='<section class="panel"><h2>Block a device fingerprint</h2><form method="post" action="/admin/devices/block">'.
            View::csrf().View::field('SDK fingerprint (32-64 hex)','device','','text','required pattern="[A-Fa-f0-9]{32,64}"').
            View::field('Reason','reason','','text','maxlength="200"').
            '<button class="danger">Block device</button></form></section>';
        $html.='<section class="panel"><h2>Blocked fingerprints</h2><div class="scroll"><table>
            <tr><th>Hashed device identifier</th><th>Reason</th><th>Time UTC</th><th>Action</th></tr>';
        foreach(Device::listBlocked() as $r) {
            $html.='<tr><td><code>'.View::e($r['device_hash']).'</code></td><td>'.View::e($r['reason']).
                '</td><td>'.View::e($r['created_at']).'</td><td><form method="post" action="/admin/devices/unblock">'.
                View::csrf().'<input type="hidden" name="device_hash" value="'.View::e($r['device_hash']).
                '"><button class="ghost">Unblock</button></form></td></tr>';
        }
        View::page('Device enforcement',$html.'</table></div></section>');
    }
    public static function block(): never {
        Auth::requireAdmin(); Auth::csrfCheck(); $v=Http::form();
        Device::block((string)($v['device']??''),(string)($v['reason']??''));
        Auth::flash('Device blocked'); Http::redirect('/admin/devices');
    }
    public static function unblock(): never {
        Auth::requireAdmin(); Auth::csrfCheck(); $v=Http::form();
        Device::unblock((string)($v['device_hash']??''));
        Auth::flash('Device unblocked'); Http::redirect('/admin/devices');
    }
}
