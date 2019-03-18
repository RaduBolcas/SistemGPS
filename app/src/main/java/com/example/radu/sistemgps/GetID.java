package com.example.radu.sistemgps;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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
    {  // JSONObject jObj = null;

        Log.i("GetID","coord: 11");

        try {
            InternetConnection.trustAllCertificates();

            String st = InternetConnection.host +"getIDs.php";
            HttpsURLConnection con =InternetConnection.connectInternet(st);

            t1.setText("GetID:   " + con.getResponseMessage()); ///verif cconexiunii

            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) ); //buffer pt a salva stream-ul
            Log.i("GetID","in: "+ in);

            StringBuilder allUsers = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                allUsers.append(line).append('\n');
            }
            Log.i("GetID","allUsers: "+ allUsers);

            JSONObject jObj =new JSONObject(String.valueOf(allUsers));
            JSONArray users = jObj.getJSONArray("users");
            for (int i=0; i<users.length();i++ ) {
                JSONObject c = users.getJSONObject(i);
                String connOK = c.getString("connOK");
                String id = c.getString("ID");
                String nickname = c.getString("Nickname");
                t2.append( id + "  "+nickname + "\n");
            }

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

}
