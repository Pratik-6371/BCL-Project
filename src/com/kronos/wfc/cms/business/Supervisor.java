package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;


public class Supervisor
{
  private ObjectIdLong suprId;
  private String name;
  private ObjectIdLong contrId;
  private String userName;
  private String password;
  private String emailAddr;
  private String phoneNum;
  private Boolean selected = Boolean.valueOf(false);
  












  public Supervisor(ObjectIdLong suprId, String name, ObjectIdLong contrId, String userName, String password, String emailAddr, String phoneNum)
  {
    this.suprId = suprId;
    this.name = name;
    this.contrId = contrId;
    this.userName = userName;
    this.password = password;
    this.emailAddr = emailAddr;
    this.phoneNum = phoneNum;
  }
  
  public ObjectIdLong getSuprId() {
    return suprId;
  }
  
  public void setSuprId(ObjectIdLong suprId) { this.suprId = suprId; }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) { this.name = name; }
  
  public ObjectIdLong getContrId() {
    return contrId;
  }
  
  public void setContrId(ObjectIdLong contrId) { this.contrId = contrId; }
  
  public String getUserName() {
    return userName;
  }
  
  public void setUserName(String userName) { this.userName = userName; }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) { this.password = password; }
  
  public String getEmailAddr() {
    return emailAddr;
  }
  
  public void setEmailAddr(String emailAddr) { this.emailAddr = emailAddr; }
  
  public String getPhoneNum() {
    return phoneNum;
  }
  
  public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
  
  public Boolean getSelected() {
    return selected;
  }
  
  public void setSelected(Boolean selected) { this.selected = selected; }
  
  public static List<Supervisor> getContractorHead(ObjectIdLong contractorId)
  {
    return new CMSService().getSupervisorsByContractorId(contractorId);
  }
  
  public static Supervisor getSupervisor(ObjectIdLong suprId) { return new CMSService().getSupervisorById(suprId); }
}
