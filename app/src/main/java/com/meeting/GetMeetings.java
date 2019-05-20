package com.meeting;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.radu.sistemgps.FindLoc;
import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
import com.example.radu.sistemgps.Meniu;
import com.example.radu.sistemgps.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;

public class GetMeetings extends AppCompatActivity {
    EditText id;
    public static TextView t1;
    private String TAG ="GetMeetings";
    public static String idMeeting ="";
    private String latitude="latitude", longitude = "longitude";
    public static final String MessagePlace="MessagePlace";
    public static ArrayList<Meeting_details> meetings = new ArrayList<Meeting_details>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_meetings);

        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);

        id = (EditText)findViewById(R.id.editTextid);
        AttemptGetMeetings getMeetings = new AttemptGetMeetings();
        try {
            getMeetings.setTAG(TAG);
            getMeetings.setTextView(t1);
            meetings=getMeetings.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final Button buttonID = (Button) findViewById(R.id.buttonID);
        buttonID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // FL based on meeting place
                // Perform action on click
                idMeeting=id.getText().toString();
                for(Meeting_details meeting :meetings){
                    if (meeting.id_Meeting.equals(idMeeting)){
                        Intent i=new Intent(GetMeetings.this, FindLoc.class);
                        i.putExtra(latitude, meeting.location.getLatitude());
                        i.putExtra(longitude,meeting.location.getLongitude());
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        final Button buttonC = (Button) findViewById(R.id.buttonC); // DeleteMeeting
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idMeeting=id.getText().toString();
                AttemptDeleteMeetings deleteMeeting = new AttemptDeleteMeetings();
                try {
                    deleteMeeting.setTAG(TAG);
                    deleteMeeting.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(GetMeetings.this, MeetingsOptions.class);
                startActivity(i);
                finish();

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent y=new Intent(GetMeetings.this, MeetingsOptions.class);
        startActivity(y);
        finish();
        return;
    }
}

class AttemptGetMeetings extends AsyncTask<Object, Object, ArrayList<Meeting_details> > {
    private String TAG ;
    private int response =0;
    protected static TextView t1;
    @Override
    protected ArrayList<Meeting_details>  doInBackground(Object... urls) {
        ArrayList<Meeting_details> mettings = new ArrayList<Meeting_details>();
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"getMeetings.php?idU=" + MainActivity.iD;
            HttpsURLConnection con =InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("meetings");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                if (jObj.getString("connOK").equals("OK")) {
                    Location location = new Location("");
                    location.setLatitude(jObj.getDouble("Latitude"));
                    location.setLongitude(jObj.getDouble("Longitude"));
                    String id_Meeting = jObj.getString("ID_Meeting");
                    String locationName = jObj.getString("LocationName");
                    String dateTime = jObj.getString("DateTime");
                    Meeting_details meeting = new Meeting_details(id_Meeting,locationName , location, dateTime);

                    mettings.add(meeting);
                    t1.append(id_Meeting + "  " + locationName + "  " + dateTime + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,"return response="+ response);
        return mettings; //serverResponse
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

class AttemptDeleteMeetings extends AsyncTask<Object, Object, Integer> {
    private String TAG;
    private int response = 0;

    @Override
    protected Integer doInBackground(Object... urls) {

        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host + "deleteMeeting.php?idM=" + GetMeetings.idMeeting;
            HttpsURLConnection con = InternetConnection.connectInternet(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "return response=" + response);
        return response; //serverResponse
    }

    protected int onPostExecute(int result) {
        return result; //serverResponse
    }

    public void setTAG(String tag) {
        this.TAG = tag;
    }

}