<%@ 
	taglib uri="/struts-tiles" prefix="tiles"%><%@ 
	taglib uri="/struts-html" prefix="html"%><%@ 
	taglib uri="/jstl-format" prefix="fmt"%><%@ 
	taglib uri="/jstl-core" prefix="c"%><%@ 
	taglib uri="/jstl-core-rt" prefix="c-rt"%><%@ 
	taglib uri="/struts-bean" prefix="bean" %><%@
	taglib uri="/kronos-html" prefix="v3html"%><%@
	taglib uri="/kronos-lists" prefix="v3lists"%>
	
	<%@ 
    taglib tagdir = "/WEB-INF/tags/wpk/kvl" prefix = "kvl"%><%@
	page import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties,
	com.kronos.wfc.platform.utility.framework.ECMAEscapeUnescape"
	
%>


<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") %>" media="all" rel="stylesheet" type="text/css">
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") %>" media="all" rel="stylesheet" type="text/css">
<%@include file = "/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp"/>

<span id="khtmlWebServiceConfigListWorkspace" />
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/cms/html/scripts/cms.js") %>"></script>
<script type="text/javascript">

function confirmDeleteWebConfigs(){
	var rows = new TableRowsSelection('khtmlWebServiceConfigListTable')
			rows = rows.getSelectedRows()
	if(rows.length == 0) return true
	if(rows.length > 1){
		deleteConfirmMessage = "<fmt:message key="wba.web.service.config.dialog.delete.confirmation.plural.text" />"
	} else {
		deleteConfirmMessage = "<fmt:message key="wba.web.service.config.dialog.delete.confirmation.singular.text" />"
	}
	if(confirm(deleteConfirmMessage)) return true
	return false
}

function setSelectedItem(id){
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}


function takeMeToSchedule(){
	var rows = new TableRowsSelection('khtmlWebServiceConfigListTable')
	rows = rows.getSelectedRows();
	if(rows.length == 0) return true;
	document.forms.laborReqScheduleForm.doAction("cms.action.go.to.schedule");
}

</script>
 <html:hidden property="value(pageId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workorderId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(fromDtm1)" styleId="khtmlWsConfigId" />
<html:hidden property="value(toDtm1)" styleId="khtmlWsConfigId" />
<html:hidden property="value(wkNumber)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedLRId)" styleId="khtmlWsConfigId" />
 <bean:define id="scheduleTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(schedules)" /> 
 <bean:define id="workmenListTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(workmenList)"/>
<bean:define id="fromdtm" name="laborReqScheduleForm" property="value(fromdtm)"/>
<bean:define id="todtm" name="laborReqScheduleForm" property="value(todtm)"/>
<bean:define id="lrLines" name="laborReqScheduleForm" property="value(lrLines)"/>
 <bean:define id="patterns" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(patterns)" />
<div class="panel first-child">
<table id="khtmlCMSConfigListTable" class="ControlLayout" cellpadding="0" cellspacing="0">
	

		<tr>
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.wkNum"  /></label></th>
                             <td><html:text property="value(wkNum)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
									
				
				
				<th><label for="khtmlNameInput"><span></span><fmt:message key="label.lrRequest"  /></label></th>
                             <td><html:text property="value(lrDispNum)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  readonly="true" disabled="true" /></td>
			
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.workordertype"  /></label></th>
                             <td><html:text property="value(wotypDispName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true"  /></td>
			
			
			<%-- <th><label for="khtmlNameInput"><span></span><fmt:message key="label.remark"  /></label></th>
                             <td><html:text property="value(remark)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true"  /></td> --%>
			</tr>
			<tr>
	
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.validFrom"  /></label></th>
                             <td><html:text property="value(validfrom)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true"  /></td>
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.validTo"  /></label></th>
                             <td><html:text property="value(validto)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>

			<%-- <th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.code" /></label></th>
							<td><html:text property="value(depCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
						
						
			<th><label for="khtmlNameInput"><span></span>
				<fmt:message key="label.section" /></label></th>
			<td><html:text property="value(secCode)"
					styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
					maxlength="50" readonly="true" disabled="true" /></td> --%>
			</tr>
			<%-- <tr>
			<th><label for="khtmlNameInput"><span></span>
				<fmt:message key="label.jobdesc" /></label></th>
			<td><html:text property="value(name)"
					styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
					maxlength="50" readonly="true" disabled="true" /></td>
			
			</tr> --%>
	</table>
	</div>
