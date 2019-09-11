package com.karki.ashish.app.io.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.karki.ashish.app.io.entity.AddressEntity;
import com.karki.ashish.app.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	/**
	 * @param userEntity
	 * @return all Address entities for the given UserEntity.
	 */
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	/**
	 * @return the specific address entry based on addressId field
	 */
	AddressEntity findByAddressId(String addressId);
}
