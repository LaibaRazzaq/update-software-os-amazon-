package com.example.appUpdate.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.FileInfo;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbrain.AdId;
import com.appbrain.InterstitialBuilder;
import com.example.appUpdate.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;


import org.jsoup.Jsoup;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Formatter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AppDetailActivity extends AppCompatActivity{
    ImageView applicationIcon;
    TextView applicationName, applicationVersion, packageName,appSize,installedDate,applicationPermissionTV;
    RelativeLayout otherappLayout;
    Button  updateLayout, launchLayout,unInstallLayout  ;
          RelativeLayout  OtherLayout,moreLayout;
     RelativeLayout appInfoLayout, viewPermission;
    String applicationCurrentVersion,appNameString;
    String applicationNameString;
    String[] permissionStringArray;
    ScrollView scrollView;
    RelativeLayout relativeAdmob;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    String installDateString;

    SharedPreferences.Editor editor;
  FrameLayout mAdView;


    private InterstitialBuilder interstitialBuilder;
    AdRequest adRequest1;
    Admenager admenager;
    int limit_value;

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed () {
        admenager.showadmobeInterstitialAd();

     super.onBackPressed();


    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);



        sharedPreferences=getSharedPreferences("appReview", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        boolean systemApp=sharedPreferences.getBoolean("systemLayout", false);


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateLayout = findViewById(R.id.updateAppBtn);
        if (systemApp){
          updateLayout.setVisibility(View.GONE);
        }else {
            updateLayout.setVisibility(View.VISIBLE);
        }

       unInstallLayout=findViewById(R.id.unInstallApp);
        applicationIcon = (ImageView) findViewById(R.id.appIcon);
        applicationName = (TextView) findViewById(R.id.appName);
        packageName=findViewById(R.id.packageName);
        installedDate=findViewById(R.id.installDate);
        applicationVersion = (TextView) findViewById(R.id.appVersion);
        appSize=findViewById(R.id.appSize);
        appNameString=this.getIntent().getStringExtra("appname");
        applicationName.setText(appNameString);
        installDateString=this.getIntent().getStringExtra("lastDate");
        installedDate.setText(installDateString);
        appInfoLayout=findViewById(R.id.appInfoAppBtn);
        applicationCurrentVersion = this.getIntent().getStringExtra("versionName");
         viewPermission=findViewById(R.id.viewPermissionlayout);
        applicationVersion.setText(applicationCurrentVersion);
        scrollView = findViewById(R.id.scrollView);

        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_intertesial_back","admobe_intertesial_back_test");
        mAdView = findViewById(R.id.adView);
        admenager.LoadMediumBannerAD(mAdView,"admobe_banner_detail_test","admobe_banner_detail");



//        limit_value = sharedPreferences.getInt("limit_value1", 1);
//        if(limit_value==1||limit_value==4){
//           // Toast.makeText(AppDetailActivity.this, "Sorry we"+limit_value, Toast.LENGTH_SHORT).show();
//
//            admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_back));
//
//        } else if(limit_value>4){
//            limit_value=0;
//        }



//        limit_value=sharedPreferences.getInt("BackCount", 0);
//        editor.putInt("BackCount",limit_value+1);
//        editor.apply();

//        if (limit_value==1){
//            editor.putInt("BackCount", 0);
//            editor.apply();
     //   }







//admenager.loadAd(getString(R.string.admobe_native_datil));
        applicationNameString=this.getIntent().getStringExtra("packageName");
        packageName.setText(applicationNameString);

        viewPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(AppDetailActivity.this, com.example.appUpdate.activities.viewPermission.class);
                intent1.putExtra("packageName", applicationNameString);
                startActivity(intent1);
            }
        });
//        Log.d("packageNmae",applicationNameString);
//        if (applicationNameString != null) {
//            editor.putString("packageName", this.getIntent().getStringExtra("packageName"));
//            editor.apply();
//        }
//        if (applicationNameString == null){
//            applicationNameString = sharedPreferences.getString("packageName","ABC");
//        }   p
        permissionStringArray = this.getIntent().getStringArrayExtra("permission");

        final String str = Arrays.toString(permissionStringArray);


        launchLayout = findViewById(R.id.launchAppBtn);



        launchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx=AppDetailActivity.this; // or you can replace **'this'** with your **ActivityName.this**
                Intent i = ctx.getPackageManager().getLaunchIntentForPackage(applicationNameString);
                try {
                    ctx.startActivity(i);
                }catch (NullPointerException e){
                    Toast.makeText(AppDetailActivity.this, "Sorry we can not launch Background running apps", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(AppDetailActivity.this, "Sorry we can not launch Background running apps", Toast.LENGTH_SHORT).show();
                }
            }
        });
        appInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + applicationNameString));
                try {
                    startActivity(i);
                }catch(ActivityNotFoundException e){

                }catch (NullPointerException e)
                {

                }
            }
        });


        updateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + applicationNameString ));
                startActivity(browserIntent);
            }
        });


             PackageManager packageManager=getPackageManager();
        try {
            ApplicationInfo applicationInfo=packageManager.getApplicationInfo(applicationNameString, 0);
            File file=new File(applicationInfo.publicSourceDir);
            long size=file.length();
           String appSizeString=readableFileSize(size);
           appSize.setText(appSizeString);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        byte[] b = new byte[0];
        try{
            b = extras.getByteArray("icon");
        }catch (NullPointerException e){

        }
        Bitmap bmp = null;
        try{
            bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        }catch (NullPointerException e){

        }

        try {
            applicationIcon.setImageBitmap(bmp);
        }catch (NullPointerException e){


            try {
                applicationIcon.setImageResource(R.mipmap.ic_launcher);
            }catch (NullPointerException fg){

                applicationIcon.setImageResource(R.drawable.icon_web);
            }

        }























        unInstallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String app_pkg_name = applicationNameString;

                int UNINSTALL_REQUEST_CODE = 1;
                Intent intent = new Intent("android.intent.action.DELETE");
                intent.setData(Uri.parse("package:" + applicationNameString));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
              //  Log.d("activityCancel", "Cancel");
                //Dismissed
            }
        }
    }
    public String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB", "PB", "EB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        String result = null;
        result = new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        return result;
    }

}
