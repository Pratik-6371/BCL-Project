
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
	prefix="kvl"%><%@page
	import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties,
	com.kronos.wfc.platform.security.business.authorization.controlpoints.AccessControlPoint,
                 com.kronos.wfc.platform.security.business.authorization.actions.Action,
                 com.kronos.wfc.platform.security.business.authorization.profiles.AccessProfile,
                 com.kronos.wfc.platform.utility.framework.list.ListFilter,
                 com.kronos.wfc.platform.persistence.framework.ObjectIdLong"%>
                 
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


 <% String nameFieldSize = KronosProperties.get("cms.name.maxlength", "25"); %> 
 <% String propertyValueFieldSize = KronosProperties.get(" cms.property.value.maxlength", "250"); %>


<script type="text/javascript"
	src='<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>'></script>
<script type="text/javascript" src="<c:out value="${initParam['WFC.context.external']}"/>/applications/cms/html/scripts/cms.js"></script>

<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(statusId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(paginate_location_page)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workorderId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(wkLineAvail)" styleId="khtmlWsConfigId" />
<html:hidden property="value(wkNum)" styleId="khtmlWsConfigId" />
<html:hidden property="value(sectionCode)" styleId="khtmlWsConfigId" />
<bean:define id="validFrom" name="workorderForm" property="value(validFrom)"/>
<bean:define id="validTo" name="workorderForm" property="value(validTo)"/>
<bean:define id="cmsConfigProperties" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(activityList)" />
<bean:define id="wkLines" name="workorderForm" property="valueAsList(wkLines)"/>
<bean:define id="availSectionNames" name="workorderForm" property="value(availSectionNames)"/>
<bean:define id="availDepartmentNames"  name="workorderForm" property="value(availDepartmentNames)"/>

<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<table>
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.workordernumber" /></label></th>
							<td><html:text property="value(number)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
							<%-- <th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.unitname" /></label></th>
							<td><html:text property="value(unitName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td> --%>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.workordertype" /></label></th>
							<td><html:text property="value(workorderDispName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.contractorname" /></label></th>
							<td><html:text property="value(contractorName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
						</tr>
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.vendorcode" /></label></th>
							<td><html:text property="value(vendorCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.validFrom" /></label></th>
							<td width=33%>
							<input id="value(validFrom)" 
											name="value(validFrom)" 
											onchange="setDataChanged(true);" 
											type=text size=20 readonly ="true" disabled ="true" value="<c:out value="${validFrom}"/>">
											</input>
											
							</td>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.validTo" /></label></th>
							<td width=33%> 
								<input id="value(validTo)" 
											name="value(validTo)" 
										type=text size=20 readonly ="true" disabled ="true" value="<c:out value="${validTo}"/>"></td>
					</tr>
						
					<%-- <tr>	
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.workordertype" /></label></th>
							<td><html:text property="value(workorderDispName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td>
						
							<% ObjectIdLong dId = (ObjectIdLong)request.getAttribute("depId"); 
							   ObjectIdLong sId = (ObjectIdLong)request.getAttribute("sId");
							 %>
							<th><label for="khtmlNameInput"><span></span>
							<fmt:message key="label.code" /></label></th>
							<td>
								  <select id="selectId" name="value(depId)"onchange="setDataChanged();"> 
								    <c:forEach var="department" items="${availDepartmentNames}">
							        <option value="<c:out value="${department.depid}"/>" <c:if test="${department.depid != dId}">disabled</c:if> <c:if test="${department.depid == dId}">selected</c:if>>
							          <c:out value="${department.code}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							</td>	  

							<th><label for="khtmlNameInput"><span></span>
							<fmt:message key="cms.label.section" /></label></th>
							<td>
								  <select id="selectedId" name="value(secId)" onchange="setDataChanged();"> 
								     <c:forEach var="section" items="${availSectionNames}">
							        <option value="<c:out value="${section.sectionId}"/>" <c:if test="${section.sectionId != sId}">disabled</c:if> <c:if test="${section.sectionId == sId}">selected</c:if>>
							          <c:out value="${section.name}"/>
							   		 </option>
							        </c:forEach>
								  </select>
							
							</td>
						</tr> --%>
						
						<tr>
							<%-- <th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.costcenter" /></label></th>
							<td><html:text property="value(costcenter)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td> --%>
						
							<%-- <th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.glcode" /></label></th>
							<td><html:text property="value(glcode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td> --%>
							<%--<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.jobdesc" /></label></th>
							 <td><html:text property="value(name)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" disabled="true" /></td> --%>
							<th><label for="khtmlNameInput"><span></span>
														<fmt:message key="cms.label.workorderowner" /></label></th>
													<td><html:text property="value(sectionHead)"
															styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
															maxlength="50" readonly="true" disabled="true" /></td>
							
							<th><label for="khtmlNameInput"><span></span>
														<fmt:message key="cms.label.supervisor" /></label></th>
													<td><html:text property="value(supervisor)"
															styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
															maxlength="50" readonly="true" disabled="true" /></td>		
						</tr>

				</table>
	
<div class="panel second-child">	
	<table id="khtmlworkorderlntable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
					<th><kvl:resource id="label.itemNumber"/></th>
					<th><kvl:resource id="label.serviceLineItemNumber"/></th>
					<th><kvl:resource id="label.job"/></th>
					<th><kvl:resource id="label.unitname"/></th>
					<th><kvl:resource id="label.code"/></th>
					<th><kvl:resource id="cms.label.section"/></th>
					<th><kvl:resource id="label.glcode"/></th>
					<th><kvl:resource id="label.costcenter"/></th>
					<th><kvl:resource id="label.serviceCode"/></th>
					<th><kvl:resource id="label.trade"/></th>
					<th><kvl:resource id="label.skill"/></th>
					<th><kvl:resource id="label.wlqty"/></th>
				  <%--   <th><kvl:resource id="label.balance.quantity"/></th> --%>
					<th><kvl:resource id="label.rate"/></th>
					<th><kvl:resource id="label.qtyCompleted"/></th>
				    <th><kvl:resource id="label.wbselement"/></th>
				    <th><kvl:resource id="label.uom"/></th>
		</tr>
	</thead>
	<tbody>
	
	
    <bean:size id="numConfigs" name="wkLines"/>
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${wkLines}" varStatus="status">					
		<c:set var="itemId" value="${item.wkLineId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
<!-- 			<td> -->
<%-- 				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.wkLineId}' />" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" /> --%>
<!-- 			</td> -->

			<td class="last-child">
			<c:if test="${item.itemNumber == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.itemNumber}' />
			</td>


			<td class="last-child">
			<c:if test="${item.serviceLineItemNumber == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.serviceLineItemNumber}' />
			</td>

			<td class="last-child">
			<c:if test="${item.jobDesc == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.jobDesc}' />
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
			<%String glCode = (String)request.getAttribute("glcode");
			if(glCode == null)
			{
			%>
				&nbsp;
			<% }else{ %>
				<c:out value= '<%=glCode%>' />
				<% } %>
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
				<c:out value='${item.tradeNm}'/>
			
			</td>			
			<td class="last-child">
			<c:if test="${item.skillId == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.skillNm}' />
			</td>
			
			<td class="last-child">
			<c:if test="${item.qty == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.qty}' />
			</td>
			
			<%-- <td class="last-child">
			<c:if test="${item.balanceQuantity == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.balanceQuantity}' />
			</td> --%>
			
			<td class="last-child">
			<c:if test="${item.rate == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.rate}' />
			</td>
			<td class="last-child">
			<c:if test="${item.qtyCompleted == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.qtyCompleted}' />
			</td>
		    <td class="last-child">
			<c:if test="${item.wbsElement == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.wbsElement}' />
			</td>
		    <td class="last-child">
			<c:if test="${item.uom == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.uom}' />
			</td>


		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</div>
</td>
</tr>
</tbody>
</table>
</div>
					
