package com.example.tomtep;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tomtep.adapter.ExpandLakeViewPagerAdapter;
import com.example.tomtep.model.Lake;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ExpandLakeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private   Lake lake;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_lake);
        lake = (Lake) getIntent().getSerializableExtra("lake_from_lakefragment");
        initView();
        setEvent();
        settupToolbar();
        setTitleForTabLayout();
    }

    private void setEvent() {

    }

    private void settupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(lake.getKey() + " - " + lake.getName());
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.expandlake_toolbar);
        viewPager2 = findViewById(R.id.expandlake_viewpager2);
        tabLayout = findViewById(R.id.expandlake_tablayout);
        ExpandLakeViewPagerAdapter expandLakeViewPagerAdapter = new ExpandLakeViewPagerAdapter(this,lake);
        viewPager2.setAdapter(expandLakeViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expandlake_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.harvest_lake) {
            Toast.makeText(this, "Chưa configure chức năng này", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.delete_lake) {
            Toast.makeText(this, "Chưa configure chức năng này", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void setTitleForTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.expandlake_tab_producthistory));
                    break;
                case 1:
                    tab.setText(getString(R.string.expandlake_tab_otherusehistory));
                    break;
                default:
                    tab.setText(getString(R.string.expandlake_tab_environmenthistory));
                    break;
            }
        }).attach();
    }
}