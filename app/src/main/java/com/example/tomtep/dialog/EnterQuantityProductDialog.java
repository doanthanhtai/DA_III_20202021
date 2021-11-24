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
import com.example.tomtep.model.LichSuNhapHang;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListResourceBundle;

public class EnterQuantityProductDialog extends Dialog {

    private Spinner sprSanPham;
    private EditText edtSoLuong;
    private Button btnDong, btnNhap;
    private final Context context;
    private SanPham sanPham;
    private final List<SanPham> sanPhams;

    public EnterQuantityProductDialog(@NonNull Context context, SanPham sanPham, List<SanPham> sanPhams) {
        super(context);
        this.context = context;
        this.sanPham = sanPham;
        this.sanPhams = sanPhams;
        initView();
        setDataForSpiner();
        setEvent();
    }

    private void setDataForSpiner() {
        List<String> tenSanPhams = new ArrayList<>();
        if (sanPhams == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_annoucement)
                    .setMessage(R.string.updatedietdialog_message_notfoundproduct)
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
            return;
        }

        //Tạo mảng string từ mảng sản phẩm
        for (SanPham sanPham : sanPhams) {
            tenSanPhams.add(sanPham.getMaSP() + " - " + sanPham.getTenSP());
        }
        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tenSanPhams);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprSanPham.setAdapter(spinerAdapter);
        sprSanPham.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sanPham = sanPhams.get(i);
                edtSoLuong.setHint(sanPhams.get(i).getDonViDung());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (sanPham != null) {
            sprSanPham.setSelection(sanPhams.indexOf(sanPham));
        }
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
            sanPham.setSoLuong(sanPham.getSoLuong() + quantity);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan/" + TaiKhoan.getInstance().getId() + "/sanPhams/" + sanPham.getId());
            databaseReference.setValue(sanPham).addOnCompleteListener(task -> {
                String thoiGianTao = DateFormat.getInstance().format(Calendar.getInstance().getTime());
                List<LichSuNhapHang> lichSuNhapHangs = new ArrayList<>();
                if (sanPham.getLichSuNhapHangs() != null){
                    lichSuNhapHangs = sanPham.getLichSuNhapHangs();
                }
                LichSuNhapHang lichSuNhapHang = new LichSuNhapHang(String.valueOf(lichSuNhapHangs.size()), quantity, thoiGianTao, thoiGianTao, false);
                lichSuNhapHangs.add(lichSuNhapHang);

                databaseReference.child("lichSuNhapHangs").child(lichSuNhapHang.getId()).setValue(lichSuNhapHang).addOnCompleteListener(task1 -> {
                    Toast.makeText(context, R.string.enterquantityproduct_toast_succesful, Toast.LENGTH_SHORT).show();
                    dismiss();
                });
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
