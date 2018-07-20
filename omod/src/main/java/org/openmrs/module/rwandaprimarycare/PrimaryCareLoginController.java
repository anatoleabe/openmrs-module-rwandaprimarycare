package org.openmrs.module.rwandaprimarycare;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.OpenmrsCookieLocaleResolver;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/module/rwandaprimarycare/login/*")
public class PrimaryCareLoginController {

	protected static final Log log = LogFactory.getLog(PrimaryCareLoginController.class);

	
	@RequestMapping(method=RequestMethod.GET)
    public String buildForm(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return "/module/rwandaprimarycare/login";
	}
	
	@RequestMapping(method=RequestMethod.POST)
    public String loginUser(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {        
	
		String redirect = null;
		
		try {
			String username = request.getParameter("uname");
			String password = request.getParameter("pw");

			// get the place to redirect: for touch screen this is simple
			redirect = determineRedirect(request);
			
			// only try to authenticate if they actually typed in a username
			if (username != null && username.length() > 0) {
				
				Context.authenticate(username, password);
				
				if (Context.isAuthenticated()) {
					
					User user = Context.getAuthenticatedUser();
					
					if(user.getUserProperties() != null && !user.getUserProperties().containsKey("keyboardType")){
						user.getUserProperties().put("keyboardType", "QWERTY"); //ABC is the other option
						user = Context.getUserService().saveUser(user, null);
					}
					session.setAttribute("keyboardType", user.getUserProperty("keyboardType"));
					
					
					// load the user's default locale if possible
					if (user.getUserProperties() != null) {
						if (user.getUserProperties().containsKey(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE)) {
							String localeString = user.getUserProperty(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE);
							Locale locale = null;
							if (localeString.length() == 5) {
								//user's locale is language_COUNTRY (i.e. en_US)
								String lang = localeString.substring(0,2);
								String country = localeString.substring(3,5);
								locale = new Locale(lang, country);
							}
							else {
								// user's locale is only the language (language plus greater than 2 char country code
								locale = new Locale(localeString);
							}
							OpenmrsCookieLocaleResolver oclr = new OpenmrsCookieLocaleResolver();
							oclr.setLocale(request, response, locale);
						}
					}
									
					// In case the user has no preferences, make sure that the context has some locale set
					if (Context.getLocale() == null) {
						Context.setLocale(OpenmrsConstants.GLOBAL_DEFAULT_LOCALE);
					}
					
				}
				
				if (log.isDebugEnabled()) {
					log.debug("Redirecting after login to: " + redirect);
					log.debug("Locale address: " + request.getLocalAddr());
				}
				
				response.sendRedirect(redirect);
											
				//return redirect;
			}
		} catch (ContextAuthenticationException e) {
			// set the error message for the user telling them
			// to try again
			//session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "auth.password.invalid");
			log.error("failed to authenticate: ", e);
		} catch (Exception e) {
			log.error("Uexpected auth error", e);
		}
	
		// send the user back the login page because they either 
		// had a bad password or are locked out
		
		session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage("rwandaprimarycare.loginFailed"));
		session.setAttribute(WebConstants.OPENMRS_LOGIN_REDIRECT_HTTPSESSION_ATTR, redirect);
				
        return "/module/rwandaprimarycare/login";
    }

	
	@RequestMapping("logout.form")
    public String logoutUser(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {        
				
		try {

			Context.logout();
			
			session.removeAttribute(WebConstants.OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "auth.logged.out");
			session.setAttribute(WebConstants.OPENMRS_LOGIN_REDIRECT_HTTPSESSION_ATTR, request.getContextPath());			
			session.invalidate();
			
			return "redirect:login.form";
											
		} catch (Exception e) {
			//TODO
			log.error("Uexpected auth error", e);
		}
					
		return "redirect:login.form";
    }
	
	
    /**
	 * Convenience method for pulling the correct page to redirect
	 * to out of the request. IN case of touchscreeen module, we are not interested in
	 * anything else but home page. Leaving method in place just in case it will change in 
	 * future.
	 * 
	 * @param request the current request
	 * @return the page to redirect to as determined by parameters in the request
	 */
	private String determineRedirect(HttpServletRequest request) {
		
		String redirect = request.getContextPath() + "/module/rwandaprimarycare/chooseLocation.form";
		
		log.debug("Going to use redirect: '" + redirect + "'");
		
		return redirect;
	}
        
}
