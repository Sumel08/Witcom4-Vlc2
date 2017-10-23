package com.upiita.witcom2016.workshop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;
import com.upiita.witcom2016.workshop.dummy.DummyContent;
import com.upiita.witcom2016.workshop.utils.ActivityType;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class WitcomActivitiesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_activities);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_activities);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final ArrayList<ActivityType> activities = new ArrayList<>();

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM activity_type where show_in_app = 'true'", null);
        if (fila.moveToFirst()) {
            do {
                activities.add(new ActivityType(fila.getInt(0), fila.getString(1), fila.getString(2), fila.getString(5)));
            } while (fila.moveToNext());
        }

        fila.close();
        bd.close();

        ActivitiesTypeAdapter adapter = new ActivitiesTypeAdapter(activities);
        mRecyclerView.setAdapter(adapter);
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

    public class ActivitiesTypeAdapter extends RecyclerView.Adapter<ActivitiesTypeAdapter.ActivityHolder> {

        ArrayList<ActivityType> activities;

        public ActivitiesTypeAdapter(ArrayList<ActivityType> activities) {
            this.activities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_view, parent, false);
            ActivityHolder ctyh = new ActivityHolder(v);
            return ctyh;
        }

        @Override
        public void onBindViewHolder(final ActivityHolder holder, final int position) {
            holder.nameCity.setText(activities.get(position).getName());
            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + activities.get(position).getImage() + "'", null);
            if (fila.moveToFirst())
                holder.imageCity.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));

            fila.close();
            bd.close();

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putInt("city_id", activities.get(position).getId());
                    extras.putString("city_name", activities.get(position).getName());
                    Intent mapIntent = new Intent(WitcomActivitiesActivity.this, WitcomWorkshopActivity.class);
                    mapIntent.putExtras(extras);
                    DummyContent.ITEM_MAP.clear();
                    DummyContent.ITEMS.clear();

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WitcomActivitiesActivity.this, holder.nameCity, getString(R.string.transition_city));
                    ActivityCompat.startActivity(WitcomActivitiesActivity.this, mapIntent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class ActivityHolder extends RecyclerView.ViewHolder{

            CardView cv;
            ImageView imageCity;
            TextView nameCity;

            ActivityHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view_city);
                imageCity = (ImageView) itemView.findViewById(R.id.ivCV_city);
                nameCity = (TextView) itemView.findViewById(R.id.tvCV_city);
            }

        }
    }
}
