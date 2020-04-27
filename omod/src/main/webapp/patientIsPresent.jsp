<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<!-- note: controller is RwandaPrimaryCarePatientDashboardController-->
<table><tr><Td>
	
	<openmrs:globalProperty var="allowBackEntry" key="registration.allowBackEntry" defaultValue=""/>
	
	<%@ include file="patientResultsTableWhite.jspf"%>
	<div style="float: left; background-color: #f0f0a0; border: 1px black solid; padding: 10px">
		<h1><spring:message code="rwandaprimarycare.touchscreen.isThePatientPresent"/></h1>
		<br/>
		<c:set var="yes"><spring:message code="rwandaprimarycare.touchscreen.yes"/></c:set>
		<c:set var="no"><spring:message code="rwandaprimarycare.touchscreen.no"/></c:set>

		<touchscreen:button label="${yes}" href="patientIsPresent.form?patientId=${patient.patientId}&printBarCode=true&serviceRequested=0" cssClass="green"/>

		<c:choose>
			<c:when test="${allowBackEntry eq 'true' }">
				<touchscreen:button label="${no}" href="patientIsNotPresent.form?patientId=${patient.patientId}" cssClass="green"/>
			</c:when>
			<c:otherwise>
				<touchscreen:button label="${no}" href="patient.form?patientId=${patient.patientId}&skipPresentQuestion=true" cssClass="green"/>
			</c:otherwise>
		</c:choose>
	</div>
</Td></tr></table>
<%@ include file="resources/touchscreenFooter.jsp"%>