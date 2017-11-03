package com.upiita.witcom2016.pager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.upiita.witcom2016.BuildConfig;
import com.upiita.witcom2016.Constants;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.indicator.IndicatorTouch;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

/**
 * Created by oscar on 27/09/16.
 */

public class WitcomPagerActivity extends WitcomBaseActivity {

    Dialog dialog;
    private FancyShowCaseQueue showCaseQueue = new FancyShowCaseQueue();
    private FancyShowCaseView caseViewIndicator, caseViewPager, caseViewUpdate;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pager);

        eventCode = getIntent().getStringExtra("eventCode");

        //Toast.makeText(this, "Main Activity: " + eventCode, Toast.LENGTH_SHORT).show();

        mAdapter = new WitcomFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);

        mIndicator = (IndicatorTouch) findViewById(R.id.indicator_test);
        mIndicator.setViewPager(mPager);

        mIndicator.setCurrentItem(getIntent().getIntExtra("page",1)-1);

        tvUpdate = (TextView) findViewById(R.id.tv_update);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchDiscount();

        //WitcomLogoActivity.URL_BASE = firebaseRemoteConfig.getString("url_witcom");
        //Toast.makeText(this, firebaseRemoteConfig.getString("url_witcom"), Toast.LENGTH_SHORT).show();

        //swipeToast();
        checkUpdate();

        /*dialog = new Dialog(this, R.style.Theme_Dialog_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating);

        ((Button) dialog.findViewById(R.id.rating_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WitcomPagerActivity.this, "OK", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.rating_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WitcomPagerActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();*/
        caseViewIndicator = new FancyShowCaseView.Builder(this)
                .focusOn((View) findViewById(R.id.indicator_test))
                .title(getString(R.string.pagerIndicator))
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .showOnce("witcomPagerIndicator")
                .build();
        caseViewUpdate = new FancyShowCaseView.Builder(this)
                .focusOn((View) findViewById(R.id.tv_update))
                .title(getString(R.string.updateIndicator))
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .showOnce("witcomUpdateIndicator")
                .build();
        caseViewPager = new FancyShowCaseView.Builder(this)
                .focusOn((View) findViewById(R.id.pager_test))
                .title(getString(R.string.pagerAction))
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .showOnce("witcomPagerAction")
                .build();

        showCaseQueue.add(caseViewIndicator).add(caseViewUpdate).add(caseViewPager);
        showCaseQueue.show();



        //GEOCERCAS
        if (android.os.Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_REQUEST_CODE);
        }

        if(this.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_GEOFENCE_ACTIVE, Constants.IS_GEOFENCE)) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Log.d(TAG, "Connected to GoogleApiClient");
                            starGeofenceMonitoring(getLocations());
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
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void swipeToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.swipe_toast, (ViewGroup) findViewById(R.id.swipe_layout));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void checkUpdate() {
        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT info_version FROM version", null);
        if (fila.moveToFirst()) {
            if (!fila.getString(0).equals(firebaseRemoteConfig.getString("data_version"))) {
                //Actualizar
                Log.d("ACTUALIZAR", "POR FAVOR ACTUALIZA");
                askForUpdate();
            }
        } else {
            askForUpdate();
        }
        fila.close();
        bd.close();
    }

    private void askForUpdate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(getString(R.string.update_request))
                .setMessage(getString(R.string.update_message))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        //GEOFENCE
        if(this.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_GEOFENCE_ACTIVE, Constants.IS_GEOFENCE)) {
            int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if(response != ConnectionResult.SUCCESS) {
                Log.d(TAG, "Google Play Services not available");
                GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
            } else {
                Log.d(TAG, "Google Play Services available - no action is required");
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        super.onStart();
        if(this.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_GEOFENCE_ACTIVE, Constants.IS_GEOFENCE)) {
            googleApiClient.reconnect();
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop called");
        super.onStop();
        //googleApiClient.disconnect();
    }

}
