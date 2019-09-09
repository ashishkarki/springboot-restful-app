package com.karki.ashish.app.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.io.repository.UserRepository;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.Utils;
import com.karki.ashish.app.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists!!");
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);

		userEntity.setUserId(utils.generateUserId(30));
		userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

		UserEntity storedUserEntity = userRepository.save(userEntity);

		UserDto returnedUserDto = new UserDto();
		BeanUtils.copyProperties(storedUserEntity, returnedUserDto);

		return returnedUserDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity foundUserEntity = userRepository.findByEmail(email);

		if (foundUserEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(foundUserEntity.getEmail(), foundUserEntity.getEncryptedPassword(),
				new ArrayList<GrantedAuthority>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity foundUserEntity = userRepository.findByEmail(email);

		if (foundUserEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnedUserDto = new UserDto();
		BeanUtils.copyProperties(foundUserEntity, returnedUserDto);

		return returnedUserDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto foundUserDto = new UserDto();

		UserEntity foundUserEntity = userRepository.findByUserId(userId);
		if (null == foundUserEntity) {
			throw new UsernameNotFoundException(userId);
		}

		BeanUtils.copyProperties(foundUserEntity, foundUserDto);

		return foundUserDto;
	}

}
