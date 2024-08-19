package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WorkorderType
{
  private ObjectIdLong typeId;
  private String name;
  private String sapType;
  private String shortDesc;
  private String longDesc;
  private static final Map<ObjectIdLong, WorkorderType> types;
  
  static
  {
    List<WorkorderType> tps = new CMSService().getWorkorderTypes();
    Map<ObjectIdLong, WorkorderType> map = new HashMap();
    for (Iterator iterator = tps.iterator(); iterator.hasNext();) {
      WorkorderType workorderType = (WorkorderType)iterator.next();
      map.put(workorderType.typeId, workorderType);
    }
    types = Collections.unmodifiableMap(map);
  }
  
  public WorkorderType(ObjectIdLong typeId, String name, String sapType, String shortDesc, String longDesc)
  {
    this.typeId = typeId;
    this.name = name;
    this.sapType = sapType;
    this.shortDesc = shortDesc;
    this.longDesc = longDesc;
  }
  
  public ObjectIdLong getTypeId() { return typeId; }
  
  public void setTypeId(ObjectIdLong typeId) {
    this.typeId = typeId;
  }
  
  public String getName() { return name; }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getSapType() { return sapType; }
  
  public void setSapType(String sapType) {
    this.sapType = sapType;
  }
  
  public String getShortDesc() { return shortDesc; }
  
  public void setShortDesc(String shortDesc) {
    this.shortDesc = shortDesc;
  }
  
  public String getLongDesc() { return longDesc; }
  
  public void setLongDesc(String longDesc) {
    this.longDesc = longDesc;
  }
  
  public static Collection<WorkorderType> getAllTypes() {
    return types.values();
  }
  
  public static WorkorderType getByTypeId(ObjectIdLong typeId) {
    return (WorkorderType)types.get(typeId);
  }
}
