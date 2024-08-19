package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.businessobject.framework.KBusinessObject;
import com.kronos.wfc.platform.member.framework.BooleanMember;
import com.kronos.wfc.platform.member.framework.BooleanProperties;
import com.kronos.wfc.platform.member.framework.StringMember;
import com.kronos.wfc.platform.member.framework.StringProperties;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import java.util.ArrayList;
import java.util.List;


public class PrincipalEmployee
  implements KBusinessObject
{
  private ObjectIdLong unitId;
  private StringMember unitName;
  private StringMember peaddress;
  private StringMember peaddress1;
  private StringMember peaddress2;
  private StringMember peaddress3;
  private StringMember managername;
  private StringMember manageraddress;
  private StringMember manageraddress1;
  private StringMember manageraddress2;
  private StringMember manageraddress3;
  private StringMember typeofbussiness;
  private StringMember maxnumberofworkmen;
  private StringMember maxnumberofcontractworkmen;
  private BooleanMember BOCWActApplicability;
  private BooleanMember ISMWActapplicability;
  private StringMember licensenumber;
  private StringMember pfCode;
  private StringMember esiwc;
  private List<CustomProperties> properties;
  private boolean isSelected = false;
  
  private String unitCode;
  
  private String organization;
  StringProperties unitNameProp = new StringProperties(KronosProperties.get("cms.unitname", "unitName"), false);
  StringProperties peAddressProp = new StringProperties(KronosProperties.get("cms.address", "peAddress"), false);
  StringProperties peAddressProp1 = new StringProperties(KronosProperties.get("cms.address1", "peAddress1"), false);
  StringProperties peAddressProp2 = new StringProperties(KronosProperties.get("cms.address2", "peAddress2"), false);
  StringProperties peAddressProp3 = new StringProperties(KronosProperties.get("cms.address3", "peAddress3"), false);
  StringProperties managerNameProp = new StringProperties(KronosProperties.get("cms.managername", "managerName"), false);
  StringProperties managerAddress1Prop = new StringProperties(KronosProperties.get("cms.manageraddress1", "managerAddress1"), false);
  StringProperties managerAddress2Prop = new StringProperties(KronosProperties.get("cms.manageraddress2", "managerAddress2"), false);
  StringProperties managerAddress3Prop = new StringProperties(KronosProperties.get("cms.manageraddress3", "managerAddress3"), false);
  StringProperties typeofbussinessProp = new StringProperties(KronosProperties.get("cms.typeofbussinessProp", "typeofbussiness"), false);
  StringProperties maxnumberofworkmenProp = new StringProperties(KronosProperties.get("cms.maxnumberofworkmen", "maxnumberofworkmen"), false);
  StringProperties maxnumberofcontractworkmenProp = new StringProperties(KronosProperties.get("cms.maxnumberofworkmen", "maxnumberofcontractworkmen"), false);
  BooleanProperties bOCWActApplicabilityProp = new BooleanProperties(KronosProperties.get("cms.bOCWActApplicabilityProp", "bOCWActApplicabilityProp"), false);
  BooleanProperties iSMWActapplicabilityProp = new BooleanProperties(KronosProperties.get("cms.iSMWActapplicabilityProp", "iSMWActapplicabilityProp"), false);
  StringProperties licensenumberProp = new StringProperties(KronosProperties.get("cms.licensenumberProp", "Name"), false);
  StringProperties pfCodeProp = new StringProperties(KronosProperties.get("cms.pfCodeProp", "Name"), false);
  StringProperties esiwcProp = new StringProperties(KronosProperties.get("cms.esiwcProp", "Name"), false);


  public PrincipalEmployee(ObjectIdLong unitId, String unitName, String peaddress, String peaddress1, String peaddress2, String peaddress3, String managername, String manageraddress, String manageraddress1, String manageraddress2, String manageraddress3, String typeofbussiness, String maxnumberofworkmen, String maxnumberofcontractworkmen, Boolean bOCWActApplicability, Boolean iSMWActapplicability, String licensenumber, String pfCode, String esiwc, String unitCode, List<CustomProperties> props, String organization)
  {
    setUnitID(unitId.longValue());
    setUnitName(unitName);
    setPeaddress(peaddress);
    setPeaddress1(peaddress1);
    setPeaddress2(peaddress2);
    setPeaddress3(peaddress3);
    setManagername(managername);
    setManageraddress(manageraddress);
    setManageraddress1(manageraddress1);
    setManageraddress2(manageraddress2);
    setManageraddress3(manageraddress3);
    setTypeofbussiness(typeofbussiness);
    setMaxnumberofworkmen(maxnumberofworkmen);
    setMaxnumberofcontractworkmen(maxnumberofcontractworkmen);
    setBOCWActApplicability(bOCWActApplicability);
    setISMWActapplicability(iSMWActapplicability);
    setLicensenumber(licensenumber);
    setPfCode(pfCode);
    setEsiwc(esiwc);
    setUnitCode(unitCode);
    setProperties(props);
    setOrganization(organization);
  }
  

  public PrincipalEmployee() {}
  

  public static PrincipalEmployee doRetrieveForUpdate(String unitName)
  {
    if (unitName != null) {
      PrincipalEmployeeService service = new PrincipalEmployeeService();
      PrincipalEmployee employee = service.retrieveByCode(unitName);
      return employee;
    }
    return null;
  }
  
  public static PrincipalEmployee doRetrieveById(ObjectIdLong oid)
  {
    if (oid != null) {
      PrincipalEmployeeService service = new PrincipalEmployeeService();
      PrincipalEmployee employee = service.retrieveById(oid);
      return employee;
    }
    return null;
  }
  
  public static PrincipalEmployee doRetrieveByCode(String name) {
    if (name != null) {
      PrincipalEmployeeService service = new PrincipalEmployeeService();
      PrincipalEmployee employee = service.retrieveByCode(name);
      return employee;
    }
    return null;
  }
  
  public static List<PrincipalEmployee> doRetrieveAll() {
    PrincipalEmployeeService service = new PrincipalEmployeeService();
    List<PrincipalEmployee> employees = service.retrieveAll();
    return employees;
  }
  

  public void doUpdate()
  {
    validateAccessRight("Update");
    
    List employees = new ArrayList(1);
    
    PrincipalEmployeeService service = new PrincipalEmployeeService();
    
    if (unitId != null)
    {
      PrincipalEmployee employee = service.retrieveById(unitId);
      if (employee != null)
      {
        employee.updateFields(this);
        employees.add(employee);
        service.updatePrincipalEmployee(employees);
      }
    }
  }
  




  private void updateFields(PrincipalEmployee principalEmployee)
  {
    setPeaddress(principalEmployee.getPeaddress());
    setPeaddress1(principalEmployee.getPeaddress1());
    setPeaddress2(principalEmployee.getPeaddress2());
    setPeaddress3(principalEmployee.getPeaddress3());
    setManagername(principalEmployee.getManagername());
    setManageraddress(principalEmployee.getManageraddress());
    setManageraddress1(principalEmployee.getManageraddress1());
    setManageraddress2(principalEmployee.getManageraddress2());
    setManageraddress3(principalEmployee.getManageraddress3());
    setMaxnumberofworkmen(principalEmployee.getMaxnumberofworkmen());
    setMaxnumberofcontractworkmen(principalEmployee.getMaxnumberofcontractworkmen());
    setTypeofbussiness(principalEmployee.getTypeofbussiness());
    setLicensenumber(principalEmployee.getLicensenumber());
    setBOCWActApplicability(principalEmployee.getBOCWActApplicability());
    setISMWActapplicability(principalEmployee.getISMWActapplicability());
    setPfCode(principalEmployee.getPfCode());
    setEsiwc(principalEmployee.getEsiwc());
    setProperties(principalEmployee.getProperties());
    setOrganization(principalEmployee.getOrganization());
  }
  
  public static boolean validateAccessRight(String actionName)
  {
    return true;
  }
  
  public void setPrincipleEmployee(ObjectIdLong id, String unitname, String address)
  {
    setPrincipleEmployee(id, unitname, address);
  }
  


  public boolean isReadable(String propertyName)
  {
    if (propertyName.equals("unitname"))
      return true;
    if (propertyName.equals("address"))
      return true;
    if (propertyName.equals("managername"))
      return true;
    return propertyName.equals("Properties");
  }
  
  public String getPeaddress() {
    return peaddress.get();
  }
  
  public void setPeaddress(String peaddress) {
    this.peaddress = new StringMember(peaddress, peAddressProp);
  }
  
  public String getPeaddress1() {
    return peaddress1.get();
  }
  
  public void setPeaddress1(String peaddress1) {
    this.peaddress1 = new StringMember(peaddress1, peAddressProp1);
  }
  
  public String getPeaddress2() {
    return peaddress2.get();
  }
  
  public void setPeaddress2(String peaddress2) {
    this.peaddress2 = new StringMember(peaddress2, peAddressProp2);
  }
  
  public String getPeaddress3() {
    return peaddress3.get();
  }
  
  public void setPeaddress3(String peaddress3) {
    this.peaddress3 = new StringMember(peaddress3, peAddressProp3);
  }
  
  public String getManagername() {
    return managername.get();
  }
  
  public void setManagername(String managername) {
    this.managername = new StringMember(managername, managerNameProp);
  }
  
  public String getManageraddress() {
    return manageraddress.get();
  }
  
  public void setManageraddress(String manageraddress) {
    this.manageraddress = new StringMember(manageraddress, managerAddress1Prop);
  }
  
  public String getManageraddress1() {
    return manageraddress1.get();
  }
  
  public void setManageraddress1(String manageraddress1) {
    this.manageraddress1 = new StringMember(manageraddress1, managerAddress1Prop);
  }
  
  public String getManageraddress2() {
    return manageraddress2.get();
  }
  
  public void setManageraddress2(String manageraddress2) {
    this.manageraddress2 = new StringMember(manageraddress2, managerAddress2Prop);
  }
  
  public String getManageraddress3() {
    return manageraddress3.get();
  }
  
  public void setManageraddress3(String manageraddress3) { this.manageraddress3 = new StringMember(manageraddress3, managerAddress3Prop); }
  

  public String getTypeofbussiness()
  {
    return typeofbussiness.get();
  }
  
  public void setTypeofbussiness(String typeofbussiness) {
    this.typeofbussiness = new StringMember(typeofbussiness, typeofbussinessProp);
  }
  
  public String getMaxnumberofworkmen() {
    return maxnumberofworkmen.get();
  }
  
  public void setMaxnumberofworkmen(String maxnumberofworkmen) {
    this.maxnumberofworkmen = new StringMember(maxnumberofworkmen, typeofbussinessProp);
  }
  
  public String getMaxnumberofcontractworkmen() {
    return maxnumberofcontractworkmen.get();
  }
  
  public void setMaxnumberofcontractworkmen(String maxnumberofcontractworkmen) {
    this.maxnumberofcontractworkmen = new StringMember(maxnumberofcontractworkmen, maxnumberofcontractworkmenProp);
  }
  
  public Boolean getBOCWActApplicability() {
    return BOCWActApplicability.get();
  }
  
  public void setBOCWActApplicability(Boolean bOCWActApplicability2) {
    BOCWActApplicability = new BooleanMember(bOCWActApplicability2, bOCWActApplicabilityProp);
  }
  
  public Boolean getISMWActapplicability() {
    return ISMWActapplicability.get();
  }
  
  public void setISMWActapplicability(Boolean iSMWActapplicability) {
    ISMWActapplicability = new BooleanMember(iSMWActapplicability, iSMWActapplicabilityProp);
  }
  
  public String getLicensenumber() {
    return licensenumber.get();
  }
  
  public void setLicensenumber(String licensenumber) {
    this.licensenumber = new StringMember(licensenumber, licensenumberProp);
  }
  
  public String getPfCode() {
    return pfCode.get();
  }
  
  public void setPfCode(String pfCode) {
    this.pfCode = new StringMember(pfCode, pfCodeProp);
  }
  
  public String getEsiwc() {
    return esiwc.get();
  }
  
  public void setEsiwc(String esiwc) {
    this.esiwc = new StringMember(esiwc, esiwcProp);
  }
  
  public ObjectId getUnitId() {
    return unitId;
  }
  
  public void setUnitID(long id)
  {
    unitId = new ObjectIdLong(id);
  }
  
  public String getUnitName() {
    return unitName.get();
  }
  
  public void setUnitName(String unitName) {
    this.unitName = new StringMember(unitName, unitNameProp);
  }
  
  public List<CustomProperties> getProperties() {
    return properties;
  }
  
  public void setProperties(List<CustomProperties> properties) {
    this.properties = properties;
  }
  
  public boolean isSelected() {
    return isSelected;
  }
  
  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }
  
  public String getUnitCode() {
    return unitCode;
  }
  
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }
  
  public String getCurrentCountWorkmen() {
    return new CMSService().getCurrentCountWorkmen(unitId.toString());
  }
  


  public String getOrganization()
  {
    return organization;
  }
  


  public void setOrganization(String organization)
  {
    this.organization = organization;
  }
}
