<%@ 
	taglib uri="/struts-tiles" prefix="tiles"%><%@ 
	taglib uri="/struts-html" prefix="html"%><%@ 
	taglib uri="/jstl-format" prefix="fmt"%><%@     
	taglib uri="/jstl-core" prefix="c"%><%@ 
	taglib uri="/jstl-core-rt" prefix="c-rt"%><%@ 
	taglib uri="/struts-bean" prefix="bean" %><%@
	taglib uri="/kronos-html" prefix="v3html"%><%@
	taglib uri="/kronos-lists" prefix="v3lists"%><%@
	page import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties"
%> 
<%@ taglib tagdir = "/WEB-INF/tags/wpk/kvl" prefix = "kvl"%>
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<%@include file = "/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp"/>

<span id="khtmlWebServiceConfigListWorkspace" />
<script type="text/javascript">

function confirmDeleteWebConfigs(){
	var rows = new TableRowsSelection('khtmlWebServiceConfigTable')
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

function getWorkmen() {
	   if(checkUnsavedData()){
	        setDataChanged(false);
	        document.forms.workmenListForm.doAction ('<%= KronosProperties.get("cms.action.laborreq.refresh").replaceAll("'","\\\\'") %>');
	    }else{
	        false;
	    }
}
</script>

<bean:define id="cmsTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(cmsWorkmen)" />
<bean:define id="statusList" name="workmenListForm" property="value(availStatus)"/>
	<% 	String statusId = (String)request.getAttribute("statusId");
	%>
<html:hidden property="value(contrId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)"/>


<div class="Panel first-child">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td >
					<table >
						
						
						<tr><th><label for="khtmlNameInput"><span></span><fmt:message key="label.contractor.name"  /></label></th>
                             <td><html:text property="value(name)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
						</tr>	
						<tr><th><label for="khtmlNameInput"><span></span><fmt:message key="label.vendor.code"  /></label></th>
                             <td><html:text property="value(vendorCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
						</tr>
						<tr><th><kvl:label id="cms.label.status"/></th>
		  					<td><select id="selectedStatus" name="value(statusId)" onchange="getWorkmen();"> 
		    						<c:forEach var="status" items="${statusList}">
	        							<option value="<c:out value="${status.key}"/>"<c:if test="${status.key == statusId}">selected</c:if>>
		          							<c:out value="${status.value}"/>
	    	    						</option>
	        						</c:forEach>
		  						</select>
   							</td>
   						</tr>
   							
						<tr><th><label for="khtmlNameInput"><span></span>
							<fmt:message key="label.eCode" />
	  							<td> <html:text property="value(empcode)" styleId="khtmlNameInput"
	  								size="10"
									maxlength="20"  />
									</td>
						<td> <fmt:message key="label.or" /> </td>
						
						<th><label for="khtmlNameInput"><span></span>
							<fmt:message key="label.name" />
	  							<td> <html:text property="value(empname)" styleId="khtmlNameInput"
	  								size="20"
									maxlength="20"  />
									</td>
									
						 <th>
                        <kvl:button id="cms.label.search" action="">
                        <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>
						</td>						 
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>

<v3lists:sort
	var			= "departmentSorter"
	items		= "cmsTable"
	property	= "state(cms_deu_state)"
	default		= "firstname">
<v3lists:paginate
	var			= "paginate1"
	page		= "value(paginate_location_page)"
	items		= "cmsTable">

<table id="khtmlWebServiceConfigTable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th class="ActionCell">
			</th>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "departmentSorter" />
				<tiles:put name = "property" value	= "state(cms_deu_state)" />
				<tiles:put name = "fieldname" value	= "eCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.eCode" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "departmentSorter" />
				<tiles:put name = "property" value	= "state(cms_deu_state)" />
				<tiles:put name = "fieldname" value	= "firstname" />
				<tiles:put name = "displayname">
					<fmt:message key="label.firstname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "departmentSorter" />
				<tiles:put name = "property" value	= "state(cms_deu_state)" />
				<tiles:put name = "fieldname" value	= "lastname" />
				<tiles:put name = "displayname">
					<fmt:message key="label.lastname" />
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
	
    <bean:size id="numConfigs" name="cmsTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${cmsTable}" varStatus="status">					
		<c:set var="itemId" value="${item.id}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.id}' />"  <c:out value='${item.checked}' /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td class="last-child">
			<c:if test="${item.eCode == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.id}" />);document.forms.workmenListForm.doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.eCode}'/></a>
			</td>
			
			<td >
			<c:if test="${item.firstname == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.id}" />);document.forms.workmenListForm.doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.firstname}'/></a>
			
			</td>			
			<td class="last-child">
			<c:if test="${item.lastname == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.lastname}' />
			</td>

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</v3lists:paginate>
</v3lists:sort>

