package org.openmrs.module.rwandaprimarycare;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
public class FindUserFromNIDAByIdController {

    protected final Log log = LogFactory.getLog(getClass());

    final static int MAX_RESULTS = 10;

    @RequestMapping("/module/rwandaprimarycare/findUserFromNIDAById")
    public String setupForm(@RequestParam(value = "search", required = false) String search, 
            @RequestParam(required = false, value = "nidaValidatedData") String nidaData, HttpSession session, ModelMap model) throws PrimaryCareException {
        //LK: Need to ensure that all primary care methods only throw a PrimaryCareException
        //So that errors will be directed to a touch screen error page

        try {
            log.info("[info]------ Getting properties.");
            final String openhimPatientUrl = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_OPENHIM_NIDA_API);
            if (openhimPatientUrl == null || openhimPatientUrl.isEmpty()) {
                log.error("[error]------ Openhim patient report URL is not defined on administration settings.");
                model.addAttribute("nidaResult", "NOAPI");
                return "/module/rwandaprimarycare/findUserFromNIDAById";
            }
            
            final String openhimClientID = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_OPENHIM_USER_NAME);
            if (openhimClientID == null || openhimClientID.isEmpty()) {
                    log.error("[error]------ Openhim client ID is not defined on administration settings.");
                    model.addAttribute("nidaResult", "NOAPI");
                    return "/module/rwandaprimarycare/findUserFromNIDAById";
            }
            final String openhimPwd = Context.getAdministrationService().getGlobalProperty(PrimaryCareConstants.GLOBAL_PROPERTY_OPENHIM_USER_PWD);
            if (openhimPwd == null || openhimPwd.isEmpty()) {
                    log.error("[error]------ Openhim client Basic Auth Password is not defined on administration settings.");
                    model.addAttribute("nidaResult", "NOAPI");
                    return "/module/rwandaprimarycare/findUserFromNIDAById";
            }
            
            if (nidaData != null){//After validation
                JsonParser parser = new JsonParser();
                JsonObject jsonnida = (JsonObject) parser.parse(nidaData);
                CreateNewPatientController createNewPatientController = new CreateNewPatientController();

                return createNewPatientController.createPatient(jsonnida, session, true);
            } else if (search != null && !search.equals("")) {

                model.addAttribute("search", search);
                //model.addAttribute("results", PrimaryCareBusinessLogic.findPatientsByIdentifier(search, PrimaryCareBusinessLogic.getLocationLoggedIn(session)));
                //model.addAttribute("identifierTypes", PrimaryCareBusinessLogic.getPatientIdentifierTypesToUse());

                search = search.trim().replace(" ", "");
                final String uri = openhimPatientUrl+"/?id=" + search;
                RestTemplate restTemplate = new RestTemplate();

                String plainCreds = openhimClientID+":"+openhimPwd;
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic " + base64Creds);
                HttpEntity<String> request = new HttpEntity<String>(headers);
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
                String result = response.getBody();

                JsonParser parser = new JsonParser();
                JsonObject jsonnida = (JsonObject) parser.parse(result);
                if (jsonnida.get("documentNumber") == null || (jsonnida.get("documentNumber") != null && "null".equals(jsonnida.get("documentNumber").toString()))) {
                    jsonnida.addProperty("documentNumber", search);
                }
                jsonnida.remove("photo");
                jsonnida.remove("signature");

                model.addAttribute("nidaResult", "NIDAVALIDATION");
                model.addAttribute("nidaDataString", jsonnida.toString());
                model.addAttribute("nidaData", jsonnida);
                return "/module/rwandaprimarycare/findUserFromNIDAById";
	        
            } else {
                return "/module/rwandaprimarycare/findUserFromNIDAById";
            }
        } catch (JsonSyntaxException e) {
            model.addAttribute("nidaResult", "NOTFOUND");
            return "/module/rwandaprimarycare/findUserFromNIDAById";
        } catch (ParseException e) {
            throw new PrimaryCareException(e);
        } catch (PrimaryCareException e) {
            boolean isInUse = e.getMessage().contains("already in use by another patient");
            if (isInUse) {
                JsonParser parser = new JsonParser();
                JsonObject jsonnida = (JsonObject) parser.parse(nidaData);
                model.addAttribute("nidaResult", "INUSE");
                model.addAttribute("errorMsg", "Oups, a patient with NID number '"+jsonnida.get("documentNumber")+"' already exist.");
                return "/module/rwandaprimarycare/findUserFromNIDAById";
            } else {
                throw new PrimaryCareException(e);
            }
        } catch (RestClientException e) {
            throw new PrimaryCareException(e);
        }
    }

    HttpHeaders createHeaders(String username, String password) {
        final String auth = username + ":" + password;
        return new HttpHeaders() {
            {
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

}
