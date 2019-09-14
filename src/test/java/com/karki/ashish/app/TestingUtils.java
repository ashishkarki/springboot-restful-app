package com.karki.ashish.app;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.karki.ashish.app.io.entity.AddressEntity;
import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.shared.dto.AddressDTO;
import com.karki.ashish.app.shared.dto.UserDto;

/**
 * @author Ashish Karki Contains static methods that help/setup values in actual
 *         tests.
 */
public class TestingUtils {

	public final static String fakeUserId = "78uyiy6rt";
	public final static String fakePassword = "HsqUI67T";
	public final static String fakeEncryptedPassword = "kfjf&3545m#!";
	public final static String fakeEmailVerificationToken = "jldkasfjlkj934870934ldkfjsdldkfjlksdjf";
	public final static String fakeAddressId = "67tryncjd54";
	public final static String fakeBillingAddressType = "billing";
	public final static String fakeShippingAddressType = "shipping";
	public final static String fakeEmail = "anyRandomStr@gmail.com";
	public final static String fakeFirstName = "John";
	public final static String fakeLastName = "Doe";
	public final static String fakeCity = "SomeCity";
	public final static String fakeCountry = "SomeCountry";
	public final static String fakeStreet = "SomeStreet";
	public final static String fakePostalCode = "545637";
	public final static int fakeIdLength = 30;

	static UserEntity fakeUserEntity;
	static AddressDTO fakeBillingAddressDto;
	static AddressDTO fakeShippingAddressDto;
	static List<AddressDTO> fakeAddressDtoList;
	static List<AddressEntity> fakeAddressEntityList;
	static UserDto fakeUserDto;

	public static List<AddressDTO> getFakeAddressDTOs() {
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

		return fakeAddressDtoList;
	}

	public static List<AddressEntity> getFakeAddressEntities() {
		fakeAddressEntityList = new ArrayList<AddressEntity>();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<List<AddressEntity>>() {
		}.getType();

		if (fakeAddressDtoList == null) {
			fakeAddressDtoList = getFakeAddressDTOs();
		}
		fakeAddressEntityList = modelMapper.map(fakeAddressDtoList, listType);

		return fakeAddressEntityList;
	}

	public static UserEntity getFakeUserEntity() {
		fakeUserEntity = new UserEntity();
		fakeUserEntity.setId(1L);
		fakeUserEntity.setFirstName(fakeFirstName);
		fakeUserEntity.setLastName(fakeLastName);
		fakeUserEntity.setUserId(fakeUserId);
		fakeUserEntity.setEncryptedPassword(fakeEncryptedPassword);
		fakeUserEntity.setEmail(fakeEmail);
		fakeUserEntity.setEmailVerificationToken(fakeEmailVerificationToken);

		if (fakeAddressEntityList == null) {
			fakeAddressEntityList = getFakeAddressEntities();
		}
		fakeUserEntity.setAddresses(fakeAddressEntityList);

		return fakeUserEntity;
	}

	public static UserDto getFakeUserDto() {
		fakeUserDto = new UserDto();
		ModelMapper modelMapper = new ModelMapper();
		Type listType = new TypeToken<UserDto>() {
		}.getType();

		if (fakeUserEntity == null) {
			fakeUserEntity = getFakeUserEntity();
		}

		fakeUserDto = modelMapper.map(fakeUserEntity, listType);
		fakeUserDto.setPassword(fakePassword); // only DTO has this property

		return fakeUserDto;
	}
}
