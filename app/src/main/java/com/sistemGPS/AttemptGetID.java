package com.sistemGPS;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class AttemptGetID extends AsyncTask<Object, Object, Integer> {
    private String TAG ;
    private int response =0;
    protected static TextView t1;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host +"getIDs.php";
            HttpsURLConnection con =InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("users");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                String connOK = jObj.getString("connOK");
                String id = jObj.getString("ID_User");
                String nickname = jObj.getString("Nickname");
                t1.append( id + "  "+nickname + "\n");
            }

        } catch (Exception e) {
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
    public void setTextView(TextView text){
        this.t1=text;
    }
    @Override
    protected void onPreExecute() {
    }
}
