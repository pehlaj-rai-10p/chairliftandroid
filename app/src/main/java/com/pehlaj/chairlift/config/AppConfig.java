package com.pehlaj.chairlift.config;

import android.content.Context;
import android.text.TextUtils;

import com.pehlaj.chairlift.activities.MainActivity;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

/**
 * @author Pehlaj
 * @since 6/7/2017.
 */
public class AppConfig {

    private static final AppConfig instance = new AppConfig();

    private String username;
    private String password;
    private boolean isAppRunning;

    public static AppConfig getInstance() {
        return instance;
    }

    private AppConfig() {
    }

    public String getBase6Authorization() {
        return Utils.getBase6Authorization(getUsername(), getPassword());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        if (TextUtils.isEmpty(username)) {
            username = PreferenceUtility.getString(MainActivity.context, Utils.USERNAME, "");
        }
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        if (TextUtils.isEmpty(password)) {
            password = PreferenceUtility.getString(MainActivity.context, Utils.PASSWORD, "");
        }
        return password;
    }

    public void isAppRunning(boolean isAppRunning) {
        this.isAppRunning = isAppRunning;
        PreferenceUtility.setBoolean(MainActivity.context, Constants.IS_APP_RUNNING, isAppRunning);
    }

    public boolean isAppRunning(Context context) {

        isAppRunning = PreferenceUtility.getBoolean(context, Constants.IS_APP_RUNNING, false);
        return isAppRunning;
    }
}
