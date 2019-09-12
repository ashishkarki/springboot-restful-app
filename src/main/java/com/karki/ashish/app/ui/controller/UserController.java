package com.karki.ashish.app.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karki.ashish.app.exceptions.UserServiceException;
import com.karki.ashish.app.service.AddressService;
import com.karki.ashish.app.service.UserService;
import com.karki.ashish.app.shared.dto.AddressDTO;
import com.karki.ashish.app.shared.dto.UserDto;
import com.karki.ashish.app.ui.model.request.UserDetailsRequestModel;
import com.karki.ashish.app.ui.model.response.AddressRestModel;
import com.karki.ashish.app.ui.model.response.ErrorMessages;
import com.karki.ashish.app.ui.model.response.OperationStatusModel;
import com.karki.ashish.app.ui.model.response.RequestOperationNames;
import com.karki.ashish.app.ui.model.response.RequestOperationStatuses;
import com.karki.ashish.app.ui.model.response.UserRest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValueRest = new UserRest();

		UserDto foundDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(foundDto, returnValueRest);

		return returnValueRest;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<UserRest>();

		List<UserDto> usersDtos = userService.getUsers(page, limit);

		// copy over objects
		for (UserDto user : usersDtos) {
			UserRest tempUserRest = new UserRest();
			BeanUtils.copyProperties(user, tempUserRest);
			returnValue.add(tempUserRest);
		}

		return returnValue;
	}

	// url to this endpoint would be like:
	// http://localhost:8080/spring-boot-app/users/<userID>/addresses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resources<AddressRestModel> getUserAddresses(@PathVariable String id) {
		List<AddressRestModel> addressRestModels = new ArrayList<AddressRestModel>();

		List<AddressDTO> addressDTOs = addressService.getUserAddresses(id);

		if (addressDTOs != null && !addressDTOs.isEmpty()) {
			// to which type we want to convert
			Type listType = new TypeToken<List<AddressRestModel>>() {
			}.getType();
			addressRestModels = new ModelMapper().map(addressDTOs, listType);

			addressRestModels.forEach(addressRest -> {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
						.withSelfRel();
				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");

				addressRest.add(addressLink);
				addressRest.add(userLink);
			});
		}

		return new Resources<>(addressRestModels);
	}

	// url to this endpoint would be like:
	// http://localhost:8080/spring-boot-app/users/<userID>/addresses/<addressID>
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE, "application/hal+json" })
	public Resource<AddressRestModel> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		AddressDTO addressDTO = addressService.getUserAddress(userId, addressId);

		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link allAddressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");

		AddressRestModel addressRestModel = new ModelMapper().map(addressDTO, AddressRestModel.class);
		addressRestModel.add(addressLink);
		addressRestModel.add(allAddressesLink);
		addressRestModel.add(userLink);

		return new Resource<>(addressRestModel);
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValueRest = new UserRest(); // to be returned to the user; doesn't contain sensitive info

		// Note: this if block is experimental used in testing the exception handling
		// code.
		if (userDetails.getEmail().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUserDto = userService.createUser(userDto); // resulting DTO from DB
		returnValueRest = modelMapper.map(createdUserDto, UserRest.class);

		return returnValueRest;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValueRest = new UserRest(); // to be returned to the user; doesn't contain sensitive info

		UserDto userDto = new UserDto(); // shared DTO to be sent over to the DB
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUserDto = userService.updateUser(id, userDto); // resulting DTO from DB
		BeanUtils.copyProperties(updatedUserDto, returnValueRest);

		return returnValueRest;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnedModel = new OperationStatusModel();

		returnedModel.setOperationName(RequestOperationNames.DELETE.name());

		userService.deleteUser(id);

		returnedModel.setOperationResult(RequestOperationStatuses.SUCCESS.name());

		return returnedModel;
	}
}
