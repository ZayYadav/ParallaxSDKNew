<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Deny extends \RuntimeException {
    public function __construct(string $reason, public readonly int $status=403) {
        parent::__construct($reason);
    }
}
