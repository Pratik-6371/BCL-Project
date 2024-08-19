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
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
</script>

<bean:define id="webServiceConfigTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(cmsPricipalEmployerList)" />
	
<v3lists:sort
	var			= "webServiceConfigSorter"
	items		= "webServiceConfigTable"
	property	= "state(cms_pe_state)"
	default		= "unitname">
<v3lists:paginate
	var			= "paginate1"
	page		= "value(paginate_location_page)"
	items		= "webServiceConfigTable">

<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th class="ActionCell">
			</th>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "unitname" />
				<tiles:put name = "displayname">
					<fmt:message key="label.unitname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_pe_state)" />
				<tiles:put name = "fieldname" value	= "licensenumber" />
				<tiles:put name = "displayname">
					<fmt:message key="label.licensenumber" />
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
	
    <bean:size id="numConfigs" name="webServiceConfigTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${webServiceConfigTable}" varStatus="status">					
		<c:set var="itemId" value="${item.id}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.peid}' />"  <c:out value='${item.checked}' /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.unitname == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.peid}" />);document.getElementById('dataForm').doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.unitname}'/></a>
			
			</td>			
			<td class="last-child">
			<c:if test="${item.licensenumber == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.licensenumber}' />
			</td>

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</v3lists:paginate>
</v3lists:sort>
