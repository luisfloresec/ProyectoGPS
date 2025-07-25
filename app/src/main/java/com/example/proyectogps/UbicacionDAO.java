package com.example.proyectogps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UbicacionDAO {

    private SQLiteDatabase db;

    public UbicacionDAO(Context context) {
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertar(Ubicacion ubicacion) {
        ContentValues values = new ContentValues();
        values.put("latitud", ubicacion.getLatitud());
        values.put("longitud", ubicacion.getLongitud());
        values.put("timestamp", ubicacion.getTimestamp());
        values.put("deviceId", ubicacion.getDeviceId());
        db.insert("ubicaciones", null, values);
    }

    public List<Ubicacion> obtenerTodas() {
        List<Ubicacion> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM ubicaciones ORDER BY timestamp DESC", null);
        while (cursor.moveToNext()) {
            lista.add(new Ubicacion(
                    cursor.getDouble(1),
                    cursor.getDouble(2),
                    cursor.getLong(3),
                    cursor.getString(4)
            ));
        }
        cursor.close();
        return lista;
    }
}
