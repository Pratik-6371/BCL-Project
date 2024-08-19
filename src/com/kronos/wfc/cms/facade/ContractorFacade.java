package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import java.util.Map;

public abstract interface ContractorFacade
{
  public static final String CMS_CONTRACTORNAME = "";
  public static final String CMS_CONTRACTORADDRESS = "caddress";
  public static final String CMS_CADDRESS1 = "caddress1";
  public static final String CMS_CADDRESS2 = "caddress2";
  public static final String CMS_CADDRESS3 = "caddress3";
  public static final String CMS_MANAGERNAME = "managername";
  public static final String CMS_ESIWCNUMBER = "esiwcnunber";
  public static final String CMS_LICENSENUMBER = "licensenumber";
  public static final String CMS_LICENSEVALIDITY1 = "licensevalidity1";
  public static final String CMS_LICENSEVALIDITY2 = "licensevalidity2";
  public static final String CMS_COVERAGE = "coverage";
  public static final String CMS_TOTALSTRENGTH = "totalstrength";
  public static final String CMS_MAXNOOFEMPLOYEES = "maxnoofemployees";
  public static final String CMS_NATUREOFWORK = "natureofwork";
  public static final String CMS_LOCATIONOFCONTRACTWORK = "locationofcontractwork";
  public static final String CMS_PERIODOFCONTRACT1 = "periododcontract1";
  public static final String CMS_PERIODOFCONTRACT2 = "periododcontract2";
  public static final String CMS_ESIWVALIDITYPERIOD = "esiwvalidityperiod";
  public static final String CMS_PFNUMBER = "pfnumber";
  public static final String CMS_PFCODEAPPLICATIONDATE = "pfcodeapplicationdate";
  
  public abstract Map[] getContractors(String paramString);
  
  public abstract Map[] getContractorConfigurations();
  
  public abstract void saveContractor(Contractor paramContractor);
  
  public abstract Contractor getContractor(String paramString1, String paramString2);
  
  public abstract Map[] getContractorsByVendorName(String paramString1, String paramString2);
  
  public abstract Map[] getContractorsByVendorCode(String paramString1, String paramString2);
}
