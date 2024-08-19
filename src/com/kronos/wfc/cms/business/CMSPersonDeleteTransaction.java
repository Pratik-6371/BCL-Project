package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public class CMSPersonDeleteTransaction
  extends Transaction
{
  private Workmen workmen;
  
  public CMSPersonDeleteTransaction(Workmen workmen)
  {
    this.workmen = workmen;
  }
  
  protected void transaction() throws SQLException
  {
    if (workmen.getPresentAddress() != null) {
      workmen.getPresentAddress().doDelete();
    }
    
    if (workmen.getPermAddress() != null) {
      workmen.getPermAddress().doDelete();
    }
    
    if (workmen.getDetail() != null) {
      workmen.getDetail().doDelete();
    }
    
    if (workmen.getWage() != null) {
      workmen.getWage().doDelete();
    }
    
    ArrayList<Object> params = new ArrayList();
    
    params.add(workmen.getEmpId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.DELETE_WORKMEN");
    update.execute(params);
  }
}
