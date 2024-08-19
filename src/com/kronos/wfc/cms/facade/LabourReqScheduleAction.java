package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSShift;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.LRShift;
import com.kronos.wfc.cms.business.LaborReqSchedule;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.datacollection.smartview.shared.PayCodeEditBean;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.timekeeping.core.api.TimekeepingAPIDefinitions;
import com.kronos.wfc.timekeeping.core.business.PayCodeEdit;
import com.kronos.wfc.timekeeping.core.business.PayCodeEditAmount;
import com.kronos.wfc.timekeeping.payrules.business.configuration.paycodes.definition.PayCode;
import com.kronos.wfc.totalizing.business.persistence.timesheetitems.PayCodeEditInsert;
import com.kronos.wfc.wfp.logging.Log;
import com.kronos.wfc.workflow.shared.TimekeepingApiTask;

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



public class LabourReqScheduleAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  


  public LabourReqScheduleAction() {}
  

  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save.schedule", "doSave");
    methods.put("cms.action.return", "doReturn");
    methods.put("cms.action.go.to.schedule", "doSchedule");
    methods.put("cms.action.remove", "doRemove");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  

  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String tradeid = (String)form.getValue("selectedId");
    return tradeid;
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  private LaborReqFacade getFacade() {
    return new LaborReqFacadeImpl();
  }
  
  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  

  public ActionForward doSchedule(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String id = getSelectedId(form);
    
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      
      return doRefresh(mapping, form, request, response);
    }
    
    String findForward = KronosProperties.get("cms.schedule.URL");
    return mapping.findForward(findForward);
  }
  

  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    
    StrutsUtils.addErrorMessage(request, e);
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;

    try { 
    	getLabourreqScheduleDataToUI(data);    
    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex); }
      catch (Exception e) {
     /* String fromDtm;
      String toDtm;
      String lrId;*/
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage())); } 
      finally { 
      //String fromDtm;
     /* String toDtm;
      String lrId;*/
      String fromDtm = (String)data.getValue("fromdtm");
      String toDtm = (String)data.getValue("todtm");
      String lrId = (String)data.getValue("lrId");
      request.setAttribute("selectedLRId", lrId);
      request.setAttribute("fromdtm", fromDtm);
      request.setAttribute("todtm", toDtm);
      request.setAttribute("patternName", "-1");
    }
    
    return mapping.findForward("success");
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String lrId = (String)data.getValue("lrId");
      request.setAttribute("selectedLrId", lrId);
      String fromDtm = (String)data.getValue("fromdtm");
      String toDtm = (String)data.getValue("todtm");
      
      if ((fromDtm == null) || (toDtm == null)) {
        throw CMSException.missingRequiredFields();
      }
      
      if ((lrId == null) || ("-1".equals(lrId))) {
        throw CMSException.missingRequiredField(KronosProperties.get("label.job", "Job"));
      }
      
      LaborReqFacade facade = getFacade();
      LaborRequisition req = facade.retrieveLaborReqById(lrId);
      KDate uiFrom = KServer.stringToDate(fromDtm);
      KDate uiTo = KServer.stringToDate(toDtm);
      if (uiTo.isBefore(uiFrom)) {
        throw CMSException.timeframesNotValid();
      }
      if ((!uiFrom.isBetweenDates(req.getFromdtm().minusDays(1), req.getTodtm().plusDays(1))) || 
        (!uiTo.isBetweenDates(req.getFromdtm().minusDays(1), req.getTodtm().plusDays(1)))) {
        throw CMSException.schedulingTimeframeIsNotValid();
      }
      

      String[] ids = (String[])data.getValue("selectedIds");
      if ((ids != null) && (ids.length > 0))
      {
        LaborReqFacade impl = getFacade();
        Workorder order = impl.retrieveWorkorderByLRPageID((String)data.getValue("pageId"));
        HashMap wkPatternMap = new HashMap();
        String tradeId = null;
        String skillId = null;
        for (int i = 0; i < ids.length; i++) {
          String id = ids[i];
          Workmen wk = new WorkmenFacadeImpl().getWorkmen(id, order.getUnitId().toString());
          String shiftPattern = (String)data.getValue(id + "_" + "pattern");
          if ("-1".equalsIgnoreCase(shiftPattern))
          {
            throw CMSException.missingRequiredField(KronosProperties.get("label.shiftPattern", "Shift Pattern"));
          }
          wkPatternMap.put(wk.getEmpId(), shiftPattern);
          
          tradeId = (String)data.getValue(id + "_" + "tradeId");
          skillId = (String)data.getValue(id + "_" + "skillId");
        }
        
        impl.validate(wkPatternMap, fromDtm, toDtm, new ObjectIdLong(tradeId), new ObjectIdLong(skillId), order);
        
        for (int i = 0; i < ids.length; i++) {
          String id = ids[i];
          String shiftPattern = (String)data.getValue(id + "_" + "pattern");
          Workmen wk = new WorkmenFacadeImpl().getWorkmen(id, order.getUnitId().toString());
          ArrayList wList = new ArrayList();
          wList.add(wk);
          
          String tId = (String)data.getValue(id + "_" + "tradeId");
          String sId = (String)data.getValue(id + "_" + "skillId");
          
          impl.saveSchedule(wList, req, tId, sId, fromDtm, toDtm, shiftPattern);
        }
        StrutsUtils.addMessage(request, new Message("cms.saved.successfully"));

      }
      else
      {
        setNoSelectionErrorMessage(request);
        //String lrId;
        return doRefresh(mapping, form, request, response);
      }
    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    } catch (Exception e) {
      String lrId;
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    } finally {
     // String lrId;
      String lrId = (String)data.getValue("lrId");
      request.setAttribute("selectedLRId", lrId);
      setValuesToUI(data);
    }
    String lrId = (String)data.getValue("lrId");
    request.setAttribute("selectedLRId", lrId);
    setValuesToUI(data);
    
    return mapping.findForward("success");
  }
  
  public ActionForward doRemove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      String lrId = (String)data.getValue("lrId");
      request.setAttribute("selectedLrId", lrId);
      String fromDtm = (String)data.getValue("fromdtm");
      String toDtm = (String)data.getValue("todtm");
      String unitId = (String)data.getValue("unitId");
      if ((fromDtm == null) || (toDtm == null)) {
        throw CMSException.missingRequiredFields();
      }
      
      LaborReqFacade facade = getFacade();
      LaborRequisition req = facade.retrieveLaborReqById(lrId);
      Workorder wo = new CMSService().retrieveWorkorderByLRId(req.getLrId());
      KDate uiFrom = KServer.stringToDate(fromDtm);
      KDate uiTo = KServer.stringToDate(toDtm);
      if (uiTo.isBefore(uiFrom)) {
        throw CMSException.timeframesNotValid();
      }
      if ((!uiFrom.isBetweenDates(req.getFromdtm().minusDays(1), req.getTodtm().plusDays(1))) || 
        (!uiTo.isBetweenDates(req.getFromdtm().minusDays(1), req.getTodtm().plusDays(1)))) {
        throw CMSException.schedulingTimeframeIsNotValid();
      }
      
      String[] ids = (String[])data.getValue("selectedIds");
      if ((ids != null) && (ids.length > 0))
      {
        for (int i = 0; i < ids.length; i++) {
          String id = ids[i];
          Workmen wk = new WorkmenFacadeImpl().getWorkmen(id, wo.getUnitId().toLong().toString());
          ArrayList wkList = new ArrayList();
          wkList.add(wk);
          LaborReqFacade impl = new LaborReqFacadeImpl();
          impl.removeSchedule(wkList, req, fromDtm, toDtm);
        }
        StrutsUtils.addMessage(request, new Message("cms.removed.successfully"));

      }
      else
      {
        setNoSelectionErrorMessage(request);
       // String lrId;
        return doRefresh(mapping, form, request, response);
      }
    }
    catch (CMSException ex)
    {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    } catch (Exception e) {
      String lrId;
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    } finally {
     // String lrId;
      String lrId = (String)data.getValue("lrId");
      request.setAttribute("selectedLRId", lrId);
      setValuesToUI(data);
    }
    String lrId = (String)data.getValue("lrId");
    request.setAttribute("selectedLRId", lrId);
    setValuesToUI(data);
    
    return mapping.findForward("success");
  }
  



  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSBulkSchedule");
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
  
  private void setValuesToUI(DynamicMapBackedForm data)
  {
    String pageId = (String)data.getValue("pageId");
    String lrId = (String)data.getValue("lrId");
    boolean cameFromWAactions = false;
    if ((pageId == null) || ("".equalsIgnoreCase(pageId))) {
      pageId = getSelectedId(data);
      cameFromWAactions = true;
    }
    LaborRequisitionPage page = getFacade().retrieveLaborReqPageById(pageId);
    
    Workorder order = new WorkOrderFacadeImpl().getWorkOrder(page.getWorkorderId().toString());
    
    String fromDtm = page.getFromDtm().toString();
    String toDtm = page.getToDtm().toString();
    
    data.setValue("pageId", page.getPageId().toString());
    data.setValue("lrnumber", page.getPageNum().toString());
    data.setValue("wkNum", order.getWkNum());
    data.setValue("name", order.getName());
    data.setValue("workordertype", order.getWkTypName());
    data.setValue("wotypDispName", order.getWkTypDispName());
    data.setValue("validfrom", order.getValidFrom().toString());
    data.setValue("validto", order.getValidTo().toString());
    data.setValue("lrDispNum", page.getPageDispNum(order));
    data.setValue("remark", page.getRemark());
    data.setValue("depCode", Department.doRetrieveById(order.getDepId()).getDescription());
    data.setValue("secCode", order.getSectionCode());
    List<LaborRequisition> reqs = filterLines(page.getLrs(), page.getWeeklyOffDays());
    
    data.setValue("lrLines", convertToLRMap(reqs, order));
    data.setValue("selectedLRId", lrId);
    
    String from = (String)data.getValue("fromdtm");
    String to = (String)data.getValue("todtm");
    if (((from == null) || ("".equalsIgnoreCase(from))) && ((to == null) || ("".equalsIgnoreCase(to)))) {
      data.setValue("fromdtm", fromDtm);
      data.setValue("todtm", toDtm);
      from = fromDtm;
      to = toDtm;
    }
    

    if ((from != null) && (to != null) && (lrId != null) && 
      (!"".equalsIgnoreCase(from)) && 
      (!"".equalsIgnoreCase(to)) && 
      (!"".equalsIgnoreCase(lrId)) && 
      (!"-1".equalsIgnoreCase(lrId)))
    {
      LaborRequisition req = getFacade().retrieveLaborReqById(lrId);
      LaborReqFacade facade = new LaborReqFacadeImpl();
      List<LaborReqSchedule> lrs = LaborReqSchedule.retrieveSchedulesByLRIDAndTimeframe(lrId, KServer.stringToDate(from), KServer.stringToDate(to));
      List<Workmen> list = getWorkMenList(order.getContractorId().toString(), req.getTrade(), req.getSkill(), order.getWkTypName(), order.getUnitId());
      HashMap<ObjectIdLong, String> empHSched = facade.hasSchedulesPerPerson(list, KServer.stringToDate(from), KServer.stringToDate(to), 
        req.getTradeId(), req.getSkillId(), order);
      data.setValue("workmenList", convertToWorkMenMap(list, empHSched, lrs, data, order.getUnitCode()));
      data.setValue("patterns", CMSShift.retrievePatternsByUnitId(order.getUnitId()));
    }
    else
    {
      data.setValue("workmenList", new Map[0]);
    }
  }
  
  private List<LaborRequisition> filterLines(List<LaborRequisition> lrs, Integer weeklyOffDays)
  {
    List<LaborRequisition> filteredList = new ArrayList();
    for (Iterator iterator = lrs.iterator(); iterator.hasNext();) {
      LaborRequisition lr = (LaborRequisition)iterator.next();
      if (lr.getTotalQty(weeklyOffDays).longValue() > 0L) {
        filteredList.add(lr);
      }
    }
    return filteredList;
  }
  
  private Map[] convertToLRMap(List<LaborRequisition> attributes, Workorder wk)
  {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        LaborRequisition wa = (LaborRequisition)iterator.next();
        props[(i++)] = createPropertyLRMap(wa, wk);
      }
    }
    return props;
  }
  

  private Map createPropertyLRMap(LaborRequisition lr, Workorder wk)
  {
    Map prop = new HashMap();
    WorkOrderFacade wFacade = new WorkOrderFacadeImpl();
    WorkorderLine wl = wFacade.getWorkOrderLineById(lr.getWorkOrderlnId().toString());
    LaborReqFacade facade = new LaborReqFacadeImpl();
    HashMap shiftBucket = facade.scheduleQuantityPerShift(lr.getFromdtm(), lr.getTodtm(), lr.getTradeId(), lr.getSkillId(), wk);
    HashMap lrQty = facade.getLRQuantityPerShift(lr.getFromdtm(), lr.getTodtm(), lr.getTradeId(), lr.getSkillId(), wk);
    
    prop.put("lrId", lr.getLrId() == null ? "" : lr.getLrId().toString());
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
    prop.put("fromDate", lr.getFromdtm().toString());
    prop.put("toDate", lr.getTodtm().toString());
    
    String shiftA = (String)CMSShift.map.get("shift_A");
    String shiftB = (String)CMSShift.map.get("shift_B");
    String shiftC = (String)CMSShift.map.get("shift_C");
    String shiftG = (String)CMSShift.map.get("shift_G");
    List<CMSShift> shifts = CMSShift.retrieveShifts(wk.getUnitId());
    for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
      CMSShift cmsShift = (CMSShift)iterator.next();
      if (cmsShift.getShiftNm().equalsIgnoreCase(shiftA)) {
        prop.put("shift_A_id", cmsShift.getShiftId().toString());

      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftB))
      {
        prop.put("shift_B_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftC))
      {
        prop.put("shift_C_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftG))
      {
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
          Long qty = (Long)shiftBucket.get(lrShift.getShiftId());
          
          if (qty == null) {
            prop.put("sched_shift_A", "0");
          }
          else {
            prop.put("sched_shift_A", qty.toString());
          }
          Long lrq = (Long)lrQty.get(lrShift.getShiftId());
          if (lrq == null) {
            prop.put("lr_shift_A", "0");
          }
          else {
            prop.put("lr_shift_A", lrq.toString());
          }
          
          prop.put("shift_A", lrShift.getQty());


        }
        else if (shiftName.equalsIgnoreCase(shiftB)) {
          Long qty = (Long)shiftBucket.get(lrShift.getShiftId());
          if (qty == null) {
            prop.put("sched_shift_B", "0");
          }
          else {
            prop.put("sched_shift_B", qty.toString());
          }
          Long lrq = (Long)lrQty.get(lrShift.getShiftId());
          if (lrq == null) {
            prop.put("lr_shift_B", "0");
          }
          else {
            prop.put("lr_shift_B", lrq.toString());
          }
          
          prop.put("shift_B", lrShift.getQty());

        }
        else if (shiftName.equalsIgnoreCase(shiftC)) {
          Long qty = (Long)shiftBucket.get(lrShift.getShiftId());
          if (qty == null) {
            prop.put("sched_shift_C", "0");
          }
          else {
            prop.put("sched_shift_C", qty.toString());
          }
          Long lrq = (Long)lrQty.get(lrShift.getShiftId());
          if (lrq == null) {
            prop.put("lr_shift_C", "0");
          }
          else {
            prop.put("lr_shift_C", lrq.toString());
          }
          
          prop.put("shift_C", lrShift.getQty());

        }
        else if (shiftName.equalsIgnoreCase(shiftG)) {
          Long qty = (Long)shiftBucket.get(lrShift.getShiftId());
          if (qty == null) {
            prop.put("sched_shift_G", "0");
          }
          else {
            prop.put("sched_shift_G", qty.toString());
          }
          Long lrq = (Long)lrQty.get(lrShift.getShiftId());
          if (lrq == null) {
            prop.put("lr_shift_G", "0");
          }
          else {
            prop.put("lr_shift_G", lrq.toString());
          }
          
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
  






  private Map[] convertToWorkMenMap(List<Workmen> attributes, HashMap empSched, List<LaborReqSchedule> lrs, DynamicMapBackedForm data, String unitCode)
  {
    ArrayList<Map> ws = new ArrayList();
    if ((attributes != null) && (!attributes.isEmpty()))
    {
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        Workmen wa = (Workmen)iterator.next();
        boolean isIncluded = false;
        try {
          Person p = wa.getPerson();
          Personality per = Personality.getByPersonId(p.getPersonId());
          PrimaryLaborAccount pla = per.getJobAssignment().getCurrentPrimaryLaborAccount();
          LaborAccount acct = pla.getLaborAccount();
          String[] names = acct.getLaborLevelEntryNames_optimized();
          for (int j = 0; j < names.length; j++) {
            String name = names[j];
            if (name.equalsIgnoreCase(unitCode)) {
              isIncluded = true;
            }
          }
        } catch (Exception e1) {
          Log.log(e1, "Error occurred in filtering workmen per unit");
          isIncluded = true;
        }
        if (isIncluded) {
          ws.add(createWKPropertyMap(wa, empSched, lrs, data));
        }
        
      }
    } else {
      return new Map[0];
    }
    
    Map[] props = new Map[ws.size()];
    int i = 0;
    for (Iterator iterator = ws.iterator(); iterator.hasNext();) {
      Map map = (Map)iterator.next();
      props[(i++)] = map;
    }
    return props;
  }
  

  private Map createWKPropertyMap(Workmen wa, HashMap empSched, List<LaborReqSchedule> lrs, DynamicMapBackedForm data)
  {
    Map prop = new HashMap();
    
    prop.put("empId", wa.getEmpId().toString());
    prop.put("firstName", wa.getFirstName());
    prop.put("lastName", wa.getLastName());
    prop.put("tradeId", wa.getTrade().getTradeId().toString());
    prop.put("skillId", wa.getSkill().getSkillId().toString());
    prop.put("empCode", wa.getEmpCode());
    prop.put("tradeName", wa.getTrade().getTradeName());
    prop.put("skillName", wa.getSkill().getSkillNm());
    prop.put("hasSchedule", empSched.get(wa.getPerson().getPersonId()));
    
    String pattern = (String)data.getValue(wa.getEmpId().toString() + "_" + "pattern");
    prop.put("patternName", pattern);
    String[] ids = (String[])data.getValue("selectedIds");
    if (ids != null) {
      for (int i = 0; i < ids.length; i++) {
        String id = ids[i];
        if (wa.getEmpId().toString().equalsIgnoreCase(id)) {
          prop.put("checked", "checked");
          break;
        }
      }
    }
    

    return prop;
  }
  

  private List<Workmen> getWorkMenList(String contId, Trade trade, Skill skill, String workorderTyp, ObjectIdLong unitId)
  {
    return new WorkmenFacadeImpl().getWorkmenList(contId, trade, skill, workorderTyp, unitId);
  }
  
  private void getLabourreqScheduleDataToUI(DynamicMapBackedForm data)
  {
    setValuesToUI(data);
  }
  
  private void logError(Exception e)
  {
    Log.log(e, "Exception occured while scheduling");
  }
  
  static Map methodsMap = methodsMap();
}
