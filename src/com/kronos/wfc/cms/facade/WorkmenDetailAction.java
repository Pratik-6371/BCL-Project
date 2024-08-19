package com.kronos.wfc.cms.facade;
import com.kronos.wfc.cms.business.Address;
import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.CMSState;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.DepartmentComparator;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Supply;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.TransferLev6;
import com.kronos.wfc.cms.business.Wage;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.cms.business.WorkmenDetail;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.commonapp.accessgrp.business.exceptions.DAGProcessException;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.BaseWageRate;
import com.kronos.wfc.commonapp.people.business.person.BaseWageRateSet;
import com.kronos.wfc.commonapp.people.business.person.CustomData;
import com.kronos.wfc.commonapp.people.business.person.CustomDataSet;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.types.business.DeviceGroup;
import com.kronos.wfc.datacollection.empphoto.business.EmpPhotoBusinessValidationException;
import com.kronos.wfc.datacollection.empphoto.business.EmpPhotoImage;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.datetime.framework.KServerException;
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
import com.kronos.wfc.timekeeping.payrules.business.configuration.fixedrules.PayPeriod;
import com.kronos.wfc.wfp.logging.Log;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class WorkmenDetailAction
  extends WFPLookupDispatchActions
{
  public WorkmenDetailAction() {}
  
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  protected static Map methodsMap() {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.workmen.detail.refresh", "doRefresh");
    methods.put("cms.action.workmen.detail.save", "doSave");
    methods.put("cms.action.workmen.detail.new", "doNew");
    methods.put("cms.action.workmen.detail.return", "doReturn");
    methods.put("cms.action.refresh.data", "doRefreshData");
    methods.put("cms.action.workmen.detail.safety.training", "doSafetyTraining");
    methods.put("cms.action.workmen.detail.safety.violation", "doSafetyViolation");
    methods.put("cms.action.workmen.detail.health.information", "doHealthInformation");
    return methods;
  }
  
  protected Map getKeyMethodMap() {
    return methodsMap();
  }
  
  public ActionForward doRefreshData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    setRefreshDataToUI(data, request);
    return mapping.findForward("success");
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    ImportActionForm importActionForm = (ImportActionForm)data;
    try {
      Workmen workmen = null;
      String unitId = (String)data.getValue("unitId");
      saveWorkmen(form, request);
      String id = (String)data.getValue("id");
      if (!"id_new".equalsIgnoreCase(id)) {
        workmen = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      }
      setValuesToUI((DynamicMapBackedForm)form, workmen, request);
    }
    catch (Exception e) {
      logError(e);
      
      if ((e instanceof CMSException)) {
        CMSException ex = (CMSException)e;
        StrutsUtils.addErrorMessage(request, ex);
        Exception[] list = ex.getWrappedExceptions();
        if ((list != null) && (list.length > 0)) {
          for (int i = 0; i < list.length; i++) {
            CMSException exception = (CMSException)list[i];
            StrutsUtils.addErrorMessage(request, exception);
          }
        }
      }
      
      else if(e instanceof PersistenceException){
    	  
    	  PersistenceException pex;
    	  pex=(PersistenceException) e;
    	  StrutsUtils.addErrorMessage(request, pex);
    	  Throwable[] list = pex.getSuppressed();
    	  if ((list != null) && (list.length > 0)) {
              for (int i = 0; i < list.length; i++) {
            	  PersistenceException exception = (PersistenceException)list[i];
                StrutsUtils.addErrorMessage(request, exception);
              }
            }
      }
      
      else if (e instanceof APIValidationException) {
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
      
      else {
        CMSException ex = CMSException.unknown(e.getLocalizedMessage());
        StrutsUtils.addErrorMessage(request, ex);
      }
      setRefreshDataToUI(data, request);
    }
    return mapping.findForward("success");
  }
  
  private void setRefreshDataToUI(DynamicMapBackedForm data, HttpServletRequest request) throws Exception {
	    String Id = (String)data.getValue("id");
	    String unitId = (String)data.getValue("sunitId");
	    data.setValue("id", Id);
	    data.setValue("unitId", unitId);
	    request.setAttribute("unitId", new ObjectIdLong(unitId));
	    List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
	    data.setValue("availUnitNames", pes);
	    String ccode = (String)data.getValue("ccode");
	    data.setValue("contrId", ccode);
	    List<Department> depList = Department.doRetrieveByUnitId(new ObjectIdLong(unitId));		
	    List<Section> secList = Section.doRetrieveByUnitId(new ObjectIdLong(unitId));
	    List<Section> secListFiltered = new ArrayList();
	    List<Contractor> contrList = Contractor.doRetrieveByUnitId(new ObjectIdLong(unitId));
	    List<Workorder> workorderList = Workorder.getWorkorderByUnitAndContId(new ObjectIdLong(unitId),new ObjectIdLong(ccode));
	    List<Workorder> workorderListFiltered = new ArrayList();
	    List<WorkorderLine> workorderLineList = WorkorderLine.getWorkorderLineByUnitAndContId(new ObjectIdLong(unitId),new ObjectIdLong(ccode));
	    List<WorkorderLine> workorderLineListFiltered = new ArrayList(); 
	    
	    data.setValue("eCode", (String)data.getValue("eCode") == null ? "" : (String)data.getValue("eCode"));
	    data.setValue("firstname", (String)data.getValue("firstname"));
	    data.setValue("lastname", (String)data.getValue("lastname"));
	    data.setValue("gender", (String)data.getValue("gender"));
	    data.setValue("prevExp", (String)data.getValue("prevExp"));
	    data.setValue("prevOrg", (String)data.getValue("prevOrg"));
	    data.setValue("dob", (String)data.getValue("dob") == null ? "" : (String)data.getValue("dob"));
	    data.setValue("relationName", (String)data.getValue("relationName"));
	    
	    ObjectIdLong dId = new ObjectIdLong(-1L);
	    ObjectIdLong secId = new ObjectIdLong(-1L);
	    ObjectIdLong contrId = new ObjectIdLong(-1L);
	    ObjectIdLong workorderId = new ObjectIdLong(-6L);
	    ObjectIdLong workorderLineId = new ObjectIdLong(-1L);
	    
	    for (Iterator iterator = depList.iterator(); iterator.hasNext();) {
	      Department department = (Department)iterator.next();
	      if (((String)data.getValue("depId")).equalsIgnoreCase(department.getCode())) {
	        dId = department.getDepid();
	        for (Iterator iterator1 = secList.iterator(); iterator1.hasNext();) {
	          Section section = (Section)iterator1.next();
	          if (section.getDeptId().equals(dId)) {
	            secListFiltered.add(section);
	          }
	        }
	        for (Iterator iterator1 = workorderList.iterator(); iterator1.hasNext();) {
	            Workorder workorder = (Workorder)iterator1.next();
	            if (workorder.getDepId().equals(dId)) {
	              workorderListFiltered.add(workorder);
	            }
	          }
	        break;
	      }
	    }
	    
	    for (Iterator iterator = secListFiltered.iterator(); iterator.hasNext();) {
	      Section section = (Section)iterator.next();
	      if (((String)data.getValue("secId")).equalsIgnoreCase(section.getSectionId().toString().trim())) {
	        secId = section.getSectionId();
	        break;
	      }
	    }
	    
	    for (Iterator iterator = workorderListFiltered.iterator(); iterator.hasNext();) {
	        Workorder workorder = (Workorder)iterator.next();
	        if (((String)data.getValue("workorderNum")).equalsIgnoreCase(workorder.getWkNum().trim())) {
	          workorderId = workorder.getWorkorderId();
	          for (Iterator iterator1 = workorderLineList.iterator(); iterator1.hasNext();) {
	              WorkorderLine workorderLine = (WorkorderLine)iterator1.next();
	              if (workorderLine.getWorkorderId().equals(workorderId)) {
	                workorderLineListFiltered.add(workorderLine);
	              }
	            }
	          break;
	        }
	      }
	    
	    
	    /*
	     * Null check is done for itemNum and serviceLinItem because these two fields(dropdown) will
	     * be disabled if user selects "-" in workorder dropdown and hence it will return null
	     */
	    
	    for (Iterator iterator = workorderLineListFiltered.iterator(); iterator.hasNext();) {
	        WorkorderLine workorderLine = (WorkorderLine)iterator.next();
	        if (((String)data.getValue("item_serviceItem_number"))!=null && ((String)data.getValue("item_serviceItem_number")).equalsIgnoreCase(workorderLine.getItem_serviceItem_number().trim())) {
	          workorderLineId = workorderLine.getWkLineId();
	          break;
	        }
	      }
	    
	    request.setAttribute("depId", dId);
	    request.setAttribute("sId", secId);
	    request.setAttribute("contrid", new ObjectIdLong(ccode));
	    data.setValue("availDepartmentNames", depList);
	    data.setValue("availSectionNames", secListFiltered);
	    data.setValue("availContrCodes", contrList);
	    data.setValue("pfaccno", (String)data.getValue("pfaccno"));
	    data.setValue("esic", (String)data.getValue("esic"));
	    data.setValue("esicExempt", (String)data.getValue("esicExempt"));
	    String esiEx = "on".equalsIgnoreCase((String)data.getValue("esicExempt")) ? "none" : "";
	    request.setAttribute("esiEx", esiEx);
	    data.setValue("pfExempt", (String)data.getValue("pfExempt"));
	    String pfEx = "on".equalsIgnoreCase((String)data.getValue("pfExempt")) ? "none" : "";
	    request.setAttribute("pfEx", pfEx);
	    data.setValue("proflic4", (String)data.getValue("proflic4"));
	    data.setValue("empTypeValue", (String)data.getValue("empType"));
	    data.setValue("supplyType", (String)data.getValue("supplyType")); 
	    data.setValue("availTradeNames", Trade.doRetrieveAll());
	    request.setAttribute("tId", new ObjectIdLong((String)data.getValue("tradeId")));
	    data.setValue("availSkillNames", Skill.retrieveSkills());
	    request.setAttribute("skId", new ObjectIdLong((String)data.getValue("skillId")));
	    data.setValue("doj", (String)data.getValue("doj"));
	    data.setValue("dot", (String)data.getValue("dot"));
	    data.setValue("basic", (String)data.getValue("basic"));
	    data.setValue("da", (String)data.getValue("da"));
	    data.setValue("allowance", (String)data.getValue("allowance"));
	    data.setValue("vda", (String)data.getValue("vda"));
	    data.setValue("pda", (String)data.getValue("pda"));
	    data.setValue("hra", (String)data.getValue("hra"));
	    data.setValue("conveyance", (String)data.getValue("conveyance"));
	    data.setValue("specialAllowance", (String)data.getValue("specialAllowance"));
	    data.setValue("shiftAllowance", (String)data.getValue("shiftAllowance"));
	    data.setValue("dustAllowance", (String)data.getValue("dustAllowance"));
	    data.setValue("medicalAllowance", (String)data.getValue("medicalAllowance"));
	    data.setValue("lta", (String)data.getValue("lta"));
	    data.setValue("educationAnnual", (String)data.getValue("educationAnnual"));
	    
	    data.setValue("pVillage", (String)data.getValue("pVillage"));
	    data.setValue("pTaluka", (String)data.getValue("pTaluka"));
	    data.setValue("pDistrict", (String)data.getValue("pDistrict"));
	    request.setAttribute("pStateId", new ObjectIdLong((String)data.getValue("pStateId")));
	    data.setValue("availPStateNames", new CMSService().getStates());
	    
	    data.setValue("permVillage", (String)data.getValue("permVillage"));
	    data.setValue("permTaluka", (String)data.getValue("permTaluka"));
	    data.setValue("permDistrict", (String)data.getValue("permDistrict"));
	    request.setAttribute("permStateId", new ObjectIdLong((String)data.getValue("permStateId")));
	    data.setValue("availPermStateNames", new CMSService().getStates());
	    
	    data.setValue("panno", (String)data.getValue("panno"));
	    data.setValue("technical", (String)data.getValue("technical"));
	    data.setValue("academic", (String)data.getValue("academic"));
	    data.setValue("shoesize", (String)data.getValue("shoesize"));
	    data.setValue("bloodgroup", (String)data.getValue("bloodgroup"));
	    data.setValue("amccheckup", (String)data.getValue("amccheckup"));
	    data.setValue("safety", (String)data.getValue("safety"));
	    data.setValue("medical", (String)data.getValue("medical"));
	    data.setValue("skillcert", (String)data.getValue("skillcert"));
	    data.setValue("aadharno", (String)data.getValue("aadharno"));
	    data.setValue("hazard", (String)data.getValue("hazard"));
	    data.setValue("marital", (String)data.getValue("marital"));
	    data.setValue("wifeName", (String)data.getValue("wifeName"));
	    data.setValue("noofChildren", (String)data.getValue("noofChildren"));
	    data.setValue("proflic1", (String)data.getValue("proflic1"));
	    data.setValue("proflic2", (String)data.getValue("proflic2"));
	    data.setValue("proflic3", (String)data.getValue("proflic3"));
	    data.setValue("prevEmployer", (String)data.getValue("prevEmployer"));
	    data.setValue("referedBy", (String)data.getValue("referedBy"));
	    data.setValue("isRelativeWIN", (String)data.getValue("isRelativeWIN"));
	    data.setValue("relativeName", (String)data.getValue("relativeName"));
	    data.setValue("relativeAddr", (String)data.getValue("relativeAddr"));
	    data.setValue("mobileno", (String)data.getValue("mobileno"));
	    data.setValue("surveyno", (String)data.getValue("surveyno"));
	    data.setValue("relationWithSeller", (String)data.getValue("relationWithSeller"));
	    data.setValue("village", (String)data.getValue("village"));
	    data.setValue("nameofseller", (String)data.getValue("nameofseller"));
	    data.setValue("extent", (String)data.getValue("extent"));
	    data.setValue("acctOpen", (String)data.getValue("acctOpen"));
	    data.setValue("bankbranch", (String)data.getValue("bankbranch"));
	    data.setValue("acctno", (String)data.getValue("acctno"));
	    data.setValue("std", (String)data.getValue("std"));
	    request.setAttribute("workorderId", workorderId);
	    request.setAttribute("workorderLineId", workorderLineId);
	    data.setValue("availWorkorder", workorderListFiltered);
	    data.setValue("availWorkorderLine", workorderLineListFiltered);
	    data.setValue("effdt", (String)data.getValue("effdt") == null ? "" : (String)data.getValue("effdt"));
	    data.setValue("age", (String)data.getValue("age") == null ? "" : (String)data.getValue("age"));  
	    data.setValue("exp", (String)data.getValue("exp") == null ? "" : (String)data.getValue("exp")); 
	    data.setValue("empmobileno", (String)data.getValue("empmobileno") == null ? "" : (String)data.getValue("empmobileno"));
	    data.setValue("wageEffdt", (String)data.getValue("wageEffdt") == null ? "" : (String)data.getValue("wageEffdt"));
	    data.setValue("vehicleType", (String)data.getValue("vehicleType"));
	    data.setValue("vehicleRent", (String)data.getValue("vehicleRent"));
	    data.setValue("quarterRent",(String) data.getValue("quarterRent"));
	    data.setValue("shift", data.getValue("shift"));
	    Workmen workmen = null;
	    Boolean displayStatus = false;
	    if (!"id_new".equalsIgnoreCase(Id)) {
	    	displayStatus = true;
	    	request.setAttribute("displayStatus", displayStatus.toString());
	    	String status = (String) data.getValue("status");
	    	if(status == null)
	    	{
	    		status="";
	    	}
	    	data.setValue("status", status);
	      workmen = Workmen.getWorkmenById(new ObjectIdLong(Id), new ObjectIdLong(unitId));
	      data.setValue("imageContent", "data:image/jpeg;base64," + (workmen.getImageContent() == null ? getDefaultImage() : workmen.getImageContent()));
	    }
	    else {
	    	request.setAttribute("displayStatus", displayStatus.toString());
	      data.setValue("imageContent", "data:image/jpeg;base64," + getDefaultImage()); }
	    data.setValue("availDeviceGroupNames", new CMSService().retrieveAllDeviceGroups());
	  }
	  
  private List validateForm(ImportActionForm data) throws Exception
  {
    boolean performEmpCodeValidation = false;
    String id = (String)data.getValue("id");
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      performEmpCodeValidation = true;
    }
    String firstName = (String)data.getValue("firstname");
    String lastName = (String)data.getValue("lastname");
    String gender = (String)data.getValue("gender");
    String prevExp= (String)data.getValue("prevExp");
    String prevOrg= (String)data.getValue("prevOrg");
    String ecode = (String)data.getValue("eCode");
    String relationName = (String)data.getValue("relationName");
    String dob = (String)data.getValue("dob");
    String empType = (String)data.getValue("empType");
    String supplyType = (String)data.getValue("supplyType");
    String tradeId = (String)data.getValue("tradeId");
    String skillId = (String)data.getValue("skillId");
    String badgeNumber = (String)data.getValue("badgeNumber");
    String doj = (String)data.getValue("doj");
    String basic = (String)data.getValue("basic");
    String da = (String)data.getValue("da");
    String allowance = (String)data.getValue("allowance");
    String vda = (String)data.getValue("vda");
    String pda = (String)data.getValue("pda");
    String hra = (String)data.getValue("hra");
    String conveyance = (String)data.getValue("conveyance");
    String specialAllowance = (String)data.getValue("specialAllowance");
    String shiftAllowance = (String)data.getValue("shiftAllowance");
    String dustAllowance = (String)data.getValue("dustAllowance");
    String medicalAllowance = (String)data.getValue("medicalAllowance");
    String lta = (String)data.getValue("lta");
    String educationAnnual = (String)data.getValue("educationAnnual");
    String dot = (String)data.getValue("dot");
    String pStateId = (String)data.getValue("pStateId");
    String pDistrict = (String)data.getValue("pDistrict");
    String pTaluka = (String)data.getValue("pTaluka");
    String pVillage = (String)data.getValue("pVillage");
    String permStateId = (String)data.getValue("permStateId");
    String permDistrict = (String)data.getValue("permDistrict");
    String permTaluka = (String)data.getValue("permTaluka");
    String permVillage = (String)data.getValue("permVillage");
    String mobileNumber = (String)data.getValue("mobileno");
    String shoeSize = (String)data.getValue("shoesize");
    String noOfChildren = (String)data.getValue("noofChildren");
    String contrId = (String)data.getValue("ccode");
    String depId = (String)data.getValue("depId");
    String secId = (String)data.getValue("secId");
    String isPF = "on".equalsIgnoreCase((String)data.getValue("pfExempt")) ? "Yes" : "No";
    String pfNumber = (String)data.getValue("pfaccno");
    String isESI = "on".equalsIgnoreCase((String)data.getValue("esicExempt")) ? "Yes" : "No";
    String esic = (String)data.getValue("esic");
    String aadharno = (String)data.getValue("aadharno");
    String isLandLoser = "on".equalsIgnoreCase((String)data.getValue("isLandLoser")) ? "Yes" : "False";
    String surveyno = (String)data.getValue("surveyno");
    String relationWithSeller = (String)data.getValue("relationWithSeller");
    String village = (String)data.getValue("village");
    String nameofseller = (String)data.getValue("nameofseller");
    String panNo = (String)data.getValue("panno");
    String workorderNum = (String) data.getValue("workorderNum");
    String item_serviceItem_number = (String) data.getValue("item_serviceItem_number");
  //  String itemNum = (String) data.getValue("itemNum");
   // String serviceLineItem = (String) data.getValue("serviceLineItem");
    String laborAccounteffdt = (String) data.getValue("effdt");
    


    List ex = new ArrayList();
    
    if ((!performEmpCodeValidation) && ("".equalsIgnoreCase(ecode))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.ecode", "Employee Code")));
    }
    else if ((performEmpCodeValidation) && (new CMSService().doesEmployeeCodeExist(ecode))) {
      ex.add(CMSException.duplicatedConfigProperty(KronosProperties.get("label.ecode", "Employee Code")));
    }
    else {
      String codeCheck = new CMSService().doPFESIAadharUniqueness("".equalsIgnoreCase(pfNumber) ? "No_Check_For" : pfNumber, "".equalsIgnoreCase(esic) ? "No_Check_For" : esic, aadharno);
      if ((!"Skip".equals(codeCheck)) && (!ecode.equalsIgnoreCase(codeCheck))) {
        ex.add(CMSException.customMessage("Employee " + codeCheck + " is having same PF/ESI/Aadhar number"));
      }
    }
    if ("".equalsIgnoreCase(firstName)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.firstName", "First Name")));
    }
    
    if ("".equalsIgnoreCase(lastName)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.lastname", "Last Name")));
    }

    if ("".equalsIgnoreCase(gender)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.gender", "Gender")));
    }
    else if ("-1".equalsIgnoreCase(gender))
    {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.gender", "Gender")));
    }
    /*if("".equalsIgnoreCase(prevExp))
    {
    	ex.add(CMSException.missingRequiredField(KronosProperties.get("lable.prevExperience", "prevExperience")));
    }*/
    if("".equalsIgnoreCase(prevOrg))
    {
    	ex.add(CMSException.missingRequiredField(KronosProperties.get("lable.prevOrganization", "PrevOrganization")));
    }
   /* if ("".equalsIgnoreCase(dob)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.dob", "Date Of Birth")));
    }*/
    
    if ("".equalsIgnoreCase(relationName)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.relationName", "Relation Name")));
    }
    
    if ((contrId == null) || ("-1".equalsIgnoreCase(contrId))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.ccode", "Contractor Code")));
    }
    
    if ((depId == null) || ("-1".equalsIgnoreCase(depId))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.code", "Department Name")));
    }
    
    if ((secId == null) || ("-1".equalsIgnoreCase(secId))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("cms.label.section", "Section")));
    }
    
    
    if (("No".equalsIgnoreCase(isPF)) && ((pfNumber == null) || ("".equalsIgnoreCase(pfNumber)))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.pfAcctNo", "label.pfAcctNo")));
    }
    
    if (("No".equalsIgnoreCase(isESI)) && ((esic == null) || ("".equalsIgnoreCase(esic)))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("ESIC", "ESIC")));
    }
    
    if ((empType == null) || ("-1".equalsIgnoreCase(empType))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("Employee Type", "Employee Type")));
    }
    if ((supplyType == null) || ("-1".equalsIgnoreCase(supplyType))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.supplyType", "Labor Type")));
    }
    
    if ((tradeId == null) || ("-1".equalsIgnoreCase(tradeId))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.trade", "Trade")));
    }
    if ((skillId == null) || ("-1".equalsIgnoreCase(skillId))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.skill", "Skill")));
    }
    
    /*KDate kdoj = null;
    if ("".equalsIgnoreCase(doj)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.dateofjoining", "Date Of Joining")));
    } else {
      try {
        kdoj = KServer.stringToDate(doj);
      } catch (Exception e) {
        ex.add(
          CMSException.incorrectDate(KronosProperties.get("label.dateofjoining", "Date Of Joining")));
      }
    }*/
    
   /* if ((dot != null) && (!"null".equals(dot)) && (!"".equals(dot)) && (!"".equals(doj))) {
      if ((dot != null) && (doj != null)) {
        KDate kdot = null;
        try {
          kdot = KServer.stringToDate(dot);
        } catch (Exception e) {
          ex.add(
            CMSException.incorrectDate(KronosProperties.get("label.dateOfTermination", "Date Of Termination")));
        }
        if ((kdot != null) && (kdoj != null) && (kdot.isBefore(kdoj))) {
          ex.add(
            CMSException.invalidDate(KronosProperties.get("label.dateOfTermination", "Date Of Termination")));
        }
        
      }
    }*/
   /* else {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.dateOfTermination", "Date of Termination")));
    }*/
    
    if ("".equalsIgnoreCase(basic)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.basic", "Basic")));
    } else if (!isNumeric(basic)) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.basic", "Basic")));
    }
    
    if ("".equalsIgnoreCase(da)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.da", "DA")));
    } else if (!isNumeric(da)) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.da", "DA")));
    }
    
    if ("".equalsIgnoreCase(allowance)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.allowance", "Allowance")));
    } else if (!isNumeric(allowance)) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.allowance", "Allowance")));
    }
    
    if (!empType.equals("OWB")) {
      if ((!"".equalsIgnoreCase(vda)) && (!isNumeric(vda))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("VDA", "VDA")));
      }
      if ((!"".equalsIgnoreCase(pda)) && (!isNumeric(pda))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("PDA", "PDA")));
      }
      if ((!"".equalsIgnoreCase(hra)) && (!isNumeric(hra))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("HRA", "HRA")));
      }
      if ((!"".equalsIgnoreCase(conveyance)) && (!isNumeric(conveyance))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Conveyance", "Conveyance")));
      }
      if ((!"".equalsIgnoreCase(specialAllowance)) && (!isNumeric(specialAllowance))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Special Allowance", "Special Allowance")));
      }
      if ((!"".equalsIgnoreCase(shiftAllowance)) && (!isNumeric(shiftAllowance))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Shift Allowance", "Shift Allowance")));
      }
      if ((!"".equalsIgnoreCase(dustAllowance)) && (!isNumeric(dustAllowance))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Dust Allowance Monthly", "Dust Allowance Monthly")));
      }
      if ((!"".equalsIgnoreCase(medicalAllowance)) && (!isNumeric(medicalAllowance))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Medical", "Medical")));
      }
      if ((!"".equalsIgnoreCase(lta)) && (!isNumeric(lta))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("LTA", "LTA")));
      }
      
      if ((!"".equalsIgnoreCase(educationAnnual)) && (!isNumeric(educationAnnual))) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("Education Annual", "Education Annual")));
      }
    }
    
    if (("-1".equals(pStateId)) && 
      (!pDistrict.isEmpty()) && (!" ".equals(pDistrict)) && (!pTaluka.isEmpty()) && (!" ".equals(pTaluka)) && (!pVillage.isEmpty()) && (!" ".equals(pVillage))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.presentAddress", "State")));
    }
    

    if (("-1".equals(permStateId)) && 
      (!permDistrict.isEmpty()) && (!" ".equals(permDistrict)) && (!permTaluka.isEmpty()) && (!" ".equals(permTaluka)) && (!permVillage.isEmpty()) && (!" ".equals(permVillage))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.permAddress", "State")));
    }
    

    if (!"".equals(panNo))
    {

      Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
      Matcher matcher = pattern.matcher(panNo.toUpperCase());
      if (!matcher.matches()) {
        ex.add(CMSException.customMessage("The PAN card number is not valid"));
      }
    }
    
    if ((aadharno == null) || ("".equalsIgnoreCase(aadharno))) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.aadharNo", "label.aadharNo")));
    }
    else if (!isNumeric(aadharno)) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.aadharNo", "label.aadharNo")));
    }
    else {
      int length = aadharno.length();
      if (length != 12) {
        ex.add(CMSException.customMessage("Aadhar Number is not valid, it should be 12 digits"));
      }
    }
    

    if ((!"".equals(mobileNumber)) && (mobileNumber != null)) {
      int length = mobileNumber.length();
      if (length != 10) {
        ex.add(CMSException.valueTooLong(KronosProperties.get("label.mobileno", "Mobile Number")));
      }
      
      if (!isNumeric(mobileNumber)) {
        ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.mobileno", "Mobile Number")));
      }
    }
    
    if ((!"".equals(shoeSize)) && (shoeSize != null) && 
      (!isNumeric(shoeSize))) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.shoesize", "Shoe Size")));
    }
    

    if ((noOfChildren != null) && (!"".equals(noOfChildren)) && (!isNumeric(noOfChildren))) {
      ex.add(CMSException.onlyNumbersAllowed(KronosProperties.get("label.noOfChildren", "Number of Children")));
    }
    
    if ("Yes".equals(isLandLoser)) {
      if ("".equalsIgnoreCase(surveyno)) {
        ex.add(CMSException.missingRequiredField(KronosProperties.get("label.surveyno", "Survey No")));
      }
      if ("".equalsIgnoreCase(relationWithSeller)) {
        ex.add(CMSException.missingRequiredField(KronosProperties.get("label.relationWithSeller", "Relation with the seller")));
      }
      if ("".equalsIgnoreCase(village)) {
        ex.add(CMSException.missingRequiredField(KronosProperties.get("label.village", "Village")));
      }
      if ("".equalsIgnoreCase(nameofseller)) {
        ex.add(CMSException.missingRequiredField(KronosProperties.get("label.nameofseller", "Name of Seller")));
      }
    }
    
    FormFile formFile = data.getFormFile();
    try {
      byte[] imageData = formFile.getFileData();
      ByteArrayInputStream stream = new ByteArrayInputStream(imageData);
      BufferedImage localBufferedImage = ImageIO.read(stream);
    } catch (IOException IOe) {
      ex.add(CMSException.invalidImage(formFile.getFileName()));
    }
    
    /*int underage = 18;
    int overage = Integer.parseInt(KronosProperties.getProperty("cms.employee.over.age"));
    if (("".equalsIgnoreCase(dob)) && (dob == null)) {
      ex.add(CMSException.missingRequiredField(KronosProperties.get("label.dateofbirth", "Date of Birth")));
    } 
      KDate kdob = null;
      try {
        kdob = KServer.stringToDate(dob);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = sdf.parse(dob);
        int age = getAge(birthDate);
        if (age < 18)
          ex.add(CMSException.customMessage("Workmen age must be " + underage + " years old"));
        if (age >= overage) {
          ex.add(CMSException.customMessage("Workmen age should not be " + overage + " years old"));
        }
      } catch (Exception e) {
        System.out.print("test---" + e);
        ex.add(CMSException.incorrectDate(KronosProperties.get("label.dateofbirth", "Date of Birth")));
      }*/

    String laborLev6="";
    if("-".equalsIgnoreCase(workorderNum)){
    	laborLev6 = workorderNum;
    }
    else{
    	laborLev6 = workorderNum + "-" + item_serviceItem_number.trim();
    }
    boolean result = new CMSService().checkLaborLev6(laborLev6);
    if(!result)
    {
    	ex.add(CMSException.customMessage(laborLev6 + " Labor Level Entry Is Invalid!!"));
    }
    
    if (ex.size() > 0) {
      CMSException e = CMSException.validationError();
      e.addWrappedExceptions(ex);
      throw e;
    }
   
    
    return ex;
  }
  
  private static int getAge(Date birthdate) {
    int age = new CMSService().getAge(birthdate);
    return age;
  }
  private static int getExp(Date experienceDate) {
	    int exp = new CMSService().getExp(experienceDate);
	    return exp;
	  }

  private boolean isNumeric(String value)
  {
    String regex = "\\d+[\\.\\d+]*";
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(value).matches();
  }
  
  void setNoFieldsSelected(HttpServletRequest request) {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
  }
  
  public ActionForward doNew(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      Workmen wk = new Workmen();
      setValuesToUI(data, wk, request);
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
  
  private void saveWorkmen(ActionForm form, HttpServletRequest request) throws Exception {
    Workmen workmen = null;
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String unitId = (String)data.getValue("unitId");
    String id = (String)data.getValue("id");
    Message message = null;
    if (!"id_new".equalsIgnoreCase(id)) {
      message = new Message("cms.message.workmen.update");
    }
    else {
      message = new Message("cms.message.workmen.create");
    }
    try {
      ImportActionForm importActionForm = (ImportActionForm)data;
      List validateForm = validateForm(importActionForm);
      if(validateForm.size() == 0)
      {
      workmen = savedManagerFromUI((DynamicMapBackedForm)form);
      WorkmenFacade facade = getFacade();
      workmen = facade.saveWorkmen(workmen, unitId);
     // new CMSService().updateEmpSeqTable(unitId);
      data.setValue("id", workmen.getEmpId().toString());
      StrutsUtils.addMessage(request, message);
      data.setValue("status", "Active");
      }
   
    } catch (Exception e) {
      logError(e);
      
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
      
      throw e;
    }
  }
  


  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-contractor-application";
  

  public ActionForward doSaveAndReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      ImportActionForm importActionForm = (ImportActionForm)data;
      validateForm(importActionForm);
      saveWorkmen(form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
    } catch (Exception e) {
      logError(e);
      
      if(e instanceof CMSException)
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
    

    return mapping.findForward("doCMSWorkmenListApplication");
  }
  
  private void setWorkmenDatatoUI(DynamicMapBackedForm form, HttpServletRequest request) {
    String id = (String)form.getValue("id");
    String unitId = (String)form.getValue("unitId");
    if ((unitId == null) || ("".equals(unitId))) {
      unitId = (String)request.getAttribute("unitId");
    }
    Workmen wk = getFacade().getWorkmen(id, unitId);
    setValuesToUI(form, wk, request);
  }
  
  public ActionForward doSafetyTraining(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    request.setAttribute("unitId", (String)((DynamicMapBackedForm)form).getValue("unitId"));
    return mapping.findForward("doCMSSafetyTraining");
  }
  
  public ActionForward doSafetyViolation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    
	  
	  DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    setRefreshDataToUI(data, request);
    return mapping.findForward("success");
  }
  
  public ActionForward doHealthInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    request.setAttribute("unitId", (String)((DynamicMapBackedForm)form).getValue("unitId"));
    return mapping.findForward("doCMSHealthInformation");
  }
  
  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    request.setAttribute("unitId", (String)((DynamicMapBackedForm)form).getValue("unitId"));
    request.setAttribute("contrId", (String)((DynamicMapBackedForm)form).getValue("contrId"));
    request.setAttribute("statusId", (String)((DynamicMapBackedForm)form).getValue("statusId"));
    return mapping.findForward("doCMSWorkmenListApplication");
  }
  
  
  private void setValuesToUI(DynamicMapBackedForm data, Workmen workmen, HttpServletRequest request)
  {
    String unitId = (String)data.getValue("unitId");
    String contId = (String) data.getValue("contrId");
    request.setAttribute("unitId", new ObjectIdLong(unitId));
    List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
    data.setValue("availUnitNames", pes);
    PrincipalEmployee pe = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
    List<Department> depList = Department.doRetrieveByUnitId(new ObjectIdLong(unitId)); 
    List<Section> secList = Section.doRetrieveByUnitId(new ObjectIdLong(unitId));
    List<Section> secListFiltered = new ArrayList();
    List<Contractor> contrList = Contractor.doRetrieveByUnitId(new ObjectIdLong(unitId));
    List<Workorder> workorderList = Workorder.getWorkorderByUnitAndContId(new ObjectIdLong(unitId),new ObjectIdLong(contId));
    List<Workorder> workorderListFiltered = new ArrayList();
    List<WorkorderLine> workorderLineList = WorkorderLine.getWorkorderLineByUnitAndContId(new ObjectIdLong(unitId),new ObjectIdLong(contId));
    List<WorkorderLine> workorderLineListFiltered = new ArrayList();
    
    Boolean displayStatus = false;
    if ((workmen != null) && (workmen.getEmpId() == null)) {
    	request.setAttribute("displayStatus", displayStatus.toString());
      data.setValue("id", "id_new");
      data.setValue("eCode", "");
      data.setValue("lastname", "");
      data.setValue("firstname", "");
      data.setValue("relationName", "");
      data.setValue("gender", "-1");
      data.setValue("dob", "");
      data.setValue("idmark", "");
      data.setValue("skillId", "-1");
      data.setValue("mobileno", "");
      data.setValue("doj", "");
      data.setValue("dot", "");
      data.setValue("amccheckup", "");
      data.setValue("supplyType", "-1");
      data.setValue("empTypeValue", "-1");
      data.setValue("bloodgroup", "-1");
      request.setAttribute("contrid", new ObjectIdLong((String)data.getValue("contrId")));
      data.setValue("std", "");
      data.setValue("effdt", "");
      data.setValue("wageEffdt", "");
      data.setValue("empmobileno", "");
      data.setValue("vehicleType", "-1");
      data.setValue("shift", "-1");
      data.setValue("prevExp", "");
      data.setValue("prevOrg", "");
    } else if ((workmen != null) && (workmen.getEmpId() != null)) {
    	displayStatus=true;
    	request.setAttribute("displayStatus", displayStatus.toString());
      data.setValue("id", workmen.getEmpId().toString());
      data.setValue("eCode", workmen.getEmpCode());
      data.setValue("badgeNumber", workmen.getBadgeNum() == null ? "" : workmen.getBadgeNum());
      String status = (String) request.getAttribute("status");
      if(status == null)
      {
    	  status=(String) data.getValue("status");
    	  if(status == null)
    	  {
    		  status="";
    	  }
      }
      data.setValue("status", status);
      
      Person p = workmen.getPerson();
      Personality per = Personality.getByPersonId(p.getPersonId());
      PrimaryLaborAccount pla = per.getJobAssignment().getCurrentPrimaryLaborAccount();
      LaborAccount acct = pla.getLaborAccount();
      String[] names = acct.getLaborLevelEntryNames_optimized();
      
      String[] laborLevel6;
      String workorderNum="-";
      String item_serviceItem_number="";
     // String serviceLineItem="";
      if(!"-".equalsIgnoreCase(names[5])){   	  
    	   laborLevel6 = names[5].split("-",2);
    	   workorderNum = laborLevel6[0].trim();
    	   item_serviceItem_number = laborLevel6[1].trim();
    	  // serviceLineItem = laborLevel6[2];
      }
      ObjectIdLong dId = new ObjectIdLong(-1L);
      ObjectIdLong secId = new ObjectIdLong(-1L);
      ObjectIdLong contrId = new ObjectIdLong(-1L);
      ObjectIdLong workorderId = new ObjectIdLong(-6L);
      ObjectIdLong workorderLineId = new ObjectIdLong(-1L);
      
      for (Iterator iterator = depList.iterator(); iterator.hasNext();) 
	  {
        Department department = (Department)iterator.next();
        if (names[2].equalsIgnoreCase(department.getCode())) {
          dId = department.getDepid();
          for (Iterator iterator1 = secList.iterator(); iterator1.hasNext();) {
            Section section = (Section)iterator1.next();
            if (section.getDeptId().equals(dId)) {
              secListFiltered.add(section);
            }
          }
          for (Iterator iterator1 = workorderList.iterator(); iterator1.hasNext();) {
              Workorder workorder = (Workorder)iterator1.next();
              if (workorder.getDepId().equals(dId)) {
                workorderListFiltered.add(workorder);
              }
            }
          break;
        }
      }
     ObjectIdLong sectionId= new CMSService().getSectionId(names[3],dId);
     
      for (Iterator iterator = secListFiltered.iterator(); iterator.hasNext();) {
        Section section = (Section)iterator.next();
        if (sectionId.toString().trim().equalsIgnoreCase(section.getSectionId().toString().trim())) {
          secId = section.getSectionId();
          break;
        }
      }
      
      for (Iterator iterator = workorderListFiltered.iterator(); iterator.hasNext();) {
          Workorder workorder = (Workorder)iterator.next();
          if (workorderNum.equalsIgnoreCase(workorder.getWkNum().trim())) {
            workorderId = workorder.getWorkorderId();
            for (Iterator iterator1 = workorderLineList.iterator(); iterator1.hasNext();) {
                WorkorderLine workorderLine = (WorkorderLine)iterator1.next();
                if (workorderLine.getWorkorderId().equals(workorderId)) {
                  workorderLineListFiltered.add(workorderLine);
                }
              }
            break;
          }
        } 
      
      
      for (Iterator iterator = workorderLineListFiltered.iterator(); iterator.hasNext();) {
          WorkorderLine workorderLine = (WorkorderLine)iterator.next();
          if (item_serviceItem_number.equalsIgnoreCase(workorderLine.getItem_serviceItem_number().trim())) {
            workorderLineId = workorderLine.getWkLineId();
            break;
          }
        }
      
     /* for (Iterator iterator = workorderLineList.iterator(); iterator.hasNext();) {
          WorkorderLine workorderLine = (WorkorderLine)iterator.next();
          if (itemNumber.equalsIgnoreCase(workorderLine.getItemNumber())) {
            workorderLineId = workorderLine.getWkLineId();
            break;
          }
        }*/
      
     /* for (Iterator iterator = workorderLineList.iterator(); iterator.hasNext();) {
          WorkorderLine workorderLine = (WorkorderLine)iterator.next();
          if (serviceLineItem.equalsIgnoreCase(workorderLine.getServiceLineItemNumber())) {
            workorderLineId = workorderLine.getWkLineId();
            break;
          }
        }*/
      
      for (Iterator iterator = contrList.iterator(); iterator.hasNext();) {
        Contractor contractor = (Contractor)iterator.next();
        if (names[4].equalsIgnoreCase(contractor.getCcode())) {
          contrId = (ObjectIdLong)contractor.getcontractorid();
        }
      }
      request.setAttribute("depId", dId);
      request.setAttribute("sId", secId);
      request.setAttribute("contrid", contrId);
      request.setAttribute("workorderId", workorderId);
      request.setAttribute("workorderLineId", workorderLineId);
      data.setValue("availWorkorder", workorderListFiltered);
      data.setValue("availWorkorderLine", workorderLineListFiltered);
      KDate effectiveDate = pla.getEffectiveDate();
      workmen.setLaborAccountEffectiveDate(effectiveDate);
      data.setValue("effdt", workmen.getLaborAccountEffectiveDate());
      data.setValue("age", (String)data.getValue("age") == null ? "" : (String)data.getValue("age"));
      data.setValue("exp", (String)data.getValue("exp") == null ? "" : (String)data.getValue("exp"));
      BaseWageRateSet wage = per.getBaseWageRateSet();
      BaseWageRate currentBaseWageRate = wage.getCurrentBaseWageRate();
      KDate wageEffectiveDate = currentBaseWageRate.getEffectiveDate();
      data.setValue("wageEffdt", wageEffectiveDate);
      
     CustomData d = per.getCustomData().getCustomData("Labor Type");
      if ((d != null) && (d.getCustomText() != null)) {
        data.setValue("supplyType", d.getCustomText());
      }
      else {
        data.setValue("supplyType", "Supply");
      }
	  
	  
     /* String d = workmen.getCustomFields().get("supplyType");
	  String supplyId = (String)data.getValue("supplyId");
	for (Iterator iterator = supplyList.iterator(); iterator.hasNext();) {
        Supply supply = (Supply)iterator.next();
        if (d.equalsIgnoreCase(supply.getSupplyName())) {
          supplyId = (String)supply.getSupplyId();
        }
      }
	  
	  request.setAttribute("supplyId", supplyId);
	  data.setValue("availSupplyNames", supplyList);
	  if ((d != null) && (d.getCustomText() != null))
      {
        String suppId = new String(supplyId);
        //data.setValue("supplyType", d.getCustomText());
        request.setAttribute("supplyId", suppId);
      }
      
      data.setValue("availSupplyNames", new CMSService().getSupplys());
	  
	  */
      
      
      
      
	  
      CustomData empType = per.getCustomData().getCustomData("Employee Type");
      if ((empType != null) && (empType.getCustomText() != null)) {
        data.setValue("empTypeValue", empType.getCustomText());
      }
      else {
        data.setValue("empTypeValue", "WB");
      }
      CustomData vda = per.getCustomData().getCustomData("VDA");
      data.setValue("vda", vda.getCustomText());
      CustomData pda = per.getCustomData().getCustomData("PDA");
      data.setValue("pda", pda.getCustomText());
      CustomData hra = per.getCustomData().getCustomData("HRA");
      data.setValue("hra", hra.getCustomText());
      CustomData Conveyance = per.getCustomData().getCustomData("Conveyance");
      data.setValue("conveyance", Conveyance.getCustomText());
      CustomData specialAllowance = per.getCustomData().getCustomData("Special Allowance");
      data.setValue("specialAllowance", specialAllowance.getCustomText());
      CustomData shiftAllowance = per.getCustomData().getCustomData("Shift Allowance");
      data.setValue("shiftAllowance", shiftAllowance.getCustomText());
      CustomData dustAllowance = per.getCustomData().getCustomData("Dust Allowance - Monthly");
      data.setValue("dustAllowance", dustAllowance.getCustomText());
      CustomData medicalAllowance = per.getCustomData().getCustomData("Medical");
      data.setValue("medicalAllowance", medicalAllowance.getCustomText());
      CustomData lta = per.getCustomData().getCustomData("LTA");
      data.setValue("lta", lta.getCustomText());
      CustomData education = per.getCustomData().getCustomData("Education – Annual");
      data.setValue("educationAnnual", education.getCustomText());
      CustomData esicwc = per.getCustomData().getCustomData("ESICWC");
      data.setValue("esic", esicwc.getCustomText());
      CustomData isESIC = per.getCustomData().getCustomData("ESIC Exempt");
      if ((isESIC != null) && (!"".equalsIgnoreCase(isESIC.getCustomText())) && ("Yes".equalsIgnoreCase(isESIC.getCustomText()))) {
        data.setValue("esicExempt", "on");
        request.setAttribute("esiEx", "none");
      }
      else {
        data.setValue("esicExempt", "off"); }
      CustomData isPF = per.getCustomData().getCustomData("PF Exempt");
      if ((isPF != null) && (!"".equalsIgnoreCase(isPF.getCustomText())) && ("Yes".equalsIgnoreCase(isPF.getCustomText()))) {
        data.setValue("pfExempt", "on");
        request.setAttribute("pfEx", "none");
      }
      else {
        data.setValue("pfExempt", "off");
      }
      CustomData empMobileNumber = per.getCustomData().getCustomData("Mobile Number");
      data.setValue("empmobileno", empMobileNumber.getCustomText());
      
      CustomData vehicleType = per.getCustomData().getCustomData("Vehicle Type");
      if((vehicleType!= null) && (vehicleType.getCustomText() != null) && (!"".equalsIgnoreCase(vehicleType.getCustomText())))
      {
    	  data.setValue("vehicleType", vehicleType.getCustomText());
      }
      else{
    	  data.setValue("vehicleType", "-1");
      }
      
      CustomData shift = per.getCustomData().getCustomData("Shift Hours");
      if((shift!= null) && (shift.getCustomText() != null) && (!"".equalsIgnoreCase(shift.getCustomText())))
      {
    	  data.setValue("shift", shift.getCustomText());
      }
      else{
    	  data.setValue("shift", "8");
      }
      
      CustomData vehicleRent = per.getCustomData().getCustomData("Vehicle Rent");
      data.setValue("vehicleRent", vehicleRent.getCustomText());
      CustomData quarterRent = per.getCustomData().getCustomData("Quarter Rent");
      data.setValue("quarterRent", quarterRent.getCustomText());
      
      data.setValue("lastname", workmen.getLastName());
      data.setValue("firstname", workmen.getFirstName());
      data.setValue("relationName", workmen.getRelationName());
      data.setValue("dob", workmen.getDOB());
      data.setValue("idmark", workmen.getIdMark() == null ? "" : workmen.getIdMark());
      data.setValue("skillId", workmen.getSkill().getSkillId().toString());
      data.setValue("tradeId", workmen.getTrade().getTradeId().toString());
      
      if (workmen.getDeviceGroup() != null) {
        data.setValue("deviceGroupId", workmen.getDeviceGroup().getDeviceGroupId().toString());
      }
      
      if (workmen.getPresentAddress() != null) {
        data.setValue("pAddressId", workmen.getPresentAddress().getId().toString());
        data.setValue("pStateId", workmen.getPresentAddress().getStateId().toString());
        data.setValue("pDistrict", workmen.getPresentAddress().getDistrict());
        data.setValue("pTaluka", workmen.getPresentAddress().getTaluka());
        data.setValue("pVillage", workmen.getPresentAddress().getVillage());
      }
      

      if (workmen.getPermAddress() != null) {
        data.setValue("permAddressId", workmen.getPermAddress().getId().toString());
        data.setValue("permStateId", workmen.getPermAddress().getStateId().toString());
        data.setValue("permDistrict", workmen.getPermAddress().getDistrict());
        data.setValue("permTaluka", workmen.getPermAddress().getTaluka());
        data.setValue("permVillage", workmen.getPermAddress().getVillage());
      }
      

      data.setValue("personDetlId", workmen.getDetail().getPersonDetlId().toString());
      data.setValue("doj", workmen.getDetail().getDoj());
      KDate dot = workmen.getDetail().getDot();
      if ((dot != null) && (dot.isNull())) {
        data.setValue("dot", "");
      } else {
        data.setValue("dot", dot);
      }
      
      data.setValue("pfaccno", workmen.getDetail().getPfAcctNo() == null ? "" : workmen.getDetail().getPfAcctNo());
      data.setValue("aadharno", workmen.getDetail().getAadharNo());
      data.setValue("gender", workmen.getDetail().getGender());
      data.setValue("prevExp", workmen.getDetail().getPrevExp());
      data.setValue("prevOrg", workmen.getDetail().getPrevOrg());
      data.setValue("panno", workmen.getDetail().getPanno() == null ? "" : workmen.getDetail().getPanno());
      data.setValue("hazard", 
        (workmen.getDetail().getHazard() != null) && (workmen.getDetail().getHazard().booleanValue()) ? "on" : "off");
      data.setValue("marital", 
        (workmen.getDetail().getmStatus() != null) && (workmen.getDetail().getmStatus().booleanValue()) ? "on" : "off");
      data.setValue("wifeName", workmen.getDetail().getWifeNm());
      data.setValue("noofChildren", workmen.getDetail().getNoOfChildren());
      data.setValue("technical", workmen.getDetail().getTechnical() == null ? "" : workmen.getDetail().getTechnical());
      data.setValue("academic", workmen.getDetail().getAcademic() == null ? "" : workmen.getDetail().getAcademic());
      data.setValue("proflic1", workmen.getDetail().getProf1());
      data.setValue("proflic2", workmen.getDetail().getProf2());
      data.setValue("proflic3", workmen.getDetail().getProf3());
      data.setValue("proflic4", workmen.getDetail().getProf4());
      data.setValue("prevEmployer", workmen.getDetail().getPreviousEmployer());
      data.setValue("presentContractorId", workmen.getDetail().getpContr());
      data.setValue("referedBy", workmen.getDetail().getReferredBy());
      
      data.setValue("trade", workmen.getTrade().getTradeName());
      

      data.setValue("skill", workmen.getSkill().getSkillNm());
      data.setValue("wageId", workmen.getWage().getId().toString());
      data.setValue("basic", workmen.getWage().getBasic());
      data.setValue("da", workmen.getWage().getDa());
      data.setValue("allowance", workmen.getWage().getAllowance());
      
      data.setValue("isRelativeWIN", 
        (workmen.getDetail().getWinCoRelative() != null & workmen.getDetail().getWinCoRelative().booleanValue()) ? "on" : 
        "off");
      data.setValue("relativeName", workmen.getDetail().getWinCoName());
      data.setValue("relativeAddr", workmen.getDetail().getWincoAddress());
      data.setValue("mobileno", workmen.getDetail().getMobileNo());
      
      data.setValue("landloser", 
        (workmen.getDetail().getLandLoser() != null) && (workmen.getDetail().getLandLoser().booleanValue()) ? "on" : "off");
      data.setValue("surveyno", workmen.getDetail().getSurveyNo());
      data.setValue("relationWithSeller", workmen.getDetail().getSurveyNo());
      data.setValue("village", workmen.getDetail().getVillage());
      data.setValue("nameofseller", workmen.getDetail().getNameofSeller());
      data.setValue("extent", workmen.getDetail().getExtent());
      data.setValue("acctOpen", (workmen.getDetail().getAccOpenWithContractor() != null) && 
        (workmen.getDetail().getAccOpenWithContractor().booleanValue()) ? "on" : "off");
      data.setValue("bankbranch", workmen.getDetail().getBankBranch());
      data.setValue("acctno", workmen.getDetail().getAcctNo());
      data.setValue("shoesize", workmen.getDetail().getShoesize() == null ? "" : workmen.getDetail().getShoesize());
      data.setValue("bloodgroup", workmen.getDetail().getBloodgrp() == null ? "" : workmen.getDetail().getBloodgrp());
      request.setAttribute("bloodgroup", workmen.getDetail().getBloodgrp() == null ? "" : workmen.getDetail().getBloodgrp());
      data.setValue("medical", 
    	(workmen.getDetail().getMedicalTrainingSw() != null) && (workmen.getDetail().getMedicalTrainingSw().booleanValue()) ? 
    			        "on" : "off");
      data.setValue("amccheckup", workmen.getDetail().getAmcCheck().isNull() ? "" : workmen.getDetail().getAmcCheck());
      
      data.setValue("safety", 
        (workmen.getDetail().getSafetyTrainingSw() != null) && (workmen.getDetail().getSafetyTrainingSw().booleanValue()) ? 
        "on" : "off");
      CustomData std = per.getCustomData().getCustomData("Safety Training Date");
      data.setValue("std", std.getCustomText() == null ? "" : std.getCustomText());
      data.setValue("skillcert", 
        (workmen.getDetail().getSkillCert() != null) && (workmen.getDetail().getSkillCert().booleanValue()) ? "on" : "off");
      data.setValue("contrId", workmen.getContractor().getcontractorid().toString());
      data.setValue("imageContent", "data:image/jpeg;base64," + workmen.getImageContent());
    }
    data.setValue("availDepartmentNames", depList);
    data.setValue("availSectionNames", secListFiltered);
    data.setValue("availContrCodes", contrList);
    data.setValue("availWorkorder", workorderList);
    data.setValue("availWorkorderLine", workorderLineList);
    
    String stateId = (String)data.getValue("pStateId");
    
    if (stateId != null) {
      ObjectIdLong sId = new ObjectIdLong(stateId);
      request.setAttribute("pStateId", sId);
    }
    
    data.setValue("availPStateNames", new CMSService().getStates());
    
    String permStateId = (String)data.getValue("permStateId");
    if (permStateId != null) {
      ObjectIdLong permSId = new ObjectIdLong(permStateId);
      request.setAttribute("permStateId", permSId);
    }
    
    data.setValue("availPermStateNames", new CMSService().getStates());
    
    String contrId = (String)data.getValue("contrId");
    if (contrId != null) {
      ObjectIdLong cId = new ObjectIdLong(contrId);
      data.setValue("availContractors", getContractorNames(cId));
    } else {
      data.setValue("availContractors", Contractor.doRetrieveAll());
    }
    
    if ((workmen != null) && (workmen.getSkill() != null)) {
      request.setAttribute("skId", workmen.getSkill().getSkillId());
    } else {
      request.setAttribute("skId", new ObjectIdLong(-1L));
    }
    if ((workmen != null) && (workmen.getTrade() != null)) {
      request.setAttribute("tId", workmen.getTrade().getTradeId());
    } else {
      request.setAttribute("tId", new ObjectIdLong(-1L));
    }
    if ((workmen != null) && (workmen.getDeviceGroup() != null)) {
      request.setAttribute("dId", workmen.getDeviceGroup().getDeviceGroupId());
    } else {
      request.setAttribute("dId", new ObjectIdLong(-1L));
    }
    if (contrId != null) {
      Contractor contr = Contractor.doRetrieveById(new ObjectIdLong(contrId), new ObjectIdLong(unitId));
      data.setValue("availSkillNames", Skill.retrieveSkills());
      data.setValue("availTradeNames", Trade.doRetrieveAll());
    } else {
      data.setValue("availSkillNames", Skill.retrieveSkills());
      data.setValue("availTradeNames", Trade.doRetrieveAll());
    }
    
    data.setValue("availDeviceGroupNames", new CMSService().retrieveAllDeviceGroups());
    if ((workmen != null) && (workmen.getImageContent() != null)) {
      data.setValue("imageContent", getImageSource(workmen.getImageContent()));
    } else {
      FormFile formFile = ((ImportActionForm)data).getFormFile();
      try {
        byte[] imageData = formFile.getFileData();
        data.setValue("imageContent", getImageSource(EmpPhotoImage.getEncodedImage(imageData)));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        data.setValue("imageContent", getImageSource(getDefaultImage()));
      } catch (IOException ioe) {
        ioe.printStackTrace();
        data.setValue("imageContent", getImageSource(getDefaultImage()));
      } catch (EmpPhotoBusinessValidationException epbve) {
        epbve.printStackTrace();
        data.setValue("imageContent", getImageSource(getDefaultImage()));
      } catch (Exception ex) {
        data.setValue("imageContent", getImageSource(getDefaultImage()));
      }
    }
  }
  





  private String getDefaultImage()
  {
    return "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";
  }
  




  private String getImageSource(String imageContent)
  {
    return "data:image/jpeg;base64,".concat(imageContent);
  }
  
  private List<Contractor> getContractorNames(ObjectIdLong getpContr) {
    List<Contractor> contrs = Contractor.doRetrieveAll();
    
    if (getpContr != null) {
      for (Iterator iterator = contrs.iterator(); iterator.hasNext();) {
        Contractor contractor = (Contractor)iterator.next();
        if (((ObjectIdLong)contractor.getcontractorid()).longValue() == getpContr.longValue()) {
          contractor.setSelected(Boolean.valueOf(true));
        }
      }
    }
    return contrs;
  }
  
  private List<CMSState> getStates(ObjectIdLong stateId)
  {
    List<CMSState> secs = getFacade().getAllStates();
    if (stateId != null) {
      for (Iterator iterator = secs.iterator(); iterator.hasNext();) {
        CMSState cmsState = (CMSState)iterator.next();
        if (cmsState.getStateId() == stateId) {
          cmsState.setSelected(Boolean.TRUE);
          break;
        }
      }
    }
    

    return secs;
  }
  
  private void clearUiDirtyData(DynamicMapBackedForm data) {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  String getSelectedId(ActionForm form) {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  
  protected WorkmenFacade getFacade()
  {
    return new WorkmenFacadeImpl();
  }
  
  private Workmen savedManagerFromUI(ActionForm form)
  {
    ImportActionForm data = (ImportActionForm)form;
    Workmen workmen = createWorkmen(data);
    return workmen;
  }
  
  private Workmen createWorkmen(ImportActionForm data) {
    Workmen wk = new Workmen();
    
    String id = (String)data.getValue("id");
    String unitId = (String)data.getValue("sunitId");
    PrincipalEmployee pe = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      wk.setEmpId(null);
      //wk.setEmpCode(new CMSService().getNewEmpCode(pe.getUnitCode()));
    }
    else {
      wk.setEmpId(new ObjectIdLong(id));
      wk.setEmpCode((String)data.getValue("eCode"));
    }
    
    if (pe != null) {
      wk.setUnitId(pe.getUnitId().toString());
    }
    wk.setBadgeNum((String)data.getValue("badgeNumber"));
    wk.setIsBSR(Boolean.valueOf(!((String)data.getValue("supplyType")).equalsIgnoreCase("SupplyType")));
    //wk.setIsBSR(Boolean.valueOf(!((String)data.getValue("supplyId")).equalsIgnoreCase("SupplyType")));
    wk.setFirstName((String)data.getValue("firstname"));
    wk.setLastName((String)data.getValue("lastname"));
    String contrId = (String)data.getValue("ccode");
    wk.setContractor(Contractor.doRetrieveById(new ObjectIdLong(contrId), new ObjectIdLong(unitId)));
    wk.setDetail(getDetail(data));
    wk.setDOB(KServer.stringToDate((String)data.getValue("dob")));
    wk.setIdMark((String)data.getValue("idmark"));
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      wk.setPresentAddress(getAddress(null, (String)data.getValue("pVillage"), (String)data.getValue("pTaluka"), 
        (String)data.getValue("pDistrict"), (String)data.getValue("pStateId")));
    }
    else {
      String addId = (String)data.getValue("pAddressId");
      
      wk.setPresentAddress(getAddress(new ObjectIdLong(addId), (String)data.getValue("pVillage"), 
        (String)data.getValue("pTaluka"), (String)data.getValue("pDistrict"), 
        (String)data.getValue("pStateId")));
    }
    

    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      wk.setPermAddress(
        getAddress(null, (String)data.getValue("permVillage"), (String)data.getValue("permTaluka"), 
        (String)data.getValue("permDistrict"), (String)data.getValue("permStateId")));
    } else {
      String addId = (String)data.getValue("permAddressId");
      
      wk.setPermAddress(getAddress(new ObjectIdLong(addId), (String)data.getValue("permVillage"), 
        (String)data.getValue("permTaluka"), (String)data.getValue("permDistrict"), 
        (String)data.getValue("permStateId")));
    }
    

    wk.setRelationName((String)data.getValue("relationName"));
    Skill skill = Skill.retrieveSkill(new ObjectIdLong((String)data.getValue("skillId")));
    wk.setSkill(skill);
    wk.setStatusID(Integer.valueOf(0));
    Trade trade = Trade.retrieveById(new ObjectIdLong((String)data.getValue("tradeId")));
    wk.setTrade(trade);
    DeviceGroup deviceGroup = DeviceGroup.getDeviceGroup(new ObjectIdLong((String)data.getValue("deviceGroupId")));
    wk.setDeviceGroup(deviceGroup);
    
    String basic = (String)data.getValue("basic");
    String da = (String)data.getValue("da");
    String allowance = (String)data.getValue("allowance");
    
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      wk.setWage(new Wage(null, basic, da, allowance));
    } else {
      ObjectIdLong wageId = new ObjectIdLong((String)data.getValue("wageId"));
      wk.setWage(new Wage(wageId, basic, da, allowance));
    }
    
    String stringEffdt = (String)data.getValue("effdt");
    KDate effdt = KServer.stringToDate(stringEffdt);

    if (stringEffdt == null || "".equalsIgnoreCase(stringEffdt)) {
    	 Personality per = Personality.getByPersonNumber(wk.getEmpCode());         
         PrimaryLaborAccount pla = per.getJobAssignment().getCurrentPrimaryLaborAccount();
         KDate effectiveDate = pla.getEffectiveDate();
         wk.setLaborAccountEffectiveDate(effectiveDate == null ? effdt : effectiveDate);
    	
      }
      else {
    	  wk.setLaborAccountEffectiveDate(effdt);
      }
    
    String stringWageEffectiveDate = (String)data.getValue("wageEffdt");
    KDate wageEffdt = KServer.stringToDate(stringWageEffectiveDate);

    if (stringWageEffectiveDate == null || "".equalsIgnoreCase(stringWageEffectiveDate)) {
    	 Personality per = Personality.getByPersonNumber(wk.getEmpCode());        
    	 BaseWageRateSet baseWageRateSet = per.getBaseWageRateSet();
    	 BaseWageRate currentBaseWageRate = baseWageRateSet.getCurrentBaseWageRate();
    	 KDate wageEffectiveDate = currentBaseWageRate.getEffectiveDate();
    	 wk.getWage().setWageEffectiveDate(wageEffdt);
    	
      }
      else {
    	  wk.getWage().setWageEffectiveDate(wageEffdt);
      }
    
    FormFile formFile = data.getFormFile();
    try {
      byte[] imageData = formFile.getFileData();
      wk.setImageContent(EmpPhotoImage.getEncodedImage(imageData));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (EmpPhotoBusinessValidationException epbve) {
      epbve.printStackTrace();
    }
    
    Map<String, String> customFields = new HashMap();
    customFields.put("Labor Type", (String)data.getValue("supplyType"));
    //customFields.put("supplyType", (String)data.getValue("supplyId"));
    customFields.put("Department", (String)data.getValue("depId"));
    Section retrieveSection = Section.retrieveSection(new ObjectIdLong((String)data.getValue("secId")));
    customFields.put("Section", retrieveSection.getName());
    customFields.put("Employee Type", (String)data.getValue("empType"));
    
    customFields.put("VDA", (String)data.getValue("vda"));
    customFields.put("PDA", (String)data.getValue("pda"));
    customFields.put("HRA", (String)data.getValue("hra"));
    customFields.put("Conveyance", (String)data.getValue("conveyance"));
    customFields.put("Special Allowance", (String)data.getValue("specialAllowance"));
    customFields.put("Shift Allowance", (String)data.getValue("shiftAllowance"));
    customFields.put("Dust Allowance - Monthly", (String)data.getValue("dustAllowance"));
    customFields.put("Medical", (String)data.getValue("medicalAllowance"));
    customFields.put("LTA", (String)data.getValue("lta"));
    customFields.put("Education – Annual", (String)data.getValue("educationAnnual"));
    customFields.put("ESICWC", (String)data.getValue("esic"));
    customFields.put("ESIC Exempt", "on".equalsIgnoreCase((String)data.getValue("esicExempt")) ? "Yes" : "No");
    customFields.put("PF Exempt", "on".equalsIgnoreCase((String)data.getValue("pfExempt")) ? "Yes" : "No");
    customFields.put("Safety Training Date", (String)data.getValue("std"));
    
    
    customFields.put("Workorder", (String)data.getValue("workorderNum"));
    customFields.put("ItemServiceNumber", (String)data.getValue("item_serviceItem_number") == null ? "" : (String)data.getValue("item_serviceItem_number"));
   // customFields.put("ItemNumber", (String)data.getValue("itemNum") == null ? "" : (String)data.getValue("itemNum"));
   // customFields.put("ServiceLineItem", (String) data.getValue("serviceLineItem") == null ? "" :(String) data.getValue("serviceLineItem"));
    customFields.put("Gender", (String)data.getValue("gender"));
    customFields.put("prevExp", (String)data.getValue("prevExp"));
    customFields.put("prevOrg", (String)data.getValue("prevOrg"));
    customFields.put("Aadhar Number", (String)data.getValue("aadharno"));
    customFields.put("Mobile Number", (String) data.getValue("empmobileno"));
    customFields.put("Vehicle Type", (String) data.getValue("vehicleType"));
    customFields.put("Vehicle Rent", (String) data.getValue("vehicleRent"));
    customFields.put("Quarter Rent", (String) data.getValue("quarterRent"));
    customFields.put("Shift Hours", (String) data.getValue("shift"));
    wk.setCustomFields(customFields);
    
    return wk;
  }
 
  
  
  private Address getAddress(ObjectIdLong addressId, String village, String taluka, String district, String stateId) {
    Address addr = null;
    

    if ("".equals(village)) {
      village = " ";
    }
    if ("".equals(taluka)) {
      taluka = " ";
    }
    if ("".equals(district)) {
      district = " ";
    }
    try {
      ObjectIdLong stateIdLong = new ObjectIdLong(stateId);
      if (CMSState.DEFAULT_STATE_ID.equals(stateIdLong)) {
        addr = new Address(Address.DEFAULT_ADDRESS_ID, "", taluka, district, village, stateIdLong);
      } else {
        addr = new Address(addressId, "", taluka, district, village, stateIdLong);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return addr;
  }
  
  private WorkmenDetail getDetail(DynamicMapBackedForm data) {
    String id = (String)data.getValue("id");
    String pfAcctNo = (String)data.getValue("pfaccno");
    String gender = (String)data.getValue("gender");
    String prevExp = (String)data.getValue("prevExp");
    String prevOrg = (String)data.getValue("prevOrg");
    KDate doj = KServer.stringToDate((String)data.getValue("doj"));
    String string_dot = (String)data.getValue("dot");
    KDate dot = null;
    if(string_dot != null && !"".equalsIgnoreCase(string_dot)){
    
    try {
      dot = KServer.stringToDate(string_dot);
    }
    catch (Exception localException) { 
    	logError(localException);
    }
    }
    
    ObjectIdLong personDetlId;
    if ((id == null) || (id.equalsIgnoreCase("id_new"))) {
      personDetlId = null;
    } else {
      personDetlId = new ObjectIdLong((String)data.getValue("persondeid"));
    }
    String aadharNo = (String)data.getValue("aadharno");
    Boolean mStatus = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("marital")));
    String wifeNm = (String)data.getValue("wifeName");
    String panno = (String)data.getValue("panno");
    Boolean hazard = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("hazard")));
    Integer noOfChildren = null;
    try {
      noOfChildren = Integer.valueOf((String)data.getValue("noofChildren"));
    }
    catch (NumberFormatException localNumberFormatException) {}
    

    String prof1 = (String)data.getValue("proflic1");
    String prof3 = (String)data.getValue("proflic3");
    String technical = (String)data.getValue("technical");
    String prof2 = (String)data.getValue("proflic2");
    String academic = (String)data.getValue("academic");
    String prof4 = (String)data.getValue("proflic4");
    ObjectIdLong presentContractor = new ObjectIdLong((String)data.getValue("contrId"));
    String previousEmployer = (String)data.getValue("prevEmployer");
    String referredBy = (String)data.getValue("referedBy");
    String string_amcChecck = (String)data.getValue("amccheckup");
    KDate amcCheck = null;
    if(string_dot != null && !"".equalsIgnoreCase(string_dot)){
    try {
      amcCheck = KServer.stringToDate((String)data.getValue("amccheckup"));
    }
    catch (KServerException localKServerException) {
    	logError(localKServerException);
    	}
    }
    

    String size = (String)data.getValue("shoesize");
    Integer shoesize = null;
    try {
      shoesize = new Integer(size);
    }
    catch (NumberFormatException localNumberFormatException1) {}
    

    String winCoName = (String)data.getValue("relativeName");
    String bloodgrp = (String)data.getValue("bloodgroup");
    Boolean winCoRelative = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("isRelativeWIN")));
    String wincoAddress = (String)data.getValue("relativeAddr");
    String mobileNo = (String)data.getValue("mobileno");
    Boolean landLoser = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("landloser")));
    String surveyNo = (String)data.getValue("surveyno");
    String village = (String)data.getValue("village");
    String nameofSeller = (String)data.getValue("nameofseller");
    String extent = (String)data.getValue("extent");
    String relnWithSeller = (String)data.getValue("relationWithSeller");
    String acctNo = (String)data.getValue("acctno");
    String bankBranch = (String)data.getValue("bankbranch");
    Boolean accOpenWithContractor = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("acctOpen")));
    Boolean medicalTrainingSw = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("medical")));
    Boolean skillCert = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("skillcert")));
    Boolean safetyTrainingSw = Boolean.valueOf("on".equalsIgnoreCase((String)data.getValue("safety")));
    
    WorkmenDetail detail = new WorkmenDetail(personDetlId, doj, dot, pfAcctNo, aadharNo, gender, panno, hazard, 
      mStatus, wifeNm, noOfChildren, technical, academic, prof1, prof2, prof3, prof4, previousEmployer, 
      presentContractor, referredBy, shoesize, bloodgrp, amcCheck, winCoRelative, winCoName, wincoAddress, 
      mobileNo, landLoser, surveyNo, relnWithSeller, village, nameofSeller, extent, accOpenWithContractor, 
      bankBranch, acctNo, medicalTrainingSw, safetyTrainingSw, skillCert, prevExp, prevOrg);
    
    return detail;
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try {
      String id = "id_new";
      if (getSelectedId(form) != null) {
        id = getSelectedId(form);
      } else {
        id = (String)data.getValue("id");
      }
      
      data.setValue("id", id);
      
      //Display status field only if it is existing employee
      Boolean displayStatus = false;
      
      if ((id != null) && (!id.equalsIgnoreCase("id_new"))) {
    	  displayStatus = true;
    	 request.setAttribute("displayStatus", displayStatus.toString());
    	 //data.setValue("status", status);
        setWorkmenDatatoUI(data, request);
      } else {
    	  request.setAttribute("displayStatus", displayStatus.toString());
        String unitId = (String)data.getValue("unitId");
        String contrId = (String)data.getValue("contrId");
        
        Workmen wk = new Workmen();
        wk.setContractor(Contractor.doRetrieveById(new ObjectIdLong(contrId), new ObjectIdLong(unitId)));
        setValuesToUI(data, wk, request);
      }
    }
    catch (Exception e) {
      logError(e);
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);    
      data.setValue("dob", "");
      data.setValue("doj", "");
      data.setValue("dot", "");
      data.setValue("amccheckup", "");
      data.setValue("availPStateNames", new CMSService().getStates());
      data.setValue("availPermStateNames", new CMSService().getStates());
      data.setValue("availContractors", Contractor.doRetrieveAll());
      data.setValue("availTradeNames", Trade.doRetrieveAll());
      data.setValue("availSkillNames", new CMSService().getSkills());
      data.setValue("availDeviceGroupNames", new CMSService().retrieveAllDeviceGroups());
      data.setValue("imageContent", (String)data.getValue("imageContent"));
    }
    return mapping.findForward("success");
  }
  
  private void logError(Exception e) {
    Log.log(1, e, "Error in Workmen Detail Action");
  }
  

  private static Map methodsMap = methodsMap();
}
