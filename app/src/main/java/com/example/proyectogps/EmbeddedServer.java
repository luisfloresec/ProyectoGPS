package com.example.proyectogps;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class EmbeddedServer extends NanoHTTPD {
    private Context context;

    public EmbeddedServer(Context ctx) throws IOException {
        super(8080);
        this.context = ctx;
        start(SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        String auth = headers.get("authorization");

        if (auth == null || !auth.equals("Basic YWRtaW46MTIzNA==")) {
            return newFixedLengthResponse(Response.Status.UNAUTHORIZED, "text/plain", "Unauthorized");
        }

        if (session.getUri().startsWith("/api/sensor_data")) {
            Cursor cursor = GPSDatabase.getInstance(context).getReadableDatabase()
                    .rawQuery("SELECT * FROM gps", null);
            StringBuilder result = new StringBuilder();
            while (cursor.moveToNext()) {
                result.append("Lat: ").append(cursor.getDouble(1))
                        .append(", Lng: ").append(cursor.getDouble(2))
                        .append(", Time: ").append(cursor.getLong(3)).append("\n");
            }
            cursor.close();
            return newFixedLengthResponse(result.toString());
        }

        if (session.getUri().equals("/api/device_status")) {
            String response = "Battery: OK\nStorage: OK\nOS: Android\nModelo: Genérico";
            return newFixedLengthResponse(response);
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "No encontrado");
    }
}
