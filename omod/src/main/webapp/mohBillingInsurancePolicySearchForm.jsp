<%@ taglib prefix="mohbilling_tag" tagdir="/WEB-INF/tags/module/rwandaprimarycare" %>

<h2><spring:message code="rwandaprimarycare.insurance.policy.searchcreateedit" /></h2>
<openmrs:require privilege="Search Insurance Policy" otherwise="/login.htm" redirect="/module/rwandaprimarycare/insurancePolicySearch.form" />

<mohbilling_tag:insurancePolicySearchByInsuranceCardNumber redirectUrl="insurancePolicy.form" />

<br/>
<spring:message code="rwandaprimarycare.general.or"/>
<br/><br/>

<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|postURL=insurancePolicy.form|showIncludeVoided=false|viewType=shortEdit|hideAddNewPatient=true" />