package com.tejani.pehlaj.chairlift.activities;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

import meetmehdi.location.BaseActivityLocation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends BaseActivityLocation implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager ()
                .findFragmentById (R.id.map);
        mapFragment.getMapAsync (this);

    }

    protected void onStart() {
        super.onStart();
        /*if you want fetch in a service make a background thread*/

//        Intent serviceIntent = new Intent(this, LocationFetcherService.class);
////        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if you want fetch in a service make a background thread*/
//
//        Intent serviceIntent = new Intent(this, LocationFetcherService.class);
//        stopService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady (GoogleMap googleMap) {

        mMap = googleMap;
        postMapReady ();
    }

    @AfterPermissionGranted(1)
    private void postMapReady () {

        if (mMap == null) {
            return;
        }

        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };

        if (EasyPermissions.hasPermissions(this, perms)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMapType (GoogleMap.MAP_TYPE_NORMAL);

            float lat = PreferenceUtility.getFloat (this, "LAT", 0.0f);
            float lng = PreferenceUtility.getFloat (this, "LNG", 0.0f);

            if (lat != 0.0f) {
                LatLng latLng = new LatLng (lat, lat);
                addMarker (latLng, "Test");
            }

            initLocationFetching(this);
        }
        else {
            EasyPermissions.requestPermissions (this, getString (R.string.permission_required), 1, perms);
        }
    }

    @Override
    public void locationFetched(Location mLocal, Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);

        //Toast.makeText(getApplication(), "Latitude : " + mLocal.getLatitude() + " Longitude : " + mLocal.getLongitude(), Toast.LENGTH_SHORT).show();
        LatLng latLng = new LatLng (mLocal.getLatitude (), mLocal.getLongitude ());
        addMarker (latLng, time);
        PreferenceUtility.putFloat (this, "LAT", (float) mLocal.getLatitude ());
        PreferenceUtility.putFloat (this, "LNG", (float) mLocal.getLongitude ());

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(location);
    }

    private void addMarker (LatLng latLng, String label) {

        if (latLng == null) {
            return;
        }

        // Add a marker and move the camera

        mMap.addMarker (new MarkerOptions ().position (latLng).title (label));
        mMap.moveCamera (CameraUpdateFactory.newLatLng (latLng));
    }

    @Override
    public void onPermissionsGranted (int requestCode, @NonNull List<String> perms) {

        postMapReady ();
    }

    @Override
    public void onPermissionsDenied (int requestCode, @NonNull List<String> perms) {

    }
}
