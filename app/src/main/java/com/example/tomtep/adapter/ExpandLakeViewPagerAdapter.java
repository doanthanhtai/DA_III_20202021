package com.example.tomtep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tomtep.fragment.EnvironmentHistoryFragment;
import com.example.tomtep.fragment.OtherUseHistoryFragment;
import com.example.tomtep.fragment.ProductHistoryFragment;
import com.example.tomtep.model.Lake;

public class ExpandLakeViewPagerAdapter extends FragmentStateAdapter {

    private Lake lake;
    public ExpandLakeViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Lake lake) {
        super(fragmentActivity);
        this.lake = lake;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductHistoryFragment(lake);
            case 1:
                return new OtherUseHistoryFragment(lake);
            default:
                return new EnvironmentHistoryFragment(lake);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
