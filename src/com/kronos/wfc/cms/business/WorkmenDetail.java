package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;




public class WorkmenDetail
{
  private ObjectIdLong personDetlId;
  private KDate doj;
  private KDate dot;
  private String pfAcctNo;
  private String aadharNo;
  private String gender;
  private String panno;
  private Boolean hazard;
  private Boolean mStatus;
  private String prevExp;
  private String prevOrg;
  public WorkmenDetail(){}
  public WorkmenDetail(ObjectIdLong personDetlId, KDate doj, KDate dot, String pfAcctNo, String aadharNo, String gender, String panno, Boolean hazard, Boolean mStatus, String wifeNm, Integer noOfChildren, String technical, String academic, String prof1, String prof2, String prof3, String prof4, String previousEmployer, ObjectIdLong presentContractor, String referredBy, Integer shoesize, String bloodgrp, KDate amcCheck, Boolean winCoRelative, String winCoName, String wincoAddress, String mobileNo, Boolean landLoser, String surveyNo, String relnWithSeller, String village, String nameofSeller, String extent, Boolean accOpenWithContractor, String bankBranch, String acctNo, Boolean medicalTrainingSw, Boolean safetyTrainingSw, Boolean skillCert, String prevExp, String prevOrg)
  {
    this.personDetlId = personDetlId;
    this.doj = doj;
    this.dot = dot;
    this.pfAcctNo = pfAcctNo;
    this.aadharNo = aadharNo;
    this.gender = gender;
    this.panno = panno;
    this.hazard = hazard;
    this.mStatus = mStatus;
    this.wifeNm = wifeNm;
    this.noOfChildren = noOfChildren;
    this.technical = technical;
    this.academic = academic;
    this.prof1 = prof1;
    this.prof2 = prof2;
    this.prof3 = prof3;
    this.prof4 = prof4;
    this.previousEmployer = previousEmployer;
    setpContr(presentContractor);
    this.referredBy = referredBy;
    this.shoesize = shoesize;
    this.bloodgrp = bloodgrp;
    this.amcCheck = amcCheck;
    this.winCoRelative = winCoRelative;
    this.winCoName = winCoName;
    this.wincoAddress = wincoAddress;
    this.mobileNo = mobileNo;
    this.landLoser = landLoser;
    this.surveyNo = surveyNo;
    this.relnWithSeller = relnWithSeller;
    this.village = village;
    this.nameofSeller = nameofSeller;
    this.extent = extent;
    this.accOpenWithContractor = accOpenWithContractor;
    this.bankBranch = bankBranch;
    this.acctNo = acctNo;
    this.medicalTrainingSw = medicalTrainingSw;
    this.safetyTrainingSw = safetyTrainingSw;
    this.skillCert = skillCert;
    this.prevExp = prevExp;
    this.prevOrg = prevOrg;
  }
  

  private String wifeNm;
  
  private Integer noOfChildren;
  
  private String technical;
  
  private String academic;
  
  private String prof1;
  
  private String prof2;
  
  private String prof3;
  
  private String prof4;
  private String previousEmployer;
  private String referredBy;
  private Integer shoesize;
  private String bloodgrp;
  private KDate amcCheck;
  private Boolean winCoRelative;
  private String winCoName;
  private String wincoAddress;
  private String mobileNo;
  private Boolean landLoser;
  private String surveyNo;
  private String relnWithSeller;
  private String village;
  private String nameofSeller;
  private String extent;
  private Boolean accOpenWithContractor;
  private String bankBranch;
  private String acctNo;
  private Boolean medicalTrainingSw;
  private Boolean safetyTrainingSw;
  private Boolean skillCert;
  private ObjectIdLong pContr;
  public ObjectIdLong getPersonDetlId()
  {
    return personDetlId;
  }
  
  public void setPersonDetlId(ObjectIdLong personDetl) { personDetlId = personDetl; }
  
  public KDate getDoj() {
    return doj;
  }
  
  public void setDoj(KDate doj) { this.doj = doj; }
  
  public KDate getDot() {
    return dot;
  }
  
  public void setDot(KDate dot) { this.dot = dot; }
  
  public String getPfAcctNo() {
    return pfAcctNo;
  }
  
  public void setPfAcctNo(String pfAcctNo) { this.pfAcctNo = pfAcctNo; }
  
  public String getAadharNo() {
    return aadharNo;
  }
  
  public void setAadharNo(String aadharNo) { this.aadharNo = aadharNo; }
  
  public String getGender() {
    return gender;
  }
  
  public void setGender(String gender) { this.gender = gender; }
  
