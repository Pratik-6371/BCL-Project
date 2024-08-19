package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.ContractorWCLICTransaction;
import com.kronos.wfc.cms.business.Contractorwclic;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.commonapp.accessgrp.business.exceptions.DAGProcessException;
import com.kronos.wfc.commonapp.people.business.person.CustomData;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.PersistenceException;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.xml.api.bean.APIProcessingException;
import com.kronos.wfc.platform.xml.api.bean.APIValidationException;
import com.kronos.wfc.wfp.logging.Log;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




public class ContractorAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-contractor-application";
  

  public ContractorAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("cms.action.save.and.return", "doSaveAndReturn");
    methods.put("cms.action.return", "doReturn");
    methods.put("cms.action.new", "doNew");
    
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  

  //new method 
  
  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
	  String contractorid = getSelectedId(form);
      
     
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      String unitId = (String)data.getValue("unitId");
      //Contractorwclic contractorwclic = new CMSService().getContrWcLicData(contractorid, unitId);
      Contractorwclic contractorwclic = new Contractorwclic();
      Contractor cont = new Contractor();
      setValuesToUI(data, cont, contractorwclic, request);
    }
    catch (Exception e) {
      logError(e);
      
      if (e instanceof CMSException)
      {
    	  CMSException ex = CMSException.unknown(e.getLocalizedMessage());
    	  StrutsUtils.addErrorMessage(request, ex);
      }
      
       if (e instanceof APIValidationException) {
    	  APIValidationException ap;
    	  ap=(APIValidationException) e;
			StrutsUtils.addErrorMessage(request, ap);
			Exception[] list=ap.getWrappedExceptions();
			if (list != null && list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					if(list[i] instanceof APIValidationException)
					{
					APIValidationException exception = (APIValidationException) list[i];
					StrutsUtils.addErrorMessage(request, exception);
					}
					if(list[i] instanceof APIProcessingException)
					{
						APIProcessingException exception = (APIProcessingException) list[i];
						StrutsUtils.addErrorMessage(request, exception);
					}
					if(list[i] instanceof DAGProcessException)
					{
						DAGProcessException exception=(DAGProcessException) list[i];
						StrutsUtils.addErrorMessage(request, exception);
					}
					
				}

			}

		}
    }
    return mapping.findForward("success");
  }
  
  
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;

    try { validateForm(data);
      saveContractor(form);
      getContractorDataToUI((DynamicMapBackedForm)form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
    }
    catch (CMSException e)
    {
      logError(e);
      StrutsUtils.addErrorMessage(request, e);
      Exception[] list = e.getWrappedExceptions();
      if ((list != null) && (list.length > 0)) {
        for (int i = 0; i < list.length; i++) {
          CMSException exception = (CMSException)list[i];
          StrutsUtils.addErrorMessage(request, exception);
        }
      }
    }
    catch (Exception ex) {
    
      logError(ex);
      PersistenceException pe;
      if(ex instanceof PersistenceException)
      {
    	  pe = (PersistenceException) ex;
    	  StrutsUtils.addErrorMessage(request, pe);
    	   Throwable wrappedThrowable = pe.getWrappedThrowable();
    	   String message = wrappedThrowable.getMessage();
    	   StrutsUtils.addErrorMessage(request, new Message(message));
      }
      else{
      StrutsUtils.addErrorMessage(request, CMSException.unknown(ex.getLocalizedMessage()));
      }
    } finally {
      request.setAttribute("unitId", (String)data.getValue("unitId"));
      String id = (String)data.getValue("id"); 
      Contractor cont = Contractor.doRetrieveById(new ObjectIdLong(id), new ObjectIdLong((String)data.getValue("unitId")));
      data.setValue("contractorname", cont.getcontractorName());
      data.setValue("vendorcode", cont.getVendorCode());
      data.setValue("licensevalidity1", (String)data.getValue("licensevalidity1"));
      data.setValue("licensevalidity2", (String)data.getValue("licensevalidity2"));
      
      data.setValue("licensevalidityFrom", (String)data.getValue("licensevalidityFrom"));
      data.setValue("licensevalidityTo", (String)data.getValue("licensevalidityTo"));
      data.setValue("periodofcontract1", (String)data.getValue("periodofcontract1"));
      data.setValue("periodofcontract2", (String)data.getValue("periodofcontract2"));
      data.setValue("pfApplyDate", (String)data.getValue("pfApplyDate"));
      data.setValue("esistdt", (String)data.getValue("esistdt"));
      data.setValue("esieddt", (String)data.getValue("esieddt"));
      data.setValue("pfcodeapplicationdate", (String)data.getValue("pfcodeapplicationdate"));
    }
    

    return mapping.findForward("success");
  }
  
  private void saveContractor(ActionForm form)
  {
    Contractor cont = getSavedContFromUI(form);
    ContractorFacade facade = getFacade();
    facade.saveContractor(cont);
    Contractorwclic contractorwclic = gettSavedContWcLicFromUI(form);
    ContractorWCLICTransaction transaction = new ContractorWCLICTransaction(cont, contractorwclic);
    transaction.run();
    
  }
 

