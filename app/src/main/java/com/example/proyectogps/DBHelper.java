package com.example.proyectogps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ubicaciones.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ubicaciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "latitud REAL, " +
                "longitud REAL, " +
                "timestamp INTEGER, " +
                "deviceId TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS ubicaciones");
        onCreate(db);
    }
}
