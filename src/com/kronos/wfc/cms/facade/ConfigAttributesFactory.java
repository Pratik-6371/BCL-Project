package com.kronos.wfc.cms.facade;


public class ConfigAttributesFactory
{
  public ConfigAttributesFactory() {}
  

  public ConfigAttributes create(String id, PrincipalEmployeeFacade facade)
  {
    return new ExistingConfigAttributes(id, facade);
  }
}
