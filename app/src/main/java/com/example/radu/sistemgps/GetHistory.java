package com.example.radu.sistemgps;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.login.Logout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class GetHistory extends AppCompatActivity {
    private String TAG ="GetHistory" ;
    EditText historyEditText;
    public static String time ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_history);
        historyEditText = (EditText)findViewById(R.id.editTextid);

        final Button showHistory = (Button) findViewById(R.id.showHistory);
        showHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                time = historyEditText.getText().toString();

                AttemptGetHistory historyInfo = new AttemptGetHistory();
                try {
                    historyInfo.setTAG(TAG);
                    HistoryMapsActivity.history=historyInfo.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Intent i=new Intent(GetHistory.this, HistoryMapsActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent x=new Intent(GetHistory.this, Meniu.class);
        startActivity(x);
        finish();
    }
}

class AttemptGetHistory extends AsyncTask<Object, Object, ArrayList<UserHistory>> {
    private static String TAG ;

    protected ArrayList<UserHistory> doInBackground(Object... urls) {

        ArrayList<UserHistory> historyInfo = new ArrayList<UserHistory>();
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host + "getHistory.php?id="+MapsActivity.iD+"&tm=" + GetHistory.time;
            HttpsURLConnection con = InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jHistoryObj = new JSONObject(String.valueOf(builderString));
            Log.i(TAG, "jHistoryObj:"+jHistoryObj.toString());
            JSONArray arr = jHistoryObj.getJSONArray("history");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jObj = arr.getJSONObject(i);
                if (jObj.getString("connOK").equals("OK")) {
                    Location location = new Location("");
                    location.setLatitude(jObj.getDouble("latitude"));
                    location.setLongitude(jObj.getDouble("longitude"));

                    UserHistory history = new UserHistory(location, jObj.getString("dateTime"), jObj.getString("status"));
                    historyInfo.add(history);
                }
            }
            Log.i(TAG, "historyAsync:" + arr.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyInfo;
    }
    protected int onPostExecute(int result) {
        return result; //serverResponse
    }
    public void setTAG (String tag){
        this.TAG=tag;
    }
    protected void onProgressUpdate(Void... progress) {
    }
    @Override
    protected void onPreExecute() {
    }
}


