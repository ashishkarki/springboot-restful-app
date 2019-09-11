package com.karki.ashish.app.service;

import java.util.List;

import com.karki.ashish.app.shared.dto.AddressDTO;

public interface AddressService {

	List<AddressDTO> getUserAddresses(String userId);
	AddressDTO getUserAddress(String userId, String addressId);
}
