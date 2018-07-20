<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<style type="text/css">
	table.resultsTable {
		border-collapse: collapse;
	}
	table.resultsTable td, table.resultsTable th {
		border-top: 1px black solid;
		border-bottom: 1px black solid;
	}
</style>
<!-- note:  controller is FindPatientByNameController -->
&nbsp;&nbsp;&nbsp;&nbsp;
<span class="bigtext">
    <table>
    	<Tr><td width='10%'></td>
    	<td>
		<spring:message code="rwandaprimarycare.touchscreen.rightPerson"/>
		<br/>
		<br/>
		
		<table class="resultsTable">
			<tr>
				<td></td>
				<td><spring:message code="rwandaprimarycare.touchscreen.yourSearch"/></td>
				<td><spring:message code="rwandaprimarycare.touchscreen.thisPerson"/></td>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.rwandanName"/></td>
				<th>${searchRWNAME}</th>
				<th>${patient.personName.familyName}</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.christianName"/></td>
				<th>${searchFANAME}</th>
				<th>${patient.personName.givenName}</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.gender"/></td>
				<th>${searchGENDER}</th>
				<th>
					<c:if test="${patient.gender == 'M'}"><spring:message code="rwandaprimarycare.touchscreen.male"/></c:if>
					<c:if test="${patient.gender == 'F'}"><spring:message code="rwandaprimarycare.touchscreen.female"/></c:if>
				</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.age"/></td>
				<th>${searchAGE}</th>
				<th>${patient.age}</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.mom"/></td>
				<th>${searchMRWNAME}</th>
				<th>
				 <c:if test="${fn:length(parents) > 0 }">
					<c:forEach items="${parents}" var="parent" varStatus="varStatus">
						<c:if test="${parent.gender == 'F'}">
							${parent.personName.familyName} &nbsp;
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${fn:length(parents) == 0 }">
						 ${mumStr} &nbsp;
				</c:if>
				</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.dad"/></td>
				<th>${searchFATHERSRWNAME}</th>
				<th>
				 <c:if test="${fn:length(parents) > 0 }">
					<c:forEach items="${parents}" var="parent" varStatus="varStatus">
						<c:if test="${parent.gender == 'M'}">
							${parent.personName.familyName} &nbsp;
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${fn:length(parents) == 0 }">
						 ${dadStr} &nbsp;
				</c:if>
				</th>
			</tr>
			<tr>
				<td align="right"><spring:message code="rwandaprimarycare.touchscreen.umudugudu"/></td>
				<th>${searchUMUDUGUDU}</th>
				<th>
					<c:if test="${!empty patient.addresses}">
						<c:forEach items="${patient.addresses}" var="address" varStatus="varStatus">
								<c:if test="${varStatus.index == 0}">
									${address.address1}  &nbsp;
								</c:if>
						</c:forEach>	
					</c:if>
				</th>
			</tr>
			<tr>
				<td align="right"></td>
				<c:set var="yes"><spring:message code="rwandaprimarycare.touchscreen.yes"/></c:set>
				<c:set var="no"><spring:message code="rwandaprimarycare.touchscreen.no"/></c:set>
				<th><touchscreen:button label="${no}" onClick="window.back()"/></th>
				<th><touchscreen:button label="${yes}" href="patient.list?skipPresentQuestion=false&patientId=${patient.patientId}"/></th>
			</tr>
		</table>
	</td></tr></table>
</span>

<%@ include file="resources/touchscreenFooter.jsp"%>