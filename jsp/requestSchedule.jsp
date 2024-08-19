<%@
    page import="com.kronos.wfc.commonapp.reporting.facade.ForwardFormRenderer,
                 com.kronos.wfc.platform.properties.framework.KronosProperties"
%>
<html>
<title>
<%=KronosProperties.get("REPORTING.label.ReportViewer", "REPORTING.label.ReportViewer")%>
</title>
<script language="JavaScript">
function doRedirect()
{
	<%String url = (String)request.getAttribute("ScheduleParameters");%>
	window.location = "<%=url%>";
//	alert("<%=url%>");
//	window.location ="/wfc/scheduletabularlaunch/sched_nav_button?employeeIds=106,259&selectedEmployeeIDs=106,259&empIDs=106,259&BeginDate=8/01/2013&tf=9&13=1&14=mgr&Personid=106&11=1376287951981&ed=8/12/2013&12=106&end_date=8/12/2013&to_timeframe=8/12/2013&hyperfindid=1&timeframe_type=9&st=1376287951981&end=8/12/2013&bd=8/01/2013&from_timeframe=8/01/2013&beginTimeframeDate=8/01/2013&hf=1&begin_date=8/01/2013&pid=106&TimeFrame=9&scheduleEndDate=8/12/2013&3=106,259&1=8/12/2013&EndDate=8/12/2013&callerId=mgr&empList=106,259&0=8/01/2013&start=8/01/2013&6=9&IDs=106,259&timeFrameId=9&la=mgr&scheduleStartDate=8/01/2013&endTimeframeDate=8/12/2013&emplIds=106,259";
}

</script>
<body onload='doRedirect();'>
	
    <div style="position:absolute; left:350px; top:250px">
    <img src='<%=KronosProperties.get("WFC.context.external")%>/applications/wpk/html/images/ReportViewerProgressIndicator.gif'>
    </div>
    <% request.removeAttribute("ScheduleParameters"); %>
	
  </body>
</html>