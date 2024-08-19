package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderActivity;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;
import java.util.Map;

public abstract interface WorkOrderFacade
{
  public abstract void saveWorkOrder(Workorder paramWorkorder);
  
  public abstract Workorder getWorkOrder(String paramString);
  
  public abstract void updateActivityConfiguration(Map[] paramArrayOfMap, String paramString);
  
  public abstract List<WorkorderActivity> getWorkOrderActivityMap(ObjectIdLong paramObjectIdLong);
  
  public abstract List<WorkorderLine> getWorkOrderLines(String paramString);
  
  public abstract WorkorderLine getWorkOrderLineById(String paramString);
  
  public abstract Workorder getWorkOrderByNum(String paramString);
  
  public abstract Workorder getWorkOrderByNum(String paramString1, String paramString2, String paramString3, List paramList1, List paramList2);
}
