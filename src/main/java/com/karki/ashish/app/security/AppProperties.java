package com.karki.ashish.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component 
public class AppProperties {

	@Autowired
	Environment environment;
	
	public String getTokenSecret() {
		return environment.getProperty("tokenSecret");
	}
}
