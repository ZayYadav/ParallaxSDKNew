<?php
declare(strict_types=1);
// Development and CI only: php -S 127.0.0.1:8091 -t Pannel Pannel/router.php
$path = parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
if (in_array($path, ['/favicon.ico', '/robots.txt'], true)) {
    http_response_code(404);
    return true;
}
// Public media assets only: no traversal, no source files.
if (preg_match('#^/assets/([A-Za-z0-9][A-Za-z0-9._-]*)$#', $path, $m)) {
    $base = realpath(__DIR__ . '/assets');
    $file = $base !== false ? realpath($base . '/' . $m[1]) : false;
    if ($file !== false && $base !== false
        && str_starts_with($file, $base . DIRECTORY_SEPARATOR)
        && is_file($file)
        && preg_match('/\.(?:mp4|webm|jpg|jpeg|png|webp|svg|css|js|ico)$/i', $file)) {
        return false; // let the built-in server stream it with its own MIME map
    }
    http_response_code(404);
    return true;
}
require __DIR__ . '/index.php';
