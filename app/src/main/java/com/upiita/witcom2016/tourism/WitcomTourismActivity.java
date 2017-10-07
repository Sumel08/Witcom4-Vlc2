package com.upiita.witcom2016.tourism;

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
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;
import com.upiita.witcom2016.tourism.place.PlaceContent;
import com.upiita.witcom2016.tourism.utils.City;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class WitcomTourismActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_tourism);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        final ArrayList<City> cities = new ArrayList<>();

        SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM cities", null);
        if (fila.moveToFirst()) {
            do {
                cities.add(new City(fila.getString(1), fila.getString(2)));
            } while (fila.moveToNext());
        }

        fila.close();
        bd.close();

        TourismAdapter adapter = new TourismAdapter(cities);
        mRecyclerView.setAdapter(adapter);

        /*mRecyclerView.addOnItemTouchListener(
                new RecyclerCityClickListener(getApplicationContext(), mRecyclerView ,new RecyclerCityClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle extras = new Bundle();
                        extras.putInt("city_id", position+1);
                        extras.putString("city_name", cities.get(position).getCity());
                        Intent mapIntent = new Intent(WitcomTourismActivity.this, PlaceListActivity.class);
                        mapIntent.putExtras(extras);
                        PlaceContent.ITEM_MAP.clear();
                        PlaceContent.ITEMS.clear();

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WitcomTourismActivity.this, holder.mImageView, getString(R.string.transition_name));
                        startActivity(mapIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );*/
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

    public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.CityHolder> {

        ArrayList<City> cities;

        public TourismAdapter(ArrayList<City> cities) {
            this.cities = cities;
        }

        @Override
        public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_view, parent, false);
            CityHolder ctyh = new CityHolder(v);
            return ctyh;
        }

        @Override
        public void onBindViewHolder(final CityHolder holder, final int position) {
            holder.nameCity.setText(cities.get(position).getCity());
            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + cities.get(position).getImageCity() + "'", null);
            if (fila.moveToFirst())
                holder.imageCity.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));

            fila.close();
            bd.close();

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    extras.putInt("city_id", position+1);
                    extras.putString("city_name", cities.get(position).getCity());
                    Intent mapIntent = new Intent(WitcomTourismActivity.this, PlaceListActivity.class);
                    mapIntent.putExtras(extras);
                    PlaceContent.ITEM_MAP.clear();
                    PlaceContent.ITEMS.clear();

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(WitcomTourismActivity.this, holder.nameCity, getString(R.string.transition_city));
                    ActivityCompat.startActivity(WitcomTourismActivity.this, mapIntent, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class CityHolder extends RecyclerView.ViewHolder{

            CardView cv;
            ImageView imageCity;
            TextView nameCity;

            CityHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.card_view_city);
                imageCity = (ImageView) itemView.findViewById(R.id.ivCV_city);
                nameCity = (TextView) itemView.findViewById(R.id.tvCV_city);
            }

        }
    }

}
