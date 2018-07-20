<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<%@ attribute name="encounter" type="org.openmrs.Encounter" required="true" rtexprvalue="true" %>
<%@ attribute name="returnUrl" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="false" %>
<%@ attribute name="clickable" type="java.lang.Boolean" required="false" rtexprvalue="false" %>

<c:choose>
	<c:when test="${size == 'small'}">
		<c:set var="buttonLabel" value="${encounter.encounterType.name}"/>
	</c:when>
	<c:otherwise>
		<c:set var="atStr"><spring:message code="rwandaprimarycare.touchscreen.at"/></c:set>
		<c:set var="byStr"><spring:message code="rwandaprimarycare.touchscreen.by"/></c:set>
		<c:set var="buttonLabel" value="${encounter.encounterType.name} ${atStr} ${encounter.location.name} ${byStr} ${encounter.provider.personName}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${clickable == null || clickable}">
		<c:url var="buttonHref" value="encounter.form">
			<c:param name="encounterId" value="${encounter.encounterId}"/>
			<c:if test="${not empty returnUrl}">
				<c:param name="returnUrl" value="${returnUrl}"/>
			</c:if>
		</c:url>
	</c:when>
	<c:otherwise>
		<c:set var="buttonHref" value=""/>
	</c:otherwise>
</c:choose>

<touchscreen:button label="${buttonLabel}" href="${buttonHref}" />
