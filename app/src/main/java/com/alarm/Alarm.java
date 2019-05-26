package com.alarm;


import android.app.AlarmManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.radu.sistemgps.R;
import com.meeting.CreateMeeting;
import com.meeting.MeetingsOptions;

import java.util.Calendar;

public class Alarm extends AppCompatActivity{
    private String TAG ="Alarm";

    //Pending intent instance
    private static PendingIntent pendingIntent;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    public int alarmMinute,alarmHour, meetingHour, meetingMinute, alarmTime=1;

    protected void onCreate (Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent i = getIntent();
        meetingHour =i.getIntExtra("meetingHour",meetingHour );
        meetingMinute= i.getIntExtra("meetingMinute",meetingMinute);

        final Button buttonD = (Button) findViewById(R.id.buttonA); // SetAlarm
        buttonD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View dialogTime = View.inflate(Alarm.this, R.layout.time, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(Alarm.this).create();

                dialogTime.findViewById(R.id.time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();

                        TimePicker timePicker = (TimePicker) dialogTime.findViewById(R.id.time_picker);
                        alarmMinute = timePicker.getCurrentMinute();
                        alarmHour=timePicker.getCurrentHour();
                        String time= Integer.toString(timePicker.getCurrentHour())+":"+Integer.toString(timePicker.getCurrentMinute());
                        Log.i(TAG,"alarmTime="+time);
                        CreateMeeting.alarm.setText("alarmTime="+time);
                        if((meetingHour> alarmHour) || (meetingHour==alarmHour && meetingMinute >alarmMinute)){
                            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                            int currentMinute = cal.get(Calendar.MINUTE);
                            int currentTime = currentHour *60*60 + currentMinute*60;
                            int aTime = alarmHour *60*60 + alarmMinute*60;
                            alarmTime= aTime-currentTime;
                            /* Retrieve a PendingIntent that will perform a broadcast */
                            Intent alarmIntent = new Intent(Alarm.this, AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(Alarm.this, ALARM_REQUEST_CODE, alarmIntent, 0);

                            Calendar calendar = Calendar.getInstance();
                            // add alarmTriggerTime seconds to the calendar object
                            calendar.add(Calendar.SECOND, alarmTime);

                            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of activity_alarm manager
                            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);//set activity_alarm manager with entered timer by converting into milliseconds

                            Toast.makeText(Alarm.this, "Alarm Set for " + alarmTime + " seconds.", Toast.LENGTH_SHORT).show();
                            finish();

                            alertDialog.dismiss();
                        }else{
                            Toast.makeText(Alarm.this, "Alarm is set after meeting time", Toast.LENGTH_SHORT).show();
                        }
                    }});
                alertDialog.setView(dialogTime);
                alertDialog.show();
            }
        });

        //set on click over stop alarm button
        final Button buttonB = (Button) findViewById(R.id.stop_alarm);
        buttonB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAlarmManager();

                Intent m=new Intent(Alarm.this, MeetingsOptions.class);
                startActivity(m);
                finish();
            }
        });

    }


    public void stopAlarmManager() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the activity_alarm manager of the pending intent

        //Stop the Media Player Service to stop sound
        stopService(new Intent(Alarm.this, AlarmSoundService.class));

        //remove the notification from notification tray
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);

        Toast.makeText(Alarm.this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent y=new Intent(Alarm.this, CreateMeeting.class);
        startActivity(y);
        finish();
        return;
    }
}
