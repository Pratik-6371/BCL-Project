package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSWorkmenSafetyTransaction;
import com.kronos.wfc.cms.business.CMSWorkmenSafetyViolationTransaction;
import com.kronos.wfc.cms.business.WorkmenSafetyTraining;
import com.kronos.wfc.cms.business.WorkmenSafetyViolation;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SafetyFacadeImpl implements SafetyFacade
{
  public SafetyFacadeImpl() {}
  
  public Map[] getWorkmenSafetyTrainings(String employeeCode)
  {
    List<WorkmenSafetyTraining> trns = new CMSService().getWorkmenSafetyTrainings(employeeCode);
    Map[] configs = new Map[trns.size()];
    int i = 0;
    for (Iterator iterator = trns.iterator(); iterator.hasNext();) {
      Map ws = new HashMap();
      WorkmenSafetyTraining wsft = (WorkmenSafetyTraining)iterator.next();
      ws.put("id", wsft.getId().toString());
      ws.put("trnId", wsft.getTrn().getSafetyId().toString());
      ws.put("dateTaken", wsft.getDateTaken().isNull() ? "" : wsft.getDateTaken().getDate());
      ws.put("fromTime", wsft.getFromTime().isNull() ? "" : com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration.hoursMinutes(wsft.getFromTime().getHour(), wsft.getFromTime().getMinute()));
      ws.put("toTime", wsft.getToTime().isNull() ? "" : com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration.hoursMinutes(wsft.getToTime().getHour(), wsft.getToTime().getMinute()));
      ws.put("trnDesc", wsft.getTrnDesc());
      ws.put("nextTrnDa", wsft.getNextTrnDate().isNull() ? "" : wsft.getNextTrnDate());
      ws.put("dept", wsft.getDept().getDepid());
      ws.put("sec", wsft.getSec().getSectionId());
      ws.put("func", wsft.getFunc());
      ws.put("nJob", wsft.getnJob());
      ws.put("module", wsft.getModule());
      ws.put("TNI", wsft.getTNI());
      ws.put("facultyNm", wsft.getFacultyNm());
      ws.put("venue", wsft.getVenue());
      ws.put("preTestMarksObtained", wsft.getPreTestMarksObtained());
      ws.put("preTestMaxMarks", wsft.getPreTestMaxMarks());
      ws.put("preTestPercentage", wsft.getPreTestPercentage());
      ws.put("postTestMarksObtained", wsft.getPostTestMarksObtained());
      ws.put("postTestMaxMarks", wsft.getPostTestMaxMarks());
      ws.put("postTestPercentage", wsft.getPostTestPercentage());
      ws.put("recommendation", wsft.getRecommendation());
      ws.put("remarks", wsft.getRemarks());
      configs[(i++)] = ws;
    }
    return configs;
  }
  

  public void updateTrainings(String personNum, Map[] wsMaps)
  {
    if (wsMaps != null) {
      CMSWorkmenSafetyTransaction trans = new CMSWorkmenSafetyTransaction(personNum, wsMaps);
      trans.run();
    }
  }
  

  public Map[] getWorkmenSafetyViolations(String empCode)
  {
    List<WorkmenSafetyViolation> trns = new CMSService().getWorkmenSafetyViolations(empCode);
    Map[] configs = new Map[trns.size()];
    int i = 0;
    for (Iterator iterator = trns.iterator(); iterator.hasNext();) {
      Map ws = new HashMap();
      WorkmenSafetyViolation wsft = (WorkmenSafetyViolation)iterator.next();
      ws.put("id", wsft.getId().toString());
      ws.put("sId", wsft.getSafetyViolation().getSafetyViolationId());
      ws.put("violationDesc", wsft.getViolationDesc());
      ws.put("violationDate", wsft.getViolationDate().isNull() ? "" : wsft.getViolationDate().getDate());
      ws.put("actionTaken", wsft.getActionTaken());
      configs[(i++)] = ws;
    }
    return configs;
  }
  

  public void updateViolations(String empCode, Map[] wsMaps)
  {
    if (wsMaps != null) {
      CMSWorkmenSafetyViolationTransaction trans = new CMSWorkmenSafetyViolationTransaction(empCode, wsMaps);
      trans.run();
    }
  }
}
