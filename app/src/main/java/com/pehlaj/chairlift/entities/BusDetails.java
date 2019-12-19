package com.pehlaj.chairlift.entities;

/**
 * @author Pehlaj
 * @since 19th Dec, 2019.
 */

public class BusDetails extends BaseEntity {

    protected Bus data;

    protected String error;

    public BusDetails() {
    }

    public Bus getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
