package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class TimeDetailRow
{
  private ObjectIdLong empId;
  private Boolean mApproval;
  private Boolean hasException;
  private Integer regHours;
  private Integer overtimeHours;
  private Boolean hasUnApprovedOT;
  
  public TimeDetailRow(ObjectIdLong empId, Boolean mApproval, Boolean hasException, Integer regHours2, Integer overtimehours, Boolean hasUnAOT)
  {
    this.empId = empId;
    this.mApproval = mApproval;
    this.hasException = hasException;
    regHours = regHours2;
    overtimeHours = overtimehours;
    setHasUnApprovedOT(hasUnAOT);
  }
  
  public ObjectIdLong getEmpId() { return empId; }
  
  public void setEmpId(ObjectIdLong empId) {
    this.empId = empId;
  }
  
  public Boolean getmApproval() { return mApproval; }
  
  public void setmApproval(Boolean mApproval) {
    this.mApproval = mApproval;
  }
  
  public Boolean getHasException() { return hasException; }
  
  public void setHasException(Boolean hasException) {
    this.hasException = hasException;
  }
  
  public Integer getRegHours() { return regHours; }
  
  public void setRegHours(Integer regHours) {
    this.regHours = regHours;
  }
  
  public Integer getOvertimeHours() { return overtimeHours; }
  
  public void setOvertimeHours(Integer overtimeHours) {
    this.overtimeHours = overtimeHours;
  }
  
  public Boolean getHasUnApprovedOT() { return hasUnApprovedOT; }
  
  public void setHasUnApprovedOT(Boolean hasUnApprovedOT) {
    this.hasUnApprovedOT = hasUnApprovedOT;
  }
}
