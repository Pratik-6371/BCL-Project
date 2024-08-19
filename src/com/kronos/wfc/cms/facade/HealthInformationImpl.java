package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSHealthInformationTransaction;
import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.HealthInformation;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HealthInformationImpl implements HealthInformationFacade
{
  public HealthInformationImpl() {}
  
  public Map[] getWorkmenHealthRecords(String employeeCode)
  {
    List<HealthInformation> trns = new CMSService().getHealthInformationList(employeeCode);
    Map[] configs = new Map[trns.size()];
    int i = 0;
    for (Iterator iterator = trns.iterator(); iterator.hasNext();) {
      Map ws = new java.util.HashMap();
      HealthInformation wsft = (HealthInformation)iterator.next();
      ws.put("id", wsft.getId().toString());
      ws.put("healthText", wsft.getHealthText());
      ws.put("healthDesc", wsft.getHealthDesc());
      ws.put("healthTestDate", wsft.getHealthTestDate().isNull() ? "" : wsft.getHealthTestDate().getDate());
      ws.put("majorConcerns", wsft.getMajorConcerns());
      ws.put("nextMedicalTest", wsft.getNextMedicalTest().isNull() ? "" : wsft.getNextMedicalTest().getDate());
      configs[(i++)] = ws;
    }
    

    return configs;
  }
  
  public void updateHealthRecords(String personnum, List<HealthInformation> healthInformation)
  {
    if (healthInformation != null) {
      CMSHealthInformationTransaction trans = new CMSHealthInformationTransaction(personnum, healthInformation);
      trans.run();
    }
  }
}
