package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.commonapp.laborlevel.business.set.LaborAccountSet;
import com.kronos.wfc.commonapp.people.business.person.AccessAssignment;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;






public class WorkOrderListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public WorkOrderListAction() {}
  
  protected Map getKeyMethodMap() {
    return methods;
  }
  
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
    Personality personality = CurrentUserAccountManager.getPersonality();
   // List<PrincipalEmployee> pes;
    try
    {
      String unitId = (String)data.getValue("unitId");
      String statusId = (String)data.getValue("statusId");
      String wkNum = (String)data.getValue("wkNum");
      String secCode = (String)data.getValue("sectionCode");
      
      if (unitId != null) {
        Hashtable lmap = personality.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
        ArrayList lles = (ArrayList)lmap.get(Integer.valueOf(3));
        ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
        List<Workorder> wks = new ArrayList();
        if (((wkNum == null) || ("".equalsIgnoreCase(wkNum))) && (
          (secCode == null) || ("".equalsIgnoreCase(secCode)))) {
          wks = Workorder.retrieveWorkOrder(unitId, statusId, lles, sse);
        }
        else {
          if ((wkNum != null) && (!"".equalsIgnoreCase(wkNum))) {
            Workorder od = Workorder.retrieveWorkOrderByNum(wkNum, statusId, unitId, lles, sse);
            if (od != null) {
              wks.add(od);
            }
          }
          else if ((secCode != null) && (!"".equalsIgnoreCase(secCode))) {
            wks = Workorder.retrieveWorkOrderBySecCode(secCode, statusId, unitId, lles, sse);
          }
          if ((wks != null) && (wks.isEmpty())) {
            Message msg = new Message("cms.label.noworkordersfound");
            StrutsUtils.addWarningMessage(request, msg);
          }
        }
        

        data.setValue("workorders", wks);
        request.setAttribute("unitId", unitId);
        request.setAttribute("statusId", statusId);
        request.setAttribute("wkNum", wkNum);
        request.setAttribute("sectionCode", secCode);

      }
      

    }
    catch (Exception e)
    {

      Log.log(e, "Error occured in bringing back workorders");
    }
    finally
    {
      //List<PrincipalEmployee> pes;
      List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
      data.setValue("availUnitNames", pes);
      data.setValue("statusList", getStatusList());
    }
    return mapping.findForward("success");
  }
  





  private HashMap getStatusList()
  {
    return Workorder.getStatusList();
  }
  

  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String id = getSelectedId(form);
    
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      
      return doRefresh(mapping, form, request, response);
    }
    

    return mapping.findForward("doWorkOrder");
  }
  




  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    
    StrutsUtils.addErrorMessage(request, e);
  }
  



  private String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    String[] ids = (String[])data.getValue("selectedIds");
    
    String id = null;
    
    if ((ids != null) && (ids[0] != null))
    {
      id = ids[0];
    }
    return id;
  }
}
