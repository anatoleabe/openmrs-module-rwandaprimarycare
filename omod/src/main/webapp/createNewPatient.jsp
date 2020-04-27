<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/addresshierarchyrwanda.js" />
<c:url var="submitUrl" value="createNewPatient.form">
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
            <td><touchscreen:textInput required="true"  label="${rwandanName}" field_name="familyNameCreate" value="${param.familyName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=RWNAME&search=" fieldType="upper"/></td>
        </tr>
        <tr>
            <td><spring:message code='rwandaprimarycare.touchscreen.christianName' /></td>
            <c:set var="christianName"><spring:message code='rwandaprimarycare.touchscreen.christianName' /></c:set>
            <td><touchscreen:textInput required="true" label="${christianName}" field_name="givenNameCreate" value="${param.givenName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FANAME&search="/></td>
        </tr>

        <!--  <c:if test="${nationalIdIdentifierType != null}">
                <tr><td>
                        <input type="password" optional="true" helpText="<spring:message code='rwandaprimarycare.touchscreen.nationalIdNumberFor' /> ${fn:toUpperCase(param.familyName)} ${param.givenName}"  name="addNationalIdentifierCreate" value="${param.addNationalIdentifier}" field_Type="disabled" />
                </td></tr>
        </c:if>-->

        <tr>
            <td>Gender</td> 
            <td>
                <c:set var="gender"><spring:message code='rwandaprimarycare.touchscreen.gender' /></c:set>
                <c:set var="male"><spring:message code='rwandaprimarycare.touchscreen.male' /></c:set>
                <c:set var="female"><spring:message code='rwandaprimarycare.touchscreen.female' /></c:set>
                <select optional="false" name="gender" label="${gender}" value="${param.gender}" helpText="${gender}">
                    <option value="M">${male}</option>
                    <option value="F">${female}</option>
                </select>
            </td>
        </tr>

        <tr><td>
                <input type="password" optional="true" helpText="<spring:message code='rwandaprimarycare.touchscreen.existingPCId' />"  name="addPCIdentifier" value="" field_Type="disabled" />
            </td></tr>

        <!--
                <tr>
                    <td><spring:message code='rwandaprimarycare.touchscreen.birthdate' /></td>
                    <Td>
        <c:set var="bDay"><spring:message code='rwandaprimarycare.touchscreen.birthdateDay' /></c:set>
        <c:set var="bMonth"><spring:message code='rwandaprimarycare.touchscreen.birthdateMonth' /></c:set>
        <c:set var="bYear"><spring:message code='rwandaprimarycare.touchscreen.birthdateYear' /></c:set>
        <c:set var="orLeaveBlank"><spring:message code='rwandaprimarycare.touchscreen.orLeaveBlank' /></c:set>
        <touchscreen:numberInput required="false" field_name="birthdateDayCreate" value="${param.birthdateDay}" label="${bDay} (${orLeaveBlank} ${param.age})" min="1" max="31"/>
        <touchscreen:numberInput required="false" field_name="birthdateMonthCreate" value="${param.birthdateMonth}" label="${bMonth} (${orLeaveBlank} ${param.age})" min="1" max="12"/>
        <touchscreen:numberInput required="false" field_name="birthdateYearCreate" value="${param.birthdateYear}" label="${bYear} (${orLeaveBlank} ${param.age})" min="1910" max="2020"/>
    </Td>
</tr>-->

        <c:if test="${param.fromnida != null}">
            <tr>

                <td>Age</td>
                <c:set var="age"><spring:message code='rwandaprimarycare.touchscreen.age' /></c:set>
                <td><touchscreen:numberInput required="true" label="${age}" field_name="age" value="" min="0" max="150"/></td>


            </tr>
        </c:if>

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
                <td><touchscreen:textInput required="false" label="${CountryStr}" field_name="COUNTRY" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=COUNTRY&search="  javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Current Province</td>
            <td><touchscreen:textInput required="false" label="${ProvinceStr}" field_name="PROVINCE" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=PROVINCE&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Current District</td>
            <td><touchscreen:textInput required="false" label="${DistrictStr}" field_name="DISTRICT" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=DISTRICT&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Current Sector</td>
            <td><touchscreen:textInput required="false" label="${SectorStr}" field_name="SECTOR" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=SECTOR&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Current Cell</td>
            <td><touchscreen:textInput required="false" label="${CellStr}" field_name="CELL" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=CELL&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Current Umudugudu</td>
            <td><touchscreen:textInput required="false" label="${UmuduguduStr}" field_name="UMUDUGUDU" value="${param.address1}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=UMUDUGUDU&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
        </tr>
        <tr>
            <td>Mothers Name</td>
            <td><touchscreen:textInput required="false" label="${MomStr}" field_name="mothersNameCreate" value="${param.mothersName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=MRWNAME&search="/></td>
        </tr>
        <tr>
            <td>Fathers Name</td>
            <td><touchscreen:textInput required="false" label="${DadStr}" field_name="fathersNameCreate" value="${param.fathersName}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FATHERSRWNAME&search="/></td>
        </tr>
        <tr>
             <td>Education Level</td>
                    <c:set var="educationLevel"><spring:message code='EducationLevel' /></c:set>
             <td><touchscreen:textInput required="true"  label="${educationLevel}" field_name="educationLevelCreate" value="${param.educationLevel}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=EDLEV&search=" fieldType="upper"/></td>
       </tr>
       <tr>
             <td>Profession</td>
                    <c:set var="profession"><spring:message code='Profession' /></c:set>
             <td><touchscreen:textInput required="true"  label="${profession}" field_name="professionCreate" value="${param.profession}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=PROF&search=" fieldType="upper"/></td>
       </tr>
       <tr>
             <td>Religion</td>
                     <c:set var="religion"><spring:message code='Religion' /></c:set>
             <td><touchscreen:textInput required="true"  label="${religion}" field_name="religionCreate" value="${param.religion}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=RELIG&search=" fieldType="upper"/></td>
       </tr>
       <tr>
             <td>Phone number</td>
                     <c:set var="phoneNumber"><spring:message code='Phone Number' /></c:set>
             <td><touchscreen:textInput required="true"  label="${phoneNumber}" field_name="phoneNumberCreate" value="${param.phoneNumber}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=PHNUM&search=" fieldType="upper"/></td>
       </tr>

    </table>
    <input type="hidden" name="idSourceIdCreate" value="${idSource}"/>
    <input type="submit" value="Submit"/>
</form>
<%@ include file="resources/touchscreenFooter.jsp"%>