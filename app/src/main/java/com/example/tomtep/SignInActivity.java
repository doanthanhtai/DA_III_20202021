package com.example.tomtep;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private LinearLayout lnrLayoutResetPassword, lnrLayoutSignUp;
    private TextInputEditText edtEmail, edtPassword;
    private TextInputLayout tilEmail, tilPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        setEvent();
    }

    private void setEvent() {
        btnLogin.setOnClickListener(v -> onClickSigin());
        lnrLayoutResetPassword.setOnClickListener(v -> onClickToResetPasswordActivity());
        lnrLayoutSignUp.setOnClickListener(v -> onClickToSignUpActivity());
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tilEmail.setError(SignInActivity.this.getString(R.string.all_error_emailempty));
                } else if (!isEmailValid(charSequence.toString())) {
                    tilEmail.setError(SignInActivity.this.getString(R.string.all_error_emailinvalid));
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
                    tilPassword.setError(SignInActivity.this.getString(R.string.all_error_passwordempty));
                } else if (charSequence.length() < 6) {
                    tilPassword.setError(SignInActivity.this.getString(R.string.all_error_passwordinvalid));
                } else {
                    tilPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void onClickSigin() {
        String strEmail = String.valueOf(edtEmail.getText()).trim();
        String strPassword = String.valueOf(edtPassword.getText()).trim();
        if (tilEmail.getError() == null && tilPassword.getError() == null && !strEmail.isEmpty() && !strPassword.isEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finishAffinity();
                }
            });
        } else {
            Toast.makeText(this, getResources().getText(R.string.all_toast_invaled), Toast.LENGTH_SHORT).show();
        }
        Log.e("Tai","start in");
    }

    private void onClickToSignUpActivity() {
        startActivity(new Intent(SignInActivity.this, SignUpAcitivity.class));
    }

    private void onClickToResetPasswordActivity() {
        startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
    }

    private void initView() {
        tilEmail = findViewById(R.id.signin_til_email);
        tilPassword = findViewById(R.id.signin_til_password);
        edtEmail = findViewById(R.id.signin_edt_email);
        edtPassword = findViewById(R.id.signin_edt_password);
        btnLogin = findViewById(R.id.signin_btn_signin);
        lnrLayoutResetPassword = findViewById(R.id.signin_layout_forgetpassword);
        lnrLayoutSignUp = findViewById(R.id.signin_layout_signup);

    }
}