package com.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
import com.example.radu.sistemgps.Meniu;
import com.example.radu.sistemgps.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Logout extends Activity {

    public static String passLogout= Login.pass;
    public static int serverResponse=0;
    private String TAG ="Logout";

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
                Log.i(TAG, "Before asyncTask block");
                AttemptRegistration myLogout = new AttemptRegistration();
                try {
                    String url = InternetConnection.host + "logout.php?id=" + MainActivity.iD + "&pass=" + Logout.passLogout;
                    myLogout.setUrl(url);
                    myLogout.setTAG(TAG);
                    serverResponse= myLogout.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if ( serverResponse == 1) {
                    Toast.makeText(getApplicationContext(), "You succesfully logged out", Toast.LENGTH_SHORT).show();
                    Intent c = new Intent(Logout.this, Login.class);
                    startActivity(c);
                    finish();
                }
            }

        });
    }
    @Override
    public void onBackPressed() {
    }
}
