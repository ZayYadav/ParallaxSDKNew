<?php
declare(strict_types=1);
namespace TDCore\Panel;
use PDO;
final class Database {
    private static ?PDO $pdo = null;
    public static function db(): PDO {
        if (self::$pdo) return self::$pdo;
        $dsn = sprintf('mysql:host=%s;port=%d;dbname=%s;charset=utf8mb4',
            Config::need('TD_DB_HOST'), (int)Config::get('TD_DB_PORT','3306'),
            Config::need('TD_DB_NAME'));
        return self::$pdo = new PDO($dsn, Config::need('TD_DB_USER'),
            Config::need('TD_DB_PASSWORD'), [
                PDO::ATTR_ERRMODE=>PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE=>PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES=>false, PDO::ATTR_TIMEOUT=>5
            ]);
    }
    public static function one(string $sql, array $params=[]): ?array {
        $s=self::db()->prepare($sql); $s->execute($params);
        $v=$s->fetch(); return $v ?: null;
    }
    public static function all(string $sql, array $params=[]): array {
        $s=self::db()->prepare($sql); $s->execute($params); return $s->fetchAll();
    }
    public static function run(string $sql, array $params=[]): int {
        $s=self::db()->prepare($sql); $s->execute($params); return $s->rowCount();
    }
    public static function transaction(callable $fn): mixed {
        $db=self::db(); $db->beginTransaction();
        try { $result=$fn(); $db->commit(); return $result; }
        catch (\Throwable $e) { if ($db->inTransaction()) $db->rollBack(); throw $e; }
    }
}
