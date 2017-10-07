package com.upiita.witcom2016.workshop.dummy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.upiita.witcom2016.WitcomLogoActivity;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public DummyContent(Context context) {
        SQLiteDatabase bd = new WitcomDataBase(context).getReadableDatabase();

        Cursor fila = bd.rawQuery("SELECT * FROM workshops", null);


        if( fila.moveToFirst()) {

            do {
                addItem(new DummyItem(String.valueOf(fila.getString(0)), fila.getString(7), fila.getString(8), fila.getString(6), fila.getString(2), fila.getString(1), fila.getString(3), fila.getString(4), fila.getString(5)));
            }
            while (fila.moveToNext());
        }

        bd.close();
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String date;
        public String time;
        public String place;
        public String monitor;
        public String title;
        public String about;
        public String notes;
        public String content;
        public String details;
        public String image;

        public DummyItem(String id, String date, String time, String place, String monitor, String title, String about, String notes, String image) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.place = place;
            this.monitor = monitor;
            this.title = title;
            this.about = about;
            this.notes = notes;
            this.image = image;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
