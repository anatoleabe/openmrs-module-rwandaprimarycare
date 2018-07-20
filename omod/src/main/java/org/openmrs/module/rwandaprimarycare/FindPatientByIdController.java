package org.openmrs.module.rwandaprimarycare;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FindPatientByIdController {

    protected final Log log = LogFactory.getLog(getClass());
    
    final static int MAX_RESULTS = 10;
    
    @RequestMapping("/module/rwandaprimarycare/findPatientById")
    public void /*String*/ setupForm(@RequestParam(value="search", required=false) String search, HttpSession session, ModelMap model) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	if (search != null) {
	            model.addAttribute("search", search);
	            model.addAttribute("results", PrimaryCareBusinessLogic.findPatientsByIdentifier(search, PrimaryCareBusinessLogic.getLocationLoggedIn(session)));
	            model.addAttribute("identifierTypes", PrimaryCareBusinessLogic.getPatientIdentifierTypesToUse());
	        }
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
    }
    
}
