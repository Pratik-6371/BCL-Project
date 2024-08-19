package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import java.util.Map;

public class ExistingConfigAttributes extends BaseConfigAttributes implements ConfigAttributes
{
  public ExistingConfigAttributes(String id, PrincipalEmployeeFacade facade)
  {
    this.id = id;
    this.facade = facade;
  }
  

  public void attributesToUi(DynamicMapBackedForm data)
  {
    Map attrs = facade.getPrincipalEmployeeConfiguration(id);
    attributesToDmbf(attrs, data);
  }
  
  public void save(DynamicMapBackedForm data)
  {
    Map attrs = dmbfToAttributes(data);
    Map[] propertyMaps = getNamedPropertiesFromDmbf(data);
    facade.updatePEConfiguration(id, attrs, propertyMaps);
    attrs = facade.getPrincipalEmployeeConfiguration(id);
    attributesToDmbf(attrs, data);
  }
}
