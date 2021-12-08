package com.example.tomtep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tomtep.R;
import com.example.tomtep.model.EnvironmentHistory;
import com.example.tomtep.model.Lake;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class NewEnvironmentHistoryDailog extends Dialog {

    private final Context context;
    private final Lake lake;
    private final EnvironmentHistory environmentHistoryOld;
    private TextView tvTime, tvLakeInfo;
    private EditText edtPH, edtOxy, edtSalinity;
    private Button btnCancel, btnInsert;

    public NewEnvironmentHistoryDailog(@NonNull Context context, Lake lake, EnvironmentHistory environmentHistory) {
        super(context);
        this.context = context;
        this.lake = lake;
        this.environmentHistoryOld = environmentHistory;
        initView();
        setDataOld();
        setEvent();
    }

    private void setDataOld() {
        String timeUse = DateFormat.getInstance().format(Calendar.getInstance().getTime());
        String strLakeInfo = lake.getKey() + " - " + lake.getName();
        tvTime.setText(timeUse);
        tvLakeInfo.setText(strLakeInfo);
        if (environmentHistoryOld == null) return;
        edtPH.setText(String.valueOf(environmentHistoryOld.getpH()));
        edtOxy.setText(String.valueOf(environmentHistoryOld.getoXy()));
        edtSalinity.setText(String.valueOf(environmentHistoryOld.getSalinity()));
    }

    private void setEvent() {
        btnCancel.setOnClickListener(v -> onClickCancelNewProdcut());
        btnInsert.setOnClickListener(v -> insertEnvironmentHistory());
        edtPH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtPH.getText().toString().trim().isEmpty()) {
                    edtPH.setHintTextColor(context.getColor(R.color.red));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edtOxy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtOxy.getText().toString().trim().isEmpty()) {
                    edtOxy.setHintTextColor(context.getColor(R.color.red));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtSalinity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSalinity.getText().toString().trim().isEmpty()) {
                    edtSalinity.setHintTextColor(context.getColor(R.color.red));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void insertEnvironmentHistory() {
        float pH, oXy, salinity;
        try {
            pH = Float.parseFloat(String.valueOf(edtPH.getText()));
            try {
                oXy = Float.parseFloat(String.valueOf(edtOxy.getText()));
                try {
                    salinity = Float.parseFloat(String.valueOf(edtSalinity.getText()));
                    addEnvironmentHistory(pH, oXy, salinity);
                } catch (Exception e) {
                    Toast.makeText(context, R.string.dialognew_environmenthistory_toast_salinityinvalid, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(context, R.string.dialognew_environmenthistory_toast_oxyinvalid, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, R.string.dialognew_environmenthistory_toast_phinvalid, Toast.LENGTH_SHORT).show();
        }
    }

    private void addEnvironmentHistory(float pH, float oXy, float salinity) {
        if (pH < 0 || pH > 14) {
            Toast.makeText(context, R.string.dialognew_environmenthistory_toast_phinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        if (oXy < 0 || oXy > 10) {
            Toast.makeText(context, R.string.dialognew_environmenthistory_toast_oxyinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        if (salinity < 0 || salinity > 10) {
            Toast.makeText(context, R.string.dialognew_environmenthistory_toast_salinityinvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("EnvironmentHistory");

        EnvironmentHistory environmentHistory = new EnvironmentHistory();
        environmentHistory.setId(databaseReference.push().getKey());
        environmentHistory.setLakeId(lake.getId());
        environmentHistory.setpH(pH);
        environmentHistory.setoXy(oXy);
        environmentHistory.setSalinity(salinity);
        environmentHistory.setUpdateTime(String.valueOf(tvTime.getText()));

        databaseReference.child(environmentHistory.getId()).setValue(environmentHistory).addOnCompleteListener(task -> {
            Toast.makeText(context, R.string.dialognew_environmenthistory_toast_success, Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    private void onClickCancelNewProdcut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.all_title_confirmaction)
                .setMessage(R.string.all_message_confirmactioncancel)
                .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(context, R.string.newenvironmenthistory_toast_canceled, Toast.LENGTH_SHORT).show();
                    this.dismiss();
                })
                .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_environmenthistory);

        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        setCancelable(false);

        tvTime = findViewById(R.id.dialognew_environmenthistory_tv_time);
        tvLakeInfo = findViewById(R.id.dialognew_environmenthistory_tv_lakeinfo);
        edtPH = findViewById(R.id.dialognew_environmenthistory_edt_ph);
        edtOxy = findViewById(R.id.dialognew_environmenthistory_edt_oxy);
        edtSalinity = findViewById(R.id.dialognew_environmenthistory_edt_salinity);
        btnInsert = findViewById(R.id.dialognew_environmenthistory_btn_insert);
        btnCancel = findViewById(R.id.dialognew_environmenthistory_btn_cancel);
    }
}
