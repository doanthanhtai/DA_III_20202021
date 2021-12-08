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
import com.example.tomtep.model.FeedingHistory;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateFeedingHistoryDialog extends Dialog {

    private final FeedingHistory feedingHistory;
    private final Context context;
    private Button btnCancel, btnUpdate;
    private Spinner sprTinhTrang;

    public UpdateFeedingHistoryDialog(@NonNull Context context, FeedingHistory feedingHistory) {
        super(context);
        this.context = context;
        this.feedingHistory = feedingHistory;
        initView();
        setEvent();
    }

    private void setEvent() {
        btnCancel.setOnClickListener(v -> this.dismiss());
        btnUpdate.setOnClickListener(v -> updateFeedingHistory());
    }

    private void updateFeedingHistory() {
        String result = sprTinhTrang.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference("FeedingHistory").child(feedingHistory.getId()).child("result").setValue(result).addOnCompleteListener(task -> {
            Toast.makeText(context, R.string.updateeatinghistory_toast_success, Toast.LENGTH_SHORT).show();
            dismiss();
        });

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
