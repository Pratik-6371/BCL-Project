package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.logging.framework.LogContext;
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


public class HealthInformationWorkmenListAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  public HealthInformationWorkmenListAction() {}
  
  protected static Map methodsMap() {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Personality personality = CurrentUserAccountManager.getPersonality();
    try
    {
      String isReqFromListPage = (String)request.getAttribute("EditFromListPage");
      
      if (isReqFromListPage != null) { List<PrincipalEmployee> pes;
        return doRefresh(mapping, form, request, response);
      }
      
      String id = getSelectedId(form);
      
      if (id == null)
      {
        CMSException e = new CMSException(1);
        StrutsUtils.addErrorMessage(request, e);
        List<PrincipalEmployee> pes; return doRefresh(mapping, form, request, response);
      }
      
      request.setAttribute("id", id);
      List<PrincipalEmployee> pes; return mapping.findForward("doCMSHealthInformation");
    }
    catch (Exception e)
    {
      ActionForward localActionForward;
      
      logError(e);
      List<PrincipalEmployee> pes; return mapping.findForward("success");
    }
    finally {
      List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
      data.setValue("availUnitNames", pes);
    }
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, String empCode, String empName) {
    String unitId = (String)data.getValue("unitId");
    Map[] users = getWorkmenMap(empCode, empName, unitId);
    data.setValue("cmsWorkmen", users);
  }
  
  private Map[] getWorkmenMap(String empCode, String empName, String unitId) {
    List<Workmen> workmen = new ArrayList();
    
    if ((empCode != null) && (!"".equalsIgnoreCase(empCode)) && ("".equalsIgnoreCase(empName)) && (unitId != null)) {
      workmen = getFacade().getWorkmenListByEmpCode(empCode, unitId);
    }
    else if (("".equalsIgnoreCase(empCode)) && (empName != null) && (!"".equalsIgnoreCase(empName)) && (unitId != null)) {
      workmen = getFacade().getWorkmenListByEmpName(empName, unitId, Integer.toString(Workmen.ACTIVE_STATUS.intValue()));

    }
    else if ((unitId != null) && (!"".equalsIgnoreCase(unitId))) {
      workmen = getFacade().getWorkmenList(new ObjectIdLong(unitId));
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
  

  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  



  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    //List<PrincipalEmployee> pes;
    try {
      String empCode = (String)data.getValue("empcode");
      String empName = (String)data.getValue("empname");
      String unitId = (String)data.getValue("unitId");
      
      if ((unitId != null) && (!"".equalsIgnoreCase(unitId)) && (!"-1".equalsIgnoreCase(unitId))) {
        if ((empCode == null) && (empName == null)) {
          throw new CMSException(26);
        }
        setValuesToUI(data, empCode, empName);
      }
      request.setAttribute("unitId", unitId);
    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    }
    catch (Exception e) {
      //List<PrincipalEmployee> pes;
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    finally {
    //  List<PrincipalEmployee> pes;
      List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
      data.setValue("availUnitNames", pes);
    }
    return mapping.findForward("success");
  }
  
  private void logError(Exception e) {
    Log.log(e, "error occured in work men action");
  }
}
