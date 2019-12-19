package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.BaseEntity;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.GcmPreferences;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button btnSubmit;
    private Button btnSignIn;

    private ImageView imgMenu;
    private TextView txtTitle;
    private TextView txtAccount;
    private TextView txtTitleLeft;

    private EditText eTxtName;
    private EditText eTxtToken;
    private EditText eTxtNumber;
    private EditText eTxtUsername;

    private boolean shouldExit;

    @Override
    public void onBackPressed() {

        if (shouldExit) {
            super.onBackPressed();
            return;
        }

        shouldExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldExit = false;
            }
        }, Constants.BACK_PRESS_DELAY_MILLIS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

        setupToolbar();

        setListeners();

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSignIn = findViewById(R.id.btnSignIn);

        imgMenu = findViewById(R.id.imgMenu);
        txtTitle = findViewById(R.id.txtTitle);
        txtAccount = findViewById(R.id.txtAccount);
        txtTitleLeft = findViewById(R.id.txtTitleLeft);

        eTxtName = findViewById(R.id.eTxtName);
        eTxtToken = findViewById(R.id.eTxtToken);
        eTxtNumber = findViewById(R.id.eTxtNumber);
        eTxtUsername = findViewById(R.id.eTxtUsername);

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);
        txtTitle.setText(getString(R.string.sign_up));
        //actionBar.setTitle(getString(R.string.login));
        txtTitleLeft.setText(getString(R.string.login));
        txtTitleLeft.setVisibility(View.VISIBLE);

        imgMenu.setImageResource(R.drawable.arrow_left_blue);
        imgMenu.setVisibility(View.VISIBLE);
    }

    private void showLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showMessages(String username, String password) {

        PreferenceUtility.setString(SignUpActivity.this, Utils.USERNAME, username);
        PreferenceUtility.setString(SignUpActivity.this, Utils.PASSWORD, password);

        PreferenceUtility.setBoolean(this, Utils.LOGIN_STATUS, true);
        Intent intent = new Intent(this, BusListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setListeners() {

        btnSubmit.setOnClickListener(btnSubmitClickListener);

        btnSignIn.setOnClickListener(btnSignInClickListener);

        imgMenu.setOnClickListener(btnSignInClickListener);
        txtAccount.setOnClickListener(btnSignInClickListener);
        txtTitleLeft.setOnClickListener(btnSignInClickListener);
    }

    private final View.OnClickListener btnSignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            finish();
        }
    };

    private void onSubmitClick() {


        final String token = eTxtToken.getText().toString().trim();
        String number = eTxtNumber.getText().toString().trim();
        String fullName = eTxtName.getText().toString().trim();
        final String username = eTxtUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Utils.showToast(this, getString(R.string.err_email));
            return;
        }

        if (!Utils.validateEmail(username)) {
            Utils.showToast(this, getString(R.string.err_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(token)) {
            Utils.showToast(this, getString(R.string.err_token));
            return;
        }

        if (TextUtils.isEmpty(fullName)) {
            Utils.showToast(this, getString(R.string.err_name));
            return;
        }

        if (TextUtils.isEmpty(number)) {
            Utils.showToast(this, getString(R.string.err_number));
            return;
        }

        /*if (number.trim().length() < 11) {
            Utils.showToast(this, getString(R.string.err_invalid_mobile_number));
            return;
        }*/

        String gcmToken = new GcmPreferences(this).getDeviceGCMToken();
        ApiClient.getClient().create(Services.class).register(Constants.ANDROID.toLowerCase(), username, fullName, token, number, gcmToken).enqueue(new WebClientCallBack<BaseEntity>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null || !(response instanceof BaseEntity)) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((BaseEntity) response).isSuccess()) {
                    showMessages(username, token);
                    return;
                }

                Utils.showToast(SignUpActivity.this, ((BaseEntity) response).getMessage());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(SignUpActivity.this, errorMessage);
            }
        }, true));
    }


    private final View.OnClickListener btnSubmitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            onSubmitClick();
        }
    };

}
