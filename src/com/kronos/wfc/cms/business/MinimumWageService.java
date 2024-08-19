package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MinimumWageService
{
  public MinimumWageService() {}
  
  public MinimumWage retrieveById(com.kronos.wfc.platform.persistence.framework.ObjectId id)
  {
    ArrayList<String> params = new ArrayList();
    
    params.add(((ObjectIdLong)id).toSQLString());
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_BY_MINIMUMWAGEID", params);
    
    return createMWObject(ds);
  }
  

  private MinimumWage createMWObject(DataStoreIfc ds)
  {
    ObjectIdLong minimumwageId = ds.getObjectIdLong(1);
    String name = ds.getString(2);
    KDate from = ds.getKDate(3);
    KDate to = ds.getKDate(4);
    ObjectIdLong tradeId = ds.getObjectIdLong(5);
    ObjectIdLong skillId = ds.getObjectIdLong(6);
    ObjectIdLong wageId = ds.getObjectIdLong(7);
    ObjectIdLong stateid = ds.getObjectIdLong(8);
    
    Wage wage = Wage.retrieveById(wageId);
    
    MinimumWage mw = new MinimumWage(minimumwageId, name, from, to, tradeId, skillId, wage, stateid);
    
    return mw;
  }
  

  private DataStoreIfc getDataStoreFromSQLStatement(String sqlStatement, ArrayList params)
  {
    SQLStatement select = new SQLStatement(3, sqlStatement);
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    return ds;
  }
  

  public MinimumWage retrieveByName(String name)
  {
    ArrayList<String> params = new ArrayList();
    params.add(name);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_BY_MINIMUMWAGENAME", params);
    if (ds.nextRow()) {
      return createMWObject(ds);
    }
    return null;
  }
  










  public List<MinimumWage> retrieveAll()
  {
    ArrayList<String> params = new ArrayList();
    
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_BY_MINIMUMWAGE", params);
    
    return createMWObjects(ds);
  }
  

  private List<MinimumWage> createMWObjects(DataStoreIfc ds)
  {
    ArrayList<MinimumWage> list = new ArrayList();
    
    while (ds.nextRow())
    {
      MinimumWage mw = createMWObject(ds);
      
      list.add(mw);
    }
    return list;
  }
  

  public void updateMinimumWage(List employees)
  {
    if ((employees != null) && (!employees.isEmpty())) {
      Iterator<MinimumWage> iterator = employees.iterator();
      while (iterator.hasNext()) {
        MinimumWage employee = (MinimumWage)iterator.next();
        updateMinimumWage(employee);
      }
    }
  }
  

  private void updateMinimumWage(MinimumWage employee)
  {
    MinimumWageTransaction trans = new MinimumWageTransaction(employee);
    trans.run();
  }
  
  public List<MinimumWage> retrieveMinimumWagesByStateId(ObjectIdLong unitId, KDate date) {
    ArrayList params = new ArrayList();
    params.add(unitId.toSQLString());
    params.add(date);
    params.add(date);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_MW_STATE", params);
    
    return createMWObjects(ds);
  }
}
