package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.List;

public abstract interface WorkmenFacade
{
  public abstract List<Contractor> getContractors(String paramString);
  
  public abstract Workmen getWorkmen(String paramString1, String paramString2);
  
  public abstract void addWorkmen(Workmen paramWorkmen);
  
  public abstract void updateWorkmen(Workmen paramWorkmen);
  
  public abstract void deleteWorkmen(String paramString1, String paramString2);
  
  public abstract Workmen saveWorkmen(Workmen paramWorkmen, String paramString);
  
  public abstract Workmen rehireWorkmen(Workmen paramWorkmen, String paramString);
  
  public abstract List<CMSState> getAllStates();
  
  public abstract List<Skill> getSkills();
  
  public abstract List<Workmen> getWorkmenList(String paramString1, Trade paramTrade, Skill paramSkill, String paramString2, ObjectIdLong paramObjectIdLong);
  
  public abstract List<Workmen> getWorkmenList(String paramString1, ObjectIdLong paramObjectIdLong, String paramString2);
  
  public abstract List<Workmen> getWorkmenListByEmpCode(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract List<Workmen> getWorkmenListByEmpName(String paramString1, String paramString2, String paramString3);
  
  public abstract List<Workmen> getWorkmenListByEmpCode(String paramString1, String paramString2);
  
  public abstract List<Workmen> getWorkmenListByEmpName(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract List<Workmen> getWorkmenList(ObjectIdLong paramObjectIdLong);
}
