package com.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.radu.sistemgps.Group;
import com.example.radu.sistemgps.GroupOption;
import com.example.radu.sistemgps.InternetConnection;
import com.example.radu.sistemgps.MainActivity;
import com.example.radu.sistemgps.R;
import java.util.concurrent.ExecutionException;

public class Register extends Activity {
    EditText edtxt1, edtxt2, edtxt3, edtxt4;
    public static String passRegister, pass2;
    public static int serverResponse=0;
    private String TAG ="Register" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtxt1 = (EditText) findViewById(R.id.editTexta);
        edtxt2 = (EditText) findViewById(R.id.editTextb);
        edtxt3 = (EditText) findViewById(R.id.editTextc);
        edtxt4 = (EditText) findViewById(R.id.editTextd);

        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                MainActivity.iD = edtxt1.getText().toString();
                passRegister = edtxt2.getText().toString();
                pass2 = edtxt3.getText().toString();
                MainActivity.name = edtxt4.getText().toString();

                Log.i(TAG, "user:" + edtxt1.getText().toString());//id
                Log.i(TAG, "pass:" + passRegister);
                Log.i(TAG, "pass2:" + pass2);

                if (passRegister.equals(pass2) ) {
                    Logout.passLogout=passRegister;

                    Log.i(TAG, "Before asyncTask block");
                    AttemptRegistration myRegister = new AttemptRegistration();

                    try {
                        String url =InternetConnection.host + "register.php?id=" + MainActivity.iD +"&n="+ MainActivity.name+"&pass=" + Register.passRegister;
                        myRegister.setUrl(url);
                        myRegister.setTAG(TAG);
                        serverResponse=myRegister.execute().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "Attempt Register finished");
                    Log.i(TAG, "serverResponse=" + serverResponse);
                    if (serverResponse==1){
                        Toast.makeText(getApplicationContext(), "You succesfully registered. Please Login", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Register.this, Login.class);
                        startActivity(a);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "The account exists already", Toast.LENGTH_SHORT).show();
                        resetRegisterValues();
                        Log.i(TAG, "The account exists already");
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "The passwords don't match", Toast.LENGTH_SHORT).show();
                    resetRegisterValues();
                    Log.i(TAG, "The passwords don't match");
                }
            }
        });
    }
    private void resetRegisterValues() {

        MainActivity.iD = null;
        passRegister = null;
        pass2 = null;
        MainActivity.name = null;
    }

    @Override
    public void onBackPressed() {
        Intent y=new Intent(Register.this, Login.class);
        startActivity(y);
        finish();
        return;
    }
}
