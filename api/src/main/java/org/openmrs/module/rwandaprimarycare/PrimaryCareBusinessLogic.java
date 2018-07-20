package org.openmrs.module.rwandaprimarycare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Provider;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitAttributeType;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.namephonetics.NamePhoneticsService;


public class PrimaryCareBusinessLogic {
    
    protected static final Log log = LogFactory.getLog(PrimaryCareBusinessLogic.class);
	
	/**
	 * Convenience method that returns the handle to the primary care service instance.
	 *   
	 * @return PrimaryCareService service instance
	 */
    public static PrimaryCareService getService() {
        return Context.getService(PrimaryCareService.class);
    }
	
    /**
     * Records that the given patient was seen at the given location on the given datetime.
     * First we look for an encounter of type 'Registration' for the given location and date (ignoring time).
     * If one exists, nothing happens, otherwise a new encounter is created with the date+time.
     * 
     * @param patient
     * @param location
     * @param datetime
     * @param provider
     * @return a registration encounter that day, either pre-existing or just created
     */
    public static Encounter patientSeen(Patient patient, Location location, Date datetime, User provider) {
        if (location == null) {
            //location = Context.getLocationService().getDefaultLocation();
            throw new IllegalArgumentException("location is required");
        }
        if (datetime == null) {
            datetime = new Date();
        }
        if (provider == null) {
            provider = Context.getAuthenticatedUser();
        }
        
        Date[] day = getStartAndEndOfDay(datetime);
        List<Encounter> any = Context.getEncounterService().getEncounters(patient, location, day[0], day[1], null, Collections.singleton(getRegistrationEncounterType()), null, false);
        if (any != null && any.size() > 0) {
            // found one
            return any.get(0);
        }
        
        // did not find an encounter that day, so we create a new one 
        Encounter enc = new Encounter();
        enc.setEncounterType(getRegistrationEncounterType());
        enc.setEncounterDatetime(datetime);
        enc.setLocation(location);
        
        {
	        // wow, this sucks to have to do....
	        Collection<Provider> provs = Context.getProviderService().getProvidersByPerson(Context.getPersonService().getPerson(provider.getPerson().getPersonId()));
	        Provider prov = null;
	        if (provs.size() > 0)
	        	prov = provs.iterator().next();
	        else {
	        	prov = new Provider();
	        	prov.setPerson(Context.getPersonService().getPerson(provider.getPerson().getPersonId()));
	        	prov.setRetired(false);
	        	prov.setIdentifier(provider.getPerson().getPersonId().toString());
	        	prov.setDescription("provider object created by ");
	        	prov = Context.getProviderService().saveProvider(prov);
	        }
	        enc.setProvider(PrimaryCareConstants.PRIMARY_CARE_ENCOUNTER_ROLE, prov);
        }   
        
//        enc.setProvider(Context.getPersonService().getPerson(provider.getPerson().getPersonId()));
        enc.setPatient(patient);
        PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(enc);
        
        
        return enc;
    }
    
    
    /**
     * Finds or creates a registration encounter on the given datetime, and adds the given observations to it.
     * 
     * @param patient
     * @param location
     * @param datetime
     * @param provider
     * @param observations
     * @return
     */
    public static Encounter addObsToRegistrationEncounter(Patient patient, Location location, Date datetime, User provider, Collection<Obs> observations) {
        Encounter enc = patientSeen(patient, location, datetime, provider);
        if (observations != null && observations.size() > 0) {
            for (Obs obs : observations) {
                enc.addObs(obs);
            }
            PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(enc);
        }
        return enc;
    }
    
    
    /**
     * Creates a new encounter, with observations
     * 
     * @param patient
     * @param encounterType
     * @param location
     * @param date
     * @param provider
     * @param observations
     * @return the encounter created
     */
    public static Encounter createEncounter(Patient patient, EncounterType encounterType, Location location, Date datetime, User provider, List<Obs> observations) {
        if (patient == null)
            throw new IllegalArgumentException("patient is required");
        if (encounterType == null)
            throw new IllegalArgumentException("encounterType is required");
        if (location == null)
            throw new IllegalArgumentException("location is required");
        if (datetime == null)
            datetime = new Date();
        if (provider == null)
            provider = Context.getAuthenticatedUser();
        
        Encounter enc = new Encounter();
        enc.setEncounterType(encounterType);
        enc.setEncounterDatetime(datetime);
        enc.setLocation(location);
        
        {
	        // wow, this sucks to have to do....
	        Collection<Provider> provs = Context.getProviderService().getProvidersByPerson(Context.getPersonService().getPerson(provider.getPerson().getPersonId()));
	        Provider prov = null;
	        if (provs.size() > 0)
	        	prov = provs.iterator().next();
	        else {
	        	prov = new Provider();
	        	prov.setPerson(Context.getPersonService().getPerson(provider.getPerson().getPersonId()));
	        	prov.setRetired(false);
	        	prov.setIdentifier(provider.getPerson().getPersonId().toString());
	        	prov.setDescription("provider object created by ");
	        	prov = Context.getProviderService().saveProvider(prov);
	        }
	        enc.setProvider(PrimaryCareConstants.PRIMARY_CARE_ENCOUNTER_ROLE, prov);
        }   
        
        
        enc.setPatient(patient);
        for (Obs obs : observations) {
            enc.addObs(obs);
        }
        return PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(enc);
    }
    
