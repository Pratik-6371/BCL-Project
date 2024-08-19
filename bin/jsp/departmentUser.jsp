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
<html:hidden property="value(depid)"/>
<html:hidden property="value(unitId)"/>
<html:hidden property="value(selectedId)"/>
<bean:define id="dot" name="departmentUserForm" property="value(dot)" />
<bean:define id="availSectionNames" name="departmentUserForm" property="value(availSectionNames)"/>
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody> 
			<tr>
				<td >
					<table >
						
		<!--   cms  contractorunit  -->
						
						<tr>
							<th ><label for="khtmlNameInput"><span><font
											color="red">* </font></span><fmt:message key="label.name" /></label></th>
							<td ><html:text property="value(name)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						
						<tr>
							<th ><label for="khtmlNameInput"><span></span><fmt:message key="label.section" /></label></th>
<%-- 							<td ><html:text property="value(section)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40"  /></td> --%>
	<td  class="last-child">
	  <select id="selectedId" name="value(sectionId)"> 
	    <option value="-1"><kvl:label id="cms.label.selectSectionName"/></option>
	    <c:forEach var="section" items="${availSectionNames}">
        <option value="<c:out value="${section.sectionId}"/>"
		 <c:if test="${section.selected == true}">selected</c:if>>
          <c:out value="${section.name}"/>
   		 
        </option>
        </c:forEach>
	  </select>
	</td>
						</tr>
						
						<tr><th><label for="khtmlNameInput"><span><font
											color="red">* </font></span><fmt:message key="label.username" /></label></th>
							<td ><html:text property="value(userName)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50"  /></td>
						</tr>
						
						<tr><th><label for="khtmlNameInput"><span><font
											color="red">* </font></span><fmt:message key="label.password" /></label></th>
							<td ><input type= "password" value="value(passwd)" name="value(passwd)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						<tr><th><label for="khtmlNameInput"><span><font
											color="red">* </font></span><fmt:message key="label.emailAddr" /></label></th>
							<td ><html:text property="value(emailAddr)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50"  /></td>
						</tr>

						<tr><th><label for="khtmlNameInput"><span><font
											color="red">* </font></span><fmt:message key="label.mobileno" /></label></th>
							<td ><html:text property="value(mobileno)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="50" /></td>
						</tr>
						<tr><th><label for="khtmlNameInput"><fmt:message key="label.isDeptMgr" /></label></th>
							<td ><html:checkbox property="value(isDeptMgr)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>
						<%-- <tr>
								<th><label for="khtmlNameInput"> <fmt:message
											key="label.dateOfTermination" /></label></th>
								<td width=33%><input id="value(dot)" name="value(dot)"
									onchange="setDataChanged(true);" type=text size=10
									value="<c:out value="${dot}"/>"> </input> <kvl:date-selector-eot-bot-popup
										id="value(dot)" eot_bot_enable="bot" start_of_week="1"
										hide_text_field="true"
										text_label_field="label.dateOfTermination" /></td>
							</tr>--%>
						<!--  end -->
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	
</div>
