package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;

public class Wage {
  private ObjectIdLong id;
  private String basic;
  private String da;
  private String allowance;
  private KDate wageEffectiveDate;
  
  public Wage(ObjectIdLong id, String basic, String da, String allowance) { this.id = id;
    this.basic = basic;
    this.da = da;
    this.allowance = allowance;
  }
  


  public ObjectIdLong getId()
  {
    return id;
  }
  
  public void setId(ObjectIdLong id) { this.id = id; }
  
  public String getBasic() {
    return basic;
  }
  
  public void setBasic(String basic) { this.basic = basic; }
  
  public String getDa() {
    return da;
  }
  
  public void setDa(String da) { this.da = da; }
  
  public String getAllowance() {
    return allowance;
  }
  
  public void setAllowance(String allowance) { this.allowance = allowance; }
  
  public static java.util.List<Wage> getWage(ObjectIdLong id)
  {
    return new CMSService().getWageById(id);
  }
  
  public void doAdd() {
    new CMSService().doAdd(this);
  }
  
  public void doUpdate() { new CMSService().doUpdate(this); }
  
  public static Wage retrieveById(ObjectIdLong objectIdLong) {
    return new CMSService().getWage(objectIdLong);
  }
  
  public void doDelete() { new CMSService().deleteWage(this); }
  
  public void updateFields(Wage wage)
  {
    if (wage.getBasic() != null) {
      setBasic(wage.getBasic());
    }
    if (wage.getDa() != null) {
      setDa(wage.getDa());
    }
    if (wage.getAllowance() != null) {
      setAllowance(wage.getAllowance());
    }
    if (wage.getWageEffectiveDate() != null) {
        setWageEffectiveDate(wage.getWageEffectiveDate());
      }
  }



public KDate getWageEffectiveDate() {
	return wageEffectiveDate;
}

public void setWageEffectiveDate(KDate wageEffectiveDate) {
	this.wageEffectiveDate = wageEffectiveDate;
}
  
  
}
