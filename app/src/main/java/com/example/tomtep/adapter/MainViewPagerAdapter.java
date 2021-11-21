package com.example.tomtep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tomtep.fragment.DietFragment;
import com.example.tomtep.fragment.LakeFragment;
import com.example.tomtep.fragment.ProductFragment;
import com.example.tomtep.fragment.StatisticsFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {
    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DietFragment();
            case 2:
                return new ProductFragment();
            case 3:
                return new StatisticsFragment();
            default:
                return new LakeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
