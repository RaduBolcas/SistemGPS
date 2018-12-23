package com.example.radu.sistemgps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
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

public class MyLocationListener implements LocationListener {

    public static double latitude;
    public static double longitude;

    public static void updateMyPos(Location loc){
        MainActivity.loc = loc;

        /// pt afisarea trunchiata a coordonatelor proprii
        MainActivity.t.setText("Eu"+(float)loc.getLatitude() + "   " + (float)loc.getLongitude());
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        try {
            trustAllCertificates();
            String st = "https://86.104.210.226/FF/putPosition.php?id=" + MainActivity.iD + "&n=" + MainActivity.name + "&La=" + loc.getLatitude() + "&Lg=" + loc.getLongitude()+"&st="+ MainActivity.myStatus;

            URL obj = new URL(st);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.connect();

            MainActivity.t4.setText("putPos: " + con.getResponseMessage()); ///verif cconexiunii

        } catch (Exception e) {
            MainActivity.t3.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();

        MyLocationListener.updateMyPos(loc);
    }

    @Override
    public  void onProviderDisabled(String provider)
    {
     //Toast.makeText(this, "Gps turned off ", Toast.LENGTH_LONG).show();
     //print "Currently GPS is Disabled";
    }
    @Override
    public  void onProviderEnabled(String provider)
    {
     // Toast.makeText(this, "Gps turned on ", Toast.LENGTH_LONG).show();
        //print "GPS got Enabled";
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }
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