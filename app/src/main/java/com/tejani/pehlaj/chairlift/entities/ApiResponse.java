package com.tejani.pehlaj.chairlift.entities;

import com.google.gson.JsonObject;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class ApiResponse extends BaseEntity {

    protected JsonObject data;

    public ApiResponse() {
    }

    public JsonObject getData() {
        return data;
    }
}
