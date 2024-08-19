package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.utility.framework.datetime.KDateTime;
import java.util.List;











public class WorkmenSafetyTraining
{
  private ObjectIdLong id;
  private SafetyModule trn;
  private KDateTime dateTaken;
  private KDateTime fromTime;
  private KDateTime toTime;
  private KDateTime nextTrnDate;
  private String trnDesc;
  private Department dept;
  private Section sec;
  private String func;
  private String nJob;
  private String module;
  private String TNI;
  private String facultyNm;
  private String venue;
  private String preTestMarksObtained;
  private String preTestMaxMarks;
  private String preTestPercentage;
  private String postTestMarksObtained;
  private String postTestMaxMarks;
  private String postTestPercentage;
  private String recommendation;
  private String remarks;
  private String updatedBy;
  private KDateTime updatedOn;
  
  public WorkmenSafetyTraining(ObjectIdLong id, SafetyModule trn, KDateTime dateTaken, KDateTime fromTime, KDateTime toTime, KDateTime nextTrnDate, String trnDesc, Department dept, Section sec, String func, String nJob, String module, String tNI, String facultyNm, String venue, String preTestMarksObtained, String preTestMaxMarks, String preTestPercentage, String postTestMarksObtained, String postTestMaxMarks, String postTestPercentage, String recommendation, String remarks, String updatedBy, KDateTime updatedOn)
  {
    this.id = id;
    this.trn = trn;
    this.dateTaken = dateTaken;
    this.fromTime = fromTime;
    this.toTime = toTime;
    this.nextTrnDate = nextTrnDate;
    this.trnDesc = trnDesc;
    this.dept = dept;
    this.sec = sec;
    this.func = func;
    this.nJob = nJob;
    this.module = module;
    TNI = tNI;
    this.facultyNm = facultyNm;
    this.venue = venue;
    this.preTestMarksObtained = preTestMarksObtained;
    this.preTestMaxMarks = preTestMaxMarks;
    this.preTestPercentage = preTestPercentage;
    this.postTestMarksObtained = postTestMarksObtained;
    this.postTestMaxMarks = postTestMaxMarks;
    this.postTestPercentage = postTestPercentage;
    this.recommendation = recommendation;
    this.remarks = remarks;
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
  }
  
  public ObjectIdLong getId() { return id; }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public SafetyModule getTrn() { return trn; }
  
  public void setTrn(SafetyModule trn) {
    this.trn = trn;
  }
  
  public KDateTime getDateTaken() { return dateTaken; }
  
  public void setDateTaken(KDateTime dateTaken) {
    this.dateTaken = dateTaken;
  }
  
  public KDateTime getFromTime() { return fromTime; }
  
  public void setFromTime(KDateTime fromTime) {
    this.fromTime = fromTime;
  }
  
  public KDateTime getToTime() { return toTime; }
  
  public void setToTime(KDateTime toTime) {
    this.toTime = toTime;
  }
  
  public KDateTime getNextTrnDate() { return nextTrnDate; }
  
  public void setNextTrnDate(KDateTime nextTrnDate) {
    this.nextTrnDate = nextTrnDate;
  }
  
  public String getTrnDesc() { return trnDesc; }
  
  public void setTrnDesc(String trnDesc) {
    this.trnDesc = trnDesc;
  }
  
  public Department getDept() { return dept; }
  
  public void setDept(Department dept) {
    this.dept = dept;
  }
  
  public Section getSec() { return sec; }
  
  public void setSec(Section sec) {
    this.sec = sec;
  }
  
  public String getFunc() { return func; }
  
  public void setFunc(String func) {
    this.func = func;
  }
  
  public String getnJob() { return nJob; }
  
  public void setnJob(String nJob) {
    this.nJob = nJob;
  }
  
  public String getModule() { return module; }
  
  public void setModule(String module) {
    this.module = module;
  }
  
  public String getTNI() { return TNI; }
  
  public void setTNI(String tNI) {
    TNI = tNI;
  }
  
  public String getFacultyNm() { return facultyNm; }
  
  public void setFacultyNm(String facultyNm) {
    this.facultyNm = facultyNm;
  }
  
  public String getVenue() { return venue; }
  
  public void setVenue(String venue) {
    this.venue = venue;
  }
  
  public String getPreTestMarksObtained() { return preTestMarksObtained; }
  
  public void setPreTestMarksObtained(String preTestMarksObtained) {
    this.preTestMarksObtained = preTestMarksObtained;
  }
  
  public String getPreTestMaxMarks() { return preTestMaxMarks; }
  
  public void setPreTestMaxMarks(String preTestMaxMarks) {
    this.preTestMaxMarks = preTestMaxMarks;
  }
  
  public String getPreTestPercentage() { return preTestPercentage; }
  
  public void setPreTestPercentage(String preTestPercentage) {
    this.preTestPercentage = preTestPercentage;
  }
  
  public String getPostTestMarksObtained() { return postTestMarksObtained; }
  
  public void setPostTestMarksObtained(String postTestMarksObtained) {
    this.postTestMarksObtained = postTestMarksObtained;
  }
  
  public String getPostTestMaxMarks() { return postTestMaxMarks; }
  
  public void setPostTestMaxMarks(String postTestMaxMarks) {
    this.postTestMaxMarks = postTestMaxMarks;
  }
  
  public String getPostTestPercentage() { return postTestPercentage; }
  
  public void setPostTestPercentage(String postTestPercentage) {
    this.postTestPercentage = postTestPercentage;
  }
  
  public String getRecommendation() { return recommendation; }
  
  public void setRecommendation(String recommendation) {
    this.recommendation = recommendation;
  }
  
  public String getRemarks() { return remarks; }
  
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  
  public String getUpdatedBy() { return updatedBy; }
  
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
  
  public KDateTime getUpdatedOn() { return updatedOn; }
  
  public void setUpdatedOn(KDateTime updatedOn) {
    this.updatedOn = updatedOn;
  }
  
  public static List<WorkmenSafetyTraining> retrieveByEmployeeCode(String personNum) {
    return new CMSService().getWorkmenSafetyTrainings(personNum);
  }
}
