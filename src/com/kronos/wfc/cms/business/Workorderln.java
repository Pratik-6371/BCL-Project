package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class Workorderln { private ObjectIdLong workorderlnId;
  private ObjectIdLong workorderId;
  private ObjectIdLong serviceId;
  private Double rate;
  private Integer qty;
  
  public Workorderln() {}
  
  public ObjectIdLong getWorkorderlnId() { return workorderlnId; }
  
  public void setWorkorderlnId(ObjectIdLong workorderlnId) {
    this.workorderlnId = workorderlnId;
  }
  
  public ObjectIdLong getWorkorderId() { return workorderId; }
  
  public void setWorkorderId(ObjectIdLong workorderId) {
    this.workorderId = workorderId;
  }
  
  public ObjectIdLong getServiceId() { return serviceId; }
  
  public void setServiceId(ObjectIdLong serviceId) {
    this.serviceId = serviceId;
  }
  
  public Double getRate() { return rate; }
  
  public void setRate(Double rate) {
    this.rate = rate;
  }
  
  public Integer getQty() { return qty; }
  
  public void setQty(Integer qty) {
    this.qty = qty;
  }
}
