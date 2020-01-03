package com.tejani.pehlaj.chairlift.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.constants.Constants;
import com.tejani.pehlaj.chairlift.constants.KeyConstants;
import com.tejani.pehlaj.chairlift.entities.Booking;
import com.tejani.pehlaj.chairlift.service.LocationFetcherService;
import com.tejani.pehlaj.chairlift.utils.JsonUtility;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;
import com.tejani.pehlaj.chairlift.utils.Utils;

import org.json.JSONException;
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

    public Timer timer = new Timer();
    private GoogleMap mMap;
    private Polyline line;

    private Marker busMarker;
    private Marker userMarker;
    private Marker pickupMarker;
    private Marker dropOffMarker;

    private io.socket.client.Socket socket;

    private Booking booking;

    private String userName;
    private LatLng pickupLocation;
    private LatLng dropOffLocation;

    private LatLng busLocation;
    private LatLng userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userName = PreferenceUtility.getString(this, Utils.USERNAME, "USER");
        double lat = Double.parseDouble(PreferenceUtility.getString(this, KeyConstants.KEY_LAT, "67.0354019"));
        double lng = Double.parseDouble(PreferenceUtility.getString(this, KeyConstants.KEY_LNG, "24.8500037"));
        userLocation = new LatLng(lat, lng);

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_BOOKING)) {
            return;
        }

        booking = getIntent().getParcelableExtra(Constants.EXTRA_BOOKING);
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
            opts.path = "/api/v1/bus";
            opts.transports = new String[]{WebSocket.NAME};

            socket = IO.socket("http://172.16.17.242:4001/", opts);

            socket.connect();

            socket.on("connect", connectListener)
                    .on(Socket.EVENT_OPEN, openEventListener)
                    .on(Socket.EVENT_DATA, dataListener)
                    .on(Socket.EVENT_ERROR, errorListener)
                    .on(Socket.EVENT_TRANSPORT, transportListener)
                    .on(Socket.EVENT_CLOSE, closeEventListener)
                    .on(Socket.EVENT_PACKET, packetListener);

            socket.io().on(Socket.EVENT_ERROR, iOErrorListener)
                    .on(Socket.EVENT_CLOSE, iOCloseEventListener);

            socket.emit("event", "hi");
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
                        double lat = data.getDouble("latitude");
                        double lng = data.getDouble("longitude");
                        Log.e("Location", lat + ", " + lng);
                        final PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);
                        final LatLng latLng = new LatLng(lat, lng);
                        options.add(latLng);
                        options.add(dropOffLocation);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (line != null) {
                                    line.remove();
                                }
                                line = mMap.addPolyline(options);
                                if(busMarker != null) {
                                    busMarker.remove();
                                }
                                busMarker = addMarker(latLng, getString(R.string.app_name));
                                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 12);
                                mMap.animateCamera(location);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (args[0] instanceof String) {
                    String data = (String) args[0];
                    Log.e("Socket IO Data", data);
                }
            }
        }

    };

    private Emitter.Listener openEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "openEventListener");
        }

    };

    private Emitter.Listener closeEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "closeEventListener");
        }

    };

    private Emitter.Listener packetListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "packetListener");
        }

    };

    private Emitter.Listener transportListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "transportListener");
        }

    };

    private Emitter.Listener connectListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "connectListener");

            timer.purge();
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    //socket.io().emit("event", "{\"content\": \"Test Message\"");
                    if (socket.connected()) {
                        socket.emit("event", "{\"content\": \"Message sent from AndroidApp using Socket.IO\"");
                    }
                }
            }, 1000, 10000);
        }

    };

    private Emitter.Listener errorListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "errorListener");
        }

    };

    private Emitter.Listener iOCloseEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOCloseEventListener");
        }

    };

    private Emitter.Listener iOErrorListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOErrorListener");
        }

    };

    @Override
    protected void onStop() {
        super.onStop();

        socket.disconnect();
        //socket.off("event", onNewMessage);
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

        mMap = googleMap;
        postMapReady();
    }

    @AfterPermissionGranted(1)
    private void postMapReady() {

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

    @Override
    public void locationFetched(Location mLocal, Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);

        userLocation = new LatLng(mLocal.getLatitude(), mLocal.getLongitude());
        /*if (userMarker != null) {
            userMarker.remove();
        }
        userMarker = addMarker(userLocation, userName);*/
        PreferenceUtility.setString(this, KeyConstants.KEY_LAT, String.valueOf(userLocation.latitude));
        PreferenceUtility.setString(this, KeyConstants.KEY_LNG, String.valueOf(userLocation.longitude));

        //CameraUpdate location = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
        //mMap.animateCamera(location);

        //drawRoute();
    }

    private void drawRoute(String jsonData) {

        //String json = "[{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5162792,\"longitude\":13.3795345},{\"latitude\":52.5163651,\"longitude\":13.3808541},{\"latitude\":52.5180817,\"longitude\":13.3804464},{\"latitude\":52.5189292,\"longitude\":13.3802962},{\"latitude\":52.5206638,\"longitude\":13.3861149}]";
        String json = "[{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162792,\"longitude\":13.3795345},{\"latitude\":52.5163651,\"longitude\":13.3808541},{\"latitude\":52.5180817,\"longitude\":13.3804464},{\"latitude\":52.5189292,\"longitude\":13.3802962}]";

        //JsonArray list = new Gson().fromJson(json, JsonArray.class);

        JsonArray list = JsonUtility.parse(jsonData).getAsJsonArray();
        PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);
        for (int z = 0; z < list.size(); z++) {
            JsonObject jsonObject = list.get(z).getAsJsonObject();
            double lat = JsonUtility.getDouble(jsonObject, "latitude", 0);
            double lng = JsonUtility.getDouble(jsonObject, "longitude", 0);
            LatLng latLng = new LatLng(lat, lng);
            options.add(latLng);
        }
        line = mMap.addPolyline(options);

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(options.getPoints().get(0), 15);
        mMap.animateCamera(location);
    }

    private Marker addMarker(LatLng latLng, String label) {

        if (latLng == null) {
            return null;
        }

        // Add a marker and move the camera

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(label));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        return marker;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        postMapReady();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
