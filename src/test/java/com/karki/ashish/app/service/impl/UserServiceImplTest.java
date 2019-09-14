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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

	final String fakeUserId = "78uyiy6rt";
	final String fakePassword = "HsqUI67T";
	final String fakeEncryptedPassword = "kfjf&3545m#!";
	final String fakeEmailVerificationToken = "jldkasfjlkj934870934ldkfjsdldkfjlksdjf";
	final String fakeAddressId = "67tryncjd54";
	final String fakeBillingAddressType = "billing";
	final String fakeShippingAddressType = "shipping";
	final String fakeEmail = "anyRandomStr@gmail.com";
	final String fakeFirstName = "John";
	final String fakeLastName = "Doe";
	final String fakeCity = "SomeCity";
	final String fakeCountry = "SomeCountry";
	final String fakeStreet = "SomeStreet";
	final String fakePostalCode = "545637";

	UserEntity fakeUserEntity;
	AddressDTO fakeBillingAddressDto;
	AddressDTO fakeShippingAddressDto;
	List<AddressDTO> fakeAddressDtoList;
	List<AddressEntity> fakeAddressEntityList;
	UserDto fakeUserDto;

	@BeforeEach
	final void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		setupFakeAddressDTOs();
		setupFakeAddressEntities();

		setupFakeUserEntity();
		setupFakeUserDto();
	}

	@Test
	final void testGetUser() {
		// setup first
		when(userRepository.findByEmail(anyString())).thenReturn(fakeUserEntity);

		// run the actual test
		UserDto testUserDto = testUserService.getUser(fakeEmail);

		// assert
		assertNotNull(testUserDto);
		assertEquals("John", testUserDto.getFirstName());
	}

	@Test
	final void testGetUser_throws_UsernameNotFoundException() {
		// setup
		when(userRepository.findByEmail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> {
			testUserService.getUser(fakeEmail);
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
		when(utils.generateUserId(anyInt())).thenReturn(fakeUserId);
		when(utils.generateEmailVerificationToken(anyString())).thenReturn(fakeEmailVerificationToken);
		when(passwordEncoder.encode(anyString())).thenReturn(fakeEncryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(fakeUserEntity);
		doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

		// run
		UserDto testReturnedDto = testUserService.createUser(fakeUserDto);

		// assert
		assertNotNull(testReturnedDto);
		assertEquals(fakeUserEntity.getFirstName(), testReturnedDto.getFirstName());
		assertEquals(fakeUserEntity.getLastName(), testReturnedDto.getLastName());
		assertEquals(fakeUserId, testReturnedDto.getUserId());
		assertEquals(fakeUserEntity.getAddresses().size(), testReturnedDto.getAddresses().size());

		// TODO this could be it's own test
		verify(utils, times(fakeUserDto.getAddresses().size())).generateAddressId(30);
		verify(passwordEncoder, times(1)).encode(fakeUserDto.getPassword());
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	private void setupFakeAddressDTOs() {
		fakeBillingAddressDto = new AddressDTO();
		fakeBillingAddressDto.setAddressId(fakeAddressId);
		fakeBillingAddressDto.setType(fakeBillingAddressType);
		fakeBillingAddressDto.setCity(fakeCity);
		fakeBillingAddressDto.setCountry(fakeCountry);
		fakeBillingAddressDto.setStreetName(fakeStreet);
		fakeBillingAddressDto.setPostalCode(fakePostalCode);

		fakeShippingAddressDto = new AddressDTO();
		fakeShippingAddressDto.setAddressId(fakeAddressId);
		fakeShippingAddressDto.setType(fakeBillingAddressType);
		fakeShippingAddressDto.setCity(fakeCity);
		fakeShippingAddressDto.setCountry(fakeCountry);
		fakeShippingAddressDto.setStreetName(fakeStreet);
		fakeShippingAddressDto.setPostalCode(fakePostalCode);

		fakeAddressDtoList = new ArrayList<AddressDTO>();
		fakeAddressDtoList.add(fakeBillingAddressDto);
		fakeAddressDtoList.add(fakeShippingAddressDto);
	}

	private void setupFakeAddressEntities() {
		fakeAddressEntityList = new ArrayList<AddressEntity>();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		fakeAddressEntityList = modelMapper.map(fakeAddressDtoList, listType);
	}

	private void setupFakeUserEntity() {
		fakeUserEntity = new UserEntity();
		fakeUserEntity.setId(1L);
		fakeUserEntity.setFirstName(fakeFirstName);
		fakeUserEntity.setLastName(fakeLastName);
		fakeUserEntity.setUserId(fakeUserId);
		fakeUserEntity.setEncryptedPassword(fakeEncryptedPassword);
		fakeUserEntity.setEmail(fakeEmail);
		fakeUserEntity.setEmailVerificationToken(fakeEmailVerificationToken);
		fakeUserEntity.setAddresses(fakeAddressEntityList);
	}

	private void setupFakeUserDto() {
		fakeUserDto = new UserDto();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<UserDto>() {
		}.getType();

		fakeUserDto = modelMapper.map(fakeUserEntity, listType);
		fakeUserDto.setPassword(fakePassword); // only DTO has this property
	}
}
