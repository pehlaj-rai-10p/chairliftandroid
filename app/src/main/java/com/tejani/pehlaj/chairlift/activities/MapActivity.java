package com.tejani.pehlaj.chairlift.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.config.AppConfig;
import com.tejani.pehlaj.chairlift.constants.Constants;
import com.tejani.pehlaj.chairlift.constants.EnvironmentConstants;
import com.tejani.pehlaj.chairlift.constants.KeyConstants;
import com.tejani.pehlaj.chairlift.entities.Booking;
import com.tejani.pehlaj.chairlift.entities.Bus;
import com.tejani.pehlaj.chairlift.entities.BusDetails;
import com.tejani.pehlaj.chairlift.entities.Location;
import com.tejani.pehlaj.chairlift.interfaces.Services;
import com.tejani.pehlaj.chairlift.interfaces.WebServiceCallBack;
import com.tejani.pehlaj.chairlift.network.ApiClient;
import com.tejani.pehlaj.chairlift.network.WebClientCallBack;
import com.tejani.pehlaj.chairlift.service.LocationFetcherService;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;
import com.tejani.pehlaj.chairlift.utils.Utils;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import meetmehdi.location.BaseActivityLocation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends BaseActivityLocation implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private static final String API_BUS_TRACK = "/api/v1/bus/track";
    private static final String CONNECT = "connect";
    private static final String EVENT = "event";

    private GoogleMap mMap;
    private Polyline line;
    private Polyline line1;
    private Polyline line2;

    private Marker busMarker;
    private Marker userMarker;
    private Marker pickupMarker;
    private Marker dropOffMarker;

    private io.socket.client.Socket socket;

    private Bus bus;
    private Booking booking;

    private LatLng pickupLocation;
    private LatLng dropOffLocation;

    private LatLng userLocation;

    private boolean isMapReady = false;

    private int counter = 0;
    private int busTracker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        double lat = Double.parseDouble(PreferenceUtility.getString(this, KeyConstants.KEY_LAT, "67.0354019"));
        double lng = Double.parseDouble(PreferenceUtility.getString(this, KeyConstants.KEY_LNG, "24.8500037"));
        userLocation = new LatLng(lat, lng);

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_BOOKING)) {
            return;
        }

        booking = getIntent().getParcelableExtra(Constants.EXTRA_BOOKING);

        getBusDetails(booking.getBusId());
    }

    private void getBusDetails(int busId) {

        String auth = AppConfig.getInstance().getBase6Authorization();
        ApiClient.getClient().create(Services.class).getBusDetails(auth, busId).enqueue(new WebClientCallBack<BusDetails>(this, new WebServiceCallBack() {
            @Override
            public void onSuccess(Object response) {

                if (!(response instanceof BusDetails) || ((BusDetails) response).getData() == null) {
                    Utils.showToast(getApplicationContext(), ((BusDetails) response).getError());
                    return;
                }

                bus = ((BusDetails) response).getData();

                drawBusRoute(bus);
                postMapReady();
                //TODO trackBusLocation(bus);
            }

            @Override
            public void onFailure(String errorMessage) {
                Utils.showToast(MapActivity.this, errorMessage);
            }
        }, true));
    }

    private void drawBusRoute(Bus bus) {

        if (bus == null || bus.getRoute() == null || bus.getRoute().size() == 0) {
            return;
        }

        final PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

        if (pickupLocation != null) {
            options.add(pickupLocation);
        }

        for (int i = 0; i < bus.getRoute().size(); i++) {

            Location data = bus.getRoute().get(i);
            LatLng latLng = new LatLng(data.getLat(), data.getLat());
            options.add(latLng);
        }

        if (dropOffLocation != null) {
            options.add(dropOffLocation);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearRoute();
                line = mMap.addPolyline(options);
            }
        });
    }

    private void populateBookingDetails(Booking booking) {

        if (booking == null || booking.getPickupLocation() == null || booking.getDropoffLocation() == null) {
            return;
        }

        pickupLocation = new LatLng(booking.getPickupLocation().getLat(), booking.getPickupLocation().getLng());
        dropOffLocation = new LatLng(booking.getDropoffLocation().getLat(), booking.getDropoffLocation().getLng());

        final PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        options.add(pickupLocation);
        options.add(dropOffLocation);
        //mMap.addPolyline(options);
        pickupMarker = addMarker(pickupLocation, getString(R.string.pickup));
        dropOffMarker = addMarker(dropOffLocation, getString(R.string.dropoff));
    }

    private void setupSocketIO() {
        try {
            IO.Options opts = new IO.Options();

            opts.secure = false;
            opts.path = API_BUS_TRACK;
            opts.transports = new String[]{WebSocket.NAME};

            socket = IO.socket(EnvironmentConstants.SOCKET_IO_URL_AWS_SERVER, opts);

            socket.connect();

            socket.on(CONNECT, connectListener)
                    .on(Socket.EVENT_DATA, dataListener)
                    .on(EVENT, eventListener);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener dataListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "Listener");
            if (args != null && args.length > 0) {

                if (args[0] instanceof JSONObject) {
                    JSONObject json = (JSONObject) args[0];
                    try {
                        JSONObject data = json.getJSONObject(KeyConstants.KEY_DATA);
                        double lat = data.getDouble("lat");
                        double lng = data.getDouble("lng");

                        int busLocator = 0;
                        if (bus != null && bus.getRoute() != null && bus.getRoute().size() > 0) {
                            for (int i = 0; i < bus.getRoute().size(); i++) {
                                Location location = bus.getRoute().get(i);
                                if (location.has(lat, lng)) {
                                    busLocator = i;
                                    break;
                                }
                            }
                        }

                        showBusLocation(busLocator, bus);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (args[0] instanceof String) {
                    String data = (String) args[0];
                    Log.e("Socket IO Data", data);
                }
            }
        }

    };

    // region Socket Listeners

    private Emitter.Listener connectListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "connectListener");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (socket.connected()) {

                        socket.emit(EVENT, bus.getId(), booking.getId());
                    }
                }
            }, 2000);
        }

    };

    private Emitter.Listener eventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "errorListener");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MapActivity.this, "Ride Completed", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
            });
        }

    };

    //endregion

    private void clearRoute() {

        if (line != null) {
            line.remove();
        }
    }

    private void clearTracks() {
        if (line1 != null) {
            line1.remove();
        }
        if (line2 != null) {
            line2.remove();
        }
        if (busMarker != null) {
            busMarker.remove();
        }
    }

    @AfterPermissionGranted(1)
    private void postMapReady() {

        if (!isMapReady || bus == null) {
            return;
        }

        //trackBusLocation(bus);

        setupSocketIO();

        if (mMap == null) {
            return;
        }

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            initLocationFetching(this);

            //drawRoute ();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_required), 1, perms);
        }

        populateBookingDetails(booking);
    }

    private void trackBusLocation(final Bus bus) {

        if (bus == null) {
            return;
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (bus == null || bus.getRoute() == null || bus.getRoute().size() == 0) {
                    return;
                }

                showBusLocation(busTracker, bus);

                busTracker++;


                if (busTracker >= bus.getRoute().size()) {
                    busTracker = bus.getRoute().size() - 1;
                }
            }
        }, 1000, 5000);
    }

    private void showBusLocation(int busTracker, Bus bus) {

        if (bus == null || bus.getRoute() == null || bus.getRoute().size() == 0) {
            return;
        }

        final PolylineOptions routeCovered = new PolylineOptions().width(10).color(Color.GRAY).geodesic(true);
        final PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);

        routeCovered.add(pickupLocation);

        LatLng busLatLng = new LatLng(0, 0);

        for (int i = 0; i < bus.getRoute().size(); i++) {

            Location location = bus.getRoute().get(i);
            LatLng latLng = new LatLng(location.getLat(), location.getLng());
            if (i == busTracker) {
                routeCovered.add(latLng);
                options.add(latLng);
                busLatLng = latLng;
            } else if (i < busTracker) {
                routeCovered.add(latLng);
            } else {
                options.add(latLng);
            }
        }

        options.add(dropOffLocation);

        final LatLng tempLatLng = busLatLng;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearTracks();
                line1 = mMap.addPolyline(options);
                line2 = mMap.addPolyline(routeCovered);
                busMarker = addMarker(tempLatLng, getString(R.string.app_name));
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(tempLatLng, 13);
                mMap.animateCamera(location);
            }
        });
    }

    private Marker addMarker(LatLng latLng, String label) {

        if (latLng == null) {
            return null;
        }

        // Add a marker and move the camera

        return mMap.addMarker(new MarkerOptions().position(latLng).title(label));
    }

    @Override
    public void locationFetched(android.location.Location mLocal, android.location.Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);

        userLocation = new LatLng(mLocal.getLatitude(), mLocal.getLongitude());
        PreferenceUtility.setString(this, KeyConstants.KEY_LAT, String.valueOf(userLocation.latitude));
        PreferenceUtility.setString(this, KeyConstants.KEY_LNG, String.valueOf(userLocation.longitude));
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        postMapReady();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (socket != null) {
            socket.off(Socket.EVENT_DATA, dataListener);
            socket.disconnect();
        }
    }

    protected void onStart() {
        super.onStart();
        /*if you want fetch in a service make a background thread*/

        Intent serviceIntent = new Intent(this, LocationFetcherService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if you want fetch in a service make a background thread*/

        Intent serviceIntent = new Intent(this, LocationFetcherService.class);
        stopService(serviceIntent);
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
    public void onMapReady(GoogleMap googleMap) {

        isMapReady = true;
        mMap = googleMap;
        postMapReady();
    }

}
