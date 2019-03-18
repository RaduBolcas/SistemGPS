package com.example.radu.sistemgps;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Eu = new LatLng(MyLocationListener.latitude, MyLocationListener.longitude);
        LatLng El = new LatLng(GetPos.longitude, GetPos.latitude);

        mMap.addMarker(new MarkerOptions().position(Eu).title("Eu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Eu));

        mMap.addMarker(new MarkerOptions().position(El).title("El"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(El));
    }

    @Override
    public void onBackPressed()
    {
        Intent y=new Intent(MapsActivity.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }
}
