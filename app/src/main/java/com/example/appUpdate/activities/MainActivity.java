package com.example.appUpdate.activities;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appUpdate.BuildConfig;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.CustomFeedBack.FeedBack;
import com.example.appUpdate.R;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
//import com.facebook.ads.AudienceNetworkAds;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;


import java.io.File;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity {
    LinearLayout installedAppsLayout, systemAppsLayout, rateLayout, systemUpdateLayout, appUsage,mobileSensor,duplicatePhoto;
    LinearLayout phoneInfo,cleanJunk,unInstallApp,adBtn,moreApps, shareApp , testMobile;
    LinearLayout otherappLayout;
    WaveLoadingView ramUsageWaveLoading;
    double totalRAMusagePercentage;
    FeedBack feedBack;
    double totalRAM, ramUsageVariable;
    int intValue = 0;
    BottomSheetDialog bottomSheetDialog;


    ProgressBar progressBar;


    long availableBytes = 0;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    int session;
     SharedPreferences preferences;
    Intent intent;
    String TAG="facebook ads";
    boolean isAlreadyReview;
    int backPressCount;
FrameLayout mAdView;
    boolean isShown ,isAdshow;
    BackTrackUtils backTrackUtils;

Admenager admenager;


    int splashCheck;


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        int ses=preferences.getInt("appSession",1);
        int backC=preferences.getInt("backtrack", 0);
        isAdshow=preferences.getBoolean("isAdLoaded", false);
        isShown=preferences.getBoolean("p1Shown"+ses+backC,false);

        isAlreadyReview=preferences.getBoolean("isAlreadyAppReview", true);
        if (isAlreadyReview ){
            if (!isShown) {
                if (isAdshow) {
                    customFeedBack(ses, backC);
                }
            }
        }


        super.onResume();
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        feedBack=new FeedBack(this,this);

        preferences = getSharedPreferences("appReview", MODE_PRIVATE);
        session=preferences.getInt("appSession", 1);
        backTrackUtils=new BackTrackUtils(this,this);
        backTrackUtils.trackSession();
        admenager=new Admenager(this,this);
        mAdView = findViewById(R.id.adView);
        admenager.LoadMediumBannerAD(mAdView,"admobe_banner_main_test","admobe_banner_main");
        mobileSensor=findViewById(R.id.sensor);
        adBtn=findViewById(R.id.ad);


        shareApp=findViewById(R.id.share);
        testMobile=findViewById(R.id.testMobile);






        moreApps=findViewById(R.id.moreApps);
        moreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ng.collagemaker.photoeditor.photocollage")));

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        unInstallApp=findViewById(R.id.unInstallApp);
        unInstallApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(MainActivity.this,uninstallSelectedApps.class));


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        phoneInfo=findViewById(R.id.phoneInfo);
        cleanJunk=findViewById(R.id.junkLayout);
        cleanJunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(MainActivity.this,  JunkCleaner.class));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        phoneInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(MainActivity.this,phoneInfoMaiin.class));

                }catch (Exception e){
                    e.printStackTrace();

                }


            }
        });

        duplicatePhoto=findViewById(R.id.duplicatePhotoslayout);
        duplicatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(MainActivity.this,  DuplicatePhotos.class));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        mobileSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(MainActivity.this,  SensorActivity.class));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        progressBar = findViewById(R.id.progressBarMain);
        ramUsageWaveLoading = (WaveLoadingView) findViewById(R.id.waveLoadingViewRAM);
        appUsage=findViewById(R.id.appUsage);
        appUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP_MR1){
                    Intent intent=new Intent(MainActivity.this, PermissionScreen.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "This feature not support your android version", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        AudienceNetworkAds.initialize(this);

        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("resume_check", 1);


        editor.apply();
        final Context context = getApplicationContext();
       // Toast.makeText(this,"* Necessário Activity  start",Toast.LENGTH_LONG).show();

        intent  =getIntent();
        splashCheck = sp.getInt("splash_check", 1);




    loadNativeAd();

        try {
            availableBytes = new File(this.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        } catch (NullPointerException e) {

        }
//        megAvailable = availableBytes/(1024*1024);
        Log.e("", "Available MB : " + availableBytes);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        // availableMegs = memoryInfo.availMem/1048576L;
        //percentageAvail = memoryInfo.availMem / memoryInfo.totalMem;

        ramUsageVariable = (int) ((int) memoryInfo.totalMem / 1048576L - (memoryInfo.availMem / 1048576L));
        ramUsageVariable = Math.abs(ramUsageVariable);
        totalRAM = ((int) (memoryInfo.totalMem / 1048576L));
        totalRAMusagePercentage = (ramUsageVariable / totalRAM) * 100;

        try {
            if ((int) totalRAMusagePercentage > 90) {
                ramUsageWaveLoading.setWaveColor(Color.RED);
            }
            ramUsageWaveLoading.setProgressValue((int) totalRAMusagePercentage);
            ramUsageWaveLoading.setCenterTitle((int) totalRAMusagePercentage + "%");
            ramUsageWaveLoading.setBottomTitle("Ram Usage");

        } catch (Exception e) {

        }
        installedAppsLayout = findViewById(R.id.installedAppBtn);
        systemAppsLayout = findViewById(R.id.systemAppBtn);
        rateLayout = findViewById(R.id.rateUsBtn);

       // shareLayout=findViewById(R.id.privacyPolicyBtn);
//        otherappLayout=findViewById(R.id.otherAppLayout);

//        ad1Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nzdeveloper.playstoressettingshortcut.playservices")));
//
//            }
//        });
        adBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.zadeveloper.playstore.playservices.info")));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        installedAppsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor=preferences.edit();
                editor.putBoolean("systemLayout", false);
                editor.apply();
                    Intent intent = new Intent(MainActivity.this, InstallappsActivity.class);
                    try {
                       // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                    //  admenager.showadmobeInterstitialAd(2);


            }
        });
//        shareLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(MainActivity.this, AboutUsActivity.class);
//                startActivity(myIntent);
//
//
//            }
//        });
//        otherappLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fr.tiktokvideodownloader.tikmate.savetiktokvideo.nowatermark")));

//            }
//        });

        systemAppsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor=preferences.edit();
                editor.putBoolean("systemLayout", true);
                editor.apply();
                Intent intent = new Intent(MainActivity.this, Systemappactivity.class);
                try {
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (Exception e) {

                }

            }
        });

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Ampere meter");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.nzdeveloper.updatlatestesoftwareandroid");

                //i.putExtra(Intent.EXTRA_TEXT   , "body ");
                try {
                    startActivity(Intent.createChooser(i, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
         testMobile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this, TestMobile.class));
             }
         });
        rateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nzdeveloper.updatlatestesoftwareandroid")));

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        systemUpdateLayout = findViewById(R.id.systemUpdateBtn);

        systemUpdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    startActivityForResult(new Intent("android.settings.SYSTEM_UPDATE_SETTINGS"), 0);
                } catch (ActivityNotFoundException e) {
                    startActivityForResult(new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS), 0);

                }


            }
        });

    }

    private void customFeedBack(int ses, int back) {



        if (session != 3) {
            if (session % 2 == 1) {
                backPressCount = preferences.getInt("backtrack", 0);
                Log.d("sesion",""+backPressCount);


                Log.d("sesion", "fromm" + backPressCount);
                feedBack.appReview(ses, back);
                editor.apply();




            } else if (session % 2 == 0) {
                backPressCount = preferences.getInt("backtrack", 0);
                Log.d("sesion",""+backPressCount);

                feedBack.appReview(ses, back);



            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {


            case R.id.PrivacyPolicy:
                // startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6016563769984552072")));

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;

            case R.id.action_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Ampere meter");
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.nzdeveloper.updatlatestesoftwareandroid");

                //i.putExtra(Intent.EXTRA_TEXT   , "body ");
                try {
                    startActivity(Intent.createChooser(i, "Share"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
  //  Admenager admenager;
    NativeAd mNativeAd;
    @Override
    public void onBackPressed() {


            showBottomSheet();


    }
    private void loadNativeAd(){
        FirebaseRemoteConfig mFirebaseRemoteConfig;
        FirebaseRemoteConfigSettings configSettings;

        mFirebaseRemoteConfig= FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG?0:3600).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    String adID;
                    if (BuildConfig.DEBUG){
                        adID=mFirebaseRemoteConfig.getString("admobe_back_nativ_test");
                    }else {
                        adID=mFirebaseRemoteConfig.getString("admobe_back_nativ");
                    }
                    AdLoader adLoader = new AdLoader.Builder(MainActivity.this, adID)

                            .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                                    Log.d(TAG, "Native Ad Loaded");


                                    if (isDestroyed()) {
                                        nativeAd.destroy();
                                        Log.d(TAG, "Native Ad Destroyed");
                                        return;
                                    }

                                    mNativeAd=nativeAd;

                                }
                            })

                            .withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError adError) {
                                    // Handle the failure by logging, altering the UI, and so on.
                                    Log.d(TAG, "Native Ad Failed To Load");

                                }
                            })
                            .withNativeAdOptions(new NativeAdOptions.Builder()
                                    .build())
                            .build();

                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            }
        });

        mFirebaseRemoteConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(@NonNull ConfigUpdate configUpdate) {
                mFirebaseRemoteConfig.activate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()){
                            String adID;
                            if (BuildConfig.DEBUG){
                                adID=mFirebaseRemoteConfig.getString("admobe_native_main_test");
                            }else {
                                adID=mFirebaseRemoteConfig.getString("admobe_native_main");
                            }
                            AdLoader adLoader = new AdLoader.Builder(MainActivity.this, adID)

                                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                        @Override
                                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                                            Log.d(TAG, "Native Ad Loaded");


                                            if (isDestroyed()) {
                                                nativeAd.destroy();
                                                Log.d(TAG, "Native Ad Destroyed");
                                                return;
                                            }

                                            mNativeAd=nativeAd;

                                        }
                                    })

                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            // Handle the failure by logging, altering the UI, and so on.
                                            Log.d(TAG, "Native Ad Failed To Load");

                                        }
                                    })
                                    .withNativeAdOptions(new NativeAdOptions.Builder()
                                            .build())
                                    .build();

                            adLoader.loadAd(new AdRequest.Builder().build());
                        }
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {

            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){

            backTrackUtils.backTrack();
              // customFeedBack();


        }
        if (feedBack != null) {
            feedBack.onActivityResult(requestCode, resultCode, data);
        } else {
            // Handle the case when feedBack is null
            // You can log an error, display an error message, or take any other appropriate action
        }
    }

    @SuppressLint("MissingInflatedId")
    private void showBottomSheet() {
        // Create and customize your bottom sheet view
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        // Customize the content of the bottom sheet view

        // Create the bottom sheet dialog
        bottomSheetDialog = new BottomSheetDialog(this);

        TemplateView template = bottomSheetView.findViewById(R.id.templateView);

        AppCompatButton exit=bottomSheetView.findViewById(R.id.exist);
        AppCompatButton cancel=bottomSheetView.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNativeAd!=null){
                    mNativeAd.destroy();
                }
                loadNativeAd();
                bottomSheetDialog.cancel();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
//                Intent intentExit = new Intent(Intent.ACTION_MAIN);
//                intentExit.addCategory(Intent.CATEGORY_HOME);
//                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentExit);
                finish();
                // System.exit(0);
            }
        });



        template.setVisibility(View.GONE);



        if(mNativeAd!=null) {
            NativeTemplateStyle styles = new
                    NativeTemplateStyle.Builder().build();


            template.setStyles(styles);
            template.setVisibility(View.VISIBLE);
            template.setNativeAd(mNativeAd);
        }
        bottomSheetDialog.setContentView(bottomSheetView);

        // Show the bottom sheet dialog
        bottomSheetDialog.show();
    }


}




