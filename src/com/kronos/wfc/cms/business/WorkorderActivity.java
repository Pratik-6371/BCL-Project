package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;

public class WorkorderActivity
{
  private ObjectIdLong id;
  private String name;
  private String value;
  private ObjectIdLong workorderId;
  
  public WorkorderActivity(ObjectIdLong id, String name, String value)
  {
    this.id = id;
    this.name = name;
    this.value = value;
  }
  
  public ObjectIdLong getId() { return id; }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public String getName() { return name; }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getValue() { return value; }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public static List<WorkorderActivity> retrieveActiviesByWorkorder(ObjectIdLong workorderId)
  {
    return new CMSService().retrieveWOActivitiesByWOId(workorderId);
  }
  
  public ObjectIdLong getWorkorderId() { return workorderId; }
  
  public void setWorkorderId(ObjectIdLong workorderId) {
    this.workorderId = workorderId;
  }
}
