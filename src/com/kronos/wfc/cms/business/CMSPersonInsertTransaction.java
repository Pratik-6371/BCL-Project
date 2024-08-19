package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.commonapp.accessgrp.business.exceptions.DAGProcessException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.PersistenceException;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.xml.api.bean.APIProcessingException;
import com.kronos.wfc.platform.xml.api.bean.APIValidationException;
import com.kronos.wfc.wfp.logging.Log;

import java.sql.SQLException;
import java.util.ArrayList;


public class CMSPersonInsertTransaction
  extends Transaction
{
  private Workmen workmen;
  //private String unitId;
  
  public CMSPersonInsertTransaction(Workmen workmen)
  {
    this.workmen = workmen;
  }
  
  protected void transaction()
    throws SQLException
  {	  
	WorkmenService service = new WorkmenService();
    String unitId = workmen.getUnitId();
  /*  PrincipalEmployee pe = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
    String unitCode =  pe.getUnitCode();*/
    String empCode="";
   // String counter="";
    empCode = new CMSService().getNewEmpCode(unitId);
    //empCode = unitCode + counter;
    workmen.setEmpCode(empCode);
    
    ObjectIdLong personId = service.updatePersonData(workmen, "insert");
    
    if ((workmen.getPresentAddress() != null) && (!Address.DEFAULT_ADDRESS_ID.equals(workmen.getPresentAddress().getId()))) {
      workmen.getPresentAddress().doAdd();
    }
    
    if ((workmen.getPermAddress() != null) && (!Address.DEFAULT_ADDRESS_ID.equals(workmen.getPermAddress().getId()))) {
      workmen.getPermAddress().doAdd();
    }
    
    if (workmen.getDetail() != null) {
      workmen.getDetail().doAdd();
    }
    
    if (workmen.getWage() != null) {
      workmen.getWage().doAdd();
    }
    
    ObjectIdLong id = ObjectIdManager.create("CMSPERSON");
    ArrayList<Object> params = new ArrayList();
    params.add(id.toSQLString());
    params.add(workmen.getEmpCode());
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
    params.add(personId.toSQLString());
    params.add(workmen.isBSR());
    params.add(unitId);
    workmen.setEmpId(id);
    SQLStatement insert = new SQLStatement(4, "business.cms.INSERT_CMSPERSON");
    insert.execute(params);
    new CMSService().updateEmpSeqTable(unitId);
	 
  }
}
