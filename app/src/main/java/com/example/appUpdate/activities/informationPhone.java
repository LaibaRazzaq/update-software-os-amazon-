package com.example.appUpdate.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appUpdate.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class informationPhone extends Fragment {
    TextView capacity, health, temperature, androidVersion, technology, buildId, charge, model, volta, deviceTimeZone,deviceLanguage,deviceCountry;

    TextView deviceTimeZoneName, deviceInternalStorage, deviceScreenSize, deviceUsedStorage, deviceAvailableStorage, deviceScreenResolution,deviceTotalRam;
    DonutProgress ramUsage, ramFree;
    private int ramFreeStatus = 0;
    private int progressStatus = 0;
    double ramUsagePercentage, ramFreePercentage = 50;
    double totalRam, ramUsageInt, ramFreeInt = 0;
    double mileVoltage, voltage = 0;

    Admenager admenager;


    Activity mActivity;
    long availableMegs,percentageAvail,megAvailable,freeBytesInternal=0;
    private Handler handler = new Handler();
    BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    technology.setText(bundle.getString("technology"));

                    health.setText(getHealthString(bundle.getInt("health")));
                    temperature.setText(bundle.getInt("temperature") / 10 + " \u2103");
                    mileVoltage = bundle.getInt("voltage");
                    voltage = mileVoltage/1000;
                    volta.setText(voltage + " V");
                    charge.setText(getPlugTypeString(bundle.getInt("plugged")));


                }
                else {
                    technology.setText("Null");
                    volta.setText("0.0 V");
                    charge.setText("Unknown");
                    health.setText("Good");
                }
                androidVersion.setText(Build.VERSION.RELEASE + "");
                buildId.setText(Build.ID + "");
                model.setText(Build.MODEL);
            }
            catch (Exception e){

                e.printStackTrace();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_information_phone, container, false);
        admenager=new Admenager(requireContext(),requireActivity());
        TemplateView templateView=view.findViewById(R.id.my_template);
        admenager.loadAd("admobe_phone_info_nativ","admobe_phone_info_nativ_test",templateView);
        deviceTimeZoneName=(TextView) view.findViewById(R.id.deviceTimeZoneName);
        technology = (TextView)view. findViewById(R.id.fragmentStaticTvTechnology);
        temperature = (TextView) view.findViewById(R.id.fragmentStaticTvTemperature);
        androidVersion = (TextView)view. findViewById(R.id.fragmentStaticAndroidVersion);
        health = (TextView) view.findViewById(R.id.fragmentStaticTvHealth);
        buildId = (TextView) view.findViewById(R.id.fragmentStaticTvBuildId);
        charge = (TextView) view.findViewById(R.id.fragmentStaticTvCharger);
        volta = (TextView) view.findViewById(R.id.fragmentStaticTvVoltage);
        model = (TextView)view. findViewById(R.id.fragmentStaticTvModel);
        ramFree = (DonutProgress)view. findViewById(R.id.ram_free);
        ramUsage = (DonutProgress) view.findViewById(R.id.ram_usage_circle);
        deviceLanguage=(TextView) view.findViewById(R.id.deviceLanguage);
        deviceCountry=(TextView) view.findViewById(R.id.deviceCountry);
        deviceTimeZone=(TextView)view. findViewById(R.id.deviceTimeZone);
        deviceAvailableStorage=view.findViewById(R.id.deviceAvailableStorage);
        deviceInternalStorage=view.findViewById(R.id.deviceInternalStorage);
        deviceUsedStorage=view.findViewById(R.id.deviceUsedStorage);
        deviceScreenSize=view.findViewById(R.id.deviceScreenSize);
        deviceScreenResolution=view.findViewById(R.id.deviceScreenResolution);
        deviceTotalRam=view.findViewById(R.id.deviceTotalRam);

        Locale locale=Locale.getDefault();
        String language=locale.getDisplayLanguage();
        String country=locale.getDisplayCountry();
        deviceCountry.setText(country);
        deviceLanguage.setText(language);

        Calendar calendar=Calendar.getInstance();
        TimeZone tz=calendar.getTimeZone();
        String timeZone=tz.getID();
        String timeZoneName=tz.getDisplayName(false, TimeZone.SHORT);
        // Toast.makeText(this, ""+timeZoneName, Toast.LENGTH_SHORT).show();
        deviceTimeZoneName.setText(timeZoneName);
        deviceTimeZone.setText(timeZone);

        StatFs internalFs=new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long internalTotal;
        long internalFree;


        StatFs externalFs=new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long externalTotal;
        long externalFree;

        internalTotal=(internalFs.getBlockCountLong()*internalFs.getBlockSizeLong());
        internalFree=(internalFs.getAvailableBlocksLong()*internalFs.getBlockSizeLong());


        long used = internalTotal - internalFree;

        String totalStorage= Formatter.formatFileSize(mActivity,internalTotal);
        deviceInternalStorage.setText(totalStorage);
        String totalFree=Formatter.formatFileSize(mActivity,internalFree);
        deviceAvailableStorage.setText(totalFree);
        String totalUsed=Formatter.formatFileSize(mActivity,used);
        deviceUsedStorage.setText(totalUsed);
        Log.d("rage","total storage"+totalStorage+"total free"+totalFree+"totalUsed"+totalUsed);

        DisplayMetrics metrics=new DisplayMetrics();
       mActivity. getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        int width=metrics.widthPixels;
        int height=metrics.heightPixels;
        String totalReso=width+" "+"x"+" "+height;
        double wi=(double)width/(double)metrics.xdpi;
        double hi=(double)height/(double)metrics.ydpi;
        double x=Math.pow(wi,2);
        double y=Math.pow(hi,2);
        double screenInches=Math.sqrt(x+y);
        Log.d("rage", String.valueOf(screenInches));
        String totalScreenSize=String.valueOf((Math.round(screenInches*10.0*10.0))/100.0);
        deviceScreenSize.setText(totalScreenSize+" "+"inches");
        deviceScreenResolution.setText(totalReso);
        try {
            freeBytesInternal = new File(mActivity.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        }
        catch (NullPointerException e){

        }
        megAvailable = freeBytesInternal/(1024*1024);
        Log.e("","Available MB : "+freeBytesInternal);
        ActivityManager.MemoryInfo memoryInfo= new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        long totalRam=memoryInfo.totalMem;
        String totalRamString=Formatter.formatFileSize(mActivity,totalRam);
        deviceTotalRam.setText(totalRamString);
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


        return view;
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
    @Override
    public void onPause() {
        super.onPause();
       mActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
     mActivity.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity= (Activity) context;
    }
}