package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SafetyTraining
{
  private ObjectIdLong id;
  private SafetyModule module;
  private String description;
  private Trade trade;
  private String qualification;
  private Integer age;
  private String certification;
  private String experience;
  private Integer days;
  private static final Map<ObjectIdLong, SafetyModule> sftTrns;
  private static final Map<ObjectIdLong, SafetyTraining> sftTrds;
  
  static
  {
    List<SafetyModule> ts = new CMSService().getSafetyTrainings();
    Map<ObjectIdLong, SafetyModule> map = new HashMap();
    for (Iterator iterator = ts.iterator(); iterator.hasNext();) {
      SafetyModule sft = (SafetyModule)iterator.next();
      map.put(sft.getSafetyId(), sft);
    }
    sftTrns = Collections.unmodifiableMap(map);
  }   

  static
  {
    List<SafetyTraining> ts = new CMSService().getSafetyTrainingsByTrade();
    Map<ObjectIdLong, SafetyTraining> map = new HashMap();
    for (Iterator iterator = ts.iterator(); iterator.hasNext();) {
      SafetyTraining sft = (SafetyTraining)iterator.next();
      map.put(sft.getid(), sft);
    }
    sftTrds = Collections.unmodifiableMap(map);
  }
  




  public SafetyTraining() {}
  



  public SafetyTraining(ObjectIdLong id, SafetyModule module, String description, Trade trade, String qualification, Integer age, String certification, String experience, Integer days)
  {
    this.id = id;
    this.module = module;
    this.description = description;
    this.trade = trade;
    this.qualification = qualification;
    this.age = age;
    this.certification = certification;
    this.experience = experience;
    this.days = days;
  }
  
  public ObjectIdLong getid()
  {
    return id;
  }
  
  public void setid(ObjectIdLong id) {
    this.id = id;
  }
  
  public SafetyModule getModule() {
    return module;
  }
  
  public void setModule(SafetyModule module) {
    this.module = module;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Trade getTrade()
  {
    return trade;
  }
  
  public void setTrade(Trade trade)
  {
    this.trade = trade;
  }
  
  public String getQualification()
  {
    return qualification;
  }
  
  public void setQualification(String qualification)
  {
    this.qualification = qualification;
  }
  
  public Integer getAge()
  {
    return age;
  }
  
  public void setAge(Integer age)
  {
    this.age = age;
  }
  
  public String getCertification()
  {
    return certification;
  }
  
  public void setCertification(String certification)
  {
    this.certification = certification;
  }
  
  public String getExperience()
  {
    return experience;
  }
  
  public void setExperience(String experience)
  {
    this.experience = experience;
  }
  
  public Integer getDays()
  {
    return days;
  }
  
  public void setDays(Integer days)
  {
    this.days = days;
  }
  
  public static SafetyTraining getById(ObjectIdLong id) {
    return (SafetyTraining)sftTrds.get(id);
  }
  
  public static SafetyModule getModuleById(ObjectIdLong id)
  {
    return (SafetyModule)sftTrns.get(id);
  }
  
  public static Collection<SafetyModule> getUniqueTrainingList()
  {
    return sftTrns.values();
  }
  
  public static List<SafetyTraining> getSafetyTrainings()
  {
    ArrayList<SafetyTraining> list = new ArrayList();
    for (Iterator iterator = sftTrns.values().iterator(); iterator.hasNext();) {
      SafetyTraining type = (SafetyTraining)iterator.next();
      list.add(type);
    }
    
    return list;
  }
  

  public static SafetyModule getModuleByName(String name)
  {
    for (Iterator iterator = sftTrns.values().iterator(); iterator.hasNext();) {
      SafetyModule module = (SafetyModule)iterator.next();
      if (module.getName().equalsIgnoreCase(name))
        return module;
    }
    return null;
  }
}
