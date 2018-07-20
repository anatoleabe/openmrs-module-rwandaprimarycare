<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<form method="post">
	
	<spring:message code="rwandaprimarycare.touchscreen.idNumberType" />
	<c:set value="numberType"><spring:message code="rwandaprimarycare.touchscreen.idNumberType" /></c:set>
	<select name="idType" label="${numberType}">
		<option value=""></option>
		<c:forEach var="idType" items="${idTypes}">
			<option value="${idType.patientIdentifierTypeId}">${idType.name}</option>
		</c:forEach>
	</select>
	<br/>
	
	<spring:message code="rwandaprimarycare.touchscreen.addNumber" />
	<c:set value="addNumber"><spring:message code='rwandaprimarycare.touchscreen.addNumber' /></c:set>
	<touchscreen:textInput label="${addNumber}" field_name="identifier" required="true"/>
	<br/>
	
	<input type="submit"/>
</form>

<%@ include file="resources/touchscreenFooter.jsp"%>