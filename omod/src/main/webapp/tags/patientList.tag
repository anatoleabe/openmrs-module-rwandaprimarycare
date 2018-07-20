<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ taglib prefix="touchscreen" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<%@ attribute name="patients" type="java.util.List" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>
<%@ attribute name="href" required="false" rtexprvalue="true" %>
<%@ attribute name="separator" required="false" rtexprvalue="false" %>
<%@ attribute name="maxResults" type="java.lang.Integer" required="false" rtexprvalue="true" %>
<%@ attribute name="size" required="false" rtexprvalue="false" %>
<%@ attribute name="showAllIds" type="java.lang.Boolean" required="false" rtexprvalue="true" %>
 
<div style="clear: both">
	<c:if test="${fn:length(patients) == 0}">
		<c:if test="${title != null}">
			&nbsp;test
		</c:if>
		<c:if test="${title == null}">
			
			<span class="bigtext">
				<br/><spring:message code="rwandaprimarycare.touchscreen.noPatients"/>
			</span>
		</c:if>
	</c:if>
	<c:if test="${fn:length(patients) > 0}">
		<u>
			<span class="bigtext">
				<c:if test="${title != null}">
					<br/>${title}
				</c:if>
				<c:if test="${title == null}">
					<!--<br/>${fn:length(patients)} patient(s):-->
				</c:if>
			</span>
		</u>
		<br/>
			<table>	<tr><td width="50px"></td><td>
				    <c:if test="${maxResults != null && fn:length(patients) > maxResults}">
					<table>
						<tr><Td style="width: 70px;">
								<touchscreen:button onClick="toggle(-1)" label="&lt;&lt;" cssClass="dark" dom_element_id="backLink" disabled="true"/>
						</td><td>
								<touchscreen:button onClick="toggle(1)" label="&gt;&gt;" cssClass="dark" dom_element_id="nextLink"/>
						</td>	
						</tr>
						<tr><td colspan="2" id=""><span id="recordSpan" class="bigtext"></span></td><tr>	
						<tr><td colspan="2"><hr></td><tr>
					</table>	
					</c:if>
				<c:forEach var="patient" items="${patients}" varStatus="status">
							
							<c:if test="${maxResults == null || status.index < maxResults }">
								<span id="nextResults_${status.index}" class="show">
									<touchscreen:patientButton patient="${patient}" href="${href}" showAllIds="${showAllIds}"/>
									<c:if test="${separator != null}">${separator}</c:if>
								</span>
							</c:if>
							<c:if test="${maxResults != null && status.index >= maxResults }">
								<span id="nextResults_${status.index}" class="hide">
									<touchscreen:patientButton patient="${patient}" href="${href}" showAllIds="${showAllIds}"/>
									<c:if test="${separator != null}">${separator}</c:if>
								</span>
							</c:if>
							
				</c:forEach>
				</td></tr>
			</table>	
	</c:if>
</div>


<script>

	var size = ${fn:length(patients)};
	var maxResults = ${maxResults};
	var whereAreWe = 0;
	var nextLink = document.getElementById("nextLink");
	var backLink = document.getElementById("backLink");
	if (whereAreWe < size - maxResults){
		 $j(nextLink).removeAttr("disabled");
		 setRecordCounterString();
	} else {
		 $j(nextLink).attr("disabled", "true");
	}
	
	
	function setRecordCounterString(){
		var recordSpan = document.getElementById("recordSpan");
		var whereAreWeReadable = whereAreWe + 1;
		var ret = whereAreWeReadable + " - ";
		var maxShown = size;
		if (maxShown > (whereAreWe + maxResults)){
			maxShown = whereAreWe + maxResults;
		}
		ret += maxShown +  " / " + size;
		$j(recordSpan).html("<it>&nbsp;&nbsp;" + ret + "</it>");
	}

	function toggle(val){
		if (val == 1){
			whereAreWe = whereAreWe + maxResults;
			if (whereAreWe < size - maxResults){
				 $j(nextLink).removeAttr("disabled");
			} else {
				 $j(nextLink).attr("disabled", "true");
			}
			$j(backLink).removeAttr("disabled");
		}
		if (val == -1){
			whereAreWe = whereAreWe - maxResults;
			if (whereAreWe < size - maxResults){
				 $j(nextLink).removeAttr("disabled");
			}
			if (whereAreWe == 0){
				$j(backLink).attr("disabled", "true");
			}
		}
		showPatientButtons();
		setRecordCounterString();
	}
	
	function showPatientButtons(){
		for (var i = 0; i < size; i++ ){
			var buttonSpanIdString = "nextResults_" + i;
			var span = document.getElementById(buttonSpanIdString); 
			if (i >= whereAreWe && i < whereAreWe + maxResults){
				$j(span).removeClass("hide");
				$j(span).addClass("show");
			} else {
				$j(span).removeClass("show");
				$j(span).addClass("hide");
			}
		}
	}
	
</script>
</script>