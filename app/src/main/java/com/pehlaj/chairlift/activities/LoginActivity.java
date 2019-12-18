package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.config.AppConfig;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.constants.KeyConstants;
import com.pehlaj.chairlift.entities.LoginResponse;
import com.pehlaj.chairlift.entities.RiderDetails;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.GcmPreferences;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignUp;
    private TextView txtTitle;
    private TextView txtRegister;
    private TextView txtForgotPassword;
    private EditText eTxtPassword;
    private EditText eTxtUsername;

    private boolean shouldExit;

    @Override
    public void onBackPressed() {

        if (shouldExit) {
            super.onBackPressed();
            return;
        }

        Utils.showToast(this, getString(R.string.msg_back_press));

        shouldExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldExit = false;
            }
        }, Constants.BACK_PRESS_DELAY_MILLIS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        setListeners();
    }

    // region handleActivityResults
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void onLoginClick() {

        String userName = eTxtUsername.getText().toString().trim();
        String password = eTxtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            Utils.showToast(this, getString(R.string.err_number));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Utils.showToast(this, getString(R.string.err_token));
            return;
        }

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }

        login(userName, password);
    }

    private void login(final String userName, final String password) {

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty ("userName", userName);

        ApiClient.getClient().create(Services.class).getRiderInfo(requestBody/*.toString()*/).enqueue(new WebClientCallBack<RiderDetails>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((RiderDetails) response).getData() != null) {
                    PreferenceUtility.setInteger(LoginActivity.this, KeyConstants.KEY_RIDER, ((RiderDetails) response).getData().getId());
                    showMessages(userName, password);
                    return;
                }

                Utils.showToast(LoginActivity.this, ((RiderDetails) response).getError());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(LoginActivity.this, errorMessage);
            }
        }, true));
    }

    private void showMessages(String username, String password) {

        PreferenceUtility.setString(LoginActivity.this, Utils.USERNAME, username);
        PreferenceUtility.setString(LoginActivity.this, Utils.PASSWORD, password);

        PreferenceUtility.setBoolean(this, Utils.LOGIN_STATUS, true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showRegisterScreen() {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void showForgotPasswordScreen() {

        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void initViews() {

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        txtTitle = findViewById(R.id.txtTitle);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        eTxtUsername = findViewById(R.id.eTxtUsername);
        eTxtPassword = findViewById(R.id.eTxtPassword);

        txtTitle.setText(getString(R.string.login));
    }

    private void setListeners() {

        btnLogin.setOnClickListener(btnLoginClickListener);

        btnSignUp.setOnClickListener(btnRegisterClickListener);
        txtRegister.setOnClickListener(btnRegisterClickListener);
        txtForgotPassword.setOnClickListener(forgotPasswordClickListener);
    }

    private final View.OnClickListener forgotPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showForgotPasswordScreen();
        }
    };

    private final View.OnClickListener btnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showRegisterScreen();
        }
    };

    private final View.OnClickListener btnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            onLoginClick();
        }
    };

}
