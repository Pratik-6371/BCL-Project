package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.commonapp.email.api.SMTPEmailAPI;
import com.kronos.wfc.platform.logging.framework.Log;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;









public class LaborRequisitionPage
{
  private ObjectIdLong pageId;
  private String pageNum;
  private KDate fromDtm;
  private KDate toDtm;
  private Integer approvalSw;
  private ObjectIdLong workorderId;
  private Integer deleteSw;
  private List<LaborRequisition> lrs;
  private String remark;
  private Integer weeklyOffDays;
  private KDateTime createdOn;
  private KDateTime updatedOn;
  private String updatedBy;
  public static final int UNAPPROVED = 0;
  public static final int APPROVED = 1;
  public static final int REJECTED = 2;
  public static final int EXPIRED = 3;
  public static final int ALL = -1;
  
  public LaborRequisitionPage(ObjectIdLong pageId, String pageNum, KDate fromDtm, KDate toDtm, Integer approvalSw, ObjectIdLong workorderId, Integer deleteSw, List<LaborRequisition> lrs, String remark, KDateTime createdOn, KDateTime updatedOn, String updatedBy, Integer weeklyOffDays)
  {
    this.pageId = pageId;
    this.pageNum = pageNum;
    this.fromDtm = fromDtm;
    this.toDtm = toDtm;
    this.approvalSw = approvalSw;
    this.workorderId = workorderId;
    this.deleteSw = deleteSw;
    this.lrs = lrs;
    setRemark(remark);
    setWeeklyOffDays(weeklyOffDays);
    setCreatedOn(createdOn);
    setUpdatedOn(updatedOn);
    setUpdatedBy(updatedBy);
  }
  
  public ObjectIdLong getPageId()
  {
    return pageId;
  }
  
  public void setPageId(ObjectIdLong pageId) {
    this.pageId = pageId;
  }
  
  public String getPageNum() {
    return pageNum;
  }
  
  public void setPageNum(String pageNum) {
    this.pageNum = pageNum;
  }
  
  public KDate getFromDtm() {
    return fromDtm;
  }
  
  public void setFromDtm(KDate fromDtm) {
    this.fromDtm = fromDtm;
  }
  
  public KDate getToDtm() {
    return toDtm;
  }
  
  public void setToDtm(KDate toDtm) {
    this.toDtm = toDtm;
  }
  
  public Integer getApprovalSw() {
    return approvalSw;
  }
  
  public void setApprovalSw(Integer approvalSw) {
    this.approvalSw = approvalSw;
  }
  
  public ObjectIdLong getWorkorderId() {
    return workorderId;
  }
  
  public void setWorkorderId(ObjectIdLong workorderId) {
    this.workorderId = workorderId;
  }
  
  public Integer getDeleteSw() {
    return deleteSw;
  }
  
  public void setDeleteSw(Integer deleteSw) {
    this.deleteSw = deleteSw;
  }
  
  public List<LaborRequisition> getLrs() {
    return lrs;
  }
  
  public void setLrs(List<LaborRequisition> lrs) {
    this.lrs = lrs;
  }
  
  public void sendApprovalNotification()
  {
    if ("true".equalsIgnoreCase(KronosProperties.get("cms.enable.notifications", "false"))) {
      Workorder order = Workorder.retrieveByWorkOrder(workorderId.toString());
      
      if ((order.getOwnerId() != null) && (order.getSuprid() != null)) {
        Manager manager = Manager.doRetrieveById(order.getOwnerId());
        Supervisor supr = Supervisor.getSupervisor(order.getSuprid());
        
        if (getApprovalSw().intValue() == 1)
        {

          SMTPEmailAPI.send(manager.getEmailAddr(), KronosProperties.get("site.email.sender"), "", "LR " + getPageDispNum(order) + " Approved", "");
          
          if ((supr != null) && (supr.getEmailAddr() != null)) {
            SMTPEmailAPI.send(supr.getEmailAddr(), KronosProperties.get("site.email.sender"), "", "LR " + getPageDispNum(order) + " Approved", "");
          } else {
            Log.log(1, "Could not send email as the supervisor or supervisor email address not found" + supr != null ? supr.getName() : "null");
          }
        } else {
          SMTPEmailAPI.send(manager.getEmailAddr(), KronosProperties.get("site.email.sender"), "", "LR " + getPageDispNum(order) + " Rejected", "");
          
          if ((supr != null) && (supr.getEmailAddr() != null)) {
            SMTPEmailAPI.send(supr.getEmailAddr(), KronosProperties.get("site.email.sender"), "", "LR " + getPageDispNum(order) + " Rejected", "");
          } else {
            Log.log(1, "Could not send email as the supervisor or supervisor email address not found" + supr != null ? supr.getName() : "null");
          }
        }
      }
    }
  }
  
