package org.openmrs.module.rwandaprimarycare.db.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttributeType;
import org.openmrs.module.rwandaprimarycare.db.PrimaryCareDAO;

public class HibernatePrimaryCareDAO implements PrimaryCareDAO {

    protected final Log log = LogFactory.getLog(getClass());
	
    private SessionFactory sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<String> getPatientFamilyNamesList(String search) {
    	String hql = null;
    	if (search != null & search.length() > 0) {    		
	    	hql = "select distinct pn.familyName " +
			"from Patient p, PersonName pn where p = pn.person and pn.familyName like '%" + search + "%'";
    	}
    	return this.fetchList(hql);
    }

    public List<String> getParentsFamilyNamesList(String search,int personAttributeTypeId) {
    	String hql = null;
    	if (search != null & search.length() > 0) {    		
	    	hql = "select distinct pa.value " +
			"from PersonAttribute pa, PersonAttributeType pat where pat.personAttributeTypeId = "+personAttributeTypeId+" and pa.value like '%" + search + "%'";
    	}
    	return this.fetchList(hql);
    }

    public List<String> getPatientGivenNamesList(String search) {
    	String hql = null;
    	if (search != null & search.length() > 0) {    		
	    	hql = "select distinct pn.givenName " +
			"from Patient p, PersonName pn where p = pn.person and pn.givenName like '%" + search + "%'";
    	}
    	return this.fetchList(hql);
    }
 
    
    /**
     * Make this very specific for now, can generalize later if needed. Params are all self-explanatory search terms
     * 
     * @param givenName
     * @param familyName
     * @param age
     * @param gender
     * @param mothersName
     * @param address1
     * @return
     */
    public List<Patient> getPatients(
    		String givenName, 
    		String familyName,
    		String gender,
    		Float age,
    		int ageRange,
    		String address1,
    		PersonAttributeType healthCenterPat, 
    		Location userLocation,
    		boolean restrictByHealthCenter) {
    	
        //TODO: should this only find patients who have the context's health center?
        //relationships handled at impl level
        
    	List<Patient> patients = new ArrayList<Patient>();

    	Date minBirthdate = null;
    	Date maxBirthdate = null;
    	
    	Criteria crit = sessionFactory.getCurrentSession().createCriteria(Patient.class)
            .add(Restrictions.eq("voided", false));
    	//System.out.println("givenName " +  givenName + " familyName " + familyName + " gender " + gender
    	 //       + " age " + age + " address1 " + address1);
    	
    	if (age != null && !age.equals(Float.valueOf(0))) {
    	    Calendar cal = Calendar.getInstance();
    	    cal.add(Calendar.YEAR, -Math.round(age));
    	    cal.add(Calendar.YEAR, -ageRange);
    	    minBirthdate = cal.getTime();
    	    
    	    cal = Calendar.getInstance();
    	    cal.add(Calendar.YEAR, -Math.round(age));
    	    cal.add(Calendar.YEAR, ageRange);
    	    maxBirthdate = cal.getTime();
    	    
    	    crit.add(Restrictions.between("birthdate", minBirthdate, maxBirthdate));
    	}
    	
    	//this does strange things:
    	//crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	
    	if ((givenName != null && !givenName.equals("")) || (familyName != null && !familyName.equals(""))) {
        	Criteria namesSubquery = crit.createCriteria("names").add(Restrictions.eq("voided", false));
                if (givenName != null && !givenName.equals(""))
        	    namesSubquery.add(Restrictions.or(
                        Restrictions.like("givenName", givenName, MatchMode.ANYWHERE).ignoreCase(),
                        Restrictions.like("familyName", givenName, MatchMode.ANYWHERE).ignoreCase()));
                if (familyName != null && !familyName.equals("")) {
                    namesSubquery.add(Restrictions.or(
                            Restrictions.like("givenName", familyName, MatchMode.ANYWHERE).ignoreCase(),
                            Restrictions.like("familyName", familyName, MatchMode.ANYWHERE).ignoreCase()));
                }
    	}
    	
    	
    	if (gender != null && !gender.equals(""))
    	    crit.add(Restrictions.eq("gender", gender));

//    	
    	if (address1 != null && !address1.equals(""))
    	    crit.createCriteria("addresses")
    	    .add(Restrictions.eq("voided", false))
    	    .add(Restrictions.like("address1", address1, MatchMode.ANYWHERE).ignoreCase());
   
    	//relationships -- can we write a straight SQL subquery??
    	
    	//restrict by registered health center
    	if (restrictByHealthCenter){
        	if (userLocation != null && healthCenterPat != null)
        	    crit.createCriteria("attributes")
        	        .add(Restrictions.eq("voided", false))
        	        .add(Restrictions.eq("attributeType", healthCenterPat))
        	        .add(Restrictions.or(Restrictions.eq("value", userLocation.getLocationId().toString()),Restrictions.isNull("value")));
    	}
    	patients = crit.list();
		return patients;
    }
    
    private List<String> fetchList(String hql) {
    	List<String> rows = new ArrayList<String>();
    	if (hql != null && hql.length() > 0) {
			Query q = sessionFactory.getCurrentSession().createQuery(hql);
			rows = q.list();
    	}
		return rows;
    }
    
    
    
  public List<String> getPatientAddress1List(String search) {
    String hql = null;
    if (search != null & search.length() > 0) {         
        hql = "select distinct pa.address1 " +
        "from Person p, PersonAddress pa where p = pa.person and pa.address1 like '%" + search + "%'";
    }
    return this.fetchList(hql);     
  }
//  
//  //TODO: fix this when you get the mappings from address hierarchy && lookup options for umudugudu
//  public List<String> getPatientNeighborhoodCellList(String search) {
//      String hql = null;
//      if (search != null & search.length() > 0) {         
//          hql = "select distinct pa.address1 " +
//          "from Person p, PersonAddress pa where p = pa.person and pa.address1 like '%" + search + "%'";
//      }
//      return this.fetchList(hql);     
//  }
//  
//  //TODO: fix this when you get the mappings from address hierarchy && lookup options for cell
//  public List<String> getPatientCountyDistrictList(String search) {
//      String hql = null;
//      if (search != null & search.length() > 0) {         
//          hql = "select distinct pa.address1 " +
//          "from Person p, PersonAddress pa where p = pa.person and pa.address1 like '%" + search + "%'";
//      }
//      return this.fetchList(hql);     
//  }
//  
//  public List<String> getPatientCountryList(String search) {
//      String hql = null;
//      if (search != null & search.length() > 0) {         
//          hql = "select distinct pa.country " +
//          "from Person p, PersonAddress pa where p = pa.person and pa.country like '%" + search + "%'";
//      }
//      return this.fetchList(hql); 
//  }
//  public List<String> getPatientStateProvinceList(String search){
//      String hql = null;
//      if (search != null & search.length() > 0) {         
//          hql = "select distinct pa.stateProvince " +
//          "from Person p, PersonAddress pa where p = pa.person and pa.stateProvince like '%" + search + "%'";
//      }
//      return this.fetchList(hql); 
//  }
//  public List<String> getPatientCityVillageList(String search){
//      String hql = null;
//      if (search != null & search.length() > 0) {         
//          hql = "select distinct pa.cityVillage " +
//          "from Person p, PersonAddress pa where p = pa.person and pa.cityVillage like '%" + search + "%'";
//      }
//      return this.fetchList(hql);
//  }
    
}
