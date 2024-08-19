package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SafetyViolation
{
  private static Map<ObjectIdLong, SafetyViolation> sftVlns;
  private ObjectIdLong safetyViolationId;
  private String name;
  private String description;
  
  static
  {
    List<SafetyViolation> ts = new CMSService().getSafetyViolations();
    Map<ObjectIdLong, SafetyViolation> map = new HashMap();
    for (Iterator iterator = ts.iterator(); iterator.hasNext();) {
      SafetyViolation sft = (SafetyViolation)iterator.next();
      map.put(sft.getSafetyViolationId(), sft);
    }
    sftVlns = Collections.unmodifiableMap(map);
  }
  
  public SafetyViolation(ObjectIdLong safetyVId, String name, String description)
  {
    safetyViolationId = safetyVId;
    this.name = name;
    this.description = description;
  }
  
  public ObjectIdLong getSafetyViolationId() {
    return safetyViolationId;
  }
  
  public void setSafetyViolationId(ObjectIdLong safetyId) {
    safetyViolationId = safetyId;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public static List<SafetyViolation> getViolations() {
    List<SafetyViolation> vlns = new ArrayList();
    vlns.addAll(sftVlns.values());
    return vlns;
  }
  
  public static SafetyViolation getViolationById(ObjectIdLong id) { return (SafetyViolation)sftVlns.get(id); }
}
