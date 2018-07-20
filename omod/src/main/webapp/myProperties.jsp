<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<%
	org.openmrs.Location workstationLocation = PrimaryCareBusinessLogic
			.getLocationLoggedIn(session);
%>


<%@page
	import="org.openmrs.module.rwandaprimarycare.PrimaryCareBusinessLogic"%>
<div
	style="background-color: #f0f0f0; border: 1px black solid; padding: 5px">
	<span style="font-size: 2em"><spring:message
			code="rwandaprimarycare.touchscreen.hello" />
		${authenticatedUser.personName.givenName}.</span> <br /> <span
		style="font-size: 1.5em"><spring:message
			code="rwandaprimarycare.touchscreen.loggedInAs" /> <i>${authenticatedUser}</i>
		<%
			if (workstationLocation != null) {
		%><spring:message
			code="rwandaprimarycare.touchscreen.at" /> <i><%=workstationLocation.getName()%></i>
		<%
			}
		%>.</span>
</div>

<div align="center">
	<table>
		<tr>
			<td><c:set var="myKeyboardStr">
					<spring:message code="rwandaprimarycare.touchscreen.changekeyboard" />
				</c:set> <touchscreen:button label="${myKeyboardStr}"
					href="keyboardType.form" /></td>
			<td><c:set var="myLanguageStr">
					<spring:message code="rwandaprimarycare.touchscreen.changeLanguage" />
				</c:set> <touchscreen:button label="${myLanguageStr}"
					href="chooseLanguage.form" /></td>
		</tr>
	</table>

</div>

<%@ include file="resources/touchscreenFooter.jsp"%>