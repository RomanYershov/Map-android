package com.example.maps;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int countMarkers = 0;
    PolylineOptions polylineOptions;
    LatLng oldPosition;
    int distantion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylineOptions = new PolylineOptions();

    }

    
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapClickListener(map -> {

            double lat = map.latitude;
            double lang = map.longitude;

            LatLng newPosition = new LatLng(lat, lang);


            mMap.addMarker(new MarkerOptions().position(newPosition));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp))
            mMap.addPolyline(polylineOptions.add(newPosition));


            TextView distantionTv = findViewById(R.id.mf_distantion_tv);


            if (countMarkers == 0)
                oldPosition = newPosition;

            if (++countMarkers > 5) {
                mMap.clear();
                countMarkers = 0;
                distantion = 0;
                polylineOptions = new PolylineOptions();
                distantionTv.setText(String.valueOf(0));
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newPosition));

                float[] result = new float[1];

                Location.distanceBetween(oldPosition.latitude, oldPosition.longitude, newPosition.latitude, newPosition.longitude, result);
                oldPosition = newPosition;
                distantion += convertToKm(result[0]);

                distantionTv.setText(String.valueOf(distantion));
            }


        });
    }

    private int convertToKm(float v) {
        return (int) v / 1000;
    }

}
