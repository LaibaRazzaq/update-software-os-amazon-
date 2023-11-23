package com.example.appUpdate.CustomFeedBack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class BackTrackUtils {
    Context context;
    Activity activity;
    boolean isAdLoaded;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isAlreadyReview;
    int session;

    public BackTrackUtils(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sharedPreferences=context.getSharedPreferences("appReview",Context.MODE_PRIVATE);
     session=sharedPreferences.getInt("appSession",1);

    }

    public  void trackSession(){

        editor=sharedPreferences.edit();
        editor.putInt("appSession", session+1);
        editor.putInt("backtrack",0);
        editor.putBoolean("isAdLoaded", false);
        editor.apply();


    }
    public  void backTrack(){
        int backTrackCount=sharedPreferences.getInt("backtrack",0);
        isAlreadyReview=sharedPreferences.getBoolean("isAlreadyAppReview", true);
        isAdLoaded =sharedPreferences.getBoolean("isAdLoaded", false);
        if (isAlreadyReview){


                editor = sharedPreferences.edit();
                editor.putInt("backtrack", backTrackCount + 1);
                editor.apply();


        }
        activity.finish();
    }


}
