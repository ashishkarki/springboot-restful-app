package com.karki.ashish.app.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.repository.UserRepository;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists!!");
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);
		userEntity.setEncryptedPassword("test");
		userEntity.setUserId("testUserId");

		UserEntity storedUserEntity = userRepository.save(userEntity);

		UserDto returnedUserDto = new UserDto();
		BeanUtils.copyProperties(storedUserEntity, returnedUserDto);

		return returnedUserDto;
	}

}
