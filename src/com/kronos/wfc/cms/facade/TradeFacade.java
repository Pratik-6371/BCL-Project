package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Trade;
import java.util.List;
import java.util.Map;

public abstract interface TradeFacade
{
  public abstract List<Trade> getTrades();
  
  public abstract Trade getTrade(String paramString);
  
  public abstract Map[] getTradeConfigurations();
  
  public abstract void updateTradeConfiguration(Map[] paramArrayOfMap);
}
