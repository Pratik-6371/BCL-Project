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
<%@page
import = "com.kronos.wfc.commonapp.genies.framework.MSSGlobals"%>

 <% String nameFieldSize = KronosProperties.get("cms.name.maxlength", "50"); %> 
 <% String propertyValueFieldSize = KronosProperties.get(" cms.property.value.maxlength", "250"); %>
 
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<script type="text/javascript" src="<c:out value="${initParam['WFC.context.external']}"/>/applications/cms/html/scripts/cms.js"></script>
<bean:define id="cmsConfigProperties" name="org.apache.struts.taglib.html.BEAN" property="valueAsList(tradeList)" />

<html:hidden property="value(tradeId)" styleId="khtmlWsConfigId" />
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

<td class="last-child" colspan="299">
              <kvl:button id="cms.lr.button" action="timecardSubmit()" /> 
</td>
 <%String modulePath = request.getContextPath(); %>
 <%String tcquerystr = ""; %>            
<td class="last-child" colspan="299">
      <kvl:button id="cms.lr.button" action="scheduleSubmit()" /> 
</td>	
