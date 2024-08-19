package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.PrincipalEmployee;
import java.util.List;
import java.util.Map;

public abstract interface PrincipalEmployeeFacade
{
  public static final String CMS_UNITNAME = "unitname";
  public static final String CMS_PEADDRESS = "peaddress";
  public static final String CMS_PEADDRESS1 = "peaddress1";
  public static final String CMS_PEADDRESS2 = "peaddress2";
  public static final String CMS_PEADDRESS3 = "peaddress3";
  public static final String CMS_MANAGERNAME = "managername";
  public static final String CMS_MANAGERADDRESS = "manageraddess";
  public static final String CMS_MANAGERADDRESS1 = "manageraddress1";
  public static final String CMS_MANAGERADDRESS2 = "manageraddress2";
  public static final String CMS_MANAGERADDRESS3 = "manageraddress3";
  public static final String CMS_TYPEOFBUSSINESS = "typeofbusiness";
  public static final String CMS_MAXNUMBEROFWORKMEN = "maxnumberofworkmen";
  public static final String CMS_MAXNUMBEROFCONTRACTWORKMEN = "maxnumberofcontractworkmen";
  public static final String CMS_BOCWACTAPPLICABILITY = "BOCWActApplicability";
  public static final String CMS_ISMWACTAPPLICABILITY = "ISMWActapplicability";
  public static final String CMS_LICENSENUMBER = "licensenumber";
  public static final String PROPERTIES_ENCRYPTED = "encrypted";
  public static final String PROPERTIES_ALREADY_ENCRYPTED = "already_encrypted";
  public static final String PROPERTIES_HEADER = "header";
  
  public abstract List<PrincipalEmployee> getPrincipalEmployees();
  
  public abstract PrincipalEmployee getPrincipalEmployeeByCode(String paramString);
  
  public abstract PrincipalEmployee getPrincipalEmployee(String paramString);
  
  public abstract Map[] getPrincipalEmployeeConfigurations();
  
  public abstract Map getPrincipalEmployeeConfiguration(String paramString);
  
  public abstract void savePrincipalEmployee(PrincipalEmployee paramPrincipalEmployee);
  
  public abstract void updatePEConfiguration(String paramString, Map paramMap, Map[] paramArrayOfMap);
}
