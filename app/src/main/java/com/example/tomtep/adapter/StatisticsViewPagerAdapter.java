package com.example.tomtep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tomtep.fragment.LakeFragmentOne;
import com.example.tomtep.fragment.LakeFragmentTwo;
import com.example.tomtep.fragment.StatisticsFragmentEnvironment;
import com.example.tomtep.fragment.StatisticsFragmentInvest;
import com.example.tomtep.fragment.StatisticsFragmentProduct;

public class StatisticsViewPagerAdapter extends FragmentStateAdapter {
    public StatisticsViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new StatisticsFragmentInvest();
        }
        if (position == 1){
            return new StatisticsFragmentProduct();
        }
        return new StatisticsFragmentEnvironment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
