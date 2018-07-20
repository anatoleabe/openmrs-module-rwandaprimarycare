<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<!--  generate content of Select list -->
<c:if test="${results != null}">
	<touchscreen:resultsList items="${results}" maxResults="50" separator=""/>	
</c:if>