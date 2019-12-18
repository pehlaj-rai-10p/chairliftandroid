package com.pehlaj.chairlift.entities;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.pehlaj.chairlift.constants.KeyConstants;

/**
 * @author Pehlaj Rai
 * @since 20th-June-2017.
 */

public class BaseEntity {

    @SerializedName(KeyConstants.KEY_SUCCESS)
    protected String success;

    protected String error;

    public BaseEntity(){

    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(success);
    }

    public boolean isSuccessful() {
        return isSuccess() && success.trim().equalsIgnoreCase("Record Inserted");
    }

    public String getError() {
        return error;
    }
}
