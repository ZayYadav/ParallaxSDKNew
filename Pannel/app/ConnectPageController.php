<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Browser landing page for the /connect endpoint (the SDK still uses POST). */
final class ConnectPageController {
    public static function page(): never {
        $s=Settings::get();
        $live=(int)$s['enabled']===1 && (int)$s['maintenance']===0;
        $state=$live?'<span class="badge active">gateway online</span>'
            :'<span class="badge revoked">gateway paused</span>';
        $body='<div class="connect-wrap">'
            .'<section class="connect-hero">'
            .'<p class="eyebrow">TDCore secure gateway</p>'
            .'<h1><span class="grad">One endpoint.<br>Every protected build.</span></h1>'
            .'<p class="lead">The <code>/connect</code> gateway signs a fresh 60-second policy lease
              for each authorized SDK. Package, signer, device and nonce are verified server-side
              before a single feature bit is released.</p>'
            .'<div class="btn-row">'.$state
            .'<a class="btn" href="/admin/login">'.View::icon('lock',16).'Panel sign in</a>'
            .'<a class="btn ghost" href="/register">'.View::icon('user',16).'Create account</a></div>'
            .'<div class="connect-grid">'
            .'<div><strong>POST /connect</strong><small>Signed policy lease · 60s TTL</small></div>'
            .'<div><strong>ECDSA P-256</strong><small>Native signature verification</small></div>'
            .'<div><strong>Fail closed</strong><small>No server, no features</small></div>'
            .'</div></section>'
            .'<section class="connect-side card">'
            .'<div class="card-head"><div><h2>API contract</h2><p>The SDK never trusts local state.</p></div>'
            .View::icon('shield',22).'</div>'
            .'<dl class="kv">'
            .'<dt>Method</dt><dd><code>POST /connect</code></dd>'
            .'<dt>Fields</dt><dd><code>user_key</code>, <code>serial</code>, <code>pkg</code>, '
            .'<code>signing_sha256</code>, <code>nonce</code></dd>'
            .'<dt>Response</dt><dd>ECDSA-signed <code>policy</code> + <code>signature</code></dd>'
            .'<dt>Lease</dt><dd>60 seconds, renewed online</dd>'
            .'<dt>Offline</dt><dd>denied by design</dd>'
            .'</dl>'
            .'<div class="divider"></div>'
            .'<p class="muted small">Opening this URL in a browser shows this page. '
            .'License checks use the same path with <code>POST</code> and return JSON.</p>'
            .'</section></div>';
        View::page('Secure gateway',$body,false,'','','/assets/tdcore.mp4','auth connect');
    }
}
