package com.tejani.pehlaj.chairlift.entities;

import android.text.TextUtils;

/**
 * @author Pehlaj
 * @since 6/4/2017.
 */

public class AppUser extends BaseEntity {

    private String username;
    private String password;
    private String terminalID;

    public AppUser() {
    }

    public AppUser(String username, String password, String terminalID) {
        this.username = username;
        this.password = password;
        this.terminalID = terminalID;
    }

    public boolean hasUsername(String username) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(this.username) && this.username.equals(username);
    }

    public boolean hasPassword(String password) {
        return !TextUtils.isEmpty(password) && !TextUtils.isEmpty(this.password) && this.password.equals(password);
    }

    public boolean hasTerminalID(String terminalID) {
        return !TextUtils.isEmpty(terminalID) && !TextUtils.isEmpty(this.terminalID) && this.terminalID.equals(terminalID);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTerminalID() {
        return terminalID;
    }
}
