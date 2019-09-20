package com.karki.ashish.app.ui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.karki.ashish.app.ui.model.request.LoginRequestModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
public class AuthenticationController {

	@ApiOperation("User login")
    @ApiResponses(value = {
    @ApiResponse(code = 200, 
            message = "Response Headers", 
            responseHeaders = {
                @ResponseHeader(name = "authorization", 
                        description = "Bearer <JWT value here>"),
                @ResponseHeader(name = "userId", 
                        description = "<Public User Id value here>")
            })  
    })
	@PostMapping("/users/login")
	public void fakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException(
				"This method is implemented by Spring Security and should not be called here!!");
	}
}
