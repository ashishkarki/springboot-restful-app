package com.karki.ashish.app.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.karki.ashish.app.TestingUtils;
import com.karki.ashish.app.service.AddressService;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.dto.UserDto;
import com.karki.ashish.app.ui.model.response.UserRest;

class UserControllerTest {

	@Mock
	UserService mockUserService;

	@Mock
	AddressService mockAddressService;

	@InjectMocks
	UserController testUserController;

	UserDto fakeUserDto;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		fakeUserDto = TestingUtils.getFakeUserDto();
	}

	@Test
	void testGetUser() {
		when(mockUserService.getUserByUserId(anyString())).thenReturn(fakeUserDto);

		UserRest testReturnedRest = testUserController.getUser(TestingUtils.fakeUserId);

		assertNotNull(testReturnedRest);

		assertEquals(fakeUserDto.getFirstName(), testReturnedRest.getFirstName());
		assertEquals(fakeUserDto.getLastName(), testReturnedRest.getLastName());

		assertTrue(fakeUserDto.getAddresses().size() == testReturnedRest.getAddresses().size());
	}

}
