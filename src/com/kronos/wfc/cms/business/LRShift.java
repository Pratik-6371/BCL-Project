package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;


public class LRShift
{
  private ObjectIdLong lrId;
  private ObjectIdLong shiftId;
  private Integer qty;
  
  public LRShift(ObjectIdLong lrId, ObjectIdLong shiftId, Integer qty)
  {
    this.lrId = lrId;
    this.shiftId = shiftId;
    this.qty = qty;
  }
  
  public ObjectIdLong getLrId() { return lrId; }
  
  public void setLrId(ObjectIdLong lrId) {
    this.lrId = lrId;
  }
  
  public ObjectIdLong getShiftId() { return shiftId; }
  
  public void setShiftId(ObjectIdLong shiftId) {
    this.shiftId = shiftId;
  }
  
  public Integer getQty() { return qty; }
  
  public void setQty(Integer qty) {
    this.qty = qty;
  }
  
  public void update() { new CMSService().updateLRShift(this); }
  
  public void insert()
  {
    new CMSService().insertLRShift(this);
  }
}
