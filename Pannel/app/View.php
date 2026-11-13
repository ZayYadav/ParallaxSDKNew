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
    /** Render 31 individually selectable signed feature bits, without JS. */
    public static function featureChoices(int $mask): string {
        $html='<fieldset style="border:1px solid #475d75;border-radius:9px;margin:14px 0;padding:15px">
          <legend>Optional signed SDK feature flags</legend>
          <input type="hidden" name="feature_selection" value="1">
          <div class="grid">';
        for ($i=0;$i<31;$i++) {
            $checked=($mask & (1 << $i))!==0?' checked':'';
            $html.='<label><input type="checkbox" name="feature_bits[]" value="'.$i.'"'.$checked.'> Bit '.$i.'</label>';
        }
        return $html.'</div><p class="muted">Select bits supported by your SDK integrations.
          Bit 0 = 1, bit 1 = 2, bit 2 = 4, etc. Empty selection inherits the global
          default mask when it is nonzero. Turning on a bit alone does not add a
          new security check to an SDK function.</p></fieldset>';
    }
    public static function page(string $title,string $body,bool $admin=true): never {
        $notice=Auth::takeFlash();
        $links=$admin?'<nav><a href="/admin">Overview</a><a href="/admin/licenses">Licenses</a><a href="/admin/settings">Security & settings</a><a href="/admin/devices">Devices</a><a href="/admin/audit">Audit</a>
        <form method="post" action="/admin/logout" style="display:inline">'.self::csrf().'<button class="ghost">Log out</button></form></nav>':'';
        $flash=$notice!==''?'<p class="flash">'.self::e($notice).'</p>':'';
        $html='<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
        <meta name="robots" content="noindex,nofollow"><title>'.self::e($title).' — TDCore Panel</title><style>
        :root{color-scheme:dark}*{box-sizing:border-box}body{margin:0;background:#0c111a;color:#ecf3fb;font:14px/1.6 system-ui,-apple-system,sans-serif}
        header{padding:20px max(16px,calc((100% - 1200px)/2));background:#141e2c;border-bottom:1px solid #2e425a}
        h1{font-size:22px;margin:0}h2{font-size:18px;margin:0 0 15px}h3{font-size:15px}
        main{max-width:1200px;margin:28px auto;padding:0 16px}.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(245px,1fr));gap:15px}
        .panel{padding:20px;margin:18px 0;background:#151f2d;border:1px solid #334457;border-radius:12px}
        .flash{border-left:3px solid #70acf4;background:#1b2d44;padding:12px;border-radius:4px;overflow-wrap:anywhere}
        a{color:#96c6ff}nav{display:flex;flex-wrap:wrap;align-items:center;gap:18px;margin-top:12px}
        nav a{font-weight:600;text-decoration:none}label{display:block;font-size:13px;color:#c3d4e8;margin:8px 0}
        input,select,textarea{width:100%;color:#fff;border:1px solid #475d75;background:#0e1825;padding:10px;border-radius:7px;font:inherit}
        input[type=checkbox]{width:auto;margin-right:7px;accent-color:#74aafb}button{margin-top:8px;background:#377fd5;color:white;border:0;border-radius:7px;padding:10px 14px;font-weight:650;cursor:pointer}
        button.danger{background:#b44354}button.ghost{background:#334960}.inline{display:inline-block;margin:0 5px}
        .muted{color:#a3b8d0}.big{font-size:25px;font-weight:750}table{width:100%;border-collapse:collapse}
        th,td{padding:11px 10px;text-align:left;vertical-align:top;border-bottom:1px solid #34465a}
        th{color:#aec7e4}code{overflow-wrap:anywhere}details{max-width:350px}summary{cursor:pointer}
        .scroll{overflow-x:auto} .secret{font:13px ui-monospace,monospace;overflow-wrap:anywhere;background:#23384f;padding:16px;border-radius:8px}
        @media(max-width:680px){.grid{grid-template-columns:1fr}nav{gap:12px}}
        </style></head><body><header><h1>TDCore <span class="muted">/ Control Center</span></h1>'.$links.'</header><main><h2>'.self::e($title).'</h2>'.$flash.$body.'</main></body></html>';
        Http::text($html);
    }
}
