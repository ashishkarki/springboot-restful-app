package com.karki.ashish.app.shared;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.karki.ashish.app.TestingUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

	@InjectMocks
	Utils testUtils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	final void testGenerateUserId() {
		String testUserId = testUtils.generateUserId(TestingUtils.fakeIdLength);

		assertNotNull(testUserId);
		assertEquals(TestingUtils.fakeIdLength, testUserId.length());
	}

	@Test
	final void testGenerateUserId_Gives_UniqueValues() {
		String testUserId1 = testUtils.generateUserId(TestingUtils.fakeIdLength);
		String testUserId2 = testUtils.generateUserId(TestingUtils.fakeIdLength);

		assertFalse(testUserId1.equalsIgnoreCase(testUserId2));
	}

	@Test
	final void testHasTokenExpired_with_FreshToken() {
		String testToken = testUtils.generateEmailVerificationToken(TestingUtils.fakeUserId);
		assertNotNull(testToken);

		boolean testTokenExpired = testUtils.hasTokenExpired(testToken);
		assertFalse(testTokenExpired); // this will be false because in above line we are generating fresh token
	}
	
	@Test
	final void testHasTokenExpired_with_ExpiredToken() {
		// we are hardcoding a for-sure expired token
		String testToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzMjc3Nzc3NX0.cdudUo3pwZLN9UiTuXiT7itpaQs6BgUPU0yWbNcz56-l1Z0476N3H_qSEHXQI5lUfaK2ePtTWJfROmf0213UJA";

		boolean testTokenExpired = testUtils.hasTokenExpired(testToken);
		assertTrue(testTokenExpired); 
	}

}
