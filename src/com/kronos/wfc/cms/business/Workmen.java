package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.types.business.DeviceGroup;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Workmen
{
  public static final Integer ACTIVE_STATUS = Integer.valueOf(1);
  public static final Integer INACTIVE_STATUS = Integer.valueOf(2);
  public static final Integer TERMINATED_STATUS = Integer.valueOf(3);
  private ObjectIdLong empId;
  private String empCode;
  private String firstName;
  private String lastName;
  private String relationName;
  
  public Workmen(ObjectIdLong empId, String empCode, String firstName, String lastName, String relationName, KDate dOB, String idMark, Trade trade, Skill skill, Address presentAddress, Address permAddress, WorkmenDetail detail, Wage wage, Contractor contractor, Integer status, Person person, Boolean isBSR, String badgeNumber, DeviceGroup deviceGroup, String imageContent, String unitId) { this.empId = empId;
    this.empCode = empCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.relationName = relationName;
    DOB = dOB;
    this.idMark = idMark;
    this.trade = trade;
    this.skill = skill;
    this.presentAddress = presentAddress;
    this.permAddress = permAddress;
    this.detail = detail;
    this.wage = wage;
    this.contractor = contractor;
    statusID = Integer.valueOf(status.intValue());
    this.person = person;
    this.isBSR = isBSR;
    badgeNum = badgeNumber;
    this.deviceGroup = deviceGroup;
    setUnitId(unitId);
    setImageContent(imageContent);
   
  }
  

  private KDate DOB;
  
  private String idMark;
  
  private Trade trade;
 
  
  private Skill skill;
  
  private Address presentAddress;
  
  private Address permAddress;
  
  private WorkmenDetail detail;
  
  private Wage wage;
  
  private Contractor contractor;
  private Integer statusID;
  private Person person;
  private Boolean isBSR;
  private String badgeNum;
  private KDate badgeNumberEffectiveDate;
  private DeviceGroup deviceGroup;
  private String imageContent;
  private String unitId;
  private Map<String, String> customFields;
  private KDate laborAccountEffectiveDate;
  public Workmen() {}
  
  public Map<String, String> getCustomFields()
  {
    return customFields;
  }
  
  public void setCustomFields(Map<String, String> customFields) {
    this.customFields = customFields;
  }
  
  public ObjectIdLong getEmpId() {
    return empId;
  }
  
  public void setEmpId(ObjectIdLong empId) {
    this.empId = empId;
  }
  
  public String getEmpCode() {
    return empCode;
  }
  
  public void setEmpCode(String empCode) {
    this.empCode = empCode;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getRelationName() {
    return relationName;
  }
  
  public void setRelationName(String relationName) {
    this.relationName = relationName;
  }
  
  public KDate getDOB() {
    return DOB;
  }
  
  public void setDOB(KDate dOB) {
    DOB = dOB;
  }
  
  public String getIdMark() {
    return idMark;
  }
  
  public void setIdMark(String idMark) {
    this.idMark = idMark;
  }
  
  public Trade getTrade() {
    return trade;
  }
  
  public void setTrade(Trade trade) {
    this.trade = trade;
  }

  
  public Skill getSkill() {
    return skill;
  }
  
  public void setSkill(Skill skill) {
    this.skill = skill;
  }
  
  public Address getPresentAddress() {
    return presentAddress;
  }
  
  public void setPresentAddress(Address presentAddress) {
    this.presentAddress = presentAddress;
  }
  
  public Address getPermAddress() {
    return permAddress;
  }
  
  public void setPermAddress(Address permAddress) {
    this.permAddress = permAddress;
  }
  
  public WorkmenDetail getDetail() {
    return detail;
  }
  
  public void setDetail(WorkmenDetail detail) {
    this.detail = detail;
  }
  
  public Wage getWage() {
    return wage;
  }
  
  public void setWage(Wage wage) {
    this.wage = wage;
  }
  
  public Contractor getContractor() {
    return contractor;
  }
  
  public void setContractor(Contractor contractor) {
    this.contractor = contractor;
  }
  
  public void doAdd() {
    new CMSService().addWorkmen(this);
  }
  
  public void doUpdate() {
    new CMSService().updateWorkmen(this);
  }
  
  public void doRehire() {
    new CMSService().rehireWorkmen(this);
  }
  
  public Integer getStatusID() {
    return statusID;
  }
  
  public void setStatusID(Integer statusID) {
    this.statusID = statusID;
  }
  
  public static Workmen getWorkmenById(ObjectIdLong id, ObjectIdLong unitId) {
    return new CMSService().getWorkmenById(id, unitId);
  }
  
  public void doDelete() {
    new CMSService().deleteWorkmen(this);
  }
  
  public void updateFields(Workmen workmen) {
    if (workmen.getFirstName() != null) {
      setFirstName(workmen.getFirstName());
    }
    if (workmen.getLastName() != null) {
      setLastName(workmen.getLastName());
    }
    if (workmen.getDOB() != null) {
      setDOB(workmen.getDOB());
    }
    if (workmen.getDetail() != null)
    {
      detail.updateFields(workmen.getDetail());
    }
    if (workmen.getEmpCode() != null) {
      setEmpCode(workmen.getEmpCode());
    }
    if (workmen.getIdMark() != null) {
      setIdMark(workmen.getIdMark());
    }
    
    if (workmen.getPerson() != null) {
      setPerson(workmen.getPerson());
    }
    
    if (workmen.getContractor() != null) {
      setContractor(workmen.getContractor());
    }
    
    Address newPermanentAddress = workmen.getPermAddress();
    if (newPermanentAddress != null) {
      permAddress.updateFields(newPermanentAddress);
      if (Address.DEFAULT_ADDRESS_ID.equals(permAddress.getId())) {
        permAddress.setId(newPermanentAddress.getId());
        permAddress.doAdd();
      }
    }
    

    Address newPresentAddress = workmen.getPresentAddress();
    if (newPresentAddress != null) {
      presentAddress.updateFields(newPresentAddress);
      if (Address.DEFAULT_ADDRESS_ID.equals(presentAddress.getId())) {
        presentAddress.setId(newPresentAddress.getId());
        presentAddress.doAdd();
      }
    }
    
    if (workmen.getRelationName() != null) {
      setRelationName(workmen.getRelationName());
    }
    if (workmen.getSkill() != null) {
      setSkill(workmen.getSkill());
    }
    if (workmen.getTrade() != null) {
      setTrade(workmen.getTrade());
    }

    if (workmen.getWage() != null) {
      wage.updateFields(workmen.getWage());
    }
    if (workmen.getBadgeNum() != null) {
      setBadgeNum(workmen.getBadgeNum());
    }
    
    if (workmen.getBadgeNumberEffectiveDate() != null) {
      setBadgeNumberEffectiveDate(workmen.getBadgeNumberEffectiveDate());
    }
    
    deviceGroup = workmen.getDeviceGroup();
    
    if (workmen.getImageContent() != null) {
      setImageContent(workmen.getImageContent());
    }
    
    if (workmen.getUnitId() != null) {
      setUnitId(workmen.getUnitId());
    }
    
    if (workmen.isBSR() != isBSR) {
      setIsBSR(workmen.isBSR());
    }
    
    if (workmen.getLaborAccountEffectiveDate() != null) {
        setLaborAccountEffectiveDate(workmen.getLaborAccountEffectiveDate());
      }
    
    	Map<String, String> customFields = new HashMap();
    	
        customFields.put("Labor Type", (String)workmen.getCustomFields().get("Labor Type"));
    	/*customFields.put("supplyType", (String)workmen.getCustomFields().get("supplyType"))*/;
        customFields.put("Department", (String)workmen.getCustomFields().get("Department"));
        customFields.put("Section", (String)workmen.getCustomFields().get("Section"));
        customFields.put("Employee Type", (String)workmen.getCustomFields().get("Employee Type"));
        
        customFields.put("VDA", (String)workmen.getCustomFields().get("VDA"));
        customFields.put("PDA", (String)workmen.getCustomFields().get("PDA"));
        customFields.put("HRA", (String)workmen.getCustomFields().get("HRA"));
        customFields.put("Conveyance", (String)workmen.getCustomFields().get("Conveyance"));
        customFields.put("Special Allowance", (String)workmen.getCustomFields().get("Special Allowance"));
        customFields.put("Shift Allowance", (String)workmen.getCustomFields().get("Shift Allowance"));
        customFields.put("Dust Allowance - Monthly", (String)workmen.getCustomFields().get("Dust Allowance - Monthly"));
        customFields.put("Medical", (String)workmen.getCustomFields().get("Medical"));
        customFields.put("LTA", (String)workmen.getCustomFields().get("LTA"));
        customFields.put("Education – Annual", (String)workmen.getCustomFields().get("Education – Annual"));
        customFields.put("ESICWC", (String)workmen.getCustomFields().get("ESICWC"));
        customFields.put("ESIC Exempt", (String)workmen.getCustomFields().get("ESIC Exempt"));
        customFields.put("PF Exempt", (String)workmen.getCustomFields().get("PF Exempt"));
        customFields.put("Safety Training Date", (String)workmen.getCustomFields().get("Safety Training Date"));
        
        customFields.put("Workorder", (String)workmen.getCustomFields().get("Workorder"));
        customFields.put("ItemServiceNumber", (String)workmen.getCustomFields().get("ItemServiceNumber"));
       // customFields.put("ItemNumber", (String)workmen.getCustomFields().get("ItemNumber"));
       // customFields.put("ServiceLineItem", (String) workmen.getCustomFields().get("ServiceLineItem"));
        customFields.put("Gender", (String) workmen.getCustomFields().get("Gender"));
        customFields.put("Aadhar Number", (String) workmen.getCustomFields().get("Aadhar Number"));
        customFields.put("Mobile Number", (String) workmen.getCustomFields().get("Mobile Number"));
        customFields.put("Vehicle Type", (String) workmen.getCustomFields().get("Vehicle Type"));
        customFields.put("Vehicle Rent", (String) workmen.getCustomFields().get("Vehicle Rent"));
        customFields.put("Quarter Rent", (String) workmen.getCustomFields().get("Quarter Rent"));
        customFields.put("Shift Hours", (String) workmen.getCustomFields().get("Shift Hours"));
        setCustomFields(customFields);
      }
    
    
  public Person getPerson() {
    return person;
  }
  
  public void setPerson(Person person) {
    this.person = person;
  }
  
  public Boolean isBSR() {
    return isBSR;
  }
  
  public void setIsBSR(Boolean isBSR) {
    this.isBSR = isBSR;
  }
  
  public static List<Workmen> getWorkmenByEmployeeCodeAndUnit(String empCode2, String unitId) {
    return new CMSService().getWorkmenByCodeAndUnit(empCode2, unitId);
  }
  
  public static List<Workmen> getWorkmenByEmployeeNameAndUnit(String empName, String unitId, String statusId)
  {
    return new CMSService().getWorkmenByNameAndUnit(empName, unitId, statusId);
  }
  
  public static List<Workmen> getWorkmenByUnit(ObjectIdLong unitId) {
    return new CMSService().getWorkmenByUnit(unitId);
  }
  
  public String getBadgeNum() {
    return badgeNum;
  }
  
  public void setBadgeNum(String badgeNum) {
    this.badgeNum = badgeNum;
  }
  


  public KDate getBadgeNumberEffectiveDate()
  {
    return badgeNumberEffectiveDate;
  }
  



  public void setBadgeNumberEffectiveDate(KDate badgeNumberEffectiveDate)
  {
    this.badgeNumberEffectiveDate = badgeNumberEffectiveDate;
  }
  


  public DeviceGroup getDeviceGroup()
  {
    return deviceGroup;
  }
  



  public void setDeviceGroup(DeviceGroup deviceGroup)
  {
    this.deviceGroup = deviceGroup;
  }
  


  public String getImageContent()
  {
    return imageContent;
  }
  



  public void setImageContent(String imageContent)
  {
    this.imageContent = imageContent;
  }
  


  public String getUnitId()
  {
    return unitId;
  }

  public void setUnitId(String unitId)
  {
    this.unitId = unitId;
  }

public KDate getLaborAccountEffectiveDate() {
	return laborAccountEffectiveDate;
}

public void setLaborAccountEffectiveDate(KDate laborAccountEffectiveDate) {
	this.laborAccountEffectiveDate = laborAccountEffectiveDate;
} 

  
}
