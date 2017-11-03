package com.upiita.witcom2016;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.io.File;
import java.util.List;

/**
 * Created by Edgar on 02/11/2017.
 */

public class GeofenceService extends IntentService {
    public static final String TAG = "GeofenceService";

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

            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "Entering geofence - " + requestId);
                handleEvent(requestId);
            } else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "Exiting geofence - " + requestId);
            }
        }
    }

    private void handleEvent(String requestId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        buildNotification(mBuilder, requestId);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, mBuilder.build());
    }

    private void buildNotification(NotificationCompat.Builder mBuilder, String geofence) {
        mBuilder.setSmallIcon(R.drawable.witcomlogo);

        //if (activity != null && details != null) {

            /*SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT * FROM activities WHERE id = '" + activity + "'", null);
            if (fila.moveToFirst()) {
                Cursor fila2 = bd.rawQuery("SELECT * FROM " + fila.getString(3) + " WHERE id = '" + details + "'", null);
            }*/


        /*} else if (conference != null) {

            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT * FROM activity WHERE id = '" + conference + "'", null);
            if (fila.moveToFirst()) {

                Cursor activitPeopleCursor = bd.rawQuery("SELECT * FROM activity_people WHERE activity = " + conference, null);
                if (activitPeopleCursor.moveToFirst()) {
                    Cursor peopleCursor = bd.rawQuery("SELECT photo FROM people WHERE id = " + activitPeopleCursor.getString(2), null);
                    if (peopleCursor.moveToFirst()) {
                        Cursor fila2 = bd.rawQuery("SELECT image FROM images WHERE id = " + peopleCursor.getString(0), null);
                        if (fila2.moveToFirst()) {
                            mBuilder.setLargeIcon(BitmapFactory.decodeStream(new ByteArrayInputStream(fila2.getBlob(0))));
                        }
                        fila2.close();
                    }
                    peopleCursor.close();
                }
                activitPeopleCursor.close();
                */
                mBuilder.setContentTitle("Geocerca")
                        .setContentText("Visita el lugar:")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true);
            /*}
            fila.close();
            bd.close();

            Intent notificationIntent = new Intent(getApplicationContext(), RateActivity.class);
            notificationIntent.putExtra("conference", conference);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder.setContentIntent(contentIntent);

        } else {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.witcomlogo))
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());
        }*/

        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Adara.ogg")))
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }
}
