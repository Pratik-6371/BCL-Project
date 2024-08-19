<%@page
	import="com.kronos.wfc.platform.persistence.framework.ObjectIdLong"%>
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
	prefix="v3lists"%><%@
	page
	import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties"%>
<%@ taglib tagdir="/WEB-INF/tags/wpk/kvl" prefix="kvl"%>
<link
	href="<%=URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp")%>"
	media="all" rel="stylesheet" type="text/css">
<link
	href="<%=URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp")%>"
	media="all" rel="stylesheet" type="text/css">
<%@include file="/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp" />

<script type="text/javascript"
	src="<%=URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js")%>"></script>
<script>
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('imageId').setAttribute('src', e.target.result);
            setDataChanged();
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function disableDropDown(){
	
	var workorderNum = document.getElementById('workorderId').value;
	if(workorderNum === "-"){
		document.getElementById('item_serviceItemId').disabled=true;
	}
	else if(workorderNum === "-1"){
		//when user selects the default one it should not hit database
		document.getElementById('item_serviceItemId').disabled=false;
	}
	else
		{
		getRefreshData();
		}
}

function hideWages(){
	
	var empType = document.getElementById('empType').value;
	console.log(empType);
	var vda = document.getElementById("vda");
	var pda = document.getElementById("pda");
	var hra = document.getElementById("hra");
	var conveyance = document.getElementById("conveyance");
	var specaialAllownace = document.getElementById("specaialAllownace");
	var shihftAllowance = document.getElementById("shihftAllowance");
	var dustAllowance = document.getElementById("dustAllowance");
	var medicalAllowance = document.getElementById("medicalAllowance");
	var lta = document.getElementById("lta");
	var educationalAllowance = document.getElementById("educationalAllowance");

	//console.log(allowanceDiv);
	if(empType === "OWB"){
		console.log("Inside If");
		document.getElementById("vda").readOnly = true;
		document.getElementById("pda").readOnly = true;
		document.getElementById("hra").readOnly = true;
		document.getElementById("conveyance").readOnly= true;
		document.getElementById("specaialAllownace").readOnly= true;
		document.getElementById("shihftAllowance").readOnly = true;
		document.getElementById("dustAllowance").readOnly = true;
		document.getElementById("medicalAllowance").readOnly = true;
		document.getElementById("lta").readOnly = true;
		document.getElementById("educationalAllowance").readOnly = true;
		}
	else{
		document.getElementById("vda").readOnly = false;
		document.getElementById("pda").readOnly = false;
		document.getElementById("hra").readOnly = false;
		document.getElementById("conveyance").readOnly= false;
		document.getElementById("specaialAllownace").readOnly= false;
		document.getElementById("shihftAllowance").readOnly = false;
		document.getElementById("dustAllowance").readOnly = false;
		document.getElementById("medicalAllowance").readOnly = false;
		document.getElementById("lta").readOnly = false;
		document.getElementById("educationalAllowance").readOnly = false;
	}
}

function nonEdit(){
	alert("YOU CAN NOT EDIT THIS FIELD");
}

function getRefreshData() 
{
   setDataChanged(false);
   if(checkUnsavedData())
   {
	   document.forms.terminateWorkmenDetailForm.doAction ('<%= KronosProperties.get("cms.action.terminate.workmen.detail.refresh").replaceAll("'","\\\\'") %>');
	   /* document['com.kronos.ui.action-controller'].doAction('formAction', 'cms.action.workmen.detail.refresh', 'submit'); */
   }
   else
    false;
}

</script>

<script>
function getExp()
{
	 var doj = document.getElementById("value(doj)").value;
	var dateParts = doj.split("/");
	// month is 0-based, that's why we need dataParts[1] - 1
	var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
	var diff_ms = Date.now() - dateObject.getTime();
	var exp_dt = new Date(diff_ms); 
	var exp = Math.abs(exp_dt.getUTCFullYear() - 1970);

	document.getElementById("exp").value = exp;

}
function getAge()
{
	 var dob = document.getElementById("value(dob)").value;
	var dateParts = dob.split("/");
	// month is 0-based, that's why we need dataParts[1] - 1
	var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
	var diff_ms = Date.now() - dateObject.getTime();
	var age_dt = new Date(diff_ms); 
	var age = Math.abs(age_dt.getUTCFullYear() - 1970);

	document.getElementById("age").value = age;
	var day = dateParts[0];
	var month  = dateParts[1];
	var year = dateParts[2];
	var intYear = parseInt(year) + 58;
	var date  = day + "/" + month + "/" + intYear;
	document.getElementById("value(dot)").value = date;

}

