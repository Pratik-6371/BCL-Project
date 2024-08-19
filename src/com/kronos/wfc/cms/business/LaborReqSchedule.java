package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.List;





public class LaborReqSchedule
{
  private ObjectIdLong scheduleId;
  private ObjectIdLong workmenId;
  private ObjectIdLong tradeId;
  private ObjectIdLong skillId;
  private ObjectIdLong patternId;
  private KDate fromDTM;
  private KDate toDTM;
  private ObjectIdLong lrId;
  private Integer deletedSw;
  private KDateTime dateTime;
  
  public LaborReqSchedule(ObjectIdLong scheduleId, ObjectIdLong workmenId, ObjectIdLong tradeId, ObjectIdLong skillId, ObjectIdLong patternId, KDate fromDTM, KDate toDTM, ObjectIdLong lrId, Integer deletedSw, KDateTime createdDtm)
  {
    this.scheduleId = scheduleId;
    this.workmenId = workmenId;
    this.tradeId = tradeId;
    this.skillId = skillId;
    this.patternId = patternId;
    this.fromDTM = fromDTM;
    this.toDTM = toDTM;
    this.lrId = lrId;
  }
  
  public ObjectIdLong getScheduleId() {
    return scheduleId;
  }
  
  public void setScheduleId(ObjectIdLong scheduleId) {
    this.scheduleId = scheduleId;
  }
  
  public ObjectIdLong getWorkmenId() {
    return workmenId;
  }
  
  public void setWorkmenId(ObjectIdLong workmenId) {
    this.workmenId = workmenId;
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
  
  public ObjectIdLong getPatternId() {
    return patternId;
  }
  
  public void setPatternId(ObjectIdLong patternId) {
    this.patternId = patternId;
  }
  
  public KDate getFromDTM() {
    return fromDTM;
  }
  
  public void setFromDTM(KDate fromDTM) {
    this.fromDTM = fromDTM;
  }
  
  public KDate getToDTM() {
    return toDTM;
  }
  
  public void setToDTM(KDate toDTM) {
    this.toDTM = toDTM;
  }
  
  public ObjectIdLong getLrId() {
    return lrId;
  }
  
  public void setLrId(ObjectIdLong lrId) {
    this.lrId = lrId;
  }
  
  public static List<LaborReqSchedule> retrieveSchedulesByLRID(String lrid) {
    return new CMSService().retrieveSchedulesByLRID(new ObjectIdLong(lrid));
  }
  

  public static List<LaborReqSchedule> retrieveSchedulesByLRIDAndTimeframe(String lrId2, KDate fromdtm2, KDate todtm2)
  {
    return new CMSService().retrieveSchedulesByLRIDAndTF(lrId2, fromdtm2, todtm2);
  }
  
  public Integer getDeletedSw() {
    return deletedSw;
  }
  
  public void setDeletedSw(Integer deletedSw) {
    this.deletedSw = deletedSw;
  }
  
  public KDateTime getDateTime() {
    return dateTime;
  }
  
  public void setDateTime(KDateTime dateTime) {
    this.dateTime = dateTime;
  }
}
