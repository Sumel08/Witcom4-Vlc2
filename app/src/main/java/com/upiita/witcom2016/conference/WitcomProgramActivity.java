package com.upiita.witcom2016.conference;

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

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.conference.dummy.DummyContent;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.pager.WitcomPagerActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static com.upiita.witcom2016.conference.dummy.DummyContent.ITEMS;
import static com.upiita.witcom2016.conference.dummy.DummyContent.ITEM_MAP;

public class WitcomProgramActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witcom_program);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

        ITEMS.clear();
        ITEM_MAP.clear();
        new DummyContent(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String not = bundle.getString("fromNotification", "notNotification");
            if (!not.equals("notNotification")) {
                Intent intent = new Intent(this, ConferenceDetailActivity.class);
                intent.putExtra(ConferenceDetailFragment.ARG_ITEM_ID, not);

                this.startActivity(intent);
            }
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

            ConferenceFragment conference = new ConferenceFragment();
            Bundle args = new Bundle();
            args.putInt("section_number", position + 1);
            args.putString("date", getPos(dates, position));
            conference.setArguments(args);

            return conference;
        }

        @Override
        public int getCount() {
            SQLiteDatabase bd = new WitcomDataBase(getApplicationContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT start_date FROM activity", null);

            if (fila.moveToFirst()) {
                do {
                    String[] aux_date = fila.getString(0).split("T");
                    dates.add(aux_date[0]);
                } while (fila.moveToNext());

            }
            fila.close();
            bd.close();

            return dates.size();
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
