package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemOtherUseHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.model.OtherUseHistory;
import com.example.tomtep.model.Product;

import java.util.List;

public class OtherUseHistoryAdapter extends RecyclerView.Adapter<OtherUseHistoryAdapter.OtherUseHistoryViewHolder> {
    private final List<OtherUseHistory> otherUseHistories;
    private final IClickItemOtherUseHistoryListener iClickItemOtherUseHistoryListener;

    public OtherUseHistoryAdapter(List<OtherUseHistory> otherUseHistories, IClickItemOtherUseHistoryListener iClickItemOtherUseHistoryListener) {
        this.otherUseHistories = otherUseHistories;
        this.iClickItemOtherUseHistoryListener = iClickItemOtherUseHistoryListener;
    }

    @NonNull
    @Override
    public OtherUseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_otherusehistory, parent, false);
        return new OtherUseHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherUseHistoryViewHolder holder, int position) {
        OtherUseHistory otherUseHistory = otherUseHistories.get(position);
        if (otherUseHistory == null) return;
        holder.tvName.setText(otherUseHistory.getName());
        holder.tvDescription.setText(otherUseHistory.getDescription());
        holder.tvUseTime.setText(otherUseHistory.getUseTime());
        holder.tvUpdateTime.setText(otherUseHistory.getUpdateTime());
        holder.tvCost.setText(String.valueOf(otherUseHistory.getCost()));
        holder.itemOtherUseHistoryForeGround.setOnLongClickListener(v -> iClickItemOtherUseHistoryListener.onLongClick(otherUseHistory));
    }

    @Override
    public int getItemCount() {
        if (otherUseHistories != null) return otherUseHistories.size();
        return 0;
    }

    public static class OtherUseHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName,tvDescription, tvUseTime,tvUpdateTime, tvCost;
        public final LinearLayout itemOtherUseHistoryForeGround;

        public OtherUseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_otherusehistory_tv_name);
            tvDescription = itemView.findViewById(R.id.item_otherusehistory_tv_description);
            tvUseTime = itemView.findViewById(R.id.item_otherusehistory_tv_usetime);
            tvUpdateTime = itemView.findViewById(R.id.item_otherusehistory_tv_updatetime);
            tvCost = itemView.findViewById(R.id.item_otherusehistory_tv_cost);
            itemOtherUseHistoryForeGround = itemView.findViewById(R.id.item_otherusehistory_foreground);
        }
    }
}
