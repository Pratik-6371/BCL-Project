package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;








public class MinimumWageListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public MinimumWageListAction() {}
  
  protected Map getKeyMethodMap() { return methods; }
  
  protected static Map<String, String> methodMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.edit", "doEdit");
    methods.put("cms.action.new", "doNew");
    return methods;
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      Map[] mwList = getMiniWageList(data);
      
      if (data.getValue("validFrom") == null) {
        data.setValue("validFrom", KDate.today().toString());
      }
      data.setValue("cmsMinimumWageList", mwList);
      data.setValue("availUnitNames", new CMSService().getPrincipalEmployerList());
      request.setAttribute("unitId", data.getValue("unitId"));
    }
    catch (Exception e) {
      logError(e);
      throw e;
    }
    return mapping.findForward("success");
  }
  
  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    return mapping.findForward("");
  }
  
  void setNoSelectionErrorMessage(HttpServletRequest request) {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSMinimumwage");
  }
  
  private Map[] getMiniWageList(DynamicMapBackedForm data) {
    MinimumWageFacade facade = getMinimumWageFacade();
    String unitId = (String)data.getValue("unitId");
    String searchDate = (String)data.getValue("validFrom");
    if ((unitId == null) || (searchDate == null)) {
      return new Map[0];
    }
    Map[] miniwageList = facade.getMinimumWageConfigurations(unitId, searchDate);
    
    return miniwageList;
  }
  

  private void logError(Exception e) {}
  
  private MinimumWageFacade getMinimumWageFacade()
  {
    return new MinimumWageFacadeImpl();
  }
}
