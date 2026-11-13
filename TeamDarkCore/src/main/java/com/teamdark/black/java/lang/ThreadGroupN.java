package com.teamdark.black.java.lang;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("java.lang.ThreadGroup")
public interface ThreadGroupN {
    @BField
    java.lang.ThreadGroup[] groups();

    @BField
    Integer ngroups();

    @BField
    java.lang.ThreadGroup parent();
}
