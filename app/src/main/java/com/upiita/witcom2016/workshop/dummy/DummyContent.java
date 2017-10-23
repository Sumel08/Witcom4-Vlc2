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

    private int city;
    private Context context;

    public DummyContent(int position, Context context) {

        city = position;
        this.context = context;

        initItems();

    }

    private void initItems() {
        SQLiteDatabase bd = new WitcomDataBase(context).getReadableDatabase();

        ArrayList<String> monitors = new ArrayList<>();
        String image = "",
                title = "",
                placeName = "";

        Cursor fila = bd.rawQuery("SELECT * FROM activity WHERE activity_type = " + String.valueOf(city), null);


        if( fila.moveToFirst()) {

            do {
                String[] start_datetime = fila.getString(6).replace("Z", "").split("T");
                String[] end_datetime = fila.getString(7).replace("Z", "").split("T");
                monitors = new ArrayList<>();
                title = fila.getString(1);

                Cursor activityPeopleCursor = bd.rawQuery("SELECT * FROM activity_people WHERE activity = " + fila.getString(0), null);

                if (activityPeopleCursor.moveToFirst()) {

                    do {
                        Cursor peopleCursor = bd.rawQuery("SELECT * FROM people WHERE id = " + activityPeopleCursor.getString(2), null);

                        if (peopleCursor.moveToFirst()) {
                            image = peopleCursor.getString(4);
                            monitors.add(peopleCursor.getString(1) + " " + peopleCursor.getString(2));
                        }
                        peopleCursor.close();
                    } while (activityPeopleCursor.moveToNext());
                }
                activityPeopleCursor.close();

                Cursor placeCursor = bd.rawQuery("SELECT * FROM place WHERE id = " + fila.getString(9), null);

                if (placeCursor.moveToFirst()) {
                    placeName = placeCursor.getString(1);
                }

                addItem(new DummyItem(String.valueOf(fila.getString(0)),
                        start_datetime[0],
                        start_datetime[1],
                        end_datetime[0],
                        end_datetime[1],
                        placeName,
                        monitors,
                        title,
                        fila.getString(3),
                        "\n" + fila.getString(4) + "\n" + "Price" + ": " + fila.getString(5),
                        image));
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
        public String end_date;
        public String end_time;
        public String place;
        public ArrayList<String> monitor;
        public String title;
        public String about;
        public String notes;
        public String content;
        public String details;
        public String image;

        public DummyItem(String id, String date, String time, String end_date, String end_time, String place, ArrayList<String> monitor, String title, String about, String notes, String image) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.end_date = end_date;
            this.end_time = end_time;
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
