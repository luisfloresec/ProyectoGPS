package com.example.proyectogps;

public class Ubicacion {
    private double latitud;
    private double longitud;
    private long timestamp;
    private String deviceId;

    public Ubicacion(double latitud, double longitud, long timestamp, String deviceId) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public long getTimestamp() { return timestamp; }
    public String getDeviceId() { return deviceId; }
}
