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
import com.example.tomtep.model.ImportHistory;
import com.example.tomtep.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpandProductActivity extends AppCompatActivity implements IClickItemImportHistoryListener {

    private ImportHistoryAdapter importHistoryAdapter;
    private RecyclerView rcvImportHistory;
    private List<ImportHistory> importHistories;
    private Toolbar toolbar;
    private Product product;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_product);
        importHistories = new ArrayList<>();
        this.context = this;
        product = (Product) getIntent().getSerializableExtra("product_from_productfragment");
        getImportHistory();
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
                ImportHistory importHistory = importHistories.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(importHistory.getImportTime() + "-" + importHistory.getUpdateTime() + "\nSố lượng " +importHistory.getAmount())
                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference("ImportHistory").child(importHistory.getId()).child("deleted").setValue(true).addOnCompleteListener(task -> {
                            product.setAmount(product.getAmount() - importHistory.getAmount());
                            FirebaseDatabase.getInstance().getReference("Product").child(product.getId()).child("amount").setValue(product.getAmount()).addOnCompleteListener(v -> {
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

    private void getImportHistory() {
        FirebaseDatabase.getInstance().getReference("ImportHistory").orderByChild("productId").equalTo(product.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ImportHistory importHistory = snapshot.getValue(ImportHistory.class);
                if (importHistory == null) {
                    return;
                }
                if (!importHistory.isDeleted()) {
                    importHistories.add(0, importHistory);
                    importHistoryAdapter.notifyItemChanged(0);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ImportHistory importHistory = snapshot.getValue(ImportHistory.class);
                if (importHistory == null) return;
                for (int i = importHistories.size() - 1; i >= 0; i--) {
                    if (importHistories.get(i).getId().equals(importHistory.getId())) {
                        importHistories.set(i, importHistory);
                        if (importHistory.isDeleted()) {
                            importHistories.remove(i);
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
                ImportHistory importHistory = snapshot.getValue(ImportHistory.class);
                if (importHistory == null) return;
                for (int i = importHistories.size() - 1; i >= 0; i--) {
                    if (importHistories.get(i).getId().equals(importHistory.getId())) {
                        importHistories.remove(i);
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
        importHistoryAdapter = new ImportHistoryAdapter(importHistories, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvImportHistory.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvImportHistory.addItemDecoration(decoration);
        rcvImportHistory.setAdapter(importHistoryAdapter);

    }

    @Override
    public boolean onLongClickItemImportHistory(ImportHistory importHistory) {
        new UpdateImportHistoryDialog(this,product, importHistory).show();
        return true;
    }
}