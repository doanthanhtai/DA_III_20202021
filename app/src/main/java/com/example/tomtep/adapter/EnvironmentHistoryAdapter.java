package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.R;
import com.example.tomtep.model.EnvironmentHistory;

import java.util.List;

public class EnvironmentHistoryAdapter extends RecyclerView.Adapter<EnvironmentHistoryAdapter.EnvironmentHistoryViewHolder> {

    private final List<EnvironmentHistory> environmentHistories;

    public EnvironmentHistoryAdapter(List<EnvironmentHistory> environmentHistories) {
        this.environmentHistories = environmentHistories;
    }

    @NonNull
    @Override
    public EnvironmentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_environmenthistory, parent, false);
        return new EnvironmentHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnvironmentHistoryViewHolder holder, int position) {
        EnvironmentHistory environmentHistory = environmentHistories.get(position);
        if (environmentHistory == null) return;
        holder.tvPH.setText(String.valueOf(environmentHistory.getpH()));
        holder.tvOxy.setText(String.valueOf(environmentHistory.getoXy()));
        holder.tvSalinity.setText(String.valueOf(environmentHistory.getSalinity()));
        holder.tvUpdateTime.setText(environmentHistory.getUpdateTime());
    }

    @Override
    public int getItemCount() {
        if (environmentHistories != null) return environmentHistories.size();
        return 0;
    }

    protected static class EnvironmentHistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPH, tvOxy, tvSalinity, tvUpdateTime;
        public final LinearLayout itemEnvironmentHistoryForeGround;

        public EnvironmentHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPH = itemView.findViewById(R.id.item_environmenthistory_tv_ph);
            tvOxy = itemView.findViewById(R.id.item_environmenthistory_tv_oxy);
            tvSalinity = itemView.findViewById(R.id.item_environmenthistory_tv_salinity);
            tvUpdateTime = itemView.findViewById(R.id.item_environmenthistory_tv_time);
            itemEnvironmentHistoryForeGround = itemView.findViewById(R.id.item_environmenthistory_foreground);
        }
    }
}
