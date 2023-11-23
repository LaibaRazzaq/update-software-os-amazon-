package com.example.appUpdate.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appUpdate.R;

public class viewPermission extends AppCompatActivity {
    TextView permission;
   RelativeLayout amperMeterApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_permission);
        permission=findViewById(R.id.viewPermission);
       amperMeterApp=findViewById(R.id.amperMeterApp);
        String applicationString=this.getIntent().getStringExtra("packageName");
        String permissionString=getPermissionsByPackageName(applicationString);
        permission.setText(permissionString);
   amperMeterApp.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=zaka.com.amperemeter"));
           startActivity(intent);

       }
   });






    }
    protected String getPermissionsByPackageName(String packageName){
        // Initialize a new string builder instance
        StringBuilder builder = new StringBuilder();

        //  Log.d("packageNameADA",packageName);

        try {
            // Get the package info
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            //  Log.d("packageNameADA",packageInfo + "  packageInfo");
            // Permissions counter
            int counter = 1;

            // Loop through the package info requested permissions
            for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
                if ((packageInfo.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    String permission =packageInfo.requestedPermissions[i];

                    builder.append("").append(counter).append(". ").append(permission).append("\n");
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}