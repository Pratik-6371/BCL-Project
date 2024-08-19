package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.api.jobassignment.APIEmploymentTermBean;
import com.kronos.wfc.commonapp.people.api.jobassignment.APIJobAssignmentBean;
import com.kronos.wfc.commonapp.people.api.jobassignment.APIJobAssignmentDetailsBean;
import com.kronos.wfc.commonapp.people.api.jobassignment.APIPrimaryLaborAccountBean;
import com.kronos.wfc.commonapp.people.api.person.APIBadgeAssignmentBean;
import com.kronos.wfc.commonapp.people.api.person.APIBaseWageRateBean;
import com.kronos.wfc.commonapp.people.api.person.APICustomDataBean;
import com.kronos.wfc.commonapp.people.api.person.APIEmploymentStatusBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonInformationBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonLicenseTypeBean;
import com.kronos.wfc.commonapp.people.api.person.APIPostalAddressBean;
import com.kronos.wfc.commonapp.people.api.person.APITelephoneNumberBean;
import com.kronos.wfc.commonapp.people.api.personality.APIPersonalityBean;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.person.PersonStatus;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.types.business.DeviceGroup;
import com.kronos.wfc.commonapp.types.business.EmploymentStatusType;
import com.kronos.wfc.datacollection.empphoto.api.APIEmpPhotoBean;
import com.kronos.wfc.integration.business.LookupTable;
import com.kronos.wfc.platform.logging.framework.Log;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.xml.api.bean.APIBeanList;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import com.kronos.wfc.platform.xml.api.bean.ParameterMap;
import java.util.Map;

public class WorkmenService
{
  public static final String INSERT = "insert";
  public static final String UPDATE = "update";
  public static final String DELETE = "delete";
  public static final String REHIRE = "rehire";
  
  public WorkmenService() {}
  
