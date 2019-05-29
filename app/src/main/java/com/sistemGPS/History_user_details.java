package com.sistemGPS;

import android.location.Location;

public class History_user_details {

    Location location = null;
    String dateTime = "";
    String status = "";
    History_user_details(Location location, String dateTime, String status ){
        this.location=location;
        this.dateTime=dateTime;
        this.status=status;
    }

}