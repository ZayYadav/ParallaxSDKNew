<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class AdminFeatureController {
    public static function page(): never {
        Auth::requireStaff();
        $s=Settings::get();
        $mask=(int)$s['default_features'];
        $flags=FeatureFlags::all();
        $override=(int)(Database::one('SELECT COUNT(*) AS n FROM td_licenses WHERE feature_mask<>0')['n']??0);
        $setBits=0;
        for ($i=0;$i<=FeatureFlags::MAX_BIT;$i++) if (($mask & (1 << $i))!==0) $setBits++;

        $explain='<section class="card"><div class="card-head"><div><h2>What is a signed SDK feature flag?</h2>
            <p>A 31-bit capability mask delivered inside the ECDSA P-256 policy.</p></div>
            <span class="chip">'.View::icon('chip',18).'</span></div>
            <div class="grid g-3">
            <div><h3>1 · The panel stores bits</h3><p class="muted small">Each flag is one bit.
            Bit 0 means value <code>1</code>, bit 1 means <code>2</code>, bit 2 means <code>4</code>.
            The sum of all selected bits is the mask.</p></div>
            <div><h3>2 · The policy is signed</h3><p class="muted small">Every <code>/connect</code>
            response contains the mask plus nonce, device, expiry and policy revision, signed with
            the panel&rsquo;s private key.</p></div>
            <div><h3>3 · Your SDK decides</h3><p class="muted small">The SDK verifies the signature,
            reads the bits and unlocks only the features it implements. A label or bit alone cannot
            enable code that does not exist client-side.</p></div>
            </div>
            <div class="divider"></div>
            <div class="btn-row"><span class="badge info">default mask <code>'.$mask.'</code></span>'
            .View::maskChips($mask).'</div></section>';

        $rows='';
        foreach ($flags as $bit=>$f) {
            $inDefault=($mask & (1 << $bit))!==0;
            $rows.='<tr><td><strong>'.$bit.'</strong></td><td class="mono dim">'.(1 << $bit).'</td>
                <td><input name="labels['.$bit.']" value="'.View::e($f['label']).'" maxlength="80"
                aria-label="Label for bit '.$bit.'"></td>
                <td><input name="descriptions['.$bit.']" value="'.View::e($f['description']).'"
                maxlength="255" aria-label="Description for bit '.$bit.'"></td>
                <td>'.($inDefault?'<span class="badge active">default</span>':'<span class="badge neutral">off</span>').'</td></tr>';
        }
        $table='<section class="card"><div class="card-head"><div><h2>Flag label registry</h2>
            <p>Rename the 31 bits to match the constants in your SDK. Labels are documentation only.</p></div>
            <span class="badge info">'.$setBits.' in default</span></div>
            <form method="post" action="/admin/features">'.View::csrf()
            .'<div class="table-wrap"><table><thead><tr><th>Bit</th><th>Value</th><th>Label</th>
            <th>Description</th><th>Default</th></tr></thead><tbody>'.$rows.'</tbody></table></div>'
            .'<div class="btn-row" style="margin-top:16px"><button type="submit">'.View::icon('check',16)
            .'Save flag labels</button><a class="btn ghost" href="/admin/settings">Change default mask</a>
            </div></form></section>';

        $stats='<div class="grid g-3">'
            .View::stat('Flags in default mask',$setBits,'of '.(FeatureFlags::MAX_BIT+1).' available bits','chip')
            .View::stat('Default mask value',$mask,'Signed when license mask is empty','gear')
            .View::stat('Licenses with overrides',$override,'Keys using their own bitmask','key')
            .'</div>';

        View::page('Feature flags',$stats.$explain.$table,true,
            'Understand, label and control the signed capability bits your SDK receives.');
    }
    public static function update(): never {
        Auth::requireStaff(); Auth::csrfCheck();
        $v=Http::form();
        $labels=is_array($v['labels']??null)?$v['labels']:[];
        $descs=is_array($v['descriptions']??null)?$v['descriptions']:[];
        $n=FeatureFlags::update($labels,$descs);
        Auth::flash($n>0?$n.' flag label(s) updated':'No flag labels changed');
        Http::redirect('/admin/features');
    }
}
