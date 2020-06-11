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
    <c:param name="fromnida" value="yes"/>
</c:url>

<table><tr><Td>
            <c:if test="${nidaResult != null}">
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <c:if test="${nidaResult == 'NIDAVALIDATION'}">
                    <!--//Now create user manually-->
                    <c:url var="createNIDAHref" value="findUserFromNIDAById.form" >
                        <c:param name="nidaValidatedData" value="${nidaDataString}"/>
                    </c:url>
                    <br/>
                    <h1><spring:message code="rwandaprimarycare.touchscreen.nidaSearchResult"/></h1>
                    <%@ include file="nidaPatientPrevalidation.jspf" %>
                    <div style="float: left; background-color: #f0f0a0; border: 1px black solid; padding: 10px">
                        <h1><spring:message code="rwandaprimarycare.touchscreen.isThePatientCorrect"/></h1>
                        <br/>
                        <c:set var="yes"><spring:message code="rwandaprimarycare.touchscreen.yesCreatePatient"/></c:set>
                        <c:set var="no"><spring:message code="rwandaprimarycare.touchscreen.noCreateManually"/></c:set>

                        <touchscreen:button label="${yes}" href="${createNIDAHref}" cssClass="green"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <c:choose>
                            <c:when test="${allowBackEntry eq 'true' }">
                                <touchscreen:button label="${no}" href="${createHref}"  cssClass="green"/>
                            </c:when>
                            <c:otherwise>
                                <touchscreen:button label="${no}" href="${createHref}" cssClass="green"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
                <c:if test="${nidaResult == 'NOTFOUND'}">
                    <span style="font-size: 15pt;">No correspondence found from NIDA. Check the national id you entered.</span>
                </c:if>
                <c:if test="${nidaResult == 'NOAPI'}">
                    <span style="font-size: 15pt;">Openhim to NIDA API is not defined on administration settings. Please go to administration settings and set the OpenHIM API.</span>
                </c:if>
                <c:if test="${nidaResult == 'INUSE'}">
                    <span style="font-size: 15pt;">${errorMsg}</span>
                </c:if>
                <c:if test="${fn:length(results) == 0}">
                    <br/>
                    <br/>


                    <c:url var="findInNIDAHref" value="findUserFromNIDAById.form">
                        <c:param name="addIdentifier" value="${addIdentifier}"/>
                    </c:url>

                    <c:if test="${nidaResult != 'NIDAVALIDATION'}">
                        <c:set var="ffnidaa"><spring:message code='rwandaprimarycare.touchscreen.findCreateUserFromNIDAAgain' /></c:set>
                        <touchscreen:button label="${ffnidaa}" href="${findInNIDAHref}"/>
                        <span> &nbsp;&nbsp;&nbsp;&nbsp;or&nbsp;&nbsp;&nbsp;&nbsp; </span>
                        <c:set var="nfcm"><spring:message code='rwandaprimarycare.touchscreen.notFoundNewPatientManually' /></c:set>
                        <touchscreen:button label="${nfcm}" href="${createHref}"/>
                    </c:if>
                </c:if>
            </c:if>
        </Td></tr></table>

<%@ include file="resources/touchscreenFooter.jsp"%>