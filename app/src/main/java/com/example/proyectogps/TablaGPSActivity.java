package com.example.proyectogps;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TablaGPSActivity extends AppCompatActivity {

    private UbicacionDAO ubicacionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_gps);

        // Referencias a vistas
        TextView ipText = findViewById(R.id.ipText);
        TextView locationText = findViewById(R.id.locationText);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Mostrar IP local
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String ip = String.format(Locale.getDefault(), "%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        ipText.setText("IP Local: " + ip);

        // Obtener datos de GPS
        ubicacionDAO = new UbicacionDAO(this);
        List<Ubicacion> lista = ubicacionDAO.obtenerTodas();

        if (!lista.isEmpty()) {
            Ubicacion ultima = lista.get(lista.size() - 1);
            locationText.setText("Ubicación: " + ultima.getLatitud() + ", " + ultima.getLongitud());
        } else {
            locationText.setText("Ubicación: No disponible");
        }

        // Mostrar historial en tabla
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        for (Ubicacion u : lista) {
            TableRow row = new TableRow(this);

            TextView latitud = new TextView(this);
            latitud.setText(String.format(Locale.getDefault(), "%.5f", u.getLatitud()));
            latitud.setPadding(8, 8, 8, 8);

            TextView longitud = new TextView(this);
            longitud.setText(String.format(Locale.getDefault(), "%.5f", u.getLongitud()));
            longitud.setPadding(8, 8, 8, 8);

            TextView hora = new TextView(this);
            hora.setText(sdf.format(u.getTimestamp()));
            hora.setPadding(8, 8, 8, 8);

            row.addView(latitud);
            row.addView(longitud);
            row.addView(hora);

            tableLayout.addView(row);
        }
    }
}
