package com.karki.ashish.app.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.dto.UserDto;
import com.karki.ashish.app.ui.model.request.UserDetailsRequestModel;
import com.karki.ashish.app.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping
	public String getUser() {
		return "getUser() was called";
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValueRest = new UserRest(); // to be returned to the user; doesn't contain sensitive info

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
