<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class RateLimit {
    public static function hit(string $scope, int $max, int $window=60): void {
        $hash=Crypto::digest('rate:' . $scope);
        Database::transaction(static function () use ($hash,$max,$window): void {
            Database::run('INSERT IGNORE INTO td_rate_buckets(bucket_hash,window_start,hits) VALUES(?,?,0)',
                [$hash,time()]);
            $row=Database::one('SELECT window_start,hits FROM td_rate_buckets WHERE bucket_hash=? FOR UPDATE',[$hash]);
            if (!$row) throw new \RuntimeException('Rate limit unavailable');
            $now=time(); $fresh=((int)$row['window_start'] <= $now-$window);
            $hits=$fresh ? 1 : (int)$row['hits']+1;
            if ($hits > $max) throw new Deny('RATE_LIMITED',429);
            Database::run('UPDATE td_rate_buckets SET window_start=?,hits=? WHERE bucket_hash=?',
                [$fresh?$now:(int)$row['window_start'],$hits,$hash]);
        });
    }
}
