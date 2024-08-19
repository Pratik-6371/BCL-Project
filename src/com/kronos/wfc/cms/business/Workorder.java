package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;





public class Workorder
{
  public static final Integer UNASSIGNED = Integer.valueOf(0);
  public static final Integer ACTIVE = Integer.valueOf(1);
  public static final Integer CLOSE = Integer.valueOf(2);
  public static final Integer ALL = Integer.valueOf(4);
  
  private ObjectIdLong workorderId;
  
  private ObjectIdLong contractorId;
  
  private String name;
  
  private KDate validFrom;
  
  private KDate validTo;
  private ObjectIdLong typeId;
  private ObjectIdLong depId;
  private ObjectIdLong secId;
  private String glcode;
  private String costcenter;
  private ObjectIdLong ownerId;
  private ObjectIdLong suprid;
  private Integer status;
  private ObjectIdLong unitId;
  private String unitName;
  private String statusName;
  private String dName;
  private String wkTypName;
  private String sectionCode;
  private String unitCode;
  private String wkNum;
  private String sectionHead;
  private String wkTypDispName;
  private Contractor contractor;
  
  public Workorder(ObjectIdLong workorderId, ObjectIdLong unitId, ObjectIdLong contractorId, String name, KDate validFrom, KDate validTo, ObjectIdLong typeId, ObjectIdLong depId, ObjectIdLong secId, String glcode, String costcenter, ObjectIdLong ownerId, ObjectIdLong suprid, Integer status, String wkNum)
  {
    this.workorderId = workorderId;
    setUnitId(unitId);
    this.contractorId = contractorId;
    this.name = name;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.typeId = typeId;
    this.depId = depId;
    this.secId = secId;
    this.glcode = glcode;
    this.costcenter = costcenter;
    this.ownerId = ownerId;
    this.suprid = suprid;
    this.status = status;
    Department dep = Department.doRetrieveById(depId);
    dName = dep.getCode();
    statusName = ((String)getStatusList().get(status));
    PrincipalEmployee emp = PrincipalEmployee.doRetrieveById(unitId);
    unitName = emp.getUnitName();
    this.typeId = typeId;
    WorkorderType type = WorkorderType.getByTypeId(typeId);
    setWkTypName(type.getName());
    wkTypDispName = (type.getSapType() + ": " + type.getShortDesc());
    



    Section sec = Section.retrieveSection(secId);
    setSectionCode(sec.getCode());
    Manager manager = Manager.getSectionHead(sec.getSectionId(), unitId);
    if (manager != null) {
      sectionHead = manager.getName();
    }
    else {
      sectionHead = "";
    }
    unitCode = emp.getUnitCode();
    this.wkNum = wkNum;
    contractor = Contractor.doRetrieveById(contractorId, unitId);
  }
  
public Workorder(ObjectIdLong workorderId2, String workorderNum, ObjectIdLong depId, ObjectIdLong secId) {
	this.workorderId=workorderId2;
	this.wkNum=workorderNum;
	this.depId=depId;
	this.secId=secId;
}

public ObjectIdLong getWorkorderId() {
    return workorderId;
  }
  
  public void setWorkorderId(ObjectIdLong workorderId) { this.workorderId = workorderId; }
  
  public ObjectIdLong getUnitId() {
    return unitId;
  }
  
  public void setUnitId(ObjectIdLong unitId) { this.unitId = unitId; }
  
  public ObjectIdLong getContractorId() {
    return contractorId;
  }
  
  public void setContractorId(ObjectIdLong contractorId) { this.contractorId = contractorId; }
  
  public String getName()
  {
    if ((this.name == null) || ("".equalsIgnoreCase(this.name))) {
      List<WorkorderLine> lines = new CMSService().retrieveWorkOrderLines(getWorkorderId());
      StringBuffer name = new StringBuffer();
      Iterator iterator2 = lines.iterator(); if (iterator2.hasNext()) {
        WorkorderLine wkLine = (WorkorderLine)iterator2.next();
        name.append(wkLine.getJobDesc().trim());
      }
      
      setName(name.toString());
    }
    return this.name;
  }
  
  public void setName(String name) { this.name = name; }
  
  public KDate getValidFrom() {
    return validFrom;
  }
  
  public void setValidFrom(KDate validFrom) { this.validFrom = validFrom; }
  
  public KDate getValidTo() {
    return validTo;
  }
  
  public void setValidTo(KDate validTo) { this.validTo = validTo; }
  
  public ObjectIdLong getTypeId() {
    return typeId;
  }
  
  public void setTypeId(ObjectIdLong typeId) { this.typeId = typeId; }
  
