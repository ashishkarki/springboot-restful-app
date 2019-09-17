package com.karki.ashish.app.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "addresses")
public class AddressEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4782536969993312974L;

	@Id
	@GeneratedValue
	private long id; // auto-increment id from DB

	@Column(nullable = false, length = 30)
	private String addressId; // alphanumeric Id shared back to the user.

	@Column(nullable = false, length = 15)
	private String city;

	@Column(nullable = false, length = 15)
	private String country;

	@Column(nullable = false, length = 100)
	private String streetName;

	@Column(nullable = false, length = 7)
	private String postalCode;

	@Column(nullable = false, length = 10)
	private String type;

	@ManyToOne
	@JoinColumn(name = "users_id") // name-of-primary-table_name-of-primary-key-in-primary-table
	@JsonIgnore // so that user object isn't nested in addresses array
	private UserEntity userDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

}
