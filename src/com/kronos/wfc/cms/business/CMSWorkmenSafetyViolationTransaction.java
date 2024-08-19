package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




public class CMSWorkmenSafetyViolationTransaction
  extends Transaction
{
  private static final String TABLE_NAME = "CMSPERSONVIOLATIONMM";
  private static final String PMMID_TABLE_NAME = "CMSPERSONVIOLATIONMM1";
  Map[] propertyMap;
  String personNum;
  
  public CMSWorkmenSafetyViolationTransaction(String personNum, Map[] propertyMap)
  {
    this.propertyMap = propertyMap;
    this.personNum = personNum;
  }
  
  protected void transaction()
    throws SQLException
  {
    if ((propertyMap != null) && (propertyMap.length > 0)) {
      checkUniqueness();
      List<WorkmenSafetyViolation> wsts = WorkmenSafetyViolation.retrieveByEmployeeCode(personNum);
      HashMap map = convertToSafetyViolationMap(wsts);
      ObjectIdLong mmId = getMMId(personNum);
      
      deleteAll(personNum, mmId);
      for (int i = 0; i < propertyMap.length; i++) {
        Map property = propertyMap[i];
        String sid = (String)property.get("sId");
        if (!"-1".equalsIgnoreCase(sid)) {
          WorkmenSafetyViolation trn = new WorkmenSafetyViolation(null, 
            SafetyViolation.getViolationById(new ObjectIdLong(sid)), 
            (String)property.get("violationDesc"), 
            (String)property.get("violationDate") == null ? null : 
            KServer.stringToDateTime((String)property.get("violationDate")), 
            (String)property.get("actionTaken"), CurrentUserAccountManager.getUserAccountName(), 
            KDateTime.createDateTime());
          insertViolation(mmId, trn);
        }
      }
    }
  }
  

  private void deleteAll(String personNum, ObjectIdLong mmId)
  {
    ArrayList param = new ArrayList();
    param.add(personNum);
    SQLStatement select = new SQLStatement(4, "business.cms.UPDATECMSPERSONSAFETYVIOLID");
    select.execute(param);
    
    param = new ArrayList();
    param.add(mmId);
    
    select = new SQLStatement(4, "business.cms.DELETECMSPERSONVIOLATIONMM");
    select.execute(param);
  }
  
  private ObjectIdLong getMMId(String personNum2)
  {
    ArrayList params = new ArrayList();
    params.add(personNum2);
    SQLStatement select = new SQLStatement(3, "business.cms.SELECTVIOLATIONMMID");
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    ObjectIdLong id = null;
    if (ds.nextRow()) {
      id = ds.getObjectIdLong(1);
    }
    ds.close();
    
    if (id == null) {
      id = ObjectIdManager.create("CMSPERSONVIOLATIONMM1");
    }
    return id;
  }
  
  private HashMap convertToSafetyViolationMap(List<WorkmenSafetyViolation> wsts) {
    HashMap result = new HashMap();
    for (Iterator iterator = wsts.iterator(); iterator.hasNext();) {
      WorkmenSafetyViolation wst = (WorkmenSafetyViolation)iterator.next();
      result.put(wst.getId().toString(), wst);
    }
    return result;
  }
  
  private void insertViolation(ObjectIdLong mmId, WorkmenSafetyViolation sft) {
    ObjectIdLong id = ObjectIdManager.create("CMSPERSONVIOLATIONMM");
    ArrayList params = new ArrayList();
    params.add(id);
    params.add(sft.getSafetyViolation().getSafetyViolationId());
    params.add(sft.getViolationDesc());
    params.add(sft.getViolationDate());
    params.add(sft.getActionTaken());
    params.add(sft.getUpdatedBy());
    params.add(sft.getUpdatedOn());
    params.add(mmId);
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_SAFETY_VIOLATION");
    stmt.execute(params);
    
    params = new ArrayList();
    params.add(mmId);
    params.add(personNum);
    stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_SAFETY_VIOLATION_ID");
    stmt.execute(params);
  }
  
  protected void checkUniqueness() {}
}
