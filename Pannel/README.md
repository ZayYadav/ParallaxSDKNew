# TDCore Modular PHP + MySQL Control Center

This is a new, **fail-closed online-license** implementation, not a drop-in
replacement for all 46 historical Node/Neon APIs. It provides a PHP admin
dashboard, SQL-backed license records, device quotas, package/signing-hash
restrictions, feature bitmasks, global maintenance, revocation and audit logs.
The SDK uses a 60-second server-signed P-256 ECDSA lease. No server private
key is distributed to the SDK.

## Requirements

PHP 8.2+ with PDO MySQL, OpenSSL, mbstring; MySQL 8+ or MariaDB 10.6+;
TLS / HTTPS; Android Gradle + NDK 27.2.12479018 for the AAR.

## Deploy on cPanel / Apache

1. Create a MySQL database/user with its own permissions. Import
   `Pannel/schema.sql` using phpMyAdmin. **Do not publish schema.sql.**
2. Point a **dedicated HTTPS subdomain document root** at the contents of
   `Pannel/`. PHP admin UI is `https://<your-panel-host>/admin`;
   API is `https://<your-panel-host>/connect` via .htaccess rewrite.
3. Configure server-side environment values based on `.env.example`.
   **The example .env file is not parsed by PHP.** Configure actual
   environment variables through trusted hosting configuration, not URL
   parameters or JavaScript. On cPanel without PHP environment controls,
   create `.tdcore-private.php` exactly **one directory above the Apache
   DOCUMENT_ROOT**. It must return an associative PHP array. Example:

   ```php
   <?php
   return [
       'TD_DB_HOST' => 'localhost',
       'TD_DB_PORT' => '3306',
       'TD_DB_NAME' => 'cpanel_db',
       'TD_DB_USER' => 'cpanel_user',
       'TD_DB_PASSWORD' => 'YOUR_DB_PASSWORD',
       'TD_LICENSE_PEPPER' => 'LONG_RANDOM_HEX',
       'TD_ADMIN_PASSWORD_HASH' => 'ARGON2ID_HASH',
       'TD_SIGNING_PRIVATE_KEY_FILE' => '/home/ACCOUNT/private/tdcore_signing.pem',
       'TD_FORCE_HTTPS' => '1'
   ];
   ```

   The real file must never be committed, uploaded into public_html,
   or served by the web server. Set its permissions to 600.
4. Generate signing key on the **server**, outside public_html (keep mode
   600; use a dedicated account, not world-readable shared folders):

   ```sh
   openssl ecparam -genkey -name prime256v1 -noout -out /home/ACCOUNT/private/tdcore_signing.pem
   chmod 600 /home/ACCOUNT/private/tdcore_signing.pem
   openssl pkey -in /home/ACCOUNT/private/tdcore_signing.pem -pubout -out /home/ACCOUNT/private/tdcore_public.pem
   openssl pkey -pubin -in /home/ACCOUNT/private/tdcore_public.pem -outform DER | base64 -w0
   ```
   The last output is the Base64 **X.509 DER public key** to compile in
   the Android SDK. Never embed the private key or database password in code.
5. Generate a random HMAC pepper and a password hash (keep both server-only):

   ```sh
   php -r 'echo bin2hex(random_bytes(32)), PHP_EOL;'
   php -r 'echo password_hash($argv[1], PASSWORD_ARGON2ID), PHP_EOL;' 'YOUR_LONG_ADMIN_PASSWORD'
   ```

6. Set `TD_DB_HOST`, `TD_DB_PORT`, `TD_DB_NAME`, `TD_DB_USER`,
   `TD_DB_PASSWORD`, `TD_LICENSE_PEPPER`, `TD_ADMIN_PASSWORD_HASH`,
   `TD_SIGNING_PRIVATE_KEY_FILE` and `TD_FORCE_HTTPS=1`.
   Production traffic must be HTTPS; do not use an untrusted TLS bypass.
7. Log in, enable licensing and turn off maintenance. Create a license,
   copy the **one-time** `TD-<64 hex>` key, then configure SDK with it.

## SDK build

In a local user-only Gradle properties file (not in Git):

```properties
tdLicenseUrl=https://YOUR-PANEL-HOST/connect
tdLicenseP256PublicKey=BASE64_PUBLIC_X509_DER
```

