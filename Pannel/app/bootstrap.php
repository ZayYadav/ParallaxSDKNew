<?php
declare(strict_types=1);
namespace TDCore\Panel;
spl_autoload_register(static function (string $name): void {
    $prefix = __NAMESPACE__ . '\\';
    if (!str_starts_with($name, $prefix)) return;
    $relative = substr($name, strlen($prefix));
    if (!preg_match('/^[A-Za-z][A-Za-z0-9]*$/D', $relative)) return;
    $file = __DIR__ . '/' . $relative . '.php';
    if (is_file($file)) require_once $file;
});
date_default_timezone_set('UTC');
ini_set('display_errors', '0');
ini_set('log_errors', '1');
if (PHP_SAPI !== 'cli') {
    header('X-Content-Type-Options: nosniff');
    header('X-Frame-Options: DENY');
    header('Referrer-Policy: no-referrer');
    header("Content-Security-Policy: default-src 'self'; script-src 'none'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; media-src 'self'; frame-ancestors 'none'; base-uri 'none'; object-src 'none'; form-action 'self'");
    if (($_SERVER['HTTPS'] ?? '') === 'on')
        header('Strict-Transport-Security: max-age=31536000');
}
set_exception_handler(static function (\Throwable $e): void {
    error_log('TDCore ' . get_class($e) . ': ' . $e->getMessage());
    Http::fail('SERVICE_UNAVAILABLE', 503);
});