  public ObjectIdLong updatePersonData(Workmen workmen, String action)
  {
    Person originalPerson = workmen.getPerson();
    APIPersonIdentityBean bean = new APIPersonIdentityBean();
    
    if(workmen.getDetail().getDot()==null)
    {
    	APIPersonBean person = new APIPersonBean();
        person.setFirstName(workmen.getFirstName());
        person.setLastName(workmen.getLastName());
        person.setBirthDate(workmen.getDOB());
        person.setPersonNumber(workmen.getEmpCode());
        person.setHireDate(workmen.getDetail().getDoj());
        

        APIBadgeAssignmentBean badge = new APIBadgeAssignmentBean();
        badge.setBadgeNumber(workmen.getBadgeNum());
        badge.setEffectiveDate(KDate.today());
        //badge.setExpirationDate(KDate.getEotDate());
        badge.setIdentity(bean);
        
        APIBeanList badgeList = new APIBeanList();
        badgeList.add(badge);
        
        APIPostalAddressBean postalBean = new APIPostalAddressBean();
        Address address = workmen.getPresentAddress();
        if (address != null) {
          postalBean.setStreet(workmen.getPresentAddress().getVillage() + "\n" + 
            workmen.getPresentAddress().getTaluka() + "\n" + workmen.getPresentAddress().getDistrict());
          
          postalBean.setState(workmen.getPresentAddress().getStateNm());
          postalBean.setCountry("India");
          postalBean.setContactTypeName("Home");
        } else {
          postalBean.setStreet("");
          postalBean.setState("");
          postalBean.setContactTypeName("Home");
        }
        
        APITelephoneNumberBean apiTelephoneNumberBean = new APITelephoneNumberBean();
        
        apiTelephoneNumberBean.setContactTypeName("Phone 1");
        apiTelephoneNumberBean.setPhoneNumber(workmen.getCustomFields().get("Mobile Number"));
        
        APIBaseWageRateBean baseWageRateBean = new APIBaseWageRateBean();
        baseWageRateBean.setBaseWageRate(workmen.getWage().getBasic());
      /*  String basic_string = workmen.getWage().getBasic();
        int basic_int = Integer.parseInt(basic_string)/8;
        String basic = String.valueOf(basic_int);
        baseWageRateBean.setBaseWageRate(basic);*/
        baseWageRateBean.setEffectiveDate(workmen.getWage().getWageEffectiveDate());
        //baseWageRateBean.setExpirationDate(KDate.getEotDate());
        
        APIBeanList list = new APIBeanList();
        list.add(postalBean);
        APIBeanList telephoneNumberList = new APIBeanList();
        telephoneNumberList.add(apiTelephoneNumberBean);
        APIPersonInformationBean info = new APIPersonInformationBean();
        info.setPersonData(person);
        info.setPostalAddresses(list);
        info.setBadgeAssignments(badgeList);
        info.setTelephoneNumbers(telephoneNumberList);
        
        APIPersonalityBean pBean = new APIPersonalityBean();
        pBean.setPersonInformationData(info);
        
        APIJobAssignmentDetailsBean jBean = new APIJobAssignmentDetailsBean();
        APIJobAssignmentBean jA = new APIJobAssignmentBean();
        APIBeanList statusList = new APIBeanList();
        APIBeanList customDataList = new APIBeanList();
        APIBeanList baseWageList = new APIBeanList();
        baseWageList.add(baseWageRateBean);
        jA.setBaseWageRates(baseWageList);

        APICustomDataBean laborType = new APICustomDataBean();
        laborType.setCustomDataTypeName("Labor Type");
        laborType.setText(workmen.getCustomFields().get("Labor Type"));
        customDataList.add(laborType);
        
        APICustomDataBean empType = new APICustomDataBean();
        empType.setCustomDataTypeName("Employee Type");
        empType.setText(workmen.getCustomFields().get("Employee Type"));
        customDataList.add(empType);
        
        APICustomDataBean vda = new APICustomDataBean();
        vda.setCustomDataTypeName("VDA");
        vda.setText(workmen.getCustomFields().get("VDA"));
        customDataList.add(vda);
        
        APICustomDataBean pda = new APICustomDataBean();
        pda.setCustomDataTypeName("PDA");
        pda.setText(workmen.getCustomFields().get("PDA"));
        customDataList.add(pda);
        
        APICustomDataBean hra = new APICustomDataBean();
        hra.setCustomDataTypeName("HRA");
        hra.setText(workmen.getCustomFields().get("HRA"));
        customDataList.add(hra);
        
        APICustomDataBean conveyance = new APICustomDataBean();
        conveyance.setCustomDataTypeName("Conveyance");
        conveyance.setText(workmen.getCustomFields().get("Conveyance"));
        customDataList.add(conveyance);
        
        APICustomDataBean specialAllowance = new APICustomDataBean();
        specialAllowance.setCustomDataTypeName("Special Allowance");
        specialAllowance.setText(workmen.getCustomFields().get("Special Allowance"));
        customDataList.add(specialAllowance);
        
        APICustomDataBean shiftAllowance = new APICustomDataBean();
        shiftAllowance.setCustomDataTypeName("Shift Allowance");
        shiftAllowance.setText(workmen.getCustomFields().get("Shift Allowance"));
        customDataList.add(shiftAllowance);
        
        APICustomDataBean dustAllowance = new APICustomDataBean();
        dustAllowance.setCustomDataTypeName("Dust Allowance - Monthly");
        dustAllowance.setText(workmen.getCustomFields().get("Dust Allowance - Monthly"));
        customDataList.add(dustAllowance);
        
        APICustomDataBean medicalAllowance = new APICustomDataBean();
        medicalAllowance.setCustomDataTypeName("Medical");
        medicalAllowance.setText(workmen.getCustomFields().get("Medical"));
        customDataList.add(medicalAllowance);
        
        APICustomDataBean lta = new APICustomDataBean();
        lta.setCustomDataTypeName("LTA");
        lta.setText(workmen.getCustomFields().get("LTA"));
        customDataList.add(lta);
        
        APICustomDataBean educationAllowance = new APICustomDataBean();
        educationAllowance.setCustomDataTypeName("Education – Annual");
        educationAllowance.setText(workmen.getCustomFields().get("Education – Annual"));
        customDataList.add(educationAllowance);
        
        APICustomDataBean esicwc = new APICustomDataBean();
        esicwc.setCustomDataTypeName("ESICWC");
        esicwc.setText(workmen.getCustomFields().get("ESICWC"));
        customDataList.add(esicwc);
        
        APICustomDataBean isESIC = new APICustomDataBean();
        isESIC.setCustomDataTypeName("ESIC Exempt");
        isESIC.setText(workmen.getCustomFields().get("ESIC Exempt"));
        customDataList.add(isESIC);
        
        APICustomDataBean isPF = new APICustomDataBean();
        isPF.setCustomDataTypeName("PF Exempt");
        isPF.setText(workmen.getCustomFields().get("PF Exempt"));
        customDataList.add(isPF);
        
        APICustomDataBean safetyTrngDate = new APICustomDataBean();
        safetyTrngDate.setCustomDataTypeName("Safety Training Date");
        safetyTrngDate.setText(workmen.getCustomFields().get("Safety Training Date"));
        customDataList.add(safetyTrngDate);
        
        APICustomDataBean gender = new APICustomDataBean();
        gender.setCustomDataTypeName("Gender");
        gender.setText(workmen.getCustomFields().get("Gender"));
        customDataList.add(gender);
        
        APICustomDataBean aadharNumber = new APICustomDataBean();
        aadharNumber.setCustomDataTypeName("Aadhar Number");
        aadharNumber.setText(workmen.getCustomFields().get("Aadhar Number"));
        customDataList.add(aadharNumber);

        APICustomDataBean empMobileNumber = new APICustomDataBean();
        empMobileNumber.setCustomDataTypeName("Mobile Number");
        empMobileNumber.setText(workmen.getCustomFields().get("Mobile Number"));
        customDataList.add(empMobileNumber);
        
        APICustomDataBean vehicleType = new APICustomDataBean();
        vehicleType.setCustomDataTypeName("Vehicle Type");
        vehicleType.setText(workmen.getCustomFields().get("Vehicle Type"));
        customDataList.add(vehicleType);
        
        APICustomDataBean vehicleRent = new APICustomDataBean();
        vehicleRent.setCustomDataTypeName("Vehicle Rent");
        vehicleRent.setText(workmen.getCustomFields().get("Vehicle Rent"));
        customDataList.add(vehicleRent);
        
        APICustomDataBean quarterRent = new APICustomDataBean();
        quarterRent.setCustomDataTypeName("Quarter Rent");
        quarterRent.setText(workmen.getCustomFields().get("Quarter Rent"));
        customDataList.add(quarterRent);
        
        String unit_empcode = workmen.getEmpCode();
        String empCode = unit_empcode.substring(4);
        
        APICustomDataBean empcode = new APICustomDataBean();
        empcode.setCustomDataTypeName("Employee Code");
        empcode.setText(empCode);
        customDataList.add(empcode);
        
        APICustomDataBean shift = new APICustomDataBean();
        shift.setCustomDataTypeName("Shift Hours");
        shift.setText(workmen.getCustomFields().get("Shift Hours"));
        customDataList.add(shift);
        
        String department = (String)workmen.getCustomFields().get("Department");
        String section = (String)workmen.getCustomFields().get("Section");
        String workorder =(String) workmen.getCustomFields().get("Workorder");
        String item_serviceItem_number = (String) workmen.getCustomFields().get("ItemServiceNumber");
        //String itemNumber = (String) workmen.getCustomFields().get("ItemNumber");
        //String serviceLineItem =(String) workmen.getCustomFields().get("ServiceLineItem");
        String transferLev6 = workorder + "-" + item_serviceItem_number;
        
        PrincipalEmployee pEmp = PrincipalEmployee.doRetrieveById(workmen.getContractor().getUnitId());
        String unitCode = pEmp.getUnitCode();
        APIPrimaryLaborAccountBean account = new APIPrimaryLaborAccountBean();
        if("-".equalsIgnoreCase(workorder)){
        	
        	account.setLaborAccountName(pEmp.getOrganization() + "/" + unitCode + "/" + department + "/" + section + "/" + 
        			workmen.getContractor().getCcode() + "/" + workorder + "/" + "-");
        }
        else{
        	account.setLaborAccountName(pEmp.getOrganization() + "/" + unitCode + "/" + department + "/" + section + "/" + 
        			workmen.getContractor().getCcode() + "/" + transferLev6 + "/" + "-");
        }
        account.setEffectiveDate(workmen.getLaborAccountEffectiveDate());
        //account.setExpirationDate(KDate.getEotDate());
        account.setIdentity(bean);
        
        APIBeanList accounts = new APIBeanList();
        accounts.add(account);
        jA.setPrimaryLaborAccounts(accounts);
        
        if ((action != null) && ("insert".equals(action)))
        {

          APIPersonLicenseTypeBean license = new APIPersonLicenseTypeBean();
          license.setLicenseTypeName("Workforce_Timekeeper_Employee");
          license.setActiveFlag("true");
          
          APIBeanList licenses = new APIBeanList();
          licenses.add(license);
          
          info.setPersonLicenseTypes(licenses);
          

          APIEmploymentStatusBean status = new APIEmploymentStatusBean();
          status.setEmploymentStatusName(EmploymentStatusType.ACTIVE);
          status.setEffectiveDate(workmen.getDetail().getDoj());
          status.setIdentity(bean);
          statusList.add(status);
          



          boolean addEmploymentTerm = false;
          String employmentTermSettings = KronosProperties.getProperty("cms.employee.employmentterm.add", "false");
          addEmploymentTerm = Boolean.valueOf(employmentTermSettings).booleanValue();
          if (addEmploymentTerm) {
            APIEmploymentTermBean empTermBean = new APIEmploymentTermBean();
            empTermBean.setName(unitCode + " " + 
              KronosProperties.getProperty("cms.employee.employmentterm", "Employment Term"));
            empTermBean.setStartDate(workmen.getDetail().getDoj());
            empTermBean.setEndDate(KDate.getEotDate());
            APIBeanList eAssigns = new APIBeanList();
            eAssigns.add(empTermBean);
            jA.setEmploymentTermAssignments(eAssigns);
          }
        }
        
        LookupTable lp = LookupTable.retrieve("WORKMEN_PAYRULE_ASSIGNMENT.TBL");
        String lpdata = lp.getTableContents();
        String[] arraylpdata = lpdata.split("\n");
        String payRule = "";
        
        String supplyType = workmen.getCustomFields().get("Labor Type");
        String labor_type="BSR";
        
        if("Supply".equalsIgnoreCase(supplyType) || "Temporary Supply".equalsIgnoreCase(supplyType))
        {
        	labor_type="LSR";
        }
        String employeeType = workmen.getCustomFields().get("Employee Type");
        String employee_type="O";
        if("WB".equalsIgnoreCase(employeeType))
        {
        	employee_type="W";
        }
        String shiftHrs = workmen.getCustomFields().get("Shift Hours");
        String shift_hrs = "8";
        if("12".equalsIgnoreCase(shiftHrs)){
        	shift_hrs = "12";
        }
        
        LookupTable lp_unit_contractor = LookupTable.retrieve("PAYPERIOD.TBL");
        String lpdata_unit_contractor = lp_unit_contractor.getTableContents();
        String[] arraylpdata_unit_contractor = lpdata_unit_contractor.split("\n");
        String contractor_code = workmen.getContractor().getCcode();
        String unit_contractor = unitCode + "~" + contractor_code;
        String unitCode_contractorCode = "";
        for (int q = 1; q < arraylpdata_unit_contractor.length; q++)
        {
          String ald = arraylpdata_unit_contractor[q];
          String[] column = ald.split(",");
          if (unit_contractor.equalsIgnoreCase(column[0].toString()))
          {
        	  unitCode_contractorCode = column[0];
            break;
          }
        }
        
        String lookupValue = "";
        
        if(unitCode_contractorCode != null && !"".equalsIgnoreCase(unitCode_contractorCode))
        {
        	lookupValue  = unitCode + '~' + employee_type + '~' + labor_type + '~' + 
        			shift_hrs + '~'+ contractor_code + '~' + '*';
        }
        else{
        	lookupValue  = unitCode + '~' + employee_type + '~' + labor_type + '~' + shift_hrs + '~'+'*';
        }
        for (int q = 1; q < arraylpdata.length; q++)
        {
          String ald = arraylpdata[q];
          String[] column = ald.split(",");
          if (lookupValue.equalsIgnoreCase(column[0].toString()))
          {
            payRule = column[1];
            break;
          }
        }
        
        jBean.setPayRuleName(payRule);
        jBean.setPayRuleEffectiveDate(action == "insert" ? workmen.getDetail().getDoj() : workmen.getLaborAccountEffectiveDate());
        
        
        DeviceGroup dg = workmen.getDeviceGroup();
        if (dg != null) {
          jBean.setDeviceGroupName(dg.getName());
        } else {
          jBean.setDeviceGroupName(null);
        }
        
        APIEmpPhotoBean photoBean = null;
        if (workmen.getImageContent() != null) {
          photoBean = new APIEmpPhotoBean();
          photoBean.setEmployee(bean);
          photoBean.setImageContent(workmen.getImageContent());
          photoBean.setCaptureDateDay(new KDate());
          photoBean.setCaptureDateTime(new com.kronos.wfc.platform.utility.framework.datetime.KTime());
        }
        

        /*KDate dot = workmen.getDetail().getDot();
		if (dot != null && !dot.isNull() && !KDate.getEotDate().equals(dot)) {
			APIEmploymentStatusBean terminationStatus = new APIEmploymentStatusBean();
			terminationStatus.setEmploymentStatusName(EmploymentStatusType.TERMINATED);
			terminationStatus.setEffectiveDate(dot);
			terminationStatus.setIdentity(bean);
			statusList.add(terminationStatus);
		}*/
        
        info.setEmploymentStatusList(statusList);
        info.setCustomDataList(customDataList);
        
        jA.setIdentity(bean);
        jA.setJobAssignmentDetailsData(jBean);
        
        pBean.setJobAssignmentData(jA);
        pBean.setPersonInformationData(info);
        
        Log.log(4, "Before Update: " + pBean.toString());
        ParameterMap parameters = new ParameterMap();
        
        if ((action != null) && ("insert".equals(action))) {
          pBean.doAction("Update", parameters);
        } else {
          ObjectIdLong personId = originalPerson.getPersonId();
          bean.setPersonKey(personId);
          pBean.setIdentity(bean);
          pBean.doAction("Update", parameters);
        }
        
        Personality p = pBean.getPersonality();
        Log.log(4, "After Update: " + pBean.toString());
        
        Log.log(4, "Before photo: " + pBean.toString());
        if (photoBean != null) {
          photoBean.doAction("Update", parameters);
        }
        Log.log(4, "After photo: " + pBean.toString());
        
        return p.getPersonId();

    }
    else
    {
    	APIPersonBean person = new APIPersonBean();
        APIBadgeAssignmentBean badge = new APIBadgeAssignmentBean();
        badge.setBadgeNumber(workmen.getBadgeNum());
        badge.setEffectiveDate(KDate.today());
        badge.setExpirationDate(KDate.getEotDate());
        badge.setIdentity(bean);
        
        
        APIBeanList badgeList = new APIBeanList();
        badgeList.add(badge);
  
        APIPrimaryLaborAccountBean account = new APIPrimaryLaborAccountBean();
        account.setEffectiveDate(workmen.getLaborAccountEffectiveDate());
        account.setExpirationDate(KDate.getEotDate());
        account.setIdentity(bean);
        
        APIBeanList accounts = new APIBeanList();
        accounts.add(account);
        
        APIPersonalityBean pBean = new APIPersonalityBean();
        APIJobAssignmentDetailsBean jBean = new APIJobAssignmentDetailsBean();
        APIJobAssignmentBean jA = new APIJobAssignmentBean();
        APIBeanList statusList = new APIBeanList();
        APIPersonInformationBean info = new APIPersonInformationBean();
        
        /*KDate dot = workmen.getDetail().getDot();
        if ((dot != null) && (!dot.isNull()) && (!KDate.getEotDate().equals(dot))) {
          APIEmploymentStatusBean terminationStatus = new APIEmploymentStatusBean();
          terminationStatus.setEmploymentStatusName(EmploymentStatusType.TERMINATED);
          terminationStatus.setEffectiveDate(dot);
          terminationStatus.setIdentity(bean);
          statusList.add(terminationStatus);
        }*/
        
        KDate dot = workmen.getDetail().getDot();
		if (((dot != null) && !(dot.isNull())) && !KDate.getEotDate().equals(dot)) {
			APIEmploymentStatusBean terminationStatus = new APIEmploymentStatusBean();
			terminationStatus.setEmploymentStatusName(EmploymentStatusType.TERMINATED);
			terminationStatus.setEffectiveDate(dot);
			terminationStatus.setIdentity(bean);
			statusList.add(terminationStatus);
		}
        info.setEmploymentStatusList(statusList);
        //info.setCustomDataList(customDataList);
        
        jA.setIdentity(bean);
        jA.setJobAssignmentDetailsData(jBean);
        
        pBean.setJobAssignmentData(jA);
        pBean.setPersonInformationData(info);
        
        Log.log(4, "Before Update: " + pBean.toString());
        ParameterMap parameters = new ParameterMap();
        
        if ((action != null) && ("insert".equals(action))) {
          pBean.doAction("Update", parameters);
        } else {
          ObjectIdLong personId = originalPerson.getPersonId();
          bean.setPersonKey(personId);
          pBean.setIdentity(bean);
          pBean.doAction("Update", parameters);
        }
        
        Personality p = pBean.getPersonality();
        return p.getPersonId();
    }
  
  
  }

  

