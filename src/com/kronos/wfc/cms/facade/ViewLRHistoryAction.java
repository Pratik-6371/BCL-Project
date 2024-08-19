package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.LaborReqSchedule;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.laborlevel.business.set.LaborAccountSet;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.AccessAssignment;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;









public class ViewLRHistoryAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  




  public ViewLRHistoryAction() {}
  




  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.laborreq.refresh", "doRefresh");
    methods.put("cms.action.laborreq.view.lr", "doView");
    methods.put("cms.action.lrlist.delete", "doDelete");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  


  public ActionForward doView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String id = getSelectedId(form);
    
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      
      return doRefresh(mapping, form, request, response);
    }
    

    return mapping.findForward("doViewLR");
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
  

  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    
    StrutsUtils.addErrorMessage(request, e);
  }
  

  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  public ActionForward doDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String lrPageId = getSelectedId(form);
      if (lrPageId == null)
      {
        setNoSelectionErrorMessage(request);
        
        return doRefresh(mapping, form, request, response);
      }
      
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lrPage = facade.retrieveLaborReqPageById(lrPageId);
      if ((lrPage != null) && (lrPage.getApprovalSw().intValue() == 1))
        throw CMSException.approvedLRCannotBeDeleted();
      if ((lrPage != null) && (lrPage.getApprovalSw().intValue() == 2)) {
        Iterator iterator = lrPage.getLrs().iterator();
        while (iterator.hasNext()) {
          LaborRequisition lr = (LaborRequisition)iterator.next();
          List<LaborReqSchedule> scheds = LaborReqSchedule.retrieveSchedulesByLRID(lr.getLrId().toString());
          if ((scheds != null) && (!scheds.isEmpty())) {
            throw CMSException.lrCannotBeDeleted();
          }
        }
      }
      
      lrPage.doDelete();
    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    }
    catch (Exception e) {
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    return doRefresh(mapping, form, request, response);
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    

    try
    {
      String fromDtm = (String)data.getValue("fromDtm");
      String toDtm = (String)data.getValue("toDtm");
      String wkNum = (String)data.getValue("wkNum");
      String secCode = (String)data.getValue("sectionCode");
      String statusId = (String)data.getValue("statusId");
      request.setAttribute("statusId", statusId);
      data.setValue("availStatus", getAllStatus());
      if ((fromDtm != null) && (toDtm != null) && (!"".equalsIgnoreCase(fromDtm)) && (!"".equalsIgnoreCase(toDtm)) && 
        (wkNum != null) && (!"".equalsIgnoreCase(wkNum)))
      {
        if (KServer.stringToDate(fromDtm).isAfter(KServer.stringToDate(toDtm))) {
          throw CMSException.fromDateBeforeExistingRecord(fromDtm);
        }
        

        WorkOrderFacade facade = new WorkOrderFacadeImpl();
        Workorder order = facade.getWorkOrderByNum(wkNum);
        
        if (order == null) {
          throw CMSException.unableTofindAnyRecords();
        }
        
        LaborReqFacade lrFacade = new LaborReqFacadeImpl();
        Personality p = CurrentUserAccountManager.getPersonality();
        Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
        
        ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
        
        List<LaborRequisitionPage> reqs = lrFacade.retrieveLaborReqPagesByWID(order.getWorkorderId().toString(), 
          KServer.stringToDate(fromDtm), 
          KServer.stringToDate(toDtm), 
          sse, statusId, secCode);
        
        if ((reqs == null) || (reqs.isEmpty())) {
          throw CMSException.unableTofindAnyRecords();
        }
        
        setValuesToUI(data, reqs, order);

      }
      else if ((fromDtm != null) && (toDtm != null) && (!"".equalsIgnoreCase(fromDtm)) && (!"".equalsIgnoreCase(toDtm)) && (
        (wkNum == null) || ("".equalsIgnoreCase(wkNum))))
      {
        if (CurrentUserAccountManager.getUserAccountId().longValue() > 0L) {
          if (KServer.stringToDate(fromDtm).isAfter(KServer.stringToDate(toDtm))) {
            throw CMSException.fromDateBeforeExistingRecord(fromDtm);
          }
          LaborReqFacade lrFacade = new LaborReqFacadeImpl();
          Personality p = CurrentUserAccountManager.getPersonality();
          Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
          
          ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
          PrimaryLaborAccount la = p.getJobAssignment().getCurrentPrimaryLaborAccount();
          String[] laNames = la.getLaborAccount().getLaborLevelEntryNames_optimized();
          PrincipalEmployee employee = PrincipalEmployee.doRetrieveByCode(laNames[1]);
          
          List<LaborRequisitionPage> reqs = lrFacade.retrieveAllLRPagesByDateAndSections((ObjectIdLong)employee.getUnitId(), KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm), sse, statusId, secCode);
          
          if ((reqs == null) || (reqs.isEmpty())) {
            throw CMSException.unableTofindAnyRecords();
          }
          setValuesToUI(data, reqs, null);
        }
      }
      else if (((fromDtm == null) && (toDtm == null)) || (("".equalsIgnoreCase(fromDtm)) && ("".equalsIgnoreCase(toDtm)) && (wkNum != null) && (!"".equalsIgnoreCase(wkNum))))
      {

        WorkOrderFacade facade = new WorkOrderFacadeImpl();
        Workorder order = facade.getWorkOrderByNum(wkNum);
        
        if (order == null) {
          throw CMSException.unableTofindAnyRecords();
        }
        
        LaborReqFacade lrFacade = new LaborReqFacadeImpl();
        Personality p = CurrentUserAccountManager.getPersonality();
        Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
        
        ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
        
        List<LaborRequisitionPage> reqs = lrFacade.retrieveLaborReqPagesByWIDWithAccess(order.getWorkorderId().toString(), sse, statusId, secCode);
        
        if ((reqs == null) || (reqs.isEmpty())) {
          throw CMSException.unableTofindAnyRecords();
        }
        setValuesToUI(data, reqs, order);
      }
      else if (((fromDtm == null) && (toDtm == null)) || (("".equalsIgnoreCase(fromDtm)) && ("".equalsIgnoreCase(toDtm)) && 
        ((wkNum == null) || ("".equalsIgnoreCase(wkNum))) && 
        (secCode != null) && (!"".equalsIgnoreCase(secCode))))
      {

        LaborReqFacade lrFacade = new LaborReqFacadeImpl();
        Personality p = CurrentUserAccountManager.getPersonality();
        Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
        
        ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
        
        List<LaborRequisitionPage> reqs = lrFacade.retrieveLaborReqPagesByWIDWithAccess(null, sse, statusId, secCode);
        
        if ((reqs == null) || (reqs.isEmpty())) {
          throw CMSException.unableTofindAnyRecords();
        }
        setValuesToUI(data, reqs, null);
      }
      else
      {
        data.setValue("fromDtm", "");
        data.setValue("toDtm", "");
        
        if (CurrentUserAccountManager.getUserAccountId().longValue() > 0L) {
          LaborReqFacade lrFacade = new LaborReqFacadeImpl();
          Personality p = CurrentUserAccountManager.getPersonality();
          Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
          ArrayList sse = (ArrayList)lmap.get(Integer.valueOf(4));
          PrimaryLaborAccount la = p.getJobAssignment().getCurrentPrimaryLaborAccount();
          String[] laNames = la.getLaborAccount().getLaborLevelEntryNames_optimized();
          PrincipalEmployee employee = PrincipalEmployee.doRetrieveByCode(laNames[1]);
          if (statusId == null) {
            statusId = String.valueOf(0);
          }
          List<LaborRequisitionPage> reqs = lrFacade.retrieveAllLRPagesBySections(employee.getUnitId(), sse, statusId);
          
          setValuesToUI(data, reqs, null);
        }
        
      }
      
    }
    catch (Exception e)
    {
      logError(e);
     // CMSException ex;
      CMSException ex; if ((e instanceof CMSException)) {
        ex = (CMSException)e;
      }
      else {
        ex = CMSException.unknown(e.getLocalizedMessage());
      }
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  


  private Map getAllStatus()
  {
    return LaborRequisitionPage.getStatusMap();
  }
  


  private void setValuesToUI(DynamicMapBackedForm data, List<LaborRequisitionPage> lrPages, Workorder wk)
  {
    data.setValue("cameFrom", "viewLRHistory");
    















    Map[] lrRows = convertToMap(lrPages);
    
    data.setValue("lrRows", lrRows);
  }
  
  private Map[] convertToMap(List attributes)
  {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        LaborRequisitionPage wa = (LaborRequisitionPage)iterator.next();
        props[(i++)] = createPropertyMap(wa);
      }
    }
    return props;
  }
  

  private Map createPropertyMap(LaborRequisitionPage lr)
  {
    Map prop = new HashMap();
    
    Workorder wk = Workorder.retrieveByWorkOrder(lr.getWorkorderId().toString());
    
    prop.put("pageId", lr.getPageId().toString());
    prop.put("pageNum", lr.getPageNum().toString());
    prop.put("from", lr.getFromDtm().toString());
    prop.put("to", lr.getToDtm().toString());
    prop.put("approvedSw", getApprovalStatus(lr.getApprovalSw()));
    prop.put("contractorName", wk.getContractorName());
    prop.put("contractorCode", wk.getContractorCode());
    prop.put("sectionHead", wk.getSectionHead());
    prop.put("sectionCode", wk.getSectionCode());
    prop.put("wkNum", wk.getWkNum());
    prop.put("name", wk.getName());
    prop.put("wkTypName", wk.getWkTypDispName());
    prop.put("pageDispNum", lr.getPageDispNum(wk));
    
    return prop;
  }
  

  protected WorkOrderFacade getFacade()
  {
    return new WorkOrderFacadeImpl();
  }
  
  String[] getSelectedIds(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    return ids;
  }
  
  private String getApprovalStatus(Integer approvalSW)
  {
    if (1 == approvalSW.intValue()) {
      return "Approved";
    }
    if (2 == approvalSW.intValue()) {
      return "Rejected";
    }
    if (3 == approvalSW.intValue()) {
      return "Expired";
    }
    return "UNApproved";
  }
  



  private void logError(Exception e)
  {
    Log.log(e, "Error ocuured in Workorder Actions");
  }
}
