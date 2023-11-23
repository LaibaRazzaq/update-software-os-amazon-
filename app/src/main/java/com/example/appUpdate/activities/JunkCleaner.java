package com.example.appUpdate.activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class JunkCleaner extends AppCompatActivity {
    TextView fileNmae, ourJunk, emptyFolder,cleanTv;
    ProgressBar p1,p2,p3,p4;
    ImageView iv1,iv2,iv3,iv4;
    String[] permission={READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    Button del, scanning;
    int trackCount=3,count=0;
    LottieAnimationView lottieAnimationView,lottieAnimationView1;
    RelativeLayout cleanBtn;
    SharedPreferences sharedPreferences;
    Admenager admenager;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    String selectBtn="";
    String selectBtnCncl="";
    boolean isScan=false;

   AlertDialog alertDialog;
   BackTrackUtils backTrackUtils;
    List<Junk> list ;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junk_cleaner);
        backTrackUtils=new BackTrackUtils(this,this);


        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Clean Junk");

        }
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_interestitial_junk_cleaner_back","admobe_interestitial_junk_cleaner_back_test");
        fileNmae = findViewById(R.id.path);
        lottieAnimationView=findViewById(R.id.lottieProgress);
        scanning=findViewById(R.id.scanning);
        cleanBtn=findViewById(R.id.cleanBtn);
        cleanTv=findViewById(R.id.cleanTv);
        scanning.setEnabled(false);
        del = findViewById(R.id.del);
        scanning.setVisibility(View.VISIBLE);
        del.setVisibility(View.GONE);
        lottieAnimationView1=findViewById(R.id.lottieProgress1);

        ourJunk = findViewById(R.id.ourJunk);
        p1 = findViewById(R.id.p1);
        emptyFolder=findViewById(R.id.emptyfolder);
        iv1 = findViewById(R.id.iv1);
        iv2=findViewById(R.id.iv2);
        iv3=findViewById(R.id.iv3);
        p2=findViewById(R.id.p2);
        p3=findViewById(R.id.p3);
        p4=findViewById(R.id.p4);
        iv4=findViewById(R.id.iv4);



        sharedPreferences=getSharedPreferences("cleanJunk",MODE_PRIVATE);
           del.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AlertDialog.Builder builder=new AlertDialog.Builder(JunkCleaner.this).setCancelable(true);
                   builder.setMessage("Are you sure to clean junk files").setTitle("Clean Junk Files").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Intent intent =new Intent(JunkCleaner.this,CleanActivity.class);
                           startActivity(intent);
                           finish();
                       }
                   }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                       }
                   });
             builder.show();

               }
           });
           cleanBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (checkPermission()) {
                       p1.setVisibility(View.VISIBLE);
                       p2.setVisibility(View.VISIBLE);
                       lottieAnimationView.setVisibility(View.VISIBLE);
                       lottieAnimationView1.setVisibility(View.GONE);
                       p3.setVisibility(View.VISIBLE);
                       iv1.setVisibility(View.GONE);
                       iv2.setVisibility(View.GONE);
                       iv3.setVisibility(View.GONE);
                       iv4.setVisibility(View.GONE);
                       p4.setVisibility(View.VISIBLE);
                       cleanBtn.setVisibility(View.GONE);
                       list  = loadAllJunk();

                       new MyAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                       new task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                       new  task3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                   } else {
                       requestPermission(JunkCleaner.this);
                   }
               }
           });
          cleanTv.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (checkPermission()) {
                      list  = loadAllJunk();
                      new MyAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                      new task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                      new  task3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                      p1.setVisibility(View.VISIBLE);
                      p2.setVisibility(View.VISIBLE);
                      lottieAnimationView.setVisibility(View.VISIBLE);
                      lottieAnimationView1.setVisibility(View.GONE);
                      p3.setVisibility(View.VISIBLE);
                      iv1.setVisibility(View.GONE);
                      iv2.setVisibility(View.GONE);
                      iv3.setVisibility(View.GONE);
                      iv4.setVisibility(View.GONE);
                      p4.setVisibility(View.VISIBLE);
                      cleanBtn.setVisibility(View.GONE);



                  } else {
//            Toast.makeText(JunkCleaner.this, "permission issue laiba ", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
//            startActivity(intent);
                      requestPermission(JunkCleaner.this);
                  }
              }
          });




    }




    private void openthisPath(Junk junk, StringBuilder temp) {


        File files = new File(junk.path);


        if (files.exists() && files.isDirectory()) {
            File[] file = files.listFiles();

            if (file != null) {


                for (File item : file) {


                    if (item.getName().endsWith(".temp") || item.getName().endsWith(".log") || item.getName().endsWith(".tmp")) {

                        temp.append(item.getAbsolutePath());
                        temp.append("\n");


                    }


                }
            }
        }else if (files.exists()&&files.isDirectory()){


            
        }


    }



    @SuppressLint("Range")
    private List<Junk> loadAllJunk() {
        isScan=true;
        List<Junk> temp = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] project = {MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.DATA};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, project, null, null, null);
        if (cursor != null) {

            while (cursor.moveToNext()) {
                try {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    Junk junk = new Junk(name, path);

                    temp.add(junk);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            cursor.close();
        } else {
            Toast.makeText(this, "bye", Toast.LENGTH_SHORT).show();
        }


        return temp;
    }

    private class MyAsync extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            isScan=true;


            Log.d("rcv", "on" + list.size());
            StringBuilder temp = new StringBuilder();
            try {
                for (Junk item : list) {
                    String pa = item.getPath();
                    publishProgress(pa);


                    openthisPath(item, temp);

                }




                if (temp.toString().equals("")) {
                    return "Not found";
                } else {
                    return temp.toString();
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("rcv", "error in for loop");
                return null;
            }


        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            try {
                fileNmae.setText(values[0]);
                p1.setVisibility(View.VISIBLE);
                iv1.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            p1.setVisibility(View.GONE);
            iv1.setVisibility(View.VISIBLE);



            editor=sharedPreferences.edit().putString("tempfile",string);
            editor.apply();

            File externalDir = getCacheDir();
            if (externalDir == null) {
                return;
            }

            Log.d("rcv","on"+externalDir.length());
            editor=sharedPreferences.edit().putString("cache",externalDir.getAbsolutePath());
            editor.apply();
            iv2.setVisibility(View.VISIBLE);
            p2.setVisibility(View.GONE);
            count++;
            trackAsyncTask();



        }
    }




    private  class task2 extends AsyncTask<Void,String,String>{

        @Override
        protected String doInBackground(Void... voids) {
            isScan=true;

            StringBuilder empty=new StringBuilder();
            for (Junk item:list){
                emptyFolder(item,empty);
            }
            if (empty.toString().equals("")) {
                return "Not found";
            } else {
                return empty.toString();
            }


        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);


            editor=sharedPreferences.edit().putString("emptyFile",string);
            editor.apply();
            p3.setVisibility(View.GONE);
            iv3.setVisibility(View.VISIBLE);
            count++;
            trackAsyncTask();





        }
    }

    private void emptyFolder(Junk junk, StringBuilder empty) {
        File files = new File(junk.path);
        if (files.isDirectory()&&files.exists()){
            File [] file =files.listFiles();
            if (file!=null){
                for (File item:file){
                    if (item.isDirectory() && item.listFiles()!=null && Objects.requireNonNull(item.listFiles()).length==0){
                        empty.append(item.getAbsolutePath());
                        empty.append("\n");

                    }


                }
            }

        }



    }

    private  class  task3 extends AsyncTask<Void, String, String>{
        @Override
        protected String doInBackground(Void... voids) {
            isScan=true;

            StringBuilder apkString=new StringBuilder();
            for (Junk item:list){
                apk(item,apkString);
            }
            return apkString.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isScan=false;
            editor=sharedPreferences.edit().putString("apk",s);
            editor.apply();
            p4.setVisibility(View.GONE);
            iv4.setVisibility(View.VISIBLE);
            count++;
            trackAsyncTask();


        }
    }


        private  void apk(Junk junk, StringBuilder stringBuilder){

        File file=new File(junk.getPath());

        if (file.exists() && file.isDirectory()){
            File [] files=file.listFiles();

            if (files!=null){
                for (File file1:files){
                    if (file1.getName().endsWith(".apk")){

                        try {
                            PackageManager pm=getPackageManager();
                            PackageInfo packageInfo=pm.getPackageInfo(file1.getName().replace(".apk",""),PackageManager.GET_UNINSTALLED_PACKAGES);
                            if (packageInfo==null){
                                stringBuilder.append(file1.getAbsolutePath());
                                stringBuilder.append("\n");
                            }

                        }catch (PackageManager.NameNotFoundException e){
                            e.printStackTrace();
                            stringBuilder.append(file1.getAbsolutePath());
                            stringBuilder.append("\n");
                        }
                    }
                }
            }
        }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2296){
            if (SDK_INT>= Build.VERSION_CODES.R){
                if (Environment.isExternalStorageManager()){
                    p1.setVisibility(View.VISIBLE);
                    p2.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView1.setVisibility(View.GONE);
                    p3.setVisibility(View.VISIBLE);
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    iv4.setVisibility(View.GONE);
                    p4.setVisibility(View.VISIBLE);
                    cleanBtn.setVisibility(View.GONE);
                    list  = loadAllJunk();
                    new MyAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    new  task3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }else {
                    Toast.makeText(this, "Allow permission for storage access", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
                if (grantResults.length>0){
                    boolean READ_EXTERNAL_STORAGE=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE &&WRITE_EXTERNAL_STORAGE){
                        p1.setVisibility(View.VISIBLE);
                        p2.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.VISIBLE);
                        lottieAnimationView1.setVisibility(View.GONE);
                        p3.setVisibility(View.VISIBLE);
                        iv1.setVisibility(View.GONE);
                        iv2.setVisibility(View.GONE);
                        iv3.setVisibility(View.GONE);
                        iv4.setVisibility(View.GONE);
                        p4.setVisibility(View.VISIBLE);
                        cleanBtn.setVisibility(View.GONE);
                        list  = loadAllJunk();
                        new MyAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new task2().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        new  task3().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else {
                        Toast.makeText(this, "Allow permission for storage acces", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    boolean checkPermission(){
        if (SDK_INT>= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else {
            int readcheck= ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int writecheck= ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return  readcheck== PackageManager.PERMISSION_GRANTED && writecheck==PackageManager.PERMISSION_GRANTED;
        }

    }
    private void requestPermission(final Context context) {



        if (SDK_INT>=Build.VERSION_CODES.R){
            try {
                Intent intent =new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            }catch (Exception e){
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,2296);
            }
        }
        else {
            ActivityCompat.requestPermissions(JunkCleaner.this, permission, 101);
        }

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

            final AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setMessage("Are you sure you want to exist").setCancelable(false).setTitle("Scanning in progress");
            selectBtn = "Yes";
            selectBtnCncl = "No";

            alert.setPositiveButton(selectBtn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   backTrackUtils.backTrack();
                    admenager.showadmobeInterstitialAd();
                    JunkCleaner.this.finish();
                }
            });
            alert.setNegativeButton(selectBtnCncl, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog = alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setTextColor(ContextCompat.getColor(JunkCleaner.this, R.color.black));
                    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(ContextCompat.getColor(JunkCleaner.this, R.color.black));
                }
            });
            alertDialog.show();

        }






    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }


    }
    public void trackAsyncTask(){
        if (trackCount==count){
            scanning.setVisibility(View.GONE);
            del.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView1.setVisibility(View.VISIBLE);
            lottieAnimationView1.loop(false);
        }

    }
}
