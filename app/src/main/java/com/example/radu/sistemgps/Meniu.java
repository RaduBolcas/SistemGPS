package com.example.radu.sistemgps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
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

            final Button buttonB = (Button) findViewById(R.id.buttonB); //GoogleMaps
            buttonB.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(),"Maps...",Toast.LENGTH_SHORT).show();
                    Intent e=new Intent(Meniu.this, MapsActivity.class);
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
        final Button buttonD = (Button) findViewById(R.id.buttonD); //Logout
        buttonD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent o=new Intent(Meniu.this, Logout.class);
                startActivity(o);
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
        return;
    }
}
