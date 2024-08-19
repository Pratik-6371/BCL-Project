package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.PrincipalEmployeeFacade;
import com.kronos.wfc.commonapp.laborlevel.business.set.LaborAccountSetEntry;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignmentDetails;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.person.PersonStatus;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.personality.PersonalityException;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.commonapp.types.business.DeviceGroup;
import com.kronos.wfc.commonapp.types.business.EmploymentStatusType;
import com.kronos.wfc.datacollection.empphoto.api.APIEmpPhotoBean;
import com.kronos.wfc.datacollection.empphoto.business.EmpPhotoBusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.business.SQLStatementWithInClause;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.persistence.framework.statement.SqlDataSetter;
import com.kronos.wfc.platform.persistence.framework.statement.SqlKDate;
import com.kronos.wfc.platform.persistence.framework.statement.SqlString;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import com.kronos.wfc.platform.xml.api.bean.APIBeanList;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import com.kronos.wfc.platform.xml.api.bean.ParameterMap;
import com.kronos.wfc.wfp.logging.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CMSService
{
  public CMSService() {}
  
  protected List<Department> getDepartmentsByUnit(ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_DEPT_BY_UNITID", params);
    return createDepartmentObjects(ds);
  }
  
  private List<Department> createDepartmentObjects(DataStoreIfc ds) {
    List<Department> deps = new ArrayList();
    while (ds.nextRow()) {
      Department dep = createDepartmentObject(ds);
      deps.add(dep);
    }
    return deps;
  }
  
  private Department createDepartmentObject(DataStoreIfc ds) { ObjectIdLong id = ds.getObjectIdLong(1);
    String code = ds.getString(2);
    String desc = ds.getString(3);
    ObjectIdLong unitId = ds.getObjectIdLong(4);
    Department dep = new Department(id, code, desc, unitId);
    return dep;
  }
  
  private static DataStoreIfc getDataStoreFromSQLStatement(String sqlStatement, ArrayList<String> params) {
    SQLStatement select = new SQLStatement(3, sqlStatement);
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    return ds;
  }
  
  public Department getDepartmentByDepartmentId(ObjectIdLong deptId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(deptId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_DEPT_BY_DEPTID", params);
    return createDepartmentObject(ds);
  }
  
  protected List<Manager> getManagersByDept(ObjectIdLong deptId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(deptId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_MANAGERS_BY_DEPTID_1", params);
    return createManagers(ds);
  }
  
  protected List<Manager> createManagers(DataStoreIfc ds)
  {
    List<Manager> mgrs = new ArrayList();
    while (ds.nextRow()) {
      Manager mgr = createManagerObject(ds);
      mgrs.add(mgr);
    }
    return mgrs;
  }
  




  private Manager createManagerObject(DataStoreIfc ds)
  {
    KDate dot = null;
    ObjectIdLong id = ds.getObjectIdLong(1);
    try
    {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(10));
      if (p != null) {
        List<PersonStatus> status = p.getStatusData().getAllStatuses();
        for (PersonStatus ps : status) {
          EmploymentStatusType est = ps.getEmploymentStatusType();
          if (EmploymentStatusType.TERMINATED.equals(est)) {
            dot = ps.getEffectiveDate();
          }
        }
      }
    }
    catch (PersonalityException localPersonalityException) {}catch (Exception localException) {}
    

    String name = ds.getString(2);
    ObjectIdLong depId = ds.getObjectIdLong(3);
    ObjectIdLong secId = ds.getObjectIdLong(4);
    String username = ds.getString(5);
    String password = ds.getString(6);
    String emailaddr = ds.getString(7);
    String mobileno = ds.getString(8);
    Boolean isDepManager = ds.getBoolean(9);
    Manager manager = new Manager(id, name, username, password, mobileno, emailaddr, depId, secId, isDepManager, dot);
    return manager;
  }
  
  private void logError(Exception e) {
    Log.log(1, e, "Error in CMS Service");
  }
  




  public Manager getManagerById(ObjectIdLong id)
  {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_MANAGERS_BY_ID", params);
    if (ds.nextRow()) {
      return createManagerObject(ds);
    }
    return null;
  }
  
  public void addManager(Manager manager) {
    ManagerInsertTransaction trans = new ManagerInsertTransaction(manager);
    trans.run();
  }
  
  public void updateManager(Manager manager)
  {
    ArrayList<Object> params = new ArrayList();
    
    params.add(manager.getName());
    params.add(manager.getDeptId().toSQLString());
    if (manager.getSectionId().longValue() != -1L) {
      params.add(manager.getSectionId().toSQLString());
    } else
      params.add(null);
    params.add(manager.getUserName());
    params.add(manager.getPasswd());
    params.add(manager.getEmailAddr());
    params.add(manager.getMobilenum());
    params.add(Integer.valueOf(manager.getIsDeptMgr().booleanValue() ? 1 : 0));
    params.add(manager.getId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_MANAGER");
    update.execute(params);
  }
  
  public void deleteManager(Manager manager)
  {
    ArrayList<Object> params = new ArrayList();
    
    params.add(manager.getId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.DELETE_MANAGER");
    update.execute(params);
  }
  






  public Section getSectionById(ObjectIdLong sectionId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(sectionId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SECTION_BY_ID", params);
    return createSection(ds);
  }
  
  public List<Section> getSections(ObjectIdLong depId)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(depId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SECTION_BY_DEPID", params);
    
    return createSections(ds);
  }
  






  private List<Section> createSections(DataStoreIfc ds)
  {
    List<Section> secs = new ArrayList();
    while (ds.nextRow()) {
      Section sec = createSection(ds);
      secs.add(sec);
    }
    return secs;
  }
  




  private Section createSection(DataStoreIfc ds)
  {
    Section sec = new Section();
    sec.setSectionId(ds.getObjectIdLong(1));
    sec.setName(ds.getString(2));
    sec.setCode(ds.getString(3));
    sec.setDescription(ds.getString(4));
    sec.setDeptId(ds.getObjectIdLong(5));
    return sec;
  }
  
  public List<Skill> getSkills() {
    ArrayList<String> params = new ArrayList();
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL_SKILLS", params);
    
    ArrayList<Skill> skills = new ArrayList();
    while (ds.nextRow()) {
      Skill skill = new Skill(ds.getObjectIdLong(1), ds.getString(2));
      skills.add(skill);
    }
    
    return skills;
  }
  
  protected List<Trade> getTrades() { ArrayList<String> params = new ArrayList();
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL_TRADES", params);
    
    ArrayList<Trade> trades = new ArrayList();
    while (ds.nextRow()) {
      Trade trade = new Trade(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3));
      trades.add(trade);
    }
    
    return trades;
  }
  
  protected Trade getTradeById(ObjectIdLong id)
  {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_TRADES_BY_ID", params);
    
    Trade result = null;
    while (ds.nextRow()) {
      Trade trade = new Trade(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3));
      result = trade;
    }
    
    return result;
  }
  
  protected Skill getSkillById(ObjectIdLong id)
  {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SKILL_BY_ID", params);
    
    Skill result = null;
    while (ds.nextRow()) {
      Skill skill = new Skill(ds.getObjectIdLong(1), ds.getString(2));
      result = skill;
    }
    
    return result;
  }
  
  protected List<Wage> getWageById(ObjectIdLong id) {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WAGE_BY_ID", params);
    
    ArrayList<Wage> wages = new ArrayList();
    while (ds.nextRow()) {
      Wage wage = new Wage(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4));
      wages.add(wage);
    }
    
    return wages;
  }
  
  protected void doAdd(Wage wage) {
    ObjectIdLong id = ObjectIdManager.create("CMSWAGE");
    ArrayList<Object> params = new ArrayList();
    params.add(id.toSQLString());
    params.add(wage.getBasic());
    params.add(wage.getDa());
    params.add(wage.getAllowance());
    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_CMSWAGE");
    insert.execute(params);
    wage.setId(id);
  }
  
  protected void doUpdate(Wage wage) {
    ArrayList<Object> params = new ArrayList();
    
    params.add(wage.getBasic());
    params.add(wage.getDa());
    params.add(wage.getAllowance());
    params.add(wage.getId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_CMSWAGE");
    update.execute(params);
  }
  
  protected void addAddress(Address address)
  {
    ObjectIdLong id = ObjectIdLong.createObjectIdLong("CMSADDRESS");
    ArrayList<Object> params = new ArrayList();
    params.add(id.toSQLString());
    params.add("not registered");
    params.add(address.getDistrict());
    params.add(address.getTaluka());
    params.add(address.getVillage());
    params.add(address.getStateId());
    address.setId(id);
    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_CMSADDRESS");
    insert.execute(params);
  }
  

  public List<CMSState> getStates()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_STATES", params);
    
    List<CMSState> states = new ArrayList();
    while (ds.nextRow()) {
      CMSState state = new CMSState(ds.getObjectIdLong(1), ds.getString(2));
      if (!CMSState.DEFAULT_STATE_ID.equals(state.getStateId())) {
        states.add(state);
      }
    }
    
    return states;
  }
  
  protected void addWorkmenDetail(WorkmenDetail workmenDetail) {
    ObjectIdLong id = ObjectIdLong.createObjectIdLong("CMSPERSONDETL");
    ArrayList<Object> params = new ArrayList();
    params.add(id.toSQLString());
    params.add(workmenDetail.getDoj());
    params.add(workmenDetail.getDot());
    params.add(workmenDetail.getPfAcctNo());
    params.add(workmenDetail.getAadharNo());
    params.add(workmenDetail.getGender());
    params.add(workmenDetail.getPanno());
    params.add(Integer.valueOf(workmenDetail.getHazard().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getmStatus().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getWifeNm());
    params.add(workmenDetail.getNoOfChildren());
    params.add(workmenDetail.getTechnical());
    params.add(workmenDetail.getAcademic());
    params.add(workmenDetail.getProf1());
    params.add(workmenDetail.getProf2());
    params.add(workmenDetail.getProf3());
    params.add(workmenDetail.getProf4());
    params.add(workmenDetail.getPreviousEmployer());
    params.add(workmenDetail.getpContr());
    params.add(workmenDetail.getReferredBy());
    params.add(workmenDetail.getShoesize());
    params.add(workmenDetail.getBloodgrp());
    params.add(workmenDetail.getAmcCheck());
    params.add(Integer.valueOf(workmenDetail.getWinCoRelative().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getWinCoName());
    params.add(workmenDetail.getWincoAddress());
    params.add(workmenDetail.getMobileNo());
    params.add(Integer.valueOf(workmenDetail.getLandLoser().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getSurveyNo());
    params.add(workmenDetail.getRelnWithSeller());
    params.add(workmenDetail.getVillage());
    params.add(workmenDetail.getNameofSeller());
    params.add(workmenDetail.getExtent());
    params.add(Integer.valueOf(workmenDetail.getAccOpenWithContractor().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getBankBranch());
    params.add(workmenDetail.getAcctNo());
    params.add(Integer.valueOf(workmenDetail.getMedicalTrainingSw().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getSafetyTrainingSw().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getSkillCert().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getPrevExp());
    params.add(workmenDetail.getPrevOrg());
    workmenDetail.setPersonDetlId(id);
    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_PERSONDETL");
    insert.execute(params);
  }
  
  public void updateWorkmenDetail(WorkmenDetail workmenDetail) {
    ArrayList<Object> params = new ArrayList();
    params.add(workmenDetail.getDoj());
    params.add(workmenDetail.getDot());
    params.add(workmenDetail.getPfAcctNo());
    params.add(workmenDetail.getAadharNo());
    params.add(workmenDetail.getGender());
    params.add(workmenDetail.getPanno());
    params.add(Integer.valueOf(workmenDetail.getHazard().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getmStatus().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getWifeNm());
    params.add(workmenDetail.getNoOfChildren());
    params.add(workmenDetail.getTechnical());
    params.add(workmenDetail.getAcademic());
    params.add(workmenDetail.getProf1());
    params.add(workmenDetail.getProf2());
    params.add(workmenDetail.getProf3());
    params.add(workmenDetail.getProf4());
    params.add(workmenDetail.getPreviousEmployer());
    params.add(workmenDetail.getpContr());
    params.add(workmenDetail.getReferredBy());
    params.add(workmenDetail.getShoesize());
    params.add(workmenDetail.getBloodgrp());
    params.add(workmenDetail.getAmcCheck());
    params.add(Integer.valueOf(workmenDetail.getWinCoRelative().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getWinCoName());
    params.add(workmenDetail.getWincoAddress());
    params.add(workmenDetail.getMobileNo());
    params.add(Integer.valueOf(workmenDetail.getLandLoser().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getSurveyNo());
    params.add(workmenDetail.getRelnWithSeller());
    params.add(workmenDetail.getVillage());
    params.add(workmenDetail.getNameofSeller());
    params.add(workmenDetail.getExtent());
    params.add(Integer.valueOf(workmenDetail.getAccOpenWithContractor().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getBankBranch());
    params.add(workmenDetail.getAcctNo());
    params.add(Integer.valueOf(workmenDetail.getMedicalTrainingSw().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getSafetyTrainingSw().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(workmenDetail.getSkillCert().booleanValue() ? 1 : 0));
    params.add(workmenDetail.getPrevExp());
    params.add(workmenDetail.getPrevOrg());
    params.add(workmenDetail.getPersonDetlId().toSQLString());
    SQLStatement insert1 = new SQLStatement(4, "business.cms.UPDATE_PERSONDETL");
    insert1.execute(params);
  }
  
  public void addWorkmen(Workmen workmen) {
    CMSPersonInsertTransaction trans = new CMSPersonInsertTransaction(workmen);
    trans.run();
  }
  
  public void updateWorkmen(Workmen workmen) {
    CMSPersonUpdateTransaction trans = new CMSPersonUpdateTransaction(workmen);
    trans.run();
  }
  
  public void updateAddress(Address address) {
    ArrayList<Object> params = new ArrayList();
    params.add("future use");
    params.add(address.getDistrict());
    params.add(address.getTaluka());
    params.add(address.getVillage());
    params.add(address.getStateId());
    params.add(address.getId().toSQLString());
    SQLStatement insert = new SQLStatement(4, "business.cms.UPDATE_CMSADDRESS");
    insert.execute(params);
  }
  
  public List<Workmen> getWorkmenByContractor(ObjectIdLong id, ObjectIdLong tradeId, ObjectIdLong skillId, String workorderTyp, ObjectIdLong unitId) {
    ArrayList params = new ArrayList();
    params.add(id.toSQLString());
    params.add(tradeId.toSQLString());

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_CONTID_BY_TRADE_BY_SKILL", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), unitId), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  
  public Wage getWage(ObjectIdLong id) {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WAGE_BY_ID", params);
    
    Wage result = null;
    while (ds.nextRow()) {
      Wage wage = new Wage(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4));
      result = wage;
    }
    
    return result;
  }
  
  public Address getAddressById(ObjectIdLong id) { ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ADDRESS_BY_ID", params);
    
    Address result = null;
    while (ds.nextRow()) {
      Address address = new Address(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getString(5), ds.getObjectIdLong(6));
      result = address;
    }
    
    return result;
  }
  
  public WorkmenDetail getWorkmenDetailById(ObjectIdLong id) { ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_PERSONDETL_BY_ID", params);
    
    WorkmenDetail result = null;
    while (ds.nextRow())
    {
      WorkmenDetail detail = new WorkmenDetail(ds.getObjectIdLong(1), ds.getKDate(2), ds.getKDate(3), 
        ds.getString(4), ds.getString(5), ds.getString(6), ds.getString(7), ds.getBoolean(8), 
        ds.getBoolean(9), ds.getString(10), ds.getInt(11), ds.getString(12), ds.getString(13), 
        ds.getString(14), ds.getString(15), ds.getString(16), ds.getString(17), ds.getString(18), 
        ds.getObjectIdLong(19), ds.getString(20), ds.getInt(21), ds.getString(22), ds.getKDate(23), 
        ds.getBoolean(24), ds.getString(25), ds.getString(26), ds.getString(27), ds.getBoolean(28), 
        ds.getString(29), ds.getString(30), ds.getString(31), ds.getString(32), ds.getString(33), 
        ds.getBoolean(34), ds.getString(35), ds.getString(36), ds.getBoolean(37), ds.getBoolean(38), 
        ds.getBoolean(39), ds.getString(40), ds.getString(41));
      result = detail;
    }
    
    return result;
  }
  
  public Workmen getWorkmenById(ObjectIdLong id, ObjectIdLong unitId) {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_ID", params);
    
    Workmen ws = null;
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), ds.getObjectIdLong(18)), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws = workmen;
    }
    
    return ws;
  }
  
  public void deleteWorkmen(Workmen workmen)
  {
    CMSPersonDeleteTransaction trns = new CMSPersonDeleteTransaction(workmen);
    trns.run();
  }
  
  public void deleteAddress(Address address) {
    ArrayList<Object> params = new ArrayList();
    
    params.add(address.getId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.DELETE_ADDRESS");
    update.execute(params);
  }
  
  public void deleteWorkmenDetail(WorkmenDetail workmenDetail)
  {
    ArrayList<Object> params = new ArrayList();
    
    params.add(workmenDetail.getPersonDetlId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.DELETE_PERSONDETL");
    update.execute(params);
  }
  


  public void deleteWage(Wage wage)
  {
    ArrayList<Object> params = new ArrayList();
    
    params.add(wage.getId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.DELETE_WAGE");
    update.execute(params);
  }
  
  protected List<Workorder> retrieveAllWorkOrders()
  {
    ArrayList<String> params = new ArrayList();
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL_WORKORDERS", params);
    
    ArrayList<Workorder> wks = new ArrayList();
    while (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), ds.getString(10), 
        ds.getString(11), ds.getObjectIdLong(12), ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      wks.add(wk);
    }
    
    return wks;
  }
  

  protected List<Workorder> retrieveWorkOrdersByDeptId(ObjectIdLong depId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(depId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDERS_BY_DEPTID", params);
    
    ArrayList<Workorder> wks = new ArrayList();
    while (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), 
        ds.getString(4), ds.getKDate(5), ds.getKDate(6), ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      wks.add(wk);
    }
    
    return wks;
  }
  

  protected List<Workorder> retrieveWorkOrders(String unitId, String statusId, List lles, List sse)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    if (Workorder.ALL.toString().equalsIgnoreCase(statusId)) {
      params.add(Workorder.UNASSIGNED.toString());
      params.add(Workorder.ACTIVE.toString());
      params.add(Workorder.CLOSE.toString());
    }
    else {
      params.add(statusId.toString());
      params.add(statusId.toString());
      params.add(statusId.toString());
    }
    
    params.add(unitId);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_WORKORDERS", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    ArrayList<Workorder> wks = new ArrayList();
    while (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), 
        ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      wks.add(wk);
    }
    
    return wks;
  }
  
  protected Workorder getWorkorderById(String workorderId2) { ArrayList<String> params = new ArrayList();
    params.add(workorderId2);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_BY_ID", params);
    
    if (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), 
        ds.getKDate(6), ds.getObjectIdLong(7), ds.getObjectIdLong(8), 
        ds.getObjectIdLong(9), ds.getString(10), ds.getString(11), 
        ds.getObjectIdLong(12), ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      return wk;
    }
    
    return null;
  }
  
  protected void saveWorkorder(Workorder wk) {
    String editable = com.kronos.wfc.platform.properties.framework.KronosProperties.get("cms.workorder.editable", "false");
    if (editable.equalsIgnoreCase("true")) {
      ArrayList<Object> params = new ArrayList();
      
      params.add(wk.getDepId().toSQLString());
      params.add(wk.getSecId().toSQLString());
      params.add(wk.getValidTo());
      params.add(wk.getWorkorderId().toSQLString());
      
      SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_WORKORDER");
      update.execute(params);
    }
  }
  
  protected List<WorkorderType> getWorkorderTypes() {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_TYPES", params);
    
    List<WorkorderType> types = new ArrayList();
    while (ds.nextRow()) {
      WorkorderType type = new WorkorderType(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getString(3), ds.getString(4), ds.getString(5));
      types.add(type);
    }
    return types;
  }
  
  protected Section retrieveSection(ObjectIdLong secId) { ArrayList<String> params = new ArrayList();
    params.add(secId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SECTION_ID", params);
    
    if (ds.nextRow()) {
      Section section = new Section(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getObjectIdLong(5));
      return section;
    }
    
    return null;
  }
  

  public List<WorkorderActivity> retrieveWOActivitiesByWOId(ObjectIdLong workorderId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(workorderId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WOA_BY_WOID", params);
    ArrayList<WorkorderActivity> acts = new ArrayList();
    while (ds.nextRow()) {
      WorkorderActivity act = new WorkorderActivity(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3));
      acts.add(act);
    }
    
    return acts;
  }
  
  public List<Supervisor> getSupervisorsByContractorId(ObjectIdLong contractorId2)
  {
    ArrayList<String> params = new ArrayList();
    params.add(contractorId2.toSQLString());
    

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SUPR_BY_COID", params);
    ArrayList<Supervisor> acts = new ArrayList();
    while (ds.nextRow()) {
      Supervisor act = new Supervisor(ds.getObjectIdLong(1), ds.getString(2), contractorId2, 
        ds.getString(3), ds.getString(4), 
        ds.getString(5), ds.getString(6));
      acts.add(act);
    }
    
    return acts;
  }
  
  protected List<WorkorderLine> retrieveWorkOrderLines(ObjectIdLong wkId) {
    ArrayList<String> params = new ArrayList();
    params.add(wkId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WKLINE_BY_WKID", params);
    ArrayList<WorkorderLine> wkls = new ArrayList();
    while (ds.nextRow()) {
      WorkorderLine wkl = new WorkorderLine(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), 
        ds.getInt(4), ds.getDouble(5), ds.getObjectIdLong(6), ds.getString(7), ds.getString(8), 
        ds.getBoolean(9), ds.getInt(10), ds.getBoolean(11), ds.getString(12), ds.getString(13), 
        ds.getString(14),ds.getString(15),ds.getString(16));
      wkls.add(wkl);
    }
    return wkls;
  }
  








  public List<LaborRequisition> retrieveLRsByWoId(ObjectIdLong wId, KDate searchFDate, KDate searchTDate, ObjectIdLong woId)
  {
    ArrayList params = new ArrayList();
    params.add(wId.toSQLString());
    params.add(searchFDate);
    params.add(searchTDate);
    params.add(woId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRLINE_BY_WOID", params);
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkls.add(wkl);
    }
    
    ds.close();
    return wkls;
  }
  


  private List<LRShift> getLRshifts(ObjectIdLong lrId)
  {
    ArrayList<LRShift> lrshifts = new ArrayList();
    ArrayList params = new ArrayList();
    params.add(lrId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRSHIFT_BY_LRID", params);
    ArrayList<LaborRequisition> wkls = new ArrayList();
    
    while (ds.nextRow()) {
      LRShift lrshift = new LRShift(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getInt(3));
      lrshifts.add(lrshift);
    }
    return lrshifts;
  }
  








  public List<LaborRequisition> retrieveLRsByWorkOrder(String workorderId, KDate fromdt, KDate todt, List sse)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(workorderId);
    params.add(fromdt);
    params.add(todt);
    
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRLINE_BY_WID", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkls.add(wkl);
    }
    return wkls;
  }
  
  public WorkorderLine retrieveWorkOrderLineById(ObjectIdLong wklId) { ArrayList<String> params = new ArrayList();
    params.add(wklId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WKLINE_BY_WLID", params);
    
    if (ds.nextRow()) {
      WorkorderLine wkl = new WorkorderLine(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), 
        ds.getInt(4), ds.getDouble(5), ds.getObjectIdLong(6), ds.getString(7), ds.getString(8), 
        ds.getBoolean(9), ds.getInt(10), ds.getBoolean(11), ds.getString(12), ds.getString(13), 
        ds.getString(14));
      return wkl;
    }
    ds.close();
    return null;
  }
  




  public LaborRequisition retrieveLRById(ObjectIdLong id)
  {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRLINE_BY_ID", params);
    
    if (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      return wkl;
    }
    ds.close();
    return null;
  }
  
  public List<CMSShift> retrieveShiftsByUnit(ObjectIdLong unitId) { ArrayList<String> params = new ArrayList();
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFTS_BY_ID", params);
    
    ArrayList<CMSShift> shifts = new ArrayList();
    while (ds.nextRow()) {
      CMSShift shift = new CMSShift(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getObjectIdLong(6));
      shifts.add(shift);
    }
    return shifts;
  }
  
  public CMSShift retrieveShift(ObjectIdLong id) { ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_BY_ID", params);
    
    if (ds.nextRow()) {
      CMSShift wkl = new CMSShift(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getString(5), ds.getObjectIdLong(6));
      return wkl;
    }
    return null;
  }
  
  public List<LaborReqSchedule> retrieveSchedulesByLRID(ObjectIdLong id) { ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULES_BY_ID", params);
    
    ArrayList<LaborReqSchedule> lrs = new ArrayList();
    while (ds.nextRow()) {
      LaborReqSchedule ls = new LaborReqSchedule(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), ds.getObjectIdLong(4), 
        ds.getObjectIdLong(5), ds.getKDate(6), ds.getKDate(7), ds.getObjectIdLong(8), ds.getInt(9), 
        ds.getKDateTime(10));
      lrs.add(ls);
    }
    return lrs;
  }
  
  public Workorder retrieveWorkorderByLRId(ObjectIdLong id) {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_BY_LRID", params);
    
    if (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), 
        ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      return wk;
    }
    
    return null;
  }
  
  protected LaborRequisition saveLR(LaborRequisition laborRequisition, String unitCode)
  {
    CMSLRTransaction trans = new CMSLRTransaction(laborRequisition, unitCode);
    trans.run();
    return trans.getLaborRequisition();
  }
  


























  public void saveSchedules(ArrayList wkList, String lrId, String tradeId, String skillId, String fromDtm, String toDtm, String shiftPattern, Integer deleteSw)
  {
    for (Iterator iterator = wkList.iterator(); iterator.hasNext();) {
      Workmen w = (Workmen)iterator.next();
      
      saveSchedule(w.getEmpId(), lrId, tradeId, skillId, fromDtm, toDtm, shiftPattern, deleteSw);
    }
  }
  




  private void saveSchedule(ObjectIdLong id, String lrId, String tradeId, String skillId, String fromDtm, String toDtm, String shiftPattern, Integer deletedSw)
  {
    ObjectIdLong scheduleId = ObjectIdManager.create("CMSSCHEDULE");
    
    ArrayList<Object> params = new ArrayList();
    params.add(scheduleId.toSQLString());
    params.add(id.toSQLString());
    params.add(tradeId);
    params.add(skillId);
    ObjectIdLong patternId = CMSShift.retrievePatternIdByPatternName(shiftPattern);
    
    params.add(patternId.toSQLString());
    params.add(KServer.stringToDate(fromDtm));
    params.add(KServer.stringToDate(toDtm));
    params.add(lrId);
    params.add(CurrentUserAccountManager.getUserAccountId().toSQLString());
    params.add(deletedSw);
    params.add(KDateTime.today());
    


    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_SCHEDULE");
    insert.execute(params);
  }
  



  public List<LaborReqSchedule> retrieveSchedulesByLRIDAndTF(String lrId2, KDate fromdtm2, KDate todtm2)
  {
    ArrayList params = new ArrayList();
    params.add(lrId2);
    params.add(todtm2);
    params.add(fromdtm2);
    

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULES_BY_ID_AND_TF", params);
    
    ArrayList<LaborReqSchedule> lrs = new ArrayList();
    while (ds.nextRow()) {
      LaborReqSchedule ls = new LaborReqSchedule(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), ds.getObjectIdLong(4), 
        ds.getObjectIdLong(5), ds.getKDate(6), ds.getKDate(7), ds.getObjectIdLong(8), ds.getInt(9), 
        ds.getKDateTime(10));
      lrs.add(ls);
    }
    return lrs;
  }
  
  public List<String> retrievePatternsByUnitId(ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitId.toSQLString());
    

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_PATTERN_BY_UNITID", params);
    
    ArrayList<String> lrs = new ArrayList();
    while (ds.nextRow()) {
      String patternName = ds.getString(1);
      lrs.add(patternName);
    }
    return lrs;
  }
  
  public Workorder getWorkOrderByNum(String wkNum) {
    ArrayList<String> params = new ArrayList();
    params.add(wkNum);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_BY_NUM", params);
    
    Workorder wk = null;
    if (ds.nextRow()) {
      wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), ds.getObjectIdLong(3), 
        ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), 
        ds.getObjectIdLong(9), ds.getString(10), ds.getString(11), 
        ds.getObjectIdLong(12), ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
    }
    
    return wk;
  }
  
  public LaborRequisition approveLR(LaborRequisition laborRequisition) { ArrayList<Object> params = new ArrayList();
    
    params.add(laborRequisition.getLrId());
    

    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_LR_APPROVAL");
    update.execute(params);
    
    return laborRequisition;
  }
  
  public HashMap getApprovalsByIds(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt)
  {
    HashMap map = new HashMap();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(empIds);
    ArrayList params = new ArrayList();
    params.add(toDt);
    params.add(fromdt);
    params.add(fromdt);
    params.add(toDt);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.manager.approval", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    while (ds.nextRow()) {
      map.put(ds.getObjectIdLong(1), new ManagerApproval(ds.getObjectIdLong(1), ds.getInt(2), ds.getString(3)));
    }
    
    return map;
  }
  
  public HashMap getExceptionsByIds(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt) {
    HashMap map = new HashMap();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(empIds);
    ArrayList params = new ArrayList();
    params.add(fromdt);
    params.add(toDt);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.exceptions", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    while (ds.nextRow()) {
      map.put(ds.getObjectIdLong(1), new Boolean(true));
    }
    
    return map;
  }
  
  public HashMap getUnApprovedOvertimeByIds(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt) {
    HashMap map = new HashMap();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(empIds);
    ArrayList params = new ArrayList();
    params.add(fromdt);
    params.add(toDt);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.unapproved", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    while (ds.nextRow()) {
      map.put(ds.getObjectIdLong(1), new Boolean(true));
    }
    
    return map;
  }
  
  public HashMap getRegularHoursByIds(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt)
  {
    HashMap map = new HashMap();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(empIds);
    ArrayList params = new ArrayList();
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.regular.hours", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    while (ds.nextRow()) {
      map.put(ds.getObjectIdLong(1), ds.getInt(2));
    }
    
    return map;
  }
  
  public HashMap getUnApprovedOT(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt) {
    HashMap map = new HashMap();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(empIds);
    ArrayList params = new ArrayList();
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.unapproved.ot.hours", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    while (ds.nextRow()) {
      map.put(ds.getObjectIdLong(1), ds.getInt(2));
    }
    
    return map;
  }
  
  public CMSShift retrieveShiftByPatternName(String shiftPattern) {
    ArrayList<String> params = new ArrayList();
    params.add(shiftPattern);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_BY_PATTERN_NAME", params);
    
    if (ds.nextRow()) {
      CMSShift wkl = new CMSShift(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getString(5), ds.getObjectIdLong(6));
      return wkl;
    }
    return null;
  }
  
  protected void updateLRShift(LRShift shift) {
    ArrayList params = new ArrayList();
    
    params.add(shift.getQty());
    params.add(shift.getLrId().toSQLString());
    params.add(shift.getShiftId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_LRSHIFT");
    update.execute(params);
  }
  

  protected void insertLRShift(LRShift shift)
  {
    ArrayList params = new ArrayList();
    params.add(shift.getLrId().toSQLString());
    params.add(shift.getShiftId().toSQLString());
    params.add(shift.getQty());
    

    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_LRSHIFT");
    insert.execute(params);
  }
  

  public List<Workmen> getWorkmenByContractor(ObjectIdLong id, ObjectIdLong unitId, String statusId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(id.toSQLString());
    params.add(statusId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_CONTID", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), unitId), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  
  public List<LaborRequisition> retrieveLRsByWorkOrderAndContCodes(String workorderId, KDate fromdt, KDate todt, ArrayList cCodes)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(cCodes);
    ArrayList params = new ArrayList();
    params.add(workorderId);
    params.add(fromdt);
    params.add(todt);
    
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRLINE_BY_WID_CCODE", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkls.add(wkl);
    }
    return wkls;
  }
  

  public List<LaborRequisition> retrieveActiveLRsByContractor(ObjectIdLong unitId, ArrayList cCodes)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(cCodes);
    ArrayList params = new ArrayList();
    params.add(Integer.valueOf(1));
    params.add(unitId.toSQLString());
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRLINE_CCODE", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(7), 
        ds.getObjectIdLong(8), ds.getObjectIdLong(9));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkl.setWorkOrderNum(ds.getString(10));
      wkls.add(wkl);
    }
    
    return wkls;
  }
  

  public List<LaborRequisition> retrieveLRsBySections(ObjectIdLong unitId, ArrayList sse)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(unitId.toSQLString());
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRLINE_BY_SECTIONS", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkl.setWorkOrderNum(ds.getString(9));
      wkls.add(wkl);
    }
    
    return wkls;
  }
  


  public Manager getSectionHead(ObjectIdLong sectionId, ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(sectionId.toSQLString());
    params.add(unitId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_MANAGERS_BY_SECID", params);
    if (ds.nextRow()) {
      return createManagerObject(ds);
    }
    return null;
  }
  


  protected Workorder getWorkOrderByNum(String wkNum, String statusId, String unitId, List lles, List sse)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(wkNum);
    if (Workorder.ALL.toString().equalsIgnoreCase(statusId)) {
      params.add(Workorder.UNASSIGNED.toString());
      params.add(Workorder.ACTIVE.toString());
      params.add(Workorder.CLOSE.toString());
    }
    else {
      params.add(statusId.toString());
      params.add(statusId.toString());
      params.add(statusId.toString());
    }
    

    params.add(unitId);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_WORKORDERS_BY_WKNUM_AC", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    

    if (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), 
        ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      return wk;
    }
    
    return null;
  }
  

  public List<Workorder> getWorkOrderBySecCode(String secCode, String statusId, String unitId, ArrayList lles, ArrayList sse)
  {
    ArrayList filtered = new ArrayList();
    
    for (Iterator iterator = sse.iterator(); iterator.hasNext();) {
      String code = (String)iterator.next();
      if (code.equalsIgnoreCase(secCode)) {
        filtered.add(secCode);
        break;
      }
    }
    
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    if (filtered.isEmpty()) {
      sqlSubstitutionArrayList.add(sse);
    }
    else {
      sqlSubstitutionArrayList.add(filtered);
    }
    
    ArrayList params = new ArrayList();
    if (Workorder.ALL.toString().equalsIgnoreCase(statusId)) {
      params.add(Workorder.UNASSIGNED.toString());
      params.add(Workorder.ACTIVE.toString());
      params.add(Workorder.CLOSE.toString());
    }
    else {
      params.add(statusId.toString());
      params.add(statusId.toString());
      params.add(statusId.toString());
    }
    

    params.add(unitId);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_WORKORDERS_BY_SEC_AC", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    
    List<Workorder> wks = new ArrayList();
    while (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), 
        ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      
      wks.add(wk);
    }
    
    return wks;
  }
  
  protected Supervisor getSupervisorById(ObjectIdLong suprId) {
    ArrayList<String> params = new ArrayList();
    params.add(suprId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SUPR_BY_ID", params);
    
    if (ds.nextRow()) {
      Supervisor act = new Supervisor(ds.getObjectIdLong(1), ds.getString(2), ds.getObjectIdLong(3), 
        ds.getString(5), ds.getString(6), 
        ds.getString(7), ds.getString(8));
      return act;
    }
    
    return null;
  }
  
  public LaborRequisitionPage approveLRPage(LaborRequisitionPage laborRequisitionPage) {
    ArrayList<Object> params = new ArrayList();
    params.add(laborRequisitionPage.getApprovalSw());
    params.add(laborRequisitionPage.getPageId());
    

    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_LR_PAGE_APPROVAL");
    update.execute(params);
    
    return laborRequisitionPage;
  }
  

  public LaborRequisitionPage retrieveLRPageById(String lrPageId)
  {
    ArrayList params = new ArrayList();
    params.add(lrPageId);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRPAGE_BY_ID", params);
    
    List<LaborRequisition> lrs = new ArrayList();
    LaborRequisitionPage lrPage = null;
    if (ds.nextRow())
    {






      lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), lrs, ds.getString(8), ds.getKDateTime(9), 
        ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
    }
    


    ds.close();
    
    DataStoreIfc ds1 = getDataStoreFromSQLStatement("business.cms.SELECT_LRS_BY_PAGEID", params);
    
    while (ds1.nextRow())
    {
      LaborRequisition req = retrieveLRById(ds1.getObjectIdLong(2));
      lrs.add(req);
    }
    ds1.close();
    lrPage.setLrs(lrs);
    return lrPage;
  }
  

  public Workorder retrieveWorkorderByPageId(String lrPageId)
  {
    ArrayList params = new ArrayList();
    params.add(lrPageId);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_BY_PAGEID", params);
    
    if (ds.nextRow()) {
      Workorder wk = new Workorder(ds.getObjectIdLong(1), ds.getObjectIdLong(2), 
        ds.getObjectIdLong(3), ds.getString(4), ds.getKDate(5), ds.getKDate(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8), ds.getObjectIdLong(9), 
        ds.getString(10), ds.getString(11), ds.getObjectIdLong(12), 
        ds.getObjectIdLong(13), ds.getInt(14), ds.getString(15));
      return wk;
    }
    
    return null;
  }
  
  public LaborRequisitionPage saveLRPage(LaborRequisitionPage laborRequisitionPage, String unitCode)
  {
    CMSLRPageTransaction trans = new CMSLRPageTransaction(laborRequisitionPage, unitCode);
    trans.run();
    return trans.getLaborRequisitionPage();
  }
  




  public List<LaborRequisitionPage> retrieveLRPagesByWIDAndContCode(String workorderId, Integer approvalSw, KDate fromDtm, KDate toDtm)
  {
    ArrayList params = new ArrayList();
    params.add(workorderId);
    params.add(fromDtm);
    params.add(toDtm);
    params.add(approvalSw);
    SQLStatement stmtWithInCluase = new SQLStatement(3, "business.cms.SELECT_LRPAGE_BY_WID_CCODE");
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), 
        ds.getString(8), ds.getKDateTime(9), ds.getKDateTime(10), 
        ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    

    return wkls;
  }
  




  public List<LaborRequisitionPage> retrieveLRPagesByContractor(ObjectId unitId, int approved)
  {
    ArrayList params = new ArrayList();
    params.add(Integer.valueOf(approved));
    params.add(unitId);
    

    SQLStatement stmtWithInCluase = new SQLStatement(3, "business.cms.SELECT_LRPAGE_BY_UNITID_CCODE");
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      
      wkls.add(lrPage);
    }
    

    return wkls;
  }
  

  public List<LaborRequisitionPage> retrieveLRPagesBySections(ObjectIdLong unitId, ArrayList sse, String statusId)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(unitId.toSQLString());
    params.add(statusId);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_SECTIONS", sqlSubstitutionArrayList.toArray());
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), ds.getKDateTime(9), 
        ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    return wkls;
  }
  
  public List<LaborRequisitionPage> retrieveLRPagesByWID(String workorderId)
  {
    ArrayList params = new ArrayList();
    params.add(workorderId);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRPAGES_BY_WID", params);
    

    List<LaborRequisitionPage> lrPages = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      
      lrPages.add(lrPage);
    }
    

    return lrPages;
  }
  

  public List<LaborRequisitionPage> retrieveLRPagesByWIDAndSections(String wId, KDate fromDtm, KDate toDtm, ArrayList sse, String statusId, String secCode)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(wId);
    params.add(fromDtm);
    params.add(toDtm);
    params.add(statusId);
    SQLStatementWithInClause stmtWithInCluase = null;
    if ((secCode == null) || ("".equalsIgnoreCase(secCode)))
    {
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_SECTIONS", sqlSubstitutionArrayList.toArray());
    }
    else {
      params.add(secCode);
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_SECTIONS_1", sqlSubstitutionArrayList.toArray());
    }
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    return wkls;
  }
  
  public ObjectIdLong retrievePatternIdByPatternName(String shiftPattern) {
    ArrayList<String> params = new ArrayList();
    params.add(shiftPattern);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_PATTERN_BY_NAME", params);
    ObjectIdLong patternId = null;
    if (ds.nextRow()) {
      patternId = ds.getObjectIdLong(1);
    }
    
    return patternId;
  }
  
  public String retrievePatternNameById(ObjectIdLong patternId) {
    ArrayList<String> params = new ArrayList();
    params.add(patternId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_PATTERN_BY_ID", params);
    String patternName = null;
    if (ds.nextRow()) {
      patternName = ds.getString(1);
    }
    
    return patternName;
  }
  
  public Long getActualAttendanceForWorkOrder(Workorder wk, ObjectIdLong tradeId, ObjectIdLong skillId)
  {
    ArrayList params = new ArrayList();
    params.add(wk.getUnitId().toSQLString());
    params.add(wk.getWkNum());
    params.add(KDate.today());
    params.add(tradeId.toSQLString());
    params.add(skillId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ACTUAL_ATTENDANCE_BY_WORKORDER", params);
    
    Long total = Long.valueOf(0L);
    if (ds.nextRow())
    {
      total = Long.valueOf(ds.getLong(5).longValue() / 28800L);
    }
    return total;
  }
  
  public Long getFutureLRBalance(ObjectIdLong wLineId, Integer weeklyOffDays)
  {
    KDate today = KDate.today();
    ArrayList params = new ArrayList();
    params.add(wLineId.toSQLString());
    params.add(today);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_FUTURE_BALANCE_BY_WlID", params);
    

    Long bal = Long.valueOf(0L);
    while (ds.nextRow())
    {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      if (wkl.getFromdtm().isAfter(today.minusDays(1))) {
        bal = Long.valueOf(bal.longValue() + wkl.getTotalQty(weeklyOffDays).longValue());
      }
      
      if (wkl.getFromdtm().isBefore(today)) {
        wkl.setFromdtm(today);
        bal = Long.valueOf(bal.longValue() + wkl.getTotalQty(weeklyOffDays).longValue());
      }
    }
    

    return bal;
  }
  
  public void deleteLRPage(LaborRequisitionPage laborRequisitionPage) { CMSLRPageDeleteTransaction trans = new CMSLRPageDeleteTransaction(laborRequisitionPage);
    trans.run();
  }
  
  public void deleteLR(LaborRequisition laborRequisition)
  {
    CMSLRDeleteTransaction trans = new CMSLRDeleteTransaction(laborRequisition);
    trans.run();
  }
  

  public List<LaborRequisitionPage> retrieveLRPagesByWIDAndDates(ObjectIdLong workorderId, KDate fromDtm, KDate toDtm)
  {
    ArrayList params = new ArrayList();
    params.add(workorderId);
    params.add(fromDtm);
    params.add(toDtm);
    

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_LRPAGES_BY_WID_DATES", params);
    

    List<LaborRequisitionPage> lrPages = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      
      lrPages.add(lrPage);
    }
    

    return lrPages;
  }
  

  public List<LaborRequisitionPage> retrieveAllLRPagesByDateAndSections(ObjectIdLong unitId, KDate from, KDate to, ArrayList sse, String statusId, String sec)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    params.add(unitId);
    params.add(from);
    params.add(to);
    params.add(statusId);
   // SQLStatementWithInClause stmtWithInCluase;
    SQLStatementWithInClause stmtWithInCluase; if ((sec != null) && (!"".equalsIgnoreCase(sec))) {
      params.add(sec);
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_DATE_SECTIONS_CODE", sqlSubstitutionArrayList.toArray());
    }
    else {
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_DATE_SECTIONS", sqlSubstitutionArrayList.toArray());
    }
    
    stmtWithInCluase.execute(params);
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    return wkls;
  }
  
  public List<LaborRequisitionPage> retrieveAllLRPagesByWIDAndSections(String workorderId, ArrayList sse, String statusId, String secCode)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(sse);
    ArrayList params = new ArrayList();
    


    SQLStatementWithInClause stmtWithInCluase = null;
    if (((workorderId == null) || ("".equalsIgnoreCase(workorderId))) && (secCode != null) && (!"".equalsIgnoreCase(secCode))) {
      params.add(statusId);
      params.add(secCode);
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_SECTIONS_2_CODE_1", sqlSubstitutionArrayList.toArray());
    }
    else if ((workorderId != null) && (!"".equalsIgnoreCase(workorderId)) && ((secCode == null) || ("".equalsIgnoreCase(secCode)))) {
      params.add(workorderId);
      params.add(statusId);
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_SECTIONS_2", sqlSubstitutionArrayList.toArray());
    }
    else if ((workorderId != null) && (!"".equalsIgnoreCase(workorderId)) && (secCode != null) && (!"".equalsIgnoreCase(secCode))) {
      params.add(workorderId);
      params.add(statusId);
      params.add(secCode);
      stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_SECTIONS_2_CODE", sqlSubstitutionArrayList.toArray());
    }
    stmtWithInCluase.execute(params);
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    return wkls;
  }
  
  public List<Section> getSections() { ArrayList<String> params = new ArrayList();
    

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SECTIONS", params);
    
    ArrayList<Section> secs = new ArrayList();
    while (ds.nextRow()) {
      Section section = new Section(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getObjectIdLong(5));
      secs.add(section);
    }
    
    return secs;
  }
  
  public List<Department> getDepartments()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_DEPTS", params);
    return createDepartmentObjects(ds);
  }
  


  public HashMap<KDate, HashMap> retrieveScheduleQuantityPerWorkorderTradeSkill(KDate fromDtm, KDate toDtm, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk)
  {
    KDate today = KDate.today();
    ArrayList params = new ArrayList();
    params.add(tradeId);
    params.add(skillId);
    params.add(fromDtm);
    params.add(toDtm);
    params.add(wk.getWkNum());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULE_QUANTITY_TF_PER_PERSON", params);
    

    Long bal = Long.valueOf(0L);
    HashMap<KDate, HashMap> map = new HashMap();
    

    while (ds.nextRow()) {
      HashMap row = new HashMap();
      row.put("personId", ds.getObjectIdLong(1));
      row.put("qty", ds.getLong(2));
      row.put("startDate", ds.getKDateTime(3));
      row.put("endDate", ds.getKDateTime(4));
      
      HashMap<ObjectIdLong, HashMap> employeeMap = (HashMap)map.get(ds.getKDateTime(3).getDate());
      if (employeeMap == null) {
        employeeMap = new HashMap();
      }
      
      employeeMap.put(ds.getObjectIdLong(1), row);
      map.put(ds.getKDateTime(3).getDate(), employeeMap);
    }
    

    return map;
  }
  


  public Long retrieveScheduleQuantityPerTradeAndSkill(KDate fromDtm, KDate toDtm, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk, List<LRShift> shifts)
  {
    ArrayList params = new ArrayList();
    params.add(tradeId);
    params.add(skillId);
    params.add(fromDtm);
    params.add(toDtm.plusDays(1));
    params.add(wk.getWkNum());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULE_QUANTITY_TF_PER_PERSON", params);
    

    Long qty = Long.valueOf(0L);
    
    while (ds.nextRow())
    {
      qty = ds.getLong(1);
    }
    
    return qty;
  }
  
  public CMSShift getCMSShiftByStartDate(KDateTime st, KDateTime et, ObjectIdLong unitId) { ArrayList params = new ArrayList();
    params.add(st.getSQLTimestamp());
    params.add(et.getSQLTimestamp());
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SHIFT_BY_START_DATE", params);
    
    if (ds.nextRow()) {
      CMSShift wkl = new CMSShift(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), ds.getString(4), ds.getString(5), ds.getObjectIdLong(6));
      return wkl;
    }
    return null;
  }
  
  public List<LaborRequisition> retrieveApprovedLRsInRange(KDate fromdt, KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk)
  {
    ArrayList params = new ArrayList();
    params.add(todt);
    params.add(fromdt);
    params.add(tradeId.toSQLString());
    params.add(skillId.toSQLString());
    params.add(wk.getWorkorderId().toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_APPROVED_LRLINE_BY_WID_TF", params);
    ArrayList<LaborRequisition> wkls = new ArrayList();
    while (ds.nextRow()) {
      LaborRequisition wkl = new LaborRequisition(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDate(4), ds.getKDate(5), ds.getObjectIdLong(6), 
        ds.getObjectIdLong(7), ds.getObjectIdLong(8));
      wkl.setLrshifts(getLRshifts(ds.getObjectIdLong(1)));
      wkls.add(wkl);
    }
    
    ds.close();
    return wkls;
  }
  
  public HashMap<KDate, HashMap> retrieveSchedulesPerPerson(ObjectIdLong contractorId, KDate fromdt, KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId)
  {
    KDate today = KDate.today();
    ArrayList params = new ArrayList();
    params.add(tradeId);
    params.add(skillId);
    params.add(fromdt);
    params.add(todt);
    params.add(contractorId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULE_TF_PER_PERSON", params);
    

    Long bal = Long.valueOf(0L);
    HashMap<KDate, HashMap> map = new HashMap();
    

    while (ds.nextRow()) {
      HashMap row = new HashMap();
      row.put("personId", ds.getObjectIdLong(1));
      row.put("qty", ds.getLong(2));
      row.put("startDate", ds.getKDateTime(3));
      row.put("endDate", ds.getKDateTime(4));
      
      HashMap<ObjectIdLong, HashMap> employeeMap = (HashMap)map.get(ds.getKDateTime(3).getDate());
      if (employeeMap == null) {
        employeeMap = new HashMap();
      }
      
      employeeMap.put(ds.getObjectIdLong(1), row);
      map.put(ds.getKDateTime(3).getDate(), employeeMap);
    }
    

    return map;
  }
  





  public void deleteSchedules(ArrayList wkList, String lrId, String fromDtm, String toDtm)
  {
    for (Iterator iterator = wkList.iterator(); iterator.hasNext();) {
      Workmen wk = (Workmen)iterator.next();
      ArrayList<Object> params = new ArrayList();
      
      params.add(wk.getPerson().getPersonId().toSQLString());
      SQLStatement update = new SQLStatement(4, "business.cms.DELETE_SCHEDULE_BY_EMP");
      update.execute(params);
    }
  }
  






  public HashMap<KDate, Long> retrieveScheduleQuantityPerWorkorder(KDate fromDtm, KDate toDtm, Workorder wk)
  {
    KDate today = KDate.today();
    ArrayList params = new ArrayList();
    params.add(fromDtm);
    params.add(toDtm);
    params.add(wk.getWkNum());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SCHEDULE_QUANTITY_TF_PER_WK", params);
    

    Long bal = Long.valueOf(0L);
    HashMap<KDate, Long> map = new HashMap();
    

    while (ds.nextRow())
    {
      Long count = (Long)map.get(ds.getKDate(2));
      
      if (count == null) {
        count = Long.valueOf(0L);
      }
      else {
        count = Long.valueOf(count.longValue() + ds.getInt(1).intValue());
      }
      
      map.put(ds.getKDate(2), count);
    }
    
    return map;
  }
  
  public String getCurrentCountWorkmen(String unitId) {
    KDateTime today = KDateTime.createDateTime();
    ArrayList params = new ArrayList();
    params.add(today.changeTime(23, 59, 59, 0));
    params.add(today.changeTime(0, 0, 0, 0));
    params.add(unitId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.active.workmen", params);
    Integer count = Integer.valueOf(0);
    if (ds.nextRow()) {
      count = ds.getInt(1);
    }
    return count.toString();
  }
  
  public ArrayList<ObjectIdLong> getTradeIdsForUnit(ObjectIdLong unitId) {
    ArrayList params = new ArrayList();
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_TRADE_IDS_BY_UNIT", params);
    ArrayList<ObjectIdLong> tradeIds = new ArrayList();
    while (ds.nextRow())
    {
      tradeIds.add(ds.getObjectIdLong(1));
    }
    return tradeIds;
  }
  
  public ArrayList<ObjectIdLong> getSkillIdsForUnit(ObjectIdLong unitId)
  {
    ArrayList params = new ArrayList();
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SKILL_IDS_BY_UNIT", params);
    ArrayList<ObjectIdLong> skillIds = new ArrayList();
    while (ds.nextRow())
    {
      skillIds.add(ds.getObjectIdLong(1));
    }
    return skillIds;
  }
  
  public List<DeviceGroup> retrieveAllDeviceGroups() {
    return DeviceGroup.getDeviceGroups();
  }
  

  public List<LaborRequisitionPage> retrieveLRPagesByWIDAndContCode(String workorderId, Integer approvalSw, KDate fromDtm, KDate toDtm, ArrayList cCodes)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(cCodes);
    
    ArrayList params = new ArrayList();
    params.add(workorderId);
    params.add(fromDtm);
    params.add(toDtm);
    params.add(approvalSw);
    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_WID_CCODE_1", sqlSubstitutionArrayList);
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), 
        ds.getString(8), ds.getKDateTime(9), ds.getKDateTime(10), 
        ds.getString(11), ds.getInt(12));
      wkls.add(lrPage);
    }
    

    return wkls;
  }
  

  public List<LaborRequisitionPage> retrieveLRPagesByContractor(ObjectId unitId, int approved, ArrayList cCodes)
  {
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(cCodes);
    
    ArrayList params = new ArrayList();
    params.add(Integer.valueOf(approved));
    params.add(unitId);
    

    SQLStatementWithInClause stmtWithInCluase = new SQLStatementWithInClause("business.cms.SELECT_LRPAGE_BY_UNITID_CCODE_1", sqlSubstitutionArrayList);
    stmtWithInCluase.execute(params);
    
    DataStoreIfc ds = stmtWithInCluase.getDataStore();
    ArrayList<LaborRequisitionPage> wkls = new ArrayList();
    while (ds.nextRow())
    {
      LaborRequisitionPage lrPage = new LaborRequisitionPage(ds.getObjectIdLong(1), 
        ds.getString(2), ds.getKDate(3), ds.getKDate(4), 
        ds.getInt(5), ds.getObjectIdLong(6), 
        ds.getInt(7), new ArrayList(), ds.getString(8), 
        ds.getKDateTime(9), ds.getKDateTime(10), ds.getString(11), ds.getInt(12));
      
      wkls.add(lrPage);
    }
    

    return wkls;
  }
  

  public List<SafetyModule> getSafetyTrainings()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SAFETY_TRNS", params);
    
    List<SafetyModule> sfts = new ArrayList();
    while (ds.nextRow()) {
      SafetyModule mod = new SafetyModule(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3));
      sfts.add(mod);
    }
    
    return sfts;
  }
  
  public List<SafetyTraining> getSafetyTrainingsByTrade()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SAFETY_TRDS", params);
    
    ArrayList<SafetyTraining> sfts = new ArrayList();
    while (ds.nextRow()) {
      SafetyTraining sft = new SafetyTraining(ds.getObjectIdLong(1), SafetyTraining.getModuleByName(ds.getString(2)), ds.getString(3), 
        Trade.retrieveById(ds.getObjectIdLong(4)), ds.getString(5), 
        ds.getInt(6), ds.getString(7), ds.getString(8), ds.getInt(9));
      sfts.add(sft);
    }
    
    return sfts;
  }
  
  public List<WorkmenSafetyTraining> getWorkmenSafetyTrainings(String personNum) {
    ArrayList params = new ArrayList();
    params.add(personNum);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_SAFETY_TRNS", params);
    
    ArrayList<WorkmenSafetyTraining> sfts = new ArrayList();
    while (ds.nextRow()) {
      WorkmenSafetyTraining sft = new WorkmenSafetyTraining(ds.getObjectIdLong(1), SafetyTraining.getModuleById(ds.getObjectIdLong(2)), 
        ds.getKDateTime(3), ds.getKDateTime(4), ds.getKDateTime(5), 
        ds.getKDateTime(6), ds.getString(7), Department.doRetrieveById(ds.getObjectIdLong(8)), 
        Section.retrieveSection(ds.getObjectIdLong(9)), ds.getString(10), 
        ds.getString(11), ds.getString(12), ds.getString(13), ds.getString(14), 
        ds.getString(15), ds.getString(16), ds.getString(17), ds.getString(18), 
        ds.getString(19), ds.getString(20), ds.getString(21), ds.getString(22), 
        ds.getString(23), ds.getString(24), ds.getKDateTime(25));
      sfts.add(sft);
    }
    
    return sfts;
  }
  
  public List<Workmen> getWorkmenByCodeAndUnit(String empCode2, String unitId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitId);
    params.add(empCode2);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_EMP_CODE_UNITID", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), new ObjectIdLong(unitId)), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  
  public List<Workmen> getWorkmenByNameAndUnit(String empName, String unitId, String statusId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitId);
    params.add(empName);
    params.add(empName);
    params.add(statusId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_EMP_NAME_UNITID", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), new ObjectIdLong(unitId)), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  
  public List<Workmen> getWorkmenByUnit(ObjectIdLong unitId)
  {
    ArrayList params = new ArrayList();
    params.add(unitId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_EMP_UNITID", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        getSkillById(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), new ObjectIdLong(unitId)), 
        ds.getInt(15), Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  
  public List<SafetyViolation> getSafetyViolations()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SAFETY_VLNS", params);
    
    List<SafetyViolation> sfts = new ArrayList();
    while (ds.nextRow()) {
      SafetyViolation mod = new SafetyViolation(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3));
      sfts.add(mod);
    }
    
    return sfts;
  }
  
  public List<WorkmenSafetyViolation> getWorkmenSafetyViolations(String personNum)
  {
    ArrayList params = new ArrayList();
    params.add(personNum);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_SAFETY_VIOLATIONS", params);
    
    ArrayList<WorkmenSafetyViolation> sfts = new ArrayList();
    while (ds.nextRow()) {
      WorkmenSafetyViolation sft = new WorkmenSafetyViolation(ds.getObjectIdLong(1), SafetyViolation.getViolationById(ds.getObjectIdLong(2)), 
        ds.getString(3), ds.getKDateTime(4), ds.getString(5), ds.getString(6), ds.getKDateTime(7));
      sfts.add(sft);
    }
    
    return sfts;
  }
  
  public List<HealthInformation> getHealthInformationList(String personNum) {
    ArrayList params = new ArrayList();
    params.add(personNum);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_HEALTH_INFORMATIONS", params);
    
    ArrayList<HealthInformation> sfts = new ArrayList();
    while (ds.nextRow()) {
      HealthInformation sft = new HealthInformation(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getKDateTime(4), ds.getString(5), ds.getKDateTime(6), ds.getString(7), 
        ds.getKDateTime(8));
      sfts.add(sft);
    }
    
    return sfts;
  }
  





  private String getImageContent(String personNumber)
  {
    APIPersonIdentityBean bean = new APIPersonIdentityBean();
    bean.setPersonNumber(personNumber);
    String imageContent = null;
    APIEmpPhotoBean photoBean = new APIEmpPhotoBean();
    photoBean.setEmployee(bean);
    
    ParameterMap map = new ParameterMap();
    try {
      APIBeanList empPhotoList = (APIBeanList)photoBean.doAction("Load", map);
      APIEmpPhotoBean pbean = (APIEmpPhotoBean)empPhotoList.get(0);
      if ((pbean != null) && (!pbean.getDeleteFlag().booleanValue()))
        imageContent = pbean.getImageContent();
    } catch (EmpPhotoBusinessValidationException ex) {
      imageContent = null;
    } catch (Exception e1) {
      imageContent = null;
    }
    return imageContent;
  }
  




  public boolean doesEmployeeCodeExist(String employeeCode)
  {
    ArrayList<String> params = new ArrayList();
    params.add(employeeCode);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.CHECK_WORKMEN_EMPCODE", params);
    if (ds.nextRow()) {
      String result = ds.getString(1);
      return new Boolean(result).booleanValue();
    }
    return true;
  }
  






  public List<PrincipalEmployee> getPrincipalEmployerList()
  {
    Personality personality = CurrentUserAccountManager.getPersonality();
    PrincipalEmployeeFacade peFacade = new com.kronos.wfc.cms.facade.PrincipalEmployeeFacadeImpl();
    List<PrincipalEmployee> pes = peFacade.getPrincipalEmployees();
    List<PrincipalEmployee> filteredList = new ArrayList();
    try {
      if (personality.getPersonId().longValue() > 0L) {
        List<LaborAccountSetEntry> employeeGroup = personality.getAccessAssignment()
          .getManagerAccessSet().getLaborLevelEntries().collection();
        for (Iterator<PrincipalEmployee> iterator = pes.iterator(); iterator.hasNext();) {
			PrincipalEmployee pe = (PrincipalEmployee) iterator.next();
			for (int i = 0; i < employeeGroup.size(); i++) {
				String llEntryName = ((LaborAccountSetEntry) employeeGroup.get(i)).getLaborLevelEntryName();
				if (llEntryName.equalsIgnoreCase(pe.getUnitCode())) {
					filteredList.add(pe);
				}
			}
		}
        return filteredList;
      }
      else{
      return pes;
      }
    }
    catch (Exception e) {
      Log.log(e.getLocalizedMessage()); }
    return filteredList;
  }
  








  public String getEmployeesSchedule(KDate fromDtm, KDate toDtm, String employeeIds)
  {
    String personNum = null;
    String shiftDate = null;
    String shiftTime = null;
    Map<String, TreeMap<String, String>> schMap = new HashMap();
    TreeMap<String, String> dayAndShiftMap = new TreeMap();
    
    ArrayList<String> params = new ArrayList();
    int[] outputParameters = new int[0];
    int[] outputDataTypes = new int[0];
    int[] inputParameters = { 1, 2, 3 };
    SqlKDate frmdt = new SqlKDate(fromDtm);
    SqlKDate todt = new SqlKDate(toDtm);
    SqlString eIds = new SqlString(employeeIds);
    
    SqlDataSetter[] inputDataTypes = { frmdt, todt, eIds };
    SQLStatement select1 = new SQLStatement(5, "business.cms.SELECT_SCHEDULE_WORKMEN");
    select1.execute(null, null, inputParameters, inputDataTypes);
    
    ArrayList alist = select1.getResultFromCallableStmt();
    List list = new ArrayList();
    for (int i = 5; i < alist.size(); i++) {
      list.add(alist.get(i));
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("<html><body><table border='1' style='width:100%' border-collapse: collapse;>");
    List temp; for (int i = 0; i < list.size(); i++) {
      temp = (List)list.get(i);
      for (int j = 0; j < temp.size(); j++)
      {


        if (j == 0) {
          personNum = temp.get(j).toString();
        } else if (j == 1) {
          shiftDate = temp.get(j).toString();
        } else if (j == 2) {
          shiftTime = temp.get(j).toString();
        }
      }
      dayAndShiftMap = (TreeMap)schMap.get(personNum);
      if (dayAndShiftMap == null) {
        dayAndShiftMap = new TreeMap();
        schMap.put(personNum, dayAndShiftMap);
      }
      dayAndShiftMap.put(shiftDate, shiftTime);
    }
    
    buffer.append("<tr><td><b>Name</b></td>");
    for (String key : dayAndShiftMap.keySet()) {
      buffer.append("<td><b>");
      buffer.append(key);
      buffer.append("</b></td>");
    }
    buffer.append("</tr>");
    for (String key : schMap.keySet()) {
      buffer.append("<tr>");
      buffer.append("<td><b>");
      buffer.append(key);
      buffer.append("</b></td>");
      for (String dayAndShiftMapKey : dayAndShiftMap.keySet()) {
        buffer.append("<td>");
        buffer.append((String)((TreeMap)schMap.get(key)).get(dayAndShiftMapKey));
        buffer.append("</td>");
      }
      buffer.append("</tr>");
    }
    buffer.append("</table></body></html>");
    return buffer.toString();
  }
  



  public void rehireWorkmen(Workmen workmen)
  {
    CMSPersonRehireTransaction trans = new CMSPersonRehireTransaction(workmen);
    trans.run();
  }
  
  public List<String> getAllInterfacesList(long param) {
    ArrayList params = new ArrayList();
    

    String sqlStatement = "";
    if (param > 0L)
    {
      sqlStatement = "business.cms.SELECT_InterfaceNames";
      params.add(Long.valueOf(param));
    }
    if (param < 0L)
    {

      sqlStatement = "business.cms.SELECT_AllInterfaceNames";
    }
    SQLStatement select = new SQLStatement(3, sqlStatement);
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    
    List<String> sfts = new ArrayList();
    while (ds.nextRow())
    {
      sfts.add(ds.getString(2));
    }
    return sfts;
  }
  
  public String getNewEmpCode(String unitId)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitId);

    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.GET.NEW.EMPLOYEECODE", params);
    String result = "";
    if (ds.nextRow()) {
      result = ds.getString(1);
      
      return result;
    }
    return result;
  }
  
  public void updateEmpSeqTable(String unitId) {
    ArrayList<Object> params = new ArrayList();
    params.add(unitId);
    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE.COUNTER");
    update.execute(params);
  }
  
  public String doPFESIAadharUniqueness(String pfNumber, String ESI, String aadharNo) {
    ArrayList<String> params = new ArrayList();
    params.add(ESI);
    params.add(aadharNo);
    params.add(pfNumber);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.CHECK_PF_ESI_AADHAR_UNIQUENESS", params);
    String result = "Skip";
    if (ds.nextRow()) {
      result = ds.getString(1);
      return result;
    }
    return result;
  }
  


