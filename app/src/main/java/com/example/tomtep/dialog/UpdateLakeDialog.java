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
import com.example.tomtep.model.Lake;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UpdateLakeDialog extends Dialog {

    private EditText edtMaAo, edtTenAo, edtMoTa;
    private TextView tvNgayTao;
    private Button btnCapNhat, btnDong;
    private final Context context;
    private final Lake lake;

    public UpdateLakeDialog(@NonNull Context context, Lake lake) {
        super(context);
        this.context = context;
        this.lake = lake;
        initView();
        setDataDefault();
        setEvent();
    }

    private void setDataDefault() {
        edtMaAo.setText(lake.getKey());
        edtTenAo.setText(lake.getName());
        edtMoTa.setText(lake.getDescription());
        tvNgayTao.setText(lake.getCreationTime());
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelDialog());
        btnCapNhat.setOnClickListener(v -> onClickUpdateLake());
    }

    private void onClickUpdateLake() {
        String strMaAo = String.valueOf(edtMaAo.getText()).trim();
        String strTenAo = String.valueOf(edtTenAo.getText()).trim();
        String strMoTa = String.valueOf(edtMoTa.getText()).trim();
        if (strMoTa.isEmpty() || strTenAo.isEmpty() || strMaAo.isEmpty()) {
            Toast.makeText(context, R.string.all_toast_lackofinformation, Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("key", strMaAo);
            map.put("description", strMoTa);
            map.put("name", strTenAo);
            FirebaseDatabase.getInstance().getReference("Lake").child(lake.getId())
                    .updateChildren(map);
            Toast.makeText(context, R.string.all_toast_updateinfomationsuccess, Toast.LENGTH_SHORT).show();
            this.dismiss();
        }

    }

    private void onClickCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.updatelake_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();

    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_lake);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        this.setCancelable(false);

        edtMaAo = findViewById(R.id.dialogupdatelake_edt_ma_ao);
        edtTenAo = findViewById(R.id.dialogupdatelake_edt_ten_ao);
        edtMoTa = findViewById(R.id.dialogupdatelake_edt_mota);
        tvNgayTao = findViewById(R.id.dialogupdatelake_tv_ngaytao);
        btnDong = findViewById(R.id.dialogupdatelake_btn_cancel);
        btnCapNhat = findViewById(R.id.dialogupdatelake_btn_update);
    }
}
