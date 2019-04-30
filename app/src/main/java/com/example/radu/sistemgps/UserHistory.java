package com.example.radu.sistemgps;

import android.location.Location;

public class UserHistory {

    Location location = null;
    String dateTime = "";
    String status = "";
    UserHistory(Location location, String dateTime, String status ){
        this.location=location;
        this.dateTime=dateTime;
        this.status=status;
    }

}