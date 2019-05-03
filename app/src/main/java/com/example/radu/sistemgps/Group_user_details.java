package com.example.radu.sistemgps;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Group_user_details {
    String idUser = "";
    String nickName = "";
    Location location = null;
    String dateTime = "";
    String status = "";
    Marker marker = null;

    Group_user_details(String idUser, String nickName, Location location, String dateTime, String status ){
        this.idUser = idUser;
        this.nickName=nickName;
        this.location=location;
        this.dateTime=dateTime;
        this.status=status;
    }
    public void createMarker(GoogleMap mMap){
        LatLng member = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions =new MarkerOptions().position(member).title(idUser + ":" +nickName);
        marker = mMap.addMarker(markerOptions);
    }
    public void setMarker(Marker marker){
        this.marker=marker;

    }
    public Marker getMarker (){
        return marker;
    }
    public void updateMarker(){
        LatLng member2 = new LatLng(location.getLatitude(),location.getLongitude());
        marker.setPosition(member2);
    }
}
