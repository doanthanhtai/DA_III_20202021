package com.example.tomtep.fragment;

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

import com.example.tomtep.Interface.IClickItemDietListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.DietAdapter;
import com.example.tomtep.dialog.UpdateDietDialog;
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.CheDoAn;
import com.example.tomtep.model.SanPham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DietFragment extends Fragment implements IClickItemDietListener {

    private List<Ao> aos;
    private List<SanPham> sanPhams;
    private DietAdapter dietAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        aos = new ArrayList<>();
        sanPhams = new ArrayList<>();
        getDanhSachSanPham();
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
                    dietAdapter.notifyItemChanged(aos.size() - 1);
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
                            dietAdapter.notifyItemRemoved(i);
                            return;
                        }
                        dietAdapter.notifyItemChanged(i);
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
                        dietAdapter.notifyItemRemoved(i);
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

    private void getDanhSachSanPham() {
        String idCurrentUser = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        assert idCurrentUser != null;
        databaseReference.child(idCurrentUser).child("sanPhams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                assert sanPham != null;
                if (!sanPham.isDaXoa()) {
                    sanPhams.add(sanPham);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null) {
                    return;
                }
                for (int i = sanPhams.size() - 1; i >= 0; i--) {
                    if (sanPhams.get(i).getId().equals(sanPham.getId())) {
                        sanPhams.set(i, sanPham);
                        if (sanPham.isDaXoa()) {
                            sanPhams.remove(i);
                            return;
                        }
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null) {
                    return;
                }
                for (int i = sanPhams.size() - 1; i >= 0; i--) {
                    if (sanPhams.get(i).getId().equals(sanPham.getId())) {
                        sanPhams.remove(i);
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
        RecyclerView recyclerView = view.findViewById(R.id.diet_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        dietAdapter = new DietAdapter(aos, this);
        recyclerView.setAdapter(dietAdapter);
    }

    @Override
    public void onClick(CheDoAn cheDoAn) {

    }

    @Override
    public void onLongClick(Ao ao) {
        new UpdateDietDialog(requireContext(), ao, sanPhams).show();
    }
}