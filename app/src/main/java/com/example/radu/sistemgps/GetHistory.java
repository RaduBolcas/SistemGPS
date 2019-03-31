package com.example.radu.sistemgps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Radu on 20-Mar-19.
 */

public class GetHistory {

    private static String connOK, dateTime ;
    private static double latitude, longitude ;
    private static int status;
    private static String time;

    public static void getHistory()
    {
        Log.i("GetHistory","");

        try {
            InternetConnection.trustAllCertificates();
            //TODO to set the "time" in days for the history records. Ask users somehow
            String st = InternetConnection.host +"getHistory.php?id=" + MainActivity.iD + "&tm=" + time ;
            HttpsURLConnection con =InternetConnection.connectInternet(st);

            //t1.setText("GetHistory:   " + con.getResponseMessage()); ///verif cconexiunii

            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) ); //buffer pt a salva stream-ul
            Log.i("GetHistory","in: "+ in);

            StringBuilder history = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                history.append(line).append('\n');
            }
            Log.i("GetHistory","history: "+ history);

            JSONObject jHistoryObj =new JSONObject(String.valueOf(history));
            JSONArray users = jHistoryObj.getJSONArray("history");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                connOK = jObj.getString("connOK");
                latitude = jObj.getDouble("latitude");
                longitude = jObj.getDouble("longitude");
                dateTime = jObj.getString("dateTime");
                status = jObj.getInt("status");
            }

        } catch (Exception e) {
            //t2.setText(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBackPressed()
//    {
//        Intent m=new Intent(GetHistory.this, ?.class);
//        startActivity(m);
//        finish();
//        return;
//    }

}
