package com.karki.ashish.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.karki.ashish.app.exceptions.UserServiceException;
import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.io.repository.UserRepository;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.AmazonSES;
import com.karki.ashish.app.shared.Utils;
import com.karki.ashish.app.shared.dto.AddressDTO;
import com.karki.ashish.app.shared.dto.UserDto;
import com.karki.ashish.app.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	AmazonSES amazonSES;

	@Override
	public UserDto createUser(UserDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()) != null) {
			throw new RuntimeException("Record already exists!!");
		}

		// first fill-up info that connect this userDto to its addresses
		for (int i = 0; i < userDto.getAddresses().size(); i++) {
			AddressDTO address = userDto.getAddresses().get(i);
			address.setUserDetails(userDto);
			address.setAddressId(utils.generateAddressId(30));

			userDto.getAddresses().set(i, address);
		}

		// UserEntity userEntity = new UserEntity();
		// BeanUtils.copyProperties(userDto, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

		final String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
		userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);

		UserEntity storedUserEntity = userRepository.save(userEntity);

		UserDto returnedUserDto = modelMapper.map(storedUserEntity, UserDto.class);

		// now that the user is trying to register, send him/her a verification email
		amazonSES.verifyEmail(returnedUserDto);

		return returnedUserDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity foundUserEntity = userRepository.findByEmail(email);

		if (foundUserEntity == null)
			throw new UsernameNotFoundException(email);

//		return new User(foundUserEntity.getEmail(), foundUserEntity.getEncryptedPassword(),
//				new ArrayList<GrantedAuthority>());

		return new User(foundUserEntity.getEmail(), foundUserEntity.getEncryptedPassword(),
				foundUserEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<GrantedAuthority>());
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
			throw new UsernameNotFoundException("User with ID: " + userId + " was not Found!!");
		}

		BeanUtils.copyProperties(foundUserEntity, foundUserDto);

		return foundUserDto;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserEntity foundUserEntity = userRepository.findByUserId(userId);
		if (null == foundUserEntity) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		// only update the fields specified by the request. might want to check userDto
		// values.
		foundUserEntity.setFirstName(userDto.getFirstName());
		foundUserEntity.setLastName(userDto.getLastName());

		// save changes to DB
		UserEntity updatedUserEntity = userRepository.save(foundUserEntity);

		UserDto updatedUserDto = new UserDto();
		BeanUtils.copyProperties(updatedUserEntity, updatedUserDto);

		return updatedUserDto;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity foundUserEntity = userRepository.findByUserId(userId);
		if (null == foundUserEntity) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		userRepository.delete(foundUserEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> userDtos = new ArrayList<UserDto>();

		Pageable pageable = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = userRepository.findAll(pageable);
		List<UserEntity> userEntities = usersPage.getContent();

		for (UserEntity user : userEntities) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			userDtos.add(userDto);
		}

		return userDtos;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;

		// Find user by token
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

		if (userEntity != null) {
			boolean hastokenExpired = Utils.hasTokenExpired(token);
			if (!hastokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue = true;
			}
		}

		return returnValue;
	}

}
