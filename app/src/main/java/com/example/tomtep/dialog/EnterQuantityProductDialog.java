package com.example.tomtep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.ImportHistory;
import com.example.tomtep.model.Product;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EnterQuantityProductDialog extends Dialog {

    private final Context context;
    private final List<Product> products;
    private Spinner sprSanPham;
    private EditText edtSoLuong;
    private Button btnDong, btnNhap;
    private Product product;

    public EnterQuantityProductDialog(@NonNull Context context, Product product, List<Product> products) {
        super(context);
        this.context = context;
        this.product = product;
        this.products = products;
        initView();
        setDataForSpiner();
        setEvent();
    }

    private void setDataForSpiner() {
        List<String> productNames = new ArrayList<>();
        if (products == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_annoucement)
                    .setMessage(R.string.updatedietdialog_message_notfoundproduct)
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
            return;
        }

        //Tạo mảng string từ mảng sản phẩm
        for (Product product : products) {
            productNames.add(product.getKey() + " - " + product.getName());
        }
        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, productNames);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprSanPham.setAdapter(spinerAdapter);
        sprSanPham.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product = products.get(i);
                edtSoLuong.setHint(products.get(i).getMeasure());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sprSanPham.setSelection(products.indexOf(product));
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancel());
        btnNhap.setOnClickListener(v -> onClickEnterQuantity());
    }

    private void onClickEnterQuantity() {
        if (String.valueOf(edtSoLuong.getText()).isEmpty()) {
            Toast.makeText(context, R.string.enterquantityproduct_toast_quantityinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = Integer.parseInt(edtSoLuong.getText().toString().trim());
        if (quantity > 0) {
            String importTime = DateFormat.getInstance().format(Calendar.getInstance().getTime());
            product.setAmount(product.getAmount() + quantity);
            FirebaseDatabase.getInstance().getReference("Product").child(product.getId()).child("amount").setValue(product.getAmount());

            ImportHistory importHistory = new ImportHistory();
            importHistory.setId(FirebaseDatabase.getInstance().getReference("ImportHistory").push().getKey());
            importHistory.setProductId(product.getId());
            importHistory.setAmount(quantity);
            importHistory.setImportTime(importTime);
            importHistory.setUpdateTime(importTime);
            importHistory.setDeleted(false);

            FirebaseDatabase.getInstance().getReference("ImportHistory").child(importHistory.getId()).setValue(importHistory).addOnCompleteListener(task -> {
                Toast.makeText(context, R.string.enterquantityproduct_toast_succesful, Toast.LENGTH_SHORT).show();
                dismiss();
            });
        } else {
            Toast.makeText(context, R.string.enterquantityproduct_toast_quantityinvalid, Toast.LENGTH_SHORT).show();
        }

    }

    private void onClickCancel() {
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
        setContentView(R.layout.dialog_enter_quatity_product);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setAttributes(layoutParams);
        this.setCancelable(false);

        sprSanPham = findViewById(R.id.dialogenterquatity_spr_sanpham);
        edtSoLuong = findViewById(R.id.dialogenterquantity_edt_soluong);
        btnDong = findViewById(R.id.dialogenterquantity_btn_cancel);
        btnNhap = findViewById(R.id.dialogenterquantity_btn_add);

    }
}
