package com.karki.ashish.app.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karki.ashish.app.exceptions.UserServiceExceptions;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.dto.UserDto;
import com.karki.ashish.app.ui.model.request.UserDetailsRequestModel;
import com.karki.ashish.app.ui.model.response.ErrorMessages;
import com.karki.ashish.app.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValueRest = new UserRest();

		UserDto foundDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(foundDto, returnValueRest);

		return returnValueRest;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValueRest = new UserRest(); // to be returned to the user; doesn't contain sensitive info

		if (userDetails.getEmail().isEmpty()) {
			throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDto userDto = new UserDto(); // shared DTO to be sent over to the DB
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUserDto = userService.createUser(userDto); // resulting DTO from DB
		BeanUtils.copyProperties(createdUserDto, returnValueRest);

		return returnValueRest;
	}

	@PutMapping
	public String updateUser() {
		return "updateUser() was called";
	}

	@DeleteMapping
	public String deleteUser() {
		return "deleteUser() was called";
	}
}
