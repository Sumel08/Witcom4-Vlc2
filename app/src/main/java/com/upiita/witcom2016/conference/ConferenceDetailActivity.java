package com.upiita.witcom2016.conference;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.conference.dummy.DummyContent;

import java.util.Calendar;

/**
 * An activity representing a single Conference detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link WitcomProgramActivity}.
 */
public class ConferenceDetailActivity extends AppCompatActivity {

    AppCompatActivity appCompatActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCalendarEvent();
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ConferenceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ConferenceDetailFragment.ARG_ITEM_ID));
            ConferenceDetailFragment fragment = new ConferenceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.conference_detail_container, fragment)
                    .commit();
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
            navigateUpTo(new Intent(this, WitcomProgramActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addCalendarEvent() {
        DummyContent.DummyItem mItem = DummyContent.ITEM_MAP.get(getIntent().getStringExtra(ConferenceDetailFragment.ARG_ITEM_ID));

                /*
                    date[0] -> Day
                    date[1] -> Month
                    date[2] -> Year
                */
        String date[] = mItem.date.split("-");
        String endDate[] = mItem.end_date.split("-");
        String beginTime[] = mItem.time.split(":");
        String endTime[] = mItem.end_time.split(":");

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        cal.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]), Integer.parseInt(beginTime[0]), Integer.parseInt(beginTime[1]));
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        cal.set(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[2]), Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        intent.putExtra(CalendarContract.Events.TITLE, mItem.title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "UPIITA, UPIITA, Av. IPN 2580, Mexico City, MX");
        startActivity(intent);
    }

}
