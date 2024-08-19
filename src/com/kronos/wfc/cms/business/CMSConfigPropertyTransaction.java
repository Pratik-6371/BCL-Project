package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class CMSConfigPropertyTransaction
  extends Transaction
{
  public static final boolean DELETE = true;
  private static final String DELETE_ALL_CONFIG_PROPERTIES = "business.cms.DELETE_ALL_CONFIG_PROPERTIES";
  private static final String INSERT_CONFIG_PROPERTY = "business.cms.INSERT_CONFIG_PROPERTY";
  private static final String TABLE_NAME = "CMSPRINCIPALEMP1M";
  private boolean isDelete;
  PrincipalEmployee webConfig;
  
  public CMSConfigPropertyTransaction(PrincipalEmployee webConfig)
  {
    isDelete = false;
    this.webConfig = webConfig;
  }
  
  public CMSConfigPropertyTransaction(PrincipalEmployee webConfig, boolean delete)
  {
    isDelete = false;
    this.webConfig = webConfig;
    isDelete = delete;
  }
  
  public void transaction()
    throws SQLException
  {
    List propertyList = webConfig.getProperties();
    ObjectIdLong cid = (ObjectIdLong)webConfig.getUnitId();
    deleteAllPropertiesForConfig(cid);
    if ((propertyList != null) && (!propertyList.isEmpty()) && (!isDelete))
    {
      checkUniqueness();
      CustomProperties property;
      for (Iterator i$ = propertyList.iterator(); i$.hasNext(); insertProperty(property))
      {
        property = (CustomProperties)i$.next();
        property.setPrincipalEmployeeId(cid);
      }
    }
  }
  

  private void deleteAllPropertiesForConfig(ObjectIdLong cid)
  {
    SQLStatement stmt = new SQLStatement(4, "business.cms.DELETE_ALL_CONFIG_PROPERTIES");
    ArrayList params = new ArrayList();
    params.add(cid.toSQLString());
    stmt.execute(params);
  }
  
  private void insertProperty(CustomProperties property)
  {
    ObjectIdLong id = ObjectIdManager.create("CMSPRINCIPALEMP1M");
    property.setId(id);
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_CONFIG_PROPERTY");
    ArrayList params = new ArrayList();
    params.add(property.getId());
    params.add(property.getPrincipalEmployeeId());
    params.add(property.getPropertyName());
    params.add(property.getPropertyValue());
    stmt.execute(params);
  }
  
  protected void checkUniqueness()
  {
    if (webConfig != null)
    {
      List propertyList = webConfig.getProperties();
      HashSet nameSet = new HashSet();
      if ((propertyList != null) && (propertyList.size() > 0))
      {
        for (Iterator i$ = propertyList.iterator(); i$.hasNext();)
        {
          CustomProperties property = (CustomProperties)i$.next();
          String name = property.getPropertyName();
          if (!nameSet.contains(name))
          {
            nameSet.add(name);
          }
          else {
            CMSException exception = CMSException.duplicatedConfigProperty(name);
            throw exception;
          }
        }
      }
    }
  }
}
