package com.kronos.wfc.cms.business;

import com.ibm.icu.util.StringTokenizer;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.datacollection.empphoto.api.APIEmpPhotoBean;
import com.kronos.wfc.datacollection.empphoto.business.EmpPhotoBusinessValidationException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.xml.api.bean.APIBeanList;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContractorService
{
  public ContractorService() {}
  
  public Contractor retrieveById(ObjectIdLong contractorid, ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(contractorid.toSQLString());
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_BY_ID", params);
    
    return createContObject(ds);
  }
  


  public List<Contractor> retrieveAll()
  {
    ArrayList<String> params = new ArrayList();
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL_CONTRACTORS", params);
    
    return createContObjects(ds);
  }
  

  private List<Contractor> createContObjects(DataStoreIfc ds)
  {
    ArrayList<Contractor> list = new ArrayList();
    
    while (ds.nextRow())
    {
      Contractor contr = createContObject(ds);
      
      list.add(contr);
    }
    return list;
  }
  





  private String getAddress(String address, int i)
  {
    if (address != null)
    {
      StringTokenizer st2 = new StringTokenizer(address, "|");
      
      int j = 0;
      
      String result = "";
      
      while (st2.hasMoreElements())
      {
        result = (String)st2.nextElement();
        
        if (i == j)
        {
          return result;
        }
        j++;
      }
    }
    

    return null;
  }
  








  private Contractor createContObject(DataStoreIfc ds)
  {
    ObjectIdLong id = ds.getObjectIdLong(1);
    ObjectIdLong unitId = ds.getObjectIdLong(2);
    String contractorname = ds.getString(3);
    String caddress = getAddress(ds.getString(4), 0);
    String caddress1 = getAddress(ds.getString(4), 1);
    String caddress2 = getAddress(ds.getString(4), 2);
    String caddress3 = getAddress(ds.getString(4), 3);
    String ccode = ds.getString(5);
    String managername = ds.getString(6);
    String esiwcnumber = ds.getString(7);
    String licensenumber = ds.getString(8);
    KDate licensevalidity1 = ds.getKDate(9);
    KDate licencevalidity2 = ds.getKDate(10);
    String coverage = ds.getString(11);
    String totalstrength = ds.getString(12);
    String maxnumberofemployees = ds.getString(13);
    String natureofwork = ds.getString(14);
    String locofWork = ds.getString(15);
    KDate periodofcontact1 = ds.getKDate(16);
    KDate periodofcontract2 = ds.getKDate(17);
    String vendorcode = ds.getString(18);
    String pfCode = ds.getString(19);
    KDate esiwvalidityperiodFrom = ds.getKDate(20);
    KDate esiwvalidityperiodTo = ds.getKDate(21);
    String esicoverage = ds.getString(22);
    String pfnumber = ds.getString(23);
    KDate pfcodeapplicationdate = ds.getKDate(24);
    Boolean isBlocked = Boolean.valueOf("1".equalsIgnoreCase(ds.getString(25)));
    String commission = ds.getString(26);
    String pfCoverage = ds.getString(27);
    Boolean isPfSelf = Boolean.valueOf("1".equalsIgnoreCase(ds.getString(28)));
    Boolean isEsiSelf = Boolean.valueOf("1".equalsIgnoreCase(ds.getString(29)));
    String licensenumber1 = ds.getString(30);
    KDate licensevalidityFrom = ds.getKDate(31);
    KDate licencevalidityTo = ds.getKDate(32);
    
    Contractor contr = new Contractor(id, unitId, contractorname, caddress, caddress1, caddress2, 
    		caddress3, ccode, managername, esiwcnumber, licensenumber, licensevalidity1, 
      licencevalidity2, coverage, totalstrength, maxnumberofemployees, natureofwork, locofWork,
      periodofcontact1, periodofcontract2, vendorcode, pfCode, 
      esiwvalidityperiodFrom, esiwvalidityperiodTo, esicoverage, pfnumber, pfcodeapplicationdate, 
      isBlocked, commission, pfCoverage, isPfSelf, isEsiSelf,licensenumber1, licensevalidityFrom, 
      licencevalidityTo);
    
    return contr;
  }
  

  public Contractor retrieveByName(String contractorName)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(contractorName);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_BY_NAME", params);
    
    if (ds.nextRow()) {
      return createContObject(ds);
    }
    return null;
  }
  

  private static DataStoreIfc getDataStoreFromSQLStatement(String sqlStatement, ArrayList<String> params)
  {
    SQLStatement select = new SQLStatement(3, sqlStatement);
    
    select.execute(params);
    
    DataStoreIfc ds = select.getDataStore();
    
    return ds;
  }
  


  public void updateContractors(List employees)
  {
    if ((employees != null) && (!employees.isEmpty())) {
      Iterator<Contractor> iterator = employees.iterator();
      while (iterator.hasNext()) {
        Contractor employee = (Contractor)iterator.next();
        
        updateContractor(employee);
      }
    }
  }
  




  public void updateContractor(Contractor contr)
  {
    ContractorTransaction trans = new ContractorTransaction(contr);
    trans.run();
  }
  

  protected List retrieveByUnitId(ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(unitId.toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_BY_UNITID", params);
    
    return createContObjects(ds);
  }
  


  public Contractor retrieveByName(String name, String unitId2)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add("%" + name + "%");
    params.add(unitId2);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_BY_NAME_UNITID", params);
    
    if (ds.nextRow()) {
      return createContObject(ds);
    }
    return null;
  }
  


  public Contractor retrieveByCode(String code, String unitId2)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(code);
    params.add(unitId2);
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_CONTRACTOR_BY_CODE_UNITID", params);
    
    if (ds.nextRow()) {
      return createContObject(ds);
    }
    return null;
  }
  




  public List<Workmen> getWorkmenByContractor(ObjectIdLong contrId, String empCode, String unitId, String statusId)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(contrId.toSQLString());
    params.add(empCode);
    params.add(statusId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_CONTID_EMPCODE", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        Skill.retrieveSkill(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), new ObjectIdLong(unitId)), 
        ds.getInt(15), com.kronos.wfc.commonapp.people.business.person.Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  






  public List<Workmen> getWorkmenByContractorAndWKName(ObjectIdLong id, String empName, String unitId, String statusId)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(id.toSQLString());
    params.add(empName);
    params.add(empName);
    params.add(statusId);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_WORKMEN_BY_CONTID_EMPNAME", params);
    
    ArrayList<Workmen> ws = new ArrayList();
    while (ds.nextRow()) {
      Personality p = Personality.getByPersonId(ds.getObjectIdLong(16));
      String imageContent = getImageContent(p.getPersonNumber());
      Workmen workmen = new Workmen(ds.getObjectIdLong(1), ds.getString(2), ds.getString(3), 
        ds.getString(4), ds.getString(5), ds.getKDate(6), 
        ds.getString(7), Trade.retrieveById(ds.getObjectIdLong(8)), 
        Skill.retrieveSkill(ds.getObjectIdLong(9)), Address.retrieveById(ds.getObjectIdLong(10)), 
        Address.retrieveById(ds.getObjectIdLong(11)), WorkmenDetail.retrieveById(ds.getObjectIdLong(12)), 
        Wage.retrieveById(ds.getObjectIdLong(13)), Contractor.doRetrieveById(ds.getObjectIdLong(14), new ObjectIdLong(unitId)), 
        ds.getInt(15), com.kronos.wfc.commonapp.people.business.person.Person.getByPersonId(ds.getObjectIdLong(16)), ds.getBoolean(17), 
        p.getCurrentBadgeNumber(), p.getJobAssignment().getJobAssignmentDetails().getDeviceGroup(), 
        imageContent == null ? null : imageContent, ds.getString(18));
      ws.add(workmen);
    }
    
    return ws;
  }
  






  private String getImageContent(String personNumber)
  {
    APIPersonIdentityBean bean = new APIPersonIdentityBean();
    bean.setPersonNumber(personNumber);
    String imageContent = null;
    APIEmpPhotoBean photoBean = new APIEmpPhotoBean();
    photoBean.setEmployee(bean);
    
    com.kronos.wfc.platform.xml.api.bean.ParameterMap map = new com.kronos.wfc.platform.xml.api.bean.ParameterMap();
    try
    {
      APIBeanList empPhotoList = (APIBeanList)photoBean.doAction("Load", map);
      APIEmpPhotoBean pbean = (APIEmpPhotoBean)empPhotoList.get(0);
      if ((pbean != null) && (!pbean.getDeleteFlag().booleanValue()))
        imageContent = pbean.getImageContent();
    } catch (EmpPhotoBusinessValidationException ex) {
      ex.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return imageContent;
  }
}
