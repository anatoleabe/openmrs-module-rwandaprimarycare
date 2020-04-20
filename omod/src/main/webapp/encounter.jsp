<%@ include file="/WEB-INF/template/include.jsp"%>
<c:set var="viewing_patient" value="${patient}"/>
<%@ include file="resources/touchscreenHeader.jsp"%>

<script type="text/javascript">
	$j(document).ready(function() {
		$j("#deleteDialog").dialog({
				autoOpen: false,
				modal: true
			});
	});
</script>

<touchscreen:patientButton patient="${patient}" href="${returnUrl}"/>

<div>
	<span class="bigtext">${encounter.encounterType.name}</span> <spring:message code="rwandaprimarycare.touchscreen.encounter"/>
	<spring:message code="rwandaprimarycare.touchscreen.on"/> <span class="bigtext"><openmrs:formatDate date="${encounter.encounterDatetime}"/></span>
	<spring:message code="rwandaprimarycare.touchscreen.at"/> <span class="bigtext">${encounter.location.name}</span>
	<spring:message code="rwandaprimarycare.touchscreen.by"/> <span class="bigtext">${encounter.provider.personName}</span>
</div>

<c:if test="${encounter.voided}">
	<h1 style="font-color: red">
		<spring:message code="rwandaprimarycare.touchscreen.deletedBy"/> ${encounter.voidedBy.personName} <spring:message code="rwandaprimarycare.touchscreen.forReason"/> ${encounter.voidReason} <spring:message code="rwandaprimarycare.touchscreen.on"/> <openmrs:formatDate date="${encounter.dateVoided}"/>
	</h1>
</c:if>

<hr/>
<h3><spring:message code="rwandaprimarycare.touchscreen.observations"/></h3>
<c:if test="${fn:length(encounter.obs) == 0}">
	<spring:message code="rwandaprimarycare.touchscreen.none"/>
</c:if>
<c:if test="${fn:length(encounter.obs) != 0}">
	<table>
		<c:forEach var="obs" items="${encounter.obs}" end="6">
			<tr>
				<td align="right"><small><openmrs:format concept="${obs.concept}"/>:</small></td>
				<td><openmrs:format obsValue="${obs}"/></td>
			</tr>
		</c:forEach>
	</table>
</c:if>

<hr/>
${returnUrl}
<c:set var="back"><spring:message code="rwandaprimarycare.touchscreen.back"/></c:set>
<c:if test="${not empty href}">
	<touchscreen:button label="${back}" cssClass="gray" href="${returnUrl}"/>
</c:if>
<c:if test="${empty href}">
	<touchscreen:button label="${back}" cssClass="gray" onClick="history.back()"/>
</c:if>

  
<c:set var="delete"><spring:message code="rwandaprimarycare.touchscreen.delete"/></c:set>
<c:if test="${encounter.encounterType == vitalsEncounterType || encounter.encounterType == registrationEncounterType}">
	<touchscreen:button label="${delete}" cssClass="red button" onClick="$j('#deleteDialog').dialog('open')"/>
</c:if>

<div id="deleteDialog" title="Delete encounter" align="center">
	<c:url var="deleteUrl" value="deleteEncounter.form">
		<c:param name="encounterId" value="${encounter.encounterId}"/>
		<c:param name="returnUrl" value="${returnUrl}"/>
	</c:url>
	<span class="bigtext"><spring:message code="rwandaprimarycare.touchscreen.reallyDelete"/></span> <br/>
	<c:set var="yes"><spring:message code="rwandaprimarycare.touchscreen.yes"/></c:set>
	<c:set var="no"><spring:message code="rwandaprimarycare.touchscreen.no"/></c:set>
	<touchscreen:button label="${no}" onClick="$j('#deleteDialog').dialog('close')"/>
	<touchscreen:button label="${yes}" cssClass="red button" href="${deleteUrl}"/>
</div>

<%@ include file="resources/touchscreenFooter.jsp"%>