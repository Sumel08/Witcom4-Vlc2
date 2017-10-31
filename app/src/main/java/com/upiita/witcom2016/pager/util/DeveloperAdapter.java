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
import android.widget.TextView;

import com.upiita.witcom2016.R;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by olemu on 30/10/2017.
 */

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder> {
    private ArrayList<Developer> developersList;
    private Context mContext;

    public DeveloperAdapter(Context mContext, ArrayList<Developer> developersList) {
        this.developersList = developersList;
        this.mContext = mContext;
    }

    @Override
    public DeveloperAdapter.DeveloperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.developer_list_item, parent, false);
        return new DeveloperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeveloperAdapter.DeveloperViewHolder holder, int position) {
        final Developer developer = developersList.get(position);

        /*SQLiteDatabase bd = new WitcomDataBase(mContext).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT image FROM images WHERE id = " + developer.getImage(), null);
        if (fila.moveToFirst()) {
            do {
                holder.chairImage.setImageBitmap(BitmapFactory.decodeStream(new ByteArrayInputStream(fila.getBlob(0))));
            } while (fila.moveToNext());
        }

        fila.close();
        bd.close();*/

        holder.developerName.setText(developer.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = null;
                String email = developer.getEmail();
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
        return developersList.size();
    }

    public class DeveloperViewHolder extends RecyclerView.ViewHolder {
        public TextView developerName;
//        public ImageView chairImage;
        public View itemView;

        public DeveloperViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
//            chairImage = (ImageView) itemView.findViewById(R.id.imageViewChair);
            developerName = (TextView) itemView.findViewById(R.id.textViewDeveloperName);
        }
    }
}

