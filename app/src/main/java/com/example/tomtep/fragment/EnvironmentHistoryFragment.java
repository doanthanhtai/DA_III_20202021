package com.example.tomtep.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomtep.R;
import com.example.tomtep.model.Lake;

public class EnvironmentHistoryFragment extends Fragment {

    private Lake lake;

    public EnvironmentHistoryFragment(Lake lake) {
        this.lake = lake;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_environment_history, container, false);
        return view;
    }
}