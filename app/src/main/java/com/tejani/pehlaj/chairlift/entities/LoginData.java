package com.tejani.pehlaj.chairlift.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class LoginData extends BaseEntity {

    protected String id;

    @SerializedName("user_name")
    protected String username;

    @SerializedName("user_mobile_number")
    protected String mobileNumber;

    @SerializedName("full_name")
    protected String fullName;

    @SerializedName("user_password")
    protected String password;

    @SerializedName("user_type_id")
    protected String userTypeId;

    protected String status;

    protected String token;

    public LoginData() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
