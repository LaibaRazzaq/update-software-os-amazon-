package com.example.appUpdate.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;

import java.util.concurrent.Executor;

public class TestMobile extends AppCompatActivity {


    RelativeLayout earPhone, microPhone,flashLight,fingerPrint,multiTouch,vibration,loudSpeaker,display,earProximity,volumeUp,volumeDown,acclerometer,lightSensor;
    ImageView ivLoudSpeaker,ivEarSpeaker,ivMicrophone,ivDisplay,ivFlashLight,ivFingerPrint,ivMultiTouch,ivVibration,ivEarproximity,ivVolumeUp,ivVolumeDown,ivAcelerometer,ivLightSensor;
    BiometricPrompt biometricPrompt;
    SharedPreferences sharedPreferences;

    Admenager admenager;

    BiometricPrompt.PromptInfo promptInfo;
    Executor executor;
    SharedPreferences.Editor editor;
    BackTrackUtils backTrackUtils;
    TemplateView templateView;

    @SuppressLint("MissingInflatedId")
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mobile);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Test Mobile");

        }
        backTrackUtils=new BackTrackUtils(this,this);

        templateView=findViewById(R.id.my_template);
      admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_interestitial_test_mobile_back","admobe_interestitial_test_mobile_back_test");
      admenager.loadAd("admobe_mobileTest_nativ","admobe_mobileTest_nativ_test",templateView);
        earPhone=findViewById(R.id.EarPhone);
        display=findViewById(R.id.display);
        lightSensor=findViewById(R.id.lightSensor);
        ivLightSensor=findViewById(R.id.ivLightSensor);

        earProximity=findViewById(R.id.earProximity);
        volumeUp=findViewById(R.id.volumeUp);
        volumeDown=findViewById(R.id.voloumeDown);
        ivEarproximity=findViewById(R.id.ivEarproximity);
        ivVolumeUp=findViewById(R.id.ivVolumeUp);
        ivVolumeDown=findViewById(R.id.ivVolumeDown);
        acclerometer=findViewById(R.id.accelermeter);
        ivAcelerometer=findViewById(R.id.ivAccelarometer);
        microPhone=findViewById(R.id.microPhone);
        ivLoudSpeaker=findViewById(R.id.ivLoudSpeaker);
        flashLight=findViewById(R.id.flashLight);
        fingerPrint=findViewById(R.id.fingerPrint);
       multiTouch =findViewById(R.id.blueTooth);
        ivVibration=findViewById(R.id.ivVibration);
        loudSpeaker=findViewById(R.id.loudSpeaker);
        vibration=findViewById(R.id.vibration);
        ivEarSpeaker=findViewById(R.id.ivEarSpeaker);
        ivMultiTouch=findViewById(R.id.ivMultiTouch);
        ivMicrophone=findViewById(R.id.ivMicroPhone);
        ivDisplay=findViewById(R.id.ivDisplay);
        ivFlashLight=findViewById(R.id.ivFlashLight);
        ivFingerPrint=findViewById(R.id.ivFingerPrint);
        sharedPreferences=getSharedPreferences("testMobile",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        getData();

        loudSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestMobile.this, TestDetailActivity.class);
                intent.putExtra("testMobile","loudSpeaker");
                startActivityForResult(intent,202);
            }
        });
        earPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                intent.putExtra("testMobile","earSpeaker");
                startActivityForResult(intent, 201);
            }
        });
        vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                intent.putExtra("testMobile","vibration");
                startActivityForResult(intent, 191);
            }
        });

        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                intent.putExtra("testMobile","flashLight");
                startActivityForResult(intent, 200);
            }
        });
        multiTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TestMobile.this,MultiTouchScreen.class),130);
            }
        });


        display.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","display");
                   startActivityForResult(intent, 190);
               }
           });

        fingerPrint.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                 fingerPrintTest();
               }
           });
        earProximity.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","ear proximity");
                   startActivityForResult(intent, 132);
               }
           });
        volumeUp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","volumeUp");
                   startActivityForResult(intent, 133);
               }
           });
        volumeDown.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","volumeDown");
                   startActivityForResult(intent, 134);

               }
           });
        microPhone.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","MicroPhone");
                   startActivityForResult(intent, 135);

               }
           });
        acclerometer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","accelermeter");
                   startActivityForResult(intent, 136);
               }
           });
        lightSensor.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(TestMobile.this,TestDetailActivity.class);
                   intent.putExtra("testMobile","lightSensor");
                   startActivityForResult(intent, 137);
               }
           });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==202 &&resultCode== RESULT_OK ){
            if (data!=null){
                boolean answer=data.getBooleanExtra("answer",false);


                if (answer){
                    ivLoudSpeaker.setImageResource(R.drawable.baseline_check_circle_24);


                }else {
                    ivLoudSpeaker.setImageResource(R.drawable.baseline_cancel_24);

                }

                Log.d("rcv","bb"+answer);
            }


        }
        else if (requestCode==201  &&resultCode==RESULT_OK){
            if (data!=null){
                boolean answer=data.getBooleanExtra("answer",false);
                if (answer){
                    ivEarSpeaker.setImageResource(R.drawable.baseline_check_circle_24);
                }else {
                    ivEarSpeaker.setImageResource(R.drawable.baseline_cancel_24);
                }
            }



        }
        else if (requestCode==191 && resultCode==RESULT_OK){
            if (data!=null){
                boolean answer=data.getBooleanExtra("answer",false);
                if (answer){
                    ivVibration.setImageResource(R.drawable.baseline_check_circle_24);
                }else {
                    ivVibration.setImageResource(R.drawable.baseline_cancel_24);
                }
            }
        }
        else if (requestCode==200 && resultCode==RESULT_OK) {
            if (data!=null){
                boolean answer=data.getBooleanExtra("answer",false);
                if (answer){
                    ivFlashLight.setImageResource(R.drawable.baseline_check_circle_24);
                }else {
                    ivFlashLight.setImageResource(R.drawable.baseline_cancel_24);
                }
            }
        }
        else if (requestCode==130 && resultCode==RESULT_OK){
            if (data!=null){
                boolean answer=data.getBooleanExtra("answer",false);
                if (answer){
                    ivMultiTouch.setImageResource(R.drawable.baseline_check_circle_24);
                }else {
                    ivMultiTouch.setImageResource(R.drawable.baseline_cancel_24);
                }
            }
        }
        else if (requestCode==190&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivDisplay.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivDisplay.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }

        else if (requestCode==132&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivEarproximity.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivEarproximity.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }

        else if (requestCode==133&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivVolumeUp.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivVolumeUp.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }

        else if (requestCode==134&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivVolumeDown.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivVolumeDown.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }

        else if (requestCode==135&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivMicrophone.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivMicrophone.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }

        else if (requestCode==136&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivAcelerometer.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivAcelerometer.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }
        else if (requestCode==137&& resultCode==RESULT_OK) {
            if (data != null) {
                boolean answer = data.getBooleanExtra("answer", false);
                if (answer) {
                    ivLightSensor.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    ivLightSensor.setImageResource(R.drawable.baseline_cancel_24);
                }

            }
        }


    }
    public void fingerPrintTest(){
        executor= ContextCompat.getMainExecutor(TestMobile.this);
        biometricPrompt=new BiometricPrompt(TestMobile.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(TestMobile.this, "error", Toast.LENGTH_SHORT).show();
                ivFingerPrint.setImageResource(R.drawable.baseline_cancel_24);
                editor.putString("fingerPrint","no");
                editor.apply();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(TestMobile.this, "succes", Toast.LENGTH_SHORT).show();
                ivFingerPrint.setImageResource(R.drawable.baseline_check_circle_24);
                editor.putString("fingerPrint","yes");
                editor.apply();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                ivFingerPrint.setImageResource(R.drawable.baseline_cancel_24);
                editor.putString("fingerPrint","no");
                editor.apply();
            }
        });
        promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("FingerPrintTesting")
                .setSubtitle("Place an enrolled finger on the fingerprint  sensor to pass test. Please enroll finger if you haven'y. ")
                .setNegativeButtonText("Cancel")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }


