package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class ServiceMaster {
  public ServiceMaster() {}
  
  public ObjectIdLong getServiceId() { return serviceId; }
  
  private ObjectIdLong serviceId;
  public void setServiceId(ObjectIdLong serviceId) { this.serviceId = serviceId; }
  
  private String serviceCode;
  public String getServiceCode() { return serviceCode; }
  
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }
  
  public ObjectIdLong getTradeId() { return tradeId; }
  
  public void setTradeId(ObjectIdLong tradeId) {
    this.tradeId = tradeId;
  }
  
  public ObjectIdLong getSkillId() { return skillId; }
  
  public void setSkillId(ObjectIdLong skillId) {
    this.skillId = skillId;
  }
  
  private ObjectIdLong tradeId;
  private ObjectIdLong skillId;
}
