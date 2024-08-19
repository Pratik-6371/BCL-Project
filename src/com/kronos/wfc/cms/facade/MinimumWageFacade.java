package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.MinimumWage;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import java.util.List;
import java.util.Map;

public abstract interface MinimumWageFacade
{
  public static final String CMS_NAME = "name";
  public static final String CMS_FROM = "from";
  public static final String CMS_PEADDRESS1 = "to";
  public static final String CMS_PEADDRESS2 = "trade";
  public static final String CMS_PEADDRESS3 = "skillset";
  public static final String CMS_MANAGERNAME = "mineage";
  public static final String CMS_MANAGERADDRESS = "daperday";
  
  public abstract Map[] getMinimumWageConfigurations(String paramString1, String paramString2);
  
  public abstract MinimumWage getMinimumWage(String paramString);
  
  public abstract MinimumWage getMinimumWageByName(String paramString);
  
  public abstract List<MinimumWage> getMinimumWages(String paramString1, String paramString2);
  
  public abstract void saveMinimumWage(MinimumWage paramMinimumWage)
    throws Exception;
  
  public abstract void updateMinimumWageConfiguration(String paramString, Map paramMap)
    throws Exception;
  
  public abstract List<CMSState> getStates();
  
  public abstract List<Trade> getTrades();
  
  public abstract List<Skill> getSkills();
}
