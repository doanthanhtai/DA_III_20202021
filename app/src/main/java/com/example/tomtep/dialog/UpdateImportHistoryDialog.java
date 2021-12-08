package com.example.tomtep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.ImportHistory;
import com.example.tomtep.model.Product;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateImportHistoryDialog extends Dialog {
    private final Context context;
    private final ImportHistory importHistory;
    private final Product product;
    private TextView tvMaSPM, tvTenSP, tvNgayTao, tvNgayCapNhat;
    private EditText edtSoLuong;
    private Button btnLuu, btnDong;

    public UpdateImportHistoryDialog(@NonNull Context context, Product product, ImportHistory importHistory) {
        super(context);
        this.context = context;
        this.importHistory = importHistory;
        this.product = product;
        initView();
        setDefaultData();
        setEvent();
    }

    private void setDefaultData() {
        tvMaSPM.setText(product.getKey());
        tvTenSP.setText(product.getName());
        tvNgayTao.setText(importHistory.getImportTime());
        tvNgayCapNhat.setText(getTimeUpdate());
        edtSoLuong.setHint(product.getMeasure());
        edtSoLuong.setText(String.valueOf(importHistory.getAmount()));
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelDialog());
        btnLuu.setOnClickListener(v -> onClickUpdateImportHistory());
    }

    private String getTimeUpdate() {
        return DateFormat.getInstance().format(Calendar.getInstance().getTime());
    }

    private void onClickUpdateImportHistory() {
        if (String.valueOf(edtSoLuong.getText()).isEmpty()) {
            Toast.makeText(context, R.string.enterquantityproduct_toast_quantityinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = Integer.parseInt(edtSoLuong.getText().toString().trim());
        if (quantity > 0) {

            product.setAmount(product.getAmount() + quantity - importHistory.getAmount());
            importHistory.setAmount(quantity);

            Map<String, Object> map = new HashMap<>();
            map.put("amount", importHistory.getAmount());
            map.put("updateTime", DateFormat.getInstance().format(Calendar.getInstance().getTime()));

            FirebaseDatabase.getInstance().getReference("ImportHistory").child(importHistory.getId()).updateChildren(map)
                    .addOnCompleteListener(task -> FirebaseDatabase.getInstance().getReference("Product").child(product.getId()).child("amount").setValue(product.getAmount())
                            .addOnCompleteListener(task1 -> {
                                Toast.makeText(context, R.string.updateimporthistory_toast_succesful, Toast.LENGTH_SHORT).show();
                                dismiss();
                            }));
        } else {
            Toast.makeText(context, R.string.enterquantityproduct_toast_quantityinvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.updateimporthistory_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();

    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_import_history);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        tvMaSPM = findViewById(R.id.dialogupdateimporthistory_tv_masanpham);
        tvTenSP = findViewById(R.id.dialogupdateimporthistory_tv_tensanpham);
        tvNgayTao = findViewById(R.id.dialogupdateimporthistory_tv_ngaynhap);
        tvNgayCapNhat = findViewById(R.id.dialogupdateimporthistory_tv_ngaycapnhat);
        edtSoLuong = findViewById(R.id.dialogupdateimporthistory_edt_soluong);
        btnDong = findViewById(R.id.dialogupdateimporthistory_btn_cancel);
        btnLuu = findViewById(R.id.dialogupdateimporthistory_btn_update);
    }
}