    /**
     * @param datetime
     * @return
     */
    public static Date[] getStartAndEndOfDay(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        Date endOfDay = cal.getTime();
        return new Date[] { startOfDay, endOfDay };
    }

    /**
     * 
     * Added by module activator  so should always be there. 
     * TODO: internationalize this?
     * 
     * @return
     */
    public static EncounterType getRegistrationEncounterType() {
    	if (PrimaryCareConstants.ENCOUNTER_TYPE_REGISTRATION == null)
    		throw new RuntimeException("Registration Encounter Type is null");
    	return PrimaryCareConstants.ENCOUNTER_TYPE_REGISTRATION;
        //return Context.getEncounterService().getEncounterType("Registration");
    }

    public static EncounterType getVitalsEncounterType() {
    	if (PrimaryCareConstants.ENCOUNTER_TYPE_REGISTRATION == null)
    		throw new RuntimeException("Vitals Encounter Type is null");
    	return PrimaryCareConstants.ENCOUNTER_TYPE_VITALS;
        //return Context.getEncounterService().getEncounterType("Registration");
    }
    
    public static VisitType getOutpatientVisitType() {
    	if (PrimaryCareConstants.ENCOUNTER_TYPE_REGISTRATION == null)
    		throw new RuntimeException("Primary Care Visit Type is null");
    	return PrimaryCareConstants.VISIT_TYPE_OUTPATIENT;
        //return Context.getEncounterService().getEncounterType("Registration");
    }


    public static Concept getWeightConcept() {
        Concept concept = null;
        String gp = Context.getAdministrationService().getGlobalProperty("concept.weight");
        if (gp == null)
        	throw new RuntimeException("You must set the global property concept.weight.");
        
        concept = Context.getConceptService().getConceptByUuid(gp);
        if (concept == null){
	        try {
	            concept = Context.getConceptService().getConcept(Integer.valueOf(gp));
	        } catch (Exception ex) { }
	        if (concept == null) {
	            throw new RuntimeException("Cannot find concept specified by global property concept.weight");
	        }
        }
        return concept;
    }
    
    public static Concept getHeightConcept() {
        Concept concept = null;
        String gp = Context.getAdministrationService().getGlobalProperty("concept.height");
        if (gp == null)
        	throw new RuntimeException("You must set the global property concept.height");
        concept = Context.getConceptService().getConceptByUuid(gp);
        if (concept == null){
	        try {
	            concept = Context.getConceptService().getConcept(Integer.valueOf(gp));
	        } catch (Exception ex) { }
	        if (concept == null) {
	            throw new RuntimeException("Cannot find concept specified by global property concept.height");
	        }
        }    
        return concept;
    }

