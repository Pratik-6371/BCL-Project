package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.MinimumWage;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Wage;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;








public class MinimumWageFacadeImpl
  implements MinimumWageFacade
{
  public MinimumWageFacadeImpl() {}
  
  public MinimumWage getMinimumWage(String configId)
  {
    ObjectIdLong oid = new ObjectIdLong(configId);
    return MinimumWage.doRetrieveById(oid);
  }
  

  public MinimumWage getMinimumWageByName(String name)
  {
    MinimumWage config = null;
    config = MinimumWage.doRetrieveByName(name);
    return config;
  }
  
  public List<MinimumWage> getMinimumWages(String unitId, String searchDate)
  {
    if ((unitId != null) && (searchDate != null)) {
      ObjectIdLong uId = new ObjectIdLong(unitId);
      KDate date = KServer.stringToDate(searchDate);
      return MinimumWage.doRetrieveMWByState(uId, date);
    }
    return new ArrayList();
  }
  




  private Map fillAttributeMapNoProperties(MinimumWage config)
  {
    Map configMap = new HashMap();
    
    configMap.put("id", config.getMinimumwageId().toString());
    configMap.put("name", config.getName());
    configMap.put("from", config.getFrom().toString());
    configMap.put("to", config.getTo().toString());
    Trade trade = Trade.retrieveById(config.getTradeId());
    configMap.put("trade", trade.getTradeName());
    Skill skill = Skill.retrieveSkill(config.getSkillId());
    configMap.put("skillset", skill.getSkillNm());
    

    configMap.put("minimumwage", config.getWage().getBasic());
    configMap.put("daperday", config.getWage().getDa());
    configMap.put("otherallowance", config.getWage().getAllowance());
    configMap.put("total", calculateTotal(config.getWage()));
    

    return configMap;
  }
  


  private String calculateTotal(Wage wage)
  {
    float i = 0.0F;
    if (wage.getBasic() != null) {
      i += Float.valueOf(wage.getBasic()).floatValue();
    }
    if (wage.getDa() != null) {
      i += Float.valueOf(wage.getDa()).floatValue();
    }
    if (wage.getAllowance() != null) {
      i += Float.valueOf(wage.getAllowance()).floatValue();
    }
    
    return Float.valueOf(i).toString();
  }
  
  public Map[] getMinimumWageConfigurations(String unitId, String searchDate)
  {
    List<MinimumWage> configurations = getMinimumWages(unitId, searchDate);
    Map[] configMaps = new Map[configurations.size()];
    int i = 0;
    for (Iterator<MinimumWage> it = configurations.iterator(); it.hasNext();)
    {
      MinimumWage config = (MinimumWage)it.next();
      Map configMap = fillAttributeMapNoProperties(config);
      configMaps[(i++)] = configMap;
    }
    return configMaps;
  }
  
  public void updateMinimumWageConfiguration(String id, Map attrs) throws Exception
  {
    MinimumWage config = getMinimumWage(id);
    updateAttributes(config, attrs);
    config.doUpdate();
  }
  

  private void updateAttributes(MinimumWage mw, Map attrs)
  {
    String id = (String)attrs.get("id");
    String name = (String)attrs.get("name");
    String from = (String)attrs.get("from");
    String to = (String)attrs.get("to");
    String trade = (String)attrs.get("trade");
    String skillset = (String)attrs.get("skillset");
    String minimumwage = (String)attrs.get("minimumwage");
    String daperday = (String)attrs.get("daperday");
    String otherallowance = (String)attrs.get("otherallowance");
    String stateid = (String)attrs.get("stateid");
    


    mw.setName(name);
    mw.setFrom(KServer.stringToDate(from));
    mw.setTo(KServer.stringToDate(to));
    mw.setTradeId(new ObjectIdLong(trade));
    mw.setSkillId(new ObjectIdLong(skillset));
    mw.setWage(new Wage(null, minimumwage, daperday, otherallowance));
    mw.setStateid(new ObjectIdLong(stateid));
  }
  



  public void saveMinimumWage(MinimumWage mw)
    throws Exception
  {
    mw.doUpdate();
  }
  


  public List<CMSState> getStates()
  {
    return new CMSService().getStates();
  }
  

  public List<Trade> getTrades()
  {
    return Trade.doRetrieveAll();
  }
  
  public List<Skill> getSkills()
  {
    return Skill.retrieveSkills();
  }
}
