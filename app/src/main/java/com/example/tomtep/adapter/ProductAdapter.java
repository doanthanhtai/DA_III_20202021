package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemProductListener;
import com.example.tomtep.R;
import com.example.tomtep.model.SanPham;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<SanPham> sanPhams;
    private final IClickItemProductListener iClickItemProductListener;

    public ProductAdapter(List<SanPham> sanPhams, IClickItemProductListener iClickItemProductListener) {
        this.sanPhams = sanPhams;
        this.iClickItemProductListener = iClickItemProductListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        if (sanPham == null) return;
        holder.tvMaSP.setText(sanPham.getMaSP());
        holder.tvTenSP.setText(sanPham.getTenSP());
        holder.tvNCC.setText(sanPham.getTenNCC());
        holder.tvGiaNhap.setText(String.valueOf(sanPham.getGiaNhap()));
        holder.tvTonKho.setText(String.valueOf(sanPham.getSoLuong()));
        holder.imgEnterQuantity.setOnClickListener(view -> iClickItemProductListener.onClickEnterQuatity(sanPham));
        holder.itemProdcut.setOnClickListener(view -> iClickItemProductListener.onClickItemProduct(sanPham));
        holder.itemProdcut.setOnLongClickListener(view -> iClickItemProductListener.onLongClickItemProduct(sanPham));
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMaSP, tvTenSP, tvNCC, tvGiaNhap, tvTonKho;
        private final ImageButton imgEnterQuantity;
        private final LinearLayout itemProdcut;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSP = itemView.findViewById(R.id.itemproduct_tv_masp);
            tvTenSP = itemView.findViewById(R.id.itemproduct_tv_tensp);
            tvNCC = itemView.findViewById(R.id.itemproduct_tv_tenncc);
            tvGiaNhap = itemView.findViewById(R.id.itemproduct_tv_gianhap);
            tvTonKho = itemView.findViewById(R.id.itemproduct_tv_tonkho);
            imgEnterQuantity = itemView.findViewById(R.id.itemproduct_img_enter);
            itemProdcut = itemView.findViewById(R.id.item_product);
        }
    }
}
