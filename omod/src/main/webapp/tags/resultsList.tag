<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<%@ attribute name="items" type="java.util.List" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="false" %>
<%@ attribute name="separator" required="false" rtexprvalue="false" %>
<%@ attribute name="maxResults" type="java.lang.Integer" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="false" %>

<c:if test="${fn:length(items) == 0}">
	<c:if test="${title != null}">
		&nbsp;
	</c:if>
	<c:if test="${title == null}">
		<spring:message code="rwandaprimarycare.touchscreen.noItems"/>
	</c:if>
</c:if>
<c:if test="${fn:length(items) > 0}">
	<u>
		<c:if test="${title != null}">
			${title}
		</c:if>
		<c:if test="${title == null}">
			${fn:length(items)} item(s)
		</c:if>
	</u>
	<br/>
	<c:forEach var="item" items="${items}" varStatus="status">
		<c:if test="${maxResults == null || status.index < maxResults }">
			<li/>${item}</li>
		</c:if>
		<c:if test="${separator != null}">${separator}</c:if>
		<c:if test="${maxResults == status.count}">...</c:if>
	</c:forEach>
</c:if>
<div id="idmap" style="display:none">addressIdMap = new Array();<c:forEach var="item" items="${idmaps}" varStatus="status">var tmp${status.index} = new Object();tmp${status.index}.id=${item['id']}; tmp${status.index}.name='${item['name']}';addressIdMap.push(tmp${status.index});</c:forEach></div>