public String getWorkorderType(String workorderLineId) {
	
	ArrayList params = new ArrayList();
	params.add(workorderLineId);
	String workorderTypeId="";
	DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_GET_WORKORDER_TYPE_Id", params);
	while(ds.nextRow())
	{
		workorderTypeId=ds.getString(1);
	}
	return workorderTypeId;
}

public String getBalanceQuantity(String wkLineId, String wkLineId2, String itemNumber, String serviceLineItemNumber,
		String wkLineId3) {
	
	ArrayList params = new ArrayList();
	params.add(wkLineId);
	params.add(wkLineId2);
	params.add(itemNumber);
	params.add(serviceLineItemNumber);
	params.add(wkLineId3);
	String balanceQuantity="";
	DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_GET_BALANCE_QTY", params);
	while(ds.nextRow())
	{
		balanceQuantity=ds.getString(1);
	}
	
	return balanceQuantity;
}

public List<Workorder> retrieveWorkorderByUnitAndContId(ObjectIdLong unitId, ObjectIdLong contrId) {
	
	  ArrayList<String> params = new ArrayList();
	  ArrayList<Workorder> workorderList = new ArrayList<Workorder>();
	  params.add(unitId.toSQLString());
	  params.add(contrId.toSQLString());
	  DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDER_BY_UNITID_CONTRID", params);
	  while(ds.nextRow())
	  {
		  Workorder workorder=createWorkorderObjects(ds);
		  workorderList.add(workorder);
	  }
	
	return workorderList;
}

