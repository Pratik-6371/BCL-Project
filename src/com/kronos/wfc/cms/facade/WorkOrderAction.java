package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.Manager;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Supervisor;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderActivity;
import com.kronos.wfc.cms.business.WorkorderLine;
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
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;





public class WorkOrderAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  




  public WorkOrderAction() {}
  



  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("cms.action.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.wk.return", "doReturn");
    methods.put("cms.action.edit", "doEdit");
    methods.put("insertRow", "doInsertPropertyRow");
    methods.put("deleteRow", "doDeletePropertyRow");
    methods.put("cms.action.go.to.lr", "doGoToLRPage");
    
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try
    {
      validateFields(form);
      saveWorkorder(form);
      BaseActivityAttributes attr = new BaseActivityAttributes();
      Map[] propertyMaps = attr.getNamedPropertiesFromDmbf((DynamicMapBackedForm)form);
      getFacade().updateActivityConfiguration(propertyMaps, (String)((DynamicMapBackedForm)form).getValue("workorderId"));
      
      getWorkOrderToUI((DynamicMapBackedForm)form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
      StrutsUtils.addMessage(request, new Message("cms.wk.saved.successfully"));
    }
    catch (CMSException ex) {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);
    }
    return mapping.findForward("success");
  }
  
  public ActionForward doGoToLRPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try
    {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      KDate today = KDate.today();
      String wkId = (String)data.getValue("workorderId");
      Workorder wk = Workorder.retrieveByWorkOrder(wkId);
      if (today.isAfterDate(wk.getValidTo())) {
        throw CMSException.lrCreationNotAllowedForExpiredWorkOrder();
      }
      return mapping.findForward("doCMSLaborReqList");
    }
    catch (Exception e) {
      logError(e);
      //CMSException ex;
      CMSException ex; if ((e instanceof CMSException)) {
        ex = (CMSException)e;
      }
      else {
        ex = CMSException.unknown(e.getLocalizedMessage());
      }
      StrutsUtils.addErrorMessage(request, ex); }
    return doRefresh(mapping, form, request, response);
  }
  


  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    
    StrutsUtils.addErrorMessage(request, e);
  }
  


  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String tradeid = (String)form.getValue("selectedId");
    return tradeid;
  }
  
  public ActionForward doInsertPropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Workorder wk = Workorder.retrieveByWorkOrder((String)data.getValue("workorderId"));
    setValuesToUI(data, wk, request);
    String selectedProperty = getSelectedPropertyId(data);
    BaseActivityAttributes config = new BaseActivityAttributes();
    config.insertPropertyRow(selectedProperty, data);
    return mapping.findForward("success");
  }
  
  public ActionForward doDeletePropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Workorder wk = Workorder.retrieveByWorkOrder((String)data.getValue("workorderId"));
    setValuesToUI(data, wk, request);
    String selectedProperty = getSelectedPropertyId(data);
    BaseActivityAttributes config = new BaseActivityAttributes();
    if (!config.isPropertyBlank(selectedProperty, data))
      setUiDirtyData(data);
    config.deletePropertyRow(selectedProperty, data);
    return mapping.findForward("success");
  }
  

  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  

  private void setErrorMessagesFromExceptions(HttpServletRequest request, BusinessValidationException e)
  {
    Exception[] wrappedEx = e.getWrappedExceptions();
    for (int i = 0; i < wrappedEx.length; i++)
    {
      GenericException ge = (GenericException)wrappedEx[i];
      StrutsUtils.addErrorMessage(request, ge);
    }
  }
  

  private void saveWorkorder(ActionForm form)
  {
    Workorder cont = getSavedContFromUI(form);
    
    WorkOrderFacade facade = getFacade();
    
    facade.saveWorkOrder(cont);
  }
  
  public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      validateFields(form);
      saveWorkorder(form);
      BaseActivityAttributes attr = new BaseActivityAttributes();
      Map[] propertyMaps = attr.getNamedPropertiesFromDmbf((DynamicMapBackedForm)form);
      getFacade().updateActivityConfiguration(propertyMaps, (String)((DynamicMapBackedForm)form).getValue("workorderId"));
      
      getWorkOrderToUI((DynamicMapBackedForm)form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
      String unitId = (String)((DynamicMapBackedForm)form).getValue("unitId");
      String statusId = (String)((DynamicMapBackedForm)form).getValue("statusId");
      request.setAttribute("unitId", unitId);
      request.setAttribute("statusId", statusId);
      StrutsUtils.addMessage(request, new Message("cms.wk.saved.successfully"));
    }
    catch (Exception e) {
      logError(e);
    //  CMSException ex;
      CMSException ex; if ((e instanceof CMSException)) {
        ex = (CMSException)e;
      } else {
        ex = CMSException.errorNullInput();
      }
      StrutsUtils.addErrorMessage(request, ex);
      return doRefresh(mapping, form, request, response);
    }
    
    return mapping.findForward("doWorkOrderList");
  }
  
  private void validateFields(ActionForm form) throws Exception {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String ownerId = (String)data.getValue("ownerId");
    String suprId = (String)data.getValue("suprId");
    String validTo = (String)data.getValue("validTo");
    try {
      KServer.stringToDate(validTo);
    }
    catch (Exception e) {
      throw e;
    }
  }


  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String unitId = (String)((DynamicMapBackedForm)form).getValue("unitId");
    String statusId = (String)((DynamicMapBackedForm)form).getValue("statusId");
    request.setAttribute("unitId", unitId);
    request.setAttribute("statusId", statusId);
    return mapping.findForward("doWorkOrderList");
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    request.setAttribute("edit", KronosProperties.get("cms.workorder.editable", "N"));
    try
    {
      String workorderId = getSelectedId(form);
      WorkOrderFacade facade = getFacade();
      Workorder wk = facade.getWorkOrder(workorderId);
      setValuesToUI(data, wk, request);

    }
    catch (Exception e)
    {

      logError(e);
      CMSException ex = CMSException.errorNullInput();
      StrutsUtils.addErrorMessage(request, ex);
    }
    
    return mapping.findForward("success");
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
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
  
  protected WorkOrderFacade getFacade()
  {
    return new WorkOrderFacadeImpl();
  }
  

  private Workorder getSavedContFromUI(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String workorderId = (String)data.getValue("workorderId");
    Workorder order = Workorder.retrieveByWorkOrder(workorderId);
    String ownerId = (String)data.getValue("ownerId");
    String suprId = (String)data.getValue("suprId");
    String depId = (String)data.getValue("depId");
    String secId = (String)data.getValue("secId");
    String validTo = (String)data.getValue("validTo");
    order.setOwnerId(new ObjectIdLong(ownerId));
    order.setSuprid(new ObjectIdLong(suprId));
    order.setDepId(new ObjectIdLong(depId));
    order.setSecId(new ObjectIdLong(secId));
    order.setValidTo(KServer.stringToDate(validTo));
    return order;
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  



  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    request.setAttribute("edit", KronosProperties.get("cms.workorder.editable", "false"));
    
    try
    {
      getWorkOrderToUI(data, request);
    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.errorNullInput();
      StrutsUtils.addErrorMessage(request, ex);
    }
    return mapping.findForward("success");
  }
  
  private void getWorkOrderToUI(DynamicMapBackedForm data, HttpServletRequest request)
  {
    String wkId = (String)data.getValue("workorderId");
    WorkOrderFacade facade = getFacade();
    Workorder cont = facade.getWorkOrder(wkId.toString());
    setValuesToUI(data, cont, request);
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, Workorder wk, HttpServletRequest request)
  {
    data.setValue("workorderId", wk.getWorkorderId().toString());
    data.setValue("unitId", wk.getUnitId().toString());
    data.setValue("unitName", PrincipalEmployee.doRetrieveById(wk.getUnitId()).getUnitName());
    data.setValue("contractorName", Contractor.doRetrieveById(wk.getContractorId(), wk.getUnitId()).getcontractorName());
    data.setValue("vendorCode", Contractor.doRetrieveById(wk.getContractorId(), wk.getUnitId()).getVendorCode());
    data.setValue("workordertype", wk.getWkTypName());
    data.setValue("workorderDispName", wk.getWkTypDispName());
    data.setValue("validFrom", wk.getValidFrom());
    data.setValue("validTo", wk.getValidTo());
    data.setValue("depId", wk.getDepId().toString());
    data.setValue("depCode", Department.doRetrieveById(wk.getDepId()).getDescription());
    data.setValue("costcenter", wk.getCostcenter());
    data.setValue("glcode", wk.getGlcode());
    request.setAttribute("glcode", wk.getGlcode());
    data.setValue("ownerId", wk.getOwnerId() == null ? "" : wk.getOwnerId().toString());
    data.setValue("suprId", wk.getSuprid() == null ? "" : wk.getSuprid().toString());
    data.setValue("secId", wk.getSecId().toString());
    data.setValue("secCode", wk.getSectionCode());
    data.setValue("number", wk.getWkNum());
    data.setValue("name", wk.getName());
    data.setValue("availSectionNames", Section.doRetrieveByUnitId(wk.getUnitId()));
    data.setValue("availDepartmentNames", Department.doRetrieveByUnitId(wk.getUnitId()));
    request.setAttribute("dId", wk.getDepId());
    request.setAttribute("sId", wk.getSecId());
    List<WorkorderActivity> acts = getFacade().getWorkOrderActivityMap(wk.getWorkorderId());
    BaseActivityAttributes attr = new BaseActivityAttributes();
    attr.attributesToDmbf(acts, data);
    List<WorkorderLine> lines = getFacade().getWorkOrderLines(wk.getWorkorderId().toString());
    
    if ((lines == null) || (lines.isEmpty())) {
      data.setValue("wkLineAvail", "false");
    }
    else {
      data.setValue("wkLineAvail", "true");
    }
    data.setValue("wkLines", convertToMap(lines));
    data.setValue("sectionHead", getSelectedOwner(Manager.getSectionHead(wk.getSecId(), wk.getUnitId())));
    data.setValue("supervisor", getSelectedSupervisor(Supervisor.getContractorHead(wk.getContractorId())));
  }
  

  private Map[] convertToMap(List attributes)
  {
    Map[] props = null;
    if ((attributes != null) && (!attributes.isEmpty())) {
      props = new Map[attributes.size()];
      int i = 0;
      for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
        WorkorderLine wa = (WorkorderLine)iterator.next();
        props[(i++)] = createPropertyMap(wa);
      }
    }
    return props;
  }
  
  private Map createPropertyMap(WorkorderLine wa) { Map prop = new HashMap();
    prop.put("wkLineId", wa.getWkLineId().toString());
    prop.put("jobDesc", wa.getJobDesc());
    String str = wa.getItemDesc();
    String[] arrOfStr = str.split("~", 4);
    String unitId = arrOfStr[0];
    String deptId = arrOfStr[1];
    String secId = arrOfStr[2];
    String ccCode = arrOfStr[3];
    prop.put("unitCode", PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId)).getUnitCode());
    prop.put("depCode", Department.doRetrieveById(new ObjectIdLong(deptId)).getCode());
    prop.put("secCode", Section.retrieveSection(new ObjectIdLong(secId)).getName());
    prop.put("costCenter", ccCode);
    prop.put("tradeNm", wa.getTradeNm() == null ? "" : wa.getTradeNm());
    prop.put("skillNm", wa.getSkillNm() == null ? "" : wa.getSkillNm());
    prop.put("qty", wa.getQty().toString());
    prop.put("rate", wa.getRate().toString());
    prop.put("qtyCompleted", wa.getQtyCompleted());
    prop.put("serviceCode", wa.getServiceCode());
    prop.put("itemDesc", wa.getItemDesc());
    prop.put("wbsElement", wa.getWBSElement());
    prop.put("uom", wa.getUOM());
    prop.put("itemNumber", wa.getItemNumber() == null ? "" : wa.getItemNumber());
    prop.put("serviceLineItemNumber", wa.getServiceLineItemNumber() == null ? "" : wa.getServiceLineItemNumber());
    CMSService service =new CMSService();
    String workorderTypeId=service.getWorkorderType(wa.getWkLineId().toString());
    if("1".equalsIgnoreCase(workorderTypeId) || "2".equalsIgnoreCase(workorderTypeId))
    {
    	String wkLineId = wa.getWkLineId().toString();
    	String itemNumber = wa.getItemNumber();
    	String serviceLineItemNumber = wa.getServiceLineItemNumber();
    	String balanceQuantity = service.getBalanceQuantity(wkLineId,wkLineId,itemNumber,serviceLineItemNumber,wkLineId);
    	
    	wa.setBalanceQuantity(balanceQuantity);
    }
    else
    {
    	wa.setBalanceQuantity("NA");
    }
    prop.put("balanceQuantity", wa.getBalanceQuantity());
    return prop;
  }
  

  private Object getSelectedSupervisor(List<Supervisor> suprs)
  {
    if ((suprs != null) && (!suprs.isEmpty()))
    {






      return ((Supervisor)suprs.get(0)).getName();
    }
    return "";
  }
  
  private String getSelectedOwner(Manager manager)
  {
    if (manager != null) {
      return manager.getName();
    }
    return "";
  }
  

  private void logError(Exception e)
  {
    Log.log(e, "An error occured during the action on the workorder");
  }
}
