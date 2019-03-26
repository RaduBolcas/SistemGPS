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

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class Login extends Activity {

    Button bt1, bt2, bt3;
    EditText edt1, edt2;
    public static String pass;
    public static int m=0;
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
                AttemptLogin myLogin = new AttemptLogin();

                try {
                    m= myLogin.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Log.i("Login", "Attempt login finished");
                Log.i("Login", "m " + m);

                if ( m == 1) {
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
    public void onBackPressed() {
        //TODO sa ies din aplicatie
    }

}

class AttemptLogin extends AsyncTask<Object, Object, Integer>{
    private int response =0;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            String str = InternetConnection.host +"login.php?id=" + MainActivity.iD + "&pass=" + Login.pass;
            HttpsURLConnection conn = InternetConnection.connectInternet(str);
            Log.i("Login","response code? "+conn.getResponseCode());

            BufferedReader inBuff = new BufferedReader( new InputStreamReader(conn.getInputStream()) );

            StringBuilder login = new StringBuilder();
            String line;
            while ((line = inBuff.readLine()) != null) {
                login.append(line).append('\n');
            }

            JSONObject jObj =new JSONObject(String.valueOf(login));
            Log.i("Login","jobj"+jObj.getString("connOK"));

            if(Objects.equals(jObj.getString("connOK"), "OK")){
                response =1 ;//Login.m = 1;
                Log.i("Login","response"+response);
            }
            //MainActivity.t4.setText("putPos:   " + con.getResponseMessage()); ///verif cconexiunii

        } catch (Exception e) {
            // tx2.setText("err Login");
            Log.i("Login", "exceptie: "+e.getCause());
            //tx2.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return response; //Login.m;
    }
    protected void onProgressUpdate(Void... progress) {
    }

    protected int onPostExecute(int result) {
        return result;
    }
    @Override
    protected void onPreExecute() {
    }

}

