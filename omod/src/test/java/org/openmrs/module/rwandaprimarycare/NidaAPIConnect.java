/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.rwandaprimarycare;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Anatole
 */
public class NidaAPIConnect extends BaseModuleContextSensitiveTest{
    
    @Override
    public Boolean useInMemoryDatabase(){
        return true;
    }
    
    
    @Test
    public void testNationalIdentifierConnection() throws Exception {
        final String uri = "http://localhost:8080/springrestexample/employees.xml";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(result);
        
        
        System.out.println("type = "+json.get("type"));
        System.out.println("value = "+json.get("value"));
    }  
    
}
