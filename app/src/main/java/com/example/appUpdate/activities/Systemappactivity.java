package com.example.appUpdate.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.BuildConfig;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Systemappactivity extends AppCompatActivity {
    private SearchView editSearchView;
    ProgressBar progressBar;
    private static final String TAG = "--->Native Ad";
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private GridLayoutManager layoutManager;
    private ArrayList<ItemClass> myList;
    private List<NativeAd> nativeAdList;
    int counter = 0;
    int spanCount=2;
    int interstitialBackCount;
    private ArrayList<Object> objects;
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}

    private static ArrayList<ItemClass> getInstalledApps(Systemappactivity systemappactivity) {


        ArrayList<ItemClass> res = new ArrayList<ItemClass>();

        List<PackageInfo> packs = systemappactivity.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

//                if ((!getSysPackages) && (p.versionName == null)) {
//                    continue;
//                }
                systemappactivity.counter++;
                Log.d("j_value", "outer" + i);

                ItemClass newInfo = new ItemClass();
                newInfo.appname = p.applicationInfo.loadLabel(systemappactivity.getPackageManager()).toString();
                newInfo.packageName = p.packageName;
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String installTime = dateFormat.format(new Date(p.firstInstallTime));

                newInfo.lastDate = installTime;
                //newInfo.permission= p.permissions;

                newInfo.icon = p.applicationInfo.loadIcon(systemappactivity.getPackageManager());

                res.add(newInfo);
            }
        }
        return res;

    }
    Intent intent;

    LottieAnimationView pleasewait;
    BackTrackUtils backTrackUtils;

    Admenager admenager;
    InstalledAppsDB appsDB;
int splashCheck;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        admenager=new Admenager(this, this);
        admenager.load_InterstitialAd("admobe_interestitial_system_app_back","admobe_interestitial_system_app_back_test");
        recyclerView = findViewById(R.id.id_recyclerView);
        recyclerView.setHasFixedSize(true);
        ActionBar actionBar = getSupportActionBar();
        pleasewait=findViewById(R.id.pleaseWait);
        backTrackUtils=new BackTrackUtils(this ,this);

        pleasewait.setVisibility(View.VISIBLE);

        //  isAlreadyReview=sp.getBoolean("isAlreadyAppReview",true);


        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("System App");

        }


        final Context context = getApplicationContext();

        intent  =getIntent();

//        sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
//
//        splashCheck = sp.getInt("splash_check", 1);
//        if(splashCheck==0){

       // }


        myList = new ArrayList<>();


        layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);

        myAdapter = new MyAdapter();
        progressBar = findViewById(R.id.progressBar);
        editSearchView = (SearchView) findViewById(R.id.search);
