package com.featurestic.black.dalvik.system;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("dalvik.system.BaseDexClassLoader")
public interface BaseDexClassLoader {

  @BField
  Object sharedLibraryLoaders();
}
