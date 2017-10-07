package com.upiita.witcom2016.tourism.place;

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
public class PlaceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<PlaceItem> ITEMS = new ArrayList<PlaceItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, PlaceItem> ITEM_MAP = new HashMap<String, PlaceItem>();

    private final int COUNT = 25;
    private int city;
    private Context context;

    public PlaceContent (int position, Context context) {

        city = position;
        this.context = context;

        initItems();
    }

    private void initItems() {
        // Add some sample items.
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/

        SQLiteDatabase bd = new WitcomDataBase(context).getReadableDatabase();
        Cursor fila = bd.rawQuery("SELECT * FROM places", null);
        if (fila.moveToFirst()) {
            do {
                if (city == fila.getInt(1)) {
                    addItem(new PlaceItem(String.valueOf(fila.getInt(0)),
                            fila.getString(2),
                            fila.getString(3),
                            fila.getString(2),
                            fila.getString(3),
                            fila.getString(4),
                            fila.getString(5),
                            fila.getString(6),
                            fila.getDouble(7),
                            fila.getDouble(8),
                            fila.getString(9),
                            fila.getString(10),
                            fila.getString(11),
                            fila.getString(12)));
                }
            } while (fila.moveToNext());
        }

        fila.close();
        bd.close();
    }

    private void addItem(PlaceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class PlaceItem {
        public final String id;
        public final String content;
        public final String details;

        public final String place;
        public final String description;
        public final String address;
        public final String schedule;
        public final String extra;
        public final double latitude;
        public final double longitude;
        public final String id_image;
        public final String cost;
        public final String telephone;
        public final String webPage;

        public PlaceItem(String id, String content, String details, String place, String description, String address, String schedule, String extra, double latitude, double longitude, String id_image, String cost, String telephone, String webPage) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.place = place;
            this.description = description;
            this.address = address;
            this.schedule = schedule;
            this.extra = extra;
            this.latitude = latitude;
            this.longitude = longitude;
            this.id_image = id_image;
            this.cost = cost;
            this.telephone = telephone;
            this.webPage = webPage;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
