package com.tejani.pehlaj.chairlift.entities;

import java.util.List;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class RiderResponse {

    protected List<Rider> data;

    protected String error;

    public RiderResponse() {
    }

    public List<Rider> getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
