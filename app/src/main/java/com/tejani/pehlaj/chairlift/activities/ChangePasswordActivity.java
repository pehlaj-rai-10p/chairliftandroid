package com.tejani.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
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

public class ChangePasswordActivity extends AppCompatActivity {

	private Button   btnBack;
	private Button   btnSubmit;
	private TextView txtTitle;
	private EditText eTxtPassword;

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
        setContentView(R.layout.activity_change_password);

        initViews();

        setupActionBar();

        setListeners();
    }

    private void setupActionBar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);

        txtTitle.setText("Change Password");
    }

    private void onSubmitClick() {

        String email = AppConfig.getInstance().getUsername();
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
        changePassword(email, password);
    }

    private void changePassword(final String email, final String password) {

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return;
        }

        String base64EncodedPassword = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);

        ApiClient.getClient().create(Services.class).changePassword(Constants.API_KEY, email, base64EncodedPassword).enqueue(new WebClientCallBack<BaseEntity>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response == null || !(response instanceof BaseEntity)) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                //Utils.showToast(ChangePasswordActivity.this, ((BaseEntity) response).getMessage());

                if (((BaseEntity) response).isSuccess()) {
                    showLoginScreen();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(ChangePasswordActivity.this, errorMessage);
            }
        }, true));
    }

    private void showLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnBack = findViewById(R.id.btnBack);
        btnSubmit = findViewById(R.id.btnSubmit);

        txtTitle = findViewById(R.id.txtTitle);

        eTxtPassword = findViewById(R.id.eTxtPassword);
    }

    private void setListeners() {

        btnSubmit.setOnClickListener(btnSubmitClickListener);
        btnBack.setOnClickListener(btnBackClickListener);
    }

    private final View.OnClickListener btnSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            onSubmitClick();
        }
    };

    private final View.OnClickListener btnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            finish();
        }
    };
}
