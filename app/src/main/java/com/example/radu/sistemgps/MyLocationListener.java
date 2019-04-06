package com.example.radu.sistemgps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import javax.net.ssl.HttpsURLConnection;

import static com.example.radu.sistemgps.MainActivity.myLocation;

public class MyLocationListener implements LocationListener {

    public static double latitude, longitude;


    public static void updateMyPos(Location loc){
        MainActivity.loc = loc;

        /// Own Location
        MainActivity.t.setText("Eu"+(float)loc.getLatitude() + "   " + (float)loc.getLongitude());
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"putPosition.php?idU=" + MainActivity.iD + "&idL=" + MainActivity.iD + "&La=" + loc.getLatitude() + "&Lg=" + loc.getLongitude()+"&st="+ MainActivity.myStatus;
            Log.i("MyLocationListener", url);
            HttpsURLConnection con = InternetConnection.connectInternet(url);
            // TODO de verificat daca toast-ul urmator functioneaza
            MainActivity.t4.setText("putPos: " + con.getResponseMessage()); ///verif cconexiunii

            //Toast.makeText(null, con.getResponseMessage(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
          //  Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void updateMyHistory(Location loc){
        myLocation = loc;
        try {
            InternetConnection.trustAllCertificates();
            String st = InternetConnection.host +"putHistory.php?idU=" + MainActivity.iD + "&La=" + myLocation.getLatitude() + "&Lg=" + myLocation.getLongitude()+"&st="+ MainActivity.myStatus;
            HttpsURLConnection con = InternetConnection.connectInternet(st);
            Log.i("MyLocationListener","putHistoryOK="+ con.getResponseMessage());
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