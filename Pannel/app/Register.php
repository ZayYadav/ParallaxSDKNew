<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Public self-registration through a referral link. */
final class Register {
    public static function code(?string $raw): ?array {
        $code=trim((string)$raw);
        if ($code==='') return null;
        return Referral::byCode($code);
    }

    public static function settings(): array {
        return Settings::get();
    }

    /** Create the account, consume the code and pay owner-funded bonuses atomically. */
    public static function create(?string $code,string $username,string $password,
        string $confirm,string $displayName=''): array {
        RateLimit::hit('register-ip:'.Crypto::ipHash(),5,3600);
        if ($password!==$confirm) throw new Deny('PASSWORD_MISMATCH',422);
        $settings=self::settings();
        $ref=$code!==null && trim($code)!=='' ? Referral::byCode($code) : null;
        if ($code!==null && trim($code)!=='' && !$ref) throw new Deny('REFERRAL_INVALID',422);
        if (!$ref && (int)$settings['open_registration']!==1)
            throw new Deny('REGISTRATION_CLOSED',403);

        $result=Database::transaction(static function () use ($ref,$username,$password,$displayName,$settings): array {
            $role='reseller'; $parentId=0; $codeId=null; $referrerId=null;
            if ($ref) {
                $codeRow=Referral::consumeLocked((string)$ref['code']);
                $role=(string)$codeRow['target_role'];
                $parentId=(int)$codeRow['parent_id'];
                $codeId=(int)$codeRow['id'];
                $referrerId=$parentId;
                $parent=AdminUser::byId($parentId);
                if (!$parent || (int)$parent['disabled']===1) throw new Deny('REFERRAL_INVALID',422);
            }
            $id=AdminUser::insertLocked($username,$password,$role,$parentId,$displayName);
            if ($role!=='reseller') Referral::ensurePersonalCode($id);
            if ($ref) {
                Database::run('INSERT INTO td_referrals(code_id,referrer_id,account_id,role)
                    VALUES(?,?,?,?)',[$codeId,$referrerId,$id,$role]);
            }
            Credit::ownerBonusWithin($id,(int)$settings['signup_bonus'],'signup_bonus',
                'welcome bonus');
            if ($referrerId) {
                Credit::ownerBonusWithin($referrerId,(int)$settings['referral_bonus'],
                    'referral_bonus','new signup: '.$username);
            }
            Audit::write('account_registered','anonymous',null,
                $username.' ('.$role.')'.($ref?' via '.$ref['code']:' open registration'));
            return ['id'=>$id,'role'=>$role,'username'=>$username,'parent_id'=>$parentId];
        });
        return $result;
    }
}
