package com.example.radu.sistemgps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class CreateGroup extends Activity {
    EditText groupName;
    private String TAG ="CreateGroup";
    public int idGroup;
    public static String group_name =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupName = (EditText)findViewById(R.id.editTextid);

        final Button selectGroup = (Button) findViewById(R.id.buttonSelect);
        selectGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                group_name = groupName.getText().toString();

                AttemptCreateGroup createGroup = new AttemptCreateGroup();
                try {
                    String url = InternetConnection.host + "createGroup.php?idUA=" + MainActivity.iD+ "&nm="+ group_name;
                    createGroup.setTAG(TAG);
                    createGroup.setUrl(url);
                    idGroup= createGroup.execute().get();

                    Toast.makeText(getApplicationContext(),idGroup + "  " + group_name,Toast.LENGTH_SHORT).show();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(CreateGroup.this, GroupOption.class);
                startActivity(i);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent y=new Intent(CreateGroup.this, GroupOption.class);
        startActivity(y);
        finish();
        return;
    }

}
class AttemptCreateGroup extends AsyncTask<Object, Object, Integer> {
    private String TAG;
    int id;
    private String url;

    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con = InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jObj = new JSONObject(String.valueOf(builderString));
            id = jObj.getInt("GroupID");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "return id=" + id);
        return id; //serverResponse
    }

    protected int onPostExecute(int result) {
        return result; //serverResponse
    }

    public void setTAG(String tag) {
        this.TAG = tag;
    }

    protected void onProgressUpdate(Void... progress) {
    }

    @Override
    protected void onPreExecute() {
    }
    public void setUrl (String url){
        this.url= url;
    }
}