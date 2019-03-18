package com.example.radu.sistemgps;

import android.widget.Toast;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Radu on 08-Jan-19.
 * This class file is responsible with the internet connection.
 */

public final class InternetConnection {

    public static final String host = "https://10.2.248.69/";

    public static HttpsURLConnection connectInternet( String url_address) {
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        HttpsURLConnection con =null;
        try {
            trustAllCertificates();
          //  String st = "https://192.168.1.6/putPosition.php?id=" + MainActivity.iD + "&n=" + MainActivity.name + "&La=" + loc.getLatitude() + "&Lg=" + loc.getLongitude()+"&st="+ MainActivity.myStatus;

            URL obj = new URL(url_address);
            con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.connect();

        } catch (Exception e) {
            //MainActivity.t3.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return con;
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


