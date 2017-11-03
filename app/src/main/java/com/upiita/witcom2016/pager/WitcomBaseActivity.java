package com.upiita.witcom2016.pager;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upiita.witcom2016.Constants;
import com.upiita.witcom2016.GeofenceService;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.RegionGeofence;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.Controller;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.upiita.witcom2016.WitcomLogoActivity.URL_BASE;

/**
 * Created by oscar on 26/09/16.
 */

public class WitcomBaseActivity extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    public static String accent = "#FF9A22";
    public static String textWhite = "#EFEFEF";
    public static String blue = "#0F5DBE";
    public static String dark = "#1C1D26";
    private int requestPending;
    private ProgressDialog progressDia;
    private int total = 44;
    protected FirebaseRemoteConfig firebaseRemoteConfig;
    protected TextView tvUpdate;
    protected String eventCode;

    WitcomFragmentAdapter mAdapter;
    public static ViewPager mPager;
    PageIndicator mIndicator;

    //Geocercas
    GoogleApiClient googleApiClient = null;
    public static final String TAG = "WitcomBaseActivity";
    ////////////////////////////////////////////////////////

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.geofence);
        checkable.setChecked(this.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_GEOFENCE_ACTIVE, Constants.IS_GEOFENCE));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.about) {
            /*new AlertDialog.Builder(this)
                    .setView(R.layout.about)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();*/

            /*Intent notificationIntent = new Intent(getApplicationContext(), RateActivity.class);
            notificationIntent.putExtra("conference", "9");
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Resources res = getApplicationContext().getResources();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.witcomlogo)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.witcomlogo))
                    .setTicker("HOLA MUNDO")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Message")
                    .setContentText("LIBRE Y FELIZ");
            Notification n = builder.getNotification();

            n.defaults |= Notification.DEFAULT_ALL;
            nm.notify(0, n);*/

        if (id == R.id.update) {
            update();
        } else if(id == R.id.swapEvent) {
            clearEvent();
        } else if(id == R.id.geofence) {

            boolean isGeo = this.getPreferences(Context.MODE_PRIVATE).getBoolean(Constants.IS_GEOFENCE_ACTIVE, Constants.IS_GEOFENCE);

            item.setChecked(!isGeo);

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(Constants.IS_GEOFENCE_ACTIVE, !isGeo);
            editor.commit();

            if(isGeo) {
                stopGeofenceMonitoring();
                googleApiClient.disconnect();
            } else {
                googleApiClient.reconnect();
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
        }

        return super.onOptionsItemSelected(item);
    }

    protected void update () {
        if (checkInternetConnection()) {
            progressDia = new ProgressDialog(this);
            progressDia.setTitle(getString(R.string.updating));
            progressDia.setMessage(getString(R.string.wait));
            progressDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            getTables();
        } else {
            Log.d("INTERNETCONNECTION", "No hay conexión");
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void getTables() {
        final HashMap<String, ArrayList<String>> dataBase = new HashMap<>();
        final HashMap<String, String> getURLS = new HashMap<>();
        ArrayList<String> columns;
        requestPending = 0;

        getURLS.put("activity", Constants.GET_ACTIVITY);
        getURLS.put("activity_people", Constants.GET_ACTIVITY_PEOPLE);
        getURLS.put("activity_type", Constants.GET_ACTIVITY_TYPE);
        getURLS.put("chairs", Constants.GET_CHAIR);
        getURLS.put("developers", Constants.GET_DEVELOPER);
        getURLS.put("event", Constants.GET_EVENT);
        getURLS.put("people", Constants.GET_PEOPLE);
        getURLS.put("people_social_networks", "/peopleSocialNetworks/getPeopleSocialNetworks/");
        getURLS.put("place_category", Constants.GET_PLACE_CATEGORY);
        getURLS.put("place", Constants.GET_PLACE);
        getURLS.put("place_social_networks", "/placeSocialNetworks/getPlaceSocialNetworks/");
        getURLS.put("schedule", "/schedule/getSchedule/");
        getURLS.put("social_networks", "/socialNetworks/getSocialNetworks/");
        getURLS.put("sponsors", Constants.GET_SPONSOR);
        getURLS.put("streams", Constants.GET_STREAM);
        getURLS.put("sketch", Constants.GET_SKETCH);

        SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        for(String table: getURLS.keySet()){
            Cursor fila = db.rawQuery("SELECT * FROM "+ table, null);
            Log.d("COLUMNS FOR " + table, fila.getColumnNames().toString());
            columns = new ArrayList<>();
            for(String column: fila.getColumnNames()){
                columns.add(column);
            }
            dataBase.put(table, columns);
            fila.close();
        }
        db.close();

        //tvUpdate.setVisibility(View.INVISIBLE);

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        bd.execSQL("delete from version");
        bd = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("info_version", WitcomLogoActivity.CONTENT_VERSION);
        bd.insert("version", null, values);
        bd.close();

        WitcomLogoActivity.URL_BASE = firebaseRemoteConfig.getString("url_witcom");
        //Toast.makeText(this, WitcomLogoActivity.URL_BASE, Toast.LENGTH_SHORT).show();

        ////URL PRUEBA
        //WitcomLogoActivity.URL_BASE = "http://192.168.0.10:8080";
        //////////////

        progressDia.setMax(dataBase.size() + 1);
        progressDia.setProgress(0);
        progressDia.show();

        getImages();

        for (final String table: dataBase.keySet()) {
            JsonArrayRequest request = new JsonArrayRequest(URL_BASE + getURLS.get(table), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
                    try {
                        try {
                            db.execSQL("delete from " + table);
                        } catch (SQLiteException e) {

                        }
                        Log.d("RESPONSE FOR " + table, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            ContentValues values = new ContentValues();

                            for (String column: dataBase.get(table)) {
                                values.put(column, object.getString(column));
                            }

                            db.insert(table, null, values);
                        }

                    } catch (JSONException e) {
                        Log.d("FATALERROR1", e.toString());
                    } finally {
                        db.close();
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("FATALERROR", error.toString());
                    progressDia.setProgress(progressDia.getProgress()+1);
                    if (progressDia.getProgress() == progressDia.getMax())
                        progressDia.dismiss();
                }
            });

            Controller.getInstance().addToRequestQueue(request);
        }
    }

    private void getImages () {
        JsonArrayRequest request = new JsonArrayRequest(URL_BASE + Constants.GET_IMAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();

                db.execSQL("delete from images");
                progressDia.setMax(progressDia.getMax()+response.length());
                progressDia.setCancelable(false);

                new GetImages(response, progressDia).execute();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FATALERROR", error.toString());
                progressDia.setProgress(progressDia.getProgress()+1);
                if (progressDia.getProgress() == progressDia.getMax())
                    progressDia.dismiss();
            }
        });

        Controller.getInstance().addToRequestQueue(request);
    }

    private void getImage (final String id, String url) {
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                        byte[] b = baos.toByteArray();

                        SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("id", id);
                        values.put("image", b);
                        db.insert("images", null, values);
                        db.close();
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("IMAGEERROR", "Algo salió mal");
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                });
        Controller.getInstance().addToRequestQueue(request);
    }

    private boolean checkInternetConnection() {
        boolean mobileNwInfo = false;
        ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        try { mobileNwInfo = conxMgr.getActiveNetworkInfo().isConnected(); }
        catch (NullPointerException e) { mobileNwInfo = false; }

        return mobileNwInfo;
    }

    public void sendMail(View v) {
        TextView t = (TextView)v;

        Intent emailIntent = null;
        String email = "";

        if(t.getText().toString().compareTo(">_ Dr. Miguel Félix Mata Rivera") == 0) {
            email = "mmatar@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ M. en C. Carlos Hernández Nava") == 0) {
            email = "hernandeznc@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ M. en C. Miguel Alejandro Martínez Rosales") == 0) {
            email = "mamartinezr@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ Edgar Hernández Solís") == 0) {
            email = "edgarhzs.93@gmail.com";
        }

        else if(t.getText().toString().compareTo(">_ Oscar Alejandro Lemus Pichardo") == 0) {
            email = "oscarl.ocho@gmail.com";
        }

        else if(t.getText().toString().compareTo(">_ Miguel Armando Maldonado Vázquez") == 0) {
            email = "miguel.maldonadov@gmail.com";
        }

        emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WITCOM 2016");

        Toast.makeText(getApplicationContext(), "Sending mail to "+t.getText(), Toast.LENGTH_LONG).show();
        startActivity(Intent.createChooser(emailIntent, "Send Mail"));

    }

    protected void fetchDiscount() {
        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(WitcomBaseActivity.this, "Fetch Succeeded",
                             //       Toast.LENGTH_SHORT).show();

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            firebaseRemoteConfig.activateFetched();
                        } else {
                            //Toast.makeText(WitcomBaseActivity.this, "Fetch Failed",
                              //      Toast.LENGTH_SHORT).show();
                        }

                        WitcomLogoActivity.URL_BASE = firebaseRemoteConfig.getString("url_witcom");
                        WitcomLogoActivity.URL_STREAM = firebaseRemoteConfig.getString("url_streaming");
                        WitcomLogoActivity.URL_STREAM_LQ = firebaseRemoteConfig.getString("url_streaming_lq");

                        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
                        Cursor fila = bd.rawQuery("SELECT info_version FROM version", null);
                        if (fila.moveToFirst()) {
                            if (!fila.getString(0).equals(firebaseRemoteConfig.getString("data_version"))) {
                                //Actualizar
                                Log.d("ACTUALIZAR", "POR FAVOR ACTUALIZA");
                                tvUpdate.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvUpdate.setVisibility(View.VISIBLE);
                        }
                        WitcomLogoActivity.CONTENT_VERSION = firebaseRemoteConfig.getString("data_version");
                        fila.close();
                        bd.close();
                    }
                });
    }

    private void clearEvent() {
        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        bd.execSQL("delete from event");
        bd.close();
        startActivity(new Intent(getApplicationContext(), WitcomLogoActivity.class));
        finish();
    }

    private class GetImages extends AsyncTask<Void,Integer,Void> {

        private JSONArray jsonArray;
        private ProgressDialog progressDialog;

        GetImages(JSONArray jsonArray, ProgressDialog progressDialog) {
            this.jsonArray = jsonArray;
            this.progressDialog = progressDialog;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                progressDialog.setProgress(progressDialog.getProgress()+1);
                JSONObject object = jsonArray.getJSONObject(0);
                getImage(object.getString("id"), object.getString("image"),0);
            }
            catch (JSONException ex) {
                Log.d("LEMUS IMAGE ERROR", ex.toString());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(progressDialog.getProgress()+1);
            int index = values[0] + 1;
            try {
                JSONObject object = jsonArray.getJSONObject(index);
                getImage(object.getString("id"), object.getString("image"),index);
            }
            catch (JSONException ex) {
                Log.d("LEMUS IMAGE ERROR", ex.toString());
            }
            super.onProgressUpdate(values);
        }

        private void getImage(final String id, final String imageUrl, final int index){
            ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                            byte[] b = baos.toByteArray();

                            SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();

                            ContentValues values = new ContentValues();
                            values.put("id", id);
                            values.put("image", b);
                            db.insert("images", null, values);
                            db.close();
                            if (progressDialog.getProgress() == progressDialog.getMax()-1) {
                                progressDialog.dismiss();
                            }
                            else {
                                publishProgress(index);
                            }
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Log.d("LEMUS IMAGEERROR", error.toString());
                            Log.d("LEMUS IMAGEERROR", imageUrl);
                            if (progressDialog.getProgress() == progressDialog.getMax()-1) {
                                progressDialog.setProgress(progressDialog.getProgress()+1);
                                progressDialog.dismiss();
                            }
                            else {
                                publishProgress(index);
                            }
                        }
                    });
            Controller.getInstance().addToRequestQueue(request);
        }

    }

    //GEOCERCAS
    public void startLocationMonitoring() {
        Log.d(TAG, "startLocation called");
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(Constants.GEOFENCE_INTERVAL)
                    .setFastestInterval(Constants.GEOFENCE_FASTEST_INTERVAL)
                    //.setNumUpdates(Constants.GEOFENCE_NUM_UPDATES)
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

    public void starGeofenceMonitoring(ArrayList<RegionGeofence> coordinates) {
        Log.d(TAG, "startMonitoring called");
        List<Geofence> geofenceList = new ArrayList<>();
        Geofence geofence;

        try {
            for(RegionGeofence region: coordinates) {
                Log.d("DB", "NAME: " + region.name);
                geofence = new Geofence.Builder()
                        .setRequestId(region.id)
                        .setCircularRegion(region.latitude, region.longitude, region.radius)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setNotificationResponsiveness(Constants.GEOFENCE_NOTIFICATION_RESPONSIVENESS)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();
                geofenceList.add(geofence);
            }

            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofences(geofenceList)
                    .build();

            /*Geofence geofence = new Geofence.Builder()
                    .setRequestId(GEOFENCE_ID)
                    .setCircularRegion(19.494886, -99.119906, Constants.GEOFENCE_TOURISM_RADIUS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setNotificationResponsiveness(Constants.GEOFENCE_NOTIFICATION_RESPONSIVENESS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence)
                    .build();*/

            Intent intent = new Intent(this, GeofenceService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            googleApiClient.connect();

            if(!googleApiClient.isConnected()) {
                Log.d(TAG, "GoogleApiClient is not connected, not adding geofences");
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
        } catch (Exception e) {
            Log.d(TAG, "Exception - " + e.getMessage());
        }

    }

    public void stopGeofenceMonitoring() {
        Log.d(TAG, "stopMonitoring Called");
        ArrayList<String> geofencesIds = new ArrayList<String>();
        ArrayList<RegionGeofence> regionGeofences = getLocations();

        for(RegionGeofence geofence: regionGeofences) {
            geofencesIds.add(geofence.id);
        }

        LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofencesIds);
    }

    //Se obtiene un arreglo de geocercas apartir de los datos encontrados en la base de datos
    public ArrayList<RegionGeofence> getLocations() {
        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM place pl INNER JOIN place_category ct ON pl.place_category LIKE ct.id WHERE ct.show_in_app = 'true'", null);
        ArrayList<RegionGeofence> coordinates =  new ArrayList<RegionGeofence>();
        RegionGeofence regionGeofence;
        if (fila.moveToFirst()) {
            do {
                regionGeofence = new RegionGeofence(fila.getString(0), fila.getString(1), Double.parseDouble(fila.getString(4)), Double.parseDouble(fila.getString(3)), Constants.GEOFENCE_TOURISM_RADIUS);
                coordinates.add(regionGeofence);
            } while (fila.moveToNext());
        }
        fila.close();

        Cursor cur = bd.rawQuery("SELECT * FROM place pl INNER JOIN event ev ON pl.id LIKE ev.place", null);
        if (cur.moveToFirst()) {
            do {
                regionGeofence = new RegionGeofence(cur.getString(0), cur.getString(1), Double.parseDouble(cur.getString(4)), Double.parseDouble(cur.getString(3)), Constants.GEOFENCE_TOURISM_RADIUS);
                coordinates.add(regionGeofence);
            } while (cur.moveToNext());
        }
        cur.close();

        bd.close();

        regionGeofence = new RegionGeofence("Indios Verdes", "Indios Verdes", 19.494739, -99.122002,1000);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("Zocalo", "Zocalo", 19.432590, -99.133206, 1000);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("Bellas Artes", "Bellas Artes", 19.435293, -99.141305, 1000);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("Salon Corona", "Salon Corona", 19.433884, -99.139745, 10);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("El Huequito", "El Huequito", 19.430265, -99.138796, 10);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("Casa Lemus", "Casa Lemus", 19.498256, -99.078180, 10);
        coordinates.add(regionGeofence);

        regionGeofence = new RegionGeofence("Bancomer", "Bancomer", 19.501102, -99.131321, 100);
        coordinates.add(regionGeofence);

        return coordinates;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
