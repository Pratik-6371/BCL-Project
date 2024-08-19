package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class SafetyModule
{
  private ObjectIdLong safetyId;
  private String name;
  private String description;
  
  public SafetyModule(ObjectIdLong safetyId, String name, String description)
  {
    this.safetyId = safetyId;
    this.name = name;
    this.description = description;
  }
  
  public ObjectIdLong getSafetyId() {
    return safetyId;
  }
  
  public void setSafetyId(ObjectIdLong safetyId) {
    this.safetyId = safetyId;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
}
