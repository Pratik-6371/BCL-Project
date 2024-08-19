package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.people.api.jobassignment.APIJobAssignmentBean;
import com.kronos.wfc.commonapp.people.api.jobassignment.APIJobAssignmentDetailsBean;
import com.kronos.wfc.commonapp.people.api.jobassignment.APIPrimaryLaborAccountBean;
import com.kronos.wfc.commonapp.people.api.person.APIAccessAssignmentBean;
import com.kronos.wfc.commonapp.people.api.person.APIEMailAddressBean;
import com.kronos.wfc.commonapp.people.api.person.APIEmploymentStatusBean;
import com.kronos.wfc.commonapp.people.api.person.APIGDAPAssignmentBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonAuthenticationTypeBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonInformationBean;
import com.kronos.wfc.commonapp.people.api.person.APIPersonLicenseTypeBean;
import com.kronos.wfc.commonapp.people.api.person.APITelephoneNumberBean;
import com.kronos.wfc.commonapp.people.api.personality.APIPersonalityBean;
import com.kronos.wfc.commonapp.people.api.user.APIUserAccountBean;
import com.kronos.wfc.commonapp.people.api.user.APIUserBean;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.types.business.ContactType;
import com.kronos.wfc.commonapp.types.business.EmploymentStatusType;
import com.kronos.wfc.platform.logging.framework.Log;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.xml.api.bean.APIBeanList;
import com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean;
import com.kronos.wfc.platform.xml.api.bean.ParameterMap;


public class ManagerService
{
  public static final String INSERT = "insert";
  public static final String UPDATE = "update";
  
  public ManagerService() {}
  
