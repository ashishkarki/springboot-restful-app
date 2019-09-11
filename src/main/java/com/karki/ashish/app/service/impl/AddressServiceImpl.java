package com.karki.ashish.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karki.ashish.app.io.entity.AddressEntity;
import com.karki.ashish.app.io.entity.UserEntity;
import com.karki.ashish.app.io.repository.AddressRepository;
import com.karki.ashish.app.io.repository.UserRepository;
import com.karki.ashish.app.service.AddressService;
import com.karki.ashish.app.shared.dto.AddressDTO;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getUserAddresses(String userId) {
		List<AddressDTO> addressDTOs = new ArrayList<AddressDTO>();
		ModelMapper modelMapper = new ModelMapper();

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			return addressDTOs; // return the empty list

		// otherwise get address from the DB
		Iterable<AddressEntity> addressEntitiesIterable = addressRepository.findAllByUserDetails(userEntity);

		addressEntitiesIterable.forEach(addressEntity -> {
			addressDTOs.add(modelMapper.map(addressEntity, AddressDTO.class));
		});

		return addressDTOs;
	}

	@Override
	public AddressDTO getUserAddress(String userId, String addressId) {
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) {
			return new AddressDTO(); // or throw an exception here
		}

		if (null != addressEntity) {
			return new ModelMapper().map(addressEntity, AddressDTO.class);
		}

		return new AddressDTO();
	}

}
