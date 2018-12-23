package com.example.radu.sistemgps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Logout extends Activity {

    public static boolean connexOK;
    public static String passLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        final Button button = (Button) findViewById(R.id.buttonyy);////Cancel button trimite la Meniu
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent b = new Intent(Logout.this, Meniu.class);
                startActivity(b);
                finish();

            }
        });

        final Button button2 = (Button) findViewById(R.id.buttonxx);////Logout button trimite la Login
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i("Logout", "user");
                Log.i("Logout", "pass");

                new AttemptLogout().execute();
                Toast.makeText(getApplicationContext(), "You succesfully logged out", Toast.LENGTH_SHORT).show();

                Intent c = new Intent(Logout.this, Login.class);
                startActivity(c);
                finish();
            }

        });

    }
    @Override
    public void onBackPressed()
    {
    }

}
class AttemptLogout extends AsyncTask<Object, Object, String>
{

    protected String doInBackground(Object... urls) {
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        try {
            if(Login.pass!=null)
            {Logout.passLogout=Login.pass;}
            else{Logout.passLogout=Register.passRegister;}

            trustAllCertificates();
            String strg = "https://86.104.210.226/FF/logout.php?id=" + MainActivity.iD + "&pass=" + Logout.passLogout;

            Log.i("Logout", "str:" + strg);
            URL obj = new URL(strg);
            Log.i("Logout", "obj: " + obj);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            Log.i("Logout", "con: " + con);
            con.setRequestMethod("GET");
            Log.i("Logout", "get: ");
            con.setDoOutput(true);
            Log.i("Logout", "doOutput: ");
            con.setRequestProperty("User-Agent", USER_AGENT);
            Log.i("Logout", "req ");
            con.connect();
            Log.i("Logout", "connect: ");
            Log.i("Logout", "ok? " + con.getResponseMessage());
            //Logout.connexOK=con.getResponseMessage().equals("OK");

        } catch (Exception e) {
            // tx2.setText("err Logout");
            //tx2.setText(e.getLocalizedMessage());
            e.printStackTrace();
            Log.i("Logout","stacktrace"+ Arrays.toString(e.getStackTrace()));

        }

        return "ok";
    }
    protected void onProgressUpdate(Void... progress) {

    }

    protected String onPostExecute(Void result)
    {
        return "deconectare reusita";
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
