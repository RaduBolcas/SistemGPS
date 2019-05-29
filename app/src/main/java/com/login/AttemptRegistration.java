package com.login;

import android.os.AsyncTask;
import android.util.Log;
import com.sistemGPS.InternetConnection;
import org.json.JSONObject;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

class AttemptRegistration extends AsyncTask<Object, Object, Integer> {
    private String TAG ;
    private int response =0;
    private String url;
    @Override
    protected Integer doInBackground(Object... urls) {
        try {
            InternetConnection.trustAllCertificates();
            HttpsURLConnection conn = InternetConnection.connectInternet(url);
            Log.i(TAG,"response code="+conn.getResponseCode());
            StringBuilder builderString = InternetConnection.processServerData(conn);

            JSONObject jObj =new JSONObject(String.valueOf(builderString));
            Log.i(TAG,"jobj="+jObj.getString("connOK"));

            if(Objects.equals(jObj.getString("connOK"), "OK")){
                response =1 ;// serverResponse
                Log.i(TAG,"response="+response);
            }

        } catch (Exception e) {
            Log.i(TAG, "Exception: "+e.getCause());
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
    public void setUrl (String url){
        this.url= url;
    }
    protected void onProgressUpdate(Void... progress) {
    }
    @Override
    protected void onPreExecute() {
    }
}

