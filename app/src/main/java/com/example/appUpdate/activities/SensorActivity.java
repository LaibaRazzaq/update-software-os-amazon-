package com.example.appUpdate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.CustomFeedBack.FeedBack;
import com.example.appUpdate.R;
import com.example.appUpdate.adapters.sensorAdapter;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    SensorManager sensorManager;

    RecyclerView recyclerView;
    sensorAdapter mSensorAdatper;
    TextView textView;
    FeedBack feedBack;

    boolean isAlreadyReview;
    BackTrackUtils backTrackUtils;
    SearchView searchView;
    int backCount;
    Admenager admenager;
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
        setContentView(R.layout.activity_sensor);
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_intertesial_senser","admobe_intertesial_senser_test");
        backTrackUtils=new BackTrackUtils(this,this);
        recyclerView=findViewById(R.id.rcv);
        textView=findViewById(R.id.tv);
        searchView=findViewById(R.id.search);


        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Mobile Sensor");

        }





        try {
            sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            List<Sensor> sensorList=sensorManager.getSensorList(Sensor.TYPE_ALL);
            textView.setText(sensorList.size()+" "+"Sensor in this device");
            String sensListName = "";
            String sensTypes="";
            Sensor temp;
            int x,i;
            for (i=0; i<=sensorList.size();i++){
                temp=sensorList.get(i);
                sensListName=""+sensListName+temp.getName();
                sensTypes=""+sensTypes+temp.getVersion();

//                sensList=""+sensList+temp.getType();




                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                mSensorAdatper=new sensorAdapter(SensorActivity.this, sensorList);
                recyclerView.setAdapter(mSensorAdatper);
                // ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.list_item,R.id.textView,sensorList);


            }
//            if (i>0){
//                sensList=getString(R.string.app_name)+":"+sensList;
//
//            }

        }catch (Exception e){
            Log.d("sensor", e.getMessage());
        }
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mSensorAdatper.getFilter().filter(s);
                mSensorAdatper.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        backTrackUtils.backTrack();

        admenager.showadmobeInterstitialAd();
        super.onBackPressed();

    }


}