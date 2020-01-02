package com.tejani.pehlaj.chairlift.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.engineio.client.transports.WebSocket;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tejani.pehlaj.chairlift.R;
import com.tejani.pehlaj.chairlift.constants.KeyConstants;
import com.tejani.pehlaj.chairlift.service.LocationFetcherService;
import com.tejani.pehlaj.chairlift.utils.JsonUtility;
import com.tejani.pehlaj.chairlift.utils.PreferenceUtility;
import com.tejani.pehlaj.chairlift.utils.Utils;

import org.json.JSONArray;
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
import io.socket.parser.Packet;
import meetmehdi.location.BaseActivityLocation;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends BaseActivityLocation implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    public Timer timer = new Timer();
    private GoogleMap mMap;
    private Polyline line;

    private io.socket.client.Socket socket;

    private String userName;
    private LatLng userLocation;

    private JsonArray busRoute = new JsonArray();

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("Socket.io", "" + args[0]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String) args[0];
//                    String username;
//                    String message;
//                    try {
//                        username = data.getString("username");
//                        message = data.getString("message");
//                    } catch (JSONException e) {
//                        return;
//                    }

                    // add the message to view
                    //Log.e("Socket.io", "" + data);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userName = PreferenceUtility.getString(this, Utils.USERNAME, "USER");
        float lat = PreferenceUtility.getFloat(this, "LAT", 0);
        float lng = PreferenceUtility.getFloat(this, "LNG", 0);
        userLocation = new LatLng(lat, lng);
        //setupSecketConnection();

        setupSocketIO ();

    }

    private void setupSocketIO () {
        try {
            IO.Options opts = new IO.Options();

            opts.secure = false;
            opts.path = "/api/v1/bus";
            opts.transports = new String[]{WebSocket.NAME};

            socket = IO.socket("http://172.16.17.242:4001/", opts);

            socket.connect();

            socket.on("connect", handshakeListener)
                    .on(Socket.EVENT_OPEN, openEventListener)
                    .on(Socket.EVENT_DATA, dataListener)
                    /*.on("event", eventListener)*/
                    .on(Socket.EVENT_ERROR, errorListener)
                    /*.on(Socket.EVENT_HANDSHAKE, handshakeListener)*/
                    .on(Socket.EVENT_TRANSPORT, transportListener)
                    /*.on(Socket.EVENT_MESSAGE, messageListener)*/
                    .on(Socket.EVENT_CLOSE, closeEventListener)
                    .on(Socket.EVENT_PACKET, packetListener)
                    /*.on(Socket.EVENT_PING, pingEventListener)*/;

            socket.io()/*.on(Socket.EVENT_OPEN, iOOpenEventListener)*/
                    .on(Socket.EVENT_DATA, iODataListener)
                    .on("event", iOEventListener)
                    .on(Socket.EVENT_ERROR, iOErrorListener)
                    /*.on(Socket.EVENT_HANDSHAKE, iOHandshakeListener)*/
                    /*.on(Socket.EVENT_TRANSPORT, iOTransportListener)*/
                    .on(Socket.EVENT_MESSAGE, iOMessageListener)
                    .on(Socket.EVENT_CLOSE, iOCloseEventListener)
                    /*.on(Socket.EVENT_PACKET, iOPacketListener)*/
                    /*.on(Socket.EVENT_PING, iOPingEventListener)*/;

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
                        final PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                        final LatLng latLng = new LatLng(lat, lng);
                        options.add(latLng);
                        options.add(userLocation);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (line != null) {
                                    line.remove();
                                }
                                line = mMap.addPolyline(options);
                                addMarker(latLng, getString(R.string.app_name));
                                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(options.getPoints().get(0), 15);
                                mMap.animateCamera(location);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (args[0] instanceof String) {
                    String data = (String) args[0];
                    Log.e("Socket IO Data", data);
                }
            }
        }

    };

    private Emitter.Listener pingEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "pingEventListener");
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

    private Emitter.Listener eventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "eventListener");
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

    private Emitter.Listener handshakeListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "handshakeListener");

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

    private Emitter.Listener messageListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "messageListener");
        }

    };

    private Emitter.Listener iODataListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iODataListener");
        }

    };

    private Emitter.Listener iOPingEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOPingEventListener");
        }

    };

    private Emitter.Listener iOOpenEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOOpenEventListener");
        }

    };

    private Emitter.Listener iOCloseEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOCloseEventListener");
        }

    };

    private Emitter.Listener iOEventListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOEventListener");
        }

    };

    private Emitter.Listener iOPacketListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOPacketListener");

            if (args != null && args.length > 0) {
                if (args[0] instanceof Packet) {
                    Packet packet = (Packet) args[0];
                    if (packet.data instanceof JSONArray) {
                        JSONArray data = (JSONArray) packet.data;
                        Log.e("Socket IO Data", data.toString());
                        if (data.length() > 0) {
                            try {
                                Log.e("Socket IO Data", data.get(0).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    };

    private Emitter.Listener iOTransportListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOTransportListener");
        }

    };

    private Emitter.Listener iOHandshakeListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOHandshakeListener");
        }

    };

    private Emitter.Listener iOErrorListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOErrorListener");
        }

    };

    private Emitter.Listener iOMessageListener = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.e("Socket.io", "iOMessageListener");
        }

    };

    private void setupSecketConnection() {
        try {


            IO.Options opts = new IO.Options();
            opts.secure = false;
            opts.path = "/api/v1/bus";
            opts.transports = new String[]{WebSocket.NAME};

            socket = IO.socket("http://172.16.17.242:4003/", opts);

            //socket = IO.socket("http://172.16.17.242:4003/api/v1/bus/", );

            //socket.on("info", onNewMessage);
            //socket.on("event", onNewMessage);
            //socket.on("event", onNewMessage);
            //socket.io().on("/", onNewMessage);
            //socket.io().on("info", onNewMessage);
            //socket.io().on("event", onNewMessage);
            socket.connect();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    //socket.io().emit("event", "{\"content\": \"Test Message\"");
                    if (socket.connected()) {
                        //socket.emit("event", "{\"content\": \"Message sent from AndroidApp using Socket.IO\"");
                    }
                }
            }, 5000, 30000);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

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

        if (mMap == null) {
            return;
        }

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            float lat = PreferenceUtility.getFloat(this, "LAT", 0.0f);
            float lng = PreferenceUtility.getFloat(this, "LNG", 0.0f);

            if (lat != 0.0f) {
                LatLng latLng = new LatLng(lat, lat);
                addMarker(latLng, "Test");
            }

            initLocationFetching(this);

            //drawRoute ();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_required), 1, perms);
        }
    }

    @Override
    public void locationFetched(Location mLocal, Location oldLocation, String time, String locationProvider) {
        super.locationFetched(mLocal, oldLocation, time, locationProvider);

        //Toast.makeText(getApplication(), "Latitude : " + mLocal.getLatitude() + " Longitude : " + mLocal.getLongitude(), Toast.LENGTH_SHORT).show();
        userLocation = new LatLng(mLocal.getLatitude(), mLocal.getLongitude());
        addMarker(userLocation, userName);
        PreferenceUtility.setFloat(this, "LAT", (float) mLocal.getLatitude());
        PreferenceUtility.setFloat(this, "LNG", (float) mLocal.getLongitude());

        //CameraUpdate location = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
        //mMap.animateCamera(location);


        //drawRoute();
    }

    private void drawRoute(String jsonData) {

        //String json = "[{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5162792,\"longitude\":13.3795345},{\"latitude\":52.5163651,\"longitude\":13.3808541},{\"latitude\":52.5180817,\"longitude\":13.3804464},{\"latitude\":52.5189292,\"longitude\":13.3802962},{\"latitude\":52.5206638,\"longitude\":13.3861149}]";
        String json = "[{\"latitude\":52.5162041,\"longitude\":13.378365},{\"latitude\":52.5159999,\"longitude\":13.3778999},{\"latitude\":52.5206638,\"longitude\":13.3861149},{\"latitude\":52.5205999,\"longitude\":13.3861999},{\"latitude\":52.5162792,\"longitude\":13.3795345},{\"latitude\":52.5163651,\"longitude\":13.3808541},{\"latitude\":52.5180817,\"longitude\":13.3804464},{\"latitude\":52.5189292,\"longitude\":13.3802962}]";

        //JsonArray list = new Gson().fromJson(json, JsonArray.class);

        JsonArray list = JsonUtility.parse(jsonData).getAsJsonArray();
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
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

    private void addMarker(LatLng latLng, String label) {

        if (latLng == null) {
            return;
        }

        // Add a marker and move the camera

        mMap.addMarker(new MarkerOptions().position(latLng).title(label));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        postMapReady();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}