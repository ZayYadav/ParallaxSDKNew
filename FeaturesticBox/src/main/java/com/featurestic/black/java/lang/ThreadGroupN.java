package com.featurestic.black.java.lang;


import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("java.lang.ThreadGroup")
public interface ThreadGroupN {
    @BField
    java.lang.ThreadGroup[] groups();

    @BField
    Integer ngroups();

    @BField
    java.lang.ThreadGroup parent();
}
