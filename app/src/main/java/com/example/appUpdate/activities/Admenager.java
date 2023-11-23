package com.example.appUpdate.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.example.appUpdate.BuildConfig;
import com.example.appUpdate.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class Admenager {
    private final Context mContext;
    private final Activity sActivity;
    SharedPreferences sharedPreferences;
    private InterstitialBuilder interstitialBuilder = null;
    boolean isAdLoaded,isAdLoaded1;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;

    public Admenager(Context context, Activity activity) {

        mContext = context;
        sActivity = activity;
        sharedPreferences = context.getSharedPreferences("appReview", Context.MODE_PRIVATE);
        isAdLoaded = sharedPreferences.getBoolean("isAdLoaded", false);
        sp=context.getSharedPreferences("your_prefs",Context.MODE_PRIVATE);

        mFirebaseRemoteConfig= FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG?0:3600).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
    }

    private static final String TAG = "--->NativeAd";
    private TemplateView template;
    InterstitialAd mInterstitialAd;
    AdRequest adRequest1;

    // private InterstitialBuilder interstitialBuilder=null;




    public void showadmobeInterstitialAd() {

        Log.d("add",""+isAdLoaded);

        Log.d("add",""+isAdLoaded);
        SharedPreferences sharedPreferences1= mContext.getSharedPreferences("appReview", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor1 =sharedPreferences1.edit();
       isAdLoaded1=sharedPreferences1.getBoolean("isAdLoaded", false);

        if (isAdLoaded1){
           if (mInterstitialAd != null) {
               mInterstitialAd.show(sActivity);

               mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                   @Override
                   public void onAdDismissedFullScreenContent() {
                       finishad();
                       sp.edit().putInt("resume_check",1).apply();

                       Log.d("TAG", "The ad was dismissed.");

                   }

                   @Override
                   public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                       // Called when fullscreen content failed to show.
                       Log.d("TAG", "The ad failed to show.");

                       finishad();

                   }

                   @Override
                   public void onAdShowedFullScreenContent() {

                       editor1.putBoolean("isAdLoaded", false);
                       editor1.apply();
                       editor = sp.edit();
                       editor.putInt("resume_check", 0);
                       editor.apply();

                       mInterstitialAd = null;

                   }
               });
           } else {

               // facbookads=new facbookads(mContext,sActivity);
               if (interstitialBuilder!=null) {
                   editor = sp.edit();
                   editor.putInt("resume_check", 0);
                   editor.apply();
                   editor1.putBoolean("isAdLoaded", false);
                   editor1.apply();
                   interstitialBuilder.show(sActivity);
                   editor.putBoolean("fromBrain", true).apply();



               }else {
                   editor = sp.edit();
                   editor.putInt("resume_check", 1);
                   editor.apply();
               }

               finishad();

               // finishad();
           }
       }else {
            editor = sp.edit();
            editor.putInt("resume_check", 1);
            editor.apply();
            finishad();
        }

    }

    public void showadmobeInterstitialAd(onAdShowListener showListener) {


        Log.d("add",""+isAdLoaded);

        Log.d("add",""+isAdLoaded);
        SharedPreferences sharedPreferences1= mContext.getSharedPreferences("appReview", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 =sharedPreferences1.edit();
        isAdLoaded1=sharedPreferences1.getBoolean("isAdLoaded", false);

        if (isAdLoaded1){
            if (mInterstitialAd != null) {
                mInterstitialAd.show(sActivity);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {

                        sp.edit().putInt("resume_check",1).apply();

                        Log.d("TAG", "The ad was dismissed.");
                        showListener.onShowAd();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");

                        showListener.onShowAd();

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {

                        editor1.putBoolean("isAdLoaded", false);
                        editor1.apply();
                        editor = sp.edit();
                        editor.putInt("resume_check", 0);
                        editor.apply();

                        mInterstitialAd = null;

                    }
                });
            } else {

                // facbookads=new facbookads(mContext,sActivity);
                if (interstitialBuilder!=null) {
                    editor = sp.edit();
                    editor.putInt("resume_check", 0);
                    editor.apply();
                    editor1.putBoolean("isAdLoaded", false);
                    editor1.apply();
                    interstitialBuilder.show(sActivity);
                    editor.putBoolean("fromBrain", true).apply();



                }else {
                    editor = sp.edit();
                    editor.putInt("resume_check", 1);
                    editor.apply();
                }

                showListener.onShowAd();

                // finishad();
            }
        }else {
            editor = sp.edit();
            editor.putInt("resume_check", 1);
            editor.apply();
            showListener.onShowAd();
        }

    }

    public void load_InterstitialAd(String adId,String adTestID) {



        if(isAdLoaded){
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful()){
                        String adID;
                        if (BuildConfig.DEBUG){
                            adID=mFirebaseRemoteConfig.getString(adTestID);
                        }else {
                            adID=mFirebaseRemoteConfig.getString(adId);
                        }
                        adLoadCount(adID);

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
                                    adID=mFirebaseRemoteConfig.getString(adTestID);
                                }else {
                                    adID=mFirebaseRemoteConfig.getString(adId);
                                }
                                adLoadCount(adID);
                            }

                        }
                    });
                }

                @Override
                public void onError(@NonNull FirebaseRemoteConfigException error) {

                }
            });
        }else {
            editor=sharedPreferences.edit();
            editor.putBoolean("isAdLoaded", true);
            editor.apply();
        }




    }

    private void adLoadCount(String adId) {

        interstitialBuilder = InterstitialBuilder.create()
                .setAdId(AdId.LEVEL_COMPLETE)
                .setOnDoneCallback(new Runnable() {
                    @Override
                    public void run() {
                        // Preload again, so we can use interstitialBuilder again.
                        interstitialBuilder.preload(mContext);

                    }
                })
                .preload(mContext);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(sActivity,adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                 mInterstitialAd = interstitialAd;

                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;

            }
        });

    }



    void loadAd(String adId,String testId,TemplateView templateView) {
      //  template = sActivity.findViewById(R.id.my_template);

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    String adID;
                    if (BuildConfig.DEBUG){
                        adID=mFirebaseRemoteConfig.getString(testId);
                    }else {
                        adID=mFirebaseRemoteConfig.getString(adId);
                    }
                    AdLoader adLoader = new AdLoader.Builder(mContext, adID).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                                    Log.d(TAG, "Native Ad Loaded");

                                    if (sActivity.isDestroyed()) {
                                        nativeAd.destroy();
                                        Log.d(TAG, "Native Ad Destroyed");
                                        return;
                                    }


                                    if (nativeAd.getMediaContent() != null) {
                                        nativeAd.getMediaContent().getVideoController();
                                        nativeAd.getMediaContent().getVideoController().setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                                            @Override
                                            public void onVideoStart() {
                                                super.onVideoStart();
                                                Log.d(TAG, "Video Started");
                                            }

                                            @Override
                                            public void onVideoPlay() {
                                                super.onVideoPlay();
                                                Log.d(TAG, "Video Played");
                                            }

                                            @Override
                                            public void onVideoPause() {
                                                super.onVideoPause();
                                                Log.d(TAG, "Video Paused");
                                            }

                                            @Override
                                            public void onVideoEnd() {
                                                super.onVideoEnd();
                                                Log.d(TAG, "Video Finished");
                                            }

                                            @Override
                                            public void onVideoMute(boolean b) {
                                                super.onVideoMute(b);
                                                Log.d(TAG, "Video Mute : " + b);
                                            }
                                        });
                                    }
                                    NativeTemplateStyle styles = new
                                            NativeTemplateStyle.Builder().build();


                                    templateView.setStyles(styles);
                                    templateView.setVisibility(View.VISIBLE);
                                    templateView.setNativeAd(nativeAd);

                                }
                            })

                            .withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                    // Handle the failure by logging, altering the UI, and so on.
                                    Log.d(TAG, "Native Ad Failed To Load");
                                    templateView.setVisibility(View.GONE);


                                }
                            })
                            // .withNativeAdOptions(new NativeAdOptions.Builder().build())
                            .withNativeAdOptions(adOptions)
                            .build();

                    adLoader.loadAd(new AdRequest.Builder().build());

                }

            }
        });






    }

    void finishad() {

            sActivity.finish();
        interstitialBuilder = InterstitialBuilder.create().setAdId(AdId.EXIT)
                .setFinishOnExit(sActivity).preload(sActivity);

    }


    public void LoadMediumBannerAD(FrameLayout view, String testID, String adiD){
        AdView mAdView=new AdView(mContext);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    String adID;
                    if (BuildConfig.DEBUG){

                        adID=mFirebaseRemoteConfig.getString(testID);
                    }else {
                        adID=mFirebaseRemoteConfig.getString(adiD);
                    }


                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.setAdUnitId(adID);
                    mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    mAdView.loadAd(adRequest);




                    mAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            view.setVisibility(View.VISIBLE);
                            view.removeAllViews();
                            view.addView(mAdView);

                        }
                    });


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


                                adID=mFirebaseRemoteConfig.getString(testID);
                            }else {
                                adID=mFirebaseRemoteConfig.getString(adiD);
                            }
                            mAdView.destroy();
                            view.removeAllViews();
                            view.setVisibility(View.GONE);
                            AdView mAdView1=new AdView(mContext);

                            AdRequest adRequest = new AdRequest.Builder().build();

                            mAdView1.setAdUnitId(adID);
                            mAdView1.setAdSize(AdSize.MEDIUM_RECTANGLE);
                            mAdView1.loadAd(adRequest);



                            mAdView1.setAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    view.setVisibility(View.VISIBLE);
                                    view.removeAllViews();
                                    view.addView(mAdView1);

                                }
                            });


                        }


                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {

            }
        });
    }
public interface onAdShowListener{
        public void onShowAd();
}
}