private Workorder createWorkorderObjects(DataStoreIfc ds) {
	
	ObjectIdLong workorderId = ds.getObjectIdLong(1);
	String workorderNum = ds.getString(2);
	ObjectIdLong depId = ds.getObjectIdLong(3);
	ObjectIdLong secId = ds.getObjectIdLong(4);
	Workorder workorder =new Workorder(workorderId,workorderNum,depId,secId);
	return workorder;
}

public List<WorkorderLine> retrieveWorkorderLineByUnitAndContId(ObjectIdLong unitId, ObjectIdLong contrId) {
	
	  ArrayList<String> params = new ArrayList();
	  ArrayList<WorkorderLine> workorderLineList = new ArrayList<WorkorderLine>();
	  params.add(unitId.toSQLString());
	  params.add(contrId.toSQLString());
	  DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKORDERLINE_BY_UNITID_CONTRID", params);
	  while(ds.nextRow())
	  {
		  WorkorderLine workorderLine=createWorkorderLineObjects(ds);
		  workorderLineList.add(workorderLine);
	  }
	return workorderLineList;
}

private WorkorderLine createWorkorderLineObjects(DataStoreIfc ds) {
	
	ObjectIdLong workorderId = ds.getObjectIdLong(1);
	ObjectIdLong workorderLineId = ds.getObjectIdLong(2);
	/*String lineItem = ds.getString(3);
	String serviceLineItem = ds.getString(4);*/
	String item_serviceItem_number = ds.getString(3);
	WorkorderLine workorderLine =new WorkorderLine(workorderId,workorderLineId,item_serviceItem_number);
	return workorderLine;
}

