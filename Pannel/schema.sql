-- TDCore Licensing Panel / MySQL 8+ or MariaDB 10.6+; utf8mb4
-- Keep this file outside the public web root after installation.
CREATE TABLE IF NOT EXISTS td_settings (
 id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
 enabled TINYINT(1) NOT NULL DEFAULT 0,
 maintenance TINYINT(1) NOT NULL DEFAULT 1,
 notice VARCHAR(255) NOT NULL DEFAULT 'Service not configured',
 policy_version BIGINT UNSIGNED NOT NULL DEFAULT 1,
 default_features BIGINT UNSIGNED NOT NULL DEFAULT 0,
 key_cost INT UNSIGNED NOT NULL DEFAULT 1,
 signup_bonus INT UNSIGNED NOT NULL DEFAULT 0,
 referral_bonus INT UNSIGNED NOT NULL DEFAULT 0,
 open_registration TINYINT(1) NOT NULL DEFAULT 1,
 auto_bind_package TINYINT(1) NOT NULL DEFAULT 1,
 auto_bind_signature TINYINT(1) NOT NULL DEFAULT 1,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO td_settings (id) VALUES (1) ON DUPLICATE KEY UPDATE id=id;

CREATE TABLE IF NOT EXISTS td_licenses (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 key_digest CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL UNIQUE,
 label VARCHAR(120) NOT NULL DEFAULT '',
 status ENUM('active','disabled','revoked') NOT NULL DEFAULT 'active',
 expires_at DATETIME NOT NULL,
 max_devices SMALLINT UNSIGNED NOT NULL DEFAULT 1,
 package_name VARCHAR(180) NOT NULL DEFAULT '',
 signing_sha256 CHAR(64) CHARACTER SET ascii NOT NULL DEFAULT '',
 feature_mask BIGINT UNSIGNED NOT NULL DEFAULT 0,
 owner_account_id BIGINT UNSIGNED NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 INDEX idx_license_expiry (status, expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_devices (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 license_id BIGINT UNSIGNED NOT NULL,
 device_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
 first_seen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 last_seen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 UNIQUE KEY uniq_license_device (license_id, device_hash),
 FOREIGN KEY (license_id) REFERENCES td_licenses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_blocked_devices (
 device_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin PRIMARY KEY,
 reason VARCHAR(200) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_audit (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 action VARCHAR(60) NOT NULL,
 actor VARCHAR(60) NOT NULL DEFAULT 'system',
 license_id BIGINT UNSIGNED NULL,
 ip_hash CHAR(64) CHARACTER SET ascii NOT NULL DEFAULT '',
 details VARCHAR(500) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_audit_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_login_attempts (
 ip_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin PRIMARY KEY,
 attempts SMALLINT UNSIGNED NOT NULL DEFAULT 0,
 window_start DATETIME NOT NULL,
 blocked_until DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- New modular route/service policies. Idempotent on repeated imports.
CREATE TABLE IF NOT EXISTS td_license_rules (
 license_id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
 package_rule ENUM('required','if_present','disabled') NOT NULL DEFAULT 'required',
 signer_rule ENUM('required','if_present','disabled') NOT NULL DEFAULT 'required',
 device_rule ENUM('bind','unrestricted') NOT NULL DEFAULT 'bind',
 requests_per_minute SMALLINT UNSIGNED NOT NULL DEFAULT 30,
 auto_bind_package TINYINT(1) NULL DEFAULT NULL,
 auto_bind_signature TINYINT(1) NULL DEFAULT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY (license_id) REFERENCES td_licenses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_rate_buckets (
 bucket_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin PRIMARY KEY,
 window_start BIGINT UNSIGNED NOT NULL,
 hits INT UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_used_nonces (
 license_id BIGINT UNSIGNED NOT NULL,
 nonce_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (license_id,nonce_hash),
 INDEX idx_nonce_age(created_at),
 FOREIGN KEY (license_id) REFERENCES td_licenses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------------
-- v2: username/password administrators, editable feature labels, login feed.
-- The first owner is seeded from TD_ADMIN_USERNAME + TD_ADMIN_PASSWORD_HASH by
-- app/Migration.php on first request, so no plaintext password lives in SQL.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS td_admins (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(60) CHARACTER SET ascii COLLATE ascii_bin NOT NULL UNIQUE,
 display_name VARCHAR(80) NOT NULL DEFAULT '',
 password_hash VARCHAR(255) NOT NULL,
 role ENUM('owner','admin','reseller') NOT NULL DEFAULT 'reseller',
 parent_id BIGINT UNSIGNED NULL,
 disabled TINYINT(1) NOT NULL DEFAULT 0,
 credit_balance BIGINT NOT NULL DEFAULT 0,
 unlimited_credits TINYINT(1) NOT NULL DEFAULT 0,
 can_refer_admins TINYINT(1) NOT NULL DEFAULT 0,
 last_login_at DATETIME NULL,
 password_changed_at DATETIME NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_accounts_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_feature_flags (
 bit TINYINT UNSIGNED NOT NULL PRIMARY KEY,
 label VARCHAR(80) NOT NULL DEFAULT '',
 description VARCHAR(255) NOT NULL DEFAULT '',
 updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO td_feature_flags (bit,label,description) VALUES
 (0,'Feature 0','SDK-defined capability bit 0. Rename it to match your SDK constant.'),
 (1,'Feature 1','SDK-defined capability bit 1. Rename it to match your SDK constant.'),
 (2,'Feature 2','SDK-defined capability bit 2. Rename it to match your SDK constant.'),
 (3,'Feature 3','SDK-defined capability bit 3. Rename it to match your SDK constant.'),
 (4,'Feature 4','SDK-defined capability bit 4. Rename it to match your SDK constant.'),
 (5,'Feature 5','SDK-defined capability bit 5. Rename it to match your SDK constant.'),
 (6,'Feature 6','SDK-defined capability bit 6. Rename it to match your SDK constant.'),
 (7,'Feature 7','SDK-defined capability bit 7. Rename it to match your SDK constant.'),
 (8,'Feature 8','SDK-defined capability bit 8. Rename it to match your SDK constant.'),
 (9,'Feature 9','SDK-defined capability bit 9. Rename it to match your SDK constant.'),
 (10,'Feature 10','SDK-defined capability bit 10. Rename it to match your SDK constant.'),
 (11,'Feature 11','SDK-defined capability bit 11. Rename it to match your SDK constant.'),
 (12,'Feature 12','SDK-defined capability bit 12. Rename it to match your SDK constant.'),
 (13,'Feature 13','SDK-defined capability bit 13. Rename it to match your SDK constant.'),
 (14,'Feature 14','SDK-defined capability bit 14. Rename it to match your SDK constant.'),
 (15,'Feature 15','SDK-defined capability bit 15. Rename it to match your SDK constant.'),
 (16,'Feature 16','SDK-defined capability bit 16. Rename it to match your SDK constant.'),
 (17,'Feature 17','SDK-defined capability bit 17. Rename it to match your SDK constant.'),
 (18,'Feature 18','SDK-defined capability bit 18. Rename it to match your SDK constant.'),
 (19,'Feature 19','SDK-defined capability bit 19. Rename it to match your SDK constant.'),
 (20,'Feature 20','SDK-defined capability bit 20. Rename it to match your SDK constant.'),
 (21,'Feature 21','SDK-defined capability bit 21. Rename it to match your SDK constant.'),
 (22,'Feature 22','SDK-defined capability bit 22. Rename it to match your SDK constant.'),
 (23,'Feature 23','SDK-defined capability bit 23. Rename it to match your SDK constant.'),
 (24,'Feature 24','SDK-defined capability bit 24. Rename it to match your SDK constant.'),
 (25,'Feature 25','SDK-defined capability bit 25. Rename it to match your SDK constant.'),
 (26,'Feature 26','SDK-defined capability bit 26. Rename it to match your SDK constant.'),
 (27,'Feature 27','SDK-defined capability bit 27. Rename it to match your SDK constant.'),
 (28,'Feature 28','SDK-defined capability bit 28. Rename it to match your SDK constant.'),
 (29,'Feature 29','SDK-defined capability bit 29. Rename it to match your SDK constant.'),
 (30,'Feature 30','SDK-defined capability bit 30. Rename it to match your SDK constant.');

CREATE TABLE IF NOT EXISTS td_login_events (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 username VARCHAR(60) NOT NULL DEFAULT '',
 ip_hash CHAR(64) CHARACTER SET ascii NOT NULL DEFAULT '',
 success TINYINT(1) NOT NULL DEFAULT 0,
 user_agent VARCHAR(200) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_login_events_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------------
-- v3: owner/admin/reseller hierarchy, referral links, credit ledger.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS td_referral_codes (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 code CHAR(12) CHARACTER SET ascii COLLATE ascii_bin NOT NULL UNIQUE,
 parent_id BIGINT UNSIGNED NOT NULL,
 created_by BIGINT UNSIGNED NOT NULL,
 target_role ENUM('admin','reseller') NOT NULL DEFAULT 'reseller',
 max_uses INT UNSIGNED NOT NULL DEFAULT 0,
 uses INT UNSIGNED NOT NULL DEFAULT 0,
 active TINYINT(1) NOT NULL DEFAULT 1,
 note VARCHAR(120) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 last_used_at DATETIME NULL,
 INDEX idx_referral_parent (parent_id),
 INDEX idx_referral_creator (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_referrals (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 code_id BIGINT UNSIGNED NOT NULL,
 referrer_id BIGINT UNSIGNED NOT NULL,
 account_id BIGINT UNSIGNED NOT NULL UNIQUE,
 role ENUM('admin','reseller') NOT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_referrals_referrer (referrer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS td_credit_ledger (
 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 account_id BIGINT UNSIGNED NOT NULL,
 delta BIGINT NOT NULL DEFAULT 0,
 balance_after BIGINT NULL,
 kind VARCHAR(32) NOT NULL,
 actor_id BIGINT UNSIGNED NULL,
 license_id BIGINT UNSIGNED NULL,
 note VARCHAR(240) NOT NULL DEFAULT '',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 INDEX idx_credit_account (account_id,id),
 INDEX idx_credit_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
