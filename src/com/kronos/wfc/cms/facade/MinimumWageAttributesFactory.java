package com.kronos.wfc.cms.facade;


public class MinimumWageAttributesFactory
{
  public MinimumWageAttributesFactory() {}
  

  public MinimumWageAttributes create(String id, MinimumWageFacade facade)
  {
    return new ExistingMinimumWageAttributes(id, facade);
  }
}
