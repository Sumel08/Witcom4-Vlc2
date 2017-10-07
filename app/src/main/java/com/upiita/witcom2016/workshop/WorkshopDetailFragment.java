package com.upiita.witcom2016.workshop;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.workshop.dummy.DummyContent;

import static com.upiita.witcom2016.pager.WitcomBaseActivity.accent;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.blue;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.dark;

/**
 * A fragment representing a single Workshop detail screen.
 * This fragment is either contained in a {@link WitcomWorkshopActivity}
 * in two-pane mode (on tablets) or a {@link WorkshopDetailActivity}
 * on handsets.
 */
public class WorkshopDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkshopDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.time);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workshop_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView)rootView.findViewById(R.id.date)).setText(Html.fromHtml("<font color=" + accent + ">&lt;Date </font>" +
                    "<font color=" + dark + ">" + mItem.date + "</font>" +
                    "<font color=" + accent + "> /&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.type)).setText(Html.fromHtml("<font color="+blue+">&lt;Workshop&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.place)).setText(Html.fromHtml("<font color="+accent+">&lt;" + getString(R.string.place) + " </font>" +
                    "<font color="+dark+">"+mItem.place+"</font>"+
                    "<font color="+accent+"> /&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.title)).setText(Html.fromHtml("<font color="+accent+">&lt;" + getString(R.string.title) + " </font>" +
                    "<font color="+dark+">"+mItem.title+"</font>"+
                    "<font color="+accent+"> /&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.monitor)).setText(Html.fromHtml("<font color="+accent+">&lt;" + getString(R.string.monitor) + " </font>" +
                    "<font color="+dark+">"+mItem.monitor+"</font>"+
                    "<font color="+accent+"> /&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.about)).setText(Html.fromHtml("<font color="+accent+">&lt;" + getString(R.string.about) + "&gt;</font>"));
            ((TextView) rootView.findViewById(R.id.content)).setText(Html.fromHtml("<font color="+dark+">"+mItem.about+"</font>"));
            ((TextView)rootView.findViewById(R.id.aboutEnd)).setText(Html.fromHtml("<font color="+accent+">&lt;/" + getString(R.string.about) + "&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.note)).setText(Html.fromHtml("<font color="+accent+">&lt;Notes </font>" +
                    "<font color=#FF0000>"+mItem.notes+"</font>"+
                    "<font color="+accent+"> /&gt;</font>"));

            ((TextView)rootView.findViewById(R.id.typeEnd)).setText(Html.fromHtml("<font color="+blue+">&lt;/Workshop&gt;</font>"));

        }

        return rootView;
    }
}