  public ObjectIdLong getDepId() {
    return depId;
  }
  
  public void setDepId(ObjectIdLong depId) { this.depId = depId; }
  
  public ObjectIdLong getSecId() {
    return secId;
  }
  
  public void setSecId(ObjectIdLong secId) { this.secId = secId; }
  
  public String getGlcode() {
    return glcode;
  }
  
  public void setGlcode(String glcode) { this.glcode = glcode; }
  
  public String getCostcenter() {
    return costcenter;
  }
  
  public void setCostcenter(String costcenter) { this.costcenter = costcenter; }
  
  public ObjectIdLong getOwnerId() {
    return ownerId;
  }
  
  public void setOwnerId(ObjectIdLong ownerId) { this.ownerId = ownerId; }
  
  public ObjectIdLong getSuprid() {
    return suprid;
  }
  
  public void setSuprid(ObjectIdLong suprid) { this.suprid = suprid; }
  
  public Integer getStatus() {
    return status;
  }
  
  public void setStatus(Integer status) { this.status = status; }
  
  public String getUnitName()
  {
    return unitName;
  }
  
  public void setUnitName(String unitName) { this.unitName = unitName; }
  
  public String getStatusName() {
    return statusName;
  }
  
  public void setStatusName(String statusName) { this.statusName = statusName; }
  
  public String getdName() {
    return dName;
  }
  
  public void setdName(String dName) { this.dName = dName; }
  
  public static List<Workorder> retrieveAll() {
    return new CMSService().retrieveAllWorkOrders();
  }
  
  public static List<Workorder> retrieveByDeptId(ObjectIdLong deptId) {
    return new CMSService().retrieveWorkOrdersByDeptId(deptId);
  }
  
  public static List<Workorder> retrieveWorkOrder(String unitId, String statusId, List lles, List sse) 
  {
	  return new CMSService().retrieveWorkOrders(unitId, statusId, lles, sse); 
  }
  
  public static HashMap getStatusList() {
    HashMap map = new HashMap();
    map.put(ALL, "All");
    map.put(ACTIVE, "Active");
    map.put(CLOSE, "Closed");
    return map;
  }
  


  public static Workorder retrieveByWorkOrder(String workorderId2) { return new CMSService().getWorkorderById(workorderId2); }
  
  public void doUpdate() {
    Workorder wk = retrieveByWorkOrder(getWorkorderId().toSQLString());
    wk.setOwnerId(getOwnerId());
    wk.setSuprid(getSuprid());
    wk.setDepId(getDepId());
    wk.setSecId(getSecId());
    wk.setValidTo(getValidTo());
    new CMSService().saveWorkorder(wk);
  }
  
  public String getWkTypName() { return wkTypName; }
  
  public void setWkTypName(String wkTypName) {
    this.wkTypName = wkTypName;
  }
  
  public String getSectionCode() { return sectionCode; }
  
  public void setSectionCode(String sectionCode) {
    this.sectionCode = sectionCode;
  }
  
  public static Workorder retrieveWorkOrderByNum(String wkNum, String statusId, String unitId, List lles, List sse) {
    return new CMSService().getWorkOrderByNum(wkNum, statusId, unitId, lles, sse);
  }
  
  public String getUnitCode() { return unitCode; }
  
  public void setUnitCode(String unitCode) {
    this.unitCode = unitCode;
  }
  
  public String getWkNum() { return wkNum; }
  
  public void setWkNum(String wkNum) {
    this.wkNum = wkNum;
  }
  
  public String getContractorName() { return contractor.getcontractorName(); }
  
  public String getContractorCode() {
    return contractor.getCcode();
  }
  
  public String getSectionHead() { return sectionHead; }
  
  public void setSectionHead(String sectionHead) {
    this.sectionHead = sectionHead;
  }
  
  public String getWkTypDispName() { return wkTypDispName; }
  
  public void setWkTypDispName(String wkTypDispName) {
    this.wkTypDispName = wkTypDispName;
  }
  
  public static Workorder retrieveWorkOrderByNum(String wkNum2) { return new CMSService().getWorkOrderByNum(wkNum2); }
  

  public static List<Workorder> retrieveWorkOrderBySecCode(String secCode, String statusId, String unitId, ArrayList lles, ArrayList sse)
  {
    return new CMSService().getWorkOrderBySecCode(secCode, statusId, unitId, lles, sse);
  }
  
  public Contractor getContractor() {
    return contractor;
  }
  
  public static List<Workorder> getWorkorderByUnitAndContId(ObjectIdLong unitId, ObjectIdLong contrId)
  {
    return new CMSService().retrieveWorkorderByUnitAndContId(unitId,contrId);
  }
  
}
