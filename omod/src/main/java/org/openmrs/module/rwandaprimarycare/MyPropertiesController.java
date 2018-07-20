package org.openmrs.module.rwandaprimarycare;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPropertiesController {

	protected static final Log log = LogFactory
			.getLog(MyPropertiesController.class);

	@RequestMapping("/module/rwandaprimarycare/myProperties")
	public String showMyProperties(ModelMap model, HttpSession session)
			throws PrimaryCareException {

		return "/module/rwandaprimarycare/myProperties";
	}

	@RequestMapping("/module/rwandaprimarycare/keyboardType")
	public String showSetKeyboardType(ModelMap model, HttpSession session,
			HttpServletRequest request) throws PrimaryCareException {
		return "/module/rwandaprimarycare/keyboardType";

	}

	@RequestMapping("/module/rwandaprimarycare/chooseLanguage")
	public String showChooseLanguage(ModelMap model, HttpSession session,
			HttpServletRequest request) throws PrimaryCareException {

		model.addAttribute("locales", Context.getAdministrationService()
				.getAllowedLocales());

		return "/module/rwandaprimarycare/chooseLanguage";

	}

	@RequestMapping("/module/rwandaprimarycare/keyboardSelected")
	public String changeUserKeyboard(ModelMap model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws PrimaryCareException {
		try {
			String keyboardType = request.getParameter("keyboardSelect");
			if (keyboardType != null) {
				User user = Context.getAuthenticatedUser();
				user.getUserProperties().put("keyboardType", keyboardType);
				session.setAttribute("keyboardType", keyboardType);
				Context.getUserService().saveUser(user, null);
			}
		} catch (Exception e) {
			throw new PrimaryCareException(e);
		}
		return "/module/rwandaprimarycare/homepage";
	}

	@RequestMapping("/module/rwandaprimarycare/languageChanged.form")
	public String changeUserLanguage(ModelMap model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws PrimaryCareException {
		try {
			String locale = request.getParameter("locales");
			User user = Context.getAuthenticatedUser();
			Map<String, String> properties = user.getUserProperties();
			properties.put(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE,
					locale);
			Context.getUserService().saveUser(user, null);
			Context.setLocale(new Locale(locale));

		} catch (Exception e) {
			throw new PrimaryCareException(e);
		}
		return "/module/rwandaprimarycare/homepage";
	}
}
