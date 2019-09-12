package com.karki.ashish.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karki.ashish.app.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto updateUser(String userId, UserDto userDto);
	void deleteUser(String userId);
	List<UserDto> getUsers(int page, int limit);
	boolean verifyEmailToken(String token);
}
