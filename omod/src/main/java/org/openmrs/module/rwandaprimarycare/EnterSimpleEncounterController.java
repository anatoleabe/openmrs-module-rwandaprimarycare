package org.openmrs.module.rwandaprimarycare;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/module/rwandaprimarycare/enterSimpleEncounter.form")
public class EnterSimpleEncounterController {

    protected final Log log = LogFactory.getLog(getClass());
    
    
    private Patient getPatient(Integer patientId) {
        MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
        Patient patient = Context.getPatientService().getPatient(patientId);
        if (patient == null)
            throw new IllegalArgumentException(msa.getMessage("rwandaprimarycare.noPatientWithId") + patientId);
        if (patient.isVoided())
            throw new IllegalArgumentException(msa.getMessage("rwandaprimarycare.patientVoided"));
        return patient;
    }
    
    @RequestMapping(method=RequestMethod.GET, params="form=vitals")
    public String setupVitalsForm(@RequestParam("patientId") Integer patientId, 
                                  @RequestParam(required=false, value="visitDate") Long visitDate,
                                  ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    	
	    	Patient patient = getPatient(patientId);
	        model.addAttribute(patient);
	        model.addAttribute("encounterType", PrimaryCareConstants.ENCOUNTER_TYPE_VITALS);
	        List<Question> questions = new ArrayList<Question>();
	        MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
	        questions.add(new Question(msa.getMessage("rwandaprimarycare.temperature"), PrimaryCareBusinessLogic.getTemperatureConcept(), false));
	        questions.add(new Question(msa.getMessage("rwandaprimarycare.weight"), PrimaryCareBusinessLogic.getWeightConcept(), true));
	        
	        StringBuilder heightMsg = new StringBuilder(msa.getMessage("rwandaprimarycare.height"));
	        //LK: if the BMI automatic calculation functionality is turned on we want to display
	        //the last entered height and approximate age so the clerk can determine if remeasuring the patient is required
	        Boolean calculateBMI = new Boolean(Context.getAdministrationService().getGlobalProperty("registration.calculateBMI"));
	        if(calculateBMI)
	        {
	        	Obs mostRecentHeightOb = PrimaryCareUtil.getMostRecentHeightObservation(patient);
	        	if(mostRecentHeightOb != null)
	        	{
	        		heightMsg = heightMsg.append(". ").append(msa.getMessage("rwandaprimarycare.lastRecordedHeight")).append(": ").append(mostRecentHeightOb.getValueAsString(Context.getLocale()));
	        		heightMsg = heightMsg.append(" ").append(msa.getMessage("rwandaprimarycare.lastRecordedHeightAge")).append(": ").append(PrimaryCareUtil.getAgeAtObservation(patient, mostRecentHeightOb));
	        	}
	        }
	        questions.add(new Question(heightMsg.toString(), PrimaryCareBusinessLogic.getHeightConcept(), false));
	        model.addAttribute("questions", questions);
	        
	        if(visitDate != null)
	        {
	        	model.addAttribute("visitDate", visitDate);
	        }
	        
	        
	        
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}  
        return "/module/rwandaprimarycare/enterSimpleEncounter";
    }

    
