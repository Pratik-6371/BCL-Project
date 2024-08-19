package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Map;


public class ContractorUnitAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  


  public ContractorUnitAction() {}
  


  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("cms.action.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.return", "doReturn");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap;
  }
  






  private static Map methodsMap = methodsMap();
}
