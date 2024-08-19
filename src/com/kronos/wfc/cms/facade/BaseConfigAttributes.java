package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;










































public class BaseConfigAttributes
{
  protected PrincipalEmployeeFacade facade;
  protected String id;
  
  public BaseConfigAttributes() {}
  
  public void attributesToDmbf(ObjectIdLong unitId, String unitName, String peaddress, String peaddress1, String peaddress2, String peaddress3, String managername, String manageraddress, String manageraddress1, String manageraddress2, String manageraddress3, String typeofbussiness, String maxnumberofworkmen, String maxnumberofcontractworkmen, Boolean bOCWActApplicability, Boolean iSMWActapplicability, String licensenumber, String pfCode, String esiwc, Map[] properties, DynamicMapBackedForm data)
  {
    data.setValue("peid", unitId.toLong().toString());
    data.setValue("unitname", unitName);
    data.setValue("peaddress", peaddress);
    data.setValue("peaddress1", peaddress1);
    data.setValue("peaddress2", peaddress2);
    data.setValue("peaddress3", peaddress3);
    data.setValue("managername", managername);
    data.setValue("manageraddress", manageraddress);
    data.setValue("manageraddress1", manageraddress1);
    data.setValue("manageraddress2", manageraddress2);
    data.setValue("manageraddress3", manageraddress3);
    data.setValue("licensenumber", licensenumber);
    data.setValue("boc", Boolean.valueOf(bOCWActApplicability.booleanValue()));
    data.setValue("ismwa", Boolean.valueOf(iSMWActapplicability.booleanValue()));
    data.setValue("esiwc", esiwc);
    data.setValue("maxnumberofcontractworkmen", maxnumberofcontractworkmen);
    data.setValue("maxnumberofworkmen", maxnumberofworkmen);
    data.setValue("pfcode", pfCode);
    data.setValue("typeofbusiness", typeofbussiness);
  }
  
  protected Map createNewProperty()
  {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("name", "");
    property.put("value", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data)
  {
    Map[] propertyMap = (Map[])data.getValue("cmsConfigProperties");
    if ((propertyMap == null) || (propertyMap.length < 1))
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("cmsConfigProperties", propertyMap);
    }
    else {
      data.setValue("cmsConfigProperties", propertyMap);
    }
  }
  
  public void attributesToDmbf(Map attributes, DynamicMapBackedForm data)
  {
    data.setValue("peid", attributes.get("peid"));
    data.setValue("unitname", attributes.get("unitname"));
    data.setValue("peaddress", attributes.get("peaddress"));
    data.setValue("peaddress1", attributes.get("peaddress1"));
    data.setValue("peaddress2", attributes.get("peaddress2"));
    data.setValue("peaddress3", attributes.get("peaddress3"));
    data.setValue("managername", attributes.get("managername"));
    data.setValue("manageraddress", attributes.get("manageraddr"));
    data.setValue("manageraddress1", attributes.get("manageraddr1"));
    data.setValue("manageraddress2", attributes.get("manageraddr2"));
    data.setValue("manageraddress3", attributes.get("manageraddr3"));
    data.setValue("licensenumber", attributes.get("licensenumber"));
    data.setValue("boc", ((Boolean)attributes.get("boc")).booleanValue() ? "on" : "off");
    data.setValue("ismwa", ((Boolean)attributes.get("ismwa")).booleanValue() ? "on" : "off");
    data.setValue("esiwc", attributes.get("esiwc"));
    data.setValue("maxnumberofcontractworkmen", attributes.get("maxnumberofcontractworkmen"));
    data.setValue("maxnumberofworkmen", attributes.get("maxnumberofworkmen"));
    data.setValue("currentcountworkmen", attributes.get("currentcountworkmen"));
    data.setValue("pfcode", attributes.get("pfcode"));
    data.setValue("typeofbusiness", attributes.get("typeofbusiness"));
    Map[] propertyMap = (Map[])attributes.get("properties");
    if (propertyMap.length < 1)
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
    }
    data.setValue("cmsConfigProperties", propertyMap);
    data.setValue("availStateNames", new CMSService().getStates());
  }
  
  public Map dmbfToAttributes(DynamicMapBackedForm data)
  {
    Map attributes = new HashMap();
    attributes.put("peid", (String)data.getValue("peid"));
    attributes.put("unitname", (String)data.getValue("unitname"));
    attributes.put("peaddress", (String)data.getValue("peaddress"));
    attributes.put("peaddress1", (String)data.getValue("peaddress1"));
    attributes.put("peaddress2", (String)data.getValue("peaddress2"));
    attributes.put("peaddress3", (String)data.getValue("peaddress3"));
    attributes.put("managername", (String)data.getValue("managername"));
    attributes.put("manageraddress", (String)data.getValue("manageraddress"));
    attributes.put("manageraddress1", (String)data.getValue("manageraddress1"));
    attributes.put("manageraddress2", (String)data.getValue("manageraddress2"));
    attributes.put("manageraddress3", (String)data.getValue("manageraddress3"));
    attributes.put("licensenumber", (String)data.getValue("licensenumber"));
    attributes.put("ismwa", (String)data.getValue("ismwa"));
    attributes.put("esiwc", (String)data.getValue("esiwc"));
    attributes.put("maxnumberofcontractworkmen", (String)data.getValue("maxnumberofcontractworkmen"));
    attributes.put("maxnumberofworkmen", (String)data.getValue("maxnumberofworkmen"));
    attributes.put("pfcode", (String)data.getValue("pfcode"));
    attributes.put("typeofbusiness", (String)data.getValue("typeofbusiness"));
    attributes.put("boc", (String)data.getValue("boc"));
    
    return attributes;
  }
  
  private String getPropNameName(String id)
  {
    return id + "_name";
  }
  
  private String getPropValueName(String id)
  {
    return id + "_value";
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
      property.put("id", id);
      property.put("name", propName);
      property.put("value", propVal);
      properties[(i++)] = property;
    }
    
    return properties;
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
        String propName = (String)prop.get("name");
        String propValue = (String)prop.get("value");
        if (((propName != null) && (!propName.equals(""))) || ((propValue != null) && (!propValue.equals("")))) {
          namedPropsList.add(prop);
        }
      }
    }
    Map[] namedProps = new Map[namedPropsList.size()];
    namedProps = (Map[])namedPropsList.toArray(namedProps);
    return namedProps;
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
  
  public void insertPropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    updateNewPropertyIds(properties);
    properties = insertBlankProperty(properties);
    
    data.setValue("cmsConfigProperties", properties);
  }
  
  public void roundTripProperties(DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    data.setValue("cmsConfigProperties", properties);
  }
  
  public void deletePropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    properties = deleteProperty(properties, selectedPropId);
    updateNewPropertyIds(properties);
    if (properties.length < 1)
      properties = insertBlankProperty(properties);
    data.setValue("cmsConfigProperties", properties);
  }
  
  public boolean isPropertyBlank(String propertyId, DynamicMapBackedForm data)
  {
    boolean isBlank = true;
    Map[] properties = getPropertiesFromDmbf(data);
    for (int i = 0; i < properties.length; i++)
    {
      if (properties[i].get("id").equals(propertyId))
      {
        String name = (String)properties[i].get("name");
        String value = (String)properties[i].get("value");
        if ((name != null) && (value != null) && (!name.equals("")) && (!value.equals("")))
          isBlank = false;
      }
    }
    return isBlank;
  }
}
