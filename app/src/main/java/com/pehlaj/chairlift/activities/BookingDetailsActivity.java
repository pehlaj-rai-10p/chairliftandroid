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
import com.pehlaj.chairlift.entities.Booking;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class BookingDetailsActivity extends AppCompatActivity {

	private Toolbar toolbar;

    private Button btnBack;
    private ImageView imgMenu;
    private TextView txtTitle;
    private TextView txtTitleLeft;
    private TextView txtMessageTitle;
    private TextView txtMessageStatus;
    private TextView txtMessageTime;
    private TextView txtMessageBody;

    private ImageView imgSignOut;

    private Booking booking;

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
        setContentView(R.layout.activity_bus_details);

        initViews();

        setupToolbar();

        setListeners();

        populateMessageDetails();
    }

    private void populateMessageDetails() {

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_BOOKING)) {
            return;
        }

        booking = getIntent().getParcelableExtra(Constants.EXTRA_BOOKING);

        if(booking == null) {
            return;
        }

        txtMessageTitle.setText(booking.getTrackingNumber());
        txtMessageTime.setText(String.valueOf(booking.getBusId()));
        txtMessageBody.setText(booking.getBookingTime());
        txtMessageStatus.setText(booking.getStatus());

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnBack = findViewById(R.id.btnBack);

        imgMenu = findViewById(R.id.imgMenu);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitleLeft = findViewById(R.id.txtTitleLeft);
        txtMessageTime = findViewById(R.id.txtMessageTime);
        txtMessageBody = findViewById(R.id.txtMessageBody);
        txtMessageTitle = findViewById(R.id.txtMessageTitle);
        txtMessageStatus = findViewById(R.id.txtMessageStatus);

        imgSignOut = findViewById(R.id.imgSignOut);

        txtTitleLeft.setVisibility(View.VISIBLE);
        imgMenu.setImageResource(R.drawable.arrow_left_blue);
        txtTitle.setText(getString(R.string.booking_details));
        imgMenu.setVisibility(View.VISIBLE);
        imgSignOut.setVisibility(View.GONE);
    }

    private void setupToolbar() {

        txtTitle.setText(getString(R.string.booking_details));
        txtTitleLeft.setText(getString(R.string.app_name));
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
