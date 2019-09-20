package com.karki.ashish.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	 Contact contact = new Contact(
	            "Ashish Karki",
	            "https://www.linkedin.com/in/ashish-karki/", 
	            "a@b.com"
	    );
	    
	    @SuppressWarnings("rawtypes")
		List<VendorExtension> vendorExtensions = new ArrayList<>();
		
		ApiInfo apiInfo = new ApiInfo(
				"Spring boot app RESTful Web Service documentation",
				"This pages documents Spring boot based RESTful Web Service endpoints", 
				"1.0",
				"https://www.linkedin.com/in/ashish-karki/", 
				contact, 
				"Apache 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0", 
				vendorExtensions);
	
	@Bean
	public Docket apiDocket() {
		final Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<String>(Arrays.asList("HTTP", "HTTPS")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.karki.ashish.app"))
				.paths(PathSelectors.any())
				.build();

		return docket;
	}

}
