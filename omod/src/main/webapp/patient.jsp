<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<br/>
	<span>
	<table style="position:relative;left:10%;">
	<tr>
		<td>
		<%@ include file="patientResultsTableYellow.jspf"%>
		</td>
		<td valign="center">
			<c:set var="editPatient"><spring:message code="rwandaprimarycare.touchscreen.edit"/></c:set>
			<touchscreen:button label="${editPatient}" href="editPatient.form?patientId=${patient.patientId}" cssClass="green"/>
		</td>
		<!-- this is here so that providers who see the diagnosis capture app can jump between registration and diagnoses -->
		<c:if test="${!empty showDiagnosisLink}">
			<openmrs:hasPrivilege privilege="Diagnosis Capture">
				<c:set var="goToDiagnosisApp"><spring:message code="rwandaprimarycare.goToDiagnosisApplication"/></c:set>
				<td align="center">
					<touchscreen:button label="${goToDiagnosisApp}" cssClass="dark" href="${pageContext.request.contextPath}/module/diagnosiscapturerwanda/diagnosisPatientDashboard.list?patientId=${patient.patientId}&encounterUuid=${registrationEncounterToday.uuid}"/>
				</td>	
			</openmrs:hasPrivilege>
		</c:if>
	</tr>
	</table>
	</span>
<br/>
<br/>

<table width="100%">
	<tr>
		<td width="50%" style="background-color: #e0f0e0" align="center"><span class="bigtext"><u><c:choose><c:when test="${!empty todaysDate }"><openmrs:formatDate date="${todaysDate}" type="medium" /></c:when><c:otherwise><spring:message code="rwandaprimarycare.touchscreen.todaysVisit"/></c:otherwise></c:choose></u></span></td>
		<td width="25%" align="center"><span class="bigtext"><u><spring:message code="rwandaprimarycare.touchscreen.enterVisitData"/></u></span></td>
		<td width="25%" align="center"><span class="bigtext"><u><spring:message code="rwandaprimarycare.touchscreen.printAnotherBarCode"/></u></span></td>
	</tr>
	<tr valign="top">
		<td style="background-color: #e0f0e0">
			<c:if test="${fn:length(encountersToday) == 0}">
				<spring:message code="rwandaprimarycare.touchscreen.noData"/>
			</c:if>
			<table>
				<c:forEach var="encounter" items="${encountersToday}">
					<c:if test="${encounter.voided == false}">
						<tr>
							<td>
								<openmrs:formatDate format="HH:mm" date="${encounter.encounterDatetime}"/>
							</td>
							<td align="right">				
								<c:set var="edit"><spring:message code="rwandaprimarycare.edit"/></c:set>
								<c:if test="${encounter.encounterType == registrationEncounterType}">
										<touchscreen:button label="${edit}" href="${pageContext.request.contextPath}/module/rwandaprimarycare/patient.form?patientId=${patient.patientId}&serviceRequested=0"/>	
								</c:if>
								<touchscreen:encounterButton encounter="${encounter}" size="small"/>
							</td>
							<td>
			
									<c:if test="${fn:length(encounter.obs) == 0}">
										<spring:message code="rwandaprimarycare.touchscreen.noObservations"/>
									</c:if>
									<c:forEach var="obs" items="${encounter.obs}" end="6">
										<openmrs:format concept="${obs.concept}"/>: <openmrs:format obsValue="${obs}"/> <br/>
									</c:forEach>
									<c:if test="${fn:length(encounter.obs) > 6}">
										...
									</c:if>
						
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
		</td>
		<td align="center">
		<c:set var="bcVitals"><spring:message code="rwandaprimarycare.touchscreen.vitals"/></c:set>
		<c:set var="bcDiagnosis"><spring:message code="rwandaprimarycare.touchscreen.diagnosis"/></c:set>
			<c:choose>
				<c:when test="${empty visitDate}">
					<touchscreen:button label="${bcVitals}" href="enterSimpleEncounter.form?form=vitals&patientId=${patient.patientId}" cssClass="green"/> <br/>
				</c:when>
				<c:otherwise>
					<touchscreen:button label="${bcVitals}" href="enterSimpleEncounter.form?form=vitals&patientId=${patient.patientId}&visitDate=${visitDate}" cssClass="green"/> <br/>
				</c:otherwise>
			</c:choose>
		<br/>
		</td>
		<td align="center">
			<c:set var="bcString"><spring:message code="rwandaprimarycare.touchscreen.printAnotherBarCode"/></c:set>
			<touchscreen:button label="${bcString}" onClick="window.open('barCode.form?patientId=${patient.patientId}');" cssClass="green"/> <br/>
		</td>
	</tr>
</table>

<%-- Commenting this out until later, see JIRA ticket RWA-159
<c:if test="${fn:length(beforeToday) > 0}">
	<td rowspan="${1 + fn:length(encountersToday) }">
		<touchscreen:button cssClass="gray" label="Full visit history" href="allEncounters.form?patientId=${patient.patientId}"/>
	</td>
</c:if>
--%>

<%@ include file="resources/touchscreenFooter.jsp"%>