  public void sendLRCreationNotification() {
    if ("true".equalsIgnoreCase(KronosProperties.get("cms.enable.notifications", "false"))) {
      Workorder order = Workorder.retrieveByWorkOrder(workorderId.toString());
      
      if (order.getOwnerId() != null) {
        Manager manager = Manager.doRetrieveById(order.getOwnerId());
        Department dept = Department.doRetrieveById(order.getDepId());
        List<Manager> mgrs = Manager.getDepartmentManagers(dept.getDepid());
        
        for (Iterator iterator = mgrs.iterator(); iterator.hasNext();) {
          Manager mgr = (Manager)iterator.next();
          SMTPEmailAPI.send(mgr.getEmailAddr(), KronosProperties.get("site.email.sender"), "", "LR " + getPageDispNum(order) + " created or modified for workorder " + order.getWkNum(), 
            " Details: \n Section " + order.getSectionCode() + " \n Created or modified by " + manager.getName());
        }
      }
    }
  }
  

  public String getPageDispNum(Workorder wk)
  {
    return pageNum + "/" + wk.getSectionCode() + "/" + wk.getWkNum();
  }
  
  public void doApprove()
  {
    new CMSService().approveLRPage(this);
  }
  

  public void doUpdate(String unitCode)
  {
    validatePage();
    new CMSService().saveLRPage(this, unitCode);
  }
  








  private void validatePage()
  {
    if (sameRecordExist()) {
      throw CMSException.lrWithTheSameDateRangeExists();
    }
    
    if (getTotalLRQty() == 0L) {
      throw CMSException.lrQuantityShouldBeGreaterThanZero();
    }
  }
  

  private boolean sameRecordExist()
  {
    List<LaborRequisitionPage> list = new CMSService().retrieveLRPagesByWIDAndDates(workorderId, fromDtm, toDtm);
    KDateTime currentTime = KDateTime.createDateTime();
    
    if ((list != null) && (!list.isEmpty())) {
      for (Iterator iterator = list.iterator(); iterator.hasNext();) {
        LaborRequisitionPage lr = 
          (LaborRequisitionPage)iterator.next();
        Long seconds = Long.valueOf(currentTime.differenceInSeconds(lr.getCreatedOn()));
        if (seconds.longValue() / 10L < 60L) {
          return true;
        }
      }
    }
    return false;
  }
  
  private long getTotalLRQty()
  {
    if ((lrs != null) && (!lrs.isEmpty())) {
      long i = 0L;
      for (Iterator iterator = lrs.iterator(); iterator.hasNext();) {
        LaborRequisition type = (LaborRequisition)iterator.next();
        i += type.getTotalQty(weeklyOffDays).longValue();
      }
      return i;
    }
    return 0L;
  }
  


  public void doDelete()
  {
    new CMSService().deleteLRPage(this);
  }
  

  public String getRemark()
  {
    return remark;
  }
  
  public void setRemark(String remark)
  {
    this.remark = remark;
  }
  
  public KDateTime getCreatedOn()
  {
    return createdOn;
  }
  
  public void setCreatedOn(KDateTime createdOn)
  {
    this.createdOn = createdOn;
  }
  
  public KDateTime getUpdatedOn()
  {
    return updatedOn;
  }
  
  public void setUpdatedOn(KDateTime updatedOn)
  {
    this.updatedOn = updatedOn;
  }
  
  public String getUpdatedBy()
  {
    return updatedBy;
  }
  
  public void setUpdatedBy(String updatedBy)
  {
    this.updatedBy = updatedBy;
  }
  
  public static Map getStatusMap()
  {
    HashMap map = new HashMap();
    map.put(Integer.valueOf(0), "UnApproved");
    map.put(Integer.valueOf(1), "Approved");
    map.put(Integer.valueOf(2), "Rejected");
    map.put(Integer.valueOf(3), "Expired");
    return map;
  }
  
  public Integer getWeeklyOffDays()
  {
    return weeklyOffDays;
  }
  
  public void setWeeklyOffDays(Integer weeklyOffDays)
  {
    this.weeklyOffDays = weeklyOffDays;
  }
}
