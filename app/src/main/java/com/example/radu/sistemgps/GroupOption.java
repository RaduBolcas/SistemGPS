package com.example.radu.sistemgps;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chat.Chat_login;
import com.login.Logout;

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


        final Button deleteGroup = (Button) findViewById(R.id.buttonC); //Delete group
        deleteGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//TODO call isAdmin ca la GetID si apoi select group ID and call deleteGroup
                Toast.makeText(getApplicationContext(),"Delete group",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(GroupOption.this, DeleteGroup.class);
                startActivity(i);
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
