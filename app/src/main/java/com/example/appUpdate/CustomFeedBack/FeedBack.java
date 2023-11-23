package com.example.appUpdate.CustomFeedBack;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.appUpdate.BuildConfig;
import com.example.appUpdate.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


public class FeedBack implements View.OnClickListener {

    AlertDialog p1,p2,p3,p4;
    boolean sub=false;

    Button yes, no_really;
    Context context;
    LinearLayout ratus,attachFile, attachFile1;

    RatingBar ratingBar;
    View panel1,panel2,panel3,panel4;
    Activity activity;
    EditText feedBackText;
    EditText feedBackText1;
    ArrayList<Uri> uriArrayList1;

    ChipGroup hashTagChipGroup,dynamicChipGroup;
    ChipGroup hashTagChipGroup1,dynamicChipGroup1;
    MaterialButton submit,submit1;
    Uri imageUri;
    ArrayList<Uri> uriArrayList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String displayName;
    boolean p1Shown,p2Shown,p3Shown;
    ImageView closePannel1,closePannel2,closePannel3,closePannel4;
    int sesion, backPressCount;

    public FeedBack(Context context, Activity activity) {
        this.context = context;
        this.activity=activity;


        panel1= LayoutInflater.from(context).inflate(R.layout.feedback_custom_alert_box, null);
        panel2  =LayoutInflater.from(context).inflate(R.layout.custom_alert_box,null);
        panel3 =LayoutInflater.from(context).inflate(R.layout.feedback_alert,null);
        panel4 =LayoutInflater.from(context).inflate(R.layout.feedback_alert,null);
        sharedPreferences=context.getSharedPreferences("appReview",Context.MODE_PRIVATE);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        initView();

    }

    private void initView() {
        yes=panel1.findViewById(R.id.yes);
        no_really=panel1.findViewById(R.id.notReally);
        ratus=panel2.findViewById(R.id.rateUs);
        ratingBar=panel2.findViewById(R.id.rating_bar);
        feedBackText=panel3.findViewById(R.id.feeBacktext);
        submit=panel3.findViewById(R.id.submit);

        hashTagChipGroup=panel3.findViewById(R.id.chipGroup);
        dynamicChipGroup=panel3.findViewById(R.id.dynamicChipGroup);

        feedBackText1=panel4.findViewById(R.id.feeBacktext);
        submit1=panel4.findViewById(R.id.submit);

        hashTagChipGroup1=panel4.findViewById(R.id.chipGroup);
        dynamicChipGroup1=panel4.findViewById(R.id.dynamicChipGroup);
        closePannel1=panel1.findViewById(R.id.closePannel1);
        closePannel2=panel2.findViewById(R.id.closePannel2);
        closePannel3=panel3.findViewById(R.id.closePannel3);
        closePannel4=panel4.findViewById(R.id.closePannel3);
        attachFile=panel3.findViewById(R.id.attachFile);
        attachFile1=panel4.findViewById(R.id.attachFile);



        attachFile.setOnClickListener(this);
        attachFile1.setOnClickListener(this);
        submit1.setOnClickListener(this);

        submit.setOnClickListener(this);

        ratus.setOnClickListener(this);
        yes.setOnClickListener(this);
        no_really.setOnClickListener(this);
        closePannel3.setOnClickListener(this);
        closePannel1.setOnClickListener(this);
        closePannel2.setOnClickListener(this);
        closePannel4.setOnClickListener(this);



    }


