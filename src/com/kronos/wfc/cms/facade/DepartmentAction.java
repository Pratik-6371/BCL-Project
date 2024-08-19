package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.Manager;
import com.kronos.wfc.cms.business.Section;
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

public class DepartmentAction extends WFPLookupDispatchActions
{
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-contractor-application";
  
  public DepartmentAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.detail.refresh", "doRefresh");
    methods.put("cms.action.detail.new", "doNew");
    methods.put("cms.action.detail.delete", "doDelete");
    methods.put("cms.action.detail.return", "doReturn");
    methods.put("cms.action.detail.edit", "doEdit");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  
  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try
    {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      request.setAttribute("id", "id_new");
    }
    catch (Exception e) {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    
    return mapping.findForward("CMSDepartmentUserApplication");
  }
  
  public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      DepartmentFacade facade = getFacade();
      data.setValue("id", getSelectedId(form));
      facade.deleteManager(data);
    }
    catch (Exception e)
    {
      logError(e);
      
      StrutsUtils.addErrorMessage(request, new com.kronos.wfc.platform.i18n.framework.messages.Message("Unable to delete the user as this user was/is owner for workorder"));
    }
    
    return doRefresh(mapping, form, request, response);
  }
  
  private void setDepartmentUserDataToUI(DynamicMapBackedForm form) {
    String depId = (String)form.getValue("depid");
    Department dep = getFacade().getDepartment(depId);
    setValuesToUI(form, dep);
  }
  

  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSDepartmentListApplication");
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String isReqFromListPage = (String)request.getAttribute("EditFromListPage");
      if (isReqFromListPage != null) {
        String deptId = getSelectedId(form);
        DepartmentFacade facade = getFacade();
        Department cont = facade.getDepartment(deptId);
        setValuesToUI(data, cont);
      }
      else {
        String id = getSelectedId(form);
        data.setValue("id", id);
        if ((id == null) || ("".equals(id))) {
          return doRefresh(mapping, form, request, response);
        }
        return mapping.findForward("CMSDepartmentUserApplication");
      }
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, Department dept) {
    data.setValue("depid", dept.getDepid());
    data.setValue("code", dept.getCode());
    data.setValue("description", dept.getDescription());
    Map[] users = getManagerMap(dept);
    data.setValue("cmsManagers", users);
  }
  
  private Map[] getManagerMap(Department dept) {
    List<Manager> mgrs = Manager.getDepartmentManagers(dept.getDepid());
    Map[] users = new Map[mgrs.size()];
    int i = 0;
    for (Iterator iterator = mgrs.iterator(); iterator.hasNext();) {
      Manager manager = (Manager)iterator.next();
      Map mgr = createManagerMap(manager);
      users[(i++)] = mgr;
    }
    return users;
  }
  



  private Map createManagerMap(Manager manager)
  {
    String isDepartmentManager = "";
    Map property = new HashMap();
    property.put("id", manager.getId() == null ? "" : manager.getId().toString());
    property.put("name", manager.getName());
    property.put("userName", manager.getUserName());
    if (manager.getIsDeptMgr() == null) {
      isDepartmentManager = "";
    } else if (manager.getIsDeptMgr().booleanValue()) {
      isDepartmentManager = "Yes";
    } else if (!manager.getIsDeptMgr().booleanValue()) {
      isDepartmentManager = "No";
    }
    property.put("isDepartmentManager", isDepartmentManager);
    property.put("secCode", manager.getSectionId() == null ? "" : Section.retrieveSection(manager.getSectionId()).getCode());
    property.put("secName", manager.getSectionId() == null ? "" : Section.retrieveSection(manager.getSectionId()).getName());
    



    return property;
  }
  
  protected Map createNewManagerRow()
  {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("name", "");
    property.put("userName", "");
    return property;
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  
  protected DepartmentFacade getFacade()
  {
    return new DepartmentFacadeImpl();
  }
  
  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  
  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String configId = (String)form.getValue("selectedId");
    return configId;
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      setDepartmentUserDataToUI(data);
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void logError(Exception e)
  {
    Log.log(e, "Exception in Department Action");
  }
  




  private static Map methodsMap = methodsMap();
}