function validateDOT()
{
	var dob = document.getElementById("value(dob)").value;
	var datePartsDob = dob.split("/");
	var dateObjectDob = new Date(+datePartsDob[2], datePartsDob[1] - 1, +datePartsDob[0]); 
	
	var dot = document.getElementById("value(dot)").value;
	var datePartsDot = dot.split("/");
	var dateObjectDot = new Date(+datePartsDot[2], datePartsDot[1] - 1, +datePartsDot[0]); 
	var dateObjectDotTime = dateObjectDot.getTime();
	
	var diff_ms = dateObjectDot.getTime() - dateObjectDob.getTime();
	var diffDate = new Date(diff_ms); 	  
	var diffCount = Math.abs(diffDate.getUTCFullYear() - 1970);
	
	if(dot != null && dot !== ""){
		
	
	if(diffCount > 62)
		{
		var day = datePartsDob[0];
		var month  = datePartsDob[1];
		var year = datePartsDob[2];
		var intYear = parseInt(year) + 58;
		var date  = day + "/" + month + "/" + intYear;
		document.getElementById("value(dot)").value = date;
		alert("Date of termination cannot be more than 62 years from DOB!");
		}
	/* else if(dateObjectDotTime < new Date().getTime()){
		alert("Date of termination cannot be Before Today's Date");
		document.getElementById("value(dot)").value = "";
	} */
	else
		{
		document.getElementById("value(dot)").value = dot;
		}
	}
	}

	function validate(){
		
		var dob = document.getElementById("value(dob)").value;
		var datePartsDob = dob.split("/");
		var dateObjectDob = new Date(+datePartsDob[2], datePartsDob[1] - 1, +datePartsDob[0]); 
		var dateDobTime = dateObjectDob.getTime();
		var doj = document.getElementById("value(doj)").value;
		var datePartsDoj = doj.split("/");
		var dateObjectDoj = new Date(+datePartsDoj[2], datePartsDoj[1] - 1, +datePartsDoj[0]); 
		var dateDojTime = dateObjectDoj.getTime();
		var dot = document.getElementById("value(dot)").value;
		var datePartsDot = dot.split("/");
		var dateObjectDot = new Date(+datePartsDot[2], datePartsDot[1] - 1, +datePartsDot[0]); 
		var dateDotTime = dateObjectDot.getTime();
		var today =new Date();
		var todayTime = today.getTime();
		var effdt = document.getElementById("value(effdt)").value;
		var datePartsEffdt = effdt.split("/");
		var dateObjectEffdt = new Date(+datePartsEffdt[2], datePartsEffdt[1] - 1, +datePartsEffdt[0]); 
		var dateEffdtTime = dateObjectEffdt.getTime();
		var workorderNum = document.getElementById('workorderId').value;		
		var wageEffdt = document.getElementById("value(wageEffdt)").value;
		var datePartsWageEffdt = wageEffdt.split("/");
		var dateObjectWageEffdt = new Date(+datePartsWageEffdt[2], datePartsWageEffdt[1] - 1, +datePartsWageEffdt[0]); 
		var dateWageEffdtTime = dateObjectWageEffdt.getTime(); 
		var esic = document.getElementById('esic').value;
		var shift = document.getElementById('shift').value;
		
		if(workorderNum === "-"){
			document.getElementById('item_serviceItemId').disabled=true;
		}
		
		/* var panNo = document.getElementById('panno').value;
		var res = panNo.search(/[A-Z]{5}[0-9]{4}[A-Z]{1}/);
		if(res === -1){
			alert("Pan Number is Invalid!!");
			return false;
		} */
		/* var eCode = document.getElementById("eCode").value;
		console.log(eCode); */
		/* var ecode = document.getElementById("eCode").childNodes[2].text; */
		else if(effdt === "" || effdt == null)
			{
			alert("Effective Date cannot be empty!!");
			return false;
			}
		/* else if(dot === "" || dot == null){
			alert("Please Enter Date of Termination!!");
			return false;
		} */
		
		 
		else if(dateEffdtTime <= dateDobTime) {
			alert("Effective Date cannot be Same or Before Date of Birth");
			return false;
			}
		else if(dateEffdtTime < dateDojTime) {
			alert("Effective Date cannot be Before Date of Joining");
			return false;
			}
		
		else if(workorderNum === "-1")
		{
		alert("Please Enter Workorder!!");
		return false;
		}
		else if(workorderNum !== "-"){
			var item_serviceItemId = document.getElementById("item_serviceItemId").value;
			if(item_serviceItemId === "-1"){  
				alert("Please Select Item & Service Number!!");
				return false;
			}
			
		}
		
		/*  if(dateDotTime < new Date().getTime()){
				alert("Date of termination cannot be Before Today's Date");
				return false;
			} */
		 if(wageEffdt === "" || wageEffdt == null)
			{
			alert("Wage Effective Date cannot be empty!!");
			return false;
			}
		  if(dateWageEffdtTime < dateDojTime){
			  alert("Wage Effective Date Cannot Be Less Than Hire Date!!");
				return false;
		  }
		  if(dot != null && dot !== ""){
			  
		  	if(dateWageEffdtTime > dateDotTime){
			  alert("Wage Effective Date Cannot Be Greater Than Termiination Date!!");
				return false;
		}
		  	if(dateDojTime >= dateDotTime) {
				alert("Date of Joining cannot be Same or After Date of Termination");
				return false;
		}
			 if(dateEffdtTime >= dateDotTime) {
				alert("Effective Date cannot be Same or After Date of Termination");
				return false;
		}
	 }
		  if(shift === "-1"){
			  alert("Please Select Shift!!");
				return false;
		  }
	}

</script>

