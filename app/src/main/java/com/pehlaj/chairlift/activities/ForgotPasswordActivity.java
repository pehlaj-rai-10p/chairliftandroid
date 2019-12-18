package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pehlaj.chairlift.config.AppConfig;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.BaseEntity;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.Utils;
import com.pehlaj.chairlift.R;

public class ForgotPasswordActivity extends AppCompatActivity {

	private Button   btnNext;
	private Button   btnBack;
	private TextView txtTitle;
	private EditText eTxtEmail;

	private Toolbar  toolbar;

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
        setContentView(R.layout.activity_forgot_password);

        initViews();

        setupActionBar();

        setListeners();
    }

    private void setupActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);

        txtTitle.setText("Forgot Password");
    }

    private void onNextClick() {

        String email = eTxtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Utils.showToast(this, getString(R.string.err_email));
            return;
        }

        if (!Utils.validateEmail(email)) {
            Utils.showToast(this, getString(R.string.err_invalid_email));
            return;
        }

        forgotPassword(email);
    }

    private void forgotPassword(final String email) {

        if (TextUtils.isEmpty(email)) {
            return;
        }

        ApiClient.getClient().create(Services.class).forgotPassword(Constants.API_KEY, email).enqueue(new WebClientCallBack<BaseEntity>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null || !(response instanceof BaseEntity)) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((BaseEntity) response).isSuccess()) {
                    AppConfig.getInstance().setUsername(email);
                    showVerificationCodeScreen();
                    return;
                }

                //Utils.showToast(ForgotPasswordActivity.this, ((BaseEntity) response).getMessage());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(ForgotPasswordActivity.this, errorMessage);
            }
        }, true));
    }

    private void showVerificationCodeScreen() {

        Intent intent = new Intent(this, CodeVerificationActivity.class);
        startActivity(intent);
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        txtTitle = findViewById(R.id.txtTitle);

        eTxtEmail = findViewById(R.id.eTxtEmail);
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
