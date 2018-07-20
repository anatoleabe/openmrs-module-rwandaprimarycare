<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ page import="org.openmrs.web.WebConstants" %>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>
<%
    pageContext.setAttribute("keyboardTypes", session.getAttribute("keyboardType"));
	pageContext.setAttribute("msg", session.getAttribute(WebConstants.OPENMRS_MSG_ATTR));
	pageContext.setAttribute("msgArgs", session.getAttribute(WebConstants.OPENMRS_MSG_ARGS));
	pageContext.setAttribute("err", session.getAttribute(WebConstants.OPENMRS_ERROR_ATTR));
	pageContext.setAttribute("errArgs", session.getAttribute(WebConstants.OPENMRS_ERROR_ARGS));
	session.removeAttribute(WebConstants.OPENMRS_MSG_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_MSG_ARGS);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ATTR);
	session.removeAttribute(WebConstants.OPENMRS_ERROR_ARGS);
%>
<openmrs:authentication/>

<%@page import="org.openmrs.module.rwandaprimarycare.PrimaryCareConstants"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<openmrs:htmlInclude file="/openmrs.css" />
		<openmrs:htmlInclude file="/style.css" />
		<openmrs:htmlInclude file="/openmrs.js" />
		<!-- need to load the DWR first because touchscreenToolkit accesses it during initialization -->
		<openmrs:htmlInclude file="/dwr/engine.js" />
		<script src='<%= request.getContextPath() %>/dwr/interface/RwandaDWRService.js'></script>

		<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/touchscreenToolkit.js" />
		<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/touch-fancy.css" />
		<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/jquery-1.3.2.min.js" />
		<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/jquery-ui-1.7.1.custom.min.js" />
		<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/smoothness/jquery-ui-1.7.1.custom.css" />
		
		<c:set var="next"><spring:message code="rwandaprimarycare.touchscreen.next"/></c:set>
		<c:set var="back"><spring:message code="rwandaprimarycare.touchscreen.back"/></c:set>
		<c:set var="finish"><spring:message code="rwandaprimarycare.touchscreen.finish"/></c:set>
		<c:set var="skip"><spring:message code="rwandaprimarycare.touchscreen.skip"/></c:set>
		<c:set var="clear"><spring:message code="rwandaprimarycare.touchscreen.clear"/></c:set>
		<c:set var="cancel"><spring:message code="rwandaprimarycare.touchscreen.cancel"/></c:set>
		<c:set var="yes"><spring:message code="rwandaprimarycare.touchscreen.yes"/></c:set>
		<c:set var="no"><spring:message code="rwandaprimarycare.touchscreen.no"/></c:set>
		<c:set var="authorise"><spring:message code="rwandaprimarycare.touchscreen.authorise"/></c:set>
		<c:set var="username"><spring:message code="rwandaprimarycare.touchscreen.username"/></c:set>
		<c:set var="showData"><spring:message code="rwandaprimarycare.touchscreen.showData"/></c:set>
		<c:set var="newPatient"><spring:message code="rwandaprimarycare.touchscreen.newPatient"/></c:set>
		<c:set var="newGuardian"><spring:message code="rwandaprimarycare.touchscreen.newGuardian"/></c:set>
		<c:set var="disable"><spring:message code="rwandaprimarycare.touchscreen.disable"/></c:set>
		<c:set var="enable"><spring:message code="rwandaprimarycare.touchscreen.enable"/></c:set>
		<c:set var="year"><spring:message code="rwandaprimarycare.touchscreen.year"/></c:set>
		<c:set var="month"><spring:message code="rwandaprimarycare.touchscreen.month"/></c:set>
		<c:set var="hour"><spring:message code="rwandaprimarycare.touchscreen.hour"/></c:set>
		<c:set var="minute"><spring:message code="rwandaprimarycare.touchscreen.minute"/></c:set>
		<c:set var="date"><spring:message code="rwandaprimarycare.touchscreen.date"/></c:set>
		<c:set var="capturedDate"><spring:message code="rwandaprimarycare.touchscreen.capturedDate"/></c:set>
		<c:set var="usernameInvalid"><spring:message code="rwandaprimarycare.touchscreen.usernameInvalid"/></c:set>
		<c:set var="wantToCancel"><spring:message code="rwandaprimarycare.touchscreen.wantToCancel"/></c:set>
		<c:set var="enterValue"><spring:message code="rwandaprimarycare.touchscreen.enterValue"/></c:set>
		<c:set var="enterValidValue"><spring:message code="rwandaprimarycare.touchscreen.enterValidValue"/></c:set>
		<c:set var="valueOutOfRange"><spring:message code="rwandaprimarycare.touchscreen.valueOutOfRange"/></c:set>
		<c:set var="valueTooSmall"><spring:message code="rwandaprimarycare.touchscreen.valueTooSmall"/></c:set>
		<c:set var="valueTooBig"><spring:message code="rwandaprimarycare.touchscreen.valueTooBig"/></c:set>
		<c:set var="selectValue"><spring:message code="rwandaprimarycare.touchscreen.selectValue"/></c:set>
		<c:set var="notMsg"><spring:message code="rwandaprimarycare.touchscreen.not"/></c:set>
		<c:set var="ok"><spring:message code="rwandaprimarycare.touchscreen.ok"/></c:set>
		<c:set var="delete"><spring:message code="rwandaprimarycare.touchscreen.delete"/></c:set>
		<c:set var="space"><spring:message code="rwandaprimarycare.touchscreen.space"/></c:set>
		<c:set var="shift"><spring:message code="rwandaprimarycare.touchscreen.shift"/></c:set>
		<c:set var="unknown"><spring:message code="rwandaprimarycare.touchscreen.unknown"/></c:set>
		<c:set var="upper"><spring:message code="rwandaprimarycare.touchscreen.upper"/></c:set>
		<c:set var="lower"><spring:message code="rwandaprimarycare.touchscreen.lower"/></c:set>
		
		<script type="text/javascript">
			/* don't have jquery take over $() */
	     	var $j = jQuery.noConflict();
			/* variable used in js to know the context path */
			var openmrsContextPath = '${pageContext.request.contextPath}';
			
			var messageMap = new Array();
			messageMap = {'touchscreen.next':"${next}",
			                    'touchscreen.back':"${back}",
			                    'touchscreen.finish':"${finish}",
			                    'touchscreen.skip':"${skip}",
			                    'touchscreen.clear':"${clear}",
			                    'touchscreen.cancel':"${cancel}",
			                    'touchscreen.yes':"${yes}",
			                    'touchscreen.no':"${no}",
			                    'touchscreen.authorise':"${authorise}",
			                    'touchscreen.username':"${username}",
			                    'touchscreen.showData':"${showData}",
			                    'touchscreen.newPatient':"${newPatient}",
			                    'touchscreen.newGuardian':"${newGuardian}",
			                    'touchscreen.disable':"${disable}",
			                    'touchscreen.enable':"${enable}",
			                    'touchscreen.year':"${year}",
			                    'touchscreen.month':"${month}",
			                    'touchscreen.hour':"${hour}",
			                    'touchscreen.minute':"${minute}",
			                    'touchscreen.date':"${date}",
			                    'touchscreen.capturedDate':"${capturedDate}",
			                    'touchscreen.usernameInvalid':"${usernameInvalid}",
			                    'touchscreen.wantToCancel':"${wantToCancel}",
			                    'touchscreen.enterValue':"${enterValue}",
			                    'touchscreen.enterValidValue':"${enterValidValue}",
			                    'touchscreen.valueOutOfRange':"${valueOutOfRange}",
			                    'touchscreen.valueTooSmall':"${valueTooSmall}",
			                    'touchscreen.valueTooBig':"${valueTooBig}",
			                    'touchscreen.selectValue':"${selectValue}",
			                    'touchscreen.not':"${notMsg}",
			                    'touchscreen.ok':"${ok}",
			                    'touchscreen.delete':"${delete}",
			                    'touchscreen.space':"${space}",
			                    'touchscreen.shift':"${shift}",
			                    'touchscreen.unknown':"${unknown}",
			                    'touchscreen.upper':"${upper}",
			                    'touchscreen.lower':"${lower}"
			};
		</script>
		
		<c:choose>
			<c:when test="${!empty pageTitle}">
				<title>${pageTitle}</title>
			</c:when>
			<c:otherwise>
				<title><spring:message code="openmrs.title"/></title>
			</c:otherwise>
		</c:choose>		
		
	</head>