//    @RequestMapping(method=RequestMethod.GET, params="form=diagnosis")
//    public String setupDiagnosisForm(@RequestParam("patientId") Integer patientId, ModelMap model) throws PrimaryCareException {
//    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
//    	//So that errors will be directed to a touch screen error page
//    	try{
//    	
//	    	Patient patient = getPatient(patientId);
//	        model.addAttribute(patient);
//	        model.addAttribute("encounterType", PrimaryCareConstants.ENCOUNTER_TYPE_DIAGNOSIS);
//	        List<Question> questions = new ArrayList<Question>();
//	        MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
//	        questions.add(new Question(msa.getMessage("rwandaprimarycare.diagnosis1"), PrimaryCareBusinessLogic.getDiagnosisNonCodedConcept(), true));
//	        questions.add(new Question(msa.getMessage("rwandaprimarycare.diagnosis2"), PrimaryCareBusinessLogic.getDiagnosisNonCodedConcept(), false));
//	        questions.add(new Question(msa.getMessage("rwandaprimarycare.diagnosis3"), PrimaryCareB	usinessLogic.getDiagnosisNonCodedConcept(), false));
//	        model.addAttribute("questions", questions);
//    	} catch(Exception e)
//    	{
//    		throw new PrimaryCareException(e);
//    	}  
//        return "/module/rwandaprimarycare/enterSimpleEncounter";
//    }

    @RequestMapping(method=RequestMethod.POST)
    public String processSubmit(
            @RequestParam("patientId") Integer patientId,
            @RequestParam("encounterType") Integer encounterTypeId,
            @RequestParam("form") String form, 
            @RequestParam(required=false, value="visitDate") Long visitDate,
            WebRequest request,
            HttpSession session) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    	
	    	Patient patient = getPatient(patientId);
	        Location workstationLocation = PrimaryCareBusinessLogic.getLocationLoggedIn(session);
	        EncounterType encounterType = Context.getEncounterService().getEncounterType(encounterTypeId);
	        if (encounterType == null)
	            throw new RuntimeException("encounterType is required");
	        List<Obs> obsToCreate = new ArrayList<Obs>();
	        int i = 0;
	        while (true) {
	            String conceptId = request.getParameter("obs_concept_" + i);
	            String value = request.getParameter("obs_value_" + i);
	            ++i;
	            if (conceptId == null)
	                break;
	            if (!StringUtils.hasText(value))
	                continue;
	            Obs obs = new Obs();
	            obs.setPerson(patient);
	            obs.setConcept(Context.getConceptService().getConcept(Integer.valueOf(conceptId)));
	            obs.setLocation(workstationLocation);
	            try {
	                obs.setValueAsString(value);
	            } catch (ParseException ex) {
	                throw new IllegalArgumentException("Cannot set " + obs.getConcept().getName(Context.getLocale()).getName() + " to " + value);
	            }
	            obsToCreate.add(obs);
	        }
	        
	        //LK: if we are entering vitals we are going to check to see if we should 
	        //be automatically calculating the BMI, based on a global property  
	        Boolean calculateBMI = new Boolean(Context.getAdministrationService().getGlobalProperty("registration.calculateBMI"));
	        if(calculateBMI)
	        {
	        	//LK: we are only going to calcuate BMI for people over 20 as kiddies have different rules
	        	patient = getPatient(patient.getPatientId());
	        	if(patient.getAge() > 20)
	        	{
	        		String bmi = PrimaryCareUtil.calculateBMI(patient, obsToCreate);
	        		if(bmi != null)
	        		{
	        			Obs obs = new Obs();
	    	            obs.setPerson(patient);
	    	            obs.setConcept(Context.getConceptService().getConcept(PrimaryCareBusinessLogic.getBMIConcept().getConceptId()));
	    	            obs.setLocation(workstationLocation);
	    	            obs.setValueAsString(bmi);
	    	            obsToCreate.add(obs);
	        		}
	        	}
	        }
	        
	        if(visitDate != null)
	        {
	        	PrimaryCareBusinessLogic.createEncounter(patient, encounterType, workstationLocation, new Date(visitDate), Context.getAuthenticatedUser(), obsToCreate);
	        	 return "redirect:/module/rwandaprimarycare/patient.form?patientId=" + patientId + "&visitDate=" + visitDate;
	        }
	        else
	        {
	        	PrimaryCareBusinessLogic.createEncounter(patient, encounterType, workstationLocation, new Date(), Context.getAuthenticatedUser(), obsToCreate);
	        	 return "redirect:/module/rwandaprimarycare/patient.form?patientId=" + patientId;
	        }
	       
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}  
    }
}