<div class="panel second-child">	
	<table id="khtmlworkorderlntable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
			<th colspan=22> <fmt:message key="label.lr.req" />
		<tr>
					<th><kvl:resource id="label.lrfromdt"/></th>
					<th><kvl:resource id="label.lrtodt"/></th>
					<th><kvl:resource id="label.job"/></th>
					<th><kvl:resource id="label.unitname"/></th>
					<th><kvl:resource id="label.code"/></th>
					<th><kvl:resource id="cms.label.section"/></th>
					<th><kvl:resource id="label.costcenter"/></th>
					<th><kvl:resource id="label.serviceCode"/></th>
					<th><kvl:resource id="label.trade"/></th>
					<th><kvl:resource id="label.skill"/></th>
					<th><kvl:resource id="label.shift_A"/></th>
					<th><kvl:resource id="label.lr_shift_A"/></th>
					<th><kvl:resource id="label.sched_shift_A"/></th>
					<th><kvl:resource id="label.shift_B"/></th>
					<th><kvl:resource id="label.lr_shift_B"/></th>
					<th><kvl:resource id="label.sched_shift_B"/></th>
					<th><kvl:resource id="label.shift_C"/></th>
					<th><kvl:resource id="label.lr_shift_C"/></th>
					<th><kvl:resource id="label.sched_shift_C"/></th>
					<th><kvl:resource id="label.shift_G"/></th>
					<th><kvl:resource id="label.lr_shift_G"/></th>
					<th><kvl:resource id="label.sched_shift_G"/></th>
					
		</tr>
	</thead>
	<tbody>
	
    <bean:size id="numConfigs" name="lrLines"/>
    
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${lrLines}" varStatus="status">					
		<c:set var="itemId" value="${item.lrId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
<!-- 			<td> -->
<%-- 				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.wkLineId}' />" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" /> --%>
<!-- 			</td> -->
			<td class="last-child">
			<c:if test="${item.fromDate == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.fromDate}' />
			</td>
			<td class="last-child">
			<c:if test="${item.toDate == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.toDate}' />
			</td>

			<td class="last-child">
			<c:if test="${item.itemDesc == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.itemDesc}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.unitCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.unitCode}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.depCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.depCode}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.secCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.secCode}' />
			</td>
			
			<td class="last-child">
				<c:if test="${item.costCenter == null}">
					&nbsp;
				</c:if>
					<c:out value='${item.costCenter}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.serviceCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.serviceCode}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.tradeId == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.trade}'/>
			
			</td>			
			<td class="last-child">
			<c:if test="${item.skillId == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.skill}' />
			</td>
			<td class="last-child">
			<c:if test="${item.shift_A == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.shift_A}' />
			</td>
			<td class="last-child">
			<c:if test="${item.lr_shift_A == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lr_shift_A}' />
			</td>

			<td class="last-child">
			<c:if test="${item.sched_shift_A == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sched_shift_A}' />
			</td>

			<td class="last-child">
			<c:if test="${item.shift_B == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.shift_B}' />
			</td>
			<td class="last-child">
			<c:if test="${item.lr_shift_B == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lr_shift_B}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.sched_shift_B== null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sched_shift_B}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.shift_C == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.shift_C}' />
			</td>
			<td class="last-child">
			<c:if test="${item.lr_shift_C == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lr_shift_C}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.sched_shift_C== null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sched_shift_C}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.shift_G == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.shift_G}' />
			</td>
			<td class="last-child">
			<c:if test="${item.lr_shift_G == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lr_shift_G}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.sched_shift_G== null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sched_shift_G}' />
			</td>
			


		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</div>
	
