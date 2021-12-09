package com.example.tomtep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tomtep.fragment.DietFragment;
import com.example.tomtep.fragment.LakeFragmentMain;
import com.example.tomtep.fragment.ProductFragment;
import com.example.tomtep.fragment.StatisticsFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LakeFragmentMain();
            case 1:
                return new DietFragment();
            case 2:
                return new ProductFragment();
            default:
                return new StatisticsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
