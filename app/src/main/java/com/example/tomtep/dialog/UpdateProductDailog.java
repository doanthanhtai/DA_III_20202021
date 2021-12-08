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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.Product;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class UpdateProductDailog extends Dialog {

    private final Context context;
    private final List<String> units;
    private final Product product;
    private EditText edtMaSP, edtTenSP, edtTenNCC, edtGiaNhap;
    private Spinner sprDonVi;
    private Button btnDong, btnCapNhat;

    public UpdateProductDailog(@NonNull Context context, Product product, List<String> units) {
        super(context);
        this.context = context;
        this.units = units;
        this.product = product;
        initView();
        setDataForSpiner();
        setDataOld();
        setEvent();
    }

    private void setDataOld() {
        edtMaSP.setText(product.getKey());
        edtTenSP.setText(product.getName());
        edtTenNCC.setText(product.getSupplier());
        edtGiaNhap.setText(String.valueOf(product.getImportPrice()));
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancel());
        btnCapNhat.setOnClickListener(v -> onClickUpdateProduct());
    }

    private void onClickUpdateProduct() {
        String strMaSP = String.valueOf(edtMaSP.getText());
        String strTenSP = String.valueOf(edtTenSP.getText());
        String strTenNCC = String.valueOf(edtTenNCC.getText());
        String strGiaNhap = String.valueOf(edtGiaNhap.getText());
        if (strMaSP.isEmpty()) {
            edtMaSP.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (strTenSP.isEmpty()) {
            edtTenSP.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (strTenNCC.isEmpty()) {
            edtTenNCC.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (strGiaNhap.isEmpty()) {
            edtGiaNhap.setHintTextColor(context.getColor(R.color.red));
            return;
        }

        float giaNhap = Float.parseFloat(strGiaNhap);
        if (giaNhap < 0) {
            Toast.makeText(context, R.string.dialognew_product_toast_importpriceinvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("key", strMaSP);
        map.put("name", strTenSP);
        map.put("supplier", strTenNCC);
        map.put("importPrice", giaNhap);
        map.put("measure", product.getMeasure());

        FirebaseDatabase.getInstance().getReference("Product")
                .child(product.getId())
                .updateChildren(map);
        Toast.makeText(context, R.string.updateproduct_succesful, Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    private void onClickCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.newproduct_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_product);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        edtMaSP = findViewById(R.id.dialogupdate_product_edt_masanpham);
        edtTenSP = findViewById(R.id.dialogupdate_product_edt_tensanpham);
        edtTenNCC = findViewById(R.id.dialogupdate_product_edt_tenncc);
        edtGiaNhap = findViewById(R.id.dialogupdate_product_edt_gianhap);
        sprDonVi = findViewById(R.id.dialogupdate_product_spr_donvi);
        btnDong = findViewById(R.id.dialogupdateproduct_btn_cancel);
        btnCapNhat = findViewById(R.id.dialogupdateproduct_btn_update);
    }

    private void setDataForSpiner() {
        if (units == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_annoucement)
                    .setMessage(R.string.updatedietdialog_message_notfounddonvi)
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
            return;
        }

        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, units);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprDonVi.setAdapter(spinerAdapter);
        sprDonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product.setMeasure(units.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sprDonVi.setSelection(spinerAdapter.getPosition(product.getMeasure()));
    }
}
