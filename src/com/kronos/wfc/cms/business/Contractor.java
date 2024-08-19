package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.businessobject.framework.KBusinessObject;
import com.kronos.wfc.platform.member.framework.StringMember;
import com.kronos.wfc.platform.member.framework.StringProperties;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.List;





public class Contractor
  implements KBusinessObject
{
  private ObjectIdLong contractorid;
  private StringMember contractorname;
  private StringMember caddress;
  private StringMember caddress1;
  private StringMember caddress2;
  private StringMember caddress3;
  private StringMember ccode;
  private StringMember managername;
  private StringMember esiwc;
  private StringMember licensenumber;
  
  private KDate licensevalidity1;
  private KDate licensevalidity2;
  
  private StringMember coverage;
  private StringMember totalstrength;
  private StringMember maxnoofemployees;
  private StringMember natureofwork;
  private StringMember locationofcontractwork;
  private KDate periodofcontract1;
  private KDate periodofcontract2;
  private StringMember vendorCode;
  private StringMember pfcode;
  private KDate esistdt;
  private KDate esieddt;
  private StringMember esiwcCoverage;
  private StringMember pfnumber;
  private KDate pfcodeapplicationdate;
  private Boolean isBlocked;
  private String commission;
  private String pfCoverage;
  private Boolean isPfSelf;
  private Boolean isEsiSelf;
  
  private StringMember licensenumber1;
  private KDate licensevalidityFrom;
  private KDate licensevalidityTo;
  
  StringProperties contractorNameCont = new StringProperties(
    KronosProperties.get("cms.contractorname", "contractorname"), false);
  StringProperties caddressCont = new StringProperties(KronosProperties.get(
    "cms.caddress", "caddress"), false);
  StringProperties caddress1Cont = new StringProperties(KronosProperties.get(
    "cms.caddress1", "cddress1"), false);
  StringProperties caddress2Cont = new StringProperties(KronosProperties.get(
    "cms.caddress2", "caddress2"), false);
  StringProperties caddress3Cont = new StringProperties(KronosProperties.get(
    "cms.caddress3", "caddress3"), false);
  StringProperties managernameCont = new StringProperties(
    KronosProperties.get("cms.managername", "managername"), false);
  StringProperties esiwcCont = new StringProperties(KronosProperties.get(
    "cms.esiwc", "esiwc"), false);
  StringProperties licensenumberCont = new StringProperties(
    KronosProperties.get("cms.licensenumber", "licensenumber"), false);
  StringProperties licensevalidity1Cont = new StringProperties(
    KronosProperties.get("cms.licensevalidity1", "licensevalidity1"), 
    false);
  StringProperties licensevalidity2Cont = new StringProperties(
    KronosProperties.get("cms.licensevalidity2", "licensevalidity2"), 
    false);
  StringProperties coverageCont = new StringProperties(KronosProperties.get(
    "cms.coverage", "coverage"), false);
  StringProperties totalstrengthCont = new StringProperties(
    KronosProperties.get("cms.totalstrength", "totalstrength"), false);
  StringProperties maxnoofemployeesCont = new StringProperties(
    KronosProperties.get("cms.maxnoofemployees", "maxnoofemployees"), 
    false);
  StringProperties natureofworkCont = new StringProperties(
    KronosProperties.get("cms.natureofwork", "natureofwork"), false);
  StringProperties locationofcontractworkCont = new StringProperties(
    KronosProperties.get("cms.locationofcontractwork", 
    "locationofcontractwork"), false);
  StringProperties periodofcontract1Cont = new StringProperties(
    KronosProperties.get("cms.periodofcontract1", "periododcontract1"), 
    false);
  StringProperties periodofcontract2Cont = new StringProperties(
    KronosProperties.get("cms.periodofcontract2", "periododcontract2"), 
    false);
  StringProperties vendoridCont = new StringProperties(KronosProperties.get(
    "cms.vendorid", "vendorid"), false);
  StringProperties pfcodeCont = new StringProperties(KronosProperties.get(
    "cms.pfcode", "pfcode"), false);
  StringProperties esiwvalidityperiodCont = new StringProperties(
  
    KronosProperties.get("cms.esiwvalidityperiod", "esiwvalidityperiod"), 
    false);
  StringProperties pfnumberCont = new StringProperties(KronosProperties.get(
    "cms.pfnumber", "pfnumber"), false);
  StringProperties pfcodeapplicationdateCont = new StringProperties(
    KronosProperties.get("cms.pfcodeapplicationdate", 
    "pfcodeapplicationdate"), false);
  private ObjectIdLong unitId;
  private StringProperties esistdtProperties = new StringProperties(KronosProperties.get(
    "cms.esistdt", "esistdt"), false);
  private StringProperties esieddtProperties = new StringProperties(KronosProperties.get(
    "cms.esieddt", "esieddt"), false);
  private StringProperties ccodeprop = new StringProperties(KronosProperties.get(
    "cms.ccode", "ccode"), false);
  private StringProperties esicoverageprop = new StringProperties(KronosProperties.get(
    "cms.esicoverage", "esicoverage"), false);
  private Boolean selected = Boolean.valueOf(false);
  

  StringProperties licensenumberCont1 = new StringProperties(
		    KronosProperties.get("cms.licensenumber1", "licensenumber1"), false);
		  StringProperties licensevalidityFromCont = new StringProperties(
		    KronosProperties.get("cms.licensevalidityFrom", "licensevalidityFrom"), 
		    false);
		  StringProperties licensevalidityToCont = new StringProperties(
		    KronosProperties.get("cms.licensevalidityTo", "licensevalidityTo"), 
		    false);




public Contractor(){}

  public Contractor(ObjectIdLong contractorid, ObjectIdLong unitId, String contractorname, 
		  String caddress, String caddress1, String caddress2, String caddress3, String ccode,
		  String managername, String esiwc, String licensenumber, KDate licensevalidity1,
		  KDate licensevalidity2, String coverage, String totalstrength, String maxnoofemployees,
		  String natureofwork, String locationofcontractwork, KDate periodofcontract1,
		  KDate periodofcontract2, String vendorid, String pfcode, KDate esiwvalidityperiodfrom,
		  KDate esiwvalidityperiodto, String esicoverage, String pfnumber, KDate pfcodeapplicationdate,
		  Boolean isBlocked, String commission, String licensenumber1, KDate licensevalidityFrom,
		  KDate licensevalidityTo)
  {
    setContractorid(contractorid.longValue());
    setUnitId(unitId);
    if (contractorname == null) {
      setContractorname("-");
    }
    else {
      setContractorname(contractorname);
    }
    setCaddress(caddress);
    setCaddress1(caddress1);
    setCaddress2(caddress2);
    setCaddress3(caddress3);
    setCcode(ccode);
    setManagername(managername);
    setEsiwc(esiwc);
    setLicensenumber(licensenumber);
    setLicensevalidity1(licensevalidity1);
    setLicensevalidity2(licensevalidity2);
    setCoverage(coverage);
    setTotalstrength(totalstrength);
    setMaxnoofemployees(maxnoofemployees);
    setNatureofwork(natureofwork);
    setLocationofcontractwork(locationofcontractwork);
    setPeriodofcontract1(periodofcontract1);
    setPeriodofcontract2(periodofcontract2);
    setVendorCode(vendorid);
    setPfcode(pfcode);
    setPfnumber(pfnumber);
    setEsistdt(esiwvalidityperiodfrom);
    setEsieddt(esiwvalidityperiodto);
    setESIwcCoverage(esicoverage);
    setPfcodeapplicationdate(pfcodeapplicationdate);
    setIsBlocked(isBlocked);
    this.commission = commission;
    setLicensenumber1(licensenumber1);
    setLicensevalidityFrom(licensevalidityFrom);
    setLicensevalidityTo(licensevalidityTo);
  }
  
  public Contractor(ObjectIdLong contractorid, ObjectIdLong unitId, String contractorname, String caddress,
		String caddress1, String caddress2, String caddress3, String ccode, String managername, String esiwc,
		String licensenumber, KDate licensevalidity1, KDate licensevalidity2, String coverage,
		String totalstrength, String maxnoofemployees, String natureofwork, String locationofcontractwork,
		KDate periodofcontract1, KDate periodofcontract2, String vendorid, String pfcode, KDate esiwvalidityperiodfrom,
		KDate esiwvalidityperiodto, String esicoverage, String pfnumber, KDate pfcodeapplicationdate, Boolean isBlocked,
		String commission, String pfCoverage, Boolean isPfSelf, Boolean isEsiSelf,String licensenumber1, KDate licensevalidityFrom,
		  KDate licensevalidityTo) {
	
	  setContractorid(contractorid.longValue());
	    setUnitId(unitId);
	    if (contractorname == null) {
	      setContractorname("-");
	    }
	    else {
	      setContractorname(contractorname);
	    }
	    setCaddress(caddress);
	    setCaddress1(caddress1);
	    setCaddress2(caddress2);
	    setCaddress3(caddress3);
	    setCcode(ccode);
	    setManagername(managername);
	    setEsiwc(esiwc);
	    setLicensenumber(licensenumber);
	    setLicensevalidity1(licensevalidity1);
	    setLicensevalidity2(licensevalidity2);
	    setCoverage(coverage);
	    setTotalstrength(totalstrength);
	    setMaxnoofemployees(maxnoofemployees);
	    setNatureofwork(natureofwork);
	    setLocationofcontractwork(locationofcontractwork);
	    setPeriodofcontract1(periodofcontract1);
	    setPeriodofcontract2(periodofcontract2);
	    setVendorCode(vendorid);
	    setPfcode(pfcode);
	    setPfnumber(pfnumber);
	    setEsistdt(esiwvalidityperiodfrom);
	    setEsieddt(esiwvalidityperiodto);
	    setESIwcCoverage(esicoverage);
	    setPfcodeapplicationdate(pfcodeapplicationdate);
	    setIsBlocked(isBlocked);
	    this.commission = commission;
	    this.pfCoverage = pfCoverage;
	    this.isPfSelf = isPfSelf;
	    this.isEsiSelf = isEsiSelf;
	    setLicensenumber1(licensenumber1);
	    setLicensevalidityFrom(licensevalidityFrom);
	    setLicensevalidityTo(licensevalidityTo);
}

private void setUnitId(ObjectIdLong unitId2) {
    unitId = unitId2;
  }
  
  public static Contractor doRetrieveById(ObjectIdLong oid, ObjectIdLong unitId) {
    if (oid != null) {
      ContractorService service = new ContractorService();
      Contractor employee = service.retrieveById(oid, unitId);
      return employee;
    }
    return null;
  }
  
  public static Contractor doRetrieveByName(String name) {
    if (name != null) {
      ContractorService service = new ContractorService();
      Contractor employee = service.retrieveByName(name);
      return employee;
    }
    return null;
  }
  
  public String getCommission() {
    return commission;
  }
  
  public void setCommission(String commission) {
    this.commission = commission;
  }
  
  public KDate getPfcodeapplicationdate() {
    return pfcodeapplicationdate;
  }
  
  public void setPfcodeapplicationdate(KDate pfcodeapplicationdate) {
    this.pfcodeapplicationdate = pfcodeapplicationdate;
  }
  
  public String getCaddress3() {
    return caddress3.get();
  }
  
  public void setCaddress3(String caddress3) {
    this.caddress3 = new StringMember(caddress3, caddress3Cont);
  }
  
  public String getLicensenumber() {
    return licensenumber.get();
  }
  
  public void setLicensenumber(String licensenumber) {
    this.licensenumber = new StringMember(licensenumber, licensenumberCont);
  }
  
  public KDate getLicensevalidity1() {
    return licensevalidity1;
  }
  
  public void setLicensevalidity1(KDate licensevalidity1) {
    this.licensevalidity1 = licensevalidity1;
  }
  
  public KDate getLicensevalidity2() {
    return licensevalidity2;
  }
  
  public void setLicensevalidity2(KDate licensevalidity2) {
    this.licensevalidity2 = licensevalidity2;
  }
  
  
  
  public String getLicensenumber1() {
	return licensenumber1.get();
}

  public void setLicensenumber1(String licensenumber1) {
	    this.licensenumber1 = new StringMember(licensenumber1, licensenumberCont1);
	  }
	  

public KDate getLicensevalidityFrom() {
	return licensevalidityFrom;
}

public void setLicensevalidityFrom(KDate licensevalidityFrom) {
	this.licensevalidityFrom = licensevalidityFrom;
}

public KDate getLicensevalidityTo() {
	return licensevalidityTo;
}

public void setLicensevalidityTo(KDate licensevalidityTo) {
	this.licensevalidityTo = licensevalidityTo;
}

public String getCoverage() {
    return coverage.get();
  }
  
  public void setCoverage(String coverage) {
    this.coverage = new StringMember(coverage, coverageCont);
  }
  
  public String getTotalstrength() {
    return totalstrength.get();
  }
  
  public void setTotalstrength(String totalstrength) {
    this.totalstrength = new StringMember(totalstrength, totalstrengthCont);
  }
  
  public String getMaxnoofemployees() {
    return maxnoofemployees.get();
  }
  
  public void setMaxnoofemployees(String maxnoofemployees) {
    this.maxnoofemployees = new StringMember(maxnoofemployees, 
      maxnoofemployeesCont);
  }
  
  public String getNatureofwork() {
    return natureofwork.get();
  }
  
  public void setNatureofwork(String natureofwork) {
    this.natureofwork = new StringMember(natureofwork, natureofworkCont);
  }
  
  public String getLocationofcontractwork() {
    return locationofcontractwork.get();
  }
  
  public void setLocationofcontractwork(String locationofcontractwork) {
    this.locationofcontractwork = new StringMember(locationofcontractwork, 
      locationofcontractworkCont);
  }
  
  public KDate getPeriodofcontract2() {
    return periodofcontract2;
  }
  
  public void setPeriodofcontract2(KDate periodofcontract2) {
    this.periodofcontract2 = periodofcontract2;
  }
  
  public KDate getPeriodofcontract1() {
    return periodofcontract1;
  }
  
  public void setPeriodofcontract1(KDate periodofcontract1) {
    this.periodofcontract1 = periodofcontract1;
  }
  
  public String getVendorCode() {
    return vendorCode.get();
  }
  
  public void setVendorCode(String vendorCode) {
    this.vendorCode = new StringMember(vendorCode, vendoridCont);
  }
  
  public String getPfcode() {
    return pfcode.get();
  }
  
  public void setPfcode(String pfcode) {
    this.pfcode = new StringMember(pfcode, pfcodeCont);
  }
  
  public String getPfnumber() {
    return pfnumber.get();
  }
  
  public void setPfnumber(String pfnumber) {
    this.pfnumber = new StringMember(pfnumber, pfnumberCont);
  }
  
  public String getCaddress1() {
    return caddress1.get();
  }
  
  public void setCaddress1(String caddress1) {
    this.caddress1 = new StringMember(caddress1, caddress1Cont);
  }
  
  public String getEsiwcnumber() {
    return esiwc.get();
  }
  
  public void setEsiwc(String esiwc) {
    this.esiwc = new StringMember(esiwc, esiwcCont);
  }
  
  public String getManagername() {
    return managername.get();
  }
  
  public void setManagername(String managername) {
    this.managername = new StringMember(managername, managernameCont);
  }
  
  public String getCaddress2() {
    return caddress2.get();
  }
  
  public void setCaddress2(String caddress2) {
    this.caddress2 = new StringMember(caddress2, caddress2Cont);
  }
  
  public String getcontractorName() {
    return contractorname.get();
  }
  
  public void setContractorname(String contractorname) {
    this.contractorname = new StringMember(contractorname, 
      contractorNameCont);
  }
  
  public String getCaddress() {
    return caddress.get();
  }
  
  public void setCaddress(String caddress) {
    this.caddress = new StringMember(caddress, caddressCont);
  }
  
  public ObjectId getcontractorid() {
    return contractorid;
  }
  
  public void setContractorid(long id) {
    contractorid = new ObjectIdLong(id);
  }
  
  public static List<Contractor> doRetrieveAll()
  {
    return new ContractorService().retrieveAll();
  }
  
  public void doUpdate() {
    new ContractorService().updateContractor(this);
  }
  
  public static List<Contractor> doRetrieveByUnitId(ObjectIdLong unitId)
  {
    return new ContractorService().retrieveByUnitId(unitId);
  }
  


  public ObjectIdLong getUnitId()
  {
    return unitId;
  }
  
  public KDate getEsistdt() {
    return esistdt;
  }
  
  public void setEsistdt(KDate esistdt) {
    this.esistdt = esistdt;
  }
  
  public KDate getEsieddt() {
    return esieddt;
  }
  
  public void setEsieddt(KDate esieddt) {
    this.esieddt = esieddt;
  }
  
  public String getCcode() {
    return ccode.get();
  }
  
  public void setCcode(String ccode) {
    this.ccode = new StringMember(ccode, ccodeprop);
  }
  
  public String getESIwcCoverage() {
    return esiwcCoverage.get();
  }
  
  public void setESIwcCoverage(String esiwcCoverage) {
    this.esiwcCoverage = new StringMember(esiwcCoverage, esicoverageprop);
  }
  
  public static List<Workmen> retrieveWorkmen(ObjectIdLong id, ObjectIdLong tradeId, ObjectIdLong skillId, String workorderTyp, ObjectIdLong unitId) {
    return new CMSService().getWorkmenByContractor(id, tradeId, skillId, workorderTyp, unitId);
  }
  
  public Boolean getSelected() {
    return selected;
  }
  
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
  
  public static List<Supervisor> retrieveSupervisors(ObjectIdLong contractorId2) {
    return new CMSService().getSupervisorsByContractorId(contractorId2);
  }
  
  public static List<Workmen> retrieveWorkmen(ObjectIdLong id, ObjectIdLong unitId, String statusId)
  {
    return new CMSService().getWorkmenByContractor(id, unitId, statusId);
  }
  
  public static Contractor doRetrieveByName(String name, String unitId2)
  {
    return new ContractorService().retrieveByName(name, unitId2);
  }
  
  public static Contractor doRetrieveByCode(String code, String unitId2)
  {
    return new ContractorService().retrieveByCode(code, unitId2);
  }
  

  public static List<Workmen> retrieveWorkmenByEmpCode(ObjectIdLong contrId, String empCode, String unitId, String statusId)
  {
    return new ContractorService().getWorkmenByContractor(contrId, empCode, unitId, statusId);
  }
  

  public static List<Workmen> retrieveWorkmenByEmpName(ObjectIdLong id, String empName, String unitId, String statusId)
  {
    return new ContractorService().getWorkmenByContractorAndWKName(id, empName, unitId, statusId);
  }
  
  public Boolean getIsBlocked() {
    return isBlocked;
  }
  
  public void setIsBlocked(Boolean isBlocked) {
    this.isBlocked = isBlocked;
  }


public String getPfCoverage() {
	return pfCoverage;
}

public void setPfCoverage(String pfCoverage) {
	this.pfCoverage = pfCoverage;
}

public Boolean getIsPfSelf() {
	return isPfSelf;
}

public void setIsPfSelf(Boolean isPfSelf) {
	this.isPfSelf = isPfSelf;
}

public Boolean getIsEsiSelf() {
	return isEsiSelf;
}

public void setIsEsiSelf(Boolean isEsiSelf) {
	this.isEsiSelf = isEsiSelf;
}
  
  
}
