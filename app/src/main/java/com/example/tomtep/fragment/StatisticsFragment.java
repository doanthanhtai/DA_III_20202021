package com.example.tomtep.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tomtep.R;
import com.example.tomtep.adapter.StatisticsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticsFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initView(view);
        setTitleForTabLayout();
        return view;
    }

    private void setTitleForTabLayout() {
        new TabLayoutMediator(tabLayout,viewPager2,(tab,position)->{
            if (position == 0) {
                tab.setText(getString(R.string.statistics_tab_invest));
            } else if(position == 1){
                tab.setText(getString(R.string.statistics_tab_product));
            } else {
                tab.setText(getString(R.string.statistics_tab_environment));
            }
        }).attach();
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.statistics_tablayout);
        viewPager2 = view.findViewById(R.id.statistics_viewpager2);
        StatisticsViewPagerAdapter statisticsViewPagerAdapter = new StatisticsViewPagerAdapter(this);
        viewPager2.setAdapter(statisticsViewPagerAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Tai","Statistics");
        this.context = context;
    }
}