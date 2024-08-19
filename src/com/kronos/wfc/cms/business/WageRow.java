package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class WageRow
{
  private ObjectIdLong personId;
  private ObjectIdLong laborAcctId;
  private Integer regularHours;
  private Integer otHours;
  private Boolean type;
  private Integer holHours;
  
  public WageRow(ObjectIdLong personId, ObjectIdLong laborAcctId, Integer regularHours, Integer otHours, Integer holHours, Boolean type)
  {
    this.personId = personId;
    this.laborAcctId = laborAcctId;
    this.regularHours = regularHours;
    this.otHours = otHours;
    this.holHours = holHours;
    this.type = type;
  }
  
  public ObjectIdLong getPersonId() { return personId; }
  
  public void setPersonId(ObjectIdLong personId) {
    this.personId = personId;
  }
  
  public ObjectIdLong getLaborAcctId() { return laborAcctId; }
  
  public void setLaborAcctId(ObjectIdLong laborAcctId) {
    this.laborAcctId = laborAcctId;
  }
  
  public Integer getRegularHours() { return regularHours; }
  
  public void setRegularHours(Integer regularHours) {
    this.regularHours = regularHours;
  }
  
  public Integer getOtHours() { return otHours; }
  
  public void setOtHours(Integer otHours) {
    this.otHours = otHours;
  }
  
  public Boolean getType() { return type; }
  
  public void setType(Boolean type) {
    this.type = type;
  }
  
  public int getHolidayHours() { return holHours.intValue(); }
  
  public void setHolidayHours(int hours) {
    holHours = Integer.valueOf(hours);
  }
}
