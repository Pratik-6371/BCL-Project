package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.Manager;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class DepartmentFacadeImpl
  implements DepartmentFacade
{
  public DepartmentFacadeImpl() {}
  
  public List<Department> getDepartments(String unitId)
  {
    return Department.doRetrieveByUnitId(new ObjectIdLong(unitId));
  }
  
  public List<Manager> getDepartmentManagers(DynamicMapBackedForm data)
  {
    Map[] managers = (Map[])data.getValue("managers");
    
    List<Manager> mgrs = new ArrayList();
    if (managers != null) {
      for (int i = 0; i < managers.length; i++) {
        Map user = managers[i];
        Manager manager = createManager(data, user);
        mgrs.add(manager);
      }
    }
    return mgrs;
  }
  


  public Manager getDepartmentManager(String managerId) { return Manager.doRetrieveById(new ObjectIdLong(managerId)); }
  
  private Manager createManager(DynamicMapBackedForm data, Map user) {
    Manager manager = new Manager();
    manager.setId(new ObjectIdLong((String)data.getValue("id")));
    manager.setName((String)user.get("name"));
    manager.setUserName((String)user.get("userName"));
    manager.setPassword((String)user.get("password"));
    manager.setMobileNum((String)user.get("mobilenum"));
    manager.setEmailAddress((String)user.get("emailaddr"));
    manager.setDeptId(new ObjectIdLong((String)data.getValue("depid")));
    if ((String)user.get("sectionId") != null)
      manager.setSectionId(new ObjectIdLong((String)user.get("sectionId")));
    manager.setIsDepartmentManager((String)user.get("isDeptMgr") == "on");
    return manager;
  }
  
  public Department getDepartment(String id)
  {
    Department department = Department.doRetrieveById(new ObjectIdLong(id));
    return department;
  }
  


  public Manager saveManager(Manager manager)
  {
    if (manager.getId() == null) {
      manager.doAdd();
    }
    else {
      manager.doUpdate();
    }
    return Manager.doRetrieveById(manager.getId());
  }
  

  public void deleteManager(DynamicMapBackedForm data)
  {
    String id = (String)data.getValue("id");
    Manager manager = Manager.doRetrieveById(new ObjectIdLong(id));
    manager.doDelete();
  }
  

  public List<Section> retrieveSections(String deptId)
  {
    List<Section> sections = new CMSService().getSections(new ObjectIdLong(deptId));
    return sections;
  }
  

  public List<CMSState> getAllStates()
  {
    return new CMSService().getStates();
  }
}
