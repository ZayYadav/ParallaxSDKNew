<?php
declare(strict_types=1);
// Development and CI only: php -S 127.0.0.1:8091 -t Pannel Pannel/router.php
$path = parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
if (in_array($path, ['/favicon.ico', '/robots.txt'], true)) {
    http_response_code(404);
    return true;
}
require __DIR__ . '/index.php';
