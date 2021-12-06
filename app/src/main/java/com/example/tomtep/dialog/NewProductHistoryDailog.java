package com.example.tomtep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewProductHistoryDailog extends Dialog {

    private TextView tvTime;
    private EditText edtAmount;
    private Spinner sprProduct;
    private Button btnCancel, btnInsert;
    private final List<String> listProductInfo;
    private final Context context;
    private final List<Product> products;
    private final Lake lake;
    private Product productSeleted;

    public NewProductHistoryDailog(@NonNull Context context, List<Product> products, Lake lake) {
        super(context);
        this.context = context;
        this.products = products;
        this.lake = lake;
        productSeleted = new Product();
        listProductInfo = new ArrayList<>();
        initView();
        removeProductIsDeleted();
        setDataForSpiner();
        setEvent();
    }

    private void removeProductIsDeleted() {
        if (products == null) return;
        for (int i = products.size() - 1; i >= 0; i--) {
            if (products.get(i).isDeleted()) {
                products.remove(i);
            }
        }
    }

    private void setEvent() {
        btnCancel.setOnClickListener(v -> onClickCancelNewProdcut());
        btnInsert.setOnClickListener(v -> insertProductHistory());
    }

    private void insertProductHistory() {
        try {
            float amount = Float.parseFloat(String.valueOf(edtAmount.getText()).trim());
            if (productSeleted.getAmount() < amount) {
                Toast.makeText(context, R.string.all_toast_lackproduct, Toast.LENGTH_SHORT).show();
                return;
            }
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String productHistoryId = databaseReference.child("ProductHistory").push().getKey();
            if (productHistoryId == null) {
                return;
            }

            ProductHistory productHistory = new ProductHistory();
            productHistory.setId(productHistoryId);
            productHistory.setLakeId(lake.getId());
            productHistory.setProductId(productSeleted.getId());
            productHistory.setAmount(amount);
            productHistory.setUseTime(String.valueOf(tvTime.getText()));
            productHistory.setDeleted(false);

            databaseReference.child("ProductHistory").child(productHistoryId).setValue(productHistory).addOnCompleteListener(task -> {
                databaseReference.child("Product").child(productSeleted.getId()).child("amount").setValue(productSeleted.getAmount() - amount);
                Toast.makeText(context, R.string.insertproducthistory_success, Toast.LENGTH_SHORT).show();
                dismiss();
            });
        } catch (Exception e) {
            Toast.makeText(context, R.string.all_toast_amountvalid, Toast.LENGTH_SHORT).show();
        }

    }

    private void onClickCancelNewProdcut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.newlake_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_producthistory);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        TextView tvLakeInfo = findViewById(R.id.dialognew_producthistory_tv_lakeinfo);
        tvTime = findViewById(R.id.dialognew_producthistory_tv_time);
        sprProduct = findViewById(R.id.dialognew_producthistory_spr_product);
        edtAmount = findViewById(R.id.dialognew_producthistory_edt_amount);
        btnInsert = findViewById(R.id.dialognew_producthistory_btn_insert);
        btnCancel = findViewById(R.id.dialognew_producthistory_btn_cancel);

        String lakeInfo = lake.getKey() + " - " + lake.getName();
        String timeUse = DateFormat.getInstance().format(Calendar.getInstance().getTime());
        tvLakeInfo.setText(lakeInfo);
        tvTime.setText(timeUse);
    }

    private void setDataForSpiner() {
        if (products == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_annoucement)
                    .setMessage(R.string.all_message_notfoundproduct)
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
            return;
        }

        for (int i = products.size() - 1; i >= 0; i--) {
            listProductInfo.add(products.get(i).getKey() + "-" + products.get(i).getName());
        }

        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listProductInfo);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprProduct.setAdapter(spinerAdapter);
        sprProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productSeleted = products.get(i);
                edtAmount.setHint(productSeleted.getMeasure());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
