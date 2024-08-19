package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
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


public class WorkmenListAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  public WorkmenListAction() {}
  
  protected static Map methodsMap() { Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.new", "doNew");
    methods.put("cms.action.delete", "doDelete");
    methods.put("cms.action.return", "doReturn");
    methods.put("cms.action.edit", "doEdit");
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
      data.setValue("id", "id_new");
    }
    catch (Exception e) {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("doCMSWorkmenDetailApplication");
  }
  
  public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try
    {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      
      String id = getSelectedId(form);
      String unitId = (String)data.getValue("unitId");
      if (id == null)
      {
        CMSException e = new CMSException(1);
        StrutsUtils.addErrorMessage(request, e);
        return doRefresh(mapping, form, request, response);
      }
      
      WorkmenFacade facade = getFacade();
      String workmenId = getSelectedId(form);
      facade.deleteWorkmen(workmenId, unitId);
      
      return mapping.findForward("success");
    }
    catch (Exception e)
    {
      logError(e);
      throw e;
    }
  }
  

  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setAttribute("unitId", (String)((DynamicMapBackedForm)form).getValue("unitId"));
    return mapping.findForward("doCMSContractorWListApplication");
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String isReqFromListPage = (String)request.getAttribute("EditFromListPage");
      if (isReqFromListPage != null) {
        return doRefresh(mapping, form, request, response);
      }
      
      String id = getSelectedId(form);
      String statusId = (String)data.getValue("statusId");
      if (statusId == null) {
        statusId = (String)request.getAttribute("statusId");
        if (statusId == null) {
          statusId = Integer.toString(Workmen.ACTIVE_STATUS.intValue());
        }
      }
      request.setAttribute("statusId", statusId);
      if (id == null)
      {
        CMSException e = new CMSException(1);
        StrutsUtils.addErrorMessage(request, e);
        return doRefresh(mapping, form, request, response);
      }
      if (Integer.toString(Workmen.TERMINATED_STATUS.intValue()).equals(statusId)) {
    	  request.setAttribute("status", "Terminated");
        return mapping.findForward("doTerminateWorkmenDetail");
      }
      
      if(Integer.toString(Workmen.ACTIVE_STATUS.intValue()).equalsIgnoreCase(statusId))
      {
    	  request.setAttribute("status", "Active");
      }
      else if(Integer.toString(Workmen.TERMINATED_STATUS.intValue()).equalsIgnoreCase(statusId))
      {
    	  request.setAttribute("status", "Terminated");
      }
      else
      {
    	  request.setAttribute("status", "Inactive");
      }
      return mapping.findForward("doCMSWorkmenDetailApplication");

    }
    catch (Exception e)
    {

      logError(e); }
    return mapping.findForward("success");
  }
  


  private void setValuesToUI(DynamicMapBackedForm data, String unitId, Contractor cont, String empCode, String empName, String statusId)
  {
    data.setValue("contrId", cont.getcontractorid());
    data.setValue("name", cont.getcontractorName());
    data.setValue("vendorCode", cont.getVendorCode());
    data.setValue("unitId", cont.getUnitId());
    data.setValue("statusId", statusId);
    Map[] users = getWorkmenMap(cont, empCode, empName, unitId, statusId);
    data.setValue("cmsWorkmen", users);
  }
  
  private Map[] getWorkmenMap(Contractor cont, String empCode, String empName, String unitId, String statusId) {
    List<Workmen> workmen = new ArrayList();
    if (("".equalsIgnoreCase(empCode)) && ("".equalsIgnoreCase(empName))) {
      workmen = getFacade().getWorkmenList(cont.getcontractorid().toString(), new ObjectIdLong(unitId), statusId);
    }
    else if ((empCode != null) && ("".equalsIgnoreCase(empName))) {
      workmen = getFacade().getWorkmenListByEmpCode(cont.getcontractorid().toString(), empCode, unitId, statusId);
    }
    else if (("".equalsIgnoreCase(empCode)) && (empName != null)) {
      empName = empName.replace("*", "%");
      workmen = getFacade().getWorkmenListByEmpName(cont.getcontractorid().toString(), empName, unitId, statusId);
    }
    else {
      workmen = getFacade().getWorkmenList(cont.getcontractorid().toString(), new ObjectIdLong(unitId), statusId);
    }
    
    ArrayList<Map> ws = new ArrayList();
    
    PrincipalEmployee e = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
    

    for (Iterator iterator = workmen.iterator(); iterator.hasNext();) {
      Workmen workman = (Workmen)iterator.next();
      boolean isIncluded = false;
      try
      {
        Person p = workman.getPerson();
        Personality per = Personality.getByPersonId(p.getPersonId());
        PrimaryLaborAccount pla = per.getJobAssignment().getCurrentPrimaryLaborAccount();
        LaborAccount acct = pla.getLaborAccount();
        String[] names = acct.getLaborLevelEntryNames_optimized();
        for (int j = 0; j < names.length; j++) {
          String name = names[j];
          if (name.equalsIgnoreCase(e.getUnitCode())) {
            isIncluded = true;
          }
        }
      }
      catch (Exception e1) {
        Log.log(e1, "Error occurred in filtering workmen per unit");
        isIncluded = true;
      }
      if (isIncluded) {
        Map mgr = createWorkmenMap(workman);
        ws.add(mgr);
      }
    }
    Map[] props = new Map[ws.size()];
    int i = 0;
    for (Iterator iterator = ws.iterator(); iterator.hasNext();) {
      Map map = (Map)iterator.next();
      props[(i++)] = map;
    }
    return props;
  }
  
  private Map createWorkmenMap(Workmen workman)
  {
    Map property = new HashMap();
    property.put("id", workman.getEmpId().toString());
    property.put("eCode", workman.getEmpCode());
    property.put("firstname", workman.getFirstName());
    property.put("lastname", workman.getLastName());
    return property;
  }
  
  protected Map createNewManagerRow()
  {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("eCode", "");
    property.put("firstname", "");
    property.put("lastname", "");
    return property;
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
  
  protected WorkmenFacade getFacade()
  {
    return new WorkmenFacadeImpl();
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String id = getSelectedId(form);
      String empCode = (String)data.getValue("empcode");
      String empName = (String)data.getValue("empname");
      if (id == null)
      {
        id = (String)data.getValue("contrId");
        if (id == null)
          id = (String)request.getAttribute("contrId");
      }
      String unitId = (String)data.getValue("unitId");
      if (unitId == null)
        unitId = (String)request.getAttribute("unitId");
      String statusId = (String)data.getValue("statusId");
      if (statusId == null)
      {
        statusId = (String)request.getAttribute("statusId");
        if (statusId == null)
          statusId = Integer.toString(Workmen.ACTIVE_STATUS.intValue());
      }
      request.setAttribute("statusId", statusId);
      data.setValue("availStatus", getAllStatus());
      
      Contractor cont = Contractor.doRetrieveById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      setValuesToUI(data, unitId, cont, empCode, empName, statusId);
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private Map getAllStatus() {
    HashMap map = new HashMap();
    map.put(Workmen.ACTIVE_STATUS, "Active");
    map.put(Workmen.INACTIVE_STATUS, "Inactive");
    map.put(Workmen.TERMINATED_STATUS, "Terminated");
    return map;
  }
  
  private void logError(Exception e) {
    Log.log(e, "error occured in work men action");
  }
}
