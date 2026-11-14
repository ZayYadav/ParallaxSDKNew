<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminDeviceController {
    public static function page(): never {
        Auth::requireStaff();
        $blocked=Device::listBlocked();
        $stats='<div class="grid g-3">'
            .View::stat('Blocked fingerprints',count($blocked),'Blacklist applies before issuing a lease','shield',
                count($blocked)>0?'warn':'ok')
            .View::stat('Bound device records',(int)(Database::one('SELECT COUNT(*) AS n FROM td_devices')['n']??0),
                'Unique license + device pairs','db')
            .View::stat('Device policy','strict','Server-side binding before signing','lock')
            .'</div>';
        $block='<section class="card"><div class="card-head"><div><h2>Block a device fingerprint</h2>
            <p>The SDK sends a 32-64 hex serial; the panel stores only its keyed hash.</p></div>
            '.View::icon('alert',20).'</div>
            <form method="post" action="/admin/devices/block">'.View::csrf()
            .'<div class="grid g-2">'.View::field('SDK fingerprint (32-64 hex)','device','','text',
                'required pattern="[A-Fa-f0-9]{32,64}" placeholder="A1B2…"')
            .View::field('Reason','reason','','text','maxlength="200" placeholder="Leak attempt, abuse, chargeback…"')
            .'</div><button class="danger" type="submit">'.View::icon('shield',16).'Block device</button></form></section>';
        $rows='';
        foreach ($blocked as $r) {
            $rows.='<tr><td><code>'.View::e($r['device_hash']).'</code></td>
                <td>'.View::e($r['reason']).'</td>
                <td class="dim small">'.View::e($r['created_at']).'</td>
                <td><form method="post" action="/admin/devices/unblock">'.View::csrf()
                .'<input type="hidden" name="device_hash" value="'.View::e($r['device_hash']).'">'
                .'<button class="ghost sm" type="submit">Unblock</button></form></td></tr>';
        }
        if ($rows==='') $rows='<tr><td colspan="4"><div class="empty">'.View::icon('shield',30)
            .'<div>No blocked devices. The blacklist is empty.</div></div></td></tr>';
        $list='<section class="card"><div class="card-head"><div><h2>Blocked fingerprints</h2>
            <p>Unblocking bumps the policy revision immediately.</p></div>
            <span class="badge info">'.count($blocked).' banned</span></div>
            <div class="table-wrap"><table><thead><tr><th>Hashed device identifier</th><th>Reason</th>
            <th>Time UTC</th><th>Action</th></tr></thead><tbody>'.$rows.'</tbody></table></div></section>';
        View::page('Device enforcement',$stats.$block.$list,true,
            'Blacklist SDK device fingerprints and inspect binding volume.',
            '<a class="btn ghost" href="/admin/licenses">'.View::icon('key',16).'License controls</a>');
    }
    public static function block(): never {
        Auth::requireStaff(); Auth::csrfCheck(); $v=Http::form();
        try { Device::block((string)($v['device']??''),(string)($v['reason']??'')); }
        catch (Deny $e) { Auth::flash('Device not blocked: '.$e->getMessage()); Http::redirect('/admin/devices'); }
        Auth::flash('Device blocked. Next SDK check will be denied.');
        Http::redirect('/admin/devices');
    }
    public static function unblock(): never {
        Auth::requireStaff(); Auth::csrfCheck(); $v=Http::form();
        try { Device::unblock((string)($v['device_hash']??'')); }
        catch (Deny $e) { Auth::flash('Device not unblocked: '.$e->getMessage()); Http::redirect('/admin/devices'); }
        Auth::flash('Device unblocked');
        Http::redirect('/admin/devices');
    }
}
