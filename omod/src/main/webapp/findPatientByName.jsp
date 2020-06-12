<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/rwandaprimarycare/addresshierarchyrwanda.js" />

<c:if test="${results == null}">
    <form method="get">
        <input type="hidden" name="addIdentifier" value="${param.addIdentifier}"/>
        <table>
            <tr>
                <td>Rwandan Name</td>
                <c:set var="rwandanName"><spring:message code='rwandaprimarycare.touchscreen.rwandanName' /></c:set>
                <td><touchscreen:textInput required="false"  label="${rwandanName}" field_name="RWNAME" fieldType="upper" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=RWNAME&search="/></td>
            </tr>
            <tr>
                <td>French/Ango Name</td>
                <c:set var="christianName"><spring:message code='rwandaprimarycare.touchscreen.christianName' /></c:set>
                <td><touchscreen:textInput required="false" label="${christianName}" field_name="FANAME" value="" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FANAME&search="/></td>
            </tr>

            <tr>
                <td>Gender</td> 
                <td>
                    <c:set var="gender"><spring:message code='rwandaprimarycare.touchscreen.gender' /></c:set>
                    <c:set var="male"><spring:message code='rwandaprimarycare.touchscreen.male' /></c:set>
                    <c:set var="female"><spring:message code='rwandaprimarycare.touchscreen.female' /></c:set>
                    <select optional="true" name="GENDER" label="${gender}" helpText="${gender}">
                        <option value="M">${male}</option>
                        <option value="F">${female}</option>
                    </select>
                </td>
            </tr>	
            <tr>

                <td>Age</td>
                <c:set var="age"><spring:message code='rwandaprimarycare.touchscreen.age' /></c:set>
                <td><touchscreen:numberInput required="true" label="${age}" field_name="AGE" value="" min="0" max="150"/></td>


            </tr>	
            <!--<tr>
                    <td>Mother's Rwandan Name</td>
                    <td><touchscreen:textInput required="false" label="Mother's Rwandan Name" field_name="MRWNAME" value="${search}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=MRWNAME&search="/></td>
            </tr>	
            <tr>
                    <td>Father's Rwandan Name</td>
                    <td><touchscreen:textInput required="false" label="Father's Rwandan Name" field_name="FATHERSRWNAME" value="${search}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=FATHERSRWNAME&search="/></td>
            </tr>	-->	
            <!--  <tr>
                    <td>Current Umudugudu</td>
            <c:set var="umuduguduStr"><spring:message code='rwandaprimarycare.touchscreen.umudugudu' /></c:set>
            <td><touchscreen:textInput required="false" label="${umuduguduStr}" field_name="UMUDUGUDU" value="${search}" allowFreeText="true" ajaxURL="findPatientByNameAjax.form?searchType=UMUDUGUDU&search=" javascriptAction="updateAddressHierarchyCache()"/></td>
    </tr>	-->		
            <tr>
                <td>Submit</td>
                <c:set var="searchStr"><spring:message code='rwandaprimarycare.touchscreen.search' /></c:set>
                    <td><input type="submit" value="AMAMAMAMA MAAMAMAMAM"/></td>
                </tr>	
            </table>				
        </form>
</c:if>

<c:if test="${results != null}">
    <touchscreen:patientList patients="${results}" maxResults="5" separator="" href="findPatientByNameConfirm.list" showAllIds="true"/>
    <br/>
    <c:url var="createHref" value="createNewPatient.form">
        <c:param name="addIdentifier" value="${addIdentifier}"/>
        <c:param name="givenName" value="${param.FANAME}"/>
        <c:param name="familyName" value="${param.RWNAME}"/>
        <c:param name="gender" value="${param.GENDER}"/>
        <c:param name="birthdate_day" value="${param.BIRTHDATE_DAY}"/>
        <c:param name="birthdate_month" value="${param.BIRTHDATE_MONTH}"/>
        <c:param name="birthdate_year" value="${param.BIRTHDATE_YEAR}"/>
        <c:param name="age" value="${param.AGE}"/>
        <c:param name="mothersName" value="${param.MRWNAME}"/>
        <c:param name="fathersName" value="${param.FATHERSRWNAME}"/>
        <c:param name="country" value="${param.COUNTRY}"/>
        <c:param name="province" value="${param.PROVINCE}"/>
        <c:param name="district" value="${param.DISTRICT}"/>
        <c:param name="sector" value="${param.SECTOR}"/>
        <c:param name="cell" value="${param.CELL}"/>
        <c:param name="address1" value="${param.UMUDUGUDU}"/>
    </c:url>
    
    <c:url var="findInNIDAHref" value="findUserFromNIDAById.form">
        <c:param name="addIdentifier" value="${addIdentifier}"/>
    </c:url>
    
    <c:set var="nfcm"><spring:message code='rwandaprimarycare.touchscreen.notFoundNewPatientManually' /></c:set>
    <touchscreen:button label="'${fn:toUpperCase(param.RWNAME)} ${param.FANAME}' ${nfcm}" href="${createHref}"/>
    <span> &nbsp;&nbsp;&nbsp;&nbsp;or&nbsp;&nbsp;&nbsp;&nbsp; </span>
    <c:set var="ffnida"><spring:message code='rwandaprimarycare.touchscreen.findCreateUserFromNIDA' /></c:set>
    <touchscreen:button label="${ffnida}" href="${findInNIDAHref}"/>
</c:if>

<%@ include file="resources/touchscreenFooter.jsp"%>  
