package org.openmrs.module.rwandaprimarycare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AllEncountersController {

    @RequestMapping("/module/rwandaprimarycare/allEncounters")
    public String listAllEncounters(@RequestParam("patientId") int patientId, ModelMap model) throws PrimaryCareException {
    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        SortedMap<Date, List<Encounter>> encounters = new TreeMap<Date, List<Encounter>>(Collections.reverseOrder());
	        for (Encounter e : Context.getEncounterService().getEncountersByPatient(patient)) {
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(e.getEncounterDatetime());
	            cal.set(Calendar.HOUR_OF_DAY, 0);
	            cal.set(Calendar.MINUTE, 0);
	            cal.set(Calendar.SECOND, 0);
	            cal.set(Calendar.MILLISECOND, 0);
	            Date day = cal.getTime();
	            List<Encounter> holder = encounters.get(day);
	            if (holder == null) {
	                holder = new ArrayList<Encounter>();
	                encounters.put(day, holder);
	            }
	            holder.add(e);
	        }
	        model.addAttribute("patient", patient);
	        model.addAttribute("encounters", encounters);
	        model.addAttribute("vitalsEncounterType", PrimaryCareBusinessLogic.getVitalsEncounterType());
	        model.addAttribute("registrationEncounterType", PrimaryCareBusinessLogic.getRegistrationEncounterType());
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}
        return "/module/rwandaprimarycare/allEncounters";
    }
    
}
