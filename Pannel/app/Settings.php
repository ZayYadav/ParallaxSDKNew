<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Settings {
    public static function get(): array {
        $row=Database::one('SELECT * FROM td_settings WHERE id=1');
        if (!$row) throw new \RuntimeException('Schema missing: td_settings');
        return $row;
    }
    public static function update(array $v): void {
        $features=Validation::integer($v['default_features']??0,0,2147483647);
        $notice=mb_substr(trim((string)($v['notice']??'')),0,255);
        Database::run('UPDATE td_settings SET enabled=?,maintenance=?,notice=?,default_features=?,policy_version=policy_version+1 WHERE id=1',[
            !empty($v['enabled'])?1:0,!empty($v['maintenance'])?1:0,$notice,$features
        ]);
        Audit::write('settings_update','admin');
    }
}
