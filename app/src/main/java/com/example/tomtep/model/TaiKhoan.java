package com.example.tomtep.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaiKhoan implements Serializable {
    private static final String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private static final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private static final List<Ao> aos = getDataAos();
    private static final List<SanPham> sanPhams = getDataSanPhams();

    private static final boolean daXoa = false;

    private static final TaiKhoan inStance = new TaiKhoan();

    public static TaiKhoan getInstance() {
        return inStance;
    }

    private static List<Ao> getDataAos() {
        List<Ao> listAo = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(id).child("aos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                assert ao != null;
                if (!ao.isDaXoa()) {
                    listAo.add(snapshot.getValue(Ao.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                if (ao == null) {
                    return;
                }
                for (int i = listAo.size() - 1; i >= 0; i--) {
                    if (listAo.get(i).getId().equals(ao.getId())) {
                        listAo.set(i, ao);
                        if (ao.isDaXoa()) {
                            listAo.remove(i);
                            return;
                        }
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
                for (int i = listAo.size() - 1; i >= 0; i--) {
                    if (listAo.get(i).getId().equals(ao.getId())) {
                        listAo.remove(i);
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
        return listAo;
    }

    private static List<SanPham> getDataSanPhams() {
        List<SanPham> listSanPham = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(id).child("sanPhams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                assert ao != null;
                if (!ao.isDaXoa()) {
                    listSanPham.add(snapshot.getValue(SanPham.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sp = snapshot.getValue(SanPham.class);
                if (sp == null) {
                    return;
                }
                for (int i = listSanPham.size() - 1; i >= 0; i--) {
                    if (listSanPham.get(i).getId().equals(sp.getId())) {
                        listSanPham.set(i, sp);
                        if (sp.isDaXoa()) {
                            listSanPham.remove(i);
                            return;
                        }
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
                for (int i = listSanPham.size() - 1; i >= 0; i--) {
                    if (listSanPham.get(i).getId().equals(ao.getId())) {
                        listSanPham.remove(i);
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
        return listSanPham;
    }

    private TaiKhoan() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDaXoa() {
        return daXoa;
    }

    public List<Ao> getAos() {
        return aos;
    }

}
