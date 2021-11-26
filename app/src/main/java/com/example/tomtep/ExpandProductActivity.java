package com.example.tomtep;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemImportHistoryListener;
import com.example.tomtep.adapter.ImportHistoryAdapter;
import com.example.tomtep.dialog.UpdateImportHistoryDialog;
import com.example.tomtep.model.LichSuNhapHang;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpandProductActivity extends AppCompatActivity implements IClickItemImportHistoryListener {

    private ImportHistoryAdapter importHistoryAdapter;
    private RecyclerView rcvImportHistory;
    private List<LichSuNhapHang> lichSuNhapHangs;
    private Toolbar toolbar;
    private SanPham sanPham;
    private Context context;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_product);
        lichSuNhapHangs = new ArrayList<>();
        this.context = this;
        sanPham = (SanPham) getIntent().getSerializableExtra("sanpham_from_productfragment");
        databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan/" + TaiKhoan.getInstance().getId() + "/sanPhams/" + sanPham.getId());
        getLichSuNhapHang();
        initView();
        setSwipeDeleteImportHistory();
        setEvent();
    }

    private void setEvent() {
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void setSwipeDeleteImportHistory() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                LichSuNhapHang lichSuNhapHang = lichSuNhapHangs.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(lichSuNhapHang.getThoiGianNhap() + lichSuNhapHang.getThoiGianCapNhat() + lichSuNhapHang.getSoLuong())
                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> databaseReference.child("lichSuNhapHangs").child(lichSuNhapHang.getId()).child("daXoa").setValue(true).addOnCompleteListener(task -> {
                            sanPham.setSoLuong(sanPham.getSoLuong() - lichSuNhapHang.getSoLuong());
                            databaseReference.child("soLuong").setValue(sanPham.getSoLuong()).addOnCompleteListener(v -> {
                                Toast.makeText(context, R.string.expandproduct_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            });
                        }))
                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                            importHistoryAdapter.notifyItemChanged(position);
                            dialogInterface.dismiss();
                        });
                builder.create().show();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((ImportHistoryAdapter.ImportHistoryViewHolder) viewHolder).itemImportHistoryForeground;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ImportHistoryAdapter.ImportHistoryViewHolder) viewHolder).itemImportHistoryForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ImportHistoryAdapter.ImportHistoryViewHolder) viewHolder).itemImportHistoryForeground;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((ImportHistoryAdapter.ImportHistoryViewHolder) viewHolder).itemImportHistoryForeground;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvImportHistory);
    }

    private void getLichSuNhapHang() {
        databaseReference.child("lichSuNhapHangs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuNhapHang lichSuNhapHang = snapshot.getValue(LichSuNhapHang.class);
                if (lichSuNhapHang == null) {
                    return;
                }
                if (!lichSuNhapHang.isDaXoa()) {
                    lichSuNhapHangs.add(0, lichSuNhapHang);
                    importHistoryAdapter.notifyItemChanged(0);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuNhapHang lichSuNhapHang = snapshot.getValue(LichSuNhapHang.class);
                if (lichSuNhapHang == null) {
                    return;
                }
                for (int i = lichSuNhapHangs.size() - 1; i >= 0; i--) {
                    if (lichSuNhapHangs.get(i).getId().equals(lichSuNhapHang.getId())) {
                        lichSuNhapHangs.set(i, lichSuNhapHang);
                        if (lichSuNhapHang.isDaXoa()) {
                            lichSuNhapHangs.remove(i);
                            importHistoryAdapter.notifyItemRemoved(i);
                            return;
                        }
                        importHistoryAdapter.notifyItemChanged(i);
                        return;
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LichSuNhapHang lichSuNhapHang = snapshot.getValue(LichSuNhapHang.class);
                if (lichSuNhapHang == null) {
                    return;
                }
                for (int i = lichSuNhapHangs.size() - 1; i >= 0; i--) {
                    if (lichSuNhapHangs.get(i).getId().equals(sanPham.getId())) {
                        lichSuNhapHangs.remove(i);
                        importHistoryAdapter.notifyItemChanged(i);
                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.expandproduct_toolbar);
        rcvImportHistory = findViewById(R.id.expandproduct_rcv);
        importHistoryAdapter = new ImportHistoryAdapter(lichSuNhapHangs, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvImportHistory.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvImportHistory.addItemDecoration(decoration);
        rcvImportHistory.setAdapter(importHistoryAdapter);

    }

    @Override
    public boolean onLongClickItemImportHistory(LichSuNhapHang lichSuNhapHang) {
        new UpdateImportHistoryDialog(this, sanPham, lichSuNhapHang).show();
        return true;
    }
}