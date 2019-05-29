package com.sistemGPS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.radu.sistemgps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class HistoryMapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {

    private GoogleMap mMap;
    public static ArrayList<History_user_details> history = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Location permission needed", Toast.LENGTH_LONG).show();
            finish();
        }
        mMap.setOnMyLocationButtonClickListener(this);

        for(History_user_details loc : history){
            LatLng member = new LatLng(loc.location.getLatitude(),loc.location.getLongitude());
            MarkerOptions markerOptions =new MarkerOptions().position(member).title( loc.dateTime);
            mMap.addMarker(markerOptions);
        }

    }
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }
    @Override
    public void onBackPressed()
    {
        Intent x=new Intent(HistoryMapsActivity.this, Meniu.class);
        startActivity(x);
        finish();
    }
}
