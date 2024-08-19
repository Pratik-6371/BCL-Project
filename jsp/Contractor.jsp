<%@ 
	taglib uri="/struts-tiles" prefix="tiles"%><%@ 
	taglib
	uri="/struts-html" prefix="html"%><%@ 
	taglib uri="/jstl-format"
	prefix="fmt"%><%@ 
	taglib uri="/jstl-core" prefix="c"%><%@ 
	taglib uri="/jstl-core-rt"
	prefix="c-rt"%><%@ 
	taglib uri="/struts-bean" prefix="bean"%><%@
	taglib
	uri="/kronos-html" prefix="v3html"%><%@
	taglib uri="/kronos-lists"
	prefix="v3lists"%><%@ taglib tagdir="/WEB-INF/tags/wpk/kvl"
	prefix="kvl"%><%@
	
	page
	import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties,
	com.kronos.wfc.platform.security.business.authorization.controlpoints.AccessControlPoint,
                 com.kronos.wfc.platform.security.business.authorization.actions.Action,
                 com.kronos.wfc.platform.security.business.authorization.profiles.AccessProfile,
                 com.kronos.wfc.platform.utility.framework.list.ListFilter"%>
<%@ taglib tagdir="/WEB-INF/tags/wpk/kvl" prefix="kvl"%>
<link
	href='<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") 
%>'
	media="all" rel="stylesheet" type="text/css">
<link
	href='<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") 
%>'
	media="all" rel="stylesheet" type="text/css">
<%@include file="/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp" />



<script type="text/javascript" src="<c:out value="${initParam['WFC.context.external']}"/>/applications/wcb/html/scripts/multi-currency.js"></script>
<script type="text/javascript"
	src='<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>'></script>

<script>

