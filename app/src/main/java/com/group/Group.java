package com.group;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.sistemGPS.InternetConnection;
import com.sistemGPS.MainActivity;
import com.example.radu.sistemgps.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;

public class Group extends Activity {
    EditText id;
    public static TextView t1, t2;
    private String TAG ="Group";
//    public static String groupID;
//    ArrayList<String> al = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);

        id = (EditText)findViewById(R.id.editTextid);

        AttemptGetGroupMmbership getGroupMmbership = new AttemptGetGroupMmbership();
        try {
            getGroupMmbership.setTAG(TAG);
            getGroupMmbership.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final Button selectGroup = (Button) findViewById(R.id.buttonSelect);
        selectGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapsActivity.groupID = id.getText().toString();
                Intent i=new Intent(Group.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

      @Override
    public void onBackPressed() {
        Intent y=new Intent(Group.this, GroupOption.class);
        startActivity(y);
        finish();
        return;
    }

}

class AttemptGetGroupMmbership extends AsyncTask<Object, Object, Integer> {
    private String TAG ;
    private int response =0;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"getMyGroupMembership.php?idU=" + MainActivity.iD;
            HttpsURLConnection con =InternetConnection.connectInternet(url);
            Group.t1.setText("GetGroup:   " + con.getResponseMessage()); ///verif cconexiunii

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("groups");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                String connOK = jObj.getString("connOK");
                String idGroup = jObj.getString("ID_Group");
                String group_name = jObj.getString("Group_name");
                Group.t2.append( idGroup + "  "+group_name + "\n");
            }

        } catch (Exception e) {

            Group.t2.setText(e.getLocalizedMessage());
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
    protected void onProgressUpdate(Void... progress) {
    }
    @Override
    protected void onPreExecute() {
    }
}