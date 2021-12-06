package com.example.tomtep.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandLakeActivity;
import com.example.tomtep.Interface.IClickItemLakeListener;
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.adapter.LakeAdapter;
import com.example.tomtep.dialog.UpdateLakeDialog;
import com.example.tomtep.model.Lake;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LakeFragment extends Fragment implements IClickItemLakeListener {

    private List<Lake> lakes;
    private LakeAdapter lakeAdapter;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lake, container, false);
        lakes = new ArrayList<>();
        initView(view);
        addChildEventListener();
        return view;
    }

    @Override
    public void onClick(Lake lake) {
        startActivity(new Intent(getContext(), ExpandLakeActivity.class).putExtra("lake_from_lakefragment", lake));
    }

    @Override
    public boolean onLongClick(Lake lake) {
        new UpdateLakeDialog(context, lake).show();
        return true;
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Lake").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null || lake.isDeleted()) return;
                        lakes.add(lake);
                        lakeAdapter.notifyItemChanged(lakes.size());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.set(i, lake);
                                if (lake.isDeleted()) {
                                    lakes.remove(i);
                                }
                                lakeAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.remove(i);
                                lakeAdapter.notifyItemRemoved(i);
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
        RecyclerView rcvLake = view.findViewById(R.id.lake_rcv);
        lakeAdapter = new LakeAdapter(lakes, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvLake.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvLake.addItemDecoration(decoration);
        rcvLake.setAdapter(lakeAdapter);

    }
}