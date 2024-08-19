package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSTradeTransaction;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





public class TradeFacadeImpl
  implements TradeFacade
{
  public TradeFacadeImpl() {}
  
  public List<Trade> getTrades()
  {
    return Trade.doRetrieveAll();
  }
  
  public Trade getTrade(String configId)
  {
    ObjectIdLong oid = new ObjectIdLong(configId);
    return Trade.retrieveById(oid);
  }
  
  private Map fillAttributeMapNoProperties(Trade config) {
    Map configMap = new HashMap();
    configMap.put("tradeId", config.getTradeId().toString());
    configMap.put("tradeName", config.getTradeName());
    configMap.put("tradeDesc", config.getTradeDesc());
    

    return configMap;
  }
  

  public Map[] getTradeConfigurations()
  {
    List<Trade> configurations = getTrades();
    Map[] configMaps = new Map[configurations.size()];
    int i = 0;
    for (Iterator<Trade> it = configurations.iterator(); it.hasNext();)
    {
      Trade config = (Trade)it.next();
      Map configMap = fillAttributeMapNoProperties(config);
      configMaps[(i++)] = configMap;
    }
    
    return configMaps;
  }
  
  public void updateTradeConfiguration(Map[] propertyMaps)
  {
    if (propertyMaps != null) {
      CMSTradeTransaction trans = new CMSTradeTransaction(propertyMaps);
      trans.run();
    }
  }
}
