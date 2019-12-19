package com.tejani.pehlaj.chairlift.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.config.AppConfig;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;
import com.tejani.pehlaj.chairlift.utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 2000;

    public static Context context;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        handler.postDelayed(runnable, DELAY_MILLIS);
    }

    private void navigateToHomeScreen() {

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showLoginScreen() {

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            String username = PreferenceUtility.getString(MainActivity.this, Utils.USERNAME, "");
            String password = PreferenceUtility.getString(MainActivity.this, Utils.PASSWORD, "");

            boolean isLoggedIn = PreferenceUtility.getBoolean(MainActivity.this, Utils.LOGIN_STATUS, false);

            if (!isLoggedIn || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                showLoginScreen();
                return;
            }
            AppConfig.getInstance().setUsername(username);
            AppConfig.getInstance().setPassword(password);
            navigateToHomeScreen();
        }
    };

}
