<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<script type="text/javascript">
	tt_cancel_destination = "patient.form?patientId=${patient.patientId}";
</script>

<form method="post">
	<input type="hidden" name="encounterType" value="${encounterType.encounterTypeId}"/>
	
	<c:forEach var="question" items="${questions}" varStatus="status">
		<input type="hidden" name="obs_concept_${status.index}" value="${question.concept.conceptId}"/>
		<c:if test="${question.concept.datatype.numeric}">
			<touchscreen:numberInput label="${question.label}" field_name="obs_value_${status.index}" required="${question.required}" concept="${question.concept}"/>
		</c:if>
		<c:if test="${question.concept.datatype.text}">
			<touchscreen:textInput label="${question.label}" required="${question.required}" field_name="obs_value_${status.index}"/>
		</c:if>
	</c:forEach>
	<c:set var="save"><spring:message code="rwandaprimarycare.touchscreen.save"/></c:set>
	<input type="submit" value="${save}"/>
</form>

<%@ include file="resources/touchscreenFooter.jsp"%>