package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class CMSWorkmenSafetyTransaction
  extends Transaction
{
  private static final String TABLE_NAME = "CMSPERSONSAFETYMM";
  private static final String TABLE_NAME1 = "CMSPERSONSAFETYMM1";
  Map[] propertyMap;
  String personNum;
  
  public CMSWorkmenSafetyTransaction(String personNum, Map[] propertyMap)
  {
    this.propertyMap = propertyMap;
    this.personNum = personNum;
  }
  
  protected void transaction()
    throws SQLException
  {
    if ((propertyMap != null) && (propertyMap.length > 0)) {
      checkUniqueness();
      List<WorkmenSafetyTraining> wsts = WorkmenSafetyTraining.retrieveByEmployeeCode(personNum);
      HashMap map = convertToTrainingMap(wsts);
      ObjectIdLong mmId = getMMId(personNum);
      
      deleteAll(personNum, mmId);
      for (int i = 0; i < propertyMap.length; i++) {
        Map property = propertyMap[i];
        String trnId = (String)property.get("trnId");
        if (!"-1".equalsIgnoreCase(trnId)) {
          WorkmenSafetyTraining trn = new WorkmenSafetyTraining(null, 
            SafetyTraining.getModuleById(new ObjectIdLong(trnId)), 
            "".equalsIgnoreCase((String)property.get("dateTaken")) ? null : 
            KServer.stringToDateTime((String)property.get("dateTaken")), 
            "".equalsIgnoreCase((String)property.get("fromTime")) ? null : 
            KDateTime.createDateTime(
            KServer.stringToDuration((String)property.get("fromTime")).millisecondsValue()), 
            "".equalsIgnoreCase((String)property.get("toTime")) ? null : 
            KDateTime.createDateTime(KServer.stringToDuration((String)property.get("toTime"))
            .millisecondsValue()), 
            "".equalsIgnoreCase((String)property.get("nextTrnDa")) ? null : 
            KServer.stringToDateTime((String)property.get("nextTrnDa")), 
            (String)property.get("trnDesc"), 
            Department.doRetrieveById(new ObjectIdLong((String)property.get("dept"))), 
            Section.retrieveSection(new ObjectIdLong((String)property.get("sec"))), 
            (String)property.get("func"), (String)property.get("nJob"), 
            (String)property.get("module"), (String)property.get("TNI"), 
            (String)property.get("facultyNm"), (String)property.get("venue"), 
            (String)property.get("preTestMarksObtained"), (String)property.get("preTestMaxMarks"), 
            (String)property.get("preTestPercentage"), (String)property.get("postTestMarksObtained"), 
            (String)property.get("postTestMaxMarks"), (String)property.get("postTestPercentage"), 
            (String)property.get("recommendation"), (String)property.get("remarks"), 
            CurrentUserAccountManager.getUserAccountName(), KDateTime.createDateTime());
          insertTraining(mmId, trn);
        }
      }
    }
  }
  

  private void deleteAll(String personNum, ObjectIdLong mmId)
  {
    ArrayList param = new ArrayList();
    param.add(personNum);
    SQLStatement select = new SQLStatement(4, "business.cms.UPDATECMSPERSON");
    select.execute(param);
    
    param = new ArrayList();
    param.add(mmId);
    
    select = new SQLStatement(4, "business.cms.DELETECMSPERSONSAFETYMM");
    select.execute(param);
  }
  

  private ObjectIdLong getMMId(String personNum2)
  {
    ArrayList params = new ArrayList();
    params.add(personNum2);
    SQLStatement select = new SQLStatement(3, "business.cms.SELECTMMID");
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    ObjectIdLong id = null;
    if (ds.nextRow()) {
      id = ds.getObjectIdLong(1);
    }
    ds.close();
    
    if (id == null) {
      id = ObjectIdManager.create("CMSPERSONSAFETYMM1");
    }
    return id;
  }
  
  private HashMap convertToTrainingMap(List<WorkmenSafetyTraining> wsts) {
    HashMap result = new HashMap();
    for (Iterator iterator = wsts.iterator(); iterator.hasNext();) {
      WorkmenSafetyTraining wst = (WorkmenSafetyTraining)iterator.next();
      result.put(wst.getId().toString(), wst);
    }
    return result;
  }
  









  private void updateTraining(WorkmenSafetyTraining sft)
  {
    SQLStatement stmt = new SQLStatement(4, "business.cms.UPDATE_WORKMEN_SAFETY_TRN");
    ArrayList params = new ArrayList();
    params.add(sft.getTrn().getSafetyId());
    params.add(sft.getDateTaken());
    params.add(sft.getFromTime());
    params.add(sft.getToTime());
    params.add(sft.getNextTrnDate());
    params.add(sft.getTrnDesc());
    params.add(sft.getDept().getDepid());
    params.add(sft.getSec().getSectionId());
    params.add(sft.getFunc());
    params.add(sft.getnJob());
    params.add(sft.getModule());
    params.add(sft.getTNI());
    params.add(sft.getFacultyNm());
    params.add(sft.getVenue());
    params.add(sft.getPreTestMarksObtained());
    params.add(sft.getPreTestMaxMarks());
    params.add(sft.getPreTestPercentage());
    params.add(sft.getPostTestMarksObtained());
    params.add(sft.getPostTestMaxMarks());
    params.add(sft.getPostTestPercentage());
    params.add(sft.getRecommendation());
    params.add(sft.getRemarks());
    params.add(CurrentUserAccountManager.getUserAccountName());
    params.add(KDateTime.createDateTime());
    params.add(sft.getId());
    stmt.execute(params);
  }
  
  private void insertTraining(ObjectIdLong mmId, WorkmenSafetyTraining sft) {
    ObjectIdLong id = ObjectIdManager.create("CMSPERSONSAFETYMM");
    ArrayList params = new ArrayList();
    params.add(id);
    params.add(sft.getTrn().getSafetyId());
    params.add(sft.getDateTaken());
    params.add(sft.getFromTime());
    params.add(sft.getToTime());
    params.add(sft.getNextTrnDate());
    params.add(sft.getTrnDesc());
    params.add(sft.getDept().getDepid());
    params.add(sft.getSec().getSectionId());
    params.add(sft.getFunc());
    params.add(sft.getnJob());
    params.add(sft.getModule());
    params.add(sft.getTNI());
    params.add(sft.getFacultyNm());
    params.add(sft.getVenue());
    params.add(sft.getPreTestMarksObtained());
    params.add(sft.getPreTestMaxMarks());
    params.add(sft.getPreTestPercentage());
    params.add(sft.getPostTestMarksObtained());
    params.add(sft.getPostTestMaxMarks());
    params.add(sft.getPostTestPercentage());
    params.add(sft.getRecommendation());
    params.add(sft.getRemarks());
    params.add(sft.getUpdatedBy());
    params.add(sft.getUpdatedOn());
    params.add(mmId);
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_SAFETY_TRN");
    stmt.execute(params);
    
    params = new ArrayList();
    params.add(mmId);
    params.add(personNum);
    stmt = new SQLStatement(4, "business.cms.INSERT_WORKMEN_SAFETY_ID");
    stmt.execute(params);
  }
  
  protected void checkUniqueness() {}
}
