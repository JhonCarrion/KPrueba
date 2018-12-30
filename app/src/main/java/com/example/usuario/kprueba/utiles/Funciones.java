package com.example.usuario.kprueba.utiles;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.services.Constants;
import com.mapbox.services.api.ServicesException;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Funciones extends FragmentActivity {

    private String token = "pk.eyJ1IjoiamhvbmNhcnJpb24iLCJhIjoiY2pwbjlqdXR3MDBxejN4bnpma3lmamlxYSJ9.mPbfGaBMIH866XU-OZYOGw";
    private Polyline lineSolicitud;

    public synchronized void getRoute(Position origin, Position destination, final GoogleMap mapaOSM) {

        MapboxDirections client = null;
        try {
            client = new MapboxDirections.Builder()
                    .setOrigin(origin)
                    .setDestination(destination)
                    .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                    .setSteps(true)
                    .setAccessToken(token)
                    .build();
        } catch (ServicesException e) {
            return;
        }
        client.enqueueCall(new Callback<DirectionsResponse>() {
                               @Override
                               public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                                   if (response.code() == 200) {
                                       Log.e("route", "entro");
                                       if (response.body().getRoutes() != null) {
                                           if (response.body().getRoutes().size() > 0) {
                                               DirectionsRoute currentRoute = response.body().getRoutes().get(0);
                                               trazarRutaSolicitud(mapaOSM, currentRoute, true);
                                           }
                                       }
                                   } else {
                                       Log.e("route", "no entro");
                                      // ExtraLog.Log(TAG, "onResponse: " + response.code());
                                   }
                               }

                               @Override
                               public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                                  // ExtraLog.Log(TAG, "onFailure: " + t.getMessage());
                               }
                           }

        );
    }


    public void trazarRutaSolicitud(GoogleMap mapBox, DirectionsRoute currentRoute, boolean isEstado) {
        if (isEstado) {
            PolylineOptions polylineOptions = new PolylineOptions();
            if (currentRoute != null && mapBox != null && lineSolicitud == null) {
                LineString lineString = LineString.fromPolyline(currentRoute.getGeometry(), Constants.PRECISION_6);
                List<Position> coordinates = lineString.getCoordinates();
                for (int i = 0; i < coordinates.size(); i++) {
                    polylineOptions.add(new LatLng(coordinates.get(i).getLatitude(), coordinates.get(i).getLongitude()));
                }
                polylineOptions.color(Color.parseColor("#3bb2d0"));
                polylineOptions.width(8);
                lineSolicitud = mapBox.addPolyline(polylineOptions);
            }
        } else {
            if (mapBox != null && lineSolicitud != null) {
                lineSolicitud.remove();
                lineSolicitud = null;
            }
        }
    }

}
