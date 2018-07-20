<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<touchscreen:patientButton patient="${patient}"/>

<table>
<c:forEach var="onDate" items="${encounters}">
	<tr valign="top" style="border-top: 1px black solid">
		<td>
			<openmrs:formatDate format="dd/MMM/yyyy" date="${onDate.key}"/> <br/>
		</td>
		<td>
			<c:forEach var="encounter" items="${onDate.value}">
				<touchscreen:encounterButton size="small" encounter="${encounter}" returnUrl="allEncounters.form?patientId=${patient.patientId}"/>
			</c:forEach>
		</td>
	</tr>
</c:forEach>
</table>

<%@ include file="resources/touchscreenFooter.jsp"%>