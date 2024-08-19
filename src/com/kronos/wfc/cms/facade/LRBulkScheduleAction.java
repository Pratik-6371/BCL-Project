package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.laborlevel.business.set.LaborAccountSet;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.AccessAssignment;
import com.kronos.wfc.commonapp.people.business.person.CustomData;
import com.kronos.wfc.commonapp.people.business.person.CustomDataSet;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.datetime.framework.KServerException;
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

public class LRBulkScheduleAction extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  




  public LRBulkScheduleAction() {}
  




  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.laborreq.refresh", "doRefresh");
    methods.put("cms.action.laborreq.schedule", "doSchedule");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  


  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = CMSException.noSelectionMade();
    
    StrutsUtils.addErrorMessage(request, e);
  }
  
  public ActionForward doSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String pageId = getSelectedId(form);
      if (pageId == null)
      {
        setNoSelectionErrorMessage(request);
        
        return doRefresh(mapping, form, request, response);
      }
      
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lr = facade.retrieveLaborReqPageById(pageId);
      validate(lr);
      
      data.setValue("pageId", pageId);
      return mapping.findForward("doCMSLRSchedule");
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
    return mapping.findForward("success");
  }
  


  private void validate(LaborRequisitionPage lr)
  {
    if ((lr != null) && (lr.getApprovalSw().intValue() != 1)) {
      throw CMSException.lrNotApprovedYet();
    }
    
    Workorder wk = Workorder.retrieveByWorkOrder(lr.getWorkorderId().toString());
    Contractor contr = wk.getContractor();
    if (KDate.today().isAfter(contr.getLicensevalidity2())) {
      throw CMSException.contractorLicenseValidityExpired();
    }
  }
  

  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  




  public ActionForward doAttendance(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      return mapping.findForward("doCMSAttendanceApproval");
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  



  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Personality p = CurrentUserAccountManager.getPersonality();
    
    try
    {
      String fromDtm = (String)data.getValue("fromDtm1");
      String toDtm = (String)data.getValue("toDtm1");
      String wkNum = (String)data.getValue("wkNumber");
      if ((fromDtm != null) && (toDtm != null) && (!"".equalsIgnoreCase(fromDtm)) && (!"".equalsIgnoreCase(toDtm)) && (wkNum != null))
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
        
        Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
        
        ArrayList cCodes = (ArrayList)lmap.get(Integer.valueOf(5));
        

        CustomData d = p.getCustomData().getCustomData("Is Admin");
        
        List<LaborRequisitionPage> reqs = new ArrayList();
        if (((d != null) && (d.getCustomText() != null) && (d.getCustomText().equalsIgnoreCase("Yes"))) || 
          (CurrentUserAccountManager.getUserAccountId().longValue() < 0L)) {
          reqs = lrFacade.retrieveLaborReqPagesByWIDAndContCode(order.getWorkorderId().toString(), Integer.valueOf(1), KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm));
        }
        else {
          reqs = lrFacade.retrieveLaborReqPagesByWIDAndContCode(order.getWorkorderId().toString(), Integer.valueOf(1), KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm), cCodes);
        }
        

        if ((reqs == null) || (reqs.isEmpty())) {
          throw CMSException.unableTofindAnyRecords();
        }
        

        setValuesToUI(data, reqs, order);
      }
      else {
        data.setValue("fromDtm1", "");
        data.setValue("toDtm1", "");
        if (CurrentUserAccountManager.getUserAccountId().longValue() > 0L) {
          Hashtable lmap = p.getAccessAssignment().getManagerAccessSet().getLaborLevelEntriesMap();
          ArrayList cCodes = (ArrayList)lmap.get(Integer.valueOf(5));
          PrimaryLaborAccount la = CurrentUserAccountManager.getPersonality().getJobAssignment().getCurrentPrimaryLaborAccount();
          String[] laNames = la.getLaborAccount().getLaborLevelEntryNames_optimized();
          PrincipalEmployee employee = PrincipalEmployee.doRetrieveByCode(laNames[1]);
          LaborReqFacade lrFacade = new LaborReqFacadeImpl();
          CustomData d = p.getCustomData().getCustomData("Is Admin");
          List<LaborRequisitionPage> reqs = new ArrayList();
          if ((d.getCustomText() != null) && (d.getCustomText().equalsIgnoreCase("Yes"))) {
            reqs = lrFacade.retrieveLaborReqPagesForContractor(employee.getUnitId(), 1);
          }
          else {
            reqs = lrFacade.retrieveLaborReqPagesForContractor(employee.getUnitId(), 1, cCodes);
          }
          
          setValuesToUI(data, reqs, null);
        }
      }
    }
    catch (CMSException cmse) {
      StrutsUtils.addErrorMessage(request, cmse);
    } catch (KServerException kse) {
      StrutsUtils.addErrorMessage(request, kse);
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  


  private List<LaborRequisitionPage> filterLRs(ArrayList cCodes, List<LaborRequisitionPage> reqs)
  {
    List<LaborRequisitionPage> pages = new ArrayList();
    if ((reqs != null) && (!reqs.isEmpty())) { 
    	//Iterator iterator2;
      for (Iterator iterator = reqs.iterator(); iterator.hasNext(); )
          
         // iterator2.hasNext())
      {
        LaborRequisitionPage req = (LaborRequisitionPage)iterator.next();
        Workorder wk = new CMSService().retrieveWorkorderByPageId(req.getPageId().toString());
        for (Iterator iterator2 = cCodes.iterator(); iterator2.hasNext();) {
			String code = (String) iterator2.next();
			if(code.equalsIgnoreCase(wk.getContractorCode())) {
				pages.add(req);
			}
		}
      }
    }
    
    return pages;
  }

  private void setValuesToUI(DynamicMapBackedForm data, List<LaborRequisitionPage> lrs, Workorder wk)
  {
    Map[] lrRows = convertToMap(lrs);
    
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
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  


  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String tradeid = (String)form.getValue("selectedId");
    return tradeid;
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
  

  private Workorder getSavedContFromUI(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String workorderId = (String)data.getValue("workorderId");
    Workorder order = Workorder.retrieveByWorkOrder(workorderId);
    String ownerId = (String)data.getValue("ownerId");
    String suprId = (String)data.getValue("suprId");
    order.setOwnerId(new ObjectIdLong(ownerId));
    order.setSuprid(new ObjectIdLong(suprId));
    
    return order;
  }
  
  private String getApprovalStatus(Integer approvalSW)
  {
    if (1 == approvalSW.intValue()) {
      return "Approved";
    }
    if (2 == approvalSW.intValue()) {
      return "Rejected";
    }
    return "UNApproved";
  }
  



  private void logError(Exception e)
  {
    Log.log(e, "Error ocuured in Workorder Actions");
  }
}
