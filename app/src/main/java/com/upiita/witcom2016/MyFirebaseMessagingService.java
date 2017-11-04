package com.upiita.witcom2016;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.upiita.witcom2016.conference.WitcomProgramActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.rate.RateActivity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by oscar on 28/09/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public  MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        Bundle bundle = intent.getExtras();

        for (String key: bundle.keySet()) {
            Log.d("BUNDLEKEY", key);
//            Log.d("BUNDLEVALUE", bundle.getString(key));
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.witcomlogo))
                .setContentTitle("Lemus")
                .setContentText("nuevo");
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Adara.ogg")))
                .setCategory(NotificationCompat.CATEGORY_REMINDER);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 /* ID of notification */, mBuilder.build());

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getData());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        buildNotification(mBuilder, remoteMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, mBuilder.build());

        super.onMessageReceived(remoteMessage);

    }

    private void buildNotification(NotificationCompat.Builder mBuilder, RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String activity = data.get("activity");
        String details = data.get("details");
        String conference = data.get("conference");

        mBuilder.setSmallIcon(R.drawable.witcomlogo);

        if (activity != null) {

            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT * FROM activity WHERE id = '" + activity + "'", null);
            if (fila.moveToFirst()) {
                Cursor activityType = bd.rawQuery("SELECT * FROM activity_type WHERE id = " + fila.getString(8), null);
                if (activityType.moveToFirst()) {
                    Cursor imageActivity = bd.rawQuery("SELECT image FROM images WHERE id = " + activityType.getString(5), null);
                    if (imageActivity.moveToFirst()) {
                        mBuilder.setLargeIcon(BitmapFactory.decodeStream(new ByteArrayInputStream(imageActivity.getBlob(0))));
                    }
                    imageActivity.close();
                    mBuilder.setContentText(activityType.getString(1));
                }
                activityType.close();
                mBuilder.setContentTitle(fila.getString(1))
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(fila.getString(1)));
            }
            fila.close();
            bd.close();

            mBuilder.setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);

            Intent notificationIntent = new Intent(getApplicationContext(), WitcomProgramActivity.class);
            notificationIntent.putExtra("fromNotification", activity);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            mBuilder.setContentIntent(contentIntent);


        } else if (conference != null) {

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

                mBuilder.setContentTitle(getString(R.string.rate_conference))
                        .setContentText(fila.getString(1))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true);
            }
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
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("text"));
        }

        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Adara.ogg")))
        .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }
}
