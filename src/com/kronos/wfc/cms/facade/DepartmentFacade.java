package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.Manager;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.List;

public abstract interface DepartmentFacade
{
  public abstract List<Department> getDepartments(String paramString);
  
  public abstract List<Manager> getDepartmentManagers(DynamicMapBackedForm paramDynamicMapBackedForm);
  
  public abstract Department getDepartment(String paramString);
  
  public abstract Manager getDepartmentManager(String paramString);
  
  public abstract Manager saveManager(Manager paramManager);
  
  public abstract void deleteManager(DynamicMapBackedForm paramDynamicMapBackedForm);
  
  public abstract List<Section> retrieveSections(String paramString);
  
  public abstract List<CMSState> getAllStates();
}
