<%@ 
	taglib uri="/struts-tiles" prefix="tiles"%><%@ 
	taglib uri="/struts-html" prefix="html"%><%@ 
	taglib uri="/jstl-format" prefix="fmt"%><%@ 
	taglib uri="/jstl-core" prefix="c"%><%@ 
	taglib uri="/jstl-core-rt" prefix="c-rt"%><%@ 
	taglib uri="/struts-bean" prefix="bean" %><%@
	taglib uri="/kronos-html" prefix="v3html"%><%@
	taglib uri="/kronos-lists" prefix="v3lists"%><%@ 
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
<script type="text/javascript" src="<c:out value="${initParam['WFC.context.external']}"/>/applications/cms/html/scripts/cms.js"></script>
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
	
    var elements = document.getElementById('laborReqScheduleForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
</script>
 <html:hidden property="value(lrId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workorderId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workorderlnId)" styleId="khtmlWsConfigId" />
 <bean:define id="scheduleTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(schedules)" /> 
 <bean:define id="workmenListTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(workmenList)"/>
<%-- <bean:define id="tradeList" name="laborReqScheduleForm" property="value(tradeList)"/> --%>
<%-- <bean:define id="skillList" name="laborReqScheduleForm" property="value(skillList)"/> --%>
<%-- <bean:define id="shifts" name="laborReqScheduleForm" property="value(shifts)"/> --%>
<bean:define id="fromdtm" name="laborReqScheduleForm" property="value(fromdtm)"/>
<bean:define id="todtm" name="laborReqScheduleForm" property="value(todtm)"/>
 <bean:define id="patterns" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(patterns)" />
<div class="panel first-child">
<table id="khtmlCMSConfigListTable" class="ControlLayout" cellpadding="0" cellspacing="0">
	

		<tr>
			
			
			
				<th><label for="khtmlNameInput"><span></span><fmt:message key="label.wkNum"  /></label></th>
                             <td><html:text property="value(wkNum)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
				
				
				<th><label for="khtmlNameInput"><span></span><fmt:message key="label.lrname"  /></label></th>
                             <td><html:text property="value(lrname)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
			
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.workordertype"  /></label></th>
                             <td><html:text property="value(workordertype)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
			
			<tr>
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.validFrom"  /></label></th>
                             <td><html:text property="value(validfrom)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.validTo"  /></label></th>
                             <td><html:text property="value(validto)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
			
			</tr>
			<tr>
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.trade"  /></label></th>
                             <td><html:text property="value(trade)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
									
			
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.skill"  /></label></th>
                             <td><html:text property="value(skill)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>	
			
			<th><label for="khtmlNameInput"><span></span><fmt:message key="label.qty"  /></label></th>
                             <td><html:text property="value(qty)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>
			
				<th><label for="khtmlNameInput"><span></span><fmt:message key="label.shift"  /></label></th>
                             <td><html:text property="value(shift)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50"  /></td>					
				</tr>					
	
	</table>
	</div>
<%-- 	<%  String fromdtm =  (String) request.getAttribute("fromdtm");%> --%>
<%-- 	<%  String todtm =  (String) request.getAttribute("todtm");%> --%>
	
<div class="panel second-child">	
	<table id="detail" class="Controllayout" cellspacing="0" cellpadding="0" style="margin-bottom:1px;">
				<td width=50%>
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
										text_label_field="label.from" /></td>
	<td class="last-child" colspan="299">
               <kvl:button id="cms.lrsched.button" action="getEmployees()" /> 
               
    </td>           
	
	</table>
	</div>
 	
<%-- <v3lists:sort --%>
<%--  	var			= "webServiceConfigSorter"  --%>
<%--  	items		= "workmenListTable"  --%>
<%--  	property	= "state(cms_wo_state)"  --%>
<%--  	default		= "firstName">  --%>
<%--  <v3lists:paginate  --%>
<%-- 	var			= "paginate1"  --%>
<%--  	page		= "value(paginate_location_page)"  --%>
<%--  	items		= "workmenListTable"> --%>
	

<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0" style="margin-bottom:100px;">
 	<thead> 
 		<tr> 
 			<th class="ActionCell">
 			<input 	type="checkbox" 
								name="value(selectAll)" 
								class="selectAll"
								onclick="TableRowsSelection.doToggleSelectAll(this)">
			</th>
<%-- 			<tiles:insert name = "sort-ui" > --%>
<%-- 				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" /> --%>
<%-- 				<tiles:put name = "property" value	= "state(cms_c_state)" /> --%>
<%-- 				<tiles:put name = "fieldname" value	= "firstName" /> --%>
<%-- 				<tiles:put name = "displayname"> --%>
					<th> <fmt:message key="label.firstname" /></th>
<%-- 				</tiles:put> --%>
<%-- 				<tiles:put name = "cssClassName" value = "last-child" /> --%>
<%-- 			</tiles:insert>	 --%>
<%-- 			<tiles:insert name = "sort-ui" > --%>
<%-- 				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" /> --%>
<%-- 				<tiles:put name = "property" value	= "state(cms_c_state)" /> --%>
<%-- 				<tiles:put name = "fieldname" value	= "tradeName" /> --%>
<%-- 				<tiles:put name = "displayname"> --%>
					<th><fmt:message key="label.trade" /></th>
<%-- 				</tiles:put> --%>
<%-- 				<tiles:put name = "cssClassName" value = "last-child" /> --%>
<%-- 			</tiles:insert> --%>
<%-- 			<tiles:insert name = "sort-ui" > --%>
<%-- 				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" /> --%>
<%-- 				<tiles:put name = "property" value	= "state(cms_c_state)" /> --%>
<%-- 				<tiles:put name = "fieldname" value	= "skillName" /> --%>
<%-- 				<tiles:put name = "displayname"> --%>
					<th><fmt:message key="label.skill" /></th>
<%-- 				</tiles:put> --%>
<%-- 				<tiles:put name = "cssClassName" value = "last-child" /> --%>
<%-- 			</tiles:insert>				 --%>
<%-- 			<tiles:insert name = "sort-ui" > --%>
<%-- 				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" /> --%>
<%-- 				<tiles:put name = "property" value	= "state(cms_c_state)" /> --%>
<%-- 				<tiles:put name = "fieldname" value	= "skillName" /> --%>
<%-- 				<tiles:put name = "displayname"> --%>
					<th><fmt:message key="label.shift" /></th>
<%-- 				</tiles:put> --%>
<%-- 				<tiles:put name = "cssClassName" value = "last-child" /> --%>
<%-- 			</tiles:insert>				 --%>
						
		</tr>
	</thead>
<!-- 	<tfoot> -->
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
<!-- 	</tfoot> -->
	
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
				<input type="checkbox" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.empId}'/>" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.empId == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.empId}" />);document.getElementById('laborReqScheduleForm').doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.firstName}'/></a>
			
 			</td>			
 			<td class="last-child">
			<c:if test="${item.tradeId == null}">
 				&nbsp; 
			</c:if>
				<c:out value='${item.tradeName}' />
 			</td> 
 			<td class="last-child"> 
			<c:if test='${item.skillId == null}'>
 				&nbsp; 
			</c:if>
				<c:out value='${item.skillName}' />
 			</td> 
			
		<% String patternName = (String)request.getAttribute("patternName");%>
			<td  class="last-child">
			  <select id="selectedPE" name="value(<c:out value="${item.empId}" />_pattern)"> 
			    <option value="-1"><kvl:label id="cms.label.selectPattern"/></option>
				<c:forEach var="pats" items="${patterns}">
		        <option value="<c:out value="${pats}"/>"
				 <c:if test="${pats == patternName}">selected</c:if>
				>
		          <c:out value="${pats}"/>
		        </option>
		        </c:forEach>
			  </select>
			</td>
 			
			</tr> 
 	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
 	</tbody> 
 </table> 
<%-- </v3lists:paginate>  --%>
<%-- </v3lists:sort>  --%>

			
