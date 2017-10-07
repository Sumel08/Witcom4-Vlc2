package com.upiita.witcom2016.pager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.upiita.witcom2016.BuildConfig;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.indicator.IndicatorTouch;
import com.viewpagerindicator.IconPageIndicator;

/**
 * Created by oscar on 27/09/16.
 */

public class WitcomPagerActivity extends WitcomBaseActivity {

    Dialog dialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pager);

        eventCode = getIntent().getStringExtra("eventCode");

        Toast.makeText(this, "Main Activity: " + eventCode, Toast.LENGTH_SHORT).show();

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

        swipeToast();
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
}
