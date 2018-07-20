package org.openmrs.module.rwandaprimarycare;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//LK: The edit patient form has been split into 2 separate parts
//in order to deal with the age. If the user changes the age
//then we want to pick up the change so the the remaining
//TODO: Check with Dave this approach as the extra submit
//may be creating unneccessary latency
@Controller
@RequestMapping("/module/rwandaprimarycare/editPatient.form")
public class EditPatientController {

    protected final Log log = LogFactory.getLog(getClass());
    
    @RequestMapping(method=RequestMethod.GET)
    public String editPatientFirstSteps(
            @RequestParam(required=true, value="patientId") Integer patientId,
            HttpSession session, ModelMap map) throws PrimaryCareException {
    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    		Patient patient = Context.getPatientService().getPatient(patientId);
		
    		map.addAttribute("patient", patient);   
    		
    		Calendar birthDate = Calendar.getInstance();
    		birthDate.setTime(patient.getBirthdate());
    		
    		map.addAttribute("birthdateDay", birthDate.get(Calendar.DAY_OF_MONTH));
    		map.addAttribute("birthdateMonth", birthDate.get(Calendar.MONTH)+1);
    		map.addAttribute("birthdateYear", birthDate.get(Calendar.YEAR));
    		
    		Set<PersonAddress> addresses = patient.getAddresses();
    		
    		Iterator<PersonAddress> iter = addresses.iterator();
    	    while (iter.hasNext()) {
    	    	
    	    	PersonAddress address = iter.next();
    	    	if(address.isPreferred())
    	    	{
    	    		map.addAttribute("address", address);
    	    	}
    	    }
    	    List<Person> parents = PrimaryCareBusinessLogic.getParents(Context.getPatientService().getPatient(patientId));
    	    map.addAttribute("parents", parents);
    	    //try to use attributes if we didn't find parents
    	    if(parents.size() == 0)
    	    PrimaryCareWebLogic.findParentsNamesAttributes(parents, patient, map);
    	} catch(Exception e)
        	{
        		throw new PrimaryCareException(e);
        	}  
    	return "/module/rwandaprimarycare/editPatient";
    }
    
    
    @RequestMapping(method=RequestMethod.POST)
    public String processSubmitPartOne(
            @RequestParam(required=true, value="patientId") Integer patientId,
            @RequestParam("givenName") String givenName,
            @RequestParam("familyName") String familyName,
            @RequestParam("gender") String gender,   
            @RequestParam("birthdateDay") Integer birthdateDay,
            @RequestParam("birthdateMonth") Integer birthdateMonth,
            @RequestParam("birthdateYear") Integer birthdateYear,
            @RequestParam("COUNTRY") String country,
            @RequestParam("PROVINCE") String province,
            @RequestParam("DISTRICT") String district,
            @RequestParam("SECTOR") String sector,
            @RequestParam("CELL") String cell,
            @RequestParam("UMUDUGUDU") String address1,
            @RequestParam("mothersName") String mothersName,
            @RequestParam("fathersName") String fathersName,
            HttpSession session) throws Exception  {
    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	boolean saveRequired = false;
	    	
	    	Patient patient = Context.getPatientService().getPatient(patientId);
	    	
	    	//deal with given and family name changes
	    	if(!patient.getGivenName().equalsIgnoreCase(givenName) || !patient.getFamilyName().equalsIgnoreCase(familyName))
	    	{
	    		Set<PersonName> names = patient.getNames();
	    		
	    		//first we are going to void the current name
	    		names = unMarkPreferredName(names);
	    		patient.setNames(names);
	    		
	    		PersonName newName = setupName(givenName, familyName);
	    	    patient.addName(newName);
	
	    		saveRequired = true;
	    	}
	    	
	    	//deal with gender change
	    	if(!patient.getGender().equals(gender))
	    	{
	    		patient.setGender(gender);
	    		patient.setChangedBy(Context.getAuthenticatedUser());
	    		patient.setDateChanged(new Date());
	    		
	    		saveRequired = true;
	    	}
	    	
	    	//Deal with the changing of the birthdate
	    	Calendar birthDate = Calendar.getInstance();
			birthDate.setTime(patient.getBirthdate());
			
	    	if((birthdateYear != null && birthDate.get(Calendar.YEAR)!= birthdateYear) || birthdateDay == null || birthdateMonth == null || birthDate.get(Calendar.MONTH)!= birthdateMonth - 1 || birthDate.get(Calendar.DAY_OF_MONTH)!= birthdateDay)
	    	{
	    		Calendar c = Calendar.getInstance();
	            c.add(Calendar.YEAR, -patient.getAge());
	            c.add(Calendar.DATE, -183);
	            if (birthdateDay != null)
	            {
	            	c.set(Calendar.DAY_OF_MONTH, birthdateDay);
	            }
	            if (birthdateMonth != null)
	            {
	                c.set(Calendar.MONTH, birthdateMonth - 1);
	            }
	            if (birthdateYear != null)
	            {
	                c.set(Calendar.YEAR, birthdateYear);
	            }
	            
	            patient.setBirthdate(c.getTime());
	            
	            //TODO: check how important the estimated flag is because the logic here isn't perfect
	            //and don't think it can be without having an explicit field for estimation
	            
	            //If the month or day has been left blank then we know that
	            //the date is an estimation
	            if (birthdateDay == null || birthdateMonth == null)
	            {
	                patient.setBirthdateEstimated(true);
	            }
	            //if either the day or the month has been explicitly changed then we are
	            //going to assume that the date isn't an estimation 
	            else if(birthDate.get(Calendar.MONTH)!= birthdateMonth - 1 || birthDate.get(Calendar.DAY_OF_MONTH)!= birthdateDay)
	            {
	            	patient.setBirthdateEstimated(false);
	            } 
	            
	            patient.setChangedBy(Context.getAuthenticatedUser());
	    		patient.setDateChanged(new Date());
	    		
	    		saveRequired = true;
	    	}
	    	
	    	//Deal with the change of address
	    	Set<PersonAddress> addresses = patient.getAddresses();
	    	PersonAddress address = null;
			
			Iterator<PersonAddress> iter = addresses.iterator();
		    while (iter.hasNext()) {
		    	
		    	PersonAddress addr = iter.next();
		    	if(addr.isPreferred())
		    	{
		    		address = addr;
		    	}
		    }
	    	
	    	if((address == null && !country.equals("")) || (address != null && ((address.getCountry() != null && !address.getCountry().equals(country)) || (address.getStateProvince() != null && !address.getStateProvince().equals(province)) || (address.getCountyDistrict() != null && !address.getCountyDistrict().equals(district)) || (address.getCityVillage() != null && !address.getCityVillage().equals(sector)) || (address.getNeighborhoodCell() != null && !address.getNeighborhoodCell().equals(cell)) || (address.getAddress1() != null && !address.getAddress1().equals(address1)))))
	    	{
	    		if(address != null)
	    		{
		    		address.setPreferred(false);
		    		address.setDateChanged(new Date());
		    		address.setChangedBy(Context.getAuthenticatedUser());
		    		patient.setAddresses(addresses);
	    		}
	    		
	    		PersonAddress newAddress = setupAddress(country, province, district, sector, cell, address1);
	    		
	    		patient.addAddress(newAddress);
	
	    		saveRequired = true;
	    	}
	    	
	    	//deal with the parents names
	    	Person mother = null;
	    	Person father = null;
	    	
	    	List<Person> parents = PrimaryCareBusinessLogic.getParents(Context.getPatientService().getPatient(patientId));
	    	//we only use this process if we have parents as persons in the database
	    	if(parents.size() > 0){
				for (Person parent : parents) {
					if (parent.getGender().equals("M")) {
						father = parent;
					} else {
						mother = parent;
					}
				}
				
				if ((mother == null && !mothersName.equals(""))) {
					PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
					saveRequired = false;
					
					mother = setupParent(mothersName, "F");
					Context.getPersonService().savePerson(mother);
					Relationship maternal = setupRelationship(patient, mother);
					Context.getPersonService().saveRelationship(maternal);
				} else if (mother != null && mothersName.equals("")) {
					PrimaryCareBusinessLogic.voidParent(patient, mother);
				} else if (!mothersName.equals("") && !mother.getFamilyName().equals(mothersName)) {
					Set<PersonName> mumNames = mother.getNames();
					mumNames = unMarkPreferredName(mumNames);
					mother.setNames(mumNames);
					
					PersonName motherNewName = setupName(null, mothersName);
					mother.addName(motherNewName);
					
					Context.getPersonService().savePerson(mother);
				}
				
				if ((father == null && !fathersName.equals(""))) {
					if (saveRequired) {
						PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
						saveRequired = false;
					}
					
					father = setupParent(fathersName, "M");
					Relationship paternal = setupRelationship(patient, father);
					Context.getPersonService().savePerson(father);
					Context.getPersonService().saveRelationship(paternal);
				} else if (father != null && fathersName.equals("")) {
					PrimaryCareBusinessLogic.voidParent(patient, father);
				} else if (!fathersName.equals("") && !father.getFamilyName().equals(fathersName)) {
					Set<PersonName> dadNames = father.getNames();
					dadNames = unMarkPreferredName(dadNames);
					father.setNames(dadNames);
					
					PersonName fatherNewName = setupName(null, fathersName);
					
					father.addName(fatherNewName);
					
					Context.getPersonService().savePerson(father);
				}
				
				if (saveRequired) {
					PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
				}
			} else {
    		//use attributes if we don't have parents as persons
    		 if(parents.size() == 0 && PrimaryCareUtil.hasParentsNamesAttributes(patient)){
    		 patient.getAttribute(Context.getPersonService().getPersonAttributeTypeByName(PrimaryCareConstants.MOTHER_NAME_ATTRIBUTE_TYPE)).setValue(mothersName);
    		 patient.getAttribute(Context.getPersonService().getPersonAttributeTypeByName(PrimaryCareConstants.FATHER_NAME_ATTRIBUTE_TYPE)).setValue(fathersName);
    		 }
    		 else{
    				PrimaryCareUtil.setupParentNames(patient,mothersName,fathersName);
    		 }
    		 PrimaryCareBusinessLogic.preferredIdentifierSafeSavePatient(patient);
    	}
	        return "redirect:/module/rwandaprimarycare/patient.form?skipPresentQuestion=true&patientId=" + patientId;
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	}  
    }
    
    private Person setupParent(String parentsName, String gender){
        Person parent = new Person();
        parent.setPersonDateCreated(new Date());
        parent.setPersonVoided(false);
        parent.setPersonCreator(Context.getAuthenticatedUser());
        parent.setGender(gender);
        
        PersonName pn = new PersonName();
        pn.setCreator(Context.getAuthenticatedUser());
        pn.setDateCreated(new Date());
        pn.setFamilyName(parentsName);
        pn.setVoided(false);
        parent.addName(pn);
        return parent;
    }
    
    private Set<PersonName> unMarkPreferredName(Set<PersonName> names)
    {
    	Iterator<PersonName> nameIter = names.iterator();
	    while (nameIter.hasNext()) {
	    	
	    	PersonName name = nameIter.next();
	    	
	    	if(name.isPreferred())
	    	{
	    		name.setPreferred(false);
	    		name.setDateChanged(new Date());
	    		name.setChangedBy(Context.getAuthenticatedUser());
	    	}
	    }
	    return names;
    }
    
    private PersonName setupName(String givenName, String familyName){
    	PersonName newName = new PersonName();
    	
    	if(givenName != null)
    	{
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
	            } catch (Exception ex){}
	        }
		    newName.setGivenName(givenName);
    	}
        
    	newName.setVoided(false);
    	newName.setCreator(Context.getAuthenticatedUser());
    	newName.setDateCreated(new Date());
	    newName.setFamilyName(familyName);
	    newName.setPreferred(true);
    	
	    return newName;
    }
    
    private PersonAddress setupAddress(String country, String province, String district, String sector, String cell, String umudugudu){
    	PersonAddress newAddress = new PersonAddress();
		newAddress.setPreferred(true);
		newAddress.setCountry(country);
		newAddress.setStateProvince(province);
		newAddress.setCountyDistrict(district);
		newAddress.setCityVillage(sector);
		newAddress.setNeighborhoodCell(cell);
		newAddress.setAddress1(umudugudu);
		
		//newAddress.setVoided(false);
		//newAddress.setCreator(Context.getAuthenticatedUser());
		//newAddress.setDateCreated(new Date());
    	
	    return newAddress;
    }
    
    private Relationship setupRelationship(Patient patient, Person parent){
        Relationship r = new Relationship();
        r.setCreator(Context.getAuthenticatedUser());
        r.setDateCreated(new Date());
        r.setVoided(false);
        r.setPersonA(parent);
        r.setPersonB(patient);
        RelationshipType rt = Context.getPersonService().getRelationshipType(Integer.valueOf(Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_PARENT_TO_CHILD_RELATIONSHIP_TYPE)));
        r.setRelationshipType(rt);
        return r;
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
