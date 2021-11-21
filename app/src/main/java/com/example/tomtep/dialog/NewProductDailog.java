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
import androidx.annotation.Nullable;

import com.example.tomtep.R;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewProductDailog extends Dialog {

    private EditText edtMaSP, edtTenSP, edtTenNCC, edtGiaNhap;
    private Spinner sprDonVi;
    private Button btnDong, btnThem;
    private final Context context;
    private final List<String> listDonVi;
    private final SanPham sanPham;

    public NewProductDailog(@NonNull Context context) {
        super(context);
        this.context = context;
        listDonVi = new ArrayList<>();
        sanPham = new SanPham();
        initView();
        getDonVi();
        setDataForSpiner();
        setEvent();
    }

    private void getDonVi() {
        FirebaseDatabase.getInstance().getReference("DonViDung").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String newDonVi = snapshot.getValue(String.class);
                if (newDonVi == null) {
                    return;
                }
                listDonVi.add(newDonVi);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String donVi = snapshot.getValue(String.class);
                assert donVi != null;
                int changeIndex = Integer.parseInt(donVi);
                listDonVi.set(changeIndex, donVi);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String donVi = snapshot.getValue(String.class);
                assert donVi != null;
                int changeIndex = Integer.parseInt(donVi);
                listDonVi.remove(changeIndex);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        btnDong.setOnClickListener(v -> onClickCancelNewProdcut());
        btnThem.setOnClickListener(v -> onClickAddNewProduct());
    }

    private void onClickAddNewProduct() {
        String maSP = String.valueOf(edtMaSP.getText());
        String tenSP = String.valueOf(edtTenSP.getText());
        String tenNCC = String.valueOf(edtTenNCC.getText());
        String strGiaNhap = String.valueOf(edtGiaNhap.getText());
        if (maSP.isEmpty()) {
            edtMaSP.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (tenSP.isEmpty()) {
            edtTenSP.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (tenNCC.isEmpty()) {
            edtTenNCC.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (strGiaNhap.isEmpty()) {
            edtGiaNhap.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (sanPham.getDonViDung().equals(listDonVi.get(0))) {
            Toast.makeText(context, R.string.dialognewproduct_toast_donvidunginvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        float giaNhap = Float.parseFloat(strGiaNhap);
        if (giaNhap < 0) {
            Toast.makeText(context, R.string.dialognewproduct_toast_gianhapinvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("sanPhams");
        String key = databaseReference.push().getKey();
        if (key == null) {
            return;
        }
        sanPham.setId(key);
        sanPham.setMaSP(maSP);
        sanPham.setTenSP(tenSP);
        sanPham.setTenNCC(tenNCC);
        sanPham.setGiaNhap(giaNhap);
        sanPham.setSoLuong(0);
        sanPham.setLichSuNhapHangs(new ArrayList<>());
        sanPham.setDaXoa(false);
        FirebaseDatabase.getInstance().getReference("TaiKhoan")
                .child(TaiKhoan.getInstance().getId())
                .child("sanPhams")
                .child(key)
                .setValue(sanPham);
        Toast.makeText(context,R.string.newproduct_succesful,Toast.LENGTH_SHORT).show();
        this.dismiss();
    }

    private void onClickCancelNewProdcut() {
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
        setContentView(R.layout.dialog_new_product);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        edtMaSP = findViewById(R.id.dialognewproduct_edt_masanpham);
        edtTenSP = findViewById(R.id.dialognewproduct_edt_tensanpham);
        edtTenNCC = findViewById(R.id.dialognewproduct_edt_tenncc);
        edtGiaNhap = findViewById(R.id.dialognewproduct_edt_gianhap);
        sprDonVi = findViewById(R.id.dialognewproduct_spr_donvi);
        btnDong = findViewById(R.id.dialognewproduct_btn_cancel);
        btnThem = findViewById(R.id.dialognewproduct_btn_add);
    }

    private void setDataForSpiner() {
        if (listDonVi == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(R.string.all_title_annoucement)
                    .setMessage(R.string.updatedietdialog_message_notfounddonvi)
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
            return;
        }

        listDonVi.add("Chọn đơn vị dùng cho sản phẩm");

        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listDonVi);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprDonVi.setAdapter(spinerAdapter);
        sprDonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sanPham.setDonViDung(listDonVi.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
