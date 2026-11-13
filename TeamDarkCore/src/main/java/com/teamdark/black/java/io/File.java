package com.teamdark.black.java.io;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("java.io.File")
public interface File {
    @BStaticField
    Object fs();
}
