<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Role, hierarchy and credit-permission rules shared by every controller. */
final class Account {
    public static function current(): ?array { return Auth::current(); }
    public static function role(): string { return Auth::role(); }
    public static function isOwner(): bool { return Auth::isOwner(); }
    public static function isAdmin(): bool { return self::role()==='admin'; }
    public static function isReseller(): bool { return self::role()==='reseller'; }
    public static function isStaff(): bool { return in_array(self::role(),['owner','admin'],true); }

    public static function roleLabel(string $role): string {
        return ['owner'=>'Owner','admin'=>'Admin','reseller'=>'Reseller'][$role]??ucfirst($role);
    }

    public static function isUnlimited(int $accountId): bool {
        $row=AdminUser::byId($accountId);
        if (!$row) return false;
        return $row['role']==='owner' || (int)$row['unlimited_credits']===1;
    }

    public static function isDescendant(int $childId,int $ancestorId): bool {
        if ($childId===$ancestorId) return true;
        $seen=[];
        $cursor=$childId;
        for ($i=0;$i<20;$i++) {
            if (isset($seen[$cursor])) return false;
            $seen[$cursor]=true;
            $row=AdminUser::byId($cursor);
            $parent=(int)($row['parent_id']??0);
            if ($parent<=0) return false;
            if ($parent===$ancestorId) return true;
            $cursor=$parent;
        }
        return false;
    }

    /** null means "every account" (owner); otherwise the visible subtree. */
    public static function scopeIds(int $actorId): ?array {
        $actor=AdminUser::byId($actorId);
        if (!$actor) return [];
        if ($actor['role']==='owner') return null;
        $ids=[$actorId];
        foreach (AdminUser::downline($actorId) as $row) $ids[]=(int)$row['id'];
        return $ids;
    }

    public static function canManage(int $actorId,int $targetId): bool {
        if ($actorId===$targetId) return false;
        $actor=AdminUser::byId($actorId); $target=AdminUser::byId($targetId);
        if (!$actor || !$target) return false;
        if ($target['role']==='owner') return false;
        if ($actor['role']==='owner') return true;
        if ($actor['role']!=='admin') return false;
        if (!self::isDescendant($targetId,$actorId)) return false;
        if ($target['role']==='admin' && (int)$actor['can_refer_admins']!==1) return false;
        return true;
    }

    public static function canCreateRole(int $actorId,string $role): bool {
        $actor=AdminUser::byId($actorId);
        if (!$actor || !in_array($role,['admin','reseller'],true)) return false;
        if ($actor['role']==='owner') return true;
        if ($actor['role']==='admin') {
            return $role==='reseller' || (int)$actor['can_refer_admins']===1;
        }
        return false;
    }

    public static function canGrant(int $actorId,int $targetId): bool {
        $actor=AdminUser::byId($actorId);
        if (!$actor || $actorId===$targetId) return false;
        if ($actor['role']==='owner') {
            return (AdminUser::byId($targetId)['role']??'owner')!=='owner';
        }
        if ($actor['role']!=='admin') return false;
        return self::isDescendant($targetId,$actorId);
    }

    public static function canReferRole(int $actorId,string $role): bool {
        $actor=AdminUser::byId($actorId);
        if (!$actor || !in_array($role,['admin','reseller'],true)) return false;
        if ($actor['role']==='owner') return true;
        if ($actor['role']==='admin') {
            return $role==='reseller' || (int)$actor['can_refer_admins']===1;
        }
        return $role==='reseller';
    }

    public static function parentOptions(): array {
        $me=(int)(Auth::current()['id']??0);
        if (self::isOwner()) {
            $rows=AdminUser::all();
            $out=[];
            foreach ($rows as $r) {
                if ((int)$r['id']===$me || $r['role']==='reseller') continue;
                $out[(int)$r['id']]=$r['username'].' · '.self::roleLabel((string)$r['role']);
            }
            return $out;
        }
        return [$me=>Auth::username().' · '.self::roleLabel(self::role())];
    }

    public static function targetOptions(): array {
        $me=(int)(Auth::current()['id']??0);
        $rows=self::isOwner()
            ? array_values(array_filter(AdminUser::all(),
                static fn(array $r): bool => $r['role']!=='owner'))
            : AdminUser::downline($me);
        $out=[];
        foreach ($rows as $r) {
            if ((int)$r['disabled']===1) continue;
            $out[(int)$r['id']]=$r['username'].' · '.self::roleLabel((string)$r['role']).
                ' · '.(self::isUnlimited((int)$r['id'])?'unlimited':((int)$r['credit_balance'].' cr'));
        }
        return $out;
    }

    /** Accounts the current actor may see in management screens. */
    public static function visibleAccounts(): array {
        $me=(int)(Auth::current()['id']??0);
        if (self::isOwner()) return AdminUser::all();
        $self=AdminUser::byId($me);
        if (!$self) return [];
        return array_merge([$self],AdminUser::downline($me));
    }

    public static function balanceLabel(array $row): string {
        return self::isUnlimited((int)$row['id'])?'∞':number_format((int)$row['credit_balance']);
    }
}
