package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;

public abstract interface ConfigAttributes
{
  public abstract void attributesToUi(DynamicMapBackedForm paramDynamicMapBackedForm);
  
  public abstract void save(DynamicMapBackedForm paramDynamicMapBackedForm);
}
