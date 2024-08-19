package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.businessobject.framework.KBusinessObject;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Department implements KBusinessObject
{
  private ObjectIdLong depid;
  private String name;
  private String code;
  private String description;
  private ObjectIdLong unitId;
  private static final Map<ObjectIdLong, Department> deps;
  
  static
  {
    List<Department> sks = new CMSService().getDepartments();
    Map<ObjectIdLong, Department> map = new TreeMap();
    for (Iterator iterator = sks.iterator(); iterator.hasNext();) {
      Department dep = (Department)iterator.next();
      map.put(dep.getDepid(), dep);
    }
    deps = Collections.unmodifiableMap(map);
  }
  



  public Department(ObjectIdLong depid, String code, String description, ObjectIdLong unitId)
  {
    this.depid = depid;
    this.code = code;
    this.description = description;
    this.unitId = unitId;
  }
  
  public ObjectIdLong getDepid() {
    return depid;
  }
  
  public void setDepid(ObjectIdLong depid) {
    this.depid = depid;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public ObjectIdLong getUnitId() {
    return unitId;
  }
  
  public void setUnitId(ObjectIdLong unitId) {
    this.unitId = unitId;
  }
  
  public static List<Department> doRetrieveByUnitId(ObjectIdLong unitId)
  {
    List<Department> list = new ArrayList();
    if (unitId != null) {
      for (Iterator iterator = deps.values().iterator(); iterator.hasNext();) {
        Department department = (Department)iterator.next();
        if (department.getUnitId().longValue() == unitId.longValue()) {
          list.add(department);
        }
      }
    }
    if(list.isEmpty() || list == null)
    {
    	list = new CMSService().getDepartmentsByUnit(unitId);
    }
    Collections.sort(list, new DepartmentComparator());
    return list;
  }
  
  public static Department doRetrieveById(ObjectIdLong depId) {
    return (Department)deps.get(depId) == null ? new CMSService().getDepartmentByDepartmentId(depId) : (Department)deps.get(depId);
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
