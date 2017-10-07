package com.upiita.witcom2016.conference.dummy;

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

    public DummyContent (Context context) {
        SQLiteDatabase bd = new WitcomDataBase(context).getReadableDatabase();

        String id = "", title = "", speaker = "", from = "", about = "", date = "", time = "", type = "", image = "";
        Cursor fila = bd.rawQuery("SELECT * FROM schedule", null);

        if (fila.moveToFirst()) {

            do {
                id = fila.getString(0);
                date = fila.getString(2);
                time = fila.getString(3);

                Cursor fila2 = bd.rawQuery("SELECT * FROM activities WHERE id = " + fila.getInt(1), null);

                if (fila2.moveToFirst()) {
                    image = fila2.getString(2);
                    type = fila2.getString(1);
                }

                Cursor fila3 = bd.rawQuery("SELECT * FROM " + fila2.getString(3) + " WHERE id = " + fila.getInt(4), null);

                if (fila3.moveToFirst()) {
                    title = fila3.getString(1);
                    speaker = fila3.getString(2);
                    from = fila3.getString(3);
                    about = fila3.getString(4);
                    if (fila2.getString(3).equals("conferences"))
                        title = fila3.getString(5) + fila3.getString(1);
                }

                addItem(new DummyItem(id, title, speaker, from, about, date, time, type, image));

            }
            while (fila.moveToNext());
        }

        fila.close();
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
        public final String id;
        public final String title;
        public final String speaker;
        public final String from;
        public final String about;
        public final String date;
        public final String time;
        public final String type;
        public final String image;

        public DummyItem(String id, String title, String speaker, String from, String about, String date, String time, String type, String image) {
            this.id = id;
            this.title = title;
            this.speaker = speaker;
            this.from = from;
            this.about = about;
            this.date = date;
            this.time = time;
            this.type = type;
            this.image = image;
        }

        @Override
        public String toString() {
            return type + ": " + title;
        }
    }
}
