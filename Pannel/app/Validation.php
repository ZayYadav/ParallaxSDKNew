<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Validation {
    public static function check(array $v): array {
        $key=trim((string)($v['user_key']??$v['license_key']??$v['key']??''));
        $device=strtoupper(trim((string)($v['serial']??$v['device_id']??'')));
        $pkg=trim((string)($v['pkg']??$v['package_name']??''));
        $cert=strtoupper(trim((string)($v['signing_sha256']??'')));
        $nonce=strtoupper(trim((string)($v['nonce']??'')));
        if (!preg_match('/^TD-[A-F0-9]{64}$/D',$key) ||
            !preg_match('/^[A-F0-9]{32,64}$/D',$device) ||
            !preg_match('/^[A-Za-z][A-Za-z0-9_.]{2,179}$/D',$pkg) ||
            !preg_match('/^[A-F0-9]{64}$/D',$cert) ||
            !preg_match('/^[A-F0-9]{32}$/D',$nonce))
            throw new Deny('INVALID_REQUEST',400);
        return compact('key','device','pkg','cert','nonce');
    }
    public static function integer(mixed $v, int $min, int $max): int {
        if (!is_scalar($v)) throw new Deny('INVALID_INTEGER',422);
        $s=(string)$v;
        if (!preg_match('/^[0-9]{1,12}$/D',$s)) throw new Deny('INVALID_INTEGER',422);
        $n=(int)$s;
        if ($n<$min || $n>$max) throw new Deny('VALUE_OUT_OF_RANGE',422);
        return $n;
    }
    public static function rule(mixed $v): string {
        $s=(string)$v;
        if (!in_array($s,['required','if_present','disabled'],true))
            throw new Deny('INVALID_ENFORCEMENT_RULE',422);
        return $s;
    }
    public static function deviceRule(mixed $v): string {
        $s=(string)$v;
        if (!in_array($s,['bind','unrestricted'],true))
            throw new Deny('INVALID_DEVICE_RULE',422);
        return $s;
    }
    public static function package(mixed $v): string {
        $s=trim((string)$v);
        if ($s!=='' && !preg_match('/^[a-zA-Z][a-zA-Z0-9_.]{2,179}$/D',$s))
            throw new Deny('INVALID_PACKAGE',422);
        return $s;
    }
    public static function signer(mixed $v): string {
        $s=strtoupper(trim((string)$v));
        if ($s!=='' && !preg_match('/^[A-F0-9]{64}$/D',$s))
            throw new Deny('INVALID_SIGNER_SHA256',422);
        return $s;
    }
    /** Read either shorthand feature_bits[] checkboxes or a numeric feature_mask. */
    public static function featureMask(array $v, int $fallback=0): int {
        if (isset($v['feature_selection'])) {
            $feature=0;
            $bits=$v['feature_bits']??[];
            if (!is_array($bits) || count($bits)>31) throw new Deny('INVALID_FEATURE_SET',422);
            foreach ($bits as $bit) {
                $index=self::integer($bit,0,30);
                $feature |= (1 << $index);
            }
            return $feature;
        }
        return self::integer($v['feature_mask']??$fallback,0,2147483647);
    }
    /** Per-key auto-bind switch: inherit (NULL) follows the global setting. */
    public static function autoBindRule(mixed $v): ?int {
        $s=(string)($v??'inherit');
        return match ($s) {
            'inherit'=>null,
            'on'=>1,
            'off'=>0,
            default=>throw new Deny('INVALID_AUTO_BIND_RULE',422),
        };
    }
    public static function choices(array $v): array {
        $package=self::rule($v['package_rule']??'required');
        $signer=self::rule($v['signer_rule']??'required');
        $device=self::deviceRule($v['device_rule']??'bind');
        $max=self::integer($v['max_devices']??1,1,1000);
        $perMin=self::integer($v['requests_per_minute']??30,1,600);
        $feature=self::featureMask($v);
        $pkg=self::package($v['package_name']??'');
        $cert=self::signer($v['signing_sha256']??'');
        $abPkg=self::autoBindRule($v['auto_bind_package']??'inherit');
        $abSig=self::autoBindRule($v['auto_bind_signature']??'inherit');
        if ($package==='required' && $pkg==='') throw new Deny('PACKAGE_REQUIRED',422);
        if ($signer==='required' && $cert==='') throw new Deny('SIGNER_REQUIRED',422);
        return compact('package','signer','device','max','perMin','feature','pkg','cert','abPkg','abSig');
    }
}
