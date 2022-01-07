package com.example.tomtep.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandDietActivity;
import com.example.tomtep.Interface.IClickItemDietListener;
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.adapter.DietAdapter;
import com.example.tomtep.dialog.UpdateDietDialog;
import com.example.tomtep.model.Diet;
import com.example.tomtep.model.FeedingHistory;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.example.tomtep.service.DietReceiver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietFragment extends Fragment implements IClickItemDietListener {

    private DietAdapter dietAdapter;
    private List<Lake> lakes;
    private List<Product> products;
    private Context context;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diet, container, false);
        lakes = new ArrayList<>();
        products = new ArrayList<>();
        initView(view);
        addChildEventListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showTextViewEmpty();
    }

    private void showTextViewEmpty() {
        if (lakes.size() <= 0) {
            view.findViewById(R.id.diet_tv_empty).setVisibility(View.VISIBLE);
        }
        if (lakes.size() > 0) {
            view.findViewById(R.id.diet_tv_empty).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(Diet diet) {
        Intent intent = new Intent(context, ExpandDietActivity.class);
        intent.setAction(diet.getLakeId());
        intent.putExtra("accountId", MainActivity.MY_ACCOUNT.getId());
        startActivity(intent);
    }

    @Override
    public void onClickFeeding(Diet diet, boolean isChecked) {

        Product product = null;
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getId().equals(diet.getProductId())) {
                product = products.get(i);
                break;
            }
        }
        if (product == null) {
            Toast.makeText(context, R.string.dietfragment_toast_nonproduct, Toast.LENGTH_SHORT).show();
            return;
        }

        if (diet.getProductId().equals("default_product")) {
            Toast.makeText(context, getText(R.string.dietfragment_toast_dietinvalid), Toast.LENGTH_SHORT).show();
            return;
        }

        String lakeName = "";
        for (Lake lake : lakes) {
            if (lake.getId().equals(diet.getLakeId())) {
                lakeName = lake.getName();
                break;
            }
        }

        AlertDialog.Builder builder;
        if (isChecked) {
            if (product.getAmount() < diet.getAmount()) {
                Toast.makeText(context, "Sản phẩm hiện tại không còn đủ cho một lần ăn.", Toast.LENGTH_SHORT).show();
                return;
            }
            builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_dialogconfirm)
                    .setMessage(getText(R.string.dietfragment_message_confirm_turnon) + lakeName)
                    .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                        diet.setCondition(true);
                        FirebaseDatabase.getInstance().getReference("Lake")
                                .child(diet.getLakeId())
                                .child("diet")
                                .child("condition")
                                .setValue(true);
                        createProductHistory(diet);
                        createAlarm(diet, true);
                    })
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        } else {
            builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_dialogconfirm)
                    .setMessage(getText(R.string.dietfragment_message_confirm_turnoff) + lakeName)
                    .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                        FirebaseDatabase.getInstance().getReference("Lake")
                                .child(diet.getLakeId())
                                .child("diet")
                                .child("condition")
                                .setValue(false);
                        createAlarm(diet, false);
                    })
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        }
        builder.create().show();
    }

    private Product getProductById(String productId) {
        Product product = null;
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).getId().equals(productId)) {
                product = products.get(i);
                break;
            }
        }
        return product;
    }

    private void updateAmountProduct(Diet diet) {
        Product product = getProductById(diet.getProductId());
        if (product == null) return;
        product.setAmount(product.getAmount() - diet.getAmount());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        databaseReference.child(diet.getProductId())
                .child("amount")
                .setValue(product.getAmount());
    }

    private void createProductHistory(Diet diet) {
        String useTime = DateFormat.getInstance().format(Calendar.getInstance().getTime());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProductHistory");
        ProductHistory productHistory = new ProductHistory();
        productHistory.setId(databaseReference.push().getKey());
        productHistory.setLakeId(diet.getLakeId());
        productHistory.setProductId(diet.getProductId());
        productHistory.setUseTime(useTime);
        productHistory.setUpdateTime(useTime);
        productHistory.setAmount(diet.getAmount());
        productHistory.setDeleted(false);
        databaseReference.child(productHistory.getId()).setValue(productHistory).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateAmountProduct(diet);
                creatFeedingHistory(productHistory);
            }
        });
    }

    private void creatFeedingHistory(ProductHistory productHistory) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FeedingHistory");
        FeedingHistory feedingHistory = new FeedingHistory();
        feedingHistory.setId(databaseReference.push().getKey());
        feedingHistory.setLakeId(productHistory.getLakeId());
        feedingHistory.setProductId(productHistory.getProductId());
        feedingHistory.setProductHistoryId(productHistory.getId());
        feedingHistory.setAmount(productHistory.getAmount());
        feedingHistory.setTime(productHistory.getUseTime());
        feedingHistory.setResult(getString(R.string.dietfragment_result_default));
        feedingHistory.setDeleted(false);
        databaseReference.child(feedingHistory.getId()).setValue(feedingHistory);
    }

    @Override
    public void onLongClick(Diet diet) {
        new UpdateDietDialog(context, diet, products).show();
    }

    private void createAlarm(Diet diet, boolean trangThai) {

        String infoLake = "";
        for (Lake lake : lakes) {
            if (lake.getId().equals(diet.getLakeId())) {
                infoLake = lake.getKey() + " - " + lake.getName();
                break;
            }
        }

        Product product = getProductById(diet.getProductId());

        Intent intent = new Intent(context, DietReceiver.class);
        String strTitle = getString(R.string.dietfagment_notification_titile);
        String strContent = infoLake + "-" + diet.getProductName() + " : " + diet.getAmount() + " " + product.getMeasure();
        intent.putExtra("lake_id", diet.getLakeId());
        intent.putExtra("title", strTitle);
        intent.putExtra("content", strContent);
        intent.putExtra("account_id",MainActivity.MY_ACCOUNT.getId());
        intent.setAction("EATED");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getPositionLakeById(diet.getLakeId()), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (trangThai) {
            Toast.makeText(context, getText(R.string.dietfragment_toast_turnonsuccess), Toast.LENGTH_SHORT).show();
            long thoiGianAn = System.currentTimeMillis() + diet.getTime() * 60 * 1000L;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, thoiGianAn, pendingIntent);
        } else {
            Toast.makeText(context, getText(R.string.dietfragment_toast_turnoffsuccess), Toast.LENGTH_SHORT).show();
            alarmManager.cancel(pendingIntent);
            Intent i = new Intent(context, ExpandDietActivity.class);
            i.setAction(diet.getLakeId());
            startActivity(i);
        }
    }

    private int getPositionLakeById(String lakeId) {
        for (int i = lakes.size() - 1; i >= 0; i--) {
            if (lakes.get(i).getId().equals(lakeId)) {
                return i;
            }
        }
        return -1;
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Lake").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null || lake.isDeleted() || lake.isCondition()) return;
                        lakes.add(lake);
                        dietAdapter.notifyItemChanged(lakes.size());
                        showTextViewEmpty();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.set(i, lake);
                                if (lake.isDeleted() || lake.isCondition()) {
                                    lakes.remove(i);
                                }
                                dietAdapter.notifyItemChanged(i);
                                showTextViewEmpty();
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
                                dietAdapter.notifyItemRemoved(i);
                                showTextViewEmpty();
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
                        if (product == null || product.isDeleted()) return;
                        products.add(product);
                        dietAdapter.notifyItemChanged(products.size());
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
                                dietAdapter.notifyItemChanged(i);
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
                                dietAdapter.notifyItemChanged(i);
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

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.diet_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        dietAdapter = new DietAdapter(lakes, this);
        recyclerView.setAdapter(dietAdapter);
    }
}