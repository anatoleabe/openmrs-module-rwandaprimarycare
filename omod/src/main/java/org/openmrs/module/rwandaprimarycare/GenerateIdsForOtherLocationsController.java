package org.openmrs.module.rwandaprimarycare;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.rwandaprimarycare.PrimaryCareException;;



@Controller
@RequestMapping("/module/rwandaprimarycare/bulkGenerateIds")
public class GenerateIdsForOtherLocationsController {
	
	  protected static final Log log = LogFactory.getLog(HomepageController.class);
	    
	    @RequestMapping(method = RequestMethod.GET)
	    public String showGenerateIdsPage(ModelMap model, HttpSession session) throws PrimaryCareException {
	    	try {
	    	Location loc = (Location) Context.getVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION);
	    	model.addAttribute("userLocation", loc);
	    	//load the locations listed in the list of Location:FOSA global property
	    	List<Location> locs = PrimaryCareUtil.getLocationsInRwandaLocationCodesGP();
	    	model.addAttribute("validIdLocations", locs);
	    	} catch (Exception ex){
	    		throw new PrimaryCareException(ex);
	    	}
	    	return "/module/rwandaprimarycare/bulkGenerateIds";
	    }
	    
}
