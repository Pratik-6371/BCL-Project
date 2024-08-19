package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class ManagerInsertTransaction
  extends Transaction
{
  private Manager manager;
  
  public ManagerInsertTransaction(Manager manager)
  {
    this.manager = manager;
  }
  
  protected void transaction()
    throws SQLException
  {
    ObjectIdLong id = ObjectIdManager.create("CMSMANAGERS");
    manager.setId(id);
    
    ManagerService service = new ManagerService();
    
    ObjectIdLong personId = service.updatePersonData(manager, "insert");
    
    ArrayList<Object> params = new ArrayList();
    params.add(id.toSQLString());
    params.add(manager.getName());
    params.add(manager.getDeptId().toSQLString());
    if (manager.getSectionId().longValue() != -1L) {
      params.add(manager.getSectionId().toSQLString());
    } else {
      params.add(null);
    }
    params.add(manager.getUserName());
    params.add(manager.getPasswd());
    params.add(manager.getEmailAddr());
    params.add(manager.getMobilenum());
    params.add(Integer.valueOf(manager.getIsDeptMgr().booleanValue() ? 1 : 0));
    params.add(personId.toSQLString());
    
    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_MANAGER");
    insert.execute(params);
  }
}
