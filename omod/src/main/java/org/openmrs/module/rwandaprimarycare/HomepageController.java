package org.openmrs.module.rwandaprimarycare;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    protected static final Log log = LogFactory.getLog(HomepageController.class);
    
    @RequestMapping("/module/rwandaprimarycare/homepage")
    public String showHomepage(ModelMap model, HttpSession session) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	if (!Context.isAuthenticated() || PrimaryCareBusinessLogic.getLocationLoggedIn(session) == null) {
	            return "redirect:/module/rwandaprimarycare/login/login.form";
	        }
	
	        model.addAttribute("user", Context.getAuthenticatedUser());
	        
	        RecentlyViewedPatients recent = null;
	        try {
	            recent = (RecentlyViewedPatients) session.getAttribute("RECENT_PATIENTS");
	        } catch (ClassCastException ex) {
	            // this means the module has been reloaded 
	        }
	        if (recent == null) {
	            try {
	                recent = new RecentlyViewedPatients(Integer.valueOf(Context.getAdministrationService().getGlobalProperty("registration.maxRecentlyViewed")));
	                session.setAttribute("RECENT_PATIENTS", recent);
	            } catch (Exception ex){
	                throw new RuntimeException("The global proerty registration.maxRecentlyViewed is not set correctly.  Please verify all the registration global properties.");
	            }
	        }
	        
	        model.addAttribute(recent);
	        
	        //NOTE:  this is for a lazy loading exception
	        PersonService ps = Context.getPersonService();
	        for (Patient p : recent.getList()){
	            for (PersonAttribute pa : p.getActiveAttributes()){
	                PersonAttributeType pat = ps.getPersonAttributeType(pa.getPersonAttributeId());
	                pa.setAttributeType(pat);
	            }
	        }
	        
	        if (Context.getAdministrationService().getGlobalProperty("registration.showDiagnosisLink").equals("true")) {
	        	model.addAttribute("showDiagnosisLink", Boolean.TRUE);
	        }
	        
	        PrimaryCareWebLogic.clearSessionSearchAttributes(session);
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/homepage";
    }
    
    
    @RequestMapping("/module/rwandaprimarycare/chooseLocation")
    public String showGetLocation(ModelMap model, HttpSession session, HttpServletRequest request) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	if (!Context.isAuthenticated()) {
	            return "redirect:/module/rwandaprimarycare/login/login.form";
	        }
	        String locationStr = request.getParameter("location");
	
	        if (Context.getAdministrationService().getGlobalProperty("registration.showDiagnosisLink").equals("true")) {
	        	model.addAttribute("showDiagnosisLink", Boolean.TRUE);
	        }
	        
            if (locationStr != null && !locationStr.equals("")){
                
                Location location = Context.getLocationService().getLocation(Integer.valueOf(locationStr));
                if (location == null)
                    throw new NullPointerException();
                session.setAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_WORKSTATION_LOCATION, location);
                session.setAttribute(PrimaryCareConstants.SESSION_ATTRIBUTE_DIAGNOSIS_LOCATION_CODE, location);
                //NOTE:   this is used by the identifier validator to determine identifier prefixes.  Default to default location global property if not found when requesting new IDs.
                Context.setVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION, location);
                User user = Context.getAuthenticatedUser();
                model.addAttribute("user", user);
                
                if (Context.getAuthenticatedUser().getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION) == null || !Context.getAuthenticatedUser().getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION).equals(locationStr)){
                    user.setUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION, location.getLocationId().toString());
                    Context.getUserService().saveUser(user, null);
                }
                return "/module/rwandaprimarycare/homepage";
                
            }  else {
                
                model.addAttribute("locations", Context.getLocationService().getAllLocations(false));
                                    
                String locStr = Context.getAuthenticatedUser().getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCATION);
                Location userLocation = null;
                try { 
                    userLocation = Context.getLocationService().getLocation(Integer.valueOf(locStr));
                } catch (Exception ex){
                    //pass
                }
                if (userLocation == null){
                	model.addAttribute("userLocation", null);
                } else {
                		model.addAttribute("userLocation", userLocation);
                }
                return "/module/rwandaprimarycare/chooseLocation";
            }
	            
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
      
    }
   
        
}
