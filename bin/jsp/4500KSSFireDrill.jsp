<%@ page contentType="text/html;charset=WINDOWS-1252"%>

<%@ page import="java.lang.Exception"%>
<%@ page
	import="com.kronos.wfc.commonapp.people.business.person.CustomDataSelectByPersonID"%>
<%@ page import="java.lang.Throwable"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="com.kronos.kss.util.KSSXMLUtil"%>
<%@ page import="java.math.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.kronos.kss.util.*"%>
<%@ page import="com.kronos.kss.util.KSSPassword"%>
<%@ page import="com.kronos.kss.util.KSSProperties"%>
<%@ page import="com.kronos.kss.util.KSSWfcApi"%>

<%@ page import="com.kronos.wfc.platform.utility.framework.URLUtils"%>
<%@ page
	import="com.kronos.wfc.platform.properties.framework.KronosProperties"%>
<%@ page import="com.kronos.wfc.datacollection.smartview.shared.*"%>
<%@ page import="com.kronos.wfc.*"%>
<%@ page
	import="com.kronos.wfc.timekeeping.smalltalktotalizer.business.AccrualLedger"%>
<%@ page
	import="com.kronos.wfc.timekeeping.smalltalktotalizer.business.AccrualBalanceData"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KDate"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KDateTimeSpan"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration"%>
<%@ page import="com.kronos.wfc.commonapp.people.business.personality.*"%>
<%@ page import="com.kronos.wfc.commonapp.people.business.person.Person"%>
<%@ page
	import="com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignment"%>
<%@ page
	import="com.kronos.wfc.commonapp.people.business.jobassignment.JobAssignmentDetails"%>
<%@ page
	import="com.kronos.wfc.platform.persistence.framework.ObjectIdLong"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.business.AccrualBalanceSummary"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.api.APIAccrualBalanceSummaryBean"%>
<%@ page import="com.kronos.wfc.commonapp.rules.business.PayCodeCache"%>
<%@ page import="com.kronos.wfc.commonapp.rules.business.*"%>
<%@ page
	import="com.kronos.wfc.commonapp.processmanager.business.workflow.*"%>
<%@ page
	import="com.kronos.wfc.commonapp.processmanager.servlet.server.*"%>
<%@ page import="com.dralasoft.workflow.web.toolkit.*"%>
<%@ page import="com.dralasoft.workflow.*"%>
<%@ page import="com.kronos.wfc.platform.datetime.*"%>
<%@ page import="com.kronos.wfc.platform.datetime.framework.KServer"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KPreference"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KDateTime"%>
<%@ page import="com.kronos.wfc.platform.logging.framework.Log"%>
<%@ page import="com.kronos.wfc.platform.utility.framework.URLUtils"%>
<%@ page
	import="com.kronos.wfc.platform.properties.framework.KronosProperties"%>
<%@ page import="com.kronos.wfc.util.PreferenceHandlerManager.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page
	import="com.kronos.wfc.platform.persistence.framework.statement.DataStore"%>
<%@ page
	import="com.kronos.wfc.platform.persistence.framework.statement.SqlDataSetter"%>
<%@ page
	import="com.kronos.wfc.platform.persistence.framework.statement.SqlString"%>
<%@ page
	import="com.kronos.wfc.platform.persistence.framework.SQLStatement"%>
<%@ page import="com.kronos.wfc.platform.xml.api.bean.APIBeanList"%>
<%@ page
	import="com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean"%>
<%@page
	import="com.kronos.wfc.platform.persistence.framework.internal.KronosPassword"%>
<%@ page import="com.kronos.kss.TOR.*"%>
<%@page import="java.net.*"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.xerces.parsers.DOMParser"%>
<%@page import="org.apache.xerces.dom.DocumentImpl"%>
<%@page import="org.apache.xerces.dom.NodeImpl"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="javax.xml.transform.dom.DOMSource"%>
<%@page import="org.w3c.dom.*"%>
<%@page import="org.xml.sax.InputSource"%>
<%@page import="com.sun.net.ssl.*"%>
<%@page import="com.sun.net.ssl.internal.ssl.Provider"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="java.security.Security"%>
<%@page import="javax.net.ssl.SSLSocketFactory"%>
<%@page import="com.kronos.wfc.util.messagestream.*"%>
<%@ page import="com.kronos.kss.util.KSSWfcPersonality"%>
<%@ page import="com.kronos.kss.util.KSSWfcPersonInformation"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.shared.AccrualsRowBean"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.shared.AccrualsBean"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.servlet.AccrualsTranslator"%>

<%@ page
	import="com.kronos.wfc.datacollection.smartview.shared.M8MPreferenceUtils"%>
