<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<!-- note: controller is RwandaPrimaryCarePatientDashboardController-->

<table><tr><Td>
	
		<form method="POST" autocomplete="on">
			<select name="visitDate" helpText="<spring:message code='rwandaprimarycare.touchscreen.dateBackenter'/>">
				<c:forEach var="date" items="${dates}">
					<option value="${date.time}"><openmrs:formatDate date="${date}" type="medium" /></option>
				</c:forEach>
			</select>
			<input type="submit" value="Submit"/></td>
		</form>	
	
</Td></tr></table>
<%@ include file="resources/touchscreenFooter.jsp"%>