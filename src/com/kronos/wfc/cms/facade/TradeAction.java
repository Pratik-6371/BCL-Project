package com.kronos.wfc.cms.facade;

import com.kronos.wfc.datacollection.empphoto.api.APIEmpPhotoBean;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import com.kronos.wfc.platform.utility.framework.datetime.KTime;
import com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration;
import com.kronos.wfc.platform.xml.api.bean.APICurrentUserBean;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import com.kronos.wfc.platform.xml.api.bean.APIValidationException;
import com.kronos.wfc.platform.xml.api.bean.ParameterMap;
import com.kronos.wfc.timekeeping.core.api.APIPayCodeEditBean;
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




public class TradeAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-trade-application";
  static final String FORWARD_TO_LIST_TILE = "workspace";
  static final String UI_ID_SELECTED_IDS = "selectedIds";
  static final String UI_ID_SELECTED_ID = "id";
  
  public TradeAction() {}
  
  protected static Map methodsMap() {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("insertRow", "doInsertPropertyRow");
    methods.put("deleteRow", "doDeletePropertyRow");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap;
  }
  

  public ActionForward doInsertPropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseTradeAttributes config = new BaseTradeAttributes();
    config.insertPropertyRow(selectedProperty, data);
    return mapping.findForward("success");
  }
  



  private String getSelectedPropertyId(DynamicMapBackedForm form)
  {
    String tradeid = (String)form.getValue("selectedId");
    return tradeid;
  }
  



  public ActionForward doDeletePropertyRow(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String selectedProperty = getSelectedPropertyId(data);
    BaseTradeAttributes config = new BaseTradeAttributes();
    if (!config.isPropertyBlank(selectedProperty, data))
      setUiDirtyData(data);
    config.deletePropertyRow(selectedProperty, data);
    return mapping.findForward("success");
  }
  


  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  


  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  

  protected TradeFacade getFacade()
  {
    return new TradeFacadeImpl();
  }
  
  private void logError(Exception e)
  {
    KDateTime datetime = KDateTime.createDateTime();
    
    KTimeDuration dur = KServer.stringToDuration("08:00 AM");
    KDateTime dt = (KDateTime)datetime.plus(dur);
    KTime time = KServer.stringToTime("8:00");
    datetime.plus(0, 0, time.getTimeInSeconds(), 0);
    APIValidationException ex = new APIValidationException();
    
    APIPersonIdentityBean identity = APICurrentUserBean.createForPersonNumber("Import");
    APIPayCodeEditBean edit = new APIPayCodeEditBean();
    edit.setEmployee(identity);
    edit.setPayCodeName("SafetyDrill");
    edit.setAmountInTimeOrCurrency("1:00");
    edit.setStartTime(KServer.timeToString(KTime.create()));
    edit.setDate(KServer.stringToDate("23/09/2015"));
    edit.delete(new ParameterMap());
    edit.addOnly(new ParameterMap());
  }
  







































  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
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
  




  private String getPropNameName(String id)
  {
    return id + "_name";
  }
  
  private String getPropValueName(String id)
  {
    return id + "_value";
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
        String propName = (String)prop.get("tradeName");
        String propValue = (String)prop.get("tradeDesc");
        if (((propName != null) && (!propName.equals(""))) || ((propValue != null) && (!propValue.equals("")))) {
          namedPropsList.add(prop);
        }
      }
    }
    Map[] namedProps = new Map[namedPropsList.size()];
    namedProps = (Map[])namedPropsList.toArray(namedProps);
    return namedProps;
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
      property.put("tradeId", id);
      property.put("tradeName", propName);
      property.put("tradeDesc", propVal);
      properties[(i++)] = property;
    }
    
    return properties;
  }
  

  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    try
    {
      Map[] configs = getFacade().getTradeConfigurations();
      test();
      
      setTradeConfigsToUI(data, configs);
      clearUiDirtyData(data);

    }
    catch (APIValidationException ex)
    {
      Exception[] exs = ex.getWrappedExceptions();
      if ((exs != null) && (exs.length > 0)) {
        for (int i = 0; i < exs.length; i++) {
          Log.log(exs[i], "error from script");
        }
        
      }
    }
    catch (Exception e)
    {
      logError(e);
      throw e;
    }
    return mapping.findForward("success");
  }
  
  private void test()
  {
    APIEmpPhotoBean bean = new APIEmpPhotoBean();
    String image = "iVBORw0KGgoAAAANSUhEUgAAARoAAAA3CAYAAADT9oXmAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzYxNDY1N0I2RUYzMTFFNEIyNjFCRjQ5OTZCMEZEN0UiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MzYxNDY1N0M2RUYzMTFFNEIyNjFCRjQ5OTZCMEZEN0UiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDozNjE0NjU3OTZFRjMxMUU0QjI2MUJGNDk5NkIwRkQ3RSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDozNjE0NjU3QTZFRjMxMUU0QjI2MUJGNDk5NkIwRkQ3RSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PoY6YnAAACbGSURBVHja7H0HvB1V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";
    
    bean.setEmployee(APIPersonIdentityBean.createForPersonNumber("101"));
    bean.setImageContent(image);
    
    bean.Update();
  }
  

  private void setTradeConfigsToUI(DynamicMapBackedForm data, Map[] configs)
  {
    data.setValue("tradeList", configs);
    atLeastOneProperty(data);
  }
  

  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    saveTrade(request, form);
    return doRefresh(mapping, form, request, response);
  }
  

  public void roundTripProperties(DynamicMapBackedForm data)
  {
    Map[] properties = getPropertiesFromDmbf(data);
    data.setValue("tradeList", properties);
  }
  
  private void saveTrade(HttpServletRequest request, ActionForm form)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      Map[] propertyMaps = getNamedPropertiesFromDmbf(data);
      getFacade().updateTradeConfiguration(propertyMaps);
    }
    catch (CMSException e) {
      StrutsUtils.addErrorMessage(request, e);
      roundTripProperties(data);
      atLeastOneProperty(data);
      throw e;
    }
    catch (BusinessValidationException ex) {
      setErrorMessagesFromExceptions(request, ex);
      roundTripProperties(data);
      atLeastOneProperty(data);
      throw ex;
    }
    catch (Exception ex) {
      logError(ex);
      throw ex;
    }
  }
  

  protected Map createNewProperty()
  {
    Map<String, String> property = new HashMap();
    property.put("tradeId", "new_property");
    property.put("tradeName", "");
    property.put("tradeDesc", "");
    return property;
  }
  
  public void atLeastOneProperty(DynamicMapBackedForm data)
  {
    Map[] propertyMap = (Map[])data.getValue("tradeList");
    if ((propertyMap == null) || (propertyMap.length < 1))
    {
      propertyMap = new Map[1];
      propertyMap[0] = createNewProperty();
      data.setValue("tradeList", propertyMap);
    }
  }
  






  private static Map methodsMap = methodsMap();
}
