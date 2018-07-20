<%@page isErrorPage="true" %>
<%@ page import="org.openmrs.web.WebUtil" %>
<%@ page import="org.openmrs.web.WebConstants" %>
<%@ page import="org.openmrs.api.context.UserContext" %>
<%@ page import="org.openmrs.util.OpenmrsConstants" %>
<%@ page import="org.openmrs.api.APIAuthenticationException" %>
<%@ page import="org.springframework.transaction.UnexpectedRollbackException" %>

<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="resources/touchscreenHeader.jsp"%>

<br /><br />
<br /><br />
<br /><br />
 
<h2>An Internal Error has Occurred</h2>

<touchscreen:button label="Back" onClick="window.history.back()" cssClass="green"/> &nbsp;&nbsp;&nbsp;&nbsp;
<touchscreen:button label="Close Window" onClick="window.close()" cssClass="green"/> <br/>
<br/>


<script>
	function showOrHide() {
		var link = document.getElementById("toggleLink");
		var trace = document.getElementById("stackTrace");
		if (link.innerHTML == "Show stack trace") {
			link.innerHTML = "Hide stack trace";
			trace.style.display = "block";
		}
		else {
			link.innerHTML = "Show stack trace";
			trace.style.display = "none";
		}
	}
</script>	

	
	<br /><br />
	Consult the <a href="<%= request.getContextPath() %>/help.htm">help document</a>. <br />
	Contact your friendly neighborhood administrator if it cannot be resolved.
	
	<br /><br />
	
	<a href="#" onclick="showOrHide()" id="toggleLink" style="font-size: 12px;">Show stack trace</a>
	<br />
	<div id="stackTrace">
	
		${exception}<br/>
		<c:forEach var="stackTraceElem" items="${exception.stackTrace}">
				<c:out value="${stackTraceElem}"/><br/>
		</c:forEach>
		
	</div> <!-- close stack trace box -->

<br />

<%@ include file="resources/touchscreenFooter.jsp"%>