public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try
    {
      validateForm((DynamicMapBackedForm)form);
      saveContractor(form);
      getContractorDataToUI((DynamicMapBackedForm)form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
      request.setAttribute("unitId", ((DynamicMapBackedForm)form).getValue("unitId"));
    }
    catch (CMSException e) {
      if ((e instanceof CMSException)) {
        StrutsUtils.addErrorMessage(request, e);
      }
      logError(e);
      return mapping.findForward("success");
    }
    catch (Exception ex)
    {
      StrutsUtils.addErrorMessage(request, CMSException.unknown(ex.getLocalizedMessage()));
      logError(ex);
      return mapping.findForward("success");
    }
    return mapping.findForward("doCMSContractorlist");
  }
  

  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSContractorlist");
  }
  
  public ActionForward doEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    
    try
    {
      String contractorid = getSelectedId(form);
      if (contractorid == null)
      {
        setNoSelectionErrorMessage(request);
        return doRefresh(mapping, form, request, response);
      }
      
      ContractorFacade facade = getFacade();
      String unitId = (String)data.getValue("unitId");
      Contractor cont = facade.getContractor(contractorid, unitId);
      Contractorwclic contractorwclic = new CMSService().getContrWcLicData(contractorid, unitId);
      setValuesToUI(data, cont,contractorwclic, request);
      request.setAttribute("unitId", data.getValue("unitId"));

    }
    catch (Exception e)
    {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
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
  
  protected ContractorFacade getFacade()
  {
    return new ContractorFacadeImpl();
  }
  
  private Contractor getSavedContFromUI(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String contractorid = (String)data.getValue("id");
    String unitId = (String)data.getValue("unitId");
    String contractorname = (String)data.getValue("contractorname");
    String caddress = (String)data.getValue("caddress");
    String caddress1 = (String)data.getValue("caddress1");
    String caddress2 = (String)data.getValue("caddress2");
    String caddress3 = (String)data.getValue("caddress3");
    String ccode = (String)data.getValue("ccode");
    String managername = (String)data.getValue("managername");
    String esiwcnumber = (String)data.getValue("esiwcnumber");
    //String licensenumber = (String)data.getValue("licensenumber");
    String licensenumber = (String)data.getValue("licensenumber1") == "" ? null : (String)data.getValue("licensenumber");
    //String licensenumber1 = (String)data.getValue("licensenumber1");
    String licensenumber1 = (String)data.getValue("licensenumber1") == "" ? null : (String)data.getValue("licensenumber1");
    
    KDate licensevalidity1 = (String)data.getValue("licensevalidity1") == "" ? null : KServer.stringToDate((String)data.getValue("licensevalidity1"));
    KDate licensevalidity2 = (String)data.getValue("licensevalidity2") == "" ? null : KServer.stringToDate((String)data.getValue("licensevalidity2"));
    KDate licensevalidityFrom = (String)data.getValue("licensevalidityFrom") == "" ? null : KServer.stringToDate((String)data.getValue("licensevalidityFrom"));
    KDate licensevalidityTo = (String)data.getValue("licensevalidityTo") == "" ? null : KServer.stringToDate((String)data.getValue("licensevalidityTo"));
    String coverage = (String)data.getValue("coverage");
    String totalstrength = (String)data.getValue("totalstrength");
    String maxnoofemployees = (String)data.getValue("maxnoofemployees");
    String natureofwork = (String)data.getValue("natureofwork");
    String locationofcontractwork = (String)data.getValue("locationofcontractwork");
    KDate periodofcontract1 = (String)data.getValue("periodofcontract1") == "" ? null : KServer.stringToDate((String)data.getValue("periodofcontract1"));
    KDate periodofcontract2 = (String)data.getValue("periodofcontract2") == "" ? null : KServer.stringToDate((String)data.getValue("periodofcontract2"));
    String vendorid = (String)data.getValue("vendorcode");
    String pfcode = (String)data.getValue("pfcode");
    KDate esistdt = KServer.stringToDate((String)data.getValue("esistdt"));
    KDate esieddt = KServer.stringToDate((String)data.getValue("esieddt"));
    String esicoverage = (String)data.getValue("esicoverage");
    String pfnumber = (String)data.getValue("pfnumber");
    KDate pfcodeapplicationdate = KServer.stringToDate((String)data.getValue("pfcodeapplicationdate"));
    String commission = (String)data.getValue("commission");
    String pfCoverage = (String) data.getValue("pfCoverage");
    Boolean isPfSelf = Boolean.valueOf("on".equalsIgnoreCase((String) data.getValue("isPfSelf"))) ;
    Boolean isEsiSelf = Boolean.valueOf("on".equalsIgnoreCase((String) data.getValue("isEsiSelf"))) ;
    
    return new Contractor(new ObjectIdLong(Long.valueOf(contractorid)), new ObjectIdLong(Long.valueOf(unitId)),
    		contractorname, caddress, caddress1, caddress2, caddress3, ccode, managername, esiwcnumber, 
      licensenumber, licensevalidity1, licensevalidity2, coverage, totalstrength, maxnoofemployees, natureofwork, 
      locationofcontractwork, periodofcontract1, periodofcontract2, vendorid, pfcode, esistdt, esieddt, esicoverage,
      pfnumber, pfcodeapplicationdate, new Boolean(false), commission, pfCoverage,isPfSelf, isEsiSelf,licensenumber1,licensevalidityFrom,licensevalidityTo);
  }
  
  
  private Contractorwclic gettSavedContWcLicFromUI(ActionForm form) {
	
	  DynamicMapBackedForm data = (DynamicMapBackedForm)form;
	  String wc1 = (String) data.getValue("wc1");
	  String wc1Coverage = (String) data.getValue("wc1Coverage");
	  KDate wc1FromDate = null;
	  String stringWc1FromDate = (String)data.getValue("wc1FromDate");
	  if(stringWc1FromDate != null && !"".equalsIgnoreCase(stringWc1FromDate))
	  {
		  wc1FromDate = KServer.stringToDate((String)data.getValue("wc1FromDate"));
	  }
	  KDate wc1ToDate = null;
	  String stringWc1ToDate = (String)data.getValue("wc1ToDate");
	  if(stringWc1ToDate != null && !"".equalsIgnoreCase(stringWc1ToDate))
	  {
		  wc1ToDate = KServer.stringToDate((String)data.getValue("wc1ToDate"));
	  }

	  String wc2 = (String) data.getValue("wc2");
	  String wc2Coverage = (String) data.getValue("wc2Coverage");
	  KDate wc2FromDate = null;
	  String stringWc2FromDate = (String)data.getValue("wc2FromDate");
	  if(stringWc2FromDate != null && !"".equalsIgnoreCase(stringWc2FromDate))
	  {
		  wc2FromDate = KServer.stringToDate((String)data.getValue("wc2FromDate"));
	  }
	  KDate wc2ToDate = null;
	  String stringWc2ToDate = (String)data.getValue("wc2ToDate");
	  if(stringWc2ToDate != null && !"".equalsIgnoreCase(stringWc2ToDate))
	  {
		  wc2ToDate = KServer.stringToDate((String)data.getValue("wc2ToDate"));
	  }	 
	  
	  /*WC3*/
	  
	  String wc3 = (String) data.getValue("wc3");
	  String wc3Coverage = (String) data.getValue("wc3Coverage");
	  KDate wc3FromDate = null;
	  String stringwc3FromDate = (String)data.getValue("wc3FromDate");
	  if(stringwc3FromDate != null && !"".equalsIgnoreCase(stringwc3FromDate))
	  {
		  wc3FromDate = KServer.stringToDate((String)data.getValue("wc3FromDate"));
	  }
	  KDate wc3ToDate = null;
	  String stringwc3ToDate = (String)data.getValue("wc3ToDate");
	  if(stringwc3ToDate != null && !"".equalsIgnoreCase(stringwc3ToDate))
	  {
		  wc3ToDate = KServer.stringToDate((String)data.getValue("wc3ToDate"));
	  }
	  /*WC3 END*/
	  
	  /*wc4*/
	  String wc4 = (String) data.getValue("wc4");
	  String wc4Coverage = (String) data.getValue("wc4Coverage");
	  KDate wc4FromDate = null;
	  String stringwc4FromDate = (String)data.getValue("wc4FromDate");
	  if(stringwc4FromDate != null && !"".equalsIgnoreCase(stringwc4FromDate))
	  {
		  wc4FromDate = KServer.stringToDate((String)data.getValue("wc4FromDate"));
	  }
	  KDate wc4ToDate = null;
	  String stringwc4ToDate = (String)data.getValue("wc4ToDate");
	  if(stringwc4ToDate != null && !"".equalsIgnoreCase(stringwc4ToDate))
	  {
		  wc4ToDate = KServer.stringToDate((String)data.getValue("wc4ToDate"));
	  }
	  
	  /*wc4 end*/
	  
	  Contractorwclic contractorwclic = new Contractorwclic(wc1, wc2, wc3, wc4, wc1Coverage, wc2Coverage,
			  wc3Coverage, wc4Coverage, wc1FromDate, wc1ToDate, wc2FromDate, wc2ToDate, wc3FromDate, wc3ToDate,
			  wc4FromDate, wc4ToDate);
	return contractorwclic;
}
  
  void setNoSelectionErrorMessage(HttpServletRequest request)
  {
    CMSException e = new CMSException(1);
    StrutsUtils.addErrorMessage(request, e);
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
    
    try
    {
      getContractorDataToUI(data, request);
      request.setAttribute("unitId", data.getValue("unitId"));

    }
    catch (Exception e)
    {
      logError(e);
      
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
      data.setValue("licensevalidity1", "");
      data.setValue("licensevalidity2", "");
      data.setValue("periodofcontract1", "");
      data.setValue("periodofcontract2", "");
      data.setValue("pfApplyDate", "");
      data.setValue("esistdt", "");
      data.setValue("esieddt", "");
      data.setValue("pfcodeapplicationdate", "");
    }
    
    return mapping.findForward("success");
  }
  
  private void getContractorDataToUI(DynamicMapBackedForm data, HttpServletRequest request) {
    String contractorid = (String)data.getValue("id");
    if (contractorid == null) {
      contractorid = getSelectedId(data);
    }
    ContractorFacade facade = getFacade();
    String unitId = (String)data.getValue("unitId");
    Contractor cont = facade.getContractor(contractorid, unitId);
    Contractorwclic contractorwclic = new CMSService().getContrWcLicData(contractorid, unitId);
    setValuesToUI(data, cont, contractorwclic, request);
  }
  
  private void setValuesToUI(DynamicMapBackedForm data, Contractor cont, Contractorwclic contractorwclic,HttpServletRequest request) {
	  Boolean displayStatus = false;
	  String unitId = (String)data.getValue("unitId");
	    if ((unitId != null) && (cont.getcontractorid() == null)){
	    	request.setAttribute("displayStatus", displayStatus.toString());
	    	data.setValue("id", "");
	        data.setValue("unitId", "");
	        data.setValue("contractorname","");
	        data.setValue("caddress", "");
	        data.setValue("caddress1", "");
	        data.setValue("caddress2", "");
	        data.setValue("caddress3","");
	        data.setValue("ccode","");
	        data.setValue("managername","");
	        data.setValue("esiwcnumber", "");
	        data.setValue("licensenumber", "");
	        data.setValue("licensenumber1", "");
	        //new exempt
	        
	        data.setValue("licExempt", (String)data.getValue("licExempt"));
	        String licEx = "on".equalsIgnoreCase((String)data.getValue("licExempt")) ? "none" : "";
	        request.setAttribute("licEx", licEx);
	        
	        data.setValue("licExempt1", (String)data.getValue("licExempt1"));
	        String licEx1 = "on".equalsIgnoreCase((String)data.getValue("licExempt1")) ? "none" : "";
	        request.setAttribute("licEx1", licEx1);
	        
	        data.setValue("licensevalidity1","");
	        data.setValue("licensevalidity2", "");
	        data.setValue("licensevalidityFrom", "");
	        data.setValue("licensevalidityTo", "");
	        
	        
	        
	        data.setValue("coverage", "");
	        data.setValue("totalstrength", "");
	        data.setValue("maxnoofemployees","");
	        data.setValue("natureofwork", "");
	        data.setValue("locationofcontractwork", "");
	        data.setValue("periodofcontract1", "");
	        data.setValue("periodofcontract2","");
	        data.setValue("vendorcode", "");
	        data.setValue("esistdt","");
	        data.setValue("esieddt", "");
	        data.setValue("esicoverage", "");
	        data.setValue("pfcode", "");
	        data.setValue("pfnumber", "");
	        data.setValue("pfcodeapplicationdate", "");
	        data.setValue("commission", "");
	        data.setValue("pfCoverage", "");
	        /*data.setValue("isPfSelf", Boolean.valueOf(cont.getIsPfSelf()) ? "on" : "off");
	        data.setValue("isEsiSelf", Boolean.valueOf(cont.getIsEsiSelf()) ? "on" : "off");*/
	        data.setValue("wc1", "");
	        data.setValue("wc1FromDate", "");
	        data.setValue("wc1ToDate", "");
	        data.setValue("wc1Coverage", "");
	        data.setValue("wc2", "");
	        data.setValue("wc2FromDate", "");
	        data.setValue("wc2ToDate", "");
	        data.setValue("wc2Coverage","");
	        /*wc3*/
	        data.setValue("wc3","");
	        data.setValue("wc3FromDate", "");
	        data.setValue("wc3ToDate", "");
	        data.setValue("wc3Coverage", "");
	    	  
	        /*wc4*/
	        data.setValue("wc4","");
	        data.setValue("wc4FromDate", "");
	        data.setValue("wc4ToDate", "");
	        data.setValue("wc4Coverage","");
	    	 
	    	
	    }else if ((unitId != null) && (cont.getcontractorid() != null)){
	    	displayStatus=true;
	    	request.setAttribute("displayStatus", displayStatus.toString());
	    	data.setValue("id", cont.getcontractorid().toString());
	        data.setValue("unitId", cont.getUnitId().toString());
	        data.setValue("contractorname", cont.getcontractorName());
	        data.setValue("caddress", cont.getCaddress());
	        data.setValue("caddress1", cont.getCaddress1());
	        data.setValue("caddress2", cont.getCaddress2());
	        data.setValue("caddress3", cont.getCaddress3());
	        data.setValue("ccode", cont.getCcode());
	        data.setValue("managername", cont.getManagername());
	        data.setValue("esiwcnumber", cont.getEsiwcnumber());
	        data.setValue("licensenumber", cont.getLicensenumber());
	        data.setValue("licensenumber1", cont.getLicensenumber1());
	        //new exempt
	        
	        data.setValue("licExempt", (String)data.getValue("licExempt"));
	        String licEx = "on".equalsIgnoreCase((String)data.getValue("licExempt")) ? "none" : "";
	        request.setAttribute("licEx", licEx);
	        
	        data.setValue("licExempt1", (String)data.getValue("licExempt1"));
	        String licEx1 = "on".equalsIgnoreCase((String)data.getValue("licExempt1")) ? "none" : "";
	        request.setAttribute("licEx1", licEx1);
	        
	        data.setValue("licensevalidity1", cont.getLicensevalidity1().isNull() ? "" : KServer.dateToString(cont.getLicensevalidity1()));
	        data.setValue("licensevalidity2", cont.getLicensevalidity2().isNull() ? "" : KServer.dateToString(cont.getLicensevalidity2()));
	        data.setValue("licensevalidityFrom", cont.getLicensevalidityFrom().isNull() ? "" : KServer.dateToString(cont.getLicensevalidityFrom()));
	        data.setValue("licensevalidityTo", cont.getLicensevalidityTo().isNull() ? "" : KServer.dateToString(cont.getLicensevalidityTo()));
	        
	        
	        
	        data.setValue("coverage", cont.getCoverage());
	        data.setValue("totalstrength", cont.getTotalstrength());
	        data.setValue("maxnoofemployees", cont.getMaxnoofemployees());
	        data.setValue("natureofwork", cont.getNatureofwork());
	        data.setValue("locationofcontractwork", cont.getLocationofcontractwork());
	        data.setValue("periodofcontract1", cont.getPeriodofcontract1().isNull() ? "" : KServer.dateToString(cont.getPeriodofcontract1()));
	        data.setValue("periodofcontract2", cont.getPeriodofcontract2().isNull() ? "" : KServer.dateToString(cont.getPeriodofcontract2()));
	        data.setValue("vendorcode", cont.getVendorCode());
	        data.setValue("esistdt", cont.getEsistdt().isNull() ? "" : KServer.dateToString(cont.getEsistdt()));
	        data.setValue("esieddt", cont.getEsieddt().isNull() ? "" : KServer.dateToString(cont.getEsieddt()));
	        data.setValue("esicoverage", cont.getESIwcCoverage());
	        data.setValue("pfcode", cont.getPfcode());
	        data.setValue("pfnumber", cont.getPfnumber());
	        data.setValue("pfcodeapplicationdate", cont.getPfcodeapplicationdate().isNull() ? "" : KServer.dateToString(cont.getPfcodeapplicationdate()));
	        data.setValue("commission", cont.getCommission());
	        data.setValue("pfCoverage", cont.getPfCoverage());
	        data.setValue("isPfSelf", Boolean.valueOf(cont.getIsPfSelf()) ? "on" : "off");
	        data.setValue("isEsiSelf", Boolean.valueOf(cont.getIsEsiSelf()) ? "on" : "off");
	        data.setValue("wc1", contractorwclic.getWc1());
	        data.setValue("wc1FromDate", contractorwclic.getWc1FromDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc1FromDate()));
	        data.setValue("wc1ToDate", contractorwclic.getWc1ToDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc1ToDate()));
	        data.setValue("wc1Coverage", contractorwclic.getWc1Coverage());
	        data.setValue("wc2", contractorwclic.getWc2());
	        data.setValue("wc2FromDate", contractorwclic.getWc2FromDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc2FromDate()));
	        data.setValue("wc2ToDate", contractorwclic.getWc2ToDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc2ToDate()));
	        data.setValue("wc2Coverage", contractorwclic.getWc2Coverage());
	        /*wc3*/
	        data.setValue("wc3", contractorwclic.getWc3());
	        data.setValue("wc3FromDate", contractorwclic.getWc3FromDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc3FromDate()));
	        data.setValue("wc3ToDate", contractorwclic.getWc3ToDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc3ToDate()));
	        data.setValue("wc3Coverage", contractorwclic.getWc3Coverage());
	    	  
	        /*wc4*/
	        data.setValue("wc4", contractorwclic.getWc4());
	        data.setValue("wc4FromDate", contractorwclic.getWc4FromDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc4FromDate()));
	        data.setValue("wc4ToDate", contractorwclic.getWc4ToDate().isNull() ? "" : KServer.dateToString(contractorwclic.getWc4ToDate()));
	        data.setValue("wc4Coverage", contractorwclic.getWc4Coverage());
	    	 
	    }
	
    
  }
  
  private void logError(Exception e)
  {
    Log.log(1, e, "Error in contractor");
  }
  
  private boolean isNumeric(String value)
  {
    String regex = "\\d+[\\.\\d+]*";
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(value).matches();
  }
  
  private void validateForm(DynamicMapBackedForm data) throws Exception {
    String contractorname = (String)data.getValue("contractorname");
    String caddress = (String)data.getValue("caddress");
    String caddress1 = (String)data.getValue("caddress1");
    String caddress2 = (String)data.getValue("caddress2");
    String caddress3 = (String)data.getValue("caddress3");
    String managername = (String)data.getValue("managername");
    String esiwcnumber = (String)data.getValue("esiwcnumber");
    
  //new exempt
    String isLic = "on".equalsIgnoreCase((String)data.getValue("licExempt")) ? "Yes" : "No";
    String licensenumber = (String)data.getValue("licensenumber");
    
    String isLic1 = "on".equalsIgnoreCase((String)data.getValue("licExempt1")) ? "Yes" : "No";
    String licensenumber1 = (String)data.getValue("licensenumber1");
    
   /* String isLicsdt1 = "on".equalsIgnoreCase((String)data.getValue("licsdtExempt1")) ? "Yes" : "No";
    String isLicedt1 = "on".equalsIgnoreCase((String)data.getValue("licedtExempt1")) ? "Yes" : "No";*/
    String licensevalidity1 = (String)data.getValue("licensevalidity1");
    String licensevalidity2 = (String)data.getValue("licensevalidity2");
    
    /*String isLicsdt2 = "on".equalsIgnoreCase((String)data.getValue("licsdtExempt2")) ? "Yes" : "No";
    String isLicedt2 = "on".equalsIgnoreCase((String)data.getValue("licedtExempt2")) ? "Yes" : "No";*/
    
    String licensevalidityFrom = (String)data.getValue("licensevalidityFrom");
    String licensevalidityTo = (String)data.getValue("licensevalidityTo");
    String coverage = (String)data.getValue("coverage");
    String totalstrength = (String)data.getValue("totalstrength");
    String maxnoofemployees = (String)data.getValue("maxnoofemployees");
    String natureofwork = (String)data.getValue("natureofwork");
    String locationofcontractwork = (String)data.getValue("locationofcontractwork");
    String periodofcontract1 = (String)data.getValue("periodofcontract1");
    String periodofcontract2 = (String)data.getValue("periodofcontract2");
    String vendorid = (String)data.getValue("vendorid");
    String pfcode = (String)data.getValue("pfcode");
    String esiwvalidityperiod = (String)data.getValue("esiwvalidityperiod");
    String pfnumber = (String)data.getValue("pfnumber");
    String pfcodeapplicationdate = (String)data.getValue("pfcodeapplicationdate");
    List eList = new ArrayList();
    
    if ("".equalsIgnoreCase(contractorname)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.contractorname", " Contractor Name")));
    }
    if ("".equalsIgnoreCase(caddress)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.village", "Village")));
    }
    if ("".equalsIgnoreCase(caddress1)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.taluk", "Taluka")));
    }
    
    if ("".equalsIgnoreCase(caddress2)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.district", "District")));
    }
    
    if ("".equalsIgnoreCase(caddress3)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.state", "State")));
    }
    if ("".equalsIgnoreCase(managername)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.managername", "Manager Name")));
    }
    

    if ("".equals(esiwcnumber))
    {
      eList.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.esiwcnumber", "ESI/WC Number")));
    }
    
    if ("".equalsIgnoreCase(esiwcnumber)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.esiwcnumber", "ESI/WC Number")));
    }
    


    if ("".equalsIgnoreCase(coverage)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.coverage", "Coverage")));
    }
    
    if ("".equalsIgnoreCase(totalstrength)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.totalstrength", "Total Strength")));
    }
    

    if ("".equals(totalstrength))
    {
      eList.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.totalstrength", "TotalStrength")));
    }
    
    if ("".equalsIgnoreCase(maxnoofemployees)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.maxnoofemployees", "Max No of Employees")));
    }
    else if (!isNumeric(maxnoofemployees)) {
      eList.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.maxnoofemployees", "MaxNoOfEmployees")));
    }
    
    if (isNumeric(maxnoofemployees))
    {
      if (Integer.valueOf(maxnoofemployees).intValue() > 20)
      {
        if (("No".equalsIgnoreCase(isLic)) && ((licensenumber == null) || ("".equalsIgnoreCase(licensenumber)))) {
          eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensenumber", "License Number")));
        }
        if ( "".equalsIgnoreCase(licensevalidity1)) {
          eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensevalidity1", "License Validity")));
        }
        if ("".equalsIgnoreCase(licensevalidity2)){
          eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensevalidity1", "License Validity")));
        }
        if (("No".equalsIgnoreCase(isLic1)) && ((licensenumber1 == null) || ("".equalsIgnoreCase(licensenumber1)))) {
        	eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensenumber1", "License Number 2")));
          }
        
        if ("".equalsIgnoreCase(licensevalidityFrom)) {
            eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensevalidityFrom", "License ValidityFrom")));
          }
          if ("".equalsIgnoreCase(licensevalidityTo)){
            eList.add(CMSException.missingRequiredField(KronosProperties.get("label.licensevalidityTo", "License ValidityTo")));
          }
       
      }
    }
    
    

    if ("".equalsIgnoreCase(natureofwork)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.natureofwork", "Nature of Work")));
    }
    if ("".equalsIgnoreCase(locationofcontractwork)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.locationofcontractwork", "LocationofContractwork")));
    }
    
    if ("".equalsIgnoreCase(periodofcontract1)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.periodofcontract1", "periodofcontract")));
    }
    if ("".equalsIgnoreCase(periodofcontract2)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.periodofcontract2", "periodofcontract2")));
    }
    if ("".equalsIgnoreCase(vendorid)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.vendorid", "Vendorid")));
    }
    if ("".equalsIgnoreCase(pfcode)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.pfcode", "PF Code")));
    }
    



    if ("".equalsIgnoreCase(esiwvalidityperiod)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.esiwvalidityperiod", "ESIWValidityPeriod")));
    }
    if ("".equalsIgnoreCase(pfnumber)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.pfnumber", "PF Number")));
    }
    if ("".equals(pfnumber))
    {
      eList.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.pfnumber", "PFNunber")));
    }
    
    if ("".equalsIgnoreCase(pfcodeapplicationdate)) {
      eList.add(CMSException.missingRequiredField(KronosProperties.get("label.pfcodeapplicationdate", "pfcodeapplicationdate")));
    }
    
    if ((!"".equalsIgnoreCase(contractorname)) && (!"".equalsIgnoreCase(caddress)) && (!"".equalsIgnoreCase(caddress1)) && (!"".equalsIgnoreCase(caddress2)) && 
      (!"".equalsIgnoreCase(caddress3)) && (!"".equalsIgnoreCase(managername)) && (!"".equalsIgnoreCase(esiwcnumber)) && (!"".equalsIgnoreCase(licensenumber)) && 
      (!"".equalsIgnoreCase(coverage)) && (!"".equalsIgnoreCase(totalstrength)) && (!"".equalsIgnoreCase(maxnoofemployees)) && (!"".equalsIgnoreCase(natureofwork)) && (!"".equalsIgnoreCase(locationofcontractwork)) && 
      (!"".equalsIgnoreCase(periodofcontract1)) && (!"".equalsIgnoreCase(periodofcontract1)) && 
      (!"".equalsIgnoreCase(vendorid)) && (!"".equalsIgnoreCase(pfcode)) && (!"".equalsIgnoreCase(esiwvalidityperiod)) && 
      (!"".equalsIgnoreCase(pfnumber))) { "".equalsIgnoreCase(pfcodeapplicationdate);
    }
    
    if (eList.size() > 0) {
      CMSException ex = CMSException.validationError();
      ex.addWrappedExceptions(eList);
      throw ex;
    }
  }
  





  private static Map methodsMap = methodsMap();
}
