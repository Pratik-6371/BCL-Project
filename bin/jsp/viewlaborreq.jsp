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
	prefix="kvl"%><%@
	
	page
	import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties,
	com.kronos.wfc.platform.security.business.authorization.controlpoints.AccessControlPoint,
                 com.kronos.wfc.platform.security.business.authorization.actions.Action,
                 com.kronos.wfc.platform.security.business.authorization.profiles.AccessProfile,
                 com.kronos.wfc.platform.utility.framework.list.ListFilter"%>
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

<html:hidden property="value(statusId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workorderId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(cameFrom)" styleId="khtmlWsConfigId" />
<html:hidden property="value(workordertype)" styleId="khtmlWsConfigId" />
<html:hidden property="value(wkNum)" styleId="khtmlWsConfigId" />
<html:hidden property="value(sectionCode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(fromDtm)" styleId="khtmlWsConfigId" />
<html:hidden property="value(toDtm)" styleId="khtmlWsConfigId" />
<bean:define id="lrLines" name="viewLaborReqForm" property="valueAsList(lrLines)"/>
<bean:define id="wlLines" name="viewLaborReqForm" property="valueAsList(wlLines)"/>
<bean:define id="availShifts" name="viewLaborReqForm" property="value(availShifts)"/>
<bean:define id="tradeList" name="viewLaborReqForm" property="value(tradeList)"/>
<bean:define id="skillList" name="viewLaborReqForm" property="value(skillList)"/>
<bean:define id="fromdtm" name="viewLaborReqForm" property="value(fromdtm)"/>
<bean:define id="todtm" name="viewLaborReqForm" property="value(todtm)"/>

<div class="Panel first-child">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<table>

						<!--   cms  contractor-->

						<tr>
							<%-- <th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.unitname" /></label></th>
							<td><html:text property="value(unitName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td> --%>
						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.wkNum" /></label></th>
							<td><html:text property="value(wkNumber)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
									
						<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.lrRequest" /></label></th>
							<td><html:text property="value(lrDispNum)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>			
						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.workordertype" /></label></th>
							<td><html:text property="value(workorderDispName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
									
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.glcode" /></label></th>
							<td><html:text property="value(glcode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
						</tr>
						
						<tr>						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.contractorname" /></label></th>
							<td><html:text property="value(contractorName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
									
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.vendorcode" /></label></th>
							<td><html:text property="value(vendorCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
						
						
						<th><label for="khtmlNameInput"><span></span>
							<fmt:message key="label.validFrom" /></label></th>
						<td><html:text property="value(validFrom)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"/></td>
										
						
						<th><label for="khtmlNameInput"><span></span>
							<fmt:message key="label.validTo" /></label></th>
						 
						<td><html:text property="value(validTo)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
						</tr>
						
						
						<%-- <tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.code" /></label></th>
							<td><html:text property="value(depCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.section" /></label></th>
							<td><html:text property="value(secCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
							
									
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.costcenter" /></label></th>
							<td><html:text property="value(costcenter)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
									
						</tr> --%>
						<%-- <tr>
						<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.jobdesc" /></label></th>
							<td><html:text property="value(name)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true"  /></td>
						
						</tr>	 --%>
						<!--  end -->


					</table>
</td>
</tr></tbody></table>
<div class="panel second-child">	
	<table id="khtmlworkorderlntable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>
			<th colspan=10> <fmt:message key="label.workorder.req" />
		<tr>
