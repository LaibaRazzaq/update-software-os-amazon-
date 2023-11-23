package com.example.appUpdate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.File;

public class PhoneInfoActivity extends AppCompatActivity {
    TextView capacity, health, temperature, androidVersion, technology, buildId, charge, model, volta;
    DonutProgress ramUsage, ramFree;
    private int ramFreeStatus = 0;
    private int progressStatus = 0;
    double ramUsagePercentage, ramFreePercentage = 50;
    double totalRam, ramUsageInt, ramFreeInt = 0;
    double mileVoltage, voltage = 0;

    BackTrackUtils backTrackUtils;

    long availableMegs,percentageAvail,megAvailable,freeBytesInternal=0;
    private Handler handler = new Handler();
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}
    BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.e("string", bundle.toString()+"");

            try {
                technology.setText(bundle.getString("technology"));
                if (technology == null){
                    technology.setText("Null");
                }
                health.setText(getHealthString(bundle.getInt("health")));
                temperature.setText(bundle.getInt("temperature") / 10 + " \u2103");
                mileVoltage = bundle.getInt("voltage");
                voltage = mileVoltage/1000;
                volta.setText(voltage + " V");
                charge.setText(getPlugTypeString(bundle.getInt("plugged")));
                model.setText(Build.MODEL);
                androidVersion.setText(Build.VERSION.RELEASE + "");
                buildId.setText(Build.ID + "");
            }
            catch (NullPointerException e){

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);
        backTrackUtils=new BackTrackUtils(this,this);

        technology = (TextView) findViewById(R.id.fragmentStaticTvTechnology);
        temperature = (TextView) findViewById(R.id.fragmentStaticTvTemperature);
        androidVersion = (TextView) findViewById(R.id.fragmentStaticAndroidVersion);
        health = (TextView) findViewById(R.id.fragmentStaticTvHealth);
        buildId = (TextView) findViewById(R.id.fragmentStaticTvBuildId);
        charge = (TextView) findViewById(R.id.fragmentStaticTvCharger);
        volta = (TextView) findViewById(R.id.fragmentStaticTvVoltage);
        model = (TextView) findViewById(R.id.fragmentStaticTvModel);
        ramFree = (DonutProgress) findViewById(R.id.ram_free);
        ramUsage = (DonutProgress) findViewById(R.id.ram_usage_circle);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



        try {
            freeBytesInternal = new File(this.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        }
        catch (NullPointerException e){

        }
        megAvailable = freeBytesInternal/(1024*1024);
        Log.e("","Available MB : "+freeBytesInternal);
        ActivityManager.MemoryInfo memoryInfo= new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        availableMegs = memoryInfo.availMem/1048576L;

        percentageAvail = memoryInfo.availMem / memoryInfo.totalMem;

        ramUsageInt= (int) ((int)memoryInfo.totalMem/ 1048576L- (memoryInfo.availMem/ 1048576L));
        ramUsageInt= Math.abs(ramUsageInt);
        totalRam= ((int) (memoryInfo.totalMem/ 1048576L));
        ramUsagePercentage=(ramUsageInt/totalRam)*100;
        Log.d("memory usage", ramUsagePercentage + "");

        ramFreeInt=totalRam-ramUsageInt;
        Log.e("memory usage", ramFree + " " + totalRam);
        ramFreePercentage=(ramFreeInt/totalRam)*100;
        Log.e("memory usage", ramFreePercentage + "");

        try {
            ramUsage.setProgress((int) ramUsagePercentage);
            ramFree.setProgress((int) ramFreePercentage);
        }
        catch (NullPointerException e){

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ramFreeStatus < ramFreePercentage){
                    ramFreeStatus +=1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ramFree.setProgress(ramFreeStatus);
                            }
                            catch (NullPointerException e){

                            }
                        }
                    });

                    try {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        try {
            ActivityManager activityManager1 = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo pid : activityManager.getRunningAppProcesses()){

            }

        }
        catch (NullPointerException e){

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < ramUsagePercentage){
                    progressStatus +=1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ramUsage.setProgress(progressStatus);
                            }
                            catch (NullPointerException e){

                            }

                        }
                    });
                    try {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String getHealthString(int health){
        String healthString = getString(R.string.deadHealthString);
        switch (health){
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = getString(R.string.deadHealthString);
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = getString(R.string.goodHealthString);
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = getString(R.string.overVoltageHealthString);
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = getString(R.string.overHeatHealthString);
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = getString(R.string.faliureHealthString);
                break;
        }
        return healthString;
    }

    private String getPlugTypeString(int plugged) {
        String plugType = getString(R.string.defult_plug_type);

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = getString(R.string.AC_plug_type);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = getString(R.string.Usb_plug_type);
                break;
        }
        return plugType;
    }

    public static long getUsedMemorySize(){

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;

        try {
            Runtime info =Runtime.getRuntime();
            freeSize =  info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return usedSize;

    }


}