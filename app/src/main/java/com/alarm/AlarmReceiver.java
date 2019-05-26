package com.alarm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.radu.sistemgps.R;
import com.meeting.CreateMeeting;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by Radu on 25-May-19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent) {
        Toast.makeText(context, "ALARM!! You have a meeting soon!!", Toast.LENGTH_SHORT).show();

        //Stop sound service to play sound for activity_alarm
        context.startService(new Intent(context, AlarmSoundService.class));

        //This will send a notification message and show notification in notification tray
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmNotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

    }
}