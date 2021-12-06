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

import com.example.tomtep.Interface.IClickItemFeedingHistoryListener;
import com.example.tomtep.adapter.FeedingHistoryAdapter;
import com.example.tomtep.dialog.UpdateFeedingHistoryDialog;
import com.example.tomtep.model.FeedingHistory;
import com.example.tomtep.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpandDietActivity extends AppCompatActivity implements IClickItemFeedingHistoryListener {

    private String lakeId;
    private String accountId;
    private List<FeedingHistory> feedingHistories;
    private List<Product> products;
    private FeedingHistoryAdapter feedingHistoryAdapter;
    private RecyclerView rcvFeedingHistory;
    private Toolbar toolbar;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_diet);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        lakeId = intent.getAction();
        accountId = intent.getStringExtra("accountId");
        this.context = this;

        initView();
        setEvent();
        setSwipeDeleteItemEatingHistory();
        addChildEventListener();

    }

    private String getProductKeyById(String productId) {
        for (int i = products.size() - 1; i >= 0; i--) {
            if (productId.equals(products.get(i).getId())) {
                return products.get(i).getKey();
            }
        }
        return null;
    }

    private void setSwipeDeleteItemEatingHistory() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FeedingHistory feedingHistory = feedingHistories.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(getProductKeyById(feedingHistory.getProductId()) + "\n" + feedingHistory.getAmount() + " - " + feedingHistory.getTime())
                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                            FirebaseDatabase.getInstance().getReference("FeedingHistory").child(feedingHistory.getId()).child("deleted")
                                    .setValue(true).addOnCompleteListener(task -> {
                                Toast.makeText(context, R.string.expandproduct_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                                feedingHistoryAdapter.notifyItemChanged(position);
                                dialogInterface.dismiss();
                            });
                            updateProductAmount(feedingHistory.getProductId(), feedingHistory.getAmount());
                        })
                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                            feedingHistoryAdapter.notifyItemChanged(position);
                            dialogInterface.dismiss();
                        })
                        .setCancelable(false);
                builder.create().show();
            }

            private void updateProductAmount(String productId, float amount) {

                for (int i = products.size() - 1; i >= 0; i--) {
                    if (products.get(i).getId().equals(productId)) {
                        Product product = products.get(i);
                        FirebaseDatabase.getInstance().getReference("Product").child(productId).child("amount")
                                .setValue(product.getAmount() + amount);
                        return;
                    }
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((FeedingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((FeedingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((FeedingHistoryAdapter.EatingHisoryViewHolder) (viewHolder)).itemEatingHostoryForeground;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((FeedingHistoryAdapter.EatingHisoryViewHolder) viewHolder).itemEatingHostoryForeground;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvFeedingHistory);
    }

    private void setEvent() {
        toolbar.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.expanddiet_toolbar);
        rcvFeedingHistory = findViewById(R.id.expanddiet_rcv);
        products = new ArrayList<>();
        feedingHistories = new ArrayList<>();
        feedingHistoryAdapter = new FeedingHistoryAdapter(feedingHistories, products, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFeedingHistory.setLayoutManager(linearLayoutManager);
        rcvFeedingHistory.setAdapter(feedingHistoryAdapter);

    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Product").orderByChild("accountId").equalTo(accountId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null || product.isDeleted()) return;
                        products.add(product);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (products.get(i).getId().equals(product.getId())) {
                                if (product.isDeleted()) {
                                    products.remove(i);
                                    return;
                                }
                                products.set(i, product);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (products.get(i).getId().equals(product.getId())) {
                                products.remove(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("FeedingHistory").orderByChild("lakeId").equalTo(lakeId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        FeedingHistory feedingHistory = snapshot.getValue(FeedingHistory.class);
                        if (feedingHistory == null || feedingHistory.isDeleted()) return;
                        feedingHistories.add(0, feedingHistory);
                        feedingHistoryAdapter.notifyItemChanged(feedingHistories.size());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        FeedingHistory feedingHistory = snapshot.getValue(FeedingHistory.class);
                        if (feedingHistory == null) return;
                        for (int i = feedingHistories.size() - 1; i >= 0; i--) {
                            if (feedingHistory.getId().equals(feedingHistories.get(i).getId())) {
                                if (feedingHistory.isDeleted()) {
                                    feedingHistories.remove(i);
                                    feedingHistoryAdapter.notifyItemRemoved(i);
                                    return;
                                }
                                feedingHistories.set(i, feedingHistory);
                                feedingHistoryAdapter.notifyItemChanged(i);
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        FeedingHistory feedingHistory = snapshot.getValue(FeedingHistory.class);
                        if (feedingHistory == null) return;
                        for (int i = feedingHistories.size() - 1; i >= 0; i--) {
                            if (feedingHistory.getId().equals(feedingHistories.get(i).getId())) {
                                feedingHistories.remove(i);
                                feedingHistoryAdapter.notifyItemRemoved(i);
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
    public boolean onLongClickItemFeedingHistory(FeedingHistory feedingHistory) {
        new UpdateFeedingHistoryDialog(context, feedingHistory).show();
        return true;
    }
}