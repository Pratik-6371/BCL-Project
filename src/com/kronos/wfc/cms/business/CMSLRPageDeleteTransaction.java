package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class CMSLRPageDeleteTransaction
  extends Transaction
{
  private LaborRequisitionPage lr;
  
  public CMSLRPageDeleteTransaction(LaborRequisitionPage laborRequisitionPage)
  {
    lr = laborRequisitionPage;
  }
  



  protected void transaction()
    throws SQLException
  {
    ArrayList<Object> params = new ArrayList();
    
    params.add(lr.getPageId().toSQLString());
    
    SQLStatement delete = new SQLStatement(4, "business.cms.DELETE_CMSLRPAGELR1M");
    delete.execute(params);
    
    List<LaborRequisition> lrs = lr.getLrs();
    List<String> lrIds = new ArrayList();
    for (Iterator iterator = lrs.iterator(); iterator.hasNext();) {
      LaborRequisition laborRequisition = (LaborRequisition)iterator.next();
      laborRequisition.doDelete();
    }
    



    SQLStatement delPage = new SQLStatement(4, "business.cms.DELETE_CMSLRPAGE");
    delPage.execute(params);
  }
}
