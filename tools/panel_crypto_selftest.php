<?php
declare(strict_types=1);
// Crypto compatibility self-test: ephemeral P-256 key, no production secrets.
if (!extension_loaded('openssl')) { fwrite(STDERR, "OpenSSL missing\n"); exit(1); }
$key = openssl_pkey_new(['private_key_type' => OPENSSL_KEYTYPE_EC,
                         'curve_name' => 'prime256v1']);
if (!$key) throw new RuntimeException('P-256 key generation failed');
$details = openssl_pkey_get_details($key);
if (!$details || ($details['ec']['curve_name'] ?? '') !== 'prime256v1') {
    throw new RuntimeException('Unexpected EC curve');
}
$pub = openssl_pkey_get_public($details['key']);
$fields = ['tdcore-policy-v1', '101', str_repeat('A',32), 'com.example.app',
           str_repeat('B',64), str_repeat('C',32), (string)time(),
           (string)(time()+60), '7', '1'];
$policy = implode("\n", $fields);
$signature = '';
if (!openssl_sign($policy, $signature, $key, OPENSSL_ALGO_SHA256)) {
    throw new RuntimeException('Sign failed');
}
if (openssl_verify($policy, $signature, $pub, OPENSSL_ALGO_SHA256) !== 1) {
    throw new RuntimeException('Valid signature rejected');
}
if (openssl_verify($policy.'tampered', $signature, $pub, OPENSSL_ALGO_SHA256) !== 0) {
    throw new RuntimeException('Tampered signature accepted');
}
echo "ECDSA P-256 SHA-256 signature self-test passed\n";
