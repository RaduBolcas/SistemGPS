package com.example.radu.sistemgps;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class GetID extends Activity {
    EditText id;
    public static TextView t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);

        id = (EditText)findViewById(R.id.editTextid);
        getID();

        final Button buttonID = (Button) findViewById(R.id.buttonID);
        buttonID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                MainActivity.partneriD=id.getText().toString();
                Intent i=new Intent(GetID.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        });
    }


    public static void getID()
    {
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";
        Log.i("GetID","coord: 11");

        try {
            trustAllCertificates();

            String st = "https://86.104.210.226/FF/getIDs.php";
            Log.i("GetID","st: "+ st);

            t1.setText("connecting");
            t2.setText(st); //testare string st
            URL obj = new URL(st);
            Log.i("GetID","obj: "+ obj);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            Log.i("GetID","con: "+ con);
            con.setRequestMethod("GET");
            Log.i("GetID","get: ");
            con.setDoOutput(true);
            Log.i("GetID","doOutput: ");
            con.setRequestProperty("User-Agent", USER_AGENT);
            Log.i("GetID","req ");
            con.connect();
            Log.i("GetID","connect: ");
            Log.i("GetID","ok? "+con.getResponseMessage());
            t1.setText("GetID:   " + con.getResponseMessage()); ///verif cconexiunii

            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) ); //buffer pt a salva stream-ul
            Log.i("GetID","in: "+ in);

            StringBuilder total = new StringBuilder();
            String line;


            while ((line = in.readLine()) != null) {
                total.append(line).append('\n');
            }
            Log.i("GetID","total: "+ total);
            String sirrr = total.toString();
            sirrr =sirrr.replaceAll("\\n","&");
            sirrr =sirrr.replaceAll("\\s","");
            sirrr =sirrr.replaceAll("&&&&","\n");
            sirrr =sirrr.replaceAll("&","");
            Log.i("GetID","sirrr:"+ sirrr);

            String s= sirrr.replaceAll(",","  ") ;
            t2.setText(s);
            Log.i("GetID","s"+ s);

        } catch (Exception e) {
            t2.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent y=new Intent(GetID.this, Meniu.class);
        startActivity(y);
        finish();
        return;
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
