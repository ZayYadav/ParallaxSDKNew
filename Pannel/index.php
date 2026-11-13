<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';
try { td_https(); td_admin_session(); $db=td_db(); }
catch (Throwable $e) { http_response_code(503); exit('TDCore panel is not configured. Check server environment and database.'); }

function td_e(mixed $s): string { return htmlspecialchars((string)$s, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8'); }
function td_redirect(): never { header('Location: index.php', true, 303); exit; }
function td_flash(string $message): void { $_SESSION['notice']=$message; }

$action=(string)($_POST['action']??'');
if (($_SERVER['REQUEST_METHOD']??'')==='POST') {
    td_csrf_verify();
    try {
        if ($action==='login') {
            $ip=td_ip_hash();
            $check=$db->prepare('SELECT * FROM td_login_attempts WHERE ip_hash=?');
            $check->execute([$ip]); $attempt=$check->fetch();
            if ($attempt && $attempt['blocked_until'] && strtotime($attempt['blocked_until'].' UTC')>time()) {
                http_response_code(429); exit('Too many attempts. Try later.');
            }
            $expected=td_required('TD_ADMIN_PASSWORD_HASH');
            if (!password_verify((string)($_POST['password']??''),$expected)) {
                $db->prepare('INSERT INTO td_login_attempts(ip_hash, attempts, window_start, blocked_until)
                   VALUES(?,1,UTC_TIMESTAMP(),NULL)
                   ON DUPLICATE KEY UPDATE
                   attempts=IF(window_start<UTC_TIMESTAMP()-INTERVAL 15 MINUTE,1,attempts+1),
                   blocked_until=IF(attempts>=5,UTC_TIMESTAMP()+INTERVAL 15 MINUTE,NULL),
                   window_start=IF(window_start<UTC_TIMESTAMP()-INTERVAL 15 MINUTE,UTC_TIMESTAMP(),window_start)')
                   ->execute([$ip]);
                usleep(300000); td_flash('Invalid credentials'); td_redirect();
            }
            $db->prepare('DELETE FROM td_login_attempts WHERE ip_hash=?')->execute([$ip]);
            session_regenerate_id(true); $_SESSION['td_admin']=true;
            td_audit('admin_login','admin'); td_redirect();
        }
        td_admin_auth();
        if ($action==='logout') {
            $_SESSION=[]; session_destroy(); td_redirect();
        }
        if ($action==='settings') {
            $enabled=isset($_POST['enabled'])?1:0;
            $maintenance=isset($_POST['maintenance'])?1:0;
            $notice=mb_substr(trim((string)($_POST['notice']??'')),0,255);
            $default=filter_var($_POST['default_features']??'0', FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>0,'max_range'=>2147483647]]);
            if ($default===false) throw new RuntimeException('Invalid feature mask');
            $db->prepare('UPDATE td_settings SET enabled=?,maintenance=?,notice=?,default_features=?,policy_version=policy_version+1 WHERE id=1')
                ->execute([$enabled,$maintenance,$notice,$default]);
            td_audit('settings_update','admin',null,'enabled='.$enabled.' maintenance='.$maintenance);
            td_flash('Global settings updated');
        } elseif ($action==='issue') {
            $label=mb_substr(trim((string)($_POST['label']??'')),0,120);
            $days=filter_var($_POST['days']??'', FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>1,'max_range'=>3650]]);
            $max=filter_var($_POST['max_devices']??'', FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>1,'max_range'=>1000]]);
            $features=filter_var($_POST['feature_mask']??'0', FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>0,'max_range'=>2147483647]]);
            $pkg=trim((string)($_POST['package_name']??''));
            $cert=strtoupper(trim((string)($_POST['signing_sha256']??'')));
            if ($days===false||$max===false||$features===false||
                ($pkg!==''&&!preg_match('/^[A-Za-z][A-Za-z0-9_.]{2,179}$/D',$pkg))||
                ($cert!==''&&!preg_match('/^[A-F0-9]{64}$/D',$cert))) throw new RuntimeException('Invalid license fields');
            $key='TD-'.strtoupper(bin2hex(random_bytes(32)));
            $stmt=$db->prepare('INSERT INTO td_licenses(key_digest,label,expires_at,max_devices,package_name,signing_sha256,feature_mask)
               VALUES(?,?,DATE_ADD(UTC_TIMESTAMP(), INTERVAL '.(int)$days.' DAY),?,?,?,?)');
            $stmt->execute([td_digest($key),$label,$max,$pkg,$cert,$features]);
            td_audit('license_created','admin',(int)$db->lastInsertId(),$label);
            $_SESSION['new_key']=$key;
            td_flash('License generated. Copy it now; it will not be shown again.');
        } elseif ($action==='license') {
            $id=filter_var($_POST['id']??'',FILTER_VALIDATE_INT);
            $status=(string)($_POST['status']??'');
            if (!$id||!in_array($status,['active','disabled','revoked'],true)) throw new RuntimeException('Invalid license');
            $stmt=$db->prepare('UPDATE td_licenses SET status=? WHERE id=?');
            $stmt->execute([$status,$id]);
            $db->exec('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
            td_audit('license_status','admin',$id,$status);
            td_flash('License updated');
        } elseif ($action==='edit_license') {
            $id=filter_var($_POST['id']??'',FILTER_VALIDATE_INT);
            $max=filter_var($_POST['max_devices']??'',FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>1,'max_range'=>1000]]);
            $features=filter_var($_POST['feature_mask']??'0',FILTER_VALIDATE_INT,
                ['options'=>['min_range'=>0,'max_range'=>2147483647]]);
            $pkg=trim((string)($_POST['package_name']??''));
            $cert=strtoupper(trim((string)($_POST['signing_sha256']??'')));
            $expiry=(string)($_POST['expires_at']??'');
            if (!$id||$max===false||$features===false||
                !preg_match('/^\d{4}-\d\d-\d\d$/D',$expiry)||
                ($pkg!==''&&!preg_match('/^[a-zA-Z][a-zA-Z0-9_.]{2,179}$/D',$pkg))||
                ($cert!==''&&!preg_match('/^[A-F0-9]{64}$/D',$cert))) throw new RuntimeException('Invalid update');
            $db->prepare('UPDATE td_licenses SET max_devices=?,feature_mask=?,package_name=?,signing_sha256=?,expires_at=? WHERE id=?')
               ->execute([$max,$features,$pkg,$cert,$expiry.' 23:59:59',$id]);
            $db->exec('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
            td_audit('license_edit','admin',$id);
            td_flash('Restrictions updated');
        } elseif ($action==='reset_devices') {
            $id=filter_var($_POST['id']??'',FILTER_VALIDATE_INT);
            if (!$id) throw new RuntimeException('Invalid id');
            $db->prepare('DELETE FROM td_devices WHERE license_id=?')->execute([$id]);
            $db->exec('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
            td_audit('devices_reset','admin',$id); td_flash('Device bindings reset');
        } elseif ($action==='unblock_device') {
            $hash=strtolower(trim((string)($_POST['device_hash']??'')));
            if (!preg_match('/^[a-f0-9]{64}$/D',$hash)) throw new RuntimeException('Invalid device hash');
            $db->prepare('DELETE FROM td_blocked_devices WHERE device_hash=?')->execute([$hash]);
            $db->exec('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
            td_audit('device_unblocked','admin'); td_flash('Device unblocked');
        } elseif ($action==='block_device') {
            $device=strtoupper(trim((string)($_POST['device']??'')));
            if (!preg_match('/^[A-F0-9]{32,64}$/D',$device)) throw new RuntimeException('Invalid device fingerprint');
            $reason=mb_substr(trim((string)($_POST['reason']??'')),0,200);
            $hash=td_digest('device:'.$device);
            $db->prepare('INSERT INTO td_blocked_devices(device_hash,reason) VALUES(?,?) ON DUPLICATE KEY UPDATE reason=VALUES(reason)')
                ->execute([$hash,$reason]);
            $db->exec('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
            td_audit('device_blocked','admin'); td_flash('Device blocked');
        } else {
            throw new RuntimeException('Unknown operation');
        }
    } catch (Throwable $e) {
        error_log('TDCore admin operation failed: '.get_class($e));
        td_flash('Could not save changes. Check field values or server logs.');
    }
    td_redirect();
}
$isAdmin=!empty($_SESSION['td_admin']);
$notice=(string)($_SESSION['notice']??'');unset($_SESSION['notice']);
$created=(string)($_SESSION['new_key']??'');unset($_SESSION['new_key']);
$csrf=td_csrf();
if ($isAdmin) {
    $settings=td_settings();
    $licenses=$db->query('SELECT l.*, (SELECT COUNT(*) FROM td_devices d WHERE d.license_id=l.id) AS device_count
                          FROM td_licenses l ORDER BY l.id DESC LIMIT 150')->fetchAll();
    $audits=$db->query('SELECT action, actor, license_id, details, created_at FROM td_audit ORDER BY id DESC LIMIT 25')->fetchAll();
    $blocked=$db->query('SELECT device_hash,reason,created_at FROM td_blocked_devices ORDER BY created_at DESC LIMIT 100')->fetchAll();
}
?>
<!DOCTYPE html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>TDCore Control Center</title>
<style>
:root{color-scheme:dark}*{box-sizing:border-box}body{background:#0c1119;color:#edf4fa;font:14px system-ui,sans-serif;margin:0;line-height:1.5}
header{background:#151e2a;border-bottom:1px solid #334054;padding:18px max(20px,calc((100vw - 1200px)/2))}
h1{font-size:24px;margin:0}h2{font-size:18px;margin:0 0 14px}main{max-width:1200px;margin:28px auto;padding:0 18px}
section{background:#151e2a;border:1px solid #334054;border-radius:13px;padding:22px;margin:22px 0}
label{display:block;margin:12px 0 3px;color:#c3d0df}input,select,textarea{width:100%;padding:10px;background:#0d1520;color:#fff;border:1px solid #415067;border-radius:7px;font:inherit}
button{background:#307dde;color:white;border:0;padding:10px 16px;border-radius:8px;font-weight:650;cursor:pointer;margin-top:12px}
button.danger{background:#a63f4f}button.muted{background:#33465d}.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(190px,1fr));gap:12px}
table{width:100%;border-collapse:collapse}td,th{text-align:left;border-bottom:1px solid #334054;padding:10px;vertical-align:top}th{color:#b8c9df}
small,.sub{color:#9db1c8}.msg{padding:12px;border:1px solid #526f8d;border-radius:8px;overflow-wrap:anywhere}
.inline{display:inline-block;margin-right:6px} .inline input{width:auto}.row{display:flex;flex-wrap:wrap;gap:9px;align-items:center}
code{overflow-wrap:anywhere} .key{padding:14px;background:#23364e;border-radius:8px;font:13px monospace}
@media(max-width:700px){table{display:block;overflow-x:auto}.grid{grid-template-columns:1fr}}
</style></head><body>
<header><h1>TDCore <span class="sub">/ PHP License Control Center</span></h1></header><main>
<?php if($notice!==''): ?><p class="msg"><?=td_e($notice)?></p><?php endif; ?>
<?php if(!$isAdmin): ?>
<section style="max-width:440px;margin:10vh auto"><h2>Administrator sign in</h2>
<form method="post"><input type="hidden" name="csrf" value="<?=td_e($csrf)?>">
<input type="hidden" name="action" value="login"><label for="pass">Password</label>
<input id="pass" type="password" name="password" autocomplete="current-password" required>
<button type="submit">Sign in</button></form></section>
<?php else: ?>
<form method="post" style="text-align:right"><input type="hidden" name="csrf" value="<?=td_e($csrf)?>">
<input type="hidden" name="action" value="logout"><button class="muted">Log out</button></form>
<?php if($created!==''): ?><section><h2>New license — copy now</h2>
<p class="key"><?=td_e($created)?></p><small>Only the keyed digest is stored on the server.</small></section><?php endif; ?>
<section><h2>Global authorization</h2><form method="post">
<input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="settings">
<div class="row"><label class="inline"><input type="checkbox" name="enabled" value="1" <?=((int)$settings['enabled']===1?'checked':'')?>> Enable authorization</label>
<label class="inline"><input type="checkbox" name="maintenance" value="1" <?=((int)$settings['maintenance']===1?'checked':'')?>> Maintenance / block all</label></div>
<div class="grid"><div><label>Notice</label><input name="notice" maxlength="255" value="<?=td_e($settings['notice'])?>"></div>
<div><label>Default feature mask (reserved)</label><input type="number" name="default_features" min="0" max="2147483647" value="<?=td_e($settings['default_features'])?>"></div></div>
<button type="submit">Save settings and increment policy version</button>
<p class="sub">Policy revision: <?=td_e($settings['policy_version'])?>. License checks are online-only with a signed 60-second policy.</p></form></section>
<section><h2>Generate license</h2><form method="post">
<input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="issue">
<div class="grid">
<div><label>Customer / note</label><input name="label" maxlength="120"></div>
<div><label>Validity (days)</label><input type="number" name="days" value="30" min="1" max="3650" required></div>
<div><label>Maximum devices</label><input type="number" name="max_devices" value="1" min="1" max="1000" required></div>
<div><label>Feature bitmask (0 = no optional features)</label><input type="number" name="feature_mask" value="0" min="0" max="2147483647"></div>
<div><label>Allowed Android package (blank = any)</label><input name="package_name" placeholder="com.example.app"></div>
<div><label>Signing certificate SHA-256 (optional)</label><input name="signing_sha256" maxlength="64" placeholder="64 hex characters"></div>
</div><button type="submit">Generate cryptographically random key</button></form></section>
<section><h2>Licenses</h2><p class="sub">Edit policies, revoke keys, reset device bindings. Changes apply to new online checks.</p>
<div style="overflow-x:auto"><table><thead><tr><th>ID / label</th><th>State</th><th>Devices</th><th>Expiry</th><th>Restrictions</th><th>Actions</th></tr></thead><tbody>
<?php foreach($licenses as $l): ?>
<tr><td>#<?=td_e($l['id'])?><p><?=td_e($l['label'])?></p></td>
<td><?=td_e($l['status'])?></td><td><?=td_e($l['device_count'])?> / <?=td_e($l['max_devices'])?></td>
<td><?=td_e($l['expires_at'])?> UTC</td>
<td><details><summary>Edit restrictions</summary><form method="post">
<input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="edit_license">
<input type="hidden" name="id" value="<?=td_e($l['id'])?>">
<label>Max devices</label><input name="max_devices" type="number" min="1" max="1000" value="<?=td_e($l['max_devices'])?>">
<label>Feature mask</label><input name="feature_mask" type="number" min="0" max="2147483647" value="<?=td_e($l['feature_mask'])?>">
<label>Package</label><input name="package_name" value="<?=td_e($l['package_name'])?>">
<label>Signing SHA-256</label><input name="signing_sha256" value="<?=td_e($l['signing_sha256'])?>">
<label>Expires (UTC date)</label><input name="expires_at" type="date" value="<?=td_e(substr($l['expires_at'],0,10))?>">
<button>Save</button></form></details></td>
<td><form method="post"><input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="license"><input type="hidden" name="id" value="<?=td_e($l['id'])?>">
<select name="status"><option value="active" <?=($l['status']==='active'?'selected':'')?>>Active</option>
<option value="disabled" <?=($l['status']==='disabled'?'selected':'')?>>Disabled</option>
<option value="revoked" <?=($l['status']==='revoked'?'selected':'')?>>Revoked</option></select><button>Change status</button></form>
<form method="post"><input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="reset_devices"><input type="hidden" name="id" value="<?=td_e($l['id'])?>"><button class="muted">Reset bindings</button></form></td></tr>
<?php endforeach; ?></tbody></table></div></section>
<section><h2>Block a device</h2><form method="post">
<input type="hidden" name="csrf" value="<?=td_e($csrf)?>"><input type="hidden" name="action" value="block_device">
<div class="grid"><div><label>SDK device fingerprint (32–64 hex)</label><input name="device" required></div>
<div><label>Reason</label><input name="reason" maxlength="200"></div></div>
<button class="danger">Block device</button></form>
<?php if(count($blocked)>0): ?><h3>Blocked fingerprints</h3>
<div style="overflow-x:auto"><table><thead><tr><th>Stored hash</th><th>Reason</th><th>Blocked UTC</th><th></th></tr></thead><tbody>
<?php foreach($blocked as $blockedDevice): ?><tr>
<td><code><?=td_e($blockedDevice['device_hash'])?></code></td>
<td><?=td_e($blockedDevice['reason'])?></td>
<td><?=td_e($blockedDevice['created_at'])?></td>
<td><form method="post">
<input type="hidden" name="csrf" value="<?=td_e($csrf)?>">
<input type="hidden" name="action" value="unblock_device">
<input type="hidden" name="device_hash" value="<?=td_e($blockedDevice['device_hash'])?>">
<button class="muted">Unblock</button></form></td></tr><?php endforeach; ?>
</tbody></table></div><?php endif; ?></section>
<section><h2>Recent audit activity</h2><div style="overflow-x:auto"><table>
<tr><th>UTC</th><th>Action</th><th>Actor</th><th>License ID</th><th>Detail</th></tr>
<?php foreach($audits as $e): ?><tr><td><?=td_e($e['created_at'])?></td><td><?=td_e($e['action'])?></td>
<td><?=td_e($e['actor'])?></td><td><?=td_e($e['license_id']??'')?></td><td><?=td_e($e['details'])?></td></tr><?php endforeach; ?>
</table></div></section>
<?php endif; ?></main></body></html>