  public ObjectIdLong rehirePerson(Workmen workmen, String action) {
      Person originalPerson = workmen.getPerson();
      Personality per = Personality.getByPersonId(originalPerson.getPersonId());
      PersonStatus currentStatus = per.getCurrentStatus();
      KDate statusEffectiveDate = currentStatus.getEffectiveDate();
      KDate statusEffectiveDate_plus1 = statusEffectiveDate.plusDays(1);
      APIPersonIdentityBean bean = new APIPersonIdentityBean();
      APIPersonBean person = new APIPersonBean();
      person.setFirstName(workmen.getFirstName());
      person.setLastName(workmen.getLastName());
      person.setBirthDate(workmen.getDOB());
      person.setPersonNumber(workmen.getEmpCode());
      person.setHireDate(workmen.getDetail().getDoj());
      APITelephoneNumberBean apiTelephoneNumberBean = new APITelephoneNumberBean();
      apiTelephoneNumberBean.setContactTypeName("Phone 1");
      apiTelephoneNumberBean.setPhoneNumber(workmen.getCustomFields().get("Mobile Number"));
      APIBaseWageRateBean baseWageRateBean = new APIBaseWageRateBean();
      baseWageRateBean.setBaseWageRate(workmen.getWage().getBasic());
      baseWageRateBean.setEffectiveDate(workmen.getWage().getWageEffectiveDate());
      baseWageRateBean.setExpirationDate(KDate.getEotDate());
      APIBeanList telephoneNumberList = new APIBeanList();
      telephoneNumberList.add(apiTelephoneNumberBean);
      APIPersonInformationBean info = new APIPersonInformationBean();
      info.setPersonData(person);
      info.setTelephoneNumbers(telephoneNumberList);
      APIPersonalityBean pBean = new APIPersonalityBean();
      pBean.setPersonInformationData(info);
      APIJobAssignmentDetailsBean jBean = new APIJobAssignmentDetailsBean();
      APIJobAssignmentBean jA = new APIJobAssignmentBean();
      APIBeanList statusList = new APIBeanList();
      APIBeanList customDataList = new APIBeanList();
      APIBeanList baseWageList = new APIBeanList();
      baseWageList.add(baseWageRateBean);
      jA.setBaseWageRates(baseWageList);
      APIPersonLicenseTypeBean license = new APIPersonLicenseTypeBean();
      license.setLicenseTypeName("Workforce_Timekeeper_Employee");
      license.setActiveFlag("true");
      APIBeanList licenses = new APIBeanList();
      licenses.add(license);
      info.setPersonLicenseTypes(licenses);
      APIEmploymentStatusBean status = new APIEmploymentStatusBean();
      status.setEmploymentStatusName(EmploymentStatusType.ACTIVE);
      status.setEffectiveDate(statusEffectiveDate_plus1);
      status.setIdentity(bean);
      statusList.add(status);
      APICustomDataBean laborType = new APICustomDataBean();
      laborType.setCustomDataTypeName("Labor Type");
      laborType.setText(workmen.getCustomFields().get("Labor Type"));
      customDataList.add(laborType);
      APICustomDataBean empType = new APICustomDataBean();
      empType.setCustomDataTypeName("Employee Type");
      empType.setText(workmen.getCustomFields().get("Employee Type"));
      customDataList.add(empType);
      APICustomDataBean vda = new APICustomDataBean();
      vda.setCustomDataTypeName("VDA");
      vda.setText(workmen.getCustomFields().get("VDA"));
      customDataList.add(vda);
      APICustomDataBean pda = new APICustomDataBean();
      pda.setCustomDataTypeName("PDA");
      pda.setText(workmen.getCustomFields().get("PDA"));
      customDataList.add(pda);
      APICustomDataBean hra = new APICustomDataBean();
      hra.setCustomDataTypeName("HRA");
      hra.setText(workmen.getCustomFields().get("HRA"));
      customDataList.add(hra);
      APICustomDataBean conveyance = new APICustomDataBean();
      conveyance.setCustomDataTypeName("Conveyance");
      conveyance.setText(workmen.getCustomFields().get("Conveyance"));
      customDataList.add(conveyance);
      APICustomDataBean specialAllowance = new APICustomDataBean();
      specialAllowance.setCustomDataTypeName("Special Allowance");
      specialAllowance.setText(workmen.getCustomFields().get("Special Allowance"));
      customDataList.add(specialAllowance);
      APICustomDataBean shiftAllowance = new APICustomDataBean();
      shiftAllowance.setCustomDataTypeName("Shift Allowance");
      shiftAllowance.setText(workmen.getCustomFields().get("Shift Allowance"));
      customDataList.add(shiftAllowance);
      APICustomDataBean dustAllowance = new APICustomDataBean();
      dustAllowance.setCustomDataTypeName("Dust Allowance - Monthly");
      dustAllowance.setText(workmen.getCustomFields().get("Dust Allowance - Monthly"));
      customDataList.add(dustAllowance);
      APICustomDataBean medicalAllowance = new APICustomDataBean();
      medicalAllowance.setCustomDataTypeName("Medical");
      medicalAllowance.setText(workmen.getCustomFields().get("Medical"));
      customDataList.add(medicalAllowance);
      APICustomDataBean lta = new APICustomDataBean();
      lta.setCustomDataTypeName("LTA");
      lta.setText(workmen.getCustomFields().get("LTA"));
      customDataList.add(lta);
      APICustomDataBean educationAllowance = new APICustomDataBean();
      educationAllowance.setCustomDataTypeName("Education \u2013 Annual");
      educationAllowance.setText(workmen.getCustomFields().get("Education \u2013 Annual"));
      customDataList.add(educationAllowance);
      APICustomDataBean esicwc = new APICustomDataBean();
      esicwc.setCustomDataTypeName("ESICWC");
      esicwc.setText(workmen.getCustomFields().get("ESICWC"));
      customDataList.add(esicwc);
      APICustomDataBean isESIC = new APICustomDataBean();
      isESIC.setCustomDataTypeName("ESIC Exempt");
      isESIC.setText(workmen.getCustomFields().get("ESIC Exempt"));
      customDataList.add(isESIC);
      APICustomDataBean isPF = new APICustomDataBean();
      isPF.setCustomDataTypeName("PF Exempt");
      isPF.setText(workmen.getCustomFields().get("PF Exempt"));
      APICustomDataBean shift = new APICustomDataBean();
      shift.setCustomDataTypeName("Shift Hours");
      shift.setText(workmen.getCustomFields().get("Shift Hours"));
      customDataList.add(shift);
      String department = workmen.getCustomFields().get("Department");
      String section = workmen.getCustomFields().get("Section");
      String workorder = workmen.getCustomFields().get("Workorder");
      String item_serviceItem_number = workmen.getCustomFields().get("ItemServiceNumber");
      String transferLev6 = String.valueOf(workorder) + "-" + item_serviceItem_number;
      PrincipalEmployee pEmp = PrincipalEmployee.doRetrieveById(new ObjectIdLong(workmen.getUnitId().toString()));
      String unitCode = pEmp.getUnitCode();
      APIPrimaryLaborAccountBean account = new APIPrimaryLaborAccountBean();
      if ("-".equalsIgnoreCase(workorder)) {
          account.setLaborAccountName((String.valueOf(pEmp.getOrganization()) + "/" + unitCode + "/" + department + "/" + section + "/" + workmen.getContractor().getCcode() + "/" + workorder + "/" + "-"));
      } else {
          account.setLaborAccountName((String.valueOf(pEmp.getOrganization()) + "/" + unitCode + "/" + department + "/" + section + "/" + workmen.getContractor().getCcode() + "/" + transferLev6 + "/" + "-"));
      }
      account.setEffectiveDate(workmen.getLaborAccountEffectiveDate());
      account.setExpirationDate(KDate.getEotDate());
      account.setIdentity(bean);
      APIBeanList accounts = new APIBeanList();
      accounts.add(account);
      jA.setPrimaryLaborAccounts(accounts);
      boolean addEmploymentTerm = false;
      String employmentTermSettings = KronosProperties.getProperty("cms.employee.employmentterm.add", "false");
      addEmploymentTerm = Boolean.valueOf(employmentTermSettings);
      if (addEmploymentTerm) {
          APIEmploymentTermBean empTermBean = new APIEmploymentTermBean();
          empTermBean.setName((String.valueOf(unitCode) + " " + KronosProperties.getProperty("cms.employee.employmentterm", "Employment Term")));
          empTermBean.setStartDate(KDate.today());
          empTermBean.setEndDate(KDate.getEotDate());
          APIBeanList eAssigns = new APIBeanList();
          eAssigns.add(empTermBean);
          jA.setEmploymentTermAssignments(eAssigns);
      }
      LookupTable lp = LookupTable.retrieve("WORKMEN_PAYRULE_ASSIGNMENT.TBL");
      String lpdata = lp.getTableContents();
      String[] arraylpdata = lpdata.split("\n");
      String payRule = "";
      String supplyType = workmen.getCustomFields().get("Labor Type");
      String labor_type = "BSR";
      if ("Supply".equalsIgnoreCase(supplyType) || "Temporary Supply".equalsIgnoreCase(supplyType)) {
          labor_type = "LSR";
      }
      String employeeType = workmen.getCustomFields().get("Employee Type");
      String employee_type = "O";
      if ("WB".equalsIgnoreCase(employeeType)) {
          employee_type = "W";
      }
      String shiftHrs = workmen.getCustomFields().get("Shift Hours");
      String shift_hrs = "8";
      if ("12".equalsIgnoreCase(shiftHrs)) {
          shift_hrs = "12";
      }
      LookupTable lp_unit_contractor = LookupTable.retrieve("PAYPERIOD.TBL");
      String lpdata_unit_contractor = lp_unit_contractor.getTableContents();
      String[] arraylpdata_unit_contractor = lpdata_unit_contractor.split("\n");
      String contractor_code = workmen.getContractor().getCcode();
      String unit_contractor = String.valueOf(unitCode) + "~" + contractor_code;
      String unitCode_contractorCode = "";
      for (int q = 1; q < arraylpdata_unit_contractor.length; ++q) {
          String ald = arraylpdata_unit_contractor[q];
          String[] column = ald.split(",");
          if (!unit_contractor.equalsIgnoreCase(column[0].toString())) 
          {
        	  unitCode_contractorCode = column[0];
              break;
          }
      }
      String lookupValue = "";
      lookupValue = unitCode_contractorCode != null && !"".equalsIgnoreCase(unitCode_contractorCode) ? String.valueOf(unitCode) + '~' + employee_type + '~' + labor_type + '~' + shift_hrs + '~' + contractor_code + '~' + '*' : String.valueOf(unitCode) + '~' + employee_type + '~' + labor_type + '~' + shift_hrs + '~' + '*';
      for (int q = 1; q < arraylpdata.length; ++q) 
		{
          String ald = arraylpdata[q];
          String[] column = ald.split(",");
          if (!lookupValue.equalsIgnoreCase(column[0].toString())) 
			{
				payRule = column[1];
				break;
			}	
      }
      jBean.setPayRuleName(payRule);
      jBean.setPayRuleEffectiveDate(workmen.getLaborAccountEffectiveDate());
      KDate dot = workmen.getDetail().getDot();
      if (dot != null && !dot.isNull() && !KDate.getEotDate().equals(dot)) {
          APIEmploymentStatusBean terminationStatus = new APIEmploymentStatusBean();
          terminationStatus.setEmploymentStatusName(EmploymentStatusType.TERMINATED);
          /*terminationStatus.setEffectiveDate(dot);
          terminationStatus.setIdentity(bean);*/
          terminationStatus.setEffectiveDate(KDate.create(2999, 1, 1));
          terminationStatus.setExpirationDate(KDate.getEotDate());
          statusList.add(terminationStatus);
      }
      info.setEmploymentStatusList(statusList);
      info.setCustomDataList(customDataList);
      jA.setIdentity(bean);
      jA.setJobAssignmentDetailsData(jBean);
      pBean.setJobAssignmentData(jA);
      pBean.setPersonInformationData(info);
      Log.log(4, "Before Update: " + pBean.toString());
      ParameterMap parameters = new ParameterMap();
      ObjectIdLong personId = originalPerson.getPersonId();
      bean.setPersonKey(personId);
      pBean.setIdentity(bean);
      pBean.doAction("Update", parameters);
      Personality p = pBean.getPersonality();
      Log.log(4, "After Update: " + pBean.toString());
      return p.getPersonId();
  }


  
  
  
}

