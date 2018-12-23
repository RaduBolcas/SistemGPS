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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FindLoc extends Activity implements SensorEventListener {
    public static TextView t, t1, t2, t3, t4, t5;

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


    public static float bearingFL = 0; ///// e nevoie pt onSensorChanged pt calculul rotatiei pt cazul FL
    public static float distanceFL;

    public static Location locEu = new Location(""); //locEu=locatie telefonul meu
    public static Location locDest = new Location(""); //locDest=destinatiee


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_loc);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.pointer);
        mPointer2 = (ImageView) findViewById(R.id.pointer2);


        t = new TextView(this);
        t = (TextView) findViewById(R.id.textView);
        t1 = new TextView(this);
        t1 = (TextView) findViewById(R.id.textView1);
        t2 = new TextView(this);
        t2 = (TextView) findViewById(R.id.textView2);
        t3 = new TextView(this);
        t3 = (TextView) findViewById(R.id.textView3);
        t4 = new TextView(this);
        t4 = (TextView) findViewById(R.id.textView4);
        t5 = new TextView(this);
        t5 = (TextView) findViewById(R.id.textView5);

        ////////////////////////////////////////// verif conexiune gps
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "GPS is Disabled in your device", Toast.LENGTH_LONG).show();
        }
        /////////////////////////////////////////////////////

        locEu.setLatitude(2.0);
        locEu.setLongitude(2.0);

        Log.i("FindLoc:", "LocLong"+InsertCoord.LocLong1);
        Log.i("FindLoc", "LocLat"+InsertCoord.LocLat1);

        runThread2();/// thread
        locDest.setLongitude(Double.parseDouble(InsertCoord.LocLong1));
        locDest.setLatitude(Double.parseDouble(InsertCoord.LocLat1));
        FindLoc.t2.setText("Dest "+String.valueOf(locDest.getLatitude())+"  "+String.valueOf(locDest.getLongitude()) );
        Log.i("FindLoc:", "LocDest"+locDest.getLatitude()+"  "+locDest.getLongitude());

        LocationManager mlocManager = null;
        LocationListener mlocListener;
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();

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


    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;


            RotateAnimation ra1 = new RotateAnimation(
                    mCurrentDegree2, -azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            ra1.setDuration(250);
            ra1.setFillAfter(true);

            mPointer2.startAnimation(ra1);
            mCurrentDegree2 = -azimuthInDegrees;

            bearingFL = locEu.bearingTo(locDest);
            distanceFL = locEu.distanceTo(locDest);

            if(bearingFL<0)
            {bearingFL=bearingFL+360;}

            t1.setText("bearing:" + String.valueOf(bearingFL) );

            float heading = azimuthInDegrees - bearingFL; //directia de mers /// e necesar "-"  pt corectia sensului de rotire

//            t5.setText("distanta:" + String.valueOf(distanceFL) + "m");
            if(distanceFL<1000) {
                    t3.setText("distanta:" + String.valueOf(distanceFL) + "m");
            }else{t3.setText("distanta:" + String.valueOf(distanceFL/1000) + "km");}

            //partea de animatie /// animatia se roteste trigonometric
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree, -heading,
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


    private void runThread2() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                locEu.setLatitude(MyLocationListener.latitude);
                                locEu.setLongitude(MyLocationListener.longitude);
//
                                FindLoc.t.setText("Eu"+String.valueOf((float)locEu.getLatitude())+"  "+String.valueOf((float)locEu.getLongitude()));

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


    @Override
    public void onBackPressed()
    {
        Intent a=new Intent(FindLoc.this, Meniu.class);
        startActivity(a);
        finish();
        return;
    }
}