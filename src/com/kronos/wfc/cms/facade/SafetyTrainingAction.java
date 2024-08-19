package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.SafetyModule;
import com.kronos.wfc.cms.business.SafetyTraining;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServerException;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
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

public class SafetyTrainingAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-safety-application";
  static final String FORWARD_TO_LIST_TILE = "workspace";
  
  public SafetyTrainingAction() {}
  
  protected static Map methodsMap() {
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
    BaseSafetyTrainingAttributes config = new BaseSafetyTrainingAttributes();
    config.insertPropertyRow(selectedProperty, data);
    fillDropdowns(data);
    clearUiDirtyData(data);
    return mapping.findForward("success");
  }
  
  private void fillDropdowns(DynamicMapBackedForm data) {
    String unitId = (String)data.getValue("unitId");
    Collection<SafetyModule> sfts = SafetyTraining.getUniqueTrainingList();
    ArrayList<SafetyModule> trainings = new ArrayList();
    trainings.addAll(sfts);
    List<Department> depts = Department.doRetrieveByUnitId(new ObjectIdLong(unitId));
    List<Section> sects = Section.doRetrieveByUnitId(new ObjectIdLong(unitId));
    data.setValue("sections", sects);
    data.setValue("departments", depts);
    data.setValue("trainings", trainings);
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
    BaseSafetyTrainingAttributes config = new BaseSafetyTrainingAttributes();
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
    
    return mapping.findForward("doCMSSafetyWorkmenList");
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
      property.put("trnId", (String)data.getValue(getPropName(id, "trnId")));
      property.put("dateTaken", (String)data.getValue(getPropName(id, "trainingDate")));
      property.put("fromTime", (String)data.getValue(getPropName(id, "fromtime")));
      property.put("toTime", (String)data.getValue(getPropName(id, "totime")));
      property.put("nextTrnDa", (String)data.getValue(getPropName(id, "nextTrnDa")));
      property.put("trnDesc", (String)data.getValue(getPropName(id, "trnDesc")));
      property.put("dept", (String)data.getValue(getPropName(id, "department")));
      property.put("sec", (String)data.getValue(getPropName(id, "section")));
      property.put("func", (String)data.getValue(getPropName(id, "function")));
      property.put("nJob", (String)data.getValue(getPropName(id, "natureofjob")));
      property.put("module", (String)data.getValue(getPropName(id, "module")));
      property.put("TNI", (String)data.getValue(getPropName(id, "tni")));
      property.put("facultyNm", (String)data.getValue(getPropName(id, "faculty")));
      property.put("venue", (String)data.getValue(getPropName(id, "venue")));
      property.put("preTestMarksObtained", (String)data.getValue(getPropName(id, "preMarksObtained")));
      property.put("preTestMaxMarks", (String)data.getValue(getPropName(id, "preMaxMarks")));
      property.put("preTestPercentage", (String)data.getValue(getPropName(id, "preMarksPercent")));
      property.put("postTestMarksObtained", (String)data.getValue(getPropName(id, "postMarksObtained")));
      property.put("postTestMaxMarks", (String)data.getValue(getPropName(id, "postMaxMarks")));
      property.put("postTestPercentage", (String)data.getValue(getPropName(id, "postMarksPercent")));
      property.put("recommendation", (String)data.getValue(getPropName(id, "recommendForRetraining")));
      property.put("remarks", (String)data.getValue(getPropName(id, "remarks")));
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
      Map[] configs = getFacade().getWorkmenSafetyTrainings(wk.getEmpCode());
      test();
      data.setValue("cName", wk.getContractor().getcontractorName());
      data.setValue("unitName", PrincipalEmployee.doRetrieveById(wk.getContractor().getUnitId()).getUnitName());
      data.setValue("eCode", wk.getEmpCode());
      data.setValue("eName", wk.getFirstName() + " " + wk.getLastName());
      data.setValue("trade", wk.getTrade().getTradeName());
      data.setValue("skill", wk.getSkill().getSkillNm());
      setTradeConfigsToUI(data, configs);
      clearUiDirtyData(data);
    }
    catch (APIValidationException ex) {
      Exception[] exs = ex.getWrappedExceptions();
      if ((exs != null) && (exs.length > 0)) {
        for (int i = 0; i < exs.length; i++) {
          Log.log(exs[i], "error from script");
        }
      }
    } catch (Exception e) {
      logError(e);
    }
    return mapping.findForward("success");
  }
  
  private void test() {}
  
  private void setTradeConfigsToUI(DynamicMapBackedForm data, Map[] configs)
  {
    data.setValue("safetyList", configs);
    fillDropdowns(data);
    atLeastOneProperty(data);
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      saveTraining(request, form);
      StrutsUtils.addMessage(request, new Message("cms.record.saved.successfully"));
    } catch (Exception e) {
      logError(e);
      
      if ((e instanceof CMSException)) {
        CMSException ex = (CMSException)e;
        StrutsUtils.addErrorMessage(request, ex);
      }
    }
    return doRefresh(mapping, form, request, response);
  }
  
  public void roundTripProperties(DynamicMapBackedForm data) {
    Map[] properties = getPropertiesFromDmbf(data);
    data.setValue("safetyList", properties);
  }
  
  private void saveTraining(HttpServletRequest request, ActionForm form) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      String id = (String)data.getValue("id");
      String unitId = (String)data.getValue("unitId");
      Workmen wk = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      Map[] propertyMaps = getNamedPropertiesFromDmbf(data);
      getFacade().updateTrainings(wk.getEmpCode(), propertyMaps);
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
      if ((ex instanceof KServerException)) {
        throw CMSException.incorrectDate("Date");
      }
    }
  }
  
  protected Map createNewProperty() {
    Map<String, String> property = new HashMap();
    property.put("id", "new_property");
    property.put("trnId", "");
    property.put("dateTaken", "");
    property.put("fromTime", "");
    property.put("toTime", "");
    property.put("nextTrnDa", "");
    property.put("trnDesc", "");
    property.put("dept", "");
    property.put("sec", "");
    property.put("func", "");
    property.put("nJob", "");
    property.put("module", "");
    property.put("TNI", "");
    property.put("facultyNm", "");
    property.put("venue", "");
    property.put("preTestMarksObtained", "");
    property.put("preTestMaxMarks", "");
    property.put("preTestPercentage", "");
    property.put("postTestMarksObtained", "");
    property.put("postTestMaxMarks", "");
    property.put("postTestPercentage", "");
    property.put("recommendation", "");
    property.put("remarks", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data) {
    Map[] propertyMap = (Map[])data.getValue("safetyList");
    if ((propertyMap == null) || (propertyMap.length < 1)) {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("safetyList", propertyMap);
    }
  }
  
  public ActionForward validate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return mapping.findForward("success");
  }
  

  static final String UI_ID_SELECTED_IDS = "selectedIds";
  
  static final String UI_ID_SELECTED_ID = "id";
  private static Map methodsMap = methodsMap();
}
