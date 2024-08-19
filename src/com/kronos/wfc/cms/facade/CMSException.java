package com.kronos.wfc.cms.facade;

import com.kronos.wfc.platform.businessobject.framework.BusinessProcessingException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.struts.framework.GenericExceptionActionMessageWorkaround;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;

public class CMSException
  extends BusinessProcessingException implements GenericExceptionActionMessageWorkaround
{
  public static final int NO_SELECTION_MADE = 1;
  public static final int MISSING_REQUIRED_FIELD = 2;
  public static final int ERROR_NULL_ARGUMENTSINPUT = 3;
  public static final int VALUE_TOO_LONG = 4;
  public static final String FIELD_NAME = "FieldName";
  public static final int DUPLICATE_CONFIG_PROPERTY = 5;
  public static final int MISSING_REQUIRED_FIELDS = 6;
  public static final int FROMDATE_BEFORE_EX_FROMDATE = 7;
  public static final int ONLY_NUMBERS_ARE_ALLOWED = 8;
  public static final int UNKOWNEXCEPTIONOCCURED = 9;
  public static final int UNABLETOFINDANYRECORDS = 10;
  public static final int SCHEDULING_NOT_ALLOWED = 11;
  public static final int LRCANBERAISEDFORCURRENTCALENDAR = 12;
  public static final int LRTIMEFRAMEISINVALID = 13;
  public static final int LRQTYGREATERTHANZERO = 14;
  public static final int TIMEFRAMESELECTEDISNOTVALID = 15;
  public static final int SCHEDULETIMEFRAMEISINVALID = 16;
  public static final int CANNOTEXCEEDBALANCEQTY = 17;
  public static final int APPROVEDLRCANNOTBEDELETED = 18;
  public static final int LRCANNOTBEDELETED = 19;
  public static final int LREXISTSWITHSAMEDATERANGE = 20;
  public static final int SCHEDULEQTYEXCEEDED = 21;
  public static final int EXPIREDWORKORDER = 22;
  public static final int VALIDATIONERROR = 23;
  public static final int LICENSEVALIDITYEXPIRED = 24;
  public static final int EXCEEDEDCOVERAGEQTY = 25;
  public static final int NO_EMPLOYEE_FOUND = 26;
  public static final int GENERICERROR = 27;
  public static final int INVALID_DATE_VALUE = 28;
  public static final int INVALID_IMAGE = 29;
  public static final int SAFETY_TRAINING_MANDATORY = 30;
  public static final int INVALID_DATE = 31;
  public static final int CUSTOM_MESSAGE = 33;
  public static final String CUSTOMMSG = "Message";
  
  public CMSException(int i)
  {
    super(i);
  }
  
  public static CMSException customMessage(String message)
  {
    CMSException ex = new CMSException(33);
    ex.addUserParameter("Message", message);
    return ex;
  }
  
  public static CMSException noSelectionMade() {
    CMSException ex = new CMSException(1);
    return ex;
  }
  
  public static CMSException missingRequiredField(String fieldName) {
    CMSException ex = new CMSException(2);
    ex.addUserParameter("FieldName", fieldName);
    return ex;
  }
  
  public static CMSException errorNullInput() {
    CMSException ex = new CMSException(3);
    return ex;
  }
  
  public static CMSException valueTooLong(String propertyName) {
    CMSException ex = new CMSException(4);
    ex.addUserParameter("propertyName", propertyName);
    return ex;
  }
  
  public String getMessageKey() {
    return getMessageKey(getErrorCode());
  }
  
  public static CMSException duplicatedConfigProperty(String name) {
    CMSException ex = new CMSException(5);
    ex.addUserParameter("propertyName", name);
    return ex;
  }
  
  public static Exception missingRequiredFields() {
    CMSException ex = new CMSException(6);
    return ex;
  }
  
  public static CMSException fromDateBeforeExistingRecord(String date) {
    CMSException ex = new CMSException(7);
    ex.addUserParameter("propertyName", date);
    return ex;
  }
  
  public static CMSException unknown(String localizedMessage) {
    CMSException ex = new CMSException(9);
    ex.addUserParameter("propertyName", localizedMessage);
    return ex;
  }
  
  public static CMSException onlyNumbersAllowed(String message) {
    CMSException ex = new CMSException(8);
    ex.addUserParameter("propertyName", message);
    return ex;
  }
  
  public static CMSException unableTofindAnyRecords() {
    CMSException ex = new CMSException(10);
    return ex;
  }
  
  public static CMSException lrNotApprovedYet() {
    CMSException ex = new CMSException(11);
    return ex;
  }
  
  public static CMSException lrCanBeRaisedForCurrentCalendarMonth(String string)
  {
    CMSException ex = new CMSException(12);
    ex.addUserParameter("propertyName", string);
    return ex;
  }
  
  public static CMSException lrTimeframeIsNotValid() {
    CMSException ex = new CMSException(13);
    return ex;
  }
  
  public static CMSException lrQuantityShouldBeGreaterThanZero()
  {
    CMSException ex = new CMSException(14);
    return ex;
  }
  
  public static CMSException timeframesNotValid() {
    CMSException ex = new CMSException(15);
    return ex;
  }
  
  public static CMSException schedulingTimeframeIsNotValid() {
    CMSException ex = new CMSException(16);
    return ex;
  }
  
  public static CMSException cannotExceedBalanceQty(Long balance, String tradeName, String skillNm)
  {
    CMSException ex = new CMSException(17);
    ex.addUserParameter("balance", balance.toString());
    ex.addUserParameter("trade", tradeName);
    ex.addUserParameter("skill", skillNm);
    return ex;
  }
  


  public static CMSException approvedLRCannotBeDeleted()
  {
    CMSException ex = new CMSException(18);
    return ex;
  }
  
  public static CMSException lrCannotBeDeleted() {
    CMSException ex = new CMSException(19);
    return ex;
  }
  
  public static CMSException lrWithTheSameDateRangeExists() {
    CMSException ex = new CMSException(20);
    return ex;
  }
  
  public static CMSException scheduleQtyExceededOnDate(KDate fromdt, String shiftNm) {
    CMSException ex = new CMSException(21);
    ex.addUserParameter("date", KServer.dateToString(fromdt));
    ex.addUserParameter("shift", shiftNm);
    return ex;
  }
  
  public static CMSException lrCreationNotAllowedForExpiredWorkOrder() {
    CMSException ex = new CMSException(22);
    return ex;
  }
  
  public static CMSException validationError() {
    CMSException ex = new CMSException(23);
    return ex;
  }
  
  public static CMSException contractorLicenseValidityExpired() {
    CMSException ex = new CMSException(24);
    return ex;
  }
  
  public static CMSException exceededCoverageQty(String coverage) {
    CMSException ex = new CMSException(25);
    ex.addUserParameter("qty", coverage);
    
    return ex;
  }
  
  public static CMSException errorMessage(String errorMessage) {
    CMSException ex = new CMSException(27);
    ex.addUserParameter("message", errorMessage);
    return ex;
  }
  
  public static CMSException invalidDate(String fieldName) {
    CMSException ex = new CMSException(28);
    ex.addUserParameter("FieldName", fieldName);
    return ex;
  }
  
  public static CMSException invalidImage(String fieldName) {
    CMSException ex = new CMSException(29);
    ex.addUserParameter("FieldName", fieldName);
    return ex;
  }
  
  public static CMSException safetyTrainingIsMandatory(String message) {
    CMSException ex = new CMSException(30);
    ex.addUserParameter("propertyName", message);
    return ex;
  }
  
  public static CMSException incorrectDate(String fieldName) {
    CMSException ex = new CMSException(31);
    ex.addUserParameter("FieldName", fieldName);
    return ex;
  }
}
