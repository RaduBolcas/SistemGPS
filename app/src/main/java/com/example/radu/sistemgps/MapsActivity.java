package com.example.radu.sistemgps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
//import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener,OnMapReadyCallback {
    private static String TAG ="MapsActivity";
    public static String groupID;
    public static String iD;
    public static ArrayList<Group_user_details> groupMembers = new ArrayList<Group_user_details>();
    public static ArrayList<Group_user_details> previousGroupMembers = new ArrayList<Group_user_details>();
    public static ArrayList<Marker> markers = new ArrayList<Marker>();
    public static Location myLocation = new Location(""); //loc1=locatie telefonul meu


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationListener mlocListener  = new MyLocationListener();
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            return;
//        }
//        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mlocListener);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, "Location permission needed", Toast.LENGTH_LONG).show();
            finish();
        }
        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(member));
        runThread();
//        updateHistoryThread();

    }

    @Override
    public void onBackPressed()
    {
        Intent y=new Intent(MapsActivity.this, Meniu.class);
        startActivity(y);
        finish();
        return;
    }

//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
//    }

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
//                                MyLocationListener.updateMyPos(myLocation);
//                                ArrayList <Group_user_details> temp = new ArrayList<>();
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

                                        for(Group_user_details member : previousGroupMembers){
                                            Log.i(TAG, "prevGroup"+member.idUser +member.nickName+member.getMarker().getId());
                                        }
                                    } else {
                                        for (Group_user_details member : groupMembers) {
                                            Log.i(TAG, "groupMembers:" + member.nickName);

                                            int index = previousGroupMembers.indexOf(member);
                                            Log.i(TAG, "index:" + index);
                                            if (index == -1) {//new member
                                                member.createMarker(mMap);
                                                previousGroupMembers= (ArrayList<Group_user_details>) groupMembers.clone();
                                            }else{
                                                member.setMarker(previousGroupMembers.get(index).getMarker());
                                                member.updateMarker();
                                                previousGroupMembers= (ArrayList<Group_user_details>) groupMembers.clone();
                                                previousGroupMembers.get(index).getMarker().remove();
                                            }
                                        }
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }

//                                for (int i = 0; i < groupMembers.size(); i++) {
//                                    if (groupMembers.get(i).status.equals("1")) {
//                                        for (int j = 0; j < previousGroupMembers.size(); j++) {
//                                            Log.i(TAG, "groupMembers:" + groupMembers.get(i).nickName);
//                                            Log.i(TAG, "prevGroupMembers:" + previousGroupMembers.get(j).nickName);
//                                            if (groupMembers.get(i).idUser.equals(previousGroupMembers.get(j).idUser)) {
//
//                                                Log.i(TAG, "prevMarker:" + previousGroupMembers.get(j).getMarker().getId());
//
//                                                groupMembers.get(i).setMarker(previousGroupMembers.get(j).getMarker());
//                                                previousGroupMembers.set(j, groupMembers.get(i));
//                                                groupMembers.get(i).updateMarker();
//
//                                                Log.i(TAG, "prevMarker2:" + previousGroupMembers.get(j).getMarker().getId());
//                                                Log.i(TAG, "currentMarker2:" + groupMembers.get(i).getMarker().getId());
//                                            }
//
//                                            else if (groupMembers.indexOf(previousGroupMembers.get(j))== -1) {//previousGroupMember doesn't exist in groupMembers
//                                                previousGroupMembers.get(j).getMarker().remove();            // a member has left the group
//                                                continue;
//                                            } else if(previousGroupMembers.indexOf(groupMembers.get(i))==-1){//groupMember doesn't exist in previousGroupMember
//                                                groupMembers.get(i).createMarker(mMap);                      //a new member was added to the group
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }


//                                    markers.get(j).remove();
//                                for(int j=0;j<markers.size();j++){
//                                    markers.get(j).remove();
//                                }
//                                mMap.clear();

//                                Log.i(TAG, "groupMembers:"+ Arrays.toString(groupMembers.toArray()));
//                                for(Group_user_details member : groupMembers){
//
//                                    if(member.status.equals("1")) {
////                                            Log.i(TAG, "member:"+groupMembers.get(i).toString());
//                                        member.createMarker(mMap);
//                                        markers.add(member.marker);
//                                        Log.i(TAG, "marker:"+member.marker.getId());
//                                        Log.i(TAG, "markerList:"+markers.toString());
////                                            marker = null;
//                                    }
//
//                                }
//                                groupMembers=null;
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
//    private void updateHistoryThread() {
//
//        new Thread() {
//            public void run() {
//                while (true) {
//                    try {
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                MyLocationListener.updateMyHistory(myLocation);
//
//                            }
//                        });
//                        Thread.sleep(600000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
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
//                    Log.i(TAG, "memberA:"+member.toString());
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