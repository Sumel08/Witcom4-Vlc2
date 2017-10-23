package com.upiita.witcom2016.rate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        final String conference = getIntent().getStringExtra("conference");
        String activityTypeName = "activity";
        String activityName = "activity";
        String eventName = "WITCOM";

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM activity WHERE id = '" + conference + "'", null);
        if (fila.moveToFirst()) {

            Cursor activityTypeCursor = bd.rawQuery("SELECT name FROM activity_type WHERE id = " + fila.getString(8), null);
            if (activityTypeCursor.moveToFirst()) {
                activityTypeName = activityTypeCursor.getString(0);
            }
            activityTypeCursor.close();

            activityName = fila.getString(1);

            ((TextView) findViewById(R.id.rating_title)).setText(fila.getString(1));

            Cursor activityPeopleCursor = bd.rawQuery("SELECT * FROM activity_people WHERE activity = " + fila.getString(0), null);
            if (activityPeopleCursor.moveToFirst()) {
                Cursor peopleCursor = bd.rawQuery("SELECT photo FROM people WHERE id = " + activityPeopleCursor.getString(2), null);
                if (peopleCursor.moveToFirst()) {
                    Cursor fila2 = bd.rawQuery("SELECT image FROM images WHERE id = " + peopleCursor.getString(0), null);
                    if (fila2.moveToFirst()) {
                        ((ImageView) findViewById(R.id.rating_image)).setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila2.getBlob(0))));
                    }
                    fila2.close();
                }
                peopleCursor.close();
            }
            activityPeopleCursor.close();
        }
        fila.close();

        Cursor eventCursor = bd.rawQuery("SELECT name FROM event", null);
        if (eventCursor.moveToFirst()) {
            eventName = eventCursor.getString(0);
        }
        eventCursor.close();

        final String finalActivityTypeName = activityTypeName;
        final String finalActivityName = activityName;
        final String finalEventName = eventName;
        (findViewById(R.id.rating_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://witcom-2016.appspot.com");

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.US);
                String firebaseToken = FirebaseInstanceId.getInstance().getToken();

                String android_id = firebaseToken +
                        "-" +
                        finalActivityName +
                        "-" +
                        getIntent().getStringExtra("conference") +
                        ".json";
                StorageReference fileRef = storageRef.child(finalEventName).child(finalActivityTypeName).child(android_id);
                String textFile = "{\"activity\": \"" + conference + "\", \"comment\": \"" + ((EditText) findViewById(R.id.rating_comment)).getText().toString() + "\", \"rate\": \"" + ((RatingBar) findViewById(R.id.rating_bar)).getRating() + "\", \"token\": \"" + firebaseToken + "\" }";

                //String textFile = ((RatingBar) findViewById(R.id.rating_bar)).getRating() + "\n" + ((EditText) findViewById(R.id.rating_comment)).getText().toString();

                UploadTask uploadTask = fileRef.putBytes(textFile.getBytes());
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });

                Toast.makeText(RateActivity.this, getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RateActivity.this, WitcomPagerActivity.class);
                startActivity(intent);

            }
        });

        (findViewById(R.id.rating_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RateActivity.this, WitcomPagerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
