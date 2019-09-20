package com.karki.ashish.app.io.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

	private static UserEntity userEntity1;
	private static UserEntity userEntity2;

	@BeforeEach
	void setUp() throws Exception {

		userEntity1 = new UserEntity();
		userEntity1.setFirstName(TestingUtils.fakeFirstName);
		userEntity1.setLastName(TestingUtils.fakeLastName);
		userEntity1.setEmail(TestingUtils.getRandomFakeEmail());
		userEntity1.setEmailVerificationStatus(true);
		userEntity1.setEncryptedPassword(TestingUtils.fakeEncryptedPassword);
		userEntity1.setEmailVerificationToken(TestingUtils.fakeEmailVerificationToken);
		userEntity1.setUserId(TestingUtils.getRandomFakeUserId());

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

		userEntity2 = new UserEntity();
		userEntity2.setFirstName(TestingUtils.fakeFirstName);
		userEntity2.setLastName(TestingUtils.fakeLastName);
		userEntity2.setEmail(TestingUtils.getRandomFakeEmail());
		userEntity2.setEmailVerificationStatus(true);
		userEntity2.setEncryptedPassword(TestingUtils.fakeEncryptedPassword);
		userEntity2.setEmailVerificationToken(TestingUtils.fakeEmailVerificationToken);
		userEntity2.setUserId(TestingUtils.getRandomFakeUserId());

		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setAddressId(TestingUtils.fakeAddressId);
		addressEntity2.setType(TestingUtils.fakeBillingAddressType);
		addressEntity2.setCountry(TestingUtils.fakeCountry);
		addressEntity2.setCity(TestingUtils.fakeCity);
		addressEntity2.setStreetName(TestingUtils.fakeStreet);
		addressEntity2.setPostalCode(TestingUtils.fakePostalCode);

		List<AddressEntity> addresses2 = new ArrayList<AddressEntity>();
		addresses2.add(addressEntity2);

		userEntity2.setAddresses(addresses2);
		testUserRepository.save(userEntity2);

	}

	@AfterEach
	final void tearApart() {
		// delete the record that was just inserted
		testUserRepository.delete(userEntity1);
		testUserRepository.delete(userEntity2);
	}

	@Test
	final void testGetVerifiedUsers() {
		Pageable pageable = PageRequest.of(0, 1);
		Page<UserEntity> page = testUserRepository.findAllUsersWithConfirmedEmailAddress(pageable);

		assertNotNull(page);

		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}

	@Test
	final void testGetUsersByFirstName() {
		List<UserEntity> usersByFirstName = testUserRepository.findUsersByFirstName(TestingUtils.fakeFirstName);

		assertNotNull(usersByFirstName);
		assertTrue(usersByFirstName.size() == 2);// both our users were inserted with same name

		UserEntity oneUser = usersByFirstName.get(0);
		assertEquals(TestingUtils.fakeFirstName, oneUser.getFirstName());
	}

	@Test
	final void testGetUsersByLastName() {
		List<UserEntity> usersByLastName = testUserRepository.findUsersByFirstName(TestingUtils.fakeFirstName);

		assertNotNull(usersByLastName);
		assertTrue(usersByLastName.size() == 2);// both our users were inserted with same lname

		UserEntity oneUser = usersByLastName.get(0);
		assertEquals(TestingUtils.fakeLastName, oneUser.getLastName());
	}

	@Test
	final void testGetUsersByKeyword() {
		final String keywordString = "Joh";
		List<UserEntity> usersByLastName = testUserRepository.findUserByKeyword(keywordString);

		assertNotNull(usersByLastName);
		assertTrue(usersByLastName.size() == 2); // we have two lastnames "Joh"

		UserEntity oneUser = usersByLastName.get(0);
		assertTrue(oneUser.getLastName().contains(keywordString) || oneUser.getFirstName().contains(keywordString));
	}

	@Test
	final void testFindUserFirstNameAndLastNameByKeyword() {
		final String keywordString = "Joh";
		List<Object[]> usersByLastName = testUserRepository.findUserFirstNameAndLastNameByKeyword(keywordString);

		assertNotNull(usersByLastName);
		assertTrue(usersByLastName.size() == 2); // we have two lastnames "Joh"

		Object[] user = usersByLastName.get(0);
		assertTrue(user.length == 2); // there is only first and last name in each object

		final String firstName = String.valueOf(user[0]);
		final String lastName = String.valueOf(user[1]);

		assertNotNull(firstName);
		assertNotNull(lastName);
	}

	@Test
	final void testUpdateUserEmailVerificationStatus() {
		final boolean newStatus = false;

		testUserRepository.updateUserEmailVerificationStatus(newStatus, userEntity1.getUserId());

		UserEntity storedUserEntity = testUserRepository.findByUserId(userEntity1.getUserId());

		assertTrue(storedUserEntity.getEmailVerificationStatus() == newStatus);
	}

	@Test
	final void testFindUserEntityByUserId() {
		UserEntity userEntity = testUserRepository.findUserEntityByUserId(userEntity1.getUserId());

		assertNotNull(userEntity);
		assertEquals(userEntity1.getUserId(), userEntity.getUserId());
	}

	@Test
	final void testFindUserFullNameById() {
		List<Object[]> userFullName = testUserRepository.findUserFullNameById(userEntity1.getUserId());

		assertNotNull(userFullName);
		assertTrue(userFullName.size() == 1);

		final String fName = (String) userFullName.get(0)[0];
		final String lName = (String) userFullName.get(0)[1];

		assertNotNull(fName);
		assertNotNull(lName);
	}

	@Test
	final void testUpdateUserEntityEmailVerificationStatus() {
		final boolean newStatus = false;

		testUserRepository.updateUserEntityEmailVerificationStatus(newStatus, userEntity1.getUserId());

		UserEntity storedUserEntity = testUserRepository.findByUserId(userEntity1.getUserId());

		assertTrue(storedUserEntity.getEmailVerificationStatus() == newStatus);
	}

}
