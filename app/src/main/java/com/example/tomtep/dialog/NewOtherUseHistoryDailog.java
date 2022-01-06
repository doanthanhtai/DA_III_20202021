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
import com.example.tomtep.model.OtherUseHistory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class NewOtherUseHistoryDailog extends Dialog {

    private final Context context;
    private final Lake lake;
    private final String timeUse = DateFormat.getInstance().format(Calendar.getInstance().getTime());
    private EditText edtName, edtCost, edtDescription;
    private TextView tvTime;
    private Button btnCancel, btnInsert;

    public NewOtherUseHistoryDailog(@NonNull Context context, Lake lake) {
        super(context);
        this.context = context;
        this.lake = lake;
        initView();
        setEvent();
    }

    private void setEvent() {
        btnCancel.setOnClickListener(v -> cancelNewOtherUseHistory());
        btnInsert.setOnClickListener(v -> InsertOtherUseHistory());
    }

    private void InsertOtherUseHistory() {
        String name = String.valueOf(edtName.getText());
        String strCost = String.valueOf(edtCost.getText());
        String description = String.valueOf(edtDescription.getText());
        if (name.isEmpty()) {
            edtName.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (strCost.isEmpty()) {
            edtCost.setHintTextColor(context.getColor(R.color.red));
            return;
        }
        if (description.isEmpty()) {
            edtDescription.setHintTextColor(context.getColor(R.color.red));
            return;
        }

        try {
            double cost = Double.parseDouble(strCost);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("OtherUseHistory");
            String otherUseHistoryId = databaseReference.push().getKey();
            OtherUseHistory otherUseHistory = new OtherUseHistory();
            otherUseHistory.setId(otherUseHistoryId);
            otherUseHistory.setLakeId(lake.getId());
            otherUseHistory.setName(name);
            otherUseHistory.setCost(cost);
            otherUseHistory.setDescription(description);
            otherUseHistory.setUseTime(String.valueOf(tvTime.getText()));
            otherUseHistory.setUpdateTime(String.valueOf(tvTime.getText()));
            otherUseHistory.setDeleted(false);

            databaseReference.child(otherUseHistory.getId()).setValue(otherUseHistory).addOnCompleteListener(task -> Toast.makeText(context, R.string.dialognew_otherusehistory_toast_success, Toast.LENGTH_SHORT).show());
            dismiss();

        } catch (Exception e) {
            Toast.makeText(context, R.string.dialognew_otherusehistory_costinvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelNewOtherUseHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.dialognew_otherusehistory_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_otherusehistory);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        btnCancel = findViewById(R.id.dialognew_otherusehistory_btn_cancel);
        btnInsert = findViewById(R.id.dialognew_otherusehistory_btn_add);
        edtName = findViewById(R.id.dialognew_otherusehistory_edt_name);
        edtCost = findViewById(R.id.dialognew_otherusehistory_edt_cost);
        edtDescription = findViewById(R.id.dialognew_otherusehistory_edt_description);
        tvTime = findViewById(R.id.dialognew_otherusehistory_tv_time);
        tvTime.setText(timeUse);
    }
}
