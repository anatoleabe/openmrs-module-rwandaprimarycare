<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<c:if test="${results == null}">
	<form method="get">
		<c:set var="enterId"><spring:message code="rwandaprimarycare.touchscreen.enterIdNumber"/></c:set>
		<c:set var="searchStr"><spring:message code="rwandaprimarycare.touchscreen.search"/></c:set>
		<touchscreen:textInput label="${enterId}" field_name="search" required="true" value="" fieldType="id"/>
		<input type="submit" value="${searchStr} }"/>
	</form>
</c:if>

<c:if test="${results != null}">
	<touchscreen:patientList patients="${results}" maxResults="5" separator="" showAllIds="true"/>
	<c:if test="${fn:length(results) == 0}">
		<br/>
		<br/>
		<c:set var="searchByNameStr"><spring:message code="rwandaprimarycare.touchscreen.searchByName"/></c:set>
		<c:set var="searchByIdAgainStr"><spring:message code="rwandaprimarycare.touchscreen.searchByIdAgain"/></c:set>
		<touchscreen:button label="${searchByIdAgainStr}" href="findPatientById.form"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<touchscreen:button label="${searchByNameStr}" href="findPatientByName.form"/>
	</c:if>
</c:if>

<%@ include file="resources/touchscreenFooter.jsp"%>