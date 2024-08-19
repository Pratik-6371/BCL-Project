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


 <% String nameFieldSize = KronosProperties.get("cms.name.maxlength", "50"); %> 
 <% String propertyValueFieldSize = KronosProperties.get(" cms.property.value.maxlength", "250"); %>
 
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<bean:define id="cmsConfigProperties" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(cmsConfigProperties)" />

<html:hidden property="value(peid)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)"/>
<bean:define id="availStateNames" name="dataForm" property="value(availStateNames)"/>
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td >
					<table >
						
						
		<!--  chiru's  cms  principal employer-->
						
						<tr><th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.unitname"  /></label></th>
                             <td><html:text property="value(unitname)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" readonly="true" /></td>
							
						
						
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.address" /></label></th>
							<td><html:text property="value(peaddress)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.address1" /></label></th>
							<td><html:text property="value(peaddress1)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.address2" /></label></th>
							<td><html:text property="value(peaddress2)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>


						<tr>
						 <%String snm = (String)request.getAttribute("peState"); %>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.state" /></label></th>
							<td><html:text property="value(peaddress3)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>

<!-- 							  <select id="value(peaddress3)" name="value(peaddress3)">  -->
<%-- 							    <option value="-1"><kvl:label id="cms.label.selectState"/></option> --%>
<%-- 							    <c:forEach var="state" items="${availStateNames}"> --%>
<%-- 						        <option value="<c:out value="${state.stateNm}"/>" --%>
<%-- 								 <c:if test="${state.stateNm eq snm}">selected</c:if>> --%>
<%-- 						          <c:out value="${state.stateNm}"/> --%>
						   		  
<!-- 						        </option> -->
<%-- 						        </c:forEach> --%>
<!-- 							  </select> -->

						</tr>




						<!--  manager name and address -->

						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.managername" /></label></th>
							<td><html:text property="value(managername)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="30" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.manageraddress" /></label></th>
							<td><html:text property="value(manageraddress)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.manageraddress1" /></label></th>
							<td><html:text property="value(manageraddress1)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.manageraddress2" /></label></th>
							<td><html:text property="value(manageraddress2)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>


						<tr>				
						   <%String mStateId = (String)request.getAttribute("manageraddress3"); %>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.state" /></label></th>
							<td><html:text property="value(manageraddress3)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
							
<!-- 							<td  class="last-child"> -->
<!-- 							  <select id="value(manageraddress3)" name="value(manageraddress3)">  -->
<%-- 							    <option value="-1"><kvl:label id="cms.label.selectState"/></option> --%>
<%-- 							    <c:forEach var="state" items="${availStateNames}"> --%>
<%-- 						        <option value="<c:out value="${state.stateId}"/>" --%>
<%-- 								 <c:if test="${state.stateId == mStateId}">selected</c:if>> --%>
<%-- 						          <c:out value="${state.stateNm}"/> --%>
						   		  
<!-- 						        </option> -->
<%-- 						        </c:forEach> --%>
<!-- 							  </select> -->
<!-- 							</td> -->

						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.typeofbussiness" /></label></th>
							<td ><html:textarea property="value(typeofbusiness)" styleId="khtmlNameInput" onchange="setDataChanged();"  cols="35" rows="7"/></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.Maximumnumberofworkmen" /></label></th>
							<td ><html:text property="value(maxnumberofworkmen)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="40"  /></td>
						</tr>
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.Maxnumberofcontractworkmen" /></label></th>
							<td ><html:text property="value(maxnumberofcontractworkmen)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="40"  /></td>
							
							<th ><label for="khtmlNameInput"><fmt:message key="label.currentcountworkmen" /></label></th>
							<td ><html:text property="value(currentcountworkmen)" styleId="khtmlNameInput" onchange="setDataChanged();" size="40" maxlength="40" disabled="true" /></td>
							
						</tr>
						
						<tr>
							<th ><label for="khtmlNameInput"><span>*</span><fmt:message key="label.BOCWActApplicability" /></label></th>
							<td ><html:checkbox property="value(boc)" styleId="khtmlNameInput" onchange="setDataChanged();"   /></td>
						</tr>




						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.ISMWActapplicability" /></label></th>
							<td><html:checkbox property="value(ismwa)"
									styleId="khtmlNameInput" onchange="setDataChanged();" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.licensenumber" /></label></th>
							<td><html:text property="value(licensenumber)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.pfCode" /></label></th>
							<td><html:text property="value(pfcode)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span>*</span>
								<fmt:message key="label.esiwc" /></label></th>
							<td><html:text property="value(esiwc) "
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="40" /></td>
						</tr>

					</table>
				</td>
			</tr>
		</tbody>
	</table>
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
	    <thead>
        <tr>
				<th></th>
				<th width=3%>&nbsp</th>
				<th width=3%>&nbsp</th>
				<th><fmt:message key="cms.label.propertyName" /></th>
				<th><fmt:message key="cms.label.propertyValue" /></th>
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
					<input name="value(<c:out value="${item.id}"/>_name)" onchange="setDataChanged()"
							value="<c:out value="${item.name}"/>" size="<%=nameFieldSize%>" maxlength="<%=nameFieldSize%>"> 
				</td>
				<td>
				<input name="value(<c:out value="${item.id}"/>_value)" onchange="setDataChanged()" 
							value="<c:out value="${item.value}"/>" size="<%=nameFieldSize%>" maxlength="<%=propertyValueFieldSize%>"> 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
