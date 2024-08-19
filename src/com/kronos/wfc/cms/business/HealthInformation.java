package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.List;







public class HealthInformation
{
  private ObjectIdLong id;
  private String healthText;
  private String healthDesc;
  private KDateTime healthTestDate;
  private String majorConcerns;
  private KDateTime nextMedicalTest;
  private String updatedBy;
  private KDateTime updatedOn;
  
  public HealthInformation(ObjectIdLong id, String healthText, String healthDesc, KDateTime healthTestDate, String majorConcerns, KDateTime nextMedicalTest, String updatedBy, KDateTime updatedOn)
  {
    this.id = id;
    this.healthText = healthText;
    this.healthDesc = healthDesc;
    this.healthTestDate = healthTestDate;
    this.majorConcerns = majorConcerns;
    this.nextMedicalTest = nextMedicalTest;
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
  }
  
  public ObjectIdLong getId() {
    return id;
  }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public String getHealthText() {
    return healthText;
  }
  
  public void setHealthText(String healthText) { this.healthText = healthText; }
  
  public String getHealthDesc() {
    return healthDesc;
  }
  
  public void setHealthDesc(String healthDesc) { this.healthDesc = healthDesc; }
  
  public KDateTime getHealthTestDate() {
    return healthTestDate;
  }
  
  public void setHealthTestDate(KDateTime healthTestDate) { this.healthTestDate = healthTestDate; }
  
  public String getMajorConcerns() {
    return majorConcerns;
  }
  
  public void setMajorConcerns(String majorConcerns) { this.majorConcerns = majorConcerns; }
  
  public KDateTime getNextMedicalTest() {
    return nextMedicalTest;
  }
  
  public void setNextMedicalTest(KDateTime nextMedicalTest) { this.nextMedicalTest = nextMedicalTest; }
  

  public static List<HealthInformation> retrieveByEmployeeCode(String personNum)
  {
    return new CMSService().getHealthInformationList(personNum);
  }
  
  public String getUpdatedBy() {
    return updatedBy;
  }
  
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
  
  public KDateTime getUpdatedOn() {
    return updatedOn;
  }
  
  public void setUpdatedOn(KDateTime updatedOn) {
    this.updatedOn = updatedOn;
  }
  
  public boolean isBlank() {
    if (("".equals(healthText)) && ("".equals(healthDesc)) && ("".equals(majorConcerns)) && (healthTestDate == null) && (nextMedicalTest == null))
      return true;
    return false;
  }
}
