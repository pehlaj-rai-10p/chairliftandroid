package com.pehlaj.chairlift.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.adapters.BusAdapter;
import com.pehlaj.chairlift.components.RecyclerVerticalSpacingItemDecoration;
import com.pehlaj.chairlift.components.SpaceItemDecoration;
import com.pehlaj.chairlift.config.AppConfig;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Bus;
import com.pehlaj.chairlift.entities.BusResponse;
import com.pehlaj.chairlift.interfaces.BusCallback;
import com.pehlaj.chairlift.interfaces.Services;
import com.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.pehlaj.chairlift.network.ApiClient;
import com.pehlaj.chairlift.network.WebClientCallBack;
import com.pehlaj.chairlift.utils.PreferenceUtility;
import com.pehlaj.chairlift.utils.Utils;

public class BusListActivity extends AppCompatActivity implements BusCallback {

    private Toolbar toolbar;
    private TextView txtTitle;
    private TextView txtTitleLeft;
    private TextView txtErrorMsg;
    private ImageView imgMenu;
    private ImageView imgSignOut;
    private RecyclerView recyclerView;

    private SpaceItemDecoration spaceItemDecoration;
    private RecyclerVerticalSpacingItemDecoration verticalSpacingItemDecoration;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //TODO finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        initViews();

        setupToolbar();

        setListeners();

        setupRecyclerView();

        fetchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            getBusList();
        }
    }

    @Override
    public void onItemClick(Bus bus) {

        if(bus == null) {
            return;
        }

        showBusDetailsScreen(bus);
    }

    @Override
    public void onBookRide(Bus bus) {

        if(bus == null) {
            return;
        }

        showBookRideScreen (bus);
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);

        txtTitle.setText(getString(R.string.bus_list));
        txtTitleLeft.setVisibility(View.VISIBLE);
        imgMenu.setImageResource(R.drawable.arrow_left_blue);
        imgMenu.setVisibility(View.INVISIBLE);
        imgSignOut.setVisibility(View.GONE);

        txtTitle.setText(getString(R.string.bus_list));
        txtTitleLeft.setText(getString(R.string.app_name));
        txtTitleLeft.setVisibility(View.VISIBLE);
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);

        txtTitle = findViewById(R.id.txtTitle);
        txtTitleLeft = findViewById(R.id.txtTitleLeft);
        txtErrorMsg = findViewById(R.id.txtErrorMsg);
        recyclerView = findViewById(R.id.recyclerView);

        imgMenu = findViewById(R.id.imgMenu);
        imgSignOut = findViewById(R.id.imgSignOut);
        imgMenu.setVisibility(View.INVISIBLE);

        if (imgSignOut == null) {
            return;
        }

        imgSignOut.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_4dp));
        verticalSpacingItemDecoration = new RecyclerVerticalSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_4dp));
    }

    private void setListeners() {

        imgMenu.setOnClickListener(signOutClickListener);
        imgSignOut.setOnClickListener(signOutClickListener);
        txtTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void showBusDetailsScreen(Bus bus) {

        Intent intent = new Intent(this, BusDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_BUS, bus);
        startActivity(intent);
    }

    private void showBookRideScreen(Bus bus) {

        Intent intent = new Intent(this, BookRideActivity.class);
        intent.putExtra(Constants.EXTRA_BUS, bus);
        startActivityForResult(intent, 1);
    }

    private void showList(boolean show) {
        txtErrorMsg.setText(getString(R.string.err_no_data_found));
        txtErrorMsg.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void populateData(BusResponse busResponse) {

        if (busResponse == null || busResponse.getData() == null) {
            showList(false);
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(verticalSpacingItemDecoration);
        recyclerView.addItemDecoration(spaceItemDecoration);
        BusAdapter adapter = new BusAdapter(this, busResponse.getData(), this);

        showList(true);
        recyclerView.setAdapter(adapter);
    }

    private void fetchData() {

        getBusList();
        //TODO getCurrentBookings();
    }

    private void getBusList() {

        String auth = AppConfig.getInstance().getBase6Authorization();
        ApiClient.getClient().create(Services.class).getBusList(auth).enqueue(new WebClientCallBack<BusResponse>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (!(response instanceof BusResponse) || ((BusResponse) response).getData() == null) {
                    showList(false);
                    Utils.showToast(getApplicationContext(), ((BusResponse) response).getError());
                    return;
                }

                populateData((BusResponse) response);
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(BusListActivity.this, errorMessage);
            }
        }, true));
    }

    private void getCurrentBookings() {

        String auth = AppConfig.getInstance().getBase6Authorization();
        ApiClient.getClient().create(Services.class).getCurrentBookings(auth).enqueue(new WebClientCallBack<BusResponse>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (!(response instanceof BusResponse) || ((BusResponse) response).getData() == null) {
                    showList(false);
                    Utils.showToast(getApplicationContext(), ((BusResponse) response).getError());
                    return;
                }

                populateData((BusResponse) response);
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(BusListActivity.this, errorMessage);
            }
        }, true));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        AppConfig.getInstance().isAppRunning(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppConfig.getInstance().isAppRunning(false);
    }
}