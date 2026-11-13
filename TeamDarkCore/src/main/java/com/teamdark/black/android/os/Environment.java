package com.teamdark.black.android.os;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("android.os.Environment")
public interface Environment {

  @BStaticField
  Object sCurrentUser();
}