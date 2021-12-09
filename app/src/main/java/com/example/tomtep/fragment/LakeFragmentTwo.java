package com.example.tomtep.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandLakeActivity;
import com.example.tomtep.Interface.IClickItemLakeListener;
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.adapter.LakeAdapterTwo;
import com.example.tomtep.dialog.UpdateLakeDialog;
import com.example.tomtep.model.EnvironmentHistory;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.OtherUseHistory;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LakeFragmentTwo extends Fragment implements IClickItemLakeListener {

    private List<Lake> lakes;
    private List<EnvironmentHistory> environmentHistories;
    private List<OtherUseHistory> otherUseHistories;
    private List<ProductHistory> productHistories;
    private List<Product> products;
    private LakeAdapterTwo lakeAdapterTwo;
    private Context context;
    private ChildEventListener childEventListenerOttherUseHistory, childEventListenerProductHistory, childEventListenerEnvironment;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TOMTEP", "Lake: " + lakes.size());
        Log.e("TOMTEP", "Env: " + environmentHistories.size());
        Log.e("TOMTEP", "ProH: " + productHistories.size());
        Log.e("TOMTEP", "OtH: " + otherUseHistories.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lake_two, container, false);
        lakes = new ArrayList<>();
        environmentHistories = new ArrayList<>();
        otherUseHistories = new ArrayList<>();
        productHistories = new ArrayList<>();
        products = new ArrayList<>();
        intitChildEventListener();
        initView(view);
        addChildEventListener();
        return view;
    }

    private void intitChildEventListener() {
        childEventListenerOttherUseHistory = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                if (otherUseHistory == null || otherUseHistory.isDeleted()) return;
                otherUseHistories.add(otherUseHistory);
                notifiItemLakeByLakeId(otherUseHistory.getLakeId());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                if (otherUseHistory == null) return;
                for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
                    if (otherUseHistories.get(i).getId().equals(otherUseHistory.getId())) {
                        if (otherUseHistory.isDeleted()) {
                            otherUseHistories.remove(i);
                            notifiItemLakeByLakeId(otherUseHistory.getLakeId());
                            return;
                        }
                        otherUseHistories.set(i, otherUseHistory);
                        notifiItemLakeByLakeId(otherUseHistory.getLakeId());
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                if (otherUseHistory == null) return;
                for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
                    if (otherUseHistories.get(i).getId().equals(otherUseHistory.getId())) {
                        otherUseHistories.remove(i);
                        notifiItemLakeByLakeId(otherUseHistory.getLakeId());
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
        };
        childEventListenerEnvironment = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                environmentHistories.add(environmentHistory);
                notifiItemLakeByLakeId(environmentHistory.getLakeId());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                    if (environmentHistories.get(i).getLakeId().equals(environmentHistory.getLakeId())) {
                        environmentHistories.set(i, environmentHistory);
                        notifiItemLakeByLakeId(environmentHistory.getLakeId());
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                    if (environmentHistories.get(i).getLakeId().equals(environmentHistory.getLakeId())) {
                        environmentHistories.remove(i);
                        notifiItemLakeByLakeId(environmentHistory.getLakeId());
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
        };
        childEventListenerProductHistory = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null || productHistory.isDeleted()) return;
                productHistories.add(0, productHistory);
                notifiItemLakeByLakeId(productHistory.getLakeId());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                for (int i = productHistories.size() - 1; i >= 0; i--) {
                    if (productHistory.getId().equals(productHistories.get(i).getId())) {
                        if (productHistory.isDeleted()) {
                            productHistories.remove(i);
                            notifiItemLakeByLakeId(productHistory.getLakeId());
                            return;
                        }
                        productHistories.set(i, productHistory);
                        notifiItemLakeByLakeId(productHistory.getLakeId());
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                for (int i = productHistories.size() - 1; i >= 0; i--) {
                    if (productHistory.getId().equals(productHistories.get(i).getId())) {
                        productHistories.remove(i);
                        notifiItemLakeByLakeId(productHistory.getLakeId());
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
        };
    }

    @Override
    public void onClick(Lake lake) {
        startActivity(new Intent(getContext(), ExpandLakeActivity.class).putExtra("lake_from_lakefragment", lake));
    }

    @Override
    public boolean onLongClick(Lake lake) {
        new UpdateLakeDialog(context, lake).show();
        return true;
    }

    private void notifiItemLakeByLakeId(String lakeId) {
        for (int i = lakes.size() - 1; i >= 0; i--) {
            if (lakeId.equals(lakes.get(i).getId())) {
                lakeAdapterTwo.notifyItemChanged(i);
                return;
            }
        }
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Lake").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null || lake.isDeleted() || !lake.isCondition()) return;
                        lakes.add(lake);
                        addChildEventListenerForLake(lake);
                        lakeAdapterTwo.notifyItemChanged(lakes.size());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.set(i, lake);
                                if (lake.isDeleted() || !lake.isCondition()) {
                                    lakes.remove(i);
                                }
                                lakeAdapterTwo.notifyItemChanged(i);
                                return;
                            }
                        }
                        lakes.add(lake);
                        lakeAdapterTwo.notifyItemChanged(lakes.size());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.remove(i);
                                removeChildEventListenerOfLake(lake);
                                lakeAdapterTwo.notifyItemRemoved(i);
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

    }

    private void removeChildEventListenerOfLake(Lake lake) {
        FirebaseDatabase.getInstance().getReference("EnvironmentHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerEnvironment);
        FirebaseDatabase.getInstance().getReference("OtherUseHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerOttherUseHistory);
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerProductHistory);
    }

    private void addChildEventListenerForLake(Lake lake) {
        FirebaseDatabase.getInstance().getReference("EnvironmentHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerEnvironment);
        FirebaseDatabase.getInstance().getReference("OtherUseHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerOttherUseHistory);
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerProductHistory);
    }

    private void initView(View view) {
        RecyclerView rcvLake = view.findViewById(R.id.lake_rcv);
        lakeAdapterTwo = new LakeAdapterTwo(lakes, environmentHistories, otherUseHistories, productHistories, products, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvLake.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvLake.addItemDecoration(decoration);
        rcvLake.setAdapter(lakeAdapterTwo);
    }
}