package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.HealthInformation;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import com.kronos.wfc.platform.xml.api.bean.APIValidationException;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;






public class HealthInformationAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-health-information-application";
  static final String FORWARD_TO_LIST_TILE = "workspace";
  static final String UI_ID_SELECTED_IDS = "selectedIds";
  static final String UI_ID_SELECTED_ID = "id";
  
  public HealthInformationAction() {}
  
  protected static Map methodsMap() { Map<String, String> methods = new HashMap();
    methods.put("cms.action.safety.refresh", "doRefresh");
    methods.put("cms.action.safety.save", "doSave");
    methods.put("cms.action.safety.return", "doReturn");
    methods.put("insertRow", "doInsertPropertyRow");
    methods.put("deleteRow", "doDeletePropertyRow");
    return methods;
  }
  
  protected Map getKeyMethodMap() {
    return methodsMap;
  }
  
  public ActionForward doInsertPropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseHealthInformationAttributes config = new BaseHealthInformationAttributes();
    config.insertPropertyRow(selectedProperty, data);
    fillDropdowns(data);
    clearUiDirtyData(data);
    return mapping.findForward("success");
  }
  

  private void fillDropdowns(DynamicMapBackedForm data) {}
  
  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String healthId = (String)form.getValue("selectedId");
    return healthId;
  }
  
  public ActionForward doDeletePropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseHealthInformationAttributes config = new BaseHealthInformationAttributes();
    
    setUiDirtyData(data);
    config.deletePropertyRow(selectedProperty, data);
    fillDropdowns(data);
    return mapping.findForward("success");
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  void setNoSelectionErrorMessage(HttpServletRequest request) {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  
  protected HealthInformationFacade getFacade() {
    return new HealthInformationImpl();
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    return mapping.findForward("doCMSHealthInformationWorkmenList");
  }
  
  private void logError(Exception e) {
    Log.log(e, "Error occurred in saving health information list");
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  private void setUiDirtyData(DynamicMapBackedForm data) {
    data.setState("ui_has_unsaved_data", "true");
  }
  
  private void setErrorMessagesFromExceptions(HttpServletRequest request, BusinessValidationException e) {
    Exception[] wrappedEx = e.getWrappedExceptions();
    for (int i = 0; i < wrappedEx.length; i++) {
      GenericException ge = (GenericException)wrappedEx[i];
      StrutsUtils.addErrorMessage(request, ge);
    }
  }
  
  protected Map[] getNamedPropertiesFromDmbf(DynamicMapBackedForm data)
  {
    Map[] allProps = getPropertiesFromDmbf(data);
    List namedPropsList = new ArrayList();
    if (allProps != null) {
      Map[] arr$ = allProps;
      int len$ = arr$.length;
      for (int i$ = 0; i$ < len$; i$++) {
        Map prop = arr$[i$];
        String id = (String)prop.get("id");
        if ((id != null) && (!id.equals(""))) {
          namedPropsList.add(prop);
        }
      }
    }
    Map[] namedProps = new Map[namedPropsList.size()];
    namedProps = (Map[])namedPropsList.toArray(namedProps);
    return namedProps;
  }
  
  protected Map[] getPropertiesFromDmbf(DynamicMapBackedForm data) {
    String[] propertyIds = (String[])data.getValue("propertyIds");
    if (propertyIds == null)
      return null;
    Map[] properties = new HashMap[propertyIds.length];
    int i = 0;
    String[] arr$ = propertyIds;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++) {
      String id = arr$[i$];
      Map property = new HashMap(5);
      property.put("id", id);
      property.put("healthText", (String)data.getValue(getPropName(id, "healthText")));
      property.put("healthDesc", (String)data.getValue(getPropName(id, "healthDesc")));
      property.put("healthTestDate", (String)data.getValue(getPropName(id, "healthTestDate")));
      property.put("majorConcerns", (String)data.getValue(getPropName(id, "majorConcerns")));
      property.put("nextMedicalTest", (String)data.getValue(getPropName(id, "nextMedicalTest")));
      properties[(i++)] = property;
    }
    
    return properties;
  }
  
  private String getPropName(String id, String name) {
    return id + "_" + name;
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    String id = (String)data.getValue("id");
    if ((id == null) || ("".equalsIgnoreCase(id))) {
      id = (String)request.getAttribute("id");
      data.setValue("id", id);
    }
    String unitId = (String)data.getValue("unitId");
    Workmen wk = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
    try {
      Map[] configs = getFacade().getWorkmenHealthRecords(wk.getEmpCode());
      test();
      data.setValue("cName", wk.getContractor().getcontractorName());
      data.setValue("unitName", PrincipalEmployee.doRetrieveById(wk.getContractor().getUnitId()).getUnitName());
      data.setValue("eCode", wk.getEmpCode());
      data.setValue("eName", wk.getFirstName() + " " + wk.getLastName());
      data.setValue("trade", wk.getTrade().getTradeName());
      data.setValue("skill", wk.getSkill().getSkillNm());
      setConfigsToUI(data, configs);
      clearUiDirtyData(data);
    }
    catch (APIValidationException ex) {
      Exception[] exs = ex.getWrappedExceptions();
      if ((exs != null) && (exs.length > 0)) {
        for (int i = 0; i < exs.length; i++) {
          Log.log(exs[i], "error from script");
        }
      }
    }
    catch (Exception e) {
      logError(e);
    }
    
    return mapping.findForward("success");
  }
  
  private void test() {}
  
  private void setConfigsToUI(DynamicMapBackedForm data, Map[] configs)
  {
    data.setValue("healthInformationList", configs);
    fillDropdowns(data);
    atLeastOneProperty(data);
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      List<HealthInformation> healthInformation = getHealthInformation(form);
      saveHealthInformation(request, form, healthInformation);
      StrutsUtils.addMessage(request, new Message("cms.record.saved.successfully"));
    } catch (Exception e) {
      logError(e);
      CMSException ex;
      if (!(e instanceof CMSException)) {
    ex = (CMSException)e;
    StrutsUtils.addErrorMessage(request, ex);
    Exception[] list = ex.getWrappedExceptions();
    if ((list != null) && (list.length > 0)) {
      for (int i = 0; i < list.length; i++) {
        CMSException exception = (CMSException)list[i];
        StrutsUtils.addErrorMessage(request, exception);
      }
    }
      }
    }
    return doRefresh(mapping, form, request, response);
  }
  





  private List<HealthInformation> getHealthInformation(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Map[] propertyMaps = getNamedPropertiesFromDmbf(data);
    List<HealthInformation> healthInformation = new ArrayList(0);
    List<CMSException> ex = new ArrayList(0);
    for (int i = 0; i < propertyMaps.length; i++) {
      Map property = propertyMaps[i];
      String healthText = (String)property.get("healthText");
      if ((healthText == null) || ("".equals(healthText)))
        ex.add(CMSException.missingRequiredField(KronosProperties.get("cms.label.healthText", "Health Text")));
      String healthDescription = (String)property.get("healthDesc");
      if ((healthDescription == null) || ("".equals(healthDescription)))
        ex.add(
          CMSException.missingRequiredField(KronosProperties.get("cms.label.healthDesc", "Health Description")));
      String healthTestDateString = (String)property.get("healthTestDate");
      KDateTime healthTestDate = null;
      if (healthTestDateString == null) {
        ex.add(
          CMSException.missingRequiredField(KronosProperties.get("cms.label.healthTestDate", "Health Test Date")));
      } else {
        try {
          healthTestDate = KServer.stringToDateTime(healthTestDateString);
        } catch (Exception e) {
          ex.add(
            CMSException.incorrectDate(KronosProperties.get("cms.label.healthTestDate", "Health Test Date")));
        }
      }
      String majorConcerns = (String)property.get("majorConcerns");
      if (majorConcerns == null)
        ex.add(
          CMSException.missingRequiredField(KronosProperties.get("cms.label.majorConcerns", "Major Concerns")));
      String nextMedicalTestString = (String)property.get("nextMedicalTest");
      KDateTime nextMedicalTest = null;
      if (nextMedicalTestString == null) {
        ex.add(
          CMSException.missingRequiredField(KronosProperties.get("cms.label.nextMedicalTest", "Next Medical Test")));
      } else {
        try {
          nextMedicalTest = KServer.stringToDateTime(nextMedicalTestString);
        } catch (Exception e) {
          ex.add(
            CMSException.incorrectDate(KronosProperties.get("cms.label.nextMedicalTest", "Next Medical Test")));
        }
      }
      if ((propertyMaps.length == 1) && (isBlank(healthText, healthDescription, healthTestDateString, majorConcerns, nextMedicalTestString)))
        ex.clear();
      HealthInformation hi = createHealthInformationEntity(healthText, healthDescription, healthTestDate, 
        majorConcerns, nextMedicalTest);
      healthInformation.add(hi);
    }
    if (ex.size() > 0) {
      CMSException e = CMSException.validationError();
      e.addWrappedExceptions(ex);
      throw e;
    }
    return healthInformation;
  }
  









  private boolean isBlank(String healthText, String healthDescription, String healthTestDateString, String majorConcerns, String nextMedicalTestString)
  {
    if ((healthText.isEmpty()) && (healthDescription.isEmpty()) && (healthTestDateString.isEmpty()) && (majorConcerns.isEmpty()) && (nextMedicalTestString.isEmpty()))
      return true;
    return false;
  }
  










  private HealthInformation createHealthInformationEntity(String healthText, String healthDescription, KDateTime healthTestDate, String majorConcerns, KDateTime nextMedicalTest)
  {
    HealthInformation hi = new HealthInformation(null, healthText, healthDescription, healthTestDate, majorConcerns, 
      nextMedicalTest, CurrentUserAccountManager.getUserAccountName(), KDateTime.createDateTime());
    return hi;
  }
  
  public void roundTripProperties(DynamicMapBackedForm data) {
    Map[] properties = getPropertiesFromDmbf(data);
    data.setValue("healthInformationList", properties);
  }
  
  private void saveHealthInformation(HttpServletRequest request, ActionForm form, List<HealthInformation> healthInformation)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      String id = (String)data.getValue("id");
      String unitId = (String)data.getValue("unitId");
      Workmen wk = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      
      getFacade().updateHealthRecords(wk.getEmpCode(), healthInformation);
    } catch (CMSException e) {
      StrutsUtils.addErrorMessage(request, e);
      roundTripProperties(data);
      atLeastOneProperty(data);
    } catch (BusinessValidationException ex) {
      setErrorMessagesFromExceptions(request, ex);
      roundTripProperties(data);
      atLeastOneProperty(data);
    } catch (Exception ex) {
      logError(ex);
    }
  }
  
  protected Map createNewProperty() {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("healthText", "");
    property.put("healthDesc", "");
    property.put("healthTestDate", "");
    property.put("majorConcerns", "");
    property.put("nextMedicalTest", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data) {
    Map[] propertyMap = (Map[])data.getValue("healthInformationList");
    if ((propertyMap == null) || (propertyMap.length < 1)) {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("healthInformationList", propertyMap);
    }
  }
  




  private static Map methodsMap = methodsMap();
}
