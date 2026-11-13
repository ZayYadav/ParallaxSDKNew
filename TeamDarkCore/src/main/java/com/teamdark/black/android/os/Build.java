package com.teamdark.black.android.os;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("android.os.Build")
public interface Build {
    @BStaticField
    String BOARD();
    
    @BStaticField
    String BRAND();
    
    @BStaticField
    String DEVICE();
    
    @BStaticField
    String DISPLAY();
    
    @BStaticField
    String HOST();
    
    @BStaticField
    String ID();
    
    @BStaticField
    String MANUFACTURER();
    
    @BStaticField
    String MODEL();
    
    @BStaticField
    String PRODUCT();
    
    @BStaticField
    String TAGS();
    
    @BStaticField
    String TYPE();
    
    @BStaticField
    String USER();
}