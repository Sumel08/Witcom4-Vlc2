package com.upiita.witcom2016.speaker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class WitcomSpeakerActivity extends AppCompatActivity {

    public RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_speaker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        //adapter = new SpeakerAdapter(items, getApplicationContext());
        //adapter = new SpeakerAdapter(items, this);

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/

        readData();
    }

    private void readData() {

        List<Speaker> items = new ArrayList<>();

        SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getReadableDatabase();

        /*OBTENEMOS TODAS LAS CONFERENCIAS, DE AHÍ A LOS PONENTES*/

        Cursor activityTypeCursor = db.rawQuery("SELECT id FROM activity_type WHERE show_speakers_in_app = \"true\"", null);

        if (activityTypeCursor.moveToFirst()) {
            do {
                Cursor activityPeopleCursor = db.rawQuery("SELECT * FROM activity_people", null);

                if (activityPeopleCursor.moveToFirst()) {
                    do {
                        Cursor personCursor = db.rawQuery("SELECT * FROM people where id = " + activityPeopleCursor.getString(2), null);
                        if (personCursor.moveToFirst()) {
                            Cursor activityCursor = db.rawQuery("SELECT * FROM activity where id = " + activityPeopleCursor.getString(1) + " and activity_type = " + activityTypeCursor.getString(0), null);

                            if (activityCursor.moveToFirst()) {
                                items.add(new Speaker(
                                        personCursor.getString(4),
                                        personCursor.getString(1),
                                        personCursor.getString(8),
                                        activityCursor.getString(1),
                                        personCursor.getString(2),
                                        personCursor.getString(3),
                                        personCursor.getString(4),
                                        personCursor.getString(5),
                                        personCursor.getString(6),
                                        personCursor.getString(7),
                                        personCursor.getString(8) ));
                            }
                        }
                    } while (activityPeopleCursor.moveToNext());
                }
            } while (activityTypeCursor.moveToNext());
        }

        db.close();

        adapter = new SpeakerAdapter(items, getApplicationContext());
        recycler.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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

            Intent intent = new Intent(this, WitcomPagerActivity.class);
            intent.putExtra("page", getIntent().getIntExtra("page", 1));

            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SpeakerViewHolder> {

        List<Speaker> items;
        Context context;
        int mExpandedPosition = -1;

        class SpeakerViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {

            public ImageView image;
            public TextView name;
            public TextView from;
            public ImageView imageExpanded;
            public TextView nameExpanded;
            public TextView fromExpanded;
            public TextView resume;
            //public LinearLayout details;
            public Speaker speaker;

            public LinearLayout normalContainer;
            public LinearLayout expandedContainer;


            public SpeakerViewHolder(View v) {
                super(v);
                image = (ImageView) v.findViewById(R.id.image);
                name = (TextView) v.findViewById(R.id.name);
                from = (TextView) v.findViewById(R.id.from);
                imageExpanded = (ImageView) v.findViewById(R.id.imageExpanded);
                nameExpanded = (TextView) v.findViewById(R.id.nameExpanded);
                fromExpanded = (TextView) v.findViewById(R.id.fromExpanded);
                resume = (TextView) v.findViewById(R.id.resume);

                normalContainer = (LinearLayout) v.findViewById(R.id.normalContainer);
                expandedContainer = (LinearLayout) v.findViewById(R.id.expandedContainer);

                itemView.setOnClickListener(this);
                //details = (LinearLayout) v.findViewById(R.id.cv_details);
            }

            @Override
            public void onClick(View v) {
                /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 300, 1f);
                from.setLayoutParams(lp);*/
                items.get(getAdapterPosition()).setExpanded(!items.get(getAdapterPosition()).isExpanded());
                notifyItemChanged(getAdapterPosition());
                //recycler.smoothScrollToPosition(getAdapterPosition());
                if(items.get(getAdapterPosition()).isExpanded()) {
                    ((LinearLayoutManager) lManager).scrollToPositionWithOffset(getAdapterPosition(), 0);
                }
            }

        }

        public SpeakerAdapter(List<Speaker> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public SpeakerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.speaker_card, viewGroup, false);
            return new SpeakerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SpeakerViewHolder viewHolder, int i) {

            SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = db.rawQuery("SELECT * FROM images WHERE id = '" + items.get(i).getPhoto() + "'", null);
            Bitmap bitmap = null;
            if (fila.moveToFirst()) {
                do {
                    bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(1)));
                } while (fila.moveToNext());
            }

            fila.close();
            db.close();

            if(items.get(i).isExpanded()) {
                viewHolder.expandedContainer.setVisibility(View.VISIBLE);
                viewHolder.normalContainer.setVisibility(View.GONE);
                viewHolder.nameExpanded.setText(items.get(i).getName());
                viewHolder.fromExpanded.setText(items.get(i).getConference());
                viewHolder.resume.setText(items.get(i).getResume());
                viewHolder.imageExpanded.setImageBitmap(bitmap);
            }
            else {
                viewHolder.expandedContainer.setVisibility(View.GONE);
                viewHolder.normalContainer.setVisibility(View.VISIBLE);
                viewHolder.name.setText(items.get(i).getName());
                viewHolder.from.setText(items.get(i).getConference());
                viewHolder.image.setImageBitmap(bitmap);
            }


            //viewHolder.speaker = items.get(i);



            /*viewHolder.mView.findViewById(R.id.speaker_card_card).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    SQLiteDatabase bd = WitcomLogoActivity.wdb.getReadableDatabase();
                    Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + viewHolder.speaker.getImage() + "'", null);
                    fila.moveToFirst();
                    do {

                        try {
                            FileOutputStream fos = new FileOutputStream(new File("/sdcard/" + "aux.jpg"));
                            fos.write(fila.getBlob(0));
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    while (fila.moveToNext());

                    bd.close();

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, viewHolder.name.getText().toString());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/sdcard/" + "aux.jpg")));
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(Intent.createChooser(shareIntent, "Hola Mundo"));

                    return true;
                }
            });*/

        }

    }

}
