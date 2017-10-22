package com.upiita.witcom2016.sketch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import java.io.ByteArrayInputStream;

public class WitcomSketchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_sketch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();

        Cursor sketchCursor = bd.rawQuery("SELECT * FROM sketch", null);

        if (sketchCursor.moveToFirst()) {
            Cursor imageCursor = bd.rawQuery("SELECT image FROM images WHERE id = " + sketchCursor.getString(1), null);
            if (imageCursor.moveToFirst()) {
                ((ImageView) findViewById(R.id.sketch_view)).setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(imageCursor.getBlob(0))));
            }

            imageCursor.close();
        }

        sketchCursor.close();
        bd.close();
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

            Intent intent = new Intent(this, WitcomPagerActivity.class);
            intent.putExtra("page", getIntent().getIntExtra("page", 1));

            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
