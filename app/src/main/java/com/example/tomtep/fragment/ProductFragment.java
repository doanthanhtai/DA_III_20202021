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

import com.example.tomtep.Interface.IClickItemProductListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.ProductAdapter;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements IClickItemProductListener {
    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private List<SanPham> sanPhams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        sanPhams = new ArrayList<>();
        getDanhSachSanPham();
        initView(view);
        return view;
    }

    private void getDanhSachSanPham() {
        FirebaseDatabase.getInstance().getReference("TaiKhoan")
                .child(TaiKhoan.getInstance().getId())
                .child("sanPhams")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        SanPham sanPham = snapshot.getValue(SanPham.class);
                        if (sanPham == null) {
                            return;
                        }
                        if (!sanPham.isDaXoa()) {
                            sanPhams.add(sanPham);
                            productAdapter.notifyItemChanged(sanPhams.size() - 1);
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
                                    productAdapter.notifyItemChanged(i);
                                    return;
                                }
                                productAdapter.notifyItemChanged(i);
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
                                productAdapter.notifyItemChanged(i);
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
        rcvProduct = view.findViewById(R.id.product_rcv);
        productAdapter = new ProductAdapter(sanPhams, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvProduct.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(decoration);
        rcvProduct.setAdapter(productAdapter);
    }

    @Override
    public void onClickEnterQuatity(SanPham sanPham) {

    }

    @Override
    public void onClickItemProduct(SanPham sanPham) {

    }
}