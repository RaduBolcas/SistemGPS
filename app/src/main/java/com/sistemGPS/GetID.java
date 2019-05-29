package com.sistemGPS;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.radu.sistemgps.R;

import java.util.concurrent.ExecutionException;

public class GetID extends Activity {
    EditText id;
    public static TextView t1, t2;
    private String TAG ="GetID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);

        id = (EditText)findViewById(R.id.editTextid);


        AttemptGetID getID = new AttemptGetID();
        try {
            getID.setTAG(TAG);
            getID.setTextView(t1);
            getID.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final Button buttonID = (Button) findViewById(R.id.buttonID);
        buttonID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                MainActivity.partneriD=id.getText().toString();
                Intent i=new Intent(GetID.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent y=new Intent(GetID.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }

}