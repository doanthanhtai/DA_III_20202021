package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemDietListener;
import com.example.tomtep.R;
import com.example.tomtep.model.Diet;
import com.example.tomtep.model.Lake;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder> {
    private final List<Lake> lakes;
    private final IClickItemDietListener iClickItemDietListener;

    public DietAdapter(List<Lake> lakes, IClickItemDietListener iClickItemDietListener) {
        this.lakes = lakes;
        this.iClickItemDietListener = iClickItemDietListener;
    }

    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DietViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_diet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder holder, int position) {
        Lake lake = lakes.get(position);
        Diet diet = lake.getDiet();
        if (diet == null) return;

        holder.tvMaAo.setText(lake.getKey());
        holder.tvTrangThai.setText(getTrangThaiCheDoAn(diet.isCondition()));
        holder.tvTenSanPham.setText(diet.getProductName());
        holder.tvDemThoiGian.setText(String.valueOf(diet.getTime()));
        if (diet.getFrame() != null) {
            holder.tvKhungGioAn.setText(diet.getFrame().toString());
        } else {
            holder.tvKhungGioAn.setText("[....]");
        }

        holder.switchFeed.setChecked(diet.isCondition());
        holder.relativeLayout.setOnClickListener(v -> iClickItemDietListener.onClick(diet));
        holder.relativeLayout.setOnLongClickListener(v -> {
            iClickItemDietListener.onLongClick(diet);
            return false;
        });
        holder.switchFeed.setOnClickListener(view -> {
            iClickItemDietListener.onClickFeeding(diet, holder.switchFeed.isChecked());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        if (lakes != null) {
            return lakes.size();
        }
        return 0;
    }

    public static class DietViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMaAo;
        private final TextView tvTrangThai;
        private final TextView tvTenSanPham;
        private final TextView tvKhungGioAn;
        private final TextView tvDemThoiGian;
        private final RelativeLayout relativeLayout;
        private final SwitchCompat switchFeed;

        public DietViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.item_diet);
            tvMaAo = itemView.findViewById(R.id.itemdiet_tv_ma);
            tvTrangThai = itemView.findViewById(R.id.itemdiet_tv_trangthai);
            tvTenSanPham = itemView.findViewById(R.id.itemdiet_tv_tensanpham);
            tvKhungGioAn = itemView.findViewById(R.id.itemdiet_tv_khunggioan);
            tvDemThoiGian = itemView.findViewById(R.id.itemdiet_tv_time);
            switchFeed = itemView.findViewById(R.id.itemdiet_switch_feed);
        }
    }

    private String getTrangThaiCheDoAn(boolean trangThai) {
        if (trangThai) {
            return "Đang cho ăn";
        }
        return "Đang không ăn";
    }
}