public void getData() {
    if (sharedPreferences != null) {
        String[] keys = {
                "loudSpeaker",
                "earSpeaker",
                "display",
                "flashLight",
                "ear proximity",
                "vibration",
                "volumeUp",
                "volumeDown",
                "MicroPhone",
                "accelermeter",
                "lightSensor",
                "multiTouch",
                "fingerPrint"
        };

        int[] imageResources = {
                R.drawable.baseline_check_circle_24,
                R.drawable.baseline_cancel_24
        };

        ImageView[] imageViews = {
                ivLoudSpeaker,
                ivEarSpeaker,
                ivDisplay,
                ivFlashLight,
                ivEarproximity,
                ivVibration,
                ivVolumeUp,
                ivVolumeDown,
                ivMicrophone,
                ivAcelerometer,
                ivLightSensor,
                ivMultiTouch,
                ivFingerPrint
        };

        for (int i = 0; i < keys.length; i++) {
            String value = sharedPreferences.getString(keys[i], null);
            if (value != null) {
                if ("yes".equals(value)) {
                    imageViews[i].setImageResource(imageResources[0]);
                } else if ("no".equals(value)) {
                    imageViews[i].setImageResource(imageResources[1]);
                }
            }
        }
    }
}

    @Override
    public void onBackPressed() {
        backTrackUtils.backTrack();
        admenager.showadmobeInterstitialAd();
      super.onBackPressed();

    }
}