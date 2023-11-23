package com.example.appUpdate.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appUpdate.R;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class PermissionScreen extends AppCompatActivity {
    Button allow, deny;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_screen);

        progressBar=findViewById(R.id.progress);
        allow = findViewById(R.id.allow);
        deny = findViewById(R.id.deny);
        if (getGrantStatus(this)){
            showProgressabar();
            Intent intent=new Intent(PermissionScreen.this, appUsageActivity.class);
            startActivity(intent);
            finish();

        }else {
           allow.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                   startActivityForResult(intent,10);
               }
           });
        }

    }

    private void showProgressabar() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean getGrantStatus(Context context) {

        AppOpsManager appOps = null;
        appOps = (AppOpsManager) getApplicationContext()
                .getSystemService(Context.APP_OPS_SERVICE);

        int  mode = 0;
        mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getApplicationContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            return (getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            return (mode == MODE_ALLOWED);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getGrantStatus(this)){
                    Intent intent=new Intent(PermissionScreen.this, appUsageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
}