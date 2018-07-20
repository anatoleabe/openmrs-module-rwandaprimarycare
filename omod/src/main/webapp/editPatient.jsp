<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/addresshierarchyrwanda.js" />
<c:url var="submitUrl" value="editPatient.form">
	<c:forEach var="p" items="${param}">
		<c:if test="${p.key != 'addIdentifier'}">
			<c:if test="${p.value != ''}">
				<c:param name="${p.key}" value="${p.value}"/>
			</c:if>
		</c:if>
	</c:forEach>
</c:url>

<form method="post" action="${submitUrl}">
	<table>
		<tr>
			<td><spring:message code='rwandaprimarycare.touchscreen.rwandanName' /></td>
				<c:set var="rwandanName"><spring:message code='rwandaprimarycare.touchscreen.rwandanName' /></c:set>
				<td><touchscreen:textInput required="true"  label="${rwandanName}" field_name="familyName" value="${patient.familyName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=RWNAME&search=" fieldType="upper"/></td>
		</tr>
		<tr>
			<td><spring:message code='rwandaprimarycare.touchscreen.christianName' /></td>
				<c:set var="christianName"><spring:message code='rwandaprimarycare.touchscreen.christianName' /></c:set>
			<td><touchscreen:textInput required="true" label="${christianName}" field_name="givenName" value="${patient.givenName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FANAME&search="/></td>
		</tr>
		
		<tr>
			<td>Gender</td> 
			<td>
				<c:set var="gender"><spring:message code='rwandaprimarycare.touchscreen.gender' /></c:set>
				<c:set var="male"><spring:message code='rwandaprimarycare.touchscreen.male' /></c:set>
				<c:set var="female"><spring:message code='rwandaprimarycare.touchscreen.female' /></c:set>
				<select optional="true" name="gender" label="${gender}" helpText="${gender}">
					<c:choose>
						<c:when test="${patient.gender == 'M'}">
							<option value="M" selected=true>${male}</option>
							<option value="F">${female}</option>
						</c:when>
						<c:otherwise>
							<option value="M">${male}</option>
							<option value="F" selected=true>${female}</option>
						</c:otherwise>
					</c:choose>
				</select>
			</td>
		</tr>	
		<tr>
			<td><spring:message code='rwandaprimarycare.touchscreen.birthdate' /></td>
			<Td>
				<c:set var="bDay"><spring:message code='rwandaprimarycare.touchscreen.birthdateDay' /></c:set>
				<c:set var="bMonth"><spring:message code='rwandaprimarycare.touchscreen.birthdateMonth' /></c:set>
				<c:set var="bYear"><spring:message code='rwandaprimarycare.touchscreen.birthdateYear' /></c:set>
				<c:set var="orLeaveBlank"><spring:message code='rwandaprimarycare.touchscreen.orLeaveBlank' /></c:set>
				<touchscreen:numberInput required="false" field_name="birthdateDay" value="${birthdateDay}" label="${bDay} (${orLeaveBlank} ${patient.age})" min="1" max="31"/>
				<touchscreen:numberInput required="false" field_name="birthdateMonth" value="${birthdateMonth}" label="${bMonth} (${orLeaveBlank} ${patient.age})" min="1" max="12"/>
				<touchscreen:numberInput required="false" field_name="birthdateYear" value="${birthdateYear}" label="${bYear} (${orLeaveBlank} ${patient.age})" min="1910" max="2020"/>
			</Td>
		</tr>

		<c:set var="CountryStr"><spring:message code='rwandaprimarycare.touchscreen.country' /></c:set>
		<c:set var="ProvinceStr"><spring:message code='rwandaprimarycare.touchscreen.province' /></c:set>
		<c:set var="DistrictStr"><spring:message code='rwandaprimarycare.touchscreen.district' /></c:set>
		<c:set var="SectorStr"><spring:message code='rwandaprimarycare.touchscreen.sector' /></c:set>
		<c:set var="CellStr"><spring:message code='rwandaprimarycare.touchscreen.cell' /></c:set>
		<c:set var="UmuduguduStr"><spring:message code='rwandaprimarycare.touchscreen.umudugudu' /></c:set>
		<c:set var="MomStr"><spring:message code='rwandaprimarycare.touchscreen.mom' /></c:set>
		<c:set var="DadStr"><spring:message code='rwandaprimarycare.touchscreen.dad' /></c:set>
		
		<tr>
			<td>Current Country</td>
			<td><touchscreen:textInput required="false" label="${CountryStr}" field_name="COUNTRY" value="${address.country}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=COUNTRY&search="  javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
		<tr>
			<td>Current Province</td>
			<td><touchscreen:textInput required="false" label="${ProvinceStr}" field_name="PROVINCE" value="${address.stateProvince}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=PROVINCE&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
		<tr>
			<td>Current District</td>
			<td><touchscreen:textInput required="false" label="${DistrictStr}" field_name="DISTRICT" value="${address.countyDistrict}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=DISTRICT&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
		<tr>
			<td>Current Sector</td>
			<td><touchscreen:textInput required="false" label="${SectorStr}" field_name="SECTOR" value="${address.cityVillage}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=SECTOR&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
		<tr>
			<td>Current Cell</td>
			<td><touchscreen:textInput required="false" label="${CellStr}" field_name="CELL" value="${address.neighborhoodCell}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=CELL&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
		<tr>
			<td>Current Umudugudu</td>
			<td><touchscreen:textInput required="false" label="${UmuduguduStr}" field_name="UMUDUGUDU" value="${address.address1}" allowFreeText="true" ajaxURL="editPatientAddressAjax.form?searchType=UMUDUGUDU&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
		</tr>
	    <c:if test="${fn:length(parents) > 0 }">
		
		<c:forEach items="${parents}" var="parent" varStatus="varStatus">
			<c:if test="${parent.gender == 'F'}">
				<c:set var="mumStr" value="${parent.personName.familyName}"></c:set> 
			</c:if>

			<c:if test="${parent.gender == 'M'}">
				<c:set var="dadStr" value="${parent.personName.familyName}"></c:set> 
			</c:if>
		</c:forEach>
		
		</c:if>
		<tr>
			<td>Mothers Name</td>
			<td><touchscreen:textInput required="false" label="${MomStr}" field_name="mothersName" value="${mumStr}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=MRWNAME&search="/></td>
		</tr>
		<tr>
			<td>Fathers Name</td>
			<td><touchscreen:textInput required="false" label="${DadStr}" field_name="fathersName" value="${dadStr}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FATHERSRWNAME&search="/></td>
		</tr>
	</table>
	<input type="submit" value="Submit"/>
</form>
<%@ include file="resources/touchscreenFooter.jsp"%>