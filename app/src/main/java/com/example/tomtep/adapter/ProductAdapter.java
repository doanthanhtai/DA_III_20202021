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
import com.example.tomtep.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final IClickItemProductListener iClickItemProductListener;

    public ProductAdapter(List<Product> products, IClickItemProductListener iClickItemProductListener) {
        this.products = products;
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
        Product product = products.get(position);
//        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (product == null) return;
        holder.tvMaSP.setText(product.getKey());
        holder.tvTenSP.setText(product.getName());
        holder.tvNCC.setText(product.getSupplier());
        holder.tvGiaNhap.setText(String.valueOf(product.getImportPrice()));
        holder.tvTonKho.setText(String.valueOf(product.getAmount()));
        holder.imgEnterQuantity.setOnClickListener(view -> iClickItemProductListener.onClickEnterQuatity(product));
        holder.itemProdcutForeGround.setOnClickListener(view -> iClickItemProductListener.onClickItemProduct(product));
        holder.itemProdcutForeGround.setOnLongClickListener(view -> iClickItemProductListener.onLongClickItemProduct(product));
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        }
        return 0;
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMaSP, tvTenSP, tvNCC, tvGiaNhap, tvTonKho;
        private final ImageButton imgEnterQuantity;
        public final LinearLayout itemProdcutForeGround, itemProductBackground;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSP = itemView.findViewById(R.id.itemproduct_tv_masp);
            tvTenSP = itemView.findViewById(R.id.itemproduct_tv_tensp);
            tvNCC = itemView.findViewById(R.id.itemproduct_tv_tenncc);
            tvGiaNhap = itemView.findViewById(R.id.itemproduct_tv_gianhap);
            tvTonKho = itemView.findViewById(R.id.itemproduct_tv_tonkho);
            imgEnterQuantity = itemView.findViewById(R.id.itemproduct_img_import);
            itemProdcutForeGround = itemView.findViewById(R.id.item_product_foreground);
            itemProductBackground = itemView.findViewById(R.id.item_product_background);
        }
    }
}
