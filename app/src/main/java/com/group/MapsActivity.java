package com.group;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.sistemGPS.InternetConnection;
import com.sistemGPS.Meniu;
import com.example.radu.sistemgps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener,OnMapReadyCallback {
    private static String TAG ="MapsActivity";
    public static String groupID;
    public static String iD;
    public static ArrayList<Group_user_details> groupMembers = new ArrayList<Group_user_details>();
    public static ArrayList<Group_user_details> previousGroupMembers = new ArrayList<Group_user_details>();
    public static Location myLocation = null;
    LocationManager locationManager;
    Context context;
    public String myStatus ="1";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            myLocation= location;
//            String msg="Latitude: "+location.getLatitude() + " Longitude: "+location.getLongitude();
//            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
        }
            @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission needed", Toast.LENGTH_LONG).show();
            finish(); //if the permission is not granted, we exit the activity
        }
        mMap.setMyLocationEnabled(true);
        context=this;
        locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                5000,
                0, locationListener);
        runThread();
        updatePosition();
        updateHistory();
    }

    @Override
    public void onBackPressed() {
        Intent y=new Intent(MapsActivity.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void runThread() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                AttemptGetGroupInfo memberInfo = new AttemptGetGroupInfo();
                                try {
                                    memberInfo.setTAG(TAG);
                                    groupMembers=memberInfo.execute().get();
                                    if(previousGroupMembers.isEmpty()){

                                        Log.i(TAG, "Create previous and current GroupMember:");
                                        for(Group_user_details member : groupMembers){
                                            member.createMarker(mMap);
                                        }
                                        previousGroupMembers= (ArrayList<Group_user_details>) groupMembers.clone();

//                                        for(Group_user_details member : previousGroupMembers){
//                                            Log.i(TAG, "newGroup:"+ member+":"+member.idUser +":"+member.nickName +":"+member.getMarker().getId());
//                                        }
                                    } else {
                                        for (Group_user_details prevMember : previousGroupMembers){//remove previous markers
                                            prevMember.getMarker().remove();
                                            Log.i(TAG, "prevMember:" + prevMember.nickName);
                                        }
                                        for (Group_user_details member : groupMembers) { //create new markers
                                                member.createMarker(mMap);
                                                Log.i(TAG, "newGroupMemberUpdate:" + member.nickName+":"+member.getMarker().getId());
                                        }
                                        previousGroupMembers= (ArrayList<Group_user_details>) groupMembers.clone();
//                                        for(Group_user_details member : previousGroupMembers){
//                                            Log.i(TAG, "prevGroup:"+ member+":"+member.idUser +":"+member.nickName +":"+member.getMarker().getId());
//                                        }
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void updatePosition() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (myLocation != null) {
                            String url = InternetConnection.host + "putPosition.php?idU=" + iD
                                    + "&La=" + myLocation.getLatitude() + "&Lg=" + myLocation.getLongitude() + "&st=" + myStatus;
                            InternetConnection.trustAllCertificates();
                            HttpsURLConnection con = InternetConnection.connectInternet(url);
                            Log.i(TAG, "URL locationListener: " + url);
                            Log.i(TAG, "LocationListener: " + con.getResponseMessage());
                        }
                        Thread.sleep(3000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private void updateHistory() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        if (myLocation != null) {
                            String url = InternetConnection.host + "putHistory.php?idU=" + iD + "&La=" + myLocation.getLatitude()
                                    + "&Lg=" + myLocation.getLongitude() + "&st=" + myStatus;
                            InternetConnection.trustAllCertificates();
                            HttpsURLConnection con = InternetConnection.connectInternet(url);
                            Log.i(TAG, "URL locationListenerHistory: " + url);
                            Log.i(TAG, "LocationListenerHistory: " + con.getResponseMessage());
                        }
                        Thread.sleep(600000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}

class AttemptGetGroupInfo extends AsyncTask<Object, Object, ArrayList<Group_user_details>> {
    private static String TAG ;

    protected ArrayList<Group_user_details> doInBackground(Object... urls) {

        ArrayList<Group_user_details> membersInfo = new ArrayList<Group_user_details>();
        try {
            InternetConnection.trustAllCertificates();
            String url = InternetConnection.host + "getMyGroupPosition.php?idU="+MapsActivity.iD+"&idG=" + MapsActivity.groupID;
            HttpsURLConnection con = InternetConnection.connectInternet(url);
            StringBuilder builderString = InternetConnection.processServerData(con);

            JSONObject jUsersObj = new JSONObject(String.valueOf(builderString));
            Log.i(TAG, "jUsersObj:"+jUsersObj.toString());
            JSONArray members = jUsersObj.getJSONArray("members");
            for (int i = 0; i < members.length(); i++) {
                JSONObject jObj = members.getJSONObject(i);
                if (jObj.getString("connOK").equals("OK")) {
                    Location location = new Location("");
                    location.setLatitude(jObj.getDouble("latitude"));
                    location.setLongitude(jObj.getDouble("longitude"));

                    Group_user_details member = new Group_user_details(jObj.getString("ID_User"), jObj.getString("Nickname"), location, jObj.getString("dateTime"), jObj.getString("status"));
                    membersInfo.add(member);
                }
            }
            Log.i(TAG, "MembersAsync:" + members.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return membersInfo;
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

