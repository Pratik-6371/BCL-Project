package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.Map;




public class ExistingMinimumWageAttributes
  implements MinimumWageAttributes
{
  private String id;
  private MinimumWageFacade facade;
  
  public ExistingMinimumWageAttributes(String id, MinimumWageFacade facade)
  {
    this.id = id;
    this.facade = facade;
  }
  
  private void attributesToDmbf(Map attrs, DynamicMapBackedForm data) {}
  
  public void save(DynamicMapBackedForm dynamicmapbackedform) {}
}
