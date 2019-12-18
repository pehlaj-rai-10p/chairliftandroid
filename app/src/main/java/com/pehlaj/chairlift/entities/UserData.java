package com.pehlaj.chairlift.entities;

import android.text.TextUtils;

import com.pehlaj.chairlift.utils.Utils;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

@SuppressWarnings({"RedundantNoArgConstructor", "OverlyComplexBooleanExpression"})
public class UserData extends BaseEntity {

	private String firstName;
	private String lastName;
	private String zip;
	private String imei;
	private String city;
	private String state;
	private String email;
	private String number;

	private String addressLine1;
	private String addressLine2;
	private String addressLine3;

	public UserData() {

	}

	public boolean isValid() {

		return !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(number) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(zip) && !TextUtils.isEmpty(imei) && !TextUtils.isEmpty(addressLine1) && !TextUtils.isEmpty(addressLine2) && !TextUtils.isEmpty(addressLine3);
	}

	public boolean hasValidEmail() {

		return Utils.validateEmail(email);
	}

	public String getFirstName() {

		return firstName;
	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	public String getLastName() {

		return lastName;
	}

	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	public String getZip() {

		return zip;
	}

	public void setZip(String zip) {

		this.zip = zip;
	}

	public String getImei() {

		return imei;
	}

	public void setImei(String imei) {

		this.imei = imei;
	}

	public String getCity() {

		return city;
	}

	public void setCity(String city) {

		this.city = city;
	}

	public String getState() {

		return state;
	}

	public void setState(String state) {

		this.state = state;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getNumber() {

		return number;
	}

	public void setNumber(String number) {

		this.number = number;
	}

	public String getAddressLine1() {

		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {

		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {

		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {

		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {

		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {

		this.addressLine3 = addressLine3;
	}
}
