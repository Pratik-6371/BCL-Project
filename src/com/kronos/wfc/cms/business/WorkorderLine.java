package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.List;






public class WorkorderLine
{
  private ObjectIdLong wkLineId;
  private ObjectIdLong tradeId;
  private ObjectIdLong skillId;
  private Integer qty;
  private Double rate;
  private ObjectIdLong workorderId;
  private String tradeNm;
  private String skillNm;
  private String jobDesc;
  private String itemDesc;
  private Boolean isDeliveryCompleted;
  private Integer qtyCompleted;
  private Boolean deleteSW;
  private String serviceCode;
  private String wbsElement;
  private String uom;
  private String itemNumber;
  private String serviceLineItemNumber;
  private String balanceQuantity;
  private String item_serviceItem_number;
  
  public WorkorderLine(ObjectIdLong wkLineId, ObjectIdLong tradeId, ObjectIdLong skillId, Integer qty,
		  			Double rate, ObjectIdLong workorderId, String jobDesc, String itemDesc, 
		  			Boolean isDeliveryCompleted, Integer qtyCompleted, Boolean deleteSw, 
		  			String serviceCode, String wbsElement, String uom, String itemNumber, String serviceLineItemNumber)
  {
    this.wkLineId = wkLineId;
    this.tradeId = tradeId;
    this.skillId = skillId;
    this.qty = qty;
    this.rate = rate;
    this.workorderId = workorderId;
    
    if (tradeId != null)
      tradeNm = Trade.retrieveById(tradeId).getTradeName();
    if (skillId != null)
      skillNm = Skill.retrieveSkill(skillId).getSkillNm();
    this.jobDesc = jobDesc;
    this.itemDesc = itemDesc;
    this.isDeliveryCompleted = isDeliveryCompleted;
    this.qtyCompleted = qtyCompleted;
    deleteSW = deleteSw;
    this.serviceCode = serviceCode;
    this.wbsElement = wbsElement;
    this.uom = uom;
    this.itemNumber = itemNumber;
    this.serviceLineItemNumber = serviceLineItemNumber;
  }
  
  public WorkorderLine(ObjectIdLong wkLineId, ObjectIdLong tradeId, ObjectIdLong skillId, Integer qty,
			Double rate, ObjectIdLong workorderId, String jobDesc, String itemDesc, 
			Boolean isDeliveryCompleted, Integer qtyCompleted, Boolean deleteSw, 
			String serviceCode, String wbsElement, String uom)
{
this.wkLineId = wkLineId;
this.tradeId = tradeId;
this.skillId = skillId;
this.qty = qty;
this.rate = rate;
this.workorderId = workorderId;

if (tradeId != null)
tradeNm = Trade.retrieveById(tradeId).getTradeName();
if (skillId != null)
skillNm = Skill.retrieveSkill(skillId).getSkillNm();
this.jobDesc = jobDesc;
this.itemDesc = itemDesc;
this.isDeliveryCompleted = isDeliveryCompleted;
this.qtyCompleted = qtyCompleted;
deleteSW = deleteSw;
this.serviceCode = serviceCode;
this.wbsElement = wbsElement;
this.uom = uom;
}
  

  public WorkorderLine(ObjectIdLong workorderId2, ObjectIdLong workorderLineId, String lineItem, String serviceLineItem) {
	
	  this.workorderId=workorderId2;
	  this.wkLineId=workorderLineId;
	  this.itemNumber=lineItem;
	  this.serviceLineItemNumber=serviceLineItem;
}

public WorkorderLine(ObjectIdLong workorderId2, ObjectIdLong workorderLineId, String item_serviceItem_number2) {
	
	  this.workorderId=workorderId2;
	  this.wkLineId=workorderLineId;
	  this.item_serviceItem_number=item_serviceItem_number2;
}

public String getJobDesc()
  {
    return jobDesc;
  }
  

  public void setJobDesc(String jobDesc)
  {
    this.jobDesc = jobDesc;
  }
  

  public String getItemDesc()
  {
    return itemDesc;
  }
  

