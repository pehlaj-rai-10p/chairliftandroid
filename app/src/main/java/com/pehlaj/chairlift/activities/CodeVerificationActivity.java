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

public class CodeVerificationActivity extends AppCompatActivity {

	private Button   btnNext;
	private Button   btnBack;
	private TextView txtTitle;
	private EditText eTxtCode;

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
        setContentView(R.layout.activity_code_verification);

        initViews();

        setupActionBar();

        setListeners();
    }

    private void setupActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);

        txtTitle.setText("Code Verification");
    }

    private void onNextClick() {

        String email = AppConfig.getInstance().getUsername();
        String code = eTxtCode.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Utils.showToast(this, getString(R.string.err_email));
            return;
        }

        if (!Utils.validateEmail(email)) {
            Utils.showToast(this, getString(R.string.err_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            Utils.showToast(this, getString(R.string.err_verification_code));
            return;
        }

        if (code.length() < Constants.MIN_PASSWORD_LENGTH) {
            Utils.showToast(this, String.format(getString(R.string.err_min_password_length), Constants.MIN_PASSWORD_LENGTH));
            return;
        }

        verifyCode(email, code);
    }

    private void verifyCode(final String email, final String code) {

        if (TextUtils.isEmpty(email)) {
            return;
        }

        ApiClient.getClient().create(Services.class).verifyCode(Constants.API_KEY, email, code).enqueue(new WebClientCallBack<BaseEntity>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null || !(response instanceof BaseEntity)) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((BaseEntity) response).isSuccess()) {
                    showChangePasswordScreen();
                    return;
                }

                //Utils.showToast(CodeVerificationActivity.this, ((BaseEntity) response).getMessage());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(CodeVerificationActivity.this, errorMessage);
            }
        }, true));
    }

    private void showChangePasswordScreen() {

        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        txtTitle = findViewById(R.id.txtTitle);
        eTxtCode = findViewById(R.id.eTxtCode);
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
