package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.List;

public class Manager
{
  public static Manager doRetrieveById(ObjectIdLong id)
  {
    return new CMSService().getManagerById(id);
  }
  



  public static List<Manager> getDepartmentManagers(ObjectIdLong depid)
  {
    return new CMSService().getManagersByDept(depid);
  }
  
  public static Manager getSectionHead(ObjectIdLong sectionId, ObjectIdLong unitId) {
    return new CMSService().getSectionHead(sectionId, unitId);
  }
  
  private ObjectIdLong deptId = null;
  
  private KDate dot = null;
  
  private String emailAddr = null;
  
  private ObjectIdLong id = null;
  
  private Boolean isDeptMgr = null;
  
  private String mobilenum = null;
  
  private String name = null;
  
  private String passwd = null;
  
  private ObjectIdLong sectionId = null;
  
  private Boolean selected = Boolean.valueOf(false);
  
  private String userName = null;
  

  public Manager() {}
  

  public Manager(ObjectIdLong id, String name, String userName, String passwd, String mobilenum, String emailAddr, ObjectIdLong deptId, ObjectIdLong sectionId, Boolean isDeptMgr, KDate dot)
  {
    this.id = id;
    this.name = name;
    this.userName = userName;
    this.passwd = passwd;
    this.mobilenum = mobilenum;
    this.emailAddr = emailAddr;
    this.deptId = deptId;
    this.sectionId = sectionId;
    this.isDeptMgr = isDeptMgr;
    this.dot = dot;
  }
  
  public void doAdd() {
    new CMSService().addManager(this);
  }
  
  public void doDelete()
  {
    new CMSService().deleteManager(this);
  }
  
  public void doUpdate() { new CMSService().updateManager(this); }
  
  public boolean equals(Manager manager)
  {
    if ((manager == null) || (manager.getId() == null) || (getId() == null)) {
      return false;
    }
    if ((manager.getId().longValue() == getId().longValue()) && 
      (manager.getDeptId().longValue() == getDeptId().longValue()) && 
      (manager.getSectionId().longValue() == getSectionId().longValue()) && 
      (manager.getUserName().equalsIgnoreCase(getUserName())) && 
      (manager.getPasswd().equalsIgnoreCase(getPasswd())) && 
      (manager.getEmailAddr().equalsIgnoreCase(getEmailAddr())) && 
      (manager.getName().equalsIgnoreCase(getName())) && 
      (manager.getMobilenum().equalsIgnoreCase(getMobilenum())) && 
      (manager.getDot().equals(getDot()))) {
      return true;
    }
    return false;
  }
  
  public ObjectIdLong getDeptId() { return deptId; }
  


  public KDate getDot()
  {
    return dot;
  }
  
  public String getEmailAddr() { return emailAddr; }
  
  public ObjectIdLong getId() {
    return id;
  }
  
  public Boolean getIsDeptMgr() { return isDeptMgr; }
  
  public String getMobilenum() {
    return mobilenum;
  }
  
  public String getName() { return name; }
  
  public String getPasswd()
  {
    return passwd;
  }
  
  public ObjectIdLong getSectionId() {
    return sectionId;
  }
  
  public Boolean getSelected() {
    return selected;
  }
  
  public String getUserName() {
    return userName;
  }
  
  public void setDeptId(ObjectIdLong depId) {
    deptId = depId;
  }
  




  public void setDot(KDate dot)
  {
    this.dot = dot;
  }
  
  public void setEmailAddr(String emailAddr) {
    this.emailAddr = emailAddr;
  }
  
  public void setEmailAddress(String string) {
    emailAddr = string;
  }
  
  public void setId(ObjectIdLong id)
  {
    this.id = id;
  }
  
  public void setIsDepartmentManager(boolean b) {
    isDeptMgr = Boolean.valueOf(b);
  }
  
  public void setIsDeptMgr(Boolean isDeptMgr)
  {
    this.isDeptMgr = isDeptMgr;
  }
  
  public void setMobilenum(String mobilenum) {
    this.mobilenum = mobilenum;
  }
  
  public void setMobileNum(String string) {
    mobilenum = string;
  }
  
  public void setName(String string) {
    name = string;
  }
  
  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }
  
  public void setPassword(String string) {
    passwd = string;
  }
  
  public void setSectionId(ObjectIdLong sectionId) {
    this.sectionId = sectionId;
  }
  
  public void setSelected(Boolean selected)
  {
    this.selected = selected;
  }
  
  public void setUserName(String string) {
    userName = string;
  }
}
