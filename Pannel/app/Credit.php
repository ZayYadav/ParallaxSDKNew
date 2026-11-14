<?php
declare(strict_types=1);
namespace TDCore\Panel;
/** Atomic credit transfers, key charges and the audit ledger. */
final class Credit {
    public const MAX_GRANT=1000000000;

    public static function row(int $accountId): ?array {
        return Database::one('SELECT id,username,role,credit_balance,unlimited_credits,disabled
            FROM td_admins WHERE id=?',[$accountId]);
    }

    public static function balance(int $accountId): int {
        $row=self::row($accountId);
        return (int)($row['credit_balance']??0);
    }

    public static function keyCost(): int {
        $row=Database::one('SELECT key_cost FROM td_settings WHERE id=1');
        return max(0,min(100000,(int)($row['key_cost']??1)));
    }

    public static function amount(mixed $raw): int {
        if (!is_scalar($raw) || !preg_match('/^[0-9]{1,10}$/D',(string)$raw))
            throw new Deny('INVALID_AMOUNT',422);
        $n=(int)$raw;
        if ($n<1 || $n>self::MAX_GRANT) throw new Deny('INVALID_AMOUNT',422);
        return $n;
    }

    public static function grant(int $actorId,int $targetId,int $amount,string $note=''): void {
        if (!Account::canGrant($actorId,$targetId)) throw new Deny('CREDIT_FORBIDDEN',403);
        $amount=max(1,min(self::MAX_GRANT,$amount));
        Database::transaction(static function () use ($actorId,$targetId,$amount,$note): void {
            $actor=Database::one('SELECT * FROM td_admins WHERE id=? FOR UPDATE',[$actorId]);
            $target=Database::one('SELECT * FROM td_admins WHERE id=? FOR UPDATE',[$targetId]);
            if (!$actor || !$target) throw new Deny('ACCOUNT_NOT_FOUND',404);
            $actorUnlimited=Account::isUnlimited($actorId);
            if (!$actorUnlimited && (int)$actor['credit_balance'] < $amount)
                throw new Deny('INSUFFICIENT_CREDIT',422);

            if (!$actorUnlimited) {
                Database::run('UPDATE td_admins SET credit_balance=credit_balance-? WHERE id=?',
                    [$amount,$actorId]);
                $actorAfter=(int)$actor['credit_balance']-$amount;
                self::writeLedger($actorId,-$amount,$actorAfter,'grant_out',$actorId,null,
                    'to '.$target['username'].($note!==''?' · '.$note:''));
            }
            Database::run('UPDATE td_admins SET credit_balance=credit_balance+? WHERE id=?',
                [$amount,$targetId]);
            $targetUnlimited=Account::isUnlimited($targetId);
            $targetAfter=(int)$target['credit_balance']+$amount;
            self::writeLedger($targetId,$amount,$targetUnlimited?null:$targetAfter,'grant_in',
                $actorId,null,'from '.$actor['username'].($note!==''?' · '.$note:''));
            Audit::write('credit_grant','panel',null,
                $actor['username'].' → '.$target['username'].' · '.$amount.' cr'
                .($note!==''?' · '.$note:''));
        });
    }

    /** Owner-funded bonus inside an existing transaction. */
    public static function ownerBonusWithin(int $targetId,int $amount,string $kind,
        string $note=''): void {
        if ($amount<=0) return;
        $owner=Database::one("SELECT id FROM td_admins WHERE role='owner' ORDER BY id ASC LIMIT 1");
        if (!$owner) return;
        $target=Database::one('SELECT * FROM td_admins WHERE id=? FOR UPDATE',[$targetId]);
        if (!$target) return;
        Database::run('UPDATE td_admins SET credit_balance=credit_balance+? WHERE id=?',
            [$amount,$targetId]);
        self::writeLedger($targetId,$amount,
            Account::isUnlimited($targetId)?null:(int)$target['credit_balance']+$amount,
            $kind,(int)$owner['id'],null,$note);
    }

    /** Owner-funded bonus (owner is unlimited, so the owner balance is untouched). */
    public static function ownerBonus(int $targetId,int $amount,string $kind,string $note=''): void {
        if ($amount<=0) return;
        Database::transaction(static function () use ($targetId,$amount,$kind,$note): void {
            self::ownerBonusWithin($targetId,$amount,$kind,$note);
        });
    }

    /** Charge an account inside an existing transaction. */
    public static function spendWithin(int $accountId,int $amount,string $kind,
        string $note='',?int $licenseId=null): void {
        if ($amount<=0) return;
        $row=Database::one('SELECT * FROM td_admins WHERE id=? FOR UPDATE',[$accountId]);
        if (!$row) throw new Deny('ACCOUNT_NOT_FOUND',404);
        if (Account::isUnlimited($accountId)) {
            self::writeLedger($accountId,0,null,'key_issue',$accountId,$licenseId,
                'unlimited · '.$note);
            return;
        }
        if ((int)$row['credit_balance'] < $amount) throw new Deny('INSUFFICIENT_CREDIT',422);
        Database::run('UPDATE td_admins SET credit_balance=credit_balance-? WHERE id=?',
            [$amount,$accountId]);
        self::writeLedger($accountId,-$amount,(int)$row['credit_balance']-$amount,
            $kind,$accountId,$licenseId,$note);
    }

    public static function spend(int $accountId,int $amount,string $kind,
        string $note='',?int $licenseId=null): void {
        Database::transaction(static function () use ($accountId,$amount,$kind,$note,$licenseId): void {
            self::spendWithin($accountId,$amount,$kind,$note,$licenseId);
        });
    }

    public static function writeLedger(int $accountId,int $delta,?int $balanceAfter,
        string $kind,?int $actorId=null,?int $licenseId=null,string $note=''): void {
        Database::run('INSERT INTO td_credit_ledger
            (account_id,delta,balance_after,kind,actor_id,license_id,note)
            VALUES(?,?,?,?,?,?,?)',[
            $accountId,$delta,$balanceAfter,$kind,$actorId,$licenseId,mb_substr($note,0,240)
        ]);
    }

