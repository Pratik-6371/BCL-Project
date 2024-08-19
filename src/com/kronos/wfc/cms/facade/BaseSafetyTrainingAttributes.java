package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;









public class BaseSafetyTrainingAttributes
{
  protected TradeFacade facade;
  protected String id;
  
  public BaseSafetyTrainingAttributes() {}
  
  protected Map createNewProperty()
  {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("trnId", "");
    property.put("dateTaken", "");
    property.put("fromTime", "");
    property.put("toTime", "");
    property.put("nextTrnDa", "");
    property.put("trnDesc", "");
    property.put("dept", "");
    property.put("sec", "");
    property.put("func", "");
    property.put("nJob", "");
    property.put("module", "");
    property.put("TNI", "");
    property.put("facultyNm", "");
    property.put("venue", "");
    property.put("preTestMarksObtained", "");
    property.put("preTestMaxMarks", "");
    property.put("preTestPercentage", "");
    property.put("postTestMarksObtained", "");
    property.put("postTestMaxMarks", "");
    property.put("postTestPercentage", "");
    property.put("recommendation", "");
    property.put("remarks", "");
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
      property.put("trnId", (String)data.getValue(getPropName(id, "trnId")));
      property.put("dateTaken", (String)data.getValue(getPropName(id, "trainingDate")));
      property.put("fromTime", (String)data.getValue(getPropName(id, "fromtime")));
      property.put("toTime", (String)data.getValue(getPropName(id, "totime")));
      property.put("nextTrnDa", (String)data.getValue(getPropName(id, "nextTrnDa")));
      property.put("trnDesc", (String)data.getValue(getPropName(id, "trnDesc")));
      property.put("dept", (String)data.getValue(getPropName(id, "department")));
      property.put("sec", (String)data.getValue(getPropName(id, "section")));
      property.put("func", (String)data.getValue(getPropName(id, "function")));
      property.put("nJob", (String)data.getValue(getPropName(id, "natureofjob")));
      property.put("module", (String)data.getValue(getPropName(id, "module")));
      property.put("TNI", (String)data.getValue(getPropName(id, "tni")));
      property.put("facultyNm", (String)data.getValue(getPropName(id, "faculty")));
      property.put("venue", (String)data.getValue(getPropName(id, "venue")));
      property.put("preTestMarksObtained", (String)data.getValue(getPropName(id, "preMarksObtained")));
      property.put("preTestMaxMarks", (String)data.getValue(getPropName(id, "preMaxMarks")));
      property.put("preTestPercentage", (String)data.getValue(getPropName(id, "preMarksPercent")));
      property.put("postTestMarksObtained", (String)data.getValue(getPropName(id, "postMarksObtained")));
      property.put("postTestMaxMarks", (String)data.getValue(getPropName(id, "postMaxMarks")));
      property.put("postTestPercentage", (String)data.getValue(getPropName(id, "postMarksPercent")));
      property.put("recommendation", (String)data.getValue(getPropName(id, "recommendForRetraining")));
      property.put("remarks", (String)data.getValue(getPropName(id, "remarks")));
      properties[(i++)] = property;
    }
    
    return properties;
  }
  

  public void insertPropertyRow(String selectedPropId, DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    updateNewPropertyIds(properties);
    properties = insertBlankProperty(properties);
    
    data.setValue("safetyList", properties);
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
    data.setValue("safetyList", properties);
  }
  

  public boolean isPropertyBlank(String propertyId, DynamicMapBackedForm data)
  {
    boolean isBlank = true;
    Map[] properties = getPropertiesFromDmbf(data);
    for (int i = 0; i < properties.length; i++)
    {
      if (properties[i].get("id").equals(propertyId))
      {
        String name = (String)properties[i].get("trnId");
        String value = (String)properties[i].get("dateTaken");
        if ((name != null) && (value != null) && (!name.equals("")) && (!value.equals("")))
          isBlank = false;
      }
    }
    return isBlank;
  }
}
