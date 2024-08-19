package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrincipalEmployeeListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public PrincipalEmployeeListAction() {}
  
  protected Map getKeyMethodMap() { return methods; }
  
  protected static Map methodMap() {
    Map methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  

  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      Map[] peList = getPEList();
      data.setValue("cmsPricipalEmployerList", peList);
    }
    catch (Exception e)
    {
      logError(e);
      throw e;
    }
    return mapping.findForward("success");
  }
  
  void setNoSelectionErrorMessage(HttpServletRequest request) {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String id = getSelectedId(form);
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      return doRefresh(mapping, form, request, response);
    }
    
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    data.setValue("selectedId", id);
    return mapping.findForward("doCMSPrincipalApplication");
  }
  



  private void logError(Exception e) {}
  


  private String getId(DynamicMapBackedForm form)
  {
    String id = (String)form.getValue("id");
    return id;
  }
  
  private String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  

  private PrincipalEmployeeFacade getPrincipalEmployeeFacade() { return new PrincipalEmployeeFacadeImpl(); }
  
  private Map[] getPEList() {
    PrincipalEmployeeFacade facade = getPrincipalEmployeeFacade();
    Map[] wsList = facade.getPrincipalEmployeeConfigurations();
    return wsList;
  }
}
