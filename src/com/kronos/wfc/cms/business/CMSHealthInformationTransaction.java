package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class CMSHealthInformationTransaction
  extends Transaction
{
  private static final String TABLE_NAME = "CMSPERSONHLTMM";
  private static final String PMMID_TABLE_NAME = "CMSPERSONHLTMM1";
  List<HealthInformation> healthInformationList;
  String personNumber;
  
  public CMSHealthInformationTransaction(String personNumber, List<HealthInformation> healthInformation)
  {
    healthInformationList = healthInformation;
    this.personNumber = personNumber;
  }
  
  protected void transaction() throws SQLException
  {
    if ((healthInformationList != null) && (healthInformationList.size() > 0)) {
      checkUniqueness();
      ObjectIdLong mmId = getMMId(personNumber);
      
      deleteAll(personNumber, mmId);
      for (int i = 0; i < healthInformationList.size(); i++) {
        HealthInformation trn = (HealthInformation)healthInformationList.get(i);
        if (!trn.isBlank()) {
          insertHealthRecord(mmId, trn);
        }
      }
    }
  }
  
  private void deleteAll(String personNum, ObjectIdLong mmId)
  {
    ArrayList param = new ArrayList();
    param.add(personNum);
    SQLStatement select = new SQLStatement(4, "business.cms.UPDATECMSPERSONHLTMM");
    select.execute(param);
    
    param = new ArrayList();
    param.add(mmId);
    
    select = new SQLStatement(4, "business.cms.DELETECMSPERSONHLTMM");
    select.execute(param);
  }
  

  private ObjectIdLong getMMId(String personNum2)
  {
    ArrayList params = new ArrayList();
    params.add(personNum2);
    SQLStatement select = new SQLStatement(3, "business.cms.SELECTCMSPERSONHLTMMID");
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    ObjectIdLong id = null;
    if (ds.nextRow()) {
      id = ds.getObjectIdLong(1);
    }
    ds.close();
    
    if (id == null) {
      id = ObjectIdManager.create("CMSPERSONHLTMM1");
    }
    return id;
  }
  
  private void insertHealthRecord(ObjectIdLong mmId, HealthInformation sft) {
    ObjectIdLong id = ObjectIdManager.create("CMSPERSONHLTMM");
    ArrayList params = new ArrayList();
    params.add(id);
    params.add(sft.getHealthText());
    params.add(sft.getHealthDesc());
    params.add(sft.getHealthTestDate());
    params.add(sft.getMajorConcerns());
    params.add(sft.getNextMedicalTest());
    params.add(sft.getUpdatedBy());
    params.add(sft.getUpdatedOn());
    params.add(mmId);
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_HEALTH_INFORMATION");
    stmt.execute(params);
    
    params = new ArrayList();
    params.add(mmId);
    params.add(personNumber);
    stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_HEALTH_INFORMATION_ID");
    stmt.execute(params);
  }
  
  protected void checkUniqueness() {}
}
