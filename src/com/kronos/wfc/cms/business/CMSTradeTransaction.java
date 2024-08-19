package com.kronos.wfc.cms.business;

import com.kronos.wfc.cms.facade.CMSException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.Transaction;
import com.kronos.wfc.platform.persistence.framework.internal.ObjectIdManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class CMSTradeTransaction
  extends Transaction
{
  private static final String TABLE_NAME = "CMSTRADE";
  Map[] propertyMap;
  
  public CMSTradeTransaction(Map[] propertyMap)
  {
    this.propertyMap = propertyMap;
  }
  

  public void transaction()
    throws SQLException
  {
    if ((propertyMap != null) && (propertyMap.length > 0))
    {
      checkUniqueness();
      List<Trade> trades = Trade.doRetrieveAll();
      HashMap map = convertToTradeMap(trades);
      for (int i = 0; i < propertyMap.length; i++) {
        Map property = propertyMap[i];
        if (map.containsKey(property.get("tradeId"))) {
          Trade tr = (Trade)map.get(property.get("tradeId"));
          tr.setTradeName((String)property.get("tradeName"));
          tr.setTradeDesc((String)property.get("tradeDesc"));
          updateTrade(tr);
        }
        else {
          Trade tr = new Trade(null, (String)property.get("tradeName"), 
            (String)property.get("tradeDesc"));
          insertTrade(tr);
        }
      }
    }
  }
  


  private HashMap convertToTradeMap(List<Trade> trades)
  {
    HashMap result = new HashMap();
    for (Iterator iterator = trades.iterator(); iterator.hasNext();) {
      Trade trade = (Trade)iterator.next();
      result.put(trade.getTradeId().toString(), trade);
    }
    return result;
  }
  
  private void updateTrade(Trade trade)
  {
    SQLStatement stmt = new SQLStatement(4, "business.cms.UPDATE_TRADE");
    ArrayList params = new ArrayList();
    params.add(trade.getTradeName());
    params.add(trade.getTradeDesc());
    params.add(trade.getTradeId());
    stmt.execute(params);
  }
  
  private void insertTrade(Trade trade)
  {
    ObjectIdLong id = ObjectIdManager.create("CMSTRADE");
    trade.setTradeId(id);
    ArrayList params = new ArrayList();
    params.add(trade.getTradeId());
    params.add(trade.getTradeName());
    params.add(trade.getTradeDesc());
    SQLStatement stmt = new SQLStatement(4, "business.cms.INSERT_TRADE");
    stmt.execute(params);
  }
  
  protected void checkUniqueness()
  {
    HashSet nameSet = new HashSet();
    if ((propertyMap != null) && (propertyMap.length > 0))
    {
      for (int i = 0; i < propertyMap.length; i++)
      {
        Map property = propertyMap[i];
        String name = (String)property.get("tradeName");
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
