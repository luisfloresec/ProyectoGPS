package com.example.proyectogps;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GPSDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "gpsdata.db";
    private static final int DB_VERSION = 1;
    private static GPSDatabase instance;

    public static synchronized GPSDatabase getInstance(Context context) {
        if (instance == null)
            instance = new GPSDatabase(context.getApplicationContext());
        return instance;
    }

    public GPSDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE gps (id INTEGER PRIMARY KEY AUTOINCREMENT, lat REAL, lng REAL, timestamp LONG)");
        db.execSQL("CREATE TABLE auth (username TEXT, password TEXT)");
        db.execSQL("INSERT INTO auth VALUES ('admin', '1234')");
    }

    public void insertLocation(double lat, double lng, long timestamp) {
        ContentValues values = new ContentValues();
        values.put("lat", lat);
        values.put("lng", lng);
        values.put("timestamp", timestamp);
        getWritableDatabase().insert("gps", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
