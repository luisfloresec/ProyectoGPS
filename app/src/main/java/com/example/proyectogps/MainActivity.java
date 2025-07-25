package com.example.proyectogps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 123;
    private Button startServerBtn;
    private SimpleWebServer webServer;
    private LocationManager locationManager;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServerBtn = findViewById(R.id.startServerBtn);

        solicitarPermisos();

        startServerBtn.setOnClickListener(v -> iniciarServidor());
    }

    private void solicitarPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE
                    },
                    PERMISSIONS_REQUEST_CODE);
        } else {
            mostrarIPLocal();
            iniciarGPS();
        }
    }

    private void mostrarIPLocal() {
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            String ip = String.format("%d.%d.%d.%d",
                    (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));
            Log.i("IP Local", "IP local: " + ip);
        } catch (SecurityException e) {
            Log.e("MainActivity", "Permiso de WiFi no concedido", e);
        }
    }

    private void iniciarServidor() {
        if (webServer == null) {
            webServer = new SimpleWebServer(8080, this::getDatosGpsComoJson);
            try {
                webServer.start();
                Log.i("Servidor", "Servidor iniciado en puerto 8080");
                startActivity(new Intent(MainActivity.this, TablaGPSActivity.class));
            } catch (IOException e) {
                Log.e("Servidor", "No se pudo iniciar", e);
                Toast.makeText(this, "Error al iniciar el servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detenerServidor() {
        if (webServer != null) {
            webServer.stop();
            webServer = null;
            Log.i("Servidor", "Servidor detenido automáticamente");
        }
    }

    private String getDatosGpsComoJson() {
        if (currentLocation != null) {
            return "{ \"latitud\": " + currentLocation.getLatitude() + ", \"longitud\": " + currentLocation.getLongitude() + " }";
        } else {
            return "{ \"error\": \"GPS no disponible\" }";
        }
    }

    private void iniciarGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String deviceId = android.os.Build.SERIAL;
        UbicacionDAO ubicacionDAO = new UbicacionDAO(this);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
                long timestamp = System.currentTimeMillis();
                Ubicacion u = new Ubicacion(location.getLatitude(), location.getLongitude(), timestamp, deviceId);
                ubicacionDAO.insertar(u);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // ✅ Añade esta línea

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean concedido = true;
            for (int r : grantResults) {
                if (r != PackageManager.PERMISSION_GRANTED) {
                    concedido = false;
                    break;
                }
            }
            if (concedido) {
                mostrarIPLocal();
                iniciarGPS();
            } else {
                Toast.makeText(this, "Permisos requeridos no concedidos", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        detenerServidor(); // Detener automáticamente si se regresa a esta pantalla
    }
}
