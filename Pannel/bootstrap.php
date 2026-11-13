<?php
declare(strict_types=1);
/** TDCore PHP panel primitives. All secrets come from server environment. */
ini_set('display_errors', '0');
ini_set('log_errors', '1');
date_default_timezone_set('UTC');

function td_env(string $key, string $default = ''): string {
    $value = getenv($key);
    return $value === false ? $default : trim((string)$value);
}
function td_required(string $key): string {
    $value = td_env($key);
    if ($value === '' || str_starts_with($value, 'REPLACE')) {
        throw new RuntimeException('Missing server configuration: ' . $key);
    }
    return $value;
}
function td_db(): PDO {
    static $pdo;
    if ($pdo instanceof PDO) return $pdo;
    $dsn = sprintf('mysql:host=%s;port=%d;dbname=%s;charset=utf8mb4',
        td_required('TD_DB_HOST'), (int)td_env('TD_DB_PORT', '3306'), td_required('TD_DB_NAME'));
    return $pdo = new PDO($dsn, td_required('TD_DB_USER'), td_required('TD_DB_PASSWORD'), [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES => false,
        PDO::ATTR_TIMEOUT => 5
    ]);
}
function td_json(array $body, int $status = 200): never {
    http_response_code($status);
    header('Content-Type: application/json; charset=utf-8');
    header('Cache-Control: no-store, private');
    header('X-Content-Type-Options: nosniff');
    echo json_encode($body, JSON_UNESCAPED_SLASHES | JSON_INVALID_UTF8_SUBSTITUTE);
    exit;
}
function td_deny(string $reason, int $status = 403): never {
    td_json(['status'=>false, 'reason'=>$reason, 'message'=>$reason], $status);
}
function td_https(): void {
    if (td_env('TD_FORCE_HTTPS','1') !== '1') return;
    // Reverse proxies must terminate HTTPS and configure HTTPS=on in the origin.
    if (($_SERVER['HTTPS'] ?? '') !== 'on') {
        throw new RuntimeException('HTTPS required; configure trusted proxy HTTPS correctly');
    }
}
function td_digest(string $value): string {
    return hash_hmac('sha256', $value, td_required('TD_LICENSE_PEPPER'));
}
function td_ip_hash(): string {
    // Do not trust arbitrary X-Forwarded-For header values.
    return td_digest((string)($_SERVER['REMOTE_ADDR'] ?? 'unknown'));
}
function td_audit(string $action, string $actor='system', ?int $id=null, string $details=''): void {
    $stmt = td_db()->prepare('INSERT INTO td_audit(action, actor, license_id, ip_hash, details) VALUES(?,?,?,?,?)');
    $stmt->execute([$action, $actor, $id, td_ip_hash(), mb_substr($details,0,500)]);
}
function td_settings(): array {
    $r = td_db()->query('SELECT * FROM td_settings WHERE id=1')->fetch();
    if (!$r) throw new RuntimeException('Database schema not initialized');
    return $r;
}
function td_private_key(): OpenSSLAsymmetricKey {
    $file = td_required('TD_SIGNING_PRIVATE_KEY_FILE');
    $source = @file_get_contents($file);
    if ($source === false) throw new RuntimeException('Signing key file unreadable');
    $key = openssl_pkey_get_private($source);
    if (!$key) throw new RuntimeException('Invalid signing key');
    $meta = openssl_pkey_get_details($key);
    if (!$meta || $meta['type'] !== OPENSSL_KEYTYPE_EC) {
        throw new RuntimeException('Signing key must be P-256 ECDSA');
    }
    return $key;
}
function td_sign_policy(array $fields): array {
    $plain = implode("\n", $fields);
    $signature = '';
    if (!openssl_sign($plain, $signature, td_private_key(), OPENSSL_ALGO_SHA256)) {
        throw new RuntimeException('Unable to sign license policy');
    }
    return ['policy'=>base64_encode($plain), 'signature'=>base64_encode($signature)];
}
function td_admin_session(): void {
    if (session_status() === PHP_SESSION_ACTIVE) return;
    session_name('TDCORE_ADMIN');
    session_set_cookie_params([
        'lifetime'=>0, 'path'=>'/', 'secure'=>td_env('TD_FORCE_HTTPS','1')==='1',
        'httponly'=>true, 'samesite'=>'Strict'
    ]);
    ini_set('session.use_strict_mode','1');
    session_start();
}
function td_admin_auth(): void {
    td_admin_session();
    if (empty($_SESSION['td_admin'])) {
        http_response_code(401);
        exit('Administrator login required');
    }
}
function td_csrf(): string {
    td_admin_session();
    if (empty($_SESSION['td_csrf'])) $_SESSION['td_csrf'] = bin2hex(random_bytes(32));
    return $_SESSION['td_csrf'];
}
function td_csrf_verify(): void {
    if (!hash_equals(td_csrf(), (string)($_POST['csrf'] ?? ''))) {
        http_response_code(403);
        exit('Invalid CSRF token');
    }
}
