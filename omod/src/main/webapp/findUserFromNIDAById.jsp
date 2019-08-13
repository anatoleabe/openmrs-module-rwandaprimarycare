<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="resources/touchscreenHeader.jsp"%>

<c:if test="${nidaResult == null}">
    <form method="get">
        <c:set var="enterId"><spring:message code="rwandaprimarycare.touchscreen.enterNIDANumber"/></c:set>
        <c:set var="searchStr"><spring:message code="rwandaprimarycare.touchscreen.search"/></c:set>
        <touchscreen:textInput label="${enterId}" field_name="search" required="true" value="" fieldType="id"/>
        <input type="submit" value="${searchStr} }"/>
    </form>
</c:if>


<c:if test="${nidaResult != null}">
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <span style="font-size: 15pt;">No correspondence found from NIDA. Check the national id you entered.</span>
    <c:if test="${fn:length(results) == 0}">
        <br/>
        <br/>
        <!--//Now create user manually-->
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

        <c:set var="ffnidaa"><spring:message code='rwandaprimarycare.touchscreen.findCreateUserFromNIDAAgain' /></c:set>
        <touchscreen:button label="${ffnidaa}" href="${findInNIDAHref}"/>
        <span> &nbsp;&nbsp;&nbsp;&nbsp;or&nbsp;&nbsp;&nbsp;&nbsp; </span>
        <c:set var="nfcm"><spring:message code='rwandaprimarycare.touchscreen.notFoundNewPatientManually' /></c:set>
        <touchscreen:button label="${nfcm}" href="${createHref}"/>
    </c:if>
</c:if>

<%@ include file="resources/touchscreenFooter.jsp"%>