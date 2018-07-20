<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<!-- setup tst cancel button -->
<script>
  var tt_cancel_destination = "${pageContext.request.contextPath}/module/rwandaprimarycare/login/login.form"
</script>

<c:set var="localeText" scope="page"><spring:message code="rwandaprimarycare.touchscreen.chooseLanguage"/></c:set>
<form action="languageChanged.form" method="POST" autocomplete="off">
	<table>
		<tr>
			<td><spring:message code="rwandaprimarycare.touchscreen.locale"/></td>
			<td>
				<select name="locales" helpText="${localeText}" field_type="disabled">
					<option value=""></option>
					<c:forEach var="locale" items="${locales}">
						<option value="${locale}" >
						${locale.displayName}
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Submit"/></td>
		</tr>
	</table>
	<br/>	
</form>	

<script type="text/javascript">
 	document.getElementById('username').focus();
</script>


<%@ include file="resources/touchscreenFooter.jsp"%>
