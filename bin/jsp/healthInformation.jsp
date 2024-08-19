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
 <%@page 
import="com.kronos.wfc.platform.resources.shared.constants.CommonConstants" %>
<%@ taglib tagdir = "/WEB-INF/tags/wpk/kvl" prefix = "kvl"%>
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<%@include file = "/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp"/>

 <% String nameFieldSize = KronosProperties.get("cms.name.maxlength", "50"); %> 
 <% String propertyValueFieldSize = KronosProperties.get(" cms.property.value.maxlength", "250"); %>
 
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<bean:define id="cmsConfigProperties" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(healthInformationList)" />
<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(cName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(eCode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(eName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(trade)" styleId="khtmlWsConfigId" />
<html:hidden property="value(skill)" styleId="khtmlWsConfigId" />
<html:hidden property="value(empcode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(empname)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)"/>
<script type="text/javascript">
				/*
				 * The TakeMeTo control expects a function called 'takeMeTo' on the iframe object.
				 * This being a frameset, was defined in the summary frame.
				 * Moved the takeMeTo function up one level to make the TakeMeTo control work.
				 * NOTE if adding additional links to this method for this page you must also
				 * specify a target attribute for the form to replace this frameset with the page you
				 * want to load.  Otherwise the navigation bar will not know whether to show the 
				 * TakeMeTo control or not.
				 */
				function takeMeTo(linkName) {
					if (linkName == '<%=CommonConstants.TIMESHEET_APPLET%>') {
						frames['summary'].employeeIdListFormHandler()
					}
					else if (linkName == '<%=CommonConstants.REPORTS_APPLET%>') {
						frames['summary']['com.kronos.mss'].checkUserSelectionAndForwardToReports()
					}
					else if (linkName == '<%=CommonConstants.REPORTS_ADVANCED%>') {
						frames['summary'].gotoAdvancedReportPage()
					} else if (linkName == '<%=CommonConstants.LEAVE_CASES%>') {	
						frames['summary'].viewLeaveCase()
					}
				}
			</script>

<div class="Panel first-child">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td>
					<table>

						<!--   cms  contractor-->

						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.unitname" /></label></th>
							<td><html:text property="value(unitName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true" disabled="true" /></td>

							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.contractor.name" /></label></th>
							<td><html:text property="value(cName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true" disabled="true" /></td>
						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.eCode" /></label></th>
							<td><html:text property="value(eCode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true" disabled="true" /></td>
									
						<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.name" /></label></th>
							<td><html:text property="value(eName)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true" disabled="true" /></td>			
						
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.trade" /></label></th>
							<td><html:text property="value(trade)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true"  disabled="true"/></td>

							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.skill" /></label></th>
							<td><html:text property="value(skill)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="20"
									maxlength="20" readonly="true" disabled="true" /></td>

						</tr>

						<!--  end -->


					</table>
</td>
</tr></tbody></table>
</div>

<div class="Panel second-child">
<table class="Tabular" cellspacing="0" cellpadding="0">
	    <thead>
        <tr>
				<th></th>
				<th width=3%>&nbsp</th>
				<th width=3%>&nbsp</th>
				<th><fmt:message key="cms.label.healthText" /></th>
				<th><fmt:message key="cms.label.healthDesc" /></th>
				<th><fmt:message key="cms.label.healthTestDate" /></th>
				<th><fmt:message key="cms.label.majorConcerns" /></th>
				<th><fmt:message key="cms.label.nextMedicalTest" /></th>
		        </tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${cmsConfigProperties}" varStatus="status">
			<tr>
					<td>
					<c:set var="itemId" value="${item.id}" />
					<jsp:useBean id="itemId" type="java.lang.String" />
					<html:hidden property="valueAsStringArray(propertyIds)" value="<%=itemId%>"/>
				</td>
				<td>
					<kvl:icon id="cms.label.deleteRow" className="RowDeleteIcon">
						<jsp:attribute name="onclick">(document.getElementsByName('value(selectedId)')[0]).value = '<c:out value="${item.id}"/>';document.forms.dataForm.doAction('deleteRow')
						</jsp:attribute>
					</kvl:icon>
				</td>
				<td>
					<kvl:icon id="cms.label.addRow" className="RowInsertIcon" >
						<jsp:attribute name="onclick">(document.getElementsByName('value(selectedId)')[0]).value = '<c:out value="${item.id}"/>';document.forms.dataForm.doAction('insertRow')
						</jsp:attribute>
					</kvl:icon>
				</td>
				<td>
				    <input name="value(<c:out value="${item.id}"/>_healthText)" onchange="setDataChanged()"
							value="<c:out value="${item.healthText}"/>" size="50" maxlength="250"> 
				</td>
				<td>
				<input name="value(<c:out value="${item.id}"/>_healthDesc)" onchange="setDataChanged()" 
							value="<c:out value="${item.healthDesc}"/>" size="100" maxlength="250"> 
				</td>
				<td>
						<input id="value(<%=itemId%>_healthTestDate)"
										name="value(<%=itemId%>_healthTestDate)"  onchange="setDataChanged()"
										type=text size=10 value="<c:out value="${item.healthTestDate}"/>">
										</input>
				</td>

				<td>
				<input name="value(<c:out value="${item.id}"/>_majorConcerns)" onchange="setDataChanged()" 
							value="<c:out value="${item.majorConcerns}"/>" size="<%=nameFieldSize%>" maxlength="<%=propertyValueFieldSize%>"> 
				</td>
				<td>
				<input name="value(<c:out value="${item.id}"/>_nextMedicalTest)" onchange="setDataChanged()" 
							value="<c:out value="${item.nextMedicalTest}"/>" size="10"> 
				</td>
								
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
