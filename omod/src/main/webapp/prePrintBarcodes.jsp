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
		var select = document.getElementById('howManyBarcodes');
		if (select.options[select.selectedIndex].value == ''){
			alert('Please choose a number of IDs');
			return;
		}
		window.open('barCode.form?multiple=true&howManyOfflineIds=' + select.options[select.selectedIndex].value);
		document.getElementById('howManyBarcodes').value="";
		document.getElementById('printmessage').innerHTML='<i><spring:message code="rwandaprimarycare.barcodesPrinted"/>!</i>';
	}
	
</script>

<span class="bigtext">
<h2><spring:message code="rwandaprimarycare.offlineBarcodePrinting"/></h2><br/><div id="printmessage"></div>
<br/>
<table class="portlet" style=""> 
	<tr>	
		<td><spring:message code="rwandaprimarycare.howmanynewids"/> <spring:message code="rwandaprimarycare.touchscreen.at"/> ${userLocation}?</td>
		<td>&nbsp;&nbsp;</td>
		<td>
			<select name="howManyBarcodes" id="howManyBarcodes">
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
		<td>
			<c:set var="label"><spring:message code="rwandaprimarycare.touchscreen.finish"/></c:set>
			<touchscreen:button label="${label}" onClick="print();"/>
		</td>
	</tr>	
</table>
<br/>
</span>
<%@ include file="resources/touchscreenFooter.jsp"%>
