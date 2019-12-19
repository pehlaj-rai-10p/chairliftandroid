package com.tejani.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tejani.pehlaj.chairlift.config.AppConfig;
import com.tejani.pehlaj.chairlift.constants.Constants;
import com.tejani.pehlaj.chairlift.entities.BaseEntity;
import com.tejani.pehlaj.chairlift.interfaces.Services;
import com.tejani.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.tejani.pehlaj.chairlift.network.ApiClient;
import com.tejani.pehlaj.chairlift.network.WebClientCallBack;
import com.tejani.pehlaj.chairlift.utils.Utils;
import com.tejani.pehlaj.chairlift.R;

public class EmailValidationActivity extends AppCompatActivity {

	private Toolbar  toolbar;
	private Button   btnNext;
	private Button   btnBack;
    private TextView txtTitle;
	private EditText eTxtEmail;
	private EditText eTxtPassword;

	private boolean  shouldExit;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation);

        initViews();

        setupActionBar();

        setListeners();
    }

    @Override
    public void onBackPressed() {

        if(shouldExit) {
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

    private void setupActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);

        txtTitle.setText(getString(R.string.sign_up));
    }

    private void onNextClick() {

        String email = eTxtEmail.getText().toString().trim();
        String password = eTxtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Utils.showToast(this, getString(R.string.err_email));
            return;
        }

        if (!Utils.validateEmail(email)) {
            Utils.showToast(this, getString(R.string.err_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Utils.showToast(this, getString(R.string.err_password));
            return;
        }

        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            Utils.showToast(this, String.format(getString(R.string.err_min_password_length), Constants.MIN_PASSWORD_LENGTH));
            return;
        }

        AppConfig.getInstance().setPassword(password);
        validateEmail(email);
    }

    private void validateEmail(final String email) {

        if (TextUtils.isEmpty(email)) {
            return;
        }

        ApiClient.getClient().create(Services.class).validateEmail(Constants.API_KEY, email).enqueue(new WebClientCallBack<BaseEntity>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null || !(response instanceof BaseEntity)) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((BaseEntity) response).isSuccess()) {
                    AppConfig.getInstance().setUsername(email);
                    showSignUpScreen();
                    return;
                }

                //Utils.showToast(EmailValidationActivity.this, ((BaseEntity) response).getMessage());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(EmailValidationActivity.this, errorMessage);
            }
        }, true));
    }

    private void showSignUpScreen() {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        txtTitle = findViewById(R.id.txtTitle);
        eTxtEmail = findViewById(R.id.eTxtEmail);
        eTxtPassword = findViewById(R.id.eTxtPassword);
    }

    private void setListeners() {

        btnNext.setOnClickListener(btnNextClickListener);
        btnBack.setOnClickListener(btnBackClickListener);
    }

    private final View.OnClickListener btnNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            onNextClick();
        }
    };

    private final View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            finish();
        }
    };
}
