package com.example.tomtep.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tomtep.R;
import com.example.tomtep.model.Lake;

public class OtherUseHistoryFragment extends Fragment {
    private Lake lake;

    public OtherUseHistoryFragment(Lake lake) {
        this.lake = lake;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otherusehistory,container,false);
        return view;
    }
}
