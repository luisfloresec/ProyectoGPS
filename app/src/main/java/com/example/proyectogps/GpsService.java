package com.example.proyectogps;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

public class GpsService extends Service {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private long lastUpdateTime = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iniciarGPS();
        return START_STICKY;
    }

    private void iniciarGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = location -> {
            long ahora = SystemClock.elapsedRealtime();
            if (ahora - lastUpdateTime >= 30_000) {
                lastUpdateTime = ahora;
                GPSDatabase.getInstance(this).insertLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        System.currentTimeMillis()
                );
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10_000, 0, locationListener);
        } catch (SecurityException e) {
            Log.e("GpsService", "Permiso no concedido", e);
        }
    }

    @Override
    public void onDestroy() {
        if (locationManager != null && locationListener != null)
            locationManager.removeUpdates(locationListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}
