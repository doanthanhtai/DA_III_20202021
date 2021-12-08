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
        holder.tvProductName.setText(product.getName());
        holder.tvProductKey.setText(product.getKey());
        holder.tvUseTime.setText(productHistory.getUseTime());
        holder.tvUpdateTime.setText(productHistory.getUpdateTime());
        holder.tvAmount.setText(String.valueOf(productHistory.getAmount()));
        holder.itemProductHistoryForeGround.setOnLongClickListener(v -> iClickItemProductHistoryListener.onLongClick(productHistory));
    }

    @Override
    public int getItemCount() {
        if (productHistories != null) return productHistories.size();
        return 0;
    }

    public static class ProductHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvProductName, tvProductKey, tvUseTime, tvUpdateTime, tvAmount;
        public final LinearLayout itemProductHistoryForeGround;

        public ProductHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.item_producthistory_tv_productname);
            tvProductKey = itemView.findViewById(R.id.item_producthistory_tv_productkey);
            tvUseTime = itemView.findViewById(R.id.item_producthistory_tv_usetime);
            tvUpdateTime = itemView.findViewById(R.id.item_producthistory_tv_updatetime);
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