appsDB=new InstalledAppsDB(this);
        appsDB.execute();


       // new InstalledAppsDB(this).execute();

        editSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // Log.d("filter","mListAdapter="+myAdapter);
                myAdapter.getFilter().filter(newText);
                myAdapter.notifyDataSetChanged();
                return false;
            }
        });

        nativeAdList = new ArrayList<>();


    }
    private static void createNativeAd(Systemappactivity installappsActivity) {


        installappsActivity.objects = new ArrayList<>();

        AdClass adClass = new AdClass();

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
                        adID=mFirebaseRemoteConfig.getString("admobe_native_system_test");
                    }else {
                        adID=mFirebaseRemoteConfig.getString("admobe_native_system");
                    }
                    AdLoader adLoader = new AdLoader.Builder(installappsActivity, adID)

                            .forNativeAd(nativeAd -> {
                                Log.d(TAG, "Native Ad Loaded");

                                if (installappsActivity.isDestroyed()) {
                                    nativeAd.destroy();
                                    Log.d(TAG, "Native Ad Destroyed");
                                    return;
                                }


                                installappsActivity.nativeAdList.add(nativeAd);



                                if (!adClass.getAdLoader().isLoading()) {
                                    installappsActivity.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return (position == 6 || position == 13 || position == 20 || position == 27 || position == 34) ? installappsActivity.spanCount : 1;
                                        }
                                    });

                                    int j = 0;
                                    for (int i = 0; i < installappsActivity.counter; i++) {
                                        if (i == 6 || i == 13 || i == 20 || i == 27 || i == 34) {
                                            // This is a position where you want to insert a native ad
                                            try {
                                                installappsActivity.objects.add(installappsActivity.nativeAdList.get(j));
                                                j++;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            Log.d("j_value", "j value: " + j);
                                        } else {
                                            // This is a regular position, add your regular item
                                            try {
                                                installappsActivity.objects.add(installappsActivity.myList.get(i));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
//

                                installappsActivity.myAdapter.setObject(installappsActivity.objects);
                                installappsActivity.myAdapter.notifyDataSetChanged();
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

                    adLoader.loadAds(new AdRequest.Builder().build(),5);
                    adClass.setAdLoader(adLoader);
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
                                adID=mFirebaseRemoteConfig.getString("admobe_native_download_test");
                            }else {
                                adID=mFirebaseRemoteConfig.getString("admobe_native_download");
                            }
                            AdLoader adLoader = new AdLoader.Builder(installappsActivity, adID)

                                    .forNativeAd(nativeAd -> {
                                        Log.d(TAG, "Native Ad Loaded");

                                        if (installappsActivity.isDestroyed()) {
                                            nativeAd.destroy();
                                            Log.d(TAG, "Native Ad Destroyed");
                                            return;
                                        }


                                        installappsActivity.nativeAdList.add(nativeAd);



                                        if (!adClass.getAdLoader().isLoading()) {
                                            installappsActivity.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                @Override
                                                public int getSpanSize(int position) {
                                                    return (position==6||position==15?installappsActivity.spanCount:1);
                                                }
                                            });
                                            List<PackageInfo> packs = installappsActivity.getPackageManager().getInstalledPackages(0);
                                            int j=0;
                                            for (int i = 0; i < installappsActivity.counter; i++) {


                                                if ((i == 6) || (i == 14)  ) {

                                                    // objects.add(myList.get(i));
                                                    try {
                                                        installappsActivity.objects.add(installappsActivity.nativeAdList.get(j));
                                                        j++;
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                    Log.d("j_value", "j vale" + j);
                                                }                                               //  Log.d("j_value", "i vale"+i);
                                                try {
                                                    installappsActivity.objects.add(installappsActivity.myList.get(i));
                                                }catch (Exception e){
                                                    e.printStackTrace(
                                                    );


                                                }
                                            }

                                        }
//

                                        installappsActivity.myAdapter.setObject(installappsActivity.objects);
                                        installappsActivity.myAdapter.notifyDataSetChanged();
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

                            adLoader.loadAds(new AdRequest.Builder().build(),2);
                            adClass.setAdLoader(adLoader);
                        }
                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {

            }
        });



    }


  public static class InstalledAppsDB extends AsyncTask<Void, Void, Void> {

      private final WeakReference<Systemappactivity> activityReference;
      InstalledAppsDB(Systemappactivity context) {
          activityReference = new WeakReference<>(context);
      }

      @Override
        protected void onPreExecute() {
          Systemappactivity systemappactivity=activityReference.get();
            systemappactivity.progressBar.setIndeterminate(true);
            systemappactivity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Systemappactivity systemappactivity=activityReference.get();

           systemappactivity. myList = getInstalledApps(systemappactivity); /* false = no system packages */
            systemappactivity.myAdapter.setList(systemappactivity.myList);



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Systemappactivity systemappactivity=activityReference.get();

           systemappactivity.pleasewait.setVisibility(View.GONE);
            systemappactivity.recyclerView.setLayoutManager(systemappactivity.layoutManager);
            systemappactivity.recyclerView.setAdapter(systemappactivity.myAdapter);
            systemappactivity.progressBar.setVisibility(View.GONE);
            systemappactivity.editSearchView.setVisibility(View.VISIBLE);
            createNativeAd(systemappactivity);

        }
    }
    @Override
    public void onBackPressed() {
        if (appsDB!=null && appsDB.getStatus()== AsyncTask.Status.RUNNING){
            appsDB.cancel(true);
        }


      backTrackUtils.backTrack();

           admenager.showadmobeInterstitialAd();
        super.onBackPressed();




    }


}