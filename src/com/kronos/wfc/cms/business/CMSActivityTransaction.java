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
import java.util.Map;

public class CMSActivityTransaction
  extends Transaction
{
  public static final boolean DELETE = true;
  private static final String DELETE_ALL_CONFIG_PROPERTIES = "business.cms.DELETE_ALL_WORKORDER_PROPERTIES";
  private static final String INSERT_CONFIG_PROPERTY = "business.cms.INSERT_ALL_WORKORDER_PROPERTY";
  private static final String TABLE_NAME = "CMSWORKORDERACT";
  ObjectIdLong wkId;
  Map[] maps;
  
  public CMSActivityTransaction(Map[] propertyMaps, ObjectIdLong id)
  {
    wkId = id;
    maps = propertyMaps;
  }
  

  protected void transaction()
    throws SQLException
  {
    List propertyList = getWorkorderActivities();
    deleteAllPropertiesForConfig(wkId);
    
    if ((propertyList != null) && (!propertyList.isEmpty()))
    {
      checkUniqueness();
      
      WorkorderActivity property;
      for (Iterator i$ = propertyList.iterator(); i$.hasNext(); insertProperty(property))
      {
        property = (WorkorderActivity)i$.next();
        property.setWorkorderId(wkId);
      }
    }
  }
  



  private List getWorkorderActivities()
  {
    ArrayList result = new ArrayList();
    if ((maps != null) && (maps.length > 0)) {
      for (int i = 0; i < maps.length; i++) {
        Map map = maps[i];
        String id = (String)map.get("id");
        String name = (String)map.get("name");
        String value = (String)map.get("value");
      //  ObjectIdLong idL;
        ObjectIdLong idL; if ((id == null) || (id.startsWith("new_property"))) {
          idL = new ObjectIdLong(0L);
        }
        else {
          idL = new ObjectIdLong(id);
        }
        WorkorderActivity act = new WorkorderActivity(idL, name, value);
        act.setWorkorderId(wkId);
        result.add(act);
      }
    }
    

    return result;
  }
  
  private void deleteAllPropertiesForConfig(ObjectIdLong cid)
  {
    SQLStatement stmt = new SQLStatement(4, "business.cms.DELETE_ALL_WORKORDER_PROPERTIES");
    ArrayList params = new ArrayList();
    params.add(cid.toSQLString());
    stmt.execute(params);
  }
  
  private void insertProperty(WorkorderActivity property)
  {
    ObjectIdLong id = ObjectIdManager.create("CMSWORKORDERACT");
    property.setId(id);
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_ALL_WORKORDER_PROPERTY");
    ArrayList params = new ArrayList();
    params.add(property.getId());
    params.add(property.getName());
    params.add(property.getValue());
    params.add(wkId.toSQLString());
    stmt.execute(params);
  }
  
  protected void checkUniqueness()
  {
    if (maps != null)
    {
      List propertyList = getWorkorderActivities();
      HashSet nameSet = new HashSet();
      if ((propertyList != null) && (propertyList.size() > 0))
      {
        for (Iterator i$ = propertyList.iterator(); i$.hasNext();)
        {
          WorkorderActivity property = (WorkorderActivity)i$.next();
          String name = property.getName();
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