Build `./gradlew :TeamDarkCore:assembleRelease`. Loader integration must
set the issued license via `SdkLicenseGuard.setSdkKey(context, key)` and
must check `SdkLicenseGuard.isLicensed(context)` before privileged SDK
actions. The network check is performed on a worker but the synchronous
entry point blocks until completion; production apps should perform a
preflight check off the UI thread and gate access on its result.

The PHP `/connect` endpoint requires POST fields `user_key`, `serial`
(32-64 hex), `pkg`, `signing_sha256` (64 hex certificate SHA256), and a
fresh `nonce` (32 hex). It returns a Base64 canonical policy and ECDSA
signature, both verified inside `NativePolicyVerifier.cpp`. The API has
**no offline fallback**. Unconfigured endpoint/public key, missing native
library, invalid signature, bad cert/device/nonce, expired lease, revoked
key or server downtime always deny authorization.

## Threat model and limitations

- ECDSA signature verification provides response integrity/authenticity;
  HTTPS gives transport confidentiality. No extra custom encryption scheme.
- License IDs and device fingerprints are HMAC-hashed at rest with a server
  pepper; random license keys are revealed once on creation.
- The panel can revoke keys, block fingerprints, constrain devices/package/
  signer, disable globally and change policy revision. Revocation takes
  effect on the next live check; a signed lease is valid for at most 60s.
- A modified Android app can patch its own code. **No purely client-side
  protection is uncrackable**. Put sensitive operations/data behind
  server authorization; do not depend solely on local Java or C++ checks.
- The old Node/Neon panel's client portal, crash telemetry, plugin-hosting
  options and 46 legacy REST endpoints have not been migrated to this
  version. Existing Neon data also requires a separately tested export/import
  mapping. Do not retire the old deployment before validating this PHP
  replacement in staging with realistic license/device flows.
- Signing-key rotation requires a client build with the new public key.
  Protect the original private key and perform backups/controlled rotations.

## Modular architecture (23 PHP files, including front controller and CI router)

All public URLs are extensionless and handled through the `index.php` front
controller; PHP classes in `Pannel/app/` are not directly web-accessible.
The `router.php` is exclusively for the PHP CLI development server.
Do not deploy `LegacyNodePanel/` into the PHP document root.

**Routes:** `GET /admin`, `GET|POST /admin/login`,
`POST /admin/logout`, `GET|POST /admin/licenses`,
`POST /admin/licenses/{id}/status`,
`POST /admin/licenses/{id}/policy`,
`POST /admin/licenses/{id}/reset-devices`,
`GET|POST /admin/settings`, `GET /admin/devices`,
`POST /admin/devices/block`, `POST /admin/devices/unblock`,
`GET /admin/audit`, `POST /connect` and
`POST /api/v1/license/check`, plus `GET /health`.
`/connect` is retained for the existing Android SDK URL.

**Per-key enforcement selection** (the administrator chooses explicit modes):
- Package rule: `required` (strict exact match), `if_present` (optional
  strict match when configured), `disabled` (no package binding).
- Signing certificate: same strict/optional/disabled choices.
- Device rule: `bind` with maximum devices or `unrestricted`.
  Device blacklist continues to apply regardless of mode.
- Per-key request rate limit, feature-mask bits, expiry, global maintenance,
  key revocation, one-time license key generation and security audit.

**Hard requirements that cannot be switched off per license:** an active,
unexpired key; HTTPS; fresh nonce; verified ECDSA P-256 lease signed on the
server; 60-second lease; online check and failure-closed behavior. All admin
mutations require an authenticated session and CSRF token. IPs and device
bindings are stored only as keyed HMAC-SHA256 digests.

The license **feature mask** is signed in the policy. Native SDK APIs expose
the verified mask, but a particular SDK operation is controlled by a bit
only when the corresponding SDK entry point explicitly checks that bit.
Do not treat enabling a checkbox as magic control of every Android method.
New strict-mode keys require explicit Android package and certificate SHA-256.

On upgrade, re-import `schema.sql` to create the three new tables.
Existing license records without `td_license_rules` retain compatibility:
their configured package and signer are enforced if present, and device binding
remains enabled. Newly issued keys use the administrator-selected rules.

### Local endpoint smoke test

```sh
php -S 127.0.0.1:8091 -t Pannel Pannel/router.php
```

Then inspect `/health` and `/admin/login`. This development router must
never be used for production: use Apache HTTPS with `Pannel/.htaccess`
and a trusted PHP handler. Protect the server private key and configuration
outside the web root.
