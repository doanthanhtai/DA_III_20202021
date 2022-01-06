package com.example.tomtep;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        setEvent();
    }

    private void setEvent() {
        btnReset.setOnClickListener(v -> onClickSendEmailResetPassword());
    }

    private void onClickSendEmailResetPassword() {
        String strEmail = edtEmail.getText().toString().trim();
        if (isEmailValid(strEmail)) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(strEmail).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, R.string.resetpassword_toast_sendemailcomplete, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPasswordActivity.this,SignInActivity.class));
                }else {
                    Toast.makeText(ResetPasswordActivity.this, R.string.resetpassword_toast_emailinexist, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,R.string.all_error_emailinvalid, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void initView() {
        edtEmail = findViewById(R.id.resetpassword_edt_email);
        btnReset = findViewById(R.id.resetpassword_btn_reset);
    }
}