  public ObjectIdLong updatePersonData(Manager manager, String action)
  {
    APIPersonIdentityBean bean = new APIPersonIdentityBean();
    bean.setPersonNumber(manager.getId());
    APIPersonBean person = new APIPersonBean();
    person.setLastName(manager.getName());
    person.setPersonNumber(manager.getId());
    person.setHireDate(KDate.today());
    
    APIPersonInformationBean info = new APIPersonInformationBean();
    info.setPersonData(person);
    
    APIPersonalityBean pBean = new APIPersonalityBean();
    pBean.setIdentity(bean);
    

    APIBeanList emailList = new APIBeanList();
    APIEMailAddressBean emailBean = new APIEMailAddressBean();
    emailBean.setAddress(manager.getEmailAddr());
    emailBean.setContactTypeName(ContactType.WORK);
    emailList.add(emailBean);
    info.setEMailAddresses(emailList);
    

    APIBeanList phoneList = new APIBeanList();
    APITelephoneNumberBean phoneBean = new APITelephoneNumberBean();
    phoneBean.setContactTypeName(ContactType.PHONE_1);
    phoneBean.setPhoneNumber(manager.getMobilenum());
    phoneList.add(phoneBean);
    info.setTelephoneNumbers(phoneList);
    
    APIJobAssignmentDetailsBean jBean = new APIJobAssignmentDetailsBean();
    APIJobAssignmentBean jA = new APIJobAssignmentBean();
    APIBeanList statusList = new APIBeanList();
    APIBeanList gdapAssignments = null;
    
    if ((action != null) && ("insert".equals(action)))
    {

      APIPersonLicenseTypeBean managerLicense = new APIPersonLicenseTypeBean();
      
      managerLicense.setActiveFlag("true");
      
      managerLicense.setLicenseTypeName("Workforce_Timekeeper_Employee");
      boolean isValid = managerLicense.isValid();
      APIBeanList licenses = new APIBeanList();
      licenses.add(managerLicense);
      info.setPersonLicenseTypes(licenses);
      boolean isInfoValid = info.isValid();
      
      APIAccessAssignmentBean accessAssignmentBean = new APIAccessAssignmentBean();
      accessAssignmentBean.setAccessProfileName(KronosProperties.getProperty("cms.manager.fap", "Payroll Manager"));
      accessAssignmentBean.setManagerAccessSetName(KronosProperties.getProperty("cms.manager.employeeGroup", "All Labor Accounts"));
      accessAssignmentBean.setPreferenceProfileName(KronosProperties.getProperty("cms.manager.displayProfile", "Default"));
      accessAssignmentBean.setReportName(KronosProperties.getProperty("cms.manager.reportProfile", "All Reports"));
      info.setAccessAssignmentData(accessAssignmentBean);
      
      APIBeanList personAuthenticationTypes = new APIBeanList();
      APIPersonAuthenticationTypeBean personAuthenticationTypeBean = new APIPersonAuthenticationTypeBean();
      personAuthenticationTypeBean.setActiveFlag(Boolean.valueOf(true));
      personAuthenticationTypeBean.setAuthenticationTypeName("Kronos");
      personAuthenticationTypes.add(personAuthenticationTypeBean);
      info.setPersonAuthenticationTypes(personAuthenticationTypes);
      

      APIEmploymentStatusBean status = new APIEmploymentStatusBean();
      status.setEmploymentStatusName(EmploymentStatusType.ACTIVE);
      status.setEffectiveDate(KDate.today());
      status.setIdentity(bean);
      statusList.add(status);
      

      APIPrimaryLaborAccountBean account = new APIPrimaryLaborAccountBean();
      Department department = new CMSService().getDepartmentByDepartmentId(manager.getDeptId());
      PrincipalEmployee pEmp = PrincipalEmployee.doRetrieveById(department.getUnitId());
      String unitCode = pEmp.getUnitCode();
      account.setLaborAccountName(KronosProperties.getProperty("cms.company.name", "Company Name") + "/" + 
        unitCode + "/" + 
        getDepartment(manager.getDeptId()) + "/" + 
        getSection(manager.getSectionId()) + "/" + 
        "-" + "/" + 
        "-" + "/" + 
        "-");
      account.setEffectiveDate(KDate.today());
      account.setExpirationDate(KDate.getEotDate());
      account.setIdentity(bean);
      
      APIBeanList accounts = new APIBeanList();
      accounts.add(account);
      jA.setPrimaryLaborAccounts(accounts);
      
      jBean.setPayRuleName(unitCode + "-" + KronosProperties.getProperty("cms.employee.payrule", "PR_8hr_shift"));
      jBean.setPayRuleEffectiveDate(KDate.today());
      
      APIUserBean userBean = new APIUserBean();
      APIUserAccountBean userAccountBean = new APIUserAccountBean();
      userAccountBean.setUserName(manager.getUserName());
      userAccountBean.setUserPassword(manager.getPasswd());
      userAccountBean.setPasswordUpdateFlag(Boolean.valueOf(true));
      userBean.setUserAccountData(userAccountBean);
      userBean.setIdentity(bean);
      pBean.setUserData(userBean);
      
      gdapAssignments = new APIBeanList();
      APIGDAPAssignmentBean gdapAssignmentBean = new APIGDAPAssignmentBean();
      gdapAssignmentBean.setGDAPName(KronosProperties.getProperty("cms.manager.gdap", "ALL_DATA_ACCESS_GROUP"));
      gdapAssignmentBean.setRole("MANAGER_ROLE");
      gdapAssignmentBean.setEffectiveDate(KDate.today());
      gdapAssignmentBean.setExpirationDate(KDate.getEotDate());
      gdapAssignmentBean.setDefaultSwitch(new Boolean(true));
      gdapAssignmentBean.setIdentity(bean);
      gdapAssignments.add(gdapAssignmentBean);
    }
    

    KDate dot = manager.getDot();
    if ((dot != null) && (!dot.isNull()) && (!KDate.getEotDate().equals(dot))) {
      APIEmploymentStatusBean terminationStatus = new APIEmploymentStatusBean();
      terminationStatus.setEmploymentStatusName(EmploymentStatusType.TERMINATED);
      terminationStatus.setEffectiveDate(dot);
      terminationStatus.setIdentity(bean);
      statusList.add(terminationStatus);
    }
    info.setEmploymentStatusList(statusList);
    
    jA.setIdentity(bean);
    jA.setJobAssignmentDetailsData(jBean);
    
    pBean.setJobAssignmentData(jA);
    pBean.setPersonInformationData(info);
    pBean.setGDAPAssignments(gdapAssignments);
    
    Log.log(4, "Before Update: " + pBean.toString());
    ParameterMap parameters = new ParameterMap();
    pBean.doAction("update", parameters);
    Personality p = pBean.getPersonality();
    Log.log(4, "After Update: " + pBean.toString());
    
    return p.getPersonId();
  }
  





  private String getSection(ObjectIdLong sectionId)
  {
    if (sectionId != null) {
      Section section = new CMSService().getSectionById(sectionId);
      if (section != null) {
        return section.getCode();
      }
    }
    return null;
  }
  





  private String getDepartment(ObjectIdLong deptId)
  {
    if (deptId != null) {
      Department department = new CMSService().getDepartmentByDepartmentId(deptId);
      if (department != null) {
        return department.getCode();
      }
    }
    return null;
  }
}
