package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.LaborReqSchedule;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.TimeDetailRow;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract interface LaborReqFacade
{
  public abstract List<LaborRequisition> retrieveLaborReqsByWID(String paramString1, String paramString2, String paramString3, List paramList);
  
  public abstract List<LaborRequisition> retrieveLaborReqByWorkorderLn(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract LaborRequisition retrieveLaborReqById(String paramString);
  
  public abstract Workorder retrieveWorkorderByLRID(String paramString);
  
  public abstract LaborRequisition saveLaborRequisition(LaborRequisition paramLaborRequisition, String paramString);
  
  public abstract void saveSchedule(ArrayList paramArrayList, LaborRequisition paramLaborRequisition, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  public abstract List<LaborReqSchedule> retrieveSchedulesByLRIDAndTF(ObjectIdLong paramObjectIdLong, String paramString1, String paramString2);
  
  public abstract List<TimeDetailRow> retrieveAttendance(List<ObjectIdLong> paramList, KDate paramKDate1, KDate paramKDate2);
  
  public abstract List<LaborRequisition> retrieveLaborReqsByWIDAndContCode(String paramString1, String paramString2, String paramString3, ArrayList paramArrayList);
  
  public abstract List<LaborRequisition> retrieveLaborReqsForContractor(ObjectId paramObjectId, int paramInt, ArrayList paramArrayList);
  
  public abstract List<LaborRequisition> retrieveAllLRsBYSections(ObjectId paramObjectId, ArrayList paramArrayList);
  
  public abstract LaborRequisitionPage retrieveLaborReqPageById(String paramString);
  
  public abstract Workorder retrieveWorkorderByLRPageID(String paramString);
  
  public abstract LaborRequisitionPage saveLaborRequisitionPage(LaborRequisitionPage paramLaborRequisitionPage, String paramString);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesByWIDAndContCode(String paramString, Integer paramInteger, KDate paramKDate1, KDate paramKDate2);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesByWIDAndContCode(String paramString, Integer paramInteger, KDate paramKDate1, KDate paramKDate2, ArrayList paramArrayList);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesForContractor(ObjectId paramObjectId, int paramInt);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesForContractor(ObjectId paramObjectId, int paramInt, ArrayList paramArrayList);
  
  public abstract List<LaborRequisitionPage> retrieveAllLRPagesBySections(ObjectId paramObjectId, ArrayList paramArrayList, String paramString);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesByWID(String paramString);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesByWID(String paramString1, KDate paramKDate1, KDate paramKDate2, ArrayList paramArrayList, String paramString2, String paramString3);
  
  public abstract List<LaborRequisitionPage> retrieveAllLRPagesByDateAndSections(ObjectIdLong paramObjectIdLong, KDate paramKDate1, KDate paramKDate2, ArrayList paramArrayList, String paramString1, String paramString2);
  
  public abstract List<LaborRequisitionPage> retrieveLaborReqPagesByWIDWithAccess(String paramString1, ArrayList paramArrayList, String paramString2, String paramString3);
  
  public abstract void removeSchedule(ArrayList paramArrayList, LaborRequisition paramLaborRequisition, String paramString1, String paramString2);
  
  public abstract void validate(HashMap paramHashMap, String paramString1, String paramString2, ObjectIdLong paramObjectIdLong1, ObjectIdLong paramObjectIdLong2, Workorder paramWorkorder);
  
  public abstract HashMap<ObjectIdLong, Long> scheduleQuantityPerShift(KDate paramKDate1, KDate paramKDate2, ObjectIdLong paramObjectIdLong1, ObjectIdLong paramObjectIdLong2, Workorder paramWorkorder);
  
  public abstract HashMap<ObjectIdLong, Long> getLRQuantityPerShift(KDate paramKDate1, KDate paramKDate2, ObjectIdLong paramObjectIdLong1, ObjectIdLong paramObjectIdLong2, Workorder paramWorkorder);
  
  public abstract HashMap<ObjectIdLong, String> hasSchedulesPerPerson(List paramList, KDate paramKDate1, KDate paramKDate2, ObjectIdLong paramObjectIdLong1, ObjectIdLong paramObjectIdLong2, Workorder paramWorkorder);
}
