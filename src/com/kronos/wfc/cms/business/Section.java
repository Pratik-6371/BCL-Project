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

public class Section implements KBusinessObject
{
  private ObjectIdLong sectionId;
  private String name;
  private String code;
  private String description;
  private ObjectIdLong deptId;
  private Boolean selected;
  private static final Map<ObjectIdLong, Section> secs;
  
  static
  {
    List<Section> sks = new CMSService().getSections();
    Map<ObjectIdLong, Section> map = new HashMap();
    for (Iterator iterator = sks.iterator(); iterator.hasNext();) {
      Section sec = (Section)iterator.next();
      map.put(sec.getSectionId(), sec);
    }
    secs = Collections.unmodifiableMap(map);
  }
  


  public Section(ObjectIdLong sectionId, String name, String code, String description, ObjectIdLong deptId)
  {
    this.sectionId = sectionId;
    this.name = name;
    this.code = code;
    this.description = description;
    this.deptId = deptId;
    selected = Boolean.valueOf(false);
  }
  
  public Section() { selected = Boolean.valueOf(false); }
  
  public ObjectIdLong getSectionId() {
    return sectionId;
  }
  
  public void setSectionId(ObjectIdLong sectionId) { this.sectionId = sectionId; }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) { this.name = name; }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) { this.code = code; }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) { this.description = description; }
  
  public ObjectIdLong getDeptId() {
    return deptId;
  }
  
  public void setDeptId(ObjectIdLong depId) { deptId = depId; }
  
  public Boolean getSelected() {
    return selected;
  }
  
  public void setSelected(Boolean selected) { this.selected = selected; }
  


  public static Section retrieveSection(ObjectIdLong secId) {
	  return (Section)secs.get(secId) == null ? new CMSService().getSectionById(secId) : (Section)secs.get(secId); 
	  }
  
  public static List<Section> doRetrieveByUnitId(ObjectIdLong unitId) {
    List<Section> filter = new ArrayList();
    
    List<Section> sks = new CMSService().getSections();
    for (Iterator iterator = sks.iterator(); iterator.hasNext();) {
      Section section = (Section)iterator.next();
      Department dept = Department.doRetrieveById(section.getDeptId());
      if (dept.getUnitId().equals(unitId)) {
        filter.add(section);
      }
    }
   
    return filter;
  }
}
