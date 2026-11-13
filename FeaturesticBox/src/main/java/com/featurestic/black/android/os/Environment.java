package com.featurestic.black.android.os;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticField;

@BClassName("android.os.Environment")
public interface Environment {

  @BStaticField
  Object sCurrentUser();
}