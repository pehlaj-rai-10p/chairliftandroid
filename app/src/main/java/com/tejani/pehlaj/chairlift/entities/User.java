package com.tejani.pehlaj.chairlift.entities;

import android.text.TextUtils;

import com.tejani.pehlaj.chairlift.utils.Utils;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class User extends BaseEntity {

    private String name;
    private String cnic;
    private String email;
    private String number;

    public User() {
    }

    public User(String name, String cnic, String email, String number) {
        this.name = name;
        this.cnic = cnic;
        this.email = email;
        this.number = number;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(cnic) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(number);
    }

    public boolean hasValidEmail() {
        return Utils.validateEmail(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