  public String getPrevExp() {
	return prevExp;
}

public void setPrevExp(String prevExp) {
	this.prevExp = prevExp;
}


public String getPrevOrg() {
	return prevOrg;
}

public void setPrevOrg(String prevOrg) {
	this.prevOrg = prevOrg;
}

public String getPanno() {
    return panno;
  }
  
  public void setPanno(String panno) { this.panno = panno; }
  
  public Boolean getHazard() {
    return hazard;
  }
  
  public void setHazard(Boolean hazard) { this.hazard = hazard; }
  
  public Boolean getmStatus() {
    return mStatus;
  }
  
  public void setmStatus(Boolean mStatus) { this.mStatus = mStatus; }
  
  public String getWifeNm() {
    return wifeNm;
  }
  
  public void setWifeNm(String wifeNm) { this.wifeNm = wifeNm; }
  
  public Integer getNoOfChildren() {
    return noOfChildren;
  }
  
  public void setNoOfChildren(Integer noOfChildren) { this.noOfChildren = noOfChildren; }
  
  public String getTechnical() {
    return technical;
  }
  
  public void setTechnical(String technical) { this.technical = technical; }
  
  public String getAcademic() {
    return academic;
  }
  
  public void setAcademic(String academic) { this.academic = academic; }
  
  public String getProf1() {
    return prof1;
  }
  
  public void setProf1(String prof1) { this.prof1 = prof1; }
  
  public String getProf2() {
    return prof2;
  }
  
  public void setProf2(String prof2) { this.prof2 = prof2; }
  
  public String getProf3() {
    return prof3;
  }
  
  public void setProf3(String prof3) { this.prof3 = prof3; }
  
  public String getProf4() {
    return prof4;
  }
  
  public void setProf4(String prof4) { this.prof4 = prof4; }
  
  public String getPreviousEmployer() {
    return previousEmployer;
  }
  
  public void setPreviousEmployer(String previousEmployer) { this.previousEmployer = previousEmployer; }
  
  public String getReferredBy() {
    return referredBy;
  }
  
  public void setReferredBy(String referredBy) { this.referredBy = referredBy; }
  
  public Integer getShoesize() {
    return shoesize;
  }
  
  public void setShoesize(Integer shoesize) { this.shoesize = shoesize; }
  
  public String getBloodgrp() {
    return bloodgrp;
  }
  
  public void setBloodgrp(String bloodgrp) { this.bloodgrp = bloodgrp; }
  
  public Boolean getWinCoRelative() {
    return winCoRelative;
  }
  
  public void setWinCoRelative(Boolean winCoRelative) { this.winCoRelative = winCoRelative; }
  
  public String getWinCoName() {
    return winCoName;
  }
  
  public void setWinCoName(String winCoName) { this.winCoName = winCoName; }
  
  public String getWincoAddress() {
    return wincoAddress;
  }
  
  public void setWincoAddress(String wincoAddress) { this.wincoAddress = wincoAddress; }
  
  public String getMobileNo() {
    return mobileNo;
  }
  
  public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
  
  public Boolean getLandLoser() {
    return landLoser;
  }
  
  public void setLandLoser(Boolean landLoser) { this.landLoser = landLoser; }
  
  public String getSurveyNo() {
    return surveyNo;
  }
  
  public void setSurveyNo(String surveyNo) { this.surveyNo = surveyNo; }
  
  public String getRelnWithSeller() {
    return relnWithSeller;
  }
  
  public void setRelnWithSeller(String relnWithSeller) { this.relnWithSeller = relnWithSeller; }
  
  public String getVillage() {
    return village;
  }
  
  public void setVillage(String village) { this.village = village; }
  
  public String getNameofSeller() {
    return nameofSeller;
  }
  
  public void setNameofSeller(String nameofSeller) { this.nameofSeller = nameofSeller; }
  
  public String getExtent() {
    return extent;
  }
  
  public void setExtent(String extent) { this.extent = extent; }
  
  public Boolean getAccOpenWithContractor() {
    return accOpenWithContractor;
  }
  
  public void setAccOpenWithContractor(Boolean accOpenWithContractor) { this.accOpenWithContractor = accOpenWithContractor; }
  
  public String getBankBranch() {
    return bankBranch;
  }
  
  public void setBankBranch(String bankBranch) { this.bankBranch = bankBranch; }
  
  public String getAcctNo() {
    return acctNo;
  }
  
  public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
  
  public Boolean getMedicalTrainingSw() {
    return medicalTrainingSw;
  }
  
  public void setMedicalTrainingSw(Boolean medicalTrainingSw) { this.medicalTrainingSw = medicalTrainingSw; }
  
  public Boolean getSafetyTrainingSw() {
    return safetyTrainingSw;
  }
  
