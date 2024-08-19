package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;



public class PrincipalEmployeeTransaction
  extends Transaction
{
  private PrincipalEmployee employee = null;
  
  public PrincipalEmployeeTransaction(PrincipalEmployee emp) {
    employee = emp;
  }
  
  protected void transaction() throws SQLException
  {
    updateEmployee();
    updateCustomProperties();
  }
  

  private void updateCustomProperties()
  {
    CMSConfigPropertyTransaction configPropertyTransaction = new CMSConfigPropertyTransaction(employee);
    configPropertyTransaction.run();
  }
  
  private void updateEmployee() {
    ArrayList params = new ArrayList();
    params.add(getConcatAddress(employee.getPeaddress(), employee.getPeaddress1(), employee.getPeaddress2(), employee.getPeaddress3()));
    params.add(employee.getManagername());
    params.add(getConcatAddress(employee.getManageraddress(), employee.getManageraddress1(), employee.getManageraddress2(), employee.getManageraddress3()));
    params.add(employee.getTypeofbussiness());
    params.add(employee.getMaxnumberofworkmen());
    params.add(employee.getMaxnumberofcontractworkmen());
    params.add(employee.getBOCWActApplicability());
    params.add(employee.getISMWActapplicability());
    params.add(employee.getLicensenumber());
    params.add(employee.getPfCode());
    params.add(employee.getEsiwc());
    params.add(((ObjectIdLong)employee.getUnitId()).toSQLString());
    SQLStatement update = new SQLStatement(4, "cms.business.Update");
    update.execute(params);
  }
  
  private String getConcatAddress(String peaddress, String peaddress1, String peaddress2, String peaddress3)
  {
    StringBuffer buffer = new StringBuffer();
    if (peaddress != null) {
      buffer.append(peaddress);
    }
    buffer.append("|");
    
    if (peaddress1 != null) {
      buffer.append(peaddress1);
    }
    buffer.append("|");
    if (peaddress2 != null) {
      buffer.append(peaddress2);
    }
    buffer.append("|");
    if (peaddress3 != null) {
      buffer.append(peaddress3);
    }
    
    return buffer.toString();
  }
}
