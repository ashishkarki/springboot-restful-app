package com.karki.ashish.app.io.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.karki.ashish.app.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);

	/**
	 * @param pageableRequest
	 * @Note: countQuery is only present because this is a Pageable request. This ensures spring data 
	 * jpa uses our way to count total number of items it needs to split into each page
	 */
	@Query(value = "select * from Users u where u.EMAIL_VERIFICATION_STATUS = true", 
			countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_TOKEN = 'true'",
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
}
