<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="resources/touchscreenHeader.jsp"%>

<style>
 table {
 	border-color: black;
 	border-width: 1px;
	border-style: solid;
 	overflow-y: auto;
 	background-color: lightgrey;
 	padding: 1px;
 	height: 2.7em;
 }
input {
autocomplete:off;	
}	
</style>

<script>
	
	function print(){
		var select = document.getElementById('howManyIDs');
		var location = document.getElementById('forWhere');
		if (select.options[select.selectedIndex].value == ''){
			alert('Please choose a number of IDs');
			return;
		}
		if (location.options[location.selectedIndex].value == ''){
			alert('Please choose a location');
			return;
		}	
		window.open('bulkIds.form?howManyIds=' + select.options[select.selectedIndex].value + "&location=" + location.options[location.selectedIndex].value);
		select.value="";
		location.value="";
		document.getElementById('printmessage').innerHTML='<i><spring:message code="rwandaprimarycare.idsGenerated"/>!</i>';
	}
	
	function printAsBarcodes(){
		var select = document.getElementById('howManyIDsBarcode');
		var location = document.getElementById('forWhereBarcode');
		if (select.options[select.selectedIndex].value == ''){
			alert('Please choose a number of IDs');
			return;
		}
		if (location.options[location.selectedIndex].value == ''){
			alert('Please choose a location');
			return;
		}
		window.open('barCodeOtherLocation.form?howManyIds=' + select.options[select.selectedIndex].value + "&location=" + location.options[location.selectedIndex].value);
		select.value="";
		location.value="";
		document.getElementById('printmessage').innerHTML='<i><spring:message code="rwandaprimarycare.barcodesPrinted"/>!</i>';
	}
	
</script>

<span class="bigtext">
<h2> &nbsp;&nbsp; <spring:message code="rwandaprimarycare.bulkIdGeneration"/></h2><br/><div id="printmessage"></div>
<br/>
<h2>CSV</h2>
<br/>
<table class="portlet" style=""> 
	<tr>	
		<td><spring:message code="rwandaprimarycare.howmanyidsdoyouwanttogenerate"/>?</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<select name="howManyIDs" id="howManyIDs">
				<option value=""></option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="5">5</option>
				<option value="10">10</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
				<option value="60">60</option>
				<option value="70">70</option>
				<option value="80">80</option>
				<option value="90">90</option>
				<option value="100">100</option>
				<option value="150">150</option>
				<option value="200">200</option>
				<option value="200">300</option>
				<option value="200">500</option>
			</select>
		</td>
	</tr>
		<tr>	
		<td><spring:message code="rwandaprimarycare.forwhatlocationdoyouwanttogenerateIds"/>?</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<select name="forWhere" id="forWhere">
				<option value=""></option>
				<c:forEach var="loc" items="${validIdLocations}">
					<option value="${loc.id}">${loc.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="label"><spring:message code="rwandaprimarycare.touchscreen.finish"/></c:set>
			<touchscreen:button label="${label}" onClick="print();"/>
		</td>
	</tr>	
</table>
<br/>
<br/>
<br/>
<h2>BARCODE</h2>
<br/>
<table class="portlet" style=""> 
	<tr>	
		<td><spring:message code="rwandaprimarycare.howmanynewids"/>?</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<select name="howManyIDs" id="howManyIDsBarcode">
				<option value=""></option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="5">5</option>
				<option value="10">10</option>
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="40">40</option>
				<option value="50">50</option>
				<option value="60">60</option>
				<option value="70">70</option>
				<option value="80">80</option>
				<option value="90">90</option>
				<option value="100">100</option>
				<option value="150">150</option>
				<option value="200">200</option>
			</select>
		</td>
	</tr>
		<tr>	
		<td><spring:message code="rwandaprimarycare.forwhatlocationdoyouwanttogenerateIds"/>?</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<select name="forWhereBarcode" id="forWhereBarcode">
				<option value=""></option>
				<c:forEach var="loc" items="${validIdLocations}">
					<option value="${loc.id}">${loc.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="label"><spring:message code="rwandaprimarycare.touchscreen.finish"/></c:set>
			<touchscreen:button label="${label}" onClick="printAsBarcodes();"/>
		</td>
	</tr>	
</table>
</span>
<%@ include file="resources/touchscreenFooter.jsp"%>
