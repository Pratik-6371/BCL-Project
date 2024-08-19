package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSShift;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.LRShift;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderLine;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
















public class ViewLaborRequisitionAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  




  public ViewLaborRequisitionAction() {}
  



  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.laborreq.refresh", "doRefresh");
    methods.put("cms.action.laborreq.return", "doReturn");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  

  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String cameFrom = (String)data.getValue("cameFrom");
    String isSearch = (String)data.getValue("isSearch");
    
    if ("workorderActions".equalsIgnoreCase(cameFrom))
    {
      return mapping.findForward("doCMSLRApproval");
    }
    if ("viewLRHistory".equalsIgnoreCase(cameFrom))
    {
      return mapping.findForward("doViewLRHistory");
    }
    return mapping.findForward("doCMSLaborReqList");
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
  
  protected LaborReqFacade getFacade()
  {
    return new LaborReqFacadeImpl();
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
      getLRToUI(data, request);
    }
    catch (CMSException ex) {
      Log.log(1, ex, "Error occurred in Viewing LR");
      StrutsUtils.addErrorMessage(request, ex);

    }
    catch (Exception e)
    {
      Log.log(1, e, "Error occurred in Viewing LR");
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    
    return mapping.findForward("success");
  }
  

  private void getLRToUI(DynamicMapBackedForm data, HttpServletRequest request)
  {
    String pageId = getSelectedId(data);
    
    LaborReqFacade facade = new LaborReqFacadeImpl();
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    LaborRequisitionPage lrPage = null;
    Workorder wk = null;
    if ((pageId != null) && (!"".equalsIgnoreCase(pageId)) && (!"page_new".equalsIgnoreCase(pageId))) {
      lrPage = facade.retrieveLaborReqPageById(pageId);
      wk = wFacade.getWorkOrder(lrPage.getWorkorderId().toString());
    }
    else {
      String workorderId = (String)data.getValue("workorderId");
      wk = wFacade.getWorkOrder(workorderId);
      lrPage = new LaborRequisitionPage(null, "", null, null, null, new ObjectIdLong(workorderId), null, null, null, null, null, null, Integer.valueOf(0));
    }
    
    setValuesToUI(data, lrPage, wk);
    
    request.setAttribute("workordertyp", wk.getWkTypName());
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, LaborRequisitionPage lrPage, Workorder wk)
  {
    data.setValue("workorderId", wk.getWorkorderId());
    data.setValue("wkNumber", wk.getWkNum());
    data.setValue("unitId", wk.getUnitId());
    data.setValue("unitName", PrincipalEmployee.doRetrieveById(wk.getUnitId()).getUnitName());
    data.setValue("contractorName", Contractor.doRetrieveById(wk.getContractorId(), wk.getUnitId()).getcontractorName());
    data.setValue("vendorCode", Contractor.doRetrieveById(wk.getContractorId(), wk.getUnitId()).getVendorCode());
    data.setValue("workordertype", wk.getWkTypName());
    data.setValue("workorderDispName", wk.getWkTypDispName());
    data.setValue("validFrom", wk.getValidFrom());
    data.setValue("validTo", wk.getValidTo());
    data.setValue("depId", wk.getDepId());
    data.setValue("depCode", Department.doRetrieveById(wk.getDepId()).getDescription());
    data.setValue("costcenter", wk.getCostcenter());
    data.setValue("glcode", wk.getGlcode());
    data.setValue("ownerId", wk.getOwnerId());
    data.setValue("suprId", wk.getSuprid());
    data.setValue("secId", wk.getSecId());
    data.setValue("secCode", wk.getSectionCode());
    data.setValue("name", wk.getName());
    data.setValue("workordertype", wk.getWkTypName());
    data.setValue("lrDispNum", lrPage.getPageDispNum(wk));
    
    data.setValue("tradeList", Trade.doRetrieveAll());
    data.setValue("skillList", Skill.retrieveSkills());
    

    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    List<WorkorderLine> wls = wFacade.getWorkOrderLines(wk.getWorkorderId().toString());
    data.setValue("wlLines", convertToWlMap(wls, wk));
    
    data.setValue("availShifts", CMSShift.retrieveShifts(wk.getUnitId()));
    
    if (lrPage.getPageId() == null) {
      data.setValue("pageId", "page_new");
      data.setValue("approvalStatus", getApprovalStatus(Integer.valueOf(0)));
      data.setValue("fromdtm", KServer.dateToString(KDate.today()));
      data.setValue("todtm", KServer.dateToString(KDate.today().plusDays(7)));
      data.setValue("weeklyOffDays", Integer.valueOf(0));
      
      List<LaborRequisition> lines = createLRLinesFromWKLines(wls);
      Map lrMap = new HashMap();
      lrMap.put("lrLines", convertToMap(lines, wk, Integer.valueOf(0)));
      BaseLRAttributes attributes = new BaseLRAttributes(wk);
      attributes.attributesToDmbf(lrMap, data);

    }
    else
    {
      data.setValue("pageId", lrPage.getPageId().toString());
      data.setValue("approvalStatus", getApprovalStatus(lrPage.getApprovalSw()));
      data.setValue("fromdtm", KServer.dateToString(lrPage.getFromDtm()));
      data.setValue("todtm", KServer.dateToString(lrPage.getToDtm()));
      data.setValue("remark", lrPage.getRemark());
      data.setValue("weeklyOffDays", lrPage.getWeeklyOffDays());
      List<LaborRequisition> lines = lrPage.getLrs();
      Map lrMap = new HashMap();
      lrMap.put("lrLines", convertToMap(lines, wk, lrPage.getWeeklyOffDays()));
      BaseLRAttributes attributes = new BaseLRAttributes(wk);
      attributes.attributesToDmbf(lrMap, data);
    }
  }
  

  private List<LaborRequisition> createLRLinesFromWKLines(List<WorkorderLine> wls)
  {
    List<LaborRequisition> reqs = new ArrayList();
    for (Iterator iterator = wls.iterator(); iterator.hasNext();) {
      WorkorderLine wl = (WorkorderLine)iterator.next();
      LaborRequisition req = new LaborRequisition(null, null, "", KDate.today(), KDate.today(), wl.getWkLineId(), wl.getTradeId(), wl.getSkillId());
      reqs.add(req);
    }
    return reqs;
  }
  
  private Object convertToWlMap(List<WorkorderLine> attributes, Workorder wk)
  {
    Map[] props = new Map[0];
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        WorkorderLine wl = (WorkorderLine)iterator.next();
        props[(i++)] = createWlPropertyMap(wl, wk);
      }
    }
    return props;
  }
  

  private Map createWlPropertyMap(WorkorderLine wl, Workorder wk)
  {
    Map prop = new HashMap();
    
    prop.put("wkLineId", wl.getWkLineId().toString());
    prop.put("itemDesc", wl.getJobDesc());
    String str = wl.getItemDesc();
    String[] arrOfStr = str.split("~", 4);
    String unitId = arrOfStr[0];
    String deptId = arrOfStr[1];
    String secId = arrOfStr[2];
    String ccCode = arrOfStr[3];
    prop.put("unitCode", PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId)).getUnitCode());
    prop.put("depCode", Department.doRetrieveById(new ObjectIdLong(deptId)).getCode());
    prop.put("secCode", Section.retrieveSection(new ObjectIdLong(secId)).getName());
    prop.put("costCenter", ccCode);
    prop.put("serviceCode", wl.getServiceCode());
    prop.put("tradeId", wl.getTradeId());
    prop.put("tradeNm", wl.getTradeNm());
    prop.put("skillId", wl.getSkillId());
    prop.put("skillNm", wl.getSkillNm());
    prop.put("qty", wl.getQty());
    prop.put("qtyCompleted", wl.getQtyCompleted());
    
    return prop;
  }
  

  private Map[] convertToMap(List attributes, Workorder wk, Integer weeklyOffDays)
  {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        LaborRequisition wa = (LaborRequisition)iterator.next();
        props[(i++)] = createPropertyMap(wa, wk, i, weeklyOffDays);
      }
    }
    return props;
  }
  
  private Map createPropertyMap(LaborRequisition lr, Workorder wk, int i, Integer weeklyOffDays) {
    Map prop = new HashMap();
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    WorkorderLine wl = wFacade.getWorkOrderLineById(lr.getWorkOrderlnId().toString());
    LaborReqFacade facade = new LaborReqFacadeImpl();
    HashMap shiftBucket = facade.scheduleQuantityPerShift(lr.getFromdtm(), lr.getTodtm(), lr.getTradeId(), lr.getSkillId(), wk);
    
    prop.put("lrId", lr.getLrId() == null ? "new_property" + i : lr.getLrId().toString());
    prop.put("trade", lr.getTrade() == null ? wl.getTradeNm() : wl.getTradeId() == null ? "" : lr.getTrade().getTradeName());
    prop.put("skill", lr.getSkill() == null ? wl.getSkillNm() : wl.getSkillId() == null ? "" : lr.getSkill().getSkillNm());
    prop.put("tradeId", lr.getTrade() == null ? wl.getTradeId().toString() : wl.getTradeId() == null ? "" : lr.getTradeId().toString());
    prop.put("skillId", lr.getSkill() == null ? wl.getSkillId().toString() : wl.getSkillId() == null ? "" : lr.getSkillId().toString());
    prop.put("serviceCode", wl.getServiceCode());
    prop.put("itemDesc", wl.getJobDesc());
    
    String str = wl.getItemDesc();
    String[] arrOfStr = str.split("~", 4);
    String unitId = arrOfStr[0];
    String deptId = arrOfStr[1];
    String secId = arrOfStr[2];
    String ccCode = arrOfStr[3];
    prop.put("unitCode", PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId)).getUnitCode());
    prop.put("depCode", Department.doRetrieveById(new ObjectIdLong(deptId)).getCode());
    prop.put("secCode", Section.retrieveSection(new ObjectIdLong(secId)).getName());
    prop.put("costCenter", ccCode);
    prop.put("wlId", wl.getWkLineId().toString());
    
    if ("A".equalsIgnoreCase(wk.getWkTypName())) {
      prop.put("balanceQty", lr.calculateBalanceQty(wk, weeklyOffDays));
    }
    

    String shiftA = (String)CMSShift.map.get("shift_A");
    String shiftB = (String)CMSShift.map.get("shift_B");
    String shiftC = (String)CMSShift.map.get("shift_C");
    String shiftG = (String)CMSShift.map.get("shift_G");
    List<CMSShift> shifts = CMSShift.retrieveShifts(wk.getUnitId());
    for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
      CMSShift cmsShift = (CMSShift)iterator.next();
      if (cmsShift.getShiftNm().equalsIgnoreCase(shiftA))
      {
        prop.put("shift_A_id", cmsShift.getShiftId().toString());
        Long schedQty = (Long)shiftBucket.get(cmsShift.getShiftId());
        if (schedQty == null) {
          schedQty = new Long(0L);
        }
        prop.put("sched_A_qty", schedQty.toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftB))
      {
        prop.put("shift_B_id", cmsShift.getShiftId().toString());
        Long schedQty = (Long)shiftBucket.get(cmsShift.getShiftId());
        if (schedQty == null) {
          schedQty = new Long(0L);
        }
        prop.put("sched_B_qty", schedQty.toString());

      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftC))
      {
        prop.put("shift_C_id", cmsShift.getShiftId().toString());
        Long schedQty = (Long)shiftBucket.get(cmsShift.getShiftId());
        if (schedQty == null) {
          schedQty = new Long(0L);
        }
        prop.put("sched_C_qty", schedQty.toString());

      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftG))
      {
        prop.put("shift_G_id", cmsShift.getShiftId().toString());
        Long schedQty = (Long)shiftBucket.get(cmsShift.getShiftId());
        if (schedQty == null) {
          schedQty = new Long(0L);
        }
        prop.put("sched_G_qty", schedQty.toString());
      }
    }
    


    List<LRShift> lrshifts = lr.getLrshifts();
    if ((lrshifts != null) && (!lrshifts.isEmpty()))
    {
      for (Iterator iterator = lrshifts.iterator(); iterator.hasNext();) {
        LRShift lrShift = (LRShift)iterator.next();
        String shiftName = getShiftName(lrShift.getShiftId(), shifts);
        if (shiftName.equalsIgnoreCase(shiftA)) {
          prop.put("shift_A", lrShift.getQty());

        }
        else if (shiftName.equalsIgnoreCase(shiftB)) {
          prop.put("shift_B", lrShift.getQty());

        }
        else if (shiftName.equalsIgnoreCase(shiftC)) {
          prop.put("shift_C", lrShift.getQty());

        }
        else if (shiftName.equalsIgnoreCase(shiftG)) {
          prop.put("shift_G", lrShift.getQty());
        }
      }
    }
    return prop;
  }
  





  private String getShiftName(ObjectIdLong shiftId, List<CMSShift> shifts)
  {
    for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
      CMSShift cmsShift = (CMSShift)iterator.next();
      if (cmsShift.getShiftId().longValue() == shiftId.longValue()) {
        return cmsShift.getShiftNm();
      }
    }
    return "";
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
    Log.log(e, "Error occured in saving LR");
  }
}