function validate(){
var wc1FromDate = document.getElementById("value(wc1FromDate)").value;
var wc1ToDate = document.getElementById("value(wc1ToDate)").value;
var wc2FromDate = document.getElementById("value(wc2FromDate)").value;
var wc2ToDate = document.getElementById("value(wc2ToDate)").value;
var wc3FromDate = document.getElementById("value(wc3FromDate)").value;
var wc3ToDate = document.getElementById("value(wc3ToDate)").value;

var wc4FromDate = document.getElementById("value(wc4FromDate)").value;
var wc4ToDate = document.getElementById("value(wc4ToDate)").value;

var wc1Coverage = document.getElementById("wc1Coverage").value;
var wc2Coverage = document.getElementById("wc2Coverage").value;
var wc3Coverage = document.getElementById("wc3Coverage").value;
var wc4Coverage = document.getElementById("wc4Coverage").value;

var pfCoverage = document.getElementById("pfCoverage").value;

var licExempt1 = document.getElementById('licExempt1');
var licExempt = document.getElementById('licExempt');

if((wc1FromDate == null || wc1FromDate === "") && (wc1ToDate != null && wc1ToDate !== ""))
	{
	alert("WC1 From Date is Empty!!");
	return false;
	}
else if((wc1ToDate == null || wc1ToDate === "") && (wc1FromDate != null && wc1FromDate !== "")){
	alert("WC1 To Date is Empty!!");
	return false;
}
else if((wc2FromDate == null || wc2FromDate === "") && (wc2ToDate != null && wc2ToDate !== "")){
	alert("WC2 From Date is Empty!!");
	return false;
}
else if((wc2ToDate == null || wc2ToDate === "") && (wc2FromDate != null && wc2FromDate !== "")){
	alert("WC2 To Date is Empty!!");
	return false;
}


else if((wc3FromDate == null || wc3FromDate === "") && (wc3ToDate != null && wc3ToDate !== "")){
	alert("WC3 From Date is Empty!!");
	return false;
}
else if((wc3ToDate == null || wc3ToDate === "") && (wc3FromDate != null && wc3FromDate !== "")){
	alert("WC3 To Date is Empty!!");
	return false;
}


else if((wc4FromDate == null || wc4FromDate === "") && (wc4ToDate != null && wc4ToDate !== "")){
	alert("WC4 From Date is Empty!!");
	return false;
}
else if((wc4ToDate == null || wc4ToDate === "") && (wc4FromDate != null && wc4FromDate !== "")){
	alert("WC4 To Date is Empty!!");
	return false;
}



else if((wc1FromDate != null && wc1FromDate !== "") && (wc1ToDate != null && wc1ToDate !== "")){
	var wc1FromDateParts = wc1FromDate.split("/");
	var wc1FromDateObject = new Date(+wc1FromDateParts[2], wc1FromDateParts[1] - 1, +wc1FromDateParts[0]);
	var wc1FromDateObjectTime = wc1FromDateObject.getTime();
	
	var wc1ToDateParts = wc1ToDate.split("/");
	var wc1ToDateObject = new Date(+wc1ToDateParts[2], wc1ToDateParts[1] - 1, +wc1ToDateParts[0]);
	var wc1ToDateObjectTime = wc1ToDateObject.getTime();
	
	if(wc1FromDateObjectTime >= wc1ToDateObjectTime){
		alert("WC1:- From date Cannot be Greater than To Date!!");
		return false;
	}
}

else if((wc2FromDate != null && wc2FromDate !== "") && (wc2ToDate != null && wc2ToDate !== "")){
	var wc2FromDateParts = wc2FromDate.split("/");
	var wc2FromDateObject = new Date(+wc2FromDateParts[2], wc2FromDateParts[1] - 1, +wc2FromDateParts[0]);
	var wc2FromDateObjectTime = wc2FromDateObject.getTime();
	
	var wc2ToDateParts = wc2ToDate.split("/");
	var wc2ToDateObject = new Date(+wc2ToDateParts[2], wc2ToDateParts[1] - 1, +wc2ToDateParts[0]);
	var wc2ToDateObjectTime = wc2ToDateObject.getTime();
	
	
	 if(wc2FromDateObjectTime >= wc2ToDateObjectTime){
		alert("WC2:- From Date Cannot be Greater than To Date!!");
		return false;
	}
}


else if((wc3FromDate != null && wc3FromDate !== "") && (wc3ToDate != null && wc3ToDate !== "")){
	var wc3FromDateParts = wc3FromDate.split("/");
	var wc3FromDateObject = new Date(+wc3FromDateParts[2], wc3FromDateParts[1] - 1, +wc3FromDateParts[0]);
	var wc3FromDateObjectTime = wc3FromDateObject.getTime();
	
	var wc3ToDateParts = wc3ToDate.split("/");
	var wc3ToDateObject = new Date(+wc3ToDateParts[2], wc3ToDateParts[1] - 1, +wc3ToDateParts[0]);
	var wc3ToDateObjectTime = wc3ToDateObject.getTime();
	
	
	 if(wc3FromDateObjectTime >= wc3ToDateObjectTime){
		alert("wc3:- From Date Cannot be Greater than To Date!!");
		return false;
	}
}

else if((wc4FromDate != null && wc4FromDate !== "") && (wc4ToDate != null && wc4ToDate !== "")){
	var wc4FromDateParts = wc4FromDate.split("/");
	var wc4FromDateObject = new Date(+wc4FromDateParts[2], wc4FromDateParts[1] - 1, +wc4FromDateParts[0]);
	var wc4FromDateObjectTime = wc4FromDateObject.getTime();
	
	var wc4ToDateParts = wc4ToDate.split("/");
	var wc4ToDateObject = new Date(+wc4ToDateParts[2], wc4ToDateParts[1] - 1, +wc4ToDateParts[0]);
	var wc4ToDateObjectTime = wc4ToDateObject.getTime();
	
	
	 if(wc4FromDateObjectTime >= wc4ToDateObjectTime){
		alert("wc4:- From Date Cannot be Greater than To Date!!");
		return false;
	}
}




else if(wc1Coverage != null && wc1Coverage !== ""){
	
	if(isNaN(wc1Coverage)){
		alert("WC1 Coverage Should be Numeric!!");
		return false;
	}
}

else if(wc2Coverage != null && wc2Coverage !== ""){
	
	if(isNaN(wc2Coverage)){
		alert("WC2 Coverage Should be Numeric!!");
		return false;
	}
}

else if(wc3Coverage != null && wc3Coverage !== ""){
	
	if(isNaN(wc3Coverage)){
		alert("WC3 Coverage Should be Numeric!!");
		return false;
	}
}

else if(wc4Coverage != null && wc4Coverage !== ""){
	
	if(isNaN(wc4Coverage)){
		alert("WC4 Coverage Should be Numeric!!");
		return false;
	}
}


else if(pfCoverage != null && pfCoverage !== ""){
	
	if(isNaN(pfCoverage)){
		alert("PF Coverage Should be Numeric!!");
		return false;
	}
}
}
function getRefreshData() 
{
   setDataChanged(false);
   if(checkUnsavedData())
   {
	   document.forms.contractorForm.doAction ('<%= KronosProperties.get("cms.action.refresh").replaceAll("'","\\\\'") %>');
	   /* document['com.kronos.ui.action-controller'].doAction('formAction', 'cms.action.workmen.detail.refresh', 'submit'); */
   }
   else
    false;
}

