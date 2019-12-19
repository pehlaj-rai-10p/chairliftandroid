package com.pehlaj.chairlift.entities;

import android.text.TextUtils;

import com.pehlaj.chairlift.constants.Constants;

/**
 * @author Pehlaj Rai
 * @since 20th-June-2017.
 */

public class BaseEntity {

    private Meta meta;

    public BaseEntity() {

    }

    public boolean isSuccess() {
        return meta != null && (meta.getStatus() == 200 || meta.getStatus() == 201);
    }

    public String getMessage() {
        return (meta == null || TextUtils.isEmpty(meta.getMessage())) ? Constants.UNKNOWN_ERROR : meta.getMessage();
    }
}
