package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import java.sql.SQLException;
import java.util.ArrayList;




public class ContractorTransaction
  extends Transaction
{
  public static final String RETRIEVEBYNAME = "RetrieveByName";
  private Contractor contr = null;
  
  public ContractorTransaction(Contractor contr) {
    this.contr = contr;
  }
  
  protected void transaction()
    throws SQLException
  {
    ArrayList<String> parm = new ArrayList();
    parm.add(getConcactAddress(contr.getCaddress(), contr.getCaddress1(), contr.getCaddress2(), contr.getCaddress3()));
    parm.add(contr.getCcode());
    parm.add(((ObjectIdLong)contr.getcontractorid()).toSQLString());
    SQLStatement update = new SQLStatement(4, "cms.business.UPDATECONTRACTOR");
    update.execute(parm);
    
    ArrayList params = new ArrayList();
    params.add(contr.getManagername());
    params.add(contr.getEsiwcnumber());
    params.add(contr.getLicensenumber());
    params.add(contr.getLicensevalidity1());
    params.add(contr.getLicensevalidity2());
    params.add(contr.getCoverage());
    params.add(contr.getTotalstrength());
    params.add(contr.getMaxnoofemployees());
    params.add(contr.getNatureofwork());
    params.add(contr.getLocationofcontractwork());
    params.add(contr.getPeriodofcontract1());
    params.add(contr.getPeriodofcontract2());
    params.add(contr.getPfcode());
    params.add(contr.getEsistdt());
    params.add(contr.getEsieddt());
    params.add(contr.getESIwcCoverage());
    params.add(contr.getPfnumber());
    params.add(contr.getPfcodeapplicationdate());
    params.add(contr.getCommission());
    params.add(contr.getPfCoverage());
    params.add(Integer.valueOf(contr.getIsPfSelf().booleanValue() ? 1 : 0));
    params.add(Integer.valueOf(contr.getIsEsiSelf().booleanValue() ? 1 : 0));
    params.add(contr.getLicensenumber1());
    params.add(contr.getLicensevalidityFrom());
    params.add(contr.getLicensevalidityTo());
    
    params.add(((ObjectIdLong)contr.getcontractorid()).toSQLString());
    params.add(contr.getUnitId().toSQLString());
    
    
    SQLStatement update1 = new SQLStatement(4, "cms.business.UPDATECONTRACTOR1M");
    update1.execute(params);
  }
  

  private String getConcactAddress(String caddress, String caddress1, String caddress2, String caddress3)
  {
    StringBuffer buffer = new StringBuffer();
    
    if (caddress != null)
    {
      buffer.append(caddress);
    }
    buffer.append("|");
    
    if (caddress1 != null)
    {
      buffer.append(caddress1);
    }
    
    buffer.append("|");
    
    if (caddress2 != null)
    {
      buffer.append(caddress2);
    }
    buffer.append("|");
    
    if (caddress3 != null)
    {
      buffer.append(caddress3);
    }
    return buffer.toString();
  }
}
