package org.openmrs.module.rwandaprimarycare;

import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.Privilege;
import org.openmrs.VisitType;

public class PrimaryCareConstants {
    
    public static EncounterType ENCOUNTER_TYPE_REGISTRATION;
    public static EncounterType ENCOUNTER_TYPE_VITALS;
    public static EncounterType ENCOUNTER_TYPE_DIAGNOSIS;
    public static VisitType VISIT_TYPE_OUTPATIENT;
    public static Privilege PRINT_BARCODE_OFFLINE_PRIVILEGE;
    public static Privilege GENERATE_BULK_PRIMARY_CARE_IDS;
    public static EncounterRole PRIMARY_CARE_ENCOUNTER_ROLE;
    public static int ageRange = 10;
    public static final String GLOBAL_PROPERTY_PRIMARY_IDENTIFIER_TYPE = "registration.primaryIdentifierType";
    public static final String GLOBAL_PROPERTY_OTHER_IDENTIFIER_TYPES = "registration.otherIdentifierTypes";
    public static final String GLOBAL_PROPERTY_BAR_CODE_COUNT = "registration.barCodeCount";
    
    
    // NOTE ABOUT THE LOCATION GLOBAL PROPERTIES:  WHEN A CLERK LOGS IN AT A GIVEN LOCATION, THAT LOCATION WILL INFORM THE IDENTIFIER VALIDATOR
    // WHAT PREFIX TO APPEND.  THEREFORE, ACROSS SYNC NETWORKS, TO ENSURE THAT IDS ARE NEVER DUPLICATED, THE VALUES
    // THAT YOU ENTER FOR LOCATION CODES IN *BOTH* GLOBAL PROPERTIES MUST BE UNIQUE TO THAT SERVER.

    // FOR EXAMPLE, RWINK SHOULD HAVE THE DEFAULT SET TO 417 -- THE CODE FOR RWINK HEALTH CENTER,
    // AND THE LOCATION CODES SHOULD CONTAIN RWINK HEALTH CENTER *AND THE RWINK HOSPITAL FOSA CODES
    
    // KIREHE, FOR EXAMPLE, SHOULD ONLY HAVE KIREHE HEALTH CENTER, UNLESS THEY START DOING REGISTRATION AT THE HOSPITAL
    // IN WHICH CASE KIREHE WOULD HAVE KIREHE HEALTH CENTER AS DEFAULT, AND HOSPITAL AND HEALTH CENTER IN THE CODES
    
    // NYARABUYE, FOR EXAMPLE, SHOULD ONLY LIST NYARUBUYE FOR BOTH DEFAULT, AND CODE LIST.
    
    public static final String GLOBAL_PROPERTY_RWANDA_LOCATION_CODE = "registration.rwandaLocationCodes";
    public static final String GLOBAL_PROPERTY_DEFAULT_LOCATION_CODE = "registration.defaultLocationCode";
    public static final String VOLATILE_USER_DATA_LOGIN_LOCATION = "userLocation";
    
    public static final String GLOBAL_PROPERTY_INSURANCE_TYPE = "registration.insuranceTypeConcept";
    public static final String GLOBAL_PROPERTY_INSURANCE_NUMBER = "registration.insuranceNumberConcept";


    public static final String GLOBAL_PROPERTY_NATIONAL_ID_TYPE = "registration.nationalIdType";
    public static final String SESSION_ATTRIBUTE_WORKSTATION_LOCATION = "primaryCareWorkstationLocation";
    public static final String SESSION_ATTRIBUTE_DIAGNOSIS_LOCATION_CODE = "diagnosisWorkstationLocation";
    public static final String GLOBAL_PROPERTY_HEALTH_CENTER_ATTRIBUTE_TYPE = "registration.healthCenterPersonAttribute";
    public static final String GLOBAL_PROPERTY_INSURANCE_TYPE_ANSWERS = "registration.insuranceTypeConceptAnswers";
    public static final String GLOBAL_PROPERTY_MOTHERS_NAME_CONCEPT = "registration.mothersNameConceptId";
    public static final String GLOBAL_PROPERTY_FATHERS_NAME_CONCEPT = "registration.fathersNameConceptId";
    public static final String GLOBAL_PROPERTY_SERVICE_REQUESTED_CONCEPT = "registration.serviceRequestedConcept";
    public final static String GLOBAL_PROPERTY_PARENT_TO_CHILD_RELATIONSHIP_TYPE = "registration.parentChildRelationshipTypeId";
    public final static String GLOBAL_PROPERTY_RESTRICT_BY_HEALTH_CENTER = "registration.restrictSearchByHealthCenter";
    public static final String MOTHER_NAME_ATTRIBUTE_TYPE = "Mother's name";
    public static final String FATHER_NAME_ATTRIBUTE_TYPE = "Father's name";

    public static final String GLOBAL_PROPERTY_EDUCATION_LEVEL_CONCEPT = "Education Level";
    public static final String GLOBAL_PROPERTY_PROFESSION_CONCEPT = "Profession";
    public static final String GLOBAL_PROPERTY_RELIGION_CONCEPT = "Religion";
    public static final String GLOBAL_PROPERTY_PHONE_NUMBER_CONCEPT = "Phone Number";

    public static final String GLOBAL_PROPERTY_SERVICE_REQUESTED_CONCEPT_ANSWERS = "registration.serviceRequestedConceptAnswers";
   
    
}