<body>
	<!-- setup tst defaults: cancel button for patient search -->
	<script>
	  	var tt_cancel_destination = "${pageContext.request.contextPath}/module/rwandaprimarycare/homepage.form"
	  	var tstUsername = '${authenticatedUser.username}';
	  	var userKeyboardType= '${keyboardTypes}';
	  	setUserKeyboard(userKeyboardType);
	</script>
	<div id="pageBody">
		<%--
		<c:if test="${authenticatedUser != null}">
			<div style="float: right; background-color: #f0f0f0; border: 1px black solid; padding: 5px" align="right">
				User: ${authenticatedUser.username} <br/>
				<%
					org.openmrs.Location workstationLocation = (org.openmrs.Location) session.getAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_WORKSTATION_LOCATION);
					if (workstationLocation != null) { %>
						@ <%= workstationLocation.getName() %><br/>
				<% } %>
				<c:if test="${ !fn:contains(pageContext.request.servletPath, 'homepage.') && !fn:contains(pageContext.request.servletPath, 'login.')}">
					<touchscreen:button label="Start over" href="${pageContext.request.contextPath}/module/rwandaprimarycare/homepage.form"/> <br/>
				</c:if>
				<c:if test="${ fn:contains(pageContext.request.servletPath, 'homepage.')}">
					<touchscreen:button label="Logout" href="${pageContext.request.contextPath}/module/rwandaprimarycare/login/logout.form"/>
				</c:if>
			</div>
		</c:if>
		--%>
		<c:set var="logout"><spring:message code="rwandaprimarycare.logout"/></c:set>
		<c:set var="startover"><spring:message code="rwandaprimarycare.startover"/></c:set>
		<div id="contentMinimal" style="font-size:90%;">
			<c:if test="${ !fn:contains(pageContext.request.servletPath, 'homepage.') && !fn:contains(pageContext.request.servletPath, 'login.')}">
				<div style="float: left"><touchscreen:button label="${startover}" href="${pageContext.request.contextPath}/module/rwandaprimarycare/homepage.form"/></div>
				<div style="float: right; padding: 0px;"><touchscreen:button label="${logout}" href="${pageContext.request.contextPath}/module/rwandaprimarycare/login/logout.form"/></div>
			</c:if>
			
			<%--
			<c:if test="${msg != null}">
				<div id="openmrs_msg"><spring:message code="${msg}" text="${msg}" arguments="${msgArgs}" /></div>
			</c:if>
			<c:if test="${err != null}">
				<div id="openmrs_error"><spring:message code="${err}" text="${err}" arguments="${errArgs}"/></div>
			</c:if>
			--%>
			<c:if test="${msg != null}">
				<div id="flash_notice">${msg}</div>
			</c:if>
			<c:if test="${msg != null}">
				<div id="flash_error">${err}</div>
			</c:if>