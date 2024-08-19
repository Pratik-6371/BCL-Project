package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.MinimumWage;
import com.kronos.wfc.cms.business.Skill;
import com.kronos.wfc.cms.business.Trade;
import com.kronos.wfc.cms.business.Wage;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.datetime.framework.KServer;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.logging.framework.LogContext;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.wfp.logging.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;




public class MinimumWageAction
  extends WFPLookupDispatchActions
{
  private static LogContext logContext = new LogContext(Log.WFC, "CMS.ACTIONS");
  
  static final String FORWARD_TO_EDITOR_ACTION_HANDLER = "cms-minimumwage-application";
  

  public MinimumWageAction() {}
  

  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    methods.put("cms.action.save", "doSave");
    methods.put("cms.action.return", "doReturn");
    
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap;
  }
  


  protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return doRefresh(mapping, form, request, response);
  }
  
  public ActionForward doSave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
   /* DynamicMapBackedForm data;
    String id;
    MinimumWage wage;*/
    try {
      saveMinimumWage(form);

    }
    catch (CMSException ex)
    {

      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
      } catch (Exception e) {
   /*  DynamicMapBackedForm data;
      String id;
      MinimumWage wage;*/
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
      } finally {
     /* DynamicMapBackedForm data;
      String id;
      MinimumWage wage;*/
      DynamicMapBackedForm data = (DynamicMapBackedForm)form;
      String id = (String)data.getValue("id");
      MinimumWage wage = MinimumWage.doRetrieveById(new ObjectIdLong(id));
      setValuesToUI(data, wage);
    }
    
    return mapping.findForward("success");
  }
  




  private MinimumWage getSavedMinimumWageFromUI(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String id = (String)data.getValue("id");
    String minimumwage = (String)data.getValue("minimumwage");
    String daperday = (String)data.getValue("daperday");
    String otherallowance = (String)data.getValue("otherallowance");
    
    try
    {
      Double x = Double.valueOf(Double.parseDouble(minimumwage));
      Double y = Double.valueOf(Double.parseDouble(daperday));
      Double localDouble1 = Double.valueOf(Double.parseDouble(otherallowance));
    } catch (Exception e) {
      throw CMSException.onlyNumbersAllowed("");
    }
    


    String name = "";
    String from = "";
    String trade = "";
    String skillset = "";
    String stateId = "";
    Wage wage = null;
    if ((id != null) && (!"".equalsIgnoreCase(id))) {
      MinimumWage mwDb = MinimumWage.doRetrieveById(new ObjectIdLong(id));
      name = mwDb.getName();
      from = KServer.dateToString(mwDb.getFrom());
      trade = Trade.retrieveById(mwDb.getTradeId()).getTradeName();
      skillset = Skill.retrieveSkill(mwDb.getSkillId()).getSkillNm();
      stateId = mwDb.getStateid().toString();
      wage = mwDb.getWage();
      Wage wage1 = new Wage(null, minimumwage, daperday, otherallowance);
      wage.updateFields(wage1);
    }
    else {
      name = (String)data.getValue("name");
      from = (String)data.getValue("from");
      trade = (String)data.getValue("trade");
      skillset = (String)data.getValue("skillset");
      stateId = (String)data.getValue("stateId");
    }
    

    List<Trade> trades = getFacade().getTrades();
    List<Skill> skills = getFacade().getSkills();
    ObjectIdLong tradeId = null;
    ObjectIdLong skillId = null;
    for (Iterator iterator = trades.iterator(); iterator.hasNext();) {
      Trade tr = (Trade)iterator.next();
      if (tr.getTradeName().equalsIgnoreCase(trade)) {
        tradeId = tr.getTradeId();
        break;
      }
    }
    for (Iterator iterator = skills.iterator(); iterator.hasNext();) {
      Skill skill = (Skill)iterator.next();
      if (skill.getSkillNm().equalsIgnoreCase(skillset)) {
        skillId = skill.getSkillId();
        break;
      }
    }
    return new MinimumWage(new ObjectIdLong(id), name, KServer.stringToDate(from), KDate.getEotDate(), 
      tradeId, skillId, wage, 
      new ObjectIdLong(stateId));
  }
  





  public ActionForward doReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    return mapping.findForward("doCMSMinimumwagelist");
  }
  

  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String id = getSelectedId(data);
    
    if (id == null) {
      id = (String)data.getValue("id");
    }
    
    try
    {
      MinimumWageFacade facade = getFacade();
      MinimumWage mw = facade.getMinimumWage(id == null ? (String)data.getValue("id") : id);
      setValuesToUI(data, mw);
    } catch (CMSException ex) {
      logError(ex);
      StrutsUtils.addErrorMessage(request, ex);
    }
    catch (Exception e) {
      logError(e);
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
    }
    data.setValue("id", id);
    return mapping.findForward("success");
  }
  



  private void logError(Exception e) {}
  


  private String getSelectedId(ActionForm form)
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String[] ids = (String[])data.getValue("selectedIds");
    String id = null;
    if ((ids != null) && (ids[0] != null))
      id = ids[0];
    return id;
  }
  


  private void setUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "true");
  }
  


  private void setErrorMessagesFromExceptions(HttpServletRequest request, BusinessValidationException e)
  {
    Exception[] wrappedEx = e.getWrappedExceptions();
    for (int i = 0; i < wrappedEx.length; i++)
    {
      GenericException ge = (GenericException)wrappedEx[i];
      StrutsUtils.addErrorMessage(request, ge);
    }
  }
  





  private void clearUiDirtyData(DynamicMapBackedForm data)
  {
    data.setState("ui_has_unsaved_data", "false");
  }
  

  protected MinimumWageFacade getFacade()
  {
    return new MinimumWageFacadeImpl();
  }
  

  private String getId(DynamicMapBackedForm form)
  {
    String id = (String)form.getValue("id");
    return id;
  }
  





  private void saveMinimumWage(ActionForm form)
    throws Exception
  {
    MinimumWage mw = getSavedMinimumWageFromUI(form);
    MinimumWageFacade facade = getFacade();
    facade.saveMinimumWage(mw);
  }
  



  private void setValuesToUI(DynamicMapBackedForm data, MinimumWage mw)
  {
    data.setValue("id", mw.getMinimumwageId().toString());
    data.setValue("name", mw.getName());
    data.setValue("from", mw.getFrom() == null ? KDate.today() : mw.getFrom().toString());
    data.setValue("to", mw.getTo() == null ? KDate.getEotDate() : mw.getTo().toString());
    
    Trade trade = Trade.retrieveById(mw.getTradeId());
    data.setValue("trade", trade.getTradeName());
    Skill skill = Skill.retrieveSkill(mw.getSkillId());
    data.setValue("skillset", skill.getSkillNm());
    
    data.setValue("minimumwage", mw.getWage().getBasic());
    data.setValue("daperday", mw.getWage().getDa());
    data.setValue("otherallowance", mw.getWage().getAllowance());
  }
  
  private void getMinimumWageDataToUI(ActionForm data)
  {
    MinimumWage mw = getSavedMinimumWageFromUI(data);
    
    setValuesToUI((DynamicMapBackedForm)data, mw);
  }
  



  private static Map methodsMap = methodsMap();
}
