package com.tejani.pehlaj.chairlift.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.constants.Constants;
import com.tejani.pehlaj.chairlift.fragments.BookingFragment;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;
import com.tejani.pehlaj.chairlift.utils.Utils;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView imgSearch;
    private ImageView imgSignOut;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private float lastTranslate;

    private boolean shouldExit;

    public static BookingFragment bookingFragment;

    @Override
    public void onBackPressed() {

        if (shouldExit) {
            super.onBackPressed();
            return;
        }

        Utils.showToast(this, getString(R.string.msg_back_press));

        shouldExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldExit = false;
            }
        }, Constants.BACK_PRESS_DELAY_MILLIS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        setupToolbar();

        setupNavigationDrawer();

        setListeners();
    }

    private void showProfileScreen() {


    }

    private void showBusList() {

        Intent intent = new Intent(this, BusListActivity.class);
        startActivity(intent);
    }

    private void showLoginScreen() {

        PreferenceUtility.setBoolean(this, Utils.LOGIN_STATUS, false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showSearchOptions() {

        if (bookingFragment == null) {
            return;
        }

        bookingFragment.toggleSearchOptions(true);
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float navigationHeaderWidth = navigationView.getWidth();
                float moveFactor = (navigationHeaderWidth * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    toolbar.setTranslationX(moveFactor);
                    frameLayout.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    toolbar.startAnimation(anim);
                    frameLayout.startAnimation(anim);
                    lastTranslate = moveFactor;
                }
            }

        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);

                int itemID = item.getItemId();

                imgSearch.setVisibility(itemID == R.id.nav_bus_list ? View.VISIBLE : View.GONE);
                if (itemID == R.id.nav_home) {

                } else if (itemID == R.id.nav_book_ride) {
                    showBusList();
                } else if (itemID == R.id.nav_bus_list) {
                    showBusList();
                } else if (itemID == R.id.nav_profile) {
                    showProfileScreen();
                } else if (itemID == R.id.nav_sign_out) {
                    showLoginScreen();
                }
                return false;
            }
        });
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        imgSearch = findViewById(R.id.imgSearch);

        TextView txtTitle = findViewById(R.id.txtTitle);
        if (txtTitle != null) {
            final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
            int actionBarSize = (int) styledAttributes.getDimension(0, 0);
            txtTitle.setTextColor(Color.WHITE);
            txtTitle.setTranslationX(-1 * (actionBarSize - (actionBarSize / 3)));
        }

        imgSignOut = findViewById(R.id.imgSignOut);
        frameLayout = findViewById(R.id.frameLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        bookingFragment = BookingFragment.newInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, bookingFragment);
        ft.commit();
    }

    private void setListeners() {

        imgSearch.setOnClickListener(searchClickListener);
        imgSignOut.setOnClickListener(signOutClickListener);
    }

    private final View.OnClickListener searchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showSearchOptions();
        }
    };

    private final View.OnClickListener signOutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            showLoginScreen();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    }
}
