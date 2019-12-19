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
import com.pehlaj.chairlift.config.AppConfig;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Booking;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.entities.BusDetails;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class BookingDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button btnBook;
    private Button btnTrack;
    private Button btnTrackRide;
    private Button btnBookAnotherRide;

    private ImageView imgMenu;
    private TextView txtTitle;
    private TextView txtTitleLeft;
    private TextView txtTrackingNum;
    private TextView txtStatus;
    private TextView txtPickup;
    private TextView txtDropoff;
    private TextView txtBookingTime;
    private TextView txtDropoffTime;

    private TextView txtSeats;
    private TextView txtBusStatus;
    private TextView txtChasisNum;
    private TextView txtDriverName;
    private TextView txtRegistrationNum;


    private ImageView imgSignOut;

    private Booking booking;

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
        setContentView(R.layout.activity_booking_details);

        initViews();

        setupToolbar();

        setListeners();

        populateDetails();
    }

    private void populateDetails() {

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_BOOKING)) {
            return;
        }

        booking = getIntent().getParcelableExtra(Constants.EXTRA_BOOKING);

        if (booking == null) {
            return;
        }

        txtStatus.setText(booking.getStatus());
        txtPickup.setText(booking.getPickupLatLng());
        txtDropoff.setText(booking.getDropoffLatLng());
        txtBookingTime.setText(booking.getBookingTime());
        txtDropoffTime.setText(booking.getDropOffTime());
        txtTrackingNum.setText(booking.getTrackingNumber());

        getBusDetails(booking.getBusId());
    }

    private void populateBusDetails(Bus bus) {

        if (bus == null) {

            return;
        }

        txtSeats.setText(bus.getSeatsAvailability());
        txtBusStatus.setText(bus.getStatus());
        txtChasisNum.setText(bus.getChasisNumber());
        txtDriverName.setText(bus.getDriverName());
        txtRegistrationNum.setText(bus.getRegistrationNumber());
    }

    private void getBusDetails(int busId) {

        ApiClient.getClient().create(Services.class).getBusDetails(AppConfig.getInstance().getBase6Authorization(), busId).enqueue(new WebClientCallBack<BusDetails>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (!(response instanceof BusDetails) || ((BusDetails) response).getData() == null) {
                    Utils.showToast(getApplicationContext(), ((BusDetails) response).getError());
                    return;
                }

                populateBusDetails((((BusDetails) response).getData()));
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(BookingDetailsActivity.this, errorMessage);
            }
        }, true));
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);

        btnBook = findViewById(R.id.btnBook);
        btnTrack = findViewById(R.id.btnTrack);
        btnTrackRide = findViewById(R.id.btnTrackRide);
        btnBookAnotherRide = findViewById(R.id.btnBookAnotherRide);

        btnBook.setVisibility(View.GONE);
        btnTrack.setVisibility(View.GONE);

        imgMenu = findViewById(R.id.imgMenu);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitleLeft = findViewById(R.id.txtTitleLeft);

        txtStatus = findViewById(R.id.txtStatus);
        txtPickup = findViewById(R.id.txtPickup);
        txtDropoff = findViewById(R.id.txtDropoff);
        txtBookingTime = findViewById(R.id.txtBookingTime);
        txtDropoffTime = findViewById(R.id.txtDropoffTime);
        txtTrackingNum = findViewById(R.id.txtTrackingNum);

        txtSeats = findViewById(R.id.txtSeats);
        txtBusStatus = findViewById(R.id.txtBusStatus);
        txtChasisNum = findViewById(R.id.txtChasisNum);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtRegistrationNum = findViewById(R.id.txtRegistrationNum);

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

        btnTrackRide.setOnClickListener(btnTrackClickListener);
        btnBookAnotherRide.setOnClickListener(btnBookClickListener);

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

    private final View.OnClickListener btnBookClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            startActivity(new Intent(BookingDetailsActivity.this, BusListActivity.class));
        }
    };

    private final View.OnClickListener btnTrackClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            startActivity(new Intent(BookingDetailsActivity.this, BusListActivity.class));
        }
    };
}
