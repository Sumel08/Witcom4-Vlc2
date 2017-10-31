package com.upiita.witcom2016.pager.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by olemu on 30/10/2017.
 */

public class ChairAdapter extends RecyclerView.Adapter<ChairAdapter.ChairViewHolder> {
    private ArrayList<Chair> chairsList;
    private Context mContext;

    public ChairAdapter(Context mContext, ArrayList<Chair> chairsList) {
        this.chairsList = chairsList;
        this.mContext = mContext;
    }

    @Override
    public ChairAdapter.ChairViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.chair_list_item, parent, false);
        return new ChairViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChairAdapter.ChairViewHolder holder, int position) {
        final Chair chair = chairsList.get(position);

        SQLiteDatabase bd = new WitcomDataBase(mContext).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = " + chair.getImage(), null);
        if (fila.moveToFirst()) {
            do {
                holder.chairImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
            } while (fila.moveToNext());
        }

        fila.close();
        bd.close();

        holder.chairName.setText(chair.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = null;
                String email = chair.getEmail();
                emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WITCOM 2017");

//                Toast.makeText(mContext, "Sending mail to "+t.getText(), Toast.LENGTH_LONG).show();
                mContext.startActivity(Intent.createChooser(emailIntent, "Send Mail"));
//                Toast.makeText(mContext, chair.getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chairsList.size();
    }

    public class ChairViewHolder extends RecyclerView.ViewHolder {
        public TextView chairName;
        public ImageView chairImage;
        public View itemView;

        public ChairViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            chairImage = (ImageView) itemView.findViewById(R.id.imageViewChair);
            chairName = (TextView) itemView.findViewById(R.id.textViewChairName);
        }
    }
}
