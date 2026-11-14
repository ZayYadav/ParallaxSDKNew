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
        $features=Validation::featureMask($v,0);
        $notice=mb_substr(trim((string)($v['notice']??'')),0,255);
        $keyCost=self::bounded($v['key_cost']??1,0,100000);
        $signupBonus=self::bounded($v['signup_bonus']??0,0,100000);
        $referralBonus=self::bounded($v['referral_bonus']??0,0,100000);
        $open=!empty($v['open_registration'])?1:0;
        Database::run('UPDATE td_settings SET enabled=?,maintenance=?,notice=?,default_features=?,
            key_cost=?,signup_bonus=?,referral_bonus=?,open_registration=?,
            policy_version=policy_version+1 WHERE id=1',[
            !empty($v['enabled'])?1:0,!empty($v['maintenance'])?1:0,$notice,$features,
            $keyCost,$signupBonus,$referralBonus,$open
        ]);
        Audit::write('settings_update','panel',null,
            'default features '.$features.' · key cost '.$keyCost.' · signup '.$signupBonus.
            ' · referral '.$referralBonus.' · open registration '.$open);
    }
    /** Emergency stop: revoke every license that is not already revoked. */
    public static function revokeAll(): int {
        $n=Database::run("UPDATE td_licenses SET status='revoked' WHERE status<>'revoked'");
        Database::run('UPDATE td_settings SET policy_version=policy_version+1 WHERE id=1');
        Audit::write('licenses_revoked_all','panel',null,$n.' license(s) revoked');
        return $n;
    }
    public static function expiringSoon(): int {
        $r=Database::one("SELECT COUNT(*) AS n FROM td_licenses
            WHERE status='active' AND expires_at>UTC_TIMESTAMP()
            AND expires_at<=UTC_TIMESTAMP()+INTERVAL 7 DAY");
        return (int)($r['n']??0);
    }
    private static function bounded(mixed $value,int $min,int $max): int {
        if (!is_scalar($value) || !preg_match('/^[0-9]{1,9}$/D',(string)$value))
            throw new Deny('INVALID_INTEGER',422);
        $n=(int)$value;
        if ($n<$min || $n>$max) throw new Deny('VALUE_OUT_OF_RANGE',422);
        return $n;
    }
}