    public static Concept getTemperatureConcept() {
        Concept concept = null;
        String gp = Context.getAdministrationService().getGlobalProperty("concept.temperature");
        if (gp == null)
        	throw new RuntimeException("You must set the global property concept.temperature.");
        concept = Context.getConceptService().getConceptByUuid(gp);
        if (concept == null){
	        try {
	            concept = Context.getConceptService().getConcept(Integer.valueOf(gp));
	        } catch (Exception ex) { }
	        if (concept == null) {
	            throw new RuntimeException("Cannot find concept specified by global property concept.temperature");
	        }
        }
        return concept;
    }
    
    public static Concept getDiagnosisNonCodedConcept() {
        Concept concept = null;
        String gp = Context.getAdministrationService().getGlobalProperty("concept.diagnosisNonCoded");
        if (gp == null)
        	throw new RuntimeException("You must set the global property concept.diagnosisNonCoded");
        concept = Context.getConceptService().getConceptByUuid(gp);
        if (concept == null){
	        try {
	            concept = Context.getConceptService().getConcept(Integer.valueOf(gp));
	        } catch (Exception ex) { }
	        if (concept == null) {
	            throw new RuntimeException("Cannot find concept specified by global property concept.diagnosisNonCoded");
	        }
        }
        return concept;
    }

