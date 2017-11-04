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

        String id = "", title = "", from = "", about = "", date = "", end_date = "", time = "", end_time = "", type = "", image = "", placeName = "", placeImage = "";
        ArrayList<String> speaker = new ArrayList<>();
        Cursor fila = bd.rawQuery("SELECT * FROM activity", null);

        if (fila.moveToFirst()) {

            do {
                id = fila.getString(0);
                title = fila.getString(1);
                String[] datetime_aux = fila.getString(6).split("T");
                date = datetime_aux[0];
                time = datetime_aux[1].replace("Z", "");
                String[] end_datetime_aux = fila.getString(7).split("T");
                end_date = end_datetime_aux[0];
                end_time = end_datetime_aux[1].replace("Z", "");
                about = fila.getString(3);
                speaker = new ArrayList<>();

                Cursor fila2 = bd.rawQuery("SELECT * FROM activity_type WHERE id = " + fila.getInt(8), null);

                if (fila2.moveToFirst()) {
                    image = fila2.getString(5);
                    type = fila2.getString(1);
                }
                fila2.close();

                Cursor fila3 = bd.rawQuery("SELECT * FROM activity_people WHERE activity = " + fila.getInt(0), null);

                if (fila3.moveToFirst()) {

                    do {
                        Cursor fila4 = bd.rawQuery("SELECT * FROM people WHERE id = " + fila3.getInt(2), null);
                        if (fila4.moveToFirst()) {
                            speaker.add(fila4.getString(1) + " " + fila4.getString(2));
                        }
                    } while (fila3.moveToNext());

                }
                fila3.close();

                Cursor placeCursor = bd.rawQuery("SELECT * FROM place WHERE id = " + fila.getString(9), null);

                if (placeCursor.moveToFirst()) {
                    placeName = placeCursor.getString(1);
                    placeImage = placeCursor.getString(11);
                }
                placeCursor.close();

                addItem(new DummyItem(id, title, speaker, from, about, date, end_date, time, end_time, type, image, placeName, placeImage));

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
        public final ArrayList<String> speaker;
        public final String from;
        public final String about;
        public final String date;
        public final String end_date;
        public final String time;
        public final String end_time;
        public final String type;
        public final String image;
        public final String placeName;
        public final String placeImage;

        public DummyItem(String id, String title, ArrayList<String> speaker, String from, String about, String date, String end_date, String time, String end_time ,String type, String image, String placeName, String placeImage) {
            this.id = id;
            this.title = title;
            this.speaker = speaker;
            this.from = from;
            this.about = about;
            this.date = date;
            this.end_date = end_date;
            this.time = time;
            this.end_time = end_time;
            this.type = type;
            this.image = image;
            this.placeName = placeName;
            this.placeImage = placeImage;
        }

        @Override
        public String toString() {
            return type + ": " + title;
        }
    }
}
