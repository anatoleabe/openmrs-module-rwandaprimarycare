<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<touchscreen:patientButton patient="${patient}"/>

<br/>

<span class="bigtext">
	<h4>Identifiers:</h4>
	<table>
	<c:forEach var="identifier" items="${activeIdentifiers}">
		<c:url var="deleteUrl" value="deleteIdentifier.form">
			<c:param name="patientId" value="${param.patientId}"/>
			<c:param name="identifierTypeId" value="${identifier.identifierType.patientIdentifierTypeId}"/>
			<c:param name="identifier" value="${identifier.identifier}"/>
		</c:url>
		<tr>
			<td align="right">${identifier.identifierType.name}:</td>
			<td><b>${identifier.identifier}</b></td>
			<td><touchscreen:button label="Delete" cssClass="red button" href="${deleteUrl}"/></td>
		</tr>
	</c:forEach>
	</table>
	
	<touchscreen:button label="Add another" href="addIdentifier.form?patientId=${patient.patientId}"/>
</span>

<%@ include file="resources/touchscreenFooter.jsp"%>