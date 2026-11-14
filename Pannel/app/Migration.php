<?php
declare(strict_types=1);
namespace TDCore\Panel;
/**
 * Idempotent schema upgrades.
 *
 * v2: username/password accounts, editable feature labels, login feed.
 * v3: owner/admin/reseller roles, referral codes, credit ledger, key ownership.
 * v4: auto-bind package/signer switches (global + per-key).
 *
 * Marker files above the document root keep normal requests free of DDL.
 */
final class Migration {
    public const VERSION=4;

    public static function ensure(): void {
        $base=dirname(__DIR__,2);
        if (!is_file($base.'/private/schema-v2.done')) self::v2();
        if (!is_file($base.'/private/schema-v3.done')) self::v3();
        if (!is_file($base.'/private/schema-v4.done')) self::v4();
    }

    private static function v2(): void {
        $base=dirname(__DIR__,2);
        $marker=$base.'/private/schema-v2.done';
        $db=Database::db();
        $db->exec('CREATE TABLE IF NOT EXISTS td_admins (
            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(60) CHARACTER SET ascii COLLATE ascii_bin NOT NULL UNIQUE,
            password_hash VARCHAR(255) NOT NULL,
            role ENUM(\'owner\',\'admin\') NOT NULL DEFAULT \'admin\',
            disabled TINYINT(1) NOT NULL DEFAULT 0,
            last_login_at DATETIME NULL,
            password_changed_at DATETIME NULL,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        $db->exec('CREATE TABLE IF NOT EXISTS td_feature_flags (
            bit TINYINT UNSIGNED NOT NULL PRIMARY KEY,
            label VARCHAR(80) NOT NULL DEFAULT \'\',
            description VARCHAR(255) NOT NULL DEFAULT \'\',
            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        $db->exec('CREATE TABLE IF NOT EXISTS td_login_events (
            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(60) NOT NULL DEFAULT \'\',
            ip_hash CHAR(64) CHARACTER SET ascii NOT NULL DEFAULT \'\',
            success TINYINT(1) NOT NULL DEFAULT 0,
            user_agent VARCHAR(200) NOT NULL DEFAULT \'\',
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            INDEX idx_login_events_created (created_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        if (AdminUser::count()===0) {
            $username=trim(Config::get('TD_ADMIN_USERNAME','admin'));
            if (!preg_match('/^[A-Za-z0-9][A-Za-z0-9_.-]{2,59}$/D',$username)) $username='admin';
            $hash=Config::get('TD_ADMIN_PASSWORD_HASH');
            if (trim($hash)!=='') {
                Database::run('INSERT IGNORE INTO td_admins(username,password_hash,role) VALUES(?,?,?)',
                    [$username,$hash,'owner']);
            }
        }

        FeatureFlags::seedRows();
        self::mark($marker);
    }

    private static function v3(): void {
        $base=dirname(__DIR__,2);
        $marker=$base.'/private/schema-v3.done';
        $db=Database::db();

        $db->exec("ALTER TABLE td_admins MODIFY role ENUM('owner','admin','reseller') NOT NULL DEFAULT 'reseller'");
        self::column('td_admins','parent_id',
            'ALTER TABLE td_admins ADD COLUMN parent_id BIGINT UNSIGNED NULL AFTER role');
        self::column('td_admins','credit_balance',
            'ALTER TABLE td_admins ADD COLUMN credit_balance BIGINT NOT NULL DEFAULT 0 AFTER disabled');
        self::column('td_admins','unlimited_credits',
            'ALTER TABLE td_admins ADD COLUMN unlimited_credits TINYINT(1) NOT NULL DEFAULT 0 AFTER credit_balance');
        self::column('td_admins','can_refer_admins',
            'ALTER TABLE td_admins ADD COLUMN can_refer_admins TINYINT(1) NOT NULL DEFAULT 0 AFTER unlimited_credits');
        self::column('td_admins','display_name',
            "ALTER TABLE td_admins ADD COLUMN display_name VARCHAR(80) NOT NULL DEFAULT '' AFTER username");
        self::index('td_admins','idx_accounts_parent','ALTER TABLE td_admins ADD INDEX idx_accounts_parent (parent_id)');

        self::column('td_licenses','owner_account_id',
            'ALTER TABLE td_licenses ADD COLUMN owner_account_id BIGINT UNSIGNED NULL AFTER feature_mask');
        self::index('td_licenses','idx_licenses_owner','ALTER TABLE td_licenses ADD INDEX idx_licenses_owner (owner_account_id)');

        self::column('td_settings','key_cost',
            'ALTER TABLE td_settings ADD COLUMN key_cost INT UNSIGNED NOT NULL DEFAULT 1 AFTER default_features');
        self::column('td_settings','signup_bonus',
            'ALTER TABLE td_settings ADD COLUMN signup_bonus INT UNSIGNED NOT NULL DEFAULT 0 AFTER key_cost');
        self::column('td_settings','referral_bonus',
            'ALTER TABLE td_settings ADD COLUMN referral_bonus INT UNSIGNED NOT NULL DEFAULT 0 AFTER signup_bonus');
        self::column('td_settings','open_registration',
            'ALTER TABLE td_settings ADD COLUMN open_registration TINYINT(1) NOT NULL DEFAULT 1 AFTER referral_bonus');

        $db->exec('CREATE TABLE IF NOT EXISTS td_referral_codes (
            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
            code CHAR(12) CHARACTER SET ascii COLLATE ascii_bin NOT NULL UNIQUE,
            parent_id BIGINT UNSIGNED NOT NULL,
            created_by BIGINT UNSIGNED NOT NULL,
            target_role ENUM(\'admin\',\'reseller\') NOT NULL DEFAULT \'reseller\',
            max_uses INT UNSIGNED NOT NULL DEFAULT 0,
            uses INT UNSIGNED NOT NULL DEFAULT 0,
            active TINYINT(1) NOT NULL DEFAULT 1,
            note VARCHAR(120) NOT NULL DEFAULT \'\',
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            last_used_at DATETIME NULL,
            INDEX idx_referral_parent (parent_id),
            INDEX idx_referral_creator (created_by)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        $db->exec('CREATE TABLE IF NOT EXISTS td_referrals (
            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
            code_id BIGINT UNSIGNED NOT NULL,
            referrer_id BIGINT UNSIGNED NOT NULL,
            account_id BIGINT UNSIGNED NOT NULL UNIQUE,
            role ENUM(\'admin\',\'reseller\') NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            INDEX idx_referrals_referrer (referrer_id)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        $db->exec('CREATE TABLE IF NOT EXISTS td_credit_ledger (
            id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
            account_id BIGINT UNSIGNED NOT NULL,
            delta BIGINT NOT NULL DEFAULT 0,
            balance_after BIGINT NULL,
            kind VARCHAR(32) NOT NULL,
            actor_id BIGINT UNSIGNED NULL,
            license_id BIGINT UNSIGNED NULL,
            note VARCHAR(240) NOT NULL DEFAULT \'\',
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            INDEX idx_credit_account (account_id,id),
            INDEX idx_credit_created (created_at)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');

        // Owners are unlimited; the first owner may manage admin referrals.
        Database::run("UPDATE td_admins SET unlimited_credits=1,can_refer_admins=1 WHERE role='owner'");

        // Staff accounts get a personal reseller referral link. Resellers may
        // only issue keys, so they do not receive one.
        foreach (Database::all("SELECT id FROM td_admins WHERE role<>'reseller'") as $row) {
            Referral::ensurePersonalCode((int)$row['id']);
        }

        self::mark($marker);
    }

    private static function v4(): void {
        $base=dirname(__DIR__,2);
        $marker=$base.'/private/schema-v4.done';
        /* Global defaults: auto-bind on. Per-key values: NULL=inherit global. */
        self::column('td_settings','auto_bind_package',
            'ALTER TABLE td_settings ADD COLUMN auto_bind_package TINYINT(1) NOT NULL DEFAULT 1 AFTER open_registration');
        self::column('td_settings','auto_bind_signature',
            'ALTER TABLE td_settings ADD COLUMN auto_bind_signature TINYINT(1) NOT NULL DEFAULT 1 AFTER auto_bind_package');
        self::column('td_license_rules','auto_bind_package',
            'ALTER TABLE td_license_rules ADD COLUMN auto_bind_package TINYINT(1) NULL DEFAULT NULL AFTER requests_per_minute');
        self::column('td_license_rules','auto_bind_signature',
            'ALTER TABLE td_license_rules ADD COLUMN auto_bind_signature TINYINT(1) NULL DEFAULT NULL AFTER auto_bind_package');
        self::mark($marker);
    }

    private static function mark(string $marker): void {
        if (!is_dir(dirname($marker))) @mkdir(dirname($marker),0700,true);
        @file_put_contents($marker,gmdate('c')."\n");
    }

    private static function column(string $table,string $column,string $ddl): void {
        $row=Database::one('SELECT 1 AS x FROM information_schema.COLUMNS
            WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=? AND COLUMN_NAME=?',[$table,$column]);
        if ($row) return;
        Database::db()->exec($ddl);
    }

    private static function index(string $table,string $index,string $ddl): void {
        $row=Database::one('SELECT 1 AS x FROM information_schema.STATISTICS
            WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=? AND INDEX_NAME=?',[$table,$index]);
        if ($row) return;
        Database::db()->exec($ddl);
    }
}
