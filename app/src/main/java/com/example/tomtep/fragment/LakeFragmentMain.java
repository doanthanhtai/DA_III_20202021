package com.example.tomtep.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tomtep.R;
import com.example.tomtep.adapter.LakeViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LakeFragmentMain extends Fragment {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lake_main, container, false);
        initView(view);
        setTitleForTabLayout();
        return view;
    }

    private void setTitleForTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText(getString(R.string.lake_tab_producthistory));
            } else {
                tab.setText(getString(R.string.lake_tab_environmenthistory));
            }
        }).attach();
    }

    private void initView(View view) {
        viewPager2 = view.findViewById(R.id.lake_viewpager2);
        tabLayout = view.findViewById(R.id.lake_tablayout);
        LakeViewPagerAdapter expandLakeViewPagerAdapter = new LakeViewPagerAdapter(this);
        viewPager2.setAdapter(expandLakeViewPagerAdapter);
    }
}