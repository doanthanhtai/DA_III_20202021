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
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragmentProduct extends Fragment {

    private PieChart pieTonKho, pieSuDung;
    private List<Product> products;
    private List<ProductHistory> productHistories;
    private ChildEventListener childEventListenerProductHistory;

    private ArrayList<PieEntry> pieEntriesTonKho, pieEntriesSuDung;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_product, container, false);
        products = new ArrayList<>();
        productHistories = new ArrayList<>();
        initView(view);
        intitChildEventListener();
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
        pieTonKho.clear();
        pieSuDung.clear();
        getEntries();

        //Sudung
        PieDataSet pieDataSetSuDung = new PieDataSet(pieEntriesSuDung, "");
        pieDataSetSuDung.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSetSuDung.setValueTextColor(Color.BLACK);
        pieDataSetSuDung.setValueTextSize(8f);

        PieData pieDataSuDung = new PieData(pieDataSetSuDung);
        pieSuDung.setData(pieDataSuDung);
        pieSuDung.getDescription().setEnabled(false);
        pieSuDung.setCenterText("Sủ dụng");
        pieSuDung.animate();

        //tonkho
        PieDataSet pieDataSetTonKho = new PieDataSet(pieEntriesTonKho, "");
        pieDataSetTonKho.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSetTonKho.setValueTextColor(Color.BLACK);
        pieDataSetTonKho.setValueTextSize(8f);

        PieData pieDataTonKho = new PieData(pieDataSetTonKho);
        pieTonKho.setData(pieDataTonKho);
        pieTonKho.getDescription().setEnabled(false);
        pieTonKho.setCenterText("Tồn kho");
        pieTonKho.animate();

        pieTonKho.setDrawEntryLabels(false);
        pieTonKho.notifyDataSetChanged();
        pieSuDung.setDrawEntryLabels(false);
        pieSuDung.notifyDataSetChanged();

    }

    private void getEntries() {
        pieEntriesSuDung = new ArrayList<>();
        pieEntriesTonKho = new ArrayList<>();
        //int i = 0;
        int useAmount;
        for (Product product : products) {
            useAmount = 0;
            for (ProductHistory productHistory : productHistories) {
                if (productHistory.getProductId().equals(product.getId())) {
                    useAmount += productHistory.getAmount();
                }
            }
            if (useAmount > 0) {
                pieEntriesSuDung.add(new PieEntry(useAmount, product.getKey() + "-" + product.getName()));
            }
            if (product.getAmount() > 0) {
                pieEntriesTonKho.add(new PieEntry(product.getAmount(), product.getKey() + "-" + product.getName()));
            }
            //  i++;
        }

    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Product").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null || product.isDeleted()) return;
                        products.add(product);
                        addChildEventListenerForProduct(product);
                        pieTonKho.notifyDataSetChanged();
                        pieSuDung.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (product.getId().equals(products.get(i).getId())) {
                                if (product.isDeleted()) {
                                    products.remove(i);
                                    removeChildEventListenerForProduct(product);
                                    pieSuDung.notifyDataSetChanged();
                                    pieTonKho.notifyDataSetChanged();
                                    return;
                                }
                                products.set(i, product);
                                pieSuDung.notifyDataSetChanged();
                                pieTonKho.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (product.getId().equals(products.get(i).getId())) {
                                products.remove(i);
                                removeChildEventListenerForProduct(product);
                                pieSuDung.notifyDataSetChanged();
                                pieTonKho.notifyDataSetChanged();
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

    private void addChildEventListenerForProduct(Product product) {
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("productId").equalTo(product.getId())
                .addChildEventListener(childEventListenerProductHistory);
    }

    private void removeChildEventListenerForProduct(Product product) {
        FirebaseDatabase.getInstance().getReference("ProductHistory").orderByChild("productId").equalTo(product.getId())
                .removeEventListener(childEventListenerProductHistory);
    }

    private void intitChildEventListener() {
        childEventListenerProductHistory = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                productHistories.add(productHistory);
                pieSuDung.notifyDataSetChanged();
                pieTonKho.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                for (int i = productHistories.size(); i >= 0; i--) {
                    if (productHistory.getId().equals(productHistories.get(i).getId())) {
                        if (productHistory.isDeleted()) {
                            productHistories.remove(i);
                            pieSuDung.notifyDataSetChanged();
                            pieTonKho.notifyDataSetChanged();
                            return;
                        }
                        productHistories.set(i, productHistory);
                        pieSuDung.notifyDataSetChanged();
                        pieTonKho.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ProductHistory productHistory = snapshot.getValue(ProductHistory.class);
                if (productHistory == null) return;
                for (int i = productHistories.size(); i >= 0; i--) {
                    if (productHistory.getId().equals(productHistories.get(i).getId())) {
                        productHistories.remove(i);
                        pieSuDung.notifyDataSetChanged();
                        pieTonKho.notifyDataSetChanged();
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

    private void initView(View view) {
        pieTonKho = view.findViewById(R.id.statisticsproduct_pie_tonkho);
        pieSuDung = view.findViewById(R.id.statisticsproduct_pie_sudung);

        Legend legend1 = pieSuDung.getLegend();
        legend1.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend1.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend1.setDrawInside(false);

        Legend legend2 = pieTonKho.getLegend();
        legend2.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend2.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend2.setDrawInside(false);
    }
}