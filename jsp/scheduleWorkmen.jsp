<%@ 
	taglib uri="/struts-tiles" prefix="tiles"%><%@ 
	taglib uri="/struts-html" prefix="html"%><%@ 
	taglib uri="/jstl-format" prefix="fmt"%><%@ 
	taglib uri="/jstl-core" prefix="c"%><%@ 
	taglib uri="/jstl-core-rt" prefix="c-rt"%><%@ 
	taglib uri="/struts-bean" prefix="bean" %><%@
	taglib uri="/kronos-html" prefix="v3html"%><%@
	taglib uri="/kronos-lists" prefix="v3lists"%>
	
	<%@ 
    taglib tagdir = "/WEB-INF/tags/wpk/kvl" prefix = "kvl"%><%@
	page import="com.kronos.wfc.platform.utility.framework.URLUtils,
	com.kronos.wfc.platform.properties.framework.KronosProperties,
	com.kronos.wfc.platform.utility.framework.ECMAEscapeUnescape"
	
%>
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-workspace.css.jsp") %>" media="all" rel="stylesheet" type="text/css">
<link href="<%= URLUtils.clientUrl("/applications/wpk/html/theme/suite-headerbar.css.jsp") %>" media="all" rel="stylesheet" type="text/css">
<%@include file = "/applications/wpk/html/global-attributes.jst"%>
<jsp:include page="/applications/wpk/html/theme/suite.css.jsp"/>


<span id="khtmlWebServiceConfigListWorkspace" />
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/struts/scripts/table-utils.js") %>"></script>
<script type="text/javascript" src="<%= URLUtils.clientUrlStatic("/applications/cms/html/scripts/cms.js") %>"></script>

<div class="Panel first-child">
  <table>
    <tr>
	<%-- <td  class="last-child">
						<fmt:message key="label.from" />
						<input id="value(fromdtm)"
										name="value(fromdtm)"
										type=text size=20 value="<c:out value="${fromdtm1}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(fromdtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" />
	
						<fmt:message key="label.to" />
						<input id="value(todtm)" 
										name="value(todtm)" 
										type=text size=20 value="<c:out value="${todtm1}"/>">
										</input><kvl:date-selector-eot-bot-popup 
										id="value(todtm)" 
										eot_bot_enable="bot" 
										start_of_week="1" 
										hide_text_field="true" 
										text_label_field="label.from" /></td> 
									

 <th>
                        <kvl:button id="cms.label.search" action="">
                        <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>     
  </th>	    --%>                             
                                 
  </table>
  </div>
<%=request.getAttribute("WorkmenSchedule")%>


