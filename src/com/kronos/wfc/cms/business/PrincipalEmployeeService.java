package com.kronos.wfc.cms.business;

import com.ibm.icu.util.StringTokenizer;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import com.kronos.wfc.platform.utility.framework.collator.CollatorManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrincipalEmployeeService
{
  public PrincipalEmployeeService() {}
  
  public PrincipalEmployee retrieveByCode(String unitCode)
  {
    ArrayList<String> params = new ArrayList();
    params.add(unitCode);
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_BY_CODE", params);
    if (ds.nextRow()) {
      return createPEObject(ds);
    }
    return null;
  }
  
  public List<PrincipalEmployee> retrieveAll()
  {
    ArrayList<String> params = new ArrayList();
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_ALL", params);
    return createPEObjects(ds);
  }
  

  private PrincipalEmployee createPEObject(DataStoreIfc ds)
  {
    ObjectIdLong id = ds.getObjectIdLong(1);
    String name = ds.getString(2);
    String peaddress = getAddress(ds.getString(3), 0);
    
    String peaddress1 = getAddress(ds.getString(3), 1);
    String peaddress2 = getAddress(ds.getString(3), 2);
    String peaddress3 = getAddress(ds.getString(3), 3);
    
    String managername = ds.getString(4);
    String manageraddress = getAddress(ds.getString(5), 0);
    String manageraddress1 = getAddress(ds.getString(5), 1);
    String manageraddress2 = getAddress(ds.getString(5), 2);
    String manageraddress3 = getAddress(ds.getString(5), 3);
    String typeofbusiness = ds.getString(6);
    String maxnumberofworkmen = ds.getString(7);
    String maxnumberofcontractworkmen = ds.getString(8);
    Boolean boc = ds.getBoolean(9);
    Boolean ism = ds.getBoolean(10);
    String license = ds.getString(11);
    String pfCode = ds.getString(12);
    String esiwc = ds.getString(13);
    String unitCode = ds.getString(14);
    String organization = ds.getString(15);
    
    PrincipalEmployee employee = new PrincipalEmployee();
    employee.setUnitID(id.longValue());
    employee.setUnitName(name);
    employee.setPeaddress(peaddress);
    employee.setPeaddress1(peaddress1);
    employee.setPeaddress2(peaddress2);
    employee.setPeaddress3(peaddress3);
    employee.setManagername(managername);
    employee.setManageraddress(manageraddress);
    employee.setManageraddress1(manageraddress1);
    employee.setManageraddress2(manageraddress2);
    employee.setManageraddress3(manageraddress3);
    employee.setTypeofbussiness(typeofbusiness);
    employee.setMaxnumberofworkmen(maxnumberofworkmen);
    employee.setMaxnumberofcontractworkmen(maxnumberofcontractworkmen);
    employee.setBOCWActApplicability(boc);
    employee.setISMWActapplicability(ism);
    employee.setLicensenumber(license);
    employee.setPfCode(pfCode);
    employee.setEsiwc(esiwc);
    employee.setUnitCode(unitCode);
    employee.setOrganization(organization);
    ArrayList params = new ArrayList();
    params.add(id.toSQLString());
    ds = getDataStoreFromSQLStatement("business.cms.SELECT_PE_PROPS_BY_UNITID", params);
    ArrayList<CustomProperties> propertyList = new ArrayList();
    employee.setProperties(propertyList);
    CustomProperties property;
    for (; ds.nextRow(); propertyList.add(property))
    {
      ObjectIdLong custId = ds.getObjectIdLong(1);
      String fieldName = ds.getString(2);
      String fieldValue = ds.getString(3);
      
      property = new CustomProperties(custId, fieldName, fieldValue, id);
    }
    
    if (!propertyList.isEmpty()) {
      java.util.Collections.sort(propertyList, new CustomPropertiesComparator(CollatorManager.getCollator()));
    }
    
    return employee;
  }
  
  private List<PrincipalEmployee> createPEObjects(DataStoreIfc ds)
  {
    ArrayList<PrincipalEmployee> list = new ArrayList();
    
    while (ds.nextRow())
    {
      PrincipalEmployee employee = createPEObject(ds);
      list.add(employee);
    }
    return list;
  }
  



  private String getAddress(String address, int i)
  {
    if (address != null) {
      StringTokenizer st2 = new StringTokenizer(address, "|");
      int j = 0;
      String result = "";
      while (st2.hasMoreElements()) {
        result = (String)st2.nextElement();
        if (i == j)
          return result;
        j++;
      }
    }
    

    return null;
  }
  
  private static DataStoreIfc getDataStoreFromSQLStatement(String sqlStatement, ArrayList<String> params)
  {
    SQLStatement select = new SQLStatement(3, sqlStatement);
    select.execute(params);
    DataStoreIfc ds = select.getDataStore();
    return ds;
  }
  
  public PrincipalEmployee retrieveById(ObjectIdLong unitId)
  {
    ArrayList<String> params = new ArrayList();
    //params.add(((ObjectIdLong)unitId).toSQLString());
    params.add(unitId.toSQLString());
    DataStoreIfc ds = getDataStoreFromSQLStatement("business.cms.SELECT_BY_ID", params);
    return createPEObject(ds);
  }
  
  public void updatePrincipalEmployee(List employees)
  {
    if ((employees != null) && (!employees.isEmpty())) {
      Iterator<PrincipalEmployee> iterator = employees.iterator();
      while (iterator.hasNext()) {
        PrincipalEmployee employee = (PrincipalEmployee)iterator.next();
        updateEmployee(employee);
      }
    }
  }
  



  private void updateEmployee(PrincipalEmployee employee)
  {
    PrincipalEmployeeTransaction trans = new PrincipalEmployeeTransaction(employee);
    trans.run();
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
