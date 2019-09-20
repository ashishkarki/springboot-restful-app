package com.karki.ashish.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// WebMvcConfigurer.super.addCorsMappings(registry);
//		registry
//		.addMapping("/users/email-verification")
//		.allowedMethods("GET", "POST")
//		.allowedOrigins("http://localhost:8090");
		
		registry
		.addMapping("*")
		.allowedMethods("*") // optional, if we skip, all methods would be allowed automatically
		.allowedOrigins("*");
	}
}
