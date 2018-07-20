<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<!-- setup tst cancel button -->
<script>
  var tt_cancel_destination = "${pageContext.request.contextPath}/module/rwandaprimarycare/login/login.form"
</script>

<c:set var="locationText" scope="page"><spring:message code="rwandaprimarycare.touchscreen.chooseLocation"/></c:set>
<form method="POST" autocomplete="off">
	<table>
		<tr>
			<td><spring:message code="User.location"/></td>
			<td>
				<select name="location" helpText="${locationText}">
					<option value=""></option>
					<c:forEach var="location" items="${locations}">
						<option value="${location.locationId}" 
							<c:if test="${userLocation == location}">
								SELECTED 
							</c:if>
						>${location.name}</option>
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
