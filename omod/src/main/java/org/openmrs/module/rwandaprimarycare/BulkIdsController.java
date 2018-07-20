package org.openmrs.module.rwandaprimarycare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BulkIdsController {

		@RequestMapping("/module/rwandaprimarycare/bulkIds")
		public void renderBarCode(
	        @RequestParam(required=true, value="howManyIds") Integer numIds,
	        @RequestParam(required=true, value="location") Integer locationId,
	        HttpServletRequest request,
	        HttpServletResponse response) throws PrimaryCareException {
			
			try {
				//get request location
				Location loc = Context.getLocationService().getLocation(locationId);
				//get logged in location so that we can reset volatile location
				Location originalLoc = PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession());
				
				//set volatile location
				Context.setVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION, loc);
				
				//get ids
				List<String> idList = PrimaryCareBusinessLogic.getNewPrimaryIdentifiers(numIds);
				StringBuilder ret = new StringBuilder(loc.getName().toUpperCase() + "\nID,Kinyarwanda Name,Christian Name\n");
				for (String id: idList){
					ret.append(id + "\n");
				}
				
				//reset user location
				Context.setVolatileUserData(PrimaryCareConstants.VOLATILE_USER_DATA_LOGIN_LOCATION, originalLoc);
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			    response.setContentType("text/comma-separated-values");
			    response.setHeader("Content-Disposition","attachment; filename=\"idExport_"+ loc.getName() + "_" + sdf.format(new Date()) + ".csv\"");
				try {
			    	response.getOutputStream().write(ret.toString().getBytes());
			    } catch (Exception ex){
			    	//
			    }
			} catch (Exception ex) {
				throw new PrimaryCareException(ex);
			}
	}
			
			
			
}
