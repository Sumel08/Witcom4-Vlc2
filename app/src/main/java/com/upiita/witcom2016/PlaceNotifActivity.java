package com.upiita.witcom2016;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.tourism.PlaceDetailActivity;
import com.upiita.witcom2016.tourism.PlaceDetailFragment;
import com.upiita.witcom2016.tourism.PlaceListActivity;
import com.upiita.witcom2016.tourism.place.PlaceContent;

import java.io.ByteArrayInputStream;

public class PlaceNotifActivity extends AppCompatActivity {

    private String name;
    private String description;
    private String longitude;
    private String latitude;
    private String altitude;
    private String indication;
    private String additional_info;
    private String website;
    private String email;
    private String telephone;
    private String image;
    private String place_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_notif);

        Intent notifIntent = getIntent();
        String geofence = notifIntent.getExtras().getString("place_id");

        ImageView iv = (ImageView) findViewById(R.id.place_detail_image);
        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT im.image FROM images im INNER JOIN place pl ON im.id = pl.image WHERE pl.id = '" + geofence + "'", null);
        if (fila.moveToFirst()) {
            iv.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
        }
        fila.close();
        bd.close();

        bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        fila = bd.rawQuery("SELECT * FROM place WHERE id = '" + geofence + "'", null);
        if (fila.moveToFirst()) {
            name = fila.getString(1);
            description = fila.getString(2);
            longitude = fila.getString(3);
            latitude = fila.getString(4);
            altitude = fila.getString(5);
            indication = fila.getString(6);
            additional_info = fila.getString(7);
            website = fila.getString(8);
            email = fila.getString(9);
            telephone = fila.getString(10);
            image = fila.getString(11);
            place_category = fila.getString(12);
        }
        fila.close();
        bd.close();

        Log.d("DETAIL", "name: " + name);
        Log.d("DETAIL", "description: " + description);
        Log.d("DETAIL", "longitude: " + longitude);
        Log.d("DETAIL", "latitude: " + latitude);
        Log.d("DETAIL", "altitude: " + altitude);
        Log.d("DETAIL", "indication: " + indication);
        Log.d("DETAIL", "additional_info: " + additional_info);
        Log.d("DETAIL", "website: " + website);
        Log.d("DETAIL", "email: " + email);
        Log.d("DETAIL", "telephone: " + telephone);
        Log.d("DETAIL", "image: " + image);
        Log.d("DETAIL", "place_category: " + place_category);

        TextView place_detail = (TextView)findViewById(R.id.place_detail);
        TextView place_address = (TextView)findViewById(R.id.place_detail_address);
        TextView place_schedule = (TextView)findViewById(R.id.place_detail_schedule);

        place_detail.setText(description);
        place_address.setText(indication);
        place_schedule.setText(additional_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(name);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PlaceNotifActivity.this, WitcomStreetViewActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                startActivity(intent);
            }
        });

        FloatingActionButton fabNav = (FloatingActionButton) findViewById(R.id.fab_nav);
        fabNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View layout = inflater.inflate(R.layout.navigationmap, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlaceNotifActivity.this);
                alertDialogBuilder
                        .setTitle(getString(R.string.navigation))
                        .setMessage(getString(R.string.how_get))
                        .setCancelable(false)
                        .setView(layout)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RadioButton rbc = (RadioButton)layout.findViewById(R.id.car);
                                RadioButton rbb = (RadioButton)layout.findViewById(R.id.bike);
                                RadioButton rbw = (RadioButton)layout.findViewById(R.id.walk);

                                if(rbc.isChecked()) {
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    dialog.cancel();
                                }
                                else if(rbb.isChecked()) {
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude+ "&mode=b");
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    dialog.cancel();
                                }
                                else if(rbw.isChecked()) {
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "mode=w");
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    dialog.cancel();
                                }

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, PlaceListActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("city_id",getIntent().getIntExtra("city_id", 0));
            intent.putExtras(extras);
            //startActivity(intent);
            PlaceContent.ITEM_MAP.clear();
            PlaceContent.ITEMS.clear();
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
