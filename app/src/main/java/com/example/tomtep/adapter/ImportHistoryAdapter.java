package com.example.tomtep.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemImportHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.model.ImportHistory;

import java.util.List;

public class ImportHistoryAdapter extends RecyclerView.Adapter<ImportHistoryAdapter.ImportHistoryViewHolder> {

    private final List<ImportHistory> importHistories;
    private final IClickItemImportHistoryListener iClickItemImportHistoryListener;

    public ImportHistoryAdapter(List<ImportHistory> importHistories, IClickItemImportHistoryListener iClickItemImportHistoryListener) {
        this.importHistories = importHistories;
        this.iClickItemImportHistoryListener = iClickItemImportHistoryListener;
    }

    @NonNull
    @Override
    public ImportHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_import_history, parent, false);
        return new ImportHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportHistoryViewHolder holder, int position) {
        ImportHistory importHistory = importHistories.get(position);
        if (importHistory == null) return;
        holder.tvNgayNhap.setText(importHistory.getImportTime());
        holder.tvNgayCapNhat.setText(importHistory.getUpdateTime());
        holder.tvSoLuong.setText(String.valueOf(importHistory.getAmount()));
        holder.itemImportHistoryForeground.setOnLongClickListener(view -> iClickItemImportHistoryListener.onLongClickItemImportHistory(importHistory));
    }

    @Override
    public int getItemCount() {
        return importHistories.size();
    }

    public static class ImportHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNgayNhap;
        private final TextView tvNgayCapNhat;
        private final TextView tvSoLuong;
        public final LinearLayout itemImportHistoryForeground;

        public ImportHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayNhap = itemView.findViewById(R.id.itemimporthistory_tv_thoigiannhap);
            tvNgayCapNhat = itemView.findViewById(R.id.itemimporthistory_tv_thoigiancapnhat);
            tvSoLuong = itemView.findViewById(R.id.itemimporthistory_tv_soluong);
            itemImportHistoryForeground = itemView.findViewById(R.id.item_importhistory_foreground);
        }
    }
}
