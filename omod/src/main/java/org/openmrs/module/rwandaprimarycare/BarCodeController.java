package org.openmrs.module.rwandaprimarycare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BarCodeController {
	
	@RequestMapping("/module/rwandaprimarycare/barCode")
	public ModelAndView renderBarCode(
	        @RequestParam(required=false,value="patientId") Integer patientId, 
	        @RequestParam(required=false, value="multiple") Boolean multiple, 
	        @RequestParam(required=false, value="howManyOfflineIds") Integer howManyOfflineIds,
	        HttpServletRequest request, 
	        HttpServletResponse response, 
	        ModelMap model) throws PrimaryCareException {
		
		//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
		    if (patientId != null){
	    		// get the patient whose we want to print a bar code for
	    		Patient patient = Context.getPatientService().getPatient(patientId);
	    		model.addAttribute(patient);
	    
	    		//ensure that we're grabbing the right one; there can be multiples of the same type, at different locations:
	    		PatientIdentifier pi = PrimaryCareBusinessLogic.getPrimaryPatientIdentifierForLocation(patient, PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession()));
	    		model.addAttribute("identifier", pi.getIdentifier());	
	    		model.addAttribute("locationName", pi.getLocation().getName().replace(" Health Center", ""));
	    		
		    } else if (howManyOfflineIds != null){
		            List<String> stList = PrimaryCareBusinessLogic.getNewPrimaryIdentifiers(howManyOfflineIds);
		            model.addAttribute("idList",stList);
		            model.addAttribute("locationName", PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession()).getName().replace(" Health Center", ""));
		            System.out.println(stList);
		    }
			// determine the number of bar codes to print
			if (multiple != null && multiple == true){
				model.addAttribute("count", PrimaryCareBusinessLogic.getNumberOfBarcodeCopiesToPrint());
			}
			else {
				model.addAttribute("count", "1");
			}
			
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}
    	
		return new ModelAndView("/module/rwandaprimarycare/barCode", model);
	}
	
	
	@RequestMapping("/module/rwandaprimarycare/barCodeOtherLocation")
		public ModelAndView renderBarCode(
	        @RequestParam(required=true, value="howManyIds") Integer numIds,
	        @RequestParam(required=true, value="location") Integer locationId,
	        HttpServletRequest request,
	        HttpServletResponse response,
	        ModelMap model) throws PrimaryCareException {
		
		//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    		//get request location
			Location loc = Context.getLocationService().getLocation(locationId);
			//get logged in location so that we can reset volatile location
			Location originalLoc = PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession());
			
			//set volatile location
			Context.setVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION, loc);
			
			//get ids
			List<String> idList = PrimaryCareBusinessLogic.getNewPrimaryIdentifiers(numIds);
	        model.addAttribute("idList",idList);
	        model.addAttribute("locationName", loc.getName().replace(" Health Center", ""));
	        model.addAttribute("count", "1");
	        
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        model.addAttribute("introLabelEPL",  "A35,15,0,3,1,1,N,\"Printed on " + sdf.format(new Date()) + " for " + loc.getName().toUpperCase() + "\"");
	        
	        Context.setVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION, originalLoc);
			
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}
    	
		return new ModelAndView("/module/rwandaprimarycare/barCode", model);
	}
	
}