package org.openmrs.module.rwandaprimarycare;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManageIdentifiersController {
    
    protected final Log log = LogFactory.getLog(getClass());
    
    @RequestMapping("/module/rwandaprimarycare/manageIdentifiers")
    public void showAll(
            @RequestParam("patientId") int patientId,
            ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        model.addAttribute(patient);
	        model.addAttribute("activeIdentifiers", patient.getActiveIdentifiers());
	        model.addAttribute("allIdentifiers", patient.getIdentifiers());
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping(value="/module/rwandaprimarycare/addIdentifier", method=RequestMethod.GET)
    public void showAddForm(
            @RequestParam("patientId") int patientId,
            ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    
    		model.addAttribute("idTypes", Context.getPatientService().getAllPatientIdentifierTypes(false));
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping(value="/module/rwandaprimarycare/addIdentifier", method=RequestMethod.POST)
    public String handleAddForm(
            HttpSession session,
            @RequestParam("patientId") int patientId,
            @RequestParam("idType") int identifierTypeId,
            @RequestParam("identifier") String identifier) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    
	        Location loc = Context.getLocationService().getDefaultLocation();
	        PatientIdentifierType pit = Context.getPatientService().getPatientIdentifierType(identifierTypeId);
	        PatientIdentifier id = new PatientIdentifier(identifier, pit, loc);
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        patient.addIdentifier(id);
	        try {
	        	PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
	        } catch (Exception ex) {
	            log.warn("Error trying to add a patient identifier", ex);
	            session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, ex.getMessage());
	        }
	        return "redirect:manageIdentifiers.list?patientId=" + patientId;
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
    @RequestMapping("/module/rwandaprimarycare/deleteIdentifier")
    public String handleDelete(
            HttpSession session,
            @RequestParam("patientId") int patientId,
            @RequestParam("identifierTypeId") int identifierTypeId,
            @RequestParam("identifier") String identifier) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	Date now = new Date();
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        if (patient.getActiveIdentifiers().size() > 1) {
	            boolean didAnything = false;
	            for (PatientIdentifier id : patient.getActiveIdentifiers()) {
	                if (id.getIdentifier().equalsIgnoreCase(identifier) && id.getIdentifierType().getPatientIdentifierTypeId().equals(identifierTypeId)) {
	                    id.setVoided(true);
	                    id.setVoidedBy(Context.getAuthenticatedUser());
	                    id.setDateVoided(now);
	                    didAnything = true;
	                }
	            }
	            if (didAnything) {
	            	PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
	            }
	        } else {
	            MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
	            session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msa.getMessage("rwandaprimarycare.mustHaveAnId"));
	        }            
	        return "redirect:manageIdentifiers.list?patientId=" + patientId;
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }

}
