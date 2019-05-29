package com.sistemGPS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.radu.sistemgps.R;


public class InsertCoord extends Activity {

    EditText edt1,edt2;
    public static String LocLong1, LocLat1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_coord);

        edt1 = (EditText)findViewById(R.id.editTextX);
        edt2 = (EditText)findViewById(R.id.editTextY);

        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                LocLat1=edt1.getText().toString();
                LocLong1= edt2.getText().toString();
                Log.i("Insert coord:", "LocLat"+LocLat1);
                Log.i("Insert coord:", "LocLong"+LocLong1);

                Toast.makeText(getApplicationContext(),"FindLoc",Toast.LENGTH_SHORT).show();

                Intent m=new Intent(InsertCoord.this, FindLoc.class);
                m.putExtra("latitude", Double.parseDouble(LocLat1));
                m.putExtra("longitude",Double.parseDouble(LocLong1));
                startActivity(m);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        Intent a=new Intent(InsertCoord.this, Meniu.class);
        startActivity(a);
        finish();
        return;
    }

}
