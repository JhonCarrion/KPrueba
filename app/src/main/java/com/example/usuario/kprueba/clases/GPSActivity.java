package com.example.usuario.kprueba.clases;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.usuario.kprueba.R;
import com.example.usuario.kprueba.fragments.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class GPSActivity extends AppCompatActivity {

    private LatLng myUbicacion;
    private TextView lbl_latitud, lbl_longitud, lbl_confidencialidad, lbl_velocidad, lbl_fechaHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        control(getIntent().getExtras());
        cargarMapa();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public LatLng getMyUbicacion() {
        return myUbicacion;
    }

    protected void control(Bundle bundle) {
        lbl_latitud = findViewById(R.id.lbl_gps_latitud);
        lbl_longitud = findViewById(R.id.lbl_gps_longitud);
        lbl_confidencialidad = findViewById(R.id.lbl_gps_confidencialidad);
        lbl_velocidad = findViewById(R.id.lbl_gps_velocidad);
        lbl_fechaHora = findViewById(R.id.lbl_gps_fecha_hora);

        myUbicacion = new LatLng(bundle.getDouble("latitud"), bundle.getDouble("longitud"));

        lbl_latitud.setText(bundle.getDouble("latitud") + "");
        lbl_longitud.setText(bundle.getDouble("longitud") + "");
        lbl_confidencialidad.setText(bundle.getString("confidencialidad"));
        lbl_velocidad.setText(bundle.getFloat("velocidad") + "");
        lbl_fechaHora.setText(new Date(bundle.getLong("time")).toString());
    }

    private void cargarMapa() {
        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_map, mapFragment);
        transaction.commit();
    }
}
