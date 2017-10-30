package com.upiita.witcom2016.dataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oscar on 11/10/16.
 */

public class WitcomDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Witcom.db";

    public static final String ACTIVITY_TABLE = "CREATE TABLE activity (" +
            "id         TEXT," +
            "title   TEXT," +
            "subtitle   TEXT," +
            "description    TEXT," +
            "notes    TEXT," +
            "price    TEXT," +
            "start_date    TEXT," +
            "end_date    TEXT," +
            "activity_type    TEXT," +
            "place    TEXT," +
            "schedule    TEXT" +
            ");";

    public static final String ACTIVITY_PEOPLE_TABLE = "CREATE TABLE activity_people (" +
            "id         TEXT," +
            "activity   TEXT," +
            "person    TEXT" +
            ");";

    public static final String ACTIVITY_TYPE_TABLE = "CREATE TABLE activity_type (" +
            "id         TEXT," +
            "name   TEXT," +
            "description   TEXT," +
            "created   TEXT," +
            "show_in_app   TEXT," +
            "image   TEXT," +
            "event    TEXT," +
            "show_speakers_in_app   TEXT" +
            ");";

    public static final String CHAIRS_TABLE = "CREATE TABLE chairs (" +
            "id         TEXT," +
            "event   TEXT," +
            "person    TEXT" +
            ");";

    public static final String DEVELOPERS_TABLE = "CREATE TABLE developers (" +
            "id         TEXT," +
            "event   TEXT," +
            "person    TEXT" +
            ");";

    public static final String EVENT_TABLE = "CREATE TABLE event (" +
            "id         TEXT," +
            "code   TEXT," +
            "description   TEXT," +
            "end_date    TEXT," +
            "event_image    TEXT," +
            "name    TEXT," +
            "place    TEXT," +
            "schedule    TEXT," +
            "start_date    TEXT" +
            ");";

    public static final String IMAGES_TABLE = "CREATE TABLE images (" +
            "id     TEXT," +
            "image  BLOB" +
            ")";

    public static final String PEOPLE_TABLE = "CREATE TABLE people (" +
            "id            TEXT," +
            "name       TEXT," +
            "surname         TEXT," +
            "birthdate   TEXT," +
            "photo       TEXT," +
            "resume      TEXT," +
            "email         TEXT," +
            "phone      TEXT," +
            "provenance      TEXT," +
            "event     TEXT" +
            ")";

    public static final String PEOPLE_SOCIAL_NETWORKS_TABLE = "CREATE TABLE people_social_networks (" +
            "id         TEXT," +
            "person   TEXT," +
            "social_network    TEXT" +
            ");";

    public static final String PLACE_TABLE = "CREATE TABLE place (" +
            "id            TEXT," +
            "name       TEXT," +
            "description         TEXT," +
            "longitude   TEXT," +
            "latitude       TEXT," +
            "altitude      TEXT," +
            "indication         TEXT," +
            "additional_info      TEXT," +
            "website     TEXT," +
            "email      TEXT," +
            "telephone          TEXT," +
            "image     TEXT," +
            "place_category       TEXT" +
            ")";

    public static final String PLACE_CATEGORY_TABLE = "CREATE TABLE place_category (" +
            "id         TEXT," +
            "name   TEXT," +
            "description   TEXT," +
            "show_in_app   TEXT," +
            "event    TEXT" +
            ");";

    public static final String PLACE_SOCIAL_NETWORKS_TABLE = "CREATE TABLE place_social_networks (" +
            "id         TEXT," +
            "place   TEXT," +
            "social_network    TEXT" +
            ");";

    public static final String SCHEDULE_TABLE = "CREATE TABLE schedule (" +
            "id         TEXT," +
            "created_on    TEXT," +
            "event    TEXT" +
            ");";

    public static final String SOCIAL_NETWORKS_TABLE = "CREATE TABLE social_networks (" +
            "id         TEXT," +
            "url         TEXT," +
            "domain        TEXT," +
            "event    TEXT" +
            ");";

    public static final String SPONSORS_TABLE = "CREATE TABLE sponsors (" +
            "id         TEXT," +
            "event         TEXT," +
            "person    TEXT" +
            ");";

    public static final String STREAMS_TABLE = "CREATE TABLE streams (" +
            "id         TEXT," +
            "url         TEXT," +
            "description         TEXT," +
            "event    TEXT" +
            ");";

    public static final String SKETCH_TABLE = "CREATE TABLE sketch (" +
            "id         TEXT," +
            "image_url         TEXT," +
            "description         TEXT" +
            ");";

    /*public static final String CONFERENCES_TABLE = "CREATE TABLE conferences (" +
            "id         TEXT," +
            "title      TEXT," +
            "speaker    TEXT," +
            "come       TEXT," +
            "details    TEXT," +
            "notes      TEXT," +
            "id_image   TEXT" +
            ")";

    public static final String OTHER_TABLE = "CREATE TABLE other (" +
            "id         TEXT," +
            "title      TEXT," +
            "second     TEXT," +
            "third      TEXT," +
            "id_image   TEXT" +
            ")";
    public static final String PAPERS_TABLE = "CREATE TABLE papers (" +
            "id         TEXT," +
            "title      TEXT," +
            "author     TEXT," +
            "details    TEXT," +
            "notes      TEXT," +
            "Field6     TEXT" +
            ")";
    public static final String SCHEDULE_TABLE = "CREATE TABLE schedule (" +
            "id             TEXT," +
            "id_activity    TEXT," +
            "date           TEXT," +
            "time           TEXT," +
            "id_details     TEXT" +
            ")";
    public static final String WORKSHOPS_TABLE = "CREATE TABLE workshops (" +
            "id         TEXT," +
            "title      TEXT," +
            "monitor    TEXT," +
            "detail     TEXT," +
            "notes      TEXT," +
            "id_image   TEXT," +
            "place      TEXT," +
            "date      TEXT," +
            "time      TEXT" +
            ")";

    public static final String CITIES_TABLE = "CREATE TABLE cities (" +
            "id     TEXT," +
            "city   TEXT," +
            "id_image  TEXT" +
            ")";*/

    public static final String VERSION_TABLE = "CREATE TABLE version (" +
            "info_version   TEXT" +
            ")";

    public WitcomDataBase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACTIVITY_TABLE);
        db.execSQL(ACTIVITY_PEOPLE_TABLE);
        db.execSQL(ACTIVITY_TYPE_TABLE);
        db.execSQL(CHAIRS_TABLE);
        db.execSQL(DEVELOPERS_TABLE);
        db.execSQL(EVENT_TABLE);
        db.execSQL(IMAGES_TABLE);
        db.execSQL(PEOPLE_TABLE);
        db.execSQL(PEOPLE_SOCIAL_NETWORKS_TABLE);
        db.execSQL(PLACE_TABLE);
        db.execSQL(PLACE_CATEGORY_TABLE);
        db.execSQL(PLACE_SOCIAL_NETWORKS_TABLE);
        db.execSQL(SCHEDULE_TABLE);
        db.execSQL(SOCIAL_NETWORKS_TABLE);
        db.execSQL(SPONSORS_TABLE);
        db.execSQL(STREAMS_TABLE);
        db.execSQL(VERSION_TABLE);
        db.execSQL(SKETCH_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
