package com.tejani.pehlaj.chairlift.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class LoginResponse {

    @SerializedName("success")
    protected LoginData data;

    protected String error;

    public LoginResponse() {
    }

    public LoginData getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
