package com.example.radu.sistemgps;

import android.util.Log;
import android.location.Location;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import static com.example.radu.sistemgps.MainActivity.myLocation;
import static com.example.radu.sistemgps.MainActivity.hisLocation;
public class GetPos {

    public static float bearing,distance; // x-bearing//y-distanta
    public static String dateTime;
    public static int hisStatus;
    public static double latitude, longitude;

    public static void updateHisPos(Location loc){
        myLocation = loc;
        try {
            InternetConnection.trustAllCertificates();
            String st = InternetConnection.host +"getPosition.php?id="+ MainActivity.partneriD;
            HttpsURLConnection con =InternetConnection.connectInternet(st);
            MainActivity.t3.setText("getPos: " + con.getResponseMessage()); //verificarea conexiunii //functioneaza

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jObj =new JSONObject(String.valueOf(builderString));

            if (jObj.getString("connOK").equals("OK")) {

                hisStatus = jObj.getInt("status");  //selectie status partener
                Log.i("UpdateHisPosition","status="+ String.valueOf(hisStatus));
                if (hisStatus != 0) {
//                    Log.i("UpdateHisPosition", jObj.getString("latitude"));
                    Location loc2 = new Location(""); //loc2=locatie telefon partener
                    loc2.setLatitude(jObj.getDouble("latitude"));
                    loc2.setLongitude(jObj.getDouble("longitude"));

//                    hisLocation.setLatitude(jObj.getDouble("latitude"));
//                    hisLocation.setLongitude(jObj.getDouble("longitude"));

                    latitude = jObj.getDouble("latitude"); ///pt google maps
                    longitude = jObj.getDouble("longitude"); ///pt google maps
                    Log.i("UpdateHisPosition", "lati&longi=: " + latitude+"   "+longitude );
                    dateTime = jObj.getString("dateTime");
                    Log.i("UpdateHisPosition", "dataOra: " + dateTime);

                    MainActivity.t2.setText("Him " + (float) latitude + "  " + (float) longitude);
                    MainActivity.t6.setText("HisID: " + MainActivity.partneriD + " st:" + GetPos.hisStatus + "  ActiveAt: " + GetPos.dateTime);

                    MainActivity.myLocation.setLatitude(MyLocationListener.latitude);
                    MainActivity.myLocation.setLongitude(MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "my coord" + MainActivity.myLocation);
                    bearing = MainActivity.myLocation.bearingTo(loc2); //salvez bearingul ce ia val intre [-180,180]grade
                    Log.i("UpdateHisPosition", "coord " + MyLocationListener.latitude + " " + MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "his coord" + hisLocation);
                    Log.i("UpdateHisPosition", "his coord-loc2=" + loc2);

                    if (bearing < 0) {
                        bearing = bearing + 360;
                        Log.i("UpdateHisPosition", "bearing="+bearing);
                    } // corectia valorii bearing

                    distance = MainActivity.loc.distanceTo(loc2);//distanta intre locatii
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
