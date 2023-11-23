package com.example.appUpdate.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.R;

import java.io.File;

public class CleanActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    LottieAnimationView lottieAnimationView, notFound;
    RelativeLayout rl;
    Handler handler;
    TextView tv,totalJunk;
    String [] strings,strings1,strings2;
    SharedPreferences.Editor editor;
    String temFile,cache,apk,emptyFile;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lottieAnimationView=findViewById(R.id.cleaning);
        tv=findViewById(R.id.tv);
        notFound=findViewById(R.id.noFound);
        totalJunk=findViewById(R.id.totalJunk);
        handler=new Handler();
        rl=findViewById(R.id.rl);
        sharedPreferences=getSharedPreferences("cleanJunk", MODE_PRIVATE);


        temFile=sharedPreferences.getString("tempfile","defaultKey");
        cache=sharedPreferences.getString("cache","defaultKey");
         emptyFile=sharedPreferences.getString("emptyFile","defaultKey");
         apk=sharedPreferences.getString("apk","defaultKey");

        strings=temFile.split("\n");
        long size= strings.length;
        String tempFileSize=String.valueOf(size);
        Log.d("rcv","size"+tempFileSize);


        strings1=emptyFile.split("\n");
        long size1=strings1.length;
        String emtyFileSize=String.valueOf(size1);
        Log.d("rcv",emtyFileSize);

        strings2=apk.split("\n");
        long size2=strings2.length;
        String apkFileSize=String.valueOf(size2);
        Log.d("rcv",apkFileSize);
        file=new File(cache);
        long size3=file.length();
        String appCacheSize=String.valueOf(size3);
        Log.d("rcv",appCacheSize);
        String totalSize=size+size1+size2+" "+"Mb";

        Log.d("rcv",totalSize);
     if (apkFileSize.equals("1") && emtyFileSize.equals("1") && tempFileSize.equals("1")){
         tv.setText("No files delete");
         totalJunk.setText("Already delete junk files");
         notFound.setVisibility(View.VISIBLE);
         lottieAnimationView.setVisibility(View.GONE);
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 Intent  intent=new Intent(CleanActivity.this,  DeleteJunk.class);
                 startActivity(intent);
                 finish();

             }
         },5000);
     }else {
         totalJunk.setText("Junk File Found:"+totalSize );
         tv.setText("Deleting Temporary File");
         new Task1().execute();

     }













    }
    private  class Task1 extends AsyncTask<Void,Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            for (String file:strings){




                File junkFile=new File(file);

                junkFile.delete();



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Deleting Empty Folder");
                 new Task2().execute();
                }
            },5000);
        }
    }

    private  class Task2 extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            for (String files: strings1){


                File empty=new File(files);
                File[]child=empty.listFiles();
                if (child!=null){
                    if (child.length==0){
                        empty.delete();
                    }
                }




            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Task3().execute();
                }
            },5000);
        }
    }
   private class Task3 extends AsyncTask<Void,Void,Void>{

       @Override
       protected Void doInBackground(Void... voids) {

           for (String file2:strings2){


               File apkFile=new File(file2);
               apkFile.delete();



           }

           return null;
       }

       @Override
       protected void onPostExecute(Void unused) {
           super.onPostExecute(unused);
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   tv.setText("Deleting  App Apk ");
                   new Task4().execute();

               }
           },5000);

       }
   }
    private class Task4 extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

           deleteDir2(file);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv.setText("Deleting App cache");
                    Intent intent=new Intent(CleanActivity.this,DeleteJunk.class);
                    startActivity(intent);
                    finish();
                }
            },5000);


        }
    }

    public boolean deleteDir2(File dir) {
        if (dir.isDirectory()) {
            File[] childen = dir.listFiles();
            if (childen != null) {
                for (File child : childen) {
                    boolean success = deleteDir2(child);
                    Log.d("rcv","done");
                    if (!success) {
                        Log.d("rcv","fail");
                        return false;
                    }
                }
            }

        }
        return dir.delete();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}
}