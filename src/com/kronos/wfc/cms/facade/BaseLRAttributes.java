package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSShift;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;









public class BaseLRAttributes
{
  private Workorder wk;
  
  public BaseLRAttributes(Workorder wk)
  {
    this.wk = wk;
  }
  

  protected Map createNewProperty()
  {
    Map<String, String> prop = new HashMap();
    
    prop.put("lrId", "new_property");
    prop.put("trade", "");
    prop.put("skill", "");
    prop.put("serviceCode", "");
    prop.put("itemDesc", "");
    prop.put("wlId", "");
    
    String shiftA = (String)CMSShift.map.get("shift_A");
    String shiftB = (String)CMSShift.map.get("shift_B");
    String shiftC = (String)CMSShift.map.get("shift_C");
    String shiftG = (String)CMSShift.map.get("shift_G");
    List<CMSShift> shifts = CMSShift.retrieveShifts(wk.getUnitId());
    for (Iterator iterator = shifts.iterator(); iterator.hasNext();) {
      CMSShift cmsShift = (CMSShift)iterator.next();
      if (cmsShift.getShiftNm().equalsIgnoreCase(shiftA))
      {
        prop.put("shift_A_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftB))
      {
        prop.put("shift_B_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftC))
      {
        prop.put("shift_C_id", cmsShift.getShiftId().toString());
      }
      else if (cmsShift.getShiftNm().equalsIgnoreCase(shiftG))
      {
        prop.put("shift_G_id", cmsShift.getShiftId().toString());
      }
    }
    

    return prop;
  }
  


  public void atLeastOneProperty(DynamicMapBackedForm data)
  {
    Map[] propertyMap = (Map[])data.getValue("lrLines");
    if ((propertyMap == null) || (propertyMap.length < 1))
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("lrLines", propertyMap);
    }
  }
  

  public void attributesToDmbf(Map attributes, DynamicMapBackedForm data)
  {
    Map[] propertyMap = (Map[])attributes.get("lrLines");
    if ((propertyMap == null) || (propertyMap.length < 1))
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
    }
    data.setValue("lrLines", propertyMap);
  }
  
  protected Map[] getPropertiesFromDmbf(DynamicMapBackedForm data)
  {
    String[] propertyIds = (String[])data.getValue("propertyIds");
    if (propertyIds == null)
      return null;
    Map[] properties = new HashMap[propertyIds.length];
    int i = 0;
    String[] arr$ = propertyIds;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++)
    {
      String id = arr$[i$];
      Map property = new HashMap();
      
      String tradeId = (String)data.getValue(getTradeId(id));
      String skillId = (String)data.getValue(getSkillId(id));
      String wlId = (String)data.getValue(getWlId(id));
      String shiftA_value = (String)data.getValue(getShiftValue(id, "shift_A"));
      String shiftB_value = (String)data.getValue(getShiftValue(id, "shift_B"));
      String shiftC_value = (String)data.getValue(getShiftValue(id, "shift_C"));
      String shiftG_value = (String)data.getValue(getShiftValue(id, "shift_G"));
      String shiftA_id_value = (String)data.getValue(getShiftValue(id, "shift_A_id"));
      String shiftB_id_value = (String)data.getValue(getShiftValue(id, "shift_B_id"));
      String shiftC_id_value = (String)data.getValue(getShiftValue(id, "shift_C_id"));
      String shiftG_id_value = (String)data.getValue(getShiftValue(id, "shift_G_id"));
      

      property.put("lrId", id);
      property.put("tradeId", tradeId);
      property.put("skillId", skillId);
      property.put("wlId", wlId);
      property.put("shift_A", shiftA_value);
      property.put("shift_B", shiftB_value);
      property.put("shift_C", shiftC_value);
      property.put("shift_G", shiftG_value);
      property.put("shift_A_id", shiftA_id_value);
      property.put("shift_B_id", shiftB_id_value);
      property.put("shift_C_id", shiftC_id_value);
      property.put("shift_G_id", shiftG_id_value);
      


      properties[(i++)] = property;
    }
    
    return properties;
  }
  

  private String getShiftValue(String id, String shiftA)
  {
    return id + "_" + shiftA;
  }
  

  private String getWlId(String id)
  {
    return id + "_wlId";
  }
  

  private String getTradeId(String id)
  {
    return id + "_tradeId";
  }
  


  private String getSkillId(String id)
  {
    return id + "_skillId";
  }
  



  public void insertPropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    updateNewPropertyIds(properties);
    properties = insertBlankProperty(properties);
    
    data.setValue("lrLines", properties);
  }
  
