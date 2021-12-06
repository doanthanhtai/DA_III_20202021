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

import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewProductDailog extends Dialog {

    private EditText edtMaSP, edtTenSP, edtTenNCC, edtGiaNhap;
    private Spinner sprDonVi;
    private Button btnDong, btnThem;
    private final Context context;
    private final List<String> units;
    private final Product product;

    public NewProductDailog(@NonNull Context context) {
        super(context);
        this.context = context;
        units = new ArrayList<>();
        product = new Product();
        addChildEventListener();
        initView();
        setDataForSpiner();
        setEvent();
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Unit").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String unit = snapshot.getValue(String.class);
                if (unit == null) return;
                units.add(unit);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey() == null) return;
                int index = Integer.parseInt(snapshot.getKey());
                String unit = snapshot.getValue(String.class);
                units.set(index, unit);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey() == null) return;
                int index = Integer.parseInt(snapshot.getKey());
                units.remove(index);
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

        float giaNhap = Float.parseFloat(strGiaNhap);
        if (giaNhap < 0) {
            Toast.makeText(context, R.string.dialognewproduct_toast_gianhapinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        if (sprDonVi.getSelectedItemPosition() == 0) {
            Toast.makeText(context, R.string.dialognewproduct_toast_unitinvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        String productId = databaseReference.push().getKey();
        if (productId == null) {
            return;
        }
        product.setId(productId);
        product.setAccountId(MainActivity.MY_ACCOUNT.getId());
        product.setKey(maSP);
        product.setName(tenSP);
        product.setSupplier(tenNCC);
        product.setImportPrice(giaNhap);
        product.setAmount(0);
        product.setDeleted(false);
        FirebaseDatabase.getInstance().getReference("Product").child(productId).setValue(product).addOnCompleteListener(task -> {
            Toast.makeText(context, R.string.newproduct_succesful, Toast.LENGTH_SHORT).show();
            dismiss();
        });

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
        setCancelable(false);

        edtMaSP = findViewById(R.id.dialognewproduct_edt_masanpham);
        edtTenSP = findViewById(R.id.dialognewproduct_edt_tensanpham);
        edtTenNCC = findViewById(R.id.dialognewproduct_edt_tenncc);
        edtGiaNhap = findViewById(R.id.dialognewproduct_edt_gianhap);
        sprDonVi = findViewById(R.id.dialognewproduct_spr_donvi);
        btnDong = findViewById(R.id.dialognewproduct_btn_cancel);
        btnThem = findViewById(R.id.dialognewproduct_btn_add);
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
        units.add((String) context.getText(R.string.unit_spinner_title));

        //Cài đặt và đổ dữ liệu cho spiner
        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, units);
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
    }
}
