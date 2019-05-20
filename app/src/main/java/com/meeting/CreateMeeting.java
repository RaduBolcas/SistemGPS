package com.meeting;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
import com.example.radu.sistemgps.R;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class CreateMeeting extends AppCompatActivity {
    EditText meetingName, meetingLatitude, meetingLongitude;
    public static TextView meetingDate,meetingTime;
    private String TAG ="CreateMeeting";
    public String date="",time="",dateTime="";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        meetingName = (EditText) findViewById(R.id.editText1);
        meetingLatitude = (EditText) findViewById(R.id.editText2);
        meetingLongitude = (EditText) findViewById(R.id.editText3);
        meetingDate = new TextView(this);
        meetingDate = (TextView) findViewById(R.id.textview1);
        meetingTime = new TextView(this);
        meetingTime = (TextView) findViewById(R.id.textview2);
        context =this;


        final Button buttonA = (Button) findViewById(R.id.buttonA); // CreateMeeting
        buttonA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name= meetingName.getText().toString();
                String lat = meetingLatitude.getText().toString();
                String lng = meetingLongitude.getText().toString();
                dateTime = date+time;
                //TODO Add Alarm and notification
                if (!dateTime.equals("")) {
                    String url = InternetConnection.host + "createMeeting.php?idU=" + MainActivity.iD + "&Ln=" + name + "&La=" + lat + "&Lg=" + lng + "&date=" + dateTime;
                    AttemptCreateMeetings createMeeting = new AttemptCreateMeetings();
                    try {
                        createMeeting.setTAG(TAG);
                        createMeeting.setUrl(url);
                        createMeeting.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent a = new Intent(CreateMeeting.this, MeetingsOptions.class);
                    startActivity(a);
                    finish();
                }
            }
        });

        final Button buttonB = (Button) findViewById(R.id.buttonB); // SetDate
        buttonB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View dialogDate = View.inflate(context, R.layout.date, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                dialogDate.findViewById(R.id.date_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) dialogDate.findViewById(R.id.date_picker);
                        alertDialog.dismiss();
                        Log.i(TAG,"date="+datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth());
                        int month = datePicker.getMonth()+1 ;
                        date = datePicker.getYear()+"-"+month+"-"+datePicker.getDayOfMonth()+"%"+datePicker.getDayOfMonth();
                        meetingDate.setText("date="+datePicker.getYear()+"-"+month+"-"+datePicker.getDayOfMonth());
                    }});
                alertDialog.setView(dialogDate);
                alertDialog.show();

            }
        });
        final Button buttonC = (Button) findViewById(R.id.buttonC); // SetTime
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View dialogTime = View.inflate(context, R.layout.time, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                dialogTime.findViewById(R.id.time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        TimePicker timePicker = (TimePicker) dialogTime.findViewById(R.id.time_picker);
                        Integer minute = timePicker.getCurrentMinute();
                        Integer hour=timePicker.getCurrentHour();
                        time = Integer.toString(timePicker.getCurrentHour())+":"+Integer.toString(timePicker.getCurrentMinute());
                        Log.i(TAG,"time="+time);
                        meetingTime.setText("time="+time);
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogTime);
                alertDialog.show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent y=new Intent(CreateMeeting.this, MeetingsOptions.class);
        startActivity(y);
        finish();
        return;
    }
}


class AttemptCreateMeetings extends AsyncTask<Object, Object, Integer > {
    private String TAG ;
    private int response =0;
    protected String url;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con =InternetConnection.connectInternet(url);
            StringBuilder builderString = InternetConnection.processServerData(con);

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
    public void setUrl (String url){
        this.url= url;
    }
}