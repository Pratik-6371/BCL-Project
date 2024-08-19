<%@page import="com.kronos.wfc.platform.persistence.framework.ObjectIdLong"%>
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

<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>

<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(contrId)"styleId="khtmlWsConfigId"/>
<html:hidden property="value(unitId)"styleId="khtmlWsConfigId"/>
<html:hidden property="value(selectedId)"styleId="khtmlWsConfigId"/>
<bean:define id="dob" name="workmenDetailForm" property="value(dob)"/>
<bean:define id="doj" name="workmenDetailForm" property="value(doj)"/>
<bean:define id="dot" name="workmenDetailForm" property="value(dot)"/>
<bean:define id="amccheckup" name="workmenDetailForm" property="value(amccheckup)"/>
<bean:define id="availSKillNames" name="workmenDetailForm" property="value(availSkillNames)"/>
<bean:define id="availPStateNames" name="workmenDetailForm" property="value(availPStateNames)"/>
<bean:define id="availPermStateNames" name="workmenDetailForm" property="value(availPermStateNames)"/>
<bean:define id="availTradeNames" name="workmenDetailForm" property="value(availTradeNames)"/>
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody> 
			<tr>
				<td >
					<table >
						
		<!--   cms  -->
						
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.eCode" /></label></th>
							<td ><html:text property="value(eCode)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" readonly="true" disabled="true"/></td>
						</tr>
						
						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.firstname" /></label></th>
							<td ><html:text property="value(firstname)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50"  /></td>
						</tr>
						
						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.lastname" /></label></th>
							<td ><html:text property="value(lastname)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.supplyType" /></label></th>
							<td ><html:text property="value(supplyType)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" readonly="true" disabled="true"/></td>
						</tr>

							<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.natureOfJob" /></label></th>
						</tr>
						<% ObjectIdLong tId = (ObjectIdLong)request.getAttribute("tId"); %>
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.trade" /></label></th>
						<td  class="last-child">
						  <select id="value(tradeId)" name="value(tradeId)"> 
						    <option value="-1"><kvl:label id="cms.label.selectTrade"/></option>
						    <c:forEach var="trade" items="${availTradeNames}">
					        <option value="<c:out value="${trade.tradeId}"/>"
							 <c:if test="${trade.tradeId == tId}">selected</c:if>>
					          <c:out value="${trade.tradeName}"/>
					   		  </option>
					        </c:forEach>
						  </select>
						</td>

						</tr>
					<% ObjectIdLong skId = (ObjectIdLong)request.getAttribute("skId"); %>
					<tr>
						<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.skill" /></label></th>
						<td  class="last-child">
						  <select id="value(skillId)" name="value(skillId)"> 
						    <option value="-1"><kvl:label id="cms.label.selectSkill"/></option>
						    <c:forEach var="skill" items="${availSKillNames}">
					        <option value="<c:out value="${skill.skillId}"/>"
							 <c:if test="${skill.skillId == skId}">selected</c:if>>
					          <c:out value="${skill.skillNm}"/>
					   		  </option>
					        </c:forEach>
						  </select>
						</td>
					</tr>
	
						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.fathername" /></label></th>
							<td ><html:text property="value(relationName)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						
						<tr>
						<th><label for="khtmlNameInput"><span>*</span>
							<fmt:message key="label.dateofbirth" /></label></th>
						<td width=33%>
						<input id="value(dob)" 
										name="value(dob)" 
										onchange="setDataChanged(true);" 
										type=text size=20 value="<c:out value="${dob}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(dob)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.dateofbirth" /></td>
						</tr>
						
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.identificationmark" /></label></th>
							<td ><html:text property="value(idmark)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						
	
						
