<?php
declare(strict_types=1);
namespace TDCore\Panel;
/**
 * Labels for the 31 signed SDK feature bits. Bit N has the value 2^N.
 * The labels are documentation only: the SDK must implement the gate.
 */
final class FeatureFlags {
    public const MAX_BIT=30;

    public static function seedRows(): void {
        for ($i=0;$i<=self::MAX_BIT;$i++) {
            Database::run('INSERT IGNORE INTO td_feature_flags(bit,label,description) VALUES(?,?,?)',
                [$i,'Feature '.$i,'SDK-defined capability bit '.$i.'. Rename it to match your SDK constant.']);
        }
    }
    public static function all(): array {
        $rows=Database::all('SELECT bit,label,description FROM td_feature_flags ORDER BY bit ASC');
        $out=[];
        for ($i=0;$i<=self::MAX_BIT;$i++) {
            $out[$i]=['bit'=>$i,'label'=>'Feature '.$i,
                'description'=>'SDK-defined capability bit '.$i.'. Rename it to match your SDK constant.'];
        }
        foreach ($rows as $r) {
            $bit=(int)$r['bit'];
            if ($bit<0 || $bit>self::MAX_BIT) continue;
            if (trim((string)$r['label'])!=='') $out[$bit]['label']=(string)$r['label'];
            if (trim((string)$r['description'])!=='') $out[$bit]['description']=(string)$r['description'];
        }
        return $out;
    }
    public static function label(int $bit): string {
        if ($bit<0 || $bit>self::MAX_BIT) return 'Bit '.$bit;
        $r=Database::one('SELECT label FROM td_feature_flags WHERE bit=?',[$bit]);
        $label=trim((string)($r['label']??''));
        return $label!==''?$label:'Feature '.$bit;
    }
    public static function update(array $labels, array $descriptions): int {
        $flags=self::all(); $changed=0;
        foreach ($flags as $bit=>$def) {
            $label=trim((string)($labels[$bit]??$def['label']));
            $desc=trim((string)($descriptions[$bit]??$def['description']));
            $label=mb_substr(preg_replace('/[\x00-\x1F\x7F]/u','',$label)??'',0,80);
            $desc=mb_substr(preg_replace('/[\x00-\x1F\x7F]/u','',$desc)??'',0,255);
            if ($label==='') $label='Feature '.$bit;
            if ($desc==='') $desc='SDK-defined capability bit '.$bit.'.';
            if ($label!==$def['label'] || $desc!==$def['description']) {
                Database::run('INSERT INTO td_feature_flags(bit,label,description) VALUES(?,?,?)
                    ON DUPLICATE KEY UPDATE label=VALUES(label),description=VALUES(description)',
                    [$bit,$label,$desc]);
                $changed++;
            }
        }
        if ($changed>0) Audit::write('feature_flags_update','panel',null,$changed.' label(s) updated');
        return $changed;
    }
    /** Human labels for the bits present in a mask (empty when mask is 0). */
    public static function describe(int $mask): array {
        $out=[];
        for ($i=0;$i<=self::MAX_BIT;$i++) {
            if (($mask & (1 << $i))!==0) $out[]=['bit'=>$i,'label'=>self::label($i)];
        }
        return $out;
    }
}