<div class="panel third-child">	
	<table id="detail" class="Controllayout" cellspacing="0" cellpadding="0" style="margin-bottom:1px;">
				<td width=40%>
				</td>
						<td>
						<fmt:message key="label.from" />
						<input id="value(fromdtm)"
										name="value(fromdtm)"
										type=text size=20 value="<c:out value="${fromdtm}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(fromdtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
						<td>
						<fmt:message key="label.to" />
						<input id="value(todtm)" 
										name="value(todtm)" 
										 
										type=text size=20 value="<c:out value="${todtm}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(todtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.to" /></td>
				<td>				
				<% String selectedLRId = request.getParameter("selectedLRId");
					if(selectedLRId == null) {
						selectedLRId = (String)request.getAttribute("selectedLRID");
					}
				%>
				<select name="value(lrId)" onchange="getEmployees();"> 
					    <option value="-1"><kvl:label id="cms.label.job"/></option>
					    <c:set var="selectLRId" value="${selectedLRId}"/>
						<c:forEach var="lrs" items="${lrLines}">
				        <option value="<c:out value="${lrs.lrId}"/>"
						 <c:if test="${lrs.lrId == selectLRId}">selected</c:if>
						>
				          <c:out value="${lrs.itemDesc} ${':'} ${lrs.trade}${':'} ${lrs.skill}"/>
				        </option>
				        </c:forEach>
					  </select>
			  </td>						
	</table>
	</div>

<div class="panel third-child"> 	
<v3lists:sort
	var			= "webServiceConfigSorter"  
  	items		= "workmenListTable" 
  	property	= "state(cms_c_state)"  
  	default		= "firstName"> 
<%--   <v3lists:paginate   --%>
<%-- 	var			= "paginate1"   --%>
<%--   	page		= "value(paginate_location_page)"  --%>
<%--   	items		= "workmenListTable"> --%>
	

<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0" style="margin-bottom:10px;">
 	<thead> 
 		<tr> 
 			<th class="ActionCell">
 			<input 	type="checkbox" 
								name="value(selectAll)" 
								class="selectAll"
								onclick="TableRowsSelection.doToggleSelectAll(this)">
			</th>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "firstName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.firstname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "lastName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.lastname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "empCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.eCode" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "tradeName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.trade" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "skillName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.skill" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "patternName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.shiftPattern" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "hasSchedule" />
				<tiles:put name = "displayname">
					<fmt:message key="label.hasSchedules" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				

						
		</tr>
	</thead>
	<tfoot>
<!-- 		<tr class="TablePaginator"> -->
<!-- 			<td colspan="299" cLass="last-child">     -->
<!-- 				<div id="paginate1"> -->
<%-- 				<tiles:insert name = "paginator-ui"> --%>
<%-- 					<tiles:put name	= "paginator" beanName = "paginate1" /> --%>
<%-- 					<tiles:put name	= "property" value = "value(paginate_location_page)" /> --%>
<%-- 					<tiles:put name	= "showAll" value = "true" /> --%>
<%-- 				</tiles:insert> --%>
<!-- 				</div> -->
<!-- 			</td> -->
<!-- 		</tr> -->
	</tfoot>
	
	<tbody>
	
    <bean:size id="numConfigs" name="workmenListTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
		 <c:forEach var = "item" items = "${workmenListTable}" varStatus="status">					
		<c:set var="itemId" value="${item.empId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="checkbox" class="Selected" id="ids" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.empId}'/>" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.empId == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.firstName}'/>
			
 			</td>
 			<td >
			<c:if test="${item.lastName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lastName}'/>
			
 			</td>			
 			<td >
			<c:if test="${item.empCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.empCode}'/>
			
 			</td>			
 						
 			<td class="last-child">
			<c:if test="${item.tradeId == null}">
 				&nbsp; 
			</c:if>
				<c:out value='${item.tradeName}' />
				<input type="hidden" name = "value(<c:out value="${item.empId}" />_tradeId)" value= "<c:out value="${item.tradeId}" />">
 			</td> 
 			<td class="last-child"> 
			<c:if test='${item.skillId == null}'>
 				&nbsp; 
			</c:if>
				<c:out value='${item.skillName}' />
				<input type="hidden" name = "value(<c:out value="${item.empId}" />_skillId)" value= "<c:out value="${item.skillId}" />">
 			</td> 
			
		
			<td  class="last-child">
			  <select id="selectedPE" name="value(<c:out value="${item.empId}" />_pattern)" onchange="setSelectedItem(<c:out value="${item.empId}" />)"> 
			    <option value="-1"><kvl:label id="cms.label.selectPattern"/></option>
				<c:forEach var="pats" items="${patterns}">
		        <option value="<c:out value="${pats}"/>"
				 <c:if test="${pats == item.patternName}">selected</c:if>
				>
		          <c:out value="${pats}"/>
		        </option>
		        </c:forEach>
			  </select>
			</td>
			<td class="last-child"> 
			<c:if test='${item.hasSchedule == null}'>
 				&nbsp; 
			</c:if>
				<c:out value='${item.hasSchedule}' />
			</td> 
 			
			</tr> 
 	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
 	</tbody> 
 </table> 
</v3lists:sort> 
</div>
			
