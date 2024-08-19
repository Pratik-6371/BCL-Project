package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;








public class DepartmentListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public DepartmentListAction() {}
  
  protected Map getKeyMethodMap() {
    return methods;
  }
  
  protected static Map methodMap()
  {
    Map methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String unitId = (String)data.getValue("unitId");
    //List<PrincipalEmployee> pes;
    try {
      if (unitId != null) {
        List<Department> deps = new DepartmentFacadeImpl().getDepartments(unitId);
        Map[] attr = convertDepartmentListToAttributes(deps);
        data.setValue("cmsDepartmentList", attr);
      }
    }
    catch (Exception e)
    {
      logError(e);
      throw e;
    }
    finally {
      List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
      data.setValue("availUnitNames", pes);
      request.setAttribute("unitId", unitId);
    }
    return mapping.findForward("success");
  }
  
  private Map[] convertDepartmentListToAttributes(List<Department> deps)
  {
    Map[] configMaps = new Map[deps.size()];
    int i = 0;
    for (Iterator<Department> it = deps.iterator(); it.hasNext();)
    {
      Department config = (Department)it.next();
      Map configMap = fillAttributeMapNoProperties(config);
      configMaps[(i++)] = configMap;
    }
    
    return configMaps;
  }
  
  private Map fillAttributeMapNoProperties(Department config) {
    Map configMap = new HashMap();
    configMap.put("depid", config.getDepid().toString());
    configMap.put("code", config.getCode());
    configMap.put("description", config.getDescription());
    configMap.put("unitId", config.getUnitId().toString());
    
    return configMap;
  }
  

  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
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
    request.setAttribute("EditFromListPage", "true");
    return mapping.findForward("doCMSDepartmentApplication");
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
  
  private DepartmentFacade getDepartmentFacade() {
    return new DepartmentFacadeImpl();
  }
}
