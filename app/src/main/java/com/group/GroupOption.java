package com.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sistemGPS.Meniu;
import com.example.radu.sistemgps.R;


public class GroupOption extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_option);

        final Button selectGroup = (Button) findViewById(R.id.buttonA); //select
        selectGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Select Group",Toast.LENGTH_SHORT).show();
                Intent a=new Intent(GroupOption.this, Group.class);
                startActivity(a);
                finish();
            }
        });

        final Button createGroup = (Button) findViewById(R.id.buttonB); //CreateGroup
        createGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"Create new group",Toast.LENGTH_SHORT).show();
                Intent e=new Intent(GroupOption.this, CreateGroup.class);
                startActivity(e);
                finish();
            }
        });

        final Button addMember = (Button) findViewById(R.id.buttonC); //Add Member
        addMember.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Add or Remove Member",Toast.LENGTH_SHORT).show();
                Intent u=new Intent(GroupOption.this, EditGroupMember.class);
                startActivity(u);
                finish();

            }
        });

        final Button deleteGroup = (Button) findViewById(R.id.buttonD); //Delete group
        deleteGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Delete group",Toast.LENGTH_SHORT).show();
                Intent r=new Intent(GroupOption.this, DeleteGroup.class);
                startActivity(r);
                finish();

            }
        });


    }
    @Override
    public void onBackPressed()
    {
        Intent x=new Intent(GroupOption.this, Meniu.class);
        startActivity(x);
        finish();
        return;
    }

}
