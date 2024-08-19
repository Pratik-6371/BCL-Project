package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;








public class BaseTradeAttributes
{
  protected TradeFacade facade;
  protected String id;
  
  public BaseTradeAttributes() {}
  
  public void attributesToDmbf(ObjectIdLong id, String name, String description, Map[] properties, DynamicMapBackedForm data)
  {
    data.setValue("id", id.toLong().toString());
    data.setValue("name", name);
    data.setValue("description", description);
  }
  

  protected Map createNewProperty()
  {
    Map<String, String> property = new HashMap();
    property.put("tradeId", "new_property");
    property.put("tradeName", "");
    property.put("tradeDesc", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data)
  {
    Map[] propertyMap = (Map[])data.getValue("cmsTradeProperties");
    if ((propertyMap == null) || (propertyMap.length < 1))
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("cmsTradeProperties", propertyMap);
    }
  }
  

  public void attributesToDmbf(Map attributes, DynamicMapBackedForm data)
  {
    data.setValue("trade", attributes.get("tradeid"));
    data.setValue("name", attributes.get("name"));
    data.setValue("description", attributes.get("description"));
    Map[] propertyMap = (Map[])attributes.get("properties");
    if (propertyMap.length < 1)
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
    }
    data.setValue("tradeList", propertyMap);
  }
  
  public Map dmbfToAttributes(DynamicMapBackedForm data)
  {
    Map attributes = new HashMap();
    attributes.put("tradeid", (String)data.getValue("tradeid"));
    attributes.put("name", (String)data.getValue("name"));
    attributes.put("description", (String)data.getValue("description"));
    return attributes;
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
      Map property = new HashMap(5);
      String propName = (String)data.getValue(getPropNameName(id));
      String propVal = (String)data.getValue(getPropValueName(id));
      property.put("tradeId", id);
      property.put("tradeName", propName);
      property.put("tradeDesc", propVal);
      properties[(i++)] = property;
    }
    
    return properties;
  }
  

  private String getPropNameName(String id)
  {
    return id + "_name";
  }
  


  private String getPropValueName(String id)
  {
    return id + "_value";
  }
  



  public void insertPropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    updateNewPropertyIds(properties);
    properties = insertBlankProperty(properties);
    
    data.setValue("tradeList", properties);
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
      String id = (String)property.get("tradeId");
      if ("new_property".equals(id)) {
        property.put("tradeId", id + strNum);
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
      String id = (String)property.get("tradeId");
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
    property.put("tradeId", "new_property");
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
      if (!properties[i].get("id").equals(propertyId))
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
    data.setValue("tradeList", properties);
  }
  

  protected Map[] getNamedPropertiesFromDmbf(DynamicMapBackedForm data)
  {
    Map[] allProps = getPropertiesFromDmbf(data);
    List namedPropsList = new ArrayList();
    if (allProps != null)
    {
      Map[] arr$ = allProps;
      int len$ = arr$.length;
      for (int i$ = 0; i$ < len$; i$++)
      {
        Map prop = arr$[i$];
        String propName = (String)prop.get("tradeName");
        String propValue = (String)prop.get("tradeDesc");
        if (((propName != null) && (!propName.equals(""))) || ((propValue != null) && (!propValue.equals("")))) {
          namedPropsList.add(prop);
        }
      }
    }
    Map[] namedProps = new Map[namedPropsList.size()];
    namedProps = (Map[])namedPropsList.toArray(namedProps);
    return namedProps;
  }
  

  public boolean isPropertyBlank(String propertyId, DynamicMapBackedForm data)
  {
    boolean isBlank = true;
    Map[] properties = getPropertiesFromDmbf(data);
    for (int i = 0; i < properties.length; i++)
    {
      if (properties[i].get("tradeId").equals(propertyId))
      {
        String name = (String)properties[i].get("tradeName");
        String value = (String)properties[i].get("tradeDesc");
        if ((name != null) && (value != null) && (!name.equals("")) && (!value.equals("")))
          isBlank = false;
      }
    }
    return isBlank;
  }
}
