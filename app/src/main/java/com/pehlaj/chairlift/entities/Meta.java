package com.pehlaj.chairlift.entities;

import com.google.gson.JsonObject;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class Meta {

    protected int status;
    private String stack;
    private String message;

    public Meta() {
    }

    public int getStatus() {
        return status;
    }

    public String getStack() {
        return stack;
    }

    public String getMessage() {
        return message;
    }
}
