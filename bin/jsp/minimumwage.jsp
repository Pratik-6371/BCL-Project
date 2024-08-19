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


 
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>

<html:hidden property="value(id)" styleId="khtmlWsConfigId" />
<html:hidden property="value(unitId)" styleId="khtmlWsConfigId" />
<html:hidden property="value(validFrom)" styleId="khtmlWsConfigId" />
<html:hidden property="value(selectedId)"/>
<%--  <bean:define id="fromDtm" name="minimumwageForm" property="value(from)"/>  --%>
 
<div class="Panel">
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
		<tbody>
			<tr>
				<td >
					<table >
						
						
		<!--    cms  minimumwage-->
						
							
						
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.from" /></label></th>
							<td><html:text property="value(from)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" disabled="true" /></td>
						</tr>

						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.trade" /></label></th>
							<td><html:text property="value(trade)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" disabled="true" /></td>
						</tr>


						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.skill" /></label></th>
							<td><html:text property="value(skillset)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" disabled="true"/></td>
						</tr>


						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.minimumwage" /></label></th>
							<td><html:text property="value(minimumwage)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="30" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.daperday" /></label></th>
							<td><html:text property="value(daperday)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
						<tr>
							<th><label for="khtmlNameInput"><span></span>
								<fmt:message key="label.otherallowance" /></label></th>
							<td><html:text property="value(otherallowance)"
									styleId="khtmlNameInput" onchange="setDataChanged();" size="40"
									maxlength="50" /></td>
						</tr>
<!-- 						<tr> -->
<!-- 							<th><label for="khtmlNameInput"><span></span> -->
<%-- 								<fmt:message key="label.Total" /></label></th> --%>
<%-- 							<td> <html:text property="value(total)" --%>
<%-- 									styleId="khtmlNameInput" onchange="setDataChanged();" size="40" --%>
<%-- 									maxlength="50" />  --%>
<%-- 									<%= (Integer.parseInt(request.getParameter("minimumwage"))+Integer.parseInt(request.getParameter("daperday"))+Integer.parseInt("otherallowance")) %></td>
<%-- 						 --%></tr> 


						
						

					</table>
				</td>
			</tr>
		</tbody>
	</table>
	<table class="ControlLayout" cellspacing="0" cellpadding="0">
	    <thead>
        
		</thead>
		<tbody>
		
			<tr>
				
			</tr>
	
		</tbody>
	</table>
</div>
