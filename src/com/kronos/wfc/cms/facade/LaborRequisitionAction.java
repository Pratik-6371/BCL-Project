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
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KCalendar;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;






public class LaborRequisitionAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  


  public LaborRequisitionAction() {}
  

  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.laborreq.refresh", "doRefresh");
    methods.put("cms.action.laborreq.save", "doSave");
    methods.put("cms.action.laborreq.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.laborreq.return", "doReturn");
    methods.put("cms.action.laborreq.new", "doNew");
    methods.put("cms.action.edit", "doEdit");
    methods.put("insertRow", "doInsertPropertyRow");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    try
    {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      
      String workorderId = (String)data.getValue("workorderId");
      
      WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
      Workorder wk = wFacade.getWorkOrder(workorderId);
      
      validateFields(form, wk.getValidFrom(), wk.getValidTo());
      request.setAttribute("workordertyp", wk.getWkTypName());
      LaborRequisitionPage lr = saveLR(form, wk);
      setValuesToUI(data, lr, wk);
      clearUiDirtyData((DynamicMapBackedForm)form);
      
      Map x = new HashMap();
      x.put("lrNum", lr.getPageDispNum(wk));
      StrutsUtils.addMessage(request, new Message("cms.lr.saved.successfully", x));
    }
    catch (CMSException ex)
    {
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);

    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.errorNullInput();
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);
    }
    return mapping.findForward("success");
  }
  

  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String rowId = (String)form.getValue("selectedId");
    return rowId;
  }
  
  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  
  private void setErrorMessagesFromExceptions(HttpServletRequest request, BusinessValidationException e)
  {
    Exception[] wrappedEx = e.getWrappedExceptions();
    for (int i = 0; i < wrappedEx.length; i++)
    {
      GenericException ge = (GenericException)wrappedEx[i];
      StrutsUtils.addErrorMessage(request, ge);
    }
  }
  

  public ActionForward doInsertPropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Workorder wk = Workorder.retrieveByWorkOrder((String)data.getValue("workorderId"));
    
    if (!wk.getWkTypName().equalsIgnoreCase("A")) {
      String pageId = (String)data.getValue("pageId");
      
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lr = new LaborRequisitionPage(null, "", null, null, null, null, null, null, null, null, null, null, Integer.valueOf(0));
      if ((pageId != null) && (!"".equalsIgnoreCase(pageId)) && (!"page_new".equalsIgnoreCase(pageId))) {
        lr = facade.retrieveLaborReqPageById(pageId);
      }
      setValuesToUI(data, lr, wk);
      
      String selectedProperty = getSelectedPropertyId(data);
      BaseLRAttributes config = new BaseLRAttributes(wk);
      config.insertPropertyRow(selectedProperty, data);
    }
    else {
      return doRefresh(mapping, form, request, response);
    }
    
    return mapping.findForward("success");
  }
  
  private LaborRequisitionPage saveLR(ActionForm form, Workorder wk) throws Exception
  {
    LaborRequisitionPage req = getSavedContFromUI(form, wk);
    
    LaborReqFacade facade = getFacade();
    
    LaborRequisitionPage lr = facade.saveLaborRequisitionPage(req, wk.getUnitCode());
    return lr;
  }
  
  public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    try
    {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      

      String workorderId = (String)data.getValue("workorderId");
      

      WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
      Workorder wk = wFacade.getWorkOrder(workorderId);
      validateFields(form, wk.getValidFrom(), wk.getValidTo());
      LaborRequisitionPage lr = saveLR(form, wk);
      setValuesToUI(data, lr, wk);
      clearUiDirtyData((DynamicMapBackedForm)form);
      lr.sendLRCreationNotification();
    }
    catch (Exception e) {
      logError(e);
     // CMSException ex;
      CMSException ex; if ((e instanceof CMSException)) {
        ex = (CMSException)e;
      } else {
        ex = CMSException.errorNullInput();
      }
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);
    }
    
    return mapping.findForward("doWorkOrder");
  }
  
  private void validateFields(ActionForm form, KDate validfrom, KDate validTo) {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String fromDtm = (String)data.getValue("fromdtm");
    String toDtm = (String)data.getValue("todtm");
    String shiftId = (String)data.getValue("shiftId");
    String selectedTradeId = (String)data.getValue("selectedTradeId");
    String selectedSkillId = (String)data.getValue("selectedSkillId");
    String shift_A = (String)data.getValue("shift_A");
    String shift_B = (String)data.getValue("shift_B");
    String shift_C = (String)data.getValue("shift_C");
    String shift_G = (String)data.getValue("shift_G");
    String weeklyOffDays = (String)data.getValue("weeklyOffDays");
    if ((fromDtm == null) || ("".equalsIgnoreCase(fromDtm))) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.from", "From date"));
    }
    if ((toDtm == null) || ("".equalsIgnoreCase(toDtm)))
    {
      throw CMSException.missingRequiredField(KronosProperties.get("label.to", "To date"));
    }
    if ((weeklyOffDays == null) || ("".equalsIgnoreCase(weeklyOffDays)) || 
      (Integer.valueOf(weeklyOffDays).intValue() > 1) || (Integer.valueOf(weeklyOffDays).intValue() < 0))
    {
      throw CMSException.errorMessage("Off Days per Week value should be either 0 or 1");
    }
    


    KDate from = KServer.stringToDate(fromDtm);
    KDate to = KServer.stringToDate(toDtm);
    if (from.isAfter(to)) {
      throw CMSException.fromDateBeforeExistingRecord(to.toString());
    }
    
    Calendar cal = KCalendar.getInstance(TimeZone.getDefault());
    cal.set(5, cal.getActualMaximum(5));
    
    Date lastDayOfMonth = cal.getTime();
    KDate date = KDate.create(lastDayOfMonth);
    


    if ((from.isBeforeDate(validfrom)) || (to.isAfterDate(validTo))) {
      throw CMSException.lrTimeframeIsNotValid();
    }
    
    if ((selectedTradeId != null) && (selectedTradeId.equalsIgnoreCase("-1")))
    {
      throw CMSException.missingRequiredField(KronosProperties.get("label.trade", "Trade"));
    }
    

    if ((selectedSkillId != null) && (selectedSkillId.equalsIgnoreCase("-1")))
    {

      throw CMSException.missingRequiredField(KronosProperties.get("label.skill", "Skill"));
    }
  }
  



  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSLaborReqList");
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String pageId = getSelectedId(form);
      String workorderId = (String)data.getValue("workorderId");
      WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
      Workorder wk = wFacade.getWorkOrder(workorderId);
      request.setAttribute("workordertyp", wk.getWkTypName());
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lr = facade.retrieveLaborReqPageById(pageId);
      setValuesToUI(data, lr, wk);
    }
    catch (CMSException ex) {
      log(1, ex.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);

    }
    catch (Exception e)
    {
      log(1, e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    
    return mapping.findForward("success");
  }
  
  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String workorderId = (String)data.getValue("workorderId");
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    Workorder wk = wFacade.getWorkOrder(workorderId);
    
    try
    {
      request.setAttribute("workordertyp", wk.getWkTypName());
      LaborReqFacade facade = new LaborReqFacadeImpl();
      LaborRequisitionPage lr = new LaborRequisitionPage(null, "", null, null, null, null, null, null, null, null, null, null, Integer.valueOf(0));
      setValuesToUI(data, lr, wk);

    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);

    }
    catch (Exception e)
    {
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));

    }
    finally
    {
      data.setValue("fromdtm", KServer.dateToString(KDate.today()));
      data.setValue("todtm", KServer.dateToString(wk.getValidTo()));
      data.setValue("tradeList", Trade.retrieveTradesByUnit(wk.getUnitId()));
      data.setValue("skillList", Skill.retrieveSkillsByUnit(wk.getUnitId()));
    }
    

    return mapping.findForward("success");
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
  
  protected LaborReqFacade getFacade()
  {
    return new LaborReqFacadeImpl();
  }
  
  private LaborRequisitionPage getSavedContFromUI(ActionForm form, Workorder wk) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    String pageId = (String)data.getValue("pageId");
    

    LaborRequisitionPage page = null;
    LaborReqFacade facade = new LaborReqFacadeImpl();
    List<LaborRequisition> lrs = new ArrayList();
    BaseLRAttributes attrbutes = new BaseLRAttributes(wk);
    Map[] lrlines = attrbutes.getPropertiesFromDmbf(data);
    String fromDtm = (String)data.getValue("fromdtm");
    String toDtm = (String)data.getValue("todtm");
    String remark = (String)data.getValue("remark");
    String weeklyOffDays = (String)data.getValue("weeklyOffDays");
    for (int i = 0; i < lrlines.length; i++) {
      Map map = lrlines[i];
      

      String lrId = (String)map.get("lrId");
      
      if (!attrbutes.isPropertyBlank(lrId, data)) {
        String tradeId = (String)map.get("tradeId");
        String skillId = (String)map.get("skillId");
        String wlId = (String)map.get("wlId");
        String qtyA = "".equalsIgnoreCase((String)map.get("shift_A")) ? "0" : (String)map.get("shift_A");
        String qtyB = "".equalsIgnoreCase((String)map.get("shift_B")) ? "0" : (String)map.get("shift_B");
        String qtyC = "".equalsIgnoreCase((String)map.get("shift_C")) ? "0" : (String)map.get("shift_C");
        String qtyG = "".equalsIgnoreCase((String)map.get("shift_G")) ? "0" : (String)map.get("shift_G");
        String shiftIdA = (String)map.get("shift_A_id");
        String shiftIdB = (String)map.get("shift_B_id");
        String shiftIdC = (String)map.get("shift_C_id");
        String shiftIdG = (String)map.get("shift_G_id");
        
        String pattern = "new_property";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(lrId);
        if (matcher.find())
        {
          lrId = null;
        }
        LaborRequisition lr = null;
        if ((lrId != null) && (!lrId.equalsIgnoreCase(""))) {
          lr = LaborRequisition.retrieveLR(lrId);
          lr.setTradeId(new ObjectIdLong(tradeId));
          lr.setSkillId(new ObjectIdLong(skillId));
          lr.setFromdtm(KServer.stringToDate(fromDtm));
          lr.setTodtm(KServer.stringToDate(toDtm));
          

          LRShift lrShiftA = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdA), Integer.valueOf(Integer.parseInt(qtyA)));
          LRShift lrShiftB = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdB), Integer.valueOf(Integer.parseInt(qtyB)));
          LRShift lrShiftC = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdC), Integer.valueOf(Integer.parseInt(qtyC)));
          LRShift lrShiftG = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdG), Integer.valueOf(Integer.parseInt(qtyG)));
          
          ArrayList<LRShift> lrShifts = new ArrayList();
          lrShifts.add(lrShiftA);
          lrShifts.add(lrShiftB);
          lrShifts.add(lrShiftC);
          lrShifts.add(lrShiftG);
          
          lr.setLrshifts(lrShifts);

        }
        else
        {

          lr = new LaborRequisition(null, null, "LR", KServer.stringToDate(fromDtm), 
            KServer.stringToDate(toDtm), new ObjectIdLong(wlId), 
            new ObjectIdLong(tradeId), new ObjectIdLong(skillId));
          
          LRShift lrShiftA = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdA), Integer.valueOf(Integer.parseInt(qtyA)));
          LRShift lrShiftB = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdB), Integer.valueOf(Integer.parseInt(qtyB)));
          LRShift lrShiftC = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdC), Integer.valueOf(Integer.parseInt(qtyC)));
          LRShift lrShiftG = new LRShift(lr.getLrId(), new ObjectIdLong(shiftIdG), Integer.valueOf(Integer.parseInt(qtyG)));
          
          ArrayList<LRShift> lrShifts = new ArrayList();
          lrShifts.add(lrShiftA);
          lrShifts.add(lrShiftB);
          lrShifts.add(lrShiftC);
          lrShifts.add(lrShiftG);
          lr.setLrshifts(lrShifts);
          
          if ("A".equalsIgnoreCase(wk.getWkTypName()))
          {
            Long balance = lr.calculateBalanceQty(wk, Integer.valueOf(weeklyOffDays));
            Long totalQty = lr.getTotalQty(Integer.valueOf(weeklyOffDays));
            
            if (totalQty.longValue() > balance.longValue()) {
              throw CMSException.cannotExceedBalanceQty(balance, lr.getTrade().getTradeName(), lr.getSkill().getSkillNm());
            }
          }
        }
        
        lrs.add(lr);
      }
    }
    

    if ((pageId == null) || ("".equalsIgnoreCase(pageId)) || ("page_new".equalsIgnoreCase(pageId))) {
      page = new LaborRequisitionPage(null, null, KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm), Integer.valueOf(0), wk.getWorkorderId(), Integer.valueOf(0), lrs, remark, null, null, null, Integer.valueOf(weeklyOffDays));
    }
    else
    {
      page = facade.retrieveLaborReqPageById(pageId);
      page.setApprovalSw(Integer.valueOf(0));
      page.setRemark(remark);
      page.setWeeklyOffDays(Integer.valueOf(weeklyOffDays));
    }
    

    page.setLrs(lrs);
    return page;
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
      log(1, ex.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);

    }
    catch (Exception e)
    {
      log(1, e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    
    return mapping.findForward("success");
  }
  
  private void getLRToUI(DynamicMapBackedForm data, HttpServletRequest request) {
    String pageId = (String)data.getValue("pageId");
    String workorderId = (String)data.getValue("workorderId");
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    Workorder wk = wFacade.getWorkOrder(workorderId);
    LaborReqFacade facade = new LaborReqFacadeImpl();
    LaborRequisitionPage lrPage = new LaborRequisitionPage(null, "", null, null, null, new ObjectIdLong(workorderId), null, null, null, null, null, null, Integer.valueOf(0));
    
    if ((pageId != null) && (!"".equalsIgnoreCase(pageId)) && (!"page_new".equalsIgnoreCase(pageId))) {
      lrPage = facade.retrieveLaborReqPageById(pageId);
    }
    
    setValuesToUI(data, lrPage, wk);
    
    request.setAttribute("workordertyp", wk.getWkTypName());
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, LaborRequisitionPage lrPage, Workorder wk) {
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
    data.setValue("tradeList", Trade.retrieveTradesByUnit(wk.getUnitId()));
    data.setValue("skillList", Skill.retrieveSkillsByUnit(wk.getUnitId()));
    
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    List<WorkorderLine> wls = wFacade.getWorkOrderLines(wk.getWorkorderId().toString());
    data.setValue("wlLines", convertToWlMap(wls, wk));
    data.setValue("availShifts", CMSShift.retrieveShifts(wk.getUnitId()));
    
    if (lrPage.getPageId() == null) {
      data.setValue("pageId", "page_new");
      data.setValue("approvalStatus", getApprovalStatus(Integer.valueOf(0)));
      data.setValue("weeklyOffDays", Integer.valueOf(0));
      String fromdtm = (String)data.getValue("fromdtm");
      if ((fromdtm == null) || ("".equalsIgnoreCase(fromdtm))) {
        data.setValue("fromdtm", KServer.dateToString(KDate.today()));
      }
      String todtm = (String)data.getValue("todtm");
      if ((todtm == null) || ("".equalsIgnoreCase(todtm))) {
        data.setValue("todtm", KServer.dateToString(wk.getValidTo()));
      }
      
      List<LaborRequisition> lines = createLRLinesFromWKLines(wls, wk);
      Map lrMap = new HashMap();
      lrMap.put("lrLines", convertToMap(data, lines, wk, lrPage.getWeeklyOffDays()));
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
      lrMap.put("lrLines", convertToMap(data, lines, wk, lrPage.getWeeklyOffDays()));
      BaseLRAttributes attributes = new BaseLRAttributes(wk);
      attributes.attributesToDmbf(lrMap, data);
    }
  }
  
  private List<LaborRequisition> createLRLinesFromWKLines(List<WorkorderLine> wls, Workorder wk)
  {
    List<LaborRequisition> reqs = new ArrayList();
    for (Iterator iterator = wls.iterator(); iterator.hasNext();) {
      WorkorderLine wl = (WorkorderLine)iterator.next();
      LaborRequisition req = new LaborRequisition(null, null, "", KDate.today(), KDate.today(), wl.getWkLineId(), wl.getTradeId(), wl.getSkillId());
      
      if ((!"A".equalsIgnoreCase(wk.getWkTypName())) || (wl.getTradeId() != null) || (wl.getSkillId() != null))
        reqs.add(req);
    }
    return reqs;
  }
  
  private Object convertToWlMap(List<WorkorderLine> attributes, Workorder wk) {
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
  
  private Map[] convertToMap(DynamicMapBackedForm data, List attributes, Workorder wk, Integer weeklyOffDays)
  {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        LaborRequisition wa = (LaborRequisition)iterator.next();
        props[(i++)] = createPropertyMap(data, wa, wk, i, weeklyOffDays);
      }
    }
    return props;
  }
  
  private Map createPropertyMap(DynamicMapBackedForm data, LaborRequisition lr, Workorder wk, int i, Integer weeklyOffDays)
  {
    Map prop = new HashMap();
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    WorkorderLine wl = wFacade.getWorkOrderLineById(lr.getWorkOrderlnId().toString());
    
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
    String lrId = (String)prop.get("lrId");
    for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
      CMSShift cmsShift = (CMSShift)iterator.next();
      if (cmsShift.getShiftNm().equalsIgnoreCase(shiftA)) {
        String qtyA = (String)data.getValue(lrId + "_shift_A");
        if ((qtyA != null) && (!"".equalsIgnoreCase(qtyA))) {
          prop.put("shift_A", qtyA);
        }
        prop.put("shift_A_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftB)) {
        String qtyB = (String)data.getValue(lrId + "_shift_B");
        if ((qtyB != null) && (!"".equalsIgnoreCase(qtyB)))
          prop.put("shift_B", qtyB);
        prop.put("shift_B_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftC)) {
        String qtyC = (String)data.getValue(lrId + "_shift_C");
        if ((qtyC != null) && (!"".equalsIgnoreCase(qtyC))) {
          prop.put("shift_C", qtyC);
        }
        prop.put("shift_C_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftG)) {
        String qtyG = (String)data.getValue(lrId + "_shift_G");
        if ((qtyG != null) && (!"".equalsIgnoreCase(qtyG))) {
          prop.put("shift_G", qtyG);
        }
        prop.put("shift_G_id", cmsShift.getShiftId().toString());
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
  
  private String getApprovalStatus(Integer approvalSW) {
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
  
  private void logError(Exception e) {
    Log.log(e, "Error occured in saving LR");
  }
}
