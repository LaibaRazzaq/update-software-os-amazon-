package com.example.appUpdate.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class MobileTesting {
    public Ringtone testLoudSpeaker(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
        Ringtone defaultRingtone;

        defaultRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        if (defaultRingtone != null) {
            defaultRingtone.setStreamType(AudioManager.STREAM_MUSIC);
            defaultRingtone.play();
        }


        return defaultRingtone;
    }

    public Ringtone testingEarSpeaker(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(false);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        if (defaultRingtone != null) {
            defaultRingtone.setStreamType(AudioManager.STREAM_VOICE_CALL);
            defaultRingtone.play();

        } else {
            // Handle the case where defaultRingtone is null.
            // You may want to set a default ringtone or take appropriate action.
        }

        return defaultRingtone;

    }

    public CameraManager testFlashLight(Context context) {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (cameraIdList.length > 0) {
                String cameraId = cameraIdList[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraId != null) {
                    try {
                        if (cameraManager.getCameraCharacteristics(cameraId)
                                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                            cameraManager.setTorchMode(cameraId, true);
                        } else {
                            // Flash not available for the camera
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // No cameras available on the device

            }
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        return cameraManager;
    }




}
