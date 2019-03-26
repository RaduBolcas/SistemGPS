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
    public static int m=0;
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
                Log.i("Logout", "user&pass");
                AttemptLogout myLogout = new AttemptLogout();
                new AttemptLogout().execute();
                try {
                    m= myLogout.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if ( m == 1) {
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
class AttemptLogout extends AsyncTask<Object, Object, Integer> {
    private int response =0;

    protected Integer doInBackground(Object... urls) {

        try {
            if(Login.pass!=null){
                Logout.passLogout=Login.pass;}
            else{
                Logout.passLogout=Register.passRegister;
            }

            InternetConnection.trustAllCertificates();
            String strg = InternetConnection.host + "logout.php?id=" + MainActivity.iD + "&pass=" + Logout.passLogout;
            HttpsURLConnection con = InternetConnection.connectInternet(strg);

            int responseCode = con.getResponseCode();
            Log.i("Logout","response code?="+responseCode);

            BufferedReader inBuff = new BufferedReader( new InputStreamReader(con.getInputStream()) );
            StringBuilder logout = new StringBuilder();
            String line;
            while ((line = inBuff.readLine()) != null) {
                logout.append(line).append('\n');
            }

            JSONObject jObj =new JSONObject(String.valueOf(logout));
            Log.i("Logout","jobj="+jObj.getString("connOK"));

            if(Objects.equals(jObj.getString("connOK"), "OK")){
                response =1 ;//Login.m = 1;
                Log.i("Logout","response="+response);
            }
            //MainActivity.t4.setText("putPos:   " + con.getResponseMessage()); ///verif cconexiunii

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Logout","stacktrace="+ Arrays.toString(e.getStackTrace()));
        }
        return response;
    }
    protected void onProgressUpdate(Void... progress) {
    }

    protected int onPostExecute(int result) {
        return result;
    }
}
