// IRequestPermissionsResult.aidl
package com.teamdark.core.core.system.am;

interface IRequestPermissionsResult {
    boolean onResult(int requestCode,in String[] permissions,in int[] grantResults);
}