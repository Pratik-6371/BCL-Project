package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class Skill
{
  private ObjectIdLong skillId;
  private String skillNm;
  private Boolean selected;
  private static final Map<ObjectIdLong, Skill> skills;
  
  static
  {
    List<Skill> sks = new CMSService().getSkills();
    Map<ObjectIdLong, Skill> map = new HashMap();
    for (Iterator iterator = sks.iterator(); iterator.hasNext();) {
      Skill skill = (Skill)iterator.next();
      map.put(skill.getSkillId(), skill);
    }
    skills = Collections.unmodifiableMap(map);
  }
  
  public Skill(ObjectIdLong skillId, String skillNm)
  {
    this.skillId = skillId;
    this.skillNm = skillNm;
    selected = Boolean.valueOf(false);
  }
  
  public ObjectIdLong getSkillId() {
    return skillId;
  }
  
  public void setSkillId(ObjectIdLong skillId) { this.skillId = skillId; }
  
  public String getSkillNm() {
    return skillNm;
  }
  
  public void setSkillNm(String skillNm) { this.skillNm = skillNm; }
  
  public Boolean getSelected()
  {
    return selected;
  }
  
  public void setSelected(Boolean selected) { this.selected = selected; }
  
  public static Skill retrieveSkill(ObjectIdLong skillId)
  {
    return (Skill)skills.get(skillId) == null ? new CMSService().getSkillById(skillId) : (Skill)skills.get(skillId);
  }
  
  public static List<Skill> retrieveSkills() { 
	  return new CMSService().getSkills(); 
	}
  
  public static List<Skill> retrieveSkillsByUnit(ObjectIdLong unitId)
  {
    CMSService service = new CMSService();
    ArrayList<ObjectIdLong> skillIds = service.getSkillIdsForUnit(unitId);
    
    ArrayList<Skill> sks = filterSkills(skillIds);
    
    return sks;
  }
  
  private static ArrayList<Skill> filterSkills(ArrayList<ObjectIdLong> skillIds)
  {
    ArrayList<Skill> sks = new ArrayList();
    if ((skillIds != null) && (!skillIds.isEmpty())) {
      for (Iterator iterator = skillIds.iterator(); iterator.hasNext();) {
        ObjectIdLong skillId = (ObjectIdLong)iterator.next();
        Skill skill = (Skill)skills.get(skillId);
        sks.add(skill);
      }
    }
    return sks;
  }
}
