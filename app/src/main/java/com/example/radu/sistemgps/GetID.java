package com.example.radu.sistemgps;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.login.Login;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

public class GetID extends Activity {
    EditText id;
    public static TextView t1, t2;
    private String TAG ="GetID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);

        id = (EditText)findViewById(R.id.editTextid);


        AttemptGetID getID = new AttemptGetID();
        try {
            getID.setTAG(TAG);
            getID.setTextView(t1);
            getID.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onBackPressed() {
        Intent y=new Intent(GetID.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }

}

class AttemptGetID extends AsyncTask<Object, Object, Integer> {
    private String TAG ;
    private int response =0;
    protected static TextView t1;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"getIDs.php";
            HttpsURLConnection con =InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("users");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                String connOK = jObj.getString("connOK");
                String id = jObj.getString("ID_User");
                String nickname = jObj.getString("Nickname");
                t1.append( id + "  "+nickname + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,"return response="+ response);
        return response; //serverResponse
    }

    protected int onPostExecute(int result) {
        return result; //serverResponse
    }
    public void setTAG (String tag){
        this.TAG=tag;
    }
    protected void onProgressUpdate(Void... progress) {
    }
    public void setTextView(TextView text){
        this.t1=text;
    }
    @Override
    protected void onPreExecute() {
    }
}