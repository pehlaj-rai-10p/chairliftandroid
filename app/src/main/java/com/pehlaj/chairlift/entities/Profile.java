package com.pehlaj.chairlift.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Profile extends BaseEntity {

    private String lastName;
    private String firstName;
    private String middleName;

    @SerializedName("cellPhoneNumber")
    private String mobileNumber;

    private String employerName;
    private String employmentCountry;
    private String supervisorName;
    private String supervisorEmail;

    @SerializedName("supervisorCell")
    private String supervisorNumber;

    private String homeCity;
    private String homeState;
    private String homeZip;
    private String homeStreetAddress;

    private String paymentType;
    private String mobileAccount;
    private String paypalAddress;
    private String spiffPostalAddress;

    private String employmentStoreStreetAddress;
    private String employmentStoreCity;
    private String employmentStoreState;
    private String employmentStoreZip;

    public Profile() {
    }

    public String getHomeStreetAddress() {
        return homeStreetAddress;
    }

    public String getEmploymentStoreStreetAddress() {
        return employmentStoreStreetAddress;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmploymentCountry() {
        return employmentCountry;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public String getSupervisorNumber() {
        return supervisorNumber;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public String getHomeState() {
        return homeState;
    }

    public String getHomeZip() {
        return homeZip;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getSpiffPostalAddress() {
        return spiffPostalAddress;
    }

    public String getMobileAccount() {
        return mobileAccount;
    }

    public String getPaypalAddress() {
        return paypalAddress;
    }

    public String getEmploymentStoreCity() {
        return employmentStoreCity;
    }

    public String getEmploymentStoreState() {
        return employmentStoreState;
    }

    public String getEmploymentStoreZip() {
        return employmentStoreZip;
    }
}
