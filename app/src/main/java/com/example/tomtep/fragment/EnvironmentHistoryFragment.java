package com.example.tomtep.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.R;
import com.example.tomtep.adapter.EnvironmentHistoryAdapter;
import com.example.tomtep.dialog.NewEnvironmentHistoryDailog;
import com.example.tomtep.model.EnvironmentHistory;
import com.example.tomtep.model.Lake;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentHistoryFragment extends Fragment {

    private final Lake lake;
    private Context context;
    private EnvironmentHistoryAdapter environmentHistoryAdapter;
    private FloatingActionButton floatingEnvironmentHistory;
    private List<EnvironmentHistory> environmentHistories;

    public EnvironmentHistoryFragment(Lake lake) {
        this.lake = lake;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_environment_history, container, false);
        environmentHistories = new ArrayList<>();
        initView(view);
        addChildEventListener();
        setEvent();
        return view;
    }

    private void setEvent() {
        floatingEnvironmentHistory.setOnClickListener(v -> insertEnvironmentHistory());
    }

    private void insertEnvironmentHistory() {
        EnvironmentHistory environmentHistory = null;
        if (environmentHistories.size() > 0){
            environmentHistory = environmentHistories.get(0);
        }
        new NewEnvironmentHistoryDailog(context, lake,environmentHistory).show();
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("EnvironmentHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                        if (environmentHistory == null) return;
                        environmentHistories.add(0, environmentHistory);
                        environmentHistoryAdapter.notifyItemInserted(0);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                        if (environmentHistory == null) return;
                        for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                            if (environmentHistory.getId().equals(environmentHistories.get(i).getId())) {
                                environmentHistories.set(i, environmentHistory);
                                environmentHistoryAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                        if (environmentHistory == null) return;
                        for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                            if (environmentHistory.getId().equals(environmentHistories.get(i).getId())) {
                                environmentHistories.remove(i);
                                environmentHistoryAdapter.notifyItemRemoved(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initView(View view) {
        floatingEnvironmentHistory = view.findViewById(R.id.environmenthistory_floating);
        RecyclerView rcvEnvironmentHistory = view.findViewById(R.id.environmenthistory_rcv);
        environmentHistoryAdapter = new EnvironmentHistoryAdapter(environmentHistories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvEnvironmentHistory.setLayoutManager(linearLayoutManager);
        rcvEnvironmentHistory.setAdapter(environmentHistoryAdapter);
        if (lake.isCondition()) floatingEnvironmentHistory.setVisibility(View.GONE);
    }
}