<%@taglib tagdir="/WEB-INF/tags/wpk/kvl" prefix="kvl"%>
<%@taglib tagdir="/WEB-INF/tags/wpk/url" prefix="url"%>
<%@taglib uri="/jstl-format" prefix="fmt"%>
<%@ taglib uri="/struts-html" prefix="html"%>
<%@page import="org.apache.struts.taglib.html.Constants" %>
<%@page import="java.util.*" %>
<%@ taglib uri="/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/jstl-core" prefix="c"%>

<%@ taglib uri="/struts-bean" prefix="bean" %>


<kvl:script><jsp:attribute name="src"><url:web-server>/applications/wpk/html/js/com/kronos/action-controller.js</url:web-server></jsp:attribute></kvl:script>
<script>
document['com.kronos.wfc.wcb.textlabel.config.actions'] = {
   doImport:function(){
      var promptUserAboutLoosingChangedData = false
      document['com.kronos.ui.action-controller'].doAction('formAction', 'cms.actions.import', 'submit', promptUserAboutLoosingChangedData)
   },
   _temp:null
}
</script>
<bean:define id="availUnitNames" name="uploadForm" property="value(availUnitNames)"/>
<div class="Panel">
   <table class="ControlLayout">
      <tr>
         <td colspan="5">
            <label>
               <fmt:message bundle="${local}" key = "wcb.textlabel.config.label.import-translations"/>
            </label>
         </td>
      </tr>
      <tr>
		
		<%
		//java.util.List eList = (java.util.List)request.getAttribute("availIntrName");
		ArrayList<String> eList = (ArrayList<String>)request.getAttribute("availIntrName");
		 %>
				
		<td>
      		<kvl:label id="cms.label.selectinterfacename" required="true"/>
      	</td>
      	<% String Inf= (String)request.getAttribute("InterfaceName");
		%>
      	<td  class="last-child">
	  		<select id="selectedIN" name="value(InterfaceName)"> 
	    	<option value="-1"><kvl:label id="cms.label.selectInterface"/></option>
	    	<option value="1">Schedule Pattern Upload with Transfer</option>
	    	<%-- <%for(String name: eList){%>
	    		<option value="<%=name %>">
	    			<%=name %>
				</option>
			<%} %> --%>
			
	  		</select>
		</td>
		
		<td>
       		<kvl:label id="cms.label.principalEmployer" required="true"/>
		</td>					
		<% String unitId = (String)request.getAttribute("unitId");
		%>
		<td  class="last-child">
	  		<select id="selectedPE" name="value(unitId)"> 
	    	<option value="-1"><kvl:label id="cms.label.selectUnitName"/></option>
			<c:forEach var="principalEmployer" items="${availUnitNames}">
        		<option value="<c:out value="${principalEmployer.unitId}"/>"
		 			<c:if test="${principalEmployer.unitId == unitId}">selected</c:if>
		 		>
          		<c:out value="${principalEmployer.unitName}"/>
        		</option>
        	</c:forEach>
	  		</select>
		</td>
		
      	
         <td>
            <span class="Required">*</span><fmt:message bundle="${local}" key="wcb.textlabel.config.label.import-filename"/>
         </td>
         <td colspan="3">
            <html:file property="formFile" size="60" styleId="importFile"/>
         </td>
         <td>
            <kvl:button id="wcb.textlabel.config.label.import-button" action="document['com.kronos.wfc.wcb.textlabel.config.actions'].doImport()"></kvl:button>
         </td>					  
      </tr>
   </table>
</div>
