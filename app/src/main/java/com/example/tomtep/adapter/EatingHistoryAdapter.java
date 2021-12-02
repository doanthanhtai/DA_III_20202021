package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemEatingHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.model.LichSuSuDungSanPham;

import java.util.List;

public class EatingHistoryAdapter extends RecyclerView.Adapter<EatingHistoryAdapter.EatingHisoryViewHolder> {
    private final List<LichSuSuDungSanPham> lichSuSuDungSanPhams;
    private final IClickItemEatingHistoryListener iClickItemEatingHistoryListener;

    public EatingHistoryAdapter(List<LichSuSuDungSanPham> lichSuSuDungSanPhams, IClickItemEatingHistoryListener iClickItemEatingHistoryListener) {
        this.lichSuSuDungSanPhams = lichSuSuDungSanPhams;
        this.iClickItemEatingHistoryListener = iClickItemEatingHistoryListener;
    }

    @NonNull
    @Override
    public EatingHisoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_eatinghistory, parent, false);
        return new EatingHisoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EatingHisoryViewHolder holder, int position) {
        LichSuSuDungSanPham lichSuSuDungSanPham = lichSuSuDungSanPhams.get(position);
        if (lichSuSuDungSanPham == null || !lichSuSuDungSanPham.getLichSuChoAns().get(0).isTonTai())
            return;
        String textProduct = lichSuSuDungSanPham.getSanPham().getMaSP() + "-" + lichSuSuDungSanPham.getSanPham().getTenSP();
        String strSoLuong = lichSuSuDungSanPham.getSoLuong() + " " + lichSuSuDungSanPham.getSanPham().getDonViDung();
        holder.tvProduct.setText(textProduct);
        holder.tvSoLuong.setText(strSoLuong);
        holder.tvTime.setText(lichSuSuDungSanPham.getThoiGianDung());
        holder.tvResult.setText(lichSuSuDungSanPham.getLichSuChoAns().get(0).getKetQua());
        holder.itemEatingHostoryForeground.setOnLongClickListener(v -> iClickItemEatingHistoryListener.onLongClickItemEatingHistory(lichSuSuDungSanPham));
    }

    @Override
    public int getItemCount() {
        return lichSuSuDungSanPhams.size();
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
