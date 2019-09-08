package com.karki.ashish.app.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWNYZabcdefghijklmnopqrstuvwnyz";
//	private final int iterations = 10000;
//	private final int KEY_LENGTH = 256;

	public String generateUserId(int length) {
		return generateRandomString(length);
	}

	private String generateRandomString(int length) {
		StringBuilder returnValue = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
		}

		return new String(returnValue);
	}
}
