package com.example.radu.sistemgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {

    public static TextView t, t1, t2, t3, t4, t5, t6;

    private ImageView mPointer;
    private ImageView mPointer2;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private float mCurrentDegree2 = 0f;

    public static Location loc = null;
    public static int myStatus=0;
    public static String iD ;
    public static String partneriD;

    public static String name ;
    public static float bearing = 0; ///// metoda onSensorChanged - calculul rotatiei

    public static Location hisLocation = new Location(""); //loc2=locatie telefonul partener
    public static Location myLocation = new Location(""); //loc1=locatie telefonul meu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.pointer);
        mPointer2 = (ImageView) findViewById(R.id.pointer2);


        Switch mainSwitch= (Switch) findViewById(R.id.switch1); // seteaza dinamic val status
        mainSwitch.setChecked(false);
        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    myStatus=1;// Status is Visible
                }else{
                  //Status is Invisible
                    myStatus=0;
                }
            }
        });

        t = new TextView(this);   ///locatia mea
        t = (TextView) findViewById(R.id.textView);
        t1 = new TextView(this);        //// bearing
        t1 = (TextView) findViewById(R.id.textView1);
        t2 = new TextView(this);        //// locatie partener
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = new TextView(this);        //// verif getPos
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = new TextView(this);        //// verif putPos
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = new TextView(this);        //// distanta
        t5 = (TextView) findViewById(R.id.textView5);
        t6 = new TextView(this);        //// info
        t6 = (TextView) findViewById(R.id.textView6);

        myLocation.setLatitude(2.0);
        myLocation.setLongitude(2.0);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener  = new MyLocationListener();

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // Check if GPS is active
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "GPS is Disabled in your device", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mlocListener);

        runThread();/// thread to update GetPosition and PutPosition
        updateHistoryThread();
    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == mAccelerometer)
            {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
            }
        else if (event.sensor == mMagnetometer)
            {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
            }

        if (mLastAccelerometerSet && mLastMagnetometerSet)
        {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float)(Math.toDegrees(azimuthInRadians)+360)%360; //limitează valoarea la intervalul [0,360]

            Log.i("Main Activity","azimuth"+azimuthInDegrees);

            //// aceasta animatie indica nordul
            RotateAnimation ra1 = new RotateAnimation(
                    mCurrentDegree2,-azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            ra1.setDuration(250);
            ra1.setFillAfter(true);

            mPointer2.startAnimation(ra1);
            mCurrentDegree2 = -azimuthInDegrees;
            ////////////////////////////////

            bearing=GetPos.retBearing();
            //bearing = GetPos.bearinggg(loc1, loc2); //calcul alternativ de bearing
            MainActivity.t1.setText("bearing:"+String.valueOf(bearing));
            Log.i("Main Activity","bearing"+bearing);
            float heading = azimuthInDegrees - bearing; //directia de mers /// e necesar "-"  pt corectia sensului de rotire

            Log.i("Main Activity","heading"+heading);
            if(GetPos.hisStatus!=0) {
                if (GetPos.retDistance() < 1000) {
                    t5.setText("distanta:" + String.valueOf(GetPos.retDistance()) + "m");
                } else {
                    t5.setText("distanta:" + String.valueOf(GetPos.retDistance() / 1000) + "km");
                }
            }else{
                t5.setText("distanta:Invisible");
            }
            //animatia indica direactia de parcurs /// animatia se roteste trigonometric
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,-heading,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -heading;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed()
    {
        Intent a=new Intent(MainActivity.this, Meniu.class);
        startActivity(a);
        finish();
        return;
    }


    private void runThread() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                MyLocationListener.updateMyPos(myLocation);//TODO should be useless // TO CHECK
                                MainActivity.t.setText("Eu"+(float)loc.getLatitude() + "   " + (float)loc.getLongitude());
                                GetPos.updateHisPos(hisLocation);
                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private void updateHistoryThread() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                MyLocationListener.updateMyHistory(myLocation);

                            }
                        });
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}

