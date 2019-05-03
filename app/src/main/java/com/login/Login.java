package com.login;

import android.content.Intent;
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
import com.example.radu.sistemgps.MapsActivity;
import com.example.radu.sistemgps.Meniu;
import com.example.radu.sistemgps.R;

import java.util.concurrent.ExecutionException;

public class Login extends Activity {

    private String TAG ="Login" ;
    Button bt1, bt2, bt3;
    EditText edt1, edt2;
    public static String pass;
    public static int serverResponse=0;
    static TextView txt1, txt2, txt3;
    int k = 5; //numara incercarile de autentificare

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
            public void onClick(View v){ // Login

                MainActivity.iD = edt1.getText().toString();
                MapsActivity.iD=MainActivity.iD;
                pass = edt2.getText().toString();
                Logout.passLogout=pass;

                Log.i(TAG, "Before asyncTask block");
                AttemptRegistration myLogin = new AttemptRegistration();

                try {
                    String url = InternetConnection.host +"login.php?id=" + MainActivity.iD + "&pass=" + Login.pass;
                    myLogin.setUrl(url);
                    myLogin.setTAG(TAG);
                    serverResponse= myLogin.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Attempt login finished");
                Log.i(TAG, "serverResponse=" + serverResponse);
    /////////////////////// TODO Delete the following shortcut
                if(pass.equals("")){serverResponse=1;
                    MainActivity.iD = "1";
                    MapsActivity.iD=MainActivity.iD;
                    Logout.passLogout=pass;}
    ///////////////////////TODO delete above
                if ( serverResponse == 1) {
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
            public void onClick(View v) { // Register
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

