package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.sql.SQLException;
import java.util.ArrayList;




public class MinimumWageTransaction
  extends Transaction
{
  private MinimumWage wage = null;
  
  public MinimumWageTransaction(MinimumWage emp)
  {
    wage = emp;
  }
  
  protected void transaction()
    throws SQLException
  {
    saveMinimumwage();
  }
  
  private void saveMinimumwage()
  {
    if (wage.getWage().getId() == null)
    {
      wage.getWage().doAdd();
    }
    else {
      wage.getWage().doUpdate();
    }
  }
  

  private void updateExistingRecord(MinimumWage oldWage)
  {
    ArrayList params = new ArrayList();
    
    KDate expiredWageDate = wage.getFrom().minusDays(1);
    params.add(expiredWageDate);
    params.add(wage.getMinimumwageId().toSQLString());
    SQLStatement save = new SQLStatement(4, "cms.business.MWUpdateExRecord");
    
    save.execute(params);
  }
}
