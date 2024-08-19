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
	
    elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
function reset(){
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'value(wkNum)'){
    		elements[i].value = "";
    	}
    	else if(elements[i].name == 'value(sectionCode)'){
    		elements[i].value = "";
    	}
    }
    document.forms.workorderListForm.doAction ('<%= KronosProperties.get("cms.action.refresh").replaceAll("'","\\\\'") %>');
}
</script>

<bean:define id="webServiceConfigTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(workorders)" />
	
<v3lists:sort
	var			= "webServiceConfigSorter"
	items		= "webServiceConfigTable"
	property	= "state(cms_c_state)"
	default		= "wkNum">
<v3lists:paginate
	var			= "paginate1"
	page		= "value(paginate_location_page)"
	items		= "webServiceConfigTable">
	

<bean:define id="availUnitNames" name="workorderListForm" property="value(availUnitNames)"/>

<bean:define id="statusList" name="workorderListForm" property="value(statusList)"/>

<div class="Panel first-child">
  <table>
    <tr><td>
       <kvl:label id="cms.label.principalEmployer" required="true"/>
	</td>
	<% String unitId = (String)request.getAttribute("unitId");
		String statusId = (String)request.getAttribute("statusId");
	%>
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
   
   	<td  class="last-child">
	<kvl:label id="cms.label.status" required="true"/>
	  <select id="selectedStatus" name="value(statusId)"> 
	    <option value="-1"><kvl:label id="cms.label.selectStatus"/></option>
		<c:forEach var="status" items="${statusList}">
        <option value="<c:out value="${status.key}"/>"
		 <c:if test="${status.key == statusId}">selected</c:if>
		>
          <c:out value="${status.value}"/>
        </option>
        </c:forEach>
	  </select>
	</td>
    
	
	<td  class="last-child">
	<fmt:message key="label.wkNum" />
	  <html:text property="value(wkNum)" styleId="khtmlNameInput"
	  								size="20"
									maxlength="20"  />
	</td>
	<td  style="display:none"  class="last-child">
		<fmt:message key="label.or" />
	</td>
	<td style="display:none" class="last-child">
	<fmt:message key="label.section" />
	  <html:text property= "value(sectionCode)" size="20"
				styleId="khtmlNameInput"	maxlength="20"  />
	</td>
	
	<td>
	  <th>
             <kvl:button id="cms.label.search" action="">
             <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
             </kvl:button>
   </td>
      	<td>
	  <th>
                        <kvl:button id="cms.label.reset" action="reset()">
                        <html:button styleId="ResetButton" property="resetButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>     
  </th> </td>                                   
   
  </table>
  </div>

<v3lists:search 
    var="searchBean"
    items="webServiceConfigTable"
    property="unitId"
    pattern="value(selectedId)">


<table id="khtmlWebServiceConfigListTable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<th class="ActionCell">
			</th>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkNum" />
				<tiles:put name = "displayname">
					<fmt:message key="label.workordernumber" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "name" />
				<tiles:put name = "displayname">
					<fmt:message key="label.job" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkTypDispName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.workordertype" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<%-- <tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "sectionCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.section" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "sectionHead" />
				<tiles:put name = "displayname">
					<fmt:message key="label.sectionHead" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert> --%>
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "validFrom" />
				<tiles:put name = "displayname">
					<fmt:message key="label.validFrom" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "validTo" />
				<tiles:put name = "displayname">
					<fmt:message key="label.validTo" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
		<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "contractorName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.contractorname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "contractorCode" />
				<tiles:put name = "displayname">
					<fmt:message key="label.ccode" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<%-- <tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "unitName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.unitname" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	 --%>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "statusName" />
				<tiles:put name = "displayname">
					<fmt:message key="cms.label.status" />
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
		<c:set var="itemId" value="${item.workorderId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.workorderId}' />" <c:out value='${item.workorderId}'  /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.wkNum == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.workorderId}" />);document.forms.workorderListForm.doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.wkNum}'/></a>
			
			</td>
			<td class="last-child">
			<c:if test="${item.name == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.name}' />
			</td>
						
			<td class="last-child">
			<c:if test="${item.wkTypDispName == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.workorderId}" />);document.forms.workorderListForm.doAction('<%= KronosProperties.get("cms.action.edit").replaceAll("'","\\\\'") %>')"><c:out value='${item.wkTypDispName}'/></a>
			
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
			<c:if test="${item.validFrom == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.validFrom}' />
			</td>
			<td class="last-child">
			<c:if test="${item.validTo == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.validTo}' />
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
			<c:if test="${item.unitName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.unitName}' />
			</td> --%>
			<td class="last-child">
			<c:if test="${item.statusName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.statusName}' />
			</td>

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>

</v3lists:search>
</v3lists:paginate>
</v3lists:sort>

