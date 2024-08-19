package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSActivityTransaction;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderActivity;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;
import java.util.Map;

public class WorkOrderFacadeImpl
  implements WorkOrderFacade
{
  public WorkOrderFacadeImpl() {}
  
  public void saveWorkOrder(Workorder wk)
  {
    wk.doUpdate();
  }
  

  public Workorder getWorkOrder(String workorderId)
  {
    return Workorder.retrieveByWorkOrder(workorderId);
  }
  
  public void updateActivityConfiguration(Map[] propertyMaps, String workorderId)
  {
    if (propertyMaps != null) {
      CMSActivityTransaction trans = new CMSActivityTransaction(propertyMaps, new ObjectIdLong(workorderId));
      trans.run();
    }
  }
  



  public List<WorkorderActivity> getWorkOrderActivityMap(ObjectIdLong workorderId)
  {
    return WorkorderActivity.retrieveActiviesByWorkorder(workorderId);
  }
  

  public List<WorkorderLine> getWorkOrderLines(String workorderId)
  {
    return WorkorderLine.retriveWorkOrderln(workorderId);
  }
  

  public WorkorderLine getWorkOrderLineById(String workorderLnId)
  {
    return WorkorderLine.retriveWorkOrderlnByWOId(workorderLnId);
  }
  

  public Workorder getWorkOrderByNum(String wkNum, String statusId, String unitId, List lles, List sse)
  {
    return Workorder.retrieveWorkOrderByNum(wkNum, statusId, unitId, lles, sse);
  }
  

  public Workorder getWorkOrderByNum(String wkNum)
  {
    return Workorder.retrieveWorkOrderByNum(wkNum);
  }
}
