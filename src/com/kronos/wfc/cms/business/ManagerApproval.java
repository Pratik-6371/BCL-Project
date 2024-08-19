package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class ManagerApproval
{
  private ObjectIdLong id;
  private Integer countManagers;
  private String approvalText;
  
  public ManagerApproval(ObjectIdLong objectIdLong, Integer count, String approvalString)
  {
    id = objectIdLong;
    countManagers = count;
    approvalText = approvalString;
  }
  
  public ObjectIdLong getId() {
    return id;
  }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public Integer getCountManagers() {
    return countManagers;
  }
  
  public void setCountManagers(Integer countManagers) {
    this.countManagers = countManagers;
  }
  
  public String getApprovalText() {
    return approvalText;
  }
  
  public void setApprovalText(String approvalText) {
    this.approvalText = approvalText;
  }
}
