package com.kronos.wfc.cms.facade;

import com.kronos.wfc.business.datetime.KServer;
import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




public class GetWorkmenScheduleAction
  extends WFPLookupDispatchActions
{
  public GetWorkmenScheduleAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.workmenschedule.return", "doReturn");
    return methods;
  }
  
  protected Map getKeyMethodMap() {
    return methodsMap();
  }
  
  String getSelectedId(ActionForm form) {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  
  void setNoSelectionErrorMessage(HttpServletRequest request) {
    CMSException e = new CMSException(1);
    StrutsUtils.addErrorMessage(request, e);
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
  
  private void logError(Exception e) {
    Log.log(e, "Error ocuured in workmen schedule form");
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    try
    {
      String fromDtm = (String)data.getValue("fromdtm");
      String toDtm = (String)data.getValue("todtm");
      String[] ids = (String[])data.getValue("selectedIds");
      
      StringBuilder str = new StringBuilder();
      for (int i = 0; i < ids.length; i++) {
        str.append(ids[i]);
        if (i + 1 < ids.length)
          str.append(",");
      }
      KDate startDate = KServer.stringToDate(fromDtm);
      KDate endDate = KServer.stringToDate(toDtm);
      String scheduleString = new CMSService().getEmployeesSchedule(startDate, endDate, str.toString());
      request.setAttribute("WorkmenSchedule", scheduleString);
    }
    catch (CMSException ex)
    {
      log(1, ex.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
    } catch (Exception e) {
      Log.log(e, e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    return mapping.findForward("success");
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      String pageId = (String)data.getValue("pageId");
      if (pageId == null) {
        setNoSelectionErrorMessage(request);
        
        return doRefresh(mapping, form, request, response);
      }
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lr = facade.retrieveLaborReqPageById(pageId);
      validate(lr);
      
      data.setValue("pageId", pageId);
      return mapping.findForward("doCMSLRSchedule");
    }
    catch (CMSException ex) {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    } catch (Exception e) {
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    return mapping.findForward("success");
  }
}