  public void setItemDesc(String itemDesc)
  {
    this.itemDesc = itemDesc;
  }
  

  public Boolean getIsDeliveryCompleted()
  {
    return isDeliveryCompleted;
  }
  

  public void setIsDeliveryCompleted(Boolean isDeliveryCompleted)
  {
    this.isDeliveryCompleted = isDeliveryCompleted;
  }
  

  public Integer getQtyCompleted()
  {
    return qtyCompleted;
  }
  

  public void setQtyCompleted(Integer qtyCompleted)
  {
    this.qtyCompleted = qtyCompleted;
  }
  

  public Boolean getDeleteSW()
  {
    return deleteSW;
  }
  

  public void setDeleteSW(Boolean deleteSW)
  {
    this.deleteSW = deleteSW;
  }
  

  public ObjectIdLong getWkLineId()
  {
    return wkLineId;
  }
  

  public void setWkLineId(ObjectIdLong wkLineId)
  {
    this.wkLineId = wkLineId;
  }
  

  public String getTradeNm()
  {
    return tradeNm;
  }
  

  public void setTradeNm(String tradeNm)
  {
    this.tradeNm = tradeNm;
  }
  

  public String getSkillNm()
  {
    return skillNm;
  }
  

  public void setSkillNm(String skillNm)
  {
    this.skillNm = skillNm;
  }
  

  public ObjectIdLong getTradeId()
  {
    return tradeId;
  }
  

  public void setTradeId(ObjectIdLong tradeId)
  {
    this.tradeId = tradeId;
  }
  

  public ObjectIdLong getSkillId()
  {
    return skillId;
  }
  

  public void setSkillId(ObjectIdLong skillId)
  {
    this.skillId = skillId;
  }
  

  public Integer getQty()
  {
    return qty;
  }
  

  public void setQty(Integer qty)
  {
    this.qty = qty;
  }
  

  public Double getRate()
  {
    return rate;
  }
  

  public void setRate(Double rate)
  {
    this.rate = rate;
  }
  

  public ObjectIdLong getWorkorderId()
  {
    return workorderId;
  }
  

  public void setWorkorderId(ObjectIdLong workorderId)
  {
    this.workorderId = workorderId;
  }
  

  public static List<WorkorderLine> retriveWorkOrderln(String workorderId)
  {
    if (workorderId != null)
      return new CMSService().retrieveWorkOrderLines(new ObjectIdLong(workorderId));
    return new ArrayList();
  }
  


  public static WorkorderLine retriveWorkOrderlnByWOId(String workorderLnId)
  {
    if (workorderLnId != null)
      return new CMSService().retrieveWorkOrderLineById(new ObjectIdLong(workorderLnId));
    return null;
  }
  

  public String getServiceCode()
  {
    return serviceCode;
  }
  

  public void setServiceCode(String serviceCode)
  {
    this.serviceCode = serviceCode;
  }
  

  public String getWBSElement()
  {
    return wbsElement;
  }
  

  public String getUOM()
  {
    return uom;
  }


public String getItemNumber() {
	return itemNumber;
}


public void setItemNumber(String itemNumber) {
	this.itemNumber = itemNumber;
}


public String getServiceLineItemNumber() {
	return serviceLineItemNumber;
}


public void setServiceLineItemNumber(String serviceLineItemNumber) {
	this.serviceLineItemNumber = serviceLineItemNumber;
}

public String getBalanceQuantity() {
	return balanceQuantity;
}


public String getItem_serviceItem_number() {
	return item_serviceItem_number;
}

public void setItem_serviceItem_number(String item_serviceItem_number) {
	this.item_serviceItem_number = item_serviceItem_number;
}

public void setBalanceQuantity(String balanceQuantity) {
	this.balanceQuantity = balanceQuantity;
}

public static List<WorkorderLine> getWorkorderLineByUnitAndContId(ObjectIdLong unitId,
		ObjectIdLong contrId) {
	
	return new CMSService().retrieveWorkorderLineByUnitAndContId(unitId,contrId);
}
  
  
}
