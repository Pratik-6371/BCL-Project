package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.HashMap;
import java.util.List;


public class CMSShift
{
  private ObjectIdLong shiftId;
  private String shiftNm;
  private String shiftIn;
  private String shiftOut;
  private String patternName;
  private ObjectIdLong unitId;
  public static final HashMap<String, String> map = new HashMap();
  
  static { map.put("shift_A", "A Shift");
    map.put("shift_B", "B Shift");
    map.put("shift_C", "C Shift");
    map.put("shift_G", "G Shift");
  }
  

  public CMSShift(ObjectIdLong shiftId, String shiftNm, String shiftIn, String shiftOut, String patternName, ObjectIdLong unitId)
  {
    this.shiftId = shiftId;
    this.shiftNm = shiftNm;
    this.shiftIn = shiftIn;
    this.shiftOut = shiftOut;
    this.patternName = patternName;
    this.unitId = unitId;
  }
  
  public ObjectIdLong getShiftId() {
    return shiftId;
  }
  
  public void setShiftId(ObjectIdLong shiftId) {
    this.shiftId = shiftId;
  }
  
  public String getShiftNm() {
    return shiftNm;
  }
  
  public void setShiftNm(String shiftNm) {
    this.shiftNm = shiftNm;
  }
  
  public String getShiftIn() {
    return shiftIn;
  }
  
  public void setShiftIn(String shiftIn) {
    this.shiftIn = shiftIn;
  }
  
  public String getShiftOut() {
    return shiftOut;
  }
  
  public void setShiftOut(String shiftOut) {
    this.shiftOut = shiftOut;
  }
  
  public String getPatternName() {
    return patternName;
  }
  
  public void setPatternName(String patternName) {
    this.patternName = patternName;
  }
  
  public ObjectIdLong getUnitId() {
    return unitId;
  }
  
  public void setUnitId(ObjectIdLong unitId) {
    this.unitId = unitId;
  }
  
  public static List<CMSShift> retrieveShifts(ObjectIdLong unitId2)
  {
    return new CMSService().retrieveShiftsByUnit(unitId2);
  }
  
  public static CMSShift retrieveShift(ObjectIdLong shiftId2)
  {
    return new CMSService().retrieveShift(shiftId2);
  }
  
  public static List<String> retrievePatternsByUnitId(ObjectIdLong unitId)
  {
    return new CMSService().retrievePatternsByUnitId(unitId);
  }
  
  public static CMSShift retrieveShiftByPatternName(String shiftPattern)
  {
    return new CMSService().retrieveShiftByPatternName(shiftPattern);
  }
  
  public static ObjectIdLong retrievePatternIdByPatternName(String shiftPattern)
  {
    return new CMSService().retrievePatternIdByPatternName(shiftPattern);
  }
  
  public static String retrievePatternName(ObjectIdLong patternId)
  {
    return new CMSService().retrievePatternNameById(patternId);
  }
  
  public static CMSShift retrieveShiftByShiftStartDate(KDateTime startdt, KDateTime enddt, ObjectIdLong unitId) {
    if (startdt == null)
      return null;
    KDateTime st = KDateTime.createDateTime();
    KDateTime et = KDateTime.createDateTime();
    st = (KDateTime)st.changeDateTime(1900, 1, 1, startdt.getHour(), startdt.getMinute(), startdt.getSecond(), 0);
    et = (KDateTime)et.changeDateTime(1900, 1, 1, enddt.getHour(), enddt.getMinute(), enddt.getSecond(), 0);
    return new CMSService().getCMSShiftByStartDate(st, et, unitId);
  }
}
