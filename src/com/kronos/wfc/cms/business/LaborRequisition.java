package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.Iterator;
import java.util.List;




public class LaborRequisition
{
  private ObjectIdLong lrId;
  private String lrNum;
  private String lrName;
  private KDate fromdtm;
  private KDate todtm;
  private ObjectIdLong workOrderlnId;
  private ObjectIdLong tradeId;
  private ObjectIdLong skillId;
  private List<LRShift> lrshifts;
  private String workorderNum;
  private Long totalQty = Long.valueOf(0L);
  



  public LaborRequisition(ObjectIdLong lrId, String lrNum, String lrName, KDate fromdtm, KDate todtm, ObjectIdLong workOrderlnId, ObjectIdLong tradeId, ObjectIdLong skillId)
  {
    this.lrId = lrId;
    this.lrNum = lrNum;
    this.lrName = lrName;
    this.fromdtm = fromdtm;
    this.todtm = todtm;
    this.workOrderlnId = workOrderlnId;
    this.tradeId = tradeId;
    this.skillId = skillId;
  }
  
  public ObjectIdLong getLrId() {
    return lrId;
  }
  
  public void setLrId(ObjectIdLong lrId) { this.lrId = lrId; }
  
  public String getLrNum() {
    return lrNum;
  }
  
  public void setLrNum(String lrNum) { this.lrNum = lrNum; }
  
  public String getLrName() {
    return lrName;
  }
  
  public void setLrName(String lrName) { this.lrName = lrName; }
  
  public KDate getFromdtm() {
    return fromdtm;
  }
  
  public void setFromdtm(KDate fromdtm) { this.fromdtm = fromdtm; }
  
  public KDate getTodtm() {
    return todtm;
  }
  
  public void setTodtm(KDate todtm) { this.todtm = todtm; }
  
  public ObjectIdLong getWorkOrderlnId() {
    return workOrderlnId;
  }
  
  public void setWorkOrderlnId(ObjectIdLong workOrderlnId) { this.workOrderlnId = workOrderlnId; }
  
  public ObjectIdLong getTradeId() {
    return tradeId;
  }
  
  public void setTradeId(ObjectIdLong tradeId) { this.tradeId = tradeId; }
  
  public ObjectIdLong getSkillId() {
    return skillId;
  }
  
  public void setSkillId(ObjectIdLong skillId) { this.skillId = skillId; }
  


  public List<LaborRequisition> retrieveLaborRequisitionsByWId(ObjectIdLong wId, KDate searchFromDtm, KDate searchToDtm, ObjectIdLong woId) { return new CMSService().retrieveLRsByWoId(wId, searchFromDtm, searchToDtm, woId); }
  
  public Trade getTrade() {
    if (tradeId != null)
      return Trade.retrieveById(tradeId);
    return null;
  }
  
  public Skill getSkill() { if (skillId != null)
      return Skill.retrieveSkill(skillId);
    return null;
  }
  
  public static LaborRequisition retrieveLR(String lrId2) {
    if (lrId2 != null) {
      ObjectIdLong id = new ObjectIdLong(lrId2);
      return new CMSService().retrieveLRById(id);
    }
    return null;
  }
  
  public void doUpdate(String unitCode) { validate();
    new CMSService().saveLR(this, unitCode);
  }
  
  private void validate()
  {
    if (skillId == null) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.skill", "Skill"));
    }
    if (tradeId == null) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.trade", "Trade"));
    }
  }
  
  public void doApprove() {
    new CMSService().approveLR(this);
  }
  
  public List<LRShift> getLrshifts() {
    return lrshifts;
  }
  
  public void setLrshifts(List<LRShift> lrshifts) { this.lrshifts = lrshifts; }
  
  private void setTotalQty(List<LRShift> lrshifts, Integer weeklyOffDays) {
    if ((lrshifts != null) && (!lrshifts.isEmpty())) {
      Long quantity = Long.valueOf(0L);
      for (Iterator iterator = lrshifts.iterator(); iterator.hasNext();) {
        LRShift lrShift = (LRShift)iterator.next();
        quantity = Long.valueOf(quantity.longValue() + lrShift.getQty().intValue() * (KDate.differenceInDays(todtm, fromdtm) + 1L - Math.round((float)((KDate.differenceInDays(todtm, fromdtm) + 1L) / 7L)) * weeklyOffDays.intValue()));
      }
      setTotalQty(quantity);
    }
  }
  
  public void setWorkOrderNum(String string) {
    workorderNum = string;
  }
  

  public String getWorkOrderNum() { return workorderNum; }
  
  public Long getTotalQty(Integer weeklyOffDays) {
    setTotalQty(getLrshifts(), weeklyOffDays);
    return totalQty;
  }
  
  public void setTotalQty(Long totalQty) { this.totalQty = totalQty; }
  
  public String getLrDispNum(Workorder wk)
  {
    return lrNum + "/" + wk.getSectionCode() + "/" + wk.getWkNum();
  }
  
  public Long calculateBalanceQty(Workorder wk, Integer weeklyOffDays) {
    WorkorderLine wl = WorkorderLine.retriveWorkOrderlnByWOId(workOrderlnId.toString());
    Long wlQty = Long.valueOf(wl.getQty().longValue());
    Long actualAttendance = new CMSService().getActualAttendanceForWorkOrder(wk, tradeId, skillId);
    Long futureBalance = new CMSService().getFutureLRBalance(wl.getWkLineId(), weeklyOffDays);
    Long balanceQty = Long.valueOf(wlQty.longValue() - (actualAttendance.longValue() + futureBalance.longValue()));
    
    return balanceQty;
  }
  
  public void doDelete() { new CMSService().deleteLR(this); }
}
