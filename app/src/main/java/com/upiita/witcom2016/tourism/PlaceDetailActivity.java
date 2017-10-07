package com.upiita.witcom2016.tourism;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.WitcomStreetViewActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.tourism.place.PlaceContent;

import java.io.ByteArrayInputStream;

/**
 * An activity representing a single Place detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PlaceListActivity}.
 */
public class PlaceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PlaceDetailActivity.this, WitcomStreetViewActivity.class);
                intent.putExtra("latitude", PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).latitude);
                intent.putExtra("longitude", PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).longitude);

                startActivity(intent);

                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabNav = (FloatingActionButton) findViewById(R.id.fab_nav);
        fabNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                final View layout = inflater.inflate(R.layout.navigationmap, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlaceDetailActivity.this);
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
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).latitude + "," + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).longitude);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    dialog.cancel();
                                }
                                else if(rbb.isChecked()) {
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).latitude + "," + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).longitude+ "&mode=b");
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    dialog.cancel();
                                }
                                else if(rbw.isChecked()) {
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).latitude + "," + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).longitude + "mode=w");
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

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView iv = (ImageView) findViewById(R.id.place_detail_image);
        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + PlaceContent.ITEM_MAP.get(getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID)).id_image +"'", null);
        if (fila.moveToFirst()) {
            iv.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
        }
        fila.close();
        bd.close();

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PlaceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PlaceDetailFragment.ARG_ITEM_ID));
            PlaceDetailFragment fragment = new PlaceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.place_detail_container, fragment)
                    .commit();
        }
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