<%@ page
	import="com.kronos.wfc.datacollection.smartview.shared.M8MConstants"%>
<%@ page import="com.kronos.wfc.platform.persistence.framework.*"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.shared.AccrualsRowBean"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.shared.AccrualsBean"%>
<%@ page
	import="com.kronos.wfc.timekeeping.accruals.servlet.AccrualsTranslator"%>
<%@ page import="com.kronos.wfc.platform.resources.shared.constants.*"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.PreferenceHandlerManager"%>
<%@ page
	import="com.kronos.wfc.datacollection.smartview.shared.NGDSmartViewRequestHandler"%>

<%@ page import="com.kronos.wfc.timekeeping.core.api.APIPayCodeEditBean"%>
<%@ page
	import="com.kronos.wfc.platform.xml.api.bean.APICurrentUserBean"%>
<%@ page
	import="com.kronos.wfc.platform.xml.api.bean.APIPersonIdentityBean"%>
<%@ page import="com.kronos.wfc.platform.xml.api.bean.ParameterMap"%>
<%@ page import="com.kronos.wfc.platform.datetime.framework.KServer"%>
<%@ page
	import="com.kronos.wfc.platform.utility.framework.datetime.KTime"%>

	<%@ page
		import="com.kronos.wfc.platform.utility.framework.datetime.KDate"%>
	<%@ page
		import="com.kronos.wfc.platform.utility.framework.datetime.KDateTimeSpan"%>
	<%@ page
		import="com.kronos.wfc.platform.utility.framework.datetime.KTimeDuration"%>
<%@ page
		import="com.kronos.wfc.platform.utility.framework.datetime.KDateTime"%>
<%@ page import="com.kronos.wfc.platform.xml.api.bean.APIValidationException"%>
<html>
<body>
	<%
		String GETUNITS = "business.kss.getUnits";
		String ucode = "";
		String uname = "";
		ArrayList subList = new ArrayList();
	%>
	<div id="NGT">
		<!--
{
    "category" : "form",
		"form" : {
        	"action":"/applications/cms/html/jsp/4500KSSFireDrill_final.jsp",
        	"submitLabel":"Submit",
        	"backLabel":"Back",
        	"doneLabel":"Review",
        	"method":"post",
        	"fields" : [
				{
					"type" : "list",
					"prompt" : "Select Unit",
					"name" : "unitCode",
					<%	
						KSSSQLStatement statement = new KSSSQLStatement(KSSSQLStatement.SELECT, GETUNITS, subList);
						statement.execute();
						KSSDataStore dataStore = statement.getDataStore();
					%>
					"data" : [
					<%	
						while (dataStore.nextRow()) {
						ucode = (String) dataStore.getString(1);
						uname = (String) dataStore.getString(2);
						System.out.println("UCode:" + ucode);
					%>
							{"code":"<%=ucode%>", "description":"<%=uname%>"},
						<%}
						dataStore.close();
						%>
							],
					"blankLabel" : "Leave Blank",
					"required" : true

				},
				<%for (int i = 0; i < 10; i++) {%>
				{
                    "name" : "employeeId<%=i%>",
					"keypadType": "string",
                    "keypadResources":{"backspace":"Backspace","clear":"Clear","enter":"Enter","close":"Close","errorRequired":"This field is required [NGT 91-1]","errorMaxValue":"Maximum value is {0} [NGT 92-1]","errorMinValue":"Minimum value is {0} [NGT 92-2]","errorNumField":"Enter a numeric value [NGT 93-1]","errorTimeDigits":"At least 3 digits are required [NGT 94-2]","errorTimeMinutes":"Enter valid minutes (0..59) [NGT 94-2]","errorTimeHours12":"Enter valid hours (1..12) [NGT 94-2]","errorTimeHours24":"Enter valid hours (0..23) [NGT 94-2]","errorNoNegative":"Negative values not allowed [NGT 94-13]","errorNoPositive":"Positive values not allowed [NGT 94-19]","errorPrecision":"Field precision is {0} [NGT 94-20]","errorMaxLength":"Maximum length is {0} [NGT 96-1]","errorMinLength":"Minimum length is {0} [NGT 96-2]","errorMaxDigits":"Maximum number of digits is 16 [NGT 96-4]","invalidEntry":"Enter valid data [NGT 94-8]"},
                    "prompt" : "Employee ID / Badge ID",
                    "type" : "string",
                    "required" : false,
                    "showKeypad" : true
                },
				<%}%>
				{
                    "name" : "Submit",
                    "value" : "Submit",
                    "type" : "hidden",
                    "required" : false
                }
				
            ]
    }
}
-->
		

	</div>
</body>
</html>

