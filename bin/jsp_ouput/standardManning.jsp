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
<%@ page import="java.util.*" %>

<script type="text/javascript">
function setSelectedItem(id){
	
    var elements = document.getElementById('dataForm').elements;

    for(var i = 0; i < elements.length; i++){
    	
    	if(elements[i].name == 'valueAsStringArray(selectedIds)' && elements[i].value == id){
    		elements[i].checked = true;
    		break;
    	}	
    }
}
</script>

<div class="Panel first-child">
<table>
    
	  
	<tr>
		<td  class="last-child">
		  <fmt:message key="label.unitname" />
			  <select id="selectedId" name="value(unitId)"> 
			    <option value="-1"><kvl:label id="label.unitname"/></option>
				<c:forEach var="pe" items="${availUnitNames}">
			        <option value="<c:out value="${pe.getUnitid()}"/>"
					 <c:if test="${pe.getUnitid() == unitId}">selected</c:if>>
			          <c:out value="${pe.getUnitid()}"/>
			        </option>
		        </c:forEach>
			  </select>
		</td>
	
	<td  class="last-child">
		<fmt:message key="label.departmentName" />
	    <html:text property="value(dept)" styleId="khtmlNameInput"
	  								size="20"
									maxlength="20"  />
	</td>
	
	</tr>
	<tr>
	
	<td  class="last-child">
	<fmt:message key="label.job" />
	<html:text property= "value(job)" size="20"
				styleId="khtmlNameInput"	maxlength="20"  />
	</td>
	
		<td  class="last-child">
	  <fmt:message key="label.Effectivedate" />
	  <html:text property= "value(status)" size="20"
				styleId="khtmlNameInput"	maxlength="20"  />
	</td>

	
	
	
	<td>
	  
                        <kvl:button id="cms.label.search" action="">
                        <html:button styleId="searchButton" property="searchButton" onclick="if (checkUnsavedData()) doStateChange()"><kvl:resource id="LocaleProfile.label.search" /></html:button>
                        </kvl:button>
   </td>
   </tr>
  </table>
</div>



<div class="Panel">
	<table  border="1" width="90%" height = "90%">
<tr><th>UNIT ID</th><th>DEPARTMENT</th><th>JOB</th><th>SKILL</th><th>Perm_HC</th><th>CL_HC</th><th>EFFECTIVE DATE</th><th>STATUS</th></tr>


<c:forEach items="${ldata}" var="u">
	<tr border="1" width="50%" height = "90%" >
		<td >${u.getUnitid()}</td>
		<td>${u.getDepartment()}</td>
		<td>${u.getJob()}</td>
		<td >${u.getSkill()}</td>
		<td>${u.getPermheadcount()}</td>
		<td>${u.getClheadcount()}</td>
		<td>${u.getEffectivedate()}</td>
		<td>${u.isStatus()}</td>
		
		<%-- <td><c:set value="${u.isStatus()}" var="num"/>
			<c:out value="${num  == false ? 'ACTIVE' : 'INACTIVE'}"/></td> --%>
	<%-- 	<td  class="last-child">
	  		<html:text property= "value(unitId)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		
		<td  class="last-child">
	  		<html:text property= "value(department)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		<td  class="last-child">
	  		<html:text property= "value(job)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		<td  class="last-child">
	  		<html:text property= "value(skill)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		<td  class="last-child">
	  		<html:text property= "value(permheadcount)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		<td  class="last-child">
	  		<html:text property= "value(clheadcount)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		<td  class="last-child">
	  		<html:text property= "value(effectivedate)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td>
		
		<td  class="last-child">
	  		<html:text property= "value(isStatus)" size="20"
			styleId="khtmlNameInput"	maxlength="20"  />
		</td> --%>
	</tr>
</c:forEach>
</table>
</div>
	
	
	



