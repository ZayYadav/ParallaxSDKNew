<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AccessController {
    public static function check(): never {
        if (Http::method()!=='POST') Http::fail('METHOD_NOT_ALLOWED',405);
        RateLimit::hit('license-ip:'.Crypto::ipHash(),60,60);
        $request=Validation::check(Http::form());
        try {
            $response=License::check($request);
            Http::json($response);
        } catch (Deny $e) {
            try { Audit::write('license_denied','sdk',null,$e->getMessage()); }
            catch (\Throwable $ignored) {}
            Http::fail($e->getMessage(),$e->status);
        }
    }
}
