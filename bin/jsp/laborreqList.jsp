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
	com.kronos.wfc.platform.utility.framework.ECMAEscapeUnescape,
	com.kronos.wfc.platform.utility.framework.list.ListFilter"
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
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
</script>
<html:hidden property="value(workorderId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(wkNum)" styleId="khtmlWsConfigId" />
<html:hidden property="value(sectionCode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(statusId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(pageId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(cameFrom)" styleId="khtmlWsConfigId" />
<bean:define id="laborReqConfigTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(lrs)" />
	
<v3lists:sort
	var			= "laborReqConfigSorter"
	items		= "laborReqConfigTable"
	property	= "state(cms_c_state)"
	default		= "approvedSw">
<v3lists:paginate
	var			= "paginate1"
	page		= "value(paginate_location_page)"
	items		= "laborReqConfigTable">
	

<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th class="ActionCell">
			</th>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "pageDispNum" />
				<tiles:put name = "displayname">
					<fmt:message key="label.laborreq" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkNumber" />
				<tiles:put name = "displayname">
					<fmt:message key="label.wkNum" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<%-- <tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "name" />
				<tiles:put name = "displayname">
					<fmt:message key="label.job" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert> --%>
				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkTypDispName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.workordertype" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "contractorName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.contractorname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "contractorCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.ccode" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			
			

			<%-- <tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "sectionCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.section" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				

			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "sectionHead" />
				<tiles:put name = "displayname">
					<fmt:message key="label.sectionHead" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	 --%>	
					
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "from" />
				<tiles:put name = "displayname">
					<fmt:message key="label.from" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "to" />
				<tiles:put name = "displayname">
					<fmt:message key="label.to" />
				</tiles:put>
		<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				

			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "laborReqConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "approvedSw" />
				<tiles:put name = "displayname">
					<fmt:message key="label.approved" />
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
	
	<tbody>
	
    <bean:size id="numConfigs" name="laborReqConfigTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${laborReqConfigTable}" varStatus="status">					
		<c:set var="itemId" value="${item.pageId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.pageId}' />" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.pageNum == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.pageId}" />);document.forms.laborReqListForm.doAction('<%= KronosProperties.get("cms.action.lrlist.view").replaceAll("'","\\\\'") %>')"><c:out value='${item.pageDispNum}'/></a>
			
			</td>
			<td class="last-child">
			<c:if test="${item.wkNumber == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.wkNumber}' />
			</td>
			<%-- <td class="last-child">
			<c:if test="${item.name == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.name}' />
			</td> --%>
						
			<td class="last-child">
			<c:if test="${item.wkTypDispName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.wkTypDispName}' />
			</td>
			<td class="last-child">
			<c:if test="${item.contractorName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.contractorName}' />
			</td>
			<td class="last-child">
			<c:if test="${item.contractorCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.contractorCode}' />
			</td>
			
			<%-- <td class="last-child">
			<c:if test="${item.sectionCode == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sectionCode}' />
			</td>
			<td class="last-child">
			<c:if test="${item.sectionHead == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.sectionHead}' />
			</td> --%>
			<td class="last-child">
			<c:if test='${item.from == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.from}' />
			</td>
			
			<td class="last-child">
			<c:if test='${item.to == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.to}' />
			</td>

			<td class="last-child">
			<c:if test="${item.approvedSw == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.approvedSw}' />
			</td>

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</v3lists:paginate>
</v3lists:sort>
