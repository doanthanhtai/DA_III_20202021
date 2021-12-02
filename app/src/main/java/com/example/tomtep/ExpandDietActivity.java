package com.example.tomtep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemEatingHistoryListener;
import com.example.tomtep.adapter.EatingHistoryAdapter;
import com.example.tomtep.model.LichSuSuDungSanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpandDietActivity extends AppCompatActivity implements IClickItemEatingHistoryListener {

    private String strAoId;
    private List<LichSuSuDungSanPham> lichSuSuDungSanPhams;
    private EatingHistoryAdapter eatingHistoryAdapter;
    private RecyclerView rcvEatingHistory;
    private Toolbar toolbar;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_diet);

        this.context = this;
        Intent intent = getIntent();
        if (intent != null) {
            strAoId = intent.getAction();
        }

        initView();
        setEvent();
        setSwipeDeleteItemEatingHistory();
        getLichSuSuDungSanPham();

    }

    private void setSwipeDeleteItemEatingHistory() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                LichSuSuDungSanPham lichSuSuDungSanPham = lichSuSuDungSanPhams.get(position)
//                AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                        .setTitle(R.string.all_title_dialogconfirmdelete)
//                        .setMessage(lichSuSuDungSanPham.getSanPham().getTenSP() + "\n" + lichSuSuDungSanPham.getSoLuong() + " - " + lichSuSuDungSanPham.getThoiGianDung())
//                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> databaseReference.child("lichSuNhapHangs").child(lichSuNhapHang.getId()).child("daXoa").setValue(true).addOnCompleteListener(task -> {
//                            sanPham.setSoLuong(sanPham.getSoLuong() - lichSuNhapHang.getSoLuong());
//                            databaseReference.child("soLuong").setValue(sanPham.getSoLuong()).addOnCompleteListener(v -> {
//                                Toast.makeText(context, R.string.expandproduct_toast_deletesuccess, Toast.LENGTH_SHORT).show();
//                                dialogInterface.dismiss();
//                            });
//                        }))
//                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
//                            importHistoryAdapter.notifyItemChanged(position);
//                            dialogInterface.dismiss();
//                        });
//                builder.create().show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((EatingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((EatingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((EatingHistoryAdapter.EatingHisoryViewHolder) (viewHolder)).itemEatingHostoryForeground;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((EatingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvEatingHistory);
    }

    private void setEvent() {
        toolbar.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.expanddiet_toolbar);
        rcvEatingHistory = findViewById(R.id.expanddiet_rcv);
        lichSuSuDungSanPhams = new ArrayList<>();
        eatingHistoryAdapter = new EatingHistoryAdapter(lichSuSuDungSanPhams, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvEatingHistory.setLayoutManager(linearLayoutManager);
        rcvEatingHistory.setAdapter(eatingHistoryAdapter);

    }

    private void getLichSuSuDungSanPham() {
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(TaiKhoan.getInstance().getId())
                .child("aos").child(strAoId).child("lichSuSuDungSanPhams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuSuDungSanPham lichSuSuDungSanPham = snapshot.getValue(LichSuSuDungSanPham.class);
                if (lichSuSuDungSanPham == null || lichSuSuDungSanPham.isDaXoa()) {
                    return;
                }
                lichSuSuDungSanPhams.add(0, lichSuSuDungSanPham);
                eatingHistoryAdapter.notifyItemChanged(lichSuSuDungSanPhams.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LichSuSuDungSanPham lichSuSuDungSanPham = snapshot.getValue(LichSuSuDungSanPham.class);
                if (lichSuSuDungSanPham == null) return;
                for (int i = lichSuSuDungSanPhams.size() - 1; i >= 0; i--) {
                    if (lichSuSuDungSanPham.getId().equals(lichSuSuDungSanPhams.get(i).getId())) {
                        if (lichSuSuDungSanPham.isDaXoa()) {
                            lichSuSuDungSanPhams.remove(i);
                            eatingHistoryAdapter.notifyItemRemoved(i);
                            return;
                        }
                        lichSuSuDungSanPhams.set(i, lichSuSuDungSanPham);
                        eatingHistoryAdapter.notifyItemChanged(i);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LichSuSuDungSanPham lichSuSuDungSanPham = snapshot.getValue(LichSuSuDungSanPham.class);
                if (lichSuSuDungSanPham == null) return;
                for (int i = lichSuSuDungSanPhams.size() - 1; i >= 0; i--) {
                    if (lichSuSuDungSanPham.getId().equals(lichSuSuDungSanPhams.get(i).getId())) {
                        lichSuSuDungSanPhams.remove(i);
                        eatingHistoryAdapter.notifyItemRemoved(i);
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

    @Override
    public boolean onLongClickItemEatingHistory(LichSuSuDungSanPham lichSuSuDungSanPham) {
        return true;
    }
}