  private void updateNewPropertyIds(Map[] properties)
  {
    int num = getGreatestNewPropertyId(properties);
    num++;String strNum = Integer.toString(num);
    Map[] arr$ = properties;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++)
    {
      Map property = arr$[i$];
      String id = (String)property.get("lrId");
      if ("new_property".equals(id)) {
        property.put("lrId", id + strNum);
      }
    }
  }
  
  private int getGreatestNewPropertyId(Map[] properties)
  {
    int greatest = 0;
    Map[] arr$ = properties;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++)
    {
      Map property = arr$[i$];
      String id = (String)property.get("lrId");
      String pattern = "new_property";
      Pattern compiledPattern = Pattern.compile(pattern);
      Matcher matcher = compiledPattern.matcher(id);
      if ((matcher.find()) && (id.length() > "new_property".length()))
      {
        String num = id.substring("new_property".length());
        int iNum = Integer.parseInt(num);
        greatest = Math.max(greatest, iNum);
      }
    }
    
    return greatest;
  }
  
  private Map[] insertBlankProperty(Map[] properties)
  {
    Map property = createNewProperty();
    Map[] newProperties = new Map[properties.length + 1];
    for (int i = 0; i < properties.length; i++) {
      newProperties[i] = properties[i];
    }
    newProperties[properties.length] = property;
    return newProperties;
  }
  

  private Map[] deleteProperty(Map[] properties, String propertyId)
  {
    Map[] newProperties = new Map[properties.length - 1];
    int j = 0;
    for (int i = 0; i < properties.length; i++) {
      if (!properties[i].get("lrId").equals(propertyId))
        newProperties[(j++)] = properties[i];
    }
    return newProperties;
  }
  
  public void deletePropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    properties = deleteProperty(properties, selectedPropId);
    updateNewPropertyIds(properties);
    if (properties.length < 1)
      properties = insertBlankProperty(properties);
    data.setValue("lrLines", properties);
  }
  

  public boolean isPropertyBlank(String propertyId, DynamicMapBackedForm data)
  {
    boolean isBlank = true;
    Map[] properties = getPropertiesFromDmbf(data);
    String shiftA = (String)CMSShift.map.get("shift_A");
    String shiftB = (String)CMSShift.map.get("shift_B");
    String shiftC = (String)CMSShift.map.get("shift_C");
    String shiftG = (String)CMSShift.map.get("shift_G");
    
    for (int i = 0; i < properties.length; i++)
    {
      if (properties[i].get("lrId").equals(propertyId))
      {
        String trade = (String)properties[i].get("tradeId");
        String skill = (String)properties[i].get("skillId");
        String wlId = (String)properties[i].get("wlId");
        String shiftA_value = (String)properties[i].get(shiftA);
        String shiftB_value = (String)properties[i].get(shiftB);
        String shiftC_value = (String)properties[i].get(shiftC);
        String shiftG_value = (String)properties[i].get(shiftG);
        if ((trade != null) && (skill != null) && (wlId != null) && (!trade.equals("-1")) && (!skill.equals("-1")) && (!wlId.equals("-1")))
          isBlank = false;
      }
    }
    return isBlank;
  }
}
