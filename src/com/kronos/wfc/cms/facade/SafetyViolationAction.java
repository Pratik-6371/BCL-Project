package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.SafetyViolation;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServerException;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.xml.api.bean.APIValidationException;
import com.kronos.wfc.wfp.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SafetyViolationAction extends WFPLookupDispatchActions
{
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-safety-violation-application";
  static final String FORWARD_TO_LIST_TILE = "workspace";
  static final String UI_ID_SELECTED_IDS = "selectedIds";
  static final String UI_ID_SELECTED_ID = "id";
  
  public SafetyViolationAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
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
    BaseSafetyViolationAttributes config = new BaseSafetyViolationAttributes();
    config.insertPropertyRow(selectedProperty, data);
    fillDropdowns(data);
    clearUiDirtyData(data);
    return mapping.findForward("success");
  }
  
  private void fillDropdowns(DynamicMapBackedForm data) {
    Collection<SafetyViolation> sfts = SafetyViolation.getViolations();
    ArrayList<SafetyViolation> violations = new ArrayList();
    violations.addAll(sfts);
    data.setValue("safetyViolations", violations);
  }
  
  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String safetyId = (String)form.getValue("selectedId");
    return safetyId;
  }
  
  public ActionForward doDeletePropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseSafetyViolationAttributes config = new BaseSafetyViolationAttributes();
    if (!config.isPropertyBlank(selectedProperty, data))
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
  
  protected SafetyFacade getFacade() {
    return new SafetyFacadeImpl();
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    return mapping.findForward("doCMSSafetyViolationWorkmenList");
  }
  
  private void logError(Exception e) {
    Log.log(e, "Error occurred in saving safety training list");
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
      property.put("sId", (String)data.getValue(getPropName(id, "violation")));
      property.put("violationDesc", (String)data.getValue(getPropName(id, "violationDesc")));
      property.put("violationDate", (String)data.getValue(getPropName(id, "violationDate")));
      property.put("actionTaken", (String)data.getValue(getPropName(id, "actionTaken")));
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
      Map[] configs = getFacade().getWorkmenSafetyViolations(wk.getEmpCode());
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
    data.setValue("safetyViolationList", configs);
    fillDropdowns(data);
    atLeastOneProperty(data);
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    if (saveViolation(request, form))
      StrutsUtils.addMessage(request, new com.kronos.wfc.platform.i18n.framework.messages.Message("cms.record.saved.successfully"));
    return doRefresh(mapping, form, request, response);
  }
  
  public void roundTripProperties(DynamicMapBackedForm data) {
    Map[] properties = getPropertiesFromDmbf(data);
    data.setValue("safetyViolationList", properties);
  }
  
  private boolean saveViolation(HttpServletRequest request, ActionForm form) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    boolean isSaveSuccessful = false;
    try {
      String id = (String)data.getValue("id");
      String unitId = (String)data.getValue("unitId");
      Workmen wk = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      Map[] propertyMaps = getNamedPropertiesFromDmbf(data);
      getFacade().updateViolations(wk.getEmpCode(), propertyMaps);
      isSaveSuccessful = true;
    } catch (CMSException e) {
      StrutsUtils.addErrorMessage(request, e);
    } catch (BusinessValidationException ex) {
      setErrorMessagesFromExceptions(request, ex);
    } catch (KServerException ex) {
      StrutsUtils.addErrorMessage(request, CMSException.incorrectDate("Date"));
    } finally {
      roundTripProperties(data);
      atLeastOneProperty(data);
    }
    return isSaveSuccessful;
  }
  
  protected Map createNewProperty() {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("sId", "");
    property.put("violationDesc", "");
    property.put("violationDate", "");
    property.put("actionTaken", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data) {
    Map[] propertyMap = (Map[])data.getValue("safetyViolationList");
    if ((propertyMap == null) || (propertyMap.length < 1)) {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("safetyViolationList", propertyMap);
    }
  }
  




  private static Map methodsMap = methodsMap();
}
