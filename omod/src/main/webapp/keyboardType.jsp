<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<!-- setup tst cancel button -->
<script>
  var tt_cancel_destination = "${pageContext.request.contextPath}/module/rwandaprimarycare/homepage.form"
</script>


<c:set var="keyboardType" scope="page"><spring:message code="rwandaprimarycare.touchscreen.chooseKeyboard"/></c:set>
<form action="keyboardSelected.form"  method="POST" autocomplete="off">
	<table>
		<tr>
			<td><spring:message code="rwandaprimarycare.userKeyboard"/></td>
			<td>
				<select name="keyboardSelect" helpText="${keyboardType}">
					<option value=""></option>
					<option value="ABC"><spring:message code="rwandaprimarycare.touchscreen.ABCKeyboard"/></option>
                    <option value="QWERTY"><spring:message code="rwandaprimarycare.touchscreen.QWERTYKeyboard"/></option>
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
<%@ include file="resources/touchscreenFooter.jsp"%>
