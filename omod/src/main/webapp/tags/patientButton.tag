<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<%@ attribute name="patient" type="org.openmrs.Patient" required="true" rtexprvalue="true" %>
<%@ attribute name="href" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="false" %>
<%@ attribute name="clickable" type="java.lang.Boolean" required="false" rtexprvalue="false" %>
<%@ attribute name="showAllIds" type="java.lang.Boolean" required="false" rtexprvalue="true" %>

<c:choose>
	<c:when test="${clickable == null || clickable}">
		<c:if test="${not empty href}">
			<%-- Make sure there's a patientId= in the href --%>
			<c:if test="${!fn:contains(href, 'patientId=')}">
				<c:choose>
					<c:when test="${fn:contains(href, '?')}">
						<c:set var="href" value="${href}&skipPresentQuestion=false&patientId=${patient.patientId}"/>
					</c:when>
					<c:otherwise>
						<c:set var="href" value="${href}?skipPresentQuestion=false&patientId=${patient.patientId}"/>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:if>
		<c:if test="${empty href}">
			<c:set var="href" value="patient.form?skipPresentQuestion=false&patientId=${patient.patientId}"/>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="href" value=""/>
	</c:otherwise>
</c:choose>

<!--  get health center -->
<c:set var="healthCenter" value="" />
<c:forEach items="${patient.attributes}" var="attribute" varStatus="varStatus">
		<c:if test="${attribute.attributeType != null && attribute.attributeType.name != null && attribute.attributeType.name == 'Health Center' && attribute.voided == false}">
			<c:set var="healthCenter" value="(${attribute.hydratedObject})" />
		</c:if>
</c:forEach>

<c:choose>
	<c:when test="${size == 'small'}">
		<c:set var="buttonLabel" value="${fn:toUpperCase(patient.personName.familyName)} ${patient.personName.givenName}"/>
	</c:when>
	<c:otherwise>
		<c:set var="yoStr"><spring:message code="rwandaprimarycare.touchscreen.yearsOld"/></c:set>
		<c:set var="buttonLabel" value="${fn:toUpperCase(patient.personName.familyName)} ${patient.personName.givenName} - ${patient.age}${yoStr} ${patient.gender}  ${healthCenter} - ${patient.personAddress.address1}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty href}">
		<table><tr><td>
		<touchscreen:button cssClass="gray"	label="${buttonLabel}" href="${href}" />
		</td>
		<td style="vertical-align:top;">	
		
					<!-- BUILD THE ID TABLE -->
					<c:if test="${showAllIds}">
						<c:forEach items="${identifierTypes}" var="idType" varStatus="index">
							<c:if test="${idType.name != null}">
								<span class="bigtext"> ${idType.name}:
										<c:forEach items="${patient.identifiers}" var="id" varStatus="varStatus">
											<c:if test="${id.identifierType != null && id.identifierType == idType}">
												 <b>${id.identifier}</b>
											</c:if>
										</c:forEach>
								</span><br/>
							</c:if>			
						</c:forEach>
					</c:if>
					
		</td></tr></table>		
					
	</c:when>
	<c:otherwise>
		<touchscreen:button cssClass="dark"	label="${buttonLabel}" /> 	
	</c:otherwise>
</c:choose>
