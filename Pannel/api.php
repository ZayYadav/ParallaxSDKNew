<?php
declare(strict_types=1);
require_once __DIR__ . '/bootstrap.php';

try {
    td_https();
    if (($_SERVER['REQUEST_METHOD'] ?? '') !== 'POST') {
        header('Allow: POST');
        td_deny('METHOD_NOT_ALLOWED', 405);
    }
    if ((int)($_SERVER['CONTENT_LENGTH'] ?? 0) > 8192) td_deny('REQUEST_TOO_LARGE', 413);

    $input = $_POST;
    if (str_contains((string)($_SERVER['CONTENT_TYPE'] ?? ''), 'application/json')) {
        $parsed = json_decode((string)file_get_contents('php://input'), true);
        if (!is_array($parsed)) td_deny('INVALID_JSON', 400);
        $input = $parsed;
    }

    $key = trim((string)($input['user_key'] ?? $input['license_key'] ?? ''));
    $device = strtoupper(trim((string)($input['serial'] ?? $input['device_id'] ?? '')));
    $pkg = trim((string)($input['pkg'] ?? $input['package_name'] ?? ''));
    $cert = strtoupper(trim((string)($input['signing_sha256'] ?? '')));
    $nonce = strtoupper(trim((string)($input['nonce'] ?? '')));

    if (!preg_match('/^TD-[A-F0-9]{64}$/D', $key)
        || !preg_match('/^[A-F0-9]{32,64}$/D', $device)
        || !preg_match('/^[a-zA-Z][a-zA-Z0-9_.]{2,179}$/D', $pkg)
        || !preg_match('/^[A-F0-9]{64}$/D', $cert)
        || !preg_match('/^[A-F0-9]{32}$/D', $nonce)) {
        td_deny('INVALID_REQUEST', 400);
    }

    $settings = td_settings();
    if (!(int)$settings['enabled'] || (int)$settings['maintenance']) td_deny('SERVICE_DISABLED');

    $db = td_db();
    $db->beginTransaction();
    try {
        $stmt = $db->prepare('SELECT * FROM td_licenses WHERE key_digest=? FOR UPDATE');
        $stmt->execute([td_digest($key)]);
        $license = $stmt->fetch();
        if (!$license) td_deny('INVALID_LICENSE', 401);
        if ($license['status'] !== 'active') td_deny('LICENSE_DISABLED');
        $expiry = strtotime($license['expires_at'] . ' UTC');
        if (!$expiry || $expiry <= time()) td_deny('LICENSE_EXPIRED');
        if ($license['package_name'] !== '' && $license['package_name'] !== $pkg) td_deny('PACKAGE_MISMATCH');
        if ($license['signing_sha256'] !== '' && !hash_equals(strtoupper($license['signing_sha256']), $cert)) td_deny('SIGNER_MISMATCH');

        $deviceHash = td_digest('device:' . $device);
        $stmt = $db->prepare('SELECT 1 FROM td_blocked_devices WHERE device_hash=?');
        $stmt->execute([$deviceHash]);
        if ($stmt->fetchColumn()) td_deny('DEVICE_BLOCKED');

        $stmt = $db->prepare('SELECT id FROM td_devices WHERE license_id=? AND device_hash=?');
        $stmt->execute([$license['id'], $deviceHash]);
        if ($stmt->fetchColumn()) {
            $stmt = $db->prepare('UPDATE td_devices SET last_seen=UTC_TIMESTAMP() WHERE license_id=? AND device_hash=?');
            $stmt->execute([$license['id'], $deviceHash]);
        } else {
            $stmt = $db->prepare('SELECT COUNT(*) FROM td_devices WHERE license_id=?');
            $stmt->execute([$license['id']]);
            if ((int)$stmt->fetchColumn() >= (int)$license['max_devices']) td_deny('DEVICE_LIMIT');
            $stmt = $db->prepare('INSERT INTO td_devices(license_id,device_hash) VALUES (?,?)');
            $stmt->execute([$license['id'], $deviceHash]);
        }
        $db->commit();
    } catch (Throwable $e) {
        if ($db->inTransaction()) $db->rollBack();
        throw $e;
    }

    $issued = time();
    $validUntil = min($issued + 60, $expiry);
    // Exact ten-field canonical payload; signed on server and verified inside C++.
    $effectiveFeatures = (int)$license['feature_mask'] !== 0
        ? (int)$license['feature_mask'] : (int)$settings['default_features'];
    $policy = td_sign_policy([
        'tdcore-policy-v1', (string)$license['id'], $device, $pkg,
        $cert, $nonce, (string)$issued, (string)$validUntil,
        (string)$effectiveFeatures, (string)$settings['policy_version']
    ]);
    td_json([
        'status'=>true,
        'policy'=>$policy['policy'],
        'signature'=>$policy['signature'],
        'data'=>[
            'expires_at'=>$validUntil,
            'features'=>$effectiveFeatures,
            'notice'=>$settings['notice'],
            'policy_version'=>(int)$settings['policy_version']
        ]
    ]);
} catch (Throwable $e) {
    error_log('TDCore connect failed: ' . get_class($e) . ': ' . $e->getMessage());
    td_deny('SERVER_UNAVAILABLE', 503);
}
