package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import org.apache.struts.upload.FormFile;





public class ImportActionForm
  extends DynamicMapBackedForm
{
  private static final long serialVersionUID = 1L;
  private FormFile formFile;
  
  public ImportActionForm() {}
  
  public FormFile getFormFile()
  {
    return formFile;
  }
  
  public void setFormFile(FormFile formFile)
  {
    this.formFile = formFile;
  }
}
