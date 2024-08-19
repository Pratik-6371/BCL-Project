package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




public class CMSState
{
  private ObjectIdLong stateId;
  private String stateNm;
  private Boolean selected;
  private static final Map<ObjectIdLong, CMSState> states;
  public static final ObjectIdLong DEFAULT_STATE_ID = new ObjectIdLong(-1L);
  
  static { List<CMSState> sts = new CMSService().getStates();
    Map<ObjectIdLong, CMSState> map = new HashMap();
    for (Iterator iterator = sts.iterator(); iterator.hasNext();) {
      CMSState st = (CMSState)iterator.next();
      if (!DEFAULT_STATE_ID.equals(st.getStateId())) {
        map.put(st.getStateId(), st);
      }
    }
    states = Collections.unmodifiableMap(map);
  }
  
  public CMSState(ObjectIdLong stateId, String stateNm)
  {
    this.stateId = stateId;
    this.stateNm = stateNm;
    selected = Boolean.valueOf(false);
  }
  
  public ObjectIdLong getStateId() { return stateId; }
  
  public void setStateId(ObjectIdLong stateId) {
    this.stateId = stateId;
  }
  
  public String getStateNm() { return stateNm; }
  
  public void setStateNm(String stateNm) {
    this.stateNm = stateNm;
  }
  
  public Boolean getSelected() { return selected; }
  
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
  
  public static String getStateName(ObjectIdLong stateId2) {
    CMSState state = (CMSState)states.get(stateId2);
    if (state == null) {
      return "";
    }
    
    return state.getStateNm();
  }
}
