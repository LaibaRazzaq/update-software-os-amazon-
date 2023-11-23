package com.example.appUpdate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.appUpdate.R;

public class ColorsActivity extends AppCompatActivity {
 private  int[]colors={Color.BLACK,Color.WHITE,Color.RED,Color.GREEN,Color.BLUE};
 private int currentIndex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        getWindow().setStatusBarColor(colors[currentIndex]);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        getWindow().getDecorView().setBackgroundColor(colors[currentIndex]);

        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    currentIndex++;
                    if (currentIndex>=colors.length){
                       setResult(Activity.RESULT_OK);
                       finish();
                    }else {
                        getWindow().getDecorView().setBackgroundColor(colors[currentIndex]);
                        getWindow().setStatusBarColor(colors[currentIndex]);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}