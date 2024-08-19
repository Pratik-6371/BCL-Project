package com.kronos.wfc.cms.business;

import java.util.List;

public class Supply {
	private String supplyId;
	private String supplyName;
	public Supply(){}
	public Supply(String supplyId, String supplyName)
	{
		this.supplyId = supplyId;
		this.supplyName = supplyName;
	}
	public String getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(String supplyId) {
		this.supplyId = supplyId;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	
	
}
