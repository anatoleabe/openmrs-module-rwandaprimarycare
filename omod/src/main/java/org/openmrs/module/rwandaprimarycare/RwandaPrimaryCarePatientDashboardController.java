package org.openmrs.module.rwandaprimarycare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RwandaPrimaryCarePatientDashboardController {

    
    protected final Log log = LogFactory.getLog(this.getClass());
    
    
    @ModelAttribute("identifierTypesToDisplay")
    public List<PatientIdentifierType> populateIdentifierTypesToDisplay() {
        return PrimaryCareBusinessLogic.getPatientIdentifierTypesToUse();
    }
    
    @RequestMapping("/module/rwandaprimarycare/patient")
    public String setupForm(
            @RequestParam("patientId") int patientId,
            @RequestParam(required=false, value="skipPresentQuestion") Boolean skipPresentQuestion,
            @RequestParam(required=false, value="printBarCode") Boolean printBarCode,
            @RequestParam(required=false, value="serviceRequested") Integer serviceRequested,
            @RequestParam(required=false, value="serviceRequestResponse") Integer serviceRequestResponse,
            @RequestParam(required=false, value="gatherInsurance") Integer gatherInsurance,
            @RequestParam(required=false, value="insuranceNumber") String insuranceNumber,
            @RequestParam(required=false, value="insuranceType") Integer insuranceType,
            @RequestParam(required=false, value="visitDate") Long visitDate,
            HttpSession session,
            ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	// default to skipping the present question and not printing the bar code
	        if (skipPresentQuestion == null)
	            skipPresentQuestion = true;
	        if (printBarCode == null)
	        	printBarCode = false;
	        
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        
	        if(Context.getAdministrationService().getGlobalProperty("registration.overrideHealthCenterAttribute").equals("true"))
	        {
	        	PrimaryCareBusinessLogic.setHealthCenter(patient, PrimaryCareWebLogic.getCurrentLocation(session));
	        }
	        // lazy loading
	            for (PersonAttribute pa : patient.getAttributes()){
	                pa.getAttributeType().getName();
	            }
	            for (PatientIdentifier pi : patient.getIdentifiers()){
	                pi.getLocation().getName();
	            }
	        model.addAttribute(patient);
	        
	        //if patient doensn't have a primary identifier, generate one behind the scenes and save.  
	        //No UI necessary.
	        //
	        boolean needIDForThisLocation = PrimaryCareBusinessLogic.doesPatientNeedAnIDForThisLocation(patient, PrimaryCareWebLogic.getCurrentLocation(session));
	        if (needIDForThisLocation){
	            try {
	                String addIdentifier = PrimaryCareBusinessLogic.getNewPrimaryIdentifierString();
	                PatientIdentifier pi = new PatientIdentifier(addIdentifier,
	                        PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType(), PrimaryCareWebLogic.getCurrentLocation(session));
	                pi.setPreferred(true);
	                patient.addIdentifier(pi);
	                patient = PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
	            } catch (Exception ex){
	                ex.printStackTrace(System.out);
	                log.error("Could not create a primary id for patient: " + ex.getMessage() );
	            }
	        }
	
	        try {
	            RecentlyViewedPatients recent = (RecentlyViewedPatients) session.getAttribute("RECENT_PATIENTS");
	            if (recent != null)
	                recent.nowViewingPatient(patient);
	        } catch (ClassCastException ex) {
	            // this means the module has been reloaded
	        }
	        Encounter registrationEncounterToday = null;
	        {
	            Date[] day = null;
	            if(visitDate != null)
	            {
	            	day = PrimaryCareBusinessLogic.getStartAndEndOfDay(new Date(visitDate));
	            	model.addAttribute("visitDate", visitDate);
	            	model.addAttribute("todaysDate",new Date(visitDate));
	            }
	            else
	            {
	            	day = PrimaryCareBusinessLogic.getStartAndEndOfDay(new Date());
	            }
	            List<Encounter> today = Context.getEncounterService().getEncounters(patient, null, day[0], day[1], null, null, false);
	            Collections.sort(today, new Comparator<Encounter>() {
	                public int compare(Encounter left, Encounter right) {
	                    return OpenmrsUtil.compareWithNullAsEarliest(left.getEncounterDatetime(), right.getEncounterDatetime());
	                }
	            });
	            model.addAttribute("encountersToday", today);
	            for (Encounter enc : today) {
	                if (PrimaryCareConstants.ENCOUNTER_TYPE_REGISTRATION.equals(enc.getEncounterType())) {
	                    registrationEncounterToday = enc;
	                    break;
	                }
	            }
	            Date endOfYesterday;
	            {
	                Calendar cal = Calendar.getInstance();
	                cal.setTime(day[0]);
	                cal.add(Calendar.MILLISECOND, -1);
	                endOfYesterday = cal.getTime();
	            }
	            List<Encounter> beforeToday = Context.getEncounterService().getEncounters(patient, null, null, endOfYesterday, null, null, false);
	            model.addAttribute("beforeToday", beforeToday);
	        }
	        List<Person> parents = PrimaryCareBusinessLogic.getParents(Context.getPatientService().getPatient(patientId));
			if (parents != null) {
				model.addAttribute("parents", parents);
			}
     
	        // if the patient has no registration encounter today, we need to ask "is the patient present?"
	        if (!skipPresentQuestion && registrationEncounterToday == null)
	            return "/module/rwandaprimarycare/patientIsPresent";
	        
	        // show the print bar code dialogue if needed
	        if (printBarCode == true)
	        	return "/module/rwandaprimarycare/printBarCode";
	        
	        if (serviceRequested != null && serviceRequested.equals(0)){
	            
	            Concept c = PrimaryCareUtil.getServiceRequestedConcept();
	            //lazy loading:
	                for (ConceptAnswer ca : c.getAnswers()){
	                    ca.getAnswerConcept().getNames();
	                }
	            model.addAttribute("servicesRequested", c);
	            return "/module/rwandaprimarycare/serviceRequested";            
	        }   
	        
	        if (serviceRequestResponse != null && registrationEncounterToday != null){
	        	Concept c = PrimaryCareUtil.getServiceRequestedConcept();
	        	//create new obs if necessary
	            if (!PrimaryCareUtil.doesEncounterContainObsWithConcept(registrationEncounterToday, PrimaryCareUtil.getServiceRequestedConcept())){
	                Obs o = PrimaryCareUtil.newObs(patient, c, registrationEncounterToday.getEncounterDatetime(), PrimaryCareBusinessLogic.getLocationLoggedIn(session));
	                o.setValueCoded(Context.getConceptService().getConcept(serviceRequestResponse));
	                registrationEncounterToday.addObs(o);
	                registrationEncounterToday = PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(registrationEncounterToday);
	            } else {
	            	//edit existing obs
	            	int count = 0;
	            	for (Obs o : registrationEncounterToday.getObs()){
	            		if (o.getConcept().equals(c)){
	            			if (count == 0){
	            				//update the service
		            			o.setValueCoded(Context.getConceptService().getConcept(serviceRequestResponse));
		            			registrationEncounterToday = PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(registrationEncounterToday);
	            			} else {
	            				//void the duplicate
	            				Context.getObsService().voidObs(o, "duplicate service requested in touchscreen app");
	            			}
	            			 count ++;
	            		}
	            	}
	            }
	        }
	        
	        if (gatherInsurance != null && gatherInsurance.equals(0)){
	            
	            model.addAttribute("mostRecentType", PrimaryCareBusinessLogic.getLastInsuranceType(patient));
	            model.addAttribute("mostRecentInsuranceNumber", PrimaryCareBusinessLogic.getLastInsuranceNumber(patient));
	            model.addAttribute("insuranceTypes", PrimaryCareBusinessLogic.getInsuranceTypeAnswers());
	            
	            return "/module/rwandaprimarycare/insuranceInformation";
	        }    
	
	        if (insuranceType != null && registrationEncounterToday != null){
	            	//for edit, just void
	        		//TODO:  don't update Obs if you don't need to...
	            	for (Obs o : registrationEncounterToday.getObs()){
	            		if (o.getConcept().equals(PrimaryCareUtil.getInsuranceTypeConcept())){
	            			Context.getObsService().voidObs(o, "edited through touchscreen UI");
	            		}
	            		if (o.getConcept().equals(PrimaryCareUtil.getInsuranceNumberConcept())){
	            			Context.getObsService().voidObs(o, "edited through touchscreen UI");
	            		}
	            	}
	            	//and then recreate.
	                Obs insType = PrimaryCareUtil.newObs(patient, PrimaryCareUtil.getInsuranceTypeConcept(), registrationEncounterToday.getEncounterDatetime(), PrimaryCareBusinessLogic.getLocationLoggedIn(session));
	                insType.setValueCoded(Context.getConceptService().getConcept(insuranceType));
	                
	                Obs insNum = null;
	                if (insuranceNumber != null && !insuranceNumber.trim().equals("")){
	                    insNum = PrimaryCareUtil.newObs(patient, PrimaryCareUtil.getInsuranceNumberConcept(), registrationEncounterToday.getEncounterDatetime(), PrimaryCareBusinessLogic.getLocationLoggedIn(session));
	                    insNum.setValueText(insuranceNumber);
	                }
	    
	                if (insNum != null)
	                    registrationEncounterToday.addObs(insNum);
	                registrationEncounterToday.addObs(insType);
	                
	                registrationEncounterToday = PrimaryCareBusinessLogic.saveEncounterAndVerifyVisit(registrationEncounterToday);

	        }
	        if (Context.getAdministrationService().getGlobalProperty("registration.showDiagnosisLink").equals("true")) {
	        	model.addAttribute("showDiagnosisLink", Boolean.TRUE);
	        }
	        if (registrationEncounterToday != null)
	        	model.addAttribute("registrationEncounterToday", registrationEncounterToday);
	        model.addAttribute("registrationEncounterType", PrimaryCareBusinessLogic.getRegistrationEncounterType());
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/patient";
    }
    
    @RequestMapping("/module/rwandaprimarycare/patientIsPresent")
    public String patientIsPresent(@RequestParam("patientId") int patientId, HttpSession session) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	//note:  this adds the registration encounter.  NOTE:  provider in the created encounter is the registration clerk.
	        PrimaryCareBusinessLogic.patientSeen(
	                        Context.getPatientService().getPatient(patientId),
	                        (Location) session.getAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_WORKSTATION_LOCATION),
	                        new Date(), Context.getAuthenticatedUser());
	        return "redirect:/module/rwandaprimarycare/patient.form?patientId=" + patientId + "&printBarCode=true&serviceRequested=0";
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping("/module/rwandaprimarycare/patientIsNotPresent")
    public String patientIsNotPresent(@RequestParam("patientId") int patientId, HttpSession session, ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    		Patient patient = Context.getPatientService().getPatient(patientId);
    		model.addAttribute("patient", patient);
    		return "/module/rwandaprimarycare/backenterPatientVisit";
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping(value="/module/rwandaprimarycare/visitDate", method=RequestMethod.GET)
    public String visitDate(@RequestParam("patientId") int patientId, HttpSession session, ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	        Calendar todaysDate = Calendar.getInstance();
	        Integer backEntry = Integer.valueOf(Context.getAdministrationService().getGlobalProperty("registration.backEntryLimit"));
	        
	        List<Date> dates = new ArrayList<Date>();
	        
	        for(int i = 0; i < backEntry; i++)
	        {
	        	todaysDate.add(Calendar.DAY_OF_YEAR, -1);
	        	Date date = todaysDate.getTime();
	        	dates.add(date);
	        }
	        
    		model.addAttribute("dates", dates);
    		
    		return "/module/rwandaprimarycare/visitDate";
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping(value="/module/rwandaprimarycare/visitDate", method=RequestMethod.POST)
    public String visitDateSumbit(@RequestParam("patientId") int patientId, 
                                  @RequestParam("visitDate") Long visitDate,
                                  HttpSession session, ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	Date date = new Date(visitDate);
    		//note:  this adds the registration encounter.  NOTE:  provider in the created encounter is the registration clerk.
	        PrimaryCareBusinessLogic.patientSeen(
	                        Context.getPatientService().getPatient(patientId),
	                        (Location) session.getAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_WORKSTATION_LOCATION),
	                        date, Context.getAuthenticatedUser());
	        return "redirect:/module/rwandaprimarycare/patient.form?patientId=" + patientId + "&printBarCode=false&serviceRequested=0&visitDate=" + visitDate;
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }

}
