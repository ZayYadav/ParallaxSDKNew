<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Router {
    private array $routes=[];
    public function add(string $method, string $pattern, callable $handler): self {
        $this->routes[]=[strtoupper($method),$pattern,$handler]; return $this;
    }
    public function dispatch(): never {
        $path=Http::path(); $method=Http::method(); $allowed=[];
        $parts=explode('/',trim($path,'/'));
        foreach ($this->routes as [$m,$pattern,$fn]) {
            $fragments=explode('/',trim($pattern,'/'));
            if (count($parts)!==count($fragments)) continue;
            $args=[]; $matched=true;
            foreach ($fragments as $i=>$token) {
                if (str_starts_with($token,':')) {
                    if (!preg_match('/^[0-9]+$/D',$parts[$i])) {$matched=false;break;}
                    $args[substr($token,1)]=(int)$parts[$i];
                } elseif ($token!==$parts[$i]) { $matched=false;break; }
            }
            if (!$matched) continue;
            if ($m!==$method) {$allowed[]=$m;continue;}
            $fn($args); Http::fail('INTERNAL_ERROR',500);
        }
        if ($allowed) { header('Allow: '.implode(', ',array_unique($allowed))); Http::fail('METHOD_NOT_ALLOWED',405); }
        Http::fail('ROUTE_NOT_FOUND',404);
    }
}
