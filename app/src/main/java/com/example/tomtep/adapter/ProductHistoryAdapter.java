package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemProductHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;

import java.util.List;

public class ProductHistoryAdapter extends RecyclerView.Adapter<ProductHistoryAdapter.ProductHistoryViewHolder> {
    private final List<ProductHistory> productHistories;
    private final List<Product> products;
    private final IClickItemProductHistoryListener iClickItemProductHistoryListener;

    public ProductHistoryAdapter(List<ProductHistory> productHistories, List<Product> products, IClickItemProductHistoryListener iClickItemProductHistoryListener) {
        this.productHistories = productHistories;
        this.products = products;
        this.iClickItemProductHistoryListener = iClickItemProductHistoryListener;
    }

    @NonNull
    @Override
    public ProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_producthistory, parent, false);
        return new ProductHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHistoryViewHolder holder, int position) {
        ProductHistory productHistory = productHistories.get(position);
        if (productHistory == null) return;
        Product product = getProductById(productHistory.getProductId());
        if (product == null) return;
        String productInfo = product.getKey() + "\n" + product.getName();
        holder.tvProductInfo.setText(productInfo);
        holder.tvTime.setText(productHistory.getUseTime());
        holder.tvAmount.setText(String.valueOf(productHistory.getAmount()));
        holder.itemProductHistoryForeGround.setOnLongClickListener(v -> iClickItemProductHistoryListener.onLongClick(productHistory));
    }

    @Override
    public int getItemCount() {
        if (productHistories != null) return productHistories.size();
        return 0;
    }

    public static class ProductHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvProductInfo, tvTime, tvAmount;
        public final LinearLayout itemProductHistoryForeGround;

        public ProductHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductInfo = itemView.findViewById(R.id.item_producthistory_tv_productinfo);
            tvTime = itemView.findViewById(R.id.item_producthistory_tv_time);
            tvAmount = itemView.findViewById(R.id.item_producthistory_tv_amount);
            itemProductHistoryForeGround = itemView.findViewById(R.id.item_producthistory_foreground);
        }
    }

    private Product getProductById(String productId) {
        if (products == null) return null;
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getId().equals(productId)) {
                return products.get(i);
            }
        }
        return null;
    }
}
