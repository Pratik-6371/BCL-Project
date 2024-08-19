package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class CMSLRPageTransaction
  extends Transaction
{
  private LaborRequisitionPage lrPage;
  private String unitCode;
  
  public CMSLRPageTransaction(LaborRequisitionPage laborRequisitionPage, String unitCode)
  {
    lrPage = laborRequisitionPage;
    this.unitCode = unitCode;
  }
  

  protected void transaction()
    throws SQLException
  {
    boolean isInsert = false;
    if (lrPage.getPageId() == null) {
      ObjectIdLong lrPageId = ObjectIdManager.create("CMSLABORREQPAGE");
      lrPage.setPageId(lrPageId);
      isInsert = true;
    }
    if ((lrPage.getPageNum() == null) || 
      ("".equalsIgnoreCase(lrPage.getPageNum()))) {
      ObjectIdLong pageNum = ObjectIdManager.create("CMSLABORREQPAGENUM");
      lrPage.setPageNum(unitCode + "-" + pageNum.toString());
    }
    




    if (isInsert) {
      ArrayList<Object> params = new ArrayList();
      params.add(lrPage.getPageNum());
      params.add(lrPage.getFromDtm());
      params.add(lrPage.getToDtm());
      params.add(lrPage.getApprovalSw());
      params.add(lrPage.getWorkorderId());
      params.add(lrPage.getRemark());
      params.add(KDateTime.createDateTime());
      params.add(KDateTime.createDateTime());
      params.add(CurrentUserAccountManager.getUserAccountName());
      params.add(lrPage.getWeeklyOffDays());
      params.add(lrPage.getPageId());
      
      SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_LR_PAGE");
      insert.execute(params);
      
      for (Iterator iterator = lrPage.getLrs().iterator(); iterator.hasNext();) {
        LaborRequisition lr = (LaborRequisition)iterator.next();
        lr.doUpdate(unitCode);
        
        ArrayList pms = new ArrayList();
        pms.add(lrPage.getPageId().toSQLString());
        pms.add(lr.getLrId().toSQLString());
        SQLStatement mInsert = new SQLStatement(4, "business.cms.INSERT_LR_PAGE_MM");
        mInsert.execute(pms);
      }
    }
    else
    {
      ArrayList<Object> params = new ArrayList();
      params.add(lrPage.getPageNum());
      params.add(lrPage.getFromDtm());
      params.add(lrPage.getToDtm());
      params.add(lrPage.getApprovalSw());
      params.add(lrPage.getWorkorderId());
      params.add(lrPage.getRemark());
      params.add(KDateTime.createDateTime());
      params.add(CurrentUserAccountManager.getUserAccountName());
      params.add(lrPage.getPageId());
      SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_LR_PAGE");
      update.execute(params);
      
      ArrayList pms1 = new ArrayList();
      pms1.add(lrPage.getPageId().toSQLString());
      SQLStatement mDelete = new SQLStatement(4, "business.cms.DELETE_LR_PAGE_MM");
      mDelete.execute(pms1);
      

      for (Iterator iterator = lrPage.getLrs().iterator(); iterator.hasNext();) {
        LaborRequisition lr = (LaborRequisition)iterator.next();
        lr.doUpdate(unitCode);
        

        ArrayList pms = new ArrayList();
        pms.add(lrPage.getPageId().toSQLString());
        pms.add(lr.getLrId().toSQLString());
        SQLStatement mInsert = new SQLStatement(4, "business.cms.INSERT_LR_PAGE_MM");
        mInsert.execute(pms);
      }
    }
  }
  




  public LaborRequisitionPage getLaborRequisitionPage()
  {
    return lrPage;
  }
}