    public static function ledger(int $accountId,int $limit=50): array {
        $limit=max(1,min(200,$limit));
        return Database::all("SELECT l.*,a.username AS actor_name FROM td_credit_ledger l
            LEFT JOIN td_admins a ON a.id=l.actor_id
            WHERE l.account_id=? ORDER BY l.id DESC LIMIT $limit",[$accountId]);
    }

    public static function recentAll(int $limit=100): array {
        $limit=max(1,min(200,$limit));
        return Database::all("SELECT l.*,a.username AS actor_name,t.username AS target_name
            FROM td_credit_ledger l
            LEFT JOIN td_admins a ON a.id=l.actor_id
            LEFT JOIN td_admins t ON t.id=l.account_id
            ORDER BY l.id DESC LIMIT $limit");
    }

    /** Ledger rows for a subtree (null = every account). */
    public static function recentFor(?array $ids,int $limit=100): array {
        if ($ids===null) return self::recentAll($limit);
        if (!$ids) return [];
        $limit=max(1,min(200,$limit));
        $in=implode(',',array_fill(0,count($ids),'?'));
        return Database::all("SELECT l.*,a.username AS actor_name,t.username AS target_name
            FROM td_credit_ledger l
            LEFT JOIN td_admins a ON a.id=l.actor_id
            LEFT JOIN td_admins t ON t.id=l.account_id
            WHERE l.account_id IN ($in) OR l.actor_id IN ($in)
            ORDER BY l.id DESC LIMIT $limit",array_merge($ids,$ids));
    }

    public static function totalInCirculation(): int {
        $row=Database::one("SELECT COALESCE(SUM(credit_balance),0) AS n FROM td_admins
            WHERE role<>'owner' AND unlimited_credits=0");
        return (int)($row['n']??0);
    }
}