<!-- 			<th class="ActionCell"> -->
<!-- 			</th> -->
					<th><kvl:resource id="label.job"/></th>
					<th><kvl:resource id="label.unitname"/></th>
					<th><kvl:resource id="label.code"/></th>
					<th><kvl:resource id="cms.label.section"/></th>
					<th><kvl:resource id="label.costcenter"/></th>
					<th><kvl:resource id="label.serviceCode"/></th>
					<th><kvl:resource id="label.trade"/></th>
					<th><kvl:resource id="label.skill"/></th>
					<th><kvl:resource id="label.wlqty"/></th>
					<th><kvl:resource id="label.qtyCompleted"/></th>
					
		</tr>
	</thead>
	<tbody>
	
    <bean:size id="numConfigs" name="wlLines"/>
    
    <c:if test="${numConfigs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${wlLines}" varStatus="status">					
		<c:set var="itemId" value="${item.wkLineId}"/>
		
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
<!-- 			<td> -->
<%-- 				<input type="radio" class="Selected" name="valueAsStringArray(selectedIds)" value="<c:out value='${item.wkLineId}' />" <c:out value='${item.checked}'  /> onclick="TableRowsSelection.doSelectRow(this)" /> --%>
<!-- 			</td> -->
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
			<td class="last-child">
			<c:if test="${item.qtyCompleted == null}">
				&nbsp;
			</c:if>
				<c:out value='${item.qtyCompleted}' />
			</td>


		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
</div>
<div class="Panel third-child">
 	<table id="detail" class="Controllayout" cellspacing="0" cellpadding="0" style="margin-bottom:1px;">
 	 <th colspan=8> <fmt:message key="label.lr.req" />
				<td width=20%>
				</td>
						<td>
						<fmt:message key="label.from" />
						<input id="value(fromdtm)"
										name="value(fromdtm)"
										type=text size=20 disabled= "true" value="<c:out value="${fromdtm}"/>">
						</td>
						<td>
						<fmt:message key="label.to" />
						<input id="value(todtm)" 
										name="value(todtm)" 
										type=text size=20 disabled= "true" value="<c:out value="${todtm}"/>">
						</td>
											<td>					
					<th><label for="khtmlNameInput">
								<fmt:message key="label.weeklyOffDays" /></label></th>
							<td><html:text property="value(weeklyOffDays)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="3"
									maxlength="4"/></td>	
					</td>														
						
						<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.remark" /></label></th>
							<td><html:text property="value(remark)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" disabled="true"/></td>							
						
	</table>
 
