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
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        buildNotification(mBuilder, remoteMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, mBuilder.build());

    }

    private void buildNotification(NotificationCompat.Builder mBuilder, RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String activity = data.get("activity");
        String details = data.get("details");
        String conference = data.get("conference");

        mBuilder.setSmallIcon(R.drawable.witcomlogo);

        if (activity != null && details != null) {

            /*SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT * FROM activities WHERE id = '" + activity + "'", null);
            if (fila.moveToFirst()) {
                Cursor fila2 = bd.rawQuery("SELECT * FROM " + fila.getString(3) + " WHERE id = '" + details + "'", null);
            }*/


        } else if (conference != null) {

            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT * FROM conferences WHERE id = '" + conference + "'", null);
            if (fila.moveToFirst()) {
                Cursor fila2 = bd.rawQuery("SELECT image FROM images WHERE id = '" + fila.getString(6) + "'", null);
                if (fila2.moveToFirst()) {
                    mBuilder.setLargeIcon(BitmapFactory.decodeStream(new ByteArrayInputStream(fila2.getBlob(0))));
                }
                mBuilder.setContentTitle(getString(R.string.rate_conference))
                        .setContentText(fila.getString(1))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true);
            }

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
        }

        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] { 500, 500, 500, 500, 500 })
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Adara.ogg")))
        .setCategory(NotificationCompat.CATEGORY_REMINDER);
    }
}
