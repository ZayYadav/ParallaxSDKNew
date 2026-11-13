<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Config {
    private static ?array $secrets = null;
    public static function get(string $key, string $default = ''): string {
        $v = getenv($key);
        if ($v !== false && trim((string)$v) !== '') return trim((string)$v);
        if (self::$secrets === null) {
            $root = (string)($_SERVER['DOCUMENT_ROOT'] ?? dirname(__DIR__));
            $path = dirname(rtrim($root, '/')) . '/.tdcore-private.php';
            $loaded = is_file($path) ? require $path : [];
            self::$secrets = is_array($loaded) ? $loaded : [];
        }
        return array_key_exists($key, self::$secrets)
            ? trim((string)self::$secrets[$key]) : $default;
    }
    public static function need(string $key): string {
        $v = self::get($key);
        if ($v === '' || str_starts_with($v, 'REPLACE'))
            throw new \RuntimeException('Missing configuration: ' . $key);
        return $v;
    }
    public static function https(): void {
        if (self::get('TD_FORCE_HTTPS', '1') !== '1') return;
        if (($_SERVER['HTTPS'] ?? '') !== 'on') Http::fail('HTTPS_REQUIRED', 400);
        // Reverse proxies must set HTTPS securely at origin, never trust client X-Forwarded-Proto.
    }
}
