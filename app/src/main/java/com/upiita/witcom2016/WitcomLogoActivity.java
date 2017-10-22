package com.upiita.witcom2016;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.events.EventActivity;
import com.upiita.witcom2016.pager.WitcomPagerActivity;
import com.upiita.witcom2016.rate.RateActivity;

import java.io.ByteArrayInputStream;

public class WitcomLogoActivity extends AppCompatActivity {

    public static String URL_BASE = "https://host-test-b1ab8.firebaseapp.com";
    public static String CONTENT_VERSION = "0";
    public static String URL_STREAM = "rtmp://148.204.86.75:1935/envivo.upiita/livestream";
    public static String URL_STREAM_LQ = "rtmp://148.204.86.75:1935/envivo.upiita.lq/livestream";
    public static String IMAGE_DEFAULT = "/images/images/default.png";
    private boolean currentEvent = false;

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
    public void onPause() {
        super.onPause();
        finish();
    }
}
