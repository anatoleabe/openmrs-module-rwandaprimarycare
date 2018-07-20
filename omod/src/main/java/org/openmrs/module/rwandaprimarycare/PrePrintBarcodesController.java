package org.openmrs.module.rwandaprimarycare;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/module/rwandaprimarycare/prePrintBarcodes")
public class PrePrintBarcodesController {

    protected static final Log log = LogFactory.getLog(HomepageController.class);
    
    @RequestMapping(method = RequestMethod.GET)
    public String showPrePrintBarcodes(ModelMap model, HttpSession session) throws PrimaryCareException {
    	Location loc = (Location) Context.getVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION);
    	model.addAttribute("userLocation", loc);
    	return "/module/rwandaprimarycare/prePrintBarcodes";
    }
}
