package com.example.tomtep.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private final List<String> listTime;

    public TimeAdapter(List<String> listTime) {
        this.listTime = listTime;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        String strTime = listTime.get(position);
        if (strTime.isEmpty()) {
            return;
        }
        holder.tvTime.setText(strTime);
        holder.imgRemove.setOnClickListener(view -> {
            listTime.remove(position);
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final ImageButton imgRemove;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.itemtime_tv_time);
            imgRemove = itemView.findViewById(R.id.itemtime_img_remove);
        }
    }
}
