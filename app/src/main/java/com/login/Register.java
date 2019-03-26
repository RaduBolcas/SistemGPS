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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Register extends Activity {
    EditText edtxt1, edtxt2, edtxt3, edtxt4;
    public static String passRegister, pass2;
    public static int m=0;

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

                Log.i("Register", "user:" + edtxt1.getText().toString());//id
                Log.i("Register", "pass:" + passRegister);
                Log.i("Register", "pass2:" + pass2);

                if (passRegister.equals(pass2) ) {
                    Logout.passLogout=passRegister;

                    Log.i("Register", "inainte de async");
                    AttemptRegister myRegister = new AttemptRegister();

                    try {
                        m=myRegister.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Log.i("Register", "Attempt Register finished");
                    Log.i("Register", "m=" + m);
                    if (m==1){
                        Toast.makeText(getApplicationContext(), "You succesfully registered. Please Login", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Register.this, Login.class);
                        startActivity(a);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "The account exists already", Toast.LENGTH_SHORT).show();
                        MainActivity.iD = null;
                        passRegister = null;
                        pass2 = null;
                        MainActivity.name = null;
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "The passwords don't match", Toast.LENGTH_SHORT).show();
                    MainActivity.iD = null;
                    passRegister = null;
                    pass2 = null;
                    MainActivity.name = null;
                }
            }
        });
    }

}
    class AttemptRegister extends AsyncTask<Object, Object, Integer>
    {
        private int response =0;

        protected Integer doInBackground(Object... urls) {

        try {
            InternetConnection.trustAllCertificates();
            String strg = InternetConnection.host + "register.php?id=" + MainActivity.iD +"&n="+ MainActivity.name+"&pass=" + Register.passRegister;
            HttpsURLConnection conn = InternetConnection.connectInternet(strg);
            Log.i("Register", "ok? " + conn.getResponseMessage());

            BufferedReader inBuff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder register = new StringBuilder();
            Log.i("Register", "inBuff=" + inBuff);/// nu raspunde nimic daca nu exista; raspunde cu string "exista" daca contul exista

            String line;
            while ((line = inBuff.readLine()) != null) {
                register.append(line).append('\n');
            }
            JSONObject jObj =new JSONObject(String.valueOf(register));
            Log.i("Register", "jobj" + jObj.getString("connOK"));/// nu raspunde nimic daca nu exista; raspunde cu string "exista" daca contul exista
            if(Objects.equals(jObj.getString("connOK"), "OK")){
                response =1 ;//Login.m = 1;
                Log.i("Register","response"+response);
            }

        } catch (Exception e) {
            // tx2.setText("err Logout");
            //tx2.setText(e.getLocalizedMessage());
            Log.i("Register", "exceptie: "+e.getCause());
            e.printStackTrace();
        }
        Log.i("Register","return response="+ response);
        return response;
    }
        protected void onProgressUpdate(Void... progress) {
        }

    protected int onPostExecute(int result) {
        return result;
    }


}
