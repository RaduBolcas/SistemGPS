package com.example.radu.sistemgps;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

class AttemptIsAdmin extends AsyncTask<Object, Object, Integer> {
    protected String TAG;
    protected int response =0;
    protected String url;
    protected static TextView t1;

    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection con = InternetConnection.connectInternet(url);

            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj =new JSONObject(String.valueOf(builderString));
            JSONArray users = jUsersObj.getJSONArray("isAdmin");
            for (int i=0; i<users.length();i++ ) {
                JSONObject jObj = users.getJSONObject(i);
                String connOK = jObj.getString("connOK");
                String idGroup = jObj.getString("ID_Group");
                String group_name = jObj.getString("Group_name");
                t1.append( idGroup + "  "+group_name + "\n");
                Log.i("AttemptIsAdmin",idGroup + "   "+ group_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "responseIsAdmin =" + response);
        return response; //serverResponse
    }

    protected int onPostExecute(int result) {
        return result; //serverResponse
    }

    public void setTAG(String tag) {
        this.TAG = tag;
    }
    public void setTextView(TextView text){
        this.t1=text;
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