package com.upiita.witcom2016;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.events.EventActivity;
import com.upiita.witcom2016.pager.WitcomPagerActivity;
import com.upiita.witcom2016.rate.RateActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;



public class WitcomLogoActivity extends AppCompatActivity {

    public static String URL_BASE = "https://host-test-b1ab8.firebaseapp.com";
    public static String CONTENT_VERSION = "0";
    public static String URL_STREAM = "rtmp://148.204.86.75:1935/envivo.upiita/livestream";
    public static String URL_STREAM_LQ = "rtmp://148.204.86.75:1935/envivo.upiita.lq/livestream";
    public static String IMAGE_DEFAULT = "/images/images/default.png";
    private boolean currentEvent = false;

    GoogleApiClient googleApiClient = null;
    public static final String TAG = "WitcomLogoActivity";
    public static final String GEOFENCE_ID = "GeofenceID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(getApplicationContext());
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_witcom_logo);

        //getTables();
        SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = db.rawQuery("SELECT * FROM event", null);
        currentEvent = fila.getCount()>0;
        if (fila.moveToFirst()) {
            do {
                Cursor fila2 = db.rawQuery("SELECT image FROM images where id=" + fila.getString(4), null);
                if(fila2.moveToFirst())
                    ((ImageView)findViewById(R.id.imagelogo)).setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila2.getBlob(0))));
                ((TextView)findViewById(R.id.textlogo)).setText(fila.getString(5));
                fila2.close();
            } while (fila.moveToNext());
        }

        fila.close();
        db.close();


        if (android.os.Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(TAG, "Connected to GoogleApiClient");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(TAG, "Suspended connection to GoogleApiClient");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "Failed to connect to GoogleApiClient - " + connectionResult.getErrorMessage());
                    }
                })
                .build();
        starGeofenceMonitoring();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (getIntent().getStringExtra("conference") != null) {
                    Intent intent = new Intent(WitcomLogoActivity.this, RateActivity.class);
                    intent.putExtra("conference", getIntent().getStringExtra("conference"));
                    startActivity(intent);
                } else {
                    if(currentEvent)
                        startActivity(new Intent(getApplicationContext(), WitcomPagerActivity.class));
                    else
                        startActivity(new Intent(getApplicationContext(), EventActivity.class));
                }
                //startActivity(new Intent(getApplicationContext(), StreamingActivity.class));
            }
        }, 1680);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Services not available");
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        } else {
            Log.d(TAG, "Google Play Services not available - no action is required");
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        super.onStart();
        googleApiClient.reconnect();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop called");
        super.onStop();
        //googleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }


    private void startLocationMonitoring() {
        Log.d(TAG, "startLocation called");
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10000)
                    .setFastestInterval(5000)
                    //.setNumUpdates(5)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "Location update lat/long " + location.getLatitude() + " " + location.getLongitude());
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "Security exception - " + e.getMessage());
        }
    }

    private void starGeofenceMonitoring() {
        Log.d(TAG, "startMonitoring called");

        try {
            //googleApiClient.connect();
            ArrayList<Coordinate> coordinates;


            Geofence geofence = new Geofence.Builder()
                    .setRequestId(GEOFENCE_ID)
                    .setCircularRegion(19.494886, -99.119906, 1000)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setNotificationResponsiveness(1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build();

            Intent intent = new Intent(this, GeofenceService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(!googleApiClient.isConnected()) {
                Log.d(TAG, "GoogleApiClient is not connected");
            } else {
                LocationServices.GeofencingApi.addGeofences(googleApiClient, geofencingRequest, pendingIntent)
                        .setResultCallback(new ResultCallbacks<Status>() {
                            @Override
                            public void onSuccess(@NonNull Status status) {
                                Log.d(TAG, "Successfully added geofence");
                            }

                            @Override
                            public void onFailure(@NonNull Status status) {
                                Log.d(TAG, "Failed to add geofence - " + status.getStatus());
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "Securty exception - " + e.getMessage());
        }
    }

    private void stopGeofenceMonitoring() {
        Log.d(TAG, "stopMonitoring Called");
        ArrayList<String> geofencesIds = new ArrayList<String>();
        geofencesIds.add(GEOFENCE_ID);
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofencesIds);
    }

}
