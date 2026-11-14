<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Routes {
    public static function dispatch(): never {
        Config::https();
        Migration::ensure();
        $r=new Router();
        $r->add('GET','/',static fn()=>Http::redirect('/admin'));
        $r->add('GET','/health',static fn()=>Http::json(['service'=>'tdcore-panel','status'=>'online']));
        $r->add('GET','/connect',static fn()=>ConnectPageController::page());
        $r->add('POST','/connect',static fn()=>AccessController::check());
        $r->add('POST','/api/v1/license/check',static fn()=>AccessController::check());
        $r->add('GET','/register',static fn()=>RegisterController::page());
        $r->add('POST','/register',static fn()=>RegisterController::create());
        $r->add('GET','/admin/login',static fn()=>AdminController::loginPage());
        $r->add('POST','/admin/login',static fn()=>AdminController::login());
        $r->add('POST','/admin/logout',static fn()=>AdminController::logout());
        $r->add('GET','/admin',static fn()=>AdminController::home());
        $r->add('GET','/admin/audit',static fn()=>AdminController::audit());
        $r->add('GET','/admin/accounts',static fn()=>AdminNetworkController::accounts());
        $r->add('POST','/admin/accounts',static fn()=>AdminNetworkController::createAccount());
        $r->add('POST','/admin/accounts/:id/toggle',static fn($a)=>AdminNetworkController::toggleAccount($a['id']));
        $r->add('POST','/admin/accounts/:id/delete',static fn($a)=>AdminNetworkController::deleteAccount($a['id']));
        $r->add('POST','/admin/accounts/:id/password',static fn($a)=>AdminNetworkController::resetPassword($a['id']));
        $r->add('GET','/admin/credits',static fn()=>AdminNetworkController::credits());
        $r->add('POST','/admin/credits/grant',static fn()=>AdminNetworkController::grant());
        $r->add('POST','/admin/credits/unlimited',static fn()=>AdminNetworkController::unlimited());
        $r->add('GET','/admin/referrals',static fn()=>AdminNetworkController::referrals());
        $r->add('POST','/admin/referrals/create',static fn()=>AdminNetworkController::createReferral());
        $r->add('POST','/admin/referrals/:id/toggle',static fn($a)=>AdminNetworkController::toggleReferral($a['id']));
        $r->add('GET','/admin/account',static fn()=>AdminAccountController::page());
        $r->add('POST','/admin/account/password',static fn()=>AdminAccountController::password());
        $r->add('GET','/admin/settings',static fn()=>AdminSettingsController::page());
        $r->add('POST','/admin/settings',static fn()=>AdminSettingsController::update());
        $r->add('POST','/admin/settings/revoke-all',static fn()=>AdminSettingsController::revokeAll());
        $r->add('GET','/admin/security',static fn()=>AdminSecurityController::page());
        $r->add('POST','/admin/security/password',static fn()=>AdminSecurityController::changePassword());
        $r->add('POST','/admin/security/admins/create',static fn()=>AdminSecurityController::createAdmin());
        $r->add('POST','/admin/security/admins/toggle',static fn()=>AdminSecurityController::toggleAdmin());
        $r->add('POST','/admin/security/admins/delete',static fn()=>AdminSecurityController::deleteAdmin());
        $r->add('POST','/admin/security/admins/password',static fn()=>AdminSecurityController::resetAdminPassword());
        $r->add('GET','/admin/licenses',static fn()=>AdminLicenseController::page());
        $r->add('POST','/admin/licenses',static fn()=>AdminLicenseController::create());
        $r->add('POST','/admin/licenses/:id/status',static fn($a)=>AdminLicenseController::status($a['id']));
        $r->add('POST','/admin/licenses/:id/policy',static fn($a)=>AdminLicenseController::policy($a['id']));
        $r->add('POST','/admin/licenses/:id/reset-devices',static fn($a)=>AdminLicenseController::resetDevices($a['id']));
        $r->add('GET','/admin/devices',static fn()=>AdminDeviceController::page());
        $r->add('POST','/admin/devices/block',static fn()=>AdminDeviceController::block());
        $r->add('POST','/admin/devices/unblock',static fn()=>AdminDeviceController::unblock());
        try { $r->dispatch(); }
        catch (Deny $e) { Http::fail($e->getMessage(),$e->status); }
    }
}