</div>
<div class="Panel fourth-child">
<table id="khtmllrlntable" class="Tabular" cellpadding="0" cellspacing="0">
	<thead>	 	  
		<tr>	
					<th><kvl:resource id="label.job"/></th>
					<%-- <th><kvl:resource id="label.balance"/></th> --%>
					<th><kvl:resource id="label.unitname"/></th>
					<th><kvl:resource id="label.code"/></th>
					<th><kvl:resource id="cms.label.section"/></th>
					<th><kvl:resource id="label.costcenter"/></th>
					<th><kvl:resource id="label.serviceCode"/></th>
					<th><kvl:resource id="label.trade"/></th>
					<th><kvl:resource id="label.skill"/></th>
					<th><kvl:resource id="label.shift_A"/></th>
					<th><kvl:resource id="label.sched_shift_A"/></th>
					<th><kvl:resource id="label.shift_B"/></th>
					<th><kvl:resource id="label.sched_shift_B"/></th>
					<th><kvl:resource id="label.shift_C"/></th>
					<th><kvl:resource id="label.sched_shift_C"/></th>
					<th><kvl:resource id="label.shift_G"/></th>
					<th><kvl:resource id="label.sched_shift_G"/></th>

					
		</tr>
	</thead>
	<tbody>
	<% String workordertyp = (String)request.getAttribute("workordertyp"); %>
    <bean:size id="numConfigLrs" name="lrLines"/>
    <c:if test="${numConfigLrs < 1}">
		<tr><td class="last-child" colspan="299"><kvl:resource id='i18n.wfc.displayEmptyTableMessage'/></td></tr>
    </c:if>
    <c:set var="rowIndex" value="${0}"/>
	
	 <c:forEach var = "item" items = "${lrLines}" varStatus="status">					
		<c:set var="itemId" value="${item.lrId}"/>
		<jsp:useBean id="itemId" type="java.lang.String" />
		<html:hidden property="valueAsStringArray(propertyIds)" value="<%=itemId%>"/>
			
		<tr class="last-child <c:if test='${rowIndex % 2 !=  0}'>Even</c:if>">
			<td class="last-child">
			<c:choose>					
			<c:when test="${workordertyp == 'A'}">
			<input  name="value(<c:out value="${item.lrId}"/>_itemDesc)" value="<c:out value="${item.itemDesc}"/>"
				styleId="khtmlNameInput" onchange="setDataChanged();" size=50 maxlength="50" disabled ="true"/> 
					
			</c:when>
			<c:otherwise>
				          <c:out value="${item.itemDesc}"/>
			</c:otherwise>
			</c:choose>
			</td>

		<%-- <td class="last-child">
			<c:if test='${item.balanceQty == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.balanceQty}' />
		</td> --%>
		
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
			<c:if test='${item.serviceCode == null}'>
				&nbsp;
			</c:if>
				<c:out value='${item.serviceCode}' />
		</td>

			<td class="last-child">
			<c:choose>					
			<c:when test="${workordertyp == 'A'}">
			<input name="value(<c:out value="${item.lrId}"/>_trade)" value="<c:out value="${item.trade}"/>"
				styleId="khtmlNameInput" onchange="setDataChanged();" size=20 maxlength="20" disabled ="true"/> 

				
			</c:when>
			<c:otherwise>
				          <c:out value="${item.trade}"/>

			</c:otherwise>
			
			</c:choose>
				
			
			</td>
			
			  			
			<td class="last-child">
			<c:choose>
			
			<c:when test="${workordertyp == 'A'}">
			<input name="value(<c:out value="${item.lrId}"/>_skill)" value="<c:out value="${item.skill}"/>"
									styleId="khtmlNameInput" onchange="setDataChanged();" size=20 maxlength="20" disabled ="true"/> 
			
			</c:when>
			<c:otherwise>
		          <c:out value="${item.skill}"/>
			</c:otherwise>
			</c:choose>
			</td>

			<td class="last-child">
			<c:if test="${item.shift_A== null}">
				&nbsp;
			</c:if>
				<input name="value(<c:out value="${item.lrId}"/>_shift_A)" value="<c:out value="${item.shift_A}"/>"
									styleId="khtmlNameInput" onchange="setDataChanged();" size=3 maxlength="4" disabled="true"/>
									
			</td>
			<td class="last-child">
			<c:if test="${item.sched_A_qty== null}">
				&nbsp;
			</c:if>
				<c:out value="${item.sched_A_qty}"/>
			</td>
			
			<td class="last-child">
			<c:if test="${item.shift_B == null}">
				&nbsp;
			</c:if>
				<input name="value(<c:out value="${item.lrId}"/>_shift_B)" value="<c:out value="${item.shift_B}"/>"
									styleId="khtmlNameInput" onchange="setDataChanged();" size=3 maxlength="4" disabled="true"/>
			</td>
			<td class="last-child">
			<c:if test="${item.sched_B_qty== null}">
				&nbsp;
			</c:if>
				<c:out value="${item.sched_B_qty}"/>
			</td>
			
			
			<td class="last-child">
			<c:if test="${item.shift_C == null}">
				&nbsp;
			</c:if>
				<input name="value(<c:out value="${item.lrId}"/>_shift_C)" value="<c:out value="${item.shift_C}"/>"
									styleId="khtmlNameInput" onchange="setDataChanged();" size=3 maxlength="4" disabled="true"/>
													
			</td>

			<td class="last-child">
			<c:if test="${item.sched_C_qty== null}">
				&nbsp;
			</c:if>
				<c:out value="${item.sched_C_qty}"/>
			</td>
			
			<td class="last-child">
			<c:if test="${item.shift_G == null}">
				&nbsp;
			</c:if>
				<input name="value(<c:out value="${item.lrId}"/>_shift_G)" value="<c:out value="${item.shift_G}"/>"
									styleId="khtmlNameInput" onchange="setDataChanged();" size=3 maxlength="4" disabled="true"/>
									
			</td>
			
			<td class="last-child">
			<c:if test="${item.sched_G_qty== null}">
				&nbsp;
			</c:if>
				<c:out value="${item.sched_G_qty}"/>
			</td>
			

		</tr>
	    <c:set var="rowIndex" value="${rowIndex+1}"/>
	</c:forEach> 
				
	</tbody>
</table>
 </div>					

