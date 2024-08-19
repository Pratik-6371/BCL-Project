package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.platform.businessobject.framework.KBusinessObject;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.ArrayList;
import java.util.List;
















public class MinimumWage
  implements KBusinessObject
{
  private ObjectIdLong minimumwageId;
  private String name;
  private KDate from;
  private KDate to;
  private ObjectIdLong tradeId;
  private ObjectIdLong skillId;
  private ObjectIdLong stateid;
  private Wage wage;
  
  public MinimumWage(ObjectIdLong minimumwageId2, String name2, KDate from, KDate to, ObjectIdLong tradeId2, ObjectIdLong skillId2, Wage wage, ObjectIdLong stateid2)
  {
    minimumwageId = minimumwageId2;
    name = name2;
    this.from = from;
    this.to = to;
    tradeId = tradeId2;
    skillId = skillId2;
    this.wage = wage;
    stateid = stateid2;
  }
  
  public ObjectIdLong getMinimumwageId()
  {
    return minimumwageId;
  }
  
  public void setMinimumwageId(ObjectIdLong minimumwageId) {
    this.minimumwageId = minimumwageId;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public KDate getFrom() {
    return from;
  }
  
  public void setFrom(KDate from) {
    this.from = from;
  }
  
  public KDate getTo() {
    return to;
  }
  
  public void setTo(KDate to) {
    this.to = to;
  }
  
  public ObjectIdLong getTradeId() {
    return tradeId;
  }
  
  public void setTradeId(ObjectIdLong tradeId) {
    this.tradeId = tradeId;
  }
  
  public ObjectIdLong getSkillId() {
    return skillId;
  }
  
  public void setSkillId(ObjectIdLong skillId) {
    this.skillId = skillId;
  }
  
  public Wage getWage() {
    return wage;
  }
  
  public void setWage(Wage wage) {
    this.wage = wage;
  }
  
  public ObjectIdLong getStateid() {
    return stateid;
  }
  
  public void setStateid(ObjectIdLong stateid) {
    this.stateid = stateid;
  }
  
  public static MinimumWage doRetrieveById(ObjectIdLong oid)
  {
    if (oid != null) {
      MinimumWageService service = new MinimumWageService();
      MinimumWage employee = service.retrieveById(oid);
      return employee;
    }
    return null;
  }
  
  public static MinimumWage doRetrieveByName(String name)
  {
    if (name != null) {
      MinimumWageService service = new MinimumWageService();
      MinimumWage employee = service.retrieveByName(name);
      return employee;
    }
    return null;
  }
  
  public static List<MinimumWage> doRetrieveAll()
  {
    MinimumWageService service = new MinimumWageService();
    List<MinimumWage> employees = service.retrieveAll();
    
    return employees;
  }
  

  public void doUpdate()
    throws Exception
  {
    validateAccessRight("Update");
    
    List employees = new ArrayList(1);
    
    MinimumWageService service = new MinimumWageService();
    
    if (minimumwageId != null)
    {
      MinimumWage Mwage = service.retrieveById(minimumwageId);
      

      if (Mwage != null)
      {
        Mwage.updateFields(this);
        employees.add(Mwage);
        service.updateMinimumWage(employees);
      }
    }
  }
  

  private void updateFields(MinimumWage minimumWage)
    throws Exception
  {
    setName(minimumWage.getName());
    if (getFrom().isAfter(minimumWage.getFrom())) {
      throw CMSException.fromDateBeforeExistingRecord(toString());
    }
    
    setFrom(minimumWage.getFrom());
    setTo(minimumWage.getTo());
    setWage(minimumWage.getWage());
  }
  




  private static boolean validateAccessRight(String actionName)
  {
    return true;
  }
  

  public static List<MinimumWage> doRetrieveMWByState(ObjectIdLong unitId, KDate date)
  {
    return new MinimumWageService().retrieveMinimumWagesByStateId(unitId, date);
  }
}
