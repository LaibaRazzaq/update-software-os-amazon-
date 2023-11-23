package com.example.appUpdate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.BuildConfig;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;
import com.example.appUpdate.adapters.UninstallAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class uninstallSelectedApps extends AppCompatActivity {
 RecyclerView recyclerView;

 private final HashMap<Integer, String> packageNameMap=new HashMap<>();
    int counter=0;
    private static final String TAG = "--->Native Ad";

 Button unistallButton;
 BackTrackUtils backTrackUtils;
 Admenager admenager;

    ProgressBar progressBar;
    private SearchView editSearchView;
    private UninstallAdapter uninstallAdapter;
    private LinearLayoutManager layoutManager;
    ArrayList<Object> objects=new ArrayList<>();
    ArrayList <ItemClass> myList;
    RelativeLayout uninstallScreen;
    LottieAnimationView pleaseWait;
    ArrayList<NativeAd> nativeAdList;


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}
   @SuppressLint("MissingInflatedId")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uninstall_selected_apps);
        backTrackUtils =new BackTrackUtils(this,this);


       recyclerView=findViewById(R.id.uninstallRcv);
        unistallButton=findViewById(R.id.uninstallBtn);
        uninstallScreen=findViewById(R.id.unistllalScreen);
       pleaseWait=findViewById(R.id.pleaseWait);
       pleaseWait.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
       if (actionBar != null) {
           actionBar.setHomeButtonEnabled(true);
           actionBar.setDisplayHomeAsUpEnabled(true);
           actionBar.setTitle("Uninstall App");


       }
       admenager=new Admenager(this,this);
       admenager.load_InterstitialAd("admobe_interestitial_unistall_back","admobe_interestitial_unistall_back_test");

        progressBar = findViewById(R.id.progressBar);
        editSearchView = (SearchView) findViewById(R.id.search);
        editSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        progressBar.setVisibility(View.VISIBLE);
        uninstallScreen.setVisibility(View.GONE);
        myList=new ArrayList<>();
       uninstallAdapter=new UninstallAdapter();
       new InstallDb().execute();

       // uninstallAdapter.setObject(objects);
        editSearchView.setVisibility(View.VISIBLE);


        unistallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> packagesNames=new ArrayList<>();

                for (int i=0;i<uninstallAdapter.getItemCount();i++){
                   // ItemViewHolder1 itemViewHolder1=(ItemViewHolder1) recyclerView.findViewHolderForAdapterPosition(i);
                    RecyclerView.ViewHolder viewHolder=recyclerView.findViewHolderForAdapterPosition(i);
                    if (viewHolder instanceof ItemViewHolder1){
                        ItemViewHolder1 itemViewHolder1= (ItemViewHolder1) viewHolder;
                        if (itemViewHolder1.checkBox.isChecked()){
                            String packageName = (String) itemViewHolder1.checkBox.getTag();
                            packagesNames.add(packageName);
                        }

                    }


                }
                if (packagesNames.size()>0){
                    Intent intent=new Intent(Intent.ACTION_DELETE);

                    for (String packageName:packagesNames){


                        intent.setData(Uri.parse("package:"+packageName));

                      startActivity(intent);

                    }




                }


            }

        });

       editSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               // Log.d("filter","mListAdapter="+myAdapter);
               uninstallAdapter.getFilter().filter(newText);
               uninstallAdapter.notifyDataSetChanged();
               return false;
           }
       });

    nativeAdList=new ArrayList<>();

    }




    @Override
    public void onBackPressed()
    {
        backTrackUtils.backTrack();

      admenager.showadmobeInterstitialAd();
        super.onBackPressed();
    }

    class  InstallDb extends AsyncTask<Void, Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            myList=getAllList();
            uninstallAdapter.setList(myList);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
               progressBar.setVisibility(View.GONE);
               pleaseWait.setVisibility(View.GONE);
               uninstallScreen.setVisibility(View.VISIBLE);
            layoutManager=new LinearLayoutManager(getApplicationContext());

            recyclerView.setAdapter(uninstallAdapter);
            recyclerView.setLayoutManager(layoutManager);
            createNativeAd();
        }
    }
    private ArrayList<ItemClass> getAllList(){
        ArrayList<ItemClass> arrayList=new ArrayList<>();
        final PackageManager packageManager=getPackageManager();
      List<ApplicationInfo>infoList= packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo:infoList){
            if ((applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0){
                counter++;

                String appName=packageManager.getApplicationLabel(applicationInfo).toString();
                String packageName=applicationInfo.packageName;
                String versionName="";
                try {
                    versionName=packageManager.getPackageInfo(packageName,0).versionName;
                }catch (PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }

                CheckBox checkBox=new CheckBox(this);
                Drawable icon=packageManager.getApplicationIcon(applicationInfo);
                arrayList.add(new ItemClass(appName, packageName, versionName,0,icon,"",checkBox));
            }
        }
        return  arrayList;
    }
    private void createNativeAd(){

        objects=new ArrayList<>();
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
                    String Id;
                    if (BuildConfig.DEBUG){
                        Id=mFirebaseRemoteConfig.getString("admobe_native_uninstall_test");
                    }else {
                        Id=mFirebaseRemoteConfig.getString("admobe_native_uninstall");
                    }
                    AdLoader adLoader=new AdLoader.Builder(uninstallSelectedApps.this,Id).
                            forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded(@NonNull  NativeAd nativeAd) {
                                    if (isDestroyed()){
                                        nativeAd.destroy();
                                        Log.d(TAG, "Native Ad Destroyed");
                                        return;
                                    }
                                    nativeAdList.add(nativeAd);

                                    if (!adClass.getAdLoader().isLoading()){
                                        int j=0;
                                        for (int i=0;i<counter;i++){
                                            if (i==4||i==8||i==12||i==16||i==20){

                                                try {
                                                    objects.add(nativeAdList.get(j));
                                                    j++;

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                            try {
                                                objects.add(myList.get(i));

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }


                                    uninstallAdapter.setObject(objects);
                                    uninstallAdapter.notifyDataSetChanged();
                                }
                            }).withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(@NonNull  LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                    Log.d(TAG, "Native Ad Failed To Load");
                                }
                            }) .withNativeAdOptions(new NativeAdOptions.Builder()
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
                            String Id;
                            if (BuildConfig.DEBUG){
                                Id=mFirebaseRemoteConfig.getString("admobe_native_uninstall_test");
                            }else {
                                Id=mFirebaseRemoteConfig.getString("admobe_native_uninstall");
                            }
                            AdLoader adLoader=new AdLoader.Builder(uninstallSelectedApps.this,Id).
                                    forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                        @Override
                                        public void onNativeAdLoaded(@NonNull  NativeAd nativeAd) {
                                            if (isDestroyed()){
                                                nativeAd.destroy();
                                                Log.d(TAG, "Native Ad Destroyed");
                                                return;
                                            }
                                            nativeAdList.add(nativeAd);

                                            if (!adClass.getAdLoader().isLoading()){
                                                int j=0;
                                                for (int i=0;i<counter;i++){
                                                    if (i==4||i==11){

                                                        try {
                                                            objects.add(nativeAdList.get(j));
                                                            j++;

                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                    try {
                                                        objects.add(myList.get(i));

                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }


                                            uninstallAdapter.setObject(objects);
                                            uninstallAdapter.notifyDataSetChanged();
                                        }
                                    }).withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull  LoadAdError loadAdError) {
                                            super.onAdFailedToLoad(loadAdError);
                                            Log.d(TAG, "Native Ad Failed To Load");
                                        }
                                    }) .withNativeAdOptions(new NativeAdOptions.Builder()
                                            .build())
                                    .build();

                            adLoader.loadAds(new AdRequest.Builder().build(),2);
                            adClass.setAdLoader(adLoader);
                        }
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {

            }
        });

    }


}
