package com.teamdark.black.dalvik.system;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("dalvik.system.BaseDexClassLoader")
public interface BaseDexClassLoader {

  @BField
  Object sharedLibraryLoaders();
}
