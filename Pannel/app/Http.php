<?php
declare(strict_types=1);
namespace TDCore\Panel;
final class Http {
    public static function path(): string {
        $path=parse_url((string)($_SERVER['REQUEST_URI']??'/'), PHP_URL_PATH);
        $p=rawurldecode(is_string($path)?$path:'/');
        if (str_contains($p, "\0") || str_contains($p,'\\') || str_contains($p,'..'))
            self::fail('BAD_PATH',400);
        return '/' . trim($p,'/');
    }
    public static function method(): string { return strtoupper((string)($_SERVER['REQUEST_METHOD']??'GET')); }
    public static function form(): array {
        $size=(int)($_SERVER['CONTENT_LENGTH']??0);
        if ($size>65536) self::fail('REQUEST_TOO_LARGE',413);
        if (str_contains((string)($_SERVER['CONTENT_TYPE']??''),'application/json')) {
            $body=file_get_contents('php://input',false,null,0,65537);
            $v=json_decode(is_string($body)?$body:'',true);
            if (!is_array($v) || array_is_list($v)) self::fail('INVALID_JSON',400);
            return $v;
        }
        return $_POST;
    }
    public static function json(array $value, int $status=200): never {
        http_response_code($status);
        header('Content-Type: application/json; charset=utf-8');
        header('Cache-Control: no-store, private');
        echo json_encode($value,JSON_UNESCAPED_SLASHES|JSON_INVALID_UTF8_SUBSTITUTE);
        exit;
    }
    public static function fail(string $reason, int $status=403): never {
        if (str_starts_with(self::pathSafe(),'/api/') || self::pathSafe()==='/connect')
            self::json(['status'=>false,'reason'=>$reason,'message'=>$reason],$status);
        http_response_code($status);
        header('Content-Type: text/plain; charset=utf-8');
        header('Cache-Control: no-store');
        echo $reason; exit;
    }
    private static function pathSafe(): string {
        $p=parse_url((string)($_SERVER['REQUEST_URI']??'/'),PHP_URL_PATH);
        return is_string($p)?$p:'/';
    }
    public static function redirect(string $to): never {
        header('Location: '.$to,true,303); exit;
    }
    public static function text(string $html, int $status=200): never {
        http_response_code($status); header('Content-Type: text/html; charset=utf-8');
        header('Cache-Control: no-store, private'); echo $html; exit;
    }
}
