package com.meeting;

import android.location.Location;


public class Meeting_details {
    String id_Meeting= "";
    String locationName= "";
    Location location = null;
    String dateTime = "";

    Meeting_details(String id_Meeting, String locationName,Location location, String dateTime){
        this.id_Meeting=id_Meeting;
        this.locationName=locationName;
        this.location=location;
        this.dateTime=dateTime;
    }

}
