package com.example.appUpdate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appUpdate.R;

public class MultiTouchScreen extends AppCompatActivity {
    TouchLinesView touchLinesView;
    TextView textView;
    Button yes,no;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_touch_screen);
        touchLinesView=findViewById(R.id.touch_view);
        textView=findViewById(R.id.totalTouch);
        touchLinesView.setTextView(textView);
        sharedPreferences=getSharedPreferences("testMobile",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        yes=findViewById(R.id.yes);
        no=findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("multiTouch", "yes");
                editor.apply();
                setResult(Activity.RESULT_OK,new Intent().putExtra("answer",true));
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("multiTouch", "no");
                editor.apply();
                setResult(Activity.RESULT_OK,new Intent().putExtra("answer",false));
                finish();
            }
        });
        touchLinesView.setBackgroundColor(Color.BLACK);
    }
}