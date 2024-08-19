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
	
    var elements = document.getElementById('minimumwagelistForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
</script>

<bean:define id="minimumWageTable" name="minimumwagelistForm" property="valueAsList(cmsMinimumWageList)" />
<bean:define id="availUnitNames" name="minimumwagelistForm" property="value(availUnitNames)"/>
<bean:define id="validFrom" name="minimumwagelistForm" property="value(validFrom)"/>
<div class="Panel first-child">
  <table>
    <tr><td>
       <kvl:label id="cms.label.principalEmployer" required="true"/>
	</td>
	<% String unitId = (String)request.getAttribute("unitId");	%>
	<td  class="last-child">
	  <select id="selectedPE" name="value(unitId)"> 
	    <option value="-1"><kvl:label id="cms.label.selectUnitName"/></option>
		<c:forEach var="principalEmployer" items="${availUnitNames}">
        <option value="<c:out value="${principalEmployer.unitId}"/>"
		 <c:if test="${principalEmployer.unitId == unitId}">selected</c:if>
		>
          <c:out value="${principalEmployer.unitName}"/>
        </option>
        </c:forEach>
	  </select>
	</td>
	<th><label for="khtmlNameInput"><span></span>
					<fmt:message key="label.searchDate" /></label></th>
						<td width=33%>
						<input id="value(validFrom)" 
										name="value(validFrom)" 
										type=text size=20  value="<c:out value="${validFrom}"/>">
							<kvl:date-selector-eot-bot-popup 
										id="value(validFrom)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" />
								</input>
										
						</td>
	<td>
	  <th>
                        <kvl:button id="cms.label.search" action="">
                        <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>
   
  </table>
  </div>

	
<v3lists:sort
	var			= "webServiceConfigSorter"
	items		= "minimumWageTable"
	property	= "state(cms_pe_state)"
	default		= "trade">
<v3lists:paginate
	var			= "paginate1"
	page		= "value(paginate_location_page)"
	items		= "minimumWageTable">

<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th class="ActionCell">
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "from" />
				<tiles:put name = "displayname">
					<fmt:message key="label.effectiveFrom" />
				</tiles:put>
 				<tiles:put name = "cssClassName" value = "last-child" /> 
 			</tiles:insert>	 
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "trade" />
				<tiles:put name = "displayname">
					<fmt:message key="label.trade" />
				</tiles:put>
 				<tiles:put name = "cssClassName" value = "last-child" /> 
 			</tiles:insert>	 
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "skillset" />
				<tiles:put name = "displayname">
					<fmt:message key="label.skill" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "minimumwage" />
				<tiles:put name = "displayname">
					<fmt:message key="label.minimumwage" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "daperday" />
				<tiles:put name = "displayname">
					<fmt:message key="label.daperday" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "otherallowance" />
				<tiles:put name = "displayname">
					<fmt:message key="label.otherallowance" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>		
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "total" />
				<tiles:put name = "displayname">
					<fmt:message key="label.Total" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>		

		</tr>
	</thead>
	<tfoot>
	
		<tr class="TablePaginator">
			<td colspan="299" cLass="last-child">    
				<div id="paginate1">
				<tiles:insert name = "paginator-ui">
					<tiles:put name	= "paginator" beanName = "paginate1" />
					<tiles:put name	= "property" value = "value(paginate_location_page)" />
					<tiles:put name	= "showAll" value = "true" />
				</tiles:insert>
				</div>
			</td>
		</tr>
	</tfoot>
	<tr>

	</td>
	</tr>
	
	<tbody>
	
	
	
    <bean:size id="numConfigs" name="minimumWageTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	 
	 <c:forEach var = "item" items = "${minimumWageTable}" varStatus="status">	
	 	<c:set var="itemId" value="${item.id}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			
		<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.id}' />"  <c:out value='${item.checked}' /> onclick="TableRowsSelection.doSelectRow(this)" />
		</td>
		<td class="last-child">
			<c:if test="${item.from == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.from}' />
			</td>
		
			<td class="last-child">
			<c:if test="${item.trade == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.trade}' />
			</td>
			<td class="last-child">
			<c:if test="${item.skillset == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.skillset}' />
			</td>
			<td class="last-child">
			<c:if test="${item.minimumwage == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.minimumwage}' />
			</td>
			<td class="last-child">
			<c:if test="${item.daperday == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.daperday}' />
			</td>
			<td class="last-child">
			<c:if test="${item.otherallowance == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.otherallowance}' />
			</td>
			<td class="last-child">
			<c:if test="${item.total == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.total}' />
			</td>

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</v3lists:paginate>
</v3lists:sort>

