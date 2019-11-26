package com.tejani.pehlaj.chairlift.activities;

import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.constants.Constants;
import com.tejani.pehlaj.chairlift.ui.login.LoginActivity;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final short SPLASH_TIME = 2000;
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        boolean isUserLoggedIn = PreferenceUtility.getBoolean (this, Constants.PREFERENCE_LOGIN_STATUS, false);

        final Intent intent = new Intent (this, isUserLoggedIn ? MapsActivity.class : LoginActivity.class);

        new Handler ().postDelayed (new Runnable () {
            @Override
            public void run () {
                startActivity (intent);
                finish ();
            }
        }, SPLASH_TIME);
    }
}
