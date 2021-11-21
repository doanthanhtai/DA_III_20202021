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
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.CheDoAn;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewLakeDialog extends Dialog {

    private EditText edtMaAo, edtTenAo, edtMoTa;
    private TextView tvNgayTao;
    private Button btnThem, btnDong;
    private final Context context;

    public NewLakeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
        getTimeInit();
        setEvent();
    }

    private void getTimeInit() {
        String data = DateFormat.getInstance().format(Calendar.getInstance().getTime());
        tvNgayTao.setText(data);
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelDialog());
        btnThem.setOnClickListener(v -> onClickAddNewLake());
    }

    private void onClickAddNewLake() {
        String strMaAo = String.valueOf(edtMaAo.getText()).trim();
        String strTenAo = String.valueOf(edtTenAo.getText()).trim();
        String strMoTa = String.valueOf(edtMoTa.getText()).trim();
        String strNgayTao = String.valueOf(tvNgayTao.getText()).trim();
        String strNgayThu = "";
        if (strMoTa.isEmpty() || strTenAo.isEmpty() || strMaAo.isEmpty()) {
            Toast.makeText(context, R.string.all_toast_lackofinformation, Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
            String id = databaseReference.child(TaiKhoan.getInstance().getId()).child("aos").push().getKey();
            CheDoAn cheDoAn = intitDeitForLake();
            Ao ao = new Ao(id, strMaAo, strTenAo, strMoTa, strNgayTao, strNgayThu, cheDoAn, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, false);
            assert id != null;
            databaseReference.child(TaiKhoan.getInstance().getId()).child("aos").child(id).setValue(ao);
            this.dismiss();
        }


    }

    private CheDoAn intitDeitForLake() {
        return new CheDoAn(initDefaultProductForDiet(), 0, initDefaultTimeFrameForDiet(), "7200000", false);
    }

    private List<String> initDefaultTimeFrameForDiet() {
        List<String> listTimFrame = new ArrayList<>();
        listTimFrame.add("07:00");
        listTimFrame.add("10:00");
        listTimFrame.add("13:00");
        listTimFrame.add("14:00");
        return listTimFrame;
    }

    private SanPham initDefaultProductForDiet() {
        return new SanPham("default_product", "DP", "Sản phẩm cho ăn mặc định", "TOMTEP", 0, "", 0, new ArrayList<>(), false);
    }

    private void onClickCancelDialog() {
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
        setContentView(R.layout.dialog_new_lake);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        this.setCancelable(false);

        edtMaAo = findViewById(R.id.dialognewlake_edt_ma_ao);
        edtTenAo = findViewById(R.id.dialognewlake_edt_ten_ao);
        edtMoTa = findViewById(R.id.dialognewlake_edt_mota);
        tvNgayTao = findViewById(R.id.dialognewlake_tv_ngaytao);
        btnDong = findViewById(R.id.dialognewlake_btn_cancel);
        btnThem = findViewById(R.id.dialognewlake_btn_add);
    }


}
