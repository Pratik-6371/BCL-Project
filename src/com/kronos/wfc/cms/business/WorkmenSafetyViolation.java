package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.List;









public class WorkmenSafetyViolation
{
  private ObjectIdLong id;
  private SafetyViolation violation;
  private String violationDesc;
  private KDateTime violationDate;
  private String actionTaken;
  private String updatedBy;
  private KDateTime updatedOn;
  
  public WorkmenSafetyViolation(ObjectIdLong id, SafetyViolation trn, String violationDesc, KDateTime violationDate, String actionTaken, String updatedBy, KDateTime updatedOn)
  {
    this.id = id;
    violation = trn;
    this.violationDesc = violationDesc;
    setViolationDate(violationDate);
    setActionTaken(actionTaken);
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
  }
  
  public ObjectIdLong getId() { return id; }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public SafetyViolation getSafetyViolation() { return violation; }
  
  public void setSafetyViolation(SafetyViolation trn) {
    violation = trn;
  }
  
  public String getUpdatedBy() {
    return updatedBy;
  }
  
  public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
  
  public KDateTime getUpdatedOn() {
    return updatedOn;
  }
  
  public void setUpdatedOn(KDateTime updatedOn) { this.updatedOn = updatedOn; }
  
  public static List<WorkmenSafetyViolation> retrieveByEmployeeCode(String personNum)
  {
    return new CMSService().getWorkmenSafetyViolations(personNum);
  }
  
  public KDateTime getViolationDate() { return violationDate; }
  
  public void setViolationDate(KDateTime violationDate) {
    this.violationDate = violationDate;
  }
  
  public String getActionTaken() { return actionTaken; }
  
  public void setActionTaken(String actionTaken) {
    this.actionTaken = actionTaken;
  }
  
  public String getViolationDesc() { return violationDesc; }
  
  public void setViolationDesc(String violationDesc) {
    this.violationDesc = violationDesc;
  }
}
