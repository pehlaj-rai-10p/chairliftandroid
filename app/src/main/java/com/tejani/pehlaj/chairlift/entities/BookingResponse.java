package com.tejani.pehlaj.chairlift.entities;

import java.util.List;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class BookingResponse {

    protected List<Booking> data;

    protected String error;

    public BookingResponse() {
    }

    public List<Booking> getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
