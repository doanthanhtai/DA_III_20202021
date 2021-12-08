package com.example.tomtep.dialog;

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
import com.example.tomtep.model.FeedingHistory;
import com.example.tomtep.model.Product;
import com.example.tomtep.model.ProductHistory;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductHistoryDialog extends Dialog {

    private final ProductHistory productHistory;
    private final Product product;
    private final FeedingHistory feedingHistory;
    private final Context context;
    private TextView tvProductInfo, tvUseTime, tvUpdateTime;
    private EditText edtAmount;
    private Button btnCancel, btnSave;


    public UpdateProductHistoryDialog(@NonNull Context context, ProductHistory productHistory, Product product, FeedingHistory feedingHistory) {
        super(context);
        this.context = context;
        this.productHistory = productHistory;
        this.product = product;
        this.feedingHistory = feedingHistory;
        initView();
        setVent();
        setDataOld();
    }

    private void setDataOld() {
        if (product == null) return;
        String productInfo = product.getKey() + " - " + product.getName();
        tvProductInfo.setText(productInfo);
        tvUseTime.setText(productHistory.getUseTime());
        tvUpdateTime.setText(DateFormat.getInstance().format(Calendar.getInstance().getTime()));
        edtAmount.setText(String.valueOf(productHistory.getAmount()));
        edtAmount.setHint(product.getMeasure());
    }

    private void setVent() {
        btnCancel.setOnClickListener(v -> this.dismiss());
        btnSave.setOnClickListener(v -> onClickUpdateProductHitory());
    }

    private void onClickUpdateProductHitory() {
        String strAmount = String.valueOf(edtAmount.getText()).trim();
        if (strAmount.isEmpty()) {
            Toast.makeText(context, R.string.all_toast_amountvalid, Toast.LENGTH_SHORT).show();
            edtAmount.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        float amount = Float.parseFloat(strAmount);
        if (amount <= 0.0) {
            Toast.makeText(context, R.string.all_toast_amountvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        if (product.getAmount() + productHistory.getAmount() - amount < 0.0) {
            Toast.makeText(context, R.string.all_toast_lackproduct, Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance().getReference("ProductHistory").child(productHistory.getId()).child("amount").setValue(amount);
        FirebaseDatabase.getInstance().getReference("Product").child(productHistory.getProductId()).child("amount").setValue(product.getAmount() + productHistory.getAmount() - amount);
        if (feedingHistory != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", amount);
            map.put("updateTime", String.valueOf(tvUpdateTime.getText()));
            FirebaseDatabase.getInstance().getReference("FeedingHistory").child(feedingHistory.getId()).updateChildren(map).addOnCompleteListener(task -> {
                Toast.makeText(context, R.string.dialogupdate_producthistory_success, Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_producthistory);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        tvProductInfo = findViewById(R.id.dialogupdate_producthistory_tv_productinfo);
        tvUseTime = findViewById(R.id.dialogupdate_producthistory_tv_usetime);
        tvUpdateTime = findViewById(R.id.dialogupdate_producthistory_tv_updatetime);
        edtAmount = findViewById(R.id.dialogupdate_producthistory_edt_amount);
        btnCancel = findViewById(R.id.dialogupdate_producthistory_btn_cancel);
        btnSave = findViewById(R.id.dialogupdate_producthistory_btn_save);

    }

}
