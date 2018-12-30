package com.example.usuario.kprueba.clases;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.usuario.kprueba.R;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btn_Dlg_Aceptar, btn_fb, btn_github, btn_gitlab;
    private LatLng myUbicacion;
    private Bundle bundle;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_fb = findViewById(R.id.btn_fb_perfil);
        btn_github = findViewById(R.id.btn_git_hub);
        btn_gitlab = findViewById(R.id.btn_git_lab);

        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://profile/100001684862546";
                String urlPage = "https://www.facebook.com/AlexCarrionIsd";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    Log.e("errorfb", "Aplicación no instalada.");
                    //Abre url de pagina.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
            }
        });
        btn_github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlPage = "https://github.com/JhonCarrion";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });
        btn_gitlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlPage = "https://gitlab.com/JhonCarrion";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        miUbicacion();
        bundle = new Bundle();
        PACKAGE_NAME = getApplicationContext().getPackageName();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_a_cerca:
                final Dialog dlg = new Dialog(this);
                dlg.setContentView(R.layout.dialog_acerca);
                dlg.show();

                btn_Dlg_Aceptar = dlg.findViewById(R.id.btn_acerca_aceptar);
                btn_Dlg_Aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });
                break;
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.toast_settings), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_mapa:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            case R.id.nav_gps:
                Intent intent = new Intent(MainActivity.this, GPSActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_clima:
                startActivity(new Intent(MainActivity.this, WebServiceActivity.class));
                break;
            case R.id.nav_contactos:
                startActivity(new Intent(MainActivity.this, ListaContactoActivity.class));
                break;
            case R.id.nav_sockets:
                startActivity(new Intent(MainActivity.this, SocketActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Seccion de Código para GPS
     */

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            bundle.putString("confidencialidad", location.getProvider());
            bundle.putDouble("latitud", location.getLatitude());
            bundle.putDouble("longitud", location.getLongitude());
            bundle.putFloat("velocidad", location.getSpeed());
            bundle.putLong("time", location.getTime());
            myUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

    };


    protected void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }
}
