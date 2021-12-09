package com.example.tomtep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tomtep.fragment.LakeFragmentOne;
import com.example.tomtep.fragment.LakeFragmentTwo;

public class LakeViewPagerAdapter extends FragmentStateAdapter {
    public LakeViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LakeFragmentOne();
        }
        return new LakeFragmentTwo();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
