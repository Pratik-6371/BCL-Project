package com.kronos.wfc.cms.facade;

import java.util.Map;

public abstract interface SafetyFacade
{
  public abstract Map[] getWorkmenSafetyTrainings(String paramString);
  
  public abstract void updateTrainings(String paramString, Map[] paramArrayOfMap);
  
  public abstract Map[] getWorkmenSafetyViolations(String paramString);
  
  public abstract void updateViolations(String paramString, Map[] paramArrayOfMap);
}
