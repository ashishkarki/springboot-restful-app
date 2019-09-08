package com.karki.ashish.app.shared;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Utils {

	private final Random RANDOM = new SecureRandom();
	private final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWNYZabcdefghijklmnopqrstuvwnyz";
	private final int iterations = 10000;
	private final int KEY_LENGTH = 256;
}
