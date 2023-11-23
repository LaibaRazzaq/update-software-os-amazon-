package com.example.appUpdate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.appUpdate.R;

public class SensorDetail extends AppCompatActivity  {
      SensorManager sensorManager;
      private Sensor mSelectedSensor;
      int sensorType;
      Admenager admenager;
    int limit_value=1;
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    TextView vendor,type,power,resolution,maxRange,isWakeUp,isDynamic,sensorName, sensorName1,x_value, y_value,z_value;
    @SuppressLint("MissingInflatedId")

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        admenager=new Admenager(this,this);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);

        editor=sharedPreferences.edit();


        limit_value=sharedPreferences.getInt("show_ad_value",0);
        Log.d("limit value",""+limit_value);

//        if (limit_value%2==1){
            Log.d("limit value","ad load");
            admenager.load_InterstitialAd("admobe_interestitial_sensor_detail_back","admobe_interestitial_sensor_detail_back_test");

   //     }
//        editor.putInt("show_ad_value",limit_value+1);
//        editor.apply();
      //  sharedPreferences = this.getSharedPreferences("Your Prefrence", Activity.MODE_PRIVATE);
//        limit_value = sharedPreferences.getInt("limit_value1", 1);
//        if(limit_value==1||limit_value==4){
//            admenager.load_InterstitialAd(getString(R.string.admobe_interestitial_sensor_detail_back));
//
//        } else if(limit_value>4){
//            limit_value=0;
//        }
//        editor = sharedPreferences.edit();
//
//        editor.putInt("limit_value1",limit_value+1);
//        editor.apply();



        sensorName=findViewById(R.id.sensorName);
        sensorName1=findViewById(R.id.sensorName1);
        vendor=findViewById(R.id.vendor);
        type=findViewById(R.id.type);
        power=findViewById(R.id.power);
        resolution=findViewById(R.id.resolution);
        maxRange=findViewById(R.id.maxRange);
        isWakeUp=findViewById(R.id.isWakeUp);
        x_value=findViewById(R.id.xValue);
        y_value=findViewById(R.id.y_value);
        z_value=findViewById(R.id.z_value);

        isDynamic=findViewById(R.id.isDynamic);
        Intent intent=getIntent();


        sensorName.setText(intent.getStringExtra("sensorName"));
        sensorName1.setText(intent.getStringExtra("sensorName"));

        vendor.setText(intent.getStringExtra("vendor"));
        type.setText(intent.getStringExtra("sensorName"));
        resolution.setText(intent.getStringExtra("resulation"));
        maxRange.setText(intent.getStringExtra("maxRange"));
        power.setText(intent.getStringExtra("power"));
        sensorType=intent.getIntExtra("type",-1);
        boolean wakeUp=intent.getBooleanExtra("wakeUp",false);
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSelectedSensor=sensorManager.getDefaultSensor(sensorType);


        if (wakeUp){
            isWakeUp.setText("Yes");
        }else {
            isWakeUp.setText("No");
        }
        boolean dynamic=intent.getBooleanExtra("dynamic",false);

        if (dynamic){
            isDynamic.setText("Yes");
        }else {
            isDynamic.setText("No");
        }
    }

    private  SensorEventListener sensorEventListener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor==mSelectedSensor && event.values.length >= 3){
                float x=event.values[0];
                float y=event.values[1];
                float z=event.values[2];

                x_value.setText(String.format("%.2f", x));
                y_value.setText(String.format("%.2f", y));
                z_value.setText(String.format("%.2f", z));


                Log.d("rcv","sensor" +x);
                Log.d("rcv","sensor" +y);
                Log.d("rcv","sensor" +z);
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener,mSelectedSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void onBackPressed () {
        admenager.showadmobeInterstitialAd();
        super.onBackPressed();

    }

}