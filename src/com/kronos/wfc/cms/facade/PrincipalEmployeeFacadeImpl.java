package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CustomProperties;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrincipalEmployeeFacadeImpl
  implements PrincipalEmployeeFacade
{
  public PrincipalEmployeeFacadeImpl() {}
  
  public PrincipalEmployee getPrincipalEmployee(String configId)
  {
    ObjectIdLong oid = new ObjectIdLong(configId);
    return PrincipalEmployee.doRetrieveById(oid);
  }
  
  public PrincipalEmployee getPrincipalEmployeeByCode(String name)
  {
    PrincipalEmployee config = null;
    config = PrincipalEmployee.doRetrieveByCode(name);
    return config;
  }
  
  public List<PrincipalEmployee> getPrincipalEmployees()
  {
    return PrincipalEmployee.doRetrieveAll();
  }
  
  public Map getPrincipalEmployeeConfiguration(String id)
  {
    PrincipalEmployee config = getPrincipalEmployee(id);
    Map configMap = fillAttributeMap(config);
    return configMap;
  }
  

  public Map[] getPrincipalEmployeeConfigurations()
  {
    List<PrincipalEmployee> configurations = new CMSService().getPrincipalEmployerList();
    Map[] configMaps = new Map[configurations.size()];
    int i = 0;
    for (Iterator<PrincipalEmployee> it = configurations.iterator(); it.hasNext();)
    {
      PrincipalEmployee config = (PrincipalEmployee)it.next();
      Map configMap = fillAttributeMapNoProperties(config);
      configMaps[(i++)] = configMap;
    }
    
    return configMaps;
  }
  
  private Map fillAttributeMapNoProperties(PrincipalEmployee config) {
    Map configMap = new HashMap();
    configMap.put("peid", config.getUnitId().toString());
    configMap.put("unitname", config.getUnitName());
    configMap.put("peaddress", config.getPeaddress());
    configMap.put("peaddress1", config.getPeaddress1());
    configMap.put("peaddress2", config.getPeaddress2());
    configMap.put("peaddress3", config.getPeaddress3());
    configMap.put("managername", config.getManagername());
    configMap.put("manageraddr", config.getManageraddress());
    configMap.put("manageraddr1", config.getManageraddress1());
    configMap.put("manageraddr2", config.getManageraddress2());
    configMap.put("manageraddr3", config.getManageraddress3());
    configMap.put("maxnumberofworkmen", config.getMaxnumberofworkmen());
    configMap.put("maxnumberofcontractworkmen", config.getMaxnumberofcontractworkmen());
    if (config.getBOCWActApplicability() != null) {
      configMap.put("boc", config.getBOCWActApplicability());
    }
    else {
      configMap.put("boc", Boolean.valueOf(false));
    }
    if (config.getISMWActapplicability() != null) {
      configMap.put("ismwa", config.getISMWActapplicability());
    } else {
      configMap.put("ismwa", Boolean.valueOf(false));
    }
    configMap.put("licensenumber", config.getLicensenumber());
    configMap.put("typeofbusiness", config.getTypeofbussiness());
    configMap.put("pfcode", config.getPfCode());
    configMap.put("esiwc", config.getEsiwc());
    configMap.put("currentcountworkmen", config.getCurrentCountWorkmen());
    return configMap;
  }
  

  private Map fillAttributeMap(PrincipalEmployee config)
  {
    Map configMap = fillAttributeMapNoProperties(config);
    configMap.put("properties", getConfigurationProperties(config));
    return configMap;
  }
  
  private Object getConfigurationProperties(PrincipalEmployee config) {
    List propertyList = config.getProperties();
    Map[] properties = new Map[propertyList.size()];
    int i = 0;
    for (Iterator i$ = propertyList.iterator(); i$.hasNext();)
    {
      CustomProperties prop = (CustomProperties)i$.next();
      Map propertyMap = new HashMap();
      propertyMap.put("id", prop.getId().toString());
      propertyMap.put("name", prop.getPropertyName());
      propertyMap.put("value", prop.getPropertyValue());
      properties[(i++)] = propertyMap;
    }
    
    return properties;
  }
  
  public void savePrincipalEmployee(PrincipalEmployee pe)
  {
    pe.doUpdate();
  }
  

  public void updatePEConfiguration(String id, Map attrs, Map[] properties)
  {
    PrincipalEmployee config = getPrincipalEmployee(id);
    updateAttributes(attrs, properties, config);
    config.doUpdate();
  }
  
  private void updateAttributes(Map attrs, Map[] properties, PrincipalEmployee emp)
  {
    List propertyList = createPropertyList(properties);
    String unitId = (String)attrs.get("peid");
    String unitname = (String)attrs.get("unitname");
    String peaddress = (String)attrs.get("peaddress");
    String peaddress1 = (String)attrs.get("peaddress1");
    String peaddress2 = (String)attrs.get("peaddress2");
    String peaddress3 = (String)attrs.get("peaddress3");
    String managername = (String)attrs.get("managername");
    String manageraddress = (String)attrs.get("manageraddress");
    String manageraddress1 = (String)attrs.get("manageraddress1");
    String manageraddress2 = (String)attrs.get("manageraddress2");
    String manageraddress3 = (String)attrs.get("manageraddress3");
    String typeofbusiness = (String)attrs.get("typeofbusiness");
    String maxwm = (String)attrs.get("maxnumberofworkmen");
    String maxcm = (String)attrs.get("maxnumberofcontractworkmen");
    String boc = (String)attrs.get("boc");
    String ismwa = (String)attrs.get("ismwa");
    String licensenumber = (String)attrs.get("licensenumber");
    String pfcode = (String)attrs.get("pfcode");
    String esiwc = (String)attrs.get("esiwc");
    
    emp.setUnitID(Long.valueOf(unitId).longValue());
    emp.setUnitName(unitname);
    emp.setPeaddress(peaddress);
    emp.setPeaddress1(peaddress1);
    emp.setPeaddress2(peaddress2);
    emp.setPeaddress3(peaddress3);
    emp.setManagername(managername);
    emp.setManageraddress(manageraddress);
    emp.setManageraddress1(manageraddress1);
    emp.setManageraddress2(manageraddress2);
    emp.setManageraddress3(manageraddress3);
    emp.setTypeofbussiness(typeofbusiness);
    emp.setMaxnumberofworkmen(maxwm);
    emp.setMaxnumberofcontractworkmen(maxcm);
    emp.setBOCWActApplicability("on".equalsIgnoreCase(boc) ? new Boolean(true) : new Boolean(false));
    emp.setISMWActapplicability("on".equalsIgnoreCase(ismwa) ? new Boolean(true) : new Boolean(false));
    emp.setLicensenumber(licensenumber);
    emp.setPfCode(pfcode);
    emp.setEsiwc(esiwc);
    emp.setProperties(propertyList);
  }
  
  public List createPropertyList(Map[] properties)
  {
    if (properties == null)
    {
      CMSException e = CMSException.errorNullInput();
      throw e;
    }
    List propertyList = new ArrayList();
    Map[] arr$ = properties;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++)
    {
      Map property = arr$[i$];
      propertyList.add(createProperty(property));
    }
    
    return propertyList;
  }
  
  private CustomProperties createProperty(Map property)
  {
    CustomProperties config = new CustomProperties();
    config.setPropertyName((String)property.get("name"));
    config.setPropertyValue((String)property.get("value"));
    return config;
  }
}