  public void setSafetyTrainingSw(Boolean safetyTrainingSw) { this.safetyTrainingSw = safetyTrainingSw; }
  
  public Boolean getSkillCert() {
    return skillCert;
  }
  
  public void setSkillCert(Boolean skillCert) { this.skillCert = skillCert; }
  
  public void doAdd()
  {
    new CMSService().addWorkmenDetail(this);
  }
  
  public void doUpdate() {
    new CMSService().updateWorkmenDetail(this);
  }
  
  public KDate getAmcCheck() {
    return amcCheck;
  }
  
  public void setAmcCheck(KDate amcCheck) { this.amcCheck = amcCheck; }
  
  public static WorkmenDetail retrieveById(ObjectIdLong id) {
    return new CMSService().getWorkmenDetailById(id);
  }
  
  public void doDelete() { new CMSService().deleteWorkmenDetail(this); }
  
  public ObjectIdLong getpContr()
  {
    return pContr;
  }
  
  public void setpContr(ObjectIdLong pContr) { this.pContr = pContr; }
  

  public void updateFields(WorkmenDetail detail)
  {
    if (detail.getHazard() != null) {
      setHazard(detail.getHazard());
    }
    if (detail.getLandLoser() != null) {
      setLandLoser(detail.getLandLoser());
    }
    if (detail.getMedicalTrainingSw() != null) {
      setMedicalTrainingSw(detail.getMedicalTrainingSw());
    }
    if (detail.getmStatus() != null) {
      setmStatus(detail.getmStatus());
    }
    if (detail.getSafetyTrainingSw() != null) {
      setSafetyTrainingSw(detail.getSafetyTrainingSw());
    }
    if (detail.getSkillCert() != null) {
      setSkillCert(detail.getSkillCert());
    }
    if (detail.getWinCoRelative() != null) {
      setWinCoRelative(detail.getWinCoRelative());
    }
    
    if (detail.getAadharNo() != null) {
      setAadharNo(detail.getAadharNo());
    }
    
    setAcademic(detail.getAcademic());
    
    if (detail.getAcctNo() != null) {
      setAcctNo(detail.getAcctNo());
    }
    
    setAmcCheck(detail.getAmcCheck());
    
    if (detail.getBankBranch() != null) {
      setBankBranch(detail.getBankBranch());
    }
    if (detail.getBloodgrp() != null) {
      setBloodgrp(detail.getBloodgrp());
    }
    
    if (detail.getDoj() != null) {
      setDoj(detail.getDoj());
    }
    
    setDot(detail.getDot());
    
    if (detail.getExtent() != null) {
      setExtent(detail.getExtent());
    }
    if (detail.getGender() != null) {
      setGender(detail.getGender());
    }
    if (detail.getPrevExp() != null) {
    	setPrevExp(detail.getPrevExp());
      }
    if (detail.getPrevOrg() != null) {
    	setPrevOrg(detail.getPrevOrg());
      }
    
    if (detail.getMobileNo() != null) {
      setMobileNo(detail.getMobileNo());
    }
    if (detail.getNameofSeller() != null) {
      setNameofSeller(detail.getNameofSeller());
    }
    setNoOfChildren(detail.getNoOfChildren());
    
    setPanno(detail.getPanno());
    setPfAcctNo(detail.getPfAcctNo());
    
    if (detail.getPreviousEmployer() != null) {
      setPreviousEmployer(detail.getPreviousEmployer());
    }
    if (detail.getProf1() != null) {
      setProf1(detail.getProf1());
    }
    if (detail.getProf2() != null) {
      setProf2(detail.getProf2());
    }
    if (detail.getProf3() != null) {
      setProf3(detail.getProf3());
    }
    if (detail.getProf4() != null) {
      setProf4(detail.getProf4());
    }
    if (detail.getReferredBy() != null) {
      setReferredBy(detail.getReferredBy());
    }
    
    if (detail.getRelnWithSeller() != null) {
      setRelnWithSeller(detail.getRelnWithSeller());
    }
    
    setShoesize(detail.getShoesize());
    
    if (detail.getSurveyNo() != null) {
      setSurveyNo(detail.getSurveyNo());
    }
    
    setTechnical(detail.getTechnical());
    
    if (detail.getVillage() != null) {
      setVillage(detail.getVillage());
    }
    if (detail.getWifeNm() != null) {
      setWifeNm(detail.getWifeNm());
    }
    if (detail.getWincoAddress() != null) {
      setWincoAddress(detail.getWincoAddress());
    }
    if (detail.getWinCoName() != null) {
      setWinCoName(detail.getWinCoName());
    }
    
    if (detail.getAccOpenWithContractor() != null) {
      setAccOpenWithContractor(detail.getAccOpenWithContractor());
    }
  }
}
