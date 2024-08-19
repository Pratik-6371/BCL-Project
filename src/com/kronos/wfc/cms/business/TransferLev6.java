package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class TransferLev6 {
	
	private ObjectIdLong workorderId;
	private ObjectIdLong workorderLnId;
	private String workorderNum;
	private String lineItemNum;
	private String serviceLineItem;
	
	
	public ObjectIdLong getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(ObjectIdLong workorderId) {
		this.workorderId = workorderId;
	}
	
	public ObjectIdLong getWorkorderLnId() {
		return workorderLnId;
	}
	public void setWorkorderLnId(ObjectIdLong workorderLnId) {
		this.workorderLnId = workorderLnId;
	}
	public String getWorkorderNum() {
		return workorderNum;
	}
	public void setWorkorderNum(String workorderNum) {
		this.workorderNum = workorderNum;
	}
	public String getLineItemNum() {
		return lineItemNum;
	}
	public void setLineItemNum(String lineItemNum) {
		this.lineItemNum = lineItemNum;
	}
	public String getServiceLineItem() {
		return serviceLineItem;
	}
	public void setServiceLineItem(String serviceLineItem) {
		this.serviceLineItem = serviceLineItem;
	}
	
	

}
