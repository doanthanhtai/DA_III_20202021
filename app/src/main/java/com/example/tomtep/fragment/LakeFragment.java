package com.example.tomtep.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandLakeActivity;
import com.example.tomtep.Interface.IClickItemLakeListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.LakeAdapter;
import com.example.tomtep.dialog.UpdateLakeDialog;
import com.example.tomtep.model.Ao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LakeFragment extends Fragment implements IClickItemLakeListener {
    private LakeAdapter lakeAdapter;
    private List<Ao> aos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lake, container, false);

        aos = new ArrayList<>();
        getDanhSachAo();
        initView(view);

        return view;
    }

    private void getDanhSachAo() {
        String idCurrentUser = FirebaseAuth.getInstance().getUid();
        assert idCurrentUser != null;
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(idCurrentUser).child("aos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                assert ao != null;
                if (!ao.isDaXoa()) {
                    aos.add(snapshot.getValue(Ao.class));
                    lakeAdapter.notifyItemChanged(aos.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                if (ao == null) {
                    return;
                }
                for (int i = aos.size() - 1; i >= 0; i--) {
                    if (aos.get(i).getId().equals(ao.getId())) {
                        aos.set(i, ao);
                        if (ao.isDaXoa()) {
                            aos.remove(i);
                            lakeAdapter.notifyItemRemoved(i);
                            return;
                        }
                        lakeAdapter.notifyItemChanged(i);
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Ao ao = snapshot.getValue(Ao.class);
                if (ao == null) {
                    return;
                }
                for (int i = aos.size() - 1; i >= 0; i--) {
                    if (aos.get(i).getId().equals(ao.getId())) {
                        aos.remove(i);
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
        RecyclerView recyclerView = view.findViewById(R.id.lake_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        lakeAdapter = new LakeAdapter(aos, this);
        recyclerView.setAdapter(lakeAdapter);
    }

    @Override
    public void onClick(Ao ao) {
        Intent intent = new Intent(this.getContext(), ExpandLakeActivity.class);
        intent.putExtra("lake_from_lakefragment", ao);
        startActivity(intent);
    }

    @Override
    public void onLongClick(Ao ao) {
        new UpdateLakeDialog(requireContext(), ao).show();
    }

    @Override
    public void onDelete(Ao ao) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext())
                .setTitle(R.string.all_title_dialogconfirmdelete)
                .setMessage(getText(R.string.lakefragment_message_confirmdelete) + ao.getMaAo())
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    //   ao.xoaAo();
                    dialogInterface.dismiss();
                    Toast.makeText(this.getContext(), R.string.lakefragment_toast_deletesucces, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }
}