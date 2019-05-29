package com.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sistemGPS.InternetConnection;
import com.sistemGPS.MainActivity;
import com.example.radu.sistemgps.R;

import java.util.concurrent.ExecutionException;

public class EditGroupMember extends Activity {
    public static TextView t1, t2;
    EditText id;
    private String TAG ="EditGroupMember";
    public static String groupID;
    public static final String Message="Member";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_member);

        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView7);
        id = (EditText)findViewById(R.id.editGroupID);

        AttemptIsAdmin isAdmin = new AttemptIsAdmin();
        try {
            String url = InternetConnection.host + "isAdmin.php?idUA=" + MainActivity.iD;
            isAdmin.setTAG(TAG);
            isAdmin.setUrl(url);
            isAdmin.setTextView(t1);
            isAdmin.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        final Button removeMember = (Button) findViewById(R.id.buttonDeleteMember); //Remove Member
        removeMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                groupID = id.getText().toString();
                Toast.makeText(getApplicationContext(),"Remove Member",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditGroupMember.this, AddRemoveMember.class);
                i.putExtra(Message, 0);
                startActivity(i);
                finish();
            }
        });
        final Button addMember = (Button) findViewById(R.id.buttonAddMember);//Add Member
        addMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                groupID = id.getText().toString();
                Toast.makeText(getApplicationContext(),"Add Member",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditGroupMember.this, AddRemoveMember.class);
                i.putExtra(Message, 1);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent x=new Intent(EditGroupMember.this, GroupOption.class);
        startActivity(x);
        finish();
        return;
    }
}
