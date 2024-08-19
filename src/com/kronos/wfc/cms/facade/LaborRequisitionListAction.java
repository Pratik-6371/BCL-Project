package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.LaborReqSchedule;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
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













public class LaborRequisitionListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public LaborRequisitionListAction() {}
  
  protected Map getKeyMethodMap() {
    return methods;
  }
  
  protected static Map methodMap() {
    Map methods = new HashMap();
    methods.put("cms.action.lrlist.new", "doNew");
    methods.put("cms.action.lrlist.view", "doView");
    methods.put("cms.action.lrlist.edit", "doEdit");
    methods.put("cms.action.lrlist.return", "doReturn");
    methods.put("cms.action.lrlist.refresh", "doRefresh");
    methods.put("cms.action.lrlist.delete", "doDelete");
    
    return methods;
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    

    try
    {
      String workorderId = (String)data.getValue("workorderId");
      
      if (workorderId != null) {
        WorkOrderFacade facade = new WorkOrderFacadeImpl();
        Workorder wk = facade.getWorkOrder(workorderId);
        LaborReqFacade lfacade = new LaborReqFacadeImpl();
        
        List<LaborRequisitionPage> lrs = lfacade.retrieveLaborReqPagesByWID(workorderId);
        data.setValue("lrs", convertToMap(lrs, wk));
        data.setValue("cameFrom", "workorderList");
      }
      
    }
    catch (CMSException ex)
    {
      StrutsUtils.addErrorMessage(request, ex);

    }
    catch (Exception e)
    {
      Log.log(e, "Error in Labor Requisition");
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    






    return mapping.findForward("success");
  }
  
  private Map[] convertToMap(List attributes, Workorder wk) {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        LaborRequisitionPage wa = (LaborRequisitionPage)iterator.next();
        props[(i++)] = createPropertyMap(wa, wk);
      }
    }
    return props;
  }
  
  private Map createPropertyMap(LaborRequisitionPage wa, Workorder wk) { Map prop = new HashMap();
    
    prop.put("pageId", wa.getPageId().toString());
    prop.put("pageNum", wa.getPageNum().toString());
    prop.put("pageDispNum", wa.getPageDispNum(wk));
    prop.put("wkNumber", wk.getWkNum());
    prop.put("name", wk.getName());
    prop.put("wkTypDispName", wk.getWkTypDispName());
    prop.put("sectionCode", wk.getSectionCode());
    prop.put("sectionHead", wk.getSectionHead());
    prop.put("contractorName", wk.getContractorName());
    prop.put("contractorCode", wk.getContractorCode());
    prop.put("from", wa.getFromDtm().toString());
    prop.put("to", wa.getToDtm().toString());
    prop.put("approvedSw", getApprovalStatus(wa.getApprovalSw()));
    return prop;
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
    return "Not Approved";
  }
  
  private LaborReqFacade getFacade()
  {
    return new LaborReqFacadeImpl();
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
      
      LaborReqFacade facade = getFacade();
      LaborRequisitionPage lrPage = facade.retrieveLaborReqPageById(lrPageId);
      if ((lrPage != null) && (lrPage.getApprovalSw().intValue() == 1))
        throw CMSException.approvedLRCannotBeDeleted();
      if (((lrPage != null) && (lrPage.getApprovalSw().intValue() == 2)) || 
        (lrPage.getApprovalSw().intValue() == 3)) {
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
  
  private void logError(Exception e) {
    Log.log(e, "Error occured in LaborReqListAction");
  }
  


  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String unitId = (String)((DynamicMapBackedForm)form).getValue("unitId");
    String statusId = (String)((DynamicMapBackedForm)form).getValue("statusId");
    request.setAttribute("unitId", unitId);
    request.setAttribute("statusId", statusId);
    
    return mapping.findForward("doWorkOrderList");
  }
  

  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSLaborReq");
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
  



  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String id = getSelectedId(form);
    
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      
      return doRefresh(mapping, form, request, response);
    }
    
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    data.setValue("selectedId", id);
    
    return mapping.findForward("doCMSLaborReq");
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
