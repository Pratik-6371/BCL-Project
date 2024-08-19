package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;






public class CMSPersonRehireTransaction
  extends Transaction
{
  private Workmen workmen;
  
  public CMSPersonRehireTransaction(Workmen workmen)
  {
    this.workmen = workmen;
  }
  
  protected void transaction()
    throws SQLException
  {
    WorkmenService service = new WorkmenService();
    
    service.rehirePerson(workmen, "rehire");
    
    if (workmen.getWage() != null) {
        workmen.getWage().doUpdate();
      }
    
    ArrayList<Object> params = new ArrayList();
    
    params.add(Integer.valueOf(0));
    params.add(workmen.getTrade().getTradeId().toSQLString());
    params.add(workmen.getSkill().getSkillId().toSQLString());
    params.add(workmen.getUnitId().toString());
    params.add(workmen.getContractor().getcontractorid());
    params.add(workmen.getEmpId().toSQLString());
    SQLStatement insert = new SQLStatement(4, "business.cms.REHIRE_CMSPERSON");
    insert.execute(params);
    

    ArrayList<Object> params1 = new ArrayList();
    params1.add(workmen.getDetail().getDoj());
    params1.add(workmen.getDetail().getDot());
    params1.add(workmen.getEmpId().toSQLString());
    SQLStatement insert1 = new SQLStatement(4, "business.cms.REHIRE_CMSPERSONDETL");
    insert1.execute(params1);
  }
}
