package com.example.appUpdate.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class viewPagerAdapter  extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;
    public viewPagerAdapter(@NonNull  FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull

    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
    public  void setData(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }
}
