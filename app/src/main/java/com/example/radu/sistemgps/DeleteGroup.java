package com.example.radu.sistemgps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class DeleteGroup extends Activity {

    EditText groupName;
    private String TAG ="DeleteGroup";
    public static TextView t1, t2;
    public int idGroup;
    public static String group_name =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);

        groupName = (EditText)findViewById(R.id.editTextid);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);

        AttemptIsAdmin isAdmin = new AttemptIsAdmin();
        try {
            String url = InternetConnection.host + "isAdmin.php?idUA=" + MainActivity.iD;
            isAdmin.setTAG(TAG);
            isAdmin.setUrl(url);
            isAdmin.setTextView(t2);
            idGroup= isAdmin.execute().get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final Button selectGroup = (Button) findViewById(R.id.buttonDelete);
        selectGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                group_name = groupName.getText().toString();
                AttemptDeleteGroup deleteGroup = new AttemptDeleteGroup();
                try {
                    String url2 = InternetConnection.host + "deleteGroup.php?idG=" + group_name;
                    deleteGroup.setTAG(TAG);
                    deleteGroup.setUrl(url2);
                    deleteGroup.execute().get();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Intent i=new Intent(DeleteGroup.this, GroupOption.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent y=new Intent(DeleteGroup.this,GroupOption.class);
        startActivity(y);
        finish();
        return;
    }

}


class AttemptDeleteGroup extends AttemptIsAdmin {

    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con = InternetConnection.connectInternet(url);
            Log.i(TAG, "okDeleteGroup="+con.getResponseMessage());
            Log.i(TAG, "urlDeleteGroup="+url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "responseDeleteGroup =" + response);
        return response; //serverResponse
    }


}