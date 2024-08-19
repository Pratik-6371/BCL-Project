package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.HealthInformation;
import java.util.List;
import java.util.Map;

public abstract interface HealthInformationFacade
{
  public abstract Map[] getWorkmenHealthRecords(String paramString);
  
  public abstract void updateHealthRecords(String paramString, List<HealthInformation> paramList);
}
