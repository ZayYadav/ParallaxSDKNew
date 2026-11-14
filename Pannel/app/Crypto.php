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
    /** Non-secret information about the active P-256 signing key. */
    public static function signingKeyInfo(): array {
        try {
            $path=Config::need('TD_SIGNING_PRIVATE_KEY_FILE');
            $pem=@file_get_contents($path);
            $private=$pem!==false?openssl_pkey_get_private($pem):false;
            if (!$private) throw new \RuntimeException('key unreadable');
            $d=openssl_pkey_get_details($private);
            if (!$d) throw new \RuntimeException('key details unavailable');
            $curve=(string)($d['ec']['curve_name']??'unknown');
            $der=self::publicKeyDer((string)($d['key']??''));
            $fp=$der!==''?trim(chunk_split(base64_encode(hash('sha256',$der,true)),4,' ')):'unavailable';
            $ok=($d['type']??0)===OPENSSL_KEYTYPE_EC && $curve==='prime256v1';
            return ['ok'=>$ok,
                'curve'=>$curve==='prime256v1'?'P-256':$curve,'bits'=>(int)($d['bits']??0),
                'fingerprint'=>$fp,'path'=>$path];
        } catch (\Throwable $e) {
            return ['ok'=>false,'curve'=>'unavailable','bits'=>0,'fingerprint'=>'unavailable','path'=>''];
        }
    }
    /** Base64 X.509 DER public key, exactly the value compiled into the SDK. */
    public static function publicKeyBase64(): string {
        return base64_encode(self::publicKeyDer(self::publicKeyPem()));
    }
    private static function publicKeyPem(): string {
        $path=Config::need('TD_SIGNING_PRIVATE_KEY_FILE');
        $pem=@file_get_contents($path);
        $private=$pem!==false?openssl_pkey_get_private($pem):false;
        if (!$private) throw new \RuntimeException('Signing key unavailable');
        $d=openssl_pkey_get_details($private);
        if (!$d) throw new \RuntimeException('Signing key details unavailable');
        return (string)($d['key']??'');
    }
    private static function publicKeyDer(string $pem): string {
        if (!preg_match('/-----BEGIN PUBLIC KEY-----(.+?)-----END PUBLIC KEY-----/s',$pem,$m))
            return '';
        $der=base64_decode(preg_replace('/\s+/','',$m[1])??'',true);
        return $der===false?'':$der;
    }
}
