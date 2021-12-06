package com.example.tomtep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tomtep.model.Lake;

public class ExpandLakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_lake);
        Lake lake = (Lake) getIntent().getSerializableExtra("lake_from_lakefragment");
    }
}