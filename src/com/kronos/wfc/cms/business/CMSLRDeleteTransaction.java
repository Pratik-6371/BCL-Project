package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public class CMSLRDeleteTransaction extends Transaction
{
  private LaborRequisition lr;
  
  public CMSLRDeleteTransaction(LaborRequisition lr)
  {
    this.lr = lr;
  }
  
  protected void transaction()
    throws SQLException
  {
    ArrayList params = new ArrayList();
    params.add(lr.getLrId().toSQLString());
    SQLStatement stmt = new SQLStatement(4, "business.cms.delete.lrshifts");
    stmt.execute(params);
    
    SQLStatement stmt1 = new SQLStatement(4, "business.cms.delete.lr");
    stmt1.execute(params);
  }
}
