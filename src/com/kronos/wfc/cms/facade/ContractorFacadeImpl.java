package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.Contractor;
import com.kronos.wfc.platform.persistence.framework.ObjectId;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





public class ContractorFacadeImpl
  implements ContractorFacade
{
  public ContractorFacadeImpl() {}
  
  private Map fillAttributeMapNoProperties(Contractor config)
  {
    Map configMap = new HashMap();
    configMap.put("id", config.getcontractorid().toString());
    configMap.put("contractorname", config.getcontractorName());
    configMap.put("caddress", config.getCaddress());
    configMap.put("caddress1", config.getCaddress1());
    configMap.put("caddress2", config.getCaddress2());
    configMap.put("caddress3", config.getCaddress3());
    configMap.put("managername", config.getManagername());
    configMap.put("esiwcnumber", config.getEsiwcnumber());
    configMap.put("licensenumber", config.getLicensenumber());
    configMap.put("licensevalidity1", config.getLicensevalidity1());
    configMap.put("licensevalidity2", config.getLicensevalidity2());
    configMap.put("coverage", config.getCoverage());
    configMap.put("totalstrength", config.getTotalstrength());
    configMap.put("maxnoofemployees", config.getMaxnoofemployees());
    configMap.put("natureofwork", config.getNatureofwork());
    configMap.put("locationofcontractwork", config.getLocationofcontractwork());
    configMap.put("periodofcontract1", config.getPeriodofcontract1());
    configMap.put("periododcontract2", config.getPeriodofcontract2());
    configMap.put("vendorcode", config.getVendorCode());
    configMap.put("pfcode", config.getPfcode());
    configMap.put("esistdt", config.getEsistdt());
    configMap.put("esieddt", config.getEsieddt());
    configMap.put("esieddt", config.getESIwcCoverage());
    configMap.put("pfnumber", config.getPfnumber());
    configMap.put("pfcodeapplicationdate", config.getPfcodeapplicationdate());
    

    return configMap;
  }
  

  public Contractor getContractorByName(String name)
  {
    Contractor config = null;
    config = Contractor.doRetrieveByName(name);
    return config;
  }
  
  public List<Contractor> getContractor()
  {
    return Contractor.doRetrieveAll();
  }
  

  public Map[] getContractorConfigurations()
  {
    List<Contractor> configurations = getContractor();
    return getContractorMap(configurations);
  }
  

  private Map[] getContractorMap(List<Contractor> configurations)
  {
    Map[] configMaps = new Map[configurations.size()];
    int i = 0;
    for (Iterator<Contractor> it = configurations.iterator(); it.hasNext();)
    {
      Contractor config = (Contractor)it.next();
      Map configMap = fillAttributeMapNoProperties(config);
      configMaps[(i++)] = configMap;
    }
    
    return configMaps;
  }
  



  public Map[] getContractors(String unitId)
  {
    List<Contractor> contractors = Contractor.doRetrieveByUnitId(new ObjectIdLong(unitId));
    return getContractorMap(contractors);
  }
  


  public void saveContractor(Contractor cont)
  {
    cont.doUpdate();
  }
  



  public Contractor getContractor(String contractorid, String unitId)
  {
    Contractor config = null;
    config = Contractor.doRetrieveById(new ObjectIdLong(contractorid), new ObjectIdLong(unitId));
    return config;
  }
  


  public Map[] getContractorsByVendorName(String name, String unitId)
  {
    Contractor cot = Contractor.doRetrieveByName(name, unitId);
    if (cot != null) {
      List result = new ArrayList();
      result.add(cot);
      return getContractorMap(result);
    }
    
    return null;
  }
  


  public Map[] getContractorsByVendorCode(String code, String unitId)
  {
    Contractor cot = Contractor.doRetrieveByCode(code, unitId);
    if (cot != null) {
      List result = new ArrayList();
      result.add(cot);
      return getContractorMap(result);
    }
    
    return null;
  }
}
