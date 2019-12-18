package com.pehlaj.chairlift.entities;

import android.text.TextUtils;

/**
 * @author Pehlaj
 * @since 14th-June-2017.
 */

public class Address extends BaseEntity {

    private String city;
    private String state;
    private String zipCode;
    private String streetAddress;

    public Address(String city, String state, String zipCode, String streetAddress) {
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.streetAddress = streetAddress;
    }

    public boolean isValid() {

        return !TextUtils.isEmpty(city) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(zipCode) && !TextUtils.isEmpty(streetAddress);
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String toString() {

        return isValid() ? String.format("%s,%s,%s", city, state, zipCode) : "";
    }
}
