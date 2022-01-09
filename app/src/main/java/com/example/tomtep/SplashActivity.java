package com.example.tomtep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tomtep.service.ConnectionReceive;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        (new Handler(Looper.getMainLooper())).postDelayed(this::nextActivity, 1000);
    }

    private void nextActivity() {

        if (!ConnectionReceive.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.splash_title_notinternet))
                    .setMessage(R.string.splash_message_notinternet)
                    .setNegativeButton(R.string.all_button_agree_text, (dialog, which) -> {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser == null) {
                            startActivity(new Intent(SplashActivity.this, SignUpAcitivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finishAffinity();
                    });
            builder.create().show();
        }else{
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser == null) {
                            startActivity(new Intent(SplashActivity.this, SignUpAcitivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                        finishAffinity();
        }
    }
}