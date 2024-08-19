package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;









public class ContractorWListAction
  extends WFPLookupDispatchActions
{
  private static Map methods = methodMap();
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS");
  
  public ContractorWListAction() {}
  
  protected Map getKeyMethodMap() {
    return methods;
  }
  
  protected static Map methodMap() {
    Map methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.edit", "doEdit");
    return methods;
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    //List<PrincipalEmployee> pes;
    try
    {
      String unitId = (String)data.getValue("unitId");
      String cCode = (String)data.getValue("ccode");
      String name = (String)data.getValue("contractorName");
      
      Map[] contList = { new HashMap() };
      if (unitId != null) {
        if ((cCode != null) && (!"".equalsIgnoreCase(cCode))) {
          contList = getContractorByVendorCode(cCode, unitId);
        }
        else if ((name != null) && (!"".equalsIgnoreCase(name))) {
          contList = getContractorByVendorName(name, unitId);
        }
        else {
          contList = getContListByUnitId(unitId);
        }
        
        data.setValue("cmsContractorList", contList);
        request.setAttribute("uId", unitId);
      }
      

    }
    catch (Exception e)
    {
      log(1, e.getLocalizedMessage());
    } finally {
      //List<PrincipalEmployee> pes;
      List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
      data.setValue("availUnitNames", pes);
    }
    
    request.setAttribute("uId", (String)data.getValue("unitId"));
    return mapping.findForward("success");
  }
  

  private Map[] getContractorByVendorName(String name, String unitId)
  {
    ContractorFacade facade = getContractorFacade();
    
    Map[] wsList = facade.getContractorsByVendorName(name, unitId);
    return wsList;
  }
  
  private Map[] getContractorByVendorCode(String cCode, String unitId)
  {
    ContractorFacade facade = getContractorFacade();
    
    Map[] wsList = facade.getContractorsByVendorCode(cCode, unitId);
    return wsList;
  }
  
  private Map[] getContListByUnitId(String unitId)
  {
    ContractorFacade facade = getContractorFacade();
    
    Map[] wsList = facade.getContractors(unitId);
    return wsList;
  }
  


  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String id = getSelectedId(form);
    
    if (id == null)
    {
      setNoSelectionErrorMessage(request);
      
      return doRefresh(mapping, form, request, response);
    }
    
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    

    request.setAttribute("EditFromListPage", "true");
    
    return mapping.findForward("doCMSWorkmenListApplication");
  }
  




  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    
    StrutsUtils.addErrorMessage(request, e);
  }
  


  private String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    String[] ids = (String[])data.getValue("selectedIds");
    
    String id = null;
    
    if ((ids != null) && (ids[0] != null))
    {
      id = ids[0];
    }
    return id;
  }
  


  private ContractorFacade getContractorFacade()
  {
    return new ContractorFacadeImpl();
  }
}
