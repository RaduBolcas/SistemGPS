package com.meeting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sistemGPS.Meniu;
import com.example.radu.sistemgps.R;

public class MeetingsOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_options);

        final Button buttonA = (Button) findViewById(R.id.buttonA); // ListMeetings
        buttonA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Meetings...",Toast.LENGTH_SHORT).show();
                Intent a=new Intent(MeetingsOptions.this, GetMeetings.class);
                startActivity(a);
                finish();
            }
        });
        final Button buttonB = (Button) findViewById(R.id.buttonB); //CreateMeeting
        buttonB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent e=new Intent(MeetingsOptions.this, CreateMeeting.class);
                startActivity(e);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent y=new Intent(MeetingsOptions.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }
}
