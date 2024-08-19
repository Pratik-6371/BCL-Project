package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Manager;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DepartmentUserAction
  extends WFPLookupDispatchActions
{
  public DepartmentUserAction() {}
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  protected static Map methodsMap() {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.user.refresh", "doRefresh");
    methods.put("cms.action.user.save", "doSave");
    methods.put("cms.action.user.save.and.new", "doSaveAndNew");
    methods.put("cms.action.user.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.user.return", "doReturn");
    
    return methods;
  }
  
  protected Map getKeyMethodMap() {
    return methodsMap();
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      validateForm(data);
      saveManager(form);
    } catch (Exception e) {
      if ((e instanceof CMSException)) {
        StrutsUtils.addErrorMessage(request, (CMSException)e);
        return doRefresh(mapping, form, request, response);
      }
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void validateForm(DynamicMapBackedForm data) throws Exception
  {
    String name = (String)data.getValue("name");
    String userName = (String)data.getValue("userName");
    String passwd = (String)data.getValue("passwd");
    String mobileno = (String)data.getValue("mobileno");
    String emailAddr = (String)data.getValue("emailAddr");
    String depId = (String)data.getValue("depid");
    if (("".equalsIgnoreCase(name)) || ("".equalsIgnoreCase(userName)) || ("".equalsIgnoreCase(passwd)) || 
      ("".equalsIgnoreCase(mobileno)) || ("".equalsIgnoreCase(emailAddr)) || ("".equalsIgnoreCase(depId))) {
      throw CMSException.missingRequiredFields();
    }
    
    String dot = (String)data.getValue("dot");
    KDate doj = KDate.today();
    if ((dot != null) && (!"null".equals(dot)) && (!"".equals(dot)) && 
      (dot != null)) {
      KDate kdot = KServer.stringToDate(dot);
      if ((kdot != null) && (doj != null) && (kdot.isBefore(doj))) {
        List ex = new ArrayList();
        ex.add(
          CMSException.invalidDate(KronosProperties.get("label.dateOfTermination", "Date Of Termination")));
        CMSException e = CMSException.validationError();
        e.addWrappedExceptions(ex);
        throw e;
      }
    }
  }
  
  void setNoFieldsSelected(HttpServletRequest request)
  {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  
  public ActionForward doSaveAndNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      validateForm(data);
      saveManager(form);
      data.setValue("id", "id_new");
      return doRefresh(mapping, form, request, response);
    } catch (Exception e) {
      if ((e instanceof CMSException)) {
        StrutsUtils.addErrorMessage(request, (CMSException)e);
        return doRefresh(mapping, form, request, response);
      }
      
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void saveManager(ActionForm form) {
    Manager mgr = savedManagerFromUI((DynamicMapBackedForm)form);
    setValuesToUI((DynamicMapBackedForm)form, mgr);
    clearUiDirtyData((DynamicMapBackedForm)form);
  }
  
  public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      validateForm(data);
      saveManager(form);
      clearUiDirtyData((DynamicMapBackedForm)form);
    } catch (Exception e) {
      if ((e instanceof CMSException)) {
        StrutsUtils.addErrorMessage(request, (CMSException)e);
        return doRefresh(mapping, form, request, response);
      }
      
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    
    return mapping.findForward("doCMSDepartmentApplication");
  }
  
  private void setDepartmentUserDataToUI(DynamicMapBackedForm form) {
    String id = (String)form.getValue("id");
    Manager mgr = getFacade().getDepartmentManager(id);
    setValuesToUI(form, mgr);
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSDepartmentApplication");
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, Manager mgr) {
    if (mgr.getId() == null) {
      data.setValue("id", "id_new");
    } else {
      data.setValue("id", mgr.getId().toString());
    }
    data.setValue("name", mgr.getName());
    data.setValue("depid", mgr.getDeptId().toString());
    if (mgr.getSectionId() != null) {
      data.setValue("sectionId", mgr.getSectionId().toString());
    } else {
      data.setValue("sectionId", "-1");
    }
    data.setValue("userName", mgr.getUserName());
    data.setValue("passwd", mgr.getPasswd());
    data.setValue("emailAddr", mgr.getEmailAddr());
    data.setValue("mobileno", mgr.getMobilenum());
    data.setValue("isDeptMgr", mgr.getIsDeptMgr().booleanValue() ? "on" : "off");
    data.setValue("availSectionNames", getSectionNames(mgr.getDeptId(), mgr.getSectionId()));
    KDate dot = mgr.getDot();
    if (dot != null) {
      data.setValue("dot", dot);
    } else {
      data.setValue("dot", "");
    }
  }
  
  private List<Section> getSectionNames(ObjectIdLong depId, ObjectIdLong secId) {
    List<Section> secs = getFacade().retrieveSections(depId.toString());
    for (Iterator iterator = secs.iterator(); iterator.hasNext();) {
      Section section = (Section)iterator.next();
      if (section.getSectionId().equals(secId)) {
        section.setSelected(Boolean.valueOf(true));
        break;
      }
    }
    
    return secs;
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data) {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  String getSelectedId(ActionForm form) {
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
  
  private Manager savedManagerFromUI(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    DepartmentFacade facade = getFacade();
    Manager manager = createManager(data);
    return facade.saveManager(manager);
  }
  


  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-contractor-application";
  
  private Manager createManager(DynamicMapBackedForm data)
  {
    Manager manager = new Manager();
    String id = (String)data.getValue("id");
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      manager.setId(null);
    } else {
      manager.setId(new ObjectIdLong(id));
    }
    manager.setName((String)data.getValue("name"));
    manager.setUserName((String)data.getValue("userName"));
    manager.setPassword((String)data.getValue("passwd"));
    manager.setMobileNum((String)data.getValue("mobileno"));
    manager.setEmailAddress((String)data.getValue("emailAddr"));
    manager.setDeptId(new ObjectIdLong((String)data.getValue("depid")));
    if ((String)data.getValue("sectionId") != null)
      manager.setSectionId(new ObjectIdLong((String)data.getValue("sectionId")));
    manager.setIsDepartmentManager("on".equalsIgnoreCase((String)data.getValue("isDeptMgr")));
    KDate dot = null;
    try {
      String sDot = (String)data.getValue("dot");
      dot = KServer.stringToDate(sDot);
    }
    catch (Exception localException) {}
    
    manager.setDot(dot);
    return manager;
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
      String id = "id_new";
      if ((getSelectedId(form) != null) && (request.getAttribute("id") == null)) {
        id = getSelectedId(form);
      } else {
        id = (String)data.getValue("id");
      }
      
      data.setValue("id", id);
      if ((id != null) && (!id.equalsIgnoreCase("id_new"))) {
        setDepartmentUserDataToUI(data);
      } else {
        String depid = (String)data.getValue("depid");
        Manager manager = new Manager(null, "", "", "", "", "", new ObjectIdLong(depid), null, Boolean.valueOf(false), null);
        setValuesToUI(data, manager);
      }
    } catch (Exception e) {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void logError(Exception e) {
    Log.log(1, e, "Error in department user");
  }
  

  private static Map methodsMap = methodsMap();
}
