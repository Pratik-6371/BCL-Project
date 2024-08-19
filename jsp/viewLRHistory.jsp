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
function reset(){
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'value(wkNumber)'){
    		elements[i].value = "";
    	}
    	else if(elements[i].name == 'value(fromDtm)'){
    		elements[i].value = "";
    	}
    	else if(elements[i].name == 'value(toDtm)'){
    		elements[i].value = "";
    	}
    }
    document.forms.viewlrHistoryForm.doAction ('<%= KronosProperties.get("cms.action.laborreq.refresh").replaceAll("'","\\\\'") %>');
}

function getLRS() {
	   if(checkUnsavedData()){
	        setDataChanged(false);
	        document.forms.viewlrHistoryForm.doAction ('<%= KronosProperties.get("cms.action.laborreq.refresh").replaceAll("'","\\\\'") %>');
	    }else{
	        false;
	    }
}
</script>
<html:hidden property="value(selectedId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(cameFrom)" styleId="khtmlWsConfigId" />
<html:hidden property="value(pageId)" styleId="khtmlWsConfigId" />
<bean:define id="webServiceConfigTable" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(lrRows)" />
<bean:define id="fromDtm" name="viewlrHistoryForm" property="value(fromDtm)"/>
<bean:define id="toDtm" name="viewlrHistoryForm" property="value(toDtm)"/>
<bean:define id="statusList" name="viewlrHistoryForm" property="value(availStatus)"/>

	<% 	String statusId = (String)request.getAttribute("statusId");
	%>

<div class="Panel first-child">
  <table>
    <tr>
	<td  class="last-child">
	<fmt:message key="label.wkNum" />
	  <html:text property="value(wkNum)"
									styleId="khtmlNameInput" size="20"
									maxlength="20"  />
									<fmt:message key="label.or" />
	<fmt:message key="label.section" />
	  <html:text property="value(sectionCode)"
									styleId="khtmlNameInput" size="20"
									maxlength="20"  />
	
						<fmt:message key="label.from" />
						<input id="value(fromDtm)"
										name="value(fromDtm)"
										type=text size=20 value="<c:out value="${fromDtm}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(fromDtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" />
	
						<fmt:message key="label.to" />
						<input id="value(toDtm)" 
										name="value(toDtm)" 
										 
										type=text size=20 value="<c:out value="${toDtm}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(toDtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td>
	
	<td>
	  <th>
                        <kvl:button id="cms.label.search" action="">
                        <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>     
  </th> </td>
  	<td>
	  <th>
                        <kvl:button id="cms.label.reset" action="reset()">
                        <html:button styleId="ResetButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>     
  </th> </td>    
  <td width="10%"/>
                                 
  <td> 
  		<kvl:label id="cms.label.status" required="true"/>
		  <select id="selectedStatus" name="value(statusId)" onchange="getLRS();"> 
		    <c:forEach var="status" items="${statusList}">
	        <option value="<c:out value="${status.key}"/>"
			 <c:if test="${status.key == statusId}">selected</c:if>
			>
	          <c:out value="${status.value}"/>
	        </option>
	        </c:forEach>
		  </select>
   </td>                                  
  </table>
  </div>





	
<v3lists:sort
	var			= "webServiceConfigSorter"
	items		= "webServiceConfigTable"
	property	= "state(cms_c_state)"
	default		= "approvedSw"
	reverseOrder =  "reverse">
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
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "pageNum" />
				<tiles:put name = "displayname">
					<fmt:message key="label.laborreq" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>	
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkNum" />
				<tiles:put name = "displayname">
					<fmt:message key="label.wkNum" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "name" />
				<tiles:put name = "displayname">
					<fmt:message key="label.jobdesc" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "wkTypName" />
				<tiles:put name = "displayname">
					<fmt:message key="label.workordertype" />
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
						<tiles:insert name = "sort-ui" >
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
			</tiles:insert>
			
			
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "from" />
				<tiles:put name = "displayname">
					<fmt:message key="label.from" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				
			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
				<tiles:put name = "property" value	= "state(cms_c_state)" />
				<tiles:put name = "fieldname" value	= "to" />
				<tiles:put name = "displayname">
					<fmt:message key="label.to" />
				</tiles:put>
				<tiles:put name = "cssClassName" value = "last-child" />
			</tiles:insert>				

			<tiles:insert name = "sort-ui" >
				<tiles:put name = "sorter" beanName	= "webServiceConfigSorter" />
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
	
    <bean:size id="numConfigs" name="webServiceConfigTable"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${webServiceConfigTable}" varStatus="status">					
		<c:set var="itemId" value="${item.pageId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td>
				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.pageId}' />" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" />
			</td>
			<td >
			<c:if test="${item.pageNum == null}">
				&nbsp;
			</c:if>
				<a href="javascript:setSelectedItem(<c:out value="${item.pageId}" />);document.forms.viewlrHistoryForm.doAction('<%= KronosProperties.get("cms.action.laborreq.view.lr").replaceAll("'","\\\\'") %>')"><c:out value='${item.pageDispNum}'/></a>
			
			</td>			
			<td class="last-child">
			<c:if test="${item.wkNum == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.wkNum}' />
			</td>
			<td class="last-child">
			<c:if test="${item.name == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.name}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.wkTypName == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.wkTypName}' />
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
			<td class="last-child">
			<c:if test='${item.sectionCode == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.sectionCode}' />
			</td>
			
			<td class="last-child">
			<c:if test='${item.sectionHead == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.sectionHead}' />
			</td>
			
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
