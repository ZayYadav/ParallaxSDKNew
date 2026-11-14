<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Public registration through an owner/admin referral link. */
final class RegisterController {
    public static function page(): never {
        Auth::session();
        if (!empty($_SESSION['admin'])) Http::redirect('/admin');

        $raw=(string)($_GET['ref']??'');
        $ref=Register::code($raw);
        $settings=Settings::get();
        $open=(int)$settings['open_registration']===1;
        $invited=$raw!=='';
        $invalid=$invited && !$ref;

        $banner='';
        if ($invalid) {
            $banner='<div class="flash bad">'.View::icon('alert',18)
                .'<span>This referral link is invalid or has expired. Ask your upline for a new link.</span></div>';
        } elseif ($ref) {
            $parent=AdminUser::byId((int)$ref['parent_id']);
            $banner='<div class="invite">'.View::icon('users',18).'<span>You were invited by <strong>'
                .View::e($parent['username']??'TDCore').'</strong> as a '
                .View::e(Account::roleLabel((string)$ref['target_role'])).'.</span></div>';
        } elseif ($invited) {
            $banner='';
        }

        $form='';
        if ($invalid || (!$ref && !$open)) {
            $form='<div class="locked">'.View::icon('lock',30).'<h3>Registration is closed</h3>
                <p class="muted">A valid referral link is required to create an account right now.
                If you already have credentials, sign in instead.</p>
                <a class="btn ghost" href="/admin/login">'.View::icon('logout',16).'Go to sign in</a></div>';
        } else {
            $form='<form method="post" action="/register">'.View::csrf()
                .($ref?'<input type="hidden" name="ref" value="'.View::e($ref['code']).'">':'')
                .View::field('Display name','display_name','','text',
                    'maxlength="80" autocomplete="nickname" placeholder="Optional, shown in the panel"')
                .View::field('Username','username','','text',
                    'required minlength="3" maxlength="60" autocomplete="username"
                     placeholder="letters, numbers, dot, dash or underscore"')
                .'<div class="grid g-2">'
                .View::field('Password','password','','password',
                    'required minlength="8" autocomplete="new-password" placeholder="At least 8 characters"')
                .View::field('Confirm password','confirm','','password',
                    'required minlength="8" autocomplete="new-password" placeholder="Repeat password"')
                .'</div>'
                .'<label class="checkline"><input type="checkbox" name="accept" value="1" required>
                  I understand that keys are bound to the server and cannot be used offline.</label>'
                .'<button type="submit" style="width:100%;margin-top:4px">'
                .View::icon('user',16).'Create my account</button></form>';
        }

        $body='<section class="card auth-card auth-card-wide"><div class="auth-head">
          <span class="brand"><span class="brand-mark">'.View::icon('logo',22).'</span>
          <span><span class="t">TDCore</span><small>Licensing network</small></span></span>
          <h1><span class="grad">Join the network</span></h1>
          <p>'.($ref?'Your invited role is <strong>'.View::e(Account::roleLabel((string)$ref['target_role']))
            .'</strong>. Finish the form to activate it.':'Create a reseller account and start issuing keys.')
            .'</p></div>'
          .$banner.$form
          .'<div class="auth-split"><span></span><em>already registered</em><span></span></div>'
          .'<a class="btn ghost" style="width:100%" href="/admin/login">'.View::icon('lock',16)
          .'Sign in to your panel</a>
          <ul class="auth-bullets">
          <li>'.View::icon('shield',15).'Every key is checked online, no offline bypass</li>
          <li>'.View::icon('coin',15).'Credits are deducted only when you generate a key</li>
          <li>'.View::icon('link',15).'Invite your own resellers with referral links</li>
          </ul></section>';

        View::page('Create account',$body,false,'','','/assets/tdcore.mp4','auth register');
    }

    public static function create(): never {
        Auth::csrfCheck();
        $v=Http::form();
        $ref=trim((string)($v['ref']??''));
        if (empty($v['accept'])) {
            Auth::flash('Please accept the licensing notice before continuing.');
            Http::redirect('/register'.($ref!==''?'?ref='.rawurlencode($ref):''));
        }
        try {
            $account=Register::create($ref!==''?$ref:null,
                (string)($v['username']??''),(string)($v['password']??''),
                (string)($v['confirm']??''),(string)($v['display_name']??''));
        } catch (Deny $e) {
            $messages=[
                'REFERRAL_INVALID'=>'that referral link is not valid',
                'REFERRAL_EXHAUSTED'=>'that referral link has reached its limit',
                'REGISTRATION_CLOSED'=>'public registration is closed',
                'USERNAME_INVALID'=>'use 3-60 letters, numbers, dot, dash or underscore',
                'USERNAME_TAKEN'=>'that username is already taken',
                'PASSWORD_TOO_SHORT'=>'password must be at least 8 characters',
                'PASSWORD_TOO_LONG'=>'password is too long',
                'PASSWORD_TOO_WEAK'=>'password must not match the username',
                'PASSWORD_MISMATCH'=>'the passwords do not match',
                'RATE_LIMITED'=>'too many attempts, please wait a few minutes',
            ];
            Auth::flash('Could not register: '.($messages[$e->getMessage()]??strtolower($e->getMessage())));
            Http::redirect('/register'.($ref!==''?'?ref='.rawurlencode($ref):''));
        }
        $row=AdminUser::byId((int)$account['id']);
        if ($row) Auth::establish($row);
        Auth::flash('Welcome to TDCore. Your '.$account['role'].' account is active.');
        Http::redirect('/admin');
    }
}
