package org.openmrs.module.rwandaprimarycare;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
/*import java.time.LocalDate;
import java.time.Period;*/
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.module.rwandaprimarycare.PrimaryCareUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PrimaryCareServiceTest extends BaseModuleContextSensitiveTest {

    @Override
    public Boolean useInMemoryDatabase() {
        return true;
    }

//    public void runBeforeAllTests() throws Exception {
//        executeDataSet("org/openmrs/module/rwandaprimarycare/extraPatients.xml");
//    }
//    @Test
//    public void testServiceLookup() throws Exception {
//        Context.authenticate("admin", "test");
//        PrimaryCareService pcs = PrimaryCareBusinessLogic.getService();
//        List<String> ret = pcs.getPatientSearchList("Dave", PrimaryCareService.PatientSearchType.FANAME, 1);
//        System.out.println(ret);
//        Assert.assertTrue(ret.size() > 0);
//        ret = pcs.getPatientSearchList("Dave", PrimaryCareService.PatientSearchType.RWNAME, 1);
//        Assert.assertTrue(ret.size() > 0);
//        System.out.println(ret);
//    }
//      REPLACED BY SOUNDEX LOOKUP    
//    /**
//     * assume registration.restrict... global property is set to true
//     */
//    @Test
//    public void testServiceFind() throws Exception {
//        Context.authenticate("admin", "test");
//        PrimaryCareService pcs = PrimaryCareBusinessLogic.getService();
//        
//        //restrict by location gp
//        List<Patient> pList = pcs.getPatients("dave", null, "M", Integer.valueOf(33).floatValue(), null, null, null, null, null, null, null, null, PrimaryCareUtil.getHealthCenterAttributeType(), Context.getLocationService().getLocation(18));
//        System.out.println(pList.size());
//        Assert.assertTrue(pList.size() > 0);
//        
//        //restrict by location gp
//        pList = pcs.getPatients("dave", null, "M", Integer.valueOf(33).floatValue(), null, null, null, null, null, null, null, null, PrimaryCareUtil.getHealthCenterAttributeType(), Context.getLocationService().getLocation(17));
//        System.out.println(pList.size());
//        Assert.assertTrue(pList.size() == 0);
//        
//        //age
//        pList = pcs.getPatients("dave", null, "M", Integer.valueOf(3).floatValue(), null, null, null, null, null, null, null, null, PrimaryCareUtil.getHealthCenterAttributeType(), Context.getLocationService().getLocation(18));
//        System.out.println(pList.size());
//        Assert.assertTrue(pList.size() == 0);
//        
//        //name switch
//        pList = pcs.getPatients(null, "dave", "M", Integer.valueOf(33).floatValue(), null, null, null, null, null, null, null, null, PrimaryCareUtil.getHealthCenterAttributeType(), Context.getLocationService().getLocation(18));
//        System.out.println(pList.size());
//        Assert.assertTrue(pList.size() > 0);
//        pList = pcs.getPatients("dave", "dave", "M", Integer.valueOf(33).floatValue(), null, null, null, null, null, null, null, null, PrimaryCareUtil.getHealthCenterAttributeType(), Context.getLocationService().getLocation(18));
//        System.out.println(pList.size());
//        Assert.assertTrue(pList.size() > 0);
//        
//    }
    @Test
    public void testNationalIdentifierStuff() throws Exception {
//        String nationalIdLong = "1 1974 8 0006220 0 690108042008THOMAS                   David                    1624";
//        String nationalIdShort = "1 1974 8 0006220 0 69";
//        
//        Assert.assertTrue(nationalIdLong.length() == 85);
//        System.out.println(PrimaryCareUtil.getFamilyNameFromNationalId(nationalIdLong));
//        System.out.println(PrimaryCareUtil.getGivenNameFromNationalId(nationalIdLong));
//        System.out.println(PrimaryCareUtil.getDOBYearFromNationalId(nationalIdLong));
//        System.out.println(PrimaryCareUtil.getGenderFromNationalId(nationalIdLong));
//        
//        System.out.println(PrimaryCareUtil.getFamilyNameFromNationalId(nationalIdShort));
//        System.out.println(PrimaryCareUtil.getGivenNameFromNationalId(nationalIdShort));
//        System.out.println(PrimaryCareUtil.getDOBYearFromNationalId(nationalIdShort));
//        System.out.println(PrimaryCareUtil.getGenderFromNationalId(nationalIdShort));

//        final String uri = "http://130.104.12.92:5001/persons/?id=1195582042830097";
//        RestTemplate restTemplate = new RestTemplate();
//        
//        String plainCreds = "openmrs:saviors";
//        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//        String base64Creds = new String(base64CredsBytes);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Basic " + base64Creds);
//        HttpEntity<String> request = new HttpEntity<String>(headers);
//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
//        String result = response.getBody();
//
//        System.out.println(result);
//        
//        JsonParser parser = new JsonParser();
//        JsonObject jsonnida = (JsonObject) parser.parse(result);
//        
//        System.out.println(jsonnida);
//        JsonNull jnull = null;
//        if (jsonnida.get("documentNumber") == null || (jsonnida.get("documentNumber") != null && "null".equals(jsonnida.get("documentNumber").toString()))){
//            jsonnida.addProperty("documentNumber", "1195582042830097");
//        }
//        System.out.println("============");
//        System.out.println(jsonnida);
//        System.out.println("============");
//        System.out.println("============");
//        System.out.println("dateOfBirth = "+jsonnida.get("dateOfBirth").getAsString());
//        Date birthdate=new SimpleDateFormat("dd/MM/yyyy").parse(jsonnida.get("dateOfBirth").getAsString()); 
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(birthdate);
//        Integer birthdateDay = calendar.get(Calendar.DAY_OF_MONTH);
//        Integer birthdateMonth = calendar.get(Calendar.MONTH)+1;
//        Integer birthdateYear = calendar.get(Calendar.YEAR);
//        
//        System.out.println("birthdate = "+birthdate);
//        System.out.println("Day = "+birthdateDay);
//        System.out.println("Month = "+birthdateMonth);
//        System.out.println("Year = "+birthdateYear);
//        
//        Integer age = getAge(birthdate); //uses original search param
//        
//        System.out.println("Age = "+age);
        
    }

    public int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }

}