    public static PatientIdentifierType getPrimaryPatientIdentiferType() {
    	String gp = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_PRIMARY_IDENTIFIER_TYPE);
    	if (gp == null)
    		throw new RuntimeException("You must set the value for the main identifier type for primary care.  This is set in GP:  registration.primaryIdentifierType");
        PatientIdentifierType pit = null;
        pit = Context.getPatientService().getPatientIdentifierTypeByUuid(gp);
        try {
            pit = Context.getPatientService().getPatientIdentifierType(Integer.valueOf(gp));
        } catch (Exception ex) { 
            pit = Context.getPatientService().getPatientIdentifierTypeByName(gp);   
        }
        if (pit == null) {
            throw new RuntimeException("Cannot find patient identifier type specified by global property " + PrimaryCareConstants.GLOBAL_PROPERTY_PRIMARY_IDENTIFIER_TYPE);
        }
        return pit;
    }
    
    public static Integer getNumberOfBarcodeCopiesToPrint(){
        try{
            return Integer.valueOf(Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_BAR_CODE_COUNT));
        } catch (Exception ex ){
        	log.error("couldn't parse or find global property registration.barCodeCount.  defaulting to 1.");
            return 1;
        }
    }
    
    /**
     * Gets all patient identifier types that should be used in this module.
     * This includes the primary type and the other types specified in the two global
     * properties.
     * 
     * The first element of the returned list is the primary type. This method ensures
     * that the returned list contains no duplicates.
     * 
     * @return
     */
    public static List<PatientIdentifierType> getPatientIdentifierTypesToUse() {
        List<PatientIdentifierType> ret = new ArrayList<PatientIdentifierType>();
        ret.add(getPrimaryPatientIdentiferType());
        
        String s = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_OTHER_IDENTIFIER_TYPES);
        if (s != null) {
            String[] ids = s.split(",");
            for (String idAsString : ids) {
                try {
                    idAsString = idAsString.trim();
                    if (idAsString.length() == 0)
                        continue;
                    PatientIdentifierType idType = null;
                    idType = Context.getPatientService().getPatientIdentifierTypeByUuid(idAsString);
                    if (idType == null){
	                    try {
	                        Integer id = Integer.valueOf(idAsString);
	                        idType = Context.getPatientService().getPatientIdentifierType(id);
	                    } catch (Exception ex){
	                        idType = Context.getPatientService().getPatientIdentifierTypeByName(idAsString);
	                    }
                    }
                    if (idType == null) {
                        throw new IllegalArgumentException("Cannot find patient identifier type " + idAsString + " specified in global property " + PrimaryCareConstants.GLOBAL_PROPERTY_OTHER_IDENTIFIER_TYPES);
                    }
                    if (!ret.contains(idType)) {
                        ret.add(idType);
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Error in global property " + PrimaryCareConstants.GLOBAL_PROPERTY_OTHER_IDENTIFIER_TYPES + " near '" + idAsString + "'");
                }
            }
        }       
        return ret;
    }
    
    /**
     * Searches for patients with the given identifier. Only looks at identifier types specified by the global
     * properties.
     * 
     * Sorts so that matches for the specified location come first.
     * Then sorts the results so that matches for primary identifier type come first.
     * 
     * @param identifier required
     * @param location optional
     * @return
     */
    public static List<Patient> findPatientsByIdentifier(String search, final Location location) {
        PatientIdentifierType preferredIdentifierType = getPrimaryPatientIdentiferType();
        List<PatientIdentifier> ids = Context.getPatientService().getPatientIdentifiers(search, getPatientIdentifierTypesToUse(), null, null, null);
        List<Patient> ret = new ArrayList<Patient>();

        // first identifiers of the preferred type, then others
        for (PatientIdentifier id : ids) {
            if (id.getIdentifierType().equals(preferredIdentifierType)) {
                if (!id.getPatient().isVoided()) {
                    ret.add(id.getPatient());
                }
            }
        }
        for (PatientIdentifier id : ids) {
            if (!id.getIdentifierType().equals(preferredIdentifierType)) {
                if (!id.getPatient().isVoided()) {
                    ret.add(id.getPatient());
                }
            }
        }
        sortResultsForUser(ret, location);
        return ret;
    }

    /**
     * 
     * Sorts patients by location
     * 
     * @param patientList
     * @param userLocation
     * @return
     */
    public static List<Patient> sortResultsForUser(List<Patient> patientList, final Location userLocation) {
        if (userLocation != null) {
            Collections.sort(patientList, new Comparator<Patient>() {
               public int compare(Patient left, Patient right) {
                   Integer leftWeight = getLocations(left).contains(userLocation) ? 0 : 1;
                   Integer rightWeight = getLocations(right).contains(userLocation) ? 0 : 1;
                   return leftWeight.compareTo(rightWeight);
                } 
            });
        }
        return patientList;
    }

    /**
     * Currently looks for a person attribute named "Health Center" but may eventually be changed to return
     * all encounter locations for the patient.
     * 
     * @param patient
     * @return
     */
    public static Collection<Location> getLocations(Patient patient) {
       Collection<Location> ret = new ArrayList<Location>();
       try {
           ret.add((Location) patient.getAttribute(getAssignedLocationPersonAttributeType()).getHydratedObject());
       } catch (Exception ex) {
           // presumably the patient has no assigned location. not really a problem
       }
       return ret;
    }

    public static Location getLocationLoggedIn(HttpSession session) {
        return (Location) session.getAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_WORKSTATION_LOCATION);
    }

    static PersonAttributeType assignedLocationPersonAttributeType = null;
    public static PersonAttributeType getAssignedLocationPersonAttributeType() {
        if (assignedLocationPersonAttributeType == null) {
            assignedLocationPersonAttributeType = PrimaryCareUtil.getHealthCenterAttributeType();
        }
        return assignedLocationPersonAttributeType;
    }

    public static List<Patient> sortResultsForUmudugudu(List<Patient> patientList, final String umudugudu) {
        if (umudugudu != null) {
            Collections.sort(patientList, new Comparator<Patient>() {
               public int compare(Patient left, Patient right) {
                   return umuduguduWeightHelper(left, umudugudu).compareTo(umuduguduWeightHelper(right, umudugudu));
                } 
            });
        }
        return patientList;

    }

    /**
     * How good a match is the given umudugudu for the given patient?
     * 
     * @param patient
     * @param umudugudu
     * @return 0 if the patient is currently in the umudugudu, 1 if they ever have been, 2 if they never have been
     */
    private static Integer umuduguduWeightHelper(Patient patient, String umudugudu) {
        boolean first = true;
        for (PersonAddress pa : patient.getAddresses()) {
            if (pa.getAddress1() != null && pa.getAddress1().equalsIgnoreCase(umudugudu)) {
                return first ? 0 : 1;
            }
            first = false;
        }
        return 2;
    }
    
    /**
     * 
     * Get the parents parents;
     * 
     * @param p
     * @return List<Person>
     */
    public static List<Person> getParents(Patient p){
        List<Person> ret = new ArrayList<Person>();
      
        RelationshipType rt = getParentRelationshipType();
        List<Relationship> rlist = Context.getPersonService().getRelationshipsByPerson(p);
        for (Relationship r: rlist){
            if (r.getRelationshipType().getRelationshipTypeId().equals(rt.getRelationshipTypeId())){
                if (r.getPersonB().getPersonId().equals(p.getPatientId())){
                    ret.add(r.getPersonA());
                } 
            }    
        }
        return ret;
    }
    
    
    public static RelationshipType getParentRelationshipType(){
    	String gp = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_PARENT_TO_CHILD_RELATIONSHIP_TYPE);
        if (gp == null)
        	throw new RuntimeException("you must set a value for global property registration.parentChildRelationshipTypeId.");
        RelationshipType rt = Context.getPersonService().getRelationshipTypeByUuid(gp);
        if (rt == null){
        	try {
        		rt = Context.getPersonService().getRelationshipType(Integer.valueOf(gp));
        	} catch (Exception ex){
        		rt = Context.getPersonService().getRelationshipTypeByName(gp);
        	}
        	if (rt == null)
        		throw new RuntimeException("can't parse registration.parentChildRelationshipTypeId.");
        }
        return rt;
    }
    /**
     * 
     * This will void a parent relationship from the patient;
     * 
     * @param patient
     * @param parent
     */
    public static void voidParent(Patient patient, Person parent){
        
        RelationshipType rt = getParentRelationshipType();
        List<Relationship> rlist = Context.getPersonService().getRelationshipsByPerson(patient);
        for (Relationship r: rlist){
            if (r.getRelationshipType().getRelationshipTypeId().equals(rt.getRelationshipTypeId())){
                if (r.getPersonA().equals(parent)){
                   r.setVoided(true);
                   r.setDateChanged(new Date());
                   r.setChangedBy(Context.getAuthenticatedUser());
                   Context.getPersonService().saveRelationship(r);
                } 
            }    
        }
        
    }
    
    
    /**
     * 
     * returns an unused primary identifier string from the identifier source
     * 
     * @return
     */
    public static String getNewPrimaryIdentifierString(){
        List<String> idList = getNewPrimaryIdentifiers(1);
        String addIdentifier = idList.get(0);
        if (addIdentifier == null || addIdentifier.length() == 0){
            IdentifierSource is = PrimaryCareUtil.getPrimaryIdentifierTypeSource();
            throw new RuntimeException("generateNewIdentifier failed for " + is.getName());
        }
        return addIdentifier;
    }
    
    
    public static List<String> getNewPrimaryIdentifiers(Integer quantity){
        IdentifierSourceService iss = Context.getService(IdentifierSourceService.class);
        IdentifierSource is = PrimaryCareUtil.getPrimaryIdentifierTypeSource();
        //TODO:   fix this -- this needs a 'while' loop.
        List<String> idList = iss.generateIdentifiers(is, quantity, "reg module");
        return idList;
    }
    
    
    

    public static List<Patient> getPatientWithSoundex(String givenName, String rwName, Location userLocation, String umudugudu){
        NamePhoneticsService nps = Context.getService(NamePhoneticsService.class);
        List<Patient> ret = nps.findPatient(givenName, null, rwName, null);
        String restrictByHealthCenter = Context.getAdministrationService().getGlobalProperty("registration.restrictSearchByHealthCenter");
        if (restrictByHealthCenter == "true"){
            List<Patient> newList = new ArrayList<Patient>();
            PersonAttributeType pat = PrimaryCareUtil.getHealthCenterAttributeType();
            for (Patient p: ret){
                PersonAttribute pa = p.getAttribute(pat);
                if (pa.getHydratedObject().equals(userLocation))
                    newList.add(p);
            }
            PrimaryCareBusinessLogic.sortResultsForUser(newList, userLocation);
            PrimaryCareBusinessLogic.sortResultsForUmudugudu(newList, umudugudu);
            return newList;
        }

        PrimaryCareBusinessLogic.sortResultsForUser(ret, userLocation);
        PrimaryCareBusinessLogic.sortResultsForUmudugudu(ret, umudugudu);
        return ret;
        
    }
    
    public static List<Concept> getInsuranceTypeAnswers(){
        List<Concept> ret = new ArrayList<Concept>();
        String sList = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_INSURANCE_TYPE_ANSWERS);
        for (StringTokenizer st = new StringTokenizer(sList, ","); st.hasMoreTokens(); ) {
            String s = st.nextToken().trim();
            Concept c = Context.getConceptService().getConceptByUuid(s);
            if (c == null){
            	try {
            		c = Context.getConceptService().getConcept(Integer.valueOf(s));
            	} catch (Exception ex){
            		throw new RuntimeException("Can't parse the global property registration.insuranceTypeConceptAnswers");
            	}
            }
            ret.add(c);
        }
        return ret;
    }
    
    public static Concept getLastInsuranceType(Patient patient){
        Obs o =PrimaryCareUtil.getMostRecentObs(patient, PrimaryCareUtil.getInsuranceTypeConcept());
        if (o != null)
            return o.getValueCoded();
        else 
            return null;
    }
    
    public static String getLastInsuranceNumber(Patient patient){
        Obs o =PrimaryCareUtil.getMostRecentObs(patient, PrimaryCareUtil.getInsuranceNumberConcept());
        if (o != null)
            return o.getValueText();
        else 
            return null;
    }
    
    public static PatientIdentifier getPrimaryPatientIdentifierForLocation(Patient patient, Location location){
        List<PatientIdentifier> piList = patient.getActiveIdentifiers();
        for (PatientIdentifier piTmp : piList){
            if (piTmp.getIdentifierType().getPatientIdentifierTypeId().equals(getPrimaryPatientIdentiferType().getPatientIdentifierTypeId())
                    && piTmp.getLocation().getLocationId().equals(location.getLocationId())){
                return piTmp;
            }
        }

       return null;
        
    }
    
    public static Patient setHealthCenter(Patient p, Location location){
        PersonAttributeType pat = PrimaryCareUtil.getHealthCenterAttributeType();
        PersonAttribute paTmp = p.getAttribute(pat);
        
        if (location != null && (paTmp == null || !paTmp.getValue().equals(String.valueOf(location.getLocationId())))){
            PersonAttribute pa = PrimaryCareUtil.newPersonAttribute(pat,location.getLocationId().toString(), p);
            p.addAttribute(pa);
            //argh.. check for preferred identifier for validator:

            return preferredIdentifierSafeSavePatient(p);
        }
        return p;
    }
    
    /**
     * Use this to save patients to ensure there's a preferred identifier
     * @param p
     * @return
     */
    public static Patient preferredIdentifierSafeSavePatient(Patient p){
    	//if there's a preferred patient, just save the patient and return
    	for (PatientIdentifier pi : p.getActiveIdentifiers()){
    		if (pi.getPreferred())
    			return Context.getPatientService().savePatient(p);
    	}
    	//else, find primary care identifier and set that as preferred
    	PatientIdentifier pi = p.getPatientIdentifier(getPrimaryPatientIdentiferType());
    	if (pi != null){
    		pi.setPreferred(true);
    	} else { //set the first identifier as preferred
    		try {
    			p.getActiveIdentifiers().get(0).setPreferred(true);
    		} catch (Exception ex){
    			//there are no identifiers on this patient!
    		}	
    	}
    	return Context.getPatientService().savePatient(p);
    }
    
    public static boolean doesPatientNeedAnIDForThisLocation(Patient patient, Location location){
        PatientIdentifierType pit = PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType();
        List<PatientIdentifier> piList = patient.getActiveIdentifiers();
        for (PatientIdentifier pi : piList){
            if (pi.getLocation().getLocationId().equals(location.getLocationId())
                    && pi.getIdentifierType().getPatientIdentifierTypeId().equals(pit.getPatientIdentifierTypeId())){
                        return  false;
            }        
        }
        return true;
    }
    
    public static boolean doesPatientAlreadyHaveThisID(Patient patient, String identifier){
        PatientIdentifierType pit = getPrimaryPatientIdentiferType();
        for (PatientIdentifier piTmp : patient.getActiveIdentifiers()){
            if (piTmp.getIdentifierType().getPatientIdentifierTypeId().equals(pit.getPatientIdentifierTypeId())
                    && piTmp.getIdentifier().equals(identifier)){
                    return true;
                
            }
        }
        return false;
    }

	public static Concept getBMIConcept() {
		Concept concept = null;
		String gp = Context.getAdministrationService().getGlobalProperty("concept.bmi");
		if (gp == null)
			throw new RuntimeException("you must set the global property concept.bmi");
		concept = Context.getConceptService().getConceptByUuid(gp);
		if (concept == null){
	        try {
	            concept = Context.getConceptService().getConcept(Integer.valueOf(gp));
	        } catch (Exception ex) { 
	        	//pass
	        }
		}    
        if (concept == null) {
            throw new RuntimeException("Cannot find concept specified by global property concept.height");
        }
        return concept;
    }
	
	/**
	 * wrapper for saveEncounter that ensures a Visit is created when registration encounter is created.
	 * Needs to handle finding the visit for creation of vitals encounter also...
	 * @param e
	 * @return
	 */
	public static Encounter saveEncounterAndVerifyVisit(Encounter e){
		
		if (e.getVisit() == null && e.getEncounterType().equals(PrimaryCareBusinessLogic.getRegistrationEncounterType())){
			Visit v = new Visit();
			v.setCreator(Context.getAuthenticatedUser());
			v.setDateCreated(new Date());
			v.setLocation(e.getLocation());
			v.setStartDatetime(e.getEncounterDatetime());
			
			//either the end of the day, or encounterDatetime + 8 hours, whichever is later.
			Calendar c = Calendar.getInstance();
			c.setTime(e.getEncounterDatetime());
			c.add(Calendar.HOUR, 8);
			//if visit is starting after 10 pm, set end time to start time plus 8 hours.  else 11:59:59 on that day is OK.
			v.setStopDatetime(getStartAndEndOfDay(e.getEncounterDatetime())[1].getTime() - e.getEncounterDatetime().getTime() < 1000*60*60*2 ? c.getTime() : getStartAndEndOfDay(e.getEncounterDatetime())[1]);
			
			v.setPatient(e.getPatient());
			v.setVisitType(getOutpatientVisitType());
			v.setVoided(false);
			//I don't know if encounter needs to be saved before visit is created, so...
			v.setEncounters(Collections.singleton(e));
			v = Context.getVisitService().saveVisit(v);
			e.setVisit(v);
		} else {
			//try to find existing visit
			List<Visit> vList = Context.getVisitService().getVisits(Collections.singletonList(getOutpatientVisitType()), Collections.singletonList(e.getPatient()),Collections.singleton(e.getLocation()), null, getStartAndEndOfDay(e.getEncounterDatetime())[0], getStartAndEndOfDay(e.getEncounterDatetime())[1],  null, null, null, true, false);
			if (vList != null && vList.size() > 0){
				Visit v = vList.get(0);
				e.setVisit(v);
			}
				
		}
		return Context.getEncounterService().saveEncounter(e);

	}
    

}
