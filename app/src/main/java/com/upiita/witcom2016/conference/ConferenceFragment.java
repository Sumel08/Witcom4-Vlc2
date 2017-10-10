package com.upiita.witcom2016.conference;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.conference.dummy.DummyContent;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.upiita.witcom2016.pager.WitcomBaseActivity.accent;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.blue;
import static com.upiita.witcom2016.pager.WitcomBaseActivity.dark;

/**
 * Created by oscar on 2/10/16.
 */

public class ConferenceFragment extends Fragment {

    private List<DummyContent.DummyItem> itemsDay;
    private String dayDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_witcom_program, container, false);

        dayDate = getArguments().getString("date");
        itemsDay = new ArrayList<>();

        View recyclerView = rootView.findViewById(R.id.conference_list_fragment);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        for (DummyContent.DummyItem dummyItem: DummyContent.ITEMS) {
            if (dummyItem.date.equals(dayDate))
                itemsDay.add(dummyItem);
        }

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(itemsDay));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.conference_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mTypeView.setText(holder.mItem.type);
            holder.mContentView.setText(holder.mItem.title);
            holder.mTimeView.setText(holder.mItem.time);

            SQLiteDatabase bd = new WitcomDataBase(getContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + holder.mItem.image + "'", null);
            if (fila.moveToFirst()) {
                do {
                    holder.mImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
                } while (fila.moveToNext());
            }

            fila.close();
            bd.close();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ConferenceDetailActivity.class);
                        intent.putExtra(ConferenceDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            //public final TextView mIdView;
            public final TextView mContentView;
            public final TextView mTimeView;
            public final ImageView mImage;
            public final TextView mTypeView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ///mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mTimeView = (TextView) view.findViewById(R.id.time);
                mImage = (ImageView) view.findViewById(R.id.schedule_image);
                mTypeView = (TextView) view.findViewById(R.id.conference_type);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
