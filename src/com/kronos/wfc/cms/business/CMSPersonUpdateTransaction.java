package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;

public class CMSPersonUpdateTransaction
  extends Transaction
{
  private Workmen workmen;
  
  public CMSPersonUpdateTransaction(Workmen workmen)
  {
    this.workmen = workmen;
  }
  
  protected void transaction()
    throws SQLException
  {
    WorkmenService service = new WorkmenService();
    
    service.updatePersonData(workmen, "update");
    
    Address presentAddress = workmen.getPresentAddress();
    if (presentAddress != null) {
      presentAddress.doUpdate();
    }
    
    Address permanentAddress = workmen.getPermAddress();
    if (permanentAddress != null) {
      permanentAddress.doUpdate();
    }
    
    if (workmen.getDetail() != null) {
      workmen.getDetail().doUpdate();
    }
    
    if (workmen.getWage() != null) {
      workmen.getWage().doUpdate();
    }
    
    ArrayList<Object> params = new ArrayList();
    
    params.add(workmen.getFirstName());
    params.add(workmen.getLastName());
    params.add(workmen.getRelationName());
    params.add(workmen.getDOB());
    params.add(workmen.getIdMark());
    params.add(workmen.getTrade().getTradeId().toSQLString());
    params.add(workmen.getSkill().getSkillId().toSQLString());
    params.add(workmen.getPresentAddress().getId().toSQLString());
    params.add(workmen.getPermAddress().getId().toSQLString());
    params.add(workmen.getDetail().getPersonDetlId().toSQLString());
    params.add(workmen.getWage().getId());
    params.add(workmen.getContractor().getcontractorid());
    params.add(workmen.getStatusID());
    params.add(workmen.getEmpCode());
    params.add(workmen.isBSR());
    params.add(workmen.getContractor().getUnitId());
    params.add(workmen.getEmpId().toSQLString());
    
    SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_CMSPERSON");
    update.execute(params);
  }
}
