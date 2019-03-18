package com.example.radu.sistemgps;


import android.util.Log;
import android.location.Location;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import static com.example.radu.sistemgps.MainActivity.loc1;

public class GetPos {

    public static float bearing,distance; // x-bearing//y-distanta
    public static String dataOra;
    public static int hisStatus;
    public static double latitude, longitude;

    public static void updateHisPos(Location loc){
        loc1 = loc;
//        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        try {
            InternetConnection.trustAllCertificates();
            String st = InternetConnection.host +"getPosition.php?id="+ MainActivity.partneriD;
            HttpsURLConnection con =InternetConnection.connectInternet(st);

            MainActivity.t3.setText("getPos: " + con.getResponseMessage()); //verificarea conexiunii //functioneaza

            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) ); //buffer pt a salva stream-ul

            StringBuilder sir = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sir.append(line).append('\n');
            }
            String coordonatePartener = sir.toString(); // sir se foloseste fara sa fie de tip String, dar coordonatePartener trebuie sa fie
            Log.i("UpdateHisPosition","coordpartener1:"+ coordonatePartener);

            if (coordonatePartener != null)
            {
                JSONObject jObj =new JSONObject(String.valueOf(sir));

                hisStatus = jObj.getInt("status");  //selectie status partener
                if (hisStatus != 0)
                {
                    Location loc2 = new Location(""); //loc2=locatie telefon partener
                    loc2.setLatitude(jObj.getDouble("latitudine"));
                    loc2.setLongitude(jObj.getDouble("longitudine"));

                    latitude = jObj.getDouble("latitudine"); ///pt google maps
                    longitude = jObj.getDouble("longitudine"); ///pt google maps

                    dataOra = jObj.getString("dataOra");
                    Log.i("UpdateHisPosition", "dataOra: " + dataOra);

                    MainActivity.t2.setText("El "+ (float) latitude + "  " + (float)longitude);
                    MainActivity.t6.setText( "HisID: "+MainActivity.partneriD+ " st:"+ GetPos.hisStatus +"  ActiveAt: "+ GetPos.dataOra);

                    loc1.setLatitude(MyLocationListener.latitude);
                    loc1.setLongitude(MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "coord mele" + loc1);
                    bearing = loc1.bearingTo(loc2); //salvez bearingul ce ia val intre [-180,180]grade
                    Log.i("UpdateHisPosition", "coord mele" + MyLocationListener.latitude + " " + MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "coord lui" + loc2);
                    if (bearing < 0) {
                        bearing = bearing + 360;
                    } // corectia valorii bearing
                    ///////////////
                    distance = MainActivity.loc.distanceTo(loc2);//distanta intre locatii
                }else{
                    onStatusChanged();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //MainActivity.t2.setText(e.getLocalizedMessage());
            MainActivity.t2.setText("err getPosition");
            Log.d("UpdateHisPOsition","exceptie getPosition", e);
        }
     }

    public static void onStatusChanged()
    {
        MainActivity.t6.setText( "HisID: "+MainActivity.partneriD+ " st:"+ hisStatus +"  ActiveAt: Invisible");
        MainActivity.t2.setText("Invisible");
        bearing=0;
        distance=0;
    }

    public static float retBearing()
    {return bearing;}

    public static float retDistance()
    {return distance;}

}
