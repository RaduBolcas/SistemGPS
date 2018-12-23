package com.example.radu.sistemgps;


import android.util.Log;
import android.location.Location;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import static com.example.radu.sistemgps.MainActivity.loc1;

public class GetPos {

    public static float x,y; // x-bearing//y-distanta
    public static String dataOra;
    public static int hisStatus;
    public static double latit1, longit1;

    public static void updateHisPos(Location loc){
        loc1 = loc;

        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        try {
            trustAllCertificates();

            String st = "https://86.104.210.226/FF/getPosition.php?id="+ MainActivity.partneriD;
            // MainActivity.t3.setText(st); //verif string
            URL obj = new URL(st);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.connect();
            MainActivity.t3.setText("getPos: " + con.getResponseMessage()); //verificarea conexiunii //functioneaza

            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) ); //buffer pt a salva stream-ul

            StringBuilder sir = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sir.append(line).append('\n');
            }
            String coordonatePartener = sir.toString(); // si se foloseste fara sa fie de tip String, dar coordonatePartener trebuie sa fie
            Log.i("UpdateHisPosition","coordpartener1:"+ coordonatePartener);
            coordonatePartener =coordonatePartener.replaceAll("\\s","");/// eliminare spatii albe din string, spatii ce provin de la functia php
            //
            Log.i("UpdateHisPosition","coordpartener2:"+ coordonatePartener);
          // MainActivity.t2.setText(coordonatePartener);///coordonatePartener = tot ce s-a primit de la server prin putPosition - lati, long, dataOra

            if (coordonatePartener != null)
            {
                hisStatus=Integer.parseInt(coordonatePartener.split(",")[2]);     //selectie status partener
                if (hisStatus!=0) {

                    Location loc2 = new Location(""); //loc2=locatie telefon partener

                    loc2.setLatitude(Double.parseDouble(coordonatePartener.split(",")[0]));//// incarca in loc2 coordonatele din stringul coordonatePartener
                    loc2.setLongitude(Double.parseDouble(coordonatePartener.split(",")[1]));//

                     latit1 = Double.parseDouble(coordonatePartener.split(",")[0]);  ///pt google maps
                     longit1 = Double.parseDouble(coordonatePartener.split(",")[1]);///pt google maps

                    float lati = (float) Double.parseDouble(coordonatePartener.split(",")[0]);  ///trunchiere latitudine partener
                    float longi = (float) Double.parseDouble(coordonatePartener.split(",")[1]); ///trunchiere longitudine partener

                    dataOra = coordonatePartener.split(",")[3];
                    Log.i("UpdateHisPosition", "coordpart split3 " + dataOra);
                    String data, ora;
                    data = dataOra.substring(0, 9);
                    ora = dataOra.substring(10);
                    dataOra = data + "  " + ora;
                    Log.i("UpdateHisPosition", "dataOra" + dataOra);

                    //MainActivity.t2.setText(Double.parseDouble(coordonatePartener.split(",")[0])+ "  " + Double.parseDouble(coordonatePartener.split(",")[1]));
                    MainActivity.t2.setText("El "+ lati + "  " + longi);
                    MainActivity.t6.setText( "HisID: "+MainActivity.partneriD+ " st:"+ GetPos.hisStatus +"  ActiveAt: "+ GetPos.getDataOra());
                    loc1.setLatitude(MyLocationListener.latitude);
                    loc1.setLongitude(MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "coord mele" + loc1);
                    x = loc1.bearingTo(loc2); //salvez bearingul ce ia val intre [-180,180]grade
                    Log.i("UpdateHisPosition", "coord mele" + MyLocationListener.latitude + " " + MyLocationListener.longitude);
                    Log.i("UpdateHisPosition", "coord lui" + loc2);
                    //MainActivity.t3.setText(String.valueOf(MyLocationListener.latitude+ " "+MyLocationListener.longitude));
                    ////////////

                    if (x < 0) {
                        x = x + 360;
                    } // corectia valorii bearing
                    ///////////////
                    y = MainActivity.loc.distanceTo(loc2);//distanta intre locatii
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
        x=0; //bearing
        y=0; //distanta
    }

    public static float retBearing()
    {return x;}

    public static float retDistance()
    {return y;}

    public static String getDataOra()
    {return dataOra;}


    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }


}
