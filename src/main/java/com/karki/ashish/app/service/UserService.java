package com.karki.ashish.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karki.ashish.app.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
}
