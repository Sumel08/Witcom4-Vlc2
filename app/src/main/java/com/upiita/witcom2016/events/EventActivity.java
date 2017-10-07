package com.upiita.witcom2016.events;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.upiita.witcom2016.BuildConfig;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.Controller;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.upiita.witcom2016.WitcomLogoActivity.IMAGE_DEFAULT;
import static com.upiita.witcom2016.WitcomLogoActivity.URL_BASE;

public class EventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    protected FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        firebaseRemoteConfig.setConfigSettings(configSettings);

        prepareEvents();
    }

    /**
     * Adding few albums for testing
     */
    private void prepareEvents() {

        final Map<String, String> images = new HashMap<>();
        Toast.makeText(this, WitcomLogoActivity.URL_BASE + "/images/getImages" , Toast.LENGTH_SHORT).show();

        JsonArrayRequest requestImage = new JsonArrayRequest(WitcomLogoActivity.URL_BASE + "/images/getImages", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);

                        images.put(object.getString("id"), object.getString("url"));
                    }

                } catch (JSONException e) {
                    Log.d("FATALERROR1", e.toString());
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FATALERROR", error.networkResponse + ", " + error.toString());
            }
        });

        Controller.getInstance().addToRequestQueue(requestImage);

        JsonArrayRequest request = new JsonArrayRequest(WitcomLogoActivity.URL_BASE + "/event/getEvents", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
                try {
                    String image = IMAGE_DEFAULT;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        try {
                            JSONObject imageObj = object.getJSONObject("eventImage");

                            image = images.get(imageObj.getString("id"));
                        } catch (JSONException e){
                            image = IMAGE_DEFAULT;
                        } finally {
                            Event event = new Event(object.getString("name"), object.getString("code"), image, new Date(), new Date());

                            eventList.add(event);
                        }
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d("FATALERROR1", e.toString());
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FATALERROR", error.toString());
            }
        });

        Controller.getInstance().addToRequestQueue(request);

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class EventAdapter extends RecyclerView.Adapter<EventActivity.EventAdapter.MyViewHolder> {

        private Context mContext;
        private List<Event> eventList;
        private ProgressDialog progressDia;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count;
            public ImageView thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                count = (TextView) view.findViewById(R.id.count);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                overflow = (ImageView) view.findViewById(R.id.overflow);
            }
        }


        public EventAdapter(Context mContext, List<Event> eventList) {
            this.mContext = mContext;
            this.eventList = eventList;
        }

        @Override
        public EventActivity.EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final EventActivity.EventAdapter.MyViewHolder holder, final int position) {
            final Event event = eventList.get(position);
            holder.title.setText(event.getEventName());
            holder.count.setText(event.getEventCode());

            new EventActivity.EventAdapter.DownloadImageTask(holder.thumbnail)
                    .execute(WitcomLogoActivity.URL_BASE + event.getEventImage());

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow);
                }
            });

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Toast.makeText(mContext, "Pulse " + event.getEventCode(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventActivity.this, NewEventActivity.class);
                    intent.putExtra("eventCode", event.getEventCode());
                    intent.putExtra("eventImage", event.getEventImage());
                    startActivity(intent);*/
                    update(event.getEventCode());
                }
            });
        }

        /**
         * Showing popup menu when tapping on 3 dots
         */
        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_event, popup.getMenu());
            popup.setOnMenuItemClickListener(new EventActivity.EventAdapter.MyMenuItemClickListener());
            popup.show();
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }

        /**
         * Click listener for popup menu items
         */
        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            public MyMenuItemClickListener() {
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_add_favourite:
                        Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_play_next:
                        Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        protected void update (String eventCode) {
            if (checkInternetConnection()) {
                progressDia = new ProgressDialog(EventActivity.this);
                progressDia.setTitle(getString(R.string.updating));
                progressDia.setMessage(getString(R.string.wait));
                progressDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                getTables(eventCode);
            } else {
                Log.d("INTERNETCONNECTION", "No hay conexión");
            }
        }

        public void getTables(String eventCode) {
            final HashMap<String, ArrayList<String>> dataBase = new HashMap<>();
            final HashMap<String, String> getURLS = new HashMap<>();
            ArrayList<String> columns;

            getURLS.put("activity", "/activity/getActivities/");
            getURLS.put("activity_people", "/activityPeople/getActivitiesPeople/");
            getURLS.put("activity_type", "/activityType/getActivitiesType/");
            getURLS.put("chairs", "/chairs/getChairs/");
            getURLS.put("developers", "/developers/getDevelopers/");
            getURLS.put("event", "/event/getEvent/");
            getURLS.put("people", "/people/getPeople/");
            getURLS.put("people_social_networks", "/peopleSocialNetworks/getPeopleSocialNetworks/");
            getURLS.put("place_category", "/placeCategory/getPlaceCategories/");
            getURLS.put("place", "/place/getPlaces/");
            getURLS.put("place_social_networks", "/placeSocialNetworks/getPlaceSocialNetworks/");
            getURLS.put("schedule", "/schedule/getSchedule/");
            getURLS.put("social_networks", "/socialNetworks/getSocialNetworks/");
            getURLS.put("sponsors", "/sponsors/getSponsors/");
            getURLS.put("streams", "/streams/getStreams/");
            //getURLS.put("place_category", "/placeCategory/getPlaceCategories/");


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


            /* = new ArrayList<>();
            columns.add("id");
            columns.add("code");
            columns.add("description");
            columns.add("endDate");
            columns.add("eventImage");
            columns.add("name");
            columns.add("place");
            columns.add("schedule");
            columns.add("sketch");
            columns.add("startDate");
            dataBase.put("event", columns);
            getURLS.put("event", "/event/getEvent/");


            columns = new ArrayList<>();
            columns.add("id");
            columns.add("placeName");
            columns.add("description");
            columns.add("longitude");
            columns.add("latitude");
            columns.add("altitude");
            columns.add("indication");
            columns.add("additionalInfo");
            columns.add("website");
            columns.add("email");
            columns.add("telephone");
            columns.add("image");
            dataBase.put("place", columns);
            getURLS.put("place", "/place/getPlaces/");


            columns = new ArrayList<>();
            columns.add("id");
            columns.add("name");
            columns.add("surname");
            columns.add("birthdate");
            columns.add("photo");
            columns.add("resume");
            columns.add("email");
            columns.add("phone");
            columns.add("provenance");
            dataBase.put("people", columns);
            getURLS.put("people", "/people/getPeople/");*/

            //tvUpdate.setVisibility(View.INVISIBLE);

            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            bd.execSQL("delete from version");
            bd = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("info_version", WitcomLogoActivity.CONTENT_VERSION);
            bd.insert("version", null, values);
            bd.close();

            //WitcomLogoActivity.URL_BASE = firebaseRemoteConfig.getString("url_witcom");

            progressDia.setMax(dataBase.size() + 1);
            progressDia.setProgress(0);
            progressDia.show();

            getImages(eventCode);

            for (final String table: dataBase.keySet()) {
                JsonArrayRequest request = new JsonArrayRequest(URL_BASE + getURLS.get(table) + eventCode, new Response.Listener<JSONArray>() {
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
                                    try{
                                        values.put(column, object.getJSONObject(column).getString("id"));
                                    } catch (Exception e) {
                                        values.put(column, object.getString(column));
                                    }
                                }

                                db.insert(table, null, values);
                            }

                        } catch (JSONException e) {
                            Log.d("FATALERROR1", e.toString());
                        } finally {
                            db.close();
                            progressDia.setProgress(progressDia.getProgress()+1);
                            if (progressDia.getProgress() == progressDia.getMax()) {
                                progressDia.dismiss();
                                startEvent();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("FATALERROR", error.toString());
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax()) {
                            progressDia.dismiss();
                            startEvent();
                        }
                    }
                });

                Controller.getInstance().addToRequestQueue(request);
            }
        }

        private void getImages (String eventCode) {
            JsonArrayRequest request = new JsonArrayRequest(URL_BASE + "/images/getImages/" + eventCode, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
                    try {
                        db.execSQL("delete from images");
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            progressDia.setMax(progressDia.getMax()+1);
                            getImage(object.getString("id"), URL_BASE + object.getString("url"));
                        }

                    } catch (JSONException e) {
                        Log.d("FATALERROR1", e.toString());
                    } finally {
                        db.close();
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax()) {
                            progressDia.dismiss();
                            startEvent();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("FATALERROR", error.toString());
                    progressDia.setProgress(progressDia.getProgress()+1);
                    if (progressDia.getProgress() == progressDia.getMax()) {
                        progressDia.dismiss();
                        startEvent();
                    }
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
                            if (progressDia.getProgress() == progressDia.getMax()) {
                                progressDia.dismiss();
                                startEvent();
                            }
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Log.d("IMAGEERROR", "Algo salió mal");
                            progressDia.setProgress(progressDia.getProgress()+1);
                            if (progressDia.getProgress() == progressDia.getMax()) {
                                progressDia.dismiss();
                                startEvent();
                            }
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

        private void startEvent() {
            Intent intent = new Intent(EventActivity.this, WitcomLogoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
