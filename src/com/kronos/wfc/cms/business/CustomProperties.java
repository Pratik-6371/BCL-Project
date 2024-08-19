package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;



public class CustomProperties
{
  private String propertyName;
  private String propertyValue;
  private ObjectIdLong propertyId;
  private ObjectIdLong principalEmployeeId;
  
  public CustomProperties() {}
  
  public CustomProperties(ObjectIdLong propId, String propertyName, String propertyValue, ObjectIdLong unitId)
  {
    propertyId = propId;
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
    principalEmployeeId = unitId;
  }
  
  public String getPropertyName() {
    return propertyName;
  }
  
  public void setPropertyName(String propertyName) { this.propertyName = propertyName; }
  
  public String getPropertyValue() {
    return propertyValue;
  }
  
  public void setPropertyValue(String propertyValue) { this.propertyValue = propertyValue; }
  
  public ObjectIdLong getId()
  {
    return propertyId;
  }
  
  public void setId(ObjectIdLong propId) { propertyId = propId; }
  
  public ObjectIdLong getPrincipalEmployeeId()
  {
    return principalEmployeeId;
  }
  
  public void setPrincipalEmployeeId(ObjectIdLong principalEmployeeId) { this.principalEmployeeId = principalEmployeeId; }
}
