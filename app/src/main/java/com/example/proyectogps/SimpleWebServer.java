package com.example.proyectogps;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;

public class SimpleWebServer extends NanoHTTPD {

    private final DataProvider dataProvider;

    public interface DataProvider {
        String getGpsDataJson();
    }

    public SimpleWebServer(int port, DataProvider provider) {
        super(port);
        this.dataProvider = provider;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Log.d("SERVER", "Petición recibida: " + uri);

        if ("/api/sensor_data".equals(uri)) {
            String json = dataProvider.getGpsDataJson();
            return newFixedLengthResponse(Response.Status.OK, "application/json", json);
        } else {
            return newFixedLengthResponse("Servidor funcionando. Usa /api/sensor_data para ver los datos GPS.");
        }
    }
}