</script>

<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<bean:define id="licStartDate" name="contractorForm" property="value(licensevalidity1)"/>
<bean:define id="licEndDate" name="contractorForm" property="value(licensevalidity2)"/>
<bean:define id="periodStartDate" name="contractorForm" property="value(periodofcontract1)"/>
<bean:define id="periodEndDate" name="contractorForm" property="value(periodofcontract2)"/>
<bean:define id="pfApplyDate" name="contractorForm" property="value(pfcodeapplicationdate)"/>
<bean:define id="esistdt" name="contractorForm" property="value(esistdt)"/>
<bean:define id="esieddt" name="contractorForm" property="value(esieddt)"/>
<bean:define id="wc1FromDate" name="contractorForm" property="value(wc1FromDate)"/>
<bean:define id="wc1ToDate" name="contractorForm" property="value(wc1ToDate)"/>
<bean:define id="wc2FromDate" name="contractorForm" property="value(wc2FromDate)"/>
<bean:define id="wc2ToDate" name="contractorForm" property="value(wc2ToDate)"/>
<bean:define id="wc3FromDate" name="contractorForm" property="value(wc3FromDate)"/>
<bean:define id="wc3ToDate" name="contractorForm" property="value(wc3ToDate)"/>
<bean:define id="wc4FromDate" name="contractorForm" property="value(wc4FromDate)"/>
<bean:define id="wc4ToDate" name="contractorForm" property="value(wc4ToDate)"/>
<bean:define id="licStartDate1" name="contractorForm" property="value(licensevalidityFrom)"/>
<bean:define id="licEndDate1" name="contractorForm" property="value(licensevalidityTo)"/>
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<table>

						<!--   cms  contractor-->
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.vendorcode" /></label></th>
							<td><html:text property="value(vendorcode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" readonly="false" disabled="false"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.contractorname" /></label></th>
							<td><html:text property="value(contractorname)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="false" disabled="false"/></td>
						</tr>


						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.village" /></label></th>
							<td><html:text property="value(caddress)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.address1" /></label></th>
							<td><html:text property="value(caddress1)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.address2" /></label></th>
							<td><html:text property="value(caddress2)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.address3" /></label></th>
							<td><html:text property="value(caddress3)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.ccode" /></label></th>
							<td><html:text property="value(ccode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.managername" /></label></th>
							<td><html:text property="value(managername)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b>
								<fmt:message key="label.esic.wc" /></b></font></span></label></th>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic" /></label></th>
							<td><html:text property="value(esiwcnumber)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="30" /></td>

									<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(esistdt)" 
										name="value(esistdt)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${esistdt}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(esistdt)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(esieddt)" 
										name="value(esieddt)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${esieddt}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(esieddt)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc1" /></label></th>
							<td><html:text property="value(wc1)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="30" /></td>
									
									<td>
						<fmt:message key="label.from" />
						<input id="value(wc1FromDate)" 
										name="value(wc1FromDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc1FromDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc1FromDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td> 
						<fmt:message key="label.to" />
						<input id="value(wc1ToDate)" 
										name="value(wc1ToDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc1ToDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc1ToDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>
						
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc2" /></label></th>
							<td><html:text property="value(wc2)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="30" /></td>
									
									<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(wc2FromDate)" 
										name="value(wc2FromDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc2FromDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc2FromDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(wc2ToDate)" 
										name="value(wc2ToDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc2ToDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc2ToDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>



<!-- wc3 -->

				<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc3" /></label></th>
							<td><html:text property="value(wc3)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="30" /></td>
									
									<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(wc3FromDate)" 
										name="value(wc3FromDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc3FromDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc3FromDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(wc3ToDate)" 
										name="value(wc3ToDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc3ToDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc3ToDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>
<!-- wc3 end-->


<!-- wc4 -->

				<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc4" /></label></th>
							<td><html:text property="value(wc4)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="30" /></td>
									
									<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(wc4FromDate)" 
										name="value(wc4FromDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc4FromDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc4FromDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(wc4ToDate)" 
										name="value(wc4ToDate)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${wc4ToDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(wc4ToDate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>
