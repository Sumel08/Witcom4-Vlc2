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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Random;

public class RateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        String conference = getIntent().getStringExtra("conference");

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM conferences WHERE id = '" + conference + "'", null);
        if (fila.moveToFirst()) {
            Cursor fila2 = bd.rawQuery("SELECT image FROM images WHERE id = '" + fila.getString(6) + "'", null);
            ((TextView) findViewById(R.id.rating_title)).setText(fila.getString(1));
            if (fila2.moveToFirst())
                ((ImageView) findViewById(R.id.rating_image)).setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila2.getBlob(0))));
        }

        (findViewById(R.id.rating_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://witcom-2016.appspot.com");
                String android_id = new Random(Calendar.getInstance().getTimeInMillis()).nextInt() + "C" + getIntent().getStringExtra("conference") + ".txt";
                StorageReference fileRef = storageRef.child("Conferences").child(android_id);

                String textFile = ((RatingBar) findViewById(R.id.rating_bar)).getRating() + "\n" + ((EditText) findViewById(R.id.rating_comment)).getText().toString();

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
