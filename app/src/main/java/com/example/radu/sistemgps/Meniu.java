package com.example.radu.sistemgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.Chat_login;
import com.login.Logout;


public class Meniu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meniu);

        final Button buttonA = (Button) findViewById(R.id.buttonA); // FF
        buttonA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"FF...",Toast.LENGTH_SHORT).show();
                Intent a=new Intent(Meniu.this, GetID.class);
                startActivity(a);
                finish();
            }
        });

        final Button buttonB = (Button) findViewById(R.id.buttonB); //GroupOption
        buttonB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Groups...",Toast.LENGTH_SHORT).show();
                Intent e=new Intent(Meniu.this, GroupOption.class);
                startActivity(e);
                finish();
            }
        });

                /*pt locatie fixaaaa*/
        final Button buttonC = (Button) findViewById(R.id.buttonC); //FL
        buttonC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(getApplicationContext(),"FL...",Toast.LENGTH_SHORT).show();

                Intent i=new Intent(Meniu.this, InsertCoord.class);
                startActivity(i);
                finish();

            }
        });

        final Button buttonD = (Button) findViewById(R.id.buttonD); //Chat
        buttonD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent o=new Intent(Meniu.this, Chat_login.class);
                startActivity(o);
                finish();

            }
        });

        final Button buttonE = (Button) findViewById(R.id.buttonE); //Logout
        buttonE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent o=new Intent(Meniu.this, Logout.class);
                startActivity(o);
                finish();

            }
        });
        final Button buttonF = (Button) findViewById(R.id.buttonF); //Logout
        buttonF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent t=new Intent(Meniu.this, GetHistory.class);
                startActivity(t);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent x=new Intent(Meniu.this, Logout.class);
        startActivity(x);
        finish();
    }
}
