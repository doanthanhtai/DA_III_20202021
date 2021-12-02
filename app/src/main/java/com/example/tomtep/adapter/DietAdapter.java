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
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.CheDoAn;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder> {
    private final List<Ao> aos;
    private final IClickItemDietListener iClickItemDietListener;

    public DietAdapter(List<Ao> aos, IClickItemDietListener iClickItemDietListener) {
        this.aos = aos;
        this.iClickItemDietListener = iClickItemDietListener;
    }

    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DietViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_diet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder holder, int position) {
        Ao ao = aos.get(position);
        if (ao == null) {
            return;
        }
        CheDoAn cheDoAn = ao.getCheDoAn();

        holder.tvMaAo.setText(ao.getMaAo());
        holder.tvTrangThai.setText(getTrangThaiCheDoAn(cheDoAn.isTrangThai()));
        holder.tvTenSanPham.setText(cheDoAn.getSanPhamChoAn().getTenSP());
        holder.tvDemThoiGian.setText(cheDoAn.getThoiGianChoAn());
        if (cheDoAn.getKhungGioChoAn() != null) {
            holder.tvKhungGioAn.setText(cheDoAn.getKhungGioChoAn().toString());
        } else {
            holder.tvKhungGioAn.setText("[....]");
        }

        holder.switchFeed.setChecked(cheDoAn.isTrangThai());
        holder.relativeLayout.setOnClickListener(v -> iClickItemDietListener.onClick(ao));
        holder.relativeLayout.setOnLongClickListener(v -> {
            iClickItemDietListener.onLongClick(ao);
            return false;
        });
        holder.switchFeed.setOnClickListener(view -> iClickItemDietListener.onChoAn(ao,holder.switchFeed.isChecked()));
    }

    @Override
    public int getItemCount() {
        if (aos != null) {
            return aos.size();
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
