package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





public class CMSLRTransaction
  extends Transaction
{
  private LaborRequisition laborRequisition;
  private String unitCode;
  
  public CMSLRTransaction(LaborRequisition lr, String unitCode)
  {
    laborRequisition = lr;
    this.unitCode = unitCode;
  }
  

  protected void transaction()
    throws SQLException
  {
    boolean isInsert = false;
    if (laborRequisition.getLrId() == null) {
      ObjectIdLong lrId = ObjectIdManager.create("CMSLABORREQ");
      laborRequisition.setLrId(lrId);
      isInsert = true;
    }
    if ((laborRequisition.getLrNum() == null) || 
      ("".equalsIgnoreCase(laborRequisition.getLrNum()))) {
      ObjectIdLong lrnum = ObjectIdManager.create("CMSLABORREQNUM");
      laborRequisition.setLrNum(unitCode + "-" + lrnum.toString());
    }
    ArrayList<Object> params = new ArrayList();
    params.add(laborRequisition.getLrName());
    params.add(laborRequisition.getLrNum());
    params.add(laborRequisition.getFromdtm());
    params.add(laborRequisition.getTodtm());
    params.add(laborRequisition.getWorkOrderlnId());
    params.add(laborRequisition.getTradeId());
    params.add(laborRequisition.getSkillId());
    params.add(laborRequisition.getLrId());
    





    if (isInsert) {
      SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_LR");
      insert.execute(params);
      


      List<LRShift> shifts = laborRequisition.getLrshifts();
      if (shifts != null) {
        for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
          LRShift lrShift = (LRShift)iterator.next();
          lrShift.setLrId(laborRequisition.getLrId());
        }
      }
      insertShifts(shifts);


    }
    else
    {

      SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_LR");
      update.execute(params);
      updateShifts(laborRequisition.getLrshifts());
    }
  }
  


  private void updateShifts(List<LRShift> lrshifts)
  {
    if ((lrshifts != null) && (!lrshifts.isEmpty())) {
      for (Iterator iterator = lrshifts.iterator(); iterator.hasNext();) {
        LRShift lrShift = (LRShift)iterator.next();
        lrShift.update();
      }
    }
  }
  





  private void insertShifts(List<LRShift> lrshifts)
  {
    if ((lrshifts != null) && (!lrshifts.isEmpty())) {
      for (Iterator iterator = lrshifts.iterator(); iterator.hasNext();) {
        LRShift lrShift = (LRShift)iterator.next();
        lrShift.insert();
      }
    }
  }
  
  public LaborRequisition getLaborRequisition()
  {
    return laborRequisition;
  }
}
