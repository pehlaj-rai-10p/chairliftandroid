package com.pehlaj.chairlift.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.pehlaj.chairlift.config.AppConfig;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;
import com.pehlaj.chairlift.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 2000;

    public static Context   context;
	private final Handler   handler      = new Handler ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        handler.postDelayed(runnable, DELAY_MILLIS);
    }

    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.idro.chairlift",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
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
