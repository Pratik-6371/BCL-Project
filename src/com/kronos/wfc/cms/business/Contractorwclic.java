package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;

public class Contractorwclic {
	
	  private ObjectIdLong wc1Id;
	  private ObjectIdLong wc2Id;
	  private ObjectIdLong wc3Id;
	  private ObjectIdLong wc4Id;
	  private String wc1;
	  private String wc2;
	  private String wc3;
	  private String wc4;
	  private String wc1Coverage;
	  private String wc2Coverage;
	  private String wc3Coverage;
	  private String wc4Coverage;
	  private KDate wc1FromDate;
	  private KDate wc1ToDate;
	  private KDate wc2FromDate;
	  private KDate wc2ToDate;
	  private KDate wc3FromDate;
	  private KDate wc3ToDate;
	  private KDate wc4FromDate;
	  private KDate wc4ToDate;
	  private ObjectIdLong contrId;
	  private ObjectIdLong unitId;
	  
	public Contractorwclic(String wc1, String wc2,String wc3,String wc4, String wc1Coverage, String wc2Coverage,String wc3Coverage, String wc4Coverage , KDate wc1FromDate,
			KDate wc1ToDate, KDate wc2FromDate, KDate wc2ToDate, KDate wc3FromDate, KDate wc3ToDate, KDate wc4FromDate, KDate wc4ToDate) {
		super();
		this.wc1 = wc1;
		this.wc2 = wc2;
		this.wc3 = wc3;
		this.wc4 = wc4;
		this.wc1Coverage = wc1Coverage;
		this.wc2Coverage = wc2Coverage;
		this.wc3Coverage = wc3Coverage;
		this.wc4Coverage = wc4Coverage;
		this.wc1FromDate = wc1FromDate;
		this.wc1ToDate = wc1ToDate;
		this.wc2FromDate = wc2FromDate;
		this.wc2ToDate = wc2ToDate;
		this.wc3FromDate = wc3FromDate;
		this.wc3ToDate = wc3ToDate;
		this.wc4FromDate = wc4FromDate;
		this.wc4ToDate = wc4ToDate;
	}
	public Contractorwclic() {
		// TODO Auto-generated constructor stub
	}
	
	public ObjectIdLong getWc1Id() {
		return wc1Id;
	}
	public void setWc1Id(ObjectIdLong wc1Id) {
		this.wc1Id = wc1Id;
	}
	public ObjectIdLong getWc2Id() {
		return wc2Id;
	}
	public void setWc2Id(ObjectIdLong wc2Id) {
		this.wc2Id = wc2Id;
	}
	public String getWc1() {
		return wc1;
	}
	public void setWc1(String wc1) {
		this.wc1 = wc1;
	}
	public String getWc2() {
		return wc2;
	}
	public void setWc2(String wc2) {
		this.wc2 = wc2;
	}
	public String getWc1Coverage() {
		return wc1Coverage;
	}
	public void setWc1Coverage(String wc1Coverage) {
		this.wc1Coverage = wc1Coverage;
	}
	public String getWc2Coverage() {
		return wc2Coverage;
	}
	public void setWc2Coverage(String wc2Coverage) {
		this.wc2Coverage = wc2Coverage;
	}
	public KDate getWc1FromDate() {
		return wc1FromDate;
	}
	public void setWc1FromDate(KDate wc1FromDate) {
		this.wc1FromDate = wc1FromDate;
	}
	public KDate getWc1ToDate() {
		return wc1ToDate;
	}
	public void setWc1ToDate(KDate wc1ToDate) {
		this.wc1ToDate = wc1ToDate;
	}
	public KDate getWc2FromDate() {
		return wc2FromDate;
	}
	public void setWc2FromDate(KDate wc2FromDate) {
		this.wc2FromDate = wc2FromDate;
	}
	public KDate getWc2ToDate() {
		return wc2ToDate;
	}
	public void setWc2ToDate(KDate wc2ToDate) {
		this.wc2ToDate = wc2ToDate;
	}
	
	
	
	public ObjectIdLong getWc3Id() {
		return wc3Id;
	}
	public void setWc3Id(ObjectIdLong wc3Id) {
		this.wc3Id = wc3Id;
	}
	public ObjectIdLong getWc4Id() {
		return wc4Id;
	}
	public void setWc4Id(ObjectIdLong wc4Id) {
		this.wc4Id = wc4Id;
	}
	public String getWc3() {
		return wc3;
	}
	public void setWc3(String wc3) {
		this.wc3 = wc3;
	}
	public String getWc4() {
		return wc4;
	}
	public void setWc4(String wc4) {
		this.wc4 = wc4;
	}
	public String getWc3Coverage() {
		return wc3Coverage;
	}
	public void setWc3Coverage(String wc3Coverage) {
		this.wc3Coverage = wc3Coverage;
	}
	public String getWc4Coverage() {
		return wc4Coverage;
	}
	public void setWc4Coverage(String wc4Coverage) {
		this.wc4Coverage = wc4Coverage;
	}
	public KDate getWc3FromDate() {
		return wc3FromDate;
	}
	public void setWc3FromDate(KDate wc3FromDate) {
		this.wc3FromDate = wc3FromDate;
	}
	public KDate getWc3ToDate() {
		return wc3ToDate;
	}
	public void setWc3ToDate(KDate wc3ToDate) {
		this.wc3ToDate = wc3ToDate;
	}
	public KDate getWc4FromDate() {
		return wc4FromDate;
	}
	public void setWc4FromDate(KDate wc4FromDate) {
		this.wc4FromDate = wc4FromDate;
	}
	public KDate getWc4ToDate() {
		return wc4ToDate;
	}
	public void setWc4ToDate(KDate wc4ToDate) {
		this.wc4ToDate = wc4ToDate;
	}
	public ObjectIdLong getContrId() {
		return contrId;
	}
	public void setContrId(ObjectIdLong contrId) {
		this.contrId = contrId;
	}
	public ObjectIdLong getUnitId() {
		return unitId;
	}
	public void setUnitId(ObjectIdLong unitId) {
		this.unitId = unitId;
	}

}
