package com.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import javax.net.ssl.HttpsURLConnection;

import com.sistemGPS.AttemptGetID;
import com.sistemGPS.InternetConnection;
import com.example.radu.sistemgps.R;

public class AddRemoveMember extends Activity {
    int message = 0;
    private String TAG ="AddRemoveMember";
    public static String userID;
    EditText id;
    public static TextView t1,t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_member);
        Intent i = getIntent();
        message = i.getIntExtra(EditGroupMember.Message, message);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);
        id = (EditText)findViewById(R.id.editTextAddRemoveMember);

        if (message ==1) { // add Member
            AttemptGetID getID = new AttemptGetID();
            try {
                getID.setTAG(TAG);
                getID.setTextView(t1);
                getID.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else { // Remove Member
            AttemptListMembers listMembers = new AttemptListMembers();
            try{
                String url = InternetConnection.host + "getGroupMembers.php?idG=" + EditGroupMember.groupID;
                listMembers.setTAG(TAG);
                listMembers.setUrl(url);
                listMembers.setTextView(t1);
                listMembers.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        final Button addRemoveMemberButton = (Button) findViewById(R.id.buttonAddRemoveMember); //Add or Remove Member
        addRemoveMemberButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userID = id.getText().toString();
                Log.i("AddRemoveMember","userID="+userID);
                AttemptAddRemoveMember addRemoveMember = new AttemptAddRemoveMember();
                String url = InternetConnection.host + "addRemoveMember.php?idU=" +userID + "&idG=" +EditGroupMember.groupID + "&idAR=" +message;
                try {
                    addRemoveMember.setTAG(TAG);
                    addRemoveMember.setUrl(url);
                    addRemoveMember.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Intent i=new Intent(AddRemoveMember.this, GroupOption.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent x=new Intent(AddRemoveMember.this, GroupOption.class);
        startActivity(x);
        finish();
        return;
    }
}

class AttemptAddRemoveMember extends AttemptIsAdmin {

    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con = InternetConnection.connectInternet(url);
            Log.i(TAG, "okAddRemoveMember="+con.getResponseMessage());
            Log.i(TAG, "urlAddRemoveMember="+url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "responseAddRemoveMember=" + response);
        return response; //serverResponse
    }
}
class AttemptListMembers extends AttemptIsAdmin {

    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con = InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("members");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                String connOK = jObj.getString("connOK");
                String idUser = jObj.getString("ID_User");
                String nickName = jObj.getString("NickName");
                AddRemoveMember.t1.append( idUser + "  "+nickName + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "responseListMembers=" + response);
        return response; //serverResponse
    }
}