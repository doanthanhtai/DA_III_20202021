package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemLakeListener;
import com.example.tomtep.R;
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.LichSuMoiTruong;

import java.util.ArrayList;
import java.util.List;

public class LakeAdapter extends RecyclerView.Adapter<LakeAdapter.LakeViewHolder> {
    private final List<Ao> aos;
    private final IClickItemLakeListener iClickItemLakeListener;

    public LakeAdapter(List<Ao> aos, IClickItemLakeListener iClickItemLakeListener) {
        this.aos = aos;
        this.iClickItemLakeListener = iClickItemLakeListener;
    }


    @NonNull
    @Override
    public LakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LakeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lake, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LakeViewHolder holder, int position) {
        Ao ao = aos.get(position);
        if (ao == null) {
            return;
        }

        holder.tvMaAo.setText(ao.getMaAo());
        holder.tvTenAo.setText(ao.getTenAo());
        holder.tvNgayTao.setText(ao.getNgayTao());
        holder.tvTrangThai.setText(getTrangThaiAo(ao.isTrangThai()));
        holder.tvChiPhi.setText(getChiPhiCuaAo(ao));
        holder.tvPH.setText(getChiSoMoiTruongMoiNhat(ao).get(0));
        holder.tvOxy.setText(getChiSoMoiTruongMoiNhat(ao).get(1));
        holder.tvDoMan.setText(getChiSoMoiTruongMoiNhat(ao).get(2));
        holder.relativeLayout.setOnClickListener(v -> iClickItemLakeListener.onClick(ao));
        holder.relativeLayout.setOnLongClickListener(view -> {
            iClickItemLakeListener.onLongClick(ao);
            return false;
        });
    }

    private String getChiPhiCuaAo(Ao ao) {
        return "120000000 đ";
    }

    private List<String> getChiSoMoiTruongMoiNhat(Ao ao) {
        List<String> listChiSo = new ArrayList<>();
        List<LichSuMoiTruong> lichSuMoiTruongs = ao.getLichSuMoiTruongs();
        if (lichSuMoiTruongs == null) {
            listChiSo.add(0, "7");
            listChiSo.add(1, "2 mg/l");
            listChiSo.add(2, "2 ‰");
            return listChiSo;
        }
        LichSuMoiTruong lichSuMoiTruong = lichSuMoiTruongs.get(lichSuMoiTruongs.size() - 1);
        listChiSo.add(0, String.valueOf(lichSuMoiTruong.getpH()));
        listChiSo.add(1, lichSuMoiTruong.getoXy() + " mg/l");
        listChiSo.add(2, lichSuMoiTruong.getDoMan() + " ‰");
        return listChiSo;
    }


    //Nếu ao có trạng thái là true thì ao hiện tại đang còn nuôi và ngược lại thì đã thu hoạch
    private String getTrangThaiAo(boolean trangThai) {
        if (trangThai) {
            return "Đã thu hoạch";
        }
        return "Đang nuôi";
    }

    @Override
    public int getItemCount() {
        if (aos != null) {
            return aos.size();
        }
        return 0;
    }

    public static class LakeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMaAo, tvTenAo, tvNgayTao, tvTrangThai, tvChiPhi, tvDoMan, tvPH, tvOxy;
        private final RelativeLayout relativeLayout;

        public LakeViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.item_lake);
            tvMaAo = itemView.findViewById(R.id.itemlake_tv_ma);
            tvTenAo = itemView.findViewById(R.id.itemlake_tv_ten);
            tvNgayTao = itemView.findViewById(R.id.itemlake_tv_ngaytao);
            tvTrangThai = itemView.findViewById(R.id.itemlake_tv_trangthai);
            tvChiPhi = itemView.findViewById(R.id.itemlake_tv_chiphi);
            tvDoMan = itemView.findViewById(R.id.itemlake_tv_doman);
            tvPH = itemView.findViewById(R.id.itemlake_tv_ph);
            tvOxy = itemView.findViewById(R.id.itemlake_tv_oxy);
        }

    }
}
