package com.example.tomtep.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemFeedingHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.model.FeedingHistory;
import com.example.tomtep.model.Product;

import java.util.List;

public class FeedingHistoryAdapter extends RecyclerView.Adapter<FeedingHistoryAdapter.EatingHisoryViewHolder> {
    private final List<FeedingHistory> feedingHistories;
    private final IClickItemFeedingHistoryListener iClickItemFeedingHistoryListener;
    private final List<Product> products;

    public FeedingHistoryAdapter(List<FeedingHistory> feedingHistories,List<Product> products, IClickItemFeedingHistoryListener iClickItemFeedingHistoryListener) {
        this.feedingHistories = feedingHistories;
        this.iClickItemFeedingHistoryListener = iClickItemFeedingHistoryListener;
        this.products = products;
    }

    @NonNull
    @Override
    public EatingHisoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_feetinghistory, parent, false);
        return new EatingHisoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EatingHisoryViewHolder holder, int position) {
        FeedingHistory feedingHistory = feedingHistories.get(position);
        if (feedingHistory == null) return;
        holder.tvSoLuong.setText(String.valueOf(feedingHistory.getAmount()));
        holder.tvTime.setText(feedingHistory.getTime());
        holder.tvResult.setText(feedingHistory.getResult());
        holder.itemEatingHostoryForeground.setOnLongClickListener(v -> iClickItemFeedingHistoryListener.onLongClickItemFeedingHistory(feedingHistory));
        for (Product product : products){
            if (product.getId().equals(feedingHistory.getProductId())){
                holder.tvProduct.setText(product.getName());
                return;
            }
        }
    }



    @Override
    public int getItemCount() {
        return feedingHistories.size();
    }

    public static class EatingHisoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvProduct, tvResult, tvTime, tvSoLuong;
        public final LinearLayout itemEatingHostoryForeground;

        public EatingHisoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.itemeatinghistory_tv_product);
            tvResult = itemView.findViewById(R.id.itemeatinghistory_tv_result);
            tvTime = itemView.findViewById(R.id.itemeatinghistory_tv_thoigianchoan);
            tvSoLuong = itemView.findViewById(R.id.itemeatinghistory_tv_soluong);
            itemEatingHostoryForeground = itemView.findViewById(R.id.item_eatinghistory_foreground);
        }
    }
}
