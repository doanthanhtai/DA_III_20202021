package com.example.tomtep.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemProductHistoryListener;
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.adapter.ProductHistoryAdapter;
import com.example.tomtep.dialog.NewProductHistoryDailog;
import com.example.tomtep.dialog.UpdateProductHistoryDialog;
import com.example.tomtep.model.FeedingHistory;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.sa90.materialarcmenu.ArcMenu;

import java.util.ArrayList;
import java.util.List;

public class ProductHistoryFragment extends Fragment implements IClickItemProductHistoryListener {

    private final Lake lake;
    private List<Product> products;
    private List<ProductHistory> productHistories;
    private List<FeedingHistory> feedingHistories;
    private ProductHistory productHistory;
    private RecyclerView rcvProductHistory;
    private ProductHistoryAdapter productHistoryAdapter;
    private Context context;
    private FloatingActionButton floatingProductHistory;

    public ProductHistoryFragment(Lake lake) {
        this.lake = lake;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producthistory, container, false);
        productHistories = new ArrayList<>();
        products = new ArrayList<>();
        feedingHistories = new ArrayList<>();
        initView(view);
        setEvent();
        addChildEventListener();
        setSwipeDeleteProductHistory();
        return view;
    }

    private void setEvent() {
        rcvProductHistory.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (i1 > i3) {
                floatingProductHistory.setVisibility(View.GONE);
            } else {
                floatingProductHistory.setVisibility(View.VISIBLE);
            }
        });

        floatingProductHistory.setOnClickListener(v->onClickAddNewProductHistory());
    }

    private void onClickAddNewProductHistory() {
        new NewProductHistoryDailog(context,products,lake).show();
    }

    private void initView(View view) {
        rcvProductHistory = view.findViewById(R.id.producthistory_rcv);
        productHistoryAdapter = new ProductHistoryAdapter(productHistories, products, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvProductHistory.setLayoutManager(linearLayoutManager);
        rcvProductHistory.setAdapter(productHistoryAdapter);
        floatingProductHistory = view.findViewById(R.id.producthistory_floating);
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Product").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        products.add(product);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (products.get(i).getId().equals(product.getId())) {
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
        FirebaseDatabase.getInstance().getReference("FeedingHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        FeedingHistory feedingHistory = snapshot.getValue(FeedingHistory.class);
                        if (feedingHistory == null || feedingHistory.isDeleted()) return;
                        feedingHistories.add(feedingHistory);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        FeedingHistory feedingHistory = snapshot.getValue(FeedingHistory.class);
                        if (feedingHistory == null) return;
                        for (int i = feedingHistories.size() - 1; i >= 0; i--) {
                            if (feedingHistory.getId().equals(feedingHistories.get(i).getId())) {
                                if (feedingHistory.isDeleted()) {
                                    feedingHistories.remove(i);
                                    return;
                                }
                                feedingHistories.set(i, feedingHistory);
                                return;
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
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        productHistory = snapshot.getValue(ProductHistory.class);
                        if (productHistory == null || productHistory.isDeleted()) return;
                        productHistories.add(0,productHistory);
                        productHistoryAdapter.notifyItemChanged(productHistories.size());

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        productHistory = snapshot.getValue(ProductHistory.class);
                        if (productHistory == null) return;
                        for (int i = productHistories.size() - 1; i >= 0; i--) {
                            if (productHistory.getId().equals(productHistories.get(i).getId())) {
                                if (productHistory.isDeleted()) {
                                    productHistories.remove(i);
                                    productHistoryAdapter.notifyItemRemoved(i);
                                    return;
                                }
                                productHistories.set(i, productHistory);
                                productHistoryAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        productHistory = snapshot.getValue(ProductHistory.class);
                        if (productHistory == null) return;
                        for (int i = productHistories.size() - 1; i >= 0; i--) {
                            if (productHistory.getId().equals(productHistories.get(i).getId())) {
                                productHistories.remove(i);
                                productHistoryAdapter.notifyItemRemoved(i);
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

    @Override
    public boolean onLongClick(ProductHistory productHistory) {
        new UpdateProductHistoryDialog(context, productHistory, getProductById(productHistory.getProductId()), getFeedingHistoryByProductHistoryId(productHistory.getId())).show();
        return true;
    }

    private void setSwipeDeleteProductHistory() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ProductHistory productHistory = productHistories.get(position);
                Product product = getProductById(productHistory.getProductId());
                assert product != null;
                String strMessage = product.getKey() + "-" + product.getName() + ": " + productHistory.getAmount() + product.getMeasure() + getText(R.string.all_message_confirmactiondeleteproducthistory) + lake.getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(strMessage)
                        .setNegativeButton(R.string.all_button_agree_text, (dialog, which) -> {
                            deleteProductHistory(productHistory);
                            Toast.makeText(getContext(), R.string.producthistoryfragmemt_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                            productHistoryAdapter.notifyItemRemoved(position);
                            dialog.dismiss();
                        })
                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                            productHistoryAdapter.notifyItemChanged(position);
                            dialogInterface.dismiss();
                        });
                builder.create().show();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((ProductHistoryAdapter.ProductHistoryViewHolder) viewHolder).itemProductHistoryForeGround;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ProductHistoryAdapter.ProductHistoryViewHolder) viewHolder).itemProductHistoryForeGround;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ProductHistoryAdapter.ProductHistoryViewHolder) viewHolder).itemProductHistoryForeGround;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((ProductHistoryAdapter.ProductHistoryViewHolder) viewHolder).itemProductHistoryForeGround;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvProductHistory);
    }

    private void deleteProductHistory(ProductHistory productHistory) {
        FirebaseDatabase.getInstance().getReference("ProductHistory").child(productHistory.getId()).child("deleted").setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Product product = getProductById(productHistory.getProductId());
                        if (product == null || product.isDeleted()) return;
                        FirebaseDatabase.getInstance().getReference("Product").child(product.getId()).child("amount")
                                .setValue(product.getAmount() + productHistory.getAmount());
                    }
                });
        for (int i = feedingHistories.size() - 1; i >= 0; i--) {
            if (feedingHistories.get(i).getProductHistoryId().equals(productHistory.getId())) {
                FirebaseDatabase.getInstance().getReference("FeedingHistory").child(feedingHistories.get(i).getId())
                        .child("deleted").setValue(true);
                feedingHistories.remove(i);
                return;
            }
        }
    }

    private Product getProductById(String productId) {
        if (products == null) return null;
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getId().equals(productId)) {
                return products.get(i);
            }
        }
        return null;
    }

    private FeedingHistory getFeedingHistoryByProductHistoryId(String productHistoryId) {
        for (int i = feedingHistories.size() - 1; i >= 0; i--) {
            if (productHistoryId.equals(feedingHistories.get(i).getProductHistoryId())) {
                return feedingHistories.get(i);
            }
        }
        return null;
    }
}
