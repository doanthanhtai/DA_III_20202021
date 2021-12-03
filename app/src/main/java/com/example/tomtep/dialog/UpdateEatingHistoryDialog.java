package com.example.tomtep.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.LichSuSuDungSanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateEatingHistoryDialog extends Dialog {

    private Button btnCancel, btnUpdate;
    private Spinner sprTinhTrang;
    private final String idAo;
    private final LichSuSuDungSanPham lichSuSuDungSanPham;
    private final Context context;

    public UpdateEatingHistoryDialog(@NonNull Context context, String idAo, LichSuSuDungSanPham lichSuSuDungSanPham) {
        super(context);
        this.context = context;
        this.idAo = idAo;
        this.lichSuSuDungSanPham = lichSuSuDungSanPham;
        initView();
        setEvent();
    }

    private void setEvent() {
        btnCancel.setOnClickListener(v -> this.dismiss());
        btnUpdate.setOnClickListener(v -> updateEatingHistory());
    }

    private void updateEatingHistory() {
        String tinhTrang = sprTinhTrang.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(TaiKhoan.getInstance().getId()).child("aos").child(idAo)
                .child("lichSuSuDungSanPhams")
                .child(lichSuSuDungSanPham.getId())
                .child("lichSuChoAns").child("ketQua").setValue(tinhTrang);
        Toast.makeText(context, R.string.updateeatinghistory_toast_success, Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_eatinghistory);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(layoutParams);
        setCancelable(false);

        btnCancel = findViewById(R.id.dialogupdateeatinghistory_btn_cancel);
        btnUpdate = findViewById(R.id.dialogupdateeatinghistory_btn_update);
        sprTinhTrang = findViewById(R.id.dialogupdateeatinghistory_spr_ketqua);
    }


}
