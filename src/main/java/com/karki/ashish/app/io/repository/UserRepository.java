package com.karki.ashish.app.io.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.karki.ashish.app.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);

	UserEntity findUserByEmailVerificationToken(String token);

	/**
	 * @param pageableRequest
	 * @Note: countQuery is only present because this is a Pageable request. This
	 *        ensures spring data jpa uses our way to count total number of items it
	 *        needs to split into each page
	 */
	@Query(value = "select * from Users u where u.EMAIL_VERIFICATION_STATUS = true", countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_TOKEN = 'true'", nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

	@Query(value = "select * from users u where u.first_name = ?1", nativeQuery = true)
	List<UserEntity> findUsersByFirstName(String firstName);

	@Query(value = "select * from users u where u.last_name = :lname", nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("lname") String lastName);

	@Query(value = "select * from users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

	@Query(value = "select u.first_name, u.last_name from users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

	@Transactional
	@Modifying
	@Query(value = "update users u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.user_id= :userId", nativeQuery = true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);

	/////////// JPQL methods below
	@Query("select user from UserEntity user where user.userId = :uId")
	UserEntity findUserEntityByUserId(@Param("uId") String userId);

	@Query("select u.firstName, u.lastName from UserEntity u where u.userId = :uId")
	List<Object[]> findUserFullNameById(@Param("uId") String userId);

	@Transactional
	@Modifying
	@Query("update UserEntity u set u.emailVerificationStatus = :emailVerificationStatus where u.userId= :userId")
	void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);
}
