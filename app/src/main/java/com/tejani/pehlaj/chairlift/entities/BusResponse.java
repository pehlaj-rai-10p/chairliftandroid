package com.tejani.pehlaj.chairlift.entities;

import java.util.List;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class BusResponse {

    protected List<Bus> data;

    protected String error;

    public BusResponse() {
    }

    public List<Bus> getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