<%-- 						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.mobileno" /></label></th> --%>
<%-- 							<td ><html:text property="value(mobileno)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td> --%>
<!-- 						</tr> -->
				
					<tr>
						<th ><label for="khtmlNameInput"><fmt:message key="label.presentAddress" /></label></th>
					</tr>	
					<% ObjectIdLong pStateId = (ObjectIdLong)request.getAttribute("pStateId"); %>	
					<tr>
						<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.state" /></label></th>
						<td  class="last-child">
						  <select id="value(pStateId)" name="value(pStateId)"> 
						    <option value="-1"><kvl:label id="cms.label.selectState"/></option>
						    <c:forEach var="state" items="${availPStateNames}">
					        <option value="<c:out value="${state.stateId}"/>"
							 <c:if test="${state.stateId == pStateId}">selected</c:if>>
					          <c:out value="${state.stateNm}"/>
					   		  
					        </option>
					        </c:forEach>
						  </select>
						</td>
						</tr>
						
						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.district" /></label></th>
							<td ><html:text property="value(pDistrict)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>

						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.taluka" /></label></th>
							<td ><html:text property="value(pTaluka)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.village" /></label></th>
							<td ><html:text property="value(pVillage)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
					<tr>
						<th ><label for="khtmlNameInput"><fmt:message key="label.permAddress" /></label></th>
					</tr>	
						<% ObjectIdLong permStateId = (ObjectIdLong)request.getAttribute("permStateId"); %>
					<tr>
						<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.state" /></label></th>
						<td  class="last-child">
						  <select id="value(permStateId)" name="value(permStateId)"> 
						    <option value="-1"><kvl:label id="cms.label.selectState"/></option>
						    <c:forEach var="permState" items="${availPermStateNames}">
					        <option value="<c:out value="${permState.stateId}"/>"
							 <c:if test="${permState.stateId == permStateId}">selected</c:if>>
					          <c:out value="${permState.stateNm}"/>
					   		  
					        </option>
					        </c:forEach>
						  </select>
						</td>
						</tr>

						<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.district" /></label></th>
							<td ><html:text property="value(permDistrict)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>

					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.taluka" /></label></th>
							<td ><html:text property="value(permTaluka)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.village" /></label></th>
							<td ><html:text property="value(permVillage)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
					</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.dateOfJoining" /></label></th>
						<td width=33%>
						<input id="value(doj)" 
										name="value(doj)" 
										onchange="setDataChanged(true);" 
										type=text size=20 value="<c:out value="${doj}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(doj)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.dateOfJoining" /></td>

					</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.dateOfTermination" /></label></th>
							<td width=33%>
							<html:text property="value(dot)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" readonly="true"/>
					</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.pfAcctNo" /></label></th>
							<td ><html:text property="value(pfaccno)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
					</tr>
					<tr><th><label for="khtmlNameInput"><fmt:message key="label.aadharNo" /></label></th>
							<td ><html:text property="value(aadharno)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
					</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.gender" /></label></th>
							<td ><html:text property="value(gender)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
					</tr>
					<tr><th><label for="khtmlNameInput"><span>*</span><fmt:message key="label.panno" /></label></th>
							<td ><html:text property="value(panno)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
					</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.hazard" /></label></th>
							<td ><html:checkbox property="value(hazard)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.marital" /></label></th>
							<td ><html:checkbox property="value(marital)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.wifeNm" /></label></th>
							<td ><html:text property="value(wifeName)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.noOfChildren" /></label></th>
							<td ><html:text property="value(noofChildren)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.technical" /></label></th>
							<td ><html:text property="value(technical)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>

						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.academic" /></label></th>
							<td ><html:text property="value(academic)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.profLic1" /></label></th>
							<th ><html:text property="value(profLic1)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></th>
							<th ><html:text property="value(profLic2)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></th>
							<th ><html:text property="value(profLic3)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></th>
							<th ><html:text property="value(profLic4)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></th>
						</tr>
					
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.prevEmp" /></label></th>
							<td ><html:text property="value(prevEmployer)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.referedBy" /></label></th>
							<td ><html:text property="value(referedBy)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>

					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.wages" /></label></th>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.basic" /></label></th>
							<td ><html:text property="value(basic)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.da" /></label></th>
							<td ><html:text property="value(da)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.allowance" /></label></th>
							<td ><html:text property="value(allowance)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>

					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.relativeWICHD" /></label></th>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.relativeWIC" /></label></th>
							<td ><html:checkbox property="value(isRelativeWIN)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.relativeName" /></label></th>
							<td ><html:text property="value(relativeName)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.relativeAddr" /></label></th>
							<td ><html:textarea property="value(relativeAddr)" styleId="khtmlNameInput" onchange="setDataChanged();" cols="35" rows="7"  /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.mobileno" /></label></th>
							<td ><html:text property="value(mobileno)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.landloser" /></label></th>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.surveyno" /></label></th>
							<td ><html:text property="value(surveyno)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.relationWithSeller" /></label></th>
							<td ><html:text property="value(relationWithSeller)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.village" /></label></th>
							<td ><html:text property="value(village)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.nameofseller" /></label></th>
							<td ><html:text property="value(nameofseller)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.extent" /></label></th>
							<td ><html:text property="value(extent)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>

					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.acctOpen" /></label></th>
							<td ><html:checkbox property="value(acctOpen)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
						
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.bankbranch" /></label></th>
							<td ><html:text property="value(bankbranch)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><fmt:message key="label.acctno" /></label></th>
							<td ><html:text property="value(acctno)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.shoesize" /></label></th>
							<td ><html:text property="value(shoesize)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.bloodgroup" /></label></th>
							<td ><html:text property="value(bloodgroup)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.amccheckup" /></label></th>
												
						<td width=33%>
						<input id="value(amccheckup)" 
										name="value(amccheckup)" 
										onchange="setDataChanged(true);" 
										type=text size=20 value="<c:out value="${amccheckup}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(amccheckup)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.amccheckup" /></td>
					</tr>

					</tr>
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.medical" /></label></th>
							<td ><html:checkbox property="value(medical)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
	
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.safety" /></label></th>
							<td ><html:checkbox property="value(safety)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
	
					<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.skillcert" /></label></th>
							<td ><html:checkbox property="value(skillcert)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
					</tr>
	
						<!--  end -->
						
						
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	
</div>
