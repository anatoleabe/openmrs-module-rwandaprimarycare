package org.openmrs.module.rwandaprimarycare;

import static org.springframework.util.StringUtils.hasText;

import java.util.Calendar;
import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.api.IdentifierNotUniqueException;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.web.WebConstants;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/module/rwandaprimarycare/createNewPatient")
public class CreateNewPatientController {

    protected final Log log = LogFactory.getLog(getClass());
    
    @RequestMapping(method=RequestMethod.GET)
    public String confirmIdNumber(
            @RequestParam(required=false, value="addNationalIdentifier") String addNationalIdentifier,
            @RequestParam("givenName") String givenName,
            @RequestParam("familyName") String familyName,
            @RequestParam("gender") String gender,
            @RequestParam("age") Integer age,
            @RequestParam(required=false,value="birthdateDay") Integer birthdateDay,
            @RequestParam(required=false,value="birthdateMonth") Integer birthdateMonth,
            @RequestParam(required=false,value="birthdateYear") Integer birthdateYear,
            @RequestParam("country") String country,
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("sector") String sector,
            @RequestParam("cell") String cell,
            @RequestParam("address1") String address1,
            @RequestParam("mothersName") String mothersName,
            @RequestParam("fathersName") String fathersName,
            HttpSession session, ModelMap map) throws PrimaryCareException {
    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
            IdentifierSource is = PrimaryCareUtil.getPrimaryIdentifierTypeSource();
            if (is == null){
                map.addAttribute("addIdentifier", "");
            } else {
                MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
                map.addAttribute("addIdentifier", msa.getMessage("rwandaprimarycare.automaticallyassigned"));
                map.addAttribute("idSource", is.getId());
            }
   
            map.addAttribute("nationalIdIdentifierType", PrimaryCareUtil.getNationalIdIdentifierType());
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}  
        return "/module/rwandaprimarycare/createNewPatient";
    }

    
    //TODO:  save all ID numbers -- need global properties for these...
    // save person address, using address hierarchy
    @RequestMapping(method=RequestMethod.POST)
    public String processSubmit(
            @RequestParam(required=false, value="addNationalIdentifier") String addNationalIdentifier,
            @RequestParam(required=false, value="addPCIdentifier") String addPCIdentifier,
            @RequestParam("givenNameCreate") String givenName,
            @RequestParam("familyNameCreate") String familyName,
            @RequestParam("gender") String gender,   //NOTE: uses original search param, not 'Create' param
            @RequestParam("age") Integer age, //uses original search param
            @RequestParam("birthdateDayCreate") Integer birthdateDay,
            @RequestParam("birthdateMonthCreate") Integer birthdateMonth,
            @RequestParam("birthdateYearCreate") Integer birthdateYear,
            @RequestParam("COUNTRY") String country,
            @RequestParam("PROVINCE") String province,
            @RequestParam("DISTRICT") String district,
            @RequestParam("SECTOR") String sector,
            @RequestParam("CELL") String cell,
            @RequestParam("UMUDUGUDU") String address1,
            @RequestParam("idSourceIdCreate") Integer sourceId,
            @RequestParam("mothersNameCreate") String mothersName,
            @RequestParam("fathersNameCreate") String fathersName,
            HttpSession session) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	        if (!hasText(givenName) || !hasText(familyName) || !hasText(gender) || age == null) {
	            throw new RuntimeException("Programming error: this shouldn't happen because params are required");
	        }
	        Patient newPatient = new Patient();
	        newPatient.setGender(gender);
	        { 
	            Calendar c = Calendar.getInstance();
	            c.add(Calendar.YEAR, -age);
	            c.add(Calendar.DATE, -183);
	            if (birthdateDay != null)
	                   c.set(Calendar.DAY_OF_MONTH, birthdateDay);
	            if (birthdateMonth != null)
	                   c.set(Calendar.MONTH, birthdateMonth - 1);
	            if (birthdateYear != null)
	                   c.set(Calendar.YEAR, birthdateYear);
	            newPatient.setBirthdate(c.getTime());
	            if (birthdateDay == null || birthdateMonth == null)
	                newPatient.setBirthdateEstimated(true);
	            //parse national ID for year:
	        }
	
	        //captialize first letter of christian name
	        givenName = capitalizeFirstLetterOfString(givenName);
	            
	        //capitalize after space character in name
	        int pos = givenName.trim().indexOf(" ");
	        if (pos > 0){
	            try {
	                if (givenName.charAt(pos+1) != ' '){
	                    String firstPart = givenName.substring(0,pos+1);
	                    String secondPart = givenName.substring(pos+1);
	                    secondPart = capitalizeFirstLetterOfString(secondPart);
	                    givenName = firstPart + secondPart;
	                }
	            } catch (Exception ex) {
		            log.error("Exception ocurred: ", ex);
	            }
	        }
	      
	        
	        PersonName pn = new PersonName(givenName, null, familyName);
	        pn.setPreferred(true);
	        newPatient.addName(pn);
	        {
	            PersonAddress pa = new PersonAddress();
	            pa.setCountry(country);
	            pa.setStateProvince(province);
	            pa.setCountyDistrict(district);
	            pa.setCityVillage(sector);
	            pa.setNeighborhoodCell(cell);
	            pa.setAddress1(address1);
	            pa.setPreferred(true);
	            newPatient.addAddress(pa);
	        }
	        
	        //new patients get a new ID:
	        try {
	            
	            boolean newIdNeeded = true;
	            //TODO:  i think this is wrong.  this could wreck sync?
	            if (addPCIdentifier != null && !addPCIdentifier.equals("") && addPCIdentifier.length() > 3){
	                //if the passed-in identifier looks like a valid id for the id type:
	                if (PrimaryCareUtil.isIdentifierStringAValidIdentifier(addPCIdentifier, PrimaryCareWebLogic.getCurrentLocation(session))){
	                    //if no patient already exists with this id.  TODO:  what if the patient already does exist??
	                    if (Context.getPatientService().getPatients(null, addPCIdentifier, Collections.singletonList(PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType()), true).size() == 0){
	                        //if the ID's 3 digits correspond to the current location:
	                        if (addPCIdentifier.substring(0, 3).equals(PrimaryCareUtil.getPrimaryCareLocationCode())){
	                        	PatientIdentifier pi = new PatientIdentifier(addPCIdentifier,PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType(), PrimaryCareWebLogic.getCurrentLocation(session));
	                        	pi.setPreferred(true);
	                            newPatient.addIdentifier(pi);
	                            newIdNeeded = false;
	                        } else {
	                            
	                            //the location code is different than the local location code, i.e. the id is from somwhere else and is not in the db
	                            //case:  we can find a location out of our location list for this id:
	                            Location thisIdsLocation = PrimaryCareUtil.getPrimaryCareLocationFromCodeList(addPCIdentifier.substring(0, 3));
	                            if (thisIdsLocation != null){
	                            	PatientIdentifier pi = new PatientIdentifier(addPCIdentifier,PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType(), thisIdsLocation);
	                            	pi.setPreferred(true);
	                                newPatient.addIdentifier(pi);
	                            } else {
	                                //case:  we can't find a location out of our location list for this id:
	                                //TODO:  when id.location stops being mandatory, we can create...
	                                // for now, pass
	                            }
	                            
	                        }
	                       
	                    }   
	                }
	            }            
	            if (newIdNeeded){
	                String addIdentifier = PrimaryCareBusinessLogic.getNewPrimaryIdentifierString();
	                PatientIdentifier pi = new PatientIdentifier(addIdentifier, PrimaryCareBusinessLogic.getPrimaryPatientIdentiferType(), PrimaryCareWebLogic.getCurrentLocation(session));
	                pi.setPreferred(true);
	                newPatient.addIdentifier(pi);
	            }
	        
	        
	        } catch (Exception ex){
	            log.error("Exception ocurred: ", ex);
	            throw new RuntimeException("Couldn't generate new ID.  Check idgen settings.");
	        }
	        
	        PersonAttributeType pat = PrimaryCareUtil.getHealthCenterAttributeType();
	        //TODO:  is this right -- verify with Cheryl?
	        if (pat != null){
	              PersonAttribute pa = PrimaryCareUtil.newPersonAttribute(pat, PrimaryCareWebLogic.getCurrentLocation(session).getLocationId().toString(), newPatient);
	              newPatient.addAttribute(pa);
	        }
	
	        if (addNationalIdentifier != null && !addNationalIdentifier.trim().equals("")){
	            PatientIdentifierType natIdType = PrimaryCareUtil.getNationalIdIdentifierType();
	            if (natIdType != null){
	                newPatient.addIdentifier(new PatientIdentifier(PrimaryCareUtil.getIdNumFromNationalId(addNationalIdentifier),
	                        natIdType, PrimaryCareWebLogic.getCurrentLocation(session))); 
	                
	                String givenNameNI = PrimaryCareUtil.getGivenNameFromNationalId(addNationalIdentifier);
	                String familyNameNI = PrimaryCareUtil.getFamilyNameFromNationalId(addNationalIdentifier);
	                
	               //if the name the patient gave doens't match their ID card, create a secondary person name: 
	               if (!givenNameNI.equals(newPatient.getGivenName()) || familyNameNI.equals(newPatient.getFamilyName())){
	                   PersonName pnNI = new PersonName(givenNameNI, null, familyNameNI);
	                   pnNI.setPreferred(false);
	                   newPatient.addName(pnNI);
	               }
	            } 
	        }
	        try {         	
	        	PrimaryCareUtil.setupParentNames(newPatient,mothersName,fathersName);
	            PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(newPatient);
	        } catch (IdentifierNotUniqueException ex){
	            MessageSourceAccessor msa = new MessageSourceAccessor(Context.getMessageSourceService().getActiveMessageSource());
	            session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, msa.getMessage("rwandaprimarycare.idAlreadyUsed"));
	            return "/module/rwandaprimarycare/createNewPatient";
	        } catch (Exception ex){
	            log.error("Exception ocurred: ", ex);
	            throw new RuntimeException (ex.getMessage());
	        }
	    	
	        return "redirect:/module/rwandaprimarycare/patient.form?skipPresentQuestion=false&patientId=" + newPatient.getPatientId();
    	} catch (Exception e) {
    		throw new PrimaryCareException(e);
    	}
    }
        
    private String capitalizeFirstLetterOfString(String givenName){
        if (givenName != null && givenName.length() > 0){
            String firstLetter = givenName.substring(0, 1).toUpperCase();
            String rest = "";
            if (givenName.length() > 1)
                rest = givenName.substring(1);
            givenName =  firstLetter + rest;
        }
        return givenName;
    }
    
}
