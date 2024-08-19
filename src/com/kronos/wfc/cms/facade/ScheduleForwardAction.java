package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.Workmen;
import com.kronos.wfc.cms.business.Workorder;
import com.kronos.wfc.commonapp.people.business.person.Person;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions;
import com.kronos.wfc.platform.utility.framework.URLUtils;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;











public class ScheduleForwardAction
  extends WFPLookupDispatchActions
{
  public ScheduleForwardAction() {}
  
  protected static Map methodsMap()
  {
    Map<String, String> methods = new HashMap();
    methods.put("cms.action.refresh", "doRefresh");
    return methods;
  }
  
  protected Map getKeyMethodMap()
  {
    return methodsMap();
  }
  


  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    try
    {
      StringBuffer buffer = new StringBuffer();
      String fromDtm = (String)data.getValue("fromdtm");
      String toDtm = (String)data.getValue("todtm");
      String lrId = (String)data.getValue("lrId");
      Workorder order = new CMSService().retrieveWorkorderByLRId(new ObjectIdLong(lrId));
      String[] ids = (String[])data.getValue("selectedIds");
      StringBuffer idBuffer = new StringBuffer();
      if ((ids != null) && (ids.length > 0))
      {
        for (int i = 0; i < ids.length; i++) {
          String id = ids[i];
          
          Workmen wk = Workmen.getWorkmenById(new ObjectIdLong(id), order.getUnitId());
          idBuffer.append(wk.getPerson().getPersonId().toString());
          if (i != ids.length - 1) {
            idBuffer.append(",");
          }
        }
        buffer.append(URLUtils.APP_SERVER_CONTEXT).append("/scheduletabularlaunch/sched_nav_button?employeeIds=");
        buffer.append(idBuffer.toString()).append("&selectedEmployeeIDs=");
        buffer.append(idBuffer.toString()).append("&empIds=");
        buffer.append(idBuffer.toString()).append("&BeginDate=");
        buffer.append(fromDtm).append("&tf=9&13=1&14=mgr&Personid=");
        buffer.append(CurrentUserAccountManager.getPersonality().getPersonId().toString());
        buffer.append("&11=1376287951981").append("&ed=").append(toDtm);
        buffer.append("&12=").append(CurrentUserAccountManager.getPersonality().getPersonId().toString());
        buffer.append("&end_date=").append(toDtm);
        buffer.append("&to_timeframe=").append(toDtm);
        buffer.append("&hyperfindid=1&timeframe_type=9");
        buffer.append("&st=1376287951981&end=").append(toDtm);
        buffer.append("&bd=").append(fromDtm).append("&from_timeframe=");
        buffer.append(fromDtm).append("&beginTimeframeDate=").append(fromDtm);
        buffer.append("&hf=1").append("&begin_date=").append(fromDtm);
        buffer.append("&pid=").append(CurrentUserAccountManager.getPersonality().getPersonId().toString());
        buffer.append("&TimeFrame=9&").append("scheduleEndDate=").append(fromDtm).append("&3=");
        buffer.append(idBuffer.toString()).append("&1=").append(toDtm).append("&EndDate=").append(toDtm);
        buffer.append(toDtm).append("&callerId=mgr&empList=").append(idBuffer.toString()).append("&0=").append(fromDtm).append("&start=");
        buffer.append(fromDtm).append("&6=9&IDs=").append(idBuffer.toString()).append("&timeFrameId=9&la=mgr&scheduleStartDate=").append(fromDtm);
        buffer.append("&endTimeframeDate=").append(toDtm).append("&emplIds=").append(idBuffer.toString());
        

        request.setAttribute("ScheduleParameters", buffer.toString());
      }
      
    }
    catch (CMSException ex)
    {
      log(1, ex.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex);
      return mapping.findForward("doCMSLRSchedule");

    }
    catch (Exception e)
    {
      log(1, e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, CMSException.unknown(e.getLocalizedMessage()));
      return mapping.findForward("doCMSLRSchedule");
    }
    return mapping.findForward("success");
  }
}