<!-- wc4 end-->







					<tr>
							<th><label for="khtmlNameInput">License exempt1 ?</label></th>
							<td><html:checkbox property="value(licExempt)"
									styleId="licExempt" onchange="getRefreshData();" /></td>
						</tr>
					<% String licEx = (String)request.getAttribute("licEx");%>
				<tr  style="display:<%=licEx%>">
						
							<th><label for="khtmlNameInput">
								<fmt:message key="label.licensenumber" /></label></th>
							<td><html:text property="value(licensenumber)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="50" /></td>
									
						<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(licensevalidity1)" 
										name="value(licensevalidity1)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${licStartDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(licensevalidity1)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(licensevalidity2)" 
										name="value(licensevalidity2)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${licEndDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(licensevalidity2)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
																			
						</tr>
					

        				<tr>
							<th><label for="khtmlNameInput">License exempt2 ?</label></th>
							<td><html:checkbox property="value(licExempt1)"
									styleId="licExempt1" onchange="getRefreshData();" /></td>
						</tr>
						<% String licEx1 = (String)request.getAttribute("licEx1");%>
						<tr  style="display:<%=licEx1%>">
						
							<th><label for="khtmlNameInput">
								<fmt:message key="label.licensenumber1" /></label></th>
							<td><html:text property="value(licensenumber1)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="30"
									maxlength="50" /></td>
									
						<td width=33%>
						<fmt:message key="label.from1" />
						<input id="value(licensevalidityFrom)" 
										name="value(licensevalidityFrom)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${licStartDate1}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(licensevalidityFrom)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from1" /></td>
						<td width=33%> 
						<fmt:message key="label.to1" />
						<input id="value(licensevalidityTo)" 
										name="value(licensevalidityTo)" 
										onchange="setDataChanged(true);" 
										type=text size=6 value="<c:out value="${licEndDate1}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(licensevalidityTo)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to1" /></td>
																			
						</tr>




				<tr>
					<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esicoverage" /></label></th>
							<td><html:text property="value(esicoverage)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
					</tr>
				<tr>					
					<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc1.coverage" /></label></th>
							<td><html:text property="value(wc1Coverage)"
									styleId="wc1Coverage" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
						</tr>
				<tr>					
					<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc2.coverage" /></label></th>
							<td><html:text property="value(wc2Coverage)"
									styleId="wc2Coverage" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
						</tr>			


<!-- W3 COVERAGE -->
				<tr>					
					<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc3.coverage" /></label></th>
							<td><html:text property="value(wc3Coverage)"
									styleId="wc3Coverage" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
						</tr>			

<!-- W3 COVERAGE -->

<!-- W4 COVERAGE -->
				<tr>					
					<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esic.wc4.coverage" /></label></th>
							<td><html:text property="value(wc4Coverage)"
									styleId="wc4Coverage" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
				</tr>			

<!-- W4 COVERAGE -->









<tr>
<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.coverage" /></label></th>
							<td><html:text property="value(coverage)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
	</tr>
	
	<tr>							
									<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.totalstrength" /></label></th>
							<td><html:text property="value(totalstrength)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="5"
									maxlength="5" /></td>
			</tr>
	<tr>
									
									<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.Maxnoofemployees" /></label></th>
							<td><html:text property="value(maxnoofemployees)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="5"
									maxlength="5" /></td></tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.natureofwork" /></label></th>
							<td><html:textarea property="value(natureofwork)"
									styleId="khtmlNameInput" onchange="setDataChanged();" cols="35" rows="7" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.locationofcontractwork" /></label></th>
							<td><html:text property="value(locationofcontractwork)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>



						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.periodofcontract" /></label></th>
							<td width=33%>
						<fmt:message key="label.from" />
						<input id="value(periodofcontract1)" 
										name="value(periodofcontract1)" 
										onchange="setDataChanged(true);" 
										type=text size=10 value="<c:out value="${periodStartDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(periodofcontract1)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td width=33%> 
						<fmt:message key="label.to" />
						<input id="value(periodofcontract2)" 
										name="value(periodofcontract2)" 
										onchange="setDataChanged(true);" 
										type=text size=10 value="<c:out value="${periodEndDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(periodofcontract2)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>


						
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.pfCode" /></label></th>
							<td><html:text property="value(pfcode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>
					
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.pfnumber" /></label></th>
							<td><html:text property="value(pfnumber) "
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.pf.coverage" /></label></th>
							<td><html:text property="value(pfCoverage)"
									styleId="pfCoverage" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.pfcodeapplicationdate" /></label></th>
							<td width=33%> 
							<input id="value(pfcodeapplicationdate)" 
										name="value(pfcodeapplicationdate)" 
										onchange="setDataChanged(true);" 
										type=text size=20 value="<c:out value="${pfApplyDate}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(pfcodeapplicationdate)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">PF Paid By Self</label></th>
							<td><html:checkbox property="value(isPfSelf)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">ESI Paid By Self</label></th>
							<td><html:checkbox property="value(isEsiSelf)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>Commission</label></th>
							<td><html:text property="value(commission) "
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>


						<!--  end -->


					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>
