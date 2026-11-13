<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminSettingsController {
    public static function page(): never {
        Auth::requireAdmin(); $s=Settings::get();
        $on=(int)$s['enabled']?' checked':'';
        $mt=(int)$s['maintenance']?' checked':'';
        $html='<section class="panel"><h2>Global authorization policy</h2>
        <p class="muted">Changing settings increments policy revision. Already-signed
        leases expire after at most 60 seconds.</p>
        <form method="post" action="/admin/settings">'.View::csrf().
        '<label><input type="checkbox" name="enabled" value="1"'.$on.'> Enable issuing authorizations</label>
        <label><input type="checkbox" name="maintenance" value="1"'.$mt.'> Maintenance / deny new authorizations</label>'.
        View::field('Notice','notice',$s['notice'],'text','maxlength="255"').
        View::field('Default feature bitmask','default_features',$s['default_features'],'number','min="0" max="2147483647"')
        .'<p>Current policy version: '.View::e($s['policy_version']).'</p>
        <button type="submit">Save policy</button></form></section>';
        View::page('Global settings',$html);
    }
    public static function update(): never {
        Auth::requireAdmin(); Auth::csrfCheck();
        Settings::update(Http::form()); Auth::flash('Global controls saved');
        Http::redirect('/admin/settings');
    }
}
