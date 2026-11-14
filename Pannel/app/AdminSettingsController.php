<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminSettingsController {
    public static function page(): never {
        Auth::requireStaff();
        $s=Settings::get();
        $on=(int)$s['enabled']?' checked':'';
        $mt=(int)$s['maintenance']?' checked':'';
        $expiring=Settings::expiringSoon();
        $form='<form method="post" action="/admin/settings">'.View::csrf()
            .'<section class="card"><div class="card-head"><div><h2>Service state</h2>
              <p>These switches gate every SDK authorization request immediately.</p></div>'
              .((int)$s['enabled']?'<span class="badge active">live</span>':'<span class="badge revoked">disabled</span>')
              .'</div>'
            .'<label><input type="checkbox" name="enabled" value="1"'.$on.'>
              Enable issuing authorizations</label>'
            .'<label><input type="checkbox" name="maintenance" value="1"'.$mt.'>
              Maintenance mode: deny new authorizations</label>'
            .View::field('Public notice shown to the SDK','notice',$s['notice'],'text','maxlength="255"')
            .'</section>'
            .'<section class="card"><div class="card-head"><div><h2>Credit economy &amp; registration</h2>
              <p>Controls key pricing and owner-funded bonuses for referrals.</p></div>'
              .((int)$s['open_registration']?'<span class="badge active">registration open</span>'
                :'<span class="badge revoked">registration closed</span>').'</div>'
            .'<div class="grid g-3">'
            .View::field('Credits charged per key','key_cost',$s['key_cost'],'number',
                'min="0" max="100000" required')
            .View::field('Signup bonus credits','signup_bonus',$s['signup_bonus'],'number',
                'min="0" max="100000" required')
            .View::field('Referral bonus credits','referral_bonus',$s['referral_bonus'],'number',
                'min="0" max="100000" required')
            .'</div>'
            .'<label><input type="checkbox" name="open_registration" value="1"'
              .((int)$s['open_registration']?' checked':'').'>
              Allow public registration without a referral link</label>'
            .'<div class="grid g-2" style="margin:10px 0 0">'
            .'<label><input type="checkbox" name="auto_bind_package" value="1"'
              .((int)$s['auto_bind_package']?' checked':'').'>
              Auto-bind Android package from first authorized SDK</label>'
            .'<label><input type="checkbox" name="auto_bind_signature" value="1"'
              .((int)$s['auto_bind_signature']?' checked':'').'>
              Auto-bind signing SHA-256 from first authorized SDK</label>'
            .'</div>'
            .'<p class="muted small" style="margin:0">Auto-bind locks the package name and
              APK signing SHA-256 the first time an SDK authorizes. Later builds signed
              with a different key or package are denied. Key cost applies to every
              non-owner account; owner and unlimited accounts are never charged.</p>'
            .'</section>'
            .'<div class="card"><div class="btn-row"><button type="submit">'.View::icon('check',16)
            .'Save global policy</button><span class="muted small">Saving bumps the policy
            revision; signed leases expire within 60 seconds.</span></div></div></form>';

        $danger='<section class="card"><div class="card-head"><div><h2>Danger zone</h2>
            <p>Emergency controls. These actions are immediate and cannot be undone.</p></div>
            <span class="badge revoked">caution</span></div>
            <div class="grid g-2"><div><h3>Revoke every license</h3>
            <p class="muted small">Sets all non-revoked keys to <code>revoked</code> and bumps the
            policy revision. All SDKs are denied on their next check.</p></div>
            <form method="post" action="/admin/settings/revoke-all">'.View::csrf()
            .'<label><input type="checkbox" name="confirm" value="1" required>
            I understand that every client will be locked out</label>
            <button class="danger" type="submit">'.View::icon('alert',16).'Revoke all licenses</button>
            </form></div></section>';

        $snapshot='<section class="card"><div class="card-head"><div><h2>Policy snapshot</h2>
            <p>Read-only values used to sign each 60-second lease.</p></div></div>
            <dl class="kv">
            <dt>Status</dt><dd>'.((int)$s['enabled']?'Enabled':'Disabled').' · maintenance '
              .((int)$s['maintenance']?'ON':'OFF').'</dd>
            <dt>Policy revision</dt><dd>v'.View::e($s['policy_version']).'</dd>
            <dt>Expiring soon</dt><dd>'.$expiring.' license(s) in the next 7 days</dd>
            <dt>Key cost</dt><dd>'.number_format((int)$s['key_cost']).' credits</dd>
            <dt>Signup / referral</dt><dd>'.View::e($s['signup_bonus']).' / '
              .View::e($s['referral_bonus']).' credits</dd>
            <dt>Open registration</dt><dd>'.((int)$s['open_registration']?'Enabled':'Referral only').'</dd>
            <dt>Last change</dt><dd>'.View::e($s['updated_at']).' UTC</dd>
            </dl></section>';

        View::page('Global settings',$form.'<div class="grid g-2" style="margin-top:18px">'
            .$snapshot.$danger.'</div>',true,
            'Master switches and emergency controls.');
    }
    public static function update(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        try {
            Settings::update(Http::form());
        } catch (Deny $e) {
            Auth::flash('Could not save: '.$e->getMessage());
            Http::redirect('/admin/settings');
        }
        Auth::flash('Global policy saved and policy revision bumped');
        Http::redirect('/admin/settings');
    }
    public static function revokeAll(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $v=Http::form();
        if (empty($v['confirm'])) {
            Auth::flash('Tick the confirmation box to revoke every license');
            Http::redirect('/admin/settings');
        }
        $n=Settings::revokeAll();
        Auth::flash($n.' license(s) revoked. SDK checks will fail immediately.');
        Http::redirect('/admin/settings');
    }
}
