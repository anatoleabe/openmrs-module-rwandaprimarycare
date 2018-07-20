package org.openmrs.module.rwandaprimarycare;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.module.rwandaprimarycare.PrimaryCareUtil;

public class PrimaryCareServiceTest extends BaseModuleContextSensitiveTest {
    
   
    @Override
    public Boolean useInMemoryDatabase(){
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
        String nationalIdLong = "1 1974 8 0006220 0 690108042008THOMAS                   David                    1624";
        String nationalIdShort = "1 1974 8 0006220 0 69";
        
        Assert.assertTrue(nationalIdLong.length() == 85);
        System.out.println(PrimaryCareUtil.getFamilyNameFromNationalId(nationalIdLong));
        System.out.println(PrimaryCareUtil.getGivenNameFromNationalId(nationalIdLong));
        System.out.println(PrimaryCareUtil.getDOBYearFromNationalId(nationalIdLong));
        System.out.println(PrimaryCareUtil.getGenderFromNationalId(nationalIdLong));
        
        System.out.println(PrimaryCareUtil.getFamilyNameFromNationalId(nationalIdShort));
        System.out.println(PrimaryCareUtil.getGivenNameFromNationalId(nationalIdShort));
        System.out.println(PrimaryCareUtil.getDOBYearFromNationalId(nationalIdShort));
        System.out.println(PrimaryCareUtil.getGenderFromNationalId(nationalIdShort));
        
    }    
    
    
   
}