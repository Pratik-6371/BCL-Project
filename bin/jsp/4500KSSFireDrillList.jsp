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
	import="com.kronos.wfc.platform.xml.api.bean.APIValidationException"%>
<html>
<body>

	<% 
		String GETPERSONNUMBER = "business.kss.getPersonNumber";
		String PAYCODECHECK = "business.kss.getCheckPaycode";
		String FIREDRILLMISSEDCOUNT = "business.kss.getSafetyDrillNotAttended";
		String FIREDRILLMISSEDLIST = "business.kss.getSafetyDrillNotAttendedList";
		String FIREDRILLATTENDEDCOUNT = "business.kss.getSafetyDrillAttended";
		String FIREDRILLATTENDEDLIST = "business.kss.getSafetyDrillAttendedList";

		String count1 = "";
		int c1 = 0;
		String count2 = "";
		String personNum = "";
		String personNum1 = "";
		String personName = "";
		String unitCode = (String) request.getParameter("unitCode");
		System.out.println("Selected Unit is:" + unitCode);

		try {
			//code for getting firedrill missed count
			System.out.println("Selected Unit1 is:" + unitCode);
			KSSSqlDataSetter[] dataSetter1 = new KSSSqlDataSetter[2];
			dataSetter1[0] = new KSSSqlString(unitCode);
			dataSetter1[1] = new KSSSqlString("FireDrill");
			KSSSQLStatement statement1 = new KSSSQLStatement(KSSSQLStatement.PREPARE_SELECT, FIREDRILLMISSEDCOUNT,
					dataSetter1);
			statement1.execute(dataSetter1);
			KSSDataStore dataStore1 = statement1.getDataStore();

			while (dataStore1.nextRow()) {
				c1 = dataStore1.getKSSInt(1);
			}
			System.out.println("Missed Count is:" +c1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	%>
<div id="NGT">
		<!--
{
"category":"report-tabular",
"empName":"My Department",
"config":{"title":"FireDrill Summary"},
"data":[
{
"summary":["FireDrill Missed By","<%=c1%>","Employees"],
"detail":[
<%//code for getting firedrill list count
			KSSSqlDataSetter[] dataSetter2 = new KSSSqlDataSetter[2];
			dataSetter2[0] = new KSSSqlString(unitCode);
			dataSetter2[1] = new KSSSqlString("FireDrill");
			KSSSQLStatement statement2 = new KSSSQLStatement(KSSSQLStatement.PREPARE_SELECT, FIREDRILLMISSEDLIST,
					dataSetter2);
			statement2.execute(dataSetter2);
			KSSDataStore dataStore2 = statement2.getDataStore();
System.out.println("Missed Name is123:");
			while (dataStore2.nextRow()) {
				personNum = dataStore2.getString(1);
				personName = dataStore2.getString(2);
				System.out.println("Missed Name is:" + personName);%>
{"name":"<%=personNum%>","value":"<%=personName%>"},
<%}%>
]
},

{
<%
			KSSSqlDataSetter[] dataSetter3 = new KSSSqlDataSetter[2];
			dataSetter3[0] = new KSSSqlString(unitCode);
			dataSetter3[1] = new KSSSqlString("FireDrill");
			KSSSQLStatement statement3 = new KSSSQLStatement(KSSSQLStatement.PREPARE_SELECT, FIREDRILLATTENDEDCOUNT,
					dataSetter3);
			statement3.execute(dataSetter3);
			System.out.println("Attended Count is123:");
			KSSDataStore dataStore3 = statement3.getDataStore();

			while (dataStore3.nextRow()) {
				c1 = dataStore3.getKSSInt(1);
			}
			System.out.println("Attended Count is:" + c1);%>	
"summary":["FireDrill Attended By","<%=c1%>","Employees"],
"detail":[
<%KSSSqlDataSetter[] dataSetter4 = new KSSSqlDataSetter[2];
			dataSetter4[0] = new KSSSqlString(unitCode);
			dataSetter4[1] = new KSSSqlString("FireDrill");
			KSSSQLStatement statement4 = new KSSSQLStatement(KSSSQLStatement.PREPARE_SELECT, FIREDRILLATTENDEDLIST,
					dataSetter4);
			statement4.execute(dataSetter4);
			KSSDataStore dataStore4 = statement4.getDataStore();

			while (dataStore4.nextRow()) {
				personNum = dataStore4.getString(1);
				personName = dataStore4.getString(2);
				System.out.println("Attended Name is:" + personName);%>
{"name":"<%=personNum%>","value":"<%=personName%>"},
<%}%>
]
}
]
}
-->
	</div>
</body>
</html>