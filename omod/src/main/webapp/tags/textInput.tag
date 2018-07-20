<%@ include file="/WEB-INF/template/include.jsp" %>

<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="field_name" required="false" rtexprvalue="true" %>
<%@ attribute name="value" required="false" rtexprvalue="true" %>
<%@ attribute name="ajaxURL" required="false" rtexprvalue="true" %>
<%@ attribute name="javascriptAction" required="false" rtexprvalue="true" %>
<%@ attribute name="allowFreeText" required="false" rtexprvalue="false" %>
<%@ attribute name="required" type="java.lang.Boolean" required="true" rtexprvalue="true" %>
<%@ attribute name="fieldType"  required="false" rtexprvalue="true" %>


<input
	helpText="${label}"
	type="text"
	name="${field_name}"
	value="${value}"
	autocomplete="off"
	<c:if test="${not empty ajaxURL}">
		ajaxURL="${ajaxURL}"
	</c:if>
	<c:if test="${not empty javascriptAction}">
		javascriptAction="${javascriptAction}"
	</c:if>
	allowFreeText = "${allowFreeText}"
	optional="${!required}" 
	field_type="${fieldType}"
/>