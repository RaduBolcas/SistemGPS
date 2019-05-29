package com.sistemGPS;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class MyLocationListener implements LocationListener {

    public static double latitude, longitude;


    public static void updateMyPos(Location loc){
        MainActivity.loc = loc;

        /// Own Location

        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"putPosition.php?idU=" + MainActivity.iD + "&La=" + loc.getLatitude() + "&Lg=" + loc.getLongitude()+"&st="+ MainActivity.myStatus;
            Log.i("MyLocationListener", url);
            HttpsURLConnection con = InternetConnection.connectInternet(url);
            MainActivity.t4.setText("putPos: " + con.getResponseMessage()); ///verif cconexiunii

            //Toast.makeText(null, con.getResponseMessage(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
          //  Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void updateMyHistory(Location loc){
        MainActivity.myLocation = loc;
//        Double lat = myLocation.getLatitude(), lng = myLocation.getLongitude();
        try {
//            if (lat!=0 && lng!=0) {//insert only corrct values
                InternetConnection.trustAllCertificates();
                String st = InternetConnection.host + "putHistory.php?idU=" + MainActivity.iD + "&La=" + MainActivity.myLocation.getLatitude() + "&Lg=" + MainActivity.myLocation.getLongitude() + "&st=" + MainActivity.myStatus;
                HttpsURLConnection con = InternetConnection.connectInternet(st);
                Log.i("MyLocationListener", "putHistoryOK=" + con.getResponseMessage());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        Log.i("MyLocList", "onLocChanged");
        MyLocationListener.updateMyPos(loc);
    }

    @Override
    public  void onProviderDisabled(String provider) {
     //Toast.makeText(this, "Gps turned off ", Toast.LENGTH_LONG).show();
     //print "Currently GPS is Disabled";
    }
    @Override
    public  void onProviderEnabled(String provider) {
     // Toast.makeText(this, "Gps turned on ", Toast.LENGTH_LONG).show();
        //print "GPS got Enabled";
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}