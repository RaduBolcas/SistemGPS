package com.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
import com.example.radu.sistemgps.Meniu;
import com.example.radu.sistemgps.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Login extends Activity {

    Button bt1, bt2, bt3;
    EditText edt1, edt2;
    public static String pass;
    public static int m;
    static TextView txt1, txt2, txt3;
    int k = 4; //numara incercarile de autentificare

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bt1 = (Button) findViewById(R.id.button); //Login button
        edt1 = (EditText) findViewById(R.id.editText); ///username
        edt2 = (EditText) findViewById(R.id.editText2); ///password

        bt2 = (Button) findViewById(R.id.button2); //Cancel Button
        bt3 = (Button) findViewById(R.id.button3); //Register button

        txt1 = (TextView) findViewById(R.id.textView3);
        txt2 = (TextView) findViewById(R.id.textView1);
        txt3 = (TextView) findViewById(R.id.textView2);
        txt1.setVisibility(View.GONE);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.iD = edt1.getText().toString();
                pass = edt2.getText().toString();
                Logout.passLogout=pass;

                Log.i("Login", "inainte de async");
                new AttemptLogin().execute();
                m = 1;
                Log.i("login", "Attempt login finished");
                //int j = retRasp();
                Log.i("login", "m " + m);
                //if(ed1.getText().toString().equals("") && ed2.getText().toString().equals(""))
                if (m == 1) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Meniu.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Wrong ID/Password", Toast.LENGTH_SHORT).show();

                    txt1.setVisibility(View.VISIBLE);
                    txt1.setBackgroundColor(Color.RED);
                    k--;
                    txt1.setText(Integer.toString(k));

                    if (k == 0) {
                        bt1.setEnabled(false);
                    }
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() { //butonul Cancel
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() { //butonul Register
            public void onClick(View v) {
                pass = null;
                Intent n = new Intent(Login.this, Register.class);
                startActivity(n);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        //TODO sa ies din aplicatie
    }

}

class AttemptLogin extends AsyncTask<Object, Object, Integer>
{

    protected Integer doInBackground(Object... urls) {

        try {
            InternetConnection.trustAllCertificates();
            String str = InternetConnection.host +"login.php?id=" + MainActivity.iD + "&pass=" + Login.pass;
            HttpsURLConnection conn = InternetConnection.connectInternet(str);
            //MainActivity.t1.setText("connecting");
            // MainActivity.t2.setText(st); //testare string st
//            Log.i("Login","str:"+ str);
//            URL obj = new URL(str);
//            Log.i("Login","obj: "+ obj);
//            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
//            Log.i("Login","con: "+ conn);
//            conn.setRequestMethod("GET");
//            Log.i("Login","get: ");
//            conn.setDoOutput(true);
//            Log.i("Login","doOutput: ");
//            conn.setRequestProperty("User-Agent", USER_AGENT);
//
//            Log.i("Login","req ");
//            conn.setInstanceFollowRedirects(false);
//            conn.connect();
//            Log.i("Login","connect: ");
//            Log.i("Login","ok? ");

            int responseCode = conn.getResponseCode();
            Log.i("Login","response code? "+responseCode);


            BufferedReader inBuff = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
            Log.i("Login","in"+ inBuff);
            StringBuilder raspuns = new StringBuilder();
            String line;
            while ((line = inBuff.readLine()) != null) {
                raspuns.append(line).append('\n');
            }
            String valoare = raspuns.toString();
            valoare =valoare.replaceAll("\\s","");
            Login.m= Integer.parseInt(valoare);
            Log.i("Login","valoare"+ Login.m);

            //MainActivity.t4.setText("putPos:   " + con.getResponseMessage()); ///verif cconexiunii

        } catch (Exception e) {
            // tx2.setText("err Login");
            Log.i("Login", "exceptie: "+e.getCause());
            //tx2.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return Login.m;
    }
    protected void onProgressUpdate(Void... progress) {

    }

    protected int onPostExecute(String result) {
        return Login.m;
    }


}

