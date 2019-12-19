package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class BusDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button btnBack;
    private ImageView imgMenu;
    private TextView txtTitle;
    private TextView txtTitleLeft;

    private TextView txtSeats;
    private TextView txtBusStatus;
    private TextView txtChasisNum;
    private TextView txtDriverName;
    private TextView txtRegistrationNum;

    private ImageView imgSignOut;

    private Bus bus;

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
        setContentView(R.layout.activity_bus_details);

        initViews();

        setupToolbar();

        setListeners();

        populateDetails();
    }

    private void populateDetails() {

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_BUS)) {
            return;
        }

        bus = getIntent().getParcelableExtra(Constants.EXTRA_BUS);

        if (bus == null) {
            return;
        }

        txtSeats.setText(bus.getSeatsAvailability());
        txtBusStatus.setText(bus.getStatus());
        txtChasisNum.setText(bus.getChasisNumber());
        txtDriverName.setText(bus.getDriverName());
        txtRegistrationNum.setText(bus.getRegistrationNumber());

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnBack = findViewById(R.id.btnBack);

        imgMenu = findViewById(R.id.imgMenu);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitleLeft = findViewById(R.id.txtTitleLeft);
        txtSeats = findViewById(R.id.txtSeats);
        txtBusStatus = findViewById(R.id.txtBusStatus);
        txtChasisNum = findViewById(R.id.txtChasisNum);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtRegistrationNum = findViewById(R.id.txtRegistrationNum);

        imgSignOut = findViewById(R.id.imgSignOut);

        txtTitleLeft.setVisibility(View.VISIBLE);
        imgMenu.setImageResource(R.drawable.arrow_left_blue);
        txtTitle.setText(getString(R.string.bus_details));
        imgMenu.setVisibility(View.VISIBLE);
        imgSignOut.setVisibility(View.GONE);
    }

    private void setupToolbar() {

        txtTitle.setText(getString(R.string.bus_details));
        txtTitleLeft.setText(getString(R.string.bus_list));
        txtTitleLeft.setVisibility(View.VISIBLE);
    }

    private void setListeners() {

        btnBack.setOnClickListener(btnBackClickListener);

        imgMenu.setOnClickListener(btnBackClickListener);
        txtTitleLeft.setOnClickListener(btnBackClickListener);
        imgSignOut.setOnClickListener(signOutClickListener);
    }

    private final View.OnClickListener signOutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showLoginScreen();
        }
    };

    private void showLoginScreen() {

        PreferenceUtility.setBoolean(this, Utils.LOGIN_STATUS, false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private final View.OnClickListener btnBackClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            finish();
        }
    };
}
