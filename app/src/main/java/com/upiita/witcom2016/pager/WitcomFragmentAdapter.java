package com.upiita.witcom2016.pager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.conference.WitcomProgramActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.events.EventActivity;
import com.upiita.witcom2016.events.aboutUtil.Sponsor;
import com.upiita.witcom2016.sketch.WitcomSketchActivity;
import com.upiita.witcom2016.speaker.WitcomSpeakerActivity;
import com.upiita.witcom2016.streaming.StreamingActivity;
import com.upiita.witcom2016.tourism.WitcomTourismActivity;
import com.upiita.witcom2016.util.UtilApp;
import com.upiita.witcom2016.workshop.WitcomActivitiesActivity;
import com.upiita.witcom2016.workshop.WitcomWorkshopActivity;
import com.viewpagerindicator.IconPagerAdapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.upiita.witcom2016.WitcomLogoActivity.URL_STREAM;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.accent;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.mPager;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.textWhite;

/**
 * Created by oscar on 27/09/16.
 */

public class WitcomFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    private static final String[] CONTENT = new String[] { "Tourism", "Speakers", "Workshops", "Test", };
    private static final int[] ICONS = new int[] {
            R.drawable.perm_group_streaming,
            R.drawable.perm_group_conferences,
            R.drawable.perm_group_speaker,
            R.drawable.perm_group_workshops,
            R.drawable.perm_group_howtoarrive,
            R.drawable.perm_group_sketch,
            R.drawable.perm_group_tourism,
            R.drawable.perm_group_sponsors
    };

    private int mCount = CONTENT.length;

    public WitcomFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //return TestFragment.newInstance(CONTENT[position % CONTENT.length]);
        return PlaceholderFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return WitcomFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.pager_section_label);
            ImageView imgV = (ImageView)rootView.findViewById(R.id.pager_image_section);

            String title = "";

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    title = getContext().getString(R.string.stream);
                    imgV.setImageResource(R.drawable.streaming);
                    break;
                case  2:
                    title = getContext().getString(R.string.conferences);
                    imgV.setImageResource(R.drawable.conference);
                    break;
                case 3:
                    title = getString(R.string.speakers);
                    imgV.setImageResource(R.drawable.speaker);
                    break;
                case 4:
                    title = getString(R.string.workshops);
                    imgV.setImageResource(R.drawable.workshop);
                    break;
                case 5:
                    title = getString(R.string.how_to_arrive);
                    imgV.setImageResource(R.drawable.map);
                    break;
                case 6:
                    title = getString(R.string.sketch);
                    imgV.setImageResource(R.drawable.sketch);
                    break;
                case 7:
                    title = getString(R.string.tourism);
                    imgV.setImageResource(R.drawable.tourism);
                    break;
                case 8:
                    title = getString(R.string.sponsor);
                    imgV.setImageResource(R.drawable.sponsors);
                    break;
            }

            UtilApp.betweenAccents(getContext(), title, "{", "}", textView);
            imgV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), rootView.findViewById(R.id.pager_image_section), getString(R.string.transition_pager));

                    if(mPager.getCurrentItem() == 0) {

                            startActivity(new Intent(getActivity(), StreamingActivity.class));
                    }
                    else if(mPager.getCurrentItem() == 1) {

                        Intent intent = new Intent(rootView.getContext(), WitcomProgramActivity.class);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                    }

                    else if(mPager.getCurrentItem() == 2) {

                        Intent intent = new Intent(rootView.getContext(), WitcomSpeakerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                        //ActivityCompat.startActivity(getActivity(), new Intent(rootView.getContext(), WitcomProgramActivity.class), options.toBundle());
                    }

                    else if(mPager.getCurrentItem() == 3) {
                        Intent intent = new Intent(rootView.getContext(), WitcomActivitiesActivity.class);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }

                    else if(mPager.getCurrentItem() == 4) {

                        String longitude = "-99.126226";
                        String latitude = "19.511371";

                        /*Obtenemos las coordenadas del evento, por default serán upiita*/
                        SQLiteDatabase db = new WitcomDataBase(getContext()).getReadableDatabase();
                        Cursor eventPlaceCursor = db.rawQuery("SELECT place FROM event", null);
                        if (eventPlaceCursor.moveToFirst()) {
                            Cursor placeCursor = db.rawQuery("SELECT * FROM place WHERE id = " + eventPlaceCursor.getString(0), null);
                            if (placeCursor.moveToFirst()) {
                                longitude = placeCursor.getString(3);
                                latitude = placeCursor.getString(4);
                            }
                            placeCursor.close();
                        }
                        eventPlaceCursor.close();
                        db.close();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                        final String finalLongitude = longitude;
                        final String finalLatitude = latitude;
                        alertDialogBuilder
                                .setCancelable(false)
                                .setTitle(getString(R.string.navigation))
                                .setMessage(getString(R.string.data_usage))
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LayoutInflater inflater = (LayoutInflater)rootView.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                        final View layout = inflater.inflate(R.layout.navigationmap, null);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                                        alertDialogBuilder
                                                .setTitle(getString(R.string.navigation))
                                                .setMessage(getString(R.string.how_get))
                                                .setCancelable(false)
                                                .setView(layout)
                                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        RadioButton rbc = (RadioButton)layout.findViewById(R.id.car);
                                                        RadioButton rbb = (RadioButton)layout.findViewById(R.id.bike);
                                                        RadioButton rbw = (RadioButton)layout.findViewById(R.id.walk);

                                                        if(rbc.isChecked()) {
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + finalLatitude + "," + finalLongitude);
                                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                            mapIntent.setPackage("com.google.android.apps.maps");
                                                            startActivity(mapIntent);
                                                            dialog.cancel();
                                                        }
                                                        else if(rbb.isChecked()) {
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + finalLatitude + "," + finalLongitude + "&mode=b");
                                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                            mapIntent.setPackage("com.google.android.apps.maps");
                                                            startActivity(mapIntent);
                                                            dialog.cancel();
                                                        }
                                                        else if(rbw.isChecked()) {
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + finalLatitude + "," + finalLongitude + "&mode=w");
                                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                            mapIntent.setPackage("com.google.android.apps.maps");
                                                            startActivity(mapIntent);
                                                            dialog.cancel();
                                                        }

                                                    }
                                                })
                                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                    else if(mPager.getCurrentItem() == 5) {
                        Intent intent = new Intent(rootView.getContext(),WitcomSketchActivity.class);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }
                    else if(mPager.getCurrentItem() == 6) {
                        Intent intent = new Intent(rootView.getContext(),WitcomTourismActivity.class);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }
                    else if(mPager.getCurrentItem() == 7) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setView(R.layout.about)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        ArrayList<Sponsor> sposorList = new ArrayList<>();

                        SQLiteDatabase db = new WitcomDataBase(getContext()).getReadableDatabase();
                        Cursor sponsorsCursor = db.rawQuery("SELECT person FROM sponsors", null);
                        if (sponsorsCursor.moveToFirst()) {
                            do {
                                Cursor peopleCursor = db.rawQuery("SELECT * FROM people WHERE id = " + sponsorsCursor.getString(0), null);
                                if (peopleCursor.moveToFirst()) {
                                    Log.d("SPONSORTAG", peopleCursor.getString(1) + ":" + peopleCursor.getString(4));
                                    sposorList.add(new Sponsor(peopleCursor.getString(1), peopleCursor.getString(4)));
                                }
                            } while (sponsorsCursor.moveToNext());
                        }
                        sponsorsCursor.close();


                        RecyclerView sponsors = (RecyclerView) alertDialog.findViewById(R.id.sponsorsRecycler);
                        SponsorAdapter adapter = new SponsorAdapter(getContext(), sposorList);

                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                        sponsors.setLayoutManager(mLayoutManager);
                        sponsors.setItemAnimator(new DefaultItemAnimator());
                        sponsors.setAdapter(adapter);

                    }
                }
            });

            return rootView;

        }
    }

    public static class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.SponsorViewHolder> {
        private ArrayList<Sponsor> sponsorList;
        private Context mContext;

        public SponsorAdapter(Context mContext, ArrayList<Sponsor> sponsorList) {
            this.sponsorList = sponsorList;
            this.mContext = mContext;
        }

        @Override
        public SponsorAdapter.SponsorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.sponsor_card, parent, false);
            return new SponsorViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SponsorAdapter.SponsorViewHolder holder, int position) {
            final Sponsor sponsor = sponsorList.get(position);

            SQLiteDatabase bd = new WitcomDataBase(mContext).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = " + sponsor.getImage(), null);
            if (fila.moveToFirst()) {
                do {
                    holder.sponsorImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
                } while (fila.moveToNext());
            }

            fila.close();
            bd.close();
        }

        @Override
        public int getItemCount() {
            return sponsorList.size();
        }

        public class SponsorViewHolder extends RecyclerView.ViewHolder {
            public ImageView sponsorImage;

            public SponsorViewHolder(View itemView) {
                super(itemView);
                sponsorImage = (ImageView) itemView.findViewById(R.id.imageViewSponsor);
            }
        }
    }
}
