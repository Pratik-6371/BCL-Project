package com.kronos.wfc.cms.business;

import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;

public class Address
{
  public static final ObjectIdLong DEFAULT_ADDRESS_ID = new ObjectIdLong(-1L);
  private ObjectIdLong id;
  private String taluka;
  
  public Address(ObjectIdLong id, String street, String taluka, String district, String village, ObjectIdLong stateId) {
    setId(id);
    this.taluka = taluka;
    this.district = district;
    this.stateId = stateId;
    this.village = village;
  }
  

  private String district;
  
  private String village;
  private ObjectIdLong stateId;
  public void setStateId(ObjectIdLong stateId)
  {
    this.stateId = stateId;
  }
  
  public String getTaluka() {
    return taluka;
  }
  
  public void setTaluka(String taluka) {
    this.taluka = taluka;
  }
  
  public String getDistrict() {
    return district;
  }
  
  public void setDistrict(String district) {
    this.district = district;
  }
  
  public String getVillage() {
    return village;
  }
  
  public void setVillage(String village) {
    this.village = village;
  }
  
  public void doAdd() {
    new CMSService().addAddress(this);
  }
  
  public void doUpdate() {
    new CMSService().updateAddress(this);
  }
  
  public ObjectIdLong getStateId() {
    return stateId;
  }
  
  public ObjectIdLong getId() {
    return id;
  }
  
  public void setId(ObjectIdLong id) {
    this.id = id;
  }
  
  public static Address retrieveById(ObjectIdLong id) {
    if (id == null)
      return null;
    return new CMSService().getAddressById(id);
  }
  
  public void doDelete() {
    new CMSService().deleteAddress(this);
  }
  
  public void updateFields(Address permAddress)
  {
    if (permAddress.getDistrict() != null) {
      setDistrict(permAddress.getDistrict());
    }
    
    if (permAddress.getStateId() != null) {
      setStateId(permAddress.getStateId());
    }
    
    if (permAddress.getTaluka() != null) {
      setTaluka(permAddress.getTaluka());
    }
    if (permAddress.getVillage() != null) {
      setVillage(permAddress.getVillage());
    }
  }
  
  public String getStateNm() {
    return CMSState.getStateName(stateId);
  }
}
