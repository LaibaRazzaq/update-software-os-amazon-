package com.example.appUpdate.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;
import com.example.appUpdate.adapters.DuplicatePhotoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.blankj.utilcode.util.FileUtils.getFileMD5ToString;

public class DuplicatePhotos extends AppCompatActivity {
    // ArrayList<String> duplicateImage;
    RecyclerView recyclerView;
    DuplicatePhotoAdapter duplicatePhotoAdapter;
    GridLayoutManager gridLayoutManager;
    LottieAnimationView progressBar;
    String[] permission={READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private SparseBooleanArray mSelectedItemsIds;
    public   static Button delBtn, selectImages;
    List<String> duplicateImages;
    TextView noFound, textView;
    ProgressDialog progressDialog;
    TextView permissionReq;
    String selectBtn="";
    String selectBtnCncl="";
    Button requestPermissionBtn;

    AlertDialog alertDialog;
    RelativeLayout layout;
    BackTrackUtils backTrackUtils;
    boolean isScan=false;
          Boolean inProgress=false;


    Admenager admenager;
    int backCount;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2296){
            if (SDK_INT>=Build.VERSION_CODES.R){
                if (Environment.isExternalStorageManager()){
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    permissionReq.setVisibility(View.GONE);
                    requestPermissionBtn.setVisibility(View.GONE);
                  new MyTask().execute();

                }else {
                    Toast.makeText(this, "Allow permission for storage access", Toast.LENGTH_SHORT).show();
                    permissionReq.setVisibility(View.VISIBLE);
                    requestPermissionBtn.setVisibility(View.VISIBLE);
                    requestPermissionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermission(DuplicatePhotos.this);
                        }
                    });

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
                        progressBar.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                        permissionReq.setVisibility(View.GONE);
                        requestPermissionBtn.setVisibility(View.GONE);
                           new MyTask().execute();
                    }else {
                        Toast.makeText(this, "Allow permission for storage acces", Toast.LENGTH_SHORT).show();
                        permissionReq.setVisibility(View.VISIBLE);
                        requestPermissionBtn.setVisibility(View.VISIBLE);
                        requestPermissionBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestPermission(DuplicatePhotos.this);
                            }
                        });
                    }
                }
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
admenager=new Admenager(this,this);
admenager.load_InterstitialAd("admobe_interestitial_duplicate_back","admobe_interestitial_duplicate_back_test");
        setContentView(R.layout.activity_duplicate_photos);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Duplicate Photos");

        }
        backTrackUtils=new BackTrackUtils(this,this);
        recyclerView = findViewById(R.id.photosRcv);
        permissionReq=findViewById(R.id.permissionReq);
        progressBar=findViewById(R.id.progressBar);
        selectImages=findViewById(R.id.selectImages);
        delBtn=findViewById(R.id.delBtn);
        requestPermissionBtn=findViewById(R.id.requestPermission);
        noFound=findViewById(R.id.noFound);
        layout=findViewById(R.id.layout);
        textView=findViewById(R.id.tv);
         duplicateImages=new ArrayList<>();


        if(SDK_INT<Build.VERSION_CODES.M){
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            new MyTask().execute();
        }else {
            if (checkPermission()) {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                new MyTask().execute();

            } else {
                requestPermission(this);
            }
        }


        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedItemsIds.size()>0){

                    AlertDialog.Builder builder=new AlertDialog.Builder(DuplicatePhotos.this);
                    builder.setTitle("Delete images ");
                    builder.setMessage("Are you sure you want to delete this images");

                    builder.setPositiveButton("Delete Images", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteSelectedItems();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    builder.show();

                }else {
                    Toast.makeText(DuplicatePhotos.this, "Select atleast one image", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void deleteSelectedItems() {
      new DeleteImagesTask().execute();

    }

    private void getDuplicatePhoto1() {

      HashMap<String, List<String>> hashMap=new HashMap<>();
      ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.DATA};
        Cursor cursor=contentResolver.query(uri,projection,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                @SuppressLint("Range") String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                try {
                    String hash = getFileMD5ToString(path);

                    if (!hashMap.containsKey(hash)) {
                        hashMap.put(hash, new ArrayList<>());
                    }
                    hashMap.get(hash).add(path);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            cursor.close();
        }


        for (List<String> imagePaths: hashMap.values()){
            if (imagePaths.size()>1){
                duplicateImages.addAll(imagePaths);
            }
        }
        Log.d("rcv", String.valueOf(duplicateImages.size()));
        Log.d("rcv", duplicateImages.toString());




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

            ActivityCompat.requestPermissions(DuplicatePhotos.this, permission, 101);
        }

    }
public  class MyTask extends AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... voids) {
        isScan=true;
        getDuplicatePhoto1();

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        isScan=false;
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
       selectImages.setVisibility(View.VISIBLE);

        mSelectedItemsIds=new SparseBooleanArray();
        duplicatePhotoAdapter=new DuplicatePhotoAdapter();
        duplicatePhotoAdapter.setmSelectedItemsIds(mSelectedItemsIds);
        gridLayoutManager=new GridLayoutManager(DuplicatePhotos.this,2);

        recyclerView.setAdapter(duplicatePhotoAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (duplicateImages.size()>0){

            duplicatePhotoAdapter.setData(duplicateImages);

        }else {
            noFound.setVisibility(View.VISIBLE);

        }
    }
}

    @Override
    public void onBackPressed() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }




            final   AlertDialog.Builder alert=new AlertDialog.Builder(this);

            alert.setMessage("Are you sure you want to exist").setCancelable(false).setTitle("Scanning images");
            selectBtn="Yes";
            selectBtnCncl="No";


            alert.setPositiveButton(selectBtn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
                   backTrackUtils.backTrack();
                    admenager.showadmobeInterstitialAd();
                    DuplicatePhotos.this.finish();


                }
            });
            alert.setNegativeButton(selectBtnCncl, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog =alert.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setTextColor(ContextCompat.getColor(DuplicatePhotos.this, R.color.black));
                    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(ContextCompat.getColor(DuplicatePhotos.this, R.color.black));
                }
            });
            alertDialog.show();





    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class DeleteImagesTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DuplicatePhotos.this);
            progressDialog.setMessage("Deleting Images...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SparseBooleanArray copy = mSelectedItemsIds.clone();
            for (int i = (copy.size() - 1); i >= 0; i--) {
                if (copy.valueAt(i) && copy.keyAt(i) < duplicateImages.size()) {
                    String imagePath = duplicateImages.get(copy.keyAt(i));
                    File imageFile = new File(imagePath);

                    if (imageFile.delete()) {
                        ContentResolver contentResolver = getContentResolver();
                        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{imagePath});
                        duplicateImages.remove(imagePath);
                        duplicatePhotoAdapter.removeImage(imagePath);
                        mSelectedItemsIds.delete(copy.keyAt(i));
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            mSelectedItemsIds.clear();
            duplicatePhotoAdapter.notifyDataSetChanged();
        }
    }

}