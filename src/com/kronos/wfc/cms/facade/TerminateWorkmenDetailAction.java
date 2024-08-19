package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Address;
import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.cms.business.Department;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.cms.business.Section;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Wage;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.cms.business.WorkmenDetail;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.cms.business.WorkorderLine;
import com.kronos.wfc.commonapp.laborlevel.business.entries.LaborAccount;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.person.BaseWageRate;
import com.kronos.wfc.commonapp.people.business.person.BaseWageRateSet;
import com.kronos.wfc.commonapp.people.business.person.CustomData;
import com.kronos.wfc.commonapp.people.business.person.CustomDataSet;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.person.PersonStatus;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.types.business.DeviceGroup;
import com.kronos.wfc.datacollection.empphoto.business.EmpPhotoBusinessValidationException;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.kronos.wfc.platform.datetime.framework.KServer;

public class TerminateWorkmenDetailAction extends com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions
{
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-contractor-application";
  
  public TerminateWorkmenDetailAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.terminate.workmen.detail.return", "doReturn");
    methods.put("cms.action.terminate.workmen.detail.rehire", "doRehire");
    methods.put("cms.action.terminate.workmen.detail.refresh", "getRefreshData");
    return methods;
  }
  
  protected Map getKeyMethodMap() {
    return methodsMap();
  }
  
  void setNoFieldsSelected(HttpServletRequest request) {
    CMSException e = CMSException.noSelectionMade();
    StrutsUtils.addErrorMessage(request, e);
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

  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    request.setAttribute("unitId", (String)((DynamicMapBackedForm)form).getValue("unitId"));
    request.setAttribute("contrId", (String)((DynamicMapBackedForm)form).getValue("contrId"));
    request.setAttribute("statusId", (String)((DynamicMapBackedForm)form).getValue("statusId"));
    return mapping.findForward("doCMSWorkmenListApplication");
  }

  private void setValuesToUI(DynamicMapBackedForm data, Workmen workmen, HttpServletRequest request)
  {
    String unitId = (String)data.getValue("unitId");
    String contId = (String)data.getValue("contrId");
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
      data.setValue("dob", "");
      data.setValue("idmark", "");
      data.setValue("skillId", "-1");
      data.setValue("mobileno", "");
      data.setValue("doj", "");
      data.setValue("dot", "");
      data.setValue("amccheckup", "");
      data.setValue("supplyType", "supplyType");
      data.setValue("empTypeValue", "WB");
    } else if ((workmen != null) && (workmen.getEmpId() != null)) {
    	displayStatus=true;
    	request.setAttribute("displayStatus", displayStatus.toString());
    	 String status = (String) request.getAttribute("status");
         if(status == null || "".equalsIgnoreCase(status))
         {
       	  status=(String) data.getValue("status");
       	  if(status == null || "".equalsIgnoreCase(status))
       	  {
       		  status="Terminated";
       	  }
         }
         data.setValue("status", status);
      data.setValue("id", workmen.getEmpId().toString());
      data.setValue("eCode", workmen.getEmpCode());
      data.setValue("badgeNumber", workmen.getBadgeNum() == null ? "" : workmen.getBadgeNum());
      
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

      for (Iterator iterator = depList.iterator(); iterator.hasNext();) {
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
        }
      
      for (Iterator iterator = workorderLineList.iterator(); iterator.hasNext();) {
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
          break;
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
      /*String effDate = (String)data.getValue("effdt");
      KDate effectiveDate = KServer.stringToDate(effDate);
      workmen.setLaborAccountEffectiveDate(effectiveDate);*/
      
      
      data.setValue("effdt", workmen.getLaborAccountEffectiveDate());
      data.setValue("age", (String)data.getValue("age") == null ? "" : (String)data.getValue("age"));
      data.setValue("exp", (String)data.getValue("exp") == null ? "" : (String)data.getValue("exp"));  
      
      CustomData d = per.getCustomData().getCustomData("Labor Type");
      if ((d != null) && (d.getCustomText() != null)) {
        data.setValue("supplyType", d.getCustomText());
      }
      else {
        data.setValue("supplyType", "Supply");
      }
      CustomData empType = per.getCustomData().getCustomData("Employee Type");
      if ((empType != null) && (empType.getCustomText() != null)) {
        data.setValue("empTypeValue", empType.getCustomText());
      }
      else {
        data.setValue("empTypeValue", "WB");
      }
      CustomData esic  = per.getCustomData().getCustomData("ESICWC");
      data.setValue("esic", esic.getCustomText());

      CustomData std = per.getCustomData().getCustomData("Safety Training Date");
      data.setValue("std", std.getCustomText() == null ? "" : std.getCustomText());
      
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
      KDate dot = per.getCurrentStatus().getEffectiveDate();
      //KDate dot = workmen.getDetail().getDot();
      if ((dot != null) && (dot.isNull())) {
        data.setValue("dot", "");
      } else {
        data.setValue("dot", dot);
      }
      
      data.setValue("pfaccno", 
        workmen.getDetail().getPfAcctNo() == null ? "" : workmen.getDetail().getPfAcctNo());
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
      data.setValue("technical", 
        workmen.getDetail().getTechnical() == null ? "" : workmen.getDetail().getTechnical());
      data.setValue("academic", 
        workmen.getDetail().getAcademic() == null ? "" : workmen.getDetail().getAcademic());
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
      data.setValue("shoesize", 
        workmen.getDetail().getShoesize() == null ? "" : workmen.getDetail().getShoesize());
      data.setValue("bloodgroup", 
        workmen.getDetail().getBloodgrp() == null ? "" : workmen.getDetail().getBloodgrp());
      data.setValue("amccheckup", 
        workmen.getDetail().getAmcCheck().isNull() ? "" : workmen.getDetail().getAmcCheck());
      data.setValue("medical", 
        (workmen.getDetail().getMedicalTrainingSw() != null) && (workmen.getDetail().getMedicalTrainingSw().booleanValue()) ? 
        "on" : "off");
      data.setValue("safety", 
        (workmen.getDetail().getSafetyTrainingSw() != null) && (workmen.getDetail().getSafetyTrainingSw().booleanValue()) ? 
        "on" : "off");
      data.setValue("skillcert", 
        (workmen.getDetail().getSkillCert() != null) && (workmen.getDetail().getSkillCert().booleanValue()) ? "on" : "off");
      data.setValue("contrId", workmen.getContractor().getcontractorid().toString());
      data.setValue("imageContent", "data:image/jpeg;base64," + workmen.getImageContent());
      
      BaseWageRateSet wage = per.getBaseWageRateSet();
      BaseWageRate currentBaseWageRate = wage.getCurrentBaseWageRate();
      KDate wageEffectiveDate = currentBaseWageRate.getEffectiveDate();
      data.setValue("wageEffdt", wageEffectiveDate);
    }
    
    data.setValue("availDepartmentNames", depList);
    data.setValue("availSectionNames", secList);
    data.setValue("availContrCodes", contrList);
    
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
    request.setAttribute("employeeStatus", "3");
    if ((workmen != null) && (workmen.getImageContent() != null)) {
      data.setValue("imageContent", getImageSource(workmen.getImageContent()));
    } else {
      org.apache.struts.upload.FormFile formFile = ((ImportActionForm)data).getFormFile();
      try {
        byte[] imageData = formFile.getFileData();
        data.setValue("imageContent", getImageSource(com.kronos.wfc.datacollection.empphoto.business.EmpPhotoImage.getEncodedImage(imageData)));
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

  private List<Contractor> getContractorNames(ObjectIdLong getpContr)
  {
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
  




  String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  
  protected WorkmenFacade getFacade() {
    return new WorkmenFacadeImpl();
  }
  
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }

  public ActionForward doRehire(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    try
    {
      rehireWorkman(form, request);
      clearUiDirtyData((DynamicMapBackedForm)form);
      StrutsUtils.addMessage(request, new com.kronos.wfc.platform.i18n.framework.messages.Message( "cms.message.rehire"));
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
      } else {
        CMSException ex = CMSException.unknown(e.getLocalizedMessage());
        StrutsUtils.addErrorMessage(request, ex);
      }
    }
    
    return doReturn(mapping, form, request, response);
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
      if ((id != null) && (!id.equalsIgnoreCase("id_new"))) {
        setWorkmenDatatoUI(data, request);
      } else {
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
  
  private void rehireWorkman(ActionForm form, HttpServletRequest request)
    throws Exception
  {
    Workmen workmen = new Workmen();
    
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String unitId = (String)data.getValue("unitId");
    String employeeId = (String)data.getValue("id");
    
    try 
    {
    	
      WorkmenFacade facade = getFacade();
      workmen = facade.getWorkmen(employeeId, unitId);
      Map<String, String> customFields = new HashMap();
      customFields.put("supplyType", (String)data.getValue("supplyType"));
      customFields.put("Department", (String)data.getValue("depId"));
      Section retrieveSection = Section.retrieveSection(new ObjectIdLong((String)data.getValue("secId")));
      customFields.put("Section", retrieveSection.getName());
      customFields.put("Employee Type", (String)data.getValue("empType"));
      customFields.put("Workorder", (String)data.getValue("workorderNum"));
      customFields.put("ItemServiceNumber", (String)data.getValue("item_serviceItem_number") == null ? "" : (String)data.getValue("item_serviceItem_number"));
     // customFields.put("ItemNumber", (String)data.getValue("itemNum") == null ? "" : (String)data.getValue("itemNum"));
     // customFields.put("ServiceLineItem", (String) data.getValue("serviceLineItem") == null ? "" :(String) data.getValue("serviceLineItem"));
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
      customFields.put("Shift Hours", (String) data.getValue("shift"));
      workmen.setCustomFields(customFields);
      KDate dot = null;
      String string_dot = (String)data.getValue("dot");
      if(string_dot != null && !"".equalsIgnoreCase(string_dot)){
    	  try {
    	        dot = KServer.stringToDate(string_dot);
    	      } catch (Exception e) {
    	        //dot = KServer.stringToDate("31/12/2999");
    	      } 
      }
      
      //new requirement
      KDate doj = null;
      String string_doj = (String)data.getValue("doj");
      if(string_doj != null && !"".equalsIgnoreCase(string_doj)){
    	  try {
    	        doj = KServer.stringToDate(string_doj);
    	      } catch (Exception e) {
    	        //dot = KServer.stringToDate("31/12/2999");
    	      } 
      }
      workmen.getDetail().setDoj(doj);
      
      
      workmen.getDetail().setDot(dot);
      KDate effdt = KServer.stringToDate((String) data.getValue("effdt"));
      workmen.setLaborAccountEffectiveDate(effdt);
    /*  WorkmenDetail wd = new WorkmenDetail();
      String doj = (String)data.getValue("doj");
      wd.setDoj(doj);*/
      workmen.setUnitId(unitId);
      Skill skill = Skill.retrieveSkill(new ObjectIdLong((String)data.getValue("skillId")));
      workmen.setSkill(skill);
      Trade trade = Trade.retrieveById(new ObjectIdLong((String)data.getValue("tradeId")));
      workmen.setTrade(trade);
      String contrId = (String)data.getValue("ccode");
      workmen.setContractor(Contractor.doRetrieveById(new ObjectIdLong(contrId), new ObjectIdLong(unitId)));
      ObjectIdLong wageId = workmen.getWage().getId();
      String basic = (String)data.getValue("basic");
      String da = (String)data.getValue("da");
      String allowance = (String)data.getValue("allowance");
      String stringWageEffectiveDate = (String)data.getValue("wageEffdt");
      KDate wageEffdt = KServer.stringToDate(stringWageEffectiveDate);
      workmen.getWage().setId(wageId);
      workmen.getWage().setBasic(basic);
      workmen.getWage().setDa(da);
      workmen.getWage().setAllowance(allowance);
      workmen.getWage().setWageEffectiveDate(wageEffdt);
      workmen = facade.rehireWorkmen(workmen, unitId);
    } catch (Exception e) {
      logError(e);
      throw e;
    } /*finally {
      String id = (String)data.getValue("id");
      if (!"id_new".equalsIgnoreCase(id)) {
        workmen = Workmen.getWorkmenById(new ObjectIdLong(id), new ObjectIdLong(unitId));
      }
      //setValuesToUI((DynamicMapBackedForm)form, workmen, request);
    }*/
  }

  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  
  private void logError(Exception e) {
    com.kronos.wfc.wfp.logging.Log.log(1, e, "Error in Terminate Workmen Detail Action");
  }
  

  private static Map methodsMap = methodsMap();
  
  public ActionForward getRefreshData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		    throws Exception
		  {
	  	DynamicMapBackedForm data = (DynamicMapBackedForm)form;
	    setRefreshDataToUI(data, request);
	    return mapping.findForward("success");
		  }

  private void setRefreshDataToUI(DynamicMapBackedForm data, HttpServletRequest request) {
	
	  String Id = (String)data.getValue("id");
	    String unitId = (String)data.getValue("sunitId");
	    data.setValue("id", Id);
	    data.setValue("unitId", unitId);
	    request.setAttribute("unitId", new ObjectIdLong(unitId));
	    List<PrincipalEmployee> pes = new CMSService().getPrincipalEmployerList();
	    data.setValue("availUnitNames", pes);
	    String ccode = (String)data.getValue("ccode");
	    data.setValue("contrId", ccode);
	    //data.setValue("contrId", (String)data.getValue("contrId"));
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
	    
	    /*for (Iterator iterator = workorderLineList.iterator(); iterator.hasNext();) {
	        WorkorderLine workorderLine = (WorkorderLine)iterator.next();
	        if (((String)data.getValue("itemNum"))!=null && ((String)data.getValue("itemNum")).equalsIgnoreCase(workorderLine.getItemNumber())) {
	          workorderLineId = workorderLine.getWkLineId();
	          break;
	        }
	      }
	    
	    for (Iterator iterator = workorderLineList.iterator(); iterator.hasNext();) {
	        WorkorderLine workorderLine = (WorkorderLine)iterator.next();
	        if ( ((String)data.getValue("serviceLineItem"))!= null && ((String)data.getValue("serviceLineItem")).equalsIgnoreCase(workorderLine.getServiceLineItemNumber())) {
	          workorderLineId = workorderLine.getWkLineId();
	          break;
	        }
	      }*/
	    
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
	    data.setValue("shift", (String)data.getValue("shift"));
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
  
}
