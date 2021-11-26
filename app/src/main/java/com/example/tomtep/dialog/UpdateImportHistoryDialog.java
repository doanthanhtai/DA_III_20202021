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
import com.example.tomtep.model.LichSuNhapHang;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateImportHistoryDialog extends Dialog {
    private TextView tvMaSPM, tvTenSP, tvNgayTao, tvNgayCapNhat;
    private EditText edtSoLuong;
    private Button btnLuu, btnDong;
    private final Context context;
    private final SanPham sanPham;
    private final LichSuNhapHang lichSuNhapHang;

    public UpdateImportHistoryDialog(@NonNull Context context, SanPham sanPham, LichSuNhapHang lichSuNhapHang) {
        super(context);
        this.context = context;
        this.sanPham = sanPham;
        this.lichSuNhapHang = lichSuNhapHang;
        initView();
        setDefaultData();
        setEvent();
    }

    private void setDefaultData() {
        tvMaSPM.setText(sanPham.getMaSP());
        tvTenSP.setText(sanPham.getTenSP());
        tvNgayTao.setText(lichSuNhapHang.getThoiGianNhap());
        getTimeUpdate();
        edtSoLuong.setHint(sanPham.getDonViDung());
        edtSoLuong.setText(String.valueOf(lichSuNhapHang.getSoLuong()));
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelDialog());
        btnLuu.setOnClickListener(v -> onClickUpdateImportHistory());
    }

    private void getTimeUpdate() {
        String data = DateFormat.getInstance().format(Calendar.getInstance().getTime());
        tvNgayCapNhat.setText(data);
    }

    private void onClickUpdateImportHistory() {
        if (String.valueOf(edtSoLuong.getText()).isEmpty()) {
            Toast.makeText(context, R.string.enterquantityproduct_toast_quantityinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        int quantity = Integer.parseInt(edtSoLuong.getText().toString().trim());
        if (quantity > 0) {
            sanPham.setSoLuong(sanPham.getSoLuong() + quantity - lichSuNhapHang.getSoLuong());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan/" + TaiKhoan.getInstance().getId() + "/sanPhams/" + sanPham.getId());
            databaseReference.child("soLuong").setValue(sanPham.getSoLuong()).addOnCompleteListener(task -> {
                String thoiGianCapNhat = DateFormat.getInstance().format(Calendar.getInstance().getTime());
                lichSuNhapHang.setThoiGianCapNhat(thoiGianCapNhat);
                lichSuNhapHang.setSoLuong(quantity);
                databaseReference.child("lichSuNhapHangs").child(lichSuNhapHang.getId()).setValue(lichSuNhapHang).addOnCompleteListener(task1 -> {
                    Toast.makeText(context, R.string.updateimporthistory_toast_succesful, Toast.LENGTH_SHORT).show();
                    dismiss();
                });
            });
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
