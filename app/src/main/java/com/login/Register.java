package com.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
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

public class Register extends Activity {
    EditText edtxt1, edtxt2, edtxt3, edtxt4;
    public static String passRegister, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtxt1 = (EditText) findViewById(R.id.editTexta);
        edtxt2 = (EditText) findViewById(R.id.editTextb);
        edtxt3 = (EditText) findViewById(R.id.editTextc);
        edtxt4 = (EditText) findViewById(R.id.editTextd);


        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                MainActivity.iD = edtxt1.getText().toString();
                passRegister = edtxt2.getText().toString();
                pass2 = edtxt3.getText().toString();
                MainActivity.name = edtxt4.getText().toString();

                Log.i("register", "user:" + edtxt1.getText().toString());//id
                Log.i("register", "pass:" + passRegister);
                Log.i("register", "pass2:" + pass2);

                if (passRegister.equals(pass2) ) {
                   Logout.passLogout=passRegister;

                    Log.i("register", "inainte de async");
                     new AttemptRegister().execute();
                    //if (connexOK = true && attemptRegister.val=="")
                    //Log.i("Register", "valoare dupa async" + AttemptRegister.val);/// nu raspunde nimic daca nu exista; raspunde cu string "exista" daca contul exista

                    if (AttemptRegister.val.equals( "" ))
                    {
                        Toast.makeText(getApplicationContext(), "You succesfully registered. Please Login", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Register.this, Login.class);
                        startActivity(a);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "The account exists already", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
    class AttemptRegister extends AsyncTask<Object, Object, String>
    {

    public static String val;
    public static boolean connexOK;


        protected String doInBackground(Object... urls) {
//        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36";

        try {
            InternetConnection.trustAllCertificates();
            String strg = InternetConnection.host + "register.php?id=" + MainActivity.iD +"&n="+ MainActivity.name+"&pass=" + Register.passRegister;
            HttpsURLConnection conn = InternetConnection.connectInternet(strg);
            //MainActivity.t1.setText("connecting");
            // MainActivity.t2.setText(st); //testare string st
//                        Log.i("register", "str:" + strg);
//            URL obj = new URL(strg);
//                        Log.i("register", "obj: " + obj);
//            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
//                        Log.i("register", "con: " + conn);
//            conn.setRequestMethod("GET");
//                        Log.i("register", "get: ");
//            conn.setDoOutput(true);
//                        Log.i("register", "doOutput: ");
//            conn.setRequestProperty("User-Agent", USER_AGENT);
//                        Log.i("register", "req ");
//            conn.connect();
//                        Log.i("register", "connect: ");
            Log.i("register", "ok? " + conn.getResponseMessage());
            connexOK=conn.getResponseMessage().equals("OK");
            //MainActivity.t4.setText("putPos:   " + con.getResponseMessage()); ///verif cconexiunii

            if (connexOK  ) {
                BufferedReader inBuff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.i("Register", "in" + inBuff);
                StringBuilder raspuns = new StringBuilder();
                Log.i("Register", "in" + raspuns);/// nu raspunde nimic daca nu exista; raspunde cu string "exista" daca contul exista

                String line;
                while ((line = inBuff.readLine()) != null) {
                    raspuns.append(line).append('\n');
                }
                val = raspuns.toString();
                val = val.replaceAll("\\s", "");
                Log.i("Register", "valoare" + val);/// nu raspunde nimic daca nu exista; raspunde cu string "exista" daca contul exista

            }
        } catch (Exception e) {
            // tx2.setText("err Logout");
            //tx2.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return val;
    }
        protected void onProgressUpdate(Void... progress) {

        }

    protected String onPostExecute(Void result)
    {
        return val;
    }


}
