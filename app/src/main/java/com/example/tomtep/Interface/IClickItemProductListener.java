package com.example.tomtep.Interface;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.model.SanPham;

public interface IClickItemProductListener {
    void onClickEnterQuatity(SanPham sanPham);

    void onClickItemProduct(SanPham sanPham);

    boolean onLongClickItemProduct(SanPham sanPham);
}
