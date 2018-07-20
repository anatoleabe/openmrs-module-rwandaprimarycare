package org.openmrs.module.rwandaprimarycare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.rwandaprimarycare.PrimaryCareService.PatientSearchType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value={"search", "addIdentifier", "FANAME", "RWNAME", "GENDER", "AGE", "BIRTHDATE_DAY", "BIRTHDATE_MONTH", "BIRTHDATE_YEAR","MRWNAME", "FATHERSRWNAME","COUNTRY","PROVINCE","DISTRICT", "SECTOR", "CELL", "UMUDUGUDU"},
        types={String.class, String.class, String.class, String.class, String.class, Float.class, Integer.class, Integer.class,Integer.class,String.class, String.class,String.class, String.class, String.class, String.class, String.class, String.class})
public class FindPatientByNameController {

    protected final Log log = LogFactory.getLog(getClass());    
    
    @RequestMapping("/module/rwandaprimarycare/findPatientByName")
    public String setupForm(
            @RequestParam(value="search", required=false) String search,
            @RequestParam(value="addIdentifier", required=false) String addIdentifier,
            ModelMap model,
            HttpServletRequest request,
            HttpServletResponse response) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
    	
	    	HttpSession session = request.getSession();
	        session.setAttribute("search", null);
	        String searchFANAME = ServletRequestUtils.getStringParameter(request, PatientSearchType.FANAME.toString(), null);
	        String searchRWNAME = ServletRequestUtils.getStringParameter(request, PatientSearchType.RWNAME.toString(), null);
	        String searchGENDER = ServletRequestUtils.getStringParameter(request, PatientSearchType.GENDER.toString(), null);
	        float searchAGE = ServletRequestUtils.getFloatParameter(request, PatientSearchType.AGE.toString(), 0);
	        Integer searchBIRTHDATE_DAY = ServletRequestUtils.getIntParameter(request, PatientSearchType.BIRTHDATE_DAY.toString(), 0);
	        Integer searchBIRTHDATE_MONTH   = ServletRequestUtils.getIntParameter(request, PatientSearchType.BIRTHDATE_MONTH.toString(), 0);
	        Integer searchBIRTHDATE_YEAR = ServletRequestUtils.getIntParameter(request, PatientSearchType.BIRTHDATE_YEAR.toString(), 0);
	        String searchMRWNAME = ServletRequestUtils.getStringParameter(request, PatientSearchType.MRWNAME.toString(), null);
	        String searchFATHERSRWNAME = ServletRequestUtils.getStringParameter(request, PatientSearchType.FATHERSRWNAME.toString(), null);
	        String searchCOUNTRY = ServletRequestUtils.getStringParameter(request, PatientSearchType.COUNTRY.toString(), null);;
	        String searchPROVINCE = ServletRequestUtils.getStringParameter(request, PatientSearchType.PROVINCE.toString(), null);;
	        String searchDISTRICT = ServletRequestUtils.getStringParameter(request, PatientSearchType.DISTRICT.toString(), null);
	        String searchSECTOR = ServletRequestUtils.getStringParameter(request, PatientSearchType.SECTOR.toString(), null);;
	        String searchCELL = ServletRequestUtils.getStringParameter(request, PatientSearchType.CELL.toString(), null);
	        String searchUMUDUGUDU = ServletRequestUtils.getStringParameter(request, PatientSearchType.UMUDUGUDU.toString(), null);
	        
	      
	    	if (searchFANAME != null) {
	            if (search == null)
	                search = "";
	            
	            model.addAttribute("search", search);
	            model.addAttribute("addIdentifier", addIdentifier);
	    	    model.addAttribute("searchFANAME", searchFANAME);
	    	    model.addAttribute("searchRWNAME", searchRWNAME);
	    	    model.addAttribute("searchGENDER", searchGENDER);
	    	    model.addAttribute("searchBIRTHDATE_DAY", searchBIRTHDATE_DAY);
	    	    model.addAttribute("searchBIRTHDATE_MONTH", searchBIRTHDATE_MONTH);
	    	    model.addAttribute("searchBIRTHDATE_YEAR", searchBIRTHDATE_YEAR);
	        	   //calculate age from birthdate if null
	    	        if (!searchBIRTHDATE_DAY.equals(0) && !searchBIRTHDATE_MONTH.equals(0) && !searchBIRTHDATE_YEAR.equals(0)){
	    	            Calendar cal = Calendar.getInstance();
	    	            cal.set(Calendar.DAY_OF_MONTH, searchBIRTHDATE_DAY);
	    	            cal.set(Calendar.MONTH, searchBIRTHDATE_MONTH - 1);
	    	            cal.set(Calendar.YEAR, searchBIRTHDATE_YEAR);
	    	            Patient pTmp = new Patient();
	    	            pTmp.setBirthdate(cal.getTime());
	    	            searchAGE = pTmp.getAge();
	    	            pTmp = null;
	    	        }
	    	    model.addAttribute("searchAGE", searchAGE);
	    	    model.addAttribute("searchMRWNAME", searchMRWNAME);
	    	    model.addAttribute("searchFATHERSRWNAME", searchFATHERSRWNAME);
	    	    model.addAttribute("searchUMUDUGUDU", searchUMUDUGUDU);
	    	    model.addAttribute("searchCELL", searchCELL);
	    	    model.addAttribute("searchDISTRICT", searchDISTRICT);
	    	    model.addAttribute("searchCOUNTRY", searchCOUNTRY);
	            model.addAttribute("searchPROVINCE", searchPROVINCE);
	            model.addAttribute("searchSECTOR", searchSECTOR);
	            
	//            List<Patient> patients = PrimaryCareBusinessLogic.getService().getPatients(
	//            		searchFANAME, 
	//            		searchRWNAME, 
	//            		searchGENDER, 
	//            		searchAGE,
	//            		searchMRWNAME, 
	//            		searchFATHERSRWNAME,
	//            		searchCOUNTRY,
	//            		searchPROVINCE,
	//            		searchDISTRICT,
	//            		searchSECTOR,
	//            		searchCELL,
	//            		searchUMUDUGUDU,
	//            		Context.getPersonService().getPersonAttributeTypeByName("Health Center"),
	//            		PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession()));
	            
	            
	            List<Patient> patients = PrimaryCareBusinessLogic.getPatientWithSoundex(searchFANAME, searchRWNAME, PrimaryCareBusinessLogic.getLocationLoggedIn(request.getSession()), searchUMUDUGUDU);
	            
	            model.addAttribute("results", patients);
	            model.addAttribute("identifierTypes", PrimaryCareBusinessLogic.getPatientIdentifierTypesToUse());
	        }
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/findPatientByName";
    }

    @RequestMapping("/module/rwandaprimarycare/findPatientByNameAjax")
    public String searchAjax(
    		@RequestParam(value="search", required=false) String search,
    		@RequestParam(value="searchType", required=false) String searchType, 
    		@RequestParam(value="previousValue", required=false) Integer previousValue,
    		ModelMap model,
    		HttpServletRequest request) throws PrimaryCareException {    	    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	List<String> list = new ArrayList<String>();
	
	        //try to covert search type to something we know
	        PatientSearchType patientSearchType = null;        
	        try { patientSearchType = PatientSearchType.valueOf(searchType);} catch (IllegalArgumentException ex) {}
	    	if (search != null && patientSearchType != null) {
	            list = PrimaryCareBusinessLogic.getService().getPatientSearchList(search, patientSearchType, previousValue);
	            //hack:parse out addresshierarchyids:
	            ArrayList<Map<String,String>> idMaps = new ArrayList<Map<String,String>>();
	            List<String> retList = new ArrayList<String>();
	            if (list != null){
		            for (String s:list){
		                if (s.contains("|")){
		                    Map<String,String> nameAndIdMap = new HashMap<String, String>();
		                    int pos = s.indexOf("|");
		                    nameAndIdMap.put("id", s.substring(0,pos));
		                    nameAndIdMap.put("name", s.substring(pos+1));
		                    idMaps.add(nameAndIdMap);
		                    s = s.substring(pos+1);
		                }
		                retList.add(s);
		            }
	            }
	            model.addAttribute("idmaps", idMaps);
	            model.addAttribute("search", search);
	            model.addAttribute("results", retList);
	        }
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/findPatientByNameAjax";
    }
    
    @RequestMapping("/module/rwandaprimarycare/editPatientAddressAjax")
    public String searchAddressAjax(
    		@RequestParam(value="search", required=false) String search,
    		@RequestParam(value="searchType", required=false) String searchType, 
    		@RequestParam(value="previousValue", required=false) Integer previousValue,
    		ModelMap model,
    		HttpServletRequest request) throws PrimaryCareException {    	    	
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	    	List<String> list = new ArrayList<String>();
	
	        //try to covert search type to something we know
	        PatientSearchType patientSearchType = null;        
	        try { patientSearchType = PatientSearchType.valueOf(searchType);} catch (IllegalArgumentException ex) {}
	    	if (search != null && patientSearchType != null) {
	    		
	    		
	            list = PrimaryCareBusinessLogic.getService().getPatientSearchList(search, patientSearchType, previousValue);
	            //hack:parse out addresshierarchyids:
	            ArrayList<Map<String,String>> idMaps = new ArrayList<Map<String,String>>();
	            List<String> retList = new ArrayList<String>();
	            for (String s:list){
	                if (s.contains("|")){
	                    Map<String,String> nameAndIdMap = new HashMap<String, String>();
	                    int pos = s.indexOf("|");
	                    nameAndIdMap.put("id", s.substring(0,pos));
	                    nameAndIdMap.put("name", s.substring(pos+1));
	                    idMaps.add(nameAndIdMap);
	                    s = s.substring(pos+1);
	                }
	                retList.add(s);
	            }
	            model.addAttribute("idmaps", idMaps);
	            model.addAttribute("search", search);
	            model.addAttribute("results", retList);
	        }
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/findPatientByNameAjax";
    }
    
    @RequestMapping("/module/rwandaprimarycare/findPatientByNameConfirm")
    public String showConfirmPage(
            @RequestParam(value="patientId") int patientId,
            ModelMap map) throws PrimaryCareException {
    	//LK: Need to ensure that all primary care methods only throw a PrimaryCareException
    	//So that errors will be directed to a touch screen error page
    	try{
	        Patient patient = Context.getPatientService().getPatient(patientId);
	        map.addAttribute(patient);
	        List<Person> parents = PrimaryCareBusinessLogic.getParents(patient);
	        map.addAttribute("parents", parents);
	        //use attributes if we don't have parents as persons in the database
	        if(parents.size() == 0)
	        PrimaryCareWebLogic.findParentsNamesAttributes(parents, patient, map);
    	} catch(Exception e)
    	{
    		throw new PrimaryCareException(e);
    	} 
        return "/module/rwandaprimarycare/findPatientByNameConfirm";
        
    }
    
}
