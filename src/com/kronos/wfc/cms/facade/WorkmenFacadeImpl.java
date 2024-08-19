package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;

public class WorkmenFacadeImpl implements WorkmenFacade
{
  public WorkmenFacadeImpl() {}
  
  public List<Contractor> getContractors(String unitId)
  {
    ObjectIdLong id = new ObjectIdLong(unitId);
    return Contractor.doRetrieveByUnitId(id);
  }
  
  public List<Workmen> getWorkmenList(String contId, Trade trade, Skill skill, String workorderTyp, ObjectIdLong unitId)
  {
    ObjectIdLong id = new ObjectIdLong(contId);
    List<Workmen> workmenList = Contractor.retrieveWorkmen(id, trade.getTradeId(), skill.getSkillId(), workorderTyp, unitId);
    return workmenList;
  }
  
  public Workmen getWorkmen(String workmenId, String unitId)
  {
    ObjectIdLong id = new ObjectIdLong(workmenId);
    ObjectIdLong unit = new ObjectIdLong(unitId);
    return Workmen.getWorkmenById(id, unit);
  }
  
  public void addWorkmen(Workmen workmen)
  {
    workmen.doAdd();
  }
  
  public void updateWorkmen(Workmen workmen)
  {
    workmen.doUpdate();
  }
  
  public void deleteWorkmen(String workmenId, String unitId)
  {
    ObjectIdLong id = new ObjectIdLong(workmenId);
    ObjectIdLong unit = new ObjectIdLong(unitId);
    Workmen workmen = Workmen.getWorkmenById(id, unit);
    workmen.doDelete();
  }
  

  public Workmen saveWorkmen(Workmen workmen, String unitId)
  {
    if (workmen.getEmpId() == null) {
      addWorkmen(workmen);
    }
    else {
    	
      Workmen workDb = Workmen.getWorkmenById(workmen.getEmpId(), new ObjectIdLong(unitId));
      workDb.updateFields(workmen);
      updateWorkmen(workDb);
    }
    return Workmen.getWorkmenById(workmen.getEmpId(), new ObjectIdLong(unitId));
  }
  

  public List<com.kronos.wfc.cms.business.CMSState> getAllStates()
  {
    return new CMSService().getStates();
  }
  
  public List<Skill> getSkills()
  {
    return new CMSService().getSkills();
  }
  
  public List<Workmen> getWorkmenList(String contId, ObjectIdLong unitId, String statusId)
  {
    ObjectIdLong id = new ObjectIdLong(contId);
    List<Workmen> workmenList = Contractor.retrieveWorkmen(id, unitId, statusId);
    return workmenList;
  }
  
  public List<Workmen> getWorkmenListByEmpCode(String contId, String empCode, String unitId, String statusId)
  {
    ObjectIdLong id = new ObjectIdLong(contId);
    List<Workmen> workmenList = Contractor.retrieveWorkmenByEmpCode(id, empCode, unitId, statusId);
    return workmenList;
  }
  
  public List<Workmen> getWorkmenListByEmpName(String contId, String empName, String unitId, String statusId)
  {
    ObjectIdLong id = new ObjectIdLong(contId);
    List<Workmen> workmenList = Contractor.retrieveWorkmenByEmpName(id, empName, unitId, statusId);
    return workmenList;
  }
  
  public List<Workmen> getWorkmenListByEmpCode(String empCode, String unitId)
  {
    List<Workmen> wks = Workmen.getWorkmenByEmployeeCodeAndUnit(empCode, unitId);
    return wks;
  }
  
  public List<Workmen> getWorkmenListByEmpName(String empName, String unitId, String statusId)
  {
    List<Workmen> wks = Workmen.getWorkmenByEmployeeNameAndUnit(empName, unitId, statusId);
    return wks;
  }
  
  public List<Workmen> getWorkmenList(ObjectIdLong unitId)
  {
    List<Workmen> wks = Workmen.getWorkmenByUnit(unitId);
    return wks;
  }
  
  public Workmen rehireWorkmen(Workmen workmen, String unitId)
  {
    rehireWorkman(workmen);
    return Workmen.getWorkmenById(workmen.getEmpId(), new ObjectIdLong(unitId));
  }
  
  private void rehireWorkman(Workmen workmen) {
    workmen.doRehire();
  }
}