<script type="text/javascript">
window.onload = function(){
	console.log("Inisde Load Age");
	var dob = document.getElementById("value(dob)").value;
	var doj = document.getElementById("value(doj)").value;
	var workorderNum = document.getElementById('workorderId').value;
	var empType = document.getElementById('empType').value;
	console.log(empType);
	var vda = document.getElementById("vda");
	var pda = document.getElementById("pda");
	var hra = document.getElementById("hra");
	var conveyance = document.getElementById("conveyance");
	var specaialAllownace = document.getElementById("specaialAllownace");
	var shihftAllowance = document.getElementById("shihftAllowance");
	var dustAllowance = document.getElementById("dustAllowance");
	var medicalAllowance = document.getElementById("medicalAllowance");
	var lta = document.getElementById("lta");
	var educationalAllowance = document.getElementById("educationalAllowance");
	
	if(workorderNum === "-"){
		document.getElementById('item_serviceItemId').disabled=true;
	}
	if(dob === "" || dob == null){
		document.getElementById("age").value = "";
	}
	else{
		var dateParts = dob.split("/");
		var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
		var diff_ms = Date.now() - dateObject.getTime();
		var age_dt = new Date(diff_ms); 
		var age = Math.abs(age_dt.getUTCFullYear() - 1970);
		document.getElementById("age").value = age;
	}
	
	if(doj === "" || doj == null){
		document.getElementById("exp").value = "";
	}
	else{			
		var dateParts = doj.split("/");
		var dateObject = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0]); 
		var diff_ms = Date.now() - dateObject.getTime();
		var exp_dt = new Date(diff_ms); 
		var exp = Math.abs(exp_dt.getUTCFullYear() - 1970);
		document.getElementById("exp").value = exp;
	}
	
	if(empType === "OWB"){
		console.log("Inside If");
		document.getElementById("vda").readOnly = true;
		document.getElementById("pda").readOnly = true;
		document.getElementById("hra").readOnly = true;
		document.getElementById("conveyance").readOnly= true;
		document.getElementById("specaialAllownace").readOnly= true;
		document.getElementById("shihftAllowance").readOnly = true;
		document.getElementById("dustAllowance").readOnly = true;
		document.getElementById("medicalAllowance").readOnly = true;
		document.getElementById("lta").readOnly = true;
		document.getElementById("educationalAllowance").readOnly = true;
		}
	else{
		document.getElementById("vda").readOnly = false;
		document.getElementById("pda").readOnly = false;
		document.getElementById("hra").readOnly = false;
		document.getElementById("conveyance").readOnly= false;
		document.getElementById("specaialAllownace").readOnly= false;
		document.getElementById("shihftAllowance").readOnly = false;
		document.getElementById("dustAllowance").readOnly = false;
		document.getElementById("medicalAllowance").readOnly = false;
		document.getElementById("lta").readOnly = false;
		document.getElementById("educationalAllowance").readOnly = false;
	}
}

</script>

