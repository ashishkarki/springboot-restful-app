package com.karki.ashish.app.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.karki.ashish.app.TestingUtils;
import com.karki.ashish.app.exceptions.UserServiceException;
import com.karki.ashish.app.io.entity.AddressEntity;
import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.io.repository.UserRepository;
import com.karki.ashish.app.shared.AmazonSES;
import com.karki.ashish.app.shared.Utils;
import com.karki.ashish.app.shared.dto.AddressDTO;
import com.karki.ashish.app.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl testUserService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder passwordEncoder;

	@Mock
	AmazonSES amazonSES;

	UserEntity fakeUserEntity;
	UserDto fakeUserDto;
	List<AddressDTO> fakeAddressDtoList;
	List<AddressEntity> fakeAddressEntityList;

	@BeforeEach
	final void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		fakeAddressDtoList = TestingUtils.getFakeAddressDTOs();
		fakeAddressEntityList = TestingUtils.getFakeAddressEntities();
		fakeUserEntity = TestingUtils.getFakeUserEntity();
		fakeUserDto = TestingUtils.getFakeUserDto();
	}

	@Test
	final void testGetUser() {
		// setup first
		when(userRepository.findByEmail(anyString())).thenReturn(fakeUserEntity);

		// run the actual test
		UserDto testUserDto = testUserService.getUser(TestingUtils.fakeEmail);

		// assert
		assertNotNull(testUserDto);
		assertEquals("John", testUserDto.getFirstName());
	}

	@Test
	final void testGetUser_throws_UsernameNotFoundException() {
		// setup
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			testUserService.getUser(TestingUtils.fakeEmail);
		});
	}

	@Test
	final void testCreateUser_throws_UserServiceException() {
		when(userRepository.findByEmail(anyString())).thenReturn(fakeUserEntity); // anything but null

		assertThrows(UserServiceException.class, () -> {
			testUserService.createUser(fakeUserDto);
		});
	}

	@Test
	final void testCreateUser() {
		final String fakeAlphaNumber = "23jhfkf9o";
		// setup
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn(fakeAlphaNumber);
		when(utils.generateUserId(anyInt())).thenReturn(TestingUtils.fakeUserId);
		when(utils.generateEmailVerificationToken(anyString())).thenReturn(TestingUtils.fakeEmailVerificationToken);
		when(passwordEncoder.encode(anyString())).thenReturn(TestingUtils.fakeEncryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(fakeUserEntity);
		doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

		// run
		UserDto testReturnedDto = testUserService.createUser(fakeUserDto);

		// assert
		assertNotNull(testReturnedDto);
		assertEquals(fakeUserEntity.getFirstName(), testReturnedDto.getFirstName());
		assertEquals(fakeUserEntity.getLastName(), testReturnedDto.getLastName());
		assertEquals(TestingUtils.fakeUserId, testReturnedDto.getUserId());
		assertEquals(fakeUserEntity.getAddresses().size(), testReturnedDto.getAddresses().size());

		// TODO this could be it's own test
		verify(utils, times(fakeUserDto.getAddresses().size())).generateAddressId(TestingUtils.fakeIdLength);
		verify(passwordEncoder, times(1)).encode(fakeUserDto.getPassword());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

}
