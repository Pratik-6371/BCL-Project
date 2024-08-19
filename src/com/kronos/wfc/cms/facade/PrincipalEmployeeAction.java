package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
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







public class PrincipalEmployeeAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-principal-application";
  static final String FORWARD_TO_LIST_TILE = "workspace";
  static final String UI_ID_SELECTED_IDS = "selectedIds";
  static final String UI_ID_SELECTED_ID = "selectedId";
  static final String UI_ID_WEB_SERVICE_CONFIG_LIST = "cmsConfigList";
  
  public PrincipalEmployeeAction() {}
  
  protected static Map methodsMap() {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("cms.action.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.return", "doReturn");
    methods.put("insertRow", "doInsertPropertyRow");
    methods.put("deleteRow", "doDeletePropertyRow");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap;
  }
  

  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return savePrincipalEmployee(mapping, request, form, "success", "success");
  }
  

  private ActionForward savePrincipalEmployee(ActionMapping mapping, HttpServletRequest request, ActionForm form, String sforwardName, String fForwardName)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    ConfigAttributesFactory factory = getConfigAttributesFactory();
    ConfigAttributes config = factory.create(getId(data), getFacade());
    BaseConfigAttributes baseConfig = new BaseConfigAttributes();
    try {
      validateForm(data);
      config.save(data);
      clearUiDirtyData(data);
      baseConfig.atLeastOneProperty(data);
    }
    catch (CMSException e) {
      StrutsUtils.addErrorMessage(request, e);
      baseConfig.roundTripProperties(data);
      baseConfig.atLeastOneProperty(data);
      logError(e);
      data.setValue("availStateNames", new CMSService().getStates());
      return mapping.findForward(fForwardName);
    }
    catch (BusinessValidationException ex) {
      setErrorMessagesFromExceptions(request, ex);
      baseConfig.roundTripProperties(data);
      baseConfig.atLeastOneProperty(data);
      logError(ex);
      data.setValue("availStateNames", new CMSService().getStates());
      return mapping.findForward(fForwardName);
    }
    catch (Exception ex) {
      logError(ex);
      baseConfig.roundTripProperties(data);
      baseConfig.atLeastOneProperty(data);
      data.setValue("availStateNames", new CMSService().getStates());
      return mapping.findForward(fForwardName);
    }
    StrutsUtils.addMessage(request, new Message("cms.pe.saved.successfully"));
    return mapping.findForward(sforwardName);
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  
  private void setErrorMessagesFromExceptions(HttpServletRequest request, BusinessValidationException e) {
    Exception[] wrappedEx = e.getWrappedExceptions();
    for (int i = 0; i < wrappedEx.length; i++)
    {
      GenericException ge = (GenericException)wrappedEx[i];
      StrutsUtils.addErrorMessage(request, ge);
    }
  }
  

  protected Map[] getNamedPropertiesFromDmbf(DynamicMapBackedForm data)
  {
    Map[] allProps = getPropertiesFromDmbf(data);
    List namedPropsList = new ArrayList();
    if (allProps != null)
    {
      Map[] arr$ = allProps;
      int len$ = arr$.length;
      for (int i$ = 0; i$ < len$; i$++)
      {
        Map prop = arr$[i$];
        String propName = (String)prop.get("name");
        String propValue = (String)prop.get("value");
        if (((propName != null) && (!propName.equals(""))) || ((propValue != null) && (!propValue.equals("")))) {
          namedPropsList.add(prop);
        }
      }
    }
    Map[] namedProps = new Map[namedPropsList.size()];
    namedProps = (Map[])namedPropsList.toArray(namedProps);
    return namedProps;
  }
  

  private String getPropNameName(String id)
  {
    return id + "_name";
  }
  
  private String getPropValueName(String id)
  {
    return id + "_value";
  }
  
  public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return savePrincipalEmployee(mapping, request, form, "doCMSPrincipalListApplication", "success");
  }
  

  protected Map[] getPropertiesFromDmbf(DynamicMapBackedForm data)
  {
    String[] propertyIds = (String[])data.getValue("propertyIds");
    if (propertyIds == null)
      return null;
    Map[] properties = new HashMap[propertyIds.length];
    int i = 0;
    String[] arr$ = propertyIds;
    int len$ = arr$.length;
    for (int i$ = 0; i$ < len$; i$++)
    {
      String id = arr$[i$];
      Map property = new HashMap(5);
      String propName = (String)data.getValue(getPropNameName(id));
      String propVal = (String)data.getValue(getPropValueName(id));
      property.put("id", id);
      property.put("name", propName);
      property.put("value", propVal);
      properties[(i++)] = property;
    }
    
    return properties;
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSPrincipalListApplication");
  }
  
  public ActionForward doInsertPropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseConfigAttributes config = new BaseConfigAttributes();
    config.insertPropertyRow(selectedProperty, data);
    data.setValue("availStateNames", new CMSService().getStates());
    request.setAttribute("peaddress3", (String)data.getValue("peaddress3"));
    request.setAttribute("manageraddress3", (String)data.getValue("manageraddress3"));
    
    return mapping.findForward("success");
  }
  
  public ActionForward doDeletePropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseConfigAttributes config = new BaseConfigAttributes();
    if (!config.isPropertyBlank(selectedProperty, data))
      setUiDirtyData(data);
    config.deletePropertyRow(selectedProperty, data);
    data.setValue("availStateNames", new CMSService().getStates());
    request.setAttribute("peaddress3", (String)data.getValue("peaddress3"));
    request.setAttribute("manageraddress3", (String)data.getValue("manageraddress3"));
    
    return mapping.findForward("success");
  }
  
  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String configId = (String)form.getValue("selectedId");
    return configId;
  }
  
  private String getId(DynamicMapBackedForm form)
  {
    String id = (String)form.getValue("peid");
    return id;
  }
  

  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    try
    {
      String unitId = getSelectedId(form);
      validateForm(data);
      ExistingConfigAttributes config = new ExistingConfigAttributes(unitId, getFacade());
      config.attributesToUi((DynamicMapBackedForm)form);
    }
    catch (CMSException e) {
      if ((e instanceof CMSException)) {
        StrutsUtils.addErrorMessage(request, e);
      }
      logError(e);

    }
    catch (Exception ex)
    {
      StrutsUtils.addErrorMessage(request, CMSException.unknown(ex.getLocalizedMessage()));
      logError(ex);
    }
    finally
    {
      data.setValue("availStateNames", new CMSService().getStates());
      request.setAttribute("peaddress3", (String)data.getValue("peaddress3"));
      request.setAttribute("manageraddress3", (String)data.getValue("manageraddress3"));
    }
    return mapping.findForward("success");
  }
  

  String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  


  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  

  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    StrutsUtils.addErrorMessage(request, e);
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      validateForm(data);
      ConfigAttributesFactory factory = getConfigAttributesFactory();
      ConfigAttributes config = factory.create(getId(data), getFacade());
      config.attributesToUi(data);
      clearUiDirtyData(data);

    }
    catch (Exception e)
    {

      logError(e);
    }
    finally
    {
      data.setValue("availStateNames", new CMSService().getStates());
      request.setAttribute("peaddress3", (String)data.getValue("peaddress3"));
      request.setAttribute("manageraddress3", (String)data.getValue("manageraddress3"));
    }
    return mapping.findForward("success");
  }
  
  private ConfigAttributesFactory getConfigAttributesFactory()
  {
    return new ConfigAttributesFactory();
  }
  



  private void logError(Exception e) {}
  


  protected PrincipalEmployeeFacade getFacade()
  {
    return new PrincipalEmployeeFacadeImpl();
  }
  
  private void validateForm(DynamicMapBackedForm data)
    throws Exception
  {
    String unitname = (String)data.getValue("unitname");
    String peaddress = (String)data.getValue("peaddress");
    String peaddress1 = (String)data.getValue("peaddress1");
    String peaddress2 = (String)data.getValue("peaddress2");
    String peaddress3 = (String)data.getValue("peaddress3");
    String managername = (String)data.getValue("managername");
    String manageraddress = (String)data.getValue("manageraddress");
    String manageraddress1 = (String)data.getValue("manageraddress1");
    String manageraddress2 = (String)data.getValue("manageraddress2");
    String manageraddress3 = (String)data.getValue("manageraddress3");
    String typeofbusiness = (String)data.getValue("typeofbusiness");
    String maxnumberofworkmen = (String)data.getValue("maxnumberofworkmen");
    String maxnumberofcontractworkmen = (String)data.getValue("maxnumberofcontractworkmen");
    String boc = (String)data.getValue("boc");
    String licensenumber = (String)data.getValue("licensenumber");
    String pfcode = (String)data.getValue("pfcode");
    String esiwc = (String)data.getValue("esiwc");
    
    if ("".equalsIgnoreCase(unitname)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.unitname", " Unit Name"));
    }
    
    if ("".equalsIgnoreCase(peaddress)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.address", " Address"));
    }
    
    if ("".equalsIgnoreCase(peaddress1)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.address1", " Address1"));
    }
    if ("".equalsIgnoreCase(peaddress2)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.address2", "Address2"));
    }
    if ("".equalsIgnoreCase(peaddress3)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.address3", " Address3"));
    }
    
    if ("".equalsIgnoreCase(managername)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.managername", " Manager Name"));
    }
    if ("".equalsIgnoreCase(manageraddress)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.manageraddress", " Manager Address"));
    }
    
    if ("".equalsIgnoreCase(manageraddress1)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.manageraddress1", " Manager Address1"));
    }
    if ("".equalsIgnoreCase(manageraddress2)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.manageraddress2", " Manager Address2"));
    }
    if ("".equalsIgnoreCase(manageraddress3)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.manageraddress3", " Manager Address3"));
    }
    if ("".equalsIgnoreCase(typeofbusiness)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.typeofbusiness", " Type Of Business"));
    }
    if ("".equalsIgnoreCase(maxnumberofworkmen)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.maxnumberofworkmen", "Max Number Of WorkMen"));
    }
    if ("".equals(maxnumberofworkmen))
    {
      throw CMSException.onlyNumbersAllowed(KronosProperties.get("label.esiwcnumber", "ESI/WC Number"));
    }
    
    if ("".equalsIgnoreCase(maxnumberofcontractworkmen)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.maxnumberofcontractworkmen", " Max Number Of ContracrWorkMen"));
    }
    if ("".equals(maxnumberofcontractworkmen))
    {
      throw CMSException.onlyNumbersAllowed(KronosProperties.get("label.esiwcnumber", "ESI/WC Number"));
    }
    if ("".equalsIgnoreCase(boc)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.boc", " BOC"));
    }
    

    if ("".equalsIgnoreCase(licensenumber)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.licensenumber", " License Number"));
    }
    
    if ("".equalsIgnoreCase(pfcode)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.pfcode", "PFCode"));
    }
    if ("".equalsIgnoreCase(esiwc)) {
      throw CMSException.missingRequiredField(KronosProperties.get("label.esiwc", " Manager Addres"));
    }
    
    if ((!"".equalsIgnoreCase(unitname)) && (!"".equalsIgnoreCase(peaddress)) && (!"".equalsIgnoreCase(peaddress1)) && (!"".equalsIgnoreCase(peaddress2)) && 
      (!"".equalsIgnoreCase(peaddress3)) && (!"".equalsIgnoreCase(managername)) && (!"".equalsIgnoreCase(manageraddress)) && (!"".equalsIgnoreCase(manageraddress1)) && 
      (!"".equalsIgnoreCase(manageraddress2)) && (!"".equalsIgnoreCase(manageraddress3)) && (!"".equalsIgnoreCase(typeofbusiness)) && (!"".equalsIgnoreCase(maxnumberofworkmen)) && (!"".equalsIgnoreCase(maxnumberofcontractworkmen)) && 
      (!"".equalsIgnoreCase(boc)) && (!"".equalsIgnoreCase(licensenumber)) && 
      (!"".equalsIgnoreCase(pfcode))) { "".equalsIgnoreCase(esiwc);
    }
  }
  







  private static Map methodsMap = methodsMap();
}
