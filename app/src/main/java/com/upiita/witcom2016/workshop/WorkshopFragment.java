package com.upiita.witcom2016.workshop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom2016.workshop.dummy.DummyContent;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 2/10/16.
 */

public class WorkshopFragment extends Fragment {

    private List<DummyContent.DummyItem> itemsDay;
    private String dayDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_witcom_workshop, container, false);

        dayDate = getArguments().getString("date");
        itemsDay = new ArrayList<>();

        View recyclerView = rootView.findViewById(R.id.workshop_list_fragment);
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
                    .inflate(R.layout.workshop_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).title);

            SQLiteDatabase bd = new WitcomDataBase(getContext()).getReadableDatabase();
            Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = '" + holder.mItem.image +"'", null);

            if (fila.moveToFirst())
                holder.mImageView.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
            bd.close();

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, WorkshopDetailActivity.class);
                        intent.putExtra(WorkshopDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), holder.mImageView, getString(R.string.transition_workshop));
                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                        //context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final ImageView mImageView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.workshop_list_id);
                mContentView = (TextView) view.findViewById(R.id.workshop_list_content);
                mImageView = (ImageView) view.findViewById(R.id.workshop_list_image);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
