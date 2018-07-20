package org.openmrs.module.rwandaprimarycare.db;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttributeType;

public interface PrimaryCareDAO {
	public List<String> getPatientFamilyNamesList(String search);
	public List<String> getPatientGivenNamesList(String search);
	public List<String> getParentsFamilyNamesList(String search,int personAttributeTypeId);
/*	public List<String> getFathersFamilyNamesList(String search);*/
	public List<String> getPatientAddress1List(String search);
	
//	public List<String> getPatientNeighborhoodCellList(String search);
//	public List<String> getPatientCountyDistrictList(String search);
//	public List<String> getPatientCountryList(String search);
//	public List<String> getPatientStateProvinceList(String search);
//	public List<String> getPatientCityVillageList(String search);
	
	public List<Patient> getPatients(
    		String givenName, 
    		String familyName,
    		String gender,
    		Float age, 
    		int ageRange,
    		String Address1,
    		PersonAttributeType healthCenterPat, 
            Location userLocation,
            boolean restrictByHealthCenter);
	
}
