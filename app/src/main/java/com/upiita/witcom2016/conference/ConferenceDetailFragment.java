package com.upiita.witcom2016.conference;

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
import com.upiita.witcom2016.conference.dummy.DummyContent;
import com.upiita.witcom2016.util.UtilApp;

import static com.upiita.witcom2016.pager.WitcomBaseActivity.accent;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.blue;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.dark;

/**
 * A fragment representing a single Conference detail screen.
 * This fragment is either contained in a {@link WitcomProgramActivity}
 * in two-pane mode (on tablets) or a {@link ConferenceDetailActivity}
 * on handsets.
 */
public class ConferenceDetailFragment extends Fragment {
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
    public ConferenceDetailFragment() {
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
                appBarLayout.setTitle(mItem.placeName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conference_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            UtilApp.betweenAccents(getContext(), mItem.date, "<", ">", (TextView)rootView.findViewById(R.id.date));

            ((TextView)rootView.findViewById(R.id.type)).setText("<" + mItem.type + ">");

            String speakers = "\n";
            for (String speaker: mItem.speaker) {
                speakers += "* " + speaker + '\n';
            }

            UtilApp.betweenAccents(getContext(), mItem.title, "<", ">", (TextView)rootView.findViewById(R.id.title));
            UtilApp.betweenAccents(getContext(), speakers, "<", ">", (TextView)rootView.findViewById(R.id.auth));
            UtilApp.betweenAccents(getContext(), mItem.from, "<", ">", (TextView)rootView.findViewById(R.id.from));
            ((TextView)rootView.findViewById(R.id.about)).setText("<" + getString(R.string.about_details) + ">");
            ((TextView) rootView.findViewById(R.id.content)).setText(mItem.about);
            ((TextView)rootView.findViewById(R.id.aboutEnd)).setText("<" + getString(R.string.about_details) + ">");
            ((TextView)rootView.findViewById(R.id.typeEnd)).setText("<" + mItem.type+">");
        }

        return rootView;
    }
}
