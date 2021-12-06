package com.example.tomtep;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tomtep.model.Account;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpAcitivity extends AppCompatActivity {
    private LinearLayout lnrLayoutSignIn;
    private TextInputEditText edtEmail, edtPassword, edtConfirmPassword;
    private TextInputLayout tilEmail, tilPassword, tilConfirmPassword;
    private Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        setEvent();
    }

    private void setEvent() {
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tilEmail.setError(SignUpAcitivity.this.getString(R.string.all_error_emailempty));
                } else if (!isEmailValid(charSequence.toString())) {
                    tilEmail.setError(SignUpAcitivity.this.getString(R.string.all_error_emailinvalid));
                } else {
                    tilEmail.setError(null);
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tilPassword.setError(SignUpAcitivity.this.getString(R.string.all_error_passwordempty));
                } else if (charSequence.length() < 6) {
                    tilPassword.setError(SignUpAcitivity.this.getString(R.string.all_error_passwordinvalid));
                } else {
                    tilPassword.setError(null);
                    if (charSequence.toString().equals(String.valueOf(edtConfirmPassword.getText()).trim())) {
                        tilConfirmPassword.setError(null);
                    } else {
                        tilConfirmPassword.setError(SignUpAcitivity.this.getString(R.string.all_error_confirmpasswordinvalid));
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
                    tilConfirmPassword.setError(SignUpAcitivity.this.getString(R.string.all_error_confirmpasswordempty));
                } else if (!charSequence.toString().equals(String.valueOf(edtPassword.getText()).trim())) {
                    tilConfirmPassword.setError(SignUpAcitivity.this.getString(R.string.all_error_confirmpasswordinvalid));
                } else {
                    tilConfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSignUp.setOnClickListener(v -> onClickSignUp());
        lnrLayoutSignIn.setOnClickListener(v -> onClickToSignInActivity());
    }

    private void onClickSignUp() {
        String strEmail = String.valueOf(edtEmail.getText()).trim();
        String strPassword = String.valueOf(edtPassword.getText()).trim();
        if (tilEmail.getError() == null && tilPassword.getError() == null && tilConfirmPassword.getError() == null && !strEmail.isEmpty() && !strPassword.isEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Account account = new Account(userId,strEmail, false);
                    FirebaseDatabase.getInstance().getReference("Account").child(account.getId()).setValue(account);
                    Toast.makeText(SignUpAcitivity.this, R.string.signup_toast_successful, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpAcitivity.this, MainActivity.class));
                    finishAffinity();
                } else {
                    alertToResetPassword();
                }
            });
        } else {
            Toast.makeText(this, R.string.all_toast_lackofinformation, Toast.LENGTH_SHORT).show();
        }
    }

    private void alertToResetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.signup_title_dialogcomfirmresetpassword)
                .setMessage(R.string.all_message_confirmresetpassword)
                .setPositiveButton(getResources().getText(R.string.all_button_agree_text), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    startActivity(new Intent(SignUpAcitivity.this, ResetPasswordActivity.class));
                    finishAffinity();
                })
                .setNegativeButton(getResources().getText(R.string.all_button_cancel_text), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void onClickToSignInActivity() {
        Intent intent = new Intent(SignUpAcitivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void initView() {
        tilEmail = findViewById(R.id.signup_til_email);
        edtEmail = findViewById(R.id.signup_edt_email);
        tilPassword = findViewById(R.id.signup_til_password);
        edtPassword = findViewById(R.id.signup_edt_password);
        tilConfirmPassword = findViewById(R.id.signup_til_confirm_password);
        edtConfirmPassword = findViewById(R.id.signup_edt_confirm_password);
        btnSignUp = findViewById(R.id.signup_btn_signup);
        lnrLayoutSignIn = findViewById(R.id.signup_layout_signin);
    }
}