<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(contrId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(statusId)" styleId="khtmlWsConfigId" />
<bean:define id="dob" name="terminateWorkmenDetailForm" property="value(dob)" />
<bean:define id="doj" name="terminateWorkmenDetailForm" property="value(doj)" />
<bean:define id="dot" name="terminateWorkmenDetailForm" property="value(dot)" />
<bean:define id="amccheckup" name="terminateWorkmenDetailForm"
	property="value(amccheckup)" />
<bean:define id="availSKillNames" name="terminateWorkmenDetailForm"
	property="value(availSkillNames)" />
<bean:define id="availPStateNames" name="terminateWorkmenDetailForm"
	property="value(availPStateNames)" />
<bean:define id="availPermStateNames" name="terminateWorkmenDetailForm"
	property="value(availPermStateNames)" />
<bean:define id="availTradeNames" name="terminateWorkmenDetailForm"
	property="value(availTradeNames)" />
<bean:define id="availDeviceGroupNames" name="terminateWorkmenDetailForm"
	property="value(availDeviceGroupNames)" />
<bean:define id="imageContentValue" name="terminateWorkmenDetailForm" property="value(imageContent)" />
<bean:define id="supplyTypeValue" name="terminateWorkmenDetailForm" property="value(supplyType)" />
<bean:define id="availSectionNames" name="terminateWorkmenDetailForm" property="value(availSectionNames)"/>
<bean:define id="availDepartmentNames"  name="terminateWorkmenDetailForm" property="value(availDepartmentNames)"/>
<bean:define id="availContrCodes" name="terminateWorkmenDetailForm" property="value(availContrCodes)" />
<bean:define id="empTypeValue" name="terminateWorkmenDetailForm" property="value(empTypeValue)" />
<html:hidden property="value(status)" styleId="khtmlWsConfigId" />
<bean:define id="availWorkorder" name="terminateWorkmenDetailForm" property="value(availWorkorder)"/>
<bean:define id="availWorkorderLine" name="terminateWorkmenDetailForm" property="value(availWorkorderLine)"/>
<bean:define id="effdt" name="terminateWorkmenDetailForm" property="value(effdt)" />
<bean:define id="std" name="terminateWorkmenDetailForm" property="value(std)" />
<bean:define id="bloodgroup" name="terminateWorkmenDetailForm" property="value(bloodgroup)" />
<bean:define id="vehicleType" name="terminateWorkmenDetailForm" property="value(vehicleType)" />
<bean:define id="wageEffdt" name="terminateWorkmenDetailForm" property="value(wageEffdt)" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<bean:define id="availUnitNames" name="terminateWorkmenDetailForm" property="value(availUnitNames)"/>
<bean:define id="shift" name="terminateWorkmenDetailForm" property="value(shift)" />

<div id="disablingDiv" ></div>
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<table>

						<!--   cms  -->
						<tr>
						<th><label for="khtmlNameInput"><font color="grey">
								<fmt:message key="label.instructions" /></font></label></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.eCode" /></label></th>
							<td><html:text property="value(eCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"/></td>
							<td rowspan="4">
								<html:file property="formFile" size="60" styleId="importFile" accept="image/jpeg" style="visibility:hidden" onchange="readURL(this)" disabled="true"/>
								<img id="imageId" src="<c:out value="${imageContentValue}"/>" width="130" height="130" onclick="document.getElementById('importFile').click();" onchange="setDataChanged();" />
							</td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.firstname" /></label></th>
							<td><html:text property="value(firstname)"
									styleId="firstName" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.lastname" /></label></th>
							<td><html:text property="value(lastname)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<%String displayStatus = (String)request.getAttribute("displayStatus");
					if("true".equalsIgnoreCase(displayStatus))
					{	%>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="cms.label.status" /></label></th>
							<td><html:text property="value(status)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="10"
									maxlength="50"  readonly="true"/></td>
							
						</tr>
					<%} %>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.gender" /></label></th>
							<td><html:text property="value(gender)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.dateofbirth" /></label></th>
							<td width=33%><input id="value(dob)" name="value(dob)"
								onchange="getAge(); setDataChanged(true);" type=text size=20
								value="<c:out value="${dob}"/>" readonly="true"> </input>
							<%-- <kvl:date-selector-eot-bot-popup id="value(dob)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.dateofbirth" /></td> --%>
						</tr>
						
					<tr>
						<th><label for="khtmlNameInput"><span>*</span>
						<fmt:message key="label.age" /></label>
						</th>
						<td><html:text property="value(age)" styleId="age"
							size="3" maxlength="3" readonly="true"/></td>
					</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.fathername" /></label></th>
							<td><html:text property="value(relationName)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>

						
<% ObjectIdLong unitId = (ObjectIdLong) request.getAttribute("unitId");
ObjectIdLong depId = (ObjectIdLong)request.getAttribute("depId"); 
ObjectIdLong sId = (ObjectIdLong)request.getAttribute("sId");
ObjectIdLong contrid = (ObjectIdLong) request.getAttribute("contrid");
ObjectIdLong workorderId = (ObjectIdLong) request.getAttribute("workorderId");
ObjectIdLong workorderLineId = (ObjectIdLong) request.getAttribute("workorderLineId");%>
	
						<tr>
							<td><label for="khtmlNameInput"><span>*</span> <fmt:message key="cms.label.principalEmployer" /></label></td>
							<td  class="last-child">
							  <select id="selectedPE" name="value(sunitId)" onchange="getRefreshData();"> 
								<c:forEach var="principalEmployer" items="${availUnitNames}">
						        <option value="<c:out value="${principalEmployer.unitId}"/>"
								 <c:if test="${principalEmployer.unitId == unitId}">selected</c:if>
								>
						          <c:out value="${principalEmployer.unitName}"/>
						        </option>
						        </c:forEach>
							  </select>
							</td>
						</tr>
						
						<tr>
							<td><label for="khtmlNameInput"><span>*</span> <fmt:message key="label.ccode" /></label></td>
							<td>							
								<select id="value(ccode)" name="value(ccode)" onchange="getRefreshData();" 
								style="width: 140px">
									<option value="-1">Select Contractor</option>
									<c:forEach var="ccode" items="${availContrCodes}">
										<option  value="<c:out value="${ccode.contractorid}"/>" 
										<c:if test="${ccode.contractorid == contrid}">selected</c:if>>
											<c:out value="${ccode.vendorCode}" /> - <c:out value="${ccode.contractorName}" />
										</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.code" /></label></th>
							<td>
							  <select id="selectId" name="value(depId)"onchange="getRefreshData();"> 
							  	<option value="-1">Select Department </option>
							    <c:forEach var="department" items="${availDepartmentNames}">
						        <option value="<c:out value="${department.code}"/>" 
								 <c:if test="${department.depid == depId}">selected</c:if>>
						          <c:out value="${department.code}"/>
						   		 </option>
						        </c:forEach>
							  </select>
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="cms.label.section" /></label></th>
							<td>
								  <select id="selectedId" name="value(secId)" onchange="setDataChanged();"> 
								  	<option value="-1">Select Section</option>
								     <c:forEach var="section" items="${availSectionNames}"> 
							        <option value="<c:out value="${section.sectionId}"/>" 
									 <c:if test="${section.sectionId == sId}">selected</c:if>>
							          <c:out value="${section.name}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.workorder" /></label></th>
							<td>
								  <select id="workorderId" name="value(workorderNum)" onchange="disableDropDown(); "> 
								  	<option value="-1">Select Work Order</option>
									<option value="-"
									<c:if test="${workorderId == '-6'}">selected = "selected"</c:if>>-</option>
								     <c:forEach var="workorder" items="${availWorkorder}"> 
							        <option value="<c:out value="${workorder.wkNum}"/>" 
									 <c:if test="${workorder.workorderId == workorderId}">selected</c:if>>
							          <c:out value="${workorder.wkNum}"/>
							   		 </option>
							        </c:forEach>
								  </select>
			
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.item.serviceItem.number" /></label></th>
							<td>
								  <select id="item_serviceItemId" name="value(item_serviceItem_number)" onchange="setDataChanged();"> 
								  	<option value="-1">Select Item & Service Item</option>
								     <c:forEach var="item_serviceItem_number" items="${availWorkorderLine}"> 
							        <option value="<c:out value="${item_serviceItem_number.item_serviceItem_number}"/>" 
									 <c:if test="${item_serviceItem_number.wkLineId == workorderLineId}">selected</c:if>>
							          <c:out value="${item_serviceItem_number.item_serviceItem_number}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							
							</td>
						</tr>
						
						<%-- <tr>
							<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.itemNumber" /></label></th>
							<td>
								  <select id="itemNumId" name="value(itemNum)" onchange="setDataChanged();"> 
								  	<option value="-1">Select Line Item</option>
								     <c:forEach var="lineItem" items="${availWorkorderLine}"> 
							        <option value="<c:out value="${lineItem.itemNumber}"/>" 
									 <c:if test="${lineItem.wkLineId == workorderLineId}">selected</c:if>>
							          <c:out value="${lineItem.itemNumber}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.serviceLineItemNumber" /></label></th>
							<td>
								  <select id="serviceLineIte
								  mId" name="value(serviceLineItem)" onchange="setDataChanged();"> 
								  	<option value="-1">Select Service Line Item</option>
								     <c:forEach var="serviceLineItem" items="${availWorkorderLine}"> 
							        <option value="<c:out value="${serviceLineItem.serviceLineItemNumber}"/>" 
									 <c:if test="${serviceLineItem.wkLineId == workorderLineId}">selected</c:if>>
							          <c:out value="${serviceLineItem.serviceLineItemNumber}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							
							</td>
						</tr> --%>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.effective.date" /></label></th>
							<td width=33%><input id="value(effdt)" name="value(effdt)"
								onchange="setDataChanged(true);" type=text size=20
								value="<c:out value="${effdt}"/>"> </input>
							<kvl:date-selector-eot-bot-popup id="value(effdt)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.effective.date" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">PF exempt?</label></th>
							<td><html:checkbox property="value(pfExempt)"
									styleId="pfExempt" onchange="getRefreshData();" /></td>
						</tr>
						<% String pfEx = (String)request.getAttribute("pfEx");%>
						
						<tr style="display:<%=pfEx%>">
							<th><label for="khtmlNameInput">
								<fmt:message key="label.pfAcctNo" /></label></th>
							<td><html:text property="value(pfaccno)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">ESIC exempt?</label></th>
							<td><html:checkbox property="value(esicExempt)"
									styleId="khtmlNameInput" onchange="getRefreshData();" /></td>
						</tr>
						<% String esiEx = (String)request.getAttribute("esiEx");%>
											
						<tr style="display:<%=esiEx%>">
							<th><span></span><label> ESIC</label></th>
							<th><html:text property="value(esic)"
									styleId="esic" onchange="setDataChanged();" size="10"
									maxlength="10"/></td>
						</tr>
						
						<tr>
							<th>Universal Account Number</th>
							<th><html:text property="value(proflic4)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></th>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.aadharNo" /></label></th>
							<td><html:text property="value(aadharno)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						
						
						
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b>
								<fmt:message key="label.natureOfJob" /></b></font></span></label></th>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>Employee Type</label></th>
							<td>
								<select id="empType" name="value(empType)"
									onchange="hideWages(); setDataChanged();">
									<option value="WB"
										<c:if test="${empTypeValue == 'WB'}">selected = "selected"</c:if>>WB</option>
									<option value="OWB"
										<c:if test="${empTypeValue == 'OWB'}">selected = "selected"</c:if>>OWB</option>	
								</select>
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.supplyType" /></label></th>
							<td>
								<select id="selectedFunc" name="value(supplyType)"
									onchange="setDataChanged()">
									<option value="Supply"
										<c:if test="${supplyTypeValue == 'Supply'}">selected = "selected"</c:if>>Supply</option>
									<option value="BSR"
										<c:if test="${supplyTypeValue == 'BSR'}">selected = "selected"</c:if>>BSR</option>	
									<option value="Temporary - Supply"
										<c:if test="${supplyTypeValue == 'Temporary - Supply'}">selected = "selected"</c:if>>Temporary - Supply</option>
									<option value="Temporary - BSR"
										<c:if test="${supplyTypeValue == 'Temporary - BSR'}">selected = "selected"</c:if>>Temporary - BSR</option>
									<option value="Monthly Paid"
										<c:if test="${supplyTypeValue == 'Monthly Paid'}">selected = "selected"</c:if>>Monthly Paid</option>
									<option value="Daily  Paid"
										<c:if test="${supplyTypeValue == 'Daily  Paid'}">selected = "selected"</c:if>>Daily  Paid</option>
									<option value="Schedule of Rate"
										<c:if test="${supplyTypeValue == 'Schedule of Rate'}">selected = "selected"</c:if>>Schedule of Rate</option>
									<option value="Job - Measurement Work"
										<c:if test="${supplyTypeValue == 'Job - Measurement Work'}">selected = "selected"</c:if>>Job - Measurement Work</option>
									<option value="Short Term Contract"
										<c:if test="${supplyTypeValue == 'Short Term Contract'}">selected = "selected"</c:if>>Short Term Contract</option>
								
								</select>
							</td>
						</tr>

					<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.shift_hr" /></label></th>
							<td>
								<select id="shift" name="value(shift)"
									onchange="setDataChanged()">
									<option value="-1"
										<c:if test="${supplyTypeValue == '-1'}">selected = "selected"</c:if>>Select Shift</option>
									<option value="8"
										<c:if test="${shift == '8'}">selected = "selected"</c:if>>8 Hours</option>
									<option value="12"
										<c:if test="${shift == '12'}">selected = "selected"</c:if>>12 Hours</option>	
								</select>
							</td>
						</tr>

						<%
							ObjectIdLong tId = (ObjectIdLong) request.getAttribute("tId");
						%>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.trade" /></label></th>
							<td class="last-child"><select id="value(tradeId)"
								name="value(tradeId)">
									<option value="-1"><kvl:label
											id="cms.label.selectTrade" /></option>
									<c:forEach var="trade" items="${availTradeNames}">
										<option value="<c:out value="${trade.tradeId}"/>"
											<c:if test="${trade.tradeId == tId}">selected</c:if>>
											<c:out value="${trade.tradeName}" />
										</option>
									</c:forEach>
							</select></td>

						</tr>
						<%
							ObjectIdLong skId = (ObjectIdLong) request.getAttribute("skId");
						%>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.skill" /></label></th>
							<td class="last-child"><select id="value(skillId)"
								name="value(skillId)">
									<option value="-1"><kvl:label
											id="cms.label.selectSkill" /></option>
									<c:forEach var="skill" items="${availSKillNames}">
										<option value="<c:out value="${skill.skillId}"/>"
											<c:if test="${skill.skillId == skId}">selected</c:if>>
											<c:out value="${skill.skillNm}" />
										</option>
									</c:forEach>
							</select></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.dateOfJoining" /></label></th>
							<td width=33%><input id="value(doj)" name="value(doj)"
								onclick="getExp(); setDataChanged(true);" type=text size=20
								value="<c:out value="${doj}"/>" > </input>
							 <kvl:date-selector-eot-bot-popup id="value(doj)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.dateOfJoining" />
									</td> 

						</tr>
					 <tr>
						<th><label for="khtmlNameInput"><span>*</span>
						<fmt:message key="lable.prevExperience" /></label>
						</th>
						<td><html:text property="value(prevExp)" styleId="prevExp"
							size="3" maxlength="3" readonly="true"/></td>
					</tr> 
					<tr>
						<th><label for="khtmlNameInput"><span>*</span>
						<fmt:message key="label.exp" /></label>
						</th>
						<td><html:text property="value(exp)" styleId="exp"
							size="3" maxlength="3" readonly="true"/></td>
					</tr>
					
						<th><label for="khtmlNameInput"><span>*</span>
						<fmt:message key="lable.prevOrganization" /></label>
						</th>
						<td><html:text property="value(prevOrg)" styleId="prevOrg"
							size="3" maxlength="3" readonly="true"/></td>
					</tr> 
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.dateOfTermination" /></label></th>
								
							<td width=33%><input id="value(dot)" name="value(dot)"
								onchange="validateDOT(); setDataChanged(true);" type=text size=20
								value="<c:out value="${dot}"/>"> </input>
							<kvl:date-selector-eot-bot-popup id="value(dot)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.dateOfTermination" /></td>	
								
						</tr>

					<tr>
						<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.mobileno" /></label></th>
						<td ><html:text property="value(empmobileno)" styleId="empmobileno" 
						onclick="nonEdit();" size="10" maxlength="10" readonly="true"/></td>
					</tr>

						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b>
								<fmt:message key="label.wages" /></b></font></span></label></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.basic" /></label></th>
							<td><html:text property="value(basic)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.da" /></label></th>
							<td><html:text property="value(da)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.allowance" /></label></th>
							<td><html:text property="value(allowance)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span> <fmt:message
										key="label.wage.effective.date" /></label></th>
							<td width=33%><input id="value(wageEffdt)" name="value(wageEffdt)"
								onchange="setDataChanged(true);" type=text size=20
								value="<c:out value="${wageEffdt}"/>"> </input>
							<kvl:date-selector-eot-bot-popup id="value(wageEffdt)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.wage.effective.date" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>VDA</label></th>
							<td><html:text property="value(vda)"
									styleId="vda" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>PDA</label></th>
							<td><html:text property="value(pda)"
									styleId="pda" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>HRA</label></th>
							<td><html:text property="value(hra)"
									styleId="hra" onchange="setDataChanged();" /></td>
						</tr>
				
						<tr>
							<th><label for="khtmlNameInput"><span></span>Conveyance</label></th>
							<td><html:text property="value(conveyance)"
									styleId="conveyance" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>Special Allowance</label></th>
							<td><html:text property="value(specialAllowance)"
									styleId="specaialAllownace" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>Shift Allowance</label></th>
							<td><html:text property="value(shiftAllowance)"
									styleId="shihftAllowance" onchange="setDataChanged();" /></td>
						</tr>		
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>Dust Allowance Monthly</label></th>
							<td><html:text property="value(dustAllowance)"
									styleId="dustAllowance" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>Medical</label></th>
							<td><html:text property="value(medicalAllowance)"
									styleId="medicalAllowance" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>LTA</label></th>
							<td><html:text property="value(lta)"
									styleId="lta" onchange="setDataChanged();" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>Education Allowance</label></th>
							<td><html:text property="value(educationAnnual)"
									styleId="educationalAllowance" onchange="setDataChanged();" /></td>
						</tr>	
						
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b><fmt:message
										key="label.presentAddress" /></b></font></span></label></th>
						</tr>

						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.village" /></label></th>
							<td><html:text property="value(pVillage)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.taluka" /></label></th>
							<td><html:text property="value(pTaluka)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.district" /></label></th>
							<td><html:text property="value(pDistrict)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>						
						
						<%
							ObjectIdLong pStateId = (ObjectIdLong) request.getAttribute("pStateId");
						%>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.state" /></label></th>
							<td class="last-child"><select id="value(pStateId)"
								name="value(pStateId)" onclick="nonEdit();">
									<option value="-1"><kvl:label
											id="cms.label.selectState" /></option>
									<c:forEach var="state" items="${availPStateNames}">
										<option value="<c:out value="${state.stateId}"/>"
											<c:if test="${state.stateId == pStateId}">selected</c:if>>
											<c:out value="${state.stateNm}" />

										</option>
									</c:forEach>
							</select></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b><fmt:message
										key="label.permAddress" /></b></font></span></label></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.village" /></label></th>
							<td><html:text property="value(permVillage)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.taluka" /></label></th>
							<td><html:text property="value(permTaluka)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.district" /></label></th>
							<td><html:text property="value(permDistrict)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						
						<%
							ObjectIdLong permStateId = (ObjectIdLong) request.getAttribute("permStateId");
						%>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.state" /></label></th>
							<td class="last-child"><select id="value(permStateId)"
								name="value(permStateId)" onclick="nonEdit();">
									<option value="-1"><kvl:label
											id="cms.label.selectState" /></option>
									<c:forEach var="permState" items="${availPermStateNames}">
										<option value="<c:out value="${permState.stateId}"/>"
											<c:if test="${permState.stateId == permStateId}">selected</c:if>>
											<c:out value="${permState.stateNm}" />

										</option>
									</c:forEach>
							</select></td>
						</tr>

						<%-- <tr>
							<th><label for="khtmlNameInput">
							<fmt:message key="label.badgeNumber" /></label></th>
							<td><html:text property="value(badgeNumber)"
									styleId="badgeNumber" onchange="setDataChanged();" size="40"
									maxlength="50" disabled="true"/></td>
						</tr> --%>
						<tr>
							<th><label for="khtmlNameInput">
							<fmt:message key="label.panno" /></label></th>
							<td><html:text property="value(panno)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.technical" /></label></th>
							<td><html:text property="value(technical)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									 readonly="true"/></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.academic" /></label></th>
							<td><html:text property="value(academic)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.shoesize" /></label></th>
							<td><html:text property="value(shoesize)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.bloodgroup" /></label></th>
			
							<td>
								<select id="selectedFunc" name="value(bloodgroup)"
									onclick="nonEdit();">
									<option value="-1"
										<c:if test="${bloodgroup == '-1'}">selected = "selected"</c:if>>Select Blood Group</option>
									<option value="A+"
										<c:if test="${bloodgroup == 'A+'}">selected = "selected"</c:if>>A+</option>
									<option value="A-"
										<c:if test="${bloodgroup == 'A-'}">selected = "selected"</c:if>>A-</option>	
										
									<option value="AB+"
										<c:if test="${bloodgroup == 'AB+'}">selected = "selected"</c:if>>AB+</option>	
									<option value="AB-"
										<c:if test="${bloodgroup == 'AB-'}">selected = "selected"</c:if>>AB-</option>	
										
									<option value="B+"
										<c:if test="${bloodgroup == 'B+'}">selected = "selected"</c:if>>B+</option>	
									<option value="B-"
										<c:if test="${bloodgroup == 'B-'}">selected = "selected"</c:if>>B-</option>	
										
									<option value="O+"
										<c:if test="${bloodgroup == 'O+'}">selected = "selected"</c:if>>O+</option>	
									<option value="O-"
										<c:if test="${bloodgroup == 'O-'}">selected = "selected"</c:if>>O-</option>	
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.amccheckup" /></label></th>
							<td width=33%><input id="value(amccheckup)"
								name="value(amccheckup)" onchange="setDataChanged(true);"
								type=text size=20 value="<c:out value="${amccheckup}"/>"
								readonly="true">
								</input>
							<%-- <kvl:date-selector-eot-bot-popup id="value(amccheckup)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="label.amccheckup" /></td> --%>
						</tr>

						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.identificationmark" /></label></th>
							<td><html:text property="value(idmark)"
									styleId="khtmlNameInput" onclick="nonEdit();" size="40"
									maxlength="50" readonly="true"/></td>
						</tr>
						
						<%
							ObjectIdLong dId = (ObjectIdLong) request.getAttribute("dId");
						%>
						<tr style="display:none">
							<th><label for="khtmlNameInput">
								<fmt:message key="label.deviceGroup" /></label></th>
							<td class="last-child"><select id="value(deviceGroupId)"
								name="value(deviceGroupId)" disabled="true">
									<option value="-1"><kvl:label
											id="cms.label.selectDeviceGroup" /></option>
									<c:forEach var="deviceGroup" items="${availDeviceGroupNames}">
										<option value="<c:out value="${deviceGroup.deviceGroupId}"/>"
											<c:if test="${deviceGroup.deviceGroupId == dId}">selected</c:if>>
											<c:out value="${deviceGroup.name}" />
										</option>
									</c:forEach>
							</select></td>
							
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.safety" /></label></th>
							<td><html:checkbox property="value(safety)"
									styleId="khtmlNameInput" onclick="return false;"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">Safety Training Date (DD/MM/YYYY)</label></th>
								<td width=33%><input id="value(std)" name="value(std)"
								onchange="setDataChanged(true);" readonly="true" type=text size=20
								value="<c:out value="${std}"/>"> </input>
							<%-- <kvl:date-selector-eot-bot-popup id="value(std)"
									eot_bot_enable="bot" start_of_week="1" hide_text_field="true"
									text_label_field="safetyTrainingDate" /></td> --%>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.medical" /></label></th>
							<td><html:checkbox property="value(medical)"
									styleId="khtmlNameInput" onclick="return false;;"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.skillcert" /></label></th>
							<td><html:checkbox property="value(skillcert)"
									styleId="khtmlNameInput" onclick="return false;;"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.hazard" /></label></th>
							<td><html:checkbox property="value(hazard)"
									styleId="khtmlNameInput" onclick="return false;;" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.marital" /></label></th>
							<td><html:checkbox property="value(marital)"
									styleId="khtmlNameInput" onclick="return false;;" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput">Husband Name</label></th>
							<td><html:text property="value(wifeName)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.noOfChildren" /></label></th>
							<td><html:text property="value(noofChildren)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>


						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.profLic1" /></label></th>
							<th><html:text property="value(proflic1)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></th>
							<th><html:text property="value(proflic2)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									 readonly="true"/></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.additionalSkill" /></label></th>
							<th><html:text property="value(proflic3)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></th>
						</tr>
						

						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.prevEmp" /></label></th>
							<td><html:text property="value(prevEmployer)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.referedBy" /></label></th>
							<td><html:text property="value(referedBy)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput">
								<fmt:message key="label.vehicle.type" /></label></th>
							<td>
								<select id="selectedFunc" name="value(vehicleType)"
									onchange="setDataChanged()" onclick="nonEdit();">
									<option value="NA"
										<c:if test="${vehicleType == 'NA'}">selected = "selected"</c:if>>Select Vehicle Type</option>
									<option value="Two Wheeler"
										<c:if test="${vehicleType == 'Two Wheeler'}">selected = "selected"</c:if>>Two Wheeler</option>
									<option value="Four Wheeler"
										<c:if test="${vehicleType == 'Four Wheeler'}">selected = "selected"</c:if>>Four Wheeler</option>	
						
								</select>
							</td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.vehicle.rent" /></label></th>
							<td><html:text property="value(vehicleRent)"
									styleId="vehicleRent" onclick="nonEdit();" 
									readonly="true"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.quarter.rent" /></label></th>
							<td><html:text property="value(quarterRent)"
									styleId="quarterRent" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b>
								<fmt:message key="label.relativeWICHD" /></b></font></span></label></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.relativeWIC" /></label></th>
							<td><html:checkbox property="value(isRelativeWIN)"
									styleId="khtmlNameInput" onclick="return false;;" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.relativeName" /></label></th>
							<td><html:text property="value(relativeName)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.relativeAddr" /></label></th>
							<td><html:textarea property="value(relativeAddr)"
									styleId="khtmlNameInput" onclick="nonEdit();" cols="35"
									rows="7" readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.mobileno" /></label></th>
							<td><html:text property="value(mobileno)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span><font color="darkblue"><b>
								<fmt:message key="label.landloser" /></b></font></span></label></th>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.surveyno" /></label></th>
							<td><html:text property="value(surveyno)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.relationWithSeller" /></label></th>
							<td><html:text property="value(relationWithSeller)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.village" /></label></th>
							<td><html:text property="value(village)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.nameofseller" /></label></th>
							<td><html:text property="value(nameofseller)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.extent" /></label></th>
							<td><html:text property="value(extent)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.acctOpen" /></label></th>
							<td><html:checkbox property="value(acctOpen)"
									styleId="khtmlNameInput" onclick="return false;" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.bankbranch" /></label></th>
							<td><html:text property="value(bankbranch)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><fmt:message
										key="label.acctno" /></label></th>
							<td><html:text property="value(acctno)"
									styleId="khtmlNameInput" onclick="nonEdit();"
									readonly="true"/></td>
						</tr>

						<!--  end -->
					</table>
				</td>
			</tr>
		</tbody>
	</table>

</div>
