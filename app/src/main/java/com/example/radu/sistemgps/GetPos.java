package com.example.radu.sistemgps;

import android.util.Log;
import android.location.Location;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;

public class GetPos {

    public static float bearing,distance; // x-bearing//y-distanta
    public static String dateTime;
    public static int hisStatus;
    public static double latitude, longitude;

    public static void updateHisPos(){
        Location hisLocation = new Location(""); //loc2=locatie telefon partener
        try {
            InternetConnection.trustAllCertificates();
            String st = InternetConnection.host +"getPosition.php?id="+ MainActivity.partneriD;
            HttpsURLConnection con =InternetConnection.connectInternet(st);
            MainActivity.t3.setText("getPos: " + con.getResponseMessage()); //verificarea conexiunii //functioneaza

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jObj =new JSONObject(String.valueOf(builderString));
            if (jObj.getString("connOK")== "OK") {
                hisStatus = jObj.getInt("status");  //selectie status partener
                if (hisStatus != 0) {
                    //Location loc2 = new Location(""); //loc2=locatie telefon partener
                    hisLocation.setLatitude(jObj.getDouble("latitudine"));
                    hisLocation.setLongitude(jObj.getDouble("longitudine"));

                    latitude = jObj.getDouble("latitudine"); ///pt google maps
                    longitude = jObj.getDouble("longitudine"); ///pt google maps

                    dateTime = jObj.getString("dataOra");
                    Log.i("UpdateHisPosition", "dataOra: " + dateTime);

                    MainActivity.t2.setText("Him " + (float) latitude + "  " + (float) longitude);
                    MainActivity.t6.setText("HisID: " + MainActivity.partneriD + " st:" + GetPos.hisStatus + "  ActiveAt: " + GetPos.dateTime);

                    MainActivity.myLocation.setLatitude(MyLocationListener.latitude);
                    MainActivity.myLocation.setLongitude(MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "my coord" + MainActivity.myLocation);
                    bearing = MainActivity.myLocation.bearingTo(hisLocation); //salvez bearingul ce ia val intre [-180,180]grade
                    Log.i("UpdateHisPosition", "coord " + MyLocationListener.latitude + " " + MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "his coord" + hisLocation);

                    if (bearing < 0) {
                        bearing = bearing + 360;
                        Log.i("UpdateHisPosition", "bearing="+bearing);
                    } // corectia valorii bearing

                    distance = MainActivity.myLocation.distanceTo(hisLocation);//distanta intre locatii
                } else {
                    onStatusChanged();
                }
            }

        } catch (Exception e) {
            //
            MainActivity.t2.setText("err getPosition");
            Log.d("UpdateHisPOsition","exceptie getPosition", e);
        }
     }

    public static void onStatusChanged() {
        MainActivity.t6.setText( "HisID: "+MainActivity.partneriD+ " st:"+ hisStatus +"  ActiveAt: Invisible");
        MainActivity.t2.setText("Invisible");
        bearing=0;
        distance=0;
    }

    public static float retBearing() {
        return bearing;
    }

    public static float retDistance() {
        return distance;
    }

}
