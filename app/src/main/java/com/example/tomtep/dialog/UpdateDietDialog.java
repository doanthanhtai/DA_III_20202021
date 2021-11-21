package com.example.tomtep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.R;
import com.example.tomtep.adapter.TimeAdapter;
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.CheDoAn;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateDietDialog extends Dialog {
    private EditText edtSoLuong, edtLongTime;
    private ImageButton imgThemGio;
    private Spinner sprSanPham;
    private Button btnLuu, btnDong;
    private final Context context;
    private final CheDoAn cheDoAn;
    private final Ao ao;
    private final TimeAdapter timeAdapter;
    private final List<SanPham> sanPhams;
    private List<String> listTime;

    public UpdateDietDialog(@NonNull Context context, Ao ao, List<SanPham> sanPhams) {
        super(context);
        this.context = context;
        this.ao = ao;
        this.cheDoAn = ao.getCheDoAn();
        this.sanPhams = sanPhams;
        listTime = new ArrayList<>();
        if (cheDoAn.getKhungGioChoAn() != null) listTime = cheDoAn.getKhungGioChoAn();
        timeAdapter = new TimeAdapter(listTime);
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
                cheDoAn.setSanPhamChoAn(sanPhams.get(i));
                edtSoLuong.setHint(sanPhams.get(i).getDonViDung());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelDialog());
        btnLuu.setOnClickListener(v -> onClickUpdateDiet());
        imgThemGio.setOnClickListener(v -> showHourPicker());
    }

    public void showHourPicker() {
        TimePickerDialog.OnTimeSetListener myTimeListener = (view, hourOfDay, minute1) -> {
            if (view.isShown()) {
                listTime.add(view.getHour() + ":" + view.getMinute());
                timeAdapter.notifyItemChanged(listTime.size() - 1);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), android.R.style.Widget_Holo_DatePicker, myTimeListener, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        timePickerDialog.show();
    }

    private void onClickUpdateDiet() {
        String strSoLuong = String.valueOf(edtSoLuong.getText());
        if (strSoLuong.isEmpty()) {
            Toast.makeText(getContext(), R.string.dialogupdatediet_toast_quantityempty, Toast.LENGTH_SHORT).show();
            return;
        }
        String strThoiGianAn = String.valueOf(edtLongTime.getText());
        if (strThoiGianAn.isEmpty()) {
            Toast.makeText(getContext(), R.string.dialogupdatediet_toast_longtimeempty, Toast.LENGTH_SHORT).show();
            return;
        }
        float soLuong = Float.parseFloat(strSoLuong.trim());
        cheDoAn.setLuongChoAn(soLuong);
        cheDoAn.setKhungGioChoAn(listTime);
        cheDoAn.setThoiGianChoAn(strThoiGianAn);

        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(TaiKhoan.getInstance().getId())
                .child("aos")
                .child(ao.getId())
                .child("cheDoAn")
                .setValue(cheDoAn);
        Toast.makeText(context, R.string.all_toast_updateinfomationsuccess, Toast.LENGTH_SHORT).show();
        this.dismiss();
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
        setContentView(R.layout.dialog_update_diet);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        edtSoLuong = findViewById(R.id.dialognewlake_edt_luong);
        edtLongTime = findViewById(R.id.dialognewlake_edt_longtime);
        sprSanPham = findViewById(R.id.dialogupdatediet_spr_sanpham);
        imgThemGio = findViewById(R.id.dialogapdatediet_img_timepicker);
        btnDong = findViewById(R.id.dialogupdatediet_btn_cancel);
        btnLuu = findViewById(R.id.dialogupdatediet_btn_save);
        RecyclerView rcvTime = findViewById(R.id.dialogupdatediet_rcv_time);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvTime.setLayoutManager(linearLayoutManager);
        rcvTime.setAdapter(timeAdapter);
    }
}
