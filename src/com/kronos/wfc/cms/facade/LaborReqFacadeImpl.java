package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSShift;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.LRShift;
import com.kronos.wfc.cms.business.LaborReqSchedule;
import com.kronos.wfc.cms.business.LaborRequisition;
import com.kronos.wfc.cms.business.LaborRequisitionPage;
import com.kronos.wfc.cms.business.ManagerApproval;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.TimeDetailRow;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.logging.framework.Log;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.utility.framework.datetime.KDateSpan;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import com.kronos.wfc.platform.xml.api.bean.APIBean;
import com.kronos.wfc.platform.xml.api.bean.APIBeanList;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import com.kronos.wfc.platform.xml.api.bean.ParameterMap;
import com.kronos.wfc.scheduling.core.api.APIPatternElementBean;
import com.kronos.wfc.scheduling.core.api.APIScheduleBean;
import com.kronos.wfc.scheduling.core.api.APISchedulePatternBean;
import com.kronos.wfc.scheduling.core.api.APISchedulePayCodeEditBean;
import com.kronos.wfc.scheduling.core.api.APIScheduleShiftBean;
import com.kronos.wfc.scheduling.core.api.APIShiftCodeBean;
import com.kronos.wfc.scheduling.core.api.APIShiftSegmentBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class LaborReqFacadeImpl
  implements LaborReqFacade
{
  public LaborReqFacadeImpl() {}
  
  public List<LaborRequisition> retrieveLaborReqsByWID(String workorderId, String from, String to, List sse)
  {
    ObjectIdLong wkId = new ObjectIdLong(workorderId);
    KDate fromdt = KServer.stringToDate(from);
    KDate todt = KServer.stringToDate(to);
    return new CMSService().retrieveLRsByWorkOrder(workorderId, fromdt, todt, sse);
  }
  


  public List<LaborRequisition> retrieveLaborReqByWorkorderLn(String workorderId, String from, String to, String workorderLnId)
  {
    return new CMSService().retrieveLRsByWoId(new ObjectIdLong(workorderId), KServer.stringToDate(from), 
      KServer.stringToDate(to), new ObjectIdLong(workorderLnId));
  }
  

  public LaborRequisition retrieveLaborReqById(String lrId)
  {
    return new CMSService().retrieveLRById(new ObjectIdLong(lrId));
  }
  

  public Workorder retrieveWorkorderByLRID(String lrId)
  {
    return new CMSService().retrieveWorkorderByLRId(new ObjectIdLong(lrId));
  }
  
  public LaborRequisition saveLaborRequisition(LaborRequisition req, String unitCode)
  {
    req.doUpdate(unitCode);
    ObjectIdLong id = req.getLrId();
    return retrieveLaborReqById(id.toString());
  }
  



  public void saveSchedule(ArrayList wkList, LaborRequisition lr, String tradeId, String skillId, String fromDtm, String toDtm, String shiftPattern)
  {
    APIBeanList list = getEmployees(wkList);
    

    APISchedulePatternBean pattern = getSchedulePattern(shiftPattern);
    APISchedulePatternBean pBean = (APISchedulePatternBean)pattern.load(new ParameterMap());
    APIBeanList pList = pBean.getPatternElements();
    List bList = pList.getList();
    APIBeanList newList = new APIBeanList(pList.size());
    for (Iterator iterator = bList.iterator(); iterator.hasNext();) {
      APIPatternElementBean bean = (APIPatternElementBean)iterator.next();
      
      APIPatternElementBean newBean = new APIPatternElementBean();
      newBean.setElementType(bean.getElementType());
      newBean.setDayNumber(bean.getDayNumber());
      

      if (bean.isProperty("PayCodeName")) {
        newBean.setPayCodeName(bean.getPayCodeName());
      }
      
      if (bean.isProperty("AmountInTime")) {
        newBean.setAmountInTime(bean.getAmountInTime());
      }
      if (bean.isProperty("DisplayTime")) {
        newBean.setDisplayTime(bean.getDisplayTime());
      }
      
      if (bean.isProperty("AccrualCodeName")) {
        newBean.setDisplayTime(bean.getAccrualCodeName());
      }
      if (bean.isProperty("LaborAcctId")) {
        newBean.setProperty("LaborAcctId", bean.getProperty("LaborAcctId"));
      }
      
      if (bean.isProperty("ShiftCode")) {
        APIShiftCodeBean sBean = bean.getShiftCode();
        APIShiftCodeBean newSBean = new APIShiftCodeBean();
        newSBean.setLabel(sBean.getLabel());
        
        APIBeanList segList = sBean.getShiftSegments();
        List sList = segList.getList();
        APIBeanList newSegList = new APIBeanList(segList.size());
        Iterator iterator2 = sList.iterator();
        while (iterator2.hasNext()) {
          APIShiftSegmentBean segBean = (APIShiftSegmentBean)iterator2.next();
          APIShiftSegmentBean newSegBean = new APIShiftSegmentBean();
          newSegBean.setSegmentTypeName(APIShiftSegmentBean.TRANSFER);
          newSegBean.setStartTime(segBean.getStartTime());
          newSegBean.setStartDayNumber(segBean.getStartDayNumber());
          newSegBean.setEndTime(segBean.getEndTime());
          newSegBean.setEndDayNumber(segBean.getEndDayNumber());
          if (segBean.isProperty("WorkRuleName")) {
            newSegBean.setWorkRuleName(segBean.getWorkRuleName());
          }
          Workorder order = new CMSService().retrieveWorkorderByLRId(lr.getLrId());
          WorkorderLine wLine = new CMSService().retrieveWorkOrderLineById(lr.getWorkOrderlnId());
          String laborAccountName = createLAName(order, wLine);
          newSegBean.setLaborAccountName(laborAccountName);
          newSegList.add(newSegBean);
        }
        newSBean.setShiftSegments(newSegList);
        newBean.setShiftCode(newSBean);
      }
      newList.add(newBean);
    }
    APISchedulePatternBean copy = new APISchedulePatternBean();
    copy.setPatternElements(newList);
    copy.setAnchorDate(pBean.getAnchorDate());
    copy.setPeriodLength(pBean.getPeriodLength());
    copy.setPeriodLengthInDays(pBean.getPeriodLengthInDays());
    copy.setPeriodCodeName(pBean.getPeriodCodeName());
    copy.setIsTemplateFlag(Boolean.valueOf(false));
    copy.setIsOverrideFlag(Boolean.valueOf(true));
    APIBeanList patternList = new APIBeanList();
    patternList.add(copy);
    


    KDateSpan span = new KDateSpan(KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm));
    APIBean a = loadShifts(list, span);
    APIBeanList c = (APIBeanList)a.getProperty("ScheduleItems");
    

    if (!c.isEmpty()) {
      deleteShifts(list, lr, span, a);
    }
    
    Log.log(3, "Loading shifts: Start");
    APIScheduleBean bean = new APIScheduleBean();
    bean.setProperty("Employees", list);
    bean.setProperty("SaveData", Boolean.TRUE);
    bean.setProperty("SchedulePatterns", patternList);
    bean.setProperty("QueryDateSpan", span);
    bean.doAction("AddScheduleItems", new ParameterMap());
    


    Log.log(3, "update shifts: Start");
    



    Log.log(3, "update shifts: End");
    
    new CMSService().saveSchedules(wkList, lr.getLrId().toString(), tradeId, skillId, fromDtm, toDtm, shiftPattern, Integer.valueOf(0));
  }
  
  private void updateShifts(APIBeanList list, LaborRequisition lr, KDateSpan span, APIBean b) {
    APIBeanList c = (APIBeanList)b.getProperty("ScheduleItems");
    APIBeanList d = new APIBeanList();
    Workorder order = new CMSService().retrieveWorkorderByLRId(lr.getLrId());
    WorkorderLine wLine = new CMSService().retrieveWorkOrderLineById(lr.getLrId());
    String laborAccountName = createLAName(order, wLine);
    for (Iterator iterator = c.iterator(); iterator.hasNext();) {
      APIScheduleShiftBean e = (APIScheduleShiftBean)iterator.next();
      APIBeanList shiftSegments = e.getShiftSegments();
      APIBeanList updatedSegments = new APIBeanList();
      for (Iterator iterator2 = shiftSegments.iterator(); iterator2.hasNext();) {
        APIShiftSegmentBean segment = (APIShiftSegmentBean)iterator2.next();
        segment.setSegmentTypeName(APIShiftSegmentBean.TRANSFER);
        segment.setLaborAccountName(laborAccountName);
        segment.removeProperty("StartDate");
        segment.removeProperty("EndDate");
        
        updatedSegments.add(segment);
      }
      e.setProperty("ShiftSegments", updatedSegments);
      
      d.add(e);
    }
    


    APIScheduleBean updateBean = new APIScheduleBean();
    updateBean.setProperty("Employees", list);
    updateBean.setProperty("QueryDateSpan", span);
    updateBean.setProperty("ScheduleItems", d);
    
    updateBean.doAction("Update", new ParameterMap());
  }
  
  private void deleteShifts(APIBeanList list, LaborRequisition lr, KDateSpan span, APIBean b) {
    APIBeanList c = (APIBeanList)b.getProperty("ScheduleItems");
    APIBeanList d = new APIBeanList();
    for (Iterator iterator = c.iterator(); iterator.hasNext();) {
      APIBean bean = (APIBean)iterator.next();
      if ((bean instanceof APIScheduleShiftBean)) {
        APIScheduleShiftBean e = (APIScheduleShiftBean)bean;
        if (e.getStartDate().isOnOrAfter(span.getBegin())) {
          APIBeanList shiftSegments = e.getShiftSegments();
          APIBeanList updatedSegments = new APIBeanList();
          for (Iterator iterator2 = shiftSegments.iterator(); iterator2.hasNext();) {
            APIShiftSegmentBean segment = (APIShiftSegmentBean)iterator2.next();
            segment.removeProperty("StartDate");
            segment.removeProperty("EndDate");
            updatedSegments.add(segment);
          }
          e.setProperty("ShiftSegments", updatedSegments);
          d.add(e);
        }
      }
      if ((bean instanceof APISchedulePayCodeEditBean)) {
        APISchedulePayCodeEditBean e = (APISchedulePayCodeEditBean)bean;
        if (e.getStartDate().isOnOrAfter(span.getBegin())) {
          d.add(e);
        }
      }
    }
    

    if (!d.isEmpty())
    {
      APIScheduleBean dBean = new APIScheduleBean();
      dBean.setProperty("Employees", list);
      dBean.setProperty("QueryDateSpan", span);
      dBean.setProperty("ScheduleItems", d);
      dBean.setProperty("SaveData", Boolean.TRUE);
      dBean.doAction("RemoveScheduleItems", new ParameterMap());
    }
  }
  


  private String createLAName(Workorder order, WorkorderLine wLine)
  {
    String str = wLine.getItemDesc();
    String[] arrOfStr = str.split("~", 4);
    String unitId = arrOfStr[0];
    String deptId = arrOfStr[1];
    String secId = arrOfStr[2];
    String ccCode = arrOfStr[3];
    PrincipalEmployee pEmp = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
    String level1 = pEmp.getOrganization();
    String level2 = pEmp.getUnitCode();
    String level3 = Department.doRetrieveById(new ObjectIdLong(deptId)).getCode();
    String level4 = Section.retrieveSection(new ObjectIdLong(secId)).getName();
    String level5 = Contractor.doRetrieveById(order.getContractorId(), (ObjectIdLong)PrincipalEmployee.doRetrieveByCode(level2).getUnitId()).getVendorCode();
    String level6 = order.getWkNum();
    String level7 = ccCode;
    String acct = level1 + "/" + level2 + "/" + level3 + "/" + level4 + "/" + level5 + "/" + level6 + "/" + level7;
    

    return acct;
  }
  
  private APIBean loadShifts(APIBeanList list, KDateSpan span) {
    ParameterMap map = new ParameterMap();
    APIScheduleBean loadBean = new APIScheduleBean();
    loadBean.setProperty("Employees", list);
    loadBean.setProperty("QueryDateSpan", span);
    APIBean b = loadBean.load(map);
    return b;
  }
  
  private APISchedulePatternBean getSchedulePattern(String shiftPattern) {
    APISchedulePatternBean bean = new APISchedulePatternBean();
    bean.setAnchorDate(getAnchorDate());
    bean.setIsTemplateFlag(Boolean.TRUE);
    bean.setSchedulePatternName(shiftPattern);
    bean.setIsOverrideFlag(Boolean.FALSE);
    return bean;
  }
  
  private KDate getAnchorDate() {
    return KDate.today().mostRecentSunday();
  }
  
  public APIBeanList getEmployees(List wks)
  {
    APIBeanList list = new APIBeanList();
    for (Iterator iterator = wks.iterator(); iterator.hasNext();) {
      Workmen person = (Workmen)iterator.next();
      APIPersonIdentityBean bean = new APIPersonIdentityBean();
      bean.setPersonNumber(person.getPerson().getPersonNumber());
      list.add(bean);
    }
    return list;
  }
  


  public List<LaborReqSchedule> retrieveSchedulesByLRIDAndTF(ObjectIdLong lrId, String fromDtm, String toDtm)
  {
    return LaborReqSchedule.retrieveSchedulesByLRIDAndTimeframe(lrId.toString(), 
      KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm));
  }
  

  public List<TimeDetailRow> retrieveAttendance(List<ObjectIdLong> empIds, KDate fromdt, KDate toDt)
  {
    CMSService service = new CMSService();
    
    HashMap approvals = service.getApprovalsByIds(empIds, fromdt, toDt);
    HashMap exceptions = service.getExceptionsByIds(empIds, fromdt, toDt);
    HashMap unApprOT = service.getUnApprovedOvertimeByIds(empIds, fromdt, toDt);
    HashMap regularHours = service.getRegularHoursByIds(empIds, fromdt, toDt);
    HashMap ots = service.getUnApprovedOT(empIds, fromdt, toDt);
    ArrayList<TimeDetailRow> rows = new ArrayList();
    for (Iterator iterator = empIds.iterator(); iterator.hasNext();) {
      ObjectIdLong empId = (ObjectIdLong)iterator.next();
      ManagerApproval approval = (ManagerApproval)approvals.get(empId);
      Boolean hasEx = (Boolean)exceptions.get(empId);
      Boolean hasUnApprovedOT = (Boolean)unApprOT.get(empId);
      Integer rHours = (Integer)regularHours.get(empId);
      Integer otHours = (Integer)regularHours.get(empId);
      Boolean mApproval = Boolean.valueOf(false);
      if (approval != null) {
        mApproval = Boolean.valueOf(true);
      }
      
      Boolean hasException = Boolean.valueOf(false);
      if (hasEx != null) {
        hasException = Boolean.valueOf(true);
      }
      
      Boolean hasUnAOT = Boolean.valueOf(false);
      if (hasUnApprovedOT != null) {
        hasUnAOT = Boolean.valueOf(true);
      }
      Integer regHours = Integer.valueOf(0);
      if (rHours != null) {
        regHours = rHours;
      }
      Integer ot = Integer.valueOf(0);
      if (rHours != null) {
        ot = otHours;
      }
      
      TimeDetailRow row = new TimeDetailRow(empId, mApproval, hasException, regHours, ot, hasUnAOT);
      rows.add(row);
    }
    return rows;
  }
  

  public List<LaborRequisition> retrieveLaborReqsByWIDAndContCode(String workorderId, String fromDtm, String toDtm, ArrayList cCodes)
  {
    ObjectIdLong wkId = new ObjectIdLong(workorderId);
    KDate fromdt = KServer.stringToDate(fromDtm);
    KDate todt = KServer.stringToDate(toDtm);
    return new CMSService().retrieveLRsByWorkOrderAndContCodes(workorderId, fromdt, todt, cCodes);
  }
  



  public List<LaborRequisition> retrieveLaborReqsForContractor(ObjectId unitId, int approved, ArrayList cCodes)
  {
    return new CMSService().retrieveActiveLRsByContractor((ObjectIdLong)unitId, cCodes);
  }
  


  public List<LaborRequisition> retrieveAllLRsBYSections(ObjectId unitId, ArrayList sse)
  {
    return new CMSService().retrieveLRsBySections((ObjectIdLong)unitId, sse);
  }
  

  public LaborRequisitionPage retrieveLaborReqPageById(String lrPageId)
  {
    return new CMSService().retrieveLRPageById(lrPageId);
  }
  

  public Workorder retrieveWorkorderByLRPageID(String lrPageId)
  {
    return new CMSService().retrieveWorkorderByPageId(lrPageId);
  }
  

  public LaborRequisitionPage saveLaborRequisitionPage(LaborRequisitionPage page, String unitCode)
  {
    page.doUpdate(unitCode);
    ObjectIdLong id = page.getPageId();
    return retrieveLaborReqPageById(id.toString());
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesByWIDAndContCode(String workorderId, Integer approvalSw, KDate fromDtm, KDate toDtm)
  {
    return new CMSService().retrieveLRPagesByWIDAndContCode(
      workorderId, approvalSw, fromDtm, toDtm);
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesByWIDAndContCode(String workorderId, Integer approvalSw, KDate fromDtm, KDate toDtm, ArrayList cCodes)
  {
    return new CMSService().retrieveLRPagesByWIDAndContCode(
      workorderId, approvalSw, fromDtm, toDtm, cCodes);
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesForContractor(ObjectId unitId, int approved)
  {
    return new CMSService().retrieveLRPagesByContractor(unitId, approved);
  }
  


  public List<LaborRequisitionPage> retrieveAllLRPagesBySections(ObjectId unitId, ArrayList sse, String statusId)
  {
    return new CMSService().retrieveLRPagesBySections((ObjectIdLong)unitId, sse, statusId);
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesByWID(String workorderId)
  {
    return new CMSService().retrieveLRPagesByWID(workorderId);
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesByWID(String wId, KDate fromDtm, KDate toDtm, ArrayList sse, String statusId, String secCode)
  {
    return new CMSService().retrieveLRPagesByWIDAndSections(wId, fromDtm, toDtm, sse, statusId, secCode);
  }
  


  public List<LaborRequisitionPage> retrieveAllLRPagesByDateAndSections(ObjectIdLong unitId, KDate from, KDate to, ArrayList sse, String statusId, String sec)
  {
    return new CMSService().retrieveAllLRPagesByDateAndSections(unitId, from, to, sse, statusId, sec);
  }
  


  public List<LaborRequisitionPage> retrieveLaborReqPagesByWIDWithAccess(String workorderId, ArrayList sse, String statusId, String sec)
  {
    return new CMSService().retrieveAllLRPagesByWIDAndSections(workorderId, sse, statusId, sec);
  }
  



  public void removeSchedule(ArrayList wkList, LaborRequisition lr, String fromDtm, String toDtm)
  {
    APIBeanList list = getEmployees(wkList);
    
    Log.log(3, "Loading shifts: Start");
    
    KDateSpan span = new KDateSpan(KServer.stringToDate(fromDtm), KServer.stringToDate(toDtm));
    APIBean a = loadShifts(list, span);
    APIBeanList c = (APIBeanList)a.getProperty("ScheduleItems");
    

    if (!c.isEmpty()) {
      deleteShifts(list, lr, span, a);
    }
    

    Log.log(3, "update shifts: End");
  }
  





  public void validate(HashMap patternMap, String fromDtm, String toDtm, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk)
  {
    KDate fromdt = KServer.stringToDate(fromDtm);
    KDate todt = KServer.stringToDate(toDtm);
    
    Contractor contr = wk.getContractor();
    if ((contr.getLicensevalidity2() != null) && (
      (fromdt.isAfter(contr.getLicensevalidity2())) || (todt.isAfter(contr.getLicensevalidity2())))) {
      throw CMSException.contractorLicenseValidityExpired();
    }
    

    CMSService service = new CMSService();
    
    if (contr.getCoverage() != null) {
      HashMap<KDate, Long> qtyPerDate = service.retrieveScheduleQuantityPerWorkorder(fromdt, todt, wk);
      long days = KDate.differenceInDays(todt, fromdt) + 1L;
      int i = 0;
      while (days > 0L) {
        KDate indexDate = fromdt.plusDays(i);
        Long count = (Long)qtyPerDate.get(indexDate);
        if ((count != null) && 
          (count.longValue() > Long.parseLong(contr.getCoverage()))) {
          throw CMSException.exceededCoverageQty(contr.getCoverage());
        }
        

        i++;
        days -= 1L;
      }
    }
    



    long days = KDate.differenceInDays(todt, fromdt) + 1L;
    

    HashMap<KDate, HashMap> qtyPerDate = service.retrieveScheduleQuantityPerWorkorderTradeSkill(fromdt, todt, tradeId, skillId, wk);
    HashMap<KDate, HashMap> scheQtyPerShift = getScheduleQtyPerShift(patternMap, fromdt, todt, wk);
    HashMap<KDate, HashMap> lrQtyperShift = getLRQtyPerShift(fromdt, todt, tradeId, skillId, wk);
    
    int i = 0;
    
    while (days > 0L) {
      KDate indexDate = fromdt.plusDays(i);
      HashMap eMap = (HashMap)qtyPerDate.get(indexDate);
      HashMap<ObjectIdLong, Long> shiftIdBucket = new HashMap();
      if ((eMap != null) && (!eMap.isEmpty())) {
        Collection rows = eMap.values();
        
        for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
          HashMap rMap = (HashMap)iterator.next();
          if ((rMap != null) && (!rMap.isEmpty())) {
            KDateTime startDate = (KDateTime)rMap.get("startDate");
            KDateTime endDate = (KDateTime)rMap.get("endDate");
            CMSShift shift = CMSShift.retrieveShiftByShiftStartDate(startDate, endDate, wk.getUnitId());
            if (shift != null) {
              Long total = (Long)shiftIdBucket.get(shift.getShiftId());
              if (total == null) {
                total = Long.valueOf(0L);
              }
              total = Long.valueOf(total.longValue() + 1L);
              shiftIdBucket.put(shift.getShiftId(), total);
            }
          }
        }
      }
      List<CMSShift> cmsShift = CMSShift.retrieveShifts(wk.getUnitId());
      
      for (Iterator iterator = cmsShift.iterator(); iterator.hasNext();) {
        CMSShift shift = (CMSShift)iterator.next();
        Long schedQty = (Long)shiftIdBucket.get(shift.getShiftId());
        if (schedQty == null) {
          schedQty = Long.valueOf(0L);
        }
        HashMap shiftQtyBucket = (HashMap)scheQtyPerShift.get(indexDate);
        Long toBeShceduled = (Long)shiftQtyBucket.get(shift.getShiftId());
        if (toBeShceduled == null) {
          toBeShceduled = Long.valueOf(0L);
        }
        
        schedQty = Long.valueOf(schedQty.longValue() + toBeShceduled.longValue());
        HashMap lrQtyMap = (HashMap)lrQtyperShift.get(indexDate);
        Long lrQty = (Long)lrQtyMap.get(shift.getShiftId());
        if (lrQty == null) {
          lrQty = Long.valueOf(0L);
        }
        if (schedQty.longValue() > lrQty.longValue()) {
          throw CMSException.scheduleQtyExceededOnDate(indexDate, shift.getShiftNm());
        }
      }
      

      i++;
      days -= 1L;
    }
  }
  




	public HashMap<ObjectIdLong, Long> getLRQuantityPerShift(KDate fromdt, KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId,
			Workorder wk) {

		List<LaborRequisition> lrs = (new CMSService()).retrieveApprovedLRsInRange(fromdt,todt, tradeId, skillId, wk);
		
		long days = KDate.differenceInDays(todt, fromdt)+1l;
		
		
		HashMap map = new HashMap();
		
		int i=0;
		while (days > 0){
		
		KDate index = fromdt.plusDays(i);

		if(lrs != null && !lrs.isEmpty()) {
					 
			for (Iterator iterator = lrs.iterator(); iterator.hasNext();) {
				LaborRequisition lr = (LaborRequisition) iterator.next();
				if(index.isOnOrAfterDate(lr.getFromdtm()) 
						&& index.isOnOrBeforeDate(lr.getTodtm())){
					List <LRShift> lrShifts = lr.getLrshifts();
					for (Iterator iterator2 = lrShifts.iterator(); iterator2.hasNext();) {
						LRShift lrShift = (LRShift) iterator2.next();
						Long total = (Long)map.get(lrShift.getShiftId());
						if(total == null){
							total = 0L;
						}
						total = total + lrShift.getQty();
						map.put(lrShift.getShiftId(), total);
					}
				}
			}
			
		 }
		
		 i++;
		 days--;
		}
		return map;
			
         
    }



  public HashMap<ObjectIdLong, Long> scheduleQuantityPerShift(KDate fromdt, KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk)
  {
    CMSService service = new CMSService();
    long days = KDate.differenceInDays(todt, fromdt) + 1L;
    

    HashMap<KDate, HashMap> qtyPerDate = service.retrieveScheduleQuantityPerWorkorderTradeSkill(fromdt, todt, tradeId, skillId, wk);
    
    int i = 0;
    HashMap<ObjectIdLong, Long> shiftIdBucket = new HashMap();
    
    while (days > 0L) {
      KDate indexDate = fromdt.plusDays(i);
      HashMap eMap = (HashMap)qtyPerDate.get(indexDate);
      

      if ((eMap != null) && (!eMap.isEmpty())) {
        Collection rows = eMap.values();
        for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
          HashMap rMap = (HashMap)iterator.next();
          if ((rMap != null) && (!rMap.isEmpty())) {
            KDateTime startDate = (KDateTime)rMap.get("startDate");
            KDateTime endDate = (KDateTime)rMap.get("endDate");
            CMSShift shift = CMSShift.retrieveShiftByShiftStartDate(startDate, endDate, wk.getUnitId());
            if (shift != null) {
              Long total = (Long)shiftIdBucket.get(shift.getShiftId());
              if (total == null) {
                total = Long.valueOf(0L);
              }
              total = Long.valueOf(total.longValue() + 1L);
              shiftIdBucket.put(shift.getShiftId(), total);
            }
          }
        }
      }
      
      i++;
      days -= 1L;
    }
    
    return shiftIdBucket;
  }
  



  public HashMap<ObjectIdLong, String> hasSchedulesPerPerson(List wkList, KDate fromdt, KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk)
  {
    CMSService service = new CMSService();
    long days = KDate.differenceInDays(todt, fromdt) + 1L;
    

    HashMap<KDate, HashMap> qtyPerDate = service.retrieveSchedulesPerPerson(wk.getContractorId(), fromdt, todt, tradeId, skillId);
    
    int i = 0;
    HashMap<ObjectIdLong, Long> empDays = new HashMap();
    
    while (days > 0L) {
      KDate indexDate = fromdt.plusDays(i);
      HashMap eMap = (HashMap)qtyPerDate.get(indexDate);
      for (Iterator iterator = wkList.iterator(); iterator.hasNext();) {
        Workmen wm = (Workmen)iterator.next();
        ObjectIdLong personId = wm.getPerson().getPersonId();
        if (eMap != null) {
          HashMap row = (HashMap)eMap.get(personId);
          if (row != null) {
            Long total = (Long)empDays.get(personId);
            if (total == null) {
              total = Long.valueOf(0L);
            }
            total = Long.valueOf(total.longValue() + 1L);
            empDays.put(personId, total);
          }
        }
      }
      i++;
      days -= 1L;
    }
    

    days = KDate.differenceInDays(todt, fromdt) + 1L;
    HashMap<ObjectIdLong, String> result = new HashMap();
    for (Iterator iterator = wkList.iterator(); iterator.hasNext();) {
      Workmen wm = (Workmen)iterator.next();
      ObjectIdLong personId = wm.getPerson().getPersonId();
      Long total = (Long)empDays.get(personId);
      
      if ((total == null) || (total.longValue() == 0L)) {
        result.put(personId, "Not Scheduled");
      }
      else
      {
        if ((total.longValue() < days) && (total.longValue() > 0L)) {
          result.put(personId, "Partially Scheduled");
        }
        
        if (total.longValue() >= days) {
          result.put(personId, "Fully Scheduled");
        }
      }
    }
    return result;
  }
  
  private HashMap<KDate, HashMap> getLRQtyPerShift(KDate fromdt, 
			KDate todt, ObjectIdLong tradeId, ObjectIdLong skillId, Workorder wk) {
		List<LaborRequisition> lrs = (new CMSService()).retrieveApprovedLRsInRange(fromdt,todt, tradeId, skillId, wk);
		
		long days = KDate.differenceInDays(todt, fromdt)+1l;
		HashMap<KDate, HashMap> result = new HashMap<KDate, HashMap>();
		int i=0;
		while (days > 0){
		
		KDate index = fromdt.plusDays(i);
		
		HashMap map = new HashMap();
		
		 if(lrs != null && !lrs.isEmpty()) {
					 
			for (Iterator iterator = lrs.iterator(); iterator.hasNext();) {
				LaborRequisition lr = (LaborRequisition) iterator.next();
				if(index.isOnOrAfterDate(lr.getFromdtm()) 
						&& index.isOnOrBeforeDate(lr.getTodtm())){
					List <LRShift> lrShifts = lr.getLrshifts();
					for (Iterator iterator2 = lrShifts.iterator(); iterator2.hasNext();) {
						LRShift lrShift = (LRShift) iterator2.next();
						Long total = (Long)map.get(lrShift.getShiftId());
						if(total == null){
							total = 0L;
						}
						total = total + lrShift.getQty();
						map.put(lrShift.getShiftId(), total);
					}
				}
			}
			
		 }
		 result.put(index, map);
		 i++;
		 days--;
		}
		return result;
	}
  

  private HashMap<KDate, HashMap> getScheduleQtyPerShift(HashMap patternMap, KDate fromdt, KDate todt, Workorder wk)
  {
    CMSService service = new CMSService();
    long days = KDate.differenceInDays(todt, fromdt) + 1L;
    
    int i = 0;
    
    HashMap<KDate, HashMap> dateMap = new HashMap();
    while (days > 0L) {
      KDate indexDate = fromdt.plusDays(i);
      HashMap<ObjectIdLong, Long> shiftIdBucket = new HashMap();
      for (Iterator iterator = patternMap.values().iterator(); iterator.hasNext();) {
        String shiftPattern = (String)iterator.next();
        
        APISchedulePatternBean pattern = getSchedulePattern(shiftPattern);
        APISchedulePatternBean pBean = (APISchedulePatternBean)pattern.load(new ParameterMap());
        APIBeanList pList = pBean.getPatternElements();
        int dayIndex = indexDate.dayIndex();
        KDateTime shiftStartDate = null;
        KDateTime shiftEndDate = null;
        for (Iterator iterator2 = pList.iterator(); iterator2.hasNext();) {
          APIPatternElementBean bean = (APIPatternElementBean)iterator2.next();
          if ((bean.getDayNumber().intValue() == dayIndex) && (!bean.isProperty("PayCodeName"))) {
            APIShiftCodeBean sBean = bean.getShiftCode();
            APIBeanList segs = sBean.getShiftSegments();
            if ((segs != null) && (!segs.isEmpty())) {
              APIShiftSegmentBean seg = (APIShiftSegmentBean)segs.get(0);
              shiftStartDate = KDateTime.createDateTime(KDate.today(), seg.getStartTime());
              shiftEndDate = KDateTime.createDateTime(KDate.today(), seg.getEndTime());
              break;
            }
          }
        }
        

        CMSShift shift = CMSShift.retrieveShiftByShiftStartDate(shiftStartDate, shiftEndDate, wk.getUnitId());
        if (shift != null) {
          Long total = (Long)shiftIdBucket.get(shift.getShiftId());
          if (total == null) {
            total = Long.valueOf(0L);
          }
          total = Long.valueOf(total.longValue() + 1L);
          shiftIdBucket.put(shift.getShiftId(), total);
        }
      }
      

      dateMap.put(indexDate, shiftIdBucket);
      i++;
      days -= 1L;
    }
    
    return dateMap;
  }
  

  public List<LaborRequisitionPage> retrieveLaborReqPagesForContractor(ObjectId unitId, int approved, ArrayList cCodes)
  {
    return new CMSService().retrieveLRPagesByContractor(unitId, approved, cCodes);
  }
}
