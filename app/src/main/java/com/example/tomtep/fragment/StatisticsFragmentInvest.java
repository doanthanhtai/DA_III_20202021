package com.example.tomtep.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.OtherUseHistory;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragmentInvest extends Fragment {
    private List<Lake> lakes;
    private List<Product> products;
    private List<OtherUseHistory> otherUseHistories;
    private List<ProductHistory> productHistories;
    private BarChart bcDauTu;
    private ChildEventListener childEventListenerOttherUseHistory, childEventListenerProductHistory;

    private ArrayList<BarEntry> barEntriesDauTu;
    private ArrayList<String> listLabels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_invest, container, false);
        lakes = new ArrayList<>();
        products = new ArrayList<>();
        otherUseHistories = new ArrayList<>();
        productHistories = new ArrayList<>();
        intitChildEventListener();
        initView(view);
        addChildEventListener();
        pinData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pinData();
    }

    private void pinData() {
        bcDauTu.clear();
        getEntries();

        BarDataSet barDataSetDauTu = new BarDataSet(barEntriesDauTu, "");
        barDataSetDauTu.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSetDauTu.setValueTextColor(Color.BLACK);
        barDataSetDauTu.setValueTextSize(8f);

        BarData barDataDauTu = new BarData(barDataSetDauTu);
        bcDauTu.setData(barDataDauTu);
        bcDauTu.getDescription().setEnabled(false);

        XAxis xAxis = bcDauTu.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(listLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setLabelCount(listLabels.size());
        bcDauTu.invalidate();

        bcDauTu.getAxisLeft().setDrawLabels(false);
        bcDauTu.getAxisRight().setDrawLabels(false);
        bcDauTu.getLegend().setEnabled(false);

        bcDauTu.notifyDataSetChanged();
    }

    private void getEntries() {
        barEntriesDauTu = new ArrayList<>();
        listLabels = new ArrayList<>();
        int useAmount;
        int i = 0;
        for (Lake lake : lakes) {
            useAmount = 0;
            for (ProductHistory productHistory : productHistories) {
                if (productHistory.getLakeId().equals(lake.getId())) {
                    Product product = getProductById(productHistory.getProductId());
                    if (product == null) continue;
                    useAmount += productHistory.getAmount() * product.getImportPrice();
                }
            }
            for (OtherUseHistory otherUseHistory : otherUseHistories) {
                if (otherUseHistory.getLakeId().equals(lake.getId())) {
                    useAmount += otherUseHistory.getCost();
                }
            }
            listLabels.add(lake.getKey() + "-" + lake.getName());
            barEntriesDauTu.add(new BarEntry(i, useAmount));
            i++;
        }
    }

    private Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private void initView(View view) {
        bcDauTu = view.findViewById(R.id.statisticsinvest_bc_dautu);
    }

    private void intitChildEventListener() {
        childEventListenerOttherUseHistory = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                if (otherUseHistory == null || otherUseHistory.isDeleted()) return;
                otherUseHistories.add(otherUseHistory);
                bcDauTu.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                if (otherUseHistory == null) return;
                for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
                    if (otherUseHistories.get(i).getId().equals(otherUseHistory.getId())) {
                        if (otherUseHistory.isDeleted()) {
                            otherUseHistories.remove(i);
                            bcDauTu.notifyDataSetChanged();
                            return;
                        }
                        otherUseHistories.set(i, otherUseHistory);
                        bcDauTu.notifyDataSetChanged();
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
                        bcDauTu.notifyDataSetChanged();
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
                bcDauTu.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                for (int i = productHistories.size() - 1; i >= 0; i--) {
                    if (productHistory.getId().equals(productHistories.get(i).getId())) {
                        if (productHistory.isDeleted()) {
                            productHistories.remove(i);
                            bcDauTu.notifyDataSetChanged();
                            return;
                        }
                        productHistories.set(i, productHistory);
                        bcDauTu.notifyDataSetChanged();
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
                        bcDauTu.notifyDataSetChanged();
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

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Lake").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null || lake.isDeleted()) return;
                        lakes.add(lake);
                        addChildEventListenerForLake(lake);
                        bcDauTu.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.set(i, lake);
                                if (lake.isDeleted()) {
                                    lakes.remove(i);
                                    bcDauTu.notifyDataSetChanged();
                                    return;
                                }
                                bcDauTu.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.remove(i);
                                removeChildEventListenerOfLake(lake);
                                bcDauTu.notifyDataSetChanged();
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
        FirebaseDatabase.getInstance().getReference("OtherUseHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerOttherUseHistory);
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerProductHistory);
    }

    private void addChildEventListenerForLake(Lake lake) {
        FirebaseDatabase.getInstance().getReference("OtherUseHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerOttherUseHistory);
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerProductHistory);
    }
}