<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class View {
    public static function e(mixed $v): string {
        return htmlspecialchars((string)$v,ENT_QUOTES|ENT_SUBSTITUTE,'UTF-8');
    }
    public static function csrf(): string {
        return '<input type="hidden" name="csrf" value="'.self::e(Auth::csrf()).'">';
    }
    public static function icon(string $name,int $size=18): string {
        $paths=[
        'logo'=>'<path d="M12 2.6 20 6v6.1c0 5-3.4 8.4-8 9.3-4.6-.9-8-4.3-8-9.3V6l8-3.4Z"/><path d="M12 9.2a2.2 2.2 0 0 1 1.1 4.1V16h-2.2v-2.7A2.2 2.2 0 0 1 12 9.2Z"/>',
        'grid'=>'<rect x="3" y="3" width="7.5" height="7.5" rx="1.6"/><rect x="13.5" y="3" width="7.5" height="7.5" rx="1.6"/><rect x="3" y="13.5" width="7.5" height="7.5" rx="1.6"/><rect x="13.5" y="13.5" width="7.5" height="7.5" rx="1.6"/>',
        'key'=>'<circle cx="8" cy="15" r="4"/><path d="m10.8 12.2 8-8M16 4.5l3.5 3.5M14 7l2.5 2.5"/>',
        'shield'=>'<path d="M12 3 19 6v5.4c0 4.5-3 7.7-7 8.7-4-1-7-4.2-7-8.7V6l7-3Z"/><path d="m9.2 12 2 2 3.6-3.8"/>',
        'chip'=>'<rect x="6" y="6" width="12" height="12" rx="2.5"/><path d="M9.5 10h5M9.5 14h3"/><path d="M9 2.5v3M15 2.5v3M9 18.5v3M15 18.5v3M2.5 9h3M2.5 15h3M18.5 9h3M18.5 15h3"/>',
        'lock'=>'<rect x="4.5" y="10.5" width="15" height="10" rx="2.5"/><path d="M8 10.5V8a4 4 0 0 1 8 0v2.5M12 14.2v2.4"/>',
        'gear'=>'<circle cx="12" cy="12" r="3.2"/><path d="M19.4 15a1.7 1.7 0 0 0 .3 1.9l.1.1a2 2 0 1 1-2.8 2.8l-.1-.1a1.7 1.7 0 0 0-1.9-.3 1.7 1.7 0 0 0-1 1.5V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.1-1.5 1.7 1.7 0 0 0-1.9.3l-.1.1a2 2 0 1 1-2.8-2.8l.1-.1a1.7 1.7 0 0 0 .3-1.9 1.7 1.7 0 0 0-1.5-1H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.5-1.1 1.7 1.7 0 0 0-.3-1.9l-.1-.1a2 2 0 1 1 2.8-2.8l.1.1a1.7 1.7 0 0 0 1.9.3H9a1.7 1.7 0 0 0 1-1.5V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1 1.5 1.7 1.7 0 0 0 1.9-.3l.1-.1a2 2 0 1 1 2.8 2.8l-.1.1a1.7 1.7 0 0 0-.3 1.9V9a1.7 1.7 0 0 0 1.5 1H21a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.5 1Z"/>',
        'activity'=>'<path d="M3 12h3.5l2.2-6 3.5 12 2.3-6H21"/>',
        'users'=>'<circle cx="9" cy="8" r="3.4"/><path d="M2.8 20a6.2 6.2 0 0 1 12.4 0"/><path d="M16.5 4.6a3.4 3.4 0 0 1 0 6.8M17.4 14.4a6.2 6.2 0 0 1 3.8 5.6"/>',
        'logout'=>'<path d="M9 4H5.5A1.5 1.5 0 0 0 4 5.5v13A1.5 1.5 0 0 0 5.5 20H9"/><path d="M15 8l4 4-4 4M19 12H9"/>',
        'plus'=>'<path d="M12 5v14M5 12h14"/>',
        'alert'=>'<path d="M12 3 2.8 19h18.4L12 3Z"/><path d="M12 9v4.5M12 16.5h.01"/>',
        'check'=>'<path d="m4.5 12.5 5 5L20 6.5"/>',
        'db'=>'<ellipse cx="12" cy="5.5" rx="7.5" ry="3"/><path d="M4.5 5.5v13c0 1.7 3.4 3 7.5 3s7.5-1.3 7.5-3v-13"/><path d="M4.5 12c0 1.7 3.4 3 7.5 3s7.5-1.3 7.5-3"/>',
        'clock'=>'<circle cx="12" cy="12" r="8.5"/><path d="M12 7.5V12l3 2"/>',
        'globe'=>'<circle cx="12" cy="12" r="8.5"/><path d="M3.5 12h17M12 3.5c2.3 2.4 3.4 5.3 3.4 8.5S14.3 18.1 12 20.5c-2.3-2.4-3.4-5.3-3.4-8.5S9.7 5.9 12 3.5Z"/>',
        'arrow'=>'<path d="M5 12h13M13 7l5 5-5 5"/>',
        'search'=>'<circle cx="11" cy="11" r="6.5"/><path d="m16 16 4.5 4.5"/>',
        'coin'=>'<circle cx="12" cy="12" r="8.5"/><path d="M12 7.5v9M14.8 9.6c-.6-.8-1.6-1.1-2.8-1.1-1.7 0-2.8.8-2.8 2s1 1.7 2.8 2 2.8.9 2.8 2-1.2 2-2.8 2c-1.3 0-2.4-.4-3-1.3"/>',
        'link'=>'<path d="M9.5 14.5 14.5 9.5"/><path d="M11 7.5 12.6 6a3.8 3.8 0 0 1 5.4 5.4L16.5 13"/><path d="M13 16.5 11.4 18a3.8 3.8 0 0 1-5.4-5.4L7.5 11"/>',
        'user'=>'<circle cx="12" cy="8" r="3.6"/><path d="M5 20a7 7 0 0 1 14 0"/>',
        'volume'=>'<path d="M4.5 10v4h3l4 3.4V6.6L7.5 10h-3Z"/><path d="M15.5 9.2a4 4 0 0 1 0 5.6"/><path d="M18 6.8a7.4 7.4 0 0 1 0 10.4"/>',
        'copy'=>'<rect x="9" y="9" width="11" height="11" rx="2.5"/><path d="M15 9V6.5A2.5 2.5 0 0 0 12.5 4H6.5A2.5 2.5 0 0 0 4 6.5v6A2.5 2.5 0 0 0 6.5 15H9"/>',
        ];
        $body=$paths[$name]??'';
        return '<svg width="'.$size.'" height="'.$size.'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">'.$body.'</svg>';
    }
    public static function field(string $label,string $name,mixed $value='',string $type='text',
        string $attributes=''): string {
        return '<label>'.self::e($label).'<input type="'.self::e($type).'" name="'.self::e($name).
            '" value="'.self::e($value).'" '.$attributes.'></label>';
    }
    public static function select(string $name,string $current,array $choices,string $label): string {
        $html='<label>'.self::e($label).'<select name="'.self::e($name).'">';
        foreach ($choices as $key=>$text) {
            $html.='<option value="'.self::e($key).'"'.($current===$key?' selected':'').'>'.self::e($text).'</option>';
        }
        return $html.'</select></label>';
    }
    public static function badge(string $status): string {
        $s=strtolower($status);
        $map=['active'=>'active','enabled'=>'active','on'=>'active','ok'=>'active','success'=>'active',
            'owner'=>'active','admin'=>'info','reseller'=>'neutral',
            'disabled'=>'disabled','maintenance'=>'disabled','warn'=>'disabled','pending'=>'disabled',
            'revoked'=>'revoked','blocked'=>'revoked','off'=>'revoked','failed'=>'revoked','bad'=>'revoked'];
        $tone=$map[$s]??'neutral';
        return '<span class="badge '.$tone.'"><span class="dotmark"></span>'.self::e($status).'</span>';
    }
    public static function stat(string $label,mixed $value,string $hint='',string $icon='grid',string $tone=''): string {
        return '<section class="card stat '.self::e($tone).'"><div class="top"><span class="chip">'.self::icon($icon,17).
            '</span>'.self::e($label).'</div><div class="value">'.self::e($value).'</div><p class="hint">'.self::e($hint).'</p></section>';
    }
    public static function page(string $title,string $body,bool $admin=true,string $subtitle='',
        string $actions='',string $video='',string $bodyClass=''): never {
        $notice=Auth::takeFlash();
        $flash=$notice!==''?'<div class="flash">'.self::icon('check',18).'<span>'.self::e($notice).'</span></div>':'';
        $eyebrow=$title==='Overview'?'Control center':'TDCore panel';
        $head=$admin?'<div class="page-head"><div><p class="eyebrow">'.self::e($eyebrow).'</p>
            <h1><span class="grad">'.self::e($title).'</span></h1>'
            .($subtitle!==''?'<p>'.self::e($subtitle).'</p>':'').'</div>'
            .($actions!==''?'<div class="head-actions">'.$actions.'</div>':'').'</div>':'';
        $main=$admin
            ? self::topbar().'<main class="shell">'.$head.$flash.$body.'</main>'
              .'<footer class="foot"><span>TDCore · signed policy engine</span><span>UTC '
              .self::e(gmdate('Y-m-d H:i')).' · PHP '.self::e(PHP_VERSION).'</span></footer>'
            : '<main class="shell auth-wrap">'.$flash.$body
              .(str_contains($bodyClass,'login')?'':'<div class="auth-foot">TDCore licensing · ECDSA P-256 signed leases · '
              .self::e(gmdate('Y')).'</div>').'</main>';
        $classes=trim(($admin?'':"auth").' '.$bodyClass);
        $stage=$video!==''?self::videoStage($video):'';
        $ambient=$admin?'<div class="panel-bg" aria-hidden="true"><div class="aurora"></div></div>':'';
        $html='<!doctype html><html lang="en"><head><meta charset="utf-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="robots" content="noindex,nofollow">
        <meta name="theme-color" content="#070c16">
        <title>'.self::e($title).' — TDCore Panel</title><style>'.self::css().'</style></head>
        <body'.($classes!==''?' class="'.self::e($classes).'"':'').'>'.$ambient.$stage.$main.'</body></html>';
        Http::text($html);
    }

    /** Full-screen looping video with an animated gradient fallback. */
    private static function videoStage(string $src): string {
        $audio=$src!=='/assets/tdcore-brand.mp4'?'1':'0';
        return '<div class="video-bg" aria-hidden="true"><div class="aurora"></div>'
            .'<video class="bg-video" autoplay muted loop playsinline preload="metadata"'
            .' data-audio="'.$audio.'" poster="/assets/tdcore-poster.jpg">'
            .'<source src="'.self::e($src).'" type="video/mp4">'
            .'<source src="/assets/tdcore-brand.mp4" type="video/mp4">'
            .'</video><div class="video-veil"></div></div>'
            .'<script src="/assets/tdcore-video.js" defer></script>';
    }
    private static function topbar(): string {
        $path=(string)(parse_url((string)($_SERVER['REQUEST_URI']??'/admin'),PHP_URL_PATH)?:'/admin');
        $path='/'.trim($path,'/');
        if (Auth::isReseller()) {
            $items=[['/admin','Overview','grid'],['/admin/licenses','Licenses','key'],
                ['/admin/credits','Credits','coin'],['/admin/referrals','Referrals','link'],
                ['/admin/account','My account','user']];
        } else {
            $items=[['/admin','Overview','grid'],['/admin/licenses','Licenses','key'],
                ['/admin/accounts','Accounts','users'],['/admin/credits','Credits','coin'],
                ['/admin/referrals','Referrals','link'],['/admin/security','Security','lock'],
                ['/admin/settings','Settings','gear'],['/admin/audit','Audit log','activity']];
        }
        $nav='';
        foreach ($items as [$href,$label,$icon]) {
            $active=$href==='/admin'?($path==='/admin'):str_starts_with($path,$href);
            $nav.='<a href="'.$href.'"'.($active?' class="active"':'').'>'.self::icon($icon,16)
                .'<span>'.$label.'</span></a>';
        }
        return '<header class="topbar"><div class="topbar-inner">
          <a class="brand" href="/admin"><span class="brand-mark">'.self::icon('logo',21).'</span>
          <span><span class="t">TDCore</span><small>Licensing control</small></span></a>
          <input type="checkbox" id="tdnav" class="nav-toggle">
          <label for="tdnav" class="burger" aria-label="Open navigation"><span></span><span></span><span></span></label>
          <div class="nav-wrap"><nav class="nav">'.$nav.'</nav>
          <form class="nav-form" method="post" action="/admin/logout">'.self::csrf().
          '<button class="logout-btn" type="submit" title="Sign out">'.self::icon('logout',15).
          '<span>'.self::e(Auth::username()).'</span></button></form></div>
          </div></header>';
    }
    private static function css(): string {
        return <<<'CSS'
*,*::before,*::after{box-sizing:border-box}
:root{color-scheme:dark;
 --bg:#060a12;--surface:rgba(18,28,45,.72);--stroke:rgba(122,162,220,.16);
 --stroke-2:rgba(140,180,240,.30);--text:#e9f1fc;--muted:#97abc8;--dim:#6d82a2;
 --blue:#6aa7ff;--violet:#a78bfa;--green:#3ddc97;--amber:#f8c25c;--red:#ff7a90;
 --r:16px;--r-lg:22px;--shadow:0 24px 60px -28px rgba(0,0,0,.8)}
body{margin:0;min-height:100vh;background:var(--bg);color:var(--text);
 font:14.5px/1.65 ui-sans-serif,system-ui,-apple-system,"Segoe UI",sans-serif;
 -webkit-font-smoothing:antialiased;overflow-x:hidden}
body::before{content:"";position:fixed;inset:0;z-index:-2;pointer-events:none;
 background:radial-gradient(1100px 700px at 10% -12%,rgba(82,120,255,.22),transparent 62%),
 radial-gradient(900px 620px at 96% -4%,rgba(150,90,255,.18),transparent 58%),
 radial-gradient(1000px 800px at 50% 118%,rgba(35,220,190,.10),transparent 62%),
 linear-gradient(180deg,#070c16,#05080f 68%)}
body::after{content:"";position:fixed;inset:0;z-index:-1;pointer-events:none;
 background-image:linear-gradient(rgba(120,160,220,.035) 1px,transparent 1px),
 linear-gradient(90deg,rgba(120,160,220,.035) 1px,transparent 1px);
 background-size:56px 56px;-webkit-mask-image:radial-gradient(circle at 50% 18%,#000 22%,transparent 78%);
 mask-image:radial-gradient(circle at 50% 18%,#000 22%,transparent 78%)}
a{color:var(--blue);text-decoration:none}a:hover{color:#b7d3ff}
h1{font-size:clamp(25px,3.2vw,34px);line-height:1.12;margin:0;letter-spacing:-.025em;font-weight:850}
h1 .grad{background:linear-gradient(100deg,#d7e6ff,#8db6ff 48%,#c0a8ff);
 -webkit-background-clip:text;background-clip:text;color:transparent}
h2{font-size:18px;margin:0 0 4px;letter-spacing:-.01em}h3{font-size:14px;margin:0}
p{overflow-wrap:anywhere}
code{font:12px ui-monospace,SFMono-Regular,Menlo,monospace;background:rgba(120,160,220,.10);
 border:1px solid var(--stroke);padding:2px 6px;border-radius:6px;overflow-wrap:anywhere}
.muted{color:var(--muted)}.dim{color:var(--dim)}.small{font-size:12.5px}
.mono{font-family:ui-monospace,SFMono-Regular,Menlo,monospace}
/* top navigation */
.topbar{position:sticky;top:0;z-index:60;background:linear-gradient(180deg,rgba(7,12,22,.94),rgba(7,12,22,.74));
 border-bottom:1px solid var(--stroke);-webkit-backdrop-filter:blur(18px) saturate(140%);
 backdrop-filter:blur(18px) saturate(140%)}
.topbar-inner{position:relative;max-width:1260px;margin:0 auto;padding:10px 20px;
 display:flex;align-items:center;gap:18px;min-height:68px}
.brand{display:flex;align-items:center;gap:11px;color:var(--text);font-weight:800;flex:none}
.brand:hover{color:var(--text)}
.brand-mark{width:40px;height:40px;border-radius:13px;display:grid;place-items:center;color:#cfe0ff;
 background:linear-gradient(145deg,rgba(106,167,255,.24),rgba(167,139,250,.18));
 border:1px solid rgba(140,180,255,.38);box-shadow:inset 0 0 24px rgba(90,140,255,.22),0 10px 26px -14px rgba(90,140,255,.9)}
.brand .t{font-size:17px;line-height:1.05;letter-spacing:.01em}
.brand small{display:block;font-size:9px;font-weight:750;letter-spacing:.22em;color:var(--dim);text-transform:uppercase;margin-top:4px}
.nav-wrap{margin-left:auto;display:flex;align-items:center;gap:10px;min-width:0}
.nav{display:flex;align-items:center;gap:3px;flex-wrap:wrap}
.nav a{display:flex;align-items:center;gap:7px;padding:9px 12px;border-radius:11px;color:var(--muted);
 font-weight:650;font-size:13px;border:1px solid transparent;transition:.2s ease;white-space:nowrap}
.nav a:hover{color:var(--text);background:rgba(120,160,220,.08)}
.nav a.active{color:#fff;background:linear-gradient(135deg,rgba(106,167,255,.20),rgba(167,139,250,.16));
 border-color:rgba(140,180,255,.30);box-shadow:0 8px 24px -14px rgba(110,160,255,.95)}
.nav a svg{opacity:.9}
.nav-form{margin:0}
.logout-btn{display:flex;align-items:center;gap:8px;padding:9px 12px;border-radius:11px;
 background:rgba(255,255,255,.04);border:1px solid var(--stroke);color:var(--muted);
 font:inherit;font-size:12.5px;font-weight:700;cursor:pointer;box-shadow:none}
.logout-btn:hover{color:#ffb7c3;border-color:rgba(255,122,144,.4);background:rgba(255,122,144,.09);
 transform:none;filter:none}
.nav-toggle{position:absolute;opacity:0;pointer-events:none}
.burger{display:none;margin-left:auto;width:42px;height:42px;border-radius:12px;
 border:1px solid var(--stroke);background:rgba(18,28,45,.75);cursor:pointer;padding:11px 10px}
.burger span{display:block;height:2px;border-radius:2px;background:var(--text);margin:4px 0;transition:.25s}
.nav-toggle:checked~.burger span:nth-child(1){transform:translateY(6px) rotate(45deg)}
.nav-toggle:checked~.burger span:nth-child(2){opacity:0}
.nav-toggle:checked~.burger span:nth-child(3){transform:translateY(-6px) rotate(-45deg)}
/* shell */
.shell{max-width:1260px;margin:0 auto;padding:34px 20px 70px}
.page-head{display:flex;gap:18px;align-items:flex-end;justify-content:space-between;
 flex-wrap:wrap;margin:2px 0 24px}
.page-head>div:first-child{min-width:0}
.page-head p{margin:9px 0 0;color:var(--muted);max-width:74ch}
.eyebrow{font-size:10.5px;letter-spacing:.22em;text-transform:uppercase;color:var(--dim);
 font-weight:800;margin:0 0 7px}
.head-actions{display:flex;gap:10px;flex-wrap:wrap}
/* cards & grid */
.card{position:relative;background:linear-gradient(160deg,rgba(22,33,52,.82),rgba(12,19,32,.88));
 border:1px solid var(--stroke);border-radius:var(--r-lg);padding:22px;overflow-x:clip;
 box-shadow:var(--shadow);-webkit-backdrop-filter:blur(12px);backdrop-filter:blur(12px)}
.card+.card,.card+.grid,.grid+.card,.card+.table-wrap{margin-top:18px}
.grid>.card,.grid>.stat,.grid>.table-wrap{margin-top:0}
.card-head{display:flex;align-items:flex-start;justify-content:space-between;gap:14px;margin-bottom:18px}
.card-head p{margin:3px 0 0;color:var(--muted);font-size:13px}
.card-head .badge,.card-head .btn{flex:none}
.grid{display:grid;gap:16px}
.grid>*{min-width:0}
.g-2{grid-template-columns:repeat(2,minmax(0,1fr))}
.g-3{grid-template-columns:repeat(auto-fit,minmax(250px,1fr))}
.g-4{grid-template-columns:repeat(auto-fit,minmax(212px,1fr))}
/* stat cards */
.stat{overflow:hidden;padding:19px 20px}
.stat::after{content:"";position:absolute;right:-46px;top:-52px;width:132px;height:132px;
 border-radius:50%;background:radial-gradient(circle,rgba(106,167,255,.18),transparent 70%)}
.stat .top{display:flex;align-items:center;gap:10px;color:var(--muted);font-size:10.5px;
 font-weight:800;letter-spacing:.12em;text-transform:uppercase}
.chip{width:34px;height:34px;border-radius:11px;display:grid;place-items:center;flex:none;
 background:rgba(106,167,255,.12);border:1px solid rgba(106,167,255,.26);color:#a9cbff}
.stat .value{font-size:32px;font-weight:850;letter-spacing:-.035em;margin:13px 0 2px;line-height:1}
.stat .hint{color:var(--dim);font-size:12.5px;margin:0}
.stat.ok .chip{background:rgba(61,220,151,.12);border-color:rgba(61,220,151,.3);color:#8ff0c4}
.stat.warn .chip{background:rgba(248,194,92,.12);border-color:rgba(248,194,92,.3);color:#ffd99a}
.stat.bad .chip{background:rgba(255,122,144,.12);border-color:rgba(255,122,144,.3);color:#ffb3c0}
/* badges */
.badge{display:inline-flex;align-items:center;gap:7px;padding:4px 10px;border-radius:999px;
 font-size:11.5px;font-weight:750;letter-spacing:.02em;border:1px solid transparent;white-space:nowrap}
.dotmark{width:6px;height:6px;border-radius:50%;background:currentColor;flex:none;opacity:.9}
.badge.active{color:#8ff0c4;background:rgba(61,220,151,.12);border-color:rgba(61,220,151,.3)}
.badge.disabled{color:#ffd99a;background:rgba(248,194,92,.12);border-color:rgba(248,194,92,.3)}
.badge.revoked{color:#ffb3c0;background:rgba(255,122,144,.12);border-color:rgba(255,122,144,.3)}
.badge.info{color:#b3d0ff;background:rgba(106,167,255,.12);border-color:rgba(106,167,255,.3)}
.badge.neutral{color:var(--muted);background:rgba(255,255,255,.05);border-color:var(--stroke)}
/* buttons */
button,.btn{display:inline-flex;align-items:center;justify-content:center;gap:8px;font:inherit;
 font-weight:750;font-size:13.5px;color:#071120;background:linear-gradient(135deg,#a6c9ff,#7fb0ff);
 border:1px solid rgba(160,200,255,.55);border-radius:11px;padding:10px 16px;cursor:pointer;
 transition:.2s ease;box-shadow:0 12px 26px -16px rgba(110,160,255,.95)}
button:hover,.btn:hover{transform:translateY(-1px);filter:brightness(1.06);color:#071120}
button:active,.btn:active{transform:translateY(0)}
button.ghost,.btn.ghost{background:rgba(120,160,220,.10);color:#cfe0ff;border-color:var(--stroke)}
button.ghost:hover,.btn.ghost:hover{color:#fff;background:rgba(120,160,220,.16);filter:none}
button.danger,.btn.danger{background:linear-gradient(135deg,#ffa3b4,#ff7590);color:#2b0710;
 border-color:rgba(255,140,160,.55)}
button.sm,.btn.sm{padding:7px 11px;font-size:12.5px;border-radius:9px;box-shadow:none}
.btn-row{display:flex;gap:10px;flex-wrap:wrap;align-items:center}
.btn-row form{margin:0}
/* forms */
label{display:block;margin:0 0 14px;font-size:12.5px;font-weight:700;color:#c4d5ec;letter-spacing:.01em}
input,select,textarea{width:100%;margin-top:6px;background:#0a1220;color:var(--text);
 border:1px solid var(--stroke-2);border-radius:11px;padding:11px 13px;font:inherit;font-size:14px;
 transition:.18s ease;appearance:none;-webkit-appearance:none}
input:focus,select:focus,textarea:focus{outline:none;border-color:rgba(120,175,255,.72);
 box-shadow:0 0 0 4px rgba(106,167,255,.12)}
input::placeholder{color:#5f7391}
input[type=checkbox]{width:17px;height:17px;margin:0 9px 0 0;accent-color:#7fb0ff;vertical-align:-3px}
input[type=date]{color-scheme:dark}
select{background-image:linear-gradient(45deg,transparent 50%,#8fa8c8 50%),
 linear-gradient(135deg,#8fa8c8 50%,transparent 50%);
 background-position:calc(100% - 18px) 52%,calc(100% - 13px) 52%;
 background-size:5px 5px,5px 5px;background-repeat:no-repeat;padding-right:34px}
fieldset{border:1px solid var(--stroke);border-radius:var(--r);padding:15px 16px 8px;
 margin:0;background:rgba(8,14,25,.45)}
legend{padding:0 8px;font-size:10.5px;font-weight:800;letter-spacing:.16em;
 text-transform:uppercase;color:var(--muted)}
/* tables */
.table-wrap{overflow:auto;border:1px solid var(--stroke);border-radius:14px;background:rgba(8,14,25,.42)}
table{width:100%;border-collapse:collapse;font-size:13.5px}
th{position:sticky;top:0;z-index:1;text-align:left;padding:12px 14px;background:#0e1829;
 color:var(--dim);font-size:10.5px;font-weight:800;letter-spacing:.12em;text-transform:uppercase;
 border-bottom:1px solid var(--stroke);white-space:nowrap}
td{padding:13px 14px;border-bottom:1px solid rgba(122,162,220,.10);vertical-align:top;color:#c9d8ec}
tbody tr{transition:background .15s}
tbody tr:hover{background:rgba(120,160,220,.05)}
tbody tr:last-child td{border-bottom:0}
td strong{color:var(--text);font-weight:750}
td p{margin:4px 0 0}
/* flash */
.flash{display:flex;align-items:center;gap:12px;margin:0 0 20px;padding:13px 16px;border-radius:14px;
 background:linear-gradient(120deg,rgba(61,220,151,.14),rgba(106,167,255,.10));
 border:1px solid rgba(61,220,151,.35);color:#c8ffe4;animation:slidein .35s ease both}
@keyframes slidein{from{opacity:0;transform:translateY(-8px)}to{opacity:1;transform:none}}
/* details / secrets / misc */
.secret{font:13px ui-monospace,SFMono-Regular,Menlo,monospace;overflow-wrap:anywhere;
 background:linear-gradient(135deg,rgba(106,167,255,.14),rgba(167,139,250,.10));
 border:1px dashed rgba(140,180,255,.42);padding:18px;border-radius:14px;color:#dcecff;
 letter-spacing:.02em;line-height:1.7}
details{max-width:430px;margin-top:8px}
summary{cursor:pointer;color:var(--muted);font-size:12.5px;font-weight:700;padding:5px 0}
summary:hover{color:var(--text)}
details[open] summary{margin-bottom:10px}
.empty{text-align:center;padding:38px 18px;color:var(--dim)}
.empty svg{opacity:.5;margin-bottom:10px}
.divider{height:1px;background:var(--stroke);margin:20px 0;border:0}
.kv{display:grid;grid-template-columns:auto 1fr;gap:11px 20px;font-size:13px;margin:0}
.kv dt{color:var(--dim);font-weight:650;white-space:nowrap}
.kv dd{margin:0;color:#d5e3f6;overflow-wrap:anywhere}
/* sparkline */
.spark{display:flex;align-items:flex-end;gap:6px;height:92px;margin-top:16px}
.spark .bar{flex:1;min-width:5px;border-radius:6px 6px 3px 3px;
 background:linear-gradient(180deg,#9cc0ff,#5a86d8);opacity:.85;transition:.2s}
.spark .bar:hover{opacity:1;filter:brightness(1.18)}
.spark .bar.zero{background:rgba(120,160,220,.14)}
.spark-labels{display:flex;justify-content:space-between;color:var(--dim);font-size:10px;
 margin-top:8px;letter-spacing:.06em}
/* score ring */
.ring{--p:0;--ring:#3ddc97;width:108px;height:108px;border-radius:50%;display:grid;place-items:center;
 background:conic-gradient(var(--ring) calc(var(--p)*1%),rgba(120,160,220,.12) 0);
 position:relative;flex:none;filter:drop-shadow(0 0 18px rgba(61,220,151,.22))}
.ring::before{content:"";position:absolute;inset:10px;border-radius:50%;
 background:#0b1424;border:1px solid var(--stroke)}
.ring span{position:relative;font-size:27px;font-weight:850;letter-spacing:-.03em}
.ring small{position:relative;display:block;text-align:center;font-size:9px;letter-spacing:.16em;
 text-transform:uppercase;color:var(--dim);margin-top:-2px}
.ring.warn{--ring:#f8c25c;filter:drop-shadow(0 0 18px rgba(248,194,92,.2))}
.ring.bad{--ring:#ff7a90;filter:drop-shadow(0 0 18px rgba(255,122,144,.2))}
/* checklist */
.checklist{list-style:none;margin:0;padding:0;display:grid;gap:11px}
.checklist li{display:flex;gap:11px;align-items:flex-start;font-size:13px;color:#c6d6ea}
.checklist .txt{min-width:0}
.checklist .txt small{display:block;color:var(--dim);font-size:11.5px;margin-top:2px}
.dot{width:19px;height:19px;border-radius:50%;display:grid;place-items:center;flex:none;
 font-size:10px;font-weight:850;margin-top:1px}
.dot.ok{background:rgba(61,220,151,.15);color:#7af0b8;border:1px solid rgba(61,220,151,.35)}
.dot.no{background:rgba(255,122,144,.13);color:#ffa9b8;border:1px solid rgba(255,122,144,.35)}
/* quick actions */
.quick{display:grid;grid-template-columns:repeat(auto-fit,minmax(186px,1fr));gap:12px}
.quick a{display:flex;align-items:center;gap:12px;padding:15px;border-radius:14px;
 border:1px solid var(--stroke);background:rgba(255,255,255,.025);color:var(--text);
 font-weight:750;transition:.2s ease}
.quick a:hover{border-color:rgba(140,180,255,.42);background:rgba(106,167,255,.09);
 transform:translateY(-2px);color:var(--text)}
.quick a .chip{width:36px;height:36px}
.quick small{display:block;color:var(--dim);font-weight:500;font-size:11.5px;margin-top:1px}
/* auth */
body.auth{display:grid;place-items:center;padding:28px 18px;min-height:100vh}
.shell.auth-wrap{display:flex;flex-direction:column;align-items:center;gap:16px;
 max-width:440px;padding:0;margin:0;width:100%}
.auth-card{width:100%;padding:30px;border-radius:26px;
 background:linear-gradient(165deg,rgba(24,36,58,.94),rgba(10,16,28,.96));
 border:1px solid var(--stroke-2);
 box-shadow:0 44px 90px -42px #000,0 0 90px -50px rgba(106,167,255,.6)}
.auth-head{text-align:center;margin-bottom:22px}
.auth-head .brand{justify-content:center;margin-bottom:16px}
.auth-head h1{font-size:25px}
.auth-head p{color:var(--muted);margin:9px 0 0;font-size:13px}
.auth-bullets{display:grid;gap:9px;margin:18px 0 0;padding:0;list-style:none;
 font-size:12.5px;color:var(--muted)}
.auth-bullets li{display:flex;gap:10px;align-items:center}
.auth-bullets svg{color:#8db6ff;flex:none}
.auth-foot{color:var(--dim);font-size:11.5px;text-align:center}
/* login: full-bleed video with the form floating straight over it */
body.login{display:block;padding:0;min-height:100vh;overflow-x:hidden}
body.login .bg-video{opacity:1;filter:saturate(1.06) contrast(1.04)}
body.login .video-veil{background:
 linear-gradient(180deg,rgba(4,8,15,.28) 0%,rgba(4,8,15,.10) 42%,rgba(4,8,15,.42) 100%),
 radial-gradient(760px 520px at 50% 52%,rgba(4,8,15,.42),rgba(4,8,15,0) 72%)}
body.login .shell.auth-wrap{max-width:none;width:100%;min-height:100vh;min-height:100dvh;
 padding:24px;justify-content:center;align-items:center}
body.login .auth-card{max-width:360px;width:100%;margin:0;padding:0;
 background:none;border:0;border-radius:0;box-shadow:none;
 backdrop-filter:none;-webkit-backdrop-filter:none}
body.login .auth-welcome{font-size:34px;line-height:1.1;text-align:center;
 letter-spacing:-.02em;margin:0 0 24px;text-shadow:0 3px 22px rgba(0,0,0,.7)}
body.login .auth-card label{margin-bottom:14px;color:#eaf2ff;
 text-shadow:0 1px 12px rgba(0,0,0,.7)}
body.login .auth-card input{padding:12px 14px;border-radius:12px;
 background:rgba(4,9,17,.66);border-color:rgba(184,208,255,.44);
 backdrop-filter:blur(10px);-webkit-backdrop-filter:blur(10px);
 box-shadow:0 10px 30px -16px rgba(0,0,0,.8)}
body.login .auth-card input::placeholder{color:rgba(200,216,240,.65)}
body.login .auth-submit,
body.login .auth-register{width:100%;padding:12px;border-radius:12px;
 text-decoration:none;letter-spacing:.01em;font-weight:750;color:#e6f0ff;
 background:rgba(6,11,20,.62);border-color:rgba(184,208,255,.38);
 box-shadow:none;
 backdrop-filter:blur(10px);-webkit-backdrop-filter:blur(10px)}
body.login .auth-submit{margin-top:6px}
body.login .auth-register{margin-top:10px}
@media(max-width:760px){
 body.login .shell.auth-wrap{padding:18px 14px max(22px,env(safe-area-inset-bottom,22px))}
 body.login .video-veil{background:
   linear-gradient(180deg,rgba(4,8,15,.26) 0%,rgba(4,8,15,.10) 42%,rgba(4,8,15,.52) 100%),
   radial-gradient(520px 420px at 50% 50%,rgba(4,8,15,.38),rgba(4,8,15,0) 74%)}
 body.login .shell.auth-wrap,
 body.login .auth-card{max-width:420px}
 body.login .auth-welcome{font-size:32px}
}
/* register: same full-bleed video with the form floating over it */
body.register{display:block;padding:0;min-height:100vh;overflow-x:hidden}
body.register .bg-video{opacity:1;filter:saturate(1.06) contrast(1.04)}
body.register .video-veil{background:
 linear-gradient(180deg,rgba(4,8,15,.36) 0%,rgba(4,8,15,.16) 40%,rgba(4,8,15,.58) 100%),
 radial-gradient(900px 640px at 50% 46%,rgba(4,8,15,.52),rgba(4,8,15,0) 76%)}
body.register .shell.auth-wrap{max-width:none;width:100%;min-height:100vh;min-height:100dvh;
 padding:30px 18px;justify-content:center;align-items:center}
body.register .auth-card{max-width:440px;width:100%;margin:0;padding:24px 22px 22px;
 background:none;border:0;border-radius:0;box-shadow:none;
 backdrop-filter:none;-webkit-backdrop-filter:none}
body.register .auth-head{margin-bottom:18px}
body.register .auth-head .brand{margin-bottom:12px}
body.register .auth-head h1{font-size:28px;text-shadow:0 3px 22px rgba(0,0,0,.7)}
body.register .auth-head p{color:#e2ecff;text-shadow:0 1px 12px rgba(0,0,0,.7)}
body.register .auth-card label,
body.register .checkline{color:#eaf2ff;text-shadow:0 1px 12px rgba(0,0,0,.7)}
body.register .auth-card input{padding:11px 13px;border-radius:12px;
 background:rgba(4,9,17,.66);border-color:rgba(184,208,255,.44);
 backdrop-filter:blur(10px);-webkit-backdrop-filter:blur(10px);
 box-shadow:0 10px 30px -16px rgba(0,0,0,.8)}
body.register .auth-card input::placeholder{color:rgba(200,216,240,.65)}
body.register .auth-card input[type=checkbox]{width:16px;height:16px;padding:0;margin:0 8px 0 0;
 box-shadow:none;accent-color:#6aa7ff}
body.register .auth-bullets,
body.register .auth-split{color:#dbe7fa;text-shadow:0 1px 12px rgba(0,0,0,.7)}
body.register .auth-card .btn.ghost{background:rgba(6,11,20,.62);
 border-color:rgba(184,208,255,.36);backdrop-filter:blur(10px);
 -webkit-backdrop-filter:blur(10px)}
body.register .auth-foot{color:#c6d8f0;text-shadow:0 1px 10px rgba(0,0,0,.7)}
@media(max-width:760px){
 body.register .shell.auth-wrap{padding:18px 14px max(22px,env(safe-area-inset-bottom,22px))}
 body.register .video-veil{background:
   linear-gradient(180deg,rgba(4,8,15,.34) 0%,rgba(4,8,15,.14) 40%,rgba(4,8,15,.60) 100%),
   radial-gradient(620px 520px at 50% 48%,rgba(4,8,15,.46),rgba(4,8,15,0) 78%)}
 body.register .auth-card{max-width:440px;padding:20px 16px 18px}
}
.foot{max-width:1260px;margin:0 auto;padding:0 20px 34px;color:var(--dim);font-size:11.5px;
 display:flex;justify-content:space-between;gap:14px;flex-wrap:wrap}
/* auth video stage */
.video-bg{position:fixed;inset:0;z-index:-2;overflow:hidden;background:#060a12}
.bg-video{position:absolute;inset:0;width:100%;height:100%;object-fit:cover;
 opacity:.55;filter:saturate(1.05) contrast(1.03)}
.video-veil{position:absolute;inset:0;
 background:radial-gradient(900px 620px at 50% 0%,rgba(6,10,18,.15),rgba(6,10,18,.82) 70%),
 linear-gradient(180deg,rgba(6,10,18,.55),rgba(5,8,15,.94))}
/* animated ambient background for every signed-in page */
.panel-bg{position:fixed;inset:0;z-index:-1;overflow:hidden;pointer-events:none;
 background:var(--bg)}
.panel-bg .aurora{inset:-32%;opacity:.55;filter:blur(72px);will-change:transform}
.panel-bg:after{content:"";position:absolute;inset:0;
 background:radial-gradient(1100px 780px at 50% 8%,rgba(6,10,18,0),rgba(6,10,18,.52) 78%),
 linear-gradient(180deg,rgba(6,10,18,.12),rgba(6,10,18,.46))}
.aurora{position:absolute;inset:-20%;
 background:
 radial-gradient(46% 38% at 20% 24%,rgba(86,128,255,.5),transparent 62%),
 radial-gradient(42% 36% at 82% 18%,rgba(170,96,255,.4),transparent 60%),
 radial-gradient(46% 40% at 62% 86%,rgba(35,220,190,.3),transparent 64%);
 filter:blur(46px);animation:aurora 22s ease-in-out infinite alternate}
@keyframes aurora{0%{transform:translate3d(-3%,-2%,0) scale(1)}
 50%{transform:translate3d(3%,2%,0) scale(1.08)}
 100%{transform:translate3d(-2%,4%,0) scale(1.03)}}
/* auth variants */
.auth-card-wide{max-width:520px}
.invite{display:flex;align-items:center;gap:11px;margin:0 0 16px;padding:12px 14px;border-radius:13px;
 font-size:12.8px;color:#cfe4ff;background:linear-gradient(130deg,rgba(106,167,255,.16),rgba(167,139,250,.12));
 border:1px solid rgba(140,180,255,.38)}
.invite svg{flex:none;color:#9dc4ff}
.flash.bad{display:flex;align-items:center;gap:10px;padding:12px 14px;border-radius:13px;
 font-size:12.8px;color:#ffc7d1;background:rgba(255,122,144,.12);border:1px solid rgba(255,122,144,.4)}
.locked{text-align:center;padding:26px 6px 8px;display:grid;gap:10px;justify-items:center;color:var(--muted)}
.locked h3{font-size:16px;color:var(--text)}
.locked p{max-width:38ch;margin:0}
.auth-split{display:flex;align-items:center;gap:12px;margin:18px 0 14px;color:var(--dim);
 font-size:10px;letter-spacing:.16em;text-transform:uppercase}
.auth-split span{height:1px;flex:1;background:var(--stroke)}
.auth-split em{font-style:normal;font-weight:800}
.checkline{display:flex;align-items:flex-start;gap:9px;font-size:12px;font-weight:600;
 line-height:1.5;color:var(--muted)}
.checkline input{flex:none;margin-top:2px}
/* connect landing */
body.connect{align-items:center;justify-items:center;padding:34px 20px}
body.connect .shell.auth-wrap{max-width:1120px;gap:18px}
.connect-wrap{display:grid;grid-template-columns:minmax(0,1.35fr) minmax(280px,.65fr);gap:20px;width:100%;
 align-items:stretch}
.connect-hero{position:relative;padding:34px 32px;border-radius:26px;overflow:hidden;
 background:linear-gradient(150deg,rgba(18,30,50,.9),rgba(8,13,24,.94));
 border:1px solid var(--stroke-2);box-shadow:var(--shadow)}
.connect-hero::after{content:"";position:absolute;right:-90px;top:-90px;width:280px;height:280px;
 border-radius:50%;background:radial-gradient(circle,rgba(106,167,255,.22),transparent 68%)}
.connect-hero h1{font-size:clamp(27px,4.4vw,44px);line-height:1.06;margin:4px 0 14px}
.connect-hero .lead{color:#c3d6ee;font-size:14.5px;max-width:52ch;margin:0 0 20px}
.connect-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:10px;margin-top:24px}
.connect-grid>div{padding:12px 14px;border-radius:13px;border:1px solid var(--stroke);
 background:rgba(255,255,255,.03)}
.connect-grid strong{display:block;font-size:12.5px}
.connect-grid small{display:block;color:var(--dim);font-size:11px;margin-top:2px}
.connect-side{display:flex;flex-direction:column;justify-content:center}
/* responsive */
@media(max-width:900px){
 .connect-wrap{grid-template-columns:1fr}
 .connect-hero{padding:26px 22px}
 .burger{display:block}
 .nav-wrap{display:none;position:absolute;left:0;right:0;top:100%;margin:0;
  padding:14px 20px 18px;background:rgba(8,13,24,.985);border-bottom:1px solid var(--stroke);
  flex-direction:column;align-items:stretch;gap:12px;box-shadow:0 42px 60px -30px #000}
 .nav-toggle:checked~.nav-wrap{display:flex}
 .nav{flex-direction:column;align-items:stretch;gap:4px}
 .nav a{padding:12px 14px;font-size:13.5px}
 .logout-btn{width:100%;justify-content:center}
}
@media(max-width:760px){
 .g-2{grid-template-columns:minmax(0,1fr)}
 .shell{padding:24px 14px 64px}
 .card{padding:17px;border-radius:18px}
 .page-head{align-items:flex-start}
 .head-actions{width:100%}
 .head-actions .btn,.head-actions button{flex:1}
 .brand small{display:none}
 .topbar-inner{min-height:62px}
}
@media(prefers-reduced-motion:reduce){*{animation:none!important;transition:none!important}}
CSS;
    }
}
