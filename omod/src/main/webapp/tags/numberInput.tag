<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="field_name" required="false" rtexprvalue="true" %>
<%@ attribute name="concept" type="org.openmrs.Concept" required="false" rtexprvalue="true" %>
<%@ attribute name="value" required="false" rtexprvalue="true" %>
<%@ attribute name="min" required="false" rtexprvalue="true" %>
<%@ attribute name="max" required="false" rtexprvalue="true" %>
<%@ attribute name="units" required="false" rtexprvalue="true" %>
<%@ attribute name="precise" required="false" rtexprvalue="true" %>
<%@ attribute name="required" type="java.lang.Boolean" required="true" rtexprvalue="true" %>

<input
	helpText="${label}"
	type="text"
	field_type="number"
	name="${field_name}"
	value="${value}"
	autocomplete="off"
	optional="${!required}"
	<c:choose>
		<c:when test="${not empty concept}">
			<c:if test="${not empty concept.hiAbsolute}">
				absoluteMax="${concept.hiAbsolute}"
			</c:if>
			<c:if test="${not empty concept.lowAbsolute}">
				absoluteMin="${concept.lowAbsolute}"
			</c:if>
			<c:if test="${not empty concept.hiCritical}">
				max="${concept.hiCritical}"
			</c:if>
			<c:if test="${not empty concept.lowCritical}">
				min="${concept.lowCritical}"
			</c:if>
			<c:if test="${not empty concept.units}">
				units="${concept.units}"
			</c:if>
			<c:choose>
				<c:when test="${concept.precise}">
					tt_pageStyleClass="Numeric NumbersOnlyWithDecimal"
				</c:when>
				<c:otherwise>
					tt_pageStyleClass="Numeric NumbersOnly"
				</c:otherwise>
			</c:choose>		
		</c:when>
		<c:otherwise>
			<c:if test="${not empty max}">
				max="${max}"
			</c:if>
			<c:if test="${not empty min}">
				min="${min}"
			</c:if>
			<c:if test="${not empty units}">
				units="${units}"
			</c:if>
			<c:choose>
				<c:when test="${precise == true}">
					tt_pageStyleClass="Numeric NumbersOnlyWithDecimal"
				</c:when>
				<c:otherwise>
					tt_pageStyleClass="Numeric NumbersOnly"
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
/>