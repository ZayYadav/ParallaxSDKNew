<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Routes {
    public static function dispatch(): never {
        Config::https();
        $r=new Router();
        $r->add('GET','/',static fn()=>Http::redirect('/admin'));
        $r->add('GET','/health',static fn()=>Http::json(['service'=>'tdcore-panel','status'=>'online']));
        $r->add('POST','/connect',static fn()=>AccessController::check());
        $r->add('POST','/api/v1/license/check',static fn()=>AccessController::check());
        $r->add('GET','/admin/login',static fn()=>AdminController::loginPage());
        $r->add('POST','/admin/login',static fn()=>AdminController::login());
        $r->add('POST','/admin/logout',static fn()=>AdminController::logout());
        $r->add('GET','/admin',static fn()=>AdminController::home());
        $r->add('GET','/admin/audit',static fn()=>AdminController::audit());
        $r->add('GET','/admin/settings',static fn()=>AdminSettingsController::page());
        $r->add('POST','/admin/settings',static fn()=>AdminSettingsController::update());
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
