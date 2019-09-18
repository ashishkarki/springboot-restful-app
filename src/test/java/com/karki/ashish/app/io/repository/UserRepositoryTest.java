package com.karki.ashish.app.io.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.karki.ashish.app.TestingUtils;
import com.karki.ashish.app.io.entity.AddressEntity;
import com.karki.ashish.app.io.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	final static String encrpted_string = "76hfjkf9";
	final static String simple_string = "any string";

	@Autowired
	UserRepository testUserRepository;

	private static boolean hasData = false;
	private static UserEntity userEntity1;

	@BeforeEach
	void setUp() throws Exception {

		if (!hasData) {
			userEntity1 = new UserEntity();
			userEntity1.setFirstName(TestingUtils.fakeFirstName);
			userEntity1.setLastName(TestingUtils.fakeLastName);
			userEntity1.setEmail(TestingUtils.getRandomFakeEmail());
			userEntity1.setEmailVerificationStatus(true);
			userEntity1.setEncryptedPassword(TestingUtils.fakeEncryptedPassword);
			userEntity1.setEmailVerificationToken(TestingUtils.fakeEmailVerificationToken);
			userEntity1.setUserId(TestingUtils.fakeUserId);

			AddressEntity addressEntity1 = new AddressEntity();
			addressEntity1.setAddressId(TestingUtils.fakeAddressId);
			addressEntity1.setType(TestingUtils.fakeBillingAddressType);
			addressEntity1.setCountry(TestingUtils.fakeCountry);
			addressEntity1.setCity(TestingUtils.fakeCity);
			addressEntity1.setStreetName(TestingUtils.fakeStreet);
			addressEntity1.setPostalCode(TestingUtils.fakePostalCode);

			List<AddressEntity> addresses = new ArrayList<AddressEntity>();
			addresses.add(addressEntity1);

			userEntity1.setAddresses(addresses);

			testUserRepository.save(userEntity1);

			hasData = true;
		}
	}

	@AfterEach
	final void tearApart() {
		// delete the record that was just inserted
		testUserRepository.delete(userEntity1);
	}

	@Test
	final void testGetVerifiedUsers() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<UserEntity> page = testUserRepository.findAllUsersWithConfirmedEmailAddress(pageable);

		assertNotNull(page);

		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}

}
