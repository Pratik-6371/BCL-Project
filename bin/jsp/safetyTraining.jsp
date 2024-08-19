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
 <%@page 
import="com.kronos.wfc.platform.resources.shared.constants.CommonConstants" %>
<%@ taglib tagdir = "/WEB-INF/tags/wpk/kvl" prefix = "kvl"%>
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") 
%>" media="all" rel="stylesheet" type="text/css">
<%@include file = "/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp"/>

 <% String nameFieldSize = KronosProperties.get("cms.name.maxlength", "10"); %> 
 <% String propertyValueFieldSize = KronosProperties.get(" cms.property.value.maxlength", "250"); %>
 
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<bean:define id="cmsConfigProperties" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(safetyList)" />

<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(tradeId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(cName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(eCode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(eName)" styleId="khtmlWsConfigId" />
<html:hidden property="value(trade)" styleId="khtmlWsConfigId" />
<html:hidden property="value(skill)" styleId="khtmlWsConfigId" />
<html:hidden property="value(empcode)" styleId="khtmlWsConfigId" />
<html:hidden property="value(empname)" styleId="khtmlWsConfigId" />
<html:hidden property="value(skill)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)"/>
<bean:define id="trainings" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(trainings)" />
<bean:define id="departments" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(departments)" />
<bean:define id="sections" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(sections)" />
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
				function setSelectedItem(id, name){
					
				    var elements = document.getElementById('dataForm').elements;

				    for(var i = 0; i < elements.length; i++){
				    	
				    	if(elements[i].name == name && elements[i].value == id){
				    		elements[i].options[elements[i].options.selectedIndex].selected = true;
				    		break;
				    	}	
				}
				//* Validate Date Field script- By JavaScriptKit.com
				//* For this script and 100s more, visit http://www.javascriptkit.com
				//* This notice must stay intact for usage
				---------------------------**/

				function checkdate(input){
				var validformat=/^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
				var returnval=false
				if (!validformat.test(input.value))
				alert("Invalid Date Format. Please correct and submit again.")
				else{ //Detailed check for valid date ranges
				var dayfield=input.value.split("/")[0]
				var monthfield=input.value.split("/")[1]
				var yearfield=input.value.split("/")[2]
				var dayobj = new Date(yearfield, monthfield-1, dayfield)
				if ((dayobj.getMonth()+1!=monthfield)||(dayobj.getDate()!=dayfield)||(dayobj.getFullYear()!=yearfield))
				alert("Invalid Day, Month, or Year range detected. Please correct and submit again.")
				else
				returnval=true
				}
				if (returnval==false) input.select()
				return returnval
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

<table class="Tabular" cellspacing="0" cellpadding="0">
<thead> 		<th width=47%><br>Training Need Assessment</th>
				
				<th width=53%><br>Training Details </th>
				</thead>
</table>

<table class="Tabular" cellspacing="0" cellpadding="0">
	    <thead>
        <tr>
				<th></th>
				<th width=3%>&nbsp</th>
				<th width=3%>&nbsp</th>
				<th><fmt:message key="cms.label.module" /></th>
				<th><fmt:message key="cms.label.tni" /></th>
				<th><fmt:message key="cms.label.department" /></th>
				<th><fmt:message key="cms.label.section" /></th>
				<th><fmt:message key="cms.label.function" /></th>
				<th><fmt:message key="cms.label.remarks" /></th>
				<th width=3%>&nbsp</th>
				<th><fmt:message key="cms.label.training" /></th>
				<th><fmt:message key="cms.label.trainingType" /></th> 
				<th><fmt:message key="cms.label.trainingDate" /></th>
				<th><fmt:message key="cms.label.natureofjob" /></th>
 				<th><fmt:message key="cms.label.fromTime" /></th>
				<th><fmt:message key="cms.label.toTime" /></th>
 			    <th><fmt:message key="cms.label.faculty" /></th>
				<th><fmt:message key="cms.label.venue" /></th>
				<th><fmt:message key="cms.label.preMarksObtained" /></th>
				<th><fmt:message key="cms.label.preMaxMarks" /></th>
<%-- 			<th><fmt:message key="cms.label.preMarksPercent" /></th> --%>
				<th><fmt:message key="cms.label.postMarksObtained" /></th>
				<th><fmt:message key="cms.label.postMaxMarks" /></th>
<%-- 			<th><fmt:message key="cms.label.postMarksPercent" /></th> --%>
				<th><fmt:message key="cms.label.recommendForRetraining" /></th>
				<th><fmt:message key="cms.label.nextTrnDue" /></th>
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
				<select id="selectedPET" name="value(<c:out value="${itemId}" />_trnId)" onchange="setSelectedItem(<c:out value="${item.trnId}" />)"> 
			    <option value="-1"><kvl:label id="cms.label.selectTraining"/></option>
				<c:forEach var="trns" items="${trainings}">
		        <option value="<c:out value="${trns.safetyId}"/>"
				 <c:if test="${item.trnId == trns.safetyId}">selected</c:if>>
		          <c:out value="${trns.name}"/>
		        </option>
		        </c:forEach>
			  </select>
				</td>
				<td>
					<select id="selectedFunc" name="value(<c:out value="${item.id}"/>_tni)"
								onchange="setDataChanged()">
						<option value="P1"
							<c:if test="${item.TNI == 'P1'}">selected = "selected"</c:if>>P1</option>
						<option value="P2"
							<c:if test="${item.TNI == 'P2'}">selected = "selected"</c:if>>P2</option>
					</select>
				</td>
				
				<td>
				<select id="selectedPED" name="value(<c:out value="${itemId}" />_department)" onchange="setSelectedItem(<c:out value="${item.dept}" />)"> 
<%-- 			    <option value="-1"><kvl:label id="cms.label.selectDepartment"/></option> --%>
				<c:forEach var="dept" items="${departments}">
		        <option value="<c:out value="${dept.depid}"/>"
				 <c:if test="${item.dept == dept.depid}">selected</c:if>>
		          <c:out value="${dept.code}"/>
		        </option>
		        </c:forEach>
			  </select>
				</td>
				<td>
				<select id="selectedPES" name="value(<c:out value="${itemId}" />_section)" onchange="setSelectedItem(<c:out value="${item.sec}" />)"> 
<%-- 			    <option value="-1"><kvl:label id="cms.label.selectSectionName"/></option> --%>
				<c:forEach var="sect" items="${sections}">
		        <option value="<c:out value="${sect.sectionId}"/>"
				 <c:if test="${item.sec == sect.sectionId}">selected</c:if>>
		          <c:out value="${sect.name}"/>
		        </option>
		        </c:forEach>
			  </select>				
			  </td>
				<td>
				<select id="selectedFunc" name="value(<c:out value="${itemId}" />_function)" onchange="setSelectedItem(<c:out value="${item.func}" />, <c:out value="${itemId}" />_function)"> 
			    <option value="T" <c:if test="${item.func == 'T'}">selected = "selected"</c:if> >T</option>
			    <option value="M" <c:if test="${item.func == 'M'}">selected = "selected"</c:if>>M</option>
			    <option value="C" <c:if test="${item.func == 'C'}">selected = "selected"</c:if>>C</option>
			    <option value="HR" <c:if test="${item.func == 'HR'}">selected = "selected"</c:if>>HR</option>
			    <option value="P"<c:if test="${item.func == 'P'}">selected = "selected"</c:if>>P</option>
			    <option value="O"<c:if test="${item.func == 'O'}">selected = "selected"</c:if>>O</option>
			  </select>				
			  </td>
				<td>
				<input name="value(<c:out value="${item.id}"/>_remarks)" onchange="setDataChanged()" 
							value="<c:out value="${item.remarks}"/>" size="10" maxlength="30"> 
				</td>
				<th width=3%>&nbsp</th>
			<td>
				<select id="selectedMod" name="value(<c:out value="${itemId}" />_module)" onchange="setDataChanged()"> 
			    <option value="Safety" <c:if test="${item.module == 'Safety'}">selected = "selected"</c:if> >Safety</option>
			    <option value="Behavioural" <c:if test="${item.module == 'Behavioural'}">selected = "selected"</c:if>>Behavioral</option>
			    <option value="Functional" <c:if test="${item.module == 'Functional'}">selected = "selected"</c:if>>Functional</option>
			    <option value="Others" <c:if test="${item.module == 'Others'}">selected = "selected"</c:if>>Others</option>
			    </select>				
			</td>
			<td>
				<select id="selectedType" name="value(<c:out value="${itemId}" />_trnDesc)" onchange="setDataChanged()"> 
			    <option value="Main" <c:if test="${item.trnDesc == 'Main'}">selected = "selected"</c:if> >Main</option>
			    <option value="Refresher" <c:if test="${item.trnDesc == 'Refresher'}">selected = "selected"</c:if>>Refresher</option>
			    </select>				
			</td>
				<td>
						<input id="value(<%=itemId%>_trainingDate)"
										name="value(<%=itemId%>_trainingDate)" onchange = "return checkdate(<%=itemId%>_trainingDate)"
										type=text size=12 value="<c:out value="${item.dateTaken}"/>">
										</input>
				</td>
	
				<td>
				<input name="value(<c:out value="${item.id}"/>_natureofjob)" onchange="setDataChanged()" 
							value="<c:out value="${item.nJob}"/>" size="10" maxlength="10"> 
				</td>

				<td>
				<input name="value(<c:out value="${item.id}"/>_fromtime)" onchange="setDataChanged()" 
							value="<c:out value="${item.fromTime}"/>" size="5" maxlength="5"> 
				</td><td>
				<input name="value(<c:out value="${item.id}"/>_totime)" onchange="setDataChanged()" 
							value="<c:out value="${item.toTime}"/>" size="5" maxlength="5"> 
				</td>
<!-- 								<td> -->
<%-- 				<input name="value(<c:out value="${item.id}"/>_duration)" onchange="setDataChanged()"  --%>
<%-- 							value="<c:out value="${item.tradeDesc}"/>" size="<%=nameFieldSize%>" maxlength="<%=propertyValueFieldSize%>">  --%>
<!-- 				</td> -->
				<td>
				<input name="value(<c:out value="${item.id}"/>_faculty)" onchange="setDataChanged()" 
							value="<c:out value="${item.facultyNm}"/>" size="10" maxlength="20"> 
				</td>
								<td>
				<input name="value(<c:out value="${item.id}"/>_venue)" onchange="setDataChanged()" 
							value="<c:out value="${item.venue}"/>" size="10" maxlength="20"> 
				</td>
				
				<td>
				<input name="value(<c:out value="${item.id}"/>_preMarksObtained)" onchange="setDataChanged()" 
							value="<c:out value="${item.preTestMarksObtained}"/>" size="3" maxlength="3"> 
				</td>
								<td>
				<input name="value(<c:out value="${item.id}"/>_preMaxMarks)" onchange="setDataChanged()" 
							value="<c:out value="${item.preTestMaxMarks}"/>" size="3" maxlength="3"> 
				</td>
<%--				<td>
 				<input name="value(<c:out value="${item.id}"/>_preMarksPercent)" onchange="setDataChanged()"  --%>
<%-- 							value="<c:out value="${item.preTestPercentage}"/>" size="3" maxlength="3">  --%>
<!-- 				</td> -->
				<td>
				<input name="value(<c:out value="${item.id}"/>_postMarksObtained)" onchange="setDataChanged()" 
							value="<c:out value="${item.postTestMarksObtained}"/>" size="3" maxlength="3"> 
				</td><td>
				<input name="value(<c:out value="${item.id}"/>_postMaxMarks)" onchange="setDataChanged()" 
							value="<c:out value="${item.postTestMaxMarks}"/>" size="3" maxlength="3"> 
				</td>
<!-- 				<td> -->
<%-- 				<input name="value(<c:out value="${item.id}"/>_postMarksPercent)" onchange="setDataChanged()"  --%>
<%-- 							value="<c:out value="${item.postTestPercentage}"/>" size="3" maxlength="3">  --%>
<!-- 				</td> -->
				<td>
				<input name="value(<c:out value="${item.id}"/>_recommendForRetraining)" onchange="setDataChanged()" 
							value="<c:out value="${item.recommendation}"/>" size="5" maxlength="5"> 
				</td>
				<td>
						<input id="value(<%=itemId%>_nextTrnDa)"
										name="value(<%=itemId%>_nextTrnDa)" onchange = "return checkdate(<%=itemId%>_nextTrnDa)"
										type=text size=12 value="<c:out value="${item.nextTrnDa}"/>">
										</input>
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>

