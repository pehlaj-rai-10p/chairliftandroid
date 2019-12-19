package com.tejani.pehlaj.chairlift.entities;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tejani.pehlaj.chairlift.utils.Utils;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class UserProfile extends BaseEntity {

    private String lastName;
    private String firstName;
    private String middleName;
    private String mobileNumber;
    private String employerName;
    private String employmentCountry;
    private String supervisorName;
    private String supervisorEmail;
    private String supervisorNumber;

    private String googleID;
    private String facebookID;

    private Address homeAddress;
    private Address storeAddress;

    @SerializedName("data")
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public UserProfile() {
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(middleName)
                && !TextUtils.isEmpty(mobileNumber) && !TextUtils.isEmpty(employerName) && !TextUtils.isEmpty(employmentCountry)
                && !TextUtils.isEmpty(supervisorName) &&!TextUtils.isEmpty(supervisorEmail) &&!TextUtils.isEmpty(supervisorNumber);
    }

    public boolean hasValidSubscriberEmail() {
        return Utils.validateEmail(supervisorEmail);
    }

}