    public void appReview(int sesin, int backPressCunt){


      p1Shown=sharedPreferences.getBoolean("p1Shown"+sesin+backPressCunt,false);

if (!p1Shown) {
    ViewGroup viewGroup = (ViewGroup) panel1.getParent();
    if (viewGroup != null) {
        p1.dismiss();
        viewGroup.removeView(panel1);
    }
    AlertDialog.Builder newAlertBuilder = new AlertDialog.Builder(context); // Create a new AlertDialog.Builder

    newAlertBuilder.setView(panel1);
    p1 = newAlertBuilder.create();
    p1.show();

    p1.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            p1.dismiss();
            sesion=sharedPreferences.getInt("appSession",1);
            backPressCount=sharedPreferences.getInt("backtrack",0);
            editor=sharedPreferences.edit();
            editor.putBoolean("p1Shown"+sesion+backPressCount, true);
            editor.apply();
            //activity.finish();
        }
    });


}

    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder newAlertBuilder = new AlertDialog.Builder(context); // Create a new AlertDialog.Builder

        switch (view.getId()){
            case R.id.yes:
                sesion=sharedPreferences.getInt("appSession",1);
                backPressCount=sharedPreferences.getInt("backtrack",0);
                editor=sharedPreferences.edit();
                editor.putBoolean("p1Shown"+sesion+backPressCount, true);
                editor.apply();
                   p1.dismiss();
                   ViewGroup viewGroup= (ViewGroup) panel2.getParent();
                   if (viewGroup!=null){

                       viewGroup.removeView(panel2);
                   }
                newAlertBuilder.setView(panel2);
                p2=newAlertBuilder.create();


                p2.show();
                p2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        p2.dismiss();
                      ///  activity.finish();
                    }
                });
                break;
            case R.id.notReally:
                sesion=sharedPreferences.getInt("appSession",1);
                backPressCount=sharedPreferences.getInt("backtrack",0);
                editor=sharedPreferences.edit();
                editor.putBoolean("p1Shown"+sesion+backPressCount, true);
                editor.apply();
                   p1.dismiss();
                ViewGroup viewGroup1= (ViewGroup) panel2.getParent();
                if (viewGroup1!=null){
                    viewGroup1.removeView(panel2);
                }
                   createFeedBackPanel();
                break;
                case R.id.rateUs:
                    p2.dismiss();
                    float  rating=ratingBar.getRating();
                    if (rating<3){
                        p2.dismiss();
                       createFeedBackPanel();
                    }else {
                        p2.dismiss();
                        try {
                            activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName())),22);


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    break;
            case R.id.attachFile:

                if (Build.VERSION.SDK_INT==Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        openGallery();

                    }
                }else {
                    openGallery();
                }
                break;
            case R.id.submit:

                openGmail();
                break;
            case R.id.closePannel1:
                p1.dismiss();
                sesion=sharedPreferences.getInt("appSession",1);
                backPressCount=sharedPreferences.getInt("backtrack",0);
                editor=sharedPreferences.edit();
                editor.putBoolean("p1Shown"+sesion+backPressCount, true);
                editor.apply();
                break;
            case R.id.closePannel2:
                p2.dismiss();
                break;
            case R.id.closePannel3:
               p3.dismiss();

               // p3.dismiss();


               // activity.finish();
                break;






        }


    }

    private void openGmail() {
        String edit=feedBackText.getText().toString();

        if(TextUtils.isEmpty(edit)|| edit.length()<6){
            Toast.makeText(context, "Character must be greater than 6", Toast.LENGTH_SHORT).show();
        }else {

            List<String> selectedChipTexts = getSelectedChipTexts(hashTagChipGroup);
            String chipString = TextUtils.join(", ", selectedChipTexts);
            String bodyText = chipString + "\n" + edit;
            Log.d("text", edit);

            Intent sE = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sE.setType("image/jpeg");
            sE.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback.nextsalution@gmail.com"});
            sE.putExtra(Intent.EXTRA_TEXT, bodyText);
            sE.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name) + BuildConfig.VERSION_NAME);
            sE.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
            sE.setPackage("com.google.android.gm");


            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(sE, 0);
            boolean isGmailInstalled = !activities.isEmpty();

            if (isGmailInstalled) {
                p3.dismiss();
                activity.startActivityForResult(sE,22);
            } else {
              p3.dismiss();

                Toast.makeText(context, "Gmail app is not installed", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private List<String> getSelectedChipTexts(ChipGroup chipGroup) {
        List<String> selectedChipTexts = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                String chipText = chip.getText().toString();
                Toast.makeText(context, "" + chipText, Toast.LENGTH_SHORT).show();
                selectedChipTexts.add(chipText);
            }
        }
        return selectedChipTexts;
    }

    @SuppressLint({"Range"})
    public void onActivityResult(int requestCode, int resultCode, Intent data){

      if(uriArrayList==null){
          uriArrayList=new ArrayList<>();
      }

        if( requestCode==41 &&resultCode==Activity.RESULT_OK){

                if (data!=null){

                    imageUri=data.getData();
                    uriArrayList.add(imageUri);

                    Cursor cursor=context.getContentResolver().query(imageUri, null,null,null,null);
                    if (cursor!=null && cursor.moveToFirst()){
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Chip chip = new Chip(dynamicChipGroup.getContext());
                        String fileNameWithoutExtension = displayName.substring(0, displayName.lastIndexOf("."));
                        String fileExtension = displayName.substring(displayName.lastIndexOf(".") + 1);

                        String chipText;
                        if (fileNameWithoutExtension.length() >= 3) {
                            chipText = fileNameWithoutExtension.substring(0, 3) + displayName.charAt(fileNameWithoutExtension.length()) + "." + fileExtension;
                        } else {
                            chipText = displayName;
                        }
                        chip.setText(chipText);

                        chip.setCloseIconVisible(true);;
                        chip.setCloseIcon(context.getResources().getDrawable(R.drawable.baseline_clear_24));
                        chip.setChipBackgroundColorResource(R.color.purple_700);
                        chip.setCloseIconTintResource(R.color.white);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dynamicChipGroup.removeView(chip);
                                uriArrayList.remove(imageUri);
                            }
                        });
                        dynamicChipGroup.addView(chip);


                    }
                    if (cursor!=null){
                        cursor.close();
                    }







                }

            }

            else if (requestCode==22){
            //  activity.finish();
            editor=sharedPreferences.edit();
            editor.putBoolean("isAlreadyAppReview", false);
            editor.apply();

        }else if (requestCode==1) {
            if (resultCode == RESULT_OK) {
                openGallery();
            } else {
                Toast.makeText(context, "Permission Required", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void createFeedBackPanel(){

     ViewGroup viewGroup= (ViewGroup) panel3.getParent();
     if (viewGroup!=null){
         viewGroup.removeView(panel3);
     }
        AlertDialog.Builder newAlertBuilder = new AlertDialog.Builder(context); // Create a new AlertDialog.Builder

        newAlertBuilder.setView(panel3);

        p3=newAlertBuilder.create();

        p3.show();

        p3.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                p3.dismiss();
              //  activity.finish();
            }
        });

    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            activity.startActivityForResult(galleryIntent, 41);
        }catch (Exception e){
            Toast.makeText(context, "Permission Required", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

    }
    private void createAlertDialog() {
        ViewGroup viewGroup= (ViewGroup) panel4.getParent();
        if (viewGroup!=null){
            viewGroup.removeView(panel4);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(panel4);
        p4 = builder.create();
        p4.show();

    }

    @SuppressLint({"Range", "UseCompatLoadingForDrawables"})
    private void updateAlertDialogWithImages() {

     uriArrayList1 =new ArrayList<>();
        for (Uri uri : uriArrayList) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
              uriArrayList1.add(uri);
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                // Rest of the chip creation code...
                Chip chip = new Chip(dynamicChipGroup.getContext());
                String fileNameWithoutExtension = displayName.substring(0, displayName.lastIndexOf("."));
                String fileExtension = displayName.substring(displayName.lastIndexOf(".") + 1);

                String chipText;
                if (fileNameWithoutExtension.length() >= 3) {
                    chipText = fileNameWithoutExtension.substring(0, 3) + displayName.charAt(fileNameWithoutExtension.length()) + "." + fileExtension;
                } else {
                    chipText = displayName;
                }
                chip.setText(chipText);

                chip.setCloseIconVisible(true);
                
                chip.setCloseIcon(context.getResources().getDrawable(R.drawable.baseline_clear_24));
                chip.setChipBackgroundColorResource(R.color.purple_700);
                chip.setCloseIconTintResource(R.color.white);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dynamicChipGroup.removeView(chip);
                        uriArrayList.remove(imageUri);
                    }
                });
                dynamicChipGroup.addView(chip);


            }


            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
