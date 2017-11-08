package com.upiita.witcom2016.workshop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;
import com.upiita.witcom2016.workshop.dummy.DummyContent;
import com.upiita.witcom2016.workshop.utils.ActivityType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import static com.upiita.witcom2016.workshop.dummy.DummyContent.ITEMS;
import static com.upiita.witcom2016.workshop.dummy.DummyContent.ITEM_MAP;

public class WitcomWorkshopActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DummyContent activities;
    private int activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_workshop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.notifyDataSetChanged();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        activityType = extras.getInt("city_id");
        ((TextView)findViewById(R.id.activity_title_details)).setText(extras.getString("city_name"));

        ITEMS.clear();
        ITEM_MAP.clear();
        activities =  new DummyContent(activityType, getApplicationContext());

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

            Intent intent = new Intent(this, WitcomActivitiesActivity.class);
            intent.putExtra("page", getIntent().getIntExtra("page", 1));

            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private LinkedHashSet<String> dates = new LinkedHashSet<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            WorkshopFragment workshop = new WorkshopFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", position + 1);
            args.putString("date", getPos(dates, position));
            workshop.setArguments(args);
            return workshop;
        }

        @Override
        public int getCount() {
            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            //Cursor fila = bd.rawQuery("SELECT start_date FROM activity WHERE activity_type = " + String.valueOf(activityType), null);
            Cursor fila = bd.rawQuery("SELECT start_date FROM activity ", null);

            if (fila.moveToFirst()) {
                do {
                    String[] aux_date = fila.getString(0).split("T");
                    dates.add(aux_date[0]);
                } while (fila.moveToNext());
            }

            fila.close();
            bd.close();

            return dates.size();
            //return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] date = getPos(dates, position).split("-");
            //date[2] = "20" + date[2];
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", getResources().getConfiguration().locale);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));

            weekDay = dayFormat.format(calendar.getTime()) + " " + date[2];

            return weekDay;
            //return "Workshops";
        }

        private String getPos(LinkedHashSet<String> set, int position) {

            String aux = "";
            Iterator<String> iterator = dates.iterator();

            for (int i = 0; i<position+1; i++) {
                if (iterator.hasNext())
                    aux = iterator.next();
                else
                    aux = "mal";
            }

            return aux;
        }
    }
}
