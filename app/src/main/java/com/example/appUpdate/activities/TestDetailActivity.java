package com.example.appUpdate.activities;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.example.appUpdate.R;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Executor;


public class TestDetailActivity extends AppCompatActivity implements SensorEventListener {
    Ringtone defaultRingtone;
    String intentStringExtra;
    Button yes, no, next;
    TextView lightSensorTv;
    private int mTotalBytesRead;
    private Handler mHandler;
    private Runnable mProgressRunnable;
    private AudioRecord mRecorder;
    private int mBufferSize;
    private byte[] mBuffer;
    Admenager admenager;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String[] permissions = {RECORD_AUDIO};
    CameraManager cameraManager;
    private final Handler handler = new Handler(Looper.getMainLooper());
    Vibrator vibrator;
    ImageView imageView;

    TextView testName, testDesc;
    SensorManager sensorManager;


    Sensor proximity;
    RelativeLayout photoEditor;
    private static final int SAMPLE_RATE_IN_HZ = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE_IN_BYTES = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioRecord audioRecord;
    private ProgressBar progressBar;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private static final float SHAKE_THRESHOLD = 15f;

    private long mShakeTimestamp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int mShakeCount;
    private boolean shouldVibrate = true;


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);

        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_interestitial_test_result_back","admobe_interestitial_test_result_back_test");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        next = findViewById(R.id.next);
        lightSensorTv = findViewById(R.id.lightSensortv);
        testDesc = findViewById(R.id.textView);
        testName = findViewById(R.id.testName);
        imageView = findViewById(R.id.image);
        progressBar = findViewById(R.id.progressBar);
        sharedPreferences = getSharedPreferences("testMobile", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent = getIntent();

        MobileTesting mobileTesting = new MobileTesting();
        intentStringExtra = intent.getStringExtra("testMobile");
        photoEditor=findViewById(R.id.photoEditor);
        photoEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ng.collagemaker.photoeditor.photocollage")));


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        if (intentStringExtra != null) {
            switch (intentStringExtra) {
                case "loudSpeaker":
                    testName.setText("LoudSpeaker");
                    testDesc.setText("Audio is playing. Can you hear it?");
                    imageView.setImageResource(R.drawable.baseline_speaker_24);
                    defaultRingtone = mobileTesting.testLoudSpeaker(this);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    if (defaultRingtone!=null){
                                        defaultRingtone.stop();
                                    }

                                    editor.putString("loudSpeaker", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));


                                    finish();

                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    if (defaultRingtone!=null){
                                        defaultRingtone.stop();
                                    }


                                    editor.putString("loudSpeaker", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));

                                    finish();
                                }
                            });

                        }
                    });


                    break;
                case "earSpeaker":
                    defaultRingtone = mobileTesting.testingEarSpeaker(this);
                    testName.setText("Ear Speaker");
                    testDesc.setText("Audio is playing. Can you hear it?");
                    imageView.setImageResource(R.drawable.baseline_hearing_24);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    if (defaultRingtone!=null){
                                        defaultRingtone.stop();
                                    }


                                    editor.putString("earSpeaker", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));


                                    finish();
                                }
                            });


                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    if (defaultRingtone!=null){
                                        defaultRingtone.stop();
                                    }
                                    editor.putString("earSpeaker", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));

                                    finish();
                                }
                            });

                        }
                    });


                    break;
                case "flashLight":
                    testName.setText("FlashLight");
                    testDesc.setText("Is the flashlight working?");
                    imageView.setImageResource(R.drawable.baseline_highlight_24);

                    cameraManager = mobileTesting.testFlashLight(this);
                    try {
                        String[] cameraIdList = cameraManager.getCameraIdList();
                        if (cameraIdList.length > 0) {
                            String cameraId = cameraIdList[0];
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraId != null) {
                                try {
                                    if (Boolean.TRUE.equals(cameraManager.getCameraCharacteristics(cameraId)
                                            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE))) {
                                        cameraManager.setTorchMode(cameraId, true);
                                    } else {
                                        // Flash not available for the camera
                                        yes.setVisibility(View.GONE);
                                        no.setVisibility(View.GONE);
                                        testDesc.setText("Flash not available for the camera");
                                    }
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // No cameras available on the device
                            yes.setVisibility(View.GONE);
                            no.setVisibility(View.GONE);
                            testDesc.setText("No cameras available on the device");

                        }
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    try {
                                        String camerId = null;
                                        camerId = cameraManager.getCameraIdList()[0];
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            cameraManager.setTorchMode(camerId, false);
                                        }

                                        editor.putString("flashLight", "yes");
                                        editor.apply();
                                        setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                        finish();

                                    } catch (Exception e) {
                                        throw new RuntimeException(e);

                                    }
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    try {
                                        String camerId = cameraManager.getCameraIdList()[0];
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            cameraManager.setTorchMode(camerId, false);
                                        }

                                        editor.putString("flashLight", "no");
                                        editor.apply();
                                        setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                        finish();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                        }
                            });


                    break;
                case "display":
                    yes.setVisibility(View.GONE);
                    no.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    testName.setText("Display");
                    testDesc.setText("Display may turn black,white,red,green,and blue. Look for different colored pixel and tap to continue");
                    imageView.setImageResource(R.drawable.display__2_);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {

                                    editor.putString("display", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("display", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });


                        }
                    });


                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    Intent intent1 = new Intent(TestDetailActivity.this, ColorsActivity.class);
                                    startActivityForResult(intent1, 202);
                                }
                            });

                        }
                    });

                    break;
                case "vibration":
                    testName.setText("Vibration");
                    testDesc.setText("Can you feel the phone vibrating");
                    imageView.setImageResource(R.drawable.baseline_vibration_24);

                    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 500, 1000};
                    vibrator.vibrate(pattern, 0);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    vibrator.cancel();
                                    shouldVibrate = false;

                                    editor.putString("vibration", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    vibrator.cancel();
                                    shouldVibrate = false;
                                    editor.putString("vibration", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });
                    break;

                case "ear proximity":
                    earProximity(this);
                    testName.setText("Ear proximity");
                    testDesc.setText("Cover the area above the display or place your palm on the display, do you get any feedback?");
                    imageView.setImageResource(R.drawable.icons8_proximity_sensor_48);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {

                                    editor.putString("ear proximity", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("ear proximity", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });
                    break;

                case "volumeUp":
                    testName.setText("Volume Up Button");
                    testDesc.setText("Press the volume up key,do you get any feedback?");
                    imageView.setImageResource(R.drawable.baseline_volume_up_24);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("volumeUp", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("volumeUp", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });
                    break;


                case "volumeDown":
                    testName.setText("Volume Down Button");
                    testDesc.setText("Press the volume down key,do you get any feedback?");
                    imageView.setImageResource(R.drawable.baseline_volume_up_24);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("volumeDown", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("volumeDown", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });
                    break;
                case "MicroPhone":
                    progressBar.setVisibility(View.VISIBLE);
                    testName.setText(R.string.microphone);
                    testDesc.setText(R.string.microPhoneDesc);
                    imageView.setImageResource(R.drawable.microphone);
                    microPhoneTesting();
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("MicroPhone", "yes");
                                    editor.apply();
                                    handler.removeCallbacks(mProgressRunnable);
                                    if (mRecorder != null) {
                                        mRecorder.stop();
                                    }
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });


                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("MicroPhone", "no");
                                    editor.apply();
                                    handler.removeCallbacks(mProgressRunnable);
                                    if (mRecorder != null) {
                                        mRecorder.stop();
                                    }
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });
                    break;

                case "accelermeter":

                    testName.setText(R.string.Accelerometer);
                    testDesc.setText(R.string.Accelerometer_dec);
                    imageView.setImageResource(R.drawable.accelerometer);

                    earProximity(this);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("accelermeter", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("accelermeter", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });


                    break;

                case "lightSensor":
                    lightSensorTv.setVisibility(View.VISIBLE);
                    testName.setText(R.string.light);
                    testDesc.setText(R.string.lightSensor);
                    imageView.setImageResource(R.drawable.baseline_brightness_7_24);
                    earProximity(this);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("lightSensor", "yes");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", true));
                                    finish();
                                }
                            });

                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
                                @Override
                                public void onShowAd() {
                                    editor.putString("lightSensor", "no");
                                    editor.apply();
                                    setResult(Activity.RESULT_OK, new Intent().putExtra("answer", false));
                                    finish();
                                }
                            });

                        }
                    });

                    break;
            }
        }


    }

    @Override
    public void onBackPressed() {
        if (intentStringExtra.equals("loudSpeaker") || intentStringExtra.equals("earSpeaker")) {
            if (defaultRingtone!=null){
                defaultRingtone.stop();
            }

        } else if (intentStringExtra.equals("flashLight")) {
            String camerId = null;
            try {
                String[] cameraIdList = cameraManager.getCameraIdList();
                if (cameraIdList.length > 0) {
                    camerId = cameraIdList[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && camerId != null) {
                        try {
                            if (Boolean.TRUE.equals(cameraManager.getCameraCharacteristics(camerId)
                                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE))) {
                                cameraManager.setTorchMode(camerId, false);
                            } else {
                                // Flash not available for the camera
                            }
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Camera camera = Camera.open();
                        Camera.Parameters parameters = camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        camera.release();
                    }
                }
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }


        } else if (intentStringExtra.equals("vibration")) {
            vibrator.cancel();
        } else if (intentStringExtra.equals("MicroPhone")) {
            if (mHandler != null) {
                mHandler.removeCallbacks(mProgressRunnable);
            }
            if (mRecorder != null) {
                mRecorder.stop();
            }
        }
        admenager.showadmobeInterstitialAd(new Admenager.onAdShowListener() {
            @Override
            public void onShowAd() {

                finish();



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 202 && resultCode == RESULT_OK) {
            yes.setVisibility(View.VISIBLE);
            no.setVisibility(View.VISIBLE);

            next.setVisibility(View.GONE);
            testDesc.setText("Were the screen one solid color?");


        }


    }

    public void earProximity(Context context) {

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        switch (intentStringExtra) {
            case "ear proximity":
                proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                break;
            case "accelermeter":
                proximity = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

                break;
            case "lightSensor":
                proximity = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (intentStringExtra.equals("ear proximity") || intentStringExtra.equals("accelermeter") || intentStringExtra.equals("lightSensor")) {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (intentStringExtra.equals("ear proximity") || intentStringExtra.equals("accelermeter") || intentStringExtra.equals("lightSensor")) {
            sensorManager.unregisterListener(this);
        } else if (intentStringExtra.equals("MicroPhone")) {
            if (mHandler != null) {
                mHandler.removeCallbacks(mProgressRunnable);
            }
            if (mRecorder != null) {
                mRecorder.stop();
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (intentStringExtra.equals("ear proximity")) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                float distance = event.values[0];

                // If an object is detected nearby, vibrate the device
                if (distance < proximity.getMaximumRange()) {

                    vibrator.vibrate(500); // Vibrate for 500 milliseconds
                }
            }
        } else if (intentStringExtra.equals("accelermeter")) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

            if (acceleration > SHAKE_THRESHOLD) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;


                vibrator.vibrate(500);


            }
        } else if (intentStringExtra.equals("lightSensor")) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float lightValue = event.values[0];

                // Update the TextView with the light value
                int lightValueInt = (int) lightValue;

                // Update the TextView with the light value
                String text = lightValueInt + " lx";
                lightSensorTv.setText(text);


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (KeyEvent.ACTION_UP == action) {
                    if (intentStringExtra.equals("volumeUp")) {
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }

                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (KeyEvent.ACTION_UP == action) {
                    if (intentStringExtra.equals("volumeDown")) {
                        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }

                }
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void microPhoneTesting() {
        if (ActivityCompat.checkSelfPermission(this, RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED) {
            int sampleRate = 44100; // set your own sample rate
            int channelConfig = android.media.AudioFormat.CHANNEL_IN_MONO;
            int audioFormat = android.media.AudioFormat.ENCODING_PCM_16BIT;

            mBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
            mBuffer = new byte[mBufferSize];
            mTotalBytesRead = 0;


            mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, mBufferSize);

            mRecorder.startRecording();

            mHandler = new Handler(Looper.getMainLooper());
            mProgressRunnable = new Runnable() {
                private static final int THRESHOLD = 2000; // adjust this threshold value as needed
                private boolean mIsAboveThreshold = false;

                @Override
                public void run() {
                    int amplitude = 0;
                    if (mRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        int bytesRead = mRecorder.read(mBuffer, 0, mBufferSize);
                        if (bytesRead > 0) {
                            mTotalBytesRead += bytesRead;
                            for (int i = 0; i < bytesRead; i += 2) {
                                short sample = (short) ((mBuffer[i]) | mBuffer[i + 1] << 8);
                                if (sample > amplitude) {
                                    amplitude = sample;
                                }
                            }
                        }
                    }

                    boolean isAboveThreshold = amplitude > THRESHOLD;
                    if (isAboveThreshold) {
                        mIsAboveThreshold = true;
                    } else if (mIsAboveThreshold) {
                        // decrease progress bar value if amplitude falls below threshold
                        progressBar.setProgress(progressBar.getProgress() - 1);
                        mIsAboveThreshold = false;
                    } else {
                        // reset progress bar value if amplitude is consistently low
                        progressBar.setProgress(0);
                    }

                    int progress = (int) (100 * (double) amplitude / Short.MAX_VALUE);
                    Log.d("TAG", "Amplitude: " + amplitude + ", Progress: " + progress);
                    progressBar.setProgress(progress);
                    mHandler.postDelayed(this, 100);
                }
            };
            mHandler.postDelayed(mProgressRunnable, 100);

        }else {
            RequestPermissions();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intentStringExtra.equals("MicroPhone"))
        {
            if (mHandler != null) {
                mHandler.removeCallbacks(mProgressRunnable);
            }

            if (mRecorder!=null){
                mRecorder.stop();
            }        }
    }

    public boolean  CheckPermissions() {
        // this method is used to check permission

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(TestDetailActivity.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0) {
                microPhoneTesting();
                } else {
                Toast.makeText(this, "permission required", Toast.LENGTH_SHORT).show();

            }


        }
    }

}
