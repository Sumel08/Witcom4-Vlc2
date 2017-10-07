package com.upiita.witcom2016.pager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.conference.WitcomProgramActivity;
import com.upiita.witcom2016.sketch.WitcomSketchActivity;
import com.upiita.witcom2016.speaker.WitcomSpeakerActivity;
import com.upiita.witcom2016.streaming.StreamingActivity;
import com.upiita.witcom2016.tourism.WitcomTourismActivity;
import com.upiita.witcom2016.workshop.WitcomWorkshopActivity;
import com.viewpagerindicator.IconPagerAdapter;

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


            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">"+ getContext().getString(R.string.stream) +"</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.streaming));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                textView.setText(Html.fromHtml("<font color=" + accent + ">{ </font><font color=" + textWhite + ">" + getContext().getString(R.string.conferences) + "</font><font color=" + accent + "> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.conference));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==3) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.speakers) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.speaker));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==4) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.workshops) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.workshop));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==5) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.how_to_arrive) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.map));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==6) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.sketch) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.sketch));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==7) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.tourism) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.tourism));
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==8) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+textWhite+">" + getString(R.string.sponsor) + "</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.sponsors));
            }


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
                        Intent intent = new Intent(rootView.getContext(), WitcomWorkshopActivity.class);
                        intent.putExtra("page", mPager.getCurrentItem()+1);
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                    }

                    else if(mPager.getCurrentItem() == 4) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
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
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + "21.0152018" + "," + "-101.5028277");
                                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                            mapIntent.setPackage("com.google.android.apps.maps");
                                                            startActivity(mapIntent);
                                                            dialog.cancel();
                                                        }
                                                        else if(rbb.isChecked()) {
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + "21.0152018" + "," + "-101.5028277" + "&mode=b");
                                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                            mapIntent.setPackage("com.google.android.apps.maps");
                                                            startActivity(mapIntent);
                                                            dialog.cancel();
                                                        }
                                                        else if(rbw.isChecked()) {
                                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + "21.0152018" + "," + "-101.5028277" + "&mode=w");
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
                        new AlertDialog.Builder(getContext())
                                .setView(R.layout.about)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    }
                }
            });

            return rootView;

        }
    }
}
