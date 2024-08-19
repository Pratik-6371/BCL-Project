package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






public class BaseHealthInformationAttributes
{
  protected TradeFacade facade;
  protected String id;
  
  public BaseHealthInformationAttributes() {}
  
  protected Map createNewProperty()
  {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("healthText", "");
    property.put("healthDesc", "");
    property.put("healthTestDate", "");
    property.put("majorConcerns", "");
    property.put("nextMedicalTest", "");
    return property;
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
      property.put("id", id);
      property.put("healthText", (String)data.getValue(getPropName(id, "healthText")));
      property.put("healthDesc", (String)data.getValue(getPropName(id, "healthDesc")));
      property.put("healthTestDate", (String)data.getValue(getPropName(id, "healthTestDate")));
      property.put("majorConcerns", (String)data.getValue(getPropName(id, "majorConcerns")));
      property.put("nextMedicalTest", (String)data.getValue(getPropName(id, "nextMedicalTest")));
      properties[(i++)] = property;
    }
    
    return properties;
  }
  

  public void insertPropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    updateNewPropertyIds(properties);
    properties = insertBlankProperty(properties);
    
    data.setValue("healthInformationList", properties);
  }
  
  private String getPropName(String id, String name) {
    return id + "_" + name;
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
      String id = (String)property.get("id");
      if ("new_property".equals(id)) {
        property.put("id", id + strNum);
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
      String id = (String)property.get("id");
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
    property.put("id", "new_property");
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
    data.setValue("healthInformationList", properties);
  }
  

  public boolean isPropertyBlank(String propertyId, DynamicMapBackedForm data)
  {
    boolean isBlank = true;
    Map[] properties = getPropertiesFromDmbf(data);
    for (int i = 0; i < properties.length; i++)
    {
      if (properties[i].get("id").equals(propertyId))
      {
        String name = (String)properties[i].get("sId");
        String value = (String)properties[i].get("violationDate");
        if ((name != null) && (value != null) && (!name.equals("")) && (!value.equals("")))
          isBlank = false;
      }
    }
    return isBlank;
  }
}