public boolean checkLaborLev6(String laborLev6) {
	
	ArrayList<String> params = new ArrayList();
	params.add(laborLev6);
	 DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.CHECK_LABOR_LEV6", params);
	 while(ds.nextRow()){
		 if("1".equalsIgnoreCase(ds.getString(1).trim())){ 
			 return true;
		 }
	 }
	return false;
}


public Contractorwclic getContrWcLicData(String contractorid, String unitId) {
	
	ArrayList<String> params = new ArrayList();
	params.add(contractorid);
	params.add(unitId);
	 DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_WC", params);
	 Contractorwclic contractorwclic =  new Contractorwclic();
	 while(ds.nextRow())
	 {
		 String checkWC = ds.getString(6);
		 if("WC1".equalsIgnoreCase(checkWC))
		 {
			 contractorwclic.setWc1Id(ds.getObjectIdLong(1));
			 contractorwclic.setWc1(ds.getString(2));
			 contractorwclic.setWc1FromDate(ds.getKDate(3));
			 contractorwclic.setWc1ToDate(ds.getKDate(4));
			 contractorwclic.setWc1Coverage(ds.getString(5));
		 }
		 else if("WC2".equalsIgnoreCase(checkWC))
		 {
			 contractorwclic.setWc2Id(ds.getObjectIdLong(1));
			 contractorwclic.setWc2(ds.getString(2));
			 contractorwclic.setWc2FromDate(ds.getKDate(3));
			 contractorwclic.setWc2ToDate(ds.getKDate(4));
			 contractorwclic.setWc2Coverage(ds.getString(5));
		 }
		 else if("WC3".equalsIgnoreCase(checkWC))
		 {
			 contractorwclic.setWc3Id(ds.getObjectIdLong(1));
			 contractorwclic.setWc3(ds.getString(2));
			 contractorwclic.setWc3FromDate(ds.getKDate(3));
			 contractorwclic.setWc3ToDate(ds.getKDate(4));
			 contractorwclic.setWc3Coverage(ds.getString(5));
		 }
		 else
		 {
			 contractorwclic.setWc4Id(ds.getObjectIdLong(1));
			 contractorwclic.setWc4(ds.getString(2));
			 contractorwclic.setWc4FromDate(ds.getKDate(3));
			 contractorwclic.setWc4ToDate(ds.getKDate(4));
			 contractorwclic.setWc4Coverage(ds.getString(5));
		 }
	 }
	return contractorwclic;
}

