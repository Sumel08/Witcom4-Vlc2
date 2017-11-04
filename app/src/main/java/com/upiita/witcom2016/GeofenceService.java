package com.upiita.witcom2016;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.rate.RateActivity;
import com.upiita.witcom2016.tourism.PlaceDetailActivity;
import com.upiita.witcom2016.tourism.PlaceDetailFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar on 02/11/2017.
 */

public class GeofenceService extends IntentService {

    public static final String TAG = "GeofenceService";
    private String witcomId = "";

    public GeofenceService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()) {
            //TODO: Handler error
        } else {
            int transition = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            String requestId = geofence.getRequestId();

            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor cur = bd.rawQuery("SELECT * FROM place pl INNER JOIN event ev ON pl.id LIKE ev.place", null);
            if (cur.moveToFirst()) {
                do {
                    witcomId = cur.getString(0);
                } while (cur.moveToNext());
            }
            cur.close();
            bd.close();

            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "Entering geofence - " + requestId);
                handleEvent(requestId, true);
            } else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "Exiting geofence - " + requestId);
                if(requestId == witcomId) {
                    handleEvent(requestId, false);
                }
            }
        }
    }

    private void handleEvent(String requestId, boolean isEnter) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        buildNotification(mBuilder, requestId, isEnter);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, mBuilder.build());
    }

    private void buildNotification(NotificationCompat.Builder mBuilder, String geofence, boolean isEnter) {

        String title = geofence;
        String message = geofence;

        if(geofence == witcomId) {
            title = "WITCOM 2017";
            if(isEnter) {
                message = "Te da la bienvenida, disfruta el evento";
            } else {
                message = "Agradece tu visita, te esperamos la próxima.";
            }

        } else {
            message = "Se encuentra cerca de ti, click para info.";
            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor cur = bd.rawQuery("SELECT * FROM place WHERE ID = '" + geofence + "'", null);
            if (cur.moveToFirst()) {
                do {
                    title = cur.getString(1);
                } while (cur.moveToNext());
            }
            cur.close();
            bd.close();
        }


        mBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.witcomlogo)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);


        if(geofence != witcomId) {
            Intent notificationIntent = new Intent(getApplicationContext(), PlaceNotifActivity.class);
            notificationIntent.putExtra("place_id", geofence);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(contentIntent);
        }
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Adara.ogg")))
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }
}
