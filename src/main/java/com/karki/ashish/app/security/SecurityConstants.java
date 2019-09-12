package com.karki.ashish.app.security;

import com.karki.ashish.app.SpringApplicationContext;

/**
 * @author Ashish Karki
 * Also, note that we have not marked this class as a Compenent of any kind.
 * So this class cannot be autowired anywhere. Its properties and methods can be accessed
 * in the same way we do for any java object prop/methods
 *
 */
public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; // 1 hour
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String VERIFICATION_URL = "/users/email-verification";

	/**
	 * Since securityConstants class isn't a spring component, we cannot autowire appProperties as a Bean
	 * So, use the SpringApp..Context class to get AppProps.. as a bean and use its method here
	 * @return
	 */
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
