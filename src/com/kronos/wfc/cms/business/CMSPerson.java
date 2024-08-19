package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class CMSPerson
{
  private boolean isManager = false;
  private String lastName = "";
  private String firstName = "";
  private String userName = "";
  private String password = "";
  private String primaryLA = "";
  private String personNum = "";
  
  private ObjectIdLong personId;
  
  public CMSPerson(ObjectIdLong personId, String personNum, boolean isManager, String lastName, String firstName, String userName, String password, String primaryLA, String license)
  {
    setPersonId(personId);
    setPersonNum(personNum);
    this.isManager = isManager;
    this.lastName = lastName;
    this.firstName = firstName;
    this.userName = userName;
    this.password = password;
    this.primaryLA = primaryLA;
    this.license = license;
  }
  
  public boolean isManager() { return isManager; }
  
  public void setManager(boolean isManager) {
    this.isManager = isManager;
  }
  
  public String getLastName() { return lastName; }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getFirstName() { return firstName; }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getUserName() { return userName; }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public String getPassword() { return password; }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getPrimaryLA() { return primaryLA; }
  
  public void setPrimaryLA(String primaryLA) {
    this.primaryLA = primaryLA;
  }
  
  public String getLicense() { return license; }
  
  public void setLicense(String license) {
    this.license = license;
  }
  
  public String getPersonNum() { return personNum; }
  
  public void setPersonNum(String personNum) {
    this.personNum = personNum;
  }
  
  public ObjectIdLong getPersonId() { return personId; }
  

  public void setPersonId(ObjectIdLong personId) { this.personId = personId; }
  
  private String license = "";
}
