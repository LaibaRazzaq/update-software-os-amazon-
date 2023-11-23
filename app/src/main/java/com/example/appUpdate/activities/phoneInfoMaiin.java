package com.example.appUpdate.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.CustomFeedBack.FeedBack;
import com.example.appUpdate.R;
import com.example.appUpdate.adapters.viewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class phoneInfoMaiin extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy {
      TabLayout tabLayout;
      ViewPager2 viewPager2;
    Admenager admenager;
      ArrayList<String> titles;
      BackTrackUtils backTrackUtils;
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
        setContentView(R.layout.activity_phone_info_maiin);
        admenager=new Admenager(this,this);
        backTrackUtils=new BackTrackUtils(this,this);
        admenager.load_InterstitialAd("admobe_interestitial_phone_info_back","admobe_interestitial_phone_info_back_test");
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setElevation(0);
            actionBar.setTitle("Phone Info");

            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);


        titles=new ArrayList<>();
        titles.add("Phone Info");
        titles.add("Sim Info");
        setViewPAgerAdapter();
        new TabLayoutMediator(tabLayout,viewPager2,this).attach();




    }

    private void setViewPAgerAdapter() {
        viewPagerAdapter viewPagerAdapter=new viewPagerAdapter(this);
        ArrayList<Fragment> fragments=new ArrayList<>();
        fragments.add(new informationPhone());
        fragments.add(new SimInfo());
        viewPagerAdapter.setData(fragments);
        viewPager2.setAdapter(viewPagerAdapter);


    }

    @Override
    public void onConfigureTab(@NonNull  TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.simInfo);
        if (fragment!=null){
            fragment.onActivityResult(requestCode,resultCode,data);

        }
    }

    @Override
    public void onBackPressed()
    {


     backTrackUtils.backTrack();

        admenager.showadmobeInterstitialAd();
        super.onBackPressed();
    }
}