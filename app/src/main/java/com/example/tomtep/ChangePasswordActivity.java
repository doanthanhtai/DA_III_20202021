package com.example.tomtep;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edtPassword, edtConfirmPassword;
    private TextInputLayout tilPassword, tilConfirmPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
        setEvent();
    }

    private void setEvent() {
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tilPassword.setError(ChangePasswordActivity.this.getString(R.string.all_error_passwordempty));
                } else if (charSequence.length() < 6) {
                    tilPassword.setError(ChangePasswordActivity.this.getString(R.string.all_error_passwordinvalid));
                } else {
                    tilPassword.setError(null);
                    if (charSequence.toString().equals(String.valueOf(edtConfirmPassword.getText()).trim())) {
                        tilConfirmPassword.setError(null);
                    } else {
                        tilConfirmPassword.setError(ChangePasswordActivity.this.getString(R.string.all_error_confirmpasswordinvalid));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tilConfirmPassword.setError(ChangePasswordActivity.this.getString(R.string.all_error_confirmpasswordempty));
                } else if (!charSequence.toString().equals(String.valueOf(edtPassword.getText()).trim())) {
                    tilConfirmPassword.setError(ChangePasswordActivity.this.getString(R.string.all_error_confirmpasswordinvalid));
                } else {
                    tilConfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnChangePassword.setOnClickListener(v -> onClickChangePassword());
    }

    private void onClickChangePassword() {
        String strNewPassword = String.valueOf(edtPassword.getText()).trim();
        if (tilPassword.getError() == null && tilConfirmPassword.getError() == null && !strNewPassword.isEmpty()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null){
                return;
            }

            FirebaseAuth.getInstance().getCurrentUser().updatePassword(strNewPassword);
            Toast.makeText(ChangePasswordActivity.this, getResources().getText(R.string.changepassword_toast_successful), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SignInActivity.class));
            finishAffinity();
        } else {
            Toast.makeText(ChangePasswordActivity.this, getResources().getText(R.string.all_toast_invaled), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        tilPassword = findViewById(R.id.changepassword_til_password);
        edtPassword = findViewById(R.id.changepassword_edt_password);
        tilConfirmPassword = findViewById(R.id.changepassword_til_confirm_password);
        edtConfirmPassword = findViewById(R.id.changepassword_edt_confirm_password);
        btnChangePassword = findViewById(R.id.changepassword_btn_change);
    }
}