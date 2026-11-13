<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Crypto {
    public static function digest(string $text): string {
        return hash_hmac('sha256',$text,Config::need('TD_LICENSE_PEPPER'));
    }
    public static function issueKey(): string { return 'TD-'.strtoupper(bin2hex(random_bytes(32))); }
    public static function sign(array $fields): array {
        $path=Config::need('TD_SIGNING_PRIVATE_KEY_FILE');
        $pem=@file_get_contents($path);
        if ($pem===false) throw new \RuntimeException('Signing key unavailable');
        $private=openssl_pkey_get_private($pem);
        if (!$private) throw new \RuntimeException('Bad signing key');
        $details=openssl_pkey_get_details($private);
        if (!$details || $details['type']!==OPENSSL_KEYTYPE_EC ||
            ($details['ec']['curve_name']??'')!=='prime256v1')
            throw new \RuntimeException('P-256 signing key required');
        $payload=implode("\n",$fields); $sig='';
        if (!openssl_sign($payload,$sig,$private,OPENSSL_ALGO_SHA256))
            throw new \RuntimeException('Signing failed');
        return ['policy'=>base64_encode($payload), 'signature'=>base64_encode($sig)];
    }
    public static function ipHash(): string {
        return self::digest((string)($_SERVER['REMOTE_ADDR']??'unknown'));
    }
}
