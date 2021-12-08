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
import com.example.tomtep.model.EnvironmentHistory;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.OtherUseHistory;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;

import java.text.DecimalFormat;
import java.util.List;

public class LakeAdapter extends RecyclerView.Adapter<LakeAdapter.LakeViewHolder> {
    private final List<Lake> lakes;
    private final List<EnvironmentHistory> environmentHistories;
    private final List<OtherUseHistory> otherUseHistories;
    private final List<ProductHistory> productHistories;
    private final List<Product> products;
    private final IClickItemLakeListener iClickItemLakeListener;
    private final String[] listCS = new String[3];
    private final DecimalFormat decimalFormat = new DecimalFormat("# đ");

    public LakeAdapter(List<Lake> lakes, List<EnvironmentHistory> environmentHistories, List<OtherUseHistory> otherUseHistories, List<ProductHistory> productHistories, List<Product> products, IClickItemLakeListener iClickItemLakeListener) {
        this.lakes = lakes;
        this.iClickItemLakeListener = iClickItemLakeListener;
        this.otherUseHistories = otherUseHistories;
        this.productHistories = productHistories;
        this.products = products;
        this.environmentHistories = environmentHistories;
    }

    @NonNull
    @Override
    public LakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lake, parent, false);
        return new LakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LakeViewHolder holder, int position) {
        Lake lake = lakes.get(position);
        if (lake == null) return;

        getEnvironmentHistoryLast(lake);

        holder.tvMaAo.setText(lake.getKey());
        holder.tvTenAo.setText(lake.getName());
        holder.tvNgayTao.setText(lake.getCreationTime());
        holder.tvTrangThai.setText(getTrangThaiAo(lake.isCondition()));
        holder.tvChiPhi.setText(getChiPhiCuaAo(lake));
        holder.tvPH.setText(listCS[0]);
        holder.tvOxy.setText(listCS[1]);
        holder.tvDoMan.setText(listCS[2]);
        holder.relativeLayout.setOnClickListener(v -> iClickItemLakeListener.onClick(lake));
        holder.relativeLayout.setOnLongClickListener(view -> iClickItemLakeListener.onLongClick(lake));
    }

    private void getEnvironmentHistoryLast(Lake lake) {
        EnvironmentHistory environmentHistory = null;
        for (EnvironmentHistory env : environmentHistories) {
            if (env.getLakeId().equals(lake.getId())) {
                environmentHistory = env;
            }
        }
        if (environmentHistory == null) {
            listCS[0] = "7";
            listCS[1] = "2 mg/l";
            listCS[2] = "2 ‰";
            return;
        }
        listCS[0] = String.valueOf(environmentHistory.getpH());
        listCS[1] = environmentHistory.getoXy() + " mg/l";
        listCS[2] = environmentHistory.getSalinity() + " ‰";
    }

    private String getChiPhiCuaAo(Lake lake) {
        double costUseProduct = 0.0F;
        ProductHistory productHistory;
        for (int i = productHistories.size() - 1; i >= 0; i--) {
            if (productHistories.get(i).getLakeId().equals(lake.getId())) {
                productHistory = productHistories.get(i);
                costUseProduct += getPriceProduct(productHistory.getProductId()) * productHistory.getAmount();
            }
        }

        double costOtherUse = 0.0F;
        for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
            if (otherUseHistories.get(i).getLakeId().equals(lake.getId())) {
                costOtherUse += otherUseHistories.get(i).getCost();
            }
        }
        return decimalFormat.format(costUseProduct + costOtherUse);
    }

    private float getPriceProduct(String productId) {
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getId().equals(productId)) {
                return products.get(i).getImportPrice();
            }
        }
        return 0.0f;
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
        if (lakes != null) {
            return lakes.size();
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