public ObjectIdLong getSectionId(String sectionName, ObjectIdLong dId) {
	ArrayList<String> params = new ArrayList();
	params.add(sectionName);
	params.add(dId.toSQLString());
	ObjectIdLong sectionId = new ObjectIdLong(-1L);
	 DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SECTION_ID2", params);
	 while(ds.nextRow()){
		 sectionId = ds.getObjectIdLong(1);
	 }
	 return sectionId;
}

public int getAge(Date birthdate) {
    ArrayList params = new ArrayList();
    params.add(new SimpleDateFormat("yyyy-MM-dd").format(birthdate));
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_GET_AGE_BY_DOB", params);
    int count = ds.getInt(1).intValue();
    return count;
  }
public int getExp(Date experienceDate) {
	ArrayList params = new ArrayList();
    params.add(new SimpleDateFormat("yyyy-MM-dd").format(experienceDate));
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_GET_EXP_BY_DOJ", params);
    int count = ds.getInt(1).intValue();
    return count;
}

public List<Supply> getSupplys() {
	  ArrayList<String> params = new ArrayList();
	  DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_SUPPLY", params);
	  List<Supply> supps = new ArrayList();
	    while (ds.nextRow()) {
	    	Supply supp = new Supply(ds.getString(1), ds.getString(2));
	    	supps.add(supp);
	    }
	    return supps;
}


public List<Supply> doRetrieveAll()
{
  ArrayList<String> params = new ArrayList();
  
  DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL_SUPPLY", params);
  ArrayList<Supply> list = new ArrayList();
  while (ds.nextRow())
  {
	  Supply supp = new Supply(ds.getString(1), ds.getString(2));
    
    list.add(supp);
  }
  return list;
  
}




}
