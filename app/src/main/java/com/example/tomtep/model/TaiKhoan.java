package com.example.tomtep.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan implements Serializable {
    private static final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private static final List<Ao> aos = getDataAos();
    private static final List<SanPham> sanPhams = getDataSanPhams();

    private static final boolean daXoa = false;

    private static TaiKhoan inStance = new TaiKhoan();

    public static TaiKhoan getInstance() {
        if (inStance == null){
            inStance = new TaiKhoan();
        }
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
                    listAo.add(ao);
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
        List<SanPham> sanPhams = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        databaseReference.child(TaiKhoan.getInstance().getId()).child("sanPhams").addChildEventListener(new ChildEventListener() {
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
        return sanPhams;
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

    public List<SanPham> getSanPhams() {
        return sanPhams;
    }
}
