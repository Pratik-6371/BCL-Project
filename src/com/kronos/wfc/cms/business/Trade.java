package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Trade
{
  private ObjectIdLong tradeId;
  private String tradeName;
  private String tradeDesc;
  private static final Map<ObjectIdLong, Trade> trades;
  
  static
  {
    List<Trade> ts = new CMSService().getTrades();
    Map<ObjectIdLong, Trade> map = new HashMap();
    for (Iterator iterator = ts.iterator(); iterator.hasNext();) {
      Trade trade = (Trade)iterator.next();
      map.put(trade.getTradeId(), trade);
    }
    trades = Collections.unmodifiableMap(map);
  }
  

  public Trade(ObjectIdLong tradeId, String tradeName, String tradeDesc)
  {
    this.tradeId = tradeId;
    this.tradeName = tradeName;
    this.tradeDesc = tradeDesc;
  }
  
  public Trade() {}
  
  public ObjectIdLong getTradeId()
  {
    return tradeId;
  }
  
  public void setTradeId(ObjectIdLong tradeId) { this.tradeId = tradeId; }
  
  public String getTradeName() {
    return tradeName;
  }
  
  public void setTradeName(String tradeName) { this.tradeName = tradeName; }
  
  public String getTradeDesc() {
    return tradeDesc;
  }
  
  public void setTradeDesc(String tradeDesc) { this.tradeDesc = tradeDesc; }
  
  public List<Trade> getTrades()
  {
    return new CMSService().getTrades();
  }
  
  public static Trade retrieveById(ObjectIdLong id) {
    return (Trade)trades.get(id) == null ? new CMSService().getTradeById(id) : (Trade)trades.get(id);
  }
  
  public static List<Trade> doRetrieveAll() { return new CMSService().getTrades(); }
  
  public static List<Trade> retrieveTradesByUnit(ObjectIdLong unitId) {
    CMSService service = new CMSService();
    ArrayList<ObjectIdLong> tradeIds = service.getTradeIdsForUnit(unitId);
    
    ArrayList<Trade> ts = filterTrades(tradeIds);
    
    return ts;
  }
  
  private static ArrayList<Trade> filterTrades(ArrayList<ObjectIdLong> tradeIds) {
    ArrayList<Trade> trs = new ArrayList();
    if ((tradeIds != null) && (!tradeIds.isEmpty())) {
      for (Iterator iterator = tradeIds.iterator(); iterator.hasNext();) {
        ObjectIdLong tradeId = (ObjectIdLong)iterator.next();
        Trade trade = (Trade)trades.get(tradeId);
        trs.add(trade);
      }
    }
    return trs;
  }
}
