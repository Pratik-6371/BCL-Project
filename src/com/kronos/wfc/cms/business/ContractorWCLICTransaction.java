package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;

import java.sql.SQLException;
import java.util.ArrayList;




public class ContractorWCLICTransaction extends Transaction
{
  public static final String RETRIEVEBYNAME = "RetrieveByName";
  private Contractor contr = null;
  private Contractorwclic contractorwclic = null;
  
  public ContractorWCLICTransaction(Contractor contr, Contractorwclic contractorwclic) {
    this.contr = contr;
    this.contractorwclic=contractorwclic;
  }
  
  protected void transaction()
    throws SQLException
  {
	  	ArrayList<Object> params = new ArrayList<Object>();
		params.add(contractorwclic.getWc1());
		params.add(contr.getcontractorid().toString());
		params.add(contr.getUnitId().toSQLString());
		params.add(contractorwclic.getWc1FromDate());
		params.add(contractorwclic.getWc1ToDate());
		params.add(contractorwclic.getWc1Coverage());
		params.add(KDate.today());
		params.add(contr.getcontractorid().toString());
		params.add(contr.getUnitId().toSQLString());
		params.add("WC1");
		SQLStatement update = new SQLStatement(4, "business.cms.UPDATE_CONTRACTOR_WC");
		update.execute(params);
		
		ArrayList<Object> params1 = new ArrayList<Object>();
		params1.add(contractorwclic.getWc2());
		params1.add(contr.getcontractorid().toString());
		params1.add(contr.getUnitId().toSQLString());
		params1.add(contractorwclic.getWc2FromDate());
		params1.add(contractorwclic.getWc2ToDate());
		params1.add(contractorwclic.getWc2Coverage());
		params1.add(KDate.today());
		params1.add(contr.getcontractorid().toString());
		params1.add(contr.getUnitId().toSQLString());
		params1.add("WC2");
		SQLStatement update1 = new SQLStatement(4, "business.cms.UPDATE_CONTRACTOR_WC");
		update1.execute(params1);
		
		ArrayList<Object> params2 = new ArrayList<Object>();
		params2.add(contractorwclic.getWc3());
		params2.add(contr.getcontractorid().toString());
		params2.add(contr.getUnitId().toSQLString());
		params2.add(contractorwclic.getWc3FromDate());
		params2.add(contractorwclic.getWc3ToDate());
		params2.add(contractorwclic.getWc3Coverage());
		params2.add(KDate.today());
		params2.add(contr.getcontractorid().toString());
		params2.add(contr.getUnitId().toSQLString());
		params2.add("Wc3");
		SQLStatement update2 = new SQLStatement(4, "business.cms.UPDATE_CONTRACTOR_WC");
		update2.execute(params2); 
		
		
		ArrayList<Object> params3 = new ArrayList<Object>();
		params3.add(contractorwclic.getWc4());
		params3.add(contr.getcontractorid().toString());
		params3.add(contr.getUnitId().toSQLString());
		params3.add(contractorwclic.getWc4FromDate());
		params3.add(contractorwclic.getWc4ToDate());
		params3.add(contractorwclic.getWc4Coverage());
		params3.add(KDate.today());
		params3.add(contr.getcontractorid().toString());
		params3.add(contr.getUnitId().toSQLString());
		params3.add("Wc4");
		SQLStatement update3 = new SQLStatement(4, "business.cms.UPDATE_CONTRACTOR_WC");
		update3.execute(params3);
	  
  }
}
