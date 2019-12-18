package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.constants.KeyConstants;
import com.pehlaj.chairlift.entities.ApiResponse;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.entities.Location;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class BookRideActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button btnBook;
    private ImageView imgMenu;
    private TextView txtTitle;
    private TextView txtTitleLeft;
    private TextView txtMessageTitle;
    private TextView txtMessageStatus;
    private TextView txtMessageTime;
    private TextView txtMessageBody;

    private ImageView imgSignOut;

    private Bus bus;
    private Location pickup;
    private Location dropOff;

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
        setContentView(R.layout.activity_book_ride);

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

        txtMessageTitle.setText(bus.getRegistrationNumber());
        txtMessageTime.setText(bus.getDriverName());
        txtMessageBody.setText(bus.getChasisNumber());
        txtMessageStatus.setText(bus.getStatus());

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        btnBook = findViewById(R.id.btnBook);

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
        txtTitle.setText(getString(R.string.bus_details));
        imgMenu.setVisibility(View.VISIBLE);
        imgSignOut.setVisibility(View.GONE);
    }

    private void setupToolbar() {

        txtTitle.setText(getString(R.string.book_ride));
        txtTitleLeft.setText(getString(R.string.bus_list));
        txtTitleLeft.setVisibility(View.VISIBLE);
    }

    private void setListeners() {

        btnBook.setOnClickListener(btnBookClickListener);

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

            bookRide(bus.getId(), pickup, dropOff);
        }
    };

    private void bookRide(final int busId, Location pickup, Location dropOff) {

        pickup = new Location("67.0559513", "24.8693563"); //dummy data
        dropOff = new Location("67.0394186", "24.8438739"); //dummy data

        if (busId < 0 || pickup == null || dropOff == null) {
            return;
        }

        int rider = PreferenceUtility.getInteger(this, KeyConstants.KEY_RIDER, 0);

        //{"riderId":1,"busId":1,"pickupLocation":{"lat":"67.0559513","lng":"24.8693563"},"dropOffLocation":{"lat":"67.0394186","lng":"24.8438739"}}
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty(KeyConstants.KEY_BUS_ID, busId);
        requestBody.addProperty(KeyConstants.KEY_RIDER_ID, rider);
        requestBody.add(KeyConstants.KEY_PICKUP_LOCATION, pickup.toJson());
        requestBody.add(KeyConstants.KEY_DROPOFF_LOCATION, dropOff.toJson());

        ApiClient.getClient().create(Services.class).bookRide(requestBody).enqueue(new WebClientCallBack<ApiResponse>(this, new WebServiceCallBack() {

            @Override
            public void onSuccess(Object response) {

                if (response == null) {
                    return;
                }

                Log.w("RESPONSE", response.toString());

                if (((ApiResponse) response).getData() != null) {

                    setResult(RESULT_OK);
                    finish();

                    return;
                }

                Utils.showToast(BookRideActivity.this, ((ApiResponse) response).getMeta().getMessage());
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(BookRideActivity.this, errorMessage);
            }
        }, true));
